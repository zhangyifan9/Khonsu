package at.jku.isse.ecco.api.resource;


import at.jku.isse.ecco.adapter.dispatch.DirectoryArtifactData;
import at.jku.isse.ecco.adapter.dispatch.PluginArtifactData;
import at.jku.isse.ecco.adapter.plugin.java.data.*;
import at.jku.isse.ecco.core.Association;
import at.jku.isse.ecco.service.EccoService;
import at.jku.isse.ecco.tree.Node;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ArtifactsResource extends EccoResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArtifactsResource.class);
    private EccoService service;

    public ArtifactsResource(String repoDir) {
        try {
            FileUtils.cleanDirectory(new File(repoDir));
        } catch (IOException e) {
            e.printStackTrace();
        }
        service = new EccoService();
        service.setRepositoryDir(Paths.get(repoDir));
        service.open();
    }

    public void close() {
        service.close();
    }

    public void commitAllVersions(String repoName, String path) {
        Jedis jedis = new Jedis("localhost", 6379);
        String projectPath = path + "\\" + repoName + "\\non-base";
        File projectDir = new File(projectPath);
        File[] allFiles = projectDir.listFiles();

        Arrays.sort(allFiles, Comparator.comparing(File::getName).reversed());
        service.setBaseDir(Paths.get(path + "\\" + repoName + "\\base"));
        service.commit(jedis.hget(repoName, "base") + ".1");
        for (File file : allFiles) {
            service.setBaseDir(Paths.get(projectPath + "\\" + file.getName()));
            service.commit(file.getName() + ".1");
        }
        jedis.close();
    }

    /**
     * 获取分割完的代码块
     * 将 ecco 的 ast 转换成较为可读的 Java 代码
     */
    public void compose(String projectName, String path) throws IOException {
        String projectPath = path + "\\code\\" + projectName;
        String codeBlockPath = path + "\\code_block\\" + projectName;
        String invalidPath = path + "\\invalid\\code\\" + projectName;
        String invalidPath2 = path + "\\invalid\\code_block\\" + projectName;
        File projectFile = new File(projectPath);
        if (projectFile.exists()) {
            FileUtils.deleteDirectory(projectFile);
        }
        projectFile.mkdirs();

        File codeBlockFile = new File(codeBlockPath);
        if (codeBlockFile.exists()) {
            FileUtils.deleteDirectory(codeBlockFile);
        }
        codeBlockFile.mkdirs();

        File invalidFile = new File(invalidPath);
        if (invalidFile.exists()) {
            FileUtils.deleteDirectory(invalidFile);
        }
        invalidFile.mkdirs();

        File invalidFile2 = new File(invalidPath2);
        if (invalidFile2.exists()) {
            FileUtils.deleteDirectory(invalidFile2);
        }
        invalidFile2.mkdirs();

        // 所有关联
        Collection<? extends Association> associations = service.getRepository().getAssociations();
        Node rootNode = null;
        String condition = "";
        // 匹配不合法的代码片段(跳版本) (PS: 跳版本可能是由于部分版本文件不可以解析导致)
        Pattern pattern = Pattern.compile("(?<=\\()[^\\)]+");
        for (Association association : associations) {
            // 获取根节点
            rootNode = association.getRootNode().getChildren().get(0);
            condition = association.computeCondition().getSimpleModuleConditionString();

            Matcher matcher = pattern.matcher(condition);
            List<Integer> arr = new ArrayList<>();
            while (matcher.find()) {
                String version = matcher.group();
                arr.add(Integer.parseInt(version));
            }
            Collections.sort(arr);
            // if (checkInvalid(arr)) {
            //     continue;
            // }

            try {
                handleRootNode(rootNode, arr.stream().map(Objects::toString).collect(Collectors.joining("&")), projectName, path + "\\code", path + "\\code_block", checkInvalid(arr), path + "\\invalid\\code");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public boolean checkInvalid(List<Integer> list) {
        if (list.size() <= 1) return false;

        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) != list.get(i - 1) + 1) return true;
        }

        return false;
    }

    public void handleRootNode(Node node, String condition, String projectName, String path, String codeBlockPath, boolean invalid, String invalidPath) {
        for (Node child : node.getChildren()) {
            if (child.getArtifact().getData() instanceof DirectoryArtifactData) {
                handleRootNode(child, condition, projectName, path, codeBlockPath, invalid, invalidPath);
            } else if (child.getArtifact().getData() instanceof PluginArtifactData) {
                if (child.getChildren().size() > 0) {
                    // 为前端展示加工的代码块
                    StringBuffer codeFile = new StringBuffer();
                    // 为相似度计算加工的代码块
                    StringBuffer codeBlockFile = new StringBuffer();
                    handleJava(child.getChildren().get(0), codeFile, codeBlockFile, condition, projectName, path, codeBlockPath, invalid, invalidPath);
                } else {
                    System.out.println("该Java文件特殊！！！");
                }
            } else {
                System.out.println("有错误发生！");
            }
        }
    }

    // 最外部的class
    public void handleJava(Node node, StringBuffer codeFile, StringBuffer codeBlockFile, String condition, String projectName, String path, String codeBlockPath, boolean invalid, String invalidPath) {
        boolean isPerfect = false;
        // 判断是否是完整的类，目前只考虑类（接口、注解类、枚举类、普通类）、方法
        if (node.getChildren().size() > 0 && node.getChildren().get(0).getArtifact().getData() instanceof IntegrityArtifactData) isPerfect = true;

        // 属于哪个包
        String packageDescription = "package " + node.getNodeString().substring(0, node.getNodeString().lastIndexOf(".")) + ";\r\n\r\n";
        codeFile.append(packageDescription);
        if (isPerfect) codeBlockFile.append(packageDescription);

        // 处理import、comment、annotation
        // 不完整的类只需要类名
        int i = 0;
        for (Node child : node.getChildren()) {
            if (child.getArtifact().getData() instanceof ImportArtifactData) {
                i++;
                codeFile.append(child.getNodeString() + "\r\n");
                if (isPerfect) codeBlockFile.append(child.getNodeString() + "\r\n");
            }
        }
        if (i > 0) {
            codeFile.append("\r\n");
            if (isPerfect) codeBlockFile.append("\r\n");
        }
        handleClass(node, codeFile, codeBlockFile, isPerfect, 0);
        String className = ((PluginArtifactData) node.getParent().getArtifact().getData()).getPath().toString().replace("\\", "&");

        try {
            writeLocal(projectName, condition, className, codeFile.toString(), path, invalid, invalidPath);
            writeLocal(projectName, condition, className, codeBlockFile.toString(), codeBlockPath, invalid, invalidPath);
            System.out.println(condition + "---" + className);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeLocal(String projectName, String condition, String className, String codeFile, String path, boolean invalid, String invalidPath) {
        if (path.endsWith("code_block")) invalidPath = invalidPath + "_block";
        File file = !invalid ? new File(path + "\\" + projectName + "\\" + condition) : new File(invalidPath + "\\" + projectName + "\\" + condition);
        String fileName = !invalid ? path + "\\" + projectName + "\\" + condition + "\\" + className + ".txt" : invalidPath + "\\" + projectName + "\\" + condition + "\\" + className + ".txt";
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(codeFile);
            writer.close();
            System.out.println("----------write----------");
            System.out.println(codeFile.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 为了处理内部类，将这部分逻辑抽出作为独立的函数
    public void handleClass(Node node, StringBuffer codeFile, StringBuffer codeBlockFile, boolean isPerfect, int tabNum) {
        preHandleDocAnno(node, codeFile, codeBlockFile, isPerfect, tabNum);
        // 处理class
        if (node.getArtifact().getData() instanceof ClassArtifactData) {
            codeFile.append(computeTab(tabNum) + ((ClassArtifactData) node.getArtifact().getData()).getFullName() + " {\r\n");
            if (isPerfect) {
                codeBlockFile.append(computeTab(tabNum) + ((ClassArtifactData) node.getArtifact().getData()).getFullName() + " {\r\n");
            } else {
                String[] classNames = ((ClassArtifactData) node.getArtifact().getData()).getName().split("\\.");
                codeBlockFile.append(computeTab(tabNum) + "class " + classNames[classNames.length-1] + " {\r\n");
            }
        } else if (node.getArtifact().getData() instanceof EnumArtifactData) {
            codeFile.append(computeTab(tabNum) + ((EnumArtifactData) node.getArtifact().getData()).getFullName() + " {\r\n");
            if (isPerfect) {
                codeBlockFile.append(computeTab(tabNum) + ((EnumArtifactData) node.getArtifact().getData()).getFullName() + " {\r\n");
            } else {
                String[] enumNames = ((EnumArtifactData) node.getArtifact().getData()).getName().split("\\.");
                codeBlockFile.append(computeTab(tabNum) + "enum " + enumNames[enumNames.length-1] + " {\r\n");
            }
        } else if (node.getArtifact().getData() instanceof AnnotationDeclarationArtifactData) {
            codeFile.append(computeTab(tabNum) + ((AnnotationDeclarationArtifactData) node.getArtifact().getData()).getFullName() + " {\r\n");
            if (isPerfect) {
                codeBlockFile.append(computeTab(tabNum) + ((AnnotationDeclarationArtifactData) node.getArtifact().getData()).getFullName() + " {\r\n");
            } else {
                String[] annotationNames = ((AnnotationDeclarationArtifactData) node.getArtifact().getData()).getName().split("\\.");
                codeBlockFile.append(computeTab(tabNum) + "@interface " + annotationNames[annotationNames.length-1] + " {\r\n");
            }
        } else {
            System.out.println("它不是类、接口、注解类、enum类，无法处理！");
        }

        if (node.getArtifact().getData() instanceof EnumArtifactData) {
            List<Node> list = new LinkedList<>();
            for (Node child : node.getChildren()) {
                if (child.getArtifact().getData() instanceof EnumConstantArtifactData)
                    list.add(child);
            }
            handleEnumConstantList(list, codeFile, codeBlockFile, tabNum + 1);
        }

        // 处理属性和方法
        for (Node child : node.getChildren()) {
            if (child.getArtifact().getData() instanceof FieldArtifactData) {
                codeFile.append(computeTab(tabNum + 1) + "\r\n");
                codeBlockFile.append(computeTab(tabNum + 1) + "\r\n");
                handleField(child, codeFile, codeBlockFile, isPerfect, tabNum + 1);
            } else if (child.getArtifact().getData() instanceof MethodArtifactData) {
                codeFile.append(computeTab(tabNum + 1) + "\r\n");
                codeBlockFile.append(computeTab(tabNum + 1) + "\r\n");
                boolean methodIsPerfect = false;
                if (child.getChildren().size() > 0 && child.getChildren().get(0).getArtifact().getData() instanceof IntegrityArtifactData) methodIsPerfect = true;
                handleMethod(child, codeFile, codeBlockFile, methodIsPerfect, tabNum + 1);
            } else if (child.getArtifact().getData() instanceof ClassArtifactData
                    || child.getArtifact().getData() instanceof EnumArtifactData) {
                codeFile.append(computeTab(tabNum + 1) + "\r\n");
                codeBlockFile.append(computeTab(tabNum + 1) + "\r\n");
                handleClass(child, codeFile, codeBlockFile, isPerfect, tabNum + 1);
            } else if (!(child.getArtifact().getData() instanceof ImportArtifactData)
                    && !(child.getArtifact().getData() instanceof DocArtifactData)
                    && !(child.getArtifact().getData() instanceof AnnotationArtifactData)
                    && !(child.getArtifact().getData() instanceof EnumConstantArtifactData)
                    && !(child.getArtifact().getData() instanceof IntegrityArtifactData)) {
                System.out.println(child.getNodeString() + "在handleJava()中无法处理");
            }
        }
        codeFile.append(computeTab(tabNum) + "}\r\n");
        codeBlockFile.append(computeTab(tabNum) + "}\r\n");
    }

    public void handleEnumConstantList(List<Node> list, StringBuffer codeFile, StringBuffer codeBlockFile, int tabNum) {
        for (int i = 0; i < list.size(); i++) {
            handleEnumConstant(list.get(i), codeFile, codeBlockFile, true, tabNum);
            if (i == list.size() - 1) {
                codeFile.append(";" + "\r\n");
                codeBlockFile.append(";" + "\r\n");
            } else {
                codeFile.append("," + "\r\n");
                codeBlockFile.append("," + "\r\n");
            }
        }
    }

    public void handleEnumConstant(Node node, StringBuffer codeFile, StringBuffer codeBlockFile, boolean isPerfect, int tabNum) {
        preHandleDocAnno(node, codeFile, codeBlockFile, isPerfect, tabNum);
        List<Node> list = new LinkedList<>();
        for (Node child : node.getChildren()) {
            if (child.getArtifact().getData() instanceof MethodArtifactData) {
                list.add(child);
            }
        }
        // 处理class名
        if (list.size() > 0) {
            codeFile.append(computeTab(tabNum) + ((EnumConstantArtifactData) node.getArtifact().getData()).getFullName() + " {\r\n");
            codeBlockFile.append(computeTab(tabNum) + ((EnumConstantArtifactData) node.getArtifact().getData()).getFullName() + " {\r\n");
        } else {
            codeFile.append(computeTab(tabNum) + ((EnumConstantArtifactData) node.getArtifact().getData()).getFullName());
            codeBlockFile.append(computeTab(tabNum) + ((EnumConstantArtifactData) node.getArtifact().getData()).getFullName() + " {\r\n");
        }
        // 处理枚举常量中的方法
        for (Node child : list) {
            handleMethod(child, codeFile, codeBlockFile, isPerfect, tabNum + 1);
        }
        if (list.size() > 0)
            codeFile.append(computeTab(tabNum) + "}");
    }

    public void handleField(Node node, StringBuffer codeFile, StringBuffer codeBlockFile, boolean isPerfect, int tabNum) {
        preHandleDocAnno(node, codeFile, codeBlockFile, isPerfect, tabNum);
        codeFile.append(multipleLines(node, tabNum).toString());
        codeBlockFile.append(multipleLines(node, tabNum).toString());
    }

    public StringBuffer multipleLines(Node node, int tabNum) {
        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(node.getNodeString().getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
        StringBuffer stringBuffer = new StringBuffer();
        String line = "";
        try {
            while ((line = br.readLine()) != null) {
                line = computeTab(tabNum) + line;
                stringBuffer.append(line + "\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    public void handleMethod(Node node, StringBuffer codeFile, StringBuffer codeBlockFile, boolean isPerfect, int tabNum) {
        preHandleDocAnno(node, codeFile, codeBlockFile, isPerfect, tabNum);
        codeFile.append(computeTab(tabNum) + ((MethodArtifactData) node.getArtifact().getData()).getFullName() + " {\r\n");
        if (isPerfect) {
            codeBlockFile.append(computeTab(tabNum) + ((MethodArtifactData) node.getArtifact().getData()).getFullName() + " {\r\n");
        } else {
            codeBlockFile.append(computeTab(tabNum) + ((MethodArtifactData) node.getArtifact().getData()).getSignature() + " {\r\n");
        }
        for (Node child : node.getChildren()) {
            if (!(child.getArtifact().getData() instanceof ImportArtifactData) && !(child.getArtifact().getData() instanceof DocArtifactData) && !(child.getArtifact().getData() instanceof AnnotationArtifactData)) {
                handleBlock(child, codeFile, tabNum + 1);
                handleBlock(child, codeBlockFile, tabNum + 1);
            }
        }
        codeFile.append(computeTab(tabNum) + "}\r\n");
        codeBlockFile.append(computeTab(tabNum) + "}\r\n");
    }

    public void preHandleDocAnno(Node node, StringBuffer codeFile, StringBuffer codeBlockFile, boolean isPerfect, int tabNum) {
        for (Node child : node.getChildren()) {
            if (child.getArtifact().getData() instanceof DocArtifactData) {
                codeFile.append(multipleLines(child, tabNum));
                if (isPerfect) codeBlockFile.append(multipleLines(child, tabNum));
            } else if (child.getArtifact().getData() instanceof AnnotationArtifactData) {
                codeFile.append(computeTab(tabNum) + child.getNodeString() + "\r\n");
                codeBlockFile.append(computeTab(tabNum) + child.getNodeString() + "\r\n");
            }
        }
    }

    public void handleBlock(Node node, StringBuffer codeFile, int tabNum) {

        if (node.getArtifact().getData() instanceof BlockArtifactData) {
            // do-while
            if (node.getArtifact().getData() instanceof DoStmtArtifactData) {
                codeFile.append(computeTab(tabNum) + "do {\r\n");
                for (Node child : node.getChildren()) {
                    handleBlock(child, codeFile, tabNum + 1);
                }
                codeFile.append(computeTab(tabNum) + "} while(" + ((DoStmtArtifactData) node.getArtifact().getData()).getCondition() + ");\r\n");
                // labeled
            } else if (node.getArtifact().getData() instanceof LabeledStmtArtifactData) {
                codeFile.append(computeTab(tabNum) + node.getNodeString() + "\r\n");
                for (Node child : node.getChildren()) {
                    handleBlock(child, codeFile, tabNum);
                }
            } else {
                // foreach、for
                if (node.getNodeString().startsWith("for (")) {
                    codeFile.append(computeTab(tabNum) + node.getNodeString() + " {\r\n");
                    for (Node child : node.getChildren()) {
                        handleBlock(child, codeFile, tabNum + 1);
                    }
                    codeFile.append(computeTab(tabNum) + "}\r\n");
                    // while
                } else if (node.getNodeString().startsWith("while (")) {
                    codeFile.append(computeTab(tabNum) + node.getNodeString() + " {\r\n");
                    for (Node child : node.getChildren()) {
                        handleBlock(child, codeFile, tabNum + 1);
                    }
                    codeFile.append(computeTab(tabNum) + "}\r\n");
                    // synchronized
                } else if (node.getNodeString().startsWith("synchronized (")) {
                    codeFile.append(computeTab(tabNum) + node.getNodeString() + " {\r\n");
                    for (Node child : node.getChildren()) {
                        handleBlock(child, codeFile, tabNum + 1);
                    }
                    codeFile.append(computeTab(tabNum) + "}\r\n");
                    // if-else
                } else if (node.getNodeString().startsWith("if (")) {
                    codeFile.append(computeTab(tabNum) + node.getNodeString() + " {\r\n");
                    for (Node child : node.getChildren()) {
                        if (!child.getNodeString().equals("else")) {
                            handleBlock(child, codeFile, tabNum + 1);
                        } else {
                            codeFile.append(computeTab(tabNum) + "} else {\r\n");
                            for (Node elseChild : child.getChildren()) {
                                handleBlock(elseChild, codeFile, tabNum + 1);
                            }
                        }
                    }
                    codeFile.append(computeTab(tabNum) + "}\r\n");
                    // try-catch-finally
                } else if (node.getNodeString().equals("try")) {
                    codeFile.append(computeTab(tabNum) + node.getNodeString() + " {\r\n");
                    for (Node child : node.getChildren()) {
                        if (child.getNodeString().startsWith("catch (")) {
                            codeFile.append(computeTab(tabNum) + "} " + child.getNodeString() + " {\r\n");
                            for (Node catchChild : child.getChildren()) {
                                handleBlock(catchChild, codeFile, tabNum + 1);
                            }
                        } else if (child.getNodeString().equals("finally")) {
                            codeFile.append(computeTab(tabNum) + "} " + child.getNodeString() + " {\r\n");
                            for (Node finallyChild : child.getChildren()) {
                                handleBlock(finallyChild, codeFile, tabNum + 1);
                            }
                        } else {
                            handleBlock(child, codeFile, tabNum + 1);
                        }
                    }
                    codeFile.append(computeTab(tabNum) + "}\r\n");
                    // switch-case-default
                } else if (node.getNodeString().startsWith("switch (")) {
                    codeFile.append(computeTab(tabNum) + node.getNodeString() + " {\r\n");
                    for (Node child : node.getChildren()) {
                        codeFile.append(computeTab(tabNum) + child.getNodeString() + "\r\n");
                        for (Node caseChild : child.getChildren()) {
                            codeFile.append(computeTab(tabNum + 1) + caseChild.getNodeString() + "\r\n");
                        }
                    }
                    codeFile.append(computeTab(tabNum) + "}\r\n");
                }
            }
        } else if (node.getArtifact().getData() instanceof LineArtifactData) {
            codeFile.append(multipleLines(node, tabNum).toString());
        } else if (!(node.getArtifact().getData() instanceof IntegrityArtifactData)){
            System.out.println(node.getNodeString() + "在handleBlock()中无法处理。");
        }
    }

    public String computeTab(int tabNum) {
        String tabNeed = "";
        for (int i = 0; i < tabNum; i++) {
            tabNeed += "\t";
        }
        return tabNeed;
    }
}
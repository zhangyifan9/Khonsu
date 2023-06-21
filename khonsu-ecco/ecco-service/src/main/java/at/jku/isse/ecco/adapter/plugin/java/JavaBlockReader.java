package at.jku.isse.ecco.adapter.plugin.java;

import at.jku.isse.ecco.EccoException;
import at.jku.isse.ecco.adapter.ArtifactReader;
import at.jku.isse.ecco.adapter.dispatch.PluginArtifactData;
import at.jku.isse.ecco.adapter.plugin.java.data.*;
import at.jku.isse.ecco.artifact.Artifact;
import at.jku.isse.ecco.dao.EntityFactory;
import at.jku.isse.ecco.service.listener.ReadListener;
import at.jku.isse.ecco.tree.Node;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class JavaBlockReader implements ArtifactReader<Path, Set<Node.Op>> {

    private final EntityFactory entityFactory;
    private static final Logger LOGGER = LoggerFactory.getLogger(JavaBlockReader.class);

    private static final String logPath = "D:\\Workplace\\Khonsu\\khonsu-ecco\\ecco-api\\logs\\java_block_reader.txt";


    @Inject
    public JavaBlockReader(EntityFactory entityFactory) {
        checkNotNull(entityFactory);

        this.entityFactory = entityFactory;
    }

    @Override
    public String getPluginId() {
        return JavaPlugin.class.getName();
    }

    private static Map<Integer, String[]> prioritizedPatterns;

    static {
        prioritizedPatterns = new HashMap<>();
        prioritizedPatterns.put(Integer.MAX_VALUE, new String[]{"**.java"});
    }

    @Override
    public Map<Integer, String[]> getPrioritizedPatterns() {
        return Collections.unmodifiableMap(prioritizedPatterns);
    }

    @Override
    public Set<Node.Op> read(Path[] input) {
        return this.read(Paths.get("."), input);
    }

    @Override
    public Set<Node.Op> read(Path base, Path[] input) {
        Set<Node.Op> nodes = new HashSet<>();

        JavaParser javaParser = new JavaParser();
        for (Path path : input) {
            Path resolvedPath = base.resolve(path);


            try {
                // 解析Java代码文件
                System.out.println(resolvedPath);
                CompilationUnit cu = javaParser.parse(resolvedPath).getResult().get();

                Artifact.Op<PluginArtifactData> pluginArtifact = this.entityFactory.createArtifact(new PluginArtifactData(this.getPluginId(), path));
                Node.Op pluginNode = this.entityFactory.createNode(pluginArtifact);
                nodes.add(pluginNode);

                // FIXME:class√, interface√, enum√, annotation√
                for (TypeDeclaration<?> typeDeclaration : cu.getTypes()) {
                    if (typeDeclaration instanceof ClassOrInterfaceDeclaration)
                        handleClassOrInterface(cu, typeDeclaration, pluginNode, true);
                    else if (typeDeclaration instanceof EnumDeclaration)
                        handleEnum(cu, typeDeclaration, pluginNode, true);
                    else if (typeDeclaration instanceof AnnotationDeclaration)
                        handleAnnotation(cu, typeDeclaration, pluginNode);
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                throw new EccoException("Error parsing java file.", e);
            } catch (ParseProblemException e) {
                e.printStackTrace();
                System.out.println();
                // e.printStackTrace();
                // try {
                //     FileWriter writer = new FileWriter(logPath, true);
                //     writer.write(resolvedPath + "存在编译问题\n");
                //     writer.close();
                // } catch (IOException ex) {
                //     e.printStackTrace();
                // }
            }
        }
        return nodes;
    }

    // 处理自定义注解(注解没有继承或实现)
    public void handleAnnotation(CompilationUnit cu, TypeDeclaration<?> typeDeclaration, Node.Op pluginNode) {
        // 包名
        String packageName = "";
        if (cu.getPackageDeclaration().isPresent())
            packageName = cu.getPackageDeclaration().get().getName().toString();

        // 注解名
        String className = typeDeclaration.getName().toString();

        // 对修饰符的支持
        String classModifiers = "";
        if (!typeDeclaration.getModifiers().isEmpty()) {
            classModifiers = typeDeclaration.getModifiers().toString().trim().replace(",", " ").substring(1, typeDeclaration.getModifiers().toString().length() - 1).toLowerCase().replaceAll("\\s+", " ");
        }

        String fullClassName = "@interface " + className;
        if (!classModifiers.equals("")) {
            fullClassName = classModifiers + " " + fullClassName;
        }
        fullClassName = fullClassName.replaceAll("\\s+", " ");
        Artifact.Op<AnnotationDeclarationArtifactData> annotationDeclarationArtifact = this.entityFactory.createArtifact(new AnnotationDeclarationArtifactData(packageName + "." + className, fullClassName));
        Node.Op annotationNode = this.entityFactory.createNode(annotationDeclarationArtifact);
        pluginNode.addChild(annotationNode);

        // 这是一个完整的类
        Artifact.Op<IntegrityArtifactData> integrityArtifact = this.entityFactory.createArtifact(new IntegrityArtifactData(true));
        Node.Op integrityNode = this.entityFactory.createNode(integrityArtifact);
        annotationNode.addChild(integrityNode);


        // add child from imports
        for (ImportDeclaration importDeclaration : cu.getImports()) {
            String importName = "import " + importDeclaration.getName().asString() + ";";
            Artifact.Op<ImportArtifactData> importsArtifact = this.entityFactory.createArtifact(new ImportArtifactData(importName));
            Node.Op importNode = this.entityFactory.createNode(importsArtifact);
            annotationNode.addChild(importNode);
        }

        // add child form Comments
        if (typeDeclaration.getComment().isPresent()) {
            addDocChild(typeDeclaration.getComment().get().toString(), annotationNode);
        }

        // add classChild from annotations
        if (typeDeclaration.getAnnotations().isNonEmpty()) {
            addAnnotationChild(typeDeclaration, annotationNode);
        }

        for (BodyDeclaration<?> node : typeDeclaration.getMembers()) {
            String member = node.getTokenRange().get().toString();

            Artifact.Op<FieldArtifactData> fieldArtifact = this.entityFactory.createArtifact(new FieldArtifactData(member));
            Node.Op fieldNode = this.entityFactory.createOrderedNode(fieldArtifact);
            annotationNode.addChild(fieldNode);

            if (node.getComment().isPresent()) {
                addDocChild(node.getComment().get().toString(), fieldNode);
            }

        }
    }

    // 处理枚举类(枚举有实现)
    public void handleEnum(CompilationUnit cu, TypeDeclaration<?> typeDeclaration, Node.Op pluginNode, Boolean isOuterClass) {
        // 包名
        String packageName = "";
        if (cu.getPackageDeclaration().isPresent())
            packageName = cu.getPackageDeclaration().get().getName().toString();

        // 枚举名
        String className = typeDeclaration.getName().toString();
        // 对修饰符、以及实现关系的支持
        String classModifiers = "";
        if (!typeDeclaration.getModifiers().isEmpty()) {
            classModifiers = typeDeclaration.getModifiers().toString().replace(",", " ").substring(1, typeDeclaration.getModifiers().toString().length() - 1).toLowerCase().replaceAll("\\s+", " ");
        }
        String classImplementTypes = " implements ";
        for (int i = 0; i < ((EnumDeclaration) typeDeclaration).getImplementedTypes().size(); ++i) {
            classImplementTypes += ((EnumDeclaration) typeDeclaration).getImplementedTypes().get(i).toString();
            if (i != ((EnumDeclaration) typeDeclaration).getImplementedTypes().size() - 1)
                classImplementTypes += " ";
        }

        String fullClassName = "enum " + className;
        if (!classModifiers.equals("")) {
            fullClassName = classModifiers + " " + fullClassName;
        }
        if (!classImplementTypes.equals(" implements ")) {
            fullClassName += classImplementTypes;
        }
        fullClassName = fullClassName.replaceAll("\\s+", " ");
        Artifact.Op<EnumArtifactData> enumArtifact;
        // 外部类需要加包名
        if (isOuterClass) {
            enumArtifact = this.entityFactory.createArtifact(new EnumArtifactData(packageName + "." + className, fullClassName));
        } else {
            enumArtifact = this.entityFactory.createArtifact(new EnumArtifactData(className, fullClassName));
        }
        Node.Op enumNode = this.entityFactory.createNode(enumArtifact);
        pluginNode.addChild(enumNode);
        //外部类有引用
        if (isOuterClass) {
            // add child from imports
            for (ImportDeclaration importDeclaration : cu.getImports()) {
                String importName = "import " + importDeclaration.getName().asString() + ";";
                Artifact.Op<ImportArtifactData> importsArtifact = this.entityFactory.createArtifact(new ImportArtifactData(importName));
                Node.Op importNode = this.entityFactory.createNode(importsArtifact);
                enumNode.addChild(importNode);
            }
        }

        // add child form Comments
        if (typeDeclaration.getComment().isPresent()) {
            addDocChild(typeDeclaration.getComment().get().toString(), enumNode);
        }

        // add classChild from annotations
        if (typeDeclaration.getAnnotations().isNonEmpty()) {
            addAnnotationChild(typeDeclaration, enumNode);
        }

        // add enumConstantChild from entries
        for (EnumConstantDeclaration node : ((EnumDeclaration) typeDeclaration).getEntries()) {
            String enumConstantName = node.getName().toString();
            String enumConstantArguments = "";
            if (node.getArguments().isNonEmpty()) {
                enumConstantArguments = node.getArguments().toString().substring(1, node.getArguments().toString().length() - 1);
            }
            String fullName = "";
            if (!enumConstantArguments.equals("")) {
                fullName = enumConstantName + "(" + enumConstantArguments + ")";
            } else {
                fullName = enumConstantName;
            }
            fullName = fullName.replaceAll("\\s+", " ");
            Artifact.Op<EnumConstantArtifactData> enumConstantArtifact = this.entityFactory.createArtifact(new EnumConstantArtifactData(enumConstantName, fullName));
            Node.Op enumConstantNode = this.entityFactory.createNode(enumConstantArtifact);
            enumNode.addChild(enumConstantNode);

            // 这是一个完整的类
            Artifact.Op<IntegrityArtifactData> integrityArtifact = this.entityFactory.createArtifact(new IntegrityArtifactData(true));
            Node.Op integrityNode = this.entityFactory.createNode(integrityArtifact);
            enumNode.addChild(integrityNode);

            // add classChild form Comments
            if (node.getComment().isPresent()) {
                addDocChild(node.getComment().get().toString(), enumConstantNode);
            }

            // add classChild from annotations
            if (node.getAnnotations().isNonEmpty()) {
                addAnnotationChild(node, enumConstantNode);
            }

            for (BodyDeclaration<?> childNode : node.getClassBody()) {
                if (childNode instanceof MethodDeclaration) {
                    String methodSignature = ((MethodDeclaration) childNode).getSignature().toString();
                    // 对修饰符、类型、参数支持
                    String methodName = ((MethodDeclaration) childNode).getName().toString();
                    String methodModifiers = "";
                    if (!((MethodDeclaration) childNode).getModifiers().isEmpty()) {
                        methodModifiers = ((MethodDeclaration) childNode).getModifiers().toString().trim().replace(",", " ").substring(1, ((MethodDeclaration) childNode).getModifiers().toString().length() - 1).toLowerCase().replaceAll("\\s+", " ");
                    }
                    String methodType = ((MethodDeclaration) childNode).getType().toString();
                    String methodParameters = "";
                    if (((MethodDeclaration) childNode).getParameters().isNonEmpty()) {
                        methodParameters = ((MethodDeclaration) childNode).getParameters().toString().substring(1, ((MethodDeclaration) childNode).getParameters().toString().length() - 1);
                    }
                    String method = methodType + " " + methodName + "(" + methodParameters + ")";
                    if (!methodModifiers.equals("")) {
                        method = methodModifiers + " " + method;
                    }
                    Artifact.Op<MethodArtifactData> methodArtifact = this.entityFactory.createArtifact(new MethodArtifactData(methodSignature, method));
                    Node.Op methodNode = this.entityFactory.createOrderedNode(methodArtifact);
                    enumConstantNode.addChild(methodNode);
                    // add Comments to method
                    if (childNode.getComment().isPresent()) {
                        addDocChild(childNode.getComment().get().toString(), methodNode);
                    }
                    //add annotations to method
                    if (childNode.getAnnotations().isNonEmpty()) {
                        addAnnotationChild(childNode, methodNode);
                    }
                    if (((MethodDeclaration) childNode).getBody().isPresent()) {
                        for (com.github.javaparser.ast.Node n : ((MethodDeclaration) childNode).getBody().get().getChildNodes()) {
                            if (n instanceof FieldDeclaration) {
                                addFieldChild(n, methodNode);
                            } else {
                                addMethodChild(n, methodNode);
                            }
                        }
                    }
                } else {
                    System.out.println("枚举实例里不是方法");
                }
            }
        }

        // add classChild from Non-Methods
        for (BodyDeclaration<?> node : typeDeclaration.getMembers()) {
            handleNonMethod(node, enumNode);
        }

        // add classChild from Methods
        handleMethod(typeDeclaration, enumNode);
    }

    // FIXME:内部类已考虑(枚举类也是内部类)
    // 接口有的,类都有,将接口作为类处理,仅在声明关键词处有差别
    public void handleClassOrInterface(CompilationUnit cu, TypeDeclaration<?> typeDeclaration, Node.Op pluginNode, Boolean isOuterClass) {
        // 包名
        String packageName = "";
        if (cu.getPackageDeclaration().isPresent())
            packageName = cu.getPackageDeclaration().get().getName().toString();

        // 类名或接口名
        String className = typeDeclaration.getName().toString();
        // 对修饰符、以及继承实现关系的支持,判断是否为接口
        String classModifiers = "";
        if (!typeDeclaration.getModifiers().isEmpty()) {
            classModifiers = typeDeclaration.getModifiers().toString().replace(",", " ").substring(1, typeDeclaration.getModifiers().toString().length() - 1).toLowerCase().replaceAll("\\s+", " ");
        }
        boolean isInterface = ((ClassOrInterfaceDeclaration) typeDeclaration).isInterface();
        String classExtendTypes = " extends ";
        for (int i = 0; i < ((ClassOrInterfaceDeclaration) typeDeclaration).getExtendedTypes().size(); ++i) {
            classExtendTypes += ((ClassOrInterfaceDeclaration) typeDeclaration).getExtendedTypes().get(i).toString();
            if (i != ((ClassOrInterfaceDeclaration) typeDeclaration).getExtendedTypes().size() - 1)
                classExtendTypes += ", ";
        }
        String classImplementTypes = " implements ";
        for (int i = 0; i < ((ClassOrInterfaceDeclaration) typeDeclaration).getImplementedTypes().size(); ++i) {
            classImplementTypes += ((ClassOrInterfaceDeclaration) typeDeclaration).getImplementedTypes().get(i).toString();
            if (i != ((ClassOrInterfaceDeclaration) typeDeclaration).getImplementedTypes().size() - 1)
                classImplementTypes += ", ";
        }
        // 泛型声明支持
        String typeParam = "";
        for (int i = 0; i < ((ClassOrInterfaceDeclaration) typeDeclaration).getTypeParameters().size(); ++i) {
            typeParam += ((ClassOrInterfaceDeclaration) typeDeclaration).getTypeParameters().get(i).toString();
            if (i != ((ClassOrInterfaceDeclaration) typeDeclaration).getTypeParameters().size() - 1)
                typeParam += ", ";
        }
        String fullClassName = "";
        // 区分接口和类的声明关键词
        if (isInterface) {
            fullClassName = "interface " + className;
        } else {
            fullClassName = "class " + className;
        }
        if (!typeParam.equals("")) {
            fullClassName = fullClassName + "<" + typeParam + ">";
        }
        if (!classModifiers.equals("")) {
            fullClassName = classModifiers + " " + fullClassName;
        }
        if (!classExtendTypes.equals(" extends ")) {
            fullClassName += classExtendTypes;
        }
        if (!classImplementTypes.equals(" implements ")) {
            fullClassName += classImplementTypes;
        }
        fullClassName = fullClassName.replaceAll("\\s+", " ");
        Artifact.Op<ClassArtifactData> classArtifact;
        if (isOuterClass) {
            classArtifact = this.entityFactory.createArtifact(new ClassArtifactData(packageName + "." + className, fullClassName));
        } else {
            classArtifact = this.entityFactory.createArtifact(new ClassArtifactData(className, fullClassName));
        }
        Node.Op classNode = this.entityFactory.createNode(classArtifact);
        pluginNode.addChild(classNode);

        // 这是一个完整的类
        Artifact.Op<IntegrityArtifactData> integrityArtifact = this.entityFactory.createArtifact(new IntegrityArtifactData(true));
        Node.Op integrityNode = this.entityFactory.createNode(integrityArtifact);
        classNode.addChild(integrityNode);

        if (isOuterClass) {
            // add classChild from imports
            for (ImportDeclaration importDeclaration : cu.getImports()) {
                String importName = "import " + importDeclaration.getName().asString() + ";";
                Artifact.Op<ImportArtifactData> importsArtifact = this.entityFactory.createArtifact(new ImportArtifactData(importName));
                Node.Op importNode = this.entityFactory.createNode(importsArtifact);
                classNode.addChild(importNode);
            }
        }

        // add classChild form Comments
        if (typeDeclaration.getComment().isPresent()) {
            addDocChild(typeDeclaration.getComment().get().toString(), classNode);
        }

        // add classChild from annotations
        if (typeDeclaration.getAnnotations().isNonEmpty()) {
            addAnnotationChild(typeDeclaration, classNode);
        }

        // add classChild from enums, inner class, fields, constructorMethod
        for (BodyDeclaration<?> node : typeDeclaration.getMembers()) {
            // add classChild from enums
            if (node instanceof EnumDeclaration) {
                handleEnum(cu, (EnumDeclaration) node, classNode, false);
                // add classChild from inner class
            } else if (node instanceof ClassOrInterfaceDeclaration) {
                handleClassOrInterface(cu, (ClassOrInterfaceDeclaration) node, classNode, false);
            } else {
                // add classChild from Non-Methods
                handleNonMethod(node, classNode);
            }
        }

        // add classChild from Methods
        handleMethod(typeDeclaration, classNode);
    }

    private void handleMethod(TypeDeclaration<?> typeDeclaration, Node.Op classNode) {
        for (MethodDeclaration methodDeclaration : typeDeclaration.getMethods()) {
            String methodSignature = methodDeclaration.getSignature().toString();
            // 对修饰符、类型、参数支持
            String methodName = methodDeclaration.getName().toString();
            String methodModifiers = "";
            if (!methodDeclaration.getModifiers().isEmpty()) {
                methodModifiers = methodDeclaration.getModifiers().toString().replace(",", " ").substring(1, methodDeclaration.getModifiers().toString().length() - 1).toLowerCase().replaceAll("\\s+", " ");
            }
            String methodType = methodDeclaration.getType().toString();
            String methodParameters = "";
            if (methodDeclaration.getParameters().isNonEmpty()) {
                methodParameters = methodDeclaration.getParameters().toString().substring(1, methodDeclaration.getParameters().toString().length() - 1);
            }
            String method = methodType + " " + methodName + "(" + methodParameters + ")";
            if (!methodModifiers.equals("")) {
                method = methodModifiers + " " + method;
            }
            method = method.replaceAll("\\s+", " ");
            Artifact.Op<MethodArtifactData> methodArtifact = this.entityFactory.createArtifact(new MethodArtifactData(methodSignature, method));
            Node.Op methodNode = this.entityFactory.createOrderedNode(methodArtifact);
            classNode.addChild(methodNode);

            // 这是一个完整的方法
            Artifact.Op<IntegrityArtifactData> integrityArtifact = this.entityFactory.createArtifact(new IntegrityArtifactData(true));
            Node.Op integrityNode = this.entityFactory.createNode(integrityArtifact);
            methodNode.addChild(integrityNode);

            // add Comments to method
            if (methodDeclaration.getComment().isPresent()) {
                addDocChild(methodDeclaration.getComment().get().toString(), methodNode);
            }
            //add annotations to method
            if (methodDeclaration.getAnnotations().isNonEmpty()) {
                addAnnotationChild(methodDeclaration, methodNode);
            }
            if (methodDeclaration.getBody().isPresent()) {
                for (com.github.javaparser.ast.Node node : methodDeclaration.getBody().get().getChildNodes()) {
                    if (node instanceof FieldDeclaration) {
                        addFieldChild(node, methodNode);
                    } else {
                        addMethodChild(node, methodNode);
                    }
                }
            }
        }
    }

    public void handleNonMethod(BodyDeclaration<?> node, Node.Op classNode) {
        // add classChild from fields
        if (node instanceof FieldDeclaration) {
            addFieldChild(node, classNode);
        } else {
            // add classChild from constructorMethod
            if (node instanceof ConstructorDeclaration) {
                String methodSignature = ((ConstructorDeclaration) node).getSignature().toString();
                // 对修饰符、参数支持
                String methodName = ((ConstructorDeclaration) node).getName().toString();
                String methodModifiers = "";
                if (!((ConstructorDeclaration) node).getModifiers().isEmpty()) {
                    methodModifiers = ((ConstructorDeclaration) node).getModifiers().toString().replace(",", " ").substring(1, ((ConstructorDeclaration) node).getModifiers().toString().length() - 1).toLowerCase().replaceAll("\\s+", " ");
                }
                String methodParameters = "";
                if (((ConstructorDeclaration) node).getParameters().isNonEmpty()) {
                    methodParameters = ((ConstructorDeclaration) node).getParameters().toString().substring(1, ((ConstructorDeclaration) node).getParameters().toString().length() - 1);
                }
                String method = methodName + "(" + methodParameters + ")";
                if (!methodModifiers.equals("")) {
                    method = methodModifiers + " " + method;
                }
                method = method.replaceAll("\\s+", " ");
                Artifact.Op<MethodArtifactData> methodArtifact = this.entityFactory.createArtifact(new MethodArtifactData(methodSignature, method));
                Node.Op methodNode = this.entityFactory.createOrderedNode(methodArtifact);
                classNode.addChild(methodNode);

                // 这是一个完整的方法
                Artifact.Op<IntegrityArtifactData> integrityArtifact = this.entityFactory.createArtifact(new IntegrityArtifactData(true));
                Node.Op integrityNode = this.entityFactory.createNode(integrityArtifact);
                methodNode.addChild(integrityNode);

                // add Comments to constructorMethod
                if (node.getComment().isPresent()) {
                    addDocChild(node.getComment().get().toString(), methodNode);
                }
                // add annotations to constructorMethod
                if (node.getAnnotations().isNonEmpty()) {
                    addAnnotationChild(node, methodNode);
                }
                if (((ConstructorDeclaration) node).getBody().getStatements().size() > 0) {
                    for (Statement stm : ((ConstructorDeclaration) node).getBody().getStatements()) {
                        addMethodChild(stm, methodNode);
                    }
                }
            }
        }
    }

    public void addDocChild(String doc, Node.Op parent) {
        Artifact.Op<DocArtifactData> docArtifact = this.entityFactory.createArtifact(new DocArtifactData(doc));
        Node.Op docNode = this.entityFactory.createNode(docArtifact);
        parent.addChild(docNode);
    }

    public void addAnnotationChild(com.github.javaparser.ast.Node node, Node.Op parent) {
        for (AnnotationExpr annotationExpr : ((BodyDeclaration<?>) node).getAnnotations()) {
            String annotation = annotationExpr.toString();
            Artifact.Op<AnnotationArtifactData> annotationArtifact = this.entityFactory.createArtifact(new AnnotationArtifactData(annotation));
            Node.Op annotationNode = this.entityFactory.createNode(annotationArtifact);
            parent.addChild(annotationNode);
        }
    }

    public void addFieldChild(com.github.javaparser.ast.Node node, Node.Op patternNode) {
        String field = node.getTokenRange().get().toString();
        // 去除注解
        if (((FieldDeclaration) node).getAnnotations().isNonEmpty()) {
            field = field.substring(field.lastIndexOf("\n") + 2).trim();
        }

        Artifact.Op<FieldArtifactData> fieldArtifact = this.entityFactory.createArtifact(new FieldArtifactData(field));
        Node.Op fieldNode = this.entityFactory.createOrderedNode(fieldArtifact);
        patternNode.addChild(fieldNode);

        if (node.getComment().isPresent()) {
            addDocChild(node.getComment().get().toString(), fieldNode);
        }

        if (((FieldDeclaration) node).getAnnotations().isNonEmpty()) {
            addAnnotationChild(node, fieldNode);
        }
    }

    public void addMethodChild(com.github.javaparser.ast.Node node, Node.Op methodNode) {

        if (node instanceof Comment) {
            System.out.println(node.toString() + "注释被忽略了。");
            return;
        }

        // 统一去除所有方法内的注释
        while (node.getComment().isPresent()) {
            node = node.removeComment();
        }

        if (node instanceof IfStmt) {
            String ifStmt = "if (" + ((IfStmt) node).getCondition().toString() + ")";
            Artifact.Op<BlockArtifactData> blockArtifact = this.entityFactory.createArtifact(new BlockArtifactData(ifStmt));
            Node.Op blockNode = this.entityFactory.createOrderedNode(blockArtifact);
            methodNode.addChild(blockNode);

            // thenStmt必存在，无需判断
            addMethodChild(((IfStmt) node).getThenStmt(), blockNode);

            // elseStmt不一定存在，需要判断
            if (((IfStmt) node).getElseStmt().isPresent()) {
                String elseStmt = "else";
                Artifact.Op<BlockArtifactData> blockElseArtifact = this.entityFactory.createArtifact(new BlockArtifactData(elseStmt));
                Node.Op blockNodeElse = this.entityFactory.createOrderedNode(blockElseArtifact);
                // FIXME:修改if else为父子关系
                blockNode.addChild(blockNodeElse);

                addMethodChild(((IfStmt) node).getElseStmt().get(), blockNodeElse);
            }

        } else if (node instanceof ForStmt) {
            String initialization = "", compare = "", update = "";

            // initialization为NodeList<Expression>类型
            if (((ForStmt) node).getInitialization().isNonEmpty()) {
                for (int i = 0; i < ((ForStmt) node).getInitialization().size(); ++i) {
                    initialization += ((ForStmt) node).getInitialization().get(i).toString();
                    if (i != ((ForStmt) node).getInitialization().size() - 1) {
                        initialization += ", ";
                    }
                }
            }

            // compare为Expression类型
            if (((ForStmt) node).getCompare().isPresent()) {
                compare = ((ForStmt) node).getCompare().get().toString();
            }

            // update为NodeList<Expression>类型
            if (((ForStmt) node).getUpdate().isNonEmpty()) {
                for (int i = 0; i < ((ForStmt) node).getUpdate().size(); ++i) {
                    update += ((ForStmt) node).getUpdate().get(i).toString();
                    if (i != ((ForStmt) node).getUpdate().size() - 1) {
                        update += ", ";
                    }
                }
            }

            String forStmt = "for (" + initialization + "; " + compare + "; " + update + ")";
            Artifact.Op<BlockArtifactData> blockArtifact = this.entityFactory.createArtifact(new BlockArtifactData(forStmt));
            Node.Op blockNode = this.entityFactory.createOrderedNode(blockArtifact);
            methodNode.addChild(blockNode);

            addMethodChild(((ForStmt) node).getBody(), blockNode);

        } else if (node instanceof DoStmt) {
            // FIXME:附带do语句的condition
            String doStmt = "do-while (" + ((DoStmt) node).getCondition().toString() + " )";
            Artifact.Op<DoStmtArtifactData> blockArtifact = this.entityFactory.createArtifact(new DoStmtArtifactData(doStmt, ((DoStmt) node).getCondition().toString()));
            Node.Op blockNode = this.entityFactory.createOrderedNode(blockArtifact);
            methodNode.addChild(blockNode);

            addMethodChild(((DoStmt) node).getBody(), blockNode);

        } else if (node instanceof ForEachStmt) {
            String foreachStmt = "for (" + ((ForEachStmt) node).getVariable().toString() + " : "
                    + ((ForEachStmt) node).getIterable().toString() + ")";
            Artifact.Op<BlockArtifactData> blockArtifact = this.entityFactory.createArtifact(new BlockArtifactData(foreachStmt));
            Node.Op blockNode = this.entityFactory.createOrderedNode(blockArtifact);
            methodNode.addChild(blockNode);

            addMethodChild(((ForEachStmt) node).getBody(), blockNode);

        } else if (node instanceof SwitchStmt) {
            String switchStmt = "switch (" + ((SwitchStmt) node).getSelector().toString() + ")";
            Artifact.Op<BlockArtifactData> blockArtifact = this.entityFactory.createArtifact(new BlockArtifactData(switchStmt));
            Node.Op blockNode = this.entityFactory.createOrderedNode(blockArtifact);
            methodNode.addChild(blockNode);

            if (((SwitchStmt) node).getEntries().isNonEmpty()) {
                for (SwitchEntry entryStmt : ((SwitchStmt) node).getEntries()) {
                    // 去除case不规范注释
                    SwitchEntry switchEntryStmt = entryStmt;
                    while (switchEntryStmt.getComment().isPresent()) {
                        switchEntryStmt = (SwitchEntry) switchEntryStmt.removeComment();
                    }
                    if (switchEntryStmt.toString().startsWith("case")) {
                        for (Expression label : switchEntryStmt.getLabels()) {
                            if (label.getComment().isPresent())
                                label.removeComment();
                        }
                    }
                    String entry = "";
                    if (switchEntryStmt.toString().startsWith("case")) {
                        entry = "case " + switchEntryStmt.getLabels().stream().map(Expression::toString).collect(Collectors.joining(", ")) + ":";
                    } else {
                        entry = "default:";
                    }
                    Artifact.Op<BlockArtifactData> blockArtifactEntry = this.entityFactory.createArtifact(new BlockArtifactData(entry));
                    Node.Op blockNodeEntry = this.entityFactory.createOrderedNode(blockArtifactEntry);
                    blockNode.addChild(blockNodeEntry);
                    addMethodChild(switchEntryStmt, blockNodeEntry);
                }
            }
        } else if (node instanceof SwitchEntry) {
            if (((SwitchEntry) node).getStatements().size() > 0) {
                for (com.github.javaparser.ast.Node switchEntry : ((SwitchEntry) node).getStatements()) {
                    addMethodChild(switchEntry, methodNode);
                }
            }

        } else if (node instanceof TryStmt) {
            // 作嵌套处理时
            // Artifact.Op<BlockArtifactData> blockArtifact = this.entityFactory.createArtifact(new BlockArtifactData("try"));
            // Node.Op blockNode = this.entityFactory.createOrderedNode(blockArtifact);
            // methodNode.addChild(blockNode);

            // tryBlock处理
            Artifact.Op<BlockArtifactData> tryArtifact = this.entityFactory.createArtifact(new BlockArtifactData("try"));
            Node.Op tryNode = this.entityFactory.createOrderedNode(tryArtifact);
            methodNode.addChild(tryNode);

            addMethodChild(((TryStmt) node).getTryBlock(), tryNode);

            // catchClauses处理
            for (CatchClause catchClause : ((TryStmt) node).getCatchClauses()) {
                // FIXME:修改try catch finally为父子关系
                addMethodChild(catchClause, tryNode);
            }

            // finallyBlock处理
            if (((TryStmt) node).getFinallyBlock().isPresent()) {
                Artifact.Op<BlockArtifactData> finallyArtifact = this.entityFactory.createArtifact(new BlockArtifactData("finally"));
                Node.Op finallyNode = this.entityFactory.createOrderedNode(finallyArtifact);
                // FIXME:修改try catch finally为父子关系
                tryNode.addChild(finallyNode);

                addMethodChild(((TryStmt) node).getFinallyBlock().get(), finallyNode);
            }

        } else if (node instanceof CatchClause) {
            String catchStmt = "catch (" + ((CatchClause) node).getParameter().toString() + ")";
            Artifact.Op<BlockArtifactData> catchArtifact = this.entityFactory.createArtifact(new BlockArtifactData(catchStmt));
            Node.Op catchNode = this.entityFactory.createOrderedNode(catchArtifact);
            methodNode.addChild(catchNode);

            addMethodChild(((CatchClause) node).getBody(), catchNode);

        } else if (node instanceof WhileStmt) {
            String whileStmt = "while (" + ((WhileStmt) node).getCondition().toString() + ")";
            Artifact.Op<BlockArtifactData> blockArtifact = this.entityFactory.createArtifact(new BlockArtifactData(whileStmt));
            Node.Op blockNode = this.entityFactory.createOrderedNode(blockArtifact);
            methodNode.addChild(blockNode);

            addMethodChild(((WhileStmt) node).getBody(), blockNode);

        } else if (node instanceof BlockStmt) {
            // BlockStmt虽然有注释问题，但无需处理，没有直接的node.toString()
            if (((BlockStmt) node).getStatements().size() > 0) {
                for (com.github.javaparser.ast.Node childNode : ((BlockStmt) node).getStatements()) {
                    addMethodChild(childNode, methodNode);
                }
            }

        } else if (node instanceof ExpressionStmt) {

            String exprStmt = ((ExpressionStmt) node).getExpression().toString() + ";";
            Artifact.Op<LineArtifactData> lineArtifact = this.entityFactory.createArtifact(new LineArtifactData(exprStmt));
            Node.Op lineNode = this.entityFactory.createOrderedNode(lineArtifact);
            methodNode.addChild(lineNode);

        } else if (node instanceof MethodCallExpr || node instanceof ContinueStmt ||
                node instanceof ExplicitConstructorInvocationStmt ||
                node instanceof ThrowStmt || node instanceof EmptyStmt ||
                node instanceof BreakStmt || node instanceof ReturnStmt ||
                node instanceof AssertStmt) {

            // 函数调用、continue、super or this构造函数、throw、空语句、break、return、assert
            Artifact.Op<LineArtifactData> lineArtifact = this.entityFactory.createArtifact(new LineArtifactData(node.toString()));
            Node.Op lineNode = this.entityFactory.createOrderedNode(lineArtifact);
            methodNode.addChild(lineNode);

        } else if (node instanceof SynchronizedStmt) {

            String syncStmt = "synchronized (" + ((SynchronizedStmt) node).getExpression().toString() + ")";
            Artifact.Op<BlockArtifactData> blockArtifact = this.entityFactory.createArtifact(new BlockArtifactData(syncStmt));
            Node.Op blockNode = this.entityFactory.createOrderedNode(blockArtifact);
            methodNode.addChild(blockNode);

            addMethodChild(((SynchronizedStmt) node).getBody(), blockNode);

        } else if (node instanceof LabeledStmt) {

            String labelStmt = ((LabeledStmt) node).getLabel().toString() + ":";
            Artifact.Op<BlockArtifactData> blockArtifact = this.entityFactory.createArtifact(new LabeledStmtArtifactData(labelStmt, ((LabeledStmt) node).getLabel().toString()));
            Node.Op blockNode = this.entityFactory.createOrderedNode(blockArtifact);
            methodNode.addChild(blockNode);

            addMethodChild(((LabeledStmt) node).getStatement(), blockNode);

        } else {
            System.out.println(node.toString() + "无法处理，所以未处理");
        }
    }

    private Collection<ReadListener> listeners = new ArrayList<>();

    @Override
    public void addListener(ReadListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(ReadListener listener) {
        this.listeners.remove(listener);
    }

}

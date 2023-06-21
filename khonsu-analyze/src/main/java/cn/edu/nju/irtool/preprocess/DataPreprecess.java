package cn.edu.nju.irtool.preprocess;

import cn.edu.nju.irtool.util.GetClassFileUtil;
import cn.edu.nju.irtool.util.TransferToTxtUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataPreprecess {

    // private static String projectPath = "D:\\nju\\irtool\\IRTool\\data\\iTrust-master";
    // private static String ucPath = "D:\\nju\\irtool\\IRTool\\data\\uc_origin";
    // private static String classTxtPath =  "D:\\nju\\irtool\\IRTool\\result\\class_txt";

    private String projectPath;
    private String ucPath;
    private String classPath;
    private String classProcessedPath;
    private String ucPreProcessedPath;

    private TransferToTxtUtil getSrcTXT = new TransferToTxtUtil();

    public DataPreprecess(String projectName, String path) {
        projectPath = path + "\\code_block\\" + projectName;
        ucPath = path + "\\release\\" + projectName;
        classPath = path + "\\result\\class\\" + projectName;
        classProcessedPath = path + "\\result\\class_preprocessed\\" + projectName;
        ucPreProcessedPath = path + "\\result\\uc_preprocessed\\" + projectName;
        List<String> paths = new ArrayList<>();
        try {
            paths.add(getClassProcessedPath());
            paths.add(getClassPath());
            paths.add(getUcPreProcessedPath());
            for (String p : paths) {
                File file = new File(p);
                if (file.exists()) {
                    FileUtils.deleteDirectory(file);
                }
                file.mkdirs();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public String getClassPath() {
        return classPath;
    }

    public String getClassProcessedPath() {
        return classProcessedPath;
    }

    public String getUcPreProcessedPath() {
        return ucPreProcessedPath;
    }

    private void cleanData() {
        deleteFileInDir(classPath);
        // deleteFileInDir(classTxtPath);
        deleteFileInDir(classProcessedPath);
        deleteFileInDir(ucPreProcessedPath);
    }

    private void deleteFileInDir(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        } else {
            for (File f : files) {
                f.delete();
            }
        }
    }

    public void arrangeData() {
        try {
            cleanData();
            GetClassFileUtil.getClassFromProject(projectPath, classPath);
            // getSrcTXT.transferTXT(classPath, classTxtPath);
            BatchingPreprocess preprocess = new BatchingPreprocess(ucPath, classPath, classProcessedPath, ucPreProcessedPath);
            preprocess.doProcess();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();
        DataPreprecess dataProcess = new DataPreprecess("lucene", "D:\\Workplace\\nju\\tmp");
        dataProcess.arrangeData();
        long endTime = System.currentTimeMillis();
        System.out.println("time cost:" + (endTime - startTime) * 1.0 / 1000 / 60);
    }

}

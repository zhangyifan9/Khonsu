package preprocess;

import util.GetClassFileUtil;
import util.TransferToTxtUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class DataPreprecess {


//    private static String projectPath = "D:\\nju\\irtool\\IRTool\\data\\iTrust-master";
    private static String projectPath = "D:\\nju\\irtool\\IRTool\\data\\lucene-singleversion";
//    private static String ucPath = "D:\\nju\\irtool\\IRTool\\data\\uc_origin";
    private static String ucPath = "D:\\nju\\irtool\\IRTool\\data\\lucene-releasesingleversion";

    private static String classPath = "D:\\nju\\irtool\\IRTool\\result\\class";
    // private static String classTxtPath =  "D:\\nju\\irtool\\IRTool\\result\\class_txt";
    private static String classProcessedPath =  "D:\\nju\\irtool\\IRTool\\result\\class_preprocessed";
    private static String ucPreProcessedPath =  "D:\\nju\\irtool\\IRTool\\result\\uc_preprocessed";




    private TransferToTxtUtil getSrcTXT = new TransferToTxtUtil();

    public static String getClassProcessedPath() {
        return classProcessedPath;
    }

    public static void setClassProcessedPath(String classProcessedPath) {
        DataPreprecess.classProcessedPath = classProcessedPath;
    }

    public static String getUcPreProcessedPath() {
        return ucPreProcessedPath;
    }

    public static void setUcPreProcessedPath(String ucPreProcessedPath) {
        DataPreprecess.ucPreProcessedPath = ucPreProcessedPath;
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
//            getSrcTXT.transferTXT(classPath, classTxtPath);
            BatchingPreprocess preprocess = new BatchingPreprocess(ucPath, classPath, classProcessedPath, ucPreProcessedPath);
            preprocess.doProcess();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();
        DataPreprecess dataProcess = new DataPreprecess();
        dataProcess.arrangeData();
        long endTime = System.currentTimeMillis();
        System.out.println("time cost:" + (endTime - startTime) * 1.0 / 1000 / 60);
    }

}

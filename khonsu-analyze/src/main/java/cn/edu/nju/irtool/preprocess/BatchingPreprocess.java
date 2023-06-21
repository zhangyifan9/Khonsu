package cn.edu.nju.irtool.preprocess;

import cn.edu.nju.irtool.util.FileIOUtil;

import java.io.File;
import java.io.IOException;

public class BatchingPreprocess {
    private File ucPath;
    private File classTxtPath;
    private File classProcessedPath;
    private File ucPreProcessedPath;

    public BatchingPreprocess(String ucPath, String classTxtPath, String classProcessedPath, String ucPreProcessedPath) {
        this.ucPath = new File(ucPath);
        this.classTxtPath = new File(classTxtPath);
        this.ucPreProcessedPath = new File(ucPreProcessedPath);
        this.classProcessedPath = new File(classProcessedPath);
    }

    public void doProcess() throws IOException {

        /**
         * 也按照源代码那套处理
         * @date 2017/10/15
         */
        preprocessUCFiles(ucPath);

        preprocessJavaAndJsPFiles(classTxtPath);

    }

    private void preprocessJavaAndJsPFiles(File dirPath) throws IOException {
        if (dirPath.isDirectory()) {
            for (File f1:dirPath.listFiles()){
                File f1dir=new File(classProcessedPath.getPath()+"\\"+f1.getName());
                if(!f1dir.exists()){
                    f1dir.mkdir();
                }
                for (File f : f1.listFiles()) {
                    if (f.getName().endsWith("_jsp.txt") || f.getName().endsWith("_jspService.txt")) {
                        TextPreprocess textPreprocessor = new TextPreprocess(FileIOUtil.readFile(f.getPath()));
                        FileIOUtil.writeFile(textPreprocessor.doJspFileProcess(), classProcessedPath.getPath()+"/"+f.getName());
                    } else if (f.getName().endsWith(".txt")) {
                        TextPreprocess textPreprocessor = new TextPreprocess(FileIOUtil.readFile(f.getPath()));
                        FileIOUtil.writeFile(textPreprocessor.doJavaFileProcess(), classProcessedPath.getPath()+"\\"+f1.getName()+"/"+f.getName());
                    }
                }
            }

        }
    }

    public void preprocessUCFiles(File ucDirPath) throws IOException {
        if (ucDirPath.isDirectory()) {
            for (File f1 : ucDirPath.listFiles()) {
                for (File f2 : f1.listFiles()) {
                    File f1dir = new File(ucPreProcessedPath.getPath() + "\\" + f1.getName() + "\\" + f2.getName());
                    if (!f1dir.exists()) {
                        f1dir.mkdirs();
                    }
                    for (File f : f2.listFiles()) {
                        if (f.getName().endsWith(".txt")) {
                            TextPreprocess textPreprocessor = new TextPreprocess(FileIOUtil.readFile(f.getPath()));
                            FileIOUtil.writeFile(textPreprocessor.doUCFileProcess(), ucPreProcessedPath.getPath()+ "\\" + f1.getName() + "\\" + f2.getName() + "\\" +f.getName());
                        }
                    }
                }
            }

        }
    }
}

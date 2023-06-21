package cn.edu.nju.irtool.util;

import java.io.*;

public class GetClassFileUtil {
    public static void getClassFromProject(String originPath, String targetPath) throws IOException {
        File dir = new File(originPath);
        for (File f1 : dir.listFiles()) {
            String targetPathAndf1 = targetPath + "\\" + f1.getName();
            for (File f : f1.listFiles()) {
                String fileName = f.getName();
                if (fileName.endsWith(".txt")) {//修改过原为：.java
                    System.out.println("java file name: " + fileName);
                    writeFile(f, targetPathAndf1);
                }
            }
        }
    }

    private static void writeFile(File javaFile, String targetPath) throws IOException {
        String name = javaFile.getName();
        BufferedReader br = new BufferedReader(new FileReader(javaFile));
        File dir = new File(targetPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(targetPath + File.separator + name));

        String line;
        while ((line = br.readLine()) != null) {
            bw.write(line);
            bw.newLine();
        }
        br.close();
        bw.close();
    }
}

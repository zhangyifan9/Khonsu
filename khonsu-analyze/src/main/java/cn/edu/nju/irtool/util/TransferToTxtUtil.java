package cn.edu.nju.irtool.util;

import java.io.*;

public class TransferToTxtUtil {
    public void transferTXT(String origin,String target) throws IOException {
        File dir = new File(origin);
        File[] childs = dir.listFiles();
        System.out.println("dd");
        for(File child:childs) {
            if(child.getName().endsWith(".txt")||child.getName().endsWith(".jsp")) {
                copy(child,target);
            }
        }
    }

    private void copy(File child, String target) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(child));
        //String name = getFileName(child.getName());
        String name = child.getName();
        name = name.replace("тАР", "-");
        if(name.endsWith(".jsp")) {
            name = name.substring(0, name.lastIndexOf("."))+"_jsp.txt";
        }
        else {
            name = name.substring(0, name.lastIndexOf("."))+".txt";
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(target+File.separator+name));
        String line = null;
        while((line=br.readLine())!=null) {
            bw.write(line);
            bw.newLine();
        }
        br.close();
        bw.close();
    }

    private String getFileName(String name) {
        return name.substring(0,name.lastIndexOf("."))+".txt";
    }

    public void printFileName(String basePath) {
        File dir = new File(basePath);
        for(File file:dir.listFiles()) {
            if(file.getName().endsWith("updateLabProc_jsp.txt")) {
                System.out.println(file.getName().replace("тАР", "-"));
            }
        }
    }

}

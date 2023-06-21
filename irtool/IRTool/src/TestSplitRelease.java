import java.io.*;

public class TestSplitRelease {
    // public String[] startStr={"(","Bug Fixes","Changes in runtime behavior"
    //         ,"New features","API Changes","Optimizations","Documentation"};
    public static void main(String[] args) {
        String dir="D:\\nju\\irtool\\IRTool\\data\\294.txt";
        String thisLine;
        String thisPart="";
        BufferedReader in = null; // 创建缓存区字符输入流，需要传如Reader对象
        int i=0;
        try {
            in = new BufferedReader(new FileReader(dir));
            while((thisLine = in.readLine()) != null) {
                System.out.println(thisLine);// 每次读取一行，直到文件结束
                if(thisLine.equals("")||thisLine.startsWith("(")||thisLine.startsWith("Bug Fixes")){
                    if(!thisPart.equals("")){
                        FileWriter fw = new FileWriter("D:\\nju\\irtool\\IRTool\\data\\lucene-release\\version2.9.4\\r"+i+".txt");
                        fw.write(thisPart);
                        fw.close();
                        thisPart="";
                        i++;
                    }
                }else{
                    thisPart+=thisLine;
                }
            }
            if(!thisPart.equals("")){
                FileWriter fw = new FileWriter("D:\\nju\\irtool\\IRTool\\data\\lucene-release\\version2.9.4\\r"+i+".txt");
                fw.write(thisPart);
                fw.close();
                thisPart="";
                i++;
            }

            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

package cn.edu.nju.irtool.preprocess;

import java.io.File;
import java.io.IOException;

public class TextPreprocess {

    File directory = new File("");// 参数为空
    String courseFile = directory.getCanonicalPath();
//        System.out.println("path2: "+courseFile+"\\irtool\\IRTool\\data\\stopwords\\stop-words_english_1_en.txt");
    private String str;
//    private String stopwordsPath = "E:\\NJU项目\\Khonsu-毕设实验\\Khonsu\\irtool\\IRTool\\data\\stopwords\\stop-words_english_1_en.txt"; // 位于data包下
//    private String jspStopwordsPath = "E:\\NJU项目\\Khonsu-毕设实验\\Khonsu\\irtool\\IRTool\\data\\stopwords\\stop-words_jsp.txt"; // 位于data包下

    private String stopwordsPath = courseFile+"\\irtool\\IRTool\\data\\stopwords\\stop-words_english_1_en.txt"; // 位于irtool下
    private String jspStopwordsPath = courseFile+"\\irtool\\IRTool\\data\\stopwords\\stop-words_jsp.txt"; // 位于irtool下

    public TextPreprocess(String str) throws IOException {
        this.str = str;
    }

    public String doUCFileProcess() {
        str = CleanUp.chararctorClean(str);
        // here
//        str = CamelCase.split(str);
        str = CleanUp.tolowerCase(str);
        str = Snowball.stemming(str);
        str = Stopwords.remover(str, stopwordsPath);
        str = CleanUp.lengthFilter(str, 3);
        return str;
    }

    public String doJavaFileProcess() {
        str = CleanUp.chararctorClean(str);
        str = CamelCase.split(str);
        str = CleanUp.tolowerCase(str);
        str = Snowball.stemming(str);
        str = Stopwords.remover(str, stopwordsPath);
        str = CleanUp.lengthFilter(str, 3);
        return str;
    }

    public String doJspFileProcess() {
        str = CleanUp.chararctorClean(str);
        str = Stopwords.remover(str, jspStopwordsPath);
        str = CamelCase.split(str);
        str = CleanUp.lengthFilter(str, 3);
        str = CleanUp.tolowerCase(str);
        str = Snowball.stemming(str);
        str = Stopwords.remover(str, stopwordsPath);
        str = Stopwords.remover(str, jspStopwordsPath);
        return str;
    }

    public static void main(String[] args) throws IOException {

        File directory = new File("");// 参数为空
        String courseFile = directory.getCanonicalPath();
        System.out.println("path2: "+courseFile+"\\irtool\\IRTool\\data\\stopwords\\stop-words_english_1_en.txt");

    }

}

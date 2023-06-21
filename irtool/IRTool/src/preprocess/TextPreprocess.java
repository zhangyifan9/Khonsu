package preprocess;

public class TextPreprocess {
    private String str;
    private String stopwordsPath = "D:\\nju\\irtool\\IRTool\\data\\stopwords\\stop-words_english_1_en.txt";
    private String jspStopwordsPath = "D:\\nju\\irtool\\IRTool\\data\\stopwords\\stop-words_jsp.txt";

    public TextPreprocess(String str) {
        this.str = str;
    }

    public String doUCFileProcess() {
        str = CleanUp.chararctorClean(str);
        // here
//        str = CamelCase.split(str);
        str = CleanUp.lengthFilter(str, 3);
        str = CleanUp.tolowerCase(str);
        str = Snowball.stemming(str);
        str = Stopwords.remover(str, stopwordsPath);
        return str;
    }

    public String doJavaFileProcess() {
        str = CleanUp.chararctorClean(str);
        str = CamelCase.split(str);
        str = CleanUp.lengthFilter(str, 3);
        str = CleanUp.tolowerCase(str);
        str = Snowball.stemming(str);
        str = Stopwords.remover(str, stopwordsPath);
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
}

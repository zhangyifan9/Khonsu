package preprocess;

import util.FileIOUtil;

import java.util.Arrays;
import java.util.List;

public class Stopwords {
    public static String remover(String input, String stopwordsPath) {

        String stopwords[] = FileIOUtil.readFile(stopwordsPath).split("\n");
        // about\r remove that \r
        for (int i = 0; i < stopwords.length; i++) {
            stopwords[i] = stopwords[i].trim();
        }
        List<String> stopwordsList = Arrays.asList(stopwords);

        String words[] = input.split(" ");

        StringBuilder sb = new StringBuilder();

        for (String word : words) {
            if (!stopwordsList.contains(word)) {
                sb.append(word);
                sb.append(" ");
            } else {
//                System.out.println("Delete word " + word);
            }
        }
        return sb.toString();
    }
}

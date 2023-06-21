package cn.edu.nju.irtool.preprocess;

import cn.edu.nju.irtool.preprocess.snowball.EnglishStemmer;

public class Snowball {
    public static String stemming(String input) {
        StringBuilder sb = new StringBuilder();

        String words[] = input.split(" ");

        EnglishStemmer stemmer = new EnglishStemmer();

        for (String word : words) {
            stemmer.setCurrent(word);
            stemmer.stem();
            sb.append(stemmer.getCurrent());
            sb.append(" ");
        }

        return sb.toString();
    }
}

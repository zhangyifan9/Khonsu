package preprocess;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CleanUp {
    public static String chararctorClean(String input) {

        StringBuilder sb = new StringBuilder();

        Pattern p = Pattern.compile("[a-z|A-Z]+");
        Matcher m = p.matcher(input);

        while (m.find()) {
            sb.append(m.group().trim());
            sb.append(" ");
//            System.out.println(m.group().trim());
        }
        return sb.toString();
    }

    public static String lengthFilter(String input, int len) {
        StringBuilder sb = new StringBuilder();

        String words[] = input.split(" ");

        for (String word : words) {
            if (word.length() >= len) {
                sb.append(word);
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    public static String tolowerCase(String input) {
        StringBuilder sb = new StringBuilder();

        String words[] = input.split(" ");

        for (String word : words) {
            sb.append(word.toLowerCase());
            sb.append(" ");
        }

        return sb.toString();
    }
}

package preprocess;

public class CamelCase {
    public static String split(String input) {
        String words[] = input.split(" ");

        StringBuilder sb = new StringBuilder();

        for (String word : words) {
            for (String cc : splitCamelCase(word)) {
                sb.append(cc);
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    private static String[] splitCamelCase(String s) {
        return s.split("(?<!^)(?=[A-Z])");
    }
}

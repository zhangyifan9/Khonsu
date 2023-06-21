public class TestSomething {
    public static void main(String[] args) {
        String issue= "A()" +
                "(Mike McCandless, Robert Muir)";
        int  leftBracketLocation = 0;
        char[] chr = issue.toCharArray();
        System.out.println(chr);
        for (int i = chr.length-1 ; i >= 0; i--) {
            if(chr[i] == '(') {
                leftBracketLocation = i;
                break;
            }
        }
        issue = issue.substring(0 ,leftBracketLocation);
        System.out.println(issue);
    }
}

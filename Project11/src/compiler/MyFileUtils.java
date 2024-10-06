package compiler;

import static compiler.JackCompiler.pr;
import static compiler.JackCompiler.bw;    // TO DELETE

public class MyFileUtils {
    private MyFileUtils() {}

    /**
     * Skips the comments in the file
     * @return true if a comment was skipped, false otherwise
     */
    private static boolean skipComments() throws Exception {
        char y = (char) pr.read();

        if (y == '/') {
            while ((char) pr.read() != '\n');       // Skip the line
        } else if (y == '*') {
            while (true) {
                if ((char) pr.read() == '*' && (char) pr.read() == '/') {
                    break;
                }
            }
        } else {
            pr.unread(y);
            return false;
        }

        return true;
    }

    /**
     * Skips all the whitespaces and comments
     * @return the first non-whitespace character
     */
    private static char skipWhitespacesAndComments() throws Exception {
        char x;

        do {
            x = (char) pr.read();

            if (x == '/') {
                skipComments();
                x = ' ';
            }
        } while (Character.isWhitespace(x));

        return x;
    }

    /**
     * Returns the substring from cursor position when it was called to the given character location
     * Skips all the comments and leading whitespaces
     * @param c the character where you want to stop
     * @return the substring before c
     */
    protected static String getUntil(char c) throws Exception {
        char x = skipWhitespacesAndComments();

        if (x == c) {
            return "";
        }

        // Building the string by appending characters
        StringBuilder sb = new StringBuilder();
        boolean insideString = false;

        do {
            if (x == '/' && skipComments()) {
                continue;
            } else if (x == '"') {
                insideString = !insideString;
            }

            sb.append(x);
        } while ((x = (char) pr.read()) != c && pr.ready());

        return sb.toString().stripTrailing();
    }

    /**
     * Returns the substring from cursor position when it was called to the next whitespace character
     * Skips all the comments and leading whitespaces
     * @return the substring before whitespace
     */
    protected static String getUntilNotWhiteSpace() throws Exception {
        char x = skipWhitespacesAndComments();

        // Building the string by appending characters
        StringBuilder sb = new StringBuilder();
        boolean insideString = false;

        do {
            if (x == '/' && skipComments()) {
                return sb.toString();
            } else if (x == '"') {
                insideString = !insideString;
            }

            sb.append(x);
            x = (char) pr.read();
        } while (!Character.isWhitespace(x) && pr.ready());

        return sb.toString();
    }

    /**
     * Matches the given string that it exists in the file from the current cursor position
     * @param s The string you want to match
     */
    protected static void eat(String s) throws Exception {
        char x = skipWhitespacesAndComments();

        int i = 0;
        while (true) {
            if (s.charAt(i) != x) {
                throw new Exception("Syntax Error!!");
            }

            if (i >= s.length() - 1) {
                return;
            }

            x = (char) pr.read();
            ++i;
        }
    }


    /** TO DELETE **/
    protected static void printKeyword(String s) throws Exception {
        bw.write("<keyword> ");
        bw.write(s);
        bw.write(" </keyword>\n");
    }

    protected static void printIdentifier(String s) throws Exception {
        bw.write("<identifier> ");
        bw.write(s);
        bw.write(" </identifier>\n");
    }

    protected static void printSymbol(char c) throws Exception {
        bw.write("<symbol> ");

        if (c == '<') {
            bw.write("&lt;");
        } else if (c == '>') {
            bw.write("&gt;");
        } else if (c == '&') {
            bw.write("&amp;");
        } else {
            bw.write(c);
        }

        bw.write(" </symbol>\n");
    }

    protected static void printIntConstant(String s) throws Exception {
        bw.write("<integerConstant> ");
        bw.write(s);
        bw.write(" </integerConstant>\n");
    }

    protected static void printStringConstant(String s) throws Exception {
        bw.write("<stringConstant> ");
        bw.write(s);
        bw.write(" </stringConstant>\n");
    }
}

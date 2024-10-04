package compiler;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public class MyFileUtils {
    private static BufferedReader br;
    private static BufferedWriter bw;

    protected static void setBrAndBw(BufferedReader br, BufferedWriter bw) {
        MyFileUtils.br = br;
        MyFileUtils.bw = bw;
    }

    /**
     * Skips all the whitespaces and comments
     * @return the first non-whitespace character
     */
    private static char skipWhitespacesAndComments() throws Exception {
        char x;

        do {
            x = (char) br.read();

            if (x == '/') {
                char y = (char) br.read();

                if (y == '/') {
                    br.readLine();
                } else if (y == '*') {
                    while (true) {
                        if ((char) br.read() == '*') {
                            if ((char) br.read() == '/') {
                                break;
                            }
                        }
                    }
                } else {
                    throw new Exception("Syntax Error!!");
                }

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

        do {
            sb.append(x);
        } while ((x = (char) br.read()) != c && br.ready());

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

        do {
            sb.append(x);
            x = (char) br.read();
        } while (!Character.isWhitespace(x) && br.ready());

        return sb.toString();
    }

    /**
     * Matches the given string that it exists in the file from the current cursor position
     * @param s The string you want to match
     */
    protected static void eat(String s) throws Exception {
        char x;

        do {
            x = (char) br.read();
        } while (x == ' ' || x == '\n');

        int i = 0;
        while (true) {
            if (s.charAt(i) != x) {
                throw new Exception("Syntax Error!!");
            }

            if (i >= s.length() - 1) {
                return;
            }

            x = (char) br.read();
            ++i;
        }
    }

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

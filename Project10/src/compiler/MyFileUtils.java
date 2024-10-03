package compiler;

import java.io.IOException;
import static compiler.JackTokenizer.br;
import static compiler.JackTokenizer.bw;

public class MyFileUtils {

    /**
     * Returns the substring from cursor position when it was called to the given character location
     * Skips all the comments and leading whitespaces
     * @param c the character where you want to stop
     * @return the substring before c
     */
    protected static String getUntil(char c) throws IOException {
        char x;

        // Skipping whitespaces and comments
        do {
            x = (char) JackTokenizer.br.read();

            if (x == '/' && br.read() == '/') {
                br.readLine();
                x = ' ';
            }

        } while (Character.isWhitespace(x));

        // Building the string by appending characters
        StringBuilder sb = new StringBuilder();

        do {
            sb.append(x);
        } while ((x = (char) br.read()) != c && br.ready());

        return sb.toString().stripTrailing();
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
        while (i < s.length()) {
            if (s.charAt(i) != x) {
                throw new Exception("Syntax Error!!");
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
        bw.write(c);
        bw.write(" </symbol>\n");
    }
}

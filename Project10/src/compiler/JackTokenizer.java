package compiler;

import java.io.*;
import static compiler.MyFileUtils.*;

public class JackTokenizer {
    protected static BufferedReader br;
    protected static BufferedWriter bw;

    public static void start(String fileName) {
        try {
            br = new BufferedReader(new FileReader(fileName));
            bw = new BufferedWriter(new FileWriter(fileName.substring(0, fileName.lastIndexOf(".")) + "T.xml"));

            bw.write("<tokens>\n");
            compileClass();
            bw.write("</tokens>\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void compileClass() throws Exception {
        eat("class");
        printKeyword("class");

        printIdentifier(getUntil('{'));
        printSymbol('{');

        handleClass();
    }

    private static void handleClass() throws Exception {
        while (true) {
            String s = getUntil(' ');

            // EOF
            if (s.charAt(0) == '\uFFFF') {
                return;
            }

            switch (s) {
                case "static", "field" -> {
                    printKeyword(s);
                    handleVars();
                }
                case "constructor", "method", "function" -> {
                    printKeyword(s);
                    handleSubroutine();
                }
                case "}" -> {
                    printSymbol('}');
                    return;
                }
                default -> throw new Exception("Syntax Error!!");
            }
        }
    }

    private static void handleVars() throws Exception {
        String s = getUntil(' ');

        if (s.equals("int") || s.equals("char") || s.equals("boolean")) {
            printKeyword(s);
        } else {
            printIdentifier(s);
        }

        s = getUntil(';');
        printIdentifier(s);
        printSymbol(';');
    }

    private static void handleSubroutine() throws Exception {
        String s = getUntil(' ');

        if (s.equals("int") || s.equals("char") || s.equals("boolean") || s.equals("void")) {
            printKeyword(s);
        } else {
            throw new Exception("Syntax Error!!");
        }

        s = getUntil('(');
        printIdentifier(s);
        printSymbol('(');

        s = getUntil(')');
        handleParameterList(s);
        printSymbol(')');

        eat("{");
        printSymbol('{');

        handleStatements();
    }

    private static void handleParameterList(String s) throws Exception {
        String[] arr = s.split(",");
        boolean first = true;

        for (String str : arr) {
            if (!first)
                printSymbol(',');

            String[] temp = str.stripLeading().split(" ");

            if (temp[0].equals("int") || temp[0].equals("char") || temp[0].equals("boolean")) {
                printKeyword(temp[0]);
            } else {
                printIdentifier(temp[0]);
            }

            printIdentifier(temp[1]);
            first = false;
        }
    }

    private static void handleStatements() throws Exception {
        while (true) {
            String s = getUntil(' ');

            if (s.equals("}")) {
                printSymbol('}');
                return;
            }
        }
    }

}

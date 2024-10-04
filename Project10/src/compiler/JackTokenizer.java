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

            br.close();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void compileClass() throws Exception {
        String s = getUntil(' ');
        if (!s.equals("class")) {
            throw new Exception("Syntax Error!!");
        }
        printKeyword("class");

        printIdentifier(getUntil('{'));
        printSymbol('{');

        handleClass();
    }

    private static void handleClass() throws Exception {
        while (true) {
            String s = getUntilNotWhiteSpace();

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
        String s = getUntilNotWhiteSpace();

        if (s.equals("int") || s.equals("char") || s.equals("boolean")) {
            printKeyword(s);
        } else {
            printIdentifier(s);
        }

        s = getUntil(';');
        handleVarList(s);
        printSymbol(';');
    }

    private static void handleVarList(String s) throws Exception {
        String[] arr = s.split(",");
        boolean first = true;

        for (String str : arr) {
            if (!first)
                printSymbol(',');

            printIdentifier(str.stripLeading());
            first = false;
        }
    }

    private static void handleSubroutine() throws Exception {
        String s = getUntilNotWhiteSpace();

        if (s.equals("int") || s.equals("char") || s.equals("boolean") || s.equals("void")) {
            printKeyword(s);
        } else {
            printIdentifier(s);
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
        if (s.isEmpty()) {
            return;
        }

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
        int checker = 0;

        while (true) {
            String s = getUntilNotWhiteSpace();

            switch (s) {
                case "var" -> {
                    printKeyword(s);
                    handleVars();
                }
                case "let" -> {
                    printKeyword(s);
                    handleLet();
                }
                case "if" -> {
                    printKeyword(s);
                    handleIfWhile();

                    checker = 1;
                }
                case "else" -> {
                    if (checker != 0)
                        throw new Exception("Syntax Error!!");

                    printKeyword(s);
                    handleElse();
                }
                case "while" -> {
                    printKeyword(s);
                    handleIfWhile();
                }
                case "do" -> {
                    printKeyword(s);
                    handleDo();
                }
                case "return" -> {
                    printKeyword(s);
                    handleReturn();
                }
                case "}" -> {
                    printSymbol('}');
                    return;
                }
                default -> {
                    if (s.equals("return;")) {
                        printKeyword("return");
                        printSymbol(';');
                    } else {
                        throw new Exception("Syntax Error!!");
                    }
                }
            }
            --checker;
        }
    }

    private static void handleLet() throws Exception {
        String s = getUntil('=');

        if (s.endsWith("]")) {
            int i = s.indexOf('[');

            printIdentifier(s.substring(0, i));
            printSymbol('[');
            handleExpression(s.substring(i + 1, s.length() - 1));
            printSymbol(']');
        } else {
            printIdentifier(s);
        }

        printSymbol('=');
        handleExpression(getUntil(';'));
        printSymbol(';');
    }

    private static void handleElse() throws Exception {
        eat("{");
        printSymbol('{');

        handleStatements();
    }

    private static void handleIfWhile() throws Exception {
        eat("(");
        printSymbol('(');

        handleExpression(getUntil(')'));
        printSymbol(')');

        eat("{");
        printSymbol('{');

        handleStatements();
    }

    private static void handleDo() throws Exception {
        handleSubroutineCall(getUntil(';'));
        printSymbol(';');
    }

    private static void handleReturn() throws Exception {
        String s = getUntil(';');

        if (!s.isEmpty()) {
            handleExpression(s);
        }

        printSymbol(';');
    }

    private static void handleExpression(String s) throws Exception {
        if (s.isEmpty()) {
            return;
        }

        if (s.equals("this")) {
            printKeyword(s);
        } else {
            printIdentifier(s);
        }
    }

    private static void handleExpressionList(String s) throws Exception {
        if (s.isEmpty()) {
            return;
        }

        String[] arr = s.split(",");
        boolean first = true;

        for (String str : arr) {
            if (!first)
                printSymbol(',');

            handleExpression(str.stripLeading());
            first = false;
        }
    }

    private static void handleSubroutineCall(String s) throws Exception {
        int i = s.indexOf('.');

        if (i != -1) {
            printIdentifier(s.substring(0, i));
            printSymbol('.');
        }

        int j = s.indexOf('(');
        printIdentifier(s.substring(i + 1, j));
        printSymbol('(');

        handleExpressionList(s.substring(j + 1, s.length() - 1));
        printSymbol(')');
    }

}

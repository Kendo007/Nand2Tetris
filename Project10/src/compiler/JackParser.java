package compiler;

import java.io.*;

import static compiler.MyFileUtils.*;

public class JackParser {
    private static BufferedReader br;
    private static BufferedWriter bw;

    public static void start(String fileName) {
        try {
            br = new BufferedReader(new FileReader(fileName));
            bw = new BufferedWriter(new FileWriter(fileName.substring(0, fileName.lastIndexOf(".")) + ".xml"));

            setBrAndBw(br, bw);

            compileClass();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void compileClass() throws Exception {
        String s = getUntil(' ');
        if (!s.equals("class")) {
            throw new Exception("Syntax Error!!");
        }
        bw.write("<class>\n");

        printKeyword("class");
        printIdentifier(getUntil('{'));
        printSymbol('{');

        handleClass();

        bw.write("</class>\n");
    }

    private static void handleClass() throws Exception {
        while (true) {
            String s = getUntilNotWhiteSpace();

            // EOF
            if (s.charAt(0) == '\uFFFF') {
                return;
            }

            switch (s) {
                case "static":
                case "field":
                    bw.write("<classVarDec>\n");
                    printKeyword(s);
                    handleVars();
                    bw.write("</classVarDec>\n");
                    break;

                case "constructor":
                case "method":
                case "function":
                    bw.write("<subroutineDec>\n");
                    printKeyword(s);
                    handleSubroutine();
                    bw.write("</subroutineDec>\n");
                    break;

                case "}":
                    printSymbol('}');
                    return;

                default:
                    throw new Exception("Syntax Error!!");
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

        bw.write("<parameterList>\n");
        handleParameterList(s);
        bw.write("</parameterList>\n");

        printSymbol(')');

        bw.write("<subroutineBody>\n");
        eat("{");
        printSymbol('{');
        handleStatements();
        bw.write("</subroutineBody>\n");
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
        int checker = -1;
        boolean first = true;

        while (true) {
            String s = getUntilNotWhiteSpace();

            if (checker == 0 && !s.equals("else")) {
                bw.write("</ifStatement>\n");
            }

            if (first && !s.equals("var")) {
                bw.write("<statements>\n");
                first = false;
            }

            switch (s) {
                case "var":
                    bw.write("<varDec>\n");
                    printKeyword(s);
                    handleVars();
                    bw.write("</varDec>\n");
                    break;

                case "let":
                    bw.write("<letStatement>\n");
                    printKeyword(s);
                    handleLet();
                    bw.write("</letStatement>\n");
                    break;

                case "if":
                    bw.write("<ifStatement>\n");
                    printKeyword(s);
                    handleIfWhile();
                    checker = 1;
                    break;

                case "else":
                    if (checker != 0) {
                        throw new Exception("Syntax Error!!");
                    }
                    printKeyword(s);
                    handleElse();
                    bw.write("</ifStatement>\n");
                    break;

                case "while":
                    bw.write("<whileStatement>\n");
                    printKeyword(s);
                    handleIfWhile();
                    bw.write("</whileStatement>\n");
                    break;

                case "do":
                    bw.write("<doStatement>\n");
                    printKeyword(s);
                    handleDo();
                    bw.write("</doStatement>\n");
                    break;

                case "return":
                    bw.write("<returnStatement>\n");
                    printKeyword(s);
                    handleReturn();
                    bw.write("</returnStatement>\n");
                    break;

                case "}":
                    bw.write("</statements>\n");
                    printSymbol('}');
                    return;

                default:
                    if (s.equals("return;")) {
                        bw.write("<returnStatement>\n");
                        printKeyword("return");
                        printSymbol(';');
                        bw.write("</returnStatement>\n");
                    } else {
                        throw new Exception("Syntax Error!!");
                    }
                    break;
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

        String s = getUntil('{');
        handleExpression(s.substring(0, s.length() - 1));

        printSymbol(')');
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

    private static boolean isOp(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '&' || c == '|' || c == '<' || c == '>' || c == '=';
    }

    private static void handleTerm(String s) throws Exception {
        bw.write("<term>\n");
        if (s.isEmpty()) {
            return;
        }

        if (s.charAt(0) == '-' || s.charAt(0) == '~') {         // unary Op
            printSymbol(s.charAt(0));
            handleTerm(s.substring(1));
        } else if (s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') {         // string constant
            printStringConstant(s.substring(1, s.length() - 1));
        } else if (Character.isDigit(s.charAt(0))) {                // integer constant
            printIntConstant(s);
        } else if (s.equals("true") || s.equals("false") || s.equals("null") || s.equals("this")) {      // keyword constant
            printKeyword(s);
        } else if (s.charAt(0) == '(') {            // expression
            printSymbol('(');
            handleExpression(s.substring(1, s.length() - 1));
            printSymbol(')');
        } else {                        // varName | varName[expression] | subroutineCall
            int i = s.indexOf('[');

            if (i != -1) {
                printIdentifier(s.substring(0, i));
                printSymbol('[');
                handleExpression(s.substring(i + 1, s.length() - 1));
                printSymbol(']');
            } else if (s.contains("(")) {
                handleSubroutineCall(s);
            } else {
                printIdentifier(s);
            }
        }

        bw.write("</term>\n");
    }

    private static void handleExpression(String s) throws Exception {
        bw.write("<expression>\n");

        StringBuilder sb = new StringBuilder();
        int n = s.length();
        boolean insideString = false;
        int balanced = 0;

        for (int i = 0; i < n; ++i) {
            char c = s.charAt(i);

            if (Character.isWhitespace(c) && !insideString) {
                continue;
            } else if (c == '"') {
                insideString = !insideString;
            } else if (c == '(') {
                ++balanced;
            } else if (c == ')') {
                --balanced;
            } else if (isOp(c)) {
                if (balanced == 0 && !((c == '-' || c == '~') && sb.isEmpty())) {
                    handleTerm(sb.toString());
                    printSymbol(c);
                    sb.setLength(0);

                    continue;
                }
            }

            sb.append(c);
        }

        if (!sb.isEmpty()) {
            handleTerm(sb.toString());
        }

        bw.write("</expression>\n");
    }

    private static void handleExpressionList(String s) throws Exception {
        bw.write("<expressionList>\n");
        if (s.isEmpty()) {
            bw.write("</expressionList>\n");
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

        bw.write("</expressionList>\n");
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

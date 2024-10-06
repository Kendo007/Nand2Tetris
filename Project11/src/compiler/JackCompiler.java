package compiler;

import java.io.*;

import static compiler.MyFileUtils.*;
import static compiler.SymbolTable.*;
import static compiler.Enums.*;
import static compiler.VMWriter.*;

public class JackCompiler {

    protected static PushbackReader pr;
    protected static BufferedWriter bw;
    protected static String className;

    public static void main(String[] args) {
        start(args[0]);
    }

    public static void start(String fileName) {
        try {
            pr = new PushbackReader(new FileReader(fileName));
            bw = new BufferedWriter(new FileWriter(fileName.substring(0, fileName.lastIndexOf(".")) + ".vm"));

            compileClass();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            pr.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Compiles the class
     */
    private static void compileClass() throws Exception {
        SymbolTable.startClass();

        eat("class");

        className = getUntil('{');

        compileClassVars();
    }

    /**
     * Compiles the class variables and builds the symbol table
     */
    private static void compileClassVars() throws Exception {
        while (true) {
            String s = getUntilNotWhiteSpace();

            switch (s) {
                case "static":
                    handleVars(Kind.STATIC);
                    break;
                case "field":
                    handleVars(Kind.FIELD);
                    break;

                case "}":
                    printSymbol('}');
                    return;

                default:
                    compileClassSubRoutines(s);
                    break;
            }
        }
    }

    /**
     * Handles the declaration of variables and builds the symbol table
     * @param kind The kind of variable (STATIC, FIELD, ARG, LCL)
     */
    private static void handleVars(Kind kind) throws Exception {
        String datatype = getUntilNotWhiteSpace();
        String s = getUntil(';');

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == ',') {
                define(sb.toString(), datatype, kind);
                sb.setLength(0);
            } else if (s.charAt(i) != ' ') {
                sb.append(s.charAt(i));
            }
        }

        define(sb.toString(), datatype, kind);
    }

    /**
     * Compiles the subroutine declarations
     * @param s The previous subroutine type (constructor, function, method)
     */
    private static void compileClassSubRoutines(String s) throws Exception {
        while (true) {
            switch (s) {
                case "constructor":
                case "function":
                    handleSubroutine(false);
                    break;
                case "method":
                    handleSubroutine(true);
                    break;

                case "}":
                    return;

                default:
                    throw new Exception("Syntax Error!!");
            }

            s = getUntilNotWhiteSpace();
        }
    }

    /**
     * Handles the subroutine declaration and builds the symbol table
     */
    private static void handleSubroutine(boolean isMethod) throws Exception {
        startSubroutine();

        if (isMethod) {
            define("this", className, Kind.ARG);
        }

        getUntilNotWhiteSpace();          // skipping return type as it is not needed for assignment

        String subRoutineName = getUntil('(');
        String paramList = getUntil(')');

        compileParameterList(paramList);

        eat("{");
        compileSubRoutineBody(subRoutineName);
    }

    /**
     * Compiles the parameter list of a subroutine
     * @param s The parameter list
     */
    private static void compileParameterList(String s) throws Exception {
        if (s.isEmpty()) {
            return;
        }

        String[] arr = s.split(", *");

        for (String str : arr) {
            String[] temp = str.split(" +");
            define(temp[1], temp[0], Kind.ARG);
        }
    }

    /**
     * Compiles the body of a subroutine
     * @param subRoutineName The name of the subroutine
     */
    private static void compileSubRoutineBody(String subRoutineName) throws Exception {
        while (true) {
            String s = getUntilNotWhiteSpace();

            if (s.equals("var")) {
                handleVars(Kind.LCL);
            } else {
                writeFunction(subRoutineName, varCount(Kind.LCL));

                if (!s.equals("}")) {
                    handleStatements();
                }
                return;
            }
        }
    }

    private static void handleStatements() throws Exception {
        int checker = -1;

        while (true) {
            String s = getUntilNotWhiteSpace();

            if (checker == 0 && !s.equals("else")) {
                bw.write("</ifStatement>\n");
            }

            switch (s) {
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

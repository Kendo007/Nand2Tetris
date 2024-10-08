package compiler;

import java.io.*;
import java.util.Objects;
import java.util.Stack;

import static compiler.MyFileUtils.*;
import static compiler.SymbolTable.*;
import static compiler.Enums.*;
import static compiler.VMWriter.*;

public class JackCompiler {

    private static int labelCounter = 0;
    protected static PushbackReader pr;
    protected static BufferedWriter bw;
    protected static String className;

    public static void main(String[] args) {
        File f = new File(args[0]);

        if (f.isDirectory()) {
            for (File file : Objects.requireNonNull(f.listFiles())) {
                if (file.getName().endsWith(".jack")) {
                    start(file.getAbsolutePath());
                }
            }
        } else {
            start(args[0]);
        }
    }

    public static void start(String fileName) {
        try {
            int BUFFER_SIZE = 7;
            labelCounter = 0;

            pr = new PushbackReader(new FileReader(fileName), BUFFER_SIZE);
            bw = new BufferedWriter(new FileWriter(fileName.substring(0, fileName.lastIndexOf(".")) + ".vm"));

            bw.write("// Compiled: " + fileName.substring(fileName.lastIndexOf('/') + 1));
            handleClass();
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
    private static void handleClass() throws Exception {
        startClass();

        eat("class");

        className = getUntil('{');

        handleClassVars();
    }

    /**
     * Compiles the class variables and builds the symbol table
     */
    private static void handleClassVars() throws Exception {
        while (true) {
            String s = getUntilNotWhiteSpace();

            if (s.equals("\uFFFF")) {
                return;
            }

            switch (s) {
                case "static":
                    handleVars(Kind.STATIC);
                    break;
                case "field":
                    handleVars(Kind.FIELD);
                    break;

                case "}":
                    return;

                default:
                    handleClassSubRoutines(s);
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
    private static void handleClassSubRoutines(String s) throws Exception {
        while (true) {
            switch (s) {
                case "constructor":
                    handleSubroutine(SubRoutineKind.CONSTRUCTOR);
                    break;
                case "function":
                    handleSubroutine(SubRoutineKind.FUNCTION);
                    break;
                case "method":
                    handleSubroutine(SubRoutineKind.METHOD);
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
    private static void handleSubroutine(SubRoutineKind sub) throws Exception {
        startSubroutine();

        if (sub == SubRoutineKind.METHOD) {
            define("this", className, Kind.ARG);
        }

        getUntilNotWhiteSpace();          // skipping return type as it is not needed for assignment

        String subRoutineName = getUntil('(');
        String paramList = getUntil(')');

        handleParameterList(paramList);

        eat("{");

        handleSubRoutineBody(subRoutineName, sub);
    }

    /**
     * Compiles the parameter list of a subroutine
     * @param s The parameter list
     */
    private static void handleParameterList(String s) throws Exception {
        if (s.isEmpty()) {
            return;
        }

        String[] arr = s.split(",[ \r\n]*");

        for (String str : arr) {
            String[] temp = str.split(" +");
            define(temp[1], temp[0], Kind.ARG);
        }
    }

    /**
     * Compiles the body of a subroutine
     * @param subRoutineName The name of the subroutine
     */
    private static void handleSubRoutineBody(String subRoutineName, SubRoutineKind sub) throws Exception {
        while (true) {
            String s = getUntilNotWhiteSpace();

            if (s.equals("var")) {
                handleVars(Kind.LCL);
            } else {
                writeFunction(className + '.' + subRoutineName, varCount(Kind.LCL));

                if (sub == SubRoutineKind.CONSTRUCTOR) {
                    writePush(Segment.CONST, varCount(Kind.FIELD));
                    writeCall("Memory.alloc", 1);
                    writePop(Segment.POINTER, 0);
                } else if (sub == SubRoutineKind.METHOD) {
                    writePush(Segment.ARG, 0);
                    writePop(Segment.POINTER, 0);
                }

                if (!s.equals("}")) {
                    goBack(s);
                    handleStatements();
                }
                return;
            }
        }
    }

    private static void handleStatements() throws Exception {
        int checker = -1, ifLabel = -1;

        while (true) {
            String s = getUntilNotWhiteSpace();

            if (checker == 0 && !s.equals("else")) {
                writeLabel(className + '_' + ifLabel);
            }

            switch (s) {
                case "let":
                    handleLet();
                    break;

                case "if":
                    ifLabel = handleIf();
                    checker = 1;
                    break;

                case "else":
                    if (checker != 0) {
                        throw new Exception("Syntax Error!!");
                    }

                    int elseLabel = labelCounter++;
                    writeGoto(className + '_' + elseLabel);
                    writeLabel(className + '_' + ifLabel);
                    handleElse();
                    writeLabel(className + '_' + elseLabel);
                    break;

                case "while":
                    handleWhile();
                    break;

                case "do":
                    handleDo();
                    break;

                case "return":
                    handleReturn();
                    break;

                case "}":
                    return;

                default:
                    if (s.equals("return;")) {
                        writePush(Segment.CONST, 0);
                        writeReturn();
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
        String expr = getUntil(';');

        handleExpression(expr);

        if (s.endsWith("]")) {
            int i = s.indexOf('[');
            String identifier = s.substring(0, i);

            writePush(segmentOf(identifier), indexOf(identifier));
            handleExpression(s.substring(i + 1, s.length() - 1));
            writeArithmetic(Command.ADD);

            writePop(Segment.POINTER, 1);
            writePop(Segment.THAT, 0);
        } else {
            writePop(segmentOf(s), indexOf(s));
        }
    }

    private static void handleElse() throws Exception {
        eat("{");
        handleStatements();
    }

    private static int handleIf() throws Exception {
        eat("(");

        String s = getUntil('{');
        handleExpression(s.substring(0, s.length() - 1));
        writeArithmetic(Command.NOT);

        int ifLabel = labelCounter++;
        writeIf(className + '_' + ifLabel);

        handleStatements();
        return ifLabel;
    }

    private static void handleWhile() throws Exception {
        eat("(");
        String s = getUntil('{');

        int startLabel = labelCounter++;
        int endLabel = labelCounter++;

        writeLabel(className + '_' + startLabel);
        handleExpression(s.substring(0, s.length() - 1));
        writeArithmetic(Command.NOT);

        writeIf(className + '_' + endLabel);
        handleStatements();
        writeGoto(className + '_' + startLabel);
        writeLabel(className + '_' + endLabel);
    }

    private static void handleDo() throws Exception {
        handleSubroutineCall(getUntil(';'));
        writePop(Segment.TEMP, 0);
    }

    private static void handleReturn() throws Exception {
        String s = getUntil(';');

        if (!s.isEmpty()) {
            handleExpression(s);
        }

        writeReturn();
    }

    private static boolean isOp(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '&' || c == '|' || c == '<' || c == '>' || c == '=' || c == '~';
    }

    private static void handleTerm(String s) throws Exception {
        if (s.isEmpty()) {
            return;
        }

        if (s.charAt(0) == '-' || s.charAt(0) == '~') {         // unary Op
            handleTerm(s.substring(1));
            if (s.charAt(0) == '-') {
                writeArithmetic(Command.NEG);
            } else {
                writeArithmetic(Command.NOT);
            }
        } else if (s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') {
            writePush(Segment.CONST, s.length() - 2);
            writeCall("String.new", 1);

            for (int i = 1; i < s.length() - 1; ++i) {
                writePush(Segment.CONST, s.charAt(i));
                writeCall("String.appendChar", 2);
            }
        } else if (Character.isDigit(s.charAt(0))) {                // integer constant
            writePush(Segment.CONST, Integer.parseInt(s));
        } else if (s.equals("true")) {
            writePush(Segment.CONST, 1);
            writeArithmetic(Command.NEG);
        } else if (s.equals("false") || s.equals("null")) {
            writePush(Segment.CONST, 0);
        } else if (s.equals("this")) {
            writePush(Segment.POINTER, 0);
        } else if (s.charAt(0) == '(') {                         // (expression)
            handleExpression(s.substring(1, s.length() - 1));
        } else {                        // varName | varName[expression] | subroutineCall
            int i = s.indexOf('[');

            if (i != -1) {
                String identifier = s.substring(0, i);
                handleExpression(s.substring(i + 1, s.length() - 1));

                writePush(segmentOf(identifier), indexOf(identifier));
                writeArithmetic(Command.ADD);

                writePop(Segment.POINTER, 1);
                writePush(Segment.THAT, 0);
            } else if (s.contains("(")) {
                handleSubroutineCall(s);
            } else {
                writePush(segmentOf(s), indexOf(s));
            }
        }
    }

    private static int priority(char c) {
        if (c == '(') {
            return -1;
        } else if (c == '*' || c == '/') {
            return 4;
        } else if (c == '+' || c == '-') {
            return 3;
        } else if (c == '>' || c == '<') {
            return 2;
        } else if (c == '|' || c == '&') {
            return 1;
        } else {
            return 0;
        }
    }

    private static void handleExpression(String s) throws Exception {
        Stack<Character> symbols = new Stack<>();
        StringBuilder sb = new StringBuilder();

        int n = s.length();
        boolean prevSymbol = true;

        for (int i = 0; i < n; ++i) {
            char c = s.charAt(i);

            if (Character.isWhitespace(c)) {
                continue;
            } else if (c == '"') {
                sb.append(c);
                do {
                    c = s.charAt(++i);
                    sb.append(c);

                } while (c != '"');

                handleTerm(sb.toString());
                sb.setLength(0);
            } else if (c == '(') {
                if (prevSymbol)
                    symbols.push(c);
                else {
                    int balanced = 1;

                    while (balanced != 0) {
                        sb.append(s.charAt(i++));

                        if (s.charAt(i) == '(') {
                            ++balanced;
                        } else if (s.charAt(i) == ')') {
                            --balanced;
                        }
                    }

                    sb.append(')');
                    handleTerm(sb.toString());
                    sb.setLength(0);
                }
            } else if (c == '[') {
                int balanced = 1;

                do {
                    sb.append(c);
                    c = s.charAt(++i);

                    if (c == '[') {
                        ++balanced;
                    } else if (c == ']') {
                        --balanced;
                    }
                } while (balanced != 0);

                sb.append(']');
            } else if (c == ')') {
                handleExpression(sb.toString());
                sb.setLength(0);

                while (!symbols.isEmpty()) {
                    char top = symbols.pop();

                    if (top == '(') {
                        break;
                    }

                    writeArithmetic(top);
                }
            } else if (isOp(c)) {
                if ((c == '-' || c == '~') && prevSymbol) {     // condition for not unary expression
                    sb.append(c);
                    prevSymbol = false;
                    continue;
                }

                handleTerm(sb.toString());
                sb.setLength(0);

                while (!symbols.isEmpty() && priority(symbols.peek()) >= priority(c)) {
                    writeArithmetic(symbols.pop());
                }

                symbols.push(c);
                prevSymbol = true;
            } else {
                sb.append(c);
                prevSymbol = false;
            }
        }

        if (!sb.isEmpty()) {
            handleTerm(sb.toString());
        }

        while (!symbols.isEmpty()) {
            writeArithmetic(symbols.pop());
        }
    }

    private static int handleExpressionList(String s) throws Exception {
        if (s.isEmpty()) {
            return 0;
        }

        String[] arr = s.split(",");

        for (String str : arr) {
            handleExpression(str.stripLeading());
        }

        return arr.length;
    }

    private static void handleSubroutineCall(String s) throws Exception {
        int i = s.indexOf('.');
        String subRoutineName = "";
        boolean isMethod = false;

        if (i != -1) {
            String identifier = s.substring(0, i);
            Segment segment = segmentOf(identifier);

            if (segment != null) {
                writePush(segment, indexOf(identifier));
                subRoutineName += typeOf(identifier) + '.';

                isMethod = true;
            } else {
                subRoutineName += identifier + '.';
            }
        } else {
            writePush(Segment.POINTER, 0);
            subRoutineName += className + '.';

            isMethod = true;
        }

        int j = s.indexOf('(');
        subRoutineName += s.substring(i + 1, j);

        int n = handleExpressionList(s.substring(j + 1, s.length() - 1));
        writeCall(subRoutineName, isMethod ? n + 1 : n);
    }
}

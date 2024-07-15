import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

public class CodeTranslator {
    protected static BufferedWriter bw;
    private static int boolCount = 0;
    protected static String fileName;

    private static HashMap<String, String> baseAddress = new HashMap<>();

    /** Translates VM command to hack assembly and writes them to filename.asm */
    public static void translate(String[] Command) throws IOException {
        if (Command[0].isEmpty())  { return; }

        bw.write("// ");
        for (String i : Command) {
            bw.write(i + " ");
        }
        bw.newLine();

        if (Command[0].charAt(0) == 'p') {
            memoryCommand(Command);
        } else {
            arthlogiCommand(Command);
        }
    }

    private static void memoryCommand(String[] Command) throws IOException {
        if (Command[0].equals("push")) {
            push(Command);
        } else {
            pop(Command);
        }
    }

    protected static void initialiseBaseAddress() {
        baseAddress.put("argument", "@ARG");
        baseAddress.put("local", "@LCL");
        baseAddress.put("this", "@THIS");
        baseAddress.put("that", "@THAT");
    }

    private static void getBaseAddress(String[] Command) throws IOException {
        if (baseAddress.containsKey(Command[1])) {
            write(baseAddress.get(Command[1]));
            write("D=M");
        } else {
            switch (Command[1]) {
                case "temp":
                    write("@5"); break;
                case "pointer":
                    write("@3"); break;
            }

            write("D=A");
        }
    }

    private static void pop(String[] Command) throws IOException {
        if (Command[1].equals("static")) {
            write("@SP");
            write("M=M-1");
            write("A=M");
            write("D=M");
            write("@"+fileName+"."+Command[2]);
            write("M=D");

            return;
        }

        getBaseAddress(Command);

        write("@"+Command[2]);
        write("D=D+A");
        write("@SP");
        write("M=M-1");
        write("A=M");
        write("D=D+M");    // D = addr + val
        write("A=D-M");    // A = addr + val - val
        write("M=D-A");    // RAM[addr] = addr + val - addr
    }

    private static void push(String[] Command) throws IOException {
        if (Command[1].equals("constant")) {
            write("@"+Command[2]);
            write("D=A");
        } else if (Command[1].equals("static")) {
            write("@"+fileName+"."+Command[2]);
            write("D=M");
        } else {
            getBaseAddress(Command);

            write("@"+Command[2]);
            write("A=D+A");
            write("D=M");
        }

        write("@SP");
        write("M=M+1");
        write("A=M-1");
        write("M=D");
    }

    private static void arthlogiCommand(String[] Command) throws IOException {
        if (Command[0].charAt(0) == 'n') {      // for not and neg operation
            write("@SP");
            write("A=M-1");

            if (Command[0].equals("not")) {
                write("M=!M");
            } else {
                write("M=-M");
            }
        } else {                                // for all other operations that require 2 operators
            write("@SP");
            write("M=M-1");
            write("A=M");
            write("D=M");
            write("A=A-1");

            switch (Command[0]) {
                case "add":
                    write("M=D+M"); break;
                case "sub":
                    write("M=M-D"); break;
                case "and":
                    write("M=D&M"); break;
                case "or":
                    write("M=D|M"); break;
                case "eq":
                    booleanPush("JEQ"); break;
                case "gt":
                    booleanPush("JGT"); break;
                case "lt":
                    booleanPush("JLT"); break;
            }
        }

    }

    private static void booleanPush(String jumpCondition) throws IOException {
        write("D=M-D");

        write("@SP");
        write("A=M-1");
        write("M=-1");
        write("@CONT" + boolCount);
        write("D;"+jumpCondition);

        write("@SP");
        write("A=M-1");
        write("M=0");

        write("(CONT" + boolCount + ")");
        ++boolCount;
    }

    private static void write(String s) throws IOException {
        bw.write(s);
        bw.newLine();
    }
}

import java.io.BufferedWriter;
import java.io.IOException;

public class CodeTranslator {
    protected static BufferedWriter bw;

    /** Translates VM command to hack assembly and writes them to filename.asm */
    public static void translate(String[] Command) throws IOException {
        if (Command[0].charAt(0) == 'p') {
            memoryCommand(Command);
        } else {
            arthlogiCommand(Command);
        }
    }

    private static void memoryCommand(String[] Command) {

    }

    private static void arthlogiCommand(String[] Command) throws IOException {
        if (Command[0].charAt(0) == 'n') {      // for not and neg operation
            singleElementPop();

            if (Command[0].equals("not")) {
                write("M=!M");
            } else {
                write("M=-M");
            }
        } else {                                // for all other operations that require 2 operators
            doubleElementPop();

            switch (Command[0]) {
                case "add":
                    singleElementPush("M=D+M"); break;
                case "sub":
                    singleElementPush("M=M-D"); break;
                case "and":
                    singleElementPush("M=D&M"); break;
                case "or":
                    singleElementPush("M=D|M"); break;
                case "eq":
                    booleanPush("JEQ"); break;
                case "gt":
                    booleanPush("JGT"); break;
                case "lt":
                    booleanPush("JLT"); break;
            }
        }

    }

    /** Writes hack assembly code to a file that means poping an element from the main stack */
    private static void singleElementPop() throws IOException {
        write("@SP");
        write("M=M-1");
        write("A=M");
    }

    private static void singleElementPush(String opr) throws IOException {
        write(opr);
        write("@SP");
        write("M=M+1");
    }

    /** Writes hack assembly code to a file that means poping two element from the main stack
     * where first popped element is stored in D and second is stored in M */
    private static void doubleElementPop() throws IOException {
        singleElementPop();
        write("D=M");
        singleElementPop();
    }

    private static void booleanPush(String jump) throws IOException {
        write("D=M-D");
        write("M=A");
        write("@TRUE");
        write("D;" + jump);
        write("@SP");
        write("M=M+1");
        write("A=M-1");
        write("M=0");
    }

    private static void write(String s) throws IOException {
        bw.write(s); bw.newLine();
    }
}

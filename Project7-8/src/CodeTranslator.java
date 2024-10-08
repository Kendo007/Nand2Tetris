import java.io.*;
import java.util.HashMap;
import java.util.Stack;

public class CodeTranslator {
    private static int boolCount = 0;
    private static int callCount = 0;
    protected static BufferedWriter bw;
    private String fileName;
    private static String currFunction = "Sys.init";
    private static boolean wroteReturn = false;
    private static boolean wroteCall = false;

    private final static HashMap<String, String> baseAddress = new HashMap<>();

    public void start(String fileName) {
        try {
            this.fileName = fileName.substring(fileName.lastIndexOf(File.separatorChar)+1, fileName.lastIndexOf('.'));
        } catch (IndexOutOfBoundsException e) {
            this.fileName = fileName;
        }

        try {
            String line;
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            while ((line = br.readLine()) != null) {
                translate(parseCommand(line));
            }

            br.close(); fr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Given a string returns an array of strings separating different words of a command */
    private static String[] parseCommand(String command) {
        int index = command.indexOf("//");

        if (index == -1) {
            return command.stripLeading().split("\\s+", 3);
        } else {
            return command.substring(0, index).stripLeading().split(" +", 3);
        }
    }

    /** Translates VM command to hack assembly and writes them to filename.asm */
    private void translate(String[] Command) throws IOException {
        if (Command[0].isEmpty())  { return; }

        bw.write("// ");
        for (String i : Command) {
            bw.write(i + " ");
        }
        bw.newLine();

        if (memoryCommand(Command) || branchingCommand(Command) || functionCommand(Command) || arthlogiCommand(Command)) {
            return;
        } else {
            throw new RuntimeException("Something is not right!!");
        }
    }

    /** Writes the assembly code for all the memory commands
     * @param Command Command you want to execute
     * */
    private boolean memoryCommand(String[] Command) throws IOException {
        if (Command[0].equals("push")) {
            push(Command);
        } else if (Command[0].equals("pop")) {
            pop(Command);
        } else {
            return false;
        }

        return true;
    }

    protected static void initialiseBaseAddress() {
        baseAddress.put("argument", "@ARG");
        baseAddress.put("local", "@LCL");
        baseAddress.put("this", "@THIS");
        baseAddress.put("that", "@THAT");
    }

    /** Returns the base address based on the hack specification
     * @param Command Command you want to execute
     * */
    private void getBaseAddress(String[] Command) throws IOException {
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

    /** Writes the assembly code to pop values from the stack
     * @param Command Command you want to execute
     * */
    private void pop(String[] Command) throws IOException {
        if (Command[1].equals("static")) {
            write("@SP");
            write("AM=M-1");
            write("D=M");
            write("@"+fileName+"."+Command[2]);
            write("M=D");

            return;
        }

        getBaseAddress(Command);

        write("@"+Command[2]);
        write("D=D+A");
        write("@SP");
        write("AM=M-1");
        write("D=D+M");    // D = addr + val
        write("A=D-M");    // A = addr + val - val
        write("M=D-A");    // RAM[addr] = addr + val - addr
    }

    /** Writes the assembly code for pushing values to the stack
     * @param Command Command you want to execute
     * */
    private void push(String[] Command) throws IOException {
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

    /** Writes the assembly code for all the arithmetic and logical commands
     * @param Command Command you want to execute
     * @return true/false based on if it wrote the command to the file
     * */
    private boolean arthlogiCommand(String[] Command) throws IOException {
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
            write("AM=M-1");
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
                default:
                    return false;
            }
        }

        return true;
    }

    /** Writes the assembly code required for comparing and pushing the appropriate answer to the stack 0 - false and -1 - true
     * @param jumpCondition the condition based on which the jump should be decided
     */
    private void booleanPush(String jumpCondition) throws IOException {
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

    /** Writes the assembly code for all the branching commands
     * @param Command Command you want to execute
     * @return true/false based on if it wrote the command to the file
     * */
    private boolean branchingCommand(String[] Command) throws IOException {
        switch (Command[0]) {
            case "label":
                write("(" + currFunction + "$" + Command[1] + ")");
                break;
            case "goto":
                write("@" + currFunction + "$" + Command[1]);
                write("0;JMP");
                break;
            case "if-goto":
                write("@SP");
                write("M=M-1");
                write("A=M");
                write("D=M");
                write("@" + currFunction + "$" + Command[1]);
                write("D;JNE");
                break;
            default:
                return false;
        }

        return true;
    }

    /** Writes the assembly code for all the function commands
     * @param Command Command you want to execute
     * @return true/false based on if it wrote the command to the file
     * */
    private boolean functionCommand(String[] Command) throws IOException {
        switch (Command[0]) {
            case "call":
                writeCall(Command);
                break;
            case "function":
                writeFunction(Command);
                break;
            case "return":
                write("@RETURN");
                write("0;JMP");

                if (!wroteReturn)
                    commonReturn();
                break;
            default:
                return false;
        }

        return true;
    }

    /** Writes the assembly code for calling the function and doing all the necessary manipulations */
    protected static void writeCall(String[] Command) throws IOException {
        // Storing value of new ARG in temp variable R13
        write("@" + Integer.valueOf(Command[2].strip()));
        write("D=A");
        write("@SP");
        write("D=M-D");
        write("@R13");
        write("M=D");

        // Pushing returnAddress on stack
        helperCall("@" + currFunction + "$ret." + callCount);

        // Storing the function address in temp variable R14
        write("@" + Command[1]);
        write("D=A");
        write("@R14");
        write("M=D");
        write("@FUNCTION");
        write("0;JMP");

        if (!wroteCall)
            commonCall();

        write("(" + currFunction + "$ret." + callCount + ")");
        ++callCount;
    }

    /** Writes the common assembly code (Call subroutine) */
    private static void commonCall() throws IOException {
        write("(FUNCTION)");
        helperCall("@LCL");
        helperCall("@ARG");
        helperCall("@THIS");
        helperCall("@THAT");

        // Setting Up New ARG
        write("@R13");
        write("D=M");
        write("@ARG");
        write("M=D");

        // Setting Up new LCL
        write("@SP");
        write("D=M");
        write("@LCL");
        write("M=D");

        write("@R14");
        write("A=M");
        write("0;JMP");

        wroteCall = true;
    }

    /** Assembly code for saving current Memory elements on the stack
     * @param addr The memory element you want to store
     * */
    private static void helperCall(String addr) throws IOException {
        write(addr);
        if (addr.contains(".")) {
            write("D=A");
        } else {
            write("D=M");
        }
        write("@SP");
        write("M=M+1");
        write("A=M-1");
        write("M=D");
    }

    /** Writes the assembly code for starting a function and doing all the necessary manipulations */
    private void writeFunction(String[] Command) throws IOException {
        currFunction = Command[1];
        write("(" + Command[1] + ")");

        if (!Command[2].equals("0")) {
            write("@" + Command[2]);                    // pushing 0 nVar times
            write("D=A");
            write("(PushZero" + boolCount + ")");
            write("@SP");
            write("M=M+1");
            write("A=M-1");
            write("M=0");
            write("D=D-1");
            write("@PushZero" + boolCount);
            write("D;JGT");

            ++boolCount;
        }
    }

    /** Writes the common assembly code (Return subroutine) */
    private void commonReturn() throws IOException {
        write("(RETURN)");
        write("@LCL");           // endframe = LCL
        write("D=M");
        write("@endframe");
        write("M=D");
        write("@5");            // returnArr = *(endframe - 5)
        write("D=A");
        write("@endframe");
        write("A=M-D");
        write("D=M");
        write("@retAddr");
        write("M=D");
        write("@SP");            // *ARG = pop()
        write("A=M-1");
        write("D=M");
        write("@ARG");
        write("A=M");
        write("M=D");
        write("@ARG");
        write("D=M");
        write("@SP");
        write("M=D+1");

        helperReturn("@THAT");
        helperReturn("@THIS");
        helperReturn("@ARG");
        helperReturn("@LCL");

        write("@retAddr");      // goto retAddr
        write("A=M");
        write("0;JMP");

        wroteReturn = true;
    }

    /** Writing code that will copy the saved memory elements to their original places
     * @param addr the memory element you want to restore
     * */
    private void helperReturn(String addr) throws IOException {
        write("@endframe");
        write("AM=M-1");
        write("D=M");
        write(addr);
        write("M=D");
    }

    protected static void write(String s) throws IOException {
        bw.write(s);
        bw.newLine();
    }
}

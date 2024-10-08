package compiler;

import static compiler.JackCompiler.bw;
import static compiler.Enums.Segment;
import static compiler.Enums.Command;

public class VMWriter {
    protected static void writePush(Segment segment, int index) throws Exception {
        bw.write("\tpush " + segment + " " + index);
        bw.newLine();
    }

    protected static void writePop(Segment segment, int index) throws Exception {
        bw.write("\tpop " + segment + " " + index);
        bw.newLine();
    }

    protected static void writeArithmetic(Command command) throws Exception {
        bw.write('\t');
        bw.write(command.toString());
        bw.newLine();
    }

    protected static void writeArithmetic(char c) throws Exception {
        bw.write("\t");
        switch (c) {
            case '+':
                bw.write("add");
                break;
            case '-':
                bw.write("sub");
                break;
            case '*':
                bw.write("call Math.multiply 2");
                break;
            case '/':
                bw.write("call Math.divide 2");
                break;
            case '&':
                bw.write("and");
                break;
            case '|':
                bw.write("or");
                break;
            case '<':
                bw.write("lt");
                break;
            case '>':
                bw.write("gt");
                break;
            case '=':
                bw.write("eq");
                break;
            case '~':
                bw.write("not");
                break;
            default:
                throw new Exception("Invalid arithmetic command");
        }

        bw.newLine();
    }

    protected static void writeLabel(String label) throws Exception {
        bw.write("label " + label);
        bw.newLine();
    }

    protected static void writeGoto(String label) throws Exception {
        bw.write("\tgoto " + label);
        bw.newLine();
    }

    protected static void writeIf(String label) throws Exception {
        bw.write("\tif-goto " + label);
        bw.newLine();
    }

    protected static void writeCall(String name, int nArgs) throws Exception {
        bw.write("\tcall " + name + " " + nArgs);
        bw.newLine();
    }

    protected static void writeFunction(String name, int nLocals) throws Exception {
        bw.write("\nfunction " + name + " " + nLocals);
        bw.newLine();
    }

    protected static void writeReturn() throws Exception {
        bw.write("\treturn");
        bw.newLine();
    }
}

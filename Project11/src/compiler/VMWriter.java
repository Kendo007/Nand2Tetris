package compiler;

import static compiler.JackCompiler.bw;
import static compiler.Enums.Segment;
import static compiler.Enums.Command;

public class VMWriter {
    protected static void writePush(Segment segment, int index) throws Exception {
        bw.write("push" + segment + " " + index);
        bw.newLine();
    }

    protected static void writePop(Segment segment, int index) throws Exception {
        bw.write("pop" + segment + " " + index);
        bw.newLine();
    }

    protected static void writeArithmetic(Command command) throws Exception {
        bw.write(command.toString());
        bw.newLine();
    }

    protected static void writeLabel(String label) throws Exception {
        bw.write("label " + label);
        bw.newLine();
    }

    protected static void writeGoto(String label) throws Exception {
        bw.write("goto " + label);
        bw.newLine();
    }

    protected static void writeIf(String label) throws Exception {
        bw.write("if-goto " + label);
        bw.newLine();
    }

    protected static void writeCall(String name, int nArgs) throws Exception {
        bw.write("call " + name + " " + nArgs);
        bw.newLine();
    }

    protected static void writeFunction(String name, int nLocals) throws Exception {
        bw.write("function " + name + " " + nLocals);
        bw.newLine();
    }

    protected static void writeReturn() throws Exception {
        bw.write("return");
        bw.newLine();
    }
}

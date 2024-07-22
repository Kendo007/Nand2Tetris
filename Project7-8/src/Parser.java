import java.io.*;

public class Parser {
    /** Reads VM commands line by line and sends it to codewriter for translation and writing */
    public static void start(String file) throws IOException {
        CodeTranslator.initialiseBaseAddress();

        File f = new File(file);

        if (f.isDirectory()) {
            FileWriter fw = new FileWriter(file + ".asm");
            CodeTranslator.bw = new BufferedWriter(fw);
            writeInit();

            for (String fileName : f.list()) {
                CodeTranslator c = new CodeTranslator();

                if (fileName.endsWith(".vm")) {
                    c.start(file + File.separatorChar + fileName);
                }
            }

            CodeTranslator.bw.close(); fw.close();
        } else {
            FileWriter fw = new FileWriter(file.substring(0, file.lastIndexOf(".")) + ".asm");
            CodeTranslator.bw = new BufferedWriter(fw);

            CodeTranslator c = new CodeTranslator();
            c.start(file);

            CodeTranslator.bw.close(); fw.close();
        }

    }

    private static void writeInit() throws IOException {
        CodeTranslator.write("@256");
        CodeTranslator.write("D=A");
        CodeTranslator.write("@SP");
        CodeTranslator.write("M=D");
        CodeTranslator.writeCall(new String[]{"call", "Sys.init", "0"});
    }
}

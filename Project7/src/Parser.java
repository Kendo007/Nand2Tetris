import java.io.*;

public class Parser {
    /** Reads VM commands line by line and sends it to codewriter for translation and writing */
    public static void start(String fileName) {
        CodeTranslator.initialiseBaseAddress();
        CodeTranslator.fileName = fileName.substring(fileName.lastIndexOf(File.separatorChar)+1, fileName.lastIndexOf('.'));

        try {
            String line;
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            FileWriter fw = new FileWriter(fileName.substring(0, fileName.lastIndexOf(".")) + ".asm");
            CodeTranslator.bw = new BufferedWriter(fw);

            while ((line = br.readLine()) != null) {
                CodeTranslator.translate(parseCommand(line));
            }

            br.close(); fr.close();
            CodeTranslator.bw.close(); fw.close();

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
}

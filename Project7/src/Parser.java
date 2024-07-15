import java.io.*;

public class Parser {
    /** Reads VM commands line by line and sends it to codewriter for translation and writing */
    public static void start(String fileName) {
        try {
            String line;
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            FileWriter fw = new FileWriter(fileName.substring(0, fileName.lastIndexOf(".")) + ".asm");
            CodeTranslator.bw = new BufferedWriter(fw);

            while ((line = br.readLine()) != null) {
                CodeTranslator.bw.write("//" + line);
                CodeTranslator.bw.newLine();

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
        return command.stripLeading().split("\\s+", 3);

    }
}

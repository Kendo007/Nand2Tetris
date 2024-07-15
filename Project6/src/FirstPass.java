import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FirstPass {
    protected static HashMap<String, String> symbolTable = new HashMap<>();

    /** Converts the given integer into 16-bit binary string */
    protected static String intToBinary(int n) {
        return String.format("%16s", Integer.toBinaryString(n)).replace(' ', '0');
    }

    /** Puts all the default values in symbol table */
    private static void initialiseTable() {
        for (int i = 0; i < 16; ++i) {
            symbolTable.put("R" + i, intToBinary(i));
        }

        symbolTable.put("SCREEN", intToBinary(16384));
        symbolTable.put("KBD", intToBinary(24576));
        symbolTable.put("SP", intToBinary(0));
        symbolTable.put("LCL", intToBinary(1));
        symbolTable.put("ARG", intToBinary(2));
        symbolTable.put("THIS", intToBinary(3));
        symbolTable.put("THAT", intToBinary(4));
    }

    /** puts all the values in the symbol table during first pass */
    public static void start(String fileName) {
        int count = 0;
        String line;

        initialiseTable();

        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            while ((line = br.readLine()) != null) {
                line = line.stripLeading();

                if (line.isBlank() || line.startsWith("//")) {
                    continue;
                } else if (line.startsWith("(")) {
                    symbolTable.put(line.substring(1, line.indexOf(")")), intToBinary(count--));
                }

                ++count;
            }

            br.close(); fr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

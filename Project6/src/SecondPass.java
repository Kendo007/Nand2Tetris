import java.io.*;
import java.util.HashMap;

public class SecondPass {
    private static int freeMemory = 16;
    private static final HashMap<String, String> jumpTable = new HashMap<>();
    private static final HashMap<String, String> compTable = new HashMap<>();

    public static void start(String fileName) {
        int pos;
        String inst, assembledInst;

        createJumpTable();
        createCompTable();

        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            FileWriter fw = new FileWriter(fileName.substring(0, fileName.indexOf(".") + 1) + "hack");
            BufferedWriter bw = new BufferedWriter(fw);

            while ((inst = br.readLine()) != null) {
                pos = inst.indexOf("//");

                if (pos == -1) {
                    assembledInst = handleInstruction(inst.strip());
                } else {
                    assembledInst = handleInstruction(inst.substring(0, pos).strip());
                }

                if (!assembledInst.isEmpty()) {
                    bw.write(assembledInst);
                    bw.newLine();
                }
            }

            br.close(); fr.close();
            bw.close(); fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** converts an Instruction into its equivalent machine code */
    private static String handleInstruction(String inst) {
        if (inst.isBlank() || inst.startsWith("(")) {
            return "";
        } else if (inst.startsWith("@")) {
            return handleAInstruction(inst);
        } else {
            return handleCInstruction(inst);
        }
    }

    private static String handleAInstruction(String inst) {
        inst = inst.substring(1);

        try {
            return FirstPass.intToBinary(Integer.parseInt(inst));
        }
        catch (NumberFormatException e) {
            if (FirstPass.symbolTable.containsKey(inst)) {
                return FirstPass.symbolTable.get(inst);
            } else {
                FirstPass.symbolTable.put(inst, FirstPass.intToBinary(freeMemory++));
                return FirstPass.symbolTable.get(inst);
            }
        }
    }

    private static String handleCInstruction(String inst) {
        int equalTOPos = inst.indexOf('=');
        int semiColonPos = inst.indexOf(';');

        return "111" + handleComp(inst, equalTOPos, semiColonPos)
                     + handleDestination(inst, equalTOPos)
                     + handleJump(inst, semiColonPos);
    }

    private static String handleComp(String inst, int equalToPos, int semiColonPos) {
        if (semiColonPos == -1) {
            inst = inst.substring(equalToPos+1);
        } else {
            inst = inst.substring(equalToPos+1, semiColonPos);
        }

        return compTable.get(inst);
    }

    private static String handleDestination(String inst, int equalToPos) {
        if (equalToPos == -1) {
            inst = "";
        } else {
            inst = inst.substring(0, equalToPos);
        }

        char d1 = inst.contains("A") ? '1' : '0';
        char d2 = inst.contains("D") ? '1' : '0';
        char d3 = inst.contains("M") ? '1' : '0';

        return "" + d1 + d2 + d3;
    }

    private static String handleJump(String inst, int semiColonPos) {
        if (semiColonPos == -1) {
            return "000";
        }

        return jumpTable.get(inst.substring(semiColonPos + 1));
    }

    private static void createCompTable() {
        compTable.put("0", "0101010");
        compTable.put("1", "0111111");
        compTable.put("-1", "0111010");
        compTable.put("D", "0001100");
        compTable.put("A", "0110000");
        compTable.put("M", "1110000");
        compTable.put("!D", "0001101");
        compTable.put("!A", "0110001");
        compTable.put("!M", "1110001");
        compTable.put("-D", "0001111");
        compTable.put("-A", "0110011");
        compTable.put("-M", "1110011");
        compTable.put("D+1", "0011111");
        compTable.put("A+1", "0110111");
        compTable.put("M+1", "1110111");
        compTable.put("D-1", "0001110");
        compTable.put("A-1", "0110010");
        compTable.put("M-1", "1110010");
        compTable.put("D+A", "0000010");
        compTable.put("D+M", "1000010");
        compTable.put("D-A", "0010011");
        compTable.put("D-M", "1010011");
        compTable.put("A-D", "0000111");
        compTable.put("M-D", "1000111");
        compTable.put("D&A", "0000000");
        compTable.put("D&M", "1000000");
        compTable.put("D|A", "0010101");
        compTable.put("D|M", "1010101");
    }

    private static void createJumpTable() {
        jumpTable.put("JGT", "001");
        jumpTable.put("JEQ", "010");
        jumpTable.put("JGE", "011");
        jumpTable.put("JLT", "100");
        jumpTable.put("JNE", "101");
        jumpTable.put("JLE", "110");
        jumpTable.put("JMP", "111");
    }
}

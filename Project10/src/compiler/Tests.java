package compiler;

public class Tests {
    public static void main(String[] args) throws Exception {

        try {
            JackTokenizer.start("/home/cu001/Desktop/Nand2Tetris/Project10/JackFiles/Square/SquareGame.jack");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        JackTokenizer.br.close();
        JackTokenizer.bw.close();
    }
}

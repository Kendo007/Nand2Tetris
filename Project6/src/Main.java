//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String fileName = "/home/cu001/Desktop/Nand2Tetris/Project6/HackAssemblyFiles/Pong.asm";
        long startTime = System.currentTimeMillis();

        FirstPass.start(fileName);
        SecondPass.start(fileName);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("Execution time: " + duration + " milliseconds");
    }
}
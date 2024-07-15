//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String fileName = "K:\\Coding\\Nand2Tetris\\Part 1\\HackAssembler\\src\\Pong.asm";

        FirstPass.start(fileName);
        SecondPass.start(fileName);
    }
}
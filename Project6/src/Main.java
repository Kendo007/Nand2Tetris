//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String fileName = "/home/cu001/Desktop/Nand2Tetris/Project6/HackAssemblyFiles/Rect.asm";

        FirstPass.start(fileName);
        SecondPass.start(fileName);
    }
}
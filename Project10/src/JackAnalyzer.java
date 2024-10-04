import compiler.JackTokenizer;

import java.io.File;
import java.util.Objects;

public class JackAnalyzer {
    public static void main(String[] args) {
        File f = new File(args[0]);

        if (f.isDirectory()) {
            for (File file : Objects.requireNonNull(f.listFiles())) {
                if (file.getName().endsWith(".jack")) {
                    JackTokenizer.start(file.getAbsolutePath());
                }
            }
        } else {
            JackTokenizer.start(args[0]);
        }
    }
}
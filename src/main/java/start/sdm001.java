package start;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class sdm001 {


    public static void main(String[] args) {
        int line = 9;
        String inputString = "b";
        String str = String.format("require(%s != 0,'error');", inputString);
        Path path = Paths.get("D:\\DectionContract\\SolidityDetection-main\\test\\over.txt");
        List<String> lines;
        try {
            lines = Files.readAllLines(path);
            lines.add(line - 1, str);
            Files.write(path, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

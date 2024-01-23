package start;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class safeMath {
    public static void main(String[] args) {
        File folder = new File("D:\\DectionContract\\SolidityDetection_fix\\test\\contracts\\Tips\\"); // 替换为你的文件夹路径
        File[] listOfFiles = folder.listFiles();
        String outputPath = "exc.txt"; // 输出的批处理文件的路径

        try (FileWriter writer = new FileWriter(outputPath)) {
            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    if (file.isFile() && file.getName().endsWith(".sol")) {
                        String command = generateCommand(file.getName());
                        writer.write(command + "\n");
                    }
                }
            } else {
                System.out.println("指定的目录不存在或不是一个目录");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateCommand(String fileName) {
        return "java -jar SolidityDetection-1.0-jar-with-dependencies.jar -p test/Example/Tips/"+fileName+" -r solidity-rules.xml";
    }
}

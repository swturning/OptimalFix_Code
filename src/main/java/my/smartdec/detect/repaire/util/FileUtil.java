package my.smartdec.detect.repaire.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static void RemoveEmptyLinesAndComments(String filename){
      //  String f = PropertiesUtil.contractsFile+filename;

        String f = PropertiesUtil.fileDe;
        Path file = Paths.get(f);

        try {
            List<String> lines = Files.readAllLines(file);
            List<String> filteredLines = new ArrayList<>();

            for (String line : lines) {
                // 去除空行和以双斜杠（//）开始的单行注释
                if (!line.trim().isEmpty() && !line.trim().startsWith("//")&& !line.trim().startsWith("/*")&& !line.trim().startsWith("*")) {
                    filteredLines.add(line);
                }
            }

            // 输出去除空行和注释后的文本
//            for (String filteredLine : filteredLines) {
//                System.out.println(filteredLine);
//            }
            Files.write(file, filteredLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("can not read file：" + e.getMessage());
        }
    }


    public static void RemoveEmptyLinesAndCommentsWithFixed(String filename){
//        String f = PropertiesUtil.rootPath+"test\\Patched\\"+filename;

        String f = "test/Patched/"+filename;

        Path file = Paths.get(f);

        try {
            List<String> lines = Files.readAllLines(file);
            List<String> filteredLines = new ArrayList<>();

            for (String line : lines) {
                // 去除空行和以双斜杠（//）开始的单行注释
                if (!line.trim().isEmpty() && !line.trim().startsWith("//")&& !line.trim().startsWith("/*")&& !line.trim().startsWith("*")) {
                    filteredLines.add(line);
                }
            }

            // 输出去除空行和注释后的文本
//            for (String filteredLine : filteredLines) {
//                System.out.println(filteredLine);
//            }
            Files.write(file, filteredLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("cant read file：" + e.getMessage());
        }
    }

    public static void readFileContent(String filePath) {
        try {
            // 读取文件内容到一个字符串并返回
            String context =  new String(Files.readAllBytes(Paths.get(filePath)));
            System.out.println(context);
        } catch (IOException e) {
            // 返回错误消息
            System.out.println("File read error");
        }
    }

    public static void appendToFile(String filePath, String data) {
        Path path = Paths.get(filePath);

        try {
            // 检查文件是否存在，如果不存在则创建新文件
            if (Files.notExists(path)) {
                Files.createFile(path);
            }

            // 使用BufferedWriter写入数据
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
                writer.write(data);
                writer.newLine(); // 确保每次写入后换行
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

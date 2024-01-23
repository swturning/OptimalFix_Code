package start;

import java.io.File;

public class file {
    public static void main(String[] args) {
        String directoryPath = "C:\\Users\\paine\\Desktop\\对比试验\\TSE\\Mine";
        File directory = new File(directoryPath);

        // 检查目标路径是否是一个文件夹
        if (directory.isDirectory()) {
            listFilesInDirectory(directory);
        } else {
            System.out.println("指定的路径不是一个文件夹。");
        }
    }

    public static void listFilesInDirectory(File directory) {
        // 获取文件夹中的所有文件和子文件夹
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 如果是子文件夹，递归调用listFilesInDirectory
                    //listFilesInDirectory(file);
                } else {
                    // 如果是文件，打印文件路径或者进行其他操作
                    String fileName = file.getName();
                    int lastDotIndex = fileName.lastIndexOf(".");
                    String nameWithoutExtension = fileName.substring(0, lastDotIndex);
                    String extension = fileName.substring(lastDotIndex);
                    String newFileName = nameWithoutExtension + "_fixed" + extension;
//                    File newFile = new File("C:\\Users\\paine\\Desktop\\对比试验\\slightContract", newFileName);
//                    try {
//                        Files.write(newFile.toPath(), new byte[0], StandardOpenOption.CREATE);
//                        System.out.println("文件重命名并移动成功，新文件路径为：" + newFile.getAbsolutePath());
//                    } catch (IOException e) {
//                        System.err.println("文件重命名并移动失败: " + e.getMessage());
//                    }
                    System.out.println(fileName);
                }
            }
        }
    }

}

package my.smartdec.detect.repaire.util;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProjectUtil {
    public static Integer i1 = null ;
    public static Integer i2 = null;
    public static Integer i3 = null;

    //windows
    public static  void prettier(String fileName) {
        String workingDir = System.getProperty("user.dir");


        try {
            // 指定工作目录
           // String workingDirectory = "D:\\DectionContract\\SolidityDetection-main\\test";

//            String cmdCommand = "npx prettier --write --plugin=prettier-plugin-solidity D:\\DectionContract\\SolidityDetection-main\\"+fileName;
            String cmdCommand = "npx prettier --write --plugin=prettier-plugin-solidity "+"."+PropertiesUtil.nonTestFile;
            // 调用CMD命令
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", cmdCommand);

            //linux版调用
           // ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "/c", cmdCommand);


            // 设置工作目录
           // processBuilder.directory(new File(PropertiesUtil.contractsFile));
            processBuilder.directory(new File(workingDir+"/test"));
            // 将错误输出流与标准输出流合并
            processBuilder.redirectErrorStream(true);

            // 启动进程
            Process process = processBuilder.start();

            // 获取命令输出结果
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
            String line;


            // 等待命令执行完成
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

//    public static  void prettier(String fileName) {
//        String workingDir = System.getProperty("user.dir");
//        //System.out.println(workingDir);
//        Scanner input = null;
//        try {
//            // 指定工作目录
//            // String workingDirectory = "D:\\DectionContract\\SolidityDetection-main\\test";
//
////            String cmdCommand = "npx prettier --write --plugin=prettier-plugin-solidity D:\\DectionContract\\SolidityDetection-main\\"+fileName;
//            String cmdCommand = "npx prettier --write --plugin=prettier-plugin-solidity "+"."+PropertiesUtil.nonTestFile;
//            StringBuilder result = new StringBuilder(cmdCommand + "\n"); // 加上命令本身
//            Process process = null;
//            try {
//                process = Runtime.getRuntime().exec(cmdCommand, null, new File(workingDir + "/test"));
//                try {
//                    //等待命令执行完成
//                    process.waitFor(10, TimeUnit.SECONDS);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                InputStream is = process.getInputStream();
//                input = new Scanner(is);
//                while (input.hasNextLine()) {
//                    result.append(input.nextLine() + "\n");
//                }
//                int i = extractTimeInMilliseconds(String.valueOf(result));
//                if (i1==null){
//                    i1 = i;
//                }else if (i2==null){
//                    i2 = i;
//                }else if(i3==null){
//                    i3 = i;
//                }
//            } finally {
//                if (input != null) {
//                    input.close();
//                }
//                if (process != null) {
//                    process.destroy();
//                }
//            }
//
//
//            // 获取命令输出结果
//
//
//            // 等待命令执行完成
//            process.waitFor();
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static  void prettierFixed(String fileName) {
//        String workingDir = System.getProperty("user.dir");
//
//        try {
//            // 指定工作目录
//            // String workingDirectory = "D:\\DectionContract\\SolidityDetection-main\\test";
//
////            String cmdCommand = "npx prettier --write --plugin=prettier-plugin-solidity D:\\DectionContract\\SolidityDetection-main\\"+fileName;
//            //  String cmdCommand = "npx prettier --write --plugin=prettier-plugin-solidity "+PropertiesUtil.rootPath+"test\\Patched\\"+fileName;
//            String cmdCommand = "npx prettier --write --plugin=prettier-plugin-solidity "+"./Patched/"+fileName;
//            StringBuilder result = new StringBuilder(cmdCommand + "\n"); // 加上命令本身
//            Scanner input = null;
//            Process process = null;
//            try {
//                process = Runtime.getRuntime().exec(cmdCommand, null, new File(workingDir + "/test"));
//                try {
//                    //等待命令执行完成
//                    process.waitFor(10, TimeUnit.SECONDS);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                InputStream is = process.getInputStream();
//                input = new Scanner(is);
//                while (input.hasNextLine()) {
//                    result.append(input.nextLine() + "\n");
//                }
//                int i = extractTimeInMilliseconds(String.valueOf(result));
//                if (i1==null){
//                    i1 = i;
//                }else if (i2==null){
//                    i2 = i;
//                }else if(i3==null){
//                    i3 = i;
//                }
//
//            } finally {
//                if (input != null) {
//                    input.close();
//                }
//                if (process != null) {
//                    process.destroy();
//                }
//            }
//            // 等待命令执行完成
//            process.waitFor();
//        } catch (IOException | InterruptedException e) {
//            System.out.println("the exe error");
//            e.printStackTrace();
//        }
//    }




//
//    //windows
    public static  void prettierFixed(String fileName) {
        String workingDir = System.getProperty("user.dir");

        try {
            // 指定工作目录
            // String workingDirectory = "D:\\DectionContract\\SolidityDetection-main\\test";

//            String cmdCommand = "npx prettier --write --plugin=prettier-plugin-solidity D:\\DectionContract\\SolidityDetection-main\\"+fileName;
          //  String cmdCommand = "npx prettier --write --plugin=prettier-plugin-solidity "+PropertiesUtil.rootPath+"test\\Patched\\"+fileName;
            String cmdCommand = "npx prettier --write --plugin=prettier-plugin-solidity "+"./Patched/"+fileName;
            // 调用CMD命令
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", cmdCommand);

            // 设置工作目录
           // processBuilder.directory(new File(PropertiesUtil.contractsFile));
            processBuilder.directory(new File( workingDir+"/test"));
            // 将错误输出流与标准输出流合并
            processBuilder.redirectErrorStream(true);

            // 启动进程
            Process process = processBuilder.start();

            // 获取命令输出结果
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
            String line;


            // 等待命令执行完成
            process.waitFor();
        } catch (IOException | InterruptedException e) {

            e.printStackTrace();
        }
    }


    private static int extractTimeInMilliseconds(String output) {
        Pattern pattern = Pattern.compile("(\\d+)ms");
        Matcher matcher = pattern.matcher(output);
        if (matcher.find()) {
            String timeString = matcher.group(1);
            // Group 1 is the first set of parentheses in the regex

                return Integer.parseInt(timeString);

        }
        return 0; // Return -1 or any other error value if the pattern is not found
    }

}

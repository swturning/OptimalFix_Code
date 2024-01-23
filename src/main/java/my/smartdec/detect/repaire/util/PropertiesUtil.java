package my.smartdec.detect.repaire.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    public static String rootPath;
    public static String contractsFile;

    //带有 sol的合约名
    public static String contractFile;
    //不带.sol的合约名
    public static String contractName;

    //带sol
    public static String fixedFileName;

    public static String fileDe;

    public static String nonTestFile;

    public static void init(String fileName){
        String[] parts = fileName.split("/");
        fileDe = fileName;

        String modifiedString = fileName.replaceFirst("^test", "");
        nonTestFile = modifiedString;
        // 获取最后一个部分，即文件名
        contractFile = parts[parts.length - 1];
        String[] dex = contractFile.split("\\.");
        contractName = dex[0];

        fixedFileName = contractName+"fixed.sol";



        rootPath = "D:\\DectionContract\\SolidityDetection_fix\\";
        contractsFile =  "";
        if(parts.length>2){
            for (int i = 0;i<parts.length-1;i++){
                contractsFile +=parts[i]+"/";
            }
        }

//        Properties properties = new Properties();
//        try (FileInputStream input = new FileInputStream("config.properties")) {
//            properties.load(input);
//
//            // 读取配置信息
//             rootPath = properties.getProperty("rootPath");
//             contractsFile = properties.getProperty("contractsPath");
//             if(parts.length>2){
//                 for (int i = 1;i<parts.length-1;i++){
//                     contractsFile +=parts[i]+"\\";
//                 }
//             }
//
//
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

}

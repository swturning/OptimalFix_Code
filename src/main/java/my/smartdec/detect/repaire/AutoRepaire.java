package my.smartdec.detect.repaire;

import my.smartdec.detect.repaire.entity.RepaireDetail;
import my.smartdec.detect.repaire.entity.RepaireResult;
import my.smartdec.detect.repaire.util.*;
import org.w3c.dom.Node;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Properties;

public class AutoRepaire {


    public void autoRepaire(List<RepaireDetail> result, Node sourceNode) throws IOException {

        int offset = 0;
        Path file = null;
        Path newFile = null;

//        File fi= new File(PropertiesUtil.rootPath+"test\\Patched\\"+PropertiesUtil.fixedFileName);
        File fi= new File("test/Patched/"+PropertiesUtil.fixedFileName);
        if (fi.exists()) {
            if (fi.delete()) {
                System.out.println("ok");
            } else {
                System.out.println("error");
            }
        }

        //先将重抽象语法树写入文件
        if (NodeCopyUtil.isTreeRepaire){
            //StringUtil.WriteFile(PropertiesUtil.rootPath+"test\\Patched\\"+PropertiesUtil.fixedFileName, StringUtil.removelastEOF(NodeUtil.getNodeTextContent(NodeCopyUtil.sourceNode)));
            StringUtil.WriteFile("test/Patched/"+PropertiesUtil.fixedFileName, StringUtil.removelastEOF(NodeUtil.getNodeTextContent(NodeCopyUtil.sourceNode)));
            ProjectUtil.prettierFixed(PropertiesUtil.fixedFileName);

            //file = Paths.get(PropertiesUtil.rootPath+"test\\Patched\\"+PropertiesUtil.fixedFileName);

            file = Paths.get("test/Patched/"+PropertiesUtil.fixedFileName);
            FileUtil.RemoveEmptyLinesAndCommentsWithFixed(PropertiesUtil.fixedFileName);
        }else{
           // file = Paths.get(PropertiesUtil.contractsFile+PropertiesUtil.contractFile);
            file = Paths.get(PropertiesUtil.fileDe);
  //          newFile = Paths.get(PropertiesUtil.rootPath+"test\\Patched\\"+PropertiesUtil.fixedFileName);
            newFile = Paths.get("test/Patched/"+PropertiesUtil.fixedFileName);

        }


//
        List<String> lines = Files.readAllLines(file);
        for (RepaireDetail detail: result){
            if (detail.getType().equals("add")){
                lines.add(detail.getLine()+offset,detail.getNewContext());
                offset+=detail.getAddLine();

            }else if (detail.getType().equals("update")){
                lines.set(detail.getLine()+offset,detail.getNewContext());

            }else if(detail.getType().equals("delete")){
                lines.set(detail.getLine()+offset," ");

            }else if (detail.getType().equals("virtual")){
                offset+=detail.getAddLine();
            }
        }
        if (newFile!=null){
            Files.write(newFile, lines, StandardOpenOption.CREATE);
        }else {
            Files.write(file, lines);
        }


        ProjectUtil.prettierFixed(PropertiesUtil.fixedFileName);

    }
}

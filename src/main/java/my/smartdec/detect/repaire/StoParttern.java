package my.smartdec.detect.repaire;

import my.smartdec.detect.repaire.entity.ErrorReport;
import my.smartdec.detect.repaire.entity.RepaireDetail;
import my.smartdec.detect.repaire.util.PropertiesUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StoParttern {
    public ErrorReport errorReport;

    public StoParttern(ErrorReport errorReport){
        this.errorReport =errorReport;
    }
    public List<RepaireDetail> sto001() throws IOException {
       // String fileName = PropertiesUtil.contractsFile +PropertiesUtil.contractFile;
        String fileName = PropertiesUtil.fileDe;
        Path file = Paths.get(fileName);
        List<String> lines = Files.readAllLines(file);
        int line = errorReport.getStartLine();
        String lineContext =lines.get(line - 1);
        List<RepaireDetail> repaireDetails = new ArrayList<>();

        String newCode = lineContext.replace("tx.origin" ,"msg.sender" );

        RepaireDetail rd = new RepaireDetail("update",line - 1,errorReport.getContext(),newCode);
        repaireDetails.add(rd);


        return repaireDetails;
    }


}

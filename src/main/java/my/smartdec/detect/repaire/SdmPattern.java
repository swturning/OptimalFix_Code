package my.smartdec.detect.repaire;

import my.smartdec.detect.app.cli.DetectionOne;
import my.smartdec.detect.repaire.entity.ErrorReport;
import my.smartdec.detect.repaire.entity.RepaireDetail;
import my.smartdec.detect.repaire.entity.RepaireResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SdmPattern {
    public ErrorReport errorReport;


    public SdmPattern(ErrorReport errorReport){
        this.errorReport =errorReport;
    }

    public List<RepaireDetail> sdm001Repaire(){
        List<RepaireDetail> repaireDetails = new ArrayList<>();

        if (DetectionOne.IntergerVul >5){






        }else {

            int line = errorReport.getStartLine();
            String inputString = errorReport.getContext();
            String str = String.format("require(%s != 0, \"Integerover error\");", inputString);

            RepaireDetail rd = new RepaireDetail("add",line - 1,errorReport.getContext(),str);
            repaireDetails.add(rd);



        }




        return repaireDetails;



    }

}

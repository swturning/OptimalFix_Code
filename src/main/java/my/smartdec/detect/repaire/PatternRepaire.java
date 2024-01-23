package my.smartdec.detect.repaire;

import my.smartdec.detect.repaire.entity.ErrorReport;
import my.smartdec.detect.repaire.entity.RepaireDetail;
import my.smartdec.detect.repaire.entity.RepaireResult;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class PatternRepaire {
    //表明当前的修复过程中是否已经 加入了SafeMath
    boolean isSafeMath = false;

    //表明当前添加了几次互斥锁
    Integer reMute = 1;


    //type 表示的是修复方式
    public  List<RepaireDetail> getRepaireResult(ErrorReport errorReport) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
        switch (errorReport.getPattern()){
            case "ree001" :{
                ReePattern reePattern = new ReePattern(errorReport,reMute);
                List<RepaireDetail> repaireDetails = reePattern.writeBefore(reMute);
                reMute++;
                return repaireDetails;

            }
            case "ree002":{
                ReePattern reePattern = new ReePattern(errorReport,reMute);
                List<RepaireDetail> repaireDetails = reePattern.ree0003Repaire(reMute);
                reMute++;
                return repaireDetails;
            }
            case "ree003" :{
                ReePattern reePattern = new ReePattern(errorReport,reMute);
                List<RepaireDetail> repaireDetails = reePattern.ree0003Repaire(reMute);
                reMute++;
                return repaireDetails;

            }
            case "sdm001":{
                SdmPattern sd = new SdmPattern(errorReport);
                return sd.sdm001Repaire();

            }
            case "SIO001":{
                SIOParttern SIO = new SIOParttern(errorReport,isSafeMath);
                List<RepaireDetail> repaireDetails = SIO.SIO001F();
                if (!this.isSafeMath){
                    this.isSafeMath = SIO.isSafeMath;
                }
                return repaireDetails;
            }
            case "SIO002":{
                SIOParttern SIO = new SIOParttern(errorReport,isSafeMath);
                List<RepaireDetail> repaireDetails = SIO.SIO002F();
                if (!this.isSafeMath){
                    this.isSafeMath = SIO.isSafeMath;
                }
                return repaireDetails;
            }
            case "SIO003":{
                SIOParttern SIO = new SIOParttern(errorReport,isSafeMath);
                List<RepaireDetail> repaireDetails = SIO.SIO003();
                if (!this.isSafeMath){
                    this.isSafeMath = SIO.isSafeMath;
                }
                return repaireDetails;
            }
            case "SIO004":{
                SIOParttern SIO = new SIOParttern(errorReport,isSafeMath);
                List<RepaireDetail> repaireDetails = SIO.SIO004F();
                if (!this.isSafeMath){
                    this.isSafeMath = SIO.isSafeMath;
                }
                return repaireDetails;
            }
            case "sto001":{
                StoParttern sto = new StoParttern(errorReport);
                return sto.sto001();

            }
            case "sto002":{
                StoParttern sto = new StoParttern(errorReport);
                return sto.sto001();

            }
            case "suc001":{
                SucPattern suc = new SucPattern(errorReport);
                return suc.suc001();


            }
            case "suc003":{
                SucPattern suc =new SucPattern(errorReport);
                List<RepaireDetail> repaireDetails = suc.suc003();
                return repaireDetails;
            }
            case "nac001":{
                StoParttern sto = new StoParttern(errorReport);
                break;
            }
            case "sus001":{
                SusPattern susPattern = new SusPattern(errorReport);
                return susPattern.sus001Repaire();
            }


        }
        return null;

    }




}

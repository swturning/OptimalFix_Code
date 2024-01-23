package my.smartdec.detect.repaire;

import my.smartdec.detect.repaire.entity.ErrorReport;
import my.smartdec.detect.repaire.entity.RepaireDetail;
import my.smartdec.detect.repaire.util.NodeCopyUtil;
import my.smartdec.detect.repaire.util.NodeUtil;
import my.smartdec.detect.repaire.util.TreeNodeUtil;
import my.smartdec.detect.repaire.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SucPattern {
    public ErrorReport errorReport;

    public SucPattern(ErrorReport errorReport){
        this.errorReport =errorReport;
    }
    public List<RepaireDetail> suc001()  {
        List<RepaireDetail> repaireDetails = new ArrayList<>();
        int line = errorReport.getStartLine();
        String inputString = errorReport.getContext();
        String newCode = String.format("require(%s);" , inputString);

        RepaireDetail rd = new RepaireDetail("update",line - 1,errorReport.getContext(),newCode);
        repaireDetails.add(rd);


        return repaireDetails;
    }

    public List<RepaireDetail> suc003() throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        List<RepaireDetail> repaireDetails = new ArrayList<>();
        int startLine = errorReport.getStartLine();
        int stopLIne = errorReport.getStopLine();

        Node suc003Node = NodeCopyUtil.getNode(XMLUtil.suc003,NodeCopyUtil.sourceNode,errorReport.getIndex());

        NodeList isHaveVariable = NodeCopyUtil.getNodelist(".//tupleExpression//variableDeclaration",suc003Node);
        if (isHaveVariable.getLength()>0){

            Node pareant = NodeCopyUtil.getNode(".//tupleExpression",suc003Node,0);
            Node variableDeNode = TreeNodeUtil.getvariableDeclaritionExpressionNode("callFlag","bool");
            Node temp = null;


            //确定需要插入的temp位置
            for (int i =0;i<pareant.getChildNodes().getLength();i++){
                Node n = pareant.getChildNodes().item(i);
                //System.out.println("TypeNmae :"+n.getNodeName()+" Type: "+n.getNodeType()+" value: "+n.getNodeValue());
                if (n.getNodeType() == 3&&n.getNodeValue().equals(",")){
                    temp = n;
                    break;
                }
            }
            Document ownerDocument=pareant.getOwnerDocument();
            Node importedTemp1 = ownerDocument.importNode(variableDeNode, true);;
            pareant.insertBefore(importedTemp1,temp);
            NodeCopyUtil.isTreeRepaire = true;
            RepaireDetail requireFlagRe = new RepaireDetail("add",stopLIne,errorReport.getContext(),"require(callFlag);");
            repaireDetails.add(requireFlagRe);
        }else {
            Node pareant = NodeCopyUtil.getNode(".//tupleExpression",suc003Node,0);
            Node primaryExpressionNode= TreeNodeUtil.getPrimaryExpressionNode("callFlag");
            //System.out.println(primaryExpressionNode.getTextContent());
            Node temp = null;
            //确定需要插入的temp位置
            for (int i =0;i<pareant.getChildNodes().getLength();i++){
                Node n = pareant.getChildNodes().item(i);
                //System.out.println("TypeNmae :"+n.getNodeName()+" Type: "+n.getNodeType()+" value: "+n.getNodeValue());
                if (n.getNodeType() == 3&&n.getNodeValue().equals(",")){
                    temp = n;
                    break;
                }
            }

            Document ownerDocument=pareant.getOwnerDocument();
            Node importedTemp1 = ownerDocument.importNode(primaryExpressionNode, true);;
            pareant.insertBefore(importedTemp1,temp);
            NodeCopyUtil.isTreeRepaire = true;

            //插入变量与判断
            List<RepaireDetail> list = new ArrayList<>();
            RepaireDetail flagRe = new RepaireDetail("add",startLine-1,errorReport.getContext(),"bool callFlag;");
            RepaireDetail requireFlagRe = new RepaireDetail("add",stopLIne,errorReport.getContext(),"require(callFlag);");
            repaireDetails.add(flagRe);
            repaireDetails.add(requireFlagRe);
        }



        return repaireDetails;
    }
}

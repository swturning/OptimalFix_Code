package my.smartdec.detect.repaire;

import lombok.Data;
import my.smartdec.detect.repaire.entity.ErrorReport;
import my.smartdec.detect.repaire.entity.NodeEntity;
import my.smartdec.detect.repaire.entity.RepaireDetail;
import my.smartdec.detect.repaire.entity.RepaireResult;
import my.smartdec.detect.repaire.util.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@Data
public class ReePattern {
    public ErrorReport errorReport;

    private String str ;

    //表明当前合约是否添加了 互斥变量
    public Boolean isMutex = false;



    public ReePattern(ErrorReport errorReport,Integer reMutex){
        this.errorReport =errorReport;
        str = "bool ReeTrAncy" + reMutex+" = false;";
    }

    public List<RepaireDetail> ree0003Repaire(Integer reMutex) throws XPathExpressionException{

        List<RepaireDetail> repaireDetails = new ArrayList<>();

        Integer lastVarLine = getVarlitionLine(errorReport.getPattern(),errorReport.getIndex());
        Integer firstStatementLine = firstStatementLine(errorReport.getPattern(),errorReport.getIndex());

        //lines.add(lastVarLine, this.str);
        if (!isMutex){
            RepaireDetail re1 = new RepaireDetail("add",lastVarLine,errorReport.getContext(),this.str);
            repaireDetails.add(re1);
            isMutex= true;
        }


        //lines.add(firstStatementLine-1,"require(!ReeTrAncy,'Reentrancy attempt detected');");
        RepaireDetail re2 = new RepaireDetail("add",firstStatementLine-1,errorReport.getContext(),"require(!ReeTrAncy"+reMutex+",'Reentrancy attempt detected');");
        repaireDetails.add(re2);

        //lines.add(errorReport.getStartLine()-1,"ReeTrAncy = true;");
        RepaireDetail re3 = new RepaireDetail("add",errorReport.getStartLine()-1,errorReport.getContext(),"ReeTrAncy"+reMutex+" = true;");


        //lines.add(errorReport.getStopLine(),"ReeTrAncy = false;");
        Integer reeStopLine = getReeStopLine(errorReport.getPattern(), errorReport.getIndex());
        RepaireDetail re4 = new RepaireDetail("add",reeStopLine,errorReport.getContext(),"ReeTrAncy"+reMutex+" = false;");
        repaireDetails.add(re4);
        repaireDetails.add(re3);


//            Files.write(file, lines);

        Collections.sort(repaireDetails);

        return repaireDetails;

    }

    public List<RepaireDetail> ree0001Repaire(Integer reMutex) throws IOException, XPathExpressionException {

       // String fileName = PropertiesUtil.contractsFile +PropertiesUtil.contractFile;
        String fileName = PropertiesUtil.fileDe;
       // Path file = Paths.get("D:\\DectionContract\\SolidityDetection-main\\test\\test11.sol");
        Path file = Paths.get(fileName);
        List<String> lines = Files.readAllLines(file);

        List<RepaireDetail> repaireDetails = new ArrayList<>();
        if (errorReport.getChooseType().equals("A")){

            String lineContext =lines.get(this.errorReport.getStartLine() - 1);
            String sendContext = replaceFirstCallWithSend(errorReport.getContext());
            String newCode = lineContext.replace(errorReport.getContext() ,sendContext );
            RepaireDetail re = new RepaireDetail("update",this.errorReport.getStartLine()-1,errorReport.getContext(),newCode);
            repaireDetails.add(re);
            lines.set(this.errorReport.getStartLine() - 1, newCode);
//            Files.write(file, lines);
        }else if (errorReport.getChooseType().equals("B")){

            Integer lastVarLine = getVarlitionLine(errorReport.getPattern(),errorReport.getIndex());
            Integer firstStatementLine = firstStatementLine(errorReport.getPattern(),errorReport.getIndex());

            //lines.add(lastVarLine, this.str);
            if (!isMutex){
                RepaireDetail re1 = new RepaireDetail("add",lastVarLine,errorReport.getContext(),this.str);
                repaireDetails.add(re1);
                isMutex= true;
            }

            //lines.add(firstStatementLine-1,"require(!ReeTrAncy,'Reentrancy attempt detected');");
            RepaireDetail re2 = new RepaireDetail("add",firstStatementLine-1,errorReport.getContext(),"require(!ReeTrAncy"+reMutex+",'Reentrancy attempt detected');");
            repaireDetails.add(re2);

            //lines.add(errorReport.getStartLine()-1,"ReeTrAncy = true;");
            RepaireDetail re3 = new RepaireDetail("add",errorReport.getStartLine()-1,errorReport.getContext(),"ReeTrAncy"+reMutex+" = true;");


            //lines.add(errorReport.getStopLine(),"ReeTrAncy = false;");
            RepaireDetail re4 = new RepaireDetail("add",errorReport.getStopLine(),errorReport.getContext(),"ReeTrAncy"+reMutex+" = false;");
            repaireDetails.add(re4);
            repaireDetails.add(re3);
//            Files.write(file, lines);

        }
        else if(errorReport.getChooseType().equals("c")){

        }
        Collections.sort(repaireDetails);

        return repaireDetails;

    }

    public List<RepaireDetail> writeBefore(Integer s) throws XPathExpressionException, IOException {
        List<RepaireDetail> repaireDetails = new ArrayList<>();
        Node reeNode = NodeCopyUtil.getNode(XMLUtil.ree001,NodeCopyUtil.sourceNode,errorReport.getIndex());
        //获取 转账地址 转账金额
        Node toAddress = NodeCopyUtil.getNode("preceding-sibling::expression[last()]",reeNode,0);
        Node vauleNode = NodeCopyUtil.getNode("./value/expression",reeNode,0);
        String address = toAddress.getTextContent();
        String value = StringUtil.removeEscape(vauleNode.getTextContent());
        Node node = NodeUtil.getNode(errorReport.getPattern(), errorReport.getIndex());
        List<NodeEntity> guolv = guolv(node, address, value);
        if (guolv == null||guolv.size()==0){
            List<RepaireDetail> repaireDetails1 = ree0001Repaire(s);
            return repaireDetails1;
        }else {
            for (NodeEntity ne:guolv){
                RepaireDetail reAdd = new RepaireDetail("add",errorReport.getStopLine()-1,errorReport.getContext(),ne.getNode().getTextContent()+";");
                repaireDetails.add(reAdd);
                RepaireDetail de = new RepaireDetail("delete",ne.getLine()-1,errorReport.getContext(),null);
                repaireDetails.add(de);
            }
        }
        return repaireDetails;

    }

    private List<NodeEntity> guolv(Node reeNode, String toAddress, String value) throws XPathExpressionException {
        boolean falg = true;
        List<NodeEntity> nodeLists = new ArrayList<>();



        NodeList assibnmentList1 = NodeCopyUtil.getNodelist("./ancestor::statement/following-sibling::statement//expression[lvalueOperator]",reeNode);
        //NodeList assibnmentList1 = NodeCopyUtil.getNodelist("./ancestor::statement/following-sibling::statement[descendant::expression[lvalueOperator]]",reeNode);
        for (int i=0;i<assibnmentList1.getLength();i++){

            String ass = assibnmentList1.item(i).getTextContent();
            if ((ass.contains(toAddress)||ass.contains(value))){
                if (ass.contains("{")&&ass.contains("}")){
                    falg = false;
                    break;
                }
                if (SplitStringExample(ass,toAddress,value)){
                    NodeEntity entity = new NodeEntity(assibnmentList1.item(i),NodeUtil.nodeToXmlContext((NodeList) assibnmentList1.item(i)));
                    nodeLists.add(entity);
                }else {
                    falg=false;
                    break;
                }
            }
        }


        NodeList assibnmentList2 = NodeCopyUtil.getNodelist("./ancestor::statement/following-sibling::statement//expression[text()[1]=\"=\"]",reeNode);
        for (int i=0;i<assibnmentList2.getLength();i++){
            String ass = assibnmentList2.item(i).getTextContent();
            if (ass.contains(toAddress)||ass.contains(value)){
                if (ass.contains("{")&&ass.contains("}")){
                    falg = false;
                    break;
                }
                if (SplitStringExample(ass,toAddress,value)){
                    NodeEntity entity = new NodeEntity(assibnmentList2.item(i),NodeUtil.nodeToXmlContext((NodeList) assibnmentList2.item(i)));
                    nodeLists.add(entity);
                }else {
                    falg = false;
                    break;
                }

            }
        }
        NodeList assibnmentList3 = NodeCopyUtil.getNodelist("./ancestor::statement/following-sibling::statement//expression[twoPlusMinusOperator]",reeNode);
        for (int i=0;i<assibnmentList3.getLength();i++){
            String ass = assibnmentList3.item(i).getTextContent();
            if (ass.contains(toAddress)||ass.contains(value)){
                if (ass.contains("{")&&ass.contains("}")){
                    falg = false;
                    break;
                }
                if (SplitStringExample(ass,toAddress,value)) {
                    NodeEntity entity = new NodeEntity(assibnmentList3.item(i),NodeUtil.nodeToXmlContext((NodeList) assibnmentList3.item(i)));
                    nodeLists.add(entity);
                }else {
                    falg= false;
                    break;
                }
            }
        }


        NodeList  callValues = NodeCopyUtil.getNodelist("./ancestor::statement/following-sibling::statement//functionCall[functionName/identifier[not(matches(text()[1], \"require|assert|revert\"))]]",reeNode);

        for (int i=0;i<callValues.getLength();i++){
            String ass = callValues.item(i).getTextContent();
            if (ass.contains(toAddress)||ass.contains(value)){
                if (ass.contains("{")&&ass.contains("}")){
                    falg = false;
                    break;
                }
                Node callNameNode = NodeCopyUtil.getNode("./functionName//identifier",callValues.item(i),0);
                String name = callNameNode.getTextContent();
                if (functionIsAssignment(toAddress,value,callNameNode,name)){
                    NodeEntity entity = new NodeEntity(callValues.item(i),NodeUtil.nodeToXmlContext((NodeList) callValues.item(i)));
                    nodeLists.add(entity);
                }else {
                    falg = false;
                }

//                if (SplitStringExample(ass,toAddress,value)) {
//                    NodeEntity entity = new NodeEntity(callValue.item(i),NodeUtil.nodeToXmlContext((NodeList) assibnmentList3.item(i)));
//                    nodeLists.add(entity);
//                }else {
//                    falg= false;
//                }
            }
        }
        if (!falg){
            return null;
        }
        Collections.sort(nodeLists);

        return nodeLists;

    }

    public boolean functionIsAssignment(String address,String value,Node callNameNOde,String functionName) throws XPathExpressionException {
        String str = String.format("ancestor::contractDefinition//functionDefinition[identifier[matches(text()[1],\"%s\")]]", functionName);
        NodeList functionNodes = NodeCopyUtil.getNodelist(str, callNameNOde);

        Node functionNode = functionNodes.item(0);
        NodeList functionAssignmentNodes = NodeCopyUtil.getNodelist(".//statement//expression[text()[1]=\"=\"]", functionNode);

        for (int i = 0; i < functionAssignmentNodes.getLength(); i++) {
            String ass = functionAssignmentNodes.item(i).getTextContent();
            if ((ass.contains(value)||ass.contains(address)) && (!ass.contains("{") && !ass.contains("}"))) {
                if (!(SplitStringExample(ass,address,value))){
                    return false;
                }
            }
        }

        NodeList functionAssignmentNode2s = NodeCopyUtil.getNodelist(".//expression[twoPlusMinusOperator]", functionNode);
        for (int i = 0; i < functionAssignmentNode2s.getLength(); i++) {
            String ass = functionAssignmentNode2s.item(i).getTextContent();
            if ((ass.contains(value)||ass.contains(address)) && (!ass.contains("{") && !ass.contains("}"))) {
                if (!(SplitStringExample(ass,address,value))){
                    return false;
                }
            }
        }

        NodeList functionAssignmentNode3s = NodeCopyUtil.getNodelist(".//expression[lvalueOperator]", functionNode);
        for (int i = 0; i < functionAssignmentNode3s.getLength(); i++) {
            String ass = functionAssignmentNode3s.item(i).getTextContent();
            if ((ass.contains(value)||ass.contains(address)) && (!ass.contains("{") && !ass.contains("}"))) {
                if (!(SplitStringExample(ass,address,value))){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean  SplitStringExample(String input,String address,String value) {
        // 要分割的字符串

        String pattern = ".*([+*-/]=).*";
        String[] parts ;
        boolean containsOperator = Pattern.matches(pattern, input);
        if (containsOperator){
            parts =input.split("\\s*([+*-/]=)\\s*");
        }else {
            parts = input.split("=");
        }
        // 使用等号("=")进行分割
//        String[] parts = input.split("=");
//        if (parts.length!=2){
//            parts = input.split("\\s*([+*-/]=)\\s*");;
//            if (parts.length!=2){
//                return false;
//            }
//
//        }

        if (parts.length!=2){
            return false;
        }
        if (parts[0].equals(address)||parts[0].equals(value)){
            return false;
        }

        return true;


    }


    //获取漏洞的结束部分
    public Integer getReeStopLine(String id,int index) throws XPathExpressionException {
        Node node = NodeUtil.nodeLists.get(id).item(index);
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();

        // String expression = String.format("//stateVariableDeclaration[typeName/elementaryTypeName[matches(text()[1], \"%s\")]]/identifier",type);
        XPathExpression identifierExpr = xpath.compile(" ancestor::ifStatement[1]");
        NodeList statementNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);
        Node identifierNode = statementNodes.item(statementNodes.getLength()-1);
        System.out.println(identifierNode.getTextContent());
        //list.add(identifierNode.getTextContent());
        Integer ReeStopLine = NodeUtil.NodeStopToXmlContext((NodeList) identifierNode);

        return ReeStopLine;
    }

    //从xml文件中 搜索全部为type类型的 状态变量 返回全部的变量名的List
    public  Integer getVarlitionLine(String id,int index) throws  XPathExpressionException {

        Node node = NodeUtil.nodeLists.get(id).item(index);
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();

       // String expression = String.format("//stateVariableDeclaration[typeName/elementaryTypeName[matches(text()[1], \"%s\")]]/identifier",type);
        XPathExpression identifierExpr = xpath.compile(" ancestor::contractDefinition//stateVariableDeclaration[typeName]");
        NodeList statementNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);


        // 输出找到的identifier数据

        Node identifierNode = statementNodes.item(statementNodes.getLength()-1);
        //list.add(identifierNode.getTextContent());
        Integer stateVariableLine = NodeUtil.nodeToXmlContext((NodeList) identifierNode);

        System.out.println("stateVariableLine: "+stateVariableLine);

        return stateVariableLine;
    }

    //获取当前漏洞所在函数的全部 statement节点
    public  Integer firstStatementLine(String id,int index) throws XPathExpressionException {
        Node node = NodeUtil.nodeLists.get(id).item(index);
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression identifierExpr = xpath.compile(" ancestor::functionDefinition/block/statement");
        NodeList statementNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);

        Node identifierNode = statementNodes.item(0);
        Integer statementLine = NodeUtil.nodeToXmlContext((NodeList) identifierNode);

        System.out.println("firstStatementLine: " +statementLine);

//        System.out.println("statementNodes"+statementNodes.getLength());
        return statementLine;
    }


    public  String replaceFirstCallWithSend(String input) {
        // 查找第一个 "call" 出现的位置
        int index = input.indexOf("call");

        if (index != -1) {
            // 将 "call" 替换为 "send"
            String replaced = input.substring(0, index) + "send" + input.substring(index + 4);
            return replaced;
        } else {
            // 如果没有找到 "call"，则返回原始字符串
            return input;
        }
    }
}

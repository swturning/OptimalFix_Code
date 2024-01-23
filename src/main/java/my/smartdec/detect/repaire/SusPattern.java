package my.smartdec.detect.repaire;

import my.smartdec.detect.repaire.entity.ErrorReport;
import my.smartdec.detect.repaire.entity.RepaireDetail;
import my.smartdec.detect.repaire.util.NodeUtil;
import my.smartdec.detect.repaire.util.StringUtil;
import my.smartdec.detect.repaire.util.TreeNodeUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SusPattern {
    public ErrorReport errorReport;

    //是否存在构造函数
    private boolean ifConstructor = false;

      //判断是否存在这个 address类型的变量
//    private boolean isOwner = false;

    //表明保存在这个 构造函数中又存在存储 msg.sender的地址  的变量名
    private String owner = null;


    public SusPattern(ErrorReport errorReport){
        this.errorReport =errorReport;
    }

    public List<RepaireDetail>  sus001Repaire() throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        //获取全部为addreee的状态变量
//        List<String> varlition = NodeUtil.getVarlition(" ", "address");
//        if (varlition.size()>0){
//            this.isOwner = true;
//        }
        //获取Constroctor Node节点
        NodeList constructorNodeList = NodeUtil.getConstructor(errorReport.getPattern(), errorReport.getIndex());
//        if (constructorNodeList.getLength()>0&&constructorNodeList!=null){
//            this.ifConstructor = true;
//        }
        if (constructorNodeList.getLength()>0&&constructorNodeList!=null){
            this.ifConstructor = true;
            Node constructorNode = constructorNodeList.item(0);
            this.owner = getConstructorStatement(constructorNode);
        }


        List<RepaireDetail> repaireDetails = new ArrayList<>();
        Integer lastVarLine = getVarlitionLine(errorReport.getPattern(),errorReport.getIndex());
        int line = errorReport.getStartLine();
        //A 表示添加 require
        if (errorReport.getChooseType().equals("A")){

            if (owner!=null){
                String str = String.format("require(msg.sender == %s);", owner);
                RepaireDetail rd = new RepaireDetail("add",line - 1,errorReport.getContext(),str);
                repaireDetails.add(rd);
            }else {
                if (ifConstructor){
                    //获取所有状态变量的最后一行  添加创建变量

                    RepaireDetail addressRd = new RepaireDetail("add",lastVarLine ,errorReport.getContext(),"address public owner;");
                    repaireDetails.add(addressRd);


                    Integer constructorLine = NodeUtil.nodeToXmlContext(constructorNodeList);
                    String str = String.format("%s = msg.sender ;", "owner");
                    RepaireDetail rd = new RepaireDetail("add",constructorLine ,errorReport.getContext(),str);
                    repaireDetails.add(rd);


                    String strs = String.format("require(msg.sender == %s);", owner);
                    RepaireDetail rds = new RepaireDetail("add",line - 1,errorReport.getContext(),strs);
                    repaireDetails.add(rds);
                }else{
                    this.owner = "owner";
                    //Integer lastVarLine = getVarlitionLine(errorReport.getPattern(),errorReport.getIndex());
                    RepaireDetail addressRd = new RepaireDetail("add",lastVarLine ,errorReport.getContext(),"address public owner;");
                    repaireDetails.add(addressRd);

                    Integer firstFunctionLine = firstFunctiontLine(errorReport.getPattern(),errorReport.getIndex());
                    String str = String.format("constructor() {%s = msg.sender;}", owner);
                    RepaireDetail constructorRd = new RepaireDetail("add",firstFunctionLine-1 ,errorReport.getContext(),str);
                    repaireDetails.add(constructorRd);

                    String strs = String.format("require(msg.sender == %s);", owner);
                    RepaireDetail rds = new RepaireDetail("add",line - 1,errorReport.getContext(),strs);
                    repaireDetails.add(rds);

                }

            }

        }
        else if (errorReport.getChooseType().equals("B")){
            Integer firstFunctionLine = firstFunctiontLine(errorReport.getPattern(),errorReport.getIndex());

            if (ifConstructor){
                if (owner!=null){

                }else {
                    
                   
                }

            }else {
                //构造全局的 owner变量
                Node stateVariableNode = TreeNodeUtil.getStateVariableNode(null, null, this.owner);
                RepaireDetail addressRd = new RepaireDetail("add",lastVarLine ,errorReport.getContext(),StringUtil.removeEscapeExOne(stateVariableNode.getTextContent()));
                repaireDetails.add(addressRd);
                //构造constructor

                Node constructorNode = TreeNodeUtil.getConstructorNode();
                Node ownerAssiagment = TreeNodeUtil.getAssignmentNode("owner", null);
                makeConstructor(constructorNode,ownerAssiagment);
                //System.out.println(StringUtil.removeEscapeExOne(constructorNode.getTextContent()));
                RepaireDetail rdConstructor = new RepaireDetail("add",firstFunctionLine - 1,errorReport.getContext(),StringUtil.removeEscapeExOne(constructorNode.getTextContent()));
                repaireDetails.add(rdConstructor);
            }


            //构造modifier
            Node modifierNode = TreeNodeUtil.getModifierNode( "isOwner");

            Node comparisonStatement = TreeNodeUtil.getComparisonStatement((this.owner == null) ? "owner" : this.owner, null, null);
            Node requireNode = TreeNodeUtil.getRequireNode(null);

            Node modifierTernaryNode = makeModefierTernary(modifierNode, comparisonStatement, requireNode);

            Integer modifierLine = null;
            NodeList modifierNodeList = NodeUtil.modifierNode(errorReport.getPattern(), errorReport.getIndex());
            if (modifierNodeList!=null){
                modifierLine = NodeUtil.NodeStopToXmlContext(modifierNodeList);
            }else if (ifConstructor){
                modifierLine = NodeUtil.nodeToXmlContext(constructorNodeList)-1;
            }else {
                modifierLine = firstFunctionLine-1;
            }
            RepaireDetail modifierRd = new RepaireDetail("add",modifierLine ,errorReport.getContext(),StringUtil.removeEscapeExOne(modifierTernaryNode.getTextContent()));
            repaireDetails.add(modifierRd);


            Node isOwner = TreeNodeUtil.getIdenfierNode("isOwner");

            //开始对目标函数添加 修饰函数
            Node functionNode = NodeUtil.getFunctionNode(errorReport.getPattern(), errorReport.getIndex());
            Integer myFunctionFirstLine = NodeUtil.nodeToXmlContext((NodeList) functionNode);

            Node cloneFunctionNode = functionNode.cloneNode(true);

            Document ownerDocument = null;

            Node visibleItem = null;

            //去除block
            for (int i =0;i<cloneFunctionNode.getChildNodes().getLength();i++){

                if (cloneFunctionNode.getChildNodes().item(i).getNodeName().equals("visibleType")){
                    ownerDocument = cloneFunctionNode.getOwnerDocument();
                    visibleItem = cloneFunctionNode.getChildNodes().item(i);
                    //cloneFunctionNode.insertBefore(importedTemp1,cloneFunctionNode.getChildNodes().item(i));



                }
                if (cloneFunctionNode.getChildNodes().item(i).getNodeName().equals("block")){
                    cloneFunctionNode.removeChild(cloneFunctionNode.getChildNodes().item(i));
                    break;
                }
            }
            Node importedTemp1 = ownerDocument.importNode(isOwner, true);;
            cloneFunctionNode.insertBefore(importedTemp1,visibleItem);



            RepaireDetail functionRD = new RepaireDetail("update",myFunctionFirstLine-1 ,errorReport.getContext(),NodeUtil.getNodeTextContent(cloneFunctionNode)+"{");
            repaireDetails.add(functionRD);

        }


        return repaireDetails;
    }

    //获取第一个函数的第一行
    public  Integer firstFunctiontLine(String id,int index) throws XPathExpressionException {
        Node node = NodeUtil.nodeLists.get(id).item(index);
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression identifierExpr = xpath.compile(" ancestor::contractDefinition//functionDefinition[matches(text()[1], \"function\")]");
        NodeList statementNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);

        Node identifierNode = statementNodes.item(0);
        Integer statementLine = NodeUtil.nodeToXmlContext((NodeList) identifierNode);

        System.out.println("firstStatementLine: " +statementLine);

//        System.out.println("statementNodes"+statementNodes.getLength());
        return statementLine;
    }



    //合成 modifier树
    private Node makeModefierTernary (Node modifierNode,Node comparisonStatement,Node requireNode) throws XPathExpressionException{

       // System.out.println("require:"+StringUtil.removeEscapeExOne(requireNode.getTextContent()));
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();


        XPathExpression identifierExpr = xpath.compile(".//callArguments");


        Node callArgumentsNode = (Node) identifierExpr.evaluate(requireNode, XPathConstants.NODE);
        NodeList childNodes = callArgumentsNode.getChildNodes();
        Node expressionNode = null;
        for (int i =0;i<childNodes.getLength();i++){
            Node node = childNodes.item(i);
            if (node.getNodeName().equals("expression")){
                expressionNode = node;
                break;
            }
        }

        Document ownerDocument = callArgumentsNode.getOwnerDocument();

        Node importedTemp1 = ownerDocument.importNode(comparisonStatement, true);

        callArgumentsNode.insertBefore(importedTemp1,expressionNode);

        callArgumentsNode.removeChild(expressionNode);

       // System.out.println(StringUtil.removeEscapeExOne(requireNode.getTextContent()));


        identifierExpr = xpath.compile(".//block/statement");
        Node statementNode = (Node) identifierExpr.evaluate(modifierNode, XPathConstants.NODE);


        Node blockNode = null;


        Node modefierItem = modifierNode.getChildNodes().item(1);
        //System.out.println(StringUtil.removeEscapeExOne(modefierItem.getTextContent()));

        for (int i =0;i<modefierItem.getChildNodes().getLength(); i++){
            Node n = modefierItem.getChildNodes().item(i);

            if (n.getNodeName().equals("block")){
                blockNode = n;
                break;
            }

        }
        //System.out.println(StringUtil.removeEscapeExOne(blockNode.getTextContent()));

        Node importedTemp2 = blockNode.getOwnerDocument().importNode(requireNode, true);
        blockNode.insertBefore(importedTemp2,statementNode);
       // System.out.println(StringUtil.removeEscapeExOne(modifierNode.getTextContent()));

        return modifierNode;

    }

    private Node makeConstructor(Node constructorNode,Node assignmentNode) throws XPathExpressionException {

        Node blockNode = null;
        Node statementNode = null;
        NodeList childNodes = constructorNode.getChildNodes();
        for (int i =0;i<childNodes.getLength(); i++){
            Node n = childNodes.item(i);

            if (n.getNodeName().equals("block")){
                blockNode = n;
                break;
            }

        }

        NodeList childNodes1 = blockNode.getChildNodes();
        for (int i =0;i<childNodes1.getLength(); i++){
            Node n = childNodes1.item(i);
            if (n.getNodeName().equals("statement")){
                statementNode = n;
                break;
            }


        }
        Node importedTemp = blockNode.getOwnerDocument().importNode(assignmentNode, true);
        blockNode.insertBefore(importedTemp,statementNode);
        blockNode.removeChild(statementNode);
        //System.out.println(StringUtil.removeEscapeExOne(constructorNode.getTextContent()));



        return constructorNode;
    }












    //从xml文件中 搜索全部为type类型的 状态变量 返回全部的变量名的List
    public  Integer getVarlitionLine(String id,int index) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {

        Node node = NodeUtil.nodeLists.get(id).item(index);
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();

        // String expression = String.format("//stateVariableDeclaration[typeName/elementaryTypeName[matches(text()[1], \"%s\")]]/identifier",type);
        XPathExpression identifierExpr = xpath.compile(" ancestor::contractDefinition//stateVariableDeclaration[typeName]");
        NodeList statementNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);

        if (statementNodes.getLength()==0){
            XPathExpression contract = xpath.compile("ancestor::contractDefinition//functionDefinition");
            NodeList contractNodes = (NodeList) contract.evaluate(node, XPathConstants.NODESET);
            Node contractNode = contractNodes.item(0);

            System.out.println(contractNode.getTextContent());

            return NodeUtil.nodeToXmlContext((NodeList) contractNode)-1;
        }
        // 输出找到的identifier数据

        Node identifierNode = statementNodes.item(statementNodes.getLength()-1);
        //list.add(identifierNode.getTextContent());
        Integer stateVariableLine = NodeUtil.nodeToXmlContext((NodeList) identifierNode);

        //System.out.println("stateVariableLine: "+stateVariableLine);

        return stateVariableLine;
    }

    //获取构造函数中全部的赋值语句
    private String getConstructorStatement(Node node) throws XPathExpressionException {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
//        String expression = String.format(".//expression[matches(text()[1],\"=\")][expression[1]/primaryExpression/identifier[matches(text()[1], \"%s\")]][expression[2]/environmentalVariable[matches(text()[1],\"msg.sender\")]]", inputVar);
        String expression = String.format(".//expression[matches(text()[1],\"=\")][expression[2]/environmentalVariable[matches(text()[1],\"msg.sender\")]]/expression/primaryExpression/identifier");

        XPathExpression identifierExpr = xpath.compile(expression);
        NodeList statementNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);

        if (statementNodes.getLength() ==0){
            return null;
        }
        Node statementNode = statementNodes.item(0);

        //System.out.println(statementNode.getFirstChild().getNodeValue());
        return statementNode.getFirstChild().getNodeValue();

    }

    public static String readXMLFromFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
}

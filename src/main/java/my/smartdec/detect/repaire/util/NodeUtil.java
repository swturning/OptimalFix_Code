package my.smartdec.detect.repaire.util;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.Tree;
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
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class NodeUtil {

    //全部的漏洞节点 N
    public static Map<String, NodeList> nodeLists = new HashMap<>();

    public static Map<Tree, Node> nodes ;

    public static Tree rule ;

//    public static filePath
    //表示合约的filePath
    public static  String filePath;


    public static Node getNode(String id,int index) throws XPathExpressionException{
        return nodeLists.get(id).item(index);
    }

    public static Node getAllCopy(String id,int index) throws XPathExpressionException{
        return nodeLists.get(id).item(index).cloneNode(true);
    }

    //获取当前漏洞所在的函数名
    public static String getFunctionName (String id,int index) throws XPathExpressionException {
        Node node = nodeLists.get(id).item(index);
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression identifierExpr = xpath.compile(" ancestor::functionDefinition/identifier");
        NodeList identifierNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);
        String functionName = null;
        //此处由于时 函数名 所以size为 1
        Node identifierNode = identifierNodes.item(0);
        //System.out.println("第一次获得的类型"+identifierNode.getClass());
        functionName = identifierNode.getTextContent();
       // System.out.println("Identifier Node Text: " + functionName);
        return functionName;
    }

    //获取当前漏洞所在的合约名
    public static String getContractName (String id,int index) throws XPathExpressionException {
        Node node = nodeLists.get(id).item(index);
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression identifierExpr = xpath.compile(" ancestor::contractDefinition/identifier");
        NodeList identifierNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);
        String functionName = null;
        //此处由于时 函数名 所以size为 1
        Node identifierNode = identifierNodes.item(0);
        //System.out.println("第一次获得的类型"+identifierNode.getClass());
        functionName = identifierNode.getTextContent();
        //System.out.println("Identifier Node Text: " + functionName);


        return functionName;
    }

    //获取 import节点
    public static NodeList getImportDirective(String id,int index) throws XPathExpressionException{
        Node node = nodeLists.get(id).item(index);
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression identifierExpr = xpath.compile("ancestor::sourceUnit/importDirective");
        NodeList identifierNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);

        if (identifierNodes.getLength()>0){
            return identifierNodes;
        }else {
            return null;
        }

    }

    public static NodeList getPragmaDirective(String id,int index) throws XPathExpressionException{
        Node node = nodeLists.get(id).item(index);
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression identifierExpr = xpath.compile("ancestor::sourceUnit/pragmaDirective");
        NodeList identifierNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);

        if (identifierNodes.getLength()>0){
            return identifierNodes;
        }else {
            return null;
        }

    }

    public static NodeList  getLibiary(String id,int index) throws XPathExpressionException{
        Node node = nodeLists.get(id).item(index);
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression identifierExpr = xpath.compile("ancestor::sourceUnit/contractDefinition/contractPartDefinition[usingForDeclaration]");
        NodeList identifierNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);
        if (identifierNodes.getLength()>0){
            return identifierNodes;
        }else {
            return null;
        }
    }

    //获取合约主体
    public static NodeList getContractPart(String id,int index) throws XPathExpressionException{
        Node node = nodeLists.get(id).item(index);
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        //这里做了一个修改
        XPathExpression identifierExpr = xpath.compile("ancestor::contractDefinition");
        NodeList identifierNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);
        return identifierNodes;
    }


    //获取当前漏洞所在函数的Node
    public static Node getFunctionNode (String id,int index) throws XPathExpressionException {
        Node node = nodeLists.get(id).item(index);

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression identifierExpr = xpath.compile(" ancestor::functionDefinition");
        NodeList identifierNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);
        return identifierNodes.item(0);

    }


    //获取当前漏洞所在函数的全部 statement节点
    public static NodeList getFunctionStatement(String id,int index) throws XPathExpressionException {
        Node node = nodeLists.get(id).item(index);
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression identifierExpr = xpath.compile(" ancestor::functionDefinition/block/statement");
        NodeList statementNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);
        for (int i = 0; i < statementNodes.getLength(); i++) {
            Node identifierNode = statementNodes.item(i);
            String functionName = identifierNode.getTextContent();
            //System.out.println("Identifier Node Text: " + functionName+"    getFunctionStatement");
        }
//        System.out.println("statementNodes"+statementNodes.getLength());
        return statementNodes;
    }

    //判断是否存在require assert revert语句
    public static List<String> requireDetermine(Node node) throws XPathExpressionException {
        List<String> expressions = new ArrayList<>();
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        String expression = ".//functionCall[functionName/identifier[matches(text()[1], \"require|assert|revert\")]]/callArguments/expression[1]";
        XPathExpression identifierExpr = xpath.compile(expression);
        NodeList statementNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);
        for (int i = 0; i < statementNodes.getLength(); i++) {
            Node identifierNode = statementNodes.item(i);
            String functionName = identifierNode.getTextContent();
            expressions.add(functionName);
            //System.out.println("Identifier Node Text: " + functionName+"      requireDetermine");
        }

        return expressions;
       // System.out.println("statementNodes"+statementNodes.getLength());
    }

    //判断是否存在关于目标变量的赋值语句语句
    public static List<String> statementDetermine(Node node,String inputVar) throws XPathExpressionException {
        List<String> expressions = new ArrayList<>();
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        String expression = String.format(".//expression[matches(text()[1],\"=\")][expression[1]/primaryExpression/identifier[matches(text()[1], \"%s\")]][expression[2]/primaryExpression/numberLiteral/decimalNumber  or expression[2]/primaryExpression/booleanLiteral]", inputVar);
        //String expression = ".//expression[matches(text()[1],\"=\")][expression[1]/primaryExpression/identifier[matches(text()[1], \"a\")]][expression[2]/primaryExpression/numberLiteral/decimalNumber  or expression[2]/primaryExpression/booleanLiteral]";
        XPathExpression identifierExpr = xpath.compile(expression);
        NodeList statementNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);
        for (int i = 0; i < statementNodes.getLength(); i++) {
            Node identifierNode = statementNodes.item(i);
            String functionName = identifierNode.getTextContent();
            expressions.add(functionName);
            //System.out.println("Identifier Node Text: " + functionName+"      statementDetermine");
        }

        return expressions;
        // System.out.println("statementNodes"+statementNodes.getLength());
    }

    //判断是否存在关于目标变量的赋值语句语句
    public static NodeList statementDetermineInBlock(Node node,String inputVar) throws XPathExpressionException {
        List<String> expressions = new ArrayList<>();
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        String expression = String.format(".//expression[matches(text()[1],\"=\")][expression[1]/primaryExpression/identifier[matches(text()[1], \"%s\")]][expression[2]/primaryExpression/numberLiteral/decimalNumber  or expression[2]/primaryExpression/booleanLiteral]", inputVar);
        //String expression = ".//expression[matches(text()[1],\"=\")][expression[1]/primaryExpression/identifier[matches(text()[1], \"a\")]][expression[2]/primaryExpression/numberLiteral/decimalNumber  or expression[2]/primaryExpression/booleanLiteral]";
        XPathExpression identifierExpr = xpath.compile(expression);
        NodeList statementNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);
       // System.out.println("nodeList"+statementNodes.getLength());

        return statementNodes;
        // System.out.println("statementNodes"+statementNodes.getLength());
    }




    //搜索当前节点的全部祖先节点中的 if 和 while 判断内容

    public static List<String> conditionDetermine(String id,int index) throws XPathExpressionException {
        List<String> conditionStatement = new ArrayList<>();
        Node node = nodeLists.get(id).item(index);
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        String expression = String.format("ancestor::ifStatement/condition/expression");
        XPathExpression identifierExpr = xpath.compile(expression);
        NodeList conditionNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);
        for (int i = 0; i < conditionNodes.getLength(); i++) {

            Node identifierNode = conditionNodes.item(i);


            conditionStatement.add(StringUtil.removeEscape(identifierNode.getTextContent()));

           // System.out.println("IconditionDetermine: " + conditionStatement+"");
        }

        return conditionStatement;
    }

    //获取所有的 modifier node
    public static NodeList modifierNode(String id,int index) throws XPathExpressionException{
        Node node = nodeLists.get(id).item(index);
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression identifierExpr = xpath.compile(" ancestor::contractDefinition//contractPartDefinition[modifierDefinition[matches(text()[1],\"modifier\")]]");

        NodeList identifierNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);
        if (identifierNodes!=null&&identifierNodes.getLength()>0){
            return identifierNodes;
        }else{
            return null;
        }

    }

    //获取所有的 整个抽象语法树的node
    public static Node sourceUnitNode(String id,int index) throws XPathExpressionException{
        Node node = nodeLists.get(id).item(index);
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression identifierExpr = xpath.compile(" ancestor::sourceUnit");

        NodeList identifierNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);
        if (identifierNodes!=null&&identifierNodes.getLength()>0){
            return identifierNodes.item(0);
        }else{
            return null;
        }

    }

    //返回node的 fNodeIndex
    public  static int nodeFNodeIndex(Node node) throws NoSuchFieldException, IllegalAccessException {
       // System.out.println(node.getClass());
        //System.out.println(node.getNodeType() == Node.ELEMENT_NODE);

       // Element target = (Element) node;
        Field field = Node.class.getDeclaredField("fNodeLineNumber");
        field.setAccessible(true); // 设置访问权限，允许访问 protected 变量
        int value = (int) field.get(node); // 获取 protected 变量的值
       // System.out.println("fNOdeIndex "+value);
        return value;

    }

    public static Stream<Node>  nodeTostream(NodeList list){
        Stream<Node>  result = IntStream
                .range(0, list.getLength())
                .mapToObj(list::item);

        return result;

    }

    public static Map<Node, Tree> collectNodes() {
        //Map<Tree, Node> elements = this.elements();
        HashMap<Node, Tree> collect = nodes.entrySet().stream().collect(
                HashMap::new,
                (map, entry) -> map.put(
                        entry.getValue(), entry.getKey()
                ),
                Map::putAll
        );
//            return this.elements().entrySet().stream().collect(
//                    HashMap::new,
//                    (map, entry) -> map.put(
//                            entry.getValue(), entry.getKey()
//                    ),
//                    Map::putAll
//            );
        return collect;
    }

    //在此处的list 长度一定唯一 因为传入的node
    public static Integer nodeToXmlContext(NodeList list){
        Stream<Node> nodeStream = nodeTostream(list);
        Stream<Tree> treeStream = nodeStream.map(collectNodes()::get);
        final Integer[] line = new Integer[1];
        Stream<ParserRuleContext> parserRuleContextStream = treeStream.filter(ParserRuleContext.class::isInstance).map(ParserRuleContext.class::cast);
        //List<ParserRuleContext> infoList = parserRuleContextStream.sorted().collect(Collectors.toList());
        parserRuleContextStream.forEach(
                context -> {
                    line[0] =context.getStart().getLine();
                    //System.out.println(context.getStart().getLine());

                    //System.out.println(line[0] +"linehahahah");
                }
        );
//        for (ParserRuleContext pr:infoList){
//            int line = pr.getStart().getLine();
//            System.out.println(line +"   line");
//        }
        return line[0];
    }

    //返回一个 NodeList的最后一行
    public static Integer NodeStopToXmlContext(NodeList list){
        Stream<Node> nodeStream = nodeTostream(list);
        Stream<Tree> treeStream = nodeStream.map(collectNodes()::get);
        final Integer[] line = new Integer[1];
        Stream<ParserRuleContext> parserRuleContextStream = treeStream.filter(ParserRuleContext.class::isInstance).map(ParserRuleContext.class::cast);
        //List<ParserRuleContext> infoList = parserRuleContextStream.sorted().collect(Collectors.toList());
        parserRuleContextStream.forEach(
                context -> {
                    line[0] =context.getStop().getLine();

//                    System.out.println(line[0] +"line   hahahah");
                }
        );
//        for (ParserRuleContext pr:infoList){
//            int line = pr.getStart().getLine();
//            System.out.println(line +"   line");
//        }
        return line[0];
    }



    //获取当前函数的
    public static NodeList getConstructor(String id,int index) throws XPathExpressionException {
        Node node = nodeLists.get(id).item(index);
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression identifierExpr = xpath.compile(" ancestor::contractDefinition//functionDefinition[matches(text()[1],\"constructor\")]");

        NodeList identifierNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);
        if (identifierNodes!=null&&identifierNodes.getLength()>0){
            return identifierNodes;
        }
        return identifierNodes;


    }

    //从xml文件中 搜索全部为type类型的 状态变量 返回全部的变量名的List
    public static List<String> getVarlition(String filePath,String type) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        List<String> list = new ArrayList<>();
  //      String xmlFilePath = PropertiesUtil.rootPath+"over222.xml";
        String xmlFilePath = "over222.xml";
        String xmlContent = NodeUtil.readXMLFromFile(xmlFilePath);
        // 解析XML文档，获取DOM树
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(xmlContent));
        Document doc = builder.parse(inputSource);

        // 执行XPath查询，找到相应的节点
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        String expression = String.format("//stateVariableDeclaration[typeName/elementaryTypeName[matches(text()[1], \"%s\")]]/identifier",type);
        XPathExpression expr = xpath.compile(expression);
        NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);


        // 输出找到的identifier数据
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node identifierNode = nodeList.item(i);
            list.add(identifierNode.getTextContent());


        }
       // System.out.println(list);
        return list;
    }

    public static NodeList getStateVariable(String id,int index) throws XPathExpressionException{
        Node node = nodeLists.get(id).item(index);
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression identifierExpr = xpath.compile(" ancestor::contractDefinition//stateVariableDeclaration");
        NodeList identifierNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);

        return identifierNodes;

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

    public static String getNodeTextContent(Node node) {
        StringBuilder textContent = new StringBuilder();

        // 遍历节点的子节点
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);

            // 根据节点类型执行不同操作
            switch (childNode.getNodeType()) {
                case Node.ELEMENT_NODE:
                    // 如果是元素节点，递归获取其文本内容
                    if (childNode.getNodeName().equals("stringLiteral")){
                        String nodeValue = childNode.getFirstChild().getNodeValue();
                       // System.out.println("stringLiteral" + " "+nodeValue);
                        // 判断字符串是否已经由双引号包裹
                        if (!nodeValue.startsWith("\"") || !nodeValue.endsWith("\"")) {
                            // 如果没有包裹，添加双引号
                            nodeValue = "\"" + nodeValue + "\"";
                        }
                        System.out.println(nodeValue);
                        childNode.getFirstChild().setNodeValue(nodeValue);
                    }
                    textContent.append(getNodeTextContent(childNode));
                    break;
                case Node.TEXT_NODE:
                case Node.CDATA_SECTION_NODE:
                    // 如果是文本节点或CDATA节点，将其文本内容添加到结果中

                    textContent.append(childNode.getNodeValue()+" ");
                    break;
                // 其他节点类型可以根据需要处理
            }
        }

        return textContent.toString();
    }
}

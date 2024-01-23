package my.smartdec.detect.repaire.util;

import com.ibm.icu.impl.data.ResourceReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;
import java.net.URL;


public class TreeNodeUtil {

    // 此比较默认为与全局变量比较    3 个参数分别表示   第一个 比较变量的 变量名 第二个参数为表示比较的符号   第三个为全局变量
    public static Node getComparisonStatement(String identifier,String comparison,String enviromentalVariable) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        //String xmlFilePath = "D:\\DectionContract\\SolidityDetection-main\\enStavarEqual.xml";

//        String xmlFilePath = PropertiesUtil.rootPath+"XMLTree\\"+"enStavarEqual.xml";
//        String xmlContent = readXMLFromFile(xmlFilePath);


        String xmlFilePath = "XMLTree/"+"enStavarEqual.xml";
        String xmlContent = readXMLFromResource(xmlFilePath);



        // 创建一个DocumentBuilder实例，用于解析XML文档
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // 解析XML字符串，生成Document对象
        InputSource inputSource = new InputSource(new java.io.StringReader(xmlContent));
        Document doc = builder.parse(inputSource);

        // 创建XPath对象
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        XPathExpression expr3 = xpath.compile("//statement");
        Node comparisonStatement = (Node) expr3.evaluate(doc, XPathConstants.NODE);
        comparisonStatementUpdate(comparisonStatement,identifier,comparison,enviromentalVariable);
        //System.out.println(StringUtil.removeEscapeExOne(comparisonStatement.getTextContent()));
        return comparisonStatement;
    }

    private static void comparisonStatementUpdate(Node node,String identifier,String comparison,String enviromentalVariable) {

        if (node.getChildNodes().getLength() == 0||node.getNodeType()==3) {
            return;
        }
       // System.out.println("TypeNmae :"+node.getNodeName()+" Type: "+node.getNodeType()+" value: "+node.getNodeValue());

        switch (node.getNodeName()){
            case "identifier":{
                if (identifier!=null){
                    node.getFirstChild().setNodeValue(" "+identifier+" ");
                }

                break;
            }
            case "comparison":{
                if (comparison!=null){
                    node.getFirstChild().setNodeValue(" "+comparison+" ");
                }

                break;
            }
            case "environmentalVariable":{
                if (enviromentalVariable!=null){
                    node.getFirstChild().setNodeValue(" "+enviromentalVariable+" ");
                }

                break;
            }
        }

        NodeList childNodes = node.getChildNodes();
        for (int i =0;i<childNodes.getLength();i++) {
            comparisonStatementUpdate(childNodes.item(i),identifier,comparison,enviromentalVariable); // 递归遍历子节点
        }
    }


    //获取默认的 modifier树   默认是onlyOwner 修饰函数
    public static Node getModifierNode(String identifier)throws IOException, ParserConfigurationException, SAXException, XPathExpressionException{

        String xmlFilePath = "XMLTree/"+"modifier.xml";
        String xmlContent = readXMLFromResource(xmlFilePath);


        //String xmlFilePath = PropertiesUtil.rootPath+"XMLTree\\"+"modifier.xml";


        //String xmlContent = readXMLFromFile(xmlFilePath);


        // 创建一个DocumentBuilder实例，用于解析XML文档
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // 解析XML字符串，生成Document对象
        InputSource inputSource = new InputSource(new java.io.StringReader(xmlContent));
        Document doc = builder.parse(inputSource);

        // 创建XPath对象
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        XPathExpression expr3 = xpath.compile("//contractPartDefinition[modifierDefinition[matches(text()[1],\"modifier\")]]");
        Node node = (Node) expr3.evaluate(doc, XPathConstants.NODE);
        modifierNodeUpdate(node,identifier);
        return node;

    }

    private static void modifierNodeUpdate(Node node,String identifier) {

        if (node.getChildNodes().getLength() == 0||node.getNodeType()==3) {
            return;
        }
       // System.out.println("TypeNmae :"+node.getNodeName()+" Type: "+node.getNodeType()+" value: "+node.getNodeValue());

        switch (node.getNodeName()){
            case "identifier":{
                if (identifier!=null){
                    node.getFirstChild().setNodeValue(" "+identifier+" ");
                }
                break;
            }

        }

        NodeList childNodes = node.getChildNodes();
        for (int i =0;i<childNodes.getLength();i++) {
            modifierNodeUpdate(childNodes.item(i),identifier); // 递归遍历子节点
        }
    }

    //这一部分主要用户获取 require的树 erroeContext是require的报错提示内容
    public static Node getRequireNode(String erroeContext) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        //String xmlFilePath = "D:\\DectionContract\\SolidityDetection-main\\require.xml";

//        String xmlFilePath = PropertiesUtil.rootPath+"XMLTree\\"+"require.xml";
//
//
//        String xmlContent = readXMLFromFile(xmlFilePath);



        String xmlFilePath = "XMLTree/"+"require.xml";


        String xmlContent = readXMLFromResource(xmlFilePath);

        // 创建一个DocumentBuilder实例，用于解析XML文档
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // 解析XML字符串，生成Document对象
        InputSource inputSource = new InputSource(new java.io.StringReader(xmlContent));
        Document doc = builder.parse(inputSource);

        // 创建XPath对象
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        XPathExpression expr3 = xpath.compile("//statement");
        Node node = (Node) expr3.evaluate(doc, XPathConstants.NODE);
        requireNodeUpdate(node,erroeContext);

        return node;
    }

    private static void requireNodeUpdate(Node node,String erroeContext) {

        if (node.getChildNodes().getLength() == 0||node.getNodeType()==3) {
            return;
        }
        //System.out.println("TypeNmae :"+node.getNodeName()+" Type: "+node.getNodeType()+" value: "+node.getNodeValue());

        switch (node.getNodeName()){
            case "stringLiteral":{
                if (erroeContext!=null){
                    node.getFirstChild().setNodeValue(" "+erroeContext+" ");
                }

                break;
            }

        }

        NodeList childNodes = node.getChildNodes();
        for (int i =0;i<childNodes.getLength();i++) {
            requireNodeUpdate(childNodes.item(i),erroeContext); // 递归遍历子节点
        }
    }

    public static Node getConstructorNode() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {


        return getNode("XMLTree/"+"constructor.xml","//functionDefinition[matches(text()[1],\"constructor\")]");
       // return getNode(PropertiesUtil.rootPath+"XMLTree\\"+"constructor.xml","//functionDefinition[matches(text()[1],\"constructor\")]");

    }

    //获取 赋值语句  类如 a = b  这里默认 b为msg.sender
    public static Node getAssignmentNode(String identifier,String enviromentalVariable)throws IOException, ParserConfigurationException, SAXException, XPathExpressionException{
        //Node node = getNode(PropertiesUtil.rootPath+"XMLTree\\"+"stateVariable_magsender.xml", "//statement");


        Node node = getNode("XMLTree/"+"stateVariable_magsender.xml", "//statement");

        assignmentNodeUpdate(node,identifier,enviromentalVariable);

        return node;
    }

    private static void assignmentNodeUpdate(Node node,String identifier,String environmentalVariable) {

        if (node.getChildNodes().getLength() == 0||node.getNodeType()==3) {
            return;
        }
        //System.out.println("TypeNmae :"+node.getNodeName()+" Type: "+node.getNodeType()+" value: "+node.getNodeValue());

        switch (node.getNodeName()){
            case "identifier":{
                if (identifier!=null){
                    node.getFirstChild().setNodeValue(" "+identifier+" ");
                }
                break;
            }
            case "environmentalVariable":{
                if (environmentalVariable!=null){
                    node.getFirstChild().setNodeValue(" "+identifier+" ");
                }
                break;
            }

        }

        NodeList childNodes = node.getChildNodes();
        for (int i =0;i<childNodes.getLength();i++) {
            assignmentNodeUpdate(childNodes.item(i),identifier,environmentalVariable); // 递归遍历子节点
        }
    }

     //获取状态便变量节点  默认是 address类型   第一个参数表示变量的类型 第二个参数 这个变量的可见形式
     public static Node getStateVariableNode(String Type,String visibleType,String identifier) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException{


         Node node = getNode("XMLTree/"+"stateVariable.xml", "//contractPartDefinition");

         //Node node = getNode(PropertiesUtil.rootPath+"XMLTree\\"+"stateVariable.xml", "//contractPartDefinition");
         stateVariableNodeUpdate(node,Type,visibleType,identifier);

         return node;
     }


    private static void stateVariableNodeUpdate(Node node,String Type,String visibleType,String identifier) {

        if (node.getChildNodes().getLength() == 0||node.getNodeType()==3) {
            return;
        }
        //System.out.println("TypeNmae :"+node.getNodeName()+" Type: "+node.getNodeType()+" value: "+node.getNodeValue());

        switch (node.getNodeName()){
            case "elementaryTypeName":{
                if (Type!=null){
                    node.getFirstChild().setNodeValue(" "+Type+" ");
                }
                break;
            }
            case "visibleType":{
                if (visibleType!=null){
                    node.getFirstChild().setNodeValue(" "+visibleType+" ");
                }
                break;
            }
            case "identifier":{
                if (identifier!=null){
                    node.getFirstChild().setNodeValue(" "+identifier+" ");
                }
                break;
            }

        }

        NodeList childNodes = node.getChildNodes();
        for (int i =0;i<childNodes.getLength();i++) {
            stateVariableNodeUpdate(childNodes.item(i),Type,visibleType,identifier); // 递归遍历子节点
        }
    }

    private static void importValueUpdate(Node node,String newValue,String NodeType) {

        if (node.getChildNodes().getLength() == 0) {
            return;
        }
        if (node.getNodeName().equals(NodeType)){
            if (newValue!=null){
                node.getFirstChild().setNodeValue(newValue);
            }
        }


    }

    private static void libiaryValueUpdate(Node node,String idenfier) {

        if (node.getChildNodes().getLength() == 0) {
            return;
        }


        switch (node.getNodeName()){

            case "identifier":{
                if (idenfier!=null){
                    node.getFirstChild().setNodeValue(idenfier);
                }
                break;
            }

        }

        NodeList childNodes = node.getChildNodes();
        for (int i =0;i<childNodes.getLength();i++) {
            libiaryValueUpdate(childNodes.item(i),idenfier); // 递归遍历子节点
        }


    }

    public static Node getIdenfierNode(String identifier) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
       // Node node = getNode(PropertiesUtil.rootPath+"XMLTree\\"+"idenfier.xml", "//identifier");


        Node node = getNode("XMLTree/"+"idenfier.xml", "//identifier");

        stateVariableNodeUpdate(node,identifier,null,null);
        return node;
    }

    public static Node getImportNode(String s) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        //Node node = getNode(PropertiesUtil.rootPath+"XMLTree\\importDirective.xml", "//importDirective");


        Node node = getNode("XMLTree/importDirective.xml", "//importDirective");


        importValueUpdate(node,s,"importDirective");
        return node;
    }

    public static Node getLibiaryNode(String identifier) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        //Node node = getNode(PropertiesUtil.rootPath+"XMLTree\\usingForDeclaration.xml", "//contractPartDefinition");


        Node node = getNode("XMLTree/usingForDeclaration.xml", "//contractPartDefinition");
        libiaryValueUpdate(node,identifier);
        return node;
    }

    public static Node getPrimaryExpressionNode(String identifier) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        //Node node = getNode(PropertiesUtil.rootPath+"XMLTree\\primaryExpression.xml", "//expression");

        Node node = getNode("XMLTree/primaryExpression.xml", "//expression");

        stateVariableNodeUpdate(node,null,null,identifier);
        return node;
    }

    public static Node getvariableDeclaritionExpressionNode(String identifier,String type) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        //Node node = getNode(PropertiesUtil.rootPath+"XMLTree\\variableDeclaration.xml", "//expression");


        Node node = getNode("XMLTree/variableDeclaration.xml", "//expression");

        stateVariableNodeUpdate(node,type,null,identifier);
        return node;
    }


    //filepath 表达的是 xml文件   xpath表示搜索语句  这里搜索出的结果一定只有一个！
    private static Node getNode(String filePath,String xpathCompile) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException{
        //String xmlFilePath = "D:\\DectionContract\\SolidityDetection-main\\require.xml";


        String xmlContent = readXMLFromResource(filePath);



        //String xmlContent = readXMLFromFile(filePath);

        // 创建一个DocumentBuilder实例，用于解析XML文档
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // 解析XML字符串，生成Document对象
        InputSource inputSource = new InputSource(new java.io.StringReader(xmlContent));
        Document doc = builder.parse(inputSource);

        // 创建XPath对象
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        XPathExpression expr3 = xpath.compile(xpathCompile);
        return (Node) expr3.evaluate(doc, XPathConstants.NODE);
    }

    public static String readXMLFromResource(String resourcePath) throws IOException {
        StringBuilder content = new StringBuilder();
        ClassLoader classLoader =  ResourceReader.class.getClassLoader();



        URL resourceUrl =classLoader.getResource(resourcePath);

        try (InputStream is = resourceUrl.openStream()){
            if (is == null) {
                throw new FileNotFoundException("Resource not found: " + resourcePath);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append(System.lineSeparator());
                }
            }
        }


        // 使用类加载器来获取资源作为InputStream
//        try (InputStream is = ResourceReader.class.getClassLoader().getResourceAsStream(resourcePath)) {
//            if (is == null) {
//                throw new FileNotFoundException("Resource not found: " + resourcePath);
//            }
//            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    content.append(line).append(System.lineSeparator());
//                }
//            }
//        }
        return content.toString();
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

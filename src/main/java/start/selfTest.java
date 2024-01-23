package start;

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

public class selfTest {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
//        try {
//            // 读取XML文件内容
        String xmlFilePath = "D:\\DectionContract\\SolidityDetection-main\\over222.xml";
        //String xmlFilePath = "D:\\DectionContract\\SolidityDetection-main\\over222.xml";
        String xmlContent = readXMLFromFile(xmlFilePath);

        // 创建一个DocumentBuilder实例，用于解析XML文档
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // 解析XML字符串，生成Document对象
        InputSource inputSource = new InputSource(new java.io.StringReader(xmlContent));
        Document doc = builder.parse(inputSource);

        // 创建XPath对象
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        XPathExpression expr = xpath.compile("//expression[matches(text()[1],\"=\")]");
        // 执行XPath查询
//            XPathExpression expr = xpath.compile(" //muldivOperator[divOperator]/following-sibling::expression[1][not(\n" +
//                    "                    .//identifier = ancestor::ifStatement/condition//expression[comparison][expression//decimalNumber = \"0\"]//expression/primaryExpression/identifier)]");
        NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        Node temp = nodeList.item(0);

        System.out.println(temp.getTextContent());


//        XPathExpression identifierExpr = xpath.compile("ancestor::statement/following-sibling::statement//callArguments/expression/primaryExpression/identifier");
//        NodeList identifierNodes = (NodeList) identifierExpr.evaluate(temp, XPathConstants.NODESET);
//
//        for (int i =0;i<nodeList.getLength();i++){
//            Node n = nodeList.item(i);
//            System.out.println(StringUtil.removeEscapeExOne(n.getTextContent()));
//            System.out.println("xinde "+ i);
//
//
////
//
//        }

//        try {
//            TransformerFactory tf = TransformerFactory.newInstance();
//            Transformer transformer = tf.newTransformer();
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//
//            DOMSource source = new DOMSource(n1);
//            StreamResult result1 = new StreamResult(new File("n5.xml"));
//
//            transformer.transform(source, result1);
//
//        } catch (TransformerException e) {
//            // Handle exception
//            e.printStackTrace();
//        }


//        TypeNmae :contractPartDefinition Type: 1 value: null
//        TypeNmae :#text Type: 3 value:
//
//        TypeNmae :stateVariableDeclaration Type: 1 value: null
//        TypeNmae :#text Type: 3 value:
//
//        TypeNmae :typeName Type: 1 value: null
//        TypeNmae :#text Type: 3 value:
//
//        TypeNmae :elementaryTypeName Type: 1 value: null
//        TypeNmae :#text Type: 3 value: bool
//        TypeNmae :#text Type: 3 value:
//
//        TypeNmae :#text Type: 3 value:
//
//        TypeNmae :visibleType Type: 1 value: null
//        TypeNmae :#text Type: 3 value: public
//        TypeNmae :#text Type: 3 value:
//
//        TypeNmae :identifier Type: 1 value: null
//        TypeNmae :#text Type: 3 value: a
//        TypeNmae :#text Type: 3 value: =
//        TypeNmae :expression Type: 1 value: null
//        TypeNmae :#text Type: 3 value:
//
//        TypeNmae :primaryExpression Type: 1 value: null
//        TypeNmae :#text Type: 3 value:
//
//        TypeNmae :booleanLiteral Type: 1 value: null
//        TypeNmae :#text Type: 3 value: false
//        TypeNmae :#text Type: 3 value:
//
//        TypeNmae :#text Type: 3 value:
//
//        TypeNmae :#text Type: 3 value: ;
//        TypeNmae :#text Type: 3 value:



    }

    public static void digui(Node node) {
        System.out.println("TypeNmae :"+node.getNodeName()+" Type: "+node.getNodeType()+" value: "+node.getNodeValue());
        if (node.getChildNodes().getLength() == 0) {
            return;
        }

        // 输出节点内容
//        if (node.getNodeName().equals("identifier")){
//            System.out.println("zai");
//            node.getFirstChild().setNodeValue(" haha ");
//        }

        NodeList childNodes = node.getChildNodes();
        for (int i =0;i<childNodes.getLength();i++) {
            digui(childNodes.item(i)); // 递归遍历子节点
        }
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

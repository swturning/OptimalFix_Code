package start;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class getTreeBody {

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

        XPathExpression expr = xpath.compile(".//tupleExpression/expression[variableDeclaration]");
        // 执行XPath查询
//            XPathExpression expr = xpath.compile(" //muldivOperator[divOperator]/following-sibling::expression[1][not(\n" +
//                    "                    .//identifier = ancestor::ifStatement/condition//expression[comparison][expression//decimalNumber = \"0\"]//expression/primaryExpression/identifier)]");
        NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        Node node = nodeList.item(0);

        //NodeList identifierNodes = (NodeList) identifierExpr1.evaluate(node, XPathConstants.NODESET);


        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(node);
            StreamResult result1 = new StreamResult(new File("variableDeclaration.xml"));

            transformer.transform(source, result1);
        } catch (TransformerException e) {
            // Handle exception
            e.printStackTrace();
        }
//
//
//
//        Node blockNode = null;
//
//        Node statementNode = null;
//
//        Node modefierItem = node.getChildNodes().item(1);
//
//        NodeList childNodes = modefierItem.getChildNodes();
//
//        for (int i =0;i<childNodes.getLength(); i++){
//            Node n = childNodes.item(i);
//
//            if (n.getNodeName().equals("block")){
//                blockNode = n;
//                break;
//            }
//
//        }
//
//        xmlFilePath = "D:\\DectionContract\\SolidityDetection-main\\require.xml";
//
//        xmlContent = readXMLFromFile(xmlFilePath);
//
//        InputSource inputSource2 = new InputSource(new java.io.StringReader(xmlContent));
//
//        Document doc2 = builder.parse(inputSource2);
//
//        XPathExpression expr2 = xpath.compile("//statement");
//        //NodeList nodeList2 = (NodeList) expr2.evaluate(doc2, XPathConstants.NODESET);
//        Node nodeList2 = (Node) expr2.evaluate(doc2, XPathConstants.NODE);
//
//        XPathExpression identifierExpr = xpath.compile(".//callArguments/expression");
//
//        NodeList identifierNodes = (NodeList) identifierExpr.evaluate(nodeList2, XPathConstants.NODESET);
//
//        for (int i =0;i<identifierNodes.getLength();i++){
//
//            Node n = identifierNodes.item(i);
//
//            System.out.println(i+"rerquire: "+StringUtil.removeEscapeExOne(n.getTextContent()));
//
//        }
//
//
//        xmlFilePath = "D:\\DectionContract\\SolidityDetection-main\\enStavarEqual.xml";
//
//        xmlContent = readXMLFromFile(xmlFilePath);
//
//        InputSource inputSource3 = new InputSource(new java.io.StringReader(xmlContent));
//
//        Document doc3 = builder.parse(inputSource3);
//
//        XPathExpression expr3 = xpath.compile("//statement");
//        Node equalStatement = (Node) expr3.evaluate(doc3, XPathConstants.NODE);
//        System.out.println(StringUtil.removeEscapeExOne(equalStatement.getTextContent()));
//        digui(equalStatement);
//        System.out.println(StringUtil.removeEscapeExOne(equalStatement.getTextContent()));

//        Node node2 = nodeList2.item(0);
//
//
//        NodeList childNodes1 = blockNode.getChildNodes();
//        for (int i =0;i<childNodes1.getLength(); i++){
//            Node n = childNodes1.item(i);
//            if (n.getNodeName().equals("statement")){
//                statementNode = n;
//                break;
//            }
//
//
//        }


//        Node importedTemp = doc.importNode(node2, true);
//        blockNode.insertBefore(importedTemp,statementNode);




    }

    public static void digui(Node node) {
        System.out.println("TypeNmae :"+node.getNodeName()+" Type: "+node.getNodeType()+" value: "+node.getNodeValue());
        if (node.getChildNodes().getLength() == 0) {
            return;
        }

         // 输出节点内容
        if (node.getNodeName().equals("identifier")){
            System.out.println("zai");
            node.getFirstChild().setNodeValue(" haha ");
        }

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

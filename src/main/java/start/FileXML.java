package start;
import my.smartdec.detect.repaire.util.StringUtil;
import org.w3c.dom.*;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;

public class FileXML {

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

        XPathExpression expr = xpath.compile("//muldivOperator[divOperator]/following-sibling::expression[1]    [./primaryExpression/identifier = ancestor::functionDefinition/parameterList//parameter/identifier\n" +
                "               or expression[2]/primaryExpression/numberLiteral\n" +
                "               or ./primaryExpression/identifier = ancestor::contractDefinition//stateVariableDeclaration[not(contains(text()[1], \"=\"))]/identifier\n" +
                "               ]");

        // 执行XPath查询
//            XPathExpression expr = xpath.compile(" //muldivOperator[divOperator]/following-sibling::expression[1][not(\n" +
//                    "                    .//identifier = ancestor::ifStatement/condition//expression[comparison][expression//decimalNumber = \"0\"]//expression/primaryExpression/identifier)]");
        NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        Node temp = nodeList.item(0);
        System.out.println(temp.getTextContent());

        XPathExpression identifierExpr2 = xpath.compile("ancestor::contractDefinition//functionDefinition[matches(text()[1], \"constructor\")]/block/statement//expressionStatement/expression[not(contains(text()[1], \"=\"))]");

        XPathExpression identifierExpr = xpath.compile(".//identifier");
        NodeList identifierNodes = (NodeList) identifierExpr2.evaluate(temp, XPathConstants.NODESET);
            for (int i =0;i<identifierNodes.getLength();i++){
                Node n = identifierNodes.item(i);
                System.out.println(StringUtil.removeEscape(n.getTextContent()));
            }


//        try {
//            TransformerFactory tf = TransformerFactory.newInstance();
//            Transformer transformer = tf.newTransformer();
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//
//            DOMSource source = new DOMSource(node);
//            StreamResult result1 = new StreamResult(new File("usingForDeclaration.xml"));
//
//            transformer.transform(source, result1);
//        } catch (TransformerException e) {
//            // Handle exception
//            e.printStackTrace();
//        }
//            Node blockNode = null;
//            Node statementNode = null;
//            NodeList childNodes = node.getChildNodes();
//            for (int i =0;i<childNodes.getLength(); i++){
//                Node n = childNodes.item(i);
//
//                if (n.getNodeName().equals("block")){
//                    blockNode = n;
//                    break;
//                }
//
//            }
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
////
//
//
//            xmlFilePath = "D:\\DectionContract\\SolidityDetection-main\\stateVariable_magsender.xml";
//            xmlContent = readXMLFromFile(xmlFilePath);
//            InputSource inputSource2 = new InputSource(new java.io.StringReader(xmlContent));
//            Document doc2 = builder.parse(inputSource2);
//            XPathExpression expr2 = xpath.compile("//statement");
//            NodeList nodeList2 = (NodeList) expr2.evaluate(doc2, XPathConstants.NODESET);
//            Node node2 = nodeList2.item(0);
//
//            Node importedTemp = doc.importNode(node2, true);
//            blockNode.insertBefore(importedTemp,statementNode);
//            blockNode.removeChild(statementNode);
//            System.out.println("block:"+StringUtil.removeEscape(node.getTextContent()));
//
////            try {
////                TransformerFactory tf = TransformerFactory.newInstance();
//                Transformer transformer = tf.newTransformer();
//                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//
//                DOMSource source = new DOMSource(ss);
//                StreamResult result1 = new StreamResult(new File("constructor.xml"));
//
//                transformer.transform(source, result1);
//
//            } catch (TransformerException e) {
//                // Handle exception
//                e.printStackTrace();
//            }



//            XPathExpression identifierExpr = xpath.compile(".//identifier");
//            NodeList identifierNodes = (NodeList) identifierExpr.evaluate(nodeList, XPathConstants.NODESET);

            // 处理查询结果

//            Node node = nodeList.item(0);
//
//            System.out.println(StringUtil.removeEscape(node.getTextContent()));

//
//            String nodeText = node.getTextContent();
//            nodeText = StringUtil.removeEscape(nodeText);
//            Node temp = node.cloneNode(true);
//            temp.getChildNodes().item(0).setNodeValue(" OnlyMy ");





//
//            xmlFilePath = "D:\\DectionContract\\SolidityDetection-main\\stateVariable_magsender.xml";
//            xmlContent = readXMLFromFile(xmlFilePath);
//            InputSource inputSource2 = new InputSource(new java.io.StringReader(xmlContent));
//            Document doc2 = builder.parse(inputSource2);
//            XPathExpression expr2 = xpath.compile("//statement");
//            NodeList nodeList2 = (NodeList) expr2.evaluate(doc2, XPathConstants.NODESET);
//
//            Node node2 = nodeList2.item(0);
//
//            Node blockNode = null;
//
//            NodeList childNodes = node.getChildNodes();
//            for (int i =0;i<childNodes.getLength(); i++){
//                Node n = childNodes.item(i);
//                System.out.println("N:"+n.getNodeType());
//                System.out.println("zifu+ "+n.getNodeName());
//                if (n.getNodeName().equals("block")){
//                    blockNode = n;
//                    break;
//                }
//
//            }
//
//            NodeList childNodes1 = blockNode.getChildNodes();
//            for (int i =0;i<childNodes1.getLength(); i++){
//                Node n = childNodes1.item(i);
//                System.out.println("N:"+n.getNodeType());
//                System.out.println("zifu+ "+n.getNodeName());
//
//
//            }
//            Node importedTemp = doc.importNode(node2, true);
//            blockNode.appendChild(importedTemp);
//            System.out.println("block:"+StringUtil.removeEscape(blockNode.getTextContent()));


//            for (int i = 0; i < childNodes.getLength(); i++) {
//                Node n = childNodes.item(i);
//                System.out.println(n.get);
//                String nodeText = node.getTextContent();
//                nodeText = nodeText.replaceAll("\\s+", " ").trim();
//                System.out.println("Identifier: " + nodeText);
//            }
           // System.out.println(StringUtil.removeEscape(node2.getTextContent()));
//
//
//
//
//            Node node2 = nodeList2.item(0);
//            NodeList functionChild = node2.getChildNodes();
//            Node visibleNode = null;
//            for (int i =0;i<functionChild.getLength();i++){
//                if (functionChild.item(i).getNodeName().equals("visibleType")){
//                    visibleNode = functionChild.item(i);
//                    break;
//                }
//            }
//            System.out.println("Identifier: " + StringUtil.removeEscapeExOne(node2.getTextContent())+"   hahahah");
//            Node importedTemp = doc2.importNode(temp, true);
//            if (visibleNode !=null){
//                node2.insertBefore(importedTemp,visibleNode);
//            }
           // NodeList childNodes = node2.getChildNodes();

//            System.out.println();
            //System.out.println("Identifier: " + StringUtil.removeEscapeExOne(node2.getTextContent())+"   hahahah");

            // 处理查询结果
//            for (int i = 0; i < identifierNodes.getLength(); i++) {
//                Node node = identifierNodes.item(i);
//                node.normalize();
//                String nodeText = node.getTextContent();
//                nodeText = nodeText.replaceAll("\\s+", " ").trim();
//                System.out.println("Identifier: " + nodeText);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    // 获取节点所在的行号
    private static int getLineNumber(Node node) {
        if (node instanceof Element) {
            Document ownerDoc = node.getOwnerDocument();
            if (ownerDoc != null) {
                DOMImplementationLS impl = (DOMImplementationLS) ownerDoc.getImplementation();
                LSSerializer serializer = impl.createLSSerializer();
                serializer.getDomConfig().setParameter("format-pretty-print", false);
                LSOutput output = impl.createLSOutput();
                output.setByteStream(new ByteArrayOutputStream());
                serializer.write(node, output);
                String xmlString = output.getByteStream().toString();
                int lineNumber = 1;
                for (int i = 0; i < xmlString.length(); i++) {
                    if (xmlString.charAt(i) == '\n') {
                        lineNumber++;
                    }
                }
                return lineNumber;
            }
        }
        return -1; // 如果无法获取行号，返回-1
    }
    // 读取XML文件内容并返回字符串
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

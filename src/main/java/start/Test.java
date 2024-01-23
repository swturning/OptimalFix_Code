package start;

import my.smartdec.detect.repaire.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

import static start.FileXML.readXMLFromFile;

public class Test {
    public static void main(String[] args) throws Exception {
      //  String xml = "<root><expression><identifier>a</identifier><identifier>b</identifier></expression></root>";
        String xmlFilePath = "D:\\DectionContract\\SolidityDetection-main\\over222.xml";
        String xmlContent = readXMLFromFile(xmlFilePath);
        // 解析XML文档，获取DOM树
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(xmlContent));
        Document doc = builder.parse(inputSource);

        // 执行XPath查询，找到相应的节点
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression expr = xpath.compile("//expression[ancestor::contractDefinition][ancestor::functionDefinition[not(visibleType = \"internal\" or visibleType = \"private\")]]\n" +
                "               [lvalueOperator/minusLvalueOperator or lvalueOperator/mulLvalueOperator or lvalueOperator/plusLvalueOperator]\n" +
                "               [not(expression[2]/primaryExpression/numberLiteral)]\n" +
                "\n" +
                "               [\n" +
                "               expression[1]/expression[1]/primaryExpression/identifier\n" +
                "               [\n" +
                "               text()[1] = (ancestor::functionDefinition//variableDeclaration[typeName/elementaryTypeName[matches(text()[1], \"uint|int\")]]/identifier)\n" +
                "               or text()[1]= (ancestor::contractDefinition//stateVariableDeclaration[typeName/elementaryTypeName[matches(text()[1], \"uint|int\")]]/identifier)\n" +
                "               or text()[1] = (ancestor::functionDefinition/parameterList/parameter[typeName/elementaryTypeName[matches(text()[1], \"uint|int\")]]/identifier)\n" +
                "\n" +
                "               ]\n" +
                "               or\n" +
                "               expression[1]/primaryExpression/identifier\n" +
                "               [\n" +
                "               text()[1] = (ancestor::functionDefinition//variableDeclaration[typeName/elementaryTypeName[matches(text()[1], \"uint|int\")]]/identifier)\n" +
                "               or text()[1]= (ancestor::contractDefinition//stateVariableDeclaration[typeName/elementaryTypeName[matches(text()[1], \"uint|int\")]]/identifier)\n" +
                "               or text()[1] = (ancestor::functionDefinition/parameterList/parameter[typeName/elementaryTypeName[matches(text()[1], \"uint|int\")]]/identifier)\n" +
                "               ]\n" +
                "               or\n" +
                "               expression[2]/primaryExpression/identifier\n" +
                "               [\n" +
                "               text()[1] = (ancestor::functionDefinition//variableDeclaration[typeName/elementaryTypeName[matches(text()[1], \"uint|int\")]]/identifier)\n" +
                "               or text()[1]= (ancestor::contractDefinition//stateVariableDeclaration[typeName/elementaryTypeName[matches(text()[1], \"uint|int\")]]/identifier)\n" +
                "               or text()[1] = (ancestor::functionDefinition/parameterList/parameter[typeName/elementaryTypeName[matches(text()[1], \"uint|int\")]]/identifier)\n" +
                "               ]\n" +
                "               ]");
        NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

        Node n = nodeList.item(0);
        XPathExpression identifierExpr = xpath.compile("./expression[1]");
        NodeList identifierNodes = (NodeList) identifierExpr.evaluate(n, XPathConstants.NODESET);
        // 在已找到的节点上搜索子节点的identifier数据
//        XPathExpression identifierExpr = xpath.compile("preceding-sibling::expression[last()]");
//        NodeList identifierNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);

        // 输出找到的identifier数据
        for (int i = 0; i < identifierNodes.getLength(); i++) {
            Node identifierNode = identifierNodes.item(i);

            System.out.println( StringUtil.removeEscape(identifierNode.getTextContent()));
        }
    }
}

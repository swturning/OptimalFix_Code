package my.smartdec.detect.repaire.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;

public class NodeCopyUtil {

    public static Node sourceNode = null;

    public static Boolean isTreeRepaire = false;

    //插入temp 节点前插入
    public static void insertBeforeNode(Node partent ,Node temp,Node newNode) throws XPathExpressionException {
//        XPathFactory xPathFactory = XPathFactory.newInstance();
//        XPath xpath = xPathFactory.newXPath();
//        XPathExpression identifierExpr = xpath.compile("ancestor::sourceUnit/contractDefinition");
//        NodeList identifierNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);
//
//
//        if (identifierNodes.getLength()>0){
//            return identifierNodes;
//        }else {
//            return null;

        Document ownerDocument=partent.getOwnerDocument();
        Node importedTemp1 = ownerDocument.importNode(newNode, true);;
        partent.insertBefore(importedTemp1,temp);



    }

    //只返回一个
    public static Node getNode(String xpathExpression,Node node,int index) throws XPathExpressionException {

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression identifierExpr = xpath.compile(xpathExpression);
        NodeList identifierNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);


        if (identifierNodes.getLength() > 0) {
            return identifierNodes.item(index);
        } else {
            return null;
        }
    }

    public static NodeList getNodelist(String xpathExpression,Node node) throws XPathExpressionException {

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression identifierExpr = xpath.compile(xpathExpression);
        NodeList evaluate = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);
        //NodeList identifierNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);
        return evaluate;


    }




}

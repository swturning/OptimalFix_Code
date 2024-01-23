package my.smartdec.detect.app.cli;



import com.google.gson.Gson;
import my.smartdec.detect.repaire.AutoRepaire;
import my.smartdec.detect.repaire.PatternRepaire;
import my.smartdec.detect.repaire.entity.ErrorReport;
import my.smartdec.detect.repaire.entity.RepaireDetail;
import my.smartdec.detect.repaire.util.NodeUtil;
import my.smartdec.detect.repaire.util.PropertiesUtil;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class RepaireAPI {

    private final String xmlPath;

    private final String  loactedXpath;

    public RepaireAPI(String xml, String loactedXpath) {
        this.xmlPath = xml;
        this.loactedXpath = loactedXpath;
    }

    public static void main(final String... args) throws Exception {
        ArgumentsDefault arguments = new ArgumentsDefault(args);

        Optional<String> xmlValue = arguments.value("-p", "--xpath");
        String xmlString = null;
        if (xmlValue.isPresent()) {
            xmlString = xmlValue.get();
        }


        // save the patched contract txt
        PropertiesUtil.init("test/temptest.sol");

        Optional<String> value = arguments.value("-p", "--xpath");
        String stringValue = null;


        if (value.isPresent()) {
            stringValue = value.get();
        }
        new RepaireAPI(xmlString, stringValue).run();
    }

    public void run() throws Exception {



        String xmlContent = NodeUtil.readXMLFromFile(xmlPath);


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(xmlContent));
        Document doc = builder.parse(inputSource);

        // 执行XPath查询，找到相应的节点
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();

        //located the vul in the ast
        XPathExpression expr = xpath.compile("  //functionCall[functionName/identifier[matches(text()[1], \"require|assert|revert\")]]\n" +
                "               [\n" +
                "               callArguments//expression[comparison]//environmentalVariable[matches(text()[1],\"^tx.origin$\")]/parent::expression[parent::expression[not(comparison and expression/environmentalVariable[matches(text()[1],\"^msg.sender$\")])]]\n" +
                "               ]/callArguments//expression[comparison]");
        NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        ErrorReport errorReport = genErrorReport();
        NodeUtil.nodeLists.put(errorReport.getPattern(),nodeList);


        List<RepaireDetail> detaiList = new ArrayList<>();
        //patch
        PatternRepaire patternRepaire = new PatternRepaire();
        List<RepaireDetail> repaireResult = patternRepaire.getRepaireResult(errorReport);
        if (repaireResult!=null){
            detaiList.addAll(repaireResult) ;
        }

        Collections.sort(detaiList);

        AutoRepaire autoRepaire =new AutoRepaire();
        autoRepaire.autoRepaire(detaiList,null);



    }

    public ErrorReport genErrorReport() {
        //Provide relevant information about the vulnerability report.
        String jsonString =
                "{" +
                        "    \"pattern\": \"sto002\"," +
                        "    \"index\": 0," +
                        "    \"level\": 3," +
                        "    \"startLine\": 9," +
                        "    \"stopLine\": 9," +
                        "    \"context\": \"tx.origin==owner\"" +
                        "}";

        Gson gson = new Gson();
        ErrorReport vulnerability = null;
        try {
            vulnerability = gson.fromJson(jsonString, ErrorReport.class);
            // 现在可以使用vulnerability对象
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vulnerability;


    }







//        // create a CharStream that reads from standard input
//        CharStream input = CharStreams.fromFileName("start/test.sol");
//        // create a lexer that feeds off of input CharStream
//        SolidityLexer lexer = new SolidityLexer(input);
//        // create a buffer of tokens pulled from the lexer
//        CommonTokenStream tokens = new CommonTokenStream(lexer);
//        // create a parser that feeds off the tokens buffer
//        SolidityParser parser = new SolidityParser(tokens);
//
//        ParseTree tree = parser.sourceUnit(); // begin parsing at init rule
//        System.out.println(tree.toStringTree(parser)); // print LISP-style tree
//
//        //show AST in GUI
//        TreeViewer viewer = new TreeViewer(Arrays.asList(
//                parser.getRuleNames()),tree);
//        viewer.open().get().setVisible(true);

}


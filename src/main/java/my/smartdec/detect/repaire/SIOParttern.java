package my.smartdec.detect.repaire;

import lombok.Data;
import my.smartdec.detect.app.cli.DetectionOne;
import my.smartdec.detect.repaire.entity.ErrorReport;
import my.smartdec.detect.repaire.entity.RepaireDetail;
import my.smartdec.detect.repaire.util.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class SIOParttern {

    public boolean isSafeMath;

    private Integer line;


    private ErrorReport er;

    public SIOParttern(ErrorReport er,boolean ifSafeMath){
        this.er = er;
        this.line = er.getStartLine();
        this.isSafeMath = ifSafeMath;
    }



    public List<RepaireDetail> SIO001F() throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        if (DetectionOne.IntergerVul>5){
            return SIO001();
        }else {
            Node node = NodeUtil.getNode(er.getPattern(), er.getIndex());

            List<String> symbols = spiltSio003Expression(node);
            if (symbols.size()!=4){
                System.out.println("reapire error");
                return null;
            }
            List<RepaireDetail> repaireDetail1s = new ArrayList<>();
            repaireDetail1s.addAll(judgeTwoToRequire(symbols));
            return repaireDetail1s;
        }
    }

    public List<RepaireDetail> SIO001() throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        Node node = NodeUtil.getNode(er.getPattern(), er.getIndex());

        List<String> symbols = spiltSio003Expression(node);
        if (symbols.size()!=4){
            System.out.println("reapire error");
            return null;
        }
        List<RepaireDetail> repaireDetail1s = new ArrayList<>();

        repaireDetail1s.addAll(judgeTwoToSafeMath(symbols));

        if (!this.isSafeMath ){
            String ifImport = judgeSafeMath();
            if (ifImport == null){

                //添加这个 import相关库
                Node importNode = TreeNodeUtil.getImportNode("import \"./SafeMath.sol\";");

                //确定这个树添加在什么位置
                //Node temp = NodeCopyUtil.getNode("//sourceUnit/contractDefinition", er.getErrorNode(), 0);
                //Node temp = NodeCopyUtil.getNode("./contractDefinition", NodeCopyUtil.sourceNode, 0);
                NodeList pragmaDirectiveNodes = NodeCopyUtil.getNodelist("./pragmaDirective", NodeCopyUtil.sourceNode);
                if (pragmaDirectiveNodes.getLength()==0){
                    System.out.println("dont have the version of the contract：");
                    System.exit(1);
                }


                Node temp = pragmaDirectiveNodes.item(0).getNextSibling();
                //Node pareant = NodeCopyUtil.getNode("//sourceUnit",er.getErrorNode(),0);
                Node pareant = NodeCopyUtil.sourceNode;


                //p 父亲节点  temp后于i个  插入的
                NodeCopyUtil.insertBeforeNode(pareant,temp,importNode);


                RepaireDetail importRe = new RepaireDetail("virtual",1,null,null);
                repaireDetail1s.add(importRe);

                //添加using 库

                //获取当前函数的FunctionName
                String contractName = NodeUtil.getContractName(er.getPattern(), er.getIndex());

                //获取添加 library的行数
                Node libiaryNode = TreeNodeUtil.getLibiaryNode("SafeMath");

                String xpahTemp = String.format("./contractDefinition[identifier[matches(text()[1], \"%s\")]]/contractPartDefinition",contractName);
                //Node libiartTemp = NodeCopyUtil.getNode(xpahTemp, er.getErrorNode(), 0);
                Node libiartTemp = NodeCopyUtil.getNode(xpahTemp, NodeCopyUtil.sourceNode, 0);

                String xpah = String.format("./contractDefinition[identifier[matches(text()[1], \"%s\")]]",contractName);
                //Node libiaryPareant = NodeCopyUtil.getNode(xpah,er.getErrorNode(),0);
                Node libiaryPareant = NodeCopyUtil.getNode(xpah,NodeCopyUtil.sourceNode,0);

                assert libiaryPareant != null;
                NodeCopyUtil.insertBeforeNode(libiaryPareant,libiartTemp,libiaryNode);


                //获得相应插入的节点
                NodeList contractPart = NodeUtil.getContractPart(er.getPattern(), er.getIndex());
                //直接获得节点在 源代码中的行数
                Integer libiaryLine = NodeUtil.nodeToXmlContext(contractPart);

                RepaireDetail libiaryRe = new RepaireDetail("virtual",libiaryLine,null,null);
                repaireDetail1s.add(libiaryRe);

                NodeCopyUtil.isTreeRepaire = true;
//                if (er.getSourceNode() == null){
//                    er.setSourceNode(pareant);
//                }

            }

            this.isSafeMath = true;

        }



        return repaireDetail1s;
    }


    public List<RepaireDetail> SIO002F() throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        if (DetectionOne.IntergerVul>5){
            return SIO002();
        }else {
            Node node = NodeUtil.getNode(er.getPattern(), er.getIndex());

//        a = b.mul(c);
            //
            List<String> symbols = splitNode(node);

            if (symbols.size()!=5){
                System.out.println("reapire error");
                return null;
            }
            List<RepaireDetail> repaireDetail1s = SIO002Require(symbols);
            return repaireDetail1s;
        }
    }




    public List<RepaireDetail> SIO002() throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
        //获得漏洞节点
        Node node = NodeUtil.getNode(er.getPattern(), er.getIndex());

//        a = b.mul(c);
        //
        List<String> symbols = splitNode(node);

        if (symbols.size()!=5){
            System.out.println("表达式暂不支持修复 漏洞类别："+er.getPattern()+" 漏洞下标:"+er.getIndex()+" 漏洞内容："+er.getContext());
            return null;
        }
        //表达式修改表
        List<RepaireDetail> repaireDetail1s = SIO002SafeMath(symbols);

        //判断其是否添加过了相关 库
        if (!this.isSafeMath ){
            String ifImport = judgeSafeMath();
            if (ifImport == null){

                //添加这个 import相关库
                Node importNode = TreeNodeUtil.getImportNode("import \"./SafeMath.sol\";");
                //确定这个树添加在什么位置
                NodeList pragmaDirectiveNodes = NodeCopyUtil.getNodelist("./pragmaDirective", NodeCopyUtil.sourceNode);
                if (pragmaDirectiveNodes.getLength()==0){
                    System.out.println("dont have the verion of the contract：");
                    System.exit(1);
                }
                Node temp = pragmaDirectiveNodes.item(0).getNextSibling();
                Node pareant = NodeCopyUtil.sourceNode;

                //p 父亲节点  temp后于i个  插入的
                NodeCopyUtil.insertBeforeNode(pareant,temp,importNode);

                //System.out.println(NodeUtil.getNodeTextContent(NodeCopyUtil.sourceNode));
                RepaireDetail importRe = new RepaireDetail("virtual",1,null,null);
                repaireDetail1s.add(importRe);

                //添加using 库

                //获取当前函数的FunctionName
                String contractName = NodeUtil.getContractName(er.getPattern(), er.getIndex());

                //获取添加 library的行数
                Node libiaryNode = TreeNodeUtil.getLibiaryNode("SafeMath");

//                Node libiartTemp = NodeCopyUtil.getNode("//sourceUnit/contractDefinition/contractPartDefinition", er.getErrorNode(), 0);
//
//                Node libiaryPareant = NodeCopyUtil.getNode("//sourceUnit/contractDefinition",er.getErrorNode(),0);

                String xpahTemp = String.format("./contractDefinition[identifier[matches(text()[1], \"%s\")]]/contractPartDefinition",contractName);

                //Node libiartTemp = NodeCopyUtil.getNode(xpahTemp, er.getErrorNode(), 0);

                Node libiartTemp = NodeCopyUtil.getNode(xpahTemp, NodeCopyUtil.sourceNode, 0);
//                String xpahTemp = String.format("//sourceUnit/contractDefinition[identifier[matches(text()[1], \"%s\")]]/contractPartDefinition",contractName);
//                Node libiartTemp = NodeCopyUtil.getNode(xpahTemp, er.getErrorNode(), 0);

                String xpah = String.format("./contractDefinition[identifier[matches(text()[1], \"%s\")]]",contractName);
                //Node libiaryPareant = NodeCopyUtil.getNode(xpah,er.getErrorNode(),0);
                Node libiaryPareant = NodeCopyUtil.getNode(xpah,NodeCopyUtil.sourceNode,0);

//                String xpahTemp = String.format("//sourceUnit/contractDefinition[identifier[matches(text()[1], \"%s\")]]/contractPartDefinition",contractName);
//                Node libiartTemp = NodeCopyUtil.getNode(xpahTemp, er.getErrorNode(), 0);
//                System.out.println(libiartTemp.getTextContent());
//                String xpah = String.format("//sourceUnit/contractDefinition[identifier[matches(text()[1], \"%s\")]]",contractName);

                assert libiaryPareant != null;
                NodeCopyUtil.insertBeforeNode(libiaryPareant,libiartTemp,libiaryNode);


                //获得相应插入的节点
                NodeList contractPart = NodeUtil.getContractPart(er.getPattern(), er.getIndex());
                //直接获得节点在 源代码中的行数
                Integer libiaryLine = NodeUtil.nodeToXmlContext(contractPart);

                RepaireDetail libiaryRe = new RepaireDetail("virtual",libiaryLine,null,null);
                repaireDetail1s.add(libiaryRe);

                NodeCopyUtil.isTreeRepaire = true;

            }

            this.isSafeMath = true;

        }
//        Node node1 = NodeUtil.getNode(er.getPattern(), er.getIndex());
//        System.out.println("node1" +"  "+NodeUtil.getNodeTextContent(node1));

        return repaireDetail1s;
    }


    public  List<RepaireDetail> SIO003() throws XPathExpressionException {
        Node node = NodeUtil.getNode(er.getPattern(), er.getIndex());

        List<String> symbols = spiltSio003Expression(node);
        if (symbols.size()!=4){
            System.out.println("reapire error");
            return null;
        }
        List<RepaireDetail> list = new ArrayList<>();
        //String expression = "amount-number";
//        this.firstExpression = StringUtil.spiltExpressionReturnFirst(this.er.getContext());
//        System.out.println("firstExpression "+this.firstExpression);
//        // 使用正则表达式匹配数字和字符变量
//        List<String> parts = Arrays.asList(this.firstExpression.split("[+\\-*]"));

        if (!this.isSafeMath){
            //添加库的导入
            NodeList importDirective = NodeUtil.getImportDirective(er.getPattern(), er.getIndex());
            String importSafeMath = null;
            Node safeMathNode = null;
            Integer importLine = null;
            if (importDirective!=null){

                for (int i =0;i<importDirective.getLength();i++){
                    Node n = importDirective.item(i);

                    String inputString = NodeUtil.getNodeTextContent(n);
                    String searchString = "SafeMath.sol";

                    // 使用正则表达式进行不区分大小写的匹配
                    Pattern pattern = Pattern.compile(searchString, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(inputString);

                    // 查找并输出匹配的字符串
                    while (matcher.find()) {

                        importSafeMath = matcher.group();
                        safeMathNode = n;
                        break;

                    }
                    if (importSafeMath != null){
                        break;
                    }
                }
            }

            if (safeMathNode !=null){
                importLine = NodeUtil.NodeStopToXmlContext((NodeList) safeMathNode);
            }else{
                importLine =NodeUtil.NodeStopToXmlContext(NodeUtil.getPragmaDirective(er.getPattern(),er.getIndex()));
            }
            if (importSafeMath == null){
                RepaireDetail re = new RepaireDetail("add",importLine ,er.getContext(),"import \"./SafeMath.sol\";");
                list.add(re);
            }

            //添加 使用库的代码

            NodeList libiaryNode = NodeUtil.getLibiary(er.getPattern(), er.getIndex());

            boolean containLibiary = false;
            if (libiaryNode != null) {
                for (int i =0;i<libiaryNode.getLength();i++){
                    Node n = libiaryNode.item(i);

                    String inputString = NodeUtil.getNodeTextContent(n);
                    String searchString = "SafeMath";

                    // 使用正则表达式进行不区分大小写的匹配
                    Pattern pattern = Pattern.compile(searchString, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(inputString);

                    // 查找并输出匹配的字符串
                    while (matcher.find()) {

                        containLibiary = true;
                        break;

                    }
                    if (containLibiary){
                        break;
                    }
                }
            }

            if (!containLibiary){
                String li;
                if (importSafeMath == null){
                    li ="SafeMath";
                }else{
                    String[] part = importSafeMath.split("\\.");
                    li = part[0];
                }

                NodeList contractPart = NodeUtil.getContractPart(er.getPattern(), er.getIndex());
                Integer libiaryLine = NodeUtil.nodeToXmlContext(contractPart);
                RepaireDetail re = new RepaireDetail("add",libiaryLine ,er.getContext(),"using "+li+" for "+"uint;");
                list.add(re);
            }
        }


        if(symbols.size()==4){
            list.addAll(judgeTwoToSafeMath(symbols));
        }else if(symbols.size()!=3){
//            List<String> postfixExpression = MathUtil.ReversePolishNotation(this.firstExpression);
//            System.out.println("hahha"+postfixExpression);
//            List<RepaireDetail> repaireDetails = moreToSafeMath(postfixExpression);
//            list.addAll(repaireDetails);
        }
        this.isSafeMath = true;


        return  list;
    }




    public List<RepaireDetail> SIO004F() throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        if (DetectionOne.IntergerVul>5){
            return SIO004();
        }else {
            //获得漏洞节点
            Node node = NodeUtil.getNode(er.getPattern(), er.getIndex());

//        a = b.mul(c);
            //
            List<String> symbols = splitExpression(node);

            if (symbols.size()!=4){
                System.out.println("reapire error");
                return null;
            }
            //表达式修改表

            return TwoToRequire(symbols);
        }
    }

    public List<RepaireDetail> SIO004() throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
        //获得漏洞节点
        Node node = NodeUtil.getNode(er.getPattern(), er.getIndex());

//        a = b.mul(c);
        //
        List<String> symbols = splitExpression(node);

        if (symbols.size()!=4){
            System.out.println("reapire error");
            return null;
        }
        //表达式修改表
        List<RepaireDetail> repaireDetail1s = TwoToSafeMath(symbols);

        //判断其是否添加过了相关 库
        if (!this.isSafeMath ){
            String ifImport = judgeSafeMath();
            if (ifImport == null){

                //添加这个 import相关库
                Node importNode = TreeNodeUtil.getImportNode("import \"./SafeMath.sol\";");
                //确定这个树添加在什么位置
                NodeList pragmaDirectiveNodes = NodeCopyUtil.getNodelist("./pragmaDirective", NodeCopyUtil.sourceNode);
                if (pragmaDirectiveNodes.getLength()==0){
                    System.out.println("合约应该包括版本号：");
                    System.exit(1);
                }
                Node temp = pragmaDirectiveNodes.item(0).getNextSibling();

                Node pareant = NodeCopyUtil.sourceNode;

                //p 父亲节点  temp后于i个  插入的
                NodeCopyUtil.insertBeforeNode(pareant,temp,importNode);


                RepaireDetail importRe = new RepaireDetail("virtual",1,null,null);
                repaireDetail1s.add(importRe);

                //添加using 库
                //获取当前函数的FunctionName
                String contractName = NodeUtil.getContractName(er.getPattern(), er.getIndex());
                //获取添加 library的行数
                Node libiaryNode = TreeNodeUtil.getLibiaryNode("SafeMath");

                String xpahTemp = String.format("./contractDefinition[identifier[matches(text()[1], \"%s\")]]/contractPartDefinition",contractName);

                Node libiartTemp = NodeCopyUtil.getNode(xpahTemp, NodeCopyUtil.sourceNode, 0);

                String xpah = String.format("./contractDefinition[identifier[matches(text()[1], \"%s\")]]",contractName);

                Node libiaryPareant = NodeCopyUtil.getNode(xpah,NodeCopyUtil.sourceNode,0);
                // Node libiaryPareant = NodeCopyUtil.getNode("//sourceUnit/contractDefinition[text()[1]=\"\"]",er.getErrorNode(),0);

                NodeCopyUtil.insertBeforeNode(libiaryPareant,libiartTemp,libiaryNode);

                //System.out.println(NodeUtil.getNodeTextContent(pareant));

                //获得相应插入的节点
                NodeList contractPart = NodeUtil.getContractPart(er.getPattern(), er.getIndex());
                //直接获得节点在 源代码中的行数
                Integer libiaryLine = NodeUtil.nodeToXmlContext(contractPart);

                RepaireDetail libiaryRe = new RepaireDetail("virtual",libiaryLine,null,null);
                repaireDetail1s.add(libiaryRe);

                NodeCopyUtil.isTreeRepaire = true;

            }

            this.isSafeMath = true;

        }
//        Node node1 = NodeUtil.getNode(er.getPattern(), er.getIndex());
//        System.out.println("node1" +"  "+NodeUtil.getNodeTextContent(node1));

        return repaireDetail1s;
    }

    //这是正常的 + - * / 操作的修改
    public List<RepaireDetail> TwoToSafeMath(List<String> symbols){
        List<RepaireDetail> list = new ArrayList<>();
        String operator = symbols.get(2);
        switch (operator){
            case "-":{
                String newCode = String.format("%s = %s.sub(%s);", symbols.get(0),symbols.get(1),symbols.get(3));

                //System.out.println("newCode"+newCode);
                RepaireDetail re = new RepaireDetail("update",line - 1,er.getContext(),newCode);
                list.add(re);
                break;
            }
            case "*":{
                String newCode = String.format("%s = %s.mul(%s);", symbols.get(0),symbols.get(1),symbols.get(3));

               // System.out.println(newCode);
                RepaireDetail re = new RepaireDetail("update",line - 1,er.getContext(),newCode);
                list.add(re);
                break;
            }
            case "+":{
                String newCode = String.format("%s = %s.add(%s);", symbols.get(0),symbols.get(1),symbols.get(3));
               // System.out.println(newCode);
                RepaireDetail re = new RepaireDetail("update",line - 1,er.getContext(),newCode);
                list.add(re);
                break;
            }
        }
        return list;
    }


    public List<RepaireDetail> TwoToRequire(List<String> symbols){
        List<RepaireDetail> list = new ArrayList<>();
        String operator = symbols.get(2);
        switch (operator){
            case "-":{
                String newCode = String.format("require(%s >= %s);", symbols.get(1),symbols.get(3));

                //System.out.println("newCode"+newCode);
                RepaireDetail re = new RepaireDetail("add",line - 1,er.getContext(),newCode);
                list.add(re);
                break;
            }
            case "*":{
                String newCode = String.format("require(%s * %s >= %s);", symbols.get(1),symbols.get(3),symbols.get(1));

                // System.out.println(newCode);
                RepaireDetail re = new RepaireDetail("add",line - 1,er.getContext(),newCode);
                list.add(re);
                break;
            }
            case "+":{
                String newCode = String.format("require(%s + %s >= %s);", symbols.get(1),symbols.get(3),symbols.get(1));
                // System.out.println(newCode);
                RepaireDetail re = new RepaireDetail("add",line - 1,er.getContext(),newCode);
                list.add(re);
                break;
            }
        }
        return list;
    }
    //此方法是针对在判断语句中 解决这个SafeMath问题
    public   List<RepaireDetail>  judgeTwoToSafeMath(List<String> varLsit) {
       // Path file = Paths.get(PropertiesUtil.contractsFile+PropertiesUtil.contractFile);
        Path file = Paths.get(PropertiesUtil.fileDe);
        List<RepaireDetail> list = new ArrayList<>();
        List<String> lines = null;
        try {
            lines = Files.readAllLines(file);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        String lineContext =lines.get(this.line - 1);
        // 使用正则表达式匹配运算符号
//        String pattern = "[-+*]";
//        Pattern p = Pattern.compile(pattern);
//        Matcher m = p.matcher(this.expression);
//
          lineContext = StringUtil.removeEscapeSaveStartEscape(lineContext);
//        System.out.println("lineconText"+ lineContext);

        // 获取匹配到的运算符号

            String operator = varLsit.get(1);

            switch (operator){
                case "-":{
                    String replaceCode = String.format("%s.sub(%s)", varLsit.get(0),varLsit.get(2));
                    //System.out.println(replaceCode);
                    String newCode = lineContext.replace(varLsit.get(3) ,replaceCode );
                    //System.out.println("newCode"+newCode);
                    RepaireDetail re = new RepaireDetail("update",line - 1,er.getContext(),newCode);
                    list.add(re);
                    break;
                }
                case "*":{
                    String replaceCode = String.format("%s.mul(%s)", varLsit.get(0),varLsit.get(2));
                    //System.out.println(replaceCode);
                    String newCode = lineContext.replace(varLsit.get(3) ,replaceCode );
                    //System.out.println(newCode);
                    RepaireDetail re = new RepaireDetail("update",line - 1,er.getContext(),newCode);
                    list.add(re);
                    break;
                }
                case "+":{
                    String replaceCode = String.format("%s.add(%s)", varLsit.get(0),varLsit.get(2));
                    //System.out.println(replaceCode);
                    String newCode = lineContext.replace(varLsit.get(3),replaceCode );
                   // System.out.println(newCode);
                    RepaireDetail re = new RepaireDetail("update",line - 1,er.getContext(),newCode);
                    list.add(re);
                    break;
                }
            }


        return list;
    }

    //此方法是针对在判断语句中 解决这个SafeMath问题
    public   List<RepaireDetail>  judgeTwoToRequire(List<String> varLsit) {
    //    Path file = Paths.get(PropertiesUtil.contractsFile+PropertiesUtil.contractFile);
        Path file = Paths.get(PropertiesUtil.fileDe);
        List<RepaireDetail> list = new ArrayList<>();
        List<String> lines = null;
        try {
            lines = Files.readAllLines(file);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        String lineContext =lines.get(this.line - 1);
        // 使用正则表达式匹配运算符号
//        String pattern = "[-+*]";
//        Pattern p = Pattern.compile(pattern);
//        Matcher m = p.matcher(this.expression);
//
        lineContext = StringUtil.removeEscapeSaveStartEscape(lineContext);
//        System.out.println("lineconText"+ lineContext);

        // 获取匹配到的运算符号

        String operator = varLsit.get(1);

        switch (operator){
            case "-":{
                String replaceCode = String.format("require(%s >= %s);", varLsit.get(0),varLsit.get(2));
                //System.out.println(replaceCode);

                //System.out.println("newCode"+newCode);
                RepaireDetail re = new RepaireDetail("add",line - 1,er.getContext(),replaceCode);
                list.add(re);
                break;
            }
            case "*":{
                String replaceCode = String.format("require(%s * %s >= %s);", varLsit.get(0),varLsit.get(2),varLsit.get(0));
                //System.out.println(replaceCode);

                //System.out.println(newCode);
                RepaireDetail re = new RepaireDetail("add",line - 1,er.getContext(),replaceCode);
                list.add(re);
                break;
            }
            case "+":{
                String replaceCode = String.format("require(%s + %s >= %s);", varLsit.get(0),varLsit.get(2),varLsit.get(0));
                //System.out.println(replaceCode);

                // System.out.println(newCode);
                RepaireDetail re = new RepaireDetail("add",line - 1,er.getContext(),replaceCode);
                list.add(re);
                break;
            }
        }


        return list;
    }



    //这是正常的 + - * / 操作的修改 SIO002
    public List<RepaireDetail> SIO002SafeMath(List<String> symbols){
        List<RepaireDetail> list = new ArrayList<>();
        String operator = symbols.get(3);
        switch (operator){
            case "-":{
                String newCode = String.format("%s %s = %s.sub(%s);", symbols.get(0),symbols.get(1),symbols.get(2),symbols.get(4));

                //System.out.println("newCode"+newCode);
                RepaireDetail re = new RepaireDetail("update",line - 1,er.getContext(),newCode);
                list.add(re);
                break;
            }
            case "*":{
                String newCode = String.format("%s %s = %s.mul(%s);", symbols.get(0),symbols.get(1),symbols.get(2),symbols.get(4));

                //System.out.println(newCode);
                RepaireDetail re = new RepaireDetail("update",line - 1,er.getContext(),newCode);
                list.add(re);
                break;
            }
            case "+":{
                String newCode = String.format("%s %s = %s.add(%s);", symbols.get(0),symbols.get(1),symbols.get(2),symbols.get(4));
                //System.out.println(newCode);
                RepaireDetail re = new RepaireDetail("update",line - 1,er.getContext(),newCode);
                list.add(re);
                break;
            }
        }
        return list;
    }

    //这是正常的 + - * / 操作的修改 SIO002
    public List<RepaireDetail> SIO002Require(List<String> symbols){
        List<RepaireDetail> list = new ArrayList<>();
        String operator = symbols.get(3);
        switch (operator){
            case "-":{
                //String newCode = String.format("%s %s = %s.sub(%s);", symbols.get(0),symbols.get(1),symbols.get(2),symbols.get(4));
                String newCode = String.format("require(%s >= %s);", symbols.get(2),symbols.get(4));
                //System.out.println("newCode"+newCode);
                RepaireDetail re = new RepaireDetail("add",line - 1,er.getContext(),newCode);
                list.add(re);
                break;
            }
            case "*":{
                //String newCode = String.format("%s %s = %s.mul(%s);", symbols.get(0),symbols.get(1),symbols.get(2),symbols.get(4));
                String newCode = String.format("require(%s * %s >= %s);", symbols.get(2),symbols.get(4),symbols.get(2));
                //System.out.println(newCode);
                RepaireDetail re = new RepaireDetail("add",line - 1,er.getContext(),newCode);
                list.add(re);
                break;
            }
            case "+":{
                //String newCode = String.format("%s %s = %s.add(%s);", symbols.get(0),symbols.get(1),symbols.get(2),symbols.get(4));
                String newCode = String.format("require(%s + %s >= %s);", symbols.get(2),symbols.get(4),symbols.get(2));
                //System.out.println(newCode);
                RepaireDetail re = new RepaireDetail("add",line - 1,er.getContext(),newCode);
                list.add(re);
                break;
            }
        }
        return list;
    }









    //使用与 一个操作符好的 += -= *=的类型  SIO002
    private List<String> splitNode(Node node) throws XPathExpressionException {
//        XPathFactory xPathFactory = XPathFactory.newInstance();
//        XPath xpath = xPathFactory.newXPath();
//        XPathExpression identifierExpr = xpath.compile("./expression[plusminusOperator or muldivOperator/mulOperator]");
//
//        //算数表达式
//
//
//        List<String> symbols = new ArrayList<>();
//        NodeList identifierNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);
//        Node expressionNOde = identifierNodes.item(0);



//        List<String> stringArray = new ArrayList<>();
        List<String> resList = new ArrayList<>();

        for (int i =0;i<node.getChildNodes().getLength();i++){
            Node n =node.getChildNodes().item(i);
            if(n.getNodeName().equals("typeName")&&n.getParentNode().getNodeName().equals("variableDeclaration")){
                resList.add(n.getTextContent());

            }else if(n.getNodeName().equals("identifier")&&n.getParentNode().getNodeName().equals("variableDeclaration")){
                resList.add(n.getTextContent());
            }else if (n.getNodeName().equals("expression")) {
                for (int j = 0; j < n.getChildNodes().getLength(); j++) {
                    Node expressionChild = n.getChildNodes().item(j);
                    if (expressionChild.getNodeName().equals("expression")) {
                        //System.out.println(expressionChild.getTextContent());
                        resList.add(expressionChild.getTextContent());
                    } else if (expressionChild.getNodeName().equals("plusminusOperator") || expressionChild.getNodeName().equals("muldivOperator")) {
                        //System.out.println(expressionChild.getTextContent());
                        resList.add(expressionChild.getTextContent());
                    }
                }
            }



                //resList.add(n.getTextContent());
//            }else if ((n.getNodeName().equals("plusminusOperator")||n.getNodeName().equals("muldivOperator"))){
//                resList.add(n.getTextContent());
//            }
        }

        return resList;
    }



    //用于计算多个参数的情况 暂不考虑
    public  List<RepaireDetail>  moreToSafeMath(List<String> postfixExpression){
        Stack<String> parameter = new Stack<>();
        String FMode=null;
        String LMode = null;
        String modeText = null;
        String LastParameter = null;
        //  Stack<String> operators = new Stack<>();
        for(int i =0;i<postfixExpression.size();i++){

            String e =postfixExpression.get(i);


            if (e.equals("+")||e.equals("-")||e.equals("/")||e.equals("*")){
                if(e.equals("/")){

                }else {
                    switch (e){
                        case "+":{
                            FMode = parameter.pop();
                            LMode = parameter.pop();

                            modeText = String.format("%s.add(%s)", LMode,FMode);
                            parameter.push(modeText);
                            break;
                        }
                    }


                }
            }else{
                parameter.push(e);
            }

        }
        modeText = parameter.pop();
        //System.out.println(modeText);
        return  null;
    }

    //判断其是否使用了safeMath库
    private String judgeSafeMath() throws XPathExpressionException {
        NodeList importDirective = NodeUtil.getImportDirective(er.getPattern(), er.getIndex());
        String importSafeMath = null;
        if (importDirective!=null){

            for (int i =0;i<importDirective.getLength();i++){
                Node n = importDirective.item(i);

                String inputString = NodeUtil.getNodeTextContent(n);
                String searchString = "SafeMath";

                // 使用正则表达式进行不区分大小写的匹配
                Pattern pattern = Pattern.compile(searchString, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(inputString);

                // 查找并输出匹配的字符串
                while (matcher.find()) {

                    importSafeMath = matcher.group();

                    break;

                }

            }
        }
        return importSafeMath;
    }




    //使用与 一个操作符好的 += -= *=的类型  SIO004
    private List<String> splitExpression(Node node){
        List<String> stringArray = new ArrayList<>();
        int flag = 0;
        for (int i =0;i<node.getChildNodes().getLength();i++){
            Node n =node.getChildNodes().item(i);
            if (n.getNodeName().equals("expression")){
                stringArray.add(n.getTextContent());
                if (flag == 0){
                    stringArray.add(n.getTextContent());
                    flag++;
                }
            }else if (n.getNodeName().equals("lvalueOperator")&&flag>0){
                stringArray.add(n.getTextContent().replace("=", ""));
            }
        }
        
        return stringArray;
    }


    //获取 两个相加或者相减的变量 以及操作符和 表达式
    private List<String> spiltSio003Expression(Node node) throws XPathExpressionException {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression identifierExpr = xpath.compile("./expression[plusminusOperator or muldivOperator/mulOperator]");
        //算数表达式
        List<String> symbols = new ArrayList<>();
        NodeList identifierNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);
        Node expressionNOde = identifierNodes.item(0);
        //表达式内容
        String alExpression = StringUtil.removeEscape(expressionNOde.getTextContent());
        for (int i =0;i<expressionNOde.getChildNodes().getLength();i++){

            Node n =expressionNOde.getChildNodes().item(i);
            if (n.getNodeName().equals("expression")){
                symbols.add(StringUtil.removeEscape(n.getTextContent()));
            }else if (n.getNodeName().equals("plusminusOperator")||n.getNodeName().equals("muldivOperator")){
                symbols.add(StringUtil.removeEscape(n.getTextContent()));
            }

        }
        symbols.add(alExpression);
        return symbols;
    }


}


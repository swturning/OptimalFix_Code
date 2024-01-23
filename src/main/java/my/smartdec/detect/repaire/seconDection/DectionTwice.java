package my.smartdec.detect.repaire.seconDection;

import my.smartdec.detect.repaire.entity.ErrorReport;
import my.smartdec.detect.repaire.entity.Variabion;
import my.smartdec.detect.repaire.util.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.script.ScriptException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class DectionTwice {

    List<Variabion> variabionLists = new ArrayList<>();

    int lineStart;

    int errorStatement ;

    int lineStop;


    public DectionTwice(int lineStart,int lineStop){
        this.lineStart = lineStart;
        this.lineStop = lineStop;

    }

    public Boolean reetrancyDection(ErrorReport er) throws XPathExpressionException, ScriptException, NoSuchFieldException, IllegalAccessException, IOException, ParserConfigurationException, SAXException {
        List<Variabion> variabionList = getVariabionLists(er);
        //获得方法名
        String ree = NodeUtil.getFunctionName(er.getPattern(), er.getIndex());
        boolean mutex = reetrancyMutex(ree,er,variabionList);
        boolean isConstantAddress = isConstantAddress("ss",er);

        if (mutex||isConstantAddress){
            return true;
        }

        return false;

    }

    //过滤互斥锁
    public boolean reetrancyMutex(String ree001,ErrorReport er,List<Variabion> variabionList) throws XPathExpressionException, ScriptException, NoSuchFieldException, IllegalAccessException {

        //String ree001 = NodeUtil.getFunctionName(er.getPattern(), er.getIndex());

        ReetrancyParseTreeListener myParseTreeListener = new ReetrancyParseTreeListener(TreeUtil.document,TreeUtil.rules,TreeUtil.terminals,ree001);

        if (TreeUtil.root == null){
            TreeUtil.getRoot();
        }


        ParseTreeWalker.DEFAULT.walk(myParseTreeListener, TreeUtil.root);

        RuleNode functionRoot = myParseTreeListener.getFunctionRoot();

        ParserRuleContext parserRuleContext = TreeUtil.walkBlock(functionRoot);

        List<ParserRuleContext> staments = TreeUtil.getStaments(parserRuleContext);

//        System.out.println(staments.size());
//        System.out.println(this.lineStart+"line"+this.lineStop);
        //找到漏洞所在的stament树
        for(int i =0;i<staments.size();i++){

            if ((staments.get(i).getStart().getLine()<=this.lineStart)&&(staments.get(i).getStop().getLine()>=this.lineStop)){
                //System.out.println("haha");
                this.errorStatement = i;
            }
            //System.out.println("errorStament "+errorStatement);
        }
        //获取所有的statament Node
        NodeList statements = NodeUtil.getFunctionStatement(er.getPattern(), er.getIndex());

        this.variabionLists = variabionList;

        //过滤
        if (variabionList.size()>0){

            //Set<String> keySet = variableAndValues.keySet();
            //根据状态变量 来遍历全部的statement Node 去寻找相关的内容
            for (Variabion key: variabionList){
//                System.out.println("");
//                System.out.println("新的变量开始遍历");
//                System.out.println("Name:"+key.getName()+" type:"+key.getType()+" value:"+key.getValue());

                for (int i = 0;i<statements.getLength();i++){
                    //System.out.println("正对变量："+key+" 第"+i+"次");
                    if (staments.get(i).getText().contains(key.getName())){
                        Node temp = statements.item(i);
                        if (i < this.errorStatement){

                            //获取了当前statement中的所有关于 当前变量 require/。。/。。的判断内容
                            List<String> requireStrings = NodeUtil.requireDetermine(temp);
                            requireStrings = equalFirstpart(key.getName(),requireStrings);
                            String result = ExpressionEvaluate(key,requireStrings);
                           // List<String> requirExpressions = ListUtil.ListContaines(NodeUtil.requireDetermine(temp),key.getName());
                           // System.out.println("获取了当前statement中的所有关于 当前变量 require/。。/。。的判断内容  "+requirExpressions);
                            //判断这个的语句是否为 true
                            //String result = ExpressionEvaluate(key,requirExpressions);
                            if (result!=null){
                                key.setDetration(true);
                                key.setMutexExpression(result);
                                //System.out.println("require结束了"+"Name:"+key.getName()+" type:"+key.getType()+" value:"+key.getValue()+" Detration:"+key.isDetration());
                            }

                            //寻找是否存在关于 互斥变量的第一次变换
                            List<String> statementExpressions = NodeUtil.statementDetermine(temp, key.getName());
                            if (statementExpressions.size()!=0&&key.getMutexExpression()!=null){
                                Boolean changeValueLast = changeValueDeter(key, false, statementExpressions.get(statementExpressions.size() - 1));
                                key.setChange(changeValueLast);
                                //System.out.println("第一次变换： "+"Name:"+key.getName()+" type:"+key.getType()+" value:"+key.getValue()+" change"+key.isChange());
                            }

                        }else if(i == this.errorStatement){
                            //探索全部的 if语句
                            List<String> contidionsExpression = NodeUtil.conditionDetermine(er.getPattern(), er.getIndex());
                            contidionsExpression = equalFirstpart(key.getName(),contidionsExpression);
                            //System.out.println("i == this.errorStatement"+contidionsExpression);
                            String result = ExpressionEvaluate(key,contidionsExpression);
                            if (result!=null){
                                key.setDetration(true);
                                key.setMutexExpression(result);
                                //System.out.println("if结束了"+"Name:"+key.getName()+" type:"+key.getType()+" value:"+key.getValue()+" Detration" +key.isDetration());
                            }

                            //搜索其中的 require判断
                            List<String> requireStrings = NodeUtil.requireDetermine(temp);
                            requireStrings = equalFirstpart(key.getName(),requireStrings);
                            //System.out.println("i == this.errorStatement requireStrings"+requireStrings);
                            String result2 = ExpressionEvaluate(key,requireStrings);
                            if (result2!=null){
                                key.setDetration(true);
                                key.setMutexExpression(result2);
                                //System.out.println("i == this.errorStatement 其中的require  "+"Name:"+key.getName()+" type:"+key.getType()+" value:"+key.getValue()+" Detration” " +key.isDetration()+"Mutex:" + key.getMutexExpression());
                            }

                            NodeList statementExpressions = NodeUtil.statementDetermineInBlock(temp, key.getName());
                           for (int j =0;j<statementExpressions.getLength();j++){
//                               boolean ree0011 = NodeUtil.nodeFNodeIndex(statementExpressions.item(j)) < NodeUtil.nodeFNodeIndex((NodeUtil.nodeLists.get("ree001").item(0)));
                               String text =statementExpressions.item(j).getTextContent();
                               Node item = statementExpressions.item(j);
                               //System.out.println("最伟大的查实");
                               //NodeUtil.nodeToXmlContext((NodeList) item);
                               //System.out.println(text);
                               //判断其是否在 漏洞之前
                               if ( NodeUtil.nodeToXmlContext((NodeList) item)<this.lineStart){
                                   if (key.getMutexExpression()!=null){
                                       Boolean changeValueLast = changeValueDeter(key, false, text);
                                       key.setChange(changeValueLast);
                                       //System.out.println("第一次变换内部："+key);
                                   }

                               }
                               //判断其是否在漏洞之后
                               else if ( NodeUtil.nodeToXmlContext((NodeList) item)>this.lineStop){
                                   if (key.getMutexExpression()!=null){
                                       Boolean changeValueLast = changeValueDeter(key, true, text);
                                       key.setIsRecovery(changeValueLast);
                                       //System.out.println("第2次变换在内部： "+key);
                                   }

                               }
                           }

                        }else{
                            //互斥变量的第二次变换
                            List<String> statementExpressions = NodeUtil.statementDetermine(temp, key.getName());
                            if (statementExpressions.size()!=0&&key.getMutexExpression()!=null){
                                Boolean changeValueLast = changeValueDeter(key, true, statementExpressions.get(statementExpressions.size() - 1));
                                key.setIsRecovery(changeValueLast);
                                //System.out.println("第2次变换： "+"Name:"+key.getName()+" type:"+key.getType()+" value:"+key.getValue()+" isrevery:"+key.isRecovery());
                            }

                        }

                    }else {
                        continue;
                    }
                }
            }

        }else{
            return false;
        }

        for (Variabion key:variabionList){
            if (key.isChange()&&key.isDetration()){
                return true;
            }
        }
        return  false;

    }

    private  Boolean modifierMutex(String ree){
        return null;
    }

    private Boolean isConstantAddress(String filePath,ErrorReport er) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        //List<String> addressList = NodeUtil.getVarlition(PropertiesUtil.rootPath+"over222.xml","address");
        List<String> addressList = NodeUtil.getVarlition("over222.xml","address");
        Node node = NodeUtil.nodeLists.get(er.getPattern()).item(er.getIndex());
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        // 在已找到的节点上搜索子节点的identifier数据
        XPathExpression identifierExpr = xpath.compile("preceding-sibling::expression[last()]");
        NodeList identifierNodes = (NodeList) identifierExpr.evaluate(node, XPathConstants.NODESET);

        // 输出找到的identifier数据
        for (int i = 0; i < identifierNodes.getLength(); i++) {
            Node identifierNode = identifierNodes.item(i);
            for (String s :addressList){
                if (StringUtil.removeEscape(identifierNode.getTextContent()).equals(s)){
                    return true;
                }

            }

            //System.out.println( StringUtil.removeEscape(identifierNode.getTextContent()));
        }
        return false;

    }

    private String ExpressionEvaluate(Variabion key, List<String> expressions) {

        boolean isBool = false;
        if (key.getType().equals("bool")){
            isBool = true;

        }
        for (String s :expressions){
            try {
                if (StringUtil.ExpressionEvaluator(s,key.getValue(),isBool,key.getName())){
                    //System.out.println("ExpressionEvaluate:" +s);
                    return s;
                }
            } catch (ScriptException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                //System.err.println("空指针异常发生了：" + e.getMessage());
            }

        }


        return null;
    }

    // 一个是用于第一次改变 也就是锁  另一个就是第二次改变 也就是解锁
    private Boolean changeValueDeter(Variabion key,boolean flag,String expression) throws ScriptException {

        String changeValue = StringUtil.spiltExpressionByIndex(expression, 2);
        //System.out.println(" changeValueDeter: "+key);
        //System.out.println();
        return flag == StringUtil.ExpressionEvaluator(key.getMutexExpression(), changeValue, key.getType().equals("bool"), key.getName());


    }

    //判断表达式的第一部分是否一致
    private List<String> equalFirstpart(String inputVar,List<String> expression){
        //匹配第一部分是否为 inputVar
        List<String> expressions = new ArrayList<>();
        for (String s:expression){
            List<String> strings = StringUtil.spiltExpressionWithSingle(s);
            if (strings.size()>1){
                if ((strings.get(0).equals(inputVar)&&equalBoolOrNumber(strings.get(2)))||(strings.get(2).equals(inputVar)&&equalBoolOrNumber(strings.get(0)))){

                    expressions.add(s);
                }
            }else if (strings.size()==1){
                if (strings.get(0).contains(inputVar)){
                    expressions.add(s);
                }
            }
        }
        return expressions;
    }

    //匹配第三部分是否为自然数 或者为 bool类型
    private Boolean equalBoolOrNumber(String value){
        if (value.matches("\\d+")) {

            //System.out.println("Value is a natural number." +value);
            return true;
        } else if (value.equals("true") || value.equals("false")) {

            //System.out.println("Value is a boolean."+value);
            return true;
        } else {

            //System.out.println("Value is neither a natural number nor a boolean.");
            return false;
        }
    }

    public List<Variabion> getVariabionLists(ErrorReport er) throws XPathExpressionException {
        NodeList stateVariableList = NodeUtil.getStateVariable(er.getPattern(), er.getIndex());
        List<Variabion> variabionList = new ArrayList<>();

        for (int i =0;i<stateVariableList.getLength();i++){
            Node n = stateVariableList.item(i);
            String type = null;
            String identifier = null;
            String value = null;
            NodeList childNodes = n.getChildNodes();
            for (int j =0;j<childNodes.getLength();j++){
                Node c = childNodes.item(j);
                if (c.getNodeName().equals("typeName")){

                    if (c.getTextContent().equals("bool")||isInt(c.getTextContent())){
                        type = c.getTextContent();
                    }else{
                        break;
                    }
                }else if (c.getNodeName().equals("identifier")&&type!=null){
                     identifier = c.getTextContent();
                }else if (c.getNodeName().equals("expression")&&type!=null){
                    value = c.getTextContent();
                }
            }
            if (type !=null){

                Variabion variabion = new Variabion(type, identifier, value);
                variabionList.add(variabion);
            }

        }
        return variabionList;

    }

    public Boolean isInt(String inputStr){
        String[] allTypes = {
                 "int", "uint",
                "int8", "uint8",
                "int16", "uint16",
                "int24", "uint24",
                "int32", "uint32",
                "int40", "uint40",
                "int48", "uint48",
                "int56", "uint56",
                "int64", "uint64",
                "int72", "uint72",
                "int80", "uint80",
                "int88", "uint88",
                "int96", "uint96",
                "int104", "uint104",
                "int112", "uint112",
                "int120", "uint120",
                "int128", "uint128",
                "int136", "uint136",
                "int144", "uint144",
                "int152", "uint152",
                "int160", "uint160",
                "int168", "uint168",
                "int176", "uint176",
                "int184", "uint184",
                "int192", "uint192",
                "int200", "uint200",
                "int208", "uint208",
                "int216", "uint216",
                "int224", "uint224",
                "int232", "uint232",
                "int240", "uint240",
                "int248", "uint248",
                "int256", "uint256"
        };
        List<String> typeList = Arrays.asList(allTypes);
        return typeList.contains(inputStr);

    }




}

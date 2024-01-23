package my.smartdec.detect.repaire.seconDection;

import lombok.Data;
import my.smartdec.detect.repaire.entity.Variabion;
import my.smartdec.detect.repaire.util.TreeUtil;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Data
public class ReetrancyParseTreeListener implements ParseTreeListener {

    private  Document document ;
    private  Function<RuleContext, String> ruleContextStringFunction;
    //private  RuleNode root;
    //表示漏洞存在当前函数的树
    private RuleNode functionRoot;
    private  Function<TerminalNode, String> terminals;
    //
    private Map<String,String> variableAndValues = new HashMap<>();

    private List<Variabion> variabionList = new ArrayList<>();
    private String functionName;


    public ReetrancyParseTreeListener(Document document, Function<RuleContext,String> ruleContextStringFunction, Function<TerminalNode,String> terminals,String functionName){
        this.document = document;
        //this.root = root;
        this.ruleContextStringFunction = ruleContextStringFunction;
        this.terminals = terminals;
        this.functionName = functionName;
    }

    public Map<String,String> getVariableAndValues(){
        return this.variableAndValues;
    }

    public List<Variabion> getVariabionList(){
        return this.variabionList;
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        final Text text = this.document.createTextNode(
                terminals.apply(node)
        );
       // System.out.println("dection    "        +text.getData());
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        // 实现 visitErrorNode() 方法的逻辑
        // ...
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        final Node element = this.document.createElement(
                ruleContextStringFunction.apply(ctx)
        );

        if (element.getNodeName().equals("stateVariableDeclaration")){
//            String text = TreeUtil.getText(ctx);
//            if (RegularMatchUtil.uint256Match(text)!=null){
//                String[] VariableAndValue = RegularMatchUtil.uint256Match(text);
//                this.variabionList.add(new Variabion("uint256",VariableAndValue[0],VariableAndValue[1]));
//                //this.variableAndValues.put(VariableAndValue[1],VariableAndValue[0]);
//            }else if(RegularMatchUtil.intMatch(text)!=null){
//                String[] VariableAndValue = RegularMatchUtil.intMatch(text);
//                this.variabionList.add(new Variabion("int",VariableAndValue[0],VariableAndValue[1]));
//                //this.variableAndValues.put(VariableAndValue[1],VariableAndValue[0]);
//            }else if(RegularMatchUtil.boolMatch(text)!=null){
//                String[] VariableAndValue = RegularMatchUtil.boolMatch(text);
//                this.variabionList.add(new Variabion("bool",VariableAndValue[0],VariableAndValue[1]));
//                //this.variableAndValues.put(VariableAndValue[1],VariableAndValue[0]);
//            }



        }else if (element.getNodeName().equals("functionDefinition")){
            String text = TreeUtil.getText(ctx);
            //System.out.println("functionDefinition 哈哈哈哈"+text);
            ParseTree child = ctx.getChild(1);
            //System.out.println(child.getText()+" woshi child ");
            //System.out.println(this.functionName);
            if (child.getText().equals(this.functionName)){
                //System.out.println("hahahahahah"+child.getText());
                this.functionRoot = ctx;
            }


        }

       // System.out.println("dection   "+element.getNodeName());

    }



    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        // 实现 exitEveryRule() 方法的逻辑
        // ...
    }
}

//    @Override
//    public void visitTerminal(final TerminalNode node) {
//        final Text text = this.document.createTextNode(
//                DocumentTreeBasic.this.terminals.apply(node)
//        );
//        System.out.println(text.getData());
//        nodes.put(node, text);
//        nodes.get(node.getParent()).appendChild(text);
//    }
//
//    @Override
//    public void visitErrorNode(final ErrorNode node) {
//    }
//
//
//    @Override
//    public void enterEveryRule(final ParserRuleContext ctx) {
//        final Node element = this.document.createElement(
//                DocumentTreeBasic.this.rules.apply(ctx)
//        );
//        nodes.put(ctx, element);
//        System.out.println(element.getNodeName());
//        Optional
//                .ofNullable(ctx.getParent())
//                .map(nodes::get)
//                .orElse(this.document)
//                .appendChild(element);
//    }
//
//    @Override
//    public void exitEveryRule(final ParserRuleContext ctx) {
//    }

package my.smartdec.detect.repaire.util;

import my.smartdec.detect.SolidityLexer;
import my.smartdec.detect.SolidityParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TreeUtil {
    public static CharStream source;

    public static Document document ;

    public static Function<RuleContext, String> rules;

    public static  Function<TerminalNode, String> terminals;

    public static RuleNode root = null;

    //获取整个树
    public static  void getRoot(){

        SolidityLexer solidityLexer = new SolidityLexer(source);
        CommonTokenStream commonTokenStream = new CommonTokenStream(solidityLexer);
        SolidityParser solidityParser = new SolidityParser(commonTokenStream);
        //SolidityParser.SourceUnitContext sourceUnitContext = solidityParser.sourceUnit();
         root = solidityParser.sourceUnit();

    }

    //ParserRuleContext 获取text 这里使用了添加空格
    public static String getText(ParserRuleContext ctx) {

        if (ctx.getChildCount() == 0) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder();

            for(int i = 0; i < ctx.getChildCount(); ++i) {

                builder.append(ctx.getChild(i).getText()).append(" ");

            }

            return builder.toString();
        }
    }

    //从functionDefine获取 block
    public static ParserRuleContext walkBlock(ParseTree t) {
        RuleNode r = (RuleNode)t;
        // this.enterRule(listener, r);
        int n = r.getChildCount();

        for(int i = 0; i < n; ++i) {
            ParseTree child = r.getChild(i);
            if (!(child instanceof ErrorNode)&&!(child instanceof TerminalNode)) {
                RuleNode rs = (RuleNode)child;
                ParserRuleContext ctx = (ParserRuleContext)rs.getRuleContext();
                final Node element = document.createElement(
                        rules.apply(ctx)
                );
                if (element.getNodeName().equals("block")) {
                    ctx.getParent();
                    return ctx;
                }

            }



        }

        return null;

    }

    public static List<ParserRuleContext> getStaments(ParseTree t){
        List<ParserRuleContext> parserRuleContexts = new ArrayList<>();
        RuleNode r = (RuleNode)t;
        // this.enterRule(listener, r);
        int n = r.getChildCount();

        for(int i = 0; i < n; ++i) {
            ParseTree child = r.getChild(i);
            if (!(child instanceof ErrorNode)&&!(child instanceof TerminalNode)){
                RuleNode rs = (RuleNode)child;
                ParserRuleContext ctx = (ParserRuleContext)rs.getRuleContext();
                final Node element = document.createElement(
                        rules.apply(ctx)
                );
                if (element.getNodeName().equals("statement")) {
                    parserRuleContexts.add(ctx);
                }
            }


        }

        return parserRuleContexts;

    }
}
//    public static RuleNode getFunctionBlock(ParseTree ctx) {
//        if (ctx.getChildCount() == 0) {
//            return null;
//        } else {
//            for (int i = 0; i < ctx.getChildCount(); i++) {
//
//                ParseTree child = ctx.getChild(i);
//                Node element = document.createElement(
//                        rules.apply((RuleContext) ctx.getChild(i))
//                );
//                if (element.getNodeName().equals("block")) {
//                    return (RuleNode) ctx.getChild(i);
//                }
//
//            }
//        }
//        return null;
//    }



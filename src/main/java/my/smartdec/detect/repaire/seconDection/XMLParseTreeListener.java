package my.smartdec.detect.repaire.seconDection;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class XMLParseTreeListener implements ParseTreeListener {
    private Document document ;
    private Function<RuleContext, String> ruleContextStringFunction;

    private  Function<TerminalNode, String> terminals;
    Map<Tree, Node> nodes = new HashMap<>();

    public XMLParseTreeListener(Document document, Function<RuleContext,String> ruleContextStringFunction, Function<TerminalNode,String> terminals){
        this.document = document;
        //this.root = root;
        this.ruleContextStringFunction = ruleContextStringFunction;
        this.terminals = terminals;

    }

    @Override
    public void visitTerminal(final TerminalNode node) {
        final Text text = this.document.createTextNode(
                terminals.apply(node)
        );
        //System.out.println(text.getData());
        nodes.put(node, text);
        nodes.get(node.getParent()).appendChild(text);
    }

    @Override
    public void visitErrorNode(final ErrorNode node) {
    }


    @Override
    public void enterEveryRule(final ParserRuleContext ctx) {
        final Node element = this.document.createElement(
                ruleContextStringFunction.apply(ctx)
        );
        nodes.put(ctx, element);
        //System.out.println(element.getNodeName());
        Optional
                .ofNullable(ctx.getParent())
                .map(nodes::get)
                .orElse(this.document)
                .appendChild(element);
    }

    @Override
    public void exitEveryRule(final ParserRuleContext ctx) {

    }

    public Map<Tree, Node> getNodes() {
        return nodes;
    }

    public void writeNode(){

    }

}

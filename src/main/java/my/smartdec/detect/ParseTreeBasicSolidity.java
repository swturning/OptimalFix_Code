package my.smartdec.detect;

import my.smartdec.detect.repaire.seconDection.DectionTwice;
import my.smartdec.detect.repaire.util.TreeUtil;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.RuleNode;

import java.io.InputStream;

/**
 *
 */
public final class ParseTreeBasicSolidity implements ParseTree {

    /**
     *
     */
    private final Source source;

    /**
     * @param src source
     */
    public ParseTreeBasicSolidity(final Source src) {
        this.source = src;
    }

    @Override
    public RuleNode root() throws Exception {
        CharStream chars = this.source.chars();
        //DectionTwice.source = chars;

        TreeUtil.source = chars;
        SolidityLexer solidityLexer = new SolidityLexer(this.source.chars());
        CommonTokenStream commonTokenStream = new CommonTokenStream(solidityLexer);
        SolidityParser solidityParser = new SolidityParser(commonTokenStream);
        //SolidityParser.SourceUnitContext sourceUnitContext = solidityParser.sourceUnit();
        return solidityParser.sourceUnit();
//        return new my.smartdec.detect.SolidityParser(
//                new CommonTokenStream(
//                        new my.smartdec.detect.SolidityLexer(
//                                this.source.chars()
//                        )
//                )
//        )
//                .sourceUnit();
    }
}

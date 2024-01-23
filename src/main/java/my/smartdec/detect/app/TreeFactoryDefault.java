package my.smartdec.detect.app;

import lombok.Data;
import my.smartdec.detect.*;

import javax.xml.parsers.DocumentBuilder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 *
 */

public final class TreeFactoryDefault implements TreeFactory {


    /**
     *
     */
    private final DocumentBuilder builder;
    /**
     *
     */
    private final Charset charset;
    /**
     *
     */
    private final SourceLanguage sourceLanguage;

    /**
     * @param db builder
     * @param ch charset
     * @param language language
     */
    public TreeFactoryDefault(
            final DocumentBuilder db,
            final Charset ch,
            final SourceLanguage language
    ) {
        this.builder = db;
        this.charset = ch;
        this.sourceLanguage = language;
    }

    /**
     * @param db builder   用于解析XML文件，读取XML文档并将其转换为内存中的一个表示形式，通常是一个DOM（Document Object Model）树
     * @param language language
     */
    public TreeFactoryDefault(
            final DocumentBuilder db,
            final SourceLanguage language
    ) {
        this(db, StandardCharsets.UTF_8, language);
    }

    @Override
    public SearchableTree tree(final Path path) {
        SourceFile sourceFile = new SourceFile(path, this.charset);
        ParseTree parseTree = sourceLanguage.createParseTree(sourceFile);
        ParseTreeCached parseTreeCached = new ParseTreeCached(parseTree);

        String[] ruleNames = sourceLanguage.getRuleNames();

        DocumentTreeBasic documentTreeBasic = new DocumentTreeBasic(parseTreeCached, this.builder, ruleNames);

        DocumentTreeCached documentTreeCached = new DocumentTreeCached(documentTreeBasic);

        SearchableTreeDefault searchableTreeDefault = new SearchableTreeDefault(documentTreeCached);

//        return new SearchableTreeDefault(
//                new DocumentTreeCached(
//                        new DocumentTreeBasic(
//                                new ParseTreeCached(
//                                        sourceLanguage.createParseTree(
//                                                new SourceFile(
//                                                        path,
//                                                        this.charset
//                                                )
//                                        )
//                                ),
//                                this.builder,
//                                sourceLanguage.getRuleNames()
//                        )
//                )
//        );

        return searchableTreeDefault;
    }
}

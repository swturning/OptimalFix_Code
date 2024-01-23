package my.smartdec.detect.app.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.antlr.v4.gui.TreeViewer;
import my.smartdec.detect.SourceFile;
import my.smartdec.detect.app.SourceLanguage;
import my.smartdec.detect.app.SourceLanguages;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 *
 */
public final class TreeView {

    /**
     * @param args args
     * @throws Exception exception
     */
    public static void main(final String... args) throws Exception {
        ArgumentsDefault arguments = new ArgumentsDefault(args);

        Path src = arguments
                .value("-p", "--path")
                .map(Paths::get)
                .orElseThrow(IllegalArgumentException::new);

        new TreeView(src).run();
    }

    /**
     *
     */
    private final Path path;

    /**
     * @param src src
     */
    public TreeView(final Path src) {
        this.path = src;
    }

    /**
     * @throws Exception exception
     */
    public void run() throws Exception {
        final SourceLanguage sourceLanguage =
                SourceLanguages.fromFileName(this.path);
        if (sourceLanguage == null) {
            throw new IllegalArgumentException();
        }
//        ParseTree astRoot = sourceLanguage.createParseTree(new SourceFile(this.path)).root();
//        // 将抽象语法树写入 JSON 文件
//        writeAstToJson(astRoot, "ast.json");
        //sourceLanguage.getRuleNames() 是Solidity.g4 文件中的首部的语法规则 sourceUnit, pragmaDirective, pragmaSolidity
        //createParseTree 调用 ParseTreeBasicSolidity类
        new TreeViewer(
                Arrays.asList(sourceLanguage.getRuleNames()),
                sourceLanguage.createParseTree(
                        new SourceFile(this.path)
                ).root()
        )
                .open()
                .get()
                .setVisible(true);
    }
    public void writeAstToJson(ParseTree astRoot, String outputPath) throws IOException {
        // 创建 ObjectMapper 对象，用于将对象转换为 JSON
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // 将 AST 转换为 JSON 字符串
            String jsonAst = objectMapper.writeValueAsString(astRoot);

            // 将 JSON 字符串写入文件
            Path filePath = Paths.get(outputPath);
            Files.write(filePath, jsonAst.getBytes());

            System.out.println("AST has been written to " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

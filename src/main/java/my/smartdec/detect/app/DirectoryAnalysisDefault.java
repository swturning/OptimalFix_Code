package my.smartdec.detect.app;

import lombok.Data;
import my.smartdec.detect.Rules;
import my.smartdec.detect.SearchableTree;
import my.smartdec.detect.TreeAnalysis;
import my.smartdec.detect.TreeAnalysisDefault;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 *
 */
@Data
public final class DirectoryAnalysisDefault implements DirectoryAnalysis {

    /**
     *
     */
    private final Path directory;
    /**
     *
     */
    private final Predicate<? super Path> fileFilter;
    /**
     *
     */
    private final TreeFactory trees;
    /**
     *
     */
    private final Rules rules;

    /**
     * @param src     path
     * @param filter filter
     * @param factory tree factory
     * @param rl      rules
     */
    public DirectoryAnalysisDefault(
            final Path src,
            final Predicate<? super Path> filter,
            final TreeFactory factory,
            final Rules rl
    ) {
        this.directory = src;
        this.fileFilter = filter;
        this.trees = factory;
        this.rules = rl;
    }

    @Override
    public Stream<DirectoryAnalysis.Info> stream() throws Exception {

        //使用 Files 类的 walk 方法对指定的目录 this.directory 进行递归遍历。这个方法返回一个流（Stream）对象，其中包含了目录及其子目录中的所有文件和文件夹路径。


        return Files
                .walk(this.directory)
                //进行两步过滤
                .filter(Files::isRegularFile)
                .filter(this.fileFilter)
                //每个保留的文件路径进行映射操作，将其转换为一个新的对象。这里使用了 lambda 表达式，将文件路径 file 转换为一个匿名内部类对象，该对象实现了 DirectoryAnalysis.Info 接口。
                .map(
                        file -> new DirectoryAnalysis.Info() {

                            @Override
                            public Path file() {
                                return file;
                            }

                            @Override
                            public TreeAnalysis treeReport() {

                                SearchableTree tree = DirectoryAnalysisDefault.this.trees.tree(file);

                                Rules rules = DirectoryAnalysisDefault.this.rules;

                                return new TreeAnalysisDefault(
//                                        DirectoryAnalysisDefault
//                                                .this
//                                                .trees
//                                                .tree(file),
                                        tree,
//                                        DirectoryAnalysisDefault.this.rules
                                        rules
                                );
                            }
                        }
                );
    }
}

package my.smartdec.detect.app;

import lombok.Data;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 *
 */
@Data
public class DirectoryAnalysisCombined implements DirectoryAnalysis {

    /**
     *
     */
    private final DirectoryAnalysis[] elements;

    /**
     *
     * @param els elements
     */
    public DirectoryAnalysisCombined(final DirectoryAnalysis... els) {
        this.elements = els;
    }

    @Override
    public final Stream<Info> stream() throws Exception {
        //数组 elements 转换为一个流（Stream）对象。这意味着可以对数组中的元素进行流式操作。  flatMap 是流操作中的一个中间操作，它接受一个函数作为参数，并将每个元素映射为一个新的流
        return Arrays.stream(elements).flatMap(directoryAnalysis -> {
            try {
                return directoryAnalysis.stream();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}

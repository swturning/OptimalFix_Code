package my.smartdec.detect;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.stream.Stream;

/**
 *
 */
public interface TreeAnalysis {

    /**
     * @return info
     * @throws Exception exception
     */
    Stream<TreeAnalysis.Info> stream() throws Exception;

    /**
     * @return info
     * 该方法是默认方法（default method），提供了一个不抛出异常的版本。它尝试调用 stream() 方法并返回结果流，如果在调用过程中捕获到异常，则将其包装为 RuntimeException 并抛出。
     */
    default Stream<TreeAnalysis.Info> streamUnchecked() {
        try {
            return this.stream();
        } catch (Exception ex) {

            throw new RuntimeException(ex);
        }
    }

    /**
     *
     */
    interface Info {

        /**
         * @return rule
         */
        Rule rule();

        /**
         * @return pattern
         */
        Pattern pattern();

        /**
         * @return contexts
         */
        Stream<ParserRuleContext> contexts();
    }
}

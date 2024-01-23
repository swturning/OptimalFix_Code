package my.smartdec.detect.app;

import com.alibaba.fastjson.JSON;
import my.smartdec.detect.TreeAnalysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 *
 */
public interface Media {

    /**
     * @param info info
     */
    void accept(DirectoryAnalysis.Info info);
}


//class Media{
//
//    accept(DirectoryAnalysis.Info info){
//        System.out.println("info   "+info);
//        Map<String, Integer> result = new HashMap<>();
//
//        Stream<TreeAnalysis.Info> infoStream = info.treeReport().streamUnchecked();
//
//        System.out.println("infoStream"+ JSON.toJSONString(infoStream));
//        info.treeReport().streamUnchecked().forEach(
//                tree -> tree.contexts().forEach(
//
//
//                        context -> {
//                            System.out.println(JSON.toJSONString(tree));
//                            System.out.printf(
//                                    "规则: %s "
//                                            + "模式: %s "
//                                            + "风险等级: %d   "
//                                            + "语言: Solidity "
//                                            + "行: %d    列: %d   "
//                                            + "代码内容: %s%n%n",
//                                    tree.rule().id(),
//                                    tree.pattern().id(),
//                                    tree.pattern().severity(),
//                                    context.getStart().getLine(),
//                                    context
//                                            .getStart()
//                                            .getCharPositionInLine(),
//                                    context.getText()
//                            );
//                            result.compute(
//                                    tree.rule().id(),
//                                    (k, v) -> Optional
//                                            .ofNullable(v)
//                                            .map(i -> i + 1)
//                                            .orElse(1)
//                            );
//                        }
//                )
//        );
//        // 统计违反规则
//        result.forEach((k, v) -> System.out.println(k + " :" + v));
//    }
//
//}
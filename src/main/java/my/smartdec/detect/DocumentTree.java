package my.smartdec.detect;

import my.smartdec.detect.repaire.util.NodeUtil;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.Tree;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public interface DocumentTree {

    /**
     * @return root
     * @throws Exception exception
     */
    DocumentTree.Info info() throws Exception;

    /**
     * @return root
     */
    default DocumentTree.Info infoUnchecked() {
        try {
            return this.info();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     *
     */
    interface Info {

        /**
         * @return root as rule node
         */
        RuleNode ruleNode();

        /**
         * @return mapping
         */
        Map<Tree, Node> elements();

        /**
         * @return root as node
         */
        default Node node() {
            if (NodeUtil.rule == null){
                NodeUtil.rule =this.ruleNode();
            }
            return this.elements().get(this.ruleNode());
        }

        /**
         * @return rule nodes
         */
        default Map<Node, Tree> ruleNodes() {
            //Map<Tree, Node> elements = this.elements();
            HashMap<Node, Tree> collect = this.elements().entrySet().stream().collect(
                    HashMap::new,
                    (map, entry) -> map.put(
                            entry.getValue(), entry.getKey()
                    ),
                    Map::putAll
            );
//            return this.elements().entrySet().stream().collect(
//                    HashMap::new,
//                    (map, entry) -> map.put(
//                            entry.getValue(), entry.getKey()
//                    ),
//                    Map::putAll
//            );
            return collect;
        }
    }
}

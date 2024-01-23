package my.smartdec.detect;

import org.antlr.v4.runtime.tree.RuleNode;

/**
 *
 */
public interface ParseTree {

    /**
     * @return root
     * @throws Exception exception
     */
    RuleNode root() throws Exception;

    /**
     * @return root  获取根节点
     */
    default RuleNode rootUnchecked() {
        try {
            RuleNode root = this.root();
            return root;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

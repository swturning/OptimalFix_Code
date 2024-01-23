package my.smartdec.detect;

import org.antlr.v4.runtime.tree.RuleNode;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 */
public final class ParseTreeCached implements ParseTree {

    /**
     *
     */
    private final ParseTree origin;
    /**
     *
     */
    private final AtomicReference<RuleNode> cache;

    /**
     * @param tree      parse tree to cache
     * @param reference cached tree
     */
    public ParseTreeCached(
            final ParseTree tree, final AtomicReference<RuleNode> reference) {
        this.origin = tree;
        this.cache = reference;
    }

    /**
     * @param tree parse tree to cache
     */
    public ParseTreeCached(final ParseTree tree) {
        this(tree, new AtomicReference<>());
    }

    @Override
    public RuleNode root() {
        //从缓存中获取规则对象，如果缓存中的规则对象为 null，则从 this.origin 对象中获取一组规则，并将其存储到缓存中。
        return this.cache.updateAndGet(
                root -> Optional
                        .ofNullable(root)
                        .orElseGet(this.origin::rootUnchecked)
        );
    }
}

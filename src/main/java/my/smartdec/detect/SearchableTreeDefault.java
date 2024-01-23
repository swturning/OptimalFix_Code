package my.smartdec.detect;

import org.antlr.v4.runtime.tree.Tree;
import org.w3c.dom.Node;

import java.util.stream.Stream;

/**
 *
 */
public final class SearchableTreeDefault implements SearchableTree {

    /**
     *
     */
    private final DocumentTree tree;

    /**
     * @param dt tree
     */
    public SearchableTreeDefault(final DocumentTree dt) {
        this.tree = dt;
    }

    @Override
    public Stream<Tree> nodes(final Query query) throws Exception {
        final DocumentTree.Info info = this.tree.info();
//        Node node = info.node();
//        Stream<Node> nodes = query.nodes((node));
        return query.nodes(info.node()).map(info.ruleNodes()::get);
    }
}

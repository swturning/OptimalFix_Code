package my.smartdec.detect;

import org.w3c.dom.Node;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.util.stream.Stream;

/**
 *
 */
public interface Query {

    /**
     * @param node node
     * @return nodes
     */
    Stream<Node> nodes(Node node) throws TransformerException;
}

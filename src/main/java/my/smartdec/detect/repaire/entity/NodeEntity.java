package my.smartdec.detect.repaire.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.w3c.dom.Node;

@Data
@AllArgsConstructor
public class NodeEntity implements Comparable<NodeEntity>{
    Node node;
    Integer line;


    @Override
    public int compareTo(NodeEntity o) {
        return Integer.compare(this.line, o.line);
    }
}

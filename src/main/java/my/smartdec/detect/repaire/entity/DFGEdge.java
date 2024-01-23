package my.smartdec.detect.repaire.entity;

import lombok.Data;

@Data
public class DFGEdge {
    private DFGNode source;
    private DFGNode destination;

    public DFGEdge(DFGNode source, DFGNode destination) {
        this.source = source;
        this.destination = destination;
    }

    public DFGNode getSource() {
        return source;
    }

    public DFGNode getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return source.getName() + " -> " + destination.getName();
    }
}

package my.smartdec.detect.repaire.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DataFlowGraph {
    private Map<String, DFGNode> nodes;
    private Map<String, DFGEdge> edges;

    public DataFlowGraph() {
        nodes = new HashMap<>();
        edges = new HashMap<>();
    }

    public void addNode(DFGNode node) {
        nodes.put(node.getName(), node);
    }

    public void addEdge(DFGEdge edge) {
        edges.put(edge.getSource().getName() + "->" + edge.getDestination().getName(), edge);
    }

    public DFGNode getNode(String name) {
        return nodes.get(name);
    }

    public DFGEdge getEdge(String sourceName, String destinationName) {
        return edges.get(sourceName + "->" + destinationName);
    }

    public Map<String, DFGNode> getNodes() {
        return nodes;
    }

    public Map<String, DFGEdge> getEdges() {
        return edges;
    }
}
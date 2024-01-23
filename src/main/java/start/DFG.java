package start;

import my.smartdec.detect.repaire.entity.DFGEdge;
import my.smartdec.detect.repaire.entity.DFGNode;
import my.smartdec.detect.repaire.entity.DataFlowGraph;

public class DFG {

        public static void main(String[] args) {
            DataFlowGraph dfg = new DataFlowGraph();

            // 创建DFG节点
            DFGNode inputNode = new DFGNode("input", "false", true, false);
            DFGNode assignNode1 = new DFGNode("assign1", "=", false, false);
            DFGNode requireNode = new DFGNode("require", "require", false, false);
            DFGNode assignNode2 = new DFGNode("assign2", "=", false, false);
            DFGNode assignNode3 = new DFGNode("assign3", "=", false, false);

            // 创建DFG边
            DFGEdge edge1 = new DFGEdge(inputNode, assignNode1);
            DFGEdge edge2 = new DFGEdge(assignNode1, requireNode);
            DFGEdge edge3 = new DFGEdge(requireNode, assignNode2);
            DFGEdge edge4 = new DFGEdge(assignNode2, assignNode3);

            // 将节点和边添加到DFG中
            dfg.addNode(inputNode);
            dfg.addNode(assignNode1);
            dfg.addNode(requireNode);
            dfg.addNode(assignNode2);
            dfg.addNode(assignNode3);

            dfg.addEdge(edge1);
            dfg.addEdge(edge2);
            dfg.addEdge(edge3);
            dfg.addEdge(edge4);

            // 输出DFG的节点和边信息
            System.out.println("DFG Nodes:");
            for (DFGNode node : dfg.getNodes().values()) {
                System.out.println(node);
            }

            System.out.println("\nDFG Edges:");
            for (DFGEdge edge : dfg.getEdges().values()) {
                System.out.println(edge);
            }
        }


}

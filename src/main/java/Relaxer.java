import java.awt.*;

final class Relaxer {

    final Graph graph;

    Relaxer(Graph graph) {
        this.graph = graph;
    }

    GEdge[] edges() { return graph.edges(); }
    GNode[] nodes() { return graph.nodes(); }

    void relax(Dimension size) {
        contractEdges();
        GNode.repelOtherNodes(nodes());
        moveNodes(size);
    }

    private void contractEdges() {
        for (GEdge e : edges()) {
            e.contract();
        }
    }

    private void moveNodes(Dimension size) {
        for (GNode n : nodes()) {
            n.moveRestrictedTo(size);
        }
    }

}

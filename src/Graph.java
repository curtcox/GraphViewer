import java.awt.*;
import java.util.*;

class Graph {

    private final GEdge[] edges;
    private final GNode[] nodes;
    private final Dimension size;

    Graph(GEdge[] edges, GNode[] nodes, Dimension size) {
        this.edges = edges;
        this.nodes = nodes;
        this.size = size;
    }

    GEdge[] edges() { return edges; }
    GNode[] nodes() { return nodes; }
    int nodeCount() { return nodes.length; }
    int edgeCount() { return edges.length; }
    int crossingCount() {
        int count = 0;
        for (GEdge e1 : edges) {
            for (GEdge e2 : edges) {
                if (e1 != e2 && e1.crosses(e2)) {
                    count++;
                }
            }
        }
        return count;
    }

    void relax() {
        relaxEdges();
        relaxNodes();
    }

    private void relaxEdges() {
        for (GEdge e : edges) {
            e.relax();
        }
    }

    void relaxNodes() {
        GNode.relaxNodes(nodes);
        for (GNode n : nodes) {
            n.relax(size);
        }
    }

    void scramble() {
        GNode.scramble(nodes,size);
    }

    void shake() {
        GNode.shake(nodes);
    }

    GNode findNearestNode(int x, int y) {
        double bestdist = Double.MAX_VALUE;
        GNode nearest = null;

        for (GNode n : nodes) {
            double dist = n.distanceTo(x,y);
            if (dist < bestdist) {
                nearest = n;
                bestdist = dist;
            }
        }
        return nearest;
    }

    public String toString() {
        return "Graph nodes = " + Arrays.asList(nodes) + " edges = " + Arrays.asList(edges);
    }

}

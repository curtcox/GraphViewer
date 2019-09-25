import java.awt.*;
import java.util.*;

class Graph {

    final GEdge[] edges;
    final GNode[] nodes;
    final Dimension size;

    Graph(GEdge[] edges, GNode[] nodes, Dimension size) {
        this.edges = edges;
        this.nodes = nodes;
        this.size = size;
    }

    void relax() {
        relaxEdges();
        relaxNodes();
    }

    void relaxEdges() {
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
            double dist = (n.x - x) * (n.x - x) + (n.y - y) * (n.y - y);
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

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

    Graph relax() {
        relaxEdges();
        relaxNodes();
        return this;
    }

    private Graph relaxEdges() {
        return new Graph(GEdge.relax(edges),nodes,size);
    }

    private Graph relaxNodes() {
        GNode[] relaxed = GNode.relaxNodes(nodes);
        for (int i=0; i<relaxed.length; i++) {
            relaxed[i] = relaxed[i].relax(size);
        }
        return new Graph(edges,relaxed,size);
    }

    Graph scramble() {
        return new Graph(edges,GNode.scramble(nodes,size),size);
    }

    Graph shake() {
        return new Graph(edges,GNode.shake(nodes),size);
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

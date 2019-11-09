import java.awt.*;
import java.util.*;

final class Graph {

    private final GEdge[] edges;
    private final GNode[] nodes;

    Graph(GEdge[] edges, GNode[] nodes) {
        this.edges = edges;
        this.nodes = nodes;
    }

    GEdge[] edges() { return edges; }
    GNode[] nodes() { return nodes; }
    int nodeCount() { return nodes.length; }
    int edgeCount() { return edges.length; }
    int crossingCount() {
        int count = 0;
        for (var e1 : edges) {
            for (var e2 : edges) {
                if (e1 != e2 && e1.crosses(e2)) {
                    count++;
                }
            }
        }
        return count;
    }

    void markCycles()          { new CycleFinder(this).markCycles(); }
    void relax(Dimension size) { new Relaxer(this).relax(size); }
    void solve(GraphPainter painter, Dimension size) {
        new Solver(this).solve(painter,size);
    }

    void scramble(Dimension size) { GNode.scramble(nodes,size); }
    void shake()                  { GNode.shake(nodes); }

    GNode findNearestNode(int x, int y) {
        double bestdist = Double.MAX_VALUE;
        GNode nearest = null;

        for (var n : nodes) {
            double dist = n.distanceTo(new XY(x,y));
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

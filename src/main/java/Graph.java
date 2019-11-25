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
    Knot[]  knots() {
        var knots = new ArrayList<Knot>();
        for (var n : nodes) {
            var knot = n.knot;
            if (!knots.contains(knot)) {
                knots.add(knot);
            }
        }
        return knots.toArray(new Knot[0]);
    }
    int nodeCount() { return nodes.length; }
    int knotCount() { return knots().length; }
    int edgeCount() { return edges.length; }

    int crossingCount(GraphFilter filter) {
        int count = 0;
        for (var e1 : edges) {
            for (var e2 : edges) {
                if (e1 != e2 && filter.passes(e1) && filter.passes(e2) && e1.crosses(e2)) {
                    count++;
                }
            }
        }
        return count;
    }

    void markCycles()          { KnotFinder.find(this); }
    void relax(Dimension size) { new Relaxer(this).relax(size); }
    void solve(GraphPainter painter, Dimension size) {
        Solver.solve(this,painter,size);
    }

    void scramble(Dimension size) { GNode.scramble(nodes,size); }
    void shake()                  { GNode.shake(nodes); }

    private static class Nearest {
        double dist = Double.MAX_VALUE;
        GNode node;
    }

    GNode findNearestNode(XY xy) {
        return findNearest(xy).node;
    }

    private static final int radius = 50;
    GNode findNodeUnderMouse(XY xy) {
        var nearest = findNearest(xy);
        return nearest.dist < radius ? nearest.node : null;
    }

    private Nearest findNearest(XY xy) {
        var nearest = new Nearest();

        for (var n : nodes) {
            double dist = n.distanceTo(xy);
            if (dist < nearest.dist) {
                nearest.node = n;
                nearest.dist = dist;
            }
        }
        return nearest;
    }

    public String toString() {
        return "Graph nodes = " + Arrays.asList(nodes) + " edges = " + Arrays.asList(edges);
    }

}

package com.curtcox.graphviewer;

import java.awt.*;
import java.util.*;

class Graph {

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
        for (GEdge e1 : edges) {
            for (GEdge e2 : edges) {
                if (e1 != e2 && e1.crosses(e2)) {
                    count++;
                }
            }
        }
        return count;
    }

    void markCycles() {
        for (GNode node : nodes()) {
            node.isInCycle = isNodeInCycle(node);
        }
    }

    private boolean isNodeInCycle(GNode node) {
        return descendantsOf(node).contains(node);
    }

    private Collection<GNode> descendantsOf(GNode node) {
        var all = new LinkedList<GNode>();
        var todo = new LinkedList<GNode>();
        todo.add(node);
        var loop = false;
        while (!todo.isEmpty() && !loop) {
            GNode current = todo.removeFirst();
            all.add(current);
            for (GNode child : childrenOf(current)) {
                if (all.contains(child)) {
                    loop = true;
                }
                todo.add(child);
            }
        }
        if (!loop) {
            all.remove(node);
        }
        return all;
    }

    private Collection<GNode> childrenOf(GNode node) {
        var all = new HashSet<GNode>();
        for (GNode other : nodes) {
            if (isChildOf(other,node)) {
                all.add(other);
            }
        }
        return all;
    }

    private boolean isChildOf(GNode other, GNode node) {
        for (GEdge edge : edges) {
            if (edge.from == node && edge.to == other) {
                return true;
            }
        }
        return false;
    }

    void relax(Dimension size) {
        contractEdges();
        GNode.repelOtherNodes(nodes);
        moveNodes(size);
    }

    static class Position {
        GNode node;
        XY start;

        void revert() {
            node.xy = start;
        }
    }

    void solve(GraphPainter painter, Dimension size) {
        int  best    = defectCount(painter);
        long endTime = System.currentTimeMillis() + 100;
        GNode node = pickARandomNode();
        while (!after(endTime)) {
            Position original = randomlyMove(node,size);
            int defects = defectCount(painter);
            if (defects > best) {
                original.revert();
            } else {
                best = defects;
            }
        }
    }

    private Position randomlyMove(GNode node, Dimension size) {
        Position position = new Position();
        position.node = node;
        position.start = node.xy;
        node.xy = new XY(random(size.width),random(size.height));
        return position;
    }

    private GNode pickARandomNode() {
        return nodes[random(nodes.length)];
    }

    private static final Random random = new Random();
    private static int random(int max) {
        return random.ints(0, (max)).limit(1).findFirst().getAsInt();

    }
    private static boolean after(long time) {
        return System.currentTimeMillis() > time;
    }

    private int defectCount(GraphPainter painter) {
        int overlaps = painter.overlapCount();
        return crossingCount() + overlaps * overlaps;
    }

    private void contractEdges() {
        for (GEdge e : edges) {
            e.contract();
        }
    }

    private void moveNodes(Dimension size) {
        for (GNode n : nodes) {
            n.moveRestrictedTo(size);
        }
    }

    void scramble(Dimension size) {
        GNode.scramble(nodes,size);
    }

    void shake() {
        GNode.shake(nodes);
    }

    GNode findNearestNode(int x, int y) {
        double bestdist = Double.MAX_VALUE;
        GNode nearest = null;

        for (GNode n : nodes) {
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

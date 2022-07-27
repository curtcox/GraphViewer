package com.curtcox.graphviewer;

import java.util.*;

final class KnotFinder {

    final Graph graph;
    final Map<GNode,Set<GNode>> reachable = new HashMap<>();

    private KnotFinder(Graph graph) {
        this.graph = graph;
    }

    GEdge[] edges() { return graph.edges(); }
    GNode[] nodes() { return graph.nodes(); }

    static void find(Graph graph) {
        var finder = new KnotFinder(graph);
        finder.findReachable();
        finder.markKnots();
    }

    void markKnots() {
        for (var a : graph.nodes()) {
            var fromA = reachable.get(a);
            var knot = new HashSet<GNode>();
            knot.add(a);
            for (var b : fromA) {
                if (reachable.get(b).contains(a)) {
                    knot.add(b);
                }
            }
            a.knot = Knot.of(knot);
        }
    }

    void findReachable() {
        for (var n : graph.nodes()) {
            findReachable(n);
        }
    }

    void findReachable(GNode root) {
        var descendants = new HashSet<GNode>();
        var todo = new LinkedList<GNode>();
        var done = new HashSet<GNode>();
        todo.add(root);
        while (!todo.isEmpty()) {
            var current = todo.removeFirst();
            descendants.add(current);
            done.add(current);
            for (var child : childrenOf(current)) {
                if (!done.contains(child)) {
                    todo.add(child);
                }
            }
        }
        reachable.put(root,descendants);
    }

    final Map<GNode,Set<GNode>> children = new HashMap<>();
    private Set<GNode> childrenOf(GNode node) {
        if (!children.containsKey(node)) {
            children.put(node,childrenOf0(node));
        }
        return children.get(node);
    }

    private Set<GNode> childrenOf0(GNode node) {
        var all = new HashSet<GNode>();
        for (GNode other : nodes()) {
            if (isChildOf(other,node)) {
                all.add(other);
            }
        }
        return all;
    }

    private boolean isChildOf(GNode other, GNode node) {
        for (var edge : edges()) {
            if (edge.from == node && edge.to == other) {
                return true;
            }
        }
        return false;
    }

}

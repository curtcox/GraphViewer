import java.util.*;

final class CycleFinder {

    final Graph graph;

    CycleFinder(Graph graph) {
        this.graph = graph;
    }

    GEdge[] edges() { return graph.edges(); }
    GNode[] nodes() { return graph.nodes(); }

    void markKnots() {
        markCyclesBasedOnNodeOfOrigin();
        combineKnots();
    }

    private void combineKnots() {
        for (var n1 : nodes()) {
            for (var n2 : nodes()) {
                if (n1.knot.containsAnyCyclesFrom(n2.knot)) {
                     var combined = n1.knot.combinedWith(n2.knot);
                     n1.knot = combined;
                     n2.knot = combined;
                }
            }
        }
    }

    private void markCyclesBasedOnNodeOfOrigin() {
        int done = 0;
        for (var node : nodes()) {
            println(done + " of " + nodes().length);
            node.knot = Knot.of(cyclesContainingNode(node));
            done++;
        }
    }

    private Set<Cycle> cyclesContainingNode(final GNode root) {
        var cycles = new HashSet<Cycle>();
        var todo = new HashSet<Chain>();
        var chain = Chain.EMPTY.plus(root);
        todo.add(chain);
        while (!todo.isEmpty()) {
            chain = todo.iterator().next();
            todo.remove(chain);
            GNode tail = chain.lastNode();
            for (var child : childrenOf(tail)) {
                var next = chain.plus(child);
                if (next.isCompleteCycle()) {
                    cycles.add(Cycle.of(next.path));
                } else if (next.isCycleWithTail()) {
                } else {
                    todo.add(next);
                }
            }
        }
        return cycles;
    }

    private Map<GNode,Set<GNode>> childrenOfCache = new HashMap<>();
    private Set<GNode> childrenOf(GNode parent) {
        if (childrenOfCache.containsKey(parent)) {
            return childrenOfCache.get(parent);
        }
        var children = childrenOf0(parent);
        childrenOfCache.put(parent,children);
        return children;
    }

    private Set<GNode> childrenOf0(GNode parent) {
        var children = new HashSet<GNode>();
        for (GNode other : nodes()) {
            if (isChildOf(other,parent)) {
                children.add(other);
            }
        }
        return children;
    }

    private boolean isChildOf(GNode other, GNode node) {
        for (var edge : edges()) {
            if (edge.from == node && edge.to == other) {
                return true;
            }
        }
        return false;
    }

    static void println(String s) {
        System.out.println(s);
    }
}

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

final class CycleFinder {

    final Graph graph;

    CycleFinder(Graph graph) {
        this.graph = graph;
    }

    GEdge[] edges() { return graph.edges(); }
    GNode[] nodes() { return graph.nodes(); }

    void markCycles() {
        for (GNode node : nodes()) {
            var cycle = cycleContainingNode(node);
            if (cycle != null) {
                node.cycles.add(cycle);
            }
        }
    }

    private Cycle cycleContainingNode(GNode node) {
        var descendants = descendantsOf(node);
        if (descendants.contains(node)) {
            return Cycle.of(descendants);
        } else {
            return null;
        }
    }

    private Set<GNode> descendantsOf(final GNode root) {
        var descendants = new HashSet<GNode>();
        var todo = new LinkedList<GNode>();
        var done = new HashSet<GNode>();
        todo.add(root);
        boolean cycle = false;
        while (!todo.isEmpty()) {
            var current = todo.removeFirst();
            descendants.add(current);
            done.add(current);
            for (var child : childrenOf(current)) {
                if (child.equals(root)) {
                    cycle = true;
                }
                if (!done.contains(child)) {
                    todo.add(child);
                }
            }
        }
        if (!cycle) {
            descendants.remove(root);
        }
        return descendants;
    }

    private Set<GNode> childrenOf(GNode node) {
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

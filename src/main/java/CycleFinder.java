import java.util.*;

final class CycleFinder {

    final Graph graph;

    CycleFinder(Graph graph) {
        this.graph = graph;
    }

    GEdge[] edges() { return graph.edges(); }
    GNode[] nodes() { return graph.nodes(); }

    void markCycles() {
        for (var node : nodes()) {
            node.cycles = cyclesContainingNode(node);
        }
    }

    private Set<Cycle> cyclesContainingNode(GNode node) {
        var cycles = new HashSet<Cycle>();
        for (var chain : descendantsOf(node)) {
            if (chain.isCycle()) {
                cycles.add(Cycle.of(new HashSet(chain.path)));
            }
        }
        return cycles;
    }

    private Set<Chain> descendantsOf(final GNode root) {
        var chains = new HashMap<GNode,Chain>(); // end node -> chain
        var todo = new HashSet<GNode>();
        todo.add(root);
        chains.put(root,Chain.EMPTY.plus(root));
        while (!todo.isEmpty()) {
            GNode current = todo.iterator().next();
            todo.remove(current);
            var chain = chains.get(current);
            for (var child : childrenOf(current)) {
                if (chain.addingWouldMakeCompleteCycle(child)) {
                    chains.put(child,chain.plus(child));
                } else if (!chain.addingWouldMakeCycleWithTail(child)) {
                    chains.put(child,chain.plus(child));
                    todo.add(child);
                }
            }
        }
        return new HashSet<>(chains.values());
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

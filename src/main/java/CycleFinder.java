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
            node.knot = Knot.of(cyclesContainingNode(node));
        }
    }

    private Set<Cycle> cyclesContainingNode(GNode node) {
        var cycles = new HashSet<Cycle>();
        for (var chain : descendantsOf(node)) {
            if (chain.isCycle()) {
                cycles.add(Cycle.of(chain.path));
            }
        }
        println("Cycles of " + node +" are " + cycles);
        return cycles;
    }

    private Set<Chain> descendantsOf(final GNode root) {
        println(root + " descendants");
        var chains = new HashSet<Chain>();
        var todo = new HashSet<Chain>();
        var chain = Chain.EMPTY.plus(root);
        todo.add(chain);
        while (!todo.isEmpty()) {
            chain = todo.iterator().next();
            todo.remove(chain);
            GNode tail = chain.lastNode();
            for (var child : childrenOf(tail)) {
                println(root + ": " + chain + " ->?" + child);
                if (chain.addingWouldMakeCompleteCycle(child)) {
                    chains.add(chain.plus(child));
                    println("addingWouldMakeCompleteCycle -> " + chains);
                } else if (!chain.addingWouldMakeCycleWithTail(child)) {
                    var next = chain.plus(child);
                    chains.add(next);
                    println("!addingWouldMakeCycleWithTail -> " + chains);
                    todo.add(next);
                } else {
                    println(root + ": Not adding " + child + " to " + chain);
                }
            }
        }
        println(root + ": descendants are " + chains);
        return chains;
    }

    private Set<GNode> childrenOf(GNode parent) {
        var children = new HashSet<GNode>();
        for (GNode other : nodes()) {
            if (isChildOf(other,parent)) {
                children.add(other);
            }
        }
        println(parent + " children are " + children);
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

    }
}

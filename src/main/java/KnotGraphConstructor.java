import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

class KnotGraphConstructor {

    private final Graph graph;

    private KnotGraphConstructor(Graph graph) {
        this.graph = graph;
    }

    static Graph makeFrom(Graph graph) {
        return new KnotGraphConstructor(graph).make();
    }

    private Graph make() {
        var nodes = nodes();
        var edges = edges(nodes);
        return new Graph(edges,nodes);
    }

    private GEdge[] edges(GNode[] nodes) {
        var edges = new HashSet<GEdge>();
        return new ArrayList<>(edges).toArray(new GEdge[0]);
    }

    private GNode[] nodes() {
        var knots = new HashSet<GNode>();
        for (var node : graph.nodes()) {
            knots.add(node(node));
        }
        return new ArrayList<>(knots).toArray(new GNode[0]);
    }

    private GNode node(GNode node) {
        var knot    = node.knot;
        var label   = knot.toString();
        var derived = GNode.of(label);
        derived.knot = Knot.of(Collections.singleton(node));
        derived.xy  = node.xy;
        return derived;
    }
}

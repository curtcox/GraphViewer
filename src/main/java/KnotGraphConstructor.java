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
        var nodes = new HashSet<GNode>();
        for (var knot : graph.knots()) {
            nodes.add(node(knot));
        }
        return new ArrayList<>(nodes).toArray(new GNode[0]);
    }

    private static GNode node(Knot knot) {
        var label   = knot.toString();
        var derived = GNode.of(label);
        derived.knot = Knot.of(Collections.singleton(derived));
        derived.xy  = xy(knot);
        return derived;
    }

    private static XY xy(Knot knot) {
        return knot.nodes.iterator().next().xy;
    }
}

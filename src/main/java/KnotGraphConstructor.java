import java.util.*;

class KnotGraphConstructor {

    private final Graph graph;

    private KnotGraphConstructor(Graph graph) {
        this.graph = graph;
    }

    static GraphMap makeFrom(Graph graph) {
        return new KnotGraphConstructor(graph).make();
    }

    private GraphMap make() {
        var mapping = mapKnots();
        var nodes = mapping.values().toArray(new GNode[0]);
        var edges = edges(nodes);
        var dest = new Graph(edges,nodes);
        return new GraphMap(graph,dest,mapping,inverse(mapping));
    }

    private Map<GNode, Knot> inverse(Map<Knot, GNode> map) {
        var inverse = new HashMap<GNode,Knot>();
        for (var entry : map.entrySet()) {
            inverse.put(entry.getValue(),entry.getKey());
        }
        return inverse;
    }

    private GEdge[] edges(GNode[] nodes) {
        var edges = new HashSet<GEdge>();
        return new ArrayList<>(edges).toArray(new GEdge[0]);
    }

    private Map<Knot,GNode> mapKnots() {
        var knots = new HashMap<Knot,GNode>();
        for (var knot : graph.knots()) {
            knots.put(knot,node(knot));
        }
        return knots;
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

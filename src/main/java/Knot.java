import java.util.*;

/**
 * A set of digraph nodes where every node is reachable by every other node.
 * In other words, the nodes are all strongly connected.
 * https://algs4.cs.princeton.edu/42digraph/
 */
final class Knot {

    final Set<GNode> nodes;
    final int number;
    final static Knot empty = new Knot(new HashSet<>(),0);
    static final Map<Set<GNode>,Knot> knots = new HashMap<>();
    static {
        knots.put(new HashSet<>(),empty);
    }

    private Knot(Set<GNode> nodes,int number) {
        this.nodes = nodes;
        this.number = number;
    }

    static Knot of(Set<GNode> nodes) {
        if (knots.containsKey(nodes)) {
            return knots.get(nodes);
        }
        if (isSimplePair(nodes)) {
            return new Knot(nodes,1);
        }
        var knot = new Knot(nodes,knots.size() + 2);
        knots.put(nodes,knot);
        System.out.println("Knot #" + knots.size() + " contains " + nodes);
        return knot;
    }

    boolean contains(GNode n) {
        return nodes.contains(n);
    }

    static boolean isSimplePair(Set<GNode> nodes) {
        return  nodes.size() == 2;
    }

    boolean isEmpty() {
        return nodes.isEmpty();
    }

    int size() {
        return nodes.size();
    }

    public int hashCode() { return nodes.hashCode(); }
    public boolean equals(Object o) {
        var that = (Knot) o;
        return nodes.equals(that.nodes);
    }

    public String toString() {
        return "knot(" + nodes + ")";
    }

}

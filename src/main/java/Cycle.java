import java.util.*;

final class Cycle {

    final Set<GNode> nodes;
    static final Map<Set<GNode>,Cycle> cycles = new HashMap<>();

    private Cycle(Set<GNode> nodes) {
        this.nodes = nodes;
    }

    static Cycle of(Set<GNode> nodes) {
        if (cycles.containsKey(nodes)) {
            return cycles.get(nodes);
        }
        var cycle = new Cycle(nodes);
        cycles.put(nodes,cycle);
        System.out.println("Cycle #" + cycles.size() + " contains " + nodes);
        return cycle;
    }

    public int hashCode() { return nodes.hashCode(); }
    public boolean equals(Object o) {
        var that = (Cycle) o;
        return nodes.equals(that.nodes);
    }

    public String toString() { return "cycle(" + nodes + ")"; }
}

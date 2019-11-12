import java.util.*;

final class Cycle {

    final Set<GNode> nodes;
    static final Map<Set<GNode>,Cycle> cycles = new HashMap<>();

    private Cycle(Set<GNode> nodes) {
        this.nodes = nodes;
    }

    static Cycle of(Collection<GNode> collection) {
        var nodes = new HashSet<>(collection);
        if (cycles.containsKey(nodes)) {
            return cycles.get(nodes);
        }
        var cycle = new Cycle(nodes);
        cycles.put(nodes,cycle);
        System.out.println("Cycle #" + cycles.size() + " contains " + nodes);
        return cycle;
    }

    public String toString() { return "cycle(" + nodes + ")"; }
}

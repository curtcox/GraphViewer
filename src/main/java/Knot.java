import java.util.*;

/**
 * A set of digraph nodes where every node is reachable by every other node.
 */
final class Knot {

    final Set<Cycle> cycles;
    final int number;
    final static Knot empty = new Knot(new HashSet<>(),0);
    static final Map<Set<Cycle>,Knot> knots = new HashMap<>();
    static {
        knots.put(new HashSet<>(),empty);
    }

    private Knot(Set<Cycle> cycles,int number) {
        this.cycles = cycles;
        this.number = number;
    }

    static Knot of(Set<Cycle> cycles) {
        if (knots.containsKey(cycles)) {
            return knots.get(cycles);
        }
        if (isSimplePair(cycles)) {
            return new Knot(cycles,1);
        }
        var knot = new Knot(cycles,knots.size() + 2);
        knots.put(cycles,knot);
        System.out.println("Knot #" + knots.size() + " contains " + cycles);
        return knot;
    }

    boolean contains(GNode n) {
        for (var cycle : cycles) {
            if (cycle.contains(n)) {
                return true;
            }
        }
        return false;
    }

    static boolean isSimplePair(Set<Cycle> cycles) {
        return  cycles.size() == 1 &&
                cycles.iterator().next().nodes.size() == 2;
    }

    boolean isEmpty() {
        return cycles.isEmpty();
    }

    boolean containsAnyCyclesFrom(Knot knot) {
        return !intersection(cycles,knot.cycles).isEmpty();
    }

    Knot combinedWith(Knot knot) {
        return Knot.of(union(cycles,knot.cycles));
    }

    static private Set<Cycle> union(Set<Cycle> a, Set<Cycle> b) {
        var all = new HashSet<>(a);
        all.addAll(b);
        return all;
    }

    static private Set<Cycle> intersection(Set<Cycle> a, Set<Cycle> b) {
        var all = new HashSet<>(a);
        all.retainAll(b);
        return all;
    }

    public int hashCode() { return cycles.hashCode(); }
    public boolean equals(Object o) {
        var that = (Knot) o;
        return cycles.equals(that.cycles);
    }

    public String toString() {
        return "knot(" + cycles + ")";
    }

}

import java.util.*;

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

    static boolean isSimplePair(Set<Cycle> cycles) {
        return  cycles.size() == 1 &&
                cycles.iterator().next().nodes.size() == 2;
    }

    boolean isEmpty() {
        return cycles.isEmpty();
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

import java.awt.*;
import java.util.*;

final class Cycle {

    final Color color;
    final Set<GNode> nodes;
    static final Map<Set<GNode>,Cycle> cycles = new HashMap<>();

    private Cycle(Set<GNode> nodes, Color color) {
        this.nodes = nodes;
        this.color = color;
    }

    static Cycle of(Set<GNode> nodes) {
        if (cycles.containsKey(nodes)) {
            return cycles.get(nodes);
        }
        var cycle = new Cycle(nodes,color(cycles.size()));
        cycles.put(nodes,cycle);
        System.out.println("Cycle #" + cycles.size() + " contains " + nodes);
        return cycle;
    }

    private static final Color color(int number) {
        return new Color(50, 200, 50);
    }

}

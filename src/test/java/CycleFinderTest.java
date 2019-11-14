import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class CycleFinderTest {

    @Test
    public void can_create() {
        new KnotFinder(new Graph(new GEdge[0],new GNode[0]));
    }

    static Graph graph(String s) {
        var parts = s.split(" ");
        var edges = new ArrayList<String>();
        for (var p : parts) {
            edges.add("|" + p.substring(0,1) + "|" + p.substring(1,2));
        }
        var center = "a";
        var reader = new GraphReader(edges.toArray(new String[0]),center);
        return reader.read();
    }

    @Test
    public void line_not_marked_as_cycle() {
        var graph = graph("ab bc cd de");
        var nodes = graph.nodes();
        assertEquals(5,nodes.length);
        for (GNode node : nodes) {
            assertFalse(node.isInCycle());
        }
    }

    @Test
    public void cycle_of_2_marked_as_cycle() {
        var graph = graph("ab ba");
        assertInCycle("a",graph);
        assertInCycle("b",graph);
    }

    @Test
    public void cycle_of_3_marked_as_cycle() {
        var graph = graph("ab bc ca");
        var a = node("a",graph);
        var b = node("b",graph);
        var c = node("c",graph);
        var cycles = new HashSet<Cycle>();
        cycles.add(cycle(a,b,c));
        var knot = Knot.of(cycles);
        assertEquals(knot,a.knot);

        assertInCycle("a",graph);
        assertInCycle("b",graph);
        assertInCycle("c",graph);
    }

    @Test
    public void cycle_of_4_marked_as_cycle() {
        var graph = graph("ab bc cd da");
        var a = node("a",graph);
        var b = node("b",graph);
        var c = node("c",graph);
        var d = node("d",graph);
        var cycles = new HashSet<Cycle>();
        cycles.add(cycle(a,b,c,d));
        var knot = Knot.of(cycles);
        assertEquals(knot,a.knot);

        assertInCycle("a",graph);
        assertInCycle("b",graph);
        assertInCycle("c",graph);
        assertInCycle("d",graph);
    }

    @Test
    public void node_in_2_cycles() {
        var graph = graph("ab ba bc cb");
        var a = node("a",graph);
        var b = node("b",graph);
        var c = node("c",graph);
        var knot = b.knot;

        var message = knot + " in " + graph;
        assertEquals(message,2,knot.cycles.size());
        assertTrue(knot.cycles.contains(cycle(a,b)));
        assertTrue(knot.cycles.contains(cycle(b,c)));
    }

    @Test
    public void node_in_3_cycles() {
        var graph = graph("ab ba bc cb bd db");
        var a = node("a",graph);
        var b = node("b",graph);
        var c = node("c",graph);
        var d = node("d",graph);
        var knot = b.knot;

        var message = knot + " in " + graph;
        assertEquals(message,3,knot.cycles.size());
        assertTrue(knot.cycles.contains(cycle(a,b)));
        assertTrue(knot.cycles.contains(cycle(b,c)));
        assertTrue(knot.cycles.contains(cycle(b,d)));
    }

    @Test
    public void cycles_that_share_a_node_marked_as_one_knot() {
        var graph = graph("ab ba bc cb");
        var a = node("a",graph);
        var b = node("b",graph);
        var c = node("c",graph);
        var knot = b.knot;

        var message = knot + " in " + graph;
        assertEquals(message,2,knot.cycles.size());
        assertSame(knot,a.knot);
        assertSame(knot,b.knot);
        assertSame(knot,c.knot);
    }

    private Cycle cycle(GNode... nodes) {
        return Cycle.of(new HashSet<>(Arrays.asList(nodes)));
    }

    @Test
    public void cycles_that_do_NOT_share_a_node_marked_as_2_knots() {
        var graph = graph("ab ba cd dc");
        var a = node("a",graph);
        var b = node("b",graph);
        var c = node("c",graph);
        var d = node("d",graph);

        var message = "knot " + a.knot;
        assertEquals(message,1,a.knot.cycles.size());
        assertTrue(a.knot.cycles.contains(cycle(a,b)));
        assertEquals(a.knot,b.knot);

        assertNotEquals(a.knot,c.knot);
        assertNotEquals(a.knot,d.knot);

        assertEquals(1,c.knot.cycles.size());
        assertTrue(c.knot.cycles.contains(cycle(c,d)));
        assertEquals(c.knot,d.knot);
    }

    @Test
    public void cycles_that_do_NOT_share_a_node_marked_as_3_knots() {
        var graph = graph("ab ba cd dc ef fe");
        var a = node("a",graph);
        var b = node("b",graph);
        var c = node("c",graph);
        var d = node("d",graph);
        var e = node("e",graph);
        var f = node("f",graph);

        var message = "knot " + a.knot;
        assertEquals(message,1,a.knot.cycles.size());
        assertTrue(a.knot.cycles.contains(cycle(a,b)));
        assertEquals(a.knot,b.knot);

        assertNotEquals(a.knot,c.knot);
        assertNotEquals(a.knot,d.knot);
        assertNotEquals(a.knot,e.knot);
        assertNotEquals(a.knot,f.knot);

        assertEquals(1,c.knot.cycles.size());
        assertEquals(1,d.knot.cycles.size());
        assertEquals(1,e.knot.cycles.size());
        assertEquals(1,f.knot.cycles.size());
        assertTrue(c.knot.cycles.contains(cycle(c,d)));
        assertEquals(c.knot,d.knot);
        assertTrue(e.knot.cycles.contains(cycle(e,f)));
        assertEquals(e.knot,f.knot);
    }

    private GNode node(String name, Graph graph) {
        for (var node : graph.nodes()) {
            if (node.label.equals(name)) {
                return node;
            }
        }
        return null;
    }

    private void assertInCycle(String name, Graph graph) {
        assertTrue(node(name,graph).isInCycle());
    }

}

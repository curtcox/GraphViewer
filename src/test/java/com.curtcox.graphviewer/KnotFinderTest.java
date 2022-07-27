package com.curtcox.graphviewer;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class KnotFinderTest {

    @Test
    public void can_create() {
        KnotFinder.find(new Graph(new GEdge[0],new GNode[0]));
    }

    static Graph graph(String s) {
        var parts = s.split(" ");
        var edges = new ArrayList<String>();
        for (var p : parts) {
            edges.add("|" + p.substring(0,1) + "|" + p.substring(1,2));
        }
        var center = "a";
        var reader = new GraphReader(edges.toArray(new String[0]),center);
        var graph = reader.read();
        KnotFinder.find(graph);
        return graph;
    }

    @Test
    public void line_not_marked_as_knot() {
        var graph = graph("ab bc cd de");
        var nodes = graph.nodes();
        assertEquals(5,nodes.length);
        for (GNode node : nodes) {
            assertFalse(node.isInKnotWithMultipleNodes());
        }
    }

    @Test
    public void knot_of_2_marked_as_knot() {
        var graph = graph("ab ba");
        var a = node("a",graph);
        var b = node("b",graph);
        var knot = Knot.of(nodes(a,b));
        assertEquals(knot,a.knot);
    }

    @Test
    public void cycle_of_3_marked_as_knot() {
        var graph = graph("ab bc ca");
        var a = node("a",graph);
        var b = node("b",graph);
        var c = node("c",graph);
        var knot = Knot.of(nodes(a,b,c));
        assertEquals(knot,a.knot);
    }

    @Test
    public void cycle_of_4_marked_as_knot() {
        var graph = graph("ab bc cd da");
        var a = node("a",graph);
        var b = node("b",graph);
        var c = node("c",graph);
        var d = node("d",graph);
        var knot = Knot.of(nodes(a,b,c,d));
        assertEquals(knot,a.knot);
    }

    @Test
    public void node_in_2_cycles() {
        var graph = graph("ab ba bc cb");
        var a = node("a",graph);
        var b = node("b",graph);
        var c = node("c",graph);
        var knot = Knot.of(nodes(a,b,c));
        assertEquals(knot,a.knot);
    }

    @Test
    public void node_in_3_cycles() {
        var graph = graph("ab ba bc cb bd db");
        var a = node("a",graph);
        var b = node("b",graph);
        var c = node("c",graph);
        var d = node("d",graph);
        var knot = Knot.of(nodes(a,b,c,d));
        assertEquals(knot,a.knot);
    }

    @Test
    public void cycles_that_share_a_node_marked_as_one_knot() {
        var graph = graph("ab ba bc cb");
        var a = node("a",graph);
        var b = node("b",graph);
        var c = node("c",graph);
        var knot = b.knot;

        var message = knot + " in " + graph;
        assertEquals(message,3,knot.size());
        assertSame(knot,a.knot);
        assertSame(knot,b.knot);
        assertSame(knot,c.knot);
    }

    private Set<GNode> nodes(GNode... nodes) {
        return new HashSet<>(Arrays.asList(nodes));
    }

    @Test
    public void cycles_that_do_NOT_share_a_node_marked_as_2_knots() {
        var graph = graph("ab ba cd dc");
        var a = node("a",graph);
        var b = node("b",graph);
        var c = node("c",graph);
        var d = node("d",graph);

        var message = "knot " + a.knot;
        assertEquals(message,2,a.knot.size());
        assertTrue(a.knot.contains(a));
        assertTrue(a.knot.contains(b));
        assertEquals(a.knot,b.knot);

        assertNotEquals(a.knot,c.knot);
        assertNotEquals(a.knot,d.knot);

        assertEquals(2,c.knot.size());
        assertTrue(c.knot.contains(c));
        assertTrue(c.knot.contains(d));
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
        assertEquals(message,2,a.knot.size());
        assertTrue(a.knot.contains(a));
        assertTrue(a.knot.contains(b));
        assertEquals(a.knot,b.knot);

        assertNotEquals(a.knot,c.knot);
        assertNotEquals(a.knot,d.knot);
        assertNotEquals(a.knot,e.knot);
        assertNotEquals(a.knot,f.knot);

        assertEquals(2,c.knot.size());
        assertEquals(2,d.knot.size());
        assertEquals(2,e.knot.size());
        assertEquals(2,f.knot.size());
        assertTrue(c.knot.contains(c));
        assertTrue(c.knot.contains(d));
        assertEquals(c.knot,d.knot);
        assertTrue(e.knot.contains(e));
        assertTrue(e.knot.contains(f));
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

}

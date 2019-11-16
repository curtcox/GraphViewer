import static org.junit.Assert.*;
import org.junit.Test;

public class GraphReaderTest {
    @Test
    public void can_create_reader() {
        var edges = new String[] {""};
        var center = "c";
        new GraphReader(edges,center);
    }

    @Test
    public void can_create_graph_with_just_center() {
        var edges = new String[] {""};
        var center = "c";
        var reader = new GraphReader(edges,center);
        var graph = reader.read();
        var node = graph.nodes()[0];
        assertEquals(node.label,"c");
    }

    @Test
    public void line_not_marked_as_cycle() {
        var edges = new String[] {"|a|b","|b|c","|c|d","|d|e"};
        var center = "c";
        var reader = new GraphReader(edges,center);
        var graph = reader.read();
        var nodes = graph.nodes();
        assertEquals(5,nodes.length);
        for (GNode node : nodes) {
            assertFalse(node.isInKnotWithMultipleNodes());
        }
    }

    @Test
    public void tree_not_marked_as_cycle() {
        var edges = new String[] {"|a|b","|b|c","|b|d","|b|e"};
        var center = "c";
        var reader = new GraphReader(edges,center);
        var graph = reader.read();
        var nodes = graph.nodes();
        assertEquals(5,nodes.length);
        for (GNode node : nodes) {
            assertFalse(node.isInKnotWithMultipleNodes());
        }
    }

    @Test
    public void cycle_of_length_0_marked_as_cycle() {
        var edges = new String[] {"|c|c"};
        var center = "c";
        var reader = new GraphReader(edges,center);
        var graph = reader.read();
        var nodes = graph.nodes();
        assertEquals(1,nodes.length);
        var node = graph.nodes()[0];
        assertTrue(node.isInKnotWithMultipleNodes());
    }

    @Test
    public void cycle_of_length_1_marked_as_cycle() {
        var edges = new String[] {"|a|b","|b|a"};
        var center = "a";
        var reader = new GraphReader(edges,center);
        var graph = reader.read();
        var nodes = graph.nodes();
        assertEquals(2,nodes.length);
        for (GNode node : nodes) {
            assertTrue(node.isInKnotWithMultipleNodes());
        }
    }

    @Test
    public void cycle_of_length_2_marked_as_cycle() {
        var edges = new String[] {"|a|b","|b|c","|c|a"};
        var center = "a";
        var reader = new GraphReader(edges,center);
        var graph = reader.read();
        var nodes = graph.nodes();
        assertEquals(3,nodes.length);
        for (GNode node : nodes) {
            assertTrue(node.isInKnotWithMultipleNodes());
        }
    }

    @Test
    public void cycle_of_length_3_marked_as_cycle() {
        var edges = new String[] {"|a|b","|b|c","|c|d","|d|a"};
        var center = "a";
        var reader = new GraphReader(edges,center);
        var graph = reader.read();
        var nodes = graph.nodes();
        assertEquals(4,nodes.length);
        for (GNode node : nodes) {
            assertTrue(node.isInKnotWithMultipleNodes());
        }
    }

}


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
    public void simple_line_not_marked_as_circuit() {
        var edges = new String[] {"|a|b","|b|c","|c|d","|d|e"};
        var center = "c";
        var reader = new GraphReader(edges,center);
        var graph = reader.read();
        var nodes = graph.nodes();
        assertEquals(5,nodes.length);
        for (GNode node : nodes) {
            assertFalse(node.isCircuit);
        }
    }

}


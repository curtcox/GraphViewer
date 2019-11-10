import org.junit.Test;

public class CycleFinderTest {

    @Test
    public void can_create() {
        new CycleFinder(new Graph(new GEdge[0],new GNode[0]));
    }
}

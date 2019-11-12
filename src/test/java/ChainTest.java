import org.junit.Test;

import static org.junit.Assert.*;

public class ChainTest {

    final Chain empty = Chain.EMPTY;

    GNode node(int number) {
        return GNode.of(Integer.toString(number));
    }

    @Test
    public void addingWouldMakeACompleteCycle_always_false_when_empty() {
        assertFalse(empty.addingWouldMakeCompleteCycle(node(1)));
        assertFalse(empty.addingWouldMakeCompleteCycle(node(2)));
    }

    @Test
    public void addingWouldMakeACompleteCycle_always_false_when_not_adding_head() {
        var chain = empty.plus(node(0));
        assertFalse(chain.addingWouldMakeCompleteCycle(node(1)));
        assertFalse(chain.addingWouldMakeCompleteCycle(node(2)));
    }

    @Test
    public void addingWouldMakeACompleteCycle_always_true_when_not_adding_head() {
        var chain = empty.plus(node(0));
        assertTrue(chain.addingWouldMakeCompleteCycle(node(0)));
    }

    @Test
    public void addingWouldMakeCycleWithTail_always_false_when_empty() {
        assertFalse(empty.addingWouldMakeCycleWithTail(node(1)));
        assertFalse(empty.addingWouldMakeCycleWithTail(node(2)));
    }

    @Test
    public void addingWouldMakeCycleWithTail_always_false_when_adding_to_one_node() {
        var chain = empty.plus(node(0));
        assertFalse(chain.addingWouldMakeCycleWithTail(node(0)));
        assertFalse(chain.addingWouldMakeCycleWithTail(node(1)));
    }

    @Test
    public void addingWouldMakeCycleWithTail_true_when_readding_2nd_of_2() {
        var chain = empty.plus(node(0)).plus(node(1));
        assertFalse(chain.addingWouldMakeCycleWithTail(node(0)));
        assertFalse(chain.addingWouldMakeCycleWithTail(node(2)));
        assertTrue(chain.addingWouldMakeCycleWithTail(node(1)));
    }

}

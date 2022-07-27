package com.curtcox.graphviewer;

import com.curtcox.graphviewer.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class KnotGraphConstructorTest {

    @Test
    public void makes_graph() {
        var graph = new Graph(new GEdge[0],new GNode[0]);
        assertNotNull(KnotGraphConstructor.makeFrom(graph));
    }
}

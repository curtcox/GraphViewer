package com.curtcox.graphviewer;

import java.util.Map;

final class GraphMap {

    final Graph source;
    final Graph dest;
    final Map<Knot,GNode> mapping;
    final Map<GNode, Knot> inverse;

    GraphMap(Graph source, Graph dest, Map<Knot, GNode> mapping, Map<GNode, Knot> inverse) {
        this.source = source;
        this.dest = dest;
        this.mapping = mapping;
        this.inverse = inverse;
    }
}

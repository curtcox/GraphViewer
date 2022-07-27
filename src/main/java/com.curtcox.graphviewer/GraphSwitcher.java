package com.curtcox.graphviewer;

import com.curtcox.graphviewer.*;

final class GraphSwitcher {

    private final GraphMap map;

    GraphSwitcher(GraphMap map) {
        this.map = map;
    }

    Graph setKnots(boolean knots) {
        if (knots) {
            return switchToKnots();
        } else {
            return switchToNodes();
        }
    }

    private Graph switchToNodes() {
        var graph = map.source;
        for (var knot : graph.knots()) {
            for (var node : knot.nodes) {
                node.xy = map.mapping.get(knot).xy;
            }
        }
        return graph;
    }

    private Graph switchToKnots() {
        var graph = map.dest;
        for (var node : graph.nodes()) {
            var knot = map.inverse.get(node);
            node.xy = averageXY(knot);
        }
        return graph;
    }

    private XY averageXY(Knot knot) {
        double x = 0;
        double y = 0;
        for (var node : knot.nodes) {
            x += node.x();
            y += node.y();
        }
        double size = knot.size();
        return new XY(x/size,y/size);
    }

}

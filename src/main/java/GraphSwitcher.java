final class GraphSwitcher {

    private Graph nodesGraph;
    private Graph knotsGraph;

    GraphSwitcher(Graph nodesGraph, Graph knotsGraph) {
        this.nodesGraph = nodesGraph;
        this.knotsGraph = knotsGraph;
    }

    Graph setKnots(boolean knots) {
        if (knots) {
            return switchToKnots();
        } else {
            return switchToNodes();
        }
    }

    private Graph switchToNodes() {
        return nodesGraph;
    }

    private Graph switchToKnots() {
        return knotsGraph;
    }

}

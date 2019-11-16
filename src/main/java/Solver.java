import java.awt.*;
import java.util.Random;

final class Solver {

    final Graph graph;

    private Solver(Graph graph) {
        this.graph = graph;
    }

    static void solve(Graph graph,GraphPainter painter, Dimension size) {
        new Solver(graph).solve(painter,size);
    }

    GNode[] nodes() { return graph.nodes(); }

    void solve(GraphPainter painter, Dimension size) {
        int  best    = defectCount(painter);
        long endTime = System.currentTimeMillis() + 100;
        var node = pickARandomNode();
        while (!after(endTime)) {
            var original = randomlyMove(node,size);
            int defects = defectCount(painter);
            if (defects > best) {
                original.revert();
            } else {
                best = defects;
            }
        }
    }

    private static class Position {
        GNode node;
        XY start;

        void revert() {
            node.xy = start;
        }
    }

    private Position randomlyMove(GNode node, Dimension size) {
        var position = new Position();
        position.node = node;
        position.start = node.xy;
        node.xy = new XY(random(size.width),random(size.height));
        return position;
    }

    private GNode pickARandomNode() {
        return nodes()[random(nodes().length)];
    }

    private static final Random random = new Random();
    private static int random(int max) {
        return random.ints(0, (max)).limit(1).findFirst().getAsInt();

    }
    private static boolean after(long time) {
        return System.currentTimeMillis() > time;
    }

    private int defectCount(GraphPainter painter) {
        int overlaps = painter.overlapCount();
        return graph.crossingCount() + overlaps * overlaps;
    }

}

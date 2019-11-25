import javax.swing.*;
import java.awt.*;

final class GraphPanel extends JPanel {

    boolean relax;
    boolean solve;
    boolean xray;
    boolean knots;
    private GraphFilter filter;
    private Graph graph;
    private GraphPainter painter;
    private GraphSwitcher switcher;
    private GraphMouseAdapter mouse;
    private DirtyTracker dirtyTracker = new DirtyTracker();

    private GraphPanel() {}

    static GraphPanel newInstance() {
        var panel = new GraphPanel();
        panel.init();
        return panel;
    }

    private void init() {
        filter = new GraphFilter("");
        mouse = new GraphMouseAdapter(this);
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    GNode findNearestNode(XY xy) { return graph.findNearestNode(xy); }
    GNode findNodeUnderMouse(XY xy) { return graph.findNodeUnderMouse(xy); }

    private void relax() {
        graph.relax(getSize());
    }

    private void solve() {
        graph.solve(painter,getSize());
    }

    void shake() {
        graph.scramble(getSize());
        repaint();
    }

    void setKnots(boolean knots) {
        graph = switcher.setKnots(knots);
        this.knots = knots;
        createPainter();
        repaint();
    }

    void setGraphs(GraphMap graphMap) {
        switcher = new GraphSwitcher(graphMap);
        graph = graphMap.source;
    }

    void setFilter(GraphFilter filter) {
        this.filter = filter;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        painter.update(g,mouse.over,mouse.pick,filter,xray);
    }

    void createPainter() {
        painter = new GraphPainter(graph,this);
    }

    public void start() {
        println("Start");
        createPainter();
        new Timer(25, evt -> advance()).start();
    }

    private long last;
    private void checkSpeed() {
        long now = System.currentTimeMillis();
        long duration = now - last;
        if (duration > 200) {
            println(duration + "ms");
        }
        last = now;
    }

    private void advance() {
        checkSpeed();
        if (relax)           { relax(); }
        if (solve)           { solve(); }
        if (shouldRepaint()) { repaint();}
    }

    private boolean shouldRepaint() {
        return relax || solve || dirty();
    }

    private boolean dirty() {
        return dirtyTracker.dirty(mouse.pick,filter,xray,knots);
    }

    private static void println(String s) {
        System.out.println(s);
    }

}

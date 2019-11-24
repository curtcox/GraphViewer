import javax.swing.*;
import java.awt.*;

final class GraphPanel extends JPanel {

    boolean relax;
    boolean solve;
    boolean xray;
    boolean knots;
    private Graph graph;
    private GraphPainter painter;
    private GraphSwitcher switcher;
    private GraphMouseAdapter mouse;

    private GraphPanel() {}

    static GraphPanel newInstance() {
        var panel = new GraphPanel();
        panel.init();
        return panel;
    }

    private void init() {
        mouse = new GraphMouseAdapter(this);
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    GNode findNearestNode(int x, int y) { return graph.findNearestNode(x,y); }

    private void relax() {
        graph.relax(getSize());
    }

    private void solve() {
        graph.solve(painter,getSize());
    }

    void scramble() {
        graph.shake();
        repaint();
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

    @Override
    public void paint(Graphics g) {
        painter.update(g,mouse.pick,xray);
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
    private void advance() {
        long now = System.currentTimeMillis();
        long duration = now - last;
        if (duration > 200) {
            println(duration + "ms");
        }
        last = now;
        if (relax) { relax(); }
        if (solve) { solve(); }
        if (relax || solve || dirty(mouse.pick,xray,knots)) {
            repaint();
        }
    }

    private static void println(String s) {
        System.out.println(s);
    }

    private GNode lastPick = GNode.of("");
    private boolean lastXray;
    private boolean lastKnots;
    private boolean dirty(GNode pick,boolean xray,boolean knots) {
        boolean clean = pick   == lastPick &&
                        xray   == lastXray &&
                        knots  == lastKnots;
        lastPick   = pick;
        lastXray   = xray;
        lastKnots  = knots;
        return !clean;
    }

}

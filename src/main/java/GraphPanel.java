import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class GraphPanel extends JPanel {

    boolean stress;
    boolean relax;
    boolean solve;
    boolean xray;
    boolean knots;
    private int numMouseButtonsDown;
    private GNode pick;
    private boolean pickfixed;
    private GraphPainter painter;
    private Graph graph;
    private GraphSwitcher switcher;

    class GraphMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            numMouseButtonsDown++;
            int x = e.getX();
            int y = e.getY();
            GNode nearest = graph.findNearestNode(x,y);
            dragNode(nearest,x,y);
            if (rightButton(e)) {
                System.out.println(nearest);
            }
            finish(e);
        }

        boolean rightButton(MouseEvent e) {
            return e.getButton() == MouseEvent.BUTTON3;
        }

        void dragNode(GNode node, int x, int y) {
            pick = node;
            pickfixed = pick.fixed;
            pick.fixed = true;
            pick.setXY(x,y);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            numMouseButtonsDown--;

            pick.fixed = pickfixed;
            pick.setXY(e.getX(),e.getY());
            if (numMouseButtonsDown == 0) {
                pick = null;
            }
            finish(e);
        }

        void finish(MouseEvent e) {
            repaint();
            e.consume();
        }
    }

    class GraphMouseMotionListener extends MouseAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            pick.setXY(e.getX(),e.getY());
            repaint();
            e.consume();
        }
    }

    GraphPanel() {
        addMouseListener(new GraphMouseAdapter());
        addMouseMotionListener(new GraphMouseMotionListener());
    }

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

    void setGraphs(Graph nodesGraph, Graph knotsGraph) {
        switcher = new GraphSwitcher(nodesGraph,knotsGraph);
        graph = nodesGraph;
    }

    @Override
    public void paint(Graphics g) {
        painter.update(g,pick,stress,xray);
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
        if (relax || solve || dirty(pick,stress,xray,knots)) {
            repaint();
        }
    }

    private static void println(String s) {
        System.out.println(s);
    }

    private GNode lastPick = GNode.of("");
    private boolean lastStress;
    private boolean lastXray;
    private boolean lastKnots;
    private boolean dirty(GNode pick,boolean stress,boolean xray,boolean knots) {
        boolean clean = pick   == lastPick &&
                        stress == lastStress &&
                        xray   == lastXray &&
                        knots  == lastKnots;
        lastPick   = pick;
        lastStress = stress;
        lastXray   = xray;
        lastKnots  = knots;
        return !clean;
    }

}

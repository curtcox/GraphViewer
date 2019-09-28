import java.awt.*;
import java.awt.event.*;

class GraphPanel extends Panel {

    Graph graph;
    boolean stress;
    boolean relax;
    private Thread relaxer;
    private int numMouseButtonsDown;
    private GNode pick;
    private boolean pickfixed;
    private GraphPainter painter;

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
            pick = node.atFixed(x,y);
            pickfixed = pick.fixed;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            numMouseButtonsDown--;

            pick = pick.at(e.getX(),e.getY(),pickfixed);
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
            pick = pick.at(e.getX(),e.getY(),pick.fixed);
            repaint();
            e.consume();
        }
    }

    GraphPanel() {
        addMouseListener(new GraphMouseAdapter());
        addMouseMotionListener(new GraphMouseMotionListener());
    }

    synchronized void relax() {
        graph = graph.relax();
    }

    void scramble() {
        graph = graph.shake();
    }

    void shake() {
        graph = graph.scramble();
    }

    @Override
    public synchronized void update(Graphics g) {
        painter.update(g,graph,pick,stress);
    }

    public void start() {
        painter = new GraphPainter(this);
        relaxer = new RelaxerThread(this);
        relaxer.start();
    }

    void advance() {
        if (relax) {
            relax();
        }
        repaint();
    }
}

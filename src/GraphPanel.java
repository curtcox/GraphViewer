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
            pick = node;
            pickfixed = pick.fixed;
            pick.fixed = true;
            pick.x = x;
            pick.y = y;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            numMouseButtonsDown--;

            pick.fixed = pickfixed;
            pick.x = e.getX();
            pick.y = e.getY();
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
            pick.x = e.getX();
            pick.y = e.getY();
            repaint();
            e.consume();
        }
    }

    GraphPanel() {
        addMouseListener(new GraphMouseAdapter());
        addMouseMotionListener(new GraphMouseMotionListener());
    }

    synchronized void relax() {
        graph.relax();
    }

    void scramble() {
        graph.shake();
    }

    void shake() {
        graph.scramble();
    }

    @Override
    public synchronized void update(Graphics g) {
        painter.update(g,pick,stress);
    }

    public void start() {
        painter = new GraphPainter(graph,this);
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

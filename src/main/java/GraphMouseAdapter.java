import java.awt.event.*;

final class GraphMouseAdapter extends MouseAdapter {

    GNode pick;
    private int numMouseButtonsDown;
    private GraphPanel panel;

    GraphMouseAdapter(GraphPanel panel) {
        this.panel = panel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        numMouseButtonsDown++;
        int x = e.getX();
        int y = e.getY();
        GNode nearest = panel.findNearestNode(x,y);
        dragNode(nearest,x,y);
        if (rightButton(e)) {
            println(nearest);
        }
        finish(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        numMouseButtonsDown--;

        pick.setXY(e.getX(),e.getY());
        if (numMouseButtonsDown == 0) {
            pick = null;
        }
        finish(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        pick.setXY(e.getX(),e.getY());
        finish(e);
    }

    private void finish(MouseEvent e) {
        panel.repaint();
        e.consume();
    }

    private boolean rightButton(MouseEvent e) {
        return e.getButton() == MouseEvent.BUTTON3;
    }

    private void dragNode(GNode node, int x, int y) {
        pick = node;
        pick.setXY(x,y);
    }

    private static void println(Object o) {
        System.out.println("" + o);
    }
}

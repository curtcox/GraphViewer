package com.curtcox.graphviewer;

import java.awt.event.*;

final class GraphMouseAdapter extends MouseAdapter {

    GNode pick;
    GNode over;
    private int numMouseButtonsDown;
    private GraphPanel panel;

    GraphMouseAdapter(GraphPanel panel) {
        this.panel = panel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        numMouseButtonsDown++;
        GNode nearest = panel.findNearestNode(XY(e));
        dragNode(nearest,XY(e));
        if (rightButton(e)) {
            println(nearest);
        }
        finish(e);
    }

    private static XY XY(MouseEvent e) {
        return new XY(e.getX(),e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        numMouseButtonsDown--;
        pick.setXY(XY(e));
        if (numMouseButtonsDown == 0) {
            pick = null;
        }
        finish(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        pick.setXY(XY(e));
        finish(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        over = panel.findNodeUnderMouse(XY(e));
        finish(e);
    }

    private void finish(MouseEvent e) {
        panel.repaint();
        e.consume();
    }

    private boolean rightButton(MouseEvent e) {
        return e.getButton() == MouseEvent.BUTTON3;
    }

    private void dragNode(GNode node, XY xy) {
        pick = node;
        pick.setXY(xy);
    }

    private static void println(Object o) {
        System.out.println("Mouse : " + o);
    }
}

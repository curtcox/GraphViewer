package com.curtcox.graphviewer;

import java.awt.*;
import java.util.*;
import java.util.List;

class GraphPainter {

    private GNode pick;
    private final Graph graph;
    private final GraphPanel panel;
    private final FontMetrics fm;
    private final int nodeHeight;
    private final int nodeAscent;
    private final int paddedHeight;

    GraphPainter(Graph graph, GraphPanel panel) {
        this.graph = graph;
        this.panel = panel;
        this.fm = getFontMetrics();
        this.nodeHeight = fm.getHeight();
        this.nodeAscent = fm.getAscent();
        this.paddedHeight = nodeHeight + 4;
    }

    private Image offscreen;
    private Graphics offGraphics;

    private void paintNode(GNode n,boolean xray) {
        if (!xray) {
            paintBackground(n);
        }
        paintLabel(n);
    }

    private void paintBackground(GNode n) {
        setColor(offGraphics,n);
        offGraphics.fillRect(x(n), y(n), paddedWidth(n), paddedHeight);
    }

    private void paintLabel(GNode n) {
        int cx = (int) n.x();
        int cy = (int) n.y();
        offGraphics.setColor(Color.black);
        offGraphics.drawRect(x(n), y(n), paddedWidth(n), paddedHeight);
        offGraphics.drawString(n.label, cx - width(n) / 2, cy - nodeHeight / 2 + nodeAscent);
    }

    int paddedWidth(GNode n) {
        return width(n) + 10;
    }

    int x(GNode n) {
        int cx = (int) n.x();
        return cx - paddedWidth(n) / 2;
    }

    int y(GNode n) {
        int cy = (int) n.y();
        return cy - paddedHeight / 2;
    }

    int width(GNode n) {
        return fm.stringWidth(n.label);
    }

    private FontMetrics getFontMetrics() {
        createOffscreenGraphics();
        return offGraphics.getFontMetrics();
    }

    private void drawStats() {
        String s = stats();
        Dimension d = size();
        int x = d.width  - fm.stringWidth(s);
        int y = d.height - fm.getHeight();
        offGraphics.drawString(s, x, y);
    }

    private String stats() {
        return graph.nodeCount()  + " / " +
            graph.edgeCount()     + " / " +
            graph.crossingCount() + " / " +
            overlapCount();
    }

    private static final Color nodeColor = new Color(250, 220, 100);
    private static final Color cycleColor = new Color(200, 200, 200);
    private void setColor(Graphics g, GNode n) {
        var  fixedColor = Color.red;
        var selectColor = Color.pink;
        var color = n.fixed ? fixedColor : nodeColor;
        if (n==pick) {
            color = selectColor;
        }
        if (n.isInCycle) {
            color = cycleColor;
        }
        g.setColor(color);
    }

    void update(Graphics g,GNode pick,boolean stress,boolean xray) {
        this.pick = pick;
        updateOffscreenGraphics();
        drawEdges(stress);
        drawNodes(xray);
        drawStats();
        g.drawImage(offscreen, 0, 0, null);
    }

    private void updateOffscreenGraphics() {
        createOffscreenGraphics();
        fillBackground();
    }

    private void fillBackground() {
        offGraphics.setColor(panel.getBackground());
        Dimension d = size();
        offGraphics.fillRect(0, 0, d.width, d.height);
    }

    private Dimension size() { return panel.getSize(); }

    private void createOffscreenGraphics() {
        Dimension d = size();
        offscreen = panel.createImage(d.width, d.height);
        if (offGraphics != null) {
            offGraphics.dispose();
        }
        offGraphics = offscreen.getGraphics();
        offGraphics.setFont(panel.getFont());
    }

    private void drawNodes(boolean xray) {
        for (GNode n : graph.nodes()) {
            paintNode(n,xray);
        }
    }

    int overlapCount() {
        List<Rectangle> rects = new ArrayList<>();
        for (GNode n : graph.nodes()) {
            rects.add(new Rectangle(x(n),y(n),paddedWidth(n),paddedHeight));
        }
        return countOverlaps(rects);
    }

    private int countOverlaps(List<Rectangle> rects) {
        int count = 0;
        for (Rectangle r1 : rects) {
            for (Rectangle r2 : rects) {
                if (r1 != r2 && r1.intersects(r2)) {
                    count++;
                }
            }
        }
        return count;
    }

    private void drawEdges(boolean stress) {
        for (GEdge e : graph.edges()) {
            drawEdge(e,stress);
        }
    }

    private void drawEdge(GEdge e, boolean stress) {
        int x1 = (int) e.from.x();
        int y1 = (int) e.from.y();
        int x2 = (int) e.to.x();
        int y2 = (int) e.to.y();
        int len = e.len();
        drawEdgeLine(x1,y1,x2,y2,len);
        if (stress) {
            labelEdgeStress(x1,y1,x2,y2,len);
        }
    }

    private void drawEdgeLine(int x1, int y1, int x2, int y2, int len) {
        offGraphics.setColor((len < 10)
                ? Color.black
                : (len < 20 ? Color.pink : Color.red));
        offGraphics.drawLine(x1, y1, x2, y2);
    }

    private void labelEdgeStress(int x1, int y1, int x2, int y2, int len) {
        String lbl = String.valueOf(len);
        offGraphics.setColor(Color.darkGray);
        offGraphics.drawString(lbl, x1 + (x2 - x1) / 2, y1 + (y2 - y1) / 2);
    }

}

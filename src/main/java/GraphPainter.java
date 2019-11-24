import java.awt.*;
import java.util.*;
import java.util.List;

final class GraphPainter {

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

    private int paddedWidth(GNode n) {
        return width(n) + 10;
    }

    private int x(GNode n) {
        int cx = (int) n.x();
        return cx - paddedWidth(n) / 2;
    }

    private int y(GNode n) {
        int cy = (int) n.y();
        return cy - paddedHeight / 2;
    }

    private int width(GNode n) {
        return fm.stringWidth(n.label);
    }

    private FontMetrics getFontMetrics() {
        createOffscreenGraphics();
        return offGraphics.getFontMetrics();
    }

    private void drawStats() {
        var s = stats();
        var d = size();
        int x = d.width  - fm.stringWidth(s);
        int y = d.height - fm.getHeight();
        offGraphics.drawString(s, x, y);
    }

    private String stats() {

        return graph.knotCount()  + " / " +
            graph.nodeCount()     + " / " +
            graph.edgeCount()     + " / " +
            graph.crossingCount() + " / " +
            overlapCount();
    }

    private static final Color color(Knot knot) {
        return Colors.unique(knot.number);
    }

    private void setColor(Graphics g, GNode n) {
        g.setColor(nodeColor(n));
    }

    private void setSelection(GNode pick) {
        this.pick = pick;
    }

    boolean isSelected(GNode n) { return n == pick; }
    boolean isInSelectedKnot(GNode n) {
        return pick != null && pick.knot.contains(n);
    }

    private Color nodeColor(GNode n) {
        if (isSelected(n))        { return Colors.selectedNode;  }
        if (isInSelectedKnot(n))  { return Colors.selectedKnot; }
        if (n.isInKnotWithMultipleNodes()) { return color(n.knot); }
        return Colors.ordinary;
    }

    void update(Graphics g,GNode pick,boolean xray) {
        setSelection(pick);
        updateOffscreenGraphics();
        drawEdges();
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
        var rects = new ArrayList<Rectangle>();
        for (var n : graph.nodes()) {
            rects.add(new Rectangle(x(n),y(n),paddedWidth(n),paddedHeight));
        }
        return countOverlaps(rects);
    }

    private int countOverlaps(List<Rectangle> rects) {
        int count = 0;
        for (var r1 : rects) {
            for (var r2 : rects) {
                if (r1 != r2 && r1.intersects(r2)) {
                    count++;
                }
            }
        }
        return count;
    }

    private void drawEdges() {
        for (var e : graph.edges()) {
            drawEdge(e);
        }
    }

    private boolean isSelected(GEdge e) {
        return e.from == pick || e.to == pick;
    }

    private void drawEdge(GEdge e) {
        int x1 = (int) e.from.x();
        int y1 = (int) e.from.y();
        int x2 = (int) e.to.x();
        int y2 = (int) e.to.y();
        setLineColor(e);
        drawEdgeLine(x1,y1,x2,y2);
    }

    private void setLineColor(GEdge e) {
        offGraphics.setColor(color(e));
    }

    private Color color(GEdge e) {
        if (pick==null) {
            return Colors.line;
        }
        if (isSelected(e)) {
            return isGoingInto(e,pick) ? Colors.incomingLine : Colors.outgoingLine;
        }
        return Colors.unselectedLine;
    }

    private boolean isGoingInto(GEdge e, GNode pick) {
        return e.to == pick;
    }

    private void drawEdgeLine(int x1, int y1, int x2, int y2) {
        offGraphics.drawLine(x1, y1, x2, y2);
    }

}

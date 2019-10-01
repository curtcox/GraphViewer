import java.awt.*;
import java.util.*;
import java.util.List;

class GraphPainter {

    private GNode pick;
    private int overlapCount;
    private final Graph graph;
    private final GraphPanel panel;

    GraphPainter(Graph graph, GraphPanel panel) {
        this.graph = graph;
        this.panel = panel;
    }

    private Image offscreen;
    private Dimension offscreenSize;
    private Graphics offGraphics;

    private Rectangle paintNode(GNode n,boolean xray) {
        FontMetrics fm = getFontMetrics();
        int w = fm.stringWidth(n.label);
        int h = fm.getHeight();
        int pw = w + 10;
        int ph = h + 4;
        int cx = (int) n.x();
        int cy = (int) n.y();
        int x = cx - pw / 2;
        int y = cy - ph / 2;
        if (!xray) {
            setColor(offGraphics,n);
            offGraphics.fillRect(x, y, pw, ph);
        }
        offGraphics.setColor(Color.black);
        offGraphics.drawRect(x, y, pw, ph);
        offGraphics.drawString(n.label, cx - w / 2, (cy - h / 2) + fm.getAscent());
        return new Rectangle(x,y,pw,ph);
    }

    private FontMetrics getFontMetrics() {
        return offGraphics.getFontMetrics();
    }

    private void drawStats() {
        String s = stats();
        FontMetrics fm = getFontMetrics();
        Dimension d = size();
        int x = d.width  - fm.stringWidth(s);
        int y = d.height - fm.getHeight();
        offGraphics.drawString(s, x, y);
    }

    private String stats() {
        return graph.nodeCount()  + " / " +
            graph.edgeCount()     + " / " +
            graph.crossingCount() + " / " +
            overlapCount;
    }

    private static final Color nodeColor = new Color(250, 220, 100);
    private void setColor(Graphics g, GNode n) {
        final Color  fixedColor = Color.red;
        final Color selectColor = Color.pink;
        g.setColor((n == pick) ? selectColor : (n.fixed ? fixedColor : nodeColor));
    }

    void update(Graphics g,GNode pick,boolean stress,boolean xray) {
        this.pick = pick;
        overlapCount = 0;
        updateOffscreenGraphics();
        drawEdges(stress);
        drawNodes(xray);
        drawStats();
        g.drawImage(offscreen, 0, 0, null);
    }

    private void updateOffscreenGraphics() {
        if (!offscreenGraphicsReady()) {
            createOffscreenGraphics();
        }

        fillBackground();
    }

    private void fillBackground() {
        offGraphics.setColor(panel.getBackground());
        Dimension d = size();
        offGraphics.fillRect(0, 0, d.width, d.height);
    }

    private Dimension size() {
        return panel.getSize();
    }

    private boolean offscreenGraphicsReady() {
        Dimension d = size();
        return offscreen != null &&
            d.width  == offscreenSize.width &&
            d.height == offscreenSize.height;
    }

    private void createOffscreenGraphics() {
        Dimension d = size();
        offscreen = panel.createImage(d.width, d.height);
        offscreenSize = d;
        if (offGraphics != null) {
            offGraphics.dispose();
        }
        offGraphics = offscreen.getGraphics();
        offGraphics.setFont(panel.getFont());
    }

    private void drawNodes(boolean xray) {
        List<Rectangle> rects = new ArrayList();
        for (GNode n : graph.nodes()) {
            rects.add(paintNode(n,xray));
        }
        overlapCount = countOverlaps(rects);
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

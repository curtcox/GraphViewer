import java.awt.*;

class GraphPainter {

    private GNode pick;
    private final GraphPanel panel;

    GraphPainter(GraphPanel panel) {
        this.panel = panel;
    }

    private Image offscreen;
    private Dimension offscreensize;
    private Graphics offgraphics;

    private void paintNode(Graphics g, GNode n, FontMetrics fm) {
        int cx = (int) n.x;
        int cy = (int) n.y;
        setColor(g,n);
        int w = fm.stringWidth(n.label);
        int h = fm.getHeight();
        int pw = w + 10;
        int ph = h + 4;
        int x = cx - pw / 2;
        int y = cy - ph / 2;
        g.fillRect(x, y, pw, ph);
        g.setColor(Color.black);
        g.drawRect(x, y, pw, ph);
        g.drawString(n.label, cx - w / 2, (cy - h / 2) + fm.getAscent());
    }

    private static final Color nodeColor = new Color(250, 220, 100);
    private void setColor(Graphics g, GNode n) {
        final Color  fixedColor = Color.red;
        final Color selectColor = Color.pink;
        g.setColor((n == pick) ? selectColor : (n.fixed ? fixedColor : nodeColor));
    }

    void update(Graphics g,Graph graph, GNode pick,boolean stress) {
        this.pick = pick;
        updateOffscreenGraphics();
        drawEdges(graph,stress);
        drawNodes(graph);
        g.drawImage(offscreen, 0, 0, null);
    }

    private void updateOffscreenGraphics() {
        Dimension d = panel.getSize();
        if ((offscreen == null) || (d.width != offscreensize.width) || (d.height != offscreensize.height)) {
            offscreen = panel.createImage(d.width, d.height);
            offscreensize = d;
            if (offgraphics != null) {
                offgraphics.dispose();
            }
            offgraphics = offscreen.getGraphics();
            offgraphics.setFont(panel.getFont());
        }

        offgraphics.setColor(panel.getBackground());
        offgraphics.fillRect(0, 0, d.width, d.height);
    }

    private void drawNodes(Graph graph) {
        FontMetrics fm = offgraphics.getFontMetrics();
        for (GNode n : graph.nodes) {
            paintNode(offgraphics, n, fm);
        }
    }

    private void drawEdges(Graph graph, boolean stress) {
        for (GEdge e : graph.edges) {
            drawEdge(e,stress);
        }
    }

    private void drawEdge(GEdge e, boolean stress) {
        int x1 = (int) e.from.x;
        int y1 = (int) e.from.y;
        int x2 = (int) e.to.x;
        int y2 = (int) e.to.y;
        int len = e.len();
        drawEdgeLine(x1,y1,x2,y2,len);
        if (stress) {
            labelEdgeStress(x1,y1,x2,y2,len);
        }
    }

    private void drawEdgeLine(int x1, int y1, int x2, int y2, int len) {
        offgraphics.setColor((len < 10)
                ? Color.black
                : (len < 20 ? Color.pink : Color.red));
        offgraphics.drawLine(x1, y1, x2, y2);
    }

    private void labelEdgeStress(int x1, int y1, int x2, int y2, int len) {
        String lbl = String.valueOf(len);
        offgraphics.setColor(Color.darkGray);
        offgraphics.drawString(lbl, x1 + (x2 - x1) / 2, y1 + (y2 - y1) / 2);
    }

}

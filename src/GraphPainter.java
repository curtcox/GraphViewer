import java.awt.*;

class GraphPainter {

    private boolean stress;
    private GNode pick;
    private final Graph graph;
    private final GraphPanel panel;

    GraphPainter(Graph graph, GraphPanel panel) {
        this.graph = graph;
        this.panel = panel;
    }

    private Image offscreen;
    private Dimension offscreenSize;
    private Graphics offGraphics;

    private void paintNode(GNode n) {
        setColor(offGraphics,n);
        FontMetrics fm = getFontMetrics();
        int w = fm.stringWidth(n.label);
        int h = fm.getHeight();
        int pw = w + 10;
        int ph = h + 4;
        int cx = (int) n.x;
        int cy = (int) n.y;
        int x = cx - pw / 2;
        int y = cy - ph / 2;
        offGraphics.fillRect(x, y, pw, ph);
        offGraphics.setColor(Color.black);
        offGraphics.drawRect(x, y, pw, ph);
        offGraphics.drawString(n.label, cx - w / 2, (cy - h / 2) + fm.getAscent());
    }

    private FontMetrics getFontMetrics() {
        return offGraphics.getFontMetrics();
    }

    private void drawStats() {
        String s = graph.nodes().length + " / " + graph.edges().length;
        FontMetrics fm = getFontMetrics();
        Dimension d = size();
        int x = d.width  - fm.stringWidth(s);
        int y = d.height - fm.getHeight();
        offGraphics.drawString(s, x, y);
    }

    private static final Color nodeColor = new Color(250, 220, 100);
    private void setColor(Graphics g, GNode n) {
        final Color  fixedColor = Color.red;
        final Color selectColor = Color.pink;
        g.setColor((n == pick) ? selectColor : (n.fixed ? fixedColor : nodeColor));
    }

    void update(Graphics g,GNode pick,boolean stress) {
        this.pick = pick;
        this.stress = stress;
        updateOffscreenGraphics();
        drawEdges();
        drawNodes();
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

    private void drawNodes() {
        for (GNode n : graph.nodes()) {
            paintNode(n);
        }
    }

    private void drawEdges() {
        for (GEdge e : graph.edges()) {
            drawEdge(e);
        }
    }

    private void drawEdge(GEdge e) {
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

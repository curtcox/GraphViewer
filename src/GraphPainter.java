import java.awt.*;

import static java.lang.Math.*;

class GraphPainter {

    private boolean stress;
    private GNode pick;
    private final Graph graph;
    private final GraphPanel panel;

    private static final Color  fixedColor = Color.red;
    private static final Color selectColor = Color.pink;
    private static final Color   edgeColor = Color.black;
    private static final Color   nodeColor = new Color(250, 220, 100);
    private static final Color stressColor = Color.darkGray;
    private static final Color   arcColor1 = Color.black;
    private static final Color   arcColor2 = Color.pink;
    private static final Color   arcColor3 = Color.red;

    GraphPainter(Graph graph, GraphPanel panel) {
        this.graph = graph;
        this.panel = panel;
    }

    private Image offscreen;
    private Dimension offscreensize;
    private Graphics offgraphics;

    private void paintNode(Graphics g, GNode n, FontMetrics fm) {
        int x = (int) n.x;
        int y = (int) n.y;
        g.setColor((n == pick) ? selectColor : (n.fixed ? fixedColor : nodeColor));
        int w = fm.stringWidth(n.label) + 10;
        int h = fm.getHeight() + 4;
        g.fillRect(x - w / 2, y - h / 2, w, h);
        g.setColor(Color.black);
        g.drawRect(x - w / 2, y - h / 2, w - 1, h - 1);
        g.drawString(n.label, x - (w - 10) / 2, (y - (h - 4) / 2) + fm.getAscent());
    }

    void update(Graphics g,GNode pick,boolean stress) {
        this.pick = pick;
        this.stress = stress;
        updateOffscreenGraphics();
        drawEdges();
        drawNodes();
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

    private void drawNodes() {
        FontMetrics fm = offgraphics.getFontMetrics();
        for (GNode n : graph.nodes) {
            paintNode(offgraphics, n, fm);
        }
    }

    private void drawEdges() {
        for (GEdge e : graph.edges) {
            drawEdge(e);
        }
    }

    private void drawEdge(GEdge e) {
        int x1 = (int) e.from.x;
        int y1 = (int) e.from.y;
        int x2 = (int) e.to.x;
        int y2 = (int) e.to.y;
        int len = (int) abs(sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) - e.len);
        offgraphics.setColor((len < 10)
                ? arcColor1
                : (len < 20 ? arcColor2 : arcColor3));
        offgraphics.drawLine(x1, y1, x2, y2);
        if (stress) {
            String lbl = String.valueOf(len);
            offgraphics.setColor(stressColor);
            offgraphics.drawString(lbl, x1 + (x2 - x1) / 2, y1 + (y2 - y1)
                    / 2);
            offgraphics.setColor(edgeColor);
        }
    }

}

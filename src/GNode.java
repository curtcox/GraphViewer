import java.awt.*;

import static java.lang.Math.*;

class GNode {
    private XY xy;
    private double dx;
    private double dy;
    boolean fixed;
    final String label;

    GNode(String label) {
        this.label = label;
    }

    XY xy() { return xy; }
    double x() { return xy.x; }
    double y() { return xy.y; }
    void setXY(double x, double y) { this.xy = new XY(x,y); }
    void addDelta(double dx, double dy) {
        this.dx += dx;
        this.dy += dy;
    }

    void relax(Dimension d) {
        if (!fixed) {
            xy.add(new XY(bounded(dx),bounded(dy)));
        }
        setXY(boundBy(x(),d.width),boundBy(y(),d.height));
        dx /= 2;
        dy /= 2;
    }

    private static double bounded(double v) {
        return max(-5, min(5,v));
    }

    private static double boundBy(double v, double max) {
        if (v < 0) {
            v = 0;
        } else if (v > max) {
            v = max;
        }
        return v;
    }

    private void adjustDeltasConsidering(GNode[] nodes) {
        double dx = 0;
        double dy = 0;

        for (GNode n2 : nodes) {
            if (this == n2) {
                continue;
            }
            double vx = x() - n2.x();
            double vy = y() - n2.y();
            double len = vx * vx + vy * vy;
            if (len == 0) {
                dx += random();
                dy += random();
            } else if (len < 100 * 100) {
                dx += vx / len;
                dy += vy / len;
            }
        }
        double dlen = dx * dx + dy * dy;
        if (dlen > 0) {
            dlen = sqrt(dlen) / 2;
            addDelta(dx / dlen,dy / dlen);
        }
    }

    static void relaxNodes(GNode[] nodes) {
        for (GNode node : nodes) {
            node.adjustDeltasConsidering(nodes);
        }
    }

    private void scramble(Dimension d) {
        if (!fixed) {
            setXY(10 + (d.width - 20)  * random(),10 + (d.height - 20) * random());
        }
    }

    private void shake() {
        if (!fixed) {
            xy.add(new XY(80 * random() - 40,80 * random() - 40));
        }
    }

    static void scramble(GNode[] nodes,Dimension d) {
        for (GNode n : nodes) {
            n.scramble(d);
        }
    }

    static void shake(GNode[] nodes) {
        for (GNode n : nodes) {
            n.shake();
        }
    }

    double distanceTo(int x, int y) {
        return (this.x() - x) * (this.x() - x) + (this.y() - y) * (this.y() - y);
    }

    public String toString() {
        return label + " @ " + xy;
    }

}

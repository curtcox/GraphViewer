import java.awt.*;

import static java.lang.Math.*;

class GNode {
    XY xy;
    XY delta = new XY();
    boolean fixed;
    final String label;

    GNode(String label) {
        this.label = label;
    }

    double x() { return xy.x; }
    double y() { return xy.y; }
    void setXY(double x, double y) { this.xy = new XY(x,y); }

    void relax(Dimension d) {
        if (!fixed) {
            xy.add(new XY(bounded(delta.x),bounded(delta.y)));
        }
        setXY(boundBy(x(),d.width),boundBy(y(),d.height));
        delta.half();
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
        XY delta = computeDelta(nodes);
        double dlen = delta.dlen();
        if (dlen > 0) {
            dlen = sqrt(dlen) / 2;
            delta.divideBy(dlen);
            delta.add(delta);
        }
    }

    XY computeDelta(GNode[] nodes) {
        XY delta = new XY();

        for (GNode n2 : nodes) {
            if (this == n2) {
                continue;
            }
            XY vector = vectorTo(n2);
            double len = vector.dlen();
            if (len == 0) {
                delta.add(new XY(random(),random()));
            } else if (len < 100 * 100) {
                vector.divideBy(len);
                delta.add(vector);
            }
        }

        return delta;
    }

    XY vectorTo(GNode other) {
        return new XY(x() - other.x(),y() - other.y());
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

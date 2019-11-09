import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.*;

final class GNode {
    XY xy;
    XY delta = new XY();
    boolean fixed;
    Set<Cycle> cycles = new HashSet<>();
    final String label;

    GNode(String label) {
        this.label = label;
    }

    double x() { return xy.x; }
    double y() { return xy.y; }
    void setXY(double x, double y) { this.xy = new XY(x,y); }

    boolean isInCycle() {
        return !cycles.isEmpty();
    }

    void moveRestrictedTo(Dimension d) {
        if (!fixed) {
            move();
        }
        restrictTo(d);
    }

    private void move() {
        xy = xy.plus(new XY(bounded(delta.x),bounded(delta.y)));
        delta = delta.half();
    }

    private void restrictTo(Dimension d) {
        setXY(boundBy(x(),d.width),boundBy(y(),d.height));
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

    private void scramble(Dimension d) {
        if (!fixed) {
            setXY(10 + (d.width - 20)  * random(),10 + (d.height - 20) * random());
        }
    }

    private void shake() {
        if (!fixed) {
            xy = xy.plus(new XY(80 * random() - 40,80 * random() - 40));
        }
    }

    private double squared(double x) {
        return x * x;
    }

    static void scramble(GNode[] nodes,Dimension d) {
        for (var n : nodes) {
            n.scramble(d);
        }
    }

    static void shake(GNode[] nodes) {
        for (var n : nodes) {
            n.shake();
        }
    }

    double distanceTo(XY xy) {
        return squared(x() - xy.x) + squared(y() - xy.y);
    }

    static void repelOtherNodes(GNode[] nodes) {
        for (var n1 : nodes) {
            for (var n2 : nodes) {
                if (n1!=n2) {
                    repelNodes(n1,n2);
                }
            }
        }
    }

    private static void repelNodes(GNode n1, GNode n2) {
        var edge = new GEdge(n1,n2,1.0);
        edge.repel();
    }

    public String toString() {
        return label;// + " @ " + xy;
    }

    public int hashCode() { return label.hashCode(); }
    public boolean equals(Object o) {
        GNode that = (GNode) o;
        return label.equals(that.label);
    }
}

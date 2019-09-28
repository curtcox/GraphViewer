import java.awt.*;

import static java.lang.Math.*;

class GNode {
    final double x;
    final double y;
    final double dx;
    final double dy;
    final boolean fixed;
    final String label;

    GNode(String label,double x, double y, double dx, double dy,boolean fixed) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.fixed = fixed;
    }

    GNode withDv(double dx, double dy) {
        return new GNode(label,x,y,this.dx+dx,this.dy+dy,fixed);
    }

    GNode relax(Dimension d) {
        double nx = fixed ? x : x + max(-5, min(5, dx));
        double ny = fixed ? y : y + max(-5, min(5, dy));
        nx = boundBy(nx,d.width);
        ny = boundBy(ny,d.height);
        return new GNode(label,nx,ny,dx/2,dy/2,fixed);
    }

    GNode atFixed(double x, double y) {
        return new GNode(label,x,y,dx,dy,true);
    }

    GNode at(double x, double y,boolean fixed) {
        return new GNode(label,x,y,dx,dy,fixed);
    }

    private static double boundBy(double v, double max) {
        if (v < 0) {
            v = 0;
        } else if (v > max) {
            v = max;
        }
        return v;
    }

    static GNode[] relaxNodes(GNode[] nodes) {
        GNode[] relaxed = new GNode[nodes.length];
        for (int i=0; i<relaxed.length; i++) {
            relaxed[i] = nodes[i].relaxWithRespectTo(nodes);
        }
        return relaxed;
    }

    GNode relaxWithRespectTo(GNode[] nodes) {
        double dx = 0;
        double dy = 0;

        for (GNode n2 : nodes) {
            if (this.label.equals(n2.label)) {
                continue;
            }
            double vx = x - n2.x;
            double vy = y - n2.y;
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
            dx += dx / dlen;
            dy += dy / dlen;
        }
        return new GNode(label,x,y,dx,dy,fixed);
    }

    private GNode scramble(Dimension d) {
        return fixed ? this
            : new GNode(label,
                10 + (d.width - 20)  * random(),
                10 + (d.height - 20) * random(),
                dx,dy,fixed);
    }

    private GNode shake() {
        return fixed ? this
            : new GNode(label,
                x + 80 * random() - 40,
                y + 80 * random() - 40,
                dx,dy,fixed);
    }

    static GNode[] scramble(GNode[] nodes,Dimension d) {
        GNode[] scrambled = new GNode[nodes.length];
        for (int i=0; i<nodes.length; i++) {
            scrambled[i] = nodes[i].scramble(d);
        }
        return scrambled;
    }

    static GNode[] shake(GNode[] nodes) {
        GNode[] shaken = new GNode[nodes.length];
        for (int i=0; i<nodes.length; i++) {
            shaken[i] = nodes[i].shake();
        }
        return shaken;
    }

    double distanceTo(int x, int y) {
        return (this.x - x) * (this.x - x) + (this.y - y) * (this.y - y);
    }

    public String toString() {
        return label + " @ " + x + "," + y;
    }
}

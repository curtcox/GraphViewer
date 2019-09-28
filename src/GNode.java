import java.awt.*;

import static java.lang.Math.*;

class GNode {
    double x;
    double y;
    double dx;
    double dy;
    boolean fixed;
    final String label;

    GNode(String label) {
        this.label = label;
    }

    void relax(Dimension d) {
        if (!fixed) {
            x += max(-5, min(5, dx));
            y += max(-5, min(5, dy));
        }
        x = boundBy(x,d.width);
        y = boundBy(y,d.height);
        dx /= 2;
        dy /= 2;
    }

    private static double boundBy(double v, double max) {
        if (v < 0) {
            v = 0;
        } else if (v > max) {
            v = max;
        }
        return v;
    }

    static void relaxNodes(GNode[] nodes) {
        for (GNode n1 : nodes) {
            double dx = 0;
            double dy = 0;

            for (GNode n2 : nodes) {
                if (n1 == n2) {
                    continue;
                }
                double vx = n1.x - n2.x;
                double vy = n1.y - n2.y;
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
                n1.dx += dx / dlen;
                n1.dy += dy / dlen;
            }
        }
    }

    private void scramble(Dimension d) {
        if (!fixed) {
            x = 10 + (d.width - 20)  * random();
            y = 10 + (d.height - 20) * random();
        }
    }

    private void shake() {
        if (!fixed) {
            x += 80 * random() - 40;
            y += 80 * random() - 40;
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
        return (this.x - x) * (this.x - x) + (this.y - y) * (this.y - y);
    }

    public String toString() {
        return label + " @ " + x + "," + y;
    }
}

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import static java.awt.geom.Line2D.linesIntersect;

class GEdge {

    final GNode from;
    final GNode to;
    final double len;

    GEdge(GNode from, GNode to, double len) {
        this.from = from;
        this.to = to;
        this.len = len;
    }

    boolean crosses(GEdge that) {
        double x1 = shorten(from.x,to.x);
        double y1 = shorten(from.y,to.y);
        double x2 = shorten(to.x,from.x);
        double y2 = shorten(to.y,from.y);
        double x3 = shorten(that.from.x,that.to.x);
        double y3 = shorten(that.from.y,that.to.y);
        double x4 = shorten(that.to.x,that.from.x);
        double y4 = shorten(that.to.y,that.from.y);
        return linesIntersect(x1,y1,x2,y2,x3,y3,x4,y4);
    }

    private static double shorten(double from, double to) {
        double weight = 100;
        return (weight * from + to) / (weight + 1.0d);
    }

    void relax() {
        double vx = vx();
        double vy = vy();
        double f = f();
        double dx = f * vx;
        double dy = f * vy;

        to.dx += dx;
        to.dy += dy;
        from.dx += -dx;
        from.dy += -dy;
    }

    private double f() {
        double vx = vx();
        double vy = vy();
        double len = sqrt(vx * vx + vy * vy);
        len = (len == 0) ? .0001 : len;
        return (this.len - len) / (len * 3);
    }

    private double vx() { return to.x - from.x; }
    private double vy() { return to.y - from.y; }

    int len() {
        int x1 = (int) from.x;
        int y1 = (int) from.y;
        int x2 = (int) to.x;
        int y2 = (int) to.y;
        return (int) abs(sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) - len);
    }


    public String toString() {
        return from.label + " / " + to.label;
    }

}

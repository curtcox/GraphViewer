import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

class GEdge {

    final GNode from;
    final GNode to;
    final double len;

    GEdge(GNode from, GNode to, double len) {
        this.from = from;
        this.to = to;
        this.len = len;
    }

    void relax() {
        double vx = to.x - from.x;
        double vy = to.y - from.y;
        double len = sqrt(vx * vx + vy * vy);
        len = (len == 0) ? .0001 : len;
        double f = (this.len - len) / (len * 3);
        double dx = f * vx;
        double dy = f * vy;

        to.dx += dx;
        to.dy += dy;
        from.dx += -dx;
        from.dy += -dy;
    }

    int len() {
        int x1 = (int) from.x;
        int y1 = (int) from.y;
        int x2 = (int) to.x;
        int y2 = (int) to.y;
        return (int) abs(sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) - len);
    }


    public String toString() {
        return from + " / " + to + " / " + len;
    }
}

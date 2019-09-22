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


    public String toString() {
        return from + " / " + to + " / " + len;
    }
}

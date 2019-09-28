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

    GEdge relax() {
        double vx = to.x - from.x;
        double vy = to.y - from.y;
        double len = sqrt(vx * vx + vy * vy);
        len = (len == 0) ? .0001 : len;
        double f = f(len);
        double dx = f * vx;
        double dy = f * vy;

        return new GEdge(to.withDv(dx,dy),from.withDv(-dx,-dy),len);
    }

    private double f(double len) {
        return (this.len - len) / (len * 3);
    }

    static GEdge[] relax(GEdge[] edges) {
        GEdge[] relaxed = new GEdge[edges.length];
        for (int i=0; i<relaxed.length; i++) {
            relaxed[i] = edges[i].relax();
        }
        return relaxed;
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

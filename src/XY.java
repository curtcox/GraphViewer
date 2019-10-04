class XY {

    double x;
    double y;

    XY(){}

    XY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    void add(XY xy) {
        this.x += xy.x;
        this.y += xy.y;
    }

    void subtract(XY xy) {
        this.x -= xy.x;
        this.y -= xy.y;
    }

    @Override
    public String toString() {
        return x + "," + y;
    }

    void half() {
        divideBy(2);
    }

    void divideBy(double v) {
        x /= v;
        y /= v;
    }

    double dlen() {
        return x * x + y * y;
    }
}

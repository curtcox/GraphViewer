class XY {

    double x;
    double y;

    XY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    void add(XY xy) {
        this.x += xy.x;
        this.y += xy.y;
    }

    @Override
    public String toString() {
        return x + "," + y;
    }

}

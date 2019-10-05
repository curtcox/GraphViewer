class XY {

    final double x;
    final double y;

    XY(){
        this(0,0);
    }

    XY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    XY add(XY xy) {
        return new XY(x + xy.x, y + xy.y);
    }

    XY subtract(XY xy) {
        return new XY(x - xy.x, y - xy.y);
    }

    @Override
    public String toString() {
        return x + "," + y;
    }

    XY half() {
        return divideBy(2);
    }

    XY divideBy(double v) {
        return new XY(x / v, y /v);
    }

    XY multiplyBy(double v) {
        return new XY(x * v, y  * v);
    }

    double dlen() {
        return x * x + y * y;
    }
}

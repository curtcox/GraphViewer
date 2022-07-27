package com.curtcox.graphviewer;

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

    XY plus(XY xy) {
        return new XY(x + xy.x, y + xy.y);
    }

    XY minus(XY xy) {
        return new XY(x - xy.x, y - xy.y);
    }

    XY times(double v) {
        return new XY(x * v, y * v);
    }

    @Override
    public String toString() {
        return x + "," + y;
    }

    XY half() {
        return dividedBy(2);
    }

    private XY dividedBy(double v) {
        return new XY(x / v, y /v);
    }

}

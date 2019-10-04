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

    @Override
    public String toString() {
        return x + "," + y;
    }

    void half() {
        x /= 2;
        y /= 2;
    }
}

package com.curtcox.graphviewer;

import static java.lang.Math.*;
import static java.awt.geom.Line2D.linesIntersect;

final class GEdge {

    final GNode from;
    final GNode to;
    private final double desiredLength;

    GEdge(GNode from, GNode to, double desiredLength) {
        this.from = from;
        this.to = to;
        this.desiredLength = desiredLength;
    }

    boolean crosses(GEdge that) {
        double x1 = betweenFirstAndSecondButMostlyFirst(from.x(),to.x());
        double y1 = betweenFirstAndSecondButMostlyFirst(from.y(),to.y());
        double x2 = betweenFirstAndSecondButMostlyFirst(to.x(),from.x());
        double y2 = betweenFirstAndSecondButMostlyFirst(to.y(),from.y());
        double x3 = betweenFirstAndSecondButMostlyFirst(that.from.x(),that.to.x());
        double y3 = betweenFirstAndSecondButMostlyFirst(that.from.y(),that.to.y());
        double x4 = betweenFirstAndSecondButMostlyFirst(that.to.x(),that.from.x());
        double y4 = betweenFirstAndSecondButMostlyFirst(that.to.y(),that.from.y());
        return linesIntersect(x1,y1,x2,y2,x3,y3,x4,y4);
    }

    private static double betweenFirstAndSecondButMostlyFirst(double from, double to) {
        double weight = 100;
        return (weight * from + to) / (weight + 1.0d);
    }

    void contract() {
        XY delta   = delta();
        to.delta   = to.delta.plus(delta);
        from.delta = from.delta.minus(delta);
    }

    void repel() {
        XY delta   = repelDelta();
        to.delta   = to.delta.plus(delta);
        from.delta = from.delta.minus(delta);
    }

    private XY delta() {
        return direction().times(f());
    }

    private XY repelDelta() {
        return direction().times(repelMultiplier());
    }

    private XY direction() {
        return new XY(dx(),dy());
    }

    private double f() {
        double actualLength = actualLength();
        return (this.desiredLength - actualLength) / (actualLength * 3);
    }

    private double repelMultiplier() {
        double actualLength = actualLength();
        return actualLength == 0 ? 0 : 1.0 / actualLength;
    }

    private double dx() { return to.x() - from.x(); }
    private double dy() { return to.y() - from.y(); }

    private double actualLength() {
        double dx = dx();
        double dy = dy();
        double actualLength = sqrt(dx * dx + dy * dy);
        return (actualLength == 0) ? .0001 : actualLength;
    }

    public String toString() {
        return from.label + " > " + to.label;
    }

}

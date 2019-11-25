final class DirtyTracker {

    private GNode lastPick = GNode.of("");
    private boolean lastXray;
    private boolean lastKnots;
    private GraphFilter lastFilter;

    boolean dirty(GNode pick, GraphFilter filter, boolean xray,boolean knots) {
        boolean clean =
                pick   == lastPick &&
                filter == lastFilter &&
                xray   == lastXray &&
                knots  == lastKnots;
        lastPick   = pick;
        lastFilter = filter;
        lastXray   = xray;
        lastKnots  = knots;
        return !clean;
    }
}

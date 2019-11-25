final class GraphFilter {

    private final boolean empty;
    private final String trimmed;
    private final String[] parts;

    GraphFilter(String text) {
        trimmed = text.trim();
        empty = trimmed.isEmpty();
        parts = trimmed.split(" ");
    }

    boolean passes(GNode node) {
        if (empty) {
            return true;
        }
        var label = node.label;
        for (var part : parts) {
            if (label.contains(part)) {
                return false;
            }
        }
        return true;
    }

    boolean passes(GEdge e) {
        return passes(e.to) && passes(e.from);
    }
}

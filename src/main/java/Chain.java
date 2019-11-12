import java.util.*;

final class Chain {

    final List<GNode> path;
    static final Chain EMPTY = new Chain(Collections.emptyList());
    private static final Map<List<GNode>,Chain> chains = new HashMap<>();

    private Chain(List<GNode> path) {
        this.path = Collections.unmodifiableList(path);
    }

    private static Chain of(List<GNode> path) {
        if (chains.containsKey(path)) {
            return chains.get(path);
        }
        var chain = new Chain(path);
        chains.put(path,chain);
        return chain;
    }

    Chain plus(GNode child) {
        var nodes = new ArrayList<>(path);
        nodes.add(child);
        return Chain.of(nodes);
    }

    GNode lastNode() {
        return path.get(path.size()-1);
    }

    boolean isCycle() {
        return path.size() > new HashSet<>(path).size();
    }

    boolean addingWouldMakeCompleteCycle(GNode node) {
        return !path.isEmpty() && path.get(0).equals(node);
    }

    boolean addingWouldMakeCycleWithTail(GNode node) {
        return path.size() > 1 && path.subList(1,path.size()).contains(node);
    }

    public String toString() {
        return "chain (" + path + ")";
    }

}

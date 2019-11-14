import java.util.*;

import static java.lang.Math.random;

class GraphReader {

    private final String[] edgeLines;
    private final String center;
    private GEdge[] edges = new GEdge[0];
    private GNode[] nodes = new GNode[0];

    GraphReader(String[] edgeLines,String center) {
        this.edgeLines = edgeLines;
        this.center = center;
    }

    Graph read() {
        addEdges();
        addCenter();
        return new Graph(edges,nodes);
    }

    private GNode findNodeFromLabel(String lbl) {
        for (GNode n : nodes) {
            if (n.label.equals(lbl)) {
                return n;
            }
        }
        return addNode(newNodeAtRandomLocation(lbl));
    }

    private GNode addNode(GNode n) {
        var old = nodes;
        nodes = new GNode[old.length + 1];
        append(old,nodes,n);
        return n;
    }

    private GNode newNodeAtRandomLocation(String lbl) {
        var n = GNode.of(lbl);
        n.setXY(10 + 380 * random(),10 + 380 * random());
        return n;
    }

    private void addEdge(String from, String to, int len) {
        var e = new GEdge(findNodeFromLabel(from),findNodeFromLabel(to),len);
        var old = edges;
        edges = new GEdge[old.length + 1];
        append(old,edges,e);
    }

    private static <T> void append(T[] old, T[] copy, T e) {
        for (int i=0; i<old.length; i++) {
            copy[i] = old[i];
        }
        copy[copy.length - 1] = e;
    }

    private void addEdges() {
        for (var line : edgeLines) {
            if (isValidEdgeFormat(line)) {
                addEdge(line);
            } else {
                System.out.println("Skipping invalid " + Arrays.asList(partsOf(line)));
            }
        }
    }

    private String[] partsOf(String line) {
        if (line.length()<1) {
            return new String[0];
        }
        String e = line.substring(0,1);
        String separator = "\\Q" + e + "\\E";
        String rest = line.substring(1);

        return rest.split(separator);
    }

    private boolean isValidEdgeFormat(String line) {
        String[] parts = partsOf(line);
        return parts.length == 2 || parts.length == 3;
    }

    private void addEdge(String line) {
        var parts = partsOf(line);
        int len = (parts.length == 3) ? Integer.parseInt(parts[2]) : 50;
        var from = parts[0];
        var   to = parts[1];
        addEdge(from, to, len);
    }

    private void addCenter() {
        if (center != null) {
            var n = findNodeFromLabel(center);
            n.setXY(500,500);
            n.fixed = true;
        }
    }

}

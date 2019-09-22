import java.awt.*;

import static java.lang.Math.random;

class GraphReader {

    private Dimension d;
    private final String[] edgeLines;
    private final String center;
    private GEdge[] edges = new GEdge[0];
    private GNode[] nodes = new GNode[0];

    GraphReader(String[] edgeLines,String center) {
        this.edgeLines = edgeLines;
        this.center = center;
    }

    Graph read(Dimension d) {
        this.d = d;
        addEdges();
        addCenter();
        return new Graph(edges,nodes,d);
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
        GNode[] old = nodes;
        nodes = new GNode[old.length + 1];
        append(old,nodes,n);
        return n;
    }

    private GNode newNodeAtRandomLocation(String lbl) {
        GNode n = new GNode(lbl);
        n.x = 10 + 380 * random();
        n.y = 10 + 380 * random();
        return n;
    }

    private void addEdge(String from, String to, int len) {
        GEdge e = new GEdge(findNodeFromLabel(from),findNodeFromLabel(to),len);
        GEdge[] old = edges;
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
        for (String str : edgeLines) {
            int i = str.indexOf('-');
            if (i > 0) {
                addEdge(str);
            }
        }
    }

    private void addEdge(String str) {
        int len = 50;
        int j = str.indexOf('/');
        if (j > 0) {
            len = Integer.valueOf(str.substring(j + 1)).intValue();
            str = str.substring(0, j);
        }
        int i = str.indexOf('-');
        addEdge(str.substring(0, i), str.substring(i + 1), len);
    }

    private void addCenter() {
        if (center != null) {
            GNode n = findNodeFromLabel(center);
            n.x = d.width  / 2;
            n.y = d.height / 2;
            n.fixed = true;
        }
    }

}

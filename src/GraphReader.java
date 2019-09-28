import java.awt.*;
import java.util.*;

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
        Graph graph = new Graph(edges,nodes,d);
        return graph;
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
        double x = 10 + 380 * random();
        double y = 10 + 380 * random();
        GNode n = new GNode(lbl,x,y,0,0,false);
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
        for (String line : edgeLines) {
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
        String[] parts = partsOf(line);
        int len = (parts.length == 3) ? Integer.valueOf(parts[2]).intValue() : 50;
        String from = parts[0];
        String   to = parts[1];
        addEdge(from, to, len);
    }

    private GNode addCenter() {
        if (center != null) {
            GNode n = findNodeFromLabel(center);
            return n.at(d.width / 2, d.height/2,true);
        }
        return null;
    }

}

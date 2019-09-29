import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Start {

    static class ExitOnClose extends WindowAdapter {
        public void windowClosing(WindowEvent e ) {
            System.exit( 0 );
        }
    }

    static GraphViewerPanel graph(String[] args)  throws IOException {
        GraphReader reader = (args.length < 1)
                ? defaultGraph()
                : graphFromArgs(args);
        return new GraphViewerPanel(reader);
    }

    static GraphReader defaultGraph() {
        return new GraphReader(new String[] {
                "|to read graph from standard in|specify a hyphen flag",
                "|specify a hyphen flag|usage",

                "|to see this graph|specify no flags",
                "|specify no flags|usage",

                "|to mark center|specify a string with no hyphen",
                "|specify a string with no hyphen|usage",

                "|to define an edge|separate node names with a hyphen",
                "|separate node names with a hyphen|usage"
        },
                "usage");
    }

    static GraphReader graphFromArgs(String[] args) throws IOException {
        String center = args.length > 0 ? args[0] : null;
        ArrayList<String> edges = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            for (String line = br.readLine();line!=null;line=br.readLine()) {       
                edges.add(line);
            }
        }
        return new GraphReader(edges.toArray(new String[0]),center);
    }

    public static void main(String[] args)  throws IOException {
        Frame frame = new Frame( "Graph" );
        GraphViewerPanel graph = graph(args);
        frame.addWindowListener( new ExitOnClose());
        frame.add(graph);
        frame.setSize( 1000, 1000 );
        graph.init();

        frame.setVisible(true);
        graph.start();
    }

}

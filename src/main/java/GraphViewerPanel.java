import javax.swing.*;
import java.awt.*;

class GraphViewerPanel extends JPanel {

    private final GraphPanel   panel = new GraphPanel();
    private final JPanel controlPanel = new JPanel();
    private final JButton    scramble = new JButton("Scramble");
    private final JButton       shake = new JButton("Shake");
    private final JCheckBox    stress = new JCheckBox("Stress");
    private final JCheckBox      xray = new JCheckBox("X-ray");
    private final JCheckBox     relax = new JCheckBox("Relax");
    private final JCheckBox     solve = new JCheckBox("Solve");
    private final JCheckBox     knots = new JCheckBox("Knots");
    private final GraphReader reader;

    GraphViewerPanel(GraphReader reader) {
        this.reader = reader;
    }

    void init() {
        setLayout(new BorderLayout());
        add("Center", panel);
        add("South", controlPanel);

        controlPanel.add(scramble);
        controlPanel.add(shake);
        controlPanel.add(stress);
        controlPanel.add(xray);
        controlPanel.add(relax);
        controlPanel.add(solve);
        controlPanel.add(knots);
        scramble.addActionListener(e -> scramble());
        shake.addActionListener(e -> shake());
        stress.addActionListener(e -> panel.stress = stress.isSelected());
        xray.addActionListener(e -> panel.xray = xray.isSelected());
        relax.addActionListener(e -> panel.relax = relax.isSelected());
        solve.addActionListener(e -> panel.solve = solve.isSelected());
        solve.addActionListener(e -> panel.setKnots(knots.isSelected()));
    }

    void start() {
        var graph = reader.read();
        panel.setGraphs(graph,graph);
        panel.start();
    }

    private void scramble() {
        panel.scramble();
    }

    private void shake() {
        panel.shake();
    }

}

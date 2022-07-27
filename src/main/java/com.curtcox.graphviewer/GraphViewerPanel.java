package com.curtcox.graphviewer;

import javax.swing.*;
import java.awt.*;

class GraphViewerPanel extends JPanel {

    private final GraphPanel    panel = GraphPanel.newInstance();
    private final JPanel controlPanel = new JPanel();
    private final FilterPanel  filter = new FilterPanel(panel);
    private final JButton       shake = new JButton("Shake");
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

        controlPanel.add(filter);
        controlPanel.add(shake);
        controlPanel.add(xray);
        controlPanel.add(relax);
        controlPanel.add(solve);
        controlPanel.add(knots);
        shake.addActionListener(e -> shake());
        xray.addActionListener(e -> panel.xray = xray.isSelected());
        relax.addActionListener(e -> panel.relax = relax.isSelected());
        solve.addActionListener(e -> panel.solve = solve.isSelected());
        knots.addActionListener(e -> panel.setKnots(knots.isSelected()));
    }

    void start() {
        var graph = reader.read();
        graph.markCycles();
        panel.setGraphs(KnotGraphConstructor.makeFrom(graph));
        panel.start();
    }

    private void shake() {
        panel.shake();
    }

}

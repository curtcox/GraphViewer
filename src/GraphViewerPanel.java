import java.awt.*;
import java.awt.event.*;

class GraphViewerPanel extends Panel implements ActionListener, ItemListener {

    private final GraphPanel   panel = new GraphPanel();
    private final Panel controlPanel = new Panel();
    private final Button    scramble = new Button("Scramble");
    private final Button       shake = new Button("Shake");
    private final Checkbox    stress = new Checkbox("Stress");
    private final Checkbox      xray = new Checkbox("X-ray");
    private final Checkbox     relax = new Checkbox("Relax");
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
        scramble.addActionListener(this);
        shake.addActionListener(this);
        stress.addItemListener(this);
        xray.addItemListener(this);
        relax.addItemListener(this);
    }

    void start() {
        panel.graph = reader.read(panel.getSize());
        panel.start();
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == scramble) scramble();
        if (src == shake)    shake();
    }

    void scramble() {
        panel.scramble();
    }

    void shake() {
        panel.shake();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object src = e.getSource();
        boolean on = e.getStateChange() == ItemEvent.SELECTED;
        if (src == stress) { panel.stress = on; }
        if (src == relax)  { panel.relax = on; }
        if (src == xray)   { panel.xray = on; }
    }

}

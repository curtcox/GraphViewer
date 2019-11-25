import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

final class FilterPanel extends JPanel {

    private final JTextField  text = new JTextField();
    private final JCheckBox filter = new JCheckBox("Filter");
    private final GraphPanel panel;

    FilterPanel(GraphPanel panel) {
        this.panel = panel;
        add(text);
        add(filter);
        text.setColumns(40);
        filter.addActionListener(e -> updateFilter());
        text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                updateFilter();
            }
        });
    }

    private void updateFilter() {
        if (filter.isSelected()) {
            panel.setFilter(new GraphFilter(text.getText()));
        } else {
            panel.setFilter(new GraphFilter(""));
        }
    }

}

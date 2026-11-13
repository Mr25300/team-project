package view;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/** Temporary screen used until real view for a feature exists */
public class PlaceholderView extends JPanel implements ActivatableView {
    private final String title;
    private final JLabel label;
    private int activations = 0;

    public PlaceholderView(String title) {
        super(new BorderLayout());
        this.title = title;
        this.label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 22f));
        add(label, BorderLayout.CENTER);
    }

    @Override 
    public void onActivated() {
        activations++;
        label.setText(title + " (shown " + activations + "x");
    }
}
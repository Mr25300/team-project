package view.dashboard;

import interface_adapter.dashboard.DashboardStateCatalog;

import javax.swing.*;
import java.awt.*;

/**
 * Render a whole-screen message.
 */
public class MessageRenderer implements DashboardRenderer<DashboardStateCatalog.MessageState> {

    @Override
    public JComponent render(DashboardStateCatalog.MessageState itemState) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(itemState.text());
        label.setFont(label.getFont().deriveFont(Font.BOLD, itemState.size()));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}

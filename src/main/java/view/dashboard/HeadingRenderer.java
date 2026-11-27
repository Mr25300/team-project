package view.dashboard;

import interface_adapter.dashboard.DashboardStateCatalog;

import javax.swing.*;
import java.awt.*;

/**
 * Render a header.
 */
public class HeadingRenderer implements DashboardRenderer<DashboardStateCatalog.HeadingState> {
    @Override
    public JComponent render(DashboardStateCatalog.HeadingState state) {
        JLabel lab = new JLabel(state.text());
        lab.setFont(lab.getFont().deriveFont(Font.BOLD, state.size()));
        return lab;
    }
}
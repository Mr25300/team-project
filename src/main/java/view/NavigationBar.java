package view;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import interface_adapter.ViewManagerModel;

/** Sidebar that navigates by updating ViewManagerModel */
public class NavigationBar extends JPanel implements PropertyChangeListener {
    private final ViewManagerModel viewManagerModel;
    private final ButtonGroup group = new ButtonGroup();
    private final Map<String, JToggleButton> navButtons = new HashMap<>();

    public NavigationBar(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, java.awt.Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(12, 8, 12, 8)));
        viewManagerModel.addPropertyChangeListener(this);
    }

    public void addNavItem(String label, String viewName) {
        JToggleButton button = new JToggleButton(label);
        button.addActionListener(e -> viewManagerModel.setActiveView(viewName));
        group.add(button);
        navButtons.put(viewName, button);
        addButton(button);
    }

    /** Package-pricate: lets tests click buttons without searching the component tree */
    JToggleButton getNavButton(String viewName) {
        return navButtons.get(viewName);
    }

    private void addButton(AbstractButton button) {
        button.setAlignmentX(LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
        add(button);
        add(Box.createVerticalStrut(4));
    }

    @Override 
    public void propertyChange(PropertyChangeEvent evt) {
        String activeView = (String) evt.getNewValue();
        SwingThread.runOnEdt(() -> {
            JToggleButton button = navButtons.get(activeView);
            if (button != null) {
                button.setSelected(true);
            }
        });
    }
}
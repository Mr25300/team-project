package view;

import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import interface_adapter.ViewManagerModel;

/** Swaps which screen is visible whenever ViewManagerModel's active view changes. */
public class ViewManager implements PropertyChangeListener {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private final Map<String, JComponent> views = new HashMap<>();

    public ViewManager(ViewManagerModel viewManagerModel) {
        viewManagerModel.addPropertyChangeListener(this);
    }

    public void addView(String viewName, JComponent view) {
        views.put(viewName, view);
        cardPanel.add(view, viewName);
    }

    /** The panel to place in the main window */
    public JPanel getCardPanel() {
        return cardPanel;
    }

    @Override 
    public void propertyChange(PropertyChangeEvent evt) {
        String viewName = (String) evt.getNewValue();
        SwingThread.runOnEdt(() -> show(viewName));
    }

    private void show(String viewName) {
        JComponent view = views.get(viewName);
        if (view == null) {
            return;     // unknown name: ignore, dont crash the UI
        }
        cardLayout.show(cardPanel, viewName);
        if (view instanceof ActivatableView) {
            ((ActivatableView) view).onActivated();
        }
    }
}
package view;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import interface_adapter.ViewManagerModel;
import static org.junit.jupiter.api.Assertions.*;

class ViewManagerTest {
    /** Test double: a panel that counts activations */
    static class FakeActivatableView extends JPanel implements ActivatableView {
        int activations = 0;

        @Override 
        public void onActivated() {
            activations++;
        }
    }

    private ViewManagerModel model;
    private ViewManager manager;
    private FakeActivatableView trips;
    private JPanel plain;

    @BeforeEach 
    void setUp() {
        model = new ViewManagerModel();
        manager = new ViewManager(model);
        trips = new FakeActivatableView();
        plain = new JPanel();
        manager.addView("trips", trips);
        manager.addView("plain", plain);
    }

    /** Run on the EDT and wait */
    private void navigate(String viewName) throws Exception {
        SwingUtilities.invokeAndWait(() -> model.setActiveView(viewName));
    }

    @Test 
    void switchingShowsRequestedCard() throws Exception {
        navigate("plain");
        assertTrue(plain.isVisible());
        assertFalse(trips.isVisible());
    }

    @Test 
    void activatableViewIsNotifiedOncePerSwitch() throws Exception {
        navigate("trips");
        navigate("plain");
        navigate("trips");
        assertEquals(2, trips.activations);
    }

    @Test 
    void unknownViewIsIgnored() throws Exception {
        assertDoesNotThrow(() -> navigate("does-not-exist"));
        assertEquals(0, trips.activations);
    }
}
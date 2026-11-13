package interface_adapter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ViewManagerModelTest {
    
    @Test
    void setActiveViewUpdatesStateAndFires() {
        ViewManagerModel model = new ViewManagerModel();
        String[] received = {null};
        model.addPropertyChangeListener(evt -> received[0] = (String) evt.getNewValue());

        model.setActiveView("map");

        assertEquals("map", model.getActiveView());
        assertEquals("map", received[0]);
    }
}

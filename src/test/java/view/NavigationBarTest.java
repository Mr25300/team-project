package view;

import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import interface_adapter.ViewManagerModel;

import static org.junit.jupiter.api.Assertions.*;

class NavigationBarTest {
    private ViewManagerModel model;
    private NavigationBar nav;

    @BeforeEach
    void setUp() {
        model = new ViewManagerModel();
        nav = new NavigationBar(model);
        nav.addNavItem("Trips", "trips");
        nav.addNavItem("Map", "map");
    }

    @Test
    void clickingButtonSetsActiveView() throws Exception {
        SwingUtilities.invokeAndWait(() -> nav.getNavButton("map").doClick());
        assertEquals("map", model.getActiveView());
    }

    @Test
    void modelChangeHighlightsMatchingButtonOnly() throws Exception {
        SwingUtilities.invokeAndWait(() -> model.setActiveView("map"));
        SwingUtilities.invokeAndWait(() -> model.setActiveView("trips"));

        assertTrue(nav.getNavButton("trips").isSelected());
        assertFalse(nav.getNavButton("map").isSelected());
    }

    @Test
    void viewWithoutButtonKeepsPreviousHighlight() throws Exception {
        SwingUtilities.invokeAndWait(() -> model.setActiveView("trips"));
        SwingUtilities.invokeAndWait(() -> model.setActiveView("trip-detail"));

        assertTrue(nav.getNavButton("trips").isSelected());
    }
}
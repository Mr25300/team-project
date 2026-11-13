package app;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.border.Border;

import data_access.InMemoryTripDataAcessObject;
import data_access.SampleData;
import interface_adapter.ViewManagerModel;
import interface_adapter.ViewNames;
import view.NavigationBar;
import view.PlaceholderView;
import view.ViewManager;

/**
 * Composition Root. Only place that creates concrete classes and connects them.
 * Use: new AppBuilder().addTripListView().addMapView()...build()
 */
public class AppBuilder {
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(viewManagerModel);
    private final NavigationBar navigationBar = new NavigationBar(viewManagerModel);


    // Single swap point for storage. (Replace this with file stuff later)
    private final InMemoryTripDataAcessObject tripDao = new InMemoryTripDataAcessObject();


    public AppBuilder() {
        SampleData.populate(tripDao);
    }

    public AppBuilder addTripListView() {
        return addScreen("Trips", ViewNames.TRIPS, new PlaceholderView("Trips"));
    }

    public AppBuilder addMapView() {
        return addScreen("Map", ViewNames.MAP, new PlaceholderView("Map"));
    }

    public AppBuilder addDashBoardView() {
        return addScreen("Dashboard", ViewNames.DASHBOARD, new PlaceholderView("Dashboard"));
    }

    private AppBuilder addScreen(String label, String viewName, JComponent view) {
        viewManager.addView(viewName, view);
        navigationBar.addNavItem(label, viewName);
        return this;
    }

    public JFrame build() {
        JFrame frame = new JFrame("Journeye");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(navigationBar, BorderLayout.WEST);
        frame.add(viewManager.getCardPanel(), BorderLayout.CENTER);
        frame.setSize(1100, 720);
        frame.setLocationRelativeTo(null);

        viewManagerModel.setActiveView(ViewNames.TRIPS);
        return frame;

    }
}
package view.map;

import interface_adapter.ViewModel;
import interface_adapter.map.MapController;
import interface_adapter.map.MapViewModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.*;
import view.ActivatableView;
import view.map.waypoints.ColoredWaypoint;
import view.map.waypoints.ColoredWaypointRenderer;

public class MapView extends JPanel implements ActivatableView, PropertyChangeListener {

    private final MapViewModel viewModel;
    private final JXMapViewer map = new JXMapViewer();
    private MapController controller;

    private final WaypointPainter<ColoredWaypoint> waypointPainter = new WaypointPainter<>();
    private final JComboBox<String> filterDropdown = new JComboBox<>();

    public MapView(MapViewModel viewModel, MapController controller){
        this.viewModel = viewModel;
        this.controller = controller;

        MouseInputListener pan = new PanMouseInputListener(map);
        map.addMouseListener(pan);
        map.addMouseMotionListener(pan);
        map.addMouseWheelListener(new ZoomMouseWheelListenerCenter(map));

        map.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Waypoint hit = findWaypointAt(e.getPoint());
                if (hit != null) {
                    GeoPosition pos = hit.getPosition();
                    System.out.println("Clicked waypoint: " + pos.getLatitude() + ", " + pos.getLongitude());
                }
            }
        });

        waypointPainter.setRenderer(new ColoredWaypointRenderer());
        viewModel.addPropertyChangeListener(this);
        map.setOverlayPainter(waypointPainter);

        CleanTileFactoryInfo info = new CleanTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        map.setTileFactory(tileFactory);

        filterDropdown.addActionListener(e -> {
            String selected = (String) filterDropdown.getSelectedItem();
            System.out.println("Dropdown selected: " + selected);
        });

        String[] items = viewModel.getState().getTripCatalog().toArray(new String[0]);
        filterDropdown.setModel(new DefaultComboBoxModel<>(items));

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.add(new JLabel("Display Trips:"));
        topBar.add(filterDropdown);


        setLayout(new BorderLayout());
        add(topBar, BorderLayout.NORTH);
        add(map, BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        waypointPainter.setWaypoints(viewModel.getState().getWaypoints());
        map.repaint();

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        viewModel.getState().getTripCatalog().forEach(model::addElement);   // includes "All" already
        filterDropdown.setModel(model);
    }

    @Override
    public void onActivated() {
        controller.execute();
        map.setZoom(6);
    }

    private Waypoint findWaypointAt(Point click) {
        Rectangle viewport = map.getViewportBounds();
        int zoom = map.getZoom();
        Waypoint closest = null;
        double closestDist = Double.MAX_VALUE;

        for (Waypoint wp : viewModel.getState().getWaypoints()) {
            Point2D world = map.getTileFactory().geoToPixel(wp.getPosition(), zoom);
            double screenX = world.getX() - viewport.getX();
            double screenY = world.getY() - viewport.getY();

            double dist = Math.hypot(click.x - screenX, click.y - (screenY - 16));
            if (dist <= 16 && dist < closestDist) {
                closest = wp;
                closestDist = dist;
            }
        }
        return closest;
    }


}

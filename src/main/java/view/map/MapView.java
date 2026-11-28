package view.map;

import interface_adapter.ViewModel;
import interface_adapter.map.MapController;
import interface_adapter.map.MapViewModel;
import interface_adapter.map.TripOption;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.*;
import view.components.ThumbnailCache;
import view.map.waypoints.ColoredWaypoint;
import view.map.waypoints.ColoredWaypointRenderer;

public class MapView extends JPanel implements PropertyChangeListener {

    private final MapViewModel viewModel;
    private final JXMapViewer map = new JXMapViewer();
    private MapController controller;

    private final WaypointPainter<ColoredWaypoint> waypointPainter = new WaypointPainter<>();
    private final JComboBox<TripOption> filterDropdown = new JComboBox<>();
    private final LocationDetailsPanel detailsPanel;

    public MapView(MapViewModel viewModel, MapController controller, ThumbnailCache thumbnails){
        this.viewModel = viewModel;
        this.controller = controller;
        this.detailsPanel = new LocationDetailsPanel(thumbnails);

        MouseInputListener pan = new PanMouseInputListener(map);
        map.addMouseListener(pan);
        map.addMouseMotionListener(pan);
        map.addMouseWheelListener(new ZoomMouseWheelListenerCenter(map));

        waypointPainter.setRenderer(new ColoredWaypointRenderer());
        viewModel.addPropertyChangeListener(this);
        map.setOverlayPainter(waypointPainter);

        map.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ColoredWaypoint hit = findWaypointAt(e.getPoint());
                if (hit != null) {
                    onWaypointClicked(hit);
                }
            }
        });

        CleanTileFactoryInfo info = new CleanTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        map.setTileFactory(tileFactory);

        controller.execute(null);

        filterDropdown.addActionListener(e -> {
            TripOption selected = (TripOption) filterDropdown.getSelectedItem();
            controller.execute(selected.id());
        });



        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.add(new JLabel("Display Trips:"));
        topBar.add(filterDropdown);

        setLayout(new BorderLayout());
        add(topBar, BorderLayout.NORTH);
        add(map, BorderLayout.CENTER);
        add(detailsPanel, BorderLayout.EAST);

        map.setZoom(6);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        waypointPainter.setWaypoints(viewModel.getState().getWaypoints()
                .stream()
                .map(waypointData ->
                        new ColoredWaypoint(waypointData.color(),
                                waypointData.tripId(),
                                waypointData.photoId(),
                                waypointData.latitude(),
                                waypointData.longitude())).collect(Collectors.toSet()));

        DefaultComboBoxModel<TripOption> model = new DefaultComboBoxModel<>();
        model.addAll(viewModel.getState().getTripOptions());
        filterDropdown.setModel(model);
        filterDropdown.setSelectedItem(viewModel.getState().getSelectedTripOption());

        if(evt.getPropertyName().equals("selectedLocation")) {
            detailsPanel.showLocation(viewModel.getState().getSelectedLocation());
        }

        map.repaint();
    }

    private static final double WAYPOINT_RADIUS = 8.0;

    private ColoredWaypoint findWaypointAt(Point click) {
        int zoom = map.getZoom();
        Rectangle viewport = map.getViewportBounds();

        double worldX = click.x + viewport.getX();
        double worldY = click.y + viewport.getY();

        ColoredWaypoint best = null;
        double bestDistSq = WAYPOINT_RADIUS * WAYPOINT_RADIUS;

        for (ColoredWaypoint wp : waypointPainter.getWaypoints()) {
            Point2D p = map.getTileFactory().geoToPixel(wp.getPosition(), zoom);
            double dx = p.getX() - worldX;
            double dy = p.getY() - worldY;
            double distSq = dx * dx + dy * dy;

            if (distSq <= bestDistSq) {   // nearest waypoint within 16px wins
                bestDistSq = distSq;
                best = wp;
            }
        }
        return best;
    }

    private void onWaypointClicked(ColoredWaypoint waypoint) {
        controller.selectWaypoint(waypoint);
    }
}

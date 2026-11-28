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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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
    private final JComboBox<TripOption> filterDropdown = new JComboBox<>();

    public MapView(MapViewModel viewModel, MapController controller){
        this.viewModel = viewModel;
        this.controller = controller;

        MouseInputListener pan = new PanMouseInputListener(map);
        map.addMouseListener(pan);
        map.addMouseMotionListener(pan);
        map.addMouseWheelListener(new ZoomMouseWheelListenerCenter(map));

        waypointPainter.setRenderer(new ColoredWaypointRenderer());
        viewModel.addPropertyChangeListener(this);
        map.setOverlayPainter(waypointPainter);

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

        map.setZoom(6);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        waypointPainter.setWaypoints(viewModel.getState().getWaypoints()
                .stream()
                .map(waypointData ->
                        new ColoredWaypoint(waypointData.color(),
                                waypointData.latitude(),
                                waypointData.longitude())).collect(Collectors.toSet()));

        DefaultComboBoxModel<TripOption> model = new DefaultComboBoxModel<>();
        model.addAll(viewModel.getState().getTripOptions());
        filterDropdown.setModel(model);
        filterDropdown.setSelectedItem(viewModel.getState().getSelectedTripOption());
        map.repaint();
    }

    @Override
    public void onActivated() {

    }


}

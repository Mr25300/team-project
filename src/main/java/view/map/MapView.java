package view.map;

import interface_adapter.ViewModel;
import interface_adapter.map.MapController;
import interface_adapter.map.MapViewModel;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.*;
import view.ActivatableView;

public class MapView extends JPanel implements ActivatableView, PropertyChangeListener {

    private final MapViewModel viewModel;
    private final JXMapViewer map = new JXMapViewer();
    private MapController controller;

    public MapView(MapViewModel viewModel, MapController controller){
        this.viewModel = viewModel;
        this.controller = controller;

        MouseInputListener pan = new PanMouseInputListener(map);
        map.addMouseListener(pan);
        map.addMouseMotionListener(pan);
        map.addMouseWheelListener(new ZoomMouseWheelListenerCenter(map));


        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();
        waypointPainter.setWaypoints(viewModel.getState().getWaypoints());
        map.setOverlayPainter(waypointPainter);

        CleanTileFactoryInfo info = new CleanTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        map.setTileFactory(tileFactory);

        setLayout(new BorderLayout());
        add(map, BorderLayout.CENTER);
    }

    private void render(){
        map.setZoom(6);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }



    @Override
    public void onActivated() {
        render();
        controller.execute();
    }
}

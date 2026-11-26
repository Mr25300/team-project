package interface_adapter.map;

import interface_adapter.ViewModel;
import interface_adapter.ViewNames;
import view.map.MapView;

public class MapViewModel extends ViewModel<MapState> {

    public MapViewModel() {
        super(ViewNames.MAP);
        setState(new MapState());
    }
}

package view.map;

import org.jxmapviewer.viewer.TileFactoryInfo;

public class CleanTileFactoryInfo extends TileFactoryInfo {
    private static final String BASE_URL =
            "https://server.arcgisonline.com/ArcGIS/rest/services/Canvas/World_Light_Gray_Base/MapServer/tile";

    public CleanTileFactoryInfo() {
        super("Esri Light Gray", 3, 19, 19, 256, true, true, BASE_URL, "x", "y", "z");
    }

    @Override
    public String getTileUrl(int x, int y, int zoom) {
        int z = getTotalMapZoom() - zoom;
        return baseURL + "/" + z + "/" + y + "/" + x;   // y before x, no .png
    }
}

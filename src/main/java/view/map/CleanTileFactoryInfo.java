package view.map;

import org.jxmapviewer.viewer.TileFactoryInfo;

public class CleanTileFactoryInfo extends TileFactoryInfo {
    private static final String BASE_URL =
            "https://tile.openstreetmap.org";

    public CleanTileFactoryInfo() {
        super("Map", 3, 19, 19, 256, true, true, BASE_URL, "x", "y", "z");
    }

    @Override
    public String getTileUrl(int x, int y, int zoom) {
        int z = getTotalMapZoom() - zoom;
        return baseURL + "/" + z + "/" + x + "/" + y + ".png";   // y before x, no .png
    }
}

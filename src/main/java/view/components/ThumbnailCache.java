package view.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingWorker;

/**
 * Loads square, center-cropped thumbnails off the EDT and keeps the most recent ones in memory.
 * Call only from EDT (renderers and SwingWorker.done() both run through there). 
 */
public class ThumbnailCache {
    private final int size;
    private final Icon placeholder;
    private final Map<String, Icon> cache;
    private final Set<String> loading = new HashSet<>();

    public ThumbnailCache(int size, int maxEntries) {
        this.size = size;
        this.placeholder = new ImageIcon(solid(size, new Color(0xDDDDDD)));
        this.cache = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Icon> eldes) {
                return size() > maxEntries;
            }
        };
    }

    public int getSize() {
        return size;
    }

    /**
     * Returns the thumbnail if cached; otherwise starts loading it, returns the placeholder,
     * and calls onLoaded() (on the EDT) once the real image is ready. 
     */
    public Icon get(String path, Runnable onLoaded) {
        if (path == null) {
            return placeholder;
        }
        Icon cached = cache.get(path);
        if (cached != null) {
            return cached;
        }
        if (loading.add(path)) {
            new SwingWorker<Icon, Void>() {
                @Override 
                protected Icon doInBackground() throws Exception {
                    BufferedImage image = ImageIO.read(new File(path));
                    return image == null ? placeholder : new ImageIcon(cropToSquare(image, size));
                }

                @Override 
                protected void done() {
                    Icon result;
                    try {
                        result = get();
                    } catch (Exception e) {
                        result = placeholder; // unreadable file: cache placeholder so we dont retry forever
                    }
                    cache.put(path, result);
                    loading.remove(path);
                    onLoaded.run();
                }
            }.execute();
        }
        return placeholder;
    }

    private static BufferedImage cropToSquare(BufferedImage source, int size) {
        double scale = (double) size / Math.min(source.getWidth(), source.getHeight());
        int width = (int) Math.round(source.getWidth() * scale);
        int height = (int) Math.round(source.getHeight() * scale);

        BufferedImage result = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = result.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(source, (size - width) / 2, (size - height) / 2, width, height, null);
        g.dispose();
        return result;
    }

    private static BufferedImage solid(int size, Color color) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, size, size);
        g.dispose();
        return image;
    }
}
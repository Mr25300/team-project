package view.map;

import interface_adapter.map.MapState;
import interface_adapter.map.SelectedLocation;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import view.components.ThumbnailCache;

public class LocationDetailsPanel extends JPanel {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final ThumbnailCache thumbnails;
    private final JLabel photo = new JLabel();
    private final JTextArea text = new JTextArea();
    private String currentPhotoId;

    public LocationDetailsPanel(ThumbnailCache thumbnails) {
        this.thumbnails = thumbnails;
        photo.setHorizontalAlignment(SwingConstants.CENTER);
        text.setEditable(false);
        text.setOpaque(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);

        setLayout(new BorderLayout(0, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setPreferredSize(new Dimension(220, 0));
        add(photo, BorderLayout.NORTH);
        add(text, BorderLayout.CENTER);
        setVisible(false);
    }

    public void showLocation(SelectedLocation location) {
        String photoId = location.photoId();
        currentPhotoId = photoId;

        photo.setIcon(thumbnails.get(photoId, () -> {
            // the user may have clicked another dot while this photo was loading
            if (photoId.equals(currentPhotoId)) {
                photo.setIcon(thumbnails.get(photoId, () -> { }));
            }
        }));
        text.setText(describe(location));
        setVisible(true);
        revalidate();
    }

    public void clear() {
        currentPhotoId = null;
        setVisible(false);
    }

    private String describe(SelectedLocation location) {
        StringBuilder message = new StringBuilder();
        if (location.placeName() != null) {
            message.append(location.placeName()).append('\n');
        }
        message.append("Trip: ").append(location.tripName()).append('\n');
        message.append(String.format(Locale.ROOT, "Coordinates: %.5f, %.5f",
                location.latitude(), location.longitude()));
        if (location.time() != null) {
            message.append("\nTaken: ").append(location.time().format(TIME_FORMAT));
        }
        return message.toString();
    }
}
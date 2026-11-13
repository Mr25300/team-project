package view.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import interface_adapter.list_trips.TripListItem;

/** Draws one trip as [thumbnail] Name / dates / place · photos. */
public class TripCardRenderer extends JPanel implements ListCellRenderer<TripListItem> {
    private final ThumbnailCache thumbnails;
    private final Runnable repaintList;
    private final JLabel cover = new JLabel();
    private final JLabel name = new JLabel();
    private final JLabel dates = new JLabel();
    private final JLabel details = new JLabel();

    public TripCardRenderer(ThumbnailCache thumbnails, Runnable repaintList) {
        super(new BorderLayout(12, 0));
        this.thumbnails = thumbnails;
        this.repaintList = repaintList;

        name.setFont(name.getFont().deriveFont(Font.BOLD, 15f));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(name);
        text.add(dates);
        text.add(details);

        add(cover, BorderLayout.WEST);
        add(text, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    }

    @Override 
    public Component getListCellRendererComponent(JList<? extends TripListItem> list, TripListItem item,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        cover.setIcon(thumbnails.get(item.getCoverPhotoPath(), repaintList));
        name.setText(item.getName());
        dates.setText(item.getDateRange());
        details.setText(item.getDetails());

        setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        for (JLabel label : new JLabel[] {name, dates, details}) {
            label.setForeground(isSelected ? list.getSelectionBackground() : list.getForeground());
        }
        return this;
    }
}
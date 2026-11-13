package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


/**
 * Observable holder for one screen's state.
 * views listen and redraw.
 * @param <T> the state type for this screen
 */
public class ViewModel<T> {
    public static final String STATE_PROPERTY = "state";

    private final String viewName;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private T state;

    public ViewModel(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public T getState() {
        return state;
    }

    public void setState(T state) {
        this.state = state;
    }

    public void firePropertyChanged() {
        firePropertyChanged(STATE_PROPERTY);
    }

    public void firePropertyChanged(String propertyName) {
        support.firePropertyChange(propertyName, null, state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}

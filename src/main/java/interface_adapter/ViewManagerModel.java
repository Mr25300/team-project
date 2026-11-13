package interface_adapter;

/** Holds which screen is showing. */
public class ViewManagerModel extends ViewModel<String> {

    public ViewManagerModel() {
        super("view manager");
    }

    public void setActiveView(String viewName) {
        setState(viewName);
        firePropertyChanged();
    }

    public String getActiveView() {
        return getState();
    }
}
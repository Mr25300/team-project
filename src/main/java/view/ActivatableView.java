package view;

/** A view that wants to refresh its data each time it becomes visible */
public interface ActivatableView {
    void onActivated();
}
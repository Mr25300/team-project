package view;

import javax.swing.SwingUtilities;

/** Swing components may only be touched on the Event Dispatch Thread */
public final class SwingThread {
    private SwingThread() {}

    public static void runOnEdt(Runnable task) {
        if (SwingUtilities.isEventDispatchThread()) {
            task.run();
        } else {
            SwingUtilities.invokeLater(task);
        }
    }
}
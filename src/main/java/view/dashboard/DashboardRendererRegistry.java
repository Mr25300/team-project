package view.dashboard;

import interface_adapter.dashboard.DashboardController;
import interface_adapter.dashboard.DashboardItemState;
import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardStateCatalog;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for the renderers for each dashboard item
 */
public class DashboardRendererRegistry {
    private final Map<Class<?>, DashboardRenderer<? extends DashboardItemState>> dashboardRenderers = new HashMap<>();

    public DashboardRenderer resolve(Class<?> dashboardItemType) {
        return dashboardRenderers.get(dashboardItemType);
    }

    public void registerAll(DashboardController dashboardController) {
        register(DashboardStateCatalog.TableState.class, new TableRenderer());
        register(DashboardStateCatalog.NestedState.class, new NestedRenderer(this));
        register(DashboardStateCatalog.DropdownState.class, new DropdownRenderer(dashboardController));
        register(DashboardStateCatalog.LinePlotState.class, new LinePlotRenderer());
        register(DashboardStateCatalog.HeadingState.class, new HeadingRenderer());
        register(DashboardStateCatalog.MessageState.class, new MessageRenderer());
    }
    private <T extends DashboardItemState> void register(Class<T> type, DashboardRenderer<? extends DashboardItemState> dashboardRenderer) {
        dashboardRenderers.put(type, dashboardRenderer);
    }
}

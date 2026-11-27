package interface_adapter.dashboard;

import use_case.dashboard.DashboardItem;

import java.util.List;

/**
 * A catalog for dashboard component states.
 */
public class DashboardStateCatalog {

    public record NestedState(
            List<DashboardItemState> dashboardStates
    ) implements DashboardItemState {}

    public record TableState(
            String title,
            List<String> columns,
            List<List<String>> rows
    ) implements DashboardItemState {}

    public record DropdownState(
            String actionId,
            String label,
            List<String> optionLabels
    ) implements DashboardItemState {}

    public record LinePlotState(
            List<Integer> x_vals,
            List<Integer> y_vals,
            List<String> dates,
            List<Integer> counts
    ) implements DashboardItemState {}

    public record HeadingState(
            String text,
            float size
    ) implements DashboardItemState {}

    public record MessageState(
            String text,
            float size
    ) implements DashboardItemState {}
}

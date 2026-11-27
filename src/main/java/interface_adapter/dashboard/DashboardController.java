package interface_adapter.dashboard;

import entity.LocationInfoType;
import use_case.dashboard.AnalyticsOptions;
import use_case.dashboard.AnalyticsType;
import use_case.dashboard.DashboardInputBoundary;
import use_case.dashboard.DashboardInputDataCatalog;


import java.util.Optional;

/**
 * Controller for the dashboard use cases.
 */
public class DashboardController {
    private final DashboardInputBoundary analyticsUseCase;

    public DashboardController(DashboardInputBoundary analyticsUseCase) {
        this.analyticsUseCase = analyticsUseCase;
    }

    public void requestAnalytics(AnalyticsType analyticsType) {
        switch (analyticsType) {
            case MOST_VISITED -> requestMostVisitedAnalytics(Optional.empty());
            case PHOTO_ACTIVITY -> requestPhotoActivityAnalytics(Optional.empty());
            default -> analyticsUseCase.execute(new DashboardInputDataCatalog.GenericInputData(analyticsType));
        }
    }
    public void requestMostVisitedAnalytics(Optional<LocationInfoType> locationInfoType) {
        analyticsUseCase.execute(new DashboardInputDataCatalog.MostVisitedInputData(locationInfoType));
    }

    public void requestPhotoActivityAnalytics(Optional<AnalyticsOptions.TimeGrouping> timeGrouping) {
        analyticsUseCase.execute(new DashboardInputDataCatalog.PhotoActivityInputData(timeGrouping));
    }
}

package use_case.list_trips;

/** Implemented by presenter. Interactor reports results through this. */
public interface ListTripsOutputBoundary {
    void prepareSuccessView(ListTripsOutputData outputData);

    void prepareFailView(String errorMessage);
}
package interface_adapter;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ViewModelTest {

    /** Mutable state, like real presenters use. */
    static class CounterState {
        int count;
    }

    @Test 
    void listenerReceivesNewState() {
        ViewModel<CounterState> vm = new ViewModel<>("counter");
        List<Object> received = new ArrayList<>();
        vm.addPropertyChangeListener(evt -> received.add(evt.getNewValue()));

        CounterState state = new CounterState();
        vm.setState(state);
        vm.firePropertyChanged();

        assertEquals(List.of(state), received);
    }

    @Test 
    void firesEvenWhenSameStateObjectIsMutatedAndReset() {
        ViewModel<CounterState> vm = new ViewModel<>("counter");
        CounterState state = new CounterState();
        vm.setState(state);

        int[] fires = {0};
        vm.addPropertyChangeListener(evt -> fires[0]++);

        state.count = 1;
        vm.setState(state);
        vm.firePropertyChanged();
        state.count = 2;
        vm.setState(state);
        vm.firePropertyChanged();

        assertEquals(2, fires[0]);
    }

    @Test 
    void customPropertyNameIsDelivered() {
        ViewModel<String> vm = new ViewModel<>("x");
        List<String> names = new ArrayList<>();
        vm.addPropertyChangeListener(evt -> names.add(evt.getPropertyName()));

        vm.firePropertyChanged("error");
        assertEquals(List.of("error"), names);
    }

    @Test 
    void allListenersNotifiedAndRemovedOnesAreNot() {
        ViewModel<String> vm = new ViewModel<>("x");
        int[] a = {0};
        int[] b = {0};
        java.beans.PropertyChangeListener la = evt -> a[0]++;
        vm.addPropertyChangeListener(la);
        vm.addPropertyChangeListener(evt -> b[0]++);

        vm.firePropertyChanged();
        vm.removePropertyChangeListener(la);
        vm.firePropertyChanged();

        assertEquals(1, a[0]);
        assertEquals(2, b[0]);
    }
}
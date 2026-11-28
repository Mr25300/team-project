package interface_adapter.map;

public record TripOption(String id, String name) {

    @Override
    public String toString(){
        return name;
    }
}

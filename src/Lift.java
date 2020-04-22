import java.util.List;

public class Lift {

    private List<Integer> stops;
    private String status;
    private LiftMovement direction;
    private int current_load;
    private List<Person> people;

    public Lift(LiftMovement direction, List<Integer> stops, String status, int current_load, List<Person> people){
        this.stops = stops;
        this.status = status;
        this.direction = direction;
        this.current_load = current_load;
        this.people = people;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Integer> getStops() {
        return stops;
    }

    public LiftMovement getDirection() {
        return direction;
    }

    public void setDirection(LiftMovement direction) {
        this.direction = direction;
    }

    public void setStops(List<Integer> stops) {
        this.stops = stops;
    }

    public int getCurrent_load() {
        return current_load;
    }

    public void setCurrent_load(int current_load) {
        this.current_load = current_load;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }
}

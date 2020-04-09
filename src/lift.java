import java.util.List;

public class lift {

    private List<Integer> stops;
    private String status;
    private LiftMovement direction;

    public lift(LiftMovement direction, List<Integer> stops, String status){
        this.stops = stops;
        this.status = status;
        this.direction = direction;
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
}

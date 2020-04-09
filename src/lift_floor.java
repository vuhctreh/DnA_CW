import java.util.ArrayList;
import java.util.List;

public class lift_floor implements Comparable<lift_floor>{

    private int floor;
    private boolean hasPeopleWaiting;
    private List<person> population;

    public lift_floor(int floor, boolean hasPeopleWaiting, List<person> population){
        this.floor = floor;
        this.hasPeopleWaiting = hasPeopleWaiting;
        this.population = population;
    }

    public void setFloor(int floor){
        this.floor = floor;
    }

    public void setPeopleWaiting(boolean hasPeopleWaiting){
        this.hasPeopleWaiting = hasPeopleWaiting;
    }

    public int getFloor(){
        return this.floor;
    }

    public boolean getPeopleWaiting() {
        return hasPeopleWaiting;
    }

    public List<person> getPopulation() {
        return population;
    }

    public void setPopulation(ArrayList<person> population) {
        this.population = population;
    }

    @Override
    public String toString(){
        return "Floor: " + getFloor();
    }

    @Override
    public int compareTo(lift_floor o) {
        return -1 * Integer.compare(o.getFloor(), getFloor());
    }
}

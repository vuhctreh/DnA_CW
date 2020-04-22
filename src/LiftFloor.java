import java.util.ArrayList;
import java.util.List;

public class LiftFloor implements Comparable<LiftFloor>{

    private int floor;
    private boolean hasPeopleWaiting;
    private List<Person> population;

    public LiftFloor(int floor, boolean hasPeopleWaiting, List<Person> population){
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

    public List<Person> getPopulation() {
        return population;
    }

    public void setPopulation(ArrayList<Person> population) {
        this.population = population;
    }

    @Override
    public String toString(){
        return "Floor: " + getFloor();
    }

    @Override
    public int compareTo(LiftFloor o) {
        return -1 * Integer.compare(o.getFloor(), getFloor());
    }
}

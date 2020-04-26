package ImprovedLift;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Lift {

    private final int maxWeight;
    private final List<Floor> floors;
    private Floor currentFloor;

    /**
     * Creates a {@link Lift} instance from multiple {@link Floor}
     *
     * @param baseFloors Floors
     */
    public Lift(int maxWeight, Floor... baseFloors) {
        this(maxWeight, new ArrayList<>(Arrays.asList(baseFloors)));
    }

    /**
     * Creates a {@link Lift} instance from multiple {@link Floor}
     *
     * @param floors Floors
     */
    public Lift(int maxWeight, List<Floor> floors) {
        this.maxWeight = maxWeight;
        this.floors = floors;
        if(floors.isEmpty()) {
            throw new RuntimeException("Lift cannot be empty");
        }
        this.currentFloor = floors.get(0);
        for(Floor floor: floors) {
            floor.forLift(this);
        }
    }

    /**
     * @return The list of each available floor
     */
    public List<Floor> getFloors() {
        return floors;
    }

    /**
     * Moves the {@link Lift} using specified {@link Movement}
     *
     * @param movement Movement
     */
    public void move(Movement movement) {
        int futureFloorIndex = currentFloor.getId() + movement.getListOperation();
        if(futureFloorIndex < 0)
            futureFloorIndex = 0;
        else if(futureFloorIndex > floors.size() - 1)
            futureFloorIndex = floors.size() - 1;

        final int currentWeight = currentFloor.getPersons().stream()
                .map(Person::getWeight)
                .reduce(Integer::sum)
                .orElse(0);

        final Floor futureFloor = floors.get(futureFloorIndex);

        if(currentWeight > maxWeight) {
            currentFloor = futureFloor;
            return;
        }

        final Iterator<Person> iterator = currentFloor.getPersons().iterator();
        while (iterator.hasNext()) {
            final Person person = iterator.next();
            if(person.hasReachedDestination())
                continue;
            int diff = person.getDestinationFloor() - futureFloorIndex;
            if(diff >= 0) {
                if(movement == Movement.UP) {
                    // Iterator allows us to remove the person while iterating through the list
                    // without a Concurrent exception
                    iterator.remove();
                    futureFloor.addPerson(person);
                }
            } else {
                if(movement == Movement.DOWN) {
                    iterator.remove();
                    futureFloor.addPerson(person);
                }
            }
        }
        currentFloor = futureFloor;
    }

    @Override
    public String toString() {
        return "Lift{" +
                "floors=" + floors +
                ", currentFloor=" + currentFloor +
                '}';
    }
}
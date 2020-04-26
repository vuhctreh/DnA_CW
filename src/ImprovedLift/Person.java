package ImprovedLift;

public final class Person {

    private Floor currentFloor;
    private final int destinationFloor;
    private final int weight;

    public Person(int destination, int weight) {
        this.destinationFloor = destination;
        this.weight = weight;
    }

    /**
     * Specifies the {@link Floor} of this {@link Person}
     *
     * @param floor Floor
     */
    public void atFloor(Floor floor) {
        this.currentFloor = floor;
    }

    /**
     * @return The destination floor of this {@link Person}
     */
    public int getDestinationFloor() {
        return destinationFloor;
    }

    public int getWeight() {
        return weight;
    }

    /**
     * @return The required movement to reach destination
     */
    public Movement getRequiredMovement() {
        final int diff = destinationFloor - currentFloor.getId();
        if(diff == 0)
            return Movement.IDLE;
        else if(diff > 0)
            return Movement.UP;
        else
            return Movement.DOWN;
    }

    /**
     * @return True if this person has reached destination
     */
    public boolean hasReachedDestination() {
        return currentFloor.getId() == destinationFloor;
    }

    @Override
    public String toString() {
        return "Person{" +
                "destinationFloor=" + destinationFloor +
                ", currentFloor=" + currentFloor.getId() +
                ", weight=" + weight +
                '}';
    }
}
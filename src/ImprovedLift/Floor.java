package ImprovedLift;

import java.util.ArrayList;
import java.util.List;

public final class Floor {

    private Lift lift;
    private final String name;
    private final List<Person> persons = new ArrayList<>();
    private int id = -10;

    /**
     * Creates a {@link Floor} with nobody inside with a specific name used to recognize it.
     *
     * @param name Floor's name
     */
    public Floor(String name) {
        this.name = name;
    }

    /**
     * Specifies the {@link Lift} of this {@link Floor}
     *
     * @param lift
     */
    public void forLift(Lift lift) {
        this.lift = lift;
        this.id = lift.getFloors().indexOf(this);
    }

    /**
     * @return The name of this {@link Floor}
     */
    public String getName() {
        checkForInitialization();
        return name;
    }

    /**
     * @return The id of this {@link Floor}
     */
    public int getId() {
        checkForInitialization();
        return id;
    }

    /**
     * Adds a person to this {@link Floor}
     * @param person Person
     */
    public Floor addPerson(Person person) {
        persons.add(person);
        person.atFloor(this);
        return this;
    }

    /**
     * @return The list of persons
     */
    public List<Person> getPersons() {
        return persons;
    }

    /**
     * @return True if there are people waiting otherwise false
     */
    public boolean hasPeopleWaiting() {
        checkForInitialization();
        return persons.stream()
                .anyMatch(person -> !person.hasReachedDestination());
    }

    private void checkForInitialization() {
        if(lift == null || id == -10) {
            throw new RuntimeException("Floor " + name + " is not attached to any lift");
        }
    }

    /**
     * Checks whether the specified object is a {@link Floor} using {@link Floor#id}
     *
     * @param o Other object
     * @return True whether they are equals or not
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Floor floor = (Floor) o;
        return id == floor.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Floor{" +
                "name='" + name + '\'' +
                ", persons=" + persons +
                ", id=" + id +
                '}';
    }

}
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class BaseCase {
    public static LiftMovement direction = LiftMovement.UP;
    public static int floors = 6;
    public static int current_floor = 0;
    public static int max_load = 150;
    public static int TotalTimeInElevator = 0;
    public static int TotalPeopleInElevator = 0;

    static List<Integer> stops = new ArrayList<>();
    static List<Person> people_in_elevator = new ArrayList<>();
    static Lift elevator = new Lift(LiftMovement.UP, stops, "Idle", 0, people_in_elevator);

    public static void main(String[] args) throws InterruptedException {

        List<LiftFloor> Building = Generators.Population_Generator(floors);

        Collections.sort(Building);

        IntStream.range(0, Building.size()).forEach(i -> TotalPeopleInElevator+= Building.get(i).getPopulation().size());

        System.out.println("-----------------------------------------");
        IntStream.range(0, Building.size()).forEach(i -> System.out.println(Building.get(i).getPopulation().size()));
        System.out.println("-----------------------------------------");

        boolean isAtTop = false;

        // stream to check if there are people still waiting
        boolean ArePeopleWaiting = Building.stream().anyMatch(LiftFloor::getPeopleWaiting);

        while (ArePeopleWaiting) {
            if (direction == LiftMovement.UP) {
                for (current_floor = 0; current_floor < floors; current_floor++) {
                    if(current_floor > floors - 1) {
                        break;
                    }

                    elevator.getPeople().forEach(i -> i.setTime_in_elevator(i.getTime_in_elevator() + 1));

                    if (Building.get(current_floor).getPopulation().isEmpty())
                        Building.get(current_floor).setPeopleWaiting(false);

                    if (Building.get(current_floor).getPeopleWaiting()) {
                        final Iterator<Person> iterator = Building.get(current_floor).getPopulation().iterator();
                        while (iterator.hasNext()) {
                            final Person person = iterator.next();
                            if (person.getWeight() + elevator.getCurrent_load() > max_load)
                                break;
                            else {
                                stops.add(person.getDestination_floor());
                                elevator.setCurrent_load(person.getWeight() + elevator.getCurrent_load());
                                elevator.getPeople().add(person);
                                iterator.remove();
                            }
                        }
                        elevator.setStops(stops);
                        TimeUnit.SECONDS.sleep(2);
                    }
                    LiftDropOffLoop();
                    direction = LiftMovement.IDLE;
                    isAtTop = true;
                }
            }
            if (direction == LiftMovement.IDLE && isAtTop)
                direction = LiftMovement.DOWN;
            if (direction == LiftMovement.DOWN){
                for (current_floor = (floors - 1); current_floor > -1; current_floor--){
                    if (current_floor == 0){
                        isAtTop = false;
                        direction = LiftMovement.IDLE;
                        break;
                    }
                    if (Building.get(current_floor).getPopulation().isEmpty())
                        Building.get(current_floor).setPeopleWaiting(false);

                    if (Building.get(current_floor).getPeopleWaiting()) {
                        final Iterator<Person> iterator = Building.get(current_floor).getPopulation().iterator();
                        while (iterator.hasNext()) {
                            final Person person = iterator.next();
                            if (person.getWeight() + elevator.getCurrent_load() > max_load)
                                break;
                            else {
                                stops.add(person.getDestination_floor());
                                elevator.setCurrent_load(person.getWeight() + elevator.getCurrent_load());
                                elevator.getPeople().add(person);
                                iterator.remove();
                                }
                            }
                        elevator.setStops(stops);
                        TimeUnit.SECONDS.sleep(2);
                    }
                    LiftDropOffLoop();
                }
            }
            if (direction == LiftMovement.IDLE && !isAtTop)
                direction = LiftMovement.UP;
            ArePeopleWaiting = Building.stream().anyMatch(LiftFloor::getPeopleWaiting);
        }
        System.out.println("-----------------------------------------");
        System.out.println("There were " + TotalPeopleInElevator + " total people waiting for the elevator.");
        System.out.println("People were in the elevator for a cumulative total of " + TotalTimeInElevator + " floors.");
        System.out.println("The average time spent in the elevator is: " + TotalTimeInElevator/TotalPeopleInElevator + ". There were: " + floors + " floors.");
        System.out.println("-----------------------------------------");
    }

    public static void LiftDropOffLoop() {

        for (int i = 0; i < elevator.getStops().size(); i++) {
            if (elevator.getStops().get(i).equals(current_floor)) {
                System.out.println("If statement in drop off loop is true");
                final Iterator<Person> iterator = elevator.getPeople().iterator();
                while (iterator.hasNext()) {
                    final Person person = iterator.next();
                    if(person.getDestination_floor() == current_floor){
                        elevator.setCurrent_load(elevator.getCurrent_load() - person.getWeight());
                        TotalTimeInElevator += person.getTime_in_elevator();
                        iterator.remove();
                        System.out.println("Person dropped off.");
                    }
                }
                elevator.getStops().remove(elevator.getStops().get(i));
            }
        }
    }
}
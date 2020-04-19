import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

//TODO fix weight and check if elevator picks people up on the way down

public class algorithm2 {
    public static LiftMovement direction = LiftMovement.UP;
    public static int floors = 6;
    public static int current_floor = 0;
    public static int max_load = 100;

    static List<Integer> stops = new ArrayList<>();
    static List<person> people_in_elevator = new ArrayList<>();
    static lift elevator = new lift(LiftMovement.UP, stops, "Idle", 0, people_in_elevator);

    public static void main(String[] args) throws InterruptedException {

        List<lift_floor> Building = Generators.Population_Generator(floors);

        Collections.sort(Building);

        boolean isAtTop = false;

        // stream to check if there are people still waiting
        boolean ArePeopleWaiting = Building.stream().anyMatch(lift_floor::getPeopleWaiting);

        while (ArePeopleWaiting) {
            if (direction == LiftMovement.UP) {
                for (current_floor = 0; current_floor < floors; current_floor++) {
                    System.out.println("Current floor is " + current_floor);
                    System.out.println(Building.get(current_floor).getPopulation().size() + " people waiting on this floor");
                    if (Building.get(current_floor).getPeopleWaiting()) {
                        final Iterator<person> iterator = Building.get(current_floor).getPopulation().iterator();
                        while (iterator.hasNext()) {
                            final person person = iterator.next();
                            if (person.getWeight() + elevator.getCurrent_load() > max_load) {
                                break;
                            }
                            else {
                                stops.add(person.getDestination_floor());
                                elevator.setCurrent_load(person.getWeight() + elevator.getCurrent_load());
                                iterator.remove();
                            }
                        }
                        List<Integer> listWithoutDuplicates = stops.stream().distinct().collect(Collectors.toList());
                        elevator.setStops(listWithoutDuplicates);
                        System.out.println("Current planned stops are: " + elevator.getStops());
                        System.out.println("Passenger(s) picked up on floor " + current_floor);
                        TimeUnit.SECONDS.sleep(2);
                    }
                    if (Building.get(current_floor).getPopulation().isEmpty()) {
                        Building.get(current_floor).setPeopleWaiting(false);
                    }
                    LiftDropOffLoop(Building);
                    direction = LiftMovement.IDLE;
                    isAtTop = true;
                }
            }
            if (direction == LiftMovement.IDLE && isAtTop)
                direction = LiftMovement.DOWN;
            if (direction == LiftMovement.DOWN){
                for (current_floor = (floors - 1); current_floor > -1; current_floor--){
                    System.out.println("Current floor is " + current_floor);
                    if (Building.get(current_floor).getPeopleWaiting()) {
                        final Iterator<person> iterator = Building.get(current_floor).getPopulation().iterator();
                        while (iterator.hasNext()) {
                            final person person = iterator.next();
                            if (person.getWeight() + elevator.getCurrent_load() > max_load) {
                                break;
                            }
                            else {
                                stops.add(person.getDestination_floor());
                                elevator.setCurrent_load(person.getWeight() + elevator.getCurrent_load());
                                iterator.remove();
                            }
                        }
                        List<Integer> listWithoutDuplicates = stops.stream().distinct().collect(Collectors.toList());
                        elevator.setStops(listWithoutDuplicates);
                        System.out.println("Current planned stops are: " + elevator.getStops());
                        System.out.println("Passenger(s) picked up on floor " + current_floor);
                        TimeUnit.SECONDS.sleep(2);
                        if (Building.get(current_floor).getPopulation().isEmpty()) {
                            Building.get(current_floor).setPeopleWaiting(false);
                        }
                    }
                    LiftDropOffLoop(Building);
                }
            }
            if (direction == LiftMovement.IDLE && !isAtTop)
                direction = LiftMovement.UP;
            ArePeopleWaiting = Building.stream().anyMatch(lift_floor::getPeopleWaiting);
        }
    }

    /*public static void MoveLift(List<lift_floor> building) throws InterruptedException {

        if(current_floor == building.size() - 1){
            direction = LiftMovement.DOWN;
            return;
        }
        if (building.get(current_floor).getPeopleWaiting()) {
            final Iterator<person> iterator = building.get(current_floor).getPopulation().iterator();
            while (iterator.hasNext()) {
                final person person = iterator.next();
                if (person.getWeight() + elevator.getCurrent_load() > max_load) {
                    current_floor++;
                    break;
                }
                stops.add(person.getDestination_floor());
                elevator.setCurrent_load(person.getWeight() + elevator.getCurrent_load());
                iterator.remove();
            }
            List<Integer> listWithoutDuplicates = stops.stream().distinct().collect(Collectors.toList());
            elevator.setStops(listWithoutDuplicates);
            //System.out.println("Current planned stops are: " + elevator.getStops());
            //System.out.println("Passenger(s) picked up on floor " + current_floor);
            TimeUnit.SECONDS.sleep(2);
            if (building.get(current_floor).getPopulation().isEmpty()) {
                building.get(current_floor).setPeopleWaiting(false);
            }
        }
    }*/

    public static void LiftDropOffLoop(List<lift_floor> Building) {

        for (int i = 0; i < elevator.getStops().size(); i++) {
            if (elevator.getStops().get(i).equals(current_floor)) {
                final Iterator<person> iterator = elevator.getPeople().iterator();
                while (iterator.hasNext()) {
                    final person person = iterator.next();
                    if(person.getDestination_floor() == current_floor){
                        elevator.setCurrent_load(elevator.getCurrent_load() - person.getWeight());
                        iterator.remove();
                    }
                }
                System.out.println("Passenger(s) dropped off on floor " + current_floor);
                elevator.getStops().remove(elevator.getStops().get(i));
                System.out.println(elevator.getStops());
            }
        }
    }
}
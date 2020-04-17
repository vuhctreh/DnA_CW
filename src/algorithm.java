import java.util.*;
import java.util.concurrent.TimeUnit;

//TODO make elevator pick people up on the way down too

public class algorithm {
    public static LiftMovement direction;
    public static int floors = 6;
    public static int current_floor = 0;
    public static int max_load = 500;

    static List<Integer> stops = new ArrayList<>();
    static List<person> people_in_elevator = new ArrayList<>();
    static lift elevator = new lift(direction, stops, "Idle", 0, people_in_elevator);

    public static void main(String[] args) throws InterruptedException {

        List<lift_floor> Building = Generators.Population_Generator(floors);

        Collections.sort(Building);

        boolean isAtTop = false;

        // stream to check if there are people still waiting
        boolean ArePeopleWaiting = Building.stream().anyMatch(lift_floor::getPeopleWaiting);

        while (ArePeopleWaiting) {
            for (current_floor = 0; current_floor < floors; current_floor++) {
                System.out.println("Lift is going up; current floor is " + current_floor);
                if (Building.get(current_floor).getPeopleWaiting() && Building.get(current_floor).getPopulation().size() > 0) {
                    for (int i = 0; i < Building.get(current_floor).getPopulation().size(); i++) {
                        System.out.println("There are " + Building.get(current_floor).getPopulation().size() + " people waiting on floor " + current_floor);
                        if (elevator.getCurrent_load() > max_load) {
                            current_floor++;
                            break;
                        } else {
                            people_in_elevator.add(Building.get(current_floor).getPopulation().get(i));
                            stops.add(Building.get(current_floor).getPopulation().get(i).getDestination_floor());
                            elevator.setCurrent_load(elevator.getCurrent_load() + Building.get(current_floor).getPopulation().get(i).getWeight());
                            Building.get(current_floor).getPopulation().remove(Building.get(current_floor).getPopulation().get(i));
                            System.out.println("Current elevator load = " + elevator.getCurrent_load());
                            if (Building.get(current_floor).getPopulation().size() == 0) {
                                Building.get(current_floor).setPeopleWaiting(false);
                            }
                        }
                    }
                    elevator.setStops(stops);
                    System.out.println("Current planned stops are: " + elevator.getStops());
                    System.out.println("Passenger(s) picked up on floor " + current_floor);
                    TimeUnit.SECONDS.sleep(2);
                    if (Building.get(current_floor).getPopulation().size() == 0) {
                        Building.get(current_floor).setPeopleWaiting(false);
                    }
                }
                LiftDropOffLoop();
                direction = LiftMovement.IDLE;
                isAtTop = true;
            }
            if (direction == LiftMovement.IDLE && isAtTop) {
                direction = LiftMovement.DOWN;
            }
            if (direction == LiftMovement.DOWN) {
                for (current_floor = (floors - 1); current_floor > -1; current_floor--) {
                    System.out.println("Lift is going down; current floor is " + current_floor);
                    LiftDropOffLoop();
                }
            } ArePeopleWaiting = Building.stream().anyMatch(lift_floor::getPeopleWaiting);
        }
    }

    public static void LiftDropOffLoop() {
        for (int i = 0; i < stops.size(); i++) {
            if (elevator.getStops().get(i).equals(current_floor)) {
                int y = 0;
                for (int x = 0; x < elevator.getPeople().size(); x++) {
                    if (elevator.getPeople().get(x).getDestination_floor() == current_floor) {
                        elevator.setCurrent_load(elevator.getCurrent_load() - elevator.getPeople().get(x).getWeight());
                        elevator.getPeople().remove(elevator.getPeople().get(x));
                        y++;
                    }
                }
                System.out.println(y + " Passenger(s) dropped off on floor " + current_floor);
                elevator.getStops().remove(elevator.getStops().get(i));
            }
        }
    }
}
import java.util.*;
import java.util.concurrent.TimeUnit;

//TODO rework elevator logic (make person object attached to elevator so I can control weight and adjust stops).

public class algorithm {
    public static LiftMovement direction;
    public static int floors = 6;
    public static int current_floor = 0;
    public static int max_load = 100;

    static List<Integer> stops = new ArrayList<>();
    static List<person> people_in_elevator = new ArrayList<>();
    static lift elevator = new lift(direction, stops, "Idle", 0, people_in_elevator);

    public static void main(String[]args) throws InterruptedException {

        List<lift_floor> Building = Generators.Population_Generator(floors);

        Collections.sort(Building);

        boolean isAtTop = false;
        for(current_floor = 0; current_floor < floors; current_floor++) {
            System.out.println("Lift is going up; current floor is " + current_floor);
            if(Building.get(current_floor).getPeopleWaiting() && Building.get(current_floor).getPopulation().size() > 0) {
                for(int i = 0; i < Building.get(current_floor).getPopulation().size(); i++) {
                    if(elevator.getCurrent_load() > max_load){
                        current_floor ++;
                        break;
                    }
                    else{
                        stops.add(Building.get(current_floor).getPopulation().get(i).getDestination_floor());
                        elevator.setCurrent_load(elevator.getCurrent_load() + Building.get(current_floor).getPopulation().get(i).getWeight());
                        System.out.println(elevator.getCurrent_load());
                    }
                }
                elevator.setStops(stops);
                System.out.println(elevator.getStops());
                System.out.println("Passenger(s) picked up on floor " + current_floor);
                TimeUnit.SECONDS.sleep(2);
                if(Building.get(current_floor).getPopulation().size() == 0){
                    Building.get(current_floor).setPeopleWaiting(false);
                }
            }
            for(int i = 0; i < stops.size(); i++){
                if(elevator.getStops().get(i).equals(current_floor)){
                    System.out.println("Passenger(s) dropped off on floor " + current_floor);
                    elevator.getStops().remove(elevator.getStops().get(i));
                }
            }
            direction = LiftMovement.IDLE;
            isAtTop = true;
        }
        if(direction == LiftMovement.IDLE && isAtTop){
            direction = LiftMovement.DOWN;
        }
        if(direction == LiftMovement.DOWN){
            for(current_floor = (floors - 1); current_floor > -1; current_floor--){
                System.out.println("Lift is going down; current floor is " + current_floor);
                for(int i = 0; i < stops.size(); i++) {
                    if (elevator.getStops().get(i).equals(current_floor)) {
                        System.out.println("Passenger(s) dropped off on floor " + current_floor);
                        elevator.getStops().remove(elevator.getStops().get(i));
                    }
                }
            }
        }
    }
}

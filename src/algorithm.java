import javax.swing.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class algorithm {
    public static LiftMovement direction;
    public static int floors = 6;
    public static int current_floor = 0;

    static List<Integer> stops = new ArrayList<>();
    static lift elevator = new lift(direction, stops, "Idle", 0);

    public static void main(String[]args) throws InterruptedException {

        List<lift_floor> Building = Generators.Population_Generator(floors);

        System.out.println(Building.get(0).getPeopleWaiting());

        Collections.sort(Building);

        boolean isAtTop = false;
        for(current_floor = 0; current_floor < floors; current_floor++) {
            System.out.println("Lift is going up; current floor is " + current_floor);
            if(Building.get(current_floor).getPeopleWaiting() && Building.get(current_floor).getPopulation().size() > 0) {
                stops.add(Building.get(current_floor).getPopulation().get(0).getDestination_floor());
                elevator.setStops(stops);
                System.out.println(elevator.getStops());
                System.out.println("Passenger picked up on floor " + current_floor);
                TimeUnit.SECONDS.sleep(2);
                Building.get(current_floor).setPeopleWaiting(false);
            }
            for(int i = 0; i < stops.size(); i++){
                if(elevator.getStops().get(i).equals(current_floor)){
                    System.out.println("Passenger(s) were dropped off on floor " + current_floor);
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
                        System.out.println("Passenger was dropped off on floor " + current_floor);
                        elevator.getStops().remove(elevator.getStops().get(i));
                    }
                }
            }
        }
    }
}

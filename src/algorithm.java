import javax.swing.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class algorithm {
    public static LiftMovement direction;
    public static int floors = 6;
    public static int current_floor = 0;

    static List<lift_floor> building = new ArrayList<>();
    static List<Integer> stops = new ArrayList<>();
    static lift elevator = new lift(direction, stops, "Idle");
    static person testy = new person(1, 3, "Waiting");
    static List<person> testList = new ArrayList<>();
    static List<person> testList2 = new ArrayList<>();
    static lift_floor floor1 = new lift_floor(1, true, testList);
    static lift_floor floor2 = new lift_floor(3, false, testList2);
    static lift_floor floor3 = new lift_floor(5, false, testList2);
    static lift_floor floor4 = new lift_floor(4, true, testList2);
    static lift_floor floor5 = new lift_floor(2, true, testList2);
    static lift_floor floor0 = new lift_floor(0, true, testList2);

    public static void main(String[]args) throws InterruptedException {
        direction = LiftMovement.UP;
        testList.add(testy);
        building.add(floor1);
        building.add(floor3);
        building.add(floor2);
        building.add(floor4);
        building.add(floor5);
        building.add(floor0);

        Collections.sort(building);

        boolean isAtTop = false;
        for(current_floor = 0; current_floor < floors; current_floor++) {
            System.out.println("Lift is going up; current floor is " + current_floor);
            for(int i = 0; i < stops.size(); i++){
                if(elevator.getStops().get(i).equals(current_floor)){
                    System.out.println("Passenger was dropped off on floor " + current_floor);
                    elevator.getStops().remove(elevator.getStops().get(i));
                }
            }
            if(building.get(current_floor).getPeopleWaiting()) {
                stops.add(building.get(current_floor).getPopulation().get(0).getDestination_floor());
                elevator.setStops(stops);
                System.out.println("Passenger picked up on floor " + current_floor);
                TimeUnit.SECONDS.sleep(2);
                building.get(current_floor).setPeopleWaiting(false);
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
            }
        }
    }
}

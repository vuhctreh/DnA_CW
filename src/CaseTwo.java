import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.lang.Math;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

//TODO weight handling
//TODO movement up and down
//TODO if floor is empty set people waiting to false.

/*Lift currently moves up to first stop, picks people up from then on until it reaches
* the highest stop in that direction (only people going in that same direction), then decides where to go next.
*/

public class CaseTwo {
    public static LiftMovement direction = LiftMovement.UP;
    public static int floors = 6;
    //public static int current_floor = 0;
    public static int max_load = 150;

    static List<Integer> stops = new ArrayList<>();
    static List<Person> people_in_elevator = new ArrayList<>();
    static Lift elevator = new Lift(LiftMovement.UP, stops, "Idle", 0, people_in_elevator);

    public static void main(String[] args) throws InterruptedException {

        List<LiftFloor> Building = Generators.Population_Generator(floors);

        System.out.println(Building.get(0).getPopulation().size());
        System.out.println(Building.get(1).getPopulation().size());
        System.out.println(Building.get(2).getPopulation().size());
        System.out.println(Building.get(3).getPopulation().size());
        System.out.println(Building.get(4).getPopulation().size());
        System.out.println(Building.get(5).getPopulation().size());


        Collections.sort(Building);

        boolean isAtTop = false;

        boolean ArePeopleWaiting = Building.stream()
                                            .anyMatch(LiftFloor::getPeopleWaiting);

        if (!ArePeopleWaiting){
            System.out.println("There is no one currently waiting for the lift.");
            return;
        }
        //while (ArePeopleWaiting) {
        LiftFloor FirstPassenger = Building.stream()
                                            .filter(LiftFloor::getPeopleWaiting)
                                            .findFirst()
                                            .get();
        int Current_Floor = 0;
        while(Current_Floor < FirstPassenger.getFloor()){
            System.out.println("Lift is going up. Current floor: " + Current_Floor);
            Current_Floor ++;
        }
        if (FirstPassenger.getFloor() == Current_Floor){
            elevator.setDirection(LiftMovement.IDLE);
            System.out.println("The first person waiting has been reached on floor " + Current_Floor);
        }
        if(FirstPassenger.getPopulation().get(0).getDestination_floor() > Current_Floor){
            elevator.setDirection(LiftMovement.UP);
        }
        else{
            elevator.setDirection(LiftMovement.DOWN);
        }
        int FirstStop = FirstPassenger.getPopulation().get(0).getDestination_floor();
        stops.add(FirstStop);
        elevator.setStops(stops);
        /*ùelevator.getPeople().add(FirstPassenger.getPopulation().get(0));
        System.out.println("Current people in elevator:" + elevator.getPeople());*/


        while(ArePeopleWaiting){
            stops = elevator.getStops();
            System.out.println("Loop started/restarted");
            System.out.println("Current floor: " + Current_Floor);
            System.out.println("Current people in elevator:" + elevator.getPeople());
            if (elevator.getDirection().equals(LiftMovement.UP)) {
                System.out.println("Elevator up instructions reached");
                CheckMax();
                //While current floor is less than maximum floor in stops does whatever needs to be done
               /* while (Current_Floor < CheckMax() + 1 && Building.get(Current_Floor).getPeopleWaiting()) {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("Max has been checked");
                    if (Building.get(Current_Floor).getPopulation().size() == 0) {
                        Building.get(Current_Floor).setPeopleWaiting(false);
                        System.out.println("Set people waiting to false on this floor.");
                        continue;
                    }
                    System.out.println("Current floor is " + Current_Floor +" (inside movement up loop)");
                    final Iterator<Person> IteratorBuilding = Building.get(Current_Floor).getPopulation().iterator();
                    //Picks people up (only those going up)
                    while (IteratorBuilding.hasNext()) {
                        final Person person = IteratorBuilding.next();
                        if (person.getDestination_floor() < Current_Floor) {
                            continue;
                        } else {
                            System.out.println("Someone picked up on floor" + Current_Floor);
                            stops.add(person.getDestination_floor());
                            elevator.getPeople().add(person);
                            IteratorBuilding.remove();
                            System.out.println("Current stops are" + elevator.getStops());
                        }
                    }
                    //Drops people off
                    final Iterator<Person> IteratorElevator = elevator.getPeople().iterator();
                    while (IteratorElevator.hasNext()) {
                        System.out.println("Reached drop off loop (direction up)");
                        final Person person = IteratorElevator.next();
                        if (person.getDestination_floor() == Current_Floor) {
                            System.out.println("Someone dropped off on floor" + Current_Floor);
                            IteratorElevator.remove();
                            for (int x = 0; x < stops.size(); x++) {
                                if (stops.get(x) == Current_Floor) {
                                    stops.remove(x);
                                }
                            }
                            elevator.setStops(stops);
                            System.out.println("Current stops are: " + elevator.getStops());
                        }
                    }
                    System.out.println("Reached floor increase");
                    Current_Floor++;
                    System.out.println("Current floor: " + Current_Floor);
                } */

                for(int floor = Current_Floor; floor < CheckMax() +1; floor++) {
                    if (floor > CheckMax())
                        break;
                    Current_Floor = floor;
                    System.out.println("Floor is: " + floor);
                    if (Building.get(floor).getPopulation().size() == 0) {
                        Building.get(floor).setPeopleWaiting(false);
                        System.out.println("Set people waiting to false on this floor.");
                    }
                    if (Building.get(floor).getPeopleWaiting()){
                        System.out.println("Floor has people waiting.");
                        final Iterator<Person> iterator = Building.get(floor).getPopulation().iterator();
                        while (iterator.hasNext()) {
                            final Person person = iterator.next();
                            if (person.getDestination_floor() > Current_Floor){
                                stops.add(person.getDestination_floor());
                                elevator.getPeople().add(person);
                                iterator.remove();
                                System.out.println("Someone was picked up");
                                System.out.println(elevator.getPeople());
                            }
                        }
                        elevator.setStops(stops);
                    }
                    System.out.println("reached drop off loop");
                    BaseCase.LiftDropOffLoop();
                    CheckMax();
                    stops = elevator.getStops();
                }

                //System.out.println(CheckMax());
                //System.out.println("Floor has been increased");

                //Once everyone has been picked up going up, checks the closest person.
                if (elevator.getStops().size() == 0) {
                    int ArbitraryFloor = Current_Floor;
                    int LowerBound = -1;
                    int UpperBound = -1;
                    //Checks closest person on the floors below the current floor.
                    while (ArbitraryFloor > -1) {
                        if (Building.get(ArbitraryFloor).getPeopleWaiting()) {
                            LowerBound = ArbitraryFloor;
                            break;
                        } else {
                            ArbitraryFloor--;
                        }
                        System.out.println("(Checking closest person below) ArbitraryFloor is currently" + ArbitraryFloor);
                    }
                    System.out.println("Closest person below determined");
                    //Checks the closest person on the floors above the current floor.
                    ArbitraryFloor = Current_Floor;
                    while (ArbitraryFloor < floors - 1) {
                        if (Building.get(ArbitraryFloor).getPeopleWaiting()) {
                            UpperBound = ArbitraryFloor;
                            break;
                        } else {
                            ArbitraryFloor++;
                        }
                        System.out.println("(Checking closest person above) ArbitraryFloor is currently" + ArbitraryFloor);
                    }
                    System.out.println("Closest person above determined");
                    //if there was no one above, lift will go down to closest floor with people.
                    if (UpperBound == -1 && LowerBound != -1) {
                        stops.add(LowerBound);
                        elevator.setDirection(LiftMovement.DOWN);
                        System.out.println("Direction has been determined; going down (no one above)");
                    }
                    //if there was no one below, list will go up to closest floor with people.
                    else if (LowerBound == -1 && UpperBound != -1) {
                        stops.add(UpperBound);
                        System.out.println("Direction has been determined; going up (no one below)");
                    }
                    //Checks if people were found both above and below the current floor.
                    else if (LowerBound != -1 && UpperBound != -1) {
                        /*
                         * if difference between upper bound and current floor is smaller than diff between lower bound
                         * and current floor, makes the elevator go to the Upper bound
                         */
                        if (Current_Floor - LowerBound > UpperBound - Current_Floor) {
                            stops.removeAll(stops);
                            stops.add(UpperBound);
                            elevator.setStops(stops);
                            System.out.println("Direction has been determined; going up (person above was closer)");
                        }
                        /*
                         * if difference between upper bound and current floor is greater than diff between lower bound
                         * and current floor, makes the elevator go to the lower bound
                         */
                        else if (Current_Floor - LowerBound < UpperBound - Current_Floor) {
                            stops.removeAll(stops);
                            stops.add(LowerBound);
                            elevator.setStops(stops);
                            elevator.setDirection(LiftMovement.DOWN);
                            System.out.println("Direction has been determined; going down (person below was closer)");
                            break;
                        }
                        // If the difference in stops is equal, randomly picks a direction.
                        else {
                            stops.removeAll(stops);
                            int Coin_Flip = (Math.random() <= 0.5) ? 1 : 2;
                            if (Coin_Flip == 1) {
                                stops.add(UpperBound);
                                elevator.setStops(stops);
                                System.out.println("Direction has been determined; going up (via coinflip)");
                            } else {
                                stops.add(LowerBound);
                                elevator.setStops(stops);
                                elevator.setDirection(LiftMovement.DOWN);
                                System.out.println("Direction has been determined; going down (via coinflip)");
                                break;
                            }
                        }
                    }

                    //if there was no one above and below, lift stops.
                    else if (LowerBound != -1 && UpperBound == -1) {
                        System.out.println("No passengers left on any floor.");
                        continue;
                    }
                }
            }
            else if (elevator.getDirection() == LiftMovement.DOWN) {
                System.out.println("Reached instructions for lift going down");
                /*while (Current_Floor >= CheckMin()) {
                    System.out.println("Entered downwards loop after CheckMin");
                    if (Building.get(Current_Floor).getPopulation().size() == 0 && Building.get(Current_Floor).getPeopleWaiting()) {
                        Building.get(Current_Floor).setPeopleWaiting(false);
                        System.out.println("Set people waiting on this floor to false");
                        continue;
                    }
                    System.out.println("Current floor is: " + Current_Floor);
                    final Iterator<Person> IteratorBuilding = Building.get(Current_Floor).getPopulation().iterator();
                    while (IteratorBuilding.hasNext()) {
                        final Person person = IteratorBuilding.next();
                        if (person.getDestination_floor() > Current_Floor) {
                            continue;
                        } else {
                            System.out.println("Someone picked up on floor" + Current_Floor);
                            stops.add(person.getDestination_floor());
                            elevator.getPeople().add(person);
                            IteratorBuilding.remove();
                        }
                    }
                    System.out.println("Reached dropping people off on the way down");
                    final Iterator<Person> IteratorElevator = elevator.getPeople().iterator();
                    while (IteratorElevator.hasNext()) {
                        final Person person = IteratorElevator.next();
                        if (person.getDestination_floor() == Current_Floor) {
                            System.out.println("Someone dropped off on floor" + Current_Floor);
                            IteratorElevator.remove();
                            for (int x = 0; x < stops.size(); x++) {
                                if (stops.get(x) == Current_Floor) {
                                    stops.remove(x);
                                }
                            }
                            elevator.setStops(stops);
                        }
                    }
                    System.out.println("Reached floor decrease");
                    Current_Floor--;


                }*/

                CheckMin();
                for (int floor = Current_Floor; floor >= CheckMin(); floor --){
                    if (floor < CheckMin())
                        break;
                 Current_Floor = floor;
                    System.out.println("Floor is: " + floor);
                    if (Building.get(floor).getPopulation().isEmpty()) {
                        Building.get(floor).setPeopleWaiting(false);
                        System.out.println("Set people waiting to false on this floor.");
                    }
                    if (Building.get(floor).getPeopleWaiting()){
                        System.out.println("Floor has people waiting.");
                        final Iterator<Person> iterator = Building.get(floor).getPopulation().iterator();
                        while (iterator.hasNext()) {
                            final Person person = iterator.next();
                            if (person.getDestination_floor() > Current_Floor){
                                stops.add(person.getDestination_floor());
                                elevator.getPeople().add(person);
                                iterator.remove();
                                System.out.println("Someone was picked up");
                                System.out.println(elevator.getPeople());
                            }
                        }
                        elevator.setStops(stops);
                    }
                    System.out.println("reached drop off loop");
                    BaseCase.LiftDropOffLoop();
                    System.out.println(elevator.getStops());
                    CheckMin();
                    stops = elevator.getStops();
                }
                //Once everyone has been picked up going up, checks the closest person.
                if (elevator.getStops().size() == 0) {
                    int ArbitraryFloor = Current_Floor;
                    int LowerBound = -1;
                    int UpperBound = -1;
                    //Checks closest person on the floors below the current floor.
                    while (ArbitraryFloor > -1) {
                        if (Building.get(ArbitraryFloor).getPeopleWaiting()) {
                            LowerBound = ArbitraryFloor;
                            break;
                        } else {
                            ArbitraryFloor--;
                        }
                    }
                    //Checks the closest person on the floros above the current floor.
                    ArbitraryFloor = Current_Floor;
                    while (ArbitraryFloor < floors) {
                        if (Building.get(ArbitraryFloor).getPeopleWaiting()) {
                            UpperBound = ArbitraryFloor;
                            break;
                        } else {
                            ArbitraryFloor++;
                        }
                    }
                    //if there was no one above, lift will go down to closest floor with people.
                    if (UpperBound == -1 && LowerBound != -1) {
                        stops.add(LowerBound);
                        elevator.setDirection(LiftMovement.DOWN);
                        break;
                    }
                    //if there was no one below, list will go up to closest floor with people.
                    else if (LowerBound == -1 && UpperBound != -1) {
                        stops.add(UpperBound);
                        break;
                    } else if (LowerBound == -1 && UpperBound == floors) {

                    }
                    //Checks if people were found both above and below the current floor.
                    else if (LowerBound != -1 && UpperBound != -1) {
                        /*
                         * if difference between upper bound and current floor is smaller than diff between lower bound
                         * and current floor, makes the elevator go to the Upper bound
                         */
                        if (Current_Floor - LowerBound > UpperBound - Current_Floor) {
                            stops.removeAll(stops);
                            stops.add(UpperBound);
                            elevator.setStops(stops);
                        }
                        /*
                         * if difference between upper bound and current floor is greater than diff between lower bound
                         * and current floor, makes the elevator go to the lower bound
                         */
                        else if (Current_Floor - LowerBound < UpperBound - Current_Floor) {
                            stops.removeAll(stops);
                            stops.add(LowerBound);
                            elevator.setStops(stops);
                            elevator.setDirection(LiftMovement.DOWN);
                            break;
                        }
                        // If the difference in stops is equal, randomly picks a direction.
                        else {
                            stops.removeAll(stops);
                            int Coin_Flip = (Math.random() <= 0.5) ? 1 : 2;
                            if (Coin_Flip == 1) {
                                stops.add(UpperBound);
                                elevator.setStops(stops);
                            } else {
                                stops.add(LowerBound);
                                elevator.setStops(stops);
                                elevator.setDirection(LiftMovement.DOWN);
                                break;
                            }
                        }
                    }

                    //if there was no one above and below, lift stops.
                    else if (LowerBound != -1 && UpperBound == -1) {
                        System.out.println("No passengers left on any floor.");
                        continue;
                    }
                }
            }
            ArePeopleWaiting = Building.stream()
                    .anyMatch(LiftFloor::getPeopleWaiting);
            System.out.println(stops);
            TimeUnit.SECONDS.sleep(5);
        }






    }
    public static int CheckMax(){
        return stops
                .stream()
                .mapToInt(v -> v)
                .max().orElse(floors +1);
    }

    public static int CheckMin(){
        return stops
                .stream()
                .mapToInt(v -> v)
                .min().orElse(-1);
    }

}


import ImprovedLift.Movement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.concurrent.TimeUnit;

public class ImprovedCase {

    public static int floors = 6;
    public static int max_load = 150;
    public static int current_floor = 0;
    public static int TotalTimeInElevator = 0;
    public static int TotalPeopleInElevator = 0;

    static List<Integer> stops = new ArrayList<>();
    static List<Person> people_in_elevator = new ArrayList<>();
    static Lift elevator = new Lift(LiftMovement.UP, stops, "Idle", 0, people_in_elevator);
    static List<LiftFloor> Building = Generators.Population_Generator(floors);

    public static void main(String[] args) throws InterruptedException {

        IntStream.range(0, Building.size()).forEach(i -> TotalPeopleInElevator+= Building.get(i).getPopulation().size());

        System.out.println("-----------------------------------------");
        IntStream.range(0, Building.size()).forEach(i -> System.out.println(Building.get(i).getPopulation().size()));
        System.out.println("-----------------------------------------");


        boolean ArePeopleWaiting = Building.stream()
                                            .anyMatch(LiftFloor::getPeopleWaiting);

        if (!ArePeopleWaiting){
            System.out.println("There is no one currently waiting for the lift.");
            return;
        }

        else{
            LiftFloor FirstPassenger = Building.stream()
                    .filter(LiftFloor::getPeopleWaiting)
                    .findFirst()
                    .get();
            IntStream.range(0, FirstPassenger.getFloor()).forEach(i -> move(Movement.UP));
            int Decider = FirstPassenger.getPopulation()
                                        .get(0)
                                        .getDestination_floor();
            if(Decider < current_floor)
                move(Movement.DOWN);
            else
                move(Movement.UP);

            while (ArePeopleWaiting || elevator.getPeople().size()>0){
                System.out.println(current_floor);
                System.out.println(elevator.getPeople());
                if (CheckMin() > current_floor){
                    move(Movement.UP);
                }
                else if (CheckMin() < 0){
                    int LowerBound = -1;
                    int UpperBound = -1;
                    System.out.println("The min is undefined.");
                    for (int ArbitraryFloor = current_floor; ArbitraryFloor>-1; ArbitraryFloor--)
                        if (Building.get(ArbitraryFloor).getPeopleWaiting()) {
                            LowerBound = ArbitraryFloor;
                            break;
                        }
                    for (int ArbitraryFloor = current_floor; ArbitraryFloor<Building.size(); ArbitraryFloor++)
                        if (Building.get(ArbitraryFloor).getPeopleWaiting()) {
                            UpperBound = ArbitraryFloor;
                            break;
                        }
                    System.out.println(LowerBound);
                    System.out.println(UpperBound);

                    if (UpperBound == current_floor || LowerBound == current_floor){
                        if (Building.get(current_floor).getPopulation().get(0).getDestination_floor() > current_floor)
                            move(Movement.UP);
                        else
                            move(Movement.DOWN);
                    }
                    TimeUnit.SECONDS.sleep(2);
                    if(UpperBound == -1){
                        while(current_floor> LowerBound){
                            move(Movement.DOWN);
                            System.out.println("Finding closest person, moving down.");
                        }
                    }
                    if (LowerBound == -1){
                        while(current_floor<UpperBound){
                            move(Movement.UP);
                            System.out.println("Finding closest person, moving up.");
                        }
                    }
                    if(LowerBound != -1 && UpperBound != -1){
                        int DiffTop = UpperBound - current_floor;
                        int DiffBot = current_floor - LowerBound;
                        if(DiffBot >DiffTop){
                            while (current_floor<UpperBound)
                                move(Movement.UP);
                        }
                        else if (DiffBot < DiffTop){
                            while (current_floor > LowerBound)
                                move(Movement.DOWN);
                        }
                        else {
                            Random r = new Random();
                            int i = r.nextInt()%2;
                            switch (i){
                                case 0:
                                    while (current_floor > LowerBound)
                                        move(Movement.DOWN);
                                case 1:
                                    while (current_floor<UpperBound)
                                        move(Movement.UP);
                                default:
                                    break;
                            }
                        }
                    }
                }
                else if (CheckMin() < current_floor){
                    move(Movement.DOWN);
                }
                else if (CheckMin() == current_floor){
                    move(Movement.DOWN);
                }
                System.out.println("-----------------------------------------");
                System.out.println(current_floor);
                System.out.println("People in elevator: " + elevator.getPeople());
                System.out.println("The highest planned stop is: " + CheckMax());
                System.out.println("The lowest planned stop is: " + CheckMin());
                System.out.println("-----------------------------------------");
                Building.forEach(i -> {
                            if (i.getPopulation().size() == 0)
                                i.setPeopleWaiting(false);
                        });
                IntStream.range(0, Building.size()).forEach(i -> System.out.println(Building.get(i).getPopulation().size()));
                System.out.println("-----------------------------------------");
                TimeUnit.SECONDS.sleep(2);
                if (Building.stream().noneMatch(LiftFloor::getPeopleWaiting))
                    ArePeopleWaiting = false;
            }
        }
        System.out.println("There were " + TotalPeopleInElevator + " total people waiting for the elevator.");
        System.out.println("People were in the elevator for a cumulative total of " + TotalTimeInElevator + " floors.");
        System.out.println("The average time spent in the elevator is: " + TotalTimeInElevator/TotalPeopleInElevator + ". There were: " + floors + " floors.");
        System.out.println("-----------------------------------------");
    }

    public static void move (Movement movement){
        int nextFloor = current_floor + movement.getListOperation();
        if(nextFloor < 0)
            nextFloor = 0;
        else if(nextFloor > Building.size() - 1)
            nextFloor = Building.size() - 1;

        //TODO weight calculations here

        final Iterator<Person> iterator = elevator.getPeople().iterator();
        while(iterator.hasNext()){
            final Person person = iterator.next();
            int diff = person.getDestination_floor() - nextFloor;
            if(diff == 0){
                TotalTimeInElevator += person.getTime_in_elevator();
                iterator.remove();
            }
        }

        final Iterator<Person> BuildingIterator = Building.get(current_floor).getPopulation().iterator();
        while(BuildingIterator.hasNext()){
            final Person PersonWaiting = BuildingIterator.next();
            if (PersonWaiting.getDestination_floor() - nextFloor > 0 && movement == Movement.UP){
                elevator.getPeople().add(PersonWaiting);
                BuildingIterator.remove();
            }
            else if(PersonWaiting.getDestination_floor() - nextFloor < 0 && movement == Movement.DOWN){
                elevator.getPeople().add(PersonWaiting);
                BuildingIterator.remove();
            }
            else if(nextFloor == floors-1){
                elevator.getPeople().add(PersonWaiting);
                BuildingIterator.remove();
            }
            else if(nextFloor == 0){
                elevator.getPeople().add(PersonWaiting);
                BuildingIterator.remove();
            }
            else if(elevator.getPeople().size() == 0){
                elevator.getPeople().add(PersonWaiting);
                BuildingIterator.remove();
            }
        }
        elevator.getPeople().forEach(i -> i.setTime_in_elevator(i.getTime_in_elevator() + 1));

        current_floor = nextFloor;
    }

    public static int CheckMin(){
        List<Integer> Comparator = new ArrayList<>();
        for (Person person : elevator.getPeople()) {
            Comparator.add(person.getDestination_floor());
        }

        return Comparator.stream()
                .mapToInt(v -> v)
                .min().orElse(-1);
    }

    public static int CheckMax(){
        List<Integer> Comparator = new ArrayList<>();
        for (Person person : elevator.getPeople()) {
            Comparator.add(person.getDestination_floor());
        }

        return Comparator.stream()
                .mapToInt(v -> v)
                .max().orElse(-1);
    }
}

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generators {

   public static List<LiftFloor> Population_Generator(int total_Floors){
       List<LiftFloor> building = new ArrayList<>();
       for(int i = 0; i < total_Floors; i++){
           Random rand = new Random();
           int coin_flip = rand.nextInt(3);
           int numberOfPeople = rand.nextInt(2) + 1;
           List<Person> people = new ArrayList<>();
           if(coin_flip < 1){
               for(int x = 0; x < numberOfPeople; x ++){
                   int destination_floor = rand.nextInt(total_Floors - 1);
                   int weight = rand.nextInt(100-30) + 30;
                   Person additional_Person = new Person(i, destination_floor, "waiting", weight);
                   people.add(additional_Person);
               }
               LiftFloor floor = new LiftFloor(i, true, people);
               building.add(floor);
           }
           else{
               LiftFloor floor = new LiftFloor(0, false, people);
               building.add(floor);
           }
       }
       return building;
   }
}

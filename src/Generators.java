import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generators {

   public static List<lift_floor> Population_Generator(int total_Floors, int current_floor){
       List<lift_floor> building = new ArrayList<>();
       for(int i = 0; i < total_Floors; i++){
           Random rand = new Random();
           int coin_flip = rand.nextInt(1);
           int numberOfPeople = rand.nextInt(5) + 1;
           List<person> people = new ArrayList<>();
           if(coin_flip == 1){
               for(int x = 0; x < numberOfPeople; x ++){
                   // String person_name = "Person" + x;
                   int destination_floor = rand.nextInt(total_Floors - 1);
                   person additional_Person = new person(i, destination_floor, "waiting");
                   people.add(additional_Person);
               }
               lift_floor floor = new lift_floor(i, true, people);
               building.add(floor);
           }
           else{
               lift_floor floor = new lift_floor (0, false, people);
               building.add(floor);
           }
       }
       return building;
   }
}

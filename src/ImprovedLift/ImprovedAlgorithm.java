package ImprovedLift;

public class ImprovedAlgorithm {
    public static void main(String[] args) {
        final Lift lift = new Lift(
                100,
                new Floor("Hub")
                        .addPerson(new Person(1, 20))
                        .addPerson(new Person(2, 50)),
                new Floor("ayo pierre"),
                new Floor("Floor 1"),
                new Floor("Bruh")
        );
        lift.move(Movement.UP);
        System.out.println(lift);
        lift.move(Movement.UP);
        System.out.println(lift);
        lift.move(Movement.UP);
        System.out.println(lift);
    }

}

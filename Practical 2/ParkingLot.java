
class ParkingLot {
    private int twoWheelers = 0;
    private int fourWheelers = 0;

    private final int twoCap;
    private final int fourCap;

    private static long revenue = 0;

    ParkingLot(int twoCap, int fourCap) {
        this.twoCap = twoCap;
        this.fourCap = fourCap;
    }

    void park(String type) {
        if (type.equals("two")) {
            if (twoWheelers < twoCap) {
                twoWheelers++;
                revenue += 20;
                System.out.println("Two-wheeler Parked");
            } else {
                System.out.println("Two-wheeler Section Full");
            }
        } else if (type.equals("four")) {
            if (fourWheelers < fourCap) {
                fourWheelers++;
                revenue += 40;
                System.out.println("Four-wheeler Parked");
            } else {
                System.out.println("Four-wheeler Section Full");
            }
        }
    }

    void leave(String type) {
        if (type.equals("two") && twoWheelers > 0) {
            twoWheelers--;
            System.out.println("Two-wheeler Left");
        } else if (type.equals("four") && fourWheelers > 0) {
            fourWheelers--;
            System.out.println("Four-wheeler Left");
        }
    }

    void display() {
        System.out.println("Two-Wheelers: " + twoWheelers);
        System.out.println("Four-Wheelers: " + fourWheelers);
    }

    static long getRevenue() {
        return revenue;
    }
}

public class Main {
    public static void main(String[] args) {

        ParkingLot p = new ParkingLot(2, 2);

        p.park("two");
        p.park("two");
        p.park("two");

        p.park("four");
        p.park("four");
        p.park("four");  

        p.leave("two");
        p.leave("four");

        System.out.println("\nFinal Occupancy:");
        p.display();

        System.out.println("Revenue = " + ParkingLot.getRevenue());
    }
}
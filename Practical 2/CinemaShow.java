class CinemaShow {
    private String title;
    private int seatsAvailable;
    private final int capacity;
    private static int totalBooked = 0;

    CinemaShow(String title, int capacity) {
        this.title = title;
        this.capacity = capacity;
        seatsAvailable = capacity;
    }

    CinemaShow(String title) {
        this(title, 100);
    }

    boolean book(int n) {
        if (n <= seatsAvailable) {
            seatsAvailable -= n;
            totalBooked += n;
            return true;
        }
        return false;
    }

    void cancel(int n) {
        seatsAvailable += n;
        if (seatsAvailable > capacity) {
            seatsAvailable = capacity;
        }
    }

    int getSeatsAvailable() {
        return seatsAvailable;
    }

    static int getTotalBooked() {
        return totalBooked;
    }
}

public class Main {
    public static void main(String[] args) {

        CinemaShow show = new CinemaShow("Movie", 50);

        System.out.println(show.book(20));
        System.out.println("Seats Left: " + show.getSeatsAvailable());

        System.out.println(show.book(15));
        System.out.println("Seats Left: " + show.getSeatsAvailable());

        System.out.println(show.book(20)); // Not enough seats
        System.out.println("Seats Left: " + show.getSeatsAvailable());

        show.cancel(10);
        System.out.println("After Cancel: " + show.getSeatsAvailable());

        System.out.println(show.book(15));
        System.out.println("Seats Left: " + show.getSeatsAvailable());

        System.out.println("Total Booked: " + CinemaShow.getTotalBooked());
    }
}
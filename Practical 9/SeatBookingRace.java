import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;

class SeatBooking {
    private int seatsLeft;

    SeatBooking(int seats) {
        seatsLeft = seats;
    }

    public boolean bookWithoutSync(CyclicBarrier barrier)
            throws InterruptedException, BrokenBarrierException {

        if (seatsLeft > 0) {
            barrier.await();

            seatsLeft--;
            return true;
        }

        return false;
    }

    public synchronized boolean book() {
        if (seatsLeft > 0) {
            seatsLeft--;
            return true;
        }

        return false;
    }

    public synchronized int getSeatsLeft() {
        return seatsLeft;
    }
}

class BookingTask implements Runnable {
    private SeatBooking booking;
    private boolean safe;
    private CyclicBarrier barrier;
    private boolean booked = false;

    BookingTask(SeatBooking booking, boolean safe,
                CyclicBarrier barrier) {
        this.booking = booking;
        this.safe = safe;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            if (safe) {
                booked = booking.book();
            } else {
                booked = booking.bookWithoutSync(barrier);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (BrokenBarrierException e) {
            System.out.println("Barrier error occurred.");
        }
    }

    public boolean wasBooked() {
        return booked;
    }
}

public class SeatBookingRace {
    public static void main(String[] args) throws InterruptedException {
        int totalSeats = 5;
        int totalThreads = 10;

        System.out.println("=== Without Synchronization ===");

        SeatBooking booking1 = new SeatBooking(totalSeats);
        CyclicBarrier barrier = new CyclicBarrier(totalThreads);

        Thread[] threads1 = new Thread[totalThreads];
        BookingTask[] tasks1 = new BookingTask[totalThreads];

        for (int i = 0; i < totalThreads; i++) {
            tasks1[i] = new BookingTask(booking1, false, barrier);
            threads1[i] = new Thread(tasks1[i]);
            threads1[i].start();
        }

        int successful1 = 0;

        for (int i = 0; i < totalThreads; i++) {
            threads1[i].join();

            if (tasks1[i].wasBooked()) {
                successful1++;
            }
        }

        System.out.println("Available seats: " + totalSeats);
        System.out.println("Successful bookings: " + successful1);

        if (successful1 > totalSeats) {
            System.out.println("Overselling occurred!");
        }

        System.out.println("\n=== With Synchronization ===");

        SeatBooking booking2 = new SeatBooking(totalSeats);

        Thread[] threads2 = new Thread[totalThreads];
        BookingTask[] tasks2 = new BookingTask[totalThreads];

        for (int i = 0; i < totalThreads; i++) {
            tasks2[i] = new BookingTask(booking2, true, null);
            threads2[i] = new Thread(tasks2[i]);
            threads2[i].start();
        }

        int successful2 = 0;

        for (int i = 0; i < totalThreads; i++) {
            threads2[i].join();

            if (tasks2[i].wasBooked()) {
                successful2++;
            }
        }

        System.out.println("Available seats: " + totalSeats);
        System.out.println("Successful bookings: " + successful2);
        System.out.println("Failed bookings: "
                + (totalThreads - successful2));
        System.out.println("Seats remaining: "
                + booking2.getSeatsLeft());
    }
}
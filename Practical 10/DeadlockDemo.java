import java.util.concurrent.CountDownLatch;

class DeadlockTask implements Runnable {
    private Object firstLock;
    private Object secondLock;
    private String firstName;
    private String secondName;
    private CountDownLatch ready;
    private CountDownLatch start;

    DeadlockTask(String firstName, String secondName,
                 Object firstLock, Object secondLock,
                 CountDownLatch ready, CountDownLatch start) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.firstLock = firstLock;
        this.secondLock = secondLock;
        this.ready = ready;
        this.start = start;
    }

    @Override
    public void run() {
        synchronized (firstLock) {
            System.out.println(Thread.currentThread().getName()
                    + " locked " + firstName);

            ready.countDown();

            try {
                start.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            synchronized (secondLock) {
                System.out.println(Thread.currentThread().getName()
                        + " locked " + secondName);
            }
        }
    }
}

// Fixed version: always acquire Lock A before Lock B
class SafeTask implements Runnable {
    private Object lockA;
    private Object lockB;

    SafeTask(Object lockA, Object lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    @Override
    public void run() {
        synchronized (lockA) {
            System.out.println(Thread.currentThread().getName()
                    + " locked Lock A");

            synchronized (lockB) {
                System.out.println(Thread.currentThread().getName()
                        + " locked Lock B");
            }
        }

        System.out.println(Thread.currentThread().getName()
                + " completed work.");
    }
}

public class DeadlockDemo {
    public static void main(String[] args) throws InterruptedException {

        Object lockA = new Object();
        Object lockB = new Object();

        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        System.out.println("=== Deadlock Demonstration ===");

        Thread t1 = new Thread(
                new DeadlockTask("Lock A", "Lock B",
                        lockA, lockB, ready, start), "Thread 1");

        Thread t2 = new Thread(
                new DeadlockTask("Lock B", "Lock A",
                        lockB, lockA, ready, start), "Thread 2");

        // Daemon threads allow the program to exit after the demo
        t1.setDaemon(true);
        t2.setDaemon(true);

        t1.start();
        t2.start();

        // Wait until both threads hold their first lock
        ready.await();

        System.out.println("Both threads hold one lock.");
        start.countDown();

        System.out.println("Deadlock occurs: both threads wait "
                + "for the other's lock.");

        // Use NEW lock objects for the fixed version
        System.out.println("\n=== Fixed Version ===");

        Object safeLockA = new Object();
        Object safeLockB = new Object();

        Thread t3 = new Thread(
                new SafeTask(safeLockA, safeLockB), "Safe Thread 1");

        Thread t4 = new Thread(
                new SafeTask(safeLockA, safeLockB), "Safe Thread 2");

        t3.start();
        t4.start();

        t3.join();
        t4.join();

        System.out.println("Deadlock avoided successfully.");
    }
}
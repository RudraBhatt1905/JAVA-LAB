class Counter {
    int count = 0;

    public void incrementUnsafe() {
        count++;
    }

    public synchronized void incrementSafe() {
        count++;
    }

    public int getCount() {
        return count;
    }
}

class CounterTask implements Runnable {
    private Counter counter;
    private int times;
    private boolean safe;

    CounterTask(Counter counter, int times, boolean safe) {
        this.counter = counter;
        this.times = times;
        this.safe = safe;
    }

    @Override
    public void run() {
        for (int i = 0; i < times; i++) {
            if (safe) {
                counter.incrementSafe();
            } else {
                counter.incrementUnsafe();
            }
        }
    }
}

public class CounterRace {
    public static void main(String[] args) throws InterruptedException {
        int numberOfThreads = 10;
        int incrementsPerThread = 100000;

        int expected = numberOfThreads * incrementsPerThread;

        Counter counter1 = new Counter();
        Thread[] threads1 = new Thread[numberOfThreads];

        System.out.println("=== Without Synchronization ===");

        for (int i = 0; i < numberOfThreads; i++) {
            threads1[i] = new Thread(
                    new CounterTask(counter1, incrementsPerThread, false)
            );
            threads1[i].start();
        }

        for (int i = 0; i < numberOfThreads; i++) {
            threads1[i].join();
        }

        System.out.println("Expected count: " + expected);
        System.out.println("Actual count:   " + counter1.getCount());

        Counter counter2 = new Counter();
        Thread[] threads2 = new Thread[numberOfThreads];

        System.out.println("\n=== With Synchronization ===");

        for (int i = 0; i < numberOfThreads; i++) {
            threads2[i] = new Thread(
                    new CounterTask(counter2, incrementsPerThread, true)
            );
            threads2[i].start();
        }

        for (int i = 0; i < numberOfThreads; i++) {
            threads2[i].join();
        }

        System.out.println("Expected count: " + expected);
        System.out.println("Actual count:   " + counter2.getCount());
    }
}
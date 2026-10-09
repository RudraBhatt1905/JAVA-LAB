import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class SharedTotal {

    static long sharedTotal = 0;
    static final Object lock = new Object();
    static AtomicLong atomicTotal = new AtomicLong(0);

    static class SumTask implements Runnable {
        long[] numbers;
        int start, end, method;
        CountDownLatch startGate;

        SumTask(long[] numbers, int start, int end,
                int method, CountDownLatch startGate) {
            this.numbers = numbers;
            this.start = start;
            this.end = end;
            this.method = method;
            this.startGate = startGate;
        }

        public void run() {
            try {
                startGate.await();

                for (int i = start; i < end; i++) {

                    if (method == 1) {
                        // Fix 1: synchronized
                        synchronized (lock) {
                            sharedTotal += numbers[i];
                        }
                    } else if (method == 2) {
                        // Fix 2: AtomicLong
                        atomicTotal.addAndGet(numbers[i]);
                    } else {
                        // No synchronization: race condition
                        sharedTotal += numbers[i];
                    }
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static void runTest(String name, int method,
                        long[] numbers, int threadCount)
            throws InterruptedException {

        sharedTotal = 0;
        atomicTotal.set(0);

        Thread[] threads = new Thread[threadCount];
        CountDownLatch startGate = new CountDownLatch(1);

        // Divide the array among the threads
        for (int i = 0; i < threadCount; i++) {
            int start = i * numbers.length / threadCount;
            int end = (i + 1) * numbers.length / threadCount;

            threads[i] = new Thread(
                new SumTask(numbers, start, end,
                            method, startGate)
            );

            threads[i].start();
        }

        // Measure execution time
        long startTime = System.nanoTime();

        startGate.countDown();

        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
        }

        long endTime = System.nanoTime();

        long actualTotal;

        if (method == 2) {
            actualTotal = atomicTotal.get();
        } else {
            actualTotal = sharedTotal;
        }

        long expectedTotal = numbers.length;
        double timeMs = (endTime - startTime) / 1_000_000.0;

        System.out.println("\n" + name);
        System.out.println("Expected total: " + expectedTotal);
        System.out.println("Actual total:   " + actualTotal);
        System.out.printf("Execution time: %.3f ms%n", timeMs);

        if (actualTotal == expectedTotal) {
            System.out.println("Result: Correct");
        } else {
            System.out.println("Result: Incorrect - race condition!");
        }
    }

    public static void main(String[] args)
            throws InterruptedException {

        int size = 1_000_000;
        int threadCount = 8;

        long[] numbers = new long[size];

        // Fill the array with 1
        Arrays.fill(numbers, 1L);

        System.out.println("Array size: " + size);
        System.out.println("Number of threads: " + threadCount);

        runTest("1. Without Synchronization",
                0, numbers, threadCount);

        runTest("2. With synchronized",
                1, numbers, threadCount);

        runTest("3. With AtomicLong",
                2, numbers, threadCount);
    }
}
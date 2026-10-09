import java.util.concurrent.*;

class MyTask implements Runnable {
    private int id;

    MyTask(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            System.out.println("Task " + id + " Started By "
                    + Thread.currentThread().getName());

            Thread.sleep(2000);

        } catch (InterruptedException e) {
            System.out.println("Task " + id + " interrupted");
            Thread.currentThread().interrupt();
        }
    }
}

public class ThreadPoolTask {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= 10; i++) {
            executor.submit(new MyTask(i));
        }

        executor.shutdown();

        try {
            if (executor.awaitTermination(30, TimeUnit.SECONDS)) {
                System.out.println("All tasks completed.");
            } else {
                System.out.println("Some tasks are still running.");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
class Buffer {
    private int item;
    private boolean available = false;

    public synchronized void put(int value) throws InterruptedException {
        while (available) {
            wait();
        }

        item = value;
        available = true;

        System.out.println("Produced: " + value);

        notify();
    }

    public synchronized int get() throws InterruptedException {
        while (!available) {
            wait();
        }

        int value = item;
        available = false;

        System.out.println("Consumed: " + value);

        notify();

        return value;
    }
}

class Producer implements Runnable {
    private Buffer buffer;

    Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 10; i++) {
                buffer.put(i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumer implements Runnable {
    private Buffer buffer;

    Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 10; i++) {
                buffer.get();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class ProducerConsumer {
    public static void main(String[] args) {
        Buffer buffer = new Buffer();

        Thread producer = new Thread(new Producer(buffer));
        Thread consumer = new Thread(new Consumer(buffer));

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("All items produced and consumed.");
    }
}
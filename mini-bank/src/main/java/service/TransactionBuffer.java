package service;

/** Practical 10: bounded buffer shared by a producer and a consumer, coordinated with wait() / notifyAll(). */
public class TransactionBuffer {
    private final String[] items;
    private int count;
    private int putIndex;
    private int takeIndex;

    public TransactionBuffer(int capacity) {
        this.items = new String[capacity];
    }

    public synchronized void put(String transaction) throws InterruptedException {
        while (count == items.length) {
            wait();
        }
        items[putIndex] = transaction;
        putIndex = (putIndex + 1) % items.length;
        count++;
        notifyAll();
    }

    public synchronized String take() throws InterruptedException {
        while (count == 0) {
            wait();
        }
        String transaction = items[takeIndex];
        items[takeIndex] = null;
        takeIndex = (takeIndex + 1) % items.length;
        count--;
        notifyAll();
        return transaction;
    }
}

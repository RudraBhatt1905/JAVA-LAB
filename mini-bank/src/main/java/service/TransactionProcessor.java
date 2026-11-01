package service;

import model.Account;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/** Practical 10 / 12: fixed thread pool over a thread-safe account map, plus a lock-order demonstration. */
public class TransactionProcessor {
    private final ExecutorService pool = Executors.newFixedThreadPool(4);
    private final ConcurrentHashMap<String, Account> accounts;

    public TransactionProcessor(ConcurrentHashMap<String, Account> accounts) {
        this.accounts = accounts;
    }

    public ConcurrentHashMap<String, Account> getAccounts() {
        return accounts;
    }

    public void submit(Runnable task) {
        pool.execute(task);
    }

    public void stop() throws InterruptedException {
        pool.shutdown();
        if (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
            pool.shutdownNow();
        }
    }

    /**
     * Two threads lock the same two accounts. With ordered == false they take the locks in opposite
     * order and deadlock; with ordered == true both use the smaller account number first.
     * Returns true when the threads are still stuck after the wait (a deadlock).
     * Only locks are used, so no money moves. Use spare accounts: a deadlocked run keeps its locks.
     */
    public static boolean demonstrateLocking(Account a, Account b, boolean ordered) throws InterruptedException {
        Account lo = a.getAccountNumber().compareTo(b.getAccountNumber()) <= 0 ? a : b;
        Account hi = (lo == a) ? b : a;
        Thread t1 = new Thread(() -> nestedLock(ordered ? lo : a, ordered ? hi : b), "transfer-1");
        Thread t2 = new Thread(() -> nestedLock(ordered ? lo : b, ordered ? hi : a), "transfer-2");
        t1.setDaemon(true);
        t2.setDaemon(true);
        t1.start();
        t2.start();
        t1.join(1500);
        t2.join(1500);
        return t1.isAlive() && t2.isAlive();
    }

    private static void nestedLock(Account first, Account second) {
        synchronized (first) {
            pause(100);
            synchronized (second) {
                pause(10);
            }
        }
    }

    private static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

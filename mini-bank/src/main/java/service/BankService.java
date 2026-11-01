package service;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InvalidAmountException;
import model.Account;
import model.AccountType;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;
import util.ReportGenerator;
import util.StatePersister;
import util.StatementFormatter;
import util.TransactionLog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Practical 14: the application layer. Wires the repository (Practical 13), the account store
 * (Practical 12), validation/exceptions (Practical 4 and 8), persistence (Practical 11), the
 * transaction processor (Practical 10) and the report (Practical 11) behind the menu.
 */
public class BankService {
    private static final long SAVINGS_MIN_BALANCE = 500;
    private static final long CURRENT_OVERDRAFT = 5000;

    private final Repository<Account> repo = new Repository<>();
    private final AccountStore store = new AccountStore();
    private final Path dataFile;
    private final Path logsDir;
    private final Path logFile;
    private final Path reportFile;
    private final Object logLock = new Object();

    public BankService(Path dataDir) {
        this.dataFile = dataDir.resolve("accounts.dat");
        this.logsDir = dataDir.resolve("logs");
        this.logFile = logsDir.resolve("transactions.log");
        this.reportFile = dataDir.resolve("reports").resolve("summary.txt");
    }

    /** Practical 14: load saved accounts on start-up. */
    public synchronized void loadState() throws IOException, ClassNotFoundException {
        if (!Files.exists(dataFile)) {
            return;
        }
        Account[] loaded = StatePersister.load(dataFile);
        long highest = 0;
        for (Account account : loaded) {
            repo.save(account);
            store.add(account);
            highest = Math.max(highest, Long.parseLong(account.getAccountNumber().substring(2)));
        }
        Account.ensureCounterAtLeast(highest);
    }

    /** Practical 14: save accounts on exit. */
    public synchronized void saveState() throws IOException {
        StatePersister.save(repo.findAll().toArray(new Account[0]), dataFile);
    }

    public synchronized Account openAccount(String ownerName, long openingBalance, AccountType type)
            throws BankException {
        if (ownerName == null || ownerName.isBlank()) {
            throw new BankException("Account holder name is required");
        }
        if (openingBalance < 0) {
            throw new InvalidAmountException("Opening balance cannot be negative");
        }
        Account account = switch (type) {
            case SAVINGS -> new SavingsAccount(ownerName, openingBalance, SAVINGS_MIN_BALANCE);
            case CURRENT -> new CurrentAccount(ownerName, openingBalance, CURRENT_OVERDRAFT);
            case FIXED_DEPOSIT -> new FixedDepositAccount(ownerName, openingBalance);
        };
        repo.save(account);
        store.add(account);
        return account;
    }

    public Account find(String accountNumber) throws AccountNotFoundException {
        Account account = repo.findById(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException(accountNumber);
        }
        return account;
    }

    public synchronized void deposit(String accountNumber, long amount) throws BankException {
        find(accountNumber).deposit(amount);
        record("DEPOSIT " + accountNumber + " " + amount);
    }

    public synchronized void withdraw(String accountNumber, long amount) throws BankException {
        find(accountNumber).withdraw(amount);
        record("WITHDRAW " + accountNumber + " " + amount);
    }

    public synchronized void transfer(String fromNumber, String toNumber, long amount) throws BankException {
        Account from = find(fromNumber);
        Account to = find(toNumber);
        from.transfer(to, amount);
        record("TRANSFER " + fromNumber + "->" + toNumber + " " + amount);
    }

    public synchronized String statement(String accountNumber) throws AccountNotFoundException {
        return StatementFormatter.buildStatement(find(accountNumber));
    }

    public synchronized List<Account> listAccounts() {
        return store.sortedByBalance();
    }

    /**
     * Practical 10 / 12: processes a batch on a fixed thread pool over a ConcurrentHashMap.
     * Each account gets three deposits of 100 and one withdrawal of 50, all running concurrently.
     */
    public synchronized String runBatch() throws InterruptedException {
        ConcurrentHashMap<String, Account> shared = new ConcurrentHashMap<>();
        for (Account account : repo.findAll()) {
            shared.put(account.getAccountNumber(), account);
        }
        if (shared.isEmpty()) {
            return "No accounts to process.";
        }
        TransactionProcessor processor = new TransactionProcessor(shared);
        AtomicInteger succeeded = new AtomicInteger();
        AtomicInteger rejected = new AtomicInteger();
        for (Account account : shared.values()) {
            for (int i = 0; i < 3; i++) {
                processor.submit(() -> {
                    try {
                        account.deposit(100);
                        record("DEPOSIT " + account.getAccountNumber() + " 100");
                        succeeded.incrementAndGet();
                    } catch (InvalidAmountException e) {
                        rejected.incrementAndGet();
                    }
                });
            }
            processor.submit(() -> {
                try {
                    account.withdraw(50);
                    record("WITHDRAW " + account.getAccountNumber() + " 50");
                    succeeded.incrementAndGet();
                } catch (BankException e) {
                    rejected.incrementAndGet();
                }
            });
        }
        processor.stop();
        return "Batch complete: " + succeeded.get() + " succeeded, " + rejected.get() + " rejected.";
    }

    /** Practical 11: end-of-day report built from the transaction log. */
    public String generateReport() throws IOException {
        synchronized (logLock) {
            return ReportGenerator.generate(logsDir, reportFile);
        }
    }

    private void record(String entry) {
        synchronized (logLock) {
            store.record(entry);
            try {
                TransactionLog.append(logFile, entry);
            } catch (IOException e) {
                System.out.println("Warning: could not write transaction log: " + e.getMessage());
            }
        }
    }
}

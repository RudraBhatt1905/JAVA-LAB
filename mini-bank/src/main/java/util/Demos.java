package util;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.AccountType;
import model.Customer;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.Premium;
import model.SavingsAccount;
import model.WithdrawRule;
import service.AccountStore;
import service.AccountWorker;
import service.Repository;
import service.TransactionBuffer;
import service.TransactionProcessor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/** Run with: java -jar mini-bank.jar --demo   Shows each practical's Part B feature working. */
public final class Demos {
    private Demos() {
    }

    public static void runAll() throws Exception {
        section("Practical 2 - Accounts: deposit, withdraw, encapsulation");
        Account riya = new CurrentAccount("Riya", 5000, 0);
        riya.deposit(2000);
        System.out.println("After deposit 2000 : " + riya.getBalance());
        riya.withdraw(3000);
        System.out.println("After withdraw 3000: " + riya.getBalance());
        try {
            riya.withdraw(10000);
        } catch (InsufficientFundsException e) {
            System.out.println("Refused as expected: " + e.getMessage());
        }
        System.out.println("Balance unchanged  : " + riya.getBalance());

        section("Practical 3 - toString, equals, nested class, clone, instanceof");
        System.out.println("toString  : " + riya);
        System.out.println("equals self: " + riya.equals(riya));
        System.out.println("equals other: " + riya.equals(new CurrentAccount("Other", 0, 0)));
        Customer customer = new Customer("Riya", "riya@example.com", "9876543210",
                new Customer.Address("12 MG Road", "Anand", "388001"));
        Customer copy = customer.clone();
        System.out.println("Customer " + customer.getCustomerId() + " cloned as " + copy.getCustomerId()
                + ", city " + copy.getAddress().getCity());
        System.out.println("instanceof Account: " + (riya instanceof Account));

        section("Practical 5 - Abstract Account and polymorphism");
        Account[] accounts = {
                new SavingsAccount("Asha", 1000, 500),
                new CurrentAccount("Ravi", 2000, 5000),
                new FixedDepositAccount("Meera", 3000)
        };
        for (Account a : accounts) {
            System.out.println(a.getOwnerName() + " (" + a.getClass().getSimpleName()
                    + ") interest " + a.interestRate() + "%");
        }
        for (Account a : accounts) {
            if (a instanceof FixedDepositAccount fd) {
                System.out.println("Special handling - locked deposit " + fd.getAccountNumber());
            }
        }

        section("Practical 4 - Validators, parser and statement");
        System.out.println("isValidMobile 9876543210 -> " + Validator.isValidMobile("9876543210"));
        System.out.println("isValidMobile 12345      -> " + Validator.isValidMobile("12345"));
        System.out.println("isValidEmail abc@xyz.com -> " + Validator.isValidEmail("abc@xyz.com"));
        System.out.println("isValidPan ABCDE1234F    -> " + Validator.isValidPan("ABCDE1234F"));
        System.out.println("isValidIfsc SBIN0001234  -> " + Validator.isValidIfsc("SBIN0001234"));
        System.out.println("Parsed: " + CommandParser.parse("DEPOSIT AC0001 500"));
        System.out.println(StatementFormatter.buildStatement(accounts[0]));

        section("Practical 6 - Interfaces, functional interface, lambda, marker");
        WithdrawRule anonymousRule = new WithdrawRule() {
            @Override
            public boolean allow(Account account, long amount) {
                return amount <= account.getBalance() / 2;
            }
        };
        WithdrawRule lambdaRule = (account, amount) -> amount <= account.getBalance() / 2;
        System.out.println("Anonymous class allows 400: " + anonymousRule.allow(accounts[0], 400));
        System.out.println("Lambda allows 400         : " + lambdaRule.allow(accounts[0], 400));
        System.out.println("Yearly interest on Asha   : " + accounts[0].yearlyInterest());
        Premium marker = new Premium() { };
        System.out.println("Marker interface object created: " + (marker != null));

        section("Practical 7 - Annotations and reflection");
        Account negative = new CurrentAccount("Test", -100, 0);
        System.out.println("Errors for balance -100: " + Arrays.toString(AnnotationValidator.validate(negative)));
        System.out.println("Errors for valid account: " + Arrays.toString(AnnotationValidator.validate(accounts[0])));

        section("Practical 8 - Custom exceptions and try-with-resources");
        Account small = new CurrentAccount("Kiran", 1000, 0);
        try {
            small.withdraw(5000);
        } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage() + " (shortfall " + e.getShortfall() + ")");
        }
        try {
            small.deposit(-5);
        } catch (InvalidAmountException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
        try (DemoResource resource = new DemoResource("transaction session")) {
            System.out.println("Working inside " + resource.name());
            throw new IllegalStateException("simulated failure");
        } catch (IllegalStateException e) {
            System.out.println("Caught: " + e.getMessage());
        } finally {
            System.out.println("finally block ran");
        }

        section("Practical 9 - Race condition vs synchronization");
        final int perThread = 20_000_000;
        UnsafeCounter unsafe = new UnsafeCounter();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(() -> {
                for (int j = 0; j < perThread; j++) {
                    unsafe.increment();
                }
            }, "unsafe-" + i));
        }
        startAndJoin(threads);
        System.out.println("Without synchronization: " + unsafe.value + " (expected " + 10 * perThread
                + "; lost updates are random, so the total may vary)");

        Account shared = new SavingsAccount("Shared", 0, 0);
        threads.clear();
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(new AccountWorker(shared, 1000, 1), "worker-" + i));
        }
        startAndJoin(threads);
        System.out.println("With synchronized deposit: " + shared.getBalance() + " (expected 10000)");
        System.out.println("Thread " + threads.get(0).getName() + " state: " + threads.get(0).getState());

        section("Practical 10 - Thread pool, producer-consumer, deadlock and fix");
        TransactionProcessor processor = new TransactionProcessor(new ConcurrentHashMap<>());
        for (int i = 1; i <= 6; i++) {
            final int id = i;
            processor.submit(() -> System.out.println("Task " + id + " ran on " + Thread.currentThread().getName()));
        }
        processor.stop();

        TransactionBuffer buffer = new TransactionBuffer(3);
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    buffer.put("TX-" + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "producer");
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    System.out.println("Consumed " + buffer.take());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "consumer");
        startAndJoin(List.of(producer, consumer));

        boolean deadlocked = TransactionProcessor.demonstrateLocking(
                new SavingsAccount("Lock-A", 100, 0), new SavingsAccount("Lock-B", 100, 0), false);
        System.out.println("Opposite lock order -> deadlock: " + deadlocked);
        boolean fixed = TransactionProcessor.demonstrateLocking(
                new SavingsAccount("Lock-C", 100, 0), new SavingsAccount("Lock-D", 100, 0), true);
        System.out.println("Consistent lock order -> deadlock: " + fixed);

        section("Practical 11 - Serialization and NIO report");
        Path tmp = Files.createTempDirectory("minibank-demo");
        Path dataFile = tmp.resolve("accounts.dat");
        StatePersister.save(new Account[] { accounts[0], accounts[1] }, dataFile);
        Account[] reloaded = StatePersister.load(dataFile);
        for (Account a : reloaded) {
            System.out.println("Reloaded: " + a);
        }
        Path logs = tmp.resolve("logs");
        TransactionLog.append(logs.resolve("day1.log"), "DEPOSIT AC0001 500");
        TransactionLog.append(logs.resolve("day1.log"), "WITHDRAW AC0002 200");
        System.out.println(ReportGenerator.generate(logs, tmp.resolve("report.txt")));

        section("Practical 12 - Collections: AccountStore and sorting");
        AccountStore store = new AccountStore();
        store.add(new SavingsAccount("Ankit", 5000, 0));
        store.add(new SavingsAccount("Bhavna", 2000, 0));
        store.add(new SavingsAccount("Chirag", 9000, 0));
        for (Account a : store.sortedByBalance()) {
            System.out.println("  " + a);
        }

        section("Practical 13 - Generic Repository<T> and bounded generics");
        Repository<Account> repo = new Repository<>();
        repo.saveAll(accounts[0], accounts[1], accounts[2]);
        System.out.println("findById(" + accounts[1].getAccountNumber() + "): " + repo.findById(accounts[1].getAccountNumber()));
        System.out.println("findAll size: " + repo.findAll().size());
        Repository<Customer> customers = new Repository<>();
        customers.save(customer);
        System.out.println("Customer repository finds " + customer.getCustomerId() + ": "
                + (customers.findById(customer.getCustomerId()) != null));
        System.out.println("Average of Integer[] {2,4,6}: " + Repository.average(new Integer[] { 2, 4, 6 }));
        System.out.println("Average of Double[] {1.5,2.5}: " + Repository.average(new Double[] { 1.5, 2.5 }));

        System.out.println("\nDemo finished.");
    }

    private static void section(String title) {
        System.out.println("\n=== " + title + " ===");
    }

    private static void startAndJoin(List<Thread> threads) throws InterruptedException {
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
    }

    /** Practical 9: no synchronization, so updates can be lost. */
    static class UnsafeCounter {
        long value;

        void increment() {
            value++;
        }
    }

    /** Practical 8: an AutoCloseable resource for try-with-resources. */
    static class DemoResource implements AutoCloseable {
        private final String name;

        DemoResource(String name) {
            this.name = name;
        }

        String name() {
            return name;
        }

        @Override
        public void close() {
            System.out.println("Resource closed: " + name);
        }
    }
}

package model;

import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.annotation.Id;
import model.annotation.MaxLength;
import model.annotation.Positive;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Practical 2-13: base account. Balance changes only through deposit(), withdraw() and transfer().
 * Practical 5: abstract, with canWithdraw() and the subclasses supplying the rules.
 */
public abstract class Account implements Transactable, InterestBearing, Comparable<Account>, Serializable {
    private static final long serialVersionUID = 1L;

    private static long accountCounter = 0;
    private static final AtomicInteger transferAttempts = new AtomicInteger();

    @Id
    private final String accountNumber;

    @MaxLength(50)
    private String ownerName;

    @Positive
    private long balance;

    private boolean active;

    public Account(String ownerName, long openingBalance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    private static synchronized String generateAccountNumber() {
        return String.format("AC%04d", ++accountCounter);
    }

    /** Used after loading saved accounts so new numbers never clash with old ones. */
    public static synchronized void ensureCounterAtLeast(long value) {
        if (value > accountCounter) {
            accountCounter = value;
        }
    }

    public static int getTransferAttempts() {
        return transferAttempts.get();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public synchronized long getBalance() {
        return balance;
    }

    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than zero");
        }
        balance += amount;
    }

    @Override
    public synchronized boolean withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than zero");
        }
        if (!canWithdraw(amount)) {
            throw new InsufficientFundsException(amount - maxWithdrawable());
        }
        balance -= amount;
        return true;
    }

    /**
     * Practical 10 / 12 / 14: locks both accounts in a fixed order (by account number),
     * so two opposite transfers cannot deadlock.
     */
    public void transfer(Account to, long amount) throws BankException {
        if (to == null) {
            throw new BankException("Destination account is missing");
        }
        Account first = accountNumber.compareTo(to.accountNumber) <= 0 ? this : to;
        Account second = (first == this) ? to : this;
        try {
            synchronized (first) {
                synchronized (second) {
                    withdraw(amount);
                    to.deposit(amount);
                }
            }
        } catch (InsufficientFundsException | InvalidAmountException e) {
            throw new BankException("Transfer failed: " + e.getMessage(), e);
        } finally {
            transferAttempts.incrementAndGet();
        }
    }

    /** Amount that could be withdrawn right now under this account's rules. */
    protected synchronized long maxWithdrawable() {
        return balance;
    }

    public abstract boolean canWithdraw(long amount);

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return o instanceof Account other && accountNumber.equals(other.accountNumber);
    }

    @Override
    public int hashCode() {
        return accountNumber.hashCode();
    }

    @Override
    public synchronized String toString() {
        return accountNumber + " | " + ownerName + " | " + balance;
    }

    @Override
    public int compareTo(Account other) {
        return accountNumber.compareTo(other.accountNumber);
    }
}

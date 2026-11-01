package model;

/** Practical 5: interest 4.0%; withdrawals may not take the balance below minBalance. */
public class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;

    private final long minBalance;

    public SavingsAccount(String ownerName, long openingBalance, long minBalance) {
        super(ownerName, openingBalance);
        this.minBalance = minBalance;
    }

    @Override
    public double interestRate() {
        return 4.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return getBalance() - amount >= minBalance;
    }

    @Override
    protected long maxWithdrawable() {
        return getBalance() - minBalance;
    }
}

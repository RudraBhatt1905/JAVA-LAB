package model;

/** Practical 5: interest 7.0%; the deposit is locked, so withdrawals are refused. */
public class FixedDepositAccount extends Account {
    private static final long serialVersionUID = 1L;

    public FixedDepositAccount(String ownerName, long openingBalance) {
        super(ownerName, openingBalance);
    }

    @Override
    public double interestRate() {
        return 7.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return false;
    }

    @Override
    protected long maxWithdrawable() {
        return 0;
    }
}

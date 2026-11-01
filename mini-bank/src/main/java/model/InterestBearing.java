package model;

/** Practical 6: interface with a default method that uses interestRate() and the balance. */
public interface InterestBearing {
    double interestRate();

    long getBalance();

    default double yearlyInterest() {
        return getBalance() * interestRate() / 100.0;
    }
}

package model;

/** Practical 4: a parsed user command such as "DEPOSIT AC0001 500". */
public record Command(TransactionType type, String accountNumber, long amount) {
}

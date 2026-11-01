package util;

import model.Command;
import model.TransactionType;

/** Practical 4: turns a typed line such as "DEPOSIT AC0001 500" into a Command. */
public final class CommandParser {
    private CommandParser() {
    }

    public static Command parse(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Empty command");
        }
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Expected: TYPE ACCOUNT AMOUNT (got " + parts.length + " parts)");
        }
        TransactionType type;
        try {
            type = TransactionType.valueOf(parts[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown transaction type: " + parts[0]);
        }
        long amount;
        try {
            amount = Long.parseLong(parts[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Amount must be a whole number: " + parts[2]);
        }
        return new Command(type, parts[1], amount);
    }
}

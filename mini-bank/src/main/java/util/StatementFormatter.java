package util;

import model.Account;

/** Practical 4: builds a multi-line statement with a StringBuilder. */
public final class StatementFormatter {
    private StatementFormatter() {
    }

    public static String buildStatement(Account account) {
        StringBuilder sb = new StringBuilder();
        sb.append("----------- Account Statement -----------\n");
        sb.append("Account No    : ").append(account.getAccountNumber()).append('\n');
        sb.append("Account Holder: ").append(account.getOwnerName()).append('\n');
        sb.append("Account Type  : ").append(account.getClass().getSimpleName()).append('\n');
        sb.append("Balance       : ").append(account.getBalance()).append('\n');
        sb.append("Interest Rate : ").append(account.interestRate()).append("%\n");
        sb.append("Status        : ").append(account.isActive() ? "Active" : "Inactive").append('\n');
        sb.append("-----------------------------------------");
        return sb.toString();
    }
}

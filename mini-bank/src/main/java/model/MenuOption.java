package model;

/** Practical 1: menu actions. OPEN_ACCOUNT..EXIT are the Practical 1 set; the rest are added in Practical 14. */
public enum MenuOption {
    OPEN_ACCOUNT("Open account"),
    DEPOSIT("Deposit"),
    WITHDRAW("Withdraw"),
    TRANSFER("Transfer"),
    STATEMENT("Account statement"),
    LIST_ACCOUNTS("List accounts (by balance)"),
    RUN_BATCH("Run batch of concurrent transactions"),
    REPORT("Generate end-of-day report"),
    EXIT("Exit");

    private final String label;

    MenuOption(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

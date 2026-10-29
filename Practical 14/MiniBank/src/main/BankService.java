import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BankService {
    private ArrayList<BankAccount> accounts = new ArrayList<>();
    private int nextId = 1;

    // Add a new account
    public BankAccount addAccount(String owner, double balance) {
        if (owner == null || owner.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner name cannot be empty");
        }

        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }

        BankAccount account = new BankAccount(nextId++, owner, balance);
        accounts.add(account);

        return account;
    }

    // Return the number of accounts
    public int getAccountCount() {
        return accounts.size();
    }

    // Return accounts for displaying
    public List<BankAccount> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    // Deposit money
    public void deposit(int id, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        BankAccount account = findAccount(id);
        account.setBalance(account.getBalance() + amount);
    }

    // Withdraw money
    public void withdraw(int id, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        BankAccount account = findAccount(id);

        if (amount > account.getBalance()) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        account.setBalance(account.getBalance() - amount);
    }

    // Find account by ID
    private BankAccount findAccount(int id) {
        for (BankAccount account : accounts) {
            if (account.getId() == id) {
                return account;
            }
        }

        throw new IllegalArgumentException("Account not found");
    }
}
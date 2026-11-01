
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BankService {
    private ArrayList<BankAccount> accounts = new ArrayList<>();
    private int nextId = 1;

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

    public int getAccountCount() {
        return accounts.size();
    }

    public List<BankAccount> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public void deposit(int id, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        BankAccount account = findAccount(id);
        account.setBalance(account.getBalance() + amount);
    }

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

    private BankAccount findAccount(int id) {
        for (BankAccount account : accounts) {
            if (account.getId() == id) {
                return account;
            }
        }

        throw new IllegalArgumentException("Account not found");
    }
}
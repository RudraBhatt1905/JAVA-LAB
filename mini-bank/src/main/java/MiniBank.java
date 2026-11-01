import static util.Validator.isValidMobile;

import exception.BankException;
import model.Account;
import model.AccountType;
import model.BankInfo;
import model.Customer;
import model.MenuOption;
import service.BankService;
import util.Demos;
import util.Validator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * MiniBank - console banking application (Practical 1 onwards).
 * Run normally for the menu, or with --demo to see each practical's features.
 */
public class MiniBank {

    private static final Scanner IN = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("--demo")) {
            Demos.runAll();
            return;
        }

        System.out.println(new BankInfo("MiniBank", "Main Branch"));
        BankService bank = new BankService(Path.of("data"));
        try {
            bank.loadState();
            System.out.println("Saved accounts loaded: " + bank.listAccounts().size());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Warning: could not load saved accounts: " + e.getMessage());
        }

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");
            if (choice < 1 || choice > MenuOption.values().length) {
                System.out.println("Invalid option. Please enter a number from 1 to " + MenuOption.values().length + ".");
                continue;
            }
            MenuOption option = MenuOption.values()[choice - 1];
            running = switch (option) {
                case EXIT -> false;
                default -> {
                    handle(option, bank);
                    yield true;
                }
            };
        }

        bank.saveState();
        System.out.println("Accounts saved. Thank you for banking with MiniBank. Goodbye!");
    }

    private static void printMenu() {
        System.out.println("\n========== MiniBank Menu ==========");
        for (int i = 0; i < MenuOption.values().length; i++) {
            System.out.printf("%d. %s%n", i + 1, MenuOption.values()[i].getLabel());
        }
    }

    private static void handle(MenuOption option, BankService bank) {
        try {
            switch (option) {
                case OPEN_ACCOUNT -> openAccountFlow(bank);
                case DEPOSIT -> {
                    String number = readLine("Account number: ");
                    long amount = readLong("Amount: ");
                    bank.deposit(number, amount);
                    System.out.println("Deposit successful. Balance: " + bank.find(number).getBalance());
                }
                case WITHDRAW -> {
                    String number = readLine("Account number: ");
                    long amount = readLong("Amount: ");
                    bank.withdraw(number, amount);
                    System.out.println("Withdrawal successful. Balance: " + bank.find(number).getBalance());
                }
                case TRANSFER -> {
                    String from = readLine("From account: ");
                    String to = readLine("To account: ");
                    long amount = readLong("Amount: ");
                    bank.transfer(from, to, amount);
                    System.out.println("Transfer successful.");
                }
                case STATEMENT -> System.out.println(bank.statement(readLine("Account number: ")));
                case LIST_ACCOUNTS -> {
                    if (bank.listAccounts().isEmpty()) {
                        System.out.println("No accounts yet.");
                    }
                    for (Account account : bank.listAccounts()) {
                        System.out.println(account);
                    }
                }
                case RUN_BATCH -> System.out.println(bank.runBatch());
                case REPORT -> System.out.println(bank.generateReport());
                case EXIT -> {
                    // handled in main
                }
            }
        } catch (BankException | IOException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void openAccountFlow(BankService bank) throws BankException {
        String name = readLine("Account holder name: ");
        String mobile = readLine("Mobile (10 digits, starts with 6-9): ");
        if (!isValidMobile(mobile)) {
            throw new BankException("Invalid mobile number");
        }
        String email = readLine("Email: ");
        if (!Validator.isValidEmail(email)) {
            throw new BankException("Invalid email address");
        }
        System.out.println("Account type: 1. Savings  2. Current  3. Fixed deposit");
        AccountType type = switch (readInt("Choose type: ")) {
            case 1 -> AccountType.SAVINGS;
            case 2 -> AccountType.CURRENT;
            case 3 -> AccountType.FIXED_DEPOSIT;
            default -> throw new BankException("Invalid account type");
        };
        long opening = readLong("Opening balance: ");

        Customer customer = new Customer(name, email, mobile);
        Account account = bank.openAccount(name, opening, type);
        System.out.println("Customer ID: " + customer.getCustomerId());
        System.out.println("Account opened: " + account);
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return IN.nextLine().trim();
    }

    private static int readInt(String prompt) {
        try {
            return Integer.parseInt(readLine(prompt));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static long readLong(String prompt) throws BankException {
        String text = readLine(prompt);
        try {
            return Long.parseLong(text);
        } catch (NumberFormatException e) {
            throw new BankException("Please enter a whole number, not '" + text + "'");
        }
    }
}

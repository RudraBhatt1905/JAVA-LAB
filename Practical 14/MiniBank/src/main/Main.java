
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankService bank = new BankService();

        while (true) {
            System.out.println("\n--- MiniBank ---");
            System.out.println("1. Add Account");
            System.out.println("2. Show Accounts");
            System.out.println("3. Deposit Money");
            System.out.println("4. Withdraw Money");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
                continue;
            }

            if (choice == 5) {
                System.out.println("Thank you!");
                break;
            }

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter owner name: ");
                        String owner = sc.nextLine();

                        System.out.print("Enter initial balance: ");
                        double balance =
                            Double.parseDouble(sc.nextLine());

                        BankAccount account =
                            bank.addAccount(owner, balance);

                        System.out.println(
                            "Account created! ID: " + account.getId());
                        break;

                    case 2:
                        System.out.println(
                            "Total accounts: " + bank.getAccountCount());

                        for (BankAccount a : bank.getAccounts()) {
                            System.out.println(
                                a.getId() + " | " + a.getOwner()
                                + " | Balance: " + a.getBalance());
                        }
                        break;

                    case 3:
                    case 4:
                        System.out.print("Enter account ID: ");
                        int id = Integer.parseInt(sc.nextLine());

                        System.out.print("Enter amount: ");
                        double amount =
                            Double.parseDouble(sc.nextLine());

                        if (choice == 3) {
                            bank.deposit(id, amount);
                            System.out.println("Deposit successful!");
                        } else {
                            bank.withdraw(id, amount);
                            System.out.println("Withdrawal successful!");
                        }
                        break;

                    default:
                        System.out.println("Invalid menu choice!");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        sc.close();
    }
}

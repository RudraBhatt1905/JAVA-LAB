
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BankServiceTest {

    @Test
    void addingAccountIncreasesCount() {
        BankService bank = new BankService();

        assertEquals(0, bank.getAccountCount());

        bank.addAccount("Rahul", 1000);

        assertEquals(1, bank.getAccountCount());
    }

    @Test
    void depositIncreasesBalance() {
        BankService bank = new BankService();

        BankAccount account = bank.addAccount("Priya", 1000);
        bank.deposit(account.getId(), 500);

        assertEquals(1500.0, account.getBalance(), 0.001);
    }

    @Test
    void invalidWithdrawalThrowsException() {
        BankService bank = new BankService();

        final BankAccount account =
            bank.addAccount("Amit", 100);

        assertThrows(IllegalArgumentException.class,
            new Executable() {
                @Override
                public void execute() {
                    bank.withdraw(account.getId(), 200);
                }
            });
    }
}
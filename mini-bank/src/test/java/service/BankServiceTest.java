package service;

import exception.InsufficientFundsException;
import model.Account;
import model.AccountType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Practical 14: JUnit tests for the required behaviour. Run with: mvn test */
class BankServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void depositIncreasesBalance() throws Exception {
        BankService bank = new BankService(tempDir);
        Account account = bank.openAccount("Riya", 1000, AccountType.SAVINGS);

        bank.deposit(account.getAccountNumber(), 500);

        assertEquals(1500, bank.find(account.getAccountNumber()).getBalance());
    }

    @Test
    void withdrawalOverLimitThrows() throws Exception {
        BankService bank = new BankService(tempDir);
        Account account = bank.openAccount("Ravi", 1000, AccountType.SAVINGS);

        assertThrows(InsufficientFundsException.class,
                () -> bank.withdraw(account.getAccountNumber(), 5000));
        assertEquals(1000, account.getBalance());
    }

    @Test
    void concurrentDepositsKeepBalanceCorrect() throws Exception {
        BankService bank = new BankService(tempDir);
        Account account = bank.openAccount("Asha", 0, AccountType.SAVINGS);

        ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            pool.execute(new AccountWorker(account, 1000, 1));
        }
        pool.shutdown();
        assertTrue(pool.awaitTermination(30, TimeUnit.SECONDS));

        assertEquals(10000, account.getBalance());
    }
}

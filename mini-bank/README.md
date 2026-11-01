# MiniBank - Console Banking Application

OOP project (CEUE203), Semester III, A.Y. 2026-27. One project that brings together the
Part B (MiniBank) work of Practicals 1-14.

## Run

Requires JDK 17 or newer.

```
sh build.sh                 # compiles src/main/java and creates mini-bank.jar
java -jar mini-bank.jar     # runs the menu (Practical 1 onwards)
java -jar mini-bank.jar --demo   # runs a short demo of every practical's features
```

Maven alternative: `mvn package` (creates target/mini-bank.jar), `mvn test` (runs JUnit tests).

Accounts and the transaction log are saved in `data/` (created on first run, not committed to git).
Accounts are saved when you choose Exit and loaded when the program starts.

## Packages

| Package | Contents | Practical |
|---|---|---|
| (default) `MiniBank` | `main`, menu, input handling | 1, 14 |
| `model` | Account (abstract), SavingsAccount, CurrentAccount, FixedDepositAccount, Customer, BankInfo, MenuOption, TransactionType, Command, AccountType | 1, 2, 3, 4, 5 |
| `model` interfaces | Transactable, InterestBearing, WithdrawRule, Premium | 6, 7 |
| `model.annotation` | @Id, @Positive, @MaxLength | 7 |
| `util` | Validator, CommandParser, StatementFormatter, AnnotationValidator, StatePersister, TransactionLog, ReportGenerator, Demos | 4, 7, 11 |
| `service` | BankService, Repository<T>, AccountStore, TransactionProcessor, TransactionBuffer, AccountWorker | 9, 10, 12, 13, 14 |
| `exception` | BankException, InsufficientFundsException, AccountNotFoundException, InvalidAmountException | 8 |

## Practical-by-practical

- **P1** BankInfo record header, MenuOption enum, numbered menu with a Scanner, loop until Exit.
- **P2** Customer and Account with encapsulated fields, generated ids (CUST101, AC0001), constructor chaining, deposit/withdraw, no balance setter.
- **P3** toString, equals/hashCode on account number, nested static class Customer.Address, Customer.clone(), instanceof.
- **P4** Validator (mobile, email, PAN, IFSC by regex), TransactionType enum, Command record, CommandParser, StatementFormatter (StringBuilder).
- **P5** Abstract Account with canWithdraw(); SavingsAccount (4%, minimum balance), CurrentAccount (0%, overdraft), FixedDepositAccount (7%, locked); polymorphic loop and pattern instanceof.
- **P6** Transactable and InterestBearing (default method), WithdrawRule (functional, anonymous class and lambda), Premium marker, package structure, static import.
- **P7** @Override and @FunctionalInterface, custom annotations @Id, @Positive, @MaxLength, reflection-based AnnotationValidator.
- **P8** Checked exception hierarchy (BankException and subclasses), throws in withdraw/deposit, transfer with try/catch/finally, try-with-resources demo.
- **P9** AccountWorker threads, synchronized deposit/withdraw, race-condition demo.
- **P10** TransactionProcessor (fixed pool of 4), batch processing, TransactionBuffer producer-consumer with wait/notifyAll, deadlock demonstration and fix by consistent lock order in transfer().
- **P11** Serializable model, StatePersister (ObjectOutputStream), append-only TransactionLog, ReportGenerator (NIO directory stream, BufferedReader, Files.readAttributes).
- **P12** AccountStore with HashMap, ArrayList transaction history, sortedByBalance() with a Comparator, Comparable accounts, ConcurrentHashMap in the batch processor.
- **P13** Generic Repository<T> keyed by the @Id field found by reflection, @SafeVarargs saveAll, bounded average(N extends Number).
- **P14** BankService wires everything together; the menu calls it; accounts load on start and save on exit; batch and report are menu options; JUnit tests in src/test.

## Rules and assumptions used

- Savings accounts keep a minimum balance of 500. Current accounts have an overdraft of 5000. Fixed deposits cannot be withdrawn.
- Account numbers continue from the highest saved number after a restart (AC0003 after AC0001 and AC0002).
- Every successful deposit, withdrawal and transfer is written to `data/logs/transactions.log`. Transfers are logged as `TRANSFER from->to amount`.
- The report's "net change" is total deposits minus total withdrawals (transfers stay inside the bank).
- Customer details (name, email, mobile) are entered when an account is opened and shown with the customer ID. Customers are not saved to disk.
- The @Positive rule ("must be > 0") is checked by AnnotationValidator, as specified in Practical 7. An account with a zero balance is reported by that check.
- The optional @Audit annotation (Practical 14, step 5) and the optional supplementary problems are not included.

## Testing

`mvn test` runs three JUnit tests in `src/test/java/service/BankServiceTest.java`:
a deposit increases the balance, a withdrawal over the limit throws, and 10,000 concurrent deposits
leave the balance correct.

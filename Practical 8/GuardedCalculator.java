import java.util.Scanner;
import java.util.InputMismatchException;

class DivideByZeroException extends Exception {
    DivideByZeroException(String message) {
        super(message);
    }
}

public class GuardedCalculator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        boolean success = false;
        int attempt = 0;

        while (!success) {
            attempt++;

            try {
                System.out.print("Enter first number: ");
                double num1 = sc.nextDouble();

                System.out.print("Enter second number: ");
                double num2 = sc.nextDouble();

                System.out.print("Enter operator (+, -, *, /): ");
                String op = sc.next();

                double result;

                if (op.equals("+")) {
                    result = num1 + num2;
                } else if (op.equals("-")) {
                    result = num1 - num2;
                } else if (op.equals("*")) {
                    result = num1 * num2;
                } else if (op.equals("/")) {
                    if (num2 == 0) {
                        throw new DivideByZeroException(
                                "Cannot divide by zero!"
                        );
                    }
                    result = num1 / num2;
                } else {
                    System.out.println("Invalid operator!");
                    continue;
                }

                System.out.println("Result: " + result);
                success = true;

            } catch (DivideByZeroException e) {
                System.out.println("Error: " + e.getMessage());

            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter numbers only.");
                sc.next(); 

            } finally {
                System.out.println("[LOG] Attempt " + attempt + " completed.");
                System.out.println();
            }
        }

        System.out.println("Valid calculation completed successfully.");
        sc.close();
    }
}
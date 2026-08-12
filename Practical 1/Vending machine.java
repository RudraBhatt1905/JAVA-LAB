import java.util.Scanner;

public class VendingMachine {

    enum Coin {
        ONE,TWO,FIVE,TEN
        }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

               int snackPrice = 15;
        int total = 0;

        System.out.println("Snack Price = " + snackPrice);
        System.out.println("Enter coins (ONE, TWO, FIVE, TEN):");

                while (total < snackPrice) {

            String input = sc.next().toUpperCase();

            Coin coin = Coin.valueOf(input);

            int value = switch (coin) {
                case ONE -> 1;
                case TWO -> 2;
                case FIVE -> 5;
                case TEN -> 10;
            };

            total += value;

            System.out.println("Total so far: " + total);
        }

        int change = total - snackPrice;
        System.out.println("Paid. Change: " + change);

        sc.close();
    }
}
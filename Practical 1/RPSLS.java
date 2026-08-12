import java.util.Scanner;
import java.util.Random;

public class RPSLS {

    enum Move {
        ROCK,PAPER,SCISSORS,LIZARD,SPOCK
        }

    public static int winner(Move a, Move b) {

        if (a == b) {
            return 0; // Tie
        }

        return switch (a) {
            case ROCK ->
                (b == Move.SCISSORS || b == Move.LIZARD) ? 1 : -1;

            case PAPER ->
                (b == Move.ROCK || b == Move.SPOCK) ? 1 : -1;

            case SCISSORS ->
                (b == Move.PAPER || b == Move.LIZARD) ? 1 : -1;

            case LIZARD ->
                (b == Move.SPOCK || b == Move.PAPER) ? 1 : -1;

            case SPOCK ->
                (b == Move.SCISSORS || b == Move.ROCK) ? 1 : -1;
        };
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        int playerScore = 0;
        int computerScore = 0;

        System.out.println("=== Rock Paper Scissors Lizard Spock ===");
        System.out.println("Moves: ROCK, PAPER, SCISSORS, LIZARD, SPOCK");

           for (int round = 1; round <= 5; round++) {

            System.out.println("\nRound " + round);

            System.out.print("Enter your move: ");
            String input = sc.next().toUpperCase();

            Move playerMove = Move.valueOf(input);

            Move computerMove = Move.values()[random.nextInt(Move.values().length)];

            System.out.println("Computer chose: " + computerMove);

            int result = winner(playerMove, computerMove);

            if (result == 1) {
                System.out.println("You win this round!");
                playerScore++;
            } 
            else if (result == -1) {
                System.out.println("Computer wins this round!");
                computerScore++;
            } 
            else {
                System.out.println("This round is a tie!");
            }

            System.out.println("Score: You " + playerScore + " - " + computerScore + " Computer");
        }

        System.out.println("\n===== Final Result =====");

        if (playerScore > computerScore) 
        {
            System.out.println("You win " + playerScore + "–" + computerScore + "!");
        } 
        else if (computerScore > playerScore) 
        {
            System.out.println("Computer wins " + computerScore + "–" + playerScore + "!");
        } 
        else 
        {
            System.out.println("Match Draw " + playerScore + "–" + computerScore + "!");
        }

        sc.close();
    }
}

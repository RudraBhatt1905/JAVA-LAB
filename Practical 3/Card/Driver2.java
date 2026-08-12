public class Driver {
    public static void main(String[] args) {

        Card[] cards = {
            new Card("Ace", "Spades"),
            new Card("Queen", "Hearts"),
            new Card("King", "Clubs"),
            new Card("Ace", "Spades"),    
            new Card("Jack", "Diamonds")
        };

        boolean duplicateFound = false;

        for (int i = 0; i < cards.length && !duplicateFound; i++) {
            for (int j = 0; j < i; j++) {
                if (cards[i].equals(cards[j])) {
                    System.out.println("Duplicate found: " + cards[i]);
                    duplicateFound = true;
                    break;
                }
            }
        }

        if (!duplicateFound) {
            System.out.println("No duplicates found.");
        }
    }
}
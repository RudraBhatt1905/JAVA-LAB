import java.util.*;

class OutOfStockException extends Exception {
    private int shortfall;

    OutOfStockException(int shortfall) {
        super("Insufficient stock! Shortfall: " + shortfall);
        this.shortfall = shortfall;
    }

    public int getShortfall() {
        return shortfall;
    }
}

class InvalidQuantityException extends Exception {
    InvalidQuantityException(String message) {
        super(message);
    }
}

class Warehouse {
    private int stock;

    Warehouse(int stock) {
        this.stock = stock;
    }

    public void issue(String item, int qty)
            throws OutOfStockException, InvalidQuantityException {

        if (qty <= 0) {
            throw new InvalidQuantityException(
                    "Quantity must be greater than zero."
            );
        }

        if (qty > stock) {
            throw new OutOfStockException(qty - stock);
        }

        stock = stock - qty;

        System.out.println("Issued " + qty + " units of " + item);
        System.out.println("Remaining stock: " + stock);
    }

    public int getStock() {
        return stock;
    }
}

public class StockIssue {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse(10);

        String[] items = {"Pen", "Book", "Pencil", "Bag", "Eraser"};
        int[] quantities = {3, 12, 0, 4, 2};

        for (int i = 0; i < quantities.length; i++) {
            System.out.println("\nRequest: " + items[i]
                    + ", Quantity: " + quantities[i]);

            try {
                warehouse.issue(items[i], quantities[i]);

            } catch (OutOfStockException e) {
                System.out.println("Failed: " + e.getMessage());

            } catch (InvalidQuantityException e) {
                System.out.println("Failed: " + e.getMessage());
            }
        }

        System.out.println("\nAll requests processed.");
        System.out.println("Final stock: " + warehouse.getStock());
    }
}
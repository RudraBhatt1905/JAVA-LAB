import java.io.IOException;

class DatabaseConnection implements AutoCloseable {
    private boolean closed = false;

    DatabaseConnection() {
        System.out.println("Database connection opened.");
    }

    public void doWork() {
        System.out.println("Performing database operation...");
        throw new IllegalArgumentException("Invalid data!");
    }

    @Override
    public void close() throws IOException {
        closed = true;
        System.out.println("Database connection closed.");

        // Simulate an error during closing
        throw new IOException("Error while closing connection!");
    }

    public boolean isClosed() {
        return closed;
    }
}

public class AutoCloseableDemo {
    public static void main(String[] args) {
        DatabaseConnection connection = new DatabaseConnection();

        try (DatabaseConnection resource = connection) {
            resource.doWork();

        } catch (Exception e) {
            System.out.println("Original error: "
                    + e.getClass().getSimpleName()
                    + " - " + e.getMessage());

            // Display exceptions that occurred during closing
            for (Throwable t : e.getSuppressed()) {
                System.out.println("Suppressed error: "
                        + t.getClass().getSimpleName()
                        + " - " + t.getMessage());
            }
        }

        System.out.println("Was resource closed? "
                + connection.isClosed());
    }
}
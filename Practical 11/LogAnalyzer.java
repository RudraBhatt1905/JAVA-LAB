
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class LogAnalyzer {
    public static void main(String[] args) {
        try {
            // Create a folder for log files
            Path folder = Paths.get("logs");
            Files.createDirectories(folder);

            // Create sample text files
            Files.write(folder.resolve("log1.txt"), List.of(
                "Application started",
                "ERROR: File not found",
                "User logged in"
            ));

            Files.write(folder.resolve("log2.txt"), List.of(
                "Application running",
                "ERROR: Connection failed",
                "WARNING: Low memory"
            ));

            Files.write(folder.resolve("log3.txt"), List.of(
                "User logged out",
                "Application closed"
            ));

            String keyword = "ERROR";
            int totalLines = 0;
            int matchingLines = 0;

            // Read every file in the folder
            try (DirectoryStream<Path> files =
                     Files.newDirectoryStream(folder, "*.txt")) {

                for (Path file : files) {
                    int fileLines = 0;
                    int fileMatches = 0;

                    // Read the file line by line
                    try (var reader = Files.newBufferedReader(file)) {
                        String line;

                        while ((line = reader.readLine()) != null) {
                            totalLines++;
                            fileLines++;

                            if (line.contains(keyword)) {
                                matchingLines++;
                                fileMatches++;
                            }
                        }
                    }

                    // Get file size using file attributes
                    BasicFileAttributes attr =
                        Files.readAttributes(file, BasicFileAttributes.class);

                    System.out.println("File: " + file.getFileName());
                    System.out.println("Size: " + attr.size() + " bytes");
                    System.out.println("Lines: " + fileLines);
                    System.out.println("Lines containing " + keyword + ": "
                                       + fileMatches);
                    System.out.println();
                }
            }

            // Display totals for all files
            System.out.println("Total lines: " + totalLines);
            System.out.println("Total lines containing " + keyword + ": "
                               + matchingLines);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}


import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;

class ReportVisitor extends SimpleFileVisitor<Path> {
    private Path root;
    private Path reportPath;
    private BufferedWriter writer;

    long totalFiles = 0;
    long totalSize = 0;

    ReportVisitor(Path root, Path reportPath, BufferedWriter writer) {
        this.root = root;
        this.reportPath = reportPath;
        this.writer = writer;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr)
            throws IOException {

        // Avoid including the report file itself
        if (file.toAbsolutePath().normalize().equals(reportPath)) {
            return FileVisitResult.CONTINUE;
        }

        writer.write("File: " + root.relativize(file));
        writer.newLine();

        writer.write("Size: " + attr.size() + " bytes");
        writer.newLine();

        writer.write("Modified: " + attr.lastModifiedTime());
        writer.newLine();

        writer.newLine();

        totalFiles++;
        totalSize += attr.size();

        return FileVisitResult.CONTINUE;
    }
}

public class FolderReport {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter folder path: ");
        String folderName = sc.nextLine();

        Path root = Paths.get(folderName).toAbsolutePath().normalize();
        Path reportPath = Paths.get("folder_report.txt")
                               .toAbsolutePath().normalize();

        if (!Files.isDirectory(root)) {
            System.out.println("Folder does not exist!");
            sc.close();
            return;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(reportPath)) {
            writer.write("FOLDER REPORT");
            writer.newLine();
            writer.write("====================");
            writer.newLine();
            writer.newLine();

            ReportVisitor visitor =
                new ReportVisitor(root, reportPath, writer);

            // Walk through the folder and its subfolders
            Files.walkFileTree(root, visitor);

            writer.write("====================");
            writer.newLine();
            writer.write("Total files: " + visitor.totalFiles);
            writer.newLine();
            writer.write("Total size: " + visitor.totalSize + " bytes");
            writer.newLine();

            System.out.println("Report created successfully!");
            System.out.println("Report file: " + reportPath);
            System.out.println("Total files: " + visitor.totalFiles);
            System.out.println("Total size: " + visitor.totalSize + " bytes");

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        sc.close();
    }
}

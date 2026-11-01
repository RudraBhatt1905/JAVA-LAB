package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Practical 11: walks the logs folder with NIO and writes an end-of-day summary. */
public final class ReportGenerator {
    private ReportGenerator() {
    }

    public static String generate(Path logsDir, Path reportFile) throws IOException {
        Files.createDirectories(logsDir);
        List<Path> logs = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsDir, "*.log")) {
            for (Path p : stream) {
                logs.add(p);
            }
        }
        Collections.sort(logs);

        long totalDeposits = 0;
        long totalWithdrawals = 0;
        long totalTransfers = 0;
        int counted = 0;
        int skipped = 0;
        StringBuilder fileList = new StringBuilder();

        for (Path log : logs) {
            BasicFileAttributes attrs = Files.readAttributes(log, BasicFileAttributes.class);
            fileList.append(String.format("  %-24s %8d bytes   modified %s%n",
                    log.getFileName(), attrs.size(), attrs.lastModifiedTime()));
            try (BufferedReader reader = Files.newBufferedReader(log)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.trim().split("\\s+");
                    if (parts.length != 3) {
                        skipped++;
                        continue;
                    }
                    long amount;
                    try {
                        amount = Long.parseLong(parts[2]);
                    } catch (NumberFormatException e) {
                        skipped++;
                        continue;
                    }
                    switch (parts[0]) {
                        case "DEPOSIT" -> totalDeposits += amount;
                        case "WITHDRAW" -> totalWithdrawals += amount;
                        case "TRANSFER" -> totalTransfers += amount;
                        default -> {
                            skipped++;
                            continue;
                        }
                    }
                    counted++;
                }
            }
        }

        StringBuilder report = new StringBuilder();
        report.append("==== MiniBank End-of-Day Report ====\n");
        report.append("Log files processed : ").append(logs.size()).append('\n');
        report.append("Transactions counted: ").append(counted).append('\n');
        report.append("Lines skipped       : ").append(skipped).append('\n');
        report.append("Total deposits      : ").append(totalDeposits).append('\n');
        report.append("Total withdrawals   : ").append(totalWithdrawals).append('\n');
        report.append("Total transfers     : ").append(totalTransfers).append('\n');
        report.append("Net change          : ").append(totalDeposits - totalWithdrawals).append('\n');
        report.append("Files:\n").append(fileList);

        Path parent = reportFile.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(reportFile, report.toString());
        return report.toString();
    }
}

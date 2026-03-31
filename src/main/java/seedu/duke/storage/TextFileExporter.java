package seedu.duke.storage;

import seedu.duke.MoneyBagProMaxException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class TextFileExporter {
    private static final String DATA_FILE = "data/transactions.txt";

    public void export(String outputPath) throws MoneyBagProMaxException {
        try {
            Path source = Paths.get(DATA_FILE);
            if (!Files.exists(source)) {
                throw new MoneyBagProMaxException("No data file found to export.");
            }
            Path destination = Paths.get(outputPath);
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Data file exported to: " + destination.toAbsolutePath());
        } catch (IOException e) {
            throw new MoneyBagProMaxException("Failed to export data file: " + e.getMessage());
        }
    }
}

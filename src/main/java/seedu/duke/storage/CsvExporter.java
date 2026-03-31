package seedu.duke.storage;

import seedu.duke.MoneyBagProMaxException;
import seedu.duke.transaction.Transaction;
import seedu.duke.transactionlist.TransactionList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvExporter {
    public Path export(TransactionList list, String outputPath) throws MoneyBagProMaxException {
        assert list != null : "TransactionList should not be null";
        try {
            List<String> lines = new ArrayList<>();
            lines.add("date,type,category,description,amount");
            for (int i = 0; i < list.size(); i++) {
                Transaction t = list.get(i);
                lines.add(String.join(",",
                                      escape(t.getDate().toString()),
                                      escape(t.getType()),
                                      escape(t.getCategory()),
                                      escape(t.getDescription()),
                                      escape(String.valueOf(t.getAmount()))
                ));
            }
            Path path = Paths.get(outputPath);
            Files.write(path, lines);
        } catch (IOException e) {
            throw new MoneyBagProMaxException("Failed to export CSV: " + e.getMessage());
        }
        return null;
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}

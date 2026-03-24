package seedu.duke.storage;

import seedu.duke.MoneyBagProMaxException;
import seedu.duke.transaction.Expense;
import seedu.duke.transaction.Income;
import seedu.duke.transaction.Transaction;
import seedu.duke.transactionlist.TransactionList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Flat-file persistent storage for MoneyBagProMax.
 * Reads and writes transactions to {@code data/transactions.txt}.
 */
public class Storage {

    private static final String DATA_DIR  = "data";
    private static final String DATA_FILE = "data/transactions.txt";

    private static final String TXN_PREFIX = "[TXN]";
    private static final String FIELD_SEP  = " | ";
    private static final String KV_SEP     = "=";


    /**
     * Populates the transaction list from disk. Call once on app startup.
     *
     * @param list TransactionList, required — the list to load transactions into.
     * @throws MoneyBagProMaxException if the file cannot be read.
     */
    public void load(TransactionList list) throws MoneyBagProMaxException {
        assert list != null : "TransactionList should not be null";
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            Path p = Paths.get(DATA_FILE);
            if (!Files.exists(p)) {
                Files.createFile(p); 
                return; 
            }

            while (!list.isEmpty()) {
                list.remove(0);
            }
            for (String line : Files.readAllLines(p)) {
                if (!line.startsWith(TXN_PREFIX)) {
                    continue;
                }
                Map<String, String> f = parseLine(line);
                if (f == null) {
                    continue;
                }
                try {
                    String type        = f.get("type");
                    String category    = f.get("category");
                    String amountStr   = f.get("amount");
                    String description = f.getOrDefault("description", "");
                    String dateStr     = f.get("date");
                    if (type == null || category == null || amountStr == null || dateStr == null) {
                        continue;
                    }
                    double amount  = Double.parseDouble(amountStr);
                    LocalDate date = LocalDate.parse(dateStr);
                    Transaction t = switch (type) {
                    case "income"  -> new Income(category, amount, description, date);
                    case "expense" -> new Expense(category, amount, description, date);
                    default        -> null;
                    };
                    if (t != null) {
                        list.add(t);
                    }
                } catch (Exception e) {
                    System.out.println("[WARN] Skipping malformed line: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new MoneyBagProMaxException("Failed to load data: " + e.getMessage());
        }
    }

    /**
     * Parses a storage line into a key-value field map.
     *
     * @param line String, required — a raw line from the storage file.
     * @return Map of field names to values, or null if the line is malformed.
     */
    private Map<String, String> parseLine(String line) {
        assert line != null : "Line should not be null";
        assert line.startsWith(TXN_PREFIX) : "Line should start with TXN prefix";
        try {
            Map<String, String> fields = new LinkedHashMap<>();
            String[] parts = line.split("\\s*\\|\\s*");
            for (int i = 1; i < parts.length; i++) {
                int eq = parts[i].indexOf(KV_SEP);
                if (eq < 0) {
                    continue;
                }
                fields.put(parts[i].substring(0, eq).trim(),
                           parts[i].substring(eq + 1).trim());
            }
            return fields;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Atomically writes the transaction list to disk. Call after every mutating command.
     *
     * @param list TransactionList, required — the list to save.
     * @throws MoneyBagProMaxException if the file cannot be written.
     */
    public void save(TransactionList list) throws MoneyBagProMaxException {
        assert list != null : "TransactionList should not be null";
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            List<String> lines = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                lines.add(serializeLine(list.get(i)));
            }

            Path target = Paths.get(DATA_FILE);
            Path tmp    = Paths.get(DATA_FILE + ".tmp");
            Files.write(tmp, lines);
            Files.move(tmp, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new MoneyBagProMaxException("Failed to save data: " + e.getMessage());
        }
    }

    /**
     * Serializes a transaction into a pipe-delimited string for storage.
     *
     * @param t Transaction, required — the transaction to serialize.
     * @return String — the formatted storage line.
     */
    private String serializeLine(Transaction t) {
        assert t != null : "Transaction should not be null";
        assert t.getType() != null : "Transaction type should not be null";
        assert t.getCategory() != null : "Transaction category should not be null";
        assert t.getAmount() > 0 : "Transaction amount should be positive";
        assert t.getDate() != null : "Transaction date should not be null";
        return TXN_PREFIX + FIELD_SEP + String.join(FIELD_SEP,
                                                    "type"        + KV_SEP + t.getType(),
                                                    "category"    + KV_SEP + t.getCategory(),
                                                    "amount"      + KV_SEP + t.getAmount(),
                                                    "description" + KV_SEP + t.getDescription(),
                                                    "date"        + KV_SEP + t.getDate()
        );
    }
}

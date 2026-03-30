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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Flat-file persistent storage for MoneyBagProMax.
 * Reads and writes transactions to {@code data/transactions.txt}.
 */
public class Storage {

    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = "data/transactions.txt";

    private static final String TXN_PREFIX = "[TXN]";
    private static final String FIELD_SEP = " | ";
    private static final String KV_SEP = "=";

    private static final List<String> VALID_TYPES = List.of("income", "expense");


    /**
     * Populates the transaction list from disk. Call once on app startup.
     *
     * @param list TransactionList, required — the list to load transactions into.
     * @throws MoneyBagProMaxException if the file cannot be read.
     */
    public void load(TransactionList list) throws MoneyBagProMaxException {
        assert list != null : "TransactionList should not be null";
        try {
            if (!initialiseFile()) {
                return;
            }
            clearList(list);
            parseLines(list, Files.readAllLines(Paths.get(DATA_FILE)));
        } catch (IOException e) {
            throw new MoneyBagProMaxException("Failed to load data: " + e.getMessage());
        }
    }

    /**
     * Creates the data directory and file if they do not exist.
     *
     * @return true if the file already existed, false if it was newly created.
     * @throws IOException if the directory or file cannot be created.
     */
    private boolean initialiseFile() throws IOException {
        Files.createDirectories(Paths.get(DATA_DIR));
        Path p = Paths.get(DATA_FILE);
        if (!Files.exists(p)) {
            Files.createFile(p);
            return false;
        }
        return true;
    }

    /**
     * Removes all transactions from the list.
     *
     * @param list TransactionList, required — the list to clear.
     */
    private void clearList(TransactionList list) {
        while (!list.isEmpty()) {
            list.remove(0);
        }
    }

    /**
     * Parses each line from the file and adds valid transactions to the list.
     *
     * @param list  TransactionList, required — the list to populate.
     * @param lines List of raw lines read from the storage file.
     */
    private void parseLines(TransactionList list, List<String> lines) {
        for (String line : lines) {
            if (!line.startsWith(TXN_PREFIX)) {
                continue;
            }
            Map<String, String> f = parseLine(line);
            if (f == null) {
                System.out.println("[WARN] Skipping malformed line (could not parse): " + line);
                continue;
            }
            try {
                Transaction t = validateAndBuild(f, line);
                if (t != null) {
                    list.add(t);
                }
            } catch (CorruptedEntryException e) {
                System.out.println("[WARN] Skipping corrupted entry: " + e.getMessage());
            }
        }
    }

    /**
     * Validates the fields of a parsed transaction and builds the Transaction object.
     *
     * @param f Map of field names to values.
     * @param line The original raw line, used for error context.
     * @return A valid Transaction, or null if type is unrecognised.
     * @throws CorruptedEntryException if any field is missing or invalid.
     */
    private Transaction validateAndBuild(Map<String, String> f, String line)
            throws CorruptedEntryException {
        validateFields(f, line);
        return buildTransaction(f, line);
    }

    /**
     * Extracts fields from the map and builds the Transaction object.
     *
     * @param f Map of field names to values.
     * @param line The original raw line, used for error context.
     * @return A valid Transaction, or null if type is unrecognised.
     * @throws CorruptedEntryException if any field is missing or invalid.
     */
    private Transaction buildTransaction(Map<String, String> f, String line)
            throws CorruptedEntryException {
        String type = f.get("type");
        String category = f.get("category");
        String amountStr = f.get("amount");
        String description = f.getOrDefault("description", "");
        String dateStr = f.get("date");

        validateType(type, line);
        double amount = validateAmount(amountStr, line);
        LocalDate date = validateDate(dateStr, line);

        return switch (type) {
        case "income" -> new Income(category, amount, description, date);
        case "expense" -> new Expense(category, amount, description, date);
        default -> null;
        };
    }

    /**
     * Checks that all required fields are present and non-blank.
     *
     * @param f Map of field names to values.
     * @param line The original raw line, used for error context.
     * @throws CorruptedEntryException if any required field is missing or blank.
     */
    private void validateFields(Map<String, String> f, String line)
            throws CorruptedEntryException {
        for (String field : new String[]{"type", "category", "amount", "date"}) {
            if (f.get(field) == null || f.get(field).isBlank()) {
                throw new CorruptedEntryException(
                        "missing or empty field '" + field + "' in: " + line);
            }
        }
    }

    /**
     * Checks that the transaction type is one of the known valid values.
     *
     * @param type The type string to validate.
     * @param line The original raw line, used for error context.
     * @throws CorruptedEntryException if the type is not recognised.
     */
    private void validateType(String type, String line) throws CorruptedEntryException {
        if (!VALID_TYPES.contains(type)) {
            throw new CorruptedEntryException(
                    "unknown transaction type '" + type + "' in: " + line);
        }
    }

    /**
     * Parses and validates the amount string, ensuring it is a positive number.
     *
     * @param amountStr The amount string to validate.
     * @param line The original raw line, used for error context.
     * @return The parsed amount as a double.
     * @throws CorruptedEntryException if the amount is not a valid positive number.
     */
    private double validateAmount(String amountStr, String line) throws CorruptedEntryException {
        double amount = parseAmount(amountStr, line);
        checkAmountPositive(amount, line);
        return amount;
    }

    /**
     * Parses the amount string into a double.
     *
     * @param amountStr The amount string to parse.
     * @param line The original raw line, used for error context.
     * @return The parsed amount as a double.
     * @throws CorruptedEntryException if the amount string is not a valid number.
     */
    private double parseAmount(String amountStr, String line) throws CorruptedEntryException {
        try {
            return Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            throw new CorruptedEntryException(
                    "amount '" + amountStr + "' is not a valid number in: " + line);
        }
    }

    /**
     * Checks that the amount is a positive number.
     *
     * @param amount The amount to check.
     * @param line The original raw line, used for error context.
     * @throws CorruptedEntryException if the amount is not positive.
     */
    private void checkAmountPositive(double amount, String line) throws CorruptedEntryException {
        if (amount <= 0) {
            throw new CorruptedEntryException(
                    "amount must be positive but was '" + amount + "' in: " + line);
        }
    }

    /**
     * Parses and validates the date string as a YYYY-MM-DD date.
     *
     * @param dateStr The date string to validate.
     * @param line The original raw line, used for error context.
     * @return The parsed LocalDate.
     * @throws CorruptedEntryException if the date is not a valid YYYY-MM-DD date.
     */
    private LocalDate validateDate(String dateStr, String line) throws CorruptedEntryException {
        try {
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            throw new CorruptedEntryException(
                    "date '" + dateStr + "' is not a valid YYYY-MM-DD date in: " + line);
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
            Path tmp = Paths.get(DATA_FILE + ".tmp");
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
                                                    "type" + KV_SEP + t.getType(),
                                                    "category" + KV_SEP + t.getCategory(),
                                                    "amount" + KV_SEP + t.getAmount(),
                                                    "description" + KV_SEP + t.getDescription(),
                                                    "date" + KV_SEP + t.getDate()
        );
    }

    /**
     * Thrown when a transaction entry in the storage file fails validation.
     */
    private static class CorruptedEntryException extends Exception {
        public CorruptedEntryException(String message) {
            super(message);
        }
    }
}

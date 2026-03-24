package seedu.duke.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import seedu.duke.MoneyBagProMaxException;
import seedu.duke.transaction.Expense;
import seedu.duke.transaction.Income;
import seedu.duke.transactionlist.TransactionList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StorageTest {

    private static final String DATA_FILE = "data/transactions.txt";
    private static final String TMP_FILE  = "data/transactions.txt.tmp";
    
    private Storage storage;
    private TransactionList list;

    @BeforeEach
    void setUp() {
        storage = new Storage();
        list = new TransactionList();
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(DATA_FILE));
        Files.deleteIfExists(Paths.get(TMP_FILE));
    }

    // load()
    @Test
    void load_noFileExists_createsEmptyFile() throws MoneyBagProMaxException {
        storage.load(list);
        assertTrue(Files.exists(Paths.get(DATA_FILE)));
        assertEquals(0, list.size());
    }

    @Test
    void load_emptyFile_listRemainsEmpty() throws MoneyBagProMaxException, IOException {
        Files.createDirectories(Paths.get("data"));
        Files.createFile(Paths.get(DATA_FILE));
        storage.load(list);
        assertEquals(0, list.size());
    }

    @Test
    void load_validExpense_restoredCorrectly() throws MoneyBagProMaxException {
        list.add(new Expense("food", 10.0, "lunch",
                             LocalDate.of(2026, 3, 23)));
        storage.save(list);

        TransactionList loaded = new TransactionList();
        storage.load(loaded);

        assertEquals(1, loaded.size());
        assertEquals("food", loaded.get(0).getCategory());
        assertEquals(10.0, loaded.get(0).getAmount());
        assertEquals("lunch", loaded.get(0).getDescription());
        assertEquals(LocalDate.of(2026, 3, 23), loaded.get(0).getDate());
        assertEquals("expense", loaded.get(0).getType());
    }

    @Test
    void load_validIncome_restoredCorrectly() throws MoneyBagProMaxException {
        list.add(new Income("salary", 3000.0, "march pay",
                            LocalDate.of(2026, 3, 1)));
        storage.save(list);

        TransactionList loaded = new TransactionList();
        storage.load(loaded);

        assertEquals(1, loaded.size());
        assertEquals("salary",  loaded.get(0).getCategory());
        assertEquals(3000.0,    loaded.get(0).getAmount());
        assertEquals("income",  loaded.get(0).getType());
    }

    @Test
    void load_multipleTransactions_allRestored() throws MoneyBagProMaxException {
        list.add(new Expense("food",      10.0,   "lunch",
                             LocalDate.of(2026, 3, 23)));
        list.add(new Income("salary",     3000.0, "march pay",
                            LocalDate.of(2026, 3, 1)));
        list.add(new Expense("transport", 2.50,   "bus",
                             LocalDate.of(2026, 3, 22)));
        storage.save(list);

        TransactionList loaded = new TransactionList();
        storage.load(loaded);

        assertEquals(3, loaded.size());
    }

    @Test
    void load_malformedLine_skipped() throws MoneyBagProMaxException, IOException {
        Files.createDirectories(Paths.get("data"));
        Files.writeString(Paths.get(DATA_FILE),
                          "[TXN] | type=expense | category=food | amount=INVALID | date=2026-03-23\n");

        storage.load(list);
        assertEquals(0, list.size()); // malformed line skipped, no crash
    }

    @Test
    void load_unknownType_skipped() throws MoneyBagProMaxException, IOException {
        Files.createDirectories(Paths.get("data"));
        Files.writeString(Paths.get(DATA_FILE),
                          "[TXN] | type=transfer | category=misc | amount=50.0 | description= | date=2026-03-23\n");

        storage.load(list);
        assertEquals(0, list.size()); // unknown type skipped, no crash
    }

    @Test
    void load_missingDescriptionField_defaultsToEmpty() throws MoneyBagProMaxException, IOException {
        Files.createDirectories(Paths.get("data"));
        Files.writeString(Paths.get(DATA_FILE),
                          "[TXN] | type=expense | category=food | amount=10.0 | date=2026-03-23\n");

        storage.load(list);
        assertEquals(1, list.size());
        assertEquals("", list.get(0).getDescription());
    }

    // save()
    @Test
    void save_emptyList_writesEmptyFile() throws MoneyBagProMaxException {
        storage.save(list);
        assertTrue(Files.exists(Paths.get(DATA_FILE)));
    }

    @Test
    void save_afterDelete_fileReflectsDeletion() throws MoneyBagProMaxException {
        list.add(new Expense("food",      10.0, "lunch",
                             LocalDate.of(2026, 3, 23)));
        list.add(new Expense("transport", 2.50, "bus",
                             LocalDate.of(2026, 3, 22)));
        storage.save(list);

        list.remove(0);
        storage.save(list);

        TransactionList loaded = new TransactionList();
        storage.load(loaded);
        assertEquals(1, loaded.size());
        assertEquals("transport", loaded.get(0).getCategory());
    }

    @Test
    void save_overwritesPreviousSave() throws MoneyBagProMaxException {
        list.add(new Expense("food", 10.0, "lunch",
                             LocalDate.of(2026, 3, 23)));
        storage.save(list);

        // Save again with different data
        TransactionList updated = new TransactionList();
        updated.add(new Income("salary", 500.0, "allowance",
                               LocalDate.of(2026, 3, 1)));
        storage.save(updated);

        TransactionList loaded = new TransactionList();
        storage.load(loaded);
        assertEquals(1, loaded.size());
        assertEquals("income", loaded.get(0).getType());
    }

    // save -> load tests
    @Test
    void load_expenseWithEmptyDescription_preservedCorrectly() throws MoneyBagProMaxException {
        list.add(new Expense("misc", 5.0, "", 
                             LocalDate.of(2026, 1, 15)));
        storage.save(list);

        TransactionList loaded = new TransactionList();
        storage.load(loaded);

        assertEquals("",     loaded.get(0).getDescription());
        assertEquals(5.0,    loaded.get(0).getAmount());
        assertEquals("misc", loaded.get(0).getCategory());
    }

    @Test
    void load_nonPrefixedLines_skipped() throws MoneyBagProMaxException, IOException {
        Files.createDirectories(Paths.get("data"));
        Files.writeString(Paths.get(DATA_FILE),
                          "# this is a comment\n\n"
                                  + "[TXN] | type=expense | category=food | amount=10.0"
                                  + " | description=lunch | date=2026-03-23\n"
                                  + "some random line\n");
        storage.load(list);
        assertEquals(1, list.size());
    }

    @Test
    void load_clearsExistingListBeforeLoading() throws MoneyBagProMaxException {
        list.add(new Expense("food", 10.0, "lunch", 
                             LocalDate.of(2026, 3, 23)));
        list.add(new Expense("transport", 2.50, "bus", 
                             LocalDate.of(2026, 3, 22)));
        storage.save(list);

        // Load into a list that already has a transaction
        TransactionList loaded = new TransactionList();
        loaded.add(new Expense("misc", 99.0, "stale", 
                               LocalDate.of(2026, 1, 1)));
        storage.load(loaded);

        assertEquals(2, loaded.size()); // stale entry replaced, not appended
    }

    @Test
    void save_andLoad_preservesTransactionOrder() throws MoneyBagProMaxException {
        list.add(new Expense("food",      10.0,  "lunch", 
                             LocalDate.of(2026, 3, 23)));
        list.add(new Income("salary",     3000.0, "pay",
                            LocalDate.of(2026, 3, 1)));
        list.add(new Expense("transport", 2.50,  "bus",
                             LocalDate.of(2026, 3, 22)));
        storage.save(list);

        TransactionList loaded = new TransactionList();
        storage.load(loaded);

        assertEquals("food",      loaded.get(0).getCategory());
        assertEquals("salary",    loaded.get(1).getCategory());
        assertEquals("transport", loaded.get(2).getCategory());
    }

    @Test
    void save_noDataDirectory_createsItAndSaves() throws MoneyBagProMaxException, IOException {
        // Remove the data directory if it exists
        Files.deleteIfExists(Paths.get(DATA_FILE));
        Files.deleteIfExists(Paths.get("data"));

        list.add(new Expense("food", 10.0, "lunch",
                             LocalDate.of(2026, 3, 23)));
        storage.save(list); // should create data/ and write the file

        assertTrue(Files.exists(Paths.get(DATA_FILE)));
    }
}

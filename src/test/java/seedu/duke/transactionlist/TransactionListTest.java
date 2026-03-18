package seedu.duke.transactionlist;

import org.junit.jupiter.api.Test;
import seedu.duke.transaction.Expense;
import seedu.duke.transaction.Transaction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransactionListTest {

    @Test
    public void add_transaction_sizeIncreases() {
        TransactionList list = new TransactionList();
        Expense expense = new Expense("food", 5.0);
        list.add(expense);
        assertEquals(1, list.size());
    }

    @Test
    public void get_validIndex_returnsCorrectTransaction() {
        TransactionList list = new TransactionList();
        Expense expense = new Expense("food", 5.0);
        list.add(expense);
        Transaction result = list.get(0);
        assertEquals(expense, result);
    }

    @Test
    public void remove_validIndex_transactionRemoved() {
        TransactionList list = new TransactionList();
        Expense expense1 = new Expense("food", 5.0);
        Expense expense2 = new Expense("transport", 2.0);

        list.add(expense1);
        list.add(expense2);
        Transaction removed = list.remove(0);
        assertEquals(expense1, removed);
        assertEquals(1, list.size());
        assertEquals(expense2, list.get(0));
    }

    @Test
    public void isEmpty_newList_returnsTrue() {
        TransactionList list = new TransactionList();
        assertTrue(list.isEmpty());
    }

    @Test
    public void isEmpty_afterAdd_returnsFalse() {
        TransactionList list = new TransactionList();
        list.add(new Expense("food", 5.0));
        assertFalse(list.isEmpty());
    }

    @Test
    public void size_multipleAdds_returnsCorrectSize() {
        TransactionList list = new TransactionList();

        list.add(new Expense("food", 5.0));
        list.add(new Expense("transport", 2.0));
        list.add(new Expense("rent", 100.0));

        assertEquals(3, list.size());
    }

    @Test
    public void remove_lastTransaction_removedCorrectly() {
        TransactionList list = new TransactionList();

        Expense expense1 = new Expense("food", 5.0);
        Expense expense2 = new Expense("transport", 2.0);

        list.add(expense1);
        list.add(expense2);

        Transaction removed = list.remove(1);

        assertEquals(expense2, removed);
        assertEquals(1, list.size());
    }

    @Test
    public void get_firstTransaction_returnsCorrectTransaction() {
        TransactionList list = new TransactionList();

        Expense expense1 = new Expense("food", 5.0);
        Expense expense2 = new Expense("transport", 2.0);

        list.add(expense1);
        list.add(expense2);

        assertEquals(expense1, list.get(0));
    }

    @Test
    public void get_lastTransaction_returnsCorrectTransaction() {
        TransactionList list = new TransactionList();

        Expense expense1 = new Expense("food", 5.0);
        Expense expense2 = new Expense("transport", 2.0);

        list.add(expense1);
        list.add(expense2);

        assertEquals(expense2, list.get(1));
    }

    @Test
    public void remove_firstTransaction_remainingTransactionsShiftCorrectly() {
        TransactionList list = new TransactionList();

        Expense expense1 = new Expense("food", 5.0);
        Expense expense2 = new Expense("transport", 2.0);

        list.add(expense1);
        list.add(expense2);

        list.remove(0);

        assertEquals(expense2, list.get(0));
    }

    @Test
    public void remove_middleTransaction_listUpdatesCorrectly() {
        TransactionList list = new TransactionList();

        Expense expense1 = new Expense("food", 5.0);
        Expense expense2 = new Expense("transport", 2.0);
        Expense expense3 = new Expense("rent", 100.0);

        list.add(expense1);
        list.add(expense2);
        list.add(expense3);

        list.remove(1);

        assertEquals(expense3, list.get(1));
    }
}

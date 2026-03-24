package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.MoneyBagProMaxException;
import seedu.duke.budget.Budget;
import seedu.duke.transaction.Expense;
import seedu.duke.transaction.Transaction;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;
import seedu.duke.undoredo.UndoRedoManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeleteCommandTest {

    private TransactionList list;
    private Budget budget;
    private Ui ui;
    private UndoRedoManager undoRedoManager;

    @BeforeEach
    public void setUp() {
        list = new TransactionList();
        budget = new Budget();
        ui = new Ui();
        undoRedoManager = new UndoRedoManager();
    }

    @Test
    public void execute_validIndex_deletesCorrectTransaction() throws MoneyBagProMaxException {
        Transaction t1 = new Expense("food", 10.0);
        Transaction t2 = new Expense("transport", 5.0);

        list.add(t1);
        list.add(t2);

        DeleteCommand command = new DeleteCommand(1, undoRedoManager);
        command.execute(list, budget, ui);

        assertEquals(1, list.size());
        assertEquals(t2, list.get(0));
    }

    @Test
    public void execute_deleteFirstTransaction_remainingTransactionsShift() throws MoneyBagProMaxException {
        Transaction t1 = new Expense("food", 10.0);
        Transaction t2 = new Expense("transport", 5.0);
        Transaction t3 = new Expense("medical", 20.0);

        list.add(t1);
        list.add(t2);
        list.add(t3);

        DeleteCommand command = new DeleteCommand(1, undoRedoManager);
        command.execute(list, budget, ui);

        assertEquals(2, list.size());
        assertEquals(t2, list.get(0));
        assertEquals(t3, list.get(1));
    }

    @Test
    public void execute_deleteLastTransaction_removesCorrectTransaction() throws MoneyBagProMaxException {
        Transaction t1 = new Expense("food", 10.0);
        Transaction t2 = new Expense("transport", 5.0);

        list.add(t1);
        list.add(t2);

        DeleteCommand command = new DeleteCommand(2, undoRedoManager);
        command.execute(list, budget, ui);

        assertEquals(1, list.size());
        assertEquals(t1, list.get(0));
    }

    @Test
    public void execute_invalidIndex_throwsException() {
        Transaction t1 = new Expense("food", 10.0);
        list.add(t1);

        DeleteCommand command = new DeleteCommand(99, undoRedoManager);

        assertThrows(MoneyBagProMaxException.class, () -> command.execute(list, budget, ui));
    }

    @Test
    public void execute_emptyList_throwsException() {
        DeleteCommand command = new DeleteCommand(1, undoRedoManager);

        assertThrows(MoneyBagProMaxException.class, () -> command.execute(list, budget, ui));
    }
}

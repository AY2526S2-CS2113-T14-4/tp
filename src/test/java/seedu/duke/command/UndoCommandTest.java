package seedu.duke.command;

import seedu.duke.MoneyBagProMaxException;
import seedu.duke.budget.Budget;
import seedu.duke.transaction.Transaction;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;
import seedu.duke.undoredo.UndoRedoManager;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UndoCommandTest {
    Budget budget = new Budget();

    @Test
    public void undoAdd_removesTransaction() throws MoneyBagProMaxException {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();
        UndoRedoManager manager = new UndoRedoManager();

        AddCommand addCmd = new AddCommand("food", 10.00, "lunch",
                LocalDate.of(2026, 3, 20), manager);
        addCmd.execute(list, budget, ui);
        assertEquals(1, list.size());

        UndoCommand undoCmd = new UndoCommand(manager);
        undoCmd.execute(list, budget, ui);
        assertEquals(0, list.size());
    }

    @Test
    public void undoDelete_restoresTransactionAtOriginalIndex() throws MoneyBagProMaxException {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();
        UndoRedoManager manager = new UndoRedoManager();

        // Add 3 transactions
        new AddCommand("food", 10.00, "item1", LocalDate.of(2026, 3, 20), manager).execute(list, budget, ui);
        new AddCommand("salary", 500.00, "item2", LocalDate.of(2026, 3, 20), manager).execute(list, budget, ui);
        new AddCommand("transport", 5.00, "item3", LocalDate.of(2026, 3, 20), manager).execute(list, budget, ui);
        assertEquals(3, list.size());

        Transaction secondItem = list.get(1);

        // Delete second item (user-facing index 2)
        DeleteCommand deleteCmd = new DeleteCommand(2, manager);
        deleteCmd.execute(list, budget, ui);
        assertEquals(2, list.size());

        // Undo should restore at index 1
        UndoCommand undoCmd = new UndoCommand(manager);
        undoCmd.execute(list, budget, ui);
        assertEquals(3, list.size());
        assertEquals(secondItem, list.get(1));
    }

    @Test
    public void undoWithNothingToUndo_throwsException() {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();
        UndoRedoManager manager = new UndoRedoManager();

        UndoCommand undoCmd = new UndoCommand(manager);
        MoneyBagProMaxException e = assertThrows(MoneyBagProMaxException.class,
                () -> undoCmd.execute(list, budget, ui));
        assertEquals("[ERROR!] Nothing to undo.", e.getMessage());
    }
}

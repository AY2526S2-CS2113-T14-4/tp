package seedu.duke.command;

import seedu.duke.MoneyBagProMaxException;
import seedu.duke.budget.Budget;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;
import seedu.duke.undoredo.UndoRedoManager;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RedoCommandTest {
    Budget budget = new Budget();
    @Test
    public void redoAfterUndoAdd_reAddsTransaction() throws MoneyBagProMaxException {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();
        UndoRedoManager manager = new UndoRedoManager();

        new AddCommand("food", 10.00, "lunch", LocalDate.of(2026, 3, 20), manager).execute(list, budget, ui);
        assertEquals(1, list.size());

        new UndoCommand(manager).execute(list, budget, ui);
        assertEquals(0, list.size());

        new RedoCommand(manager).execute(list, budget, ui);
        assertEquals(1, list.size());
        assertEquals(10.00, list.get(0).getAmount(), 0.001);
    }

    @Test
    public void redoAfterUndoDelete_reDeletesTransaction() throws MoneyBagProMaxException {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();
        UndoRedoManager manager = new UndoRedoManager();

        new AddCommand("food", 10.00, "lunch", LocalDate.of(2026, 3, 20), manager).execute(list, budget, ui);
        new DeleteCommand(1, manager).execute(list, budget, ui);
        assertEquals(0, list.size());

        // Undo the delete (restores transaction)
        new UndoCommand(manager).execute(list, budget, ui);
        assertEquals(1, list.size());

        // Redo the delete (removes it again)
        new RedoCommand(manager).execute(list, budget, ui);
        assertEquals(0, list.size());
    }

    @Test
    public void redoWithNothingToRedo_throwsException() {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();
        UndoRedoManager manager = new UndoRedoManager();

        RedoCommand redoCmd = new RedoCommand(manager);
        MoneyBagProMaxException e = assertThrows(MoneyBagProMaxException.class,
                () -> redoCmd.execute(list, budget, ui));
        assertEquals("[ERROR!] Nothing to redo.", e.getMessage());
    }
}

package seedu.duke.command;

import seedu.duke.MoneyBagProMaxException;
import seedu.duke.transaction.Expense;
import seedu.duke.transaction.Transaction;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;
import seedu.duke.undoredo.UndoRedoManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EditCommandTest {

    private TransactionList list;
    private UndoRedoManager undoRedoManager;
    private Ui ui;
    private final LocalDate testDate = LocalDate.of(2026, 3, 20);

    @BeforeEach
    void setUp() {
        list = new TransactionList();
        undoRedoManager = new UndoRedoManager();
        ui = new Ui();
        list.add(new Expense("food", 10.00, "lunch", testDate));
        list.add(new Expense("transport", 5.00, "bus", testDate));
    }

    @Test
    void execute_validEdit_replacesTransactionAtIndex() throws MoneyBagProMaxException {
        new EditCommand(1, "medical", 50.00, "checkup", testDate, undoRedoManager).execute(list, ui);

        Transaction result = list.get(0);
        assertEquals("medical", result.getCategory());
        assertEquals(50.00, result.getAmount());
        assertEquals("checkup", result.getDescription());
    }

    @Test
    void execute_validEdit_doesNotAffectOtherTransactions() throws MoneyBagProMaxException {
        Transaction originalSecond = list.get(1);
        new EditCommand(1, "medical", 50.00, "checkup", testDate, undoRedoManager).execute(list, ui);
        assertEquals(originalSecond, list.get(1));
    }

    @Test
    void execute_thenUndo_restoresOriginalTransaction() throws MoneyBagProMaxException {
        Transaction original = list.get(0);
        new EditCommand(1, "medical", 50.00, "checkup", testDate, undoRedoManager).execute(list, ui);
        new UndoCommand(undoRedoManager).execute(list, ui);
        assertEquals(original, list.get(0));
    }

    @Test
    void execute_thenUndoThenRedo_reappliesEdit() throws MoneyBagProMaxException {
        new EditCommand(1, "medical", 50.00, "checkup", testDate, undoRedoManager).execute(list, ui);
        Transaction edited = list.get(0);
        new UndoCommand(undoRedoManager).execute(list, ui);
        new RedoCommand(undoRedoManager).execute(list, ui);
        assertEquals(edited, list.get(0));
    }

    @Test
    void execute_outOfBoundsIndex_throwsException() {
        assertThrows(MoneyBagProMaxException.class,
                () -> new EditCommand(99, "food", 10.00, "", testDate, undoRedoManager).execute(list, ui));
    }
}

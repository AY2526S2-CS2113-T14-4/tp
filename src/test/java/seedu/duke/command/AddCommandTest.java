package seedu.duke.command;

import seedu.duke.budget.Budget;
import seedu.duke.transaction.Expense;
import seedu.duke.transaction.Income;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;
import seedu.duke.undoredo.UndoRedoManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class AddCommandTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void execute_validIncomeCategory_addsIncomeToList() {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();

        AddCommand command = new AddCommand("salary", 1000.00, "monthly", LocalDate.of(2026, 3, 18),
                new UndoRedoManager());
        Budget budget = new Budget();
        command.execute(list, budget, ui);

        assertEquals(1, list.size());
        assertInstanceOf(Income.class, list.get(0));
        assertEquals(1000.00, list.get(0).getAmount(), 0.001);
    }

    @Test
    public void execute_validExpenseCategory_addsExpenseToList() {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();

        AddCommand command = new AddCommand("food", 10.00, "lunch", LocalDate.of(2026, 3, 18),
                new UndoRedoManager());
        Budget budget = new Budget();
        command.execute(list, budget, ui);

        assertEquals(1, list.size());
        assertInstanceOf(Expense.class, list.get(0));
        assertEquals(10.00, list.get(0).getAmount(), 0.001);
    }

    @Test
    public void execute_invalidCategory_showsErrorMessage() {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();

        AddCommand command = new AddCommand("invalid", 10.00, "test", LocalDate.of(2026, 3, 18),
                new UndoRedoManager());
        Budget budget = new Budget();
        command.execute(list, budget, ui);

        assertEquals(0, list.size());
        String output = outContent.toString();
        assert output.contains("Invalid category 'invalid'");
    }
}

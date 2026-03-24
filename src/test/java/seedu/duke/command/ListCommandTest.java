package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.budget.Budget;
import seedu.duke.transaction.Expense;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListCommandTest {

    private TransactionList list;
    private Budget budget;
    private Ui ui;

    @BeforeEach
    public void setUp() {
        list = new TransactionList();
        budget = new Budget();
        ui = new Ui();
    }

    @Test
    public void execute_emptyList_doesNotThrowException() {
        ListCommand command = new ListCommand();
        assertDoesNotThrow(() -> command.execute(list, budget, ui));
        assertEquals(0, list.size());
    }

    @Test
    public void execute_nonEmptyList_doesNotThrowException() {
        list.add(new Expense("food", 10.0));
        list.add(new Expense("transport", 5.0));

        ListCommand command = new ListCommand();
        assertDoesNotThrow(() -> command.execute(list, budget, ui));

        assertEquals(2, list.size());
    }

    @Test
    public void execute_nonEmptyList_listRemainsUnchanged() {
        list.add(new Expense("food", 10.0));
        list.add(new Expense("transport", 5.0));

        ListCommand command = new ListCommand();
        assertDoesNotThrow(() -> command.execute(list, budget, ui));

        assertEquals("food", list.get(0).getCategory());
        assertEquals(10.0, list.get(0).getAmount());
        assertEquals("transport", list.get(1).getCategory());
        assertEquals(5.0, list.get(1).getAmount());
    }
}

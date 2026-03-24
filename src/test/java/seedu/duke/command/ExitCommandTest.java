package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.budget.Budget;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ExitCommandTest {

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
    public void execute_exitCommand_doesNotThrowException() {
        ExitCommand command = new ExitCommand();
        assertDoesNotThrow(() -> command.execute(list, budget, ui));
    }
}

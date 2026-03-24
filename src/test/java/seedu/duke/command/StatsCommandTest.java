package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.budget.Budget;
import seedu.duke.transaction.Expense;
import seedu.duke.transaction.Income;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class StatsCommandTest {

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
    public void execute_emptyList_noException() {
        StatsCommand command = new StatsCommand();
        assertDoesNotThrow(() -> command.execute(list, budget, ui));
    }

    @Test
    public void execute_withTransactions_noException() {
        list.add(new Expense("food", 10.0, "", LocalDate.now()));
        list.add(new Expense("transport", 5.0, "", LocalDate.now()));
        list.add(new Income("salary", 1000.0, "", LocalDate.now()));

        StatsCommand command = new StatsCommand();
        assertDoesNotThrow(() -> command.execute(list, budget, ui));
    }

    @Test
    public void execute_withBudget_noException() {
        budget.setMonthlyBudget(200);

        list.add(new Expense("food", 50.0, "", LocalDate.now()));
        list.add(new Expense("transport", 30.0, "", LocalDate.now()));

        StatsCommand command = new StatsCommand();
        assertDoesNotThrow(() -> command.execute(list, budget, ui));
    }
}

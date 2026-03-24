package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.budget.Budget;
import seedu.duke.transaction.Expense;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BudgetCommandTest {

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
    public void execute_setBudget_budgetUpdated() {
        BudgetCommand command = new BudgetCommand("set", 200);

        assertDoesNotThrow(() -> command.execute(list, budget, ui));
        assertEquals(200.0, budget.getMonthlyBudget());
    }

    @Test
    public void execute_statusWithoutBudget_noException() {
        BudgetCommand command = new BudgetCommand("status", 0);

        assertDoesNotThrow(() -> command.execute(list, budget, ui));
        assertFalse(budget.hasBudget());
    }

    @Test
    public void execute_statusWithBudget_noException() {
        budget.setMonthlyBudget(300);
        BudgetCommand command = new BudgetCommand("status", 0);

        assertDoesNotThrow(() -> command.execute(list, budget, ui));
        assertEquals(300.0, budget.getMonthlyBudget());
    }

    @Test
    public void execute_statusWithExpenses_noException() {
        budget.setMonthlyBudget(200);

        list.add(new Expense("food", 20.0, "", LocalDate.now()));
        list.add(new Expense("transport", 10.0, "", LocalDate.now()));

        BudgetCommand command = new BudgetCommand("status", 0);

        assertDoesNotThrow(() -> command.execute(list, budget, ui));
        assertEquals(200.0, budget.getMonthlyBudget());
    }

    @Test
    public void execute_setThenStatus_budgetPersists() {
        BudgetCommand setCommand = new BudgetCommand("set", 500);
        BudgetCommand statusCommand = new BudgetCommand("status", 0);

        assertDoesNotThrow(() -> setCommand.execute(list, budget, ui));
        assertDoesNotThrow(() -> statusCommand.execute(list, budget, ui));

        assertEquals(500.0, budget.getMonthlyBudget());
    }
}

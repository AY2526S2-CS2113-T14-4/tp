package seedu.duke.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.budget.Budget;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HelpCommandTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private TransactionList list;
    private Budget budget;
    private Ui ui;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStream));
        list = new TransactionList();
        budget = new Budget();
        ui = new Ui();
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void execute_helpCommand_containsAddCommand() {
        HelpCommand command = new HelpCommand();
        command.execute(list, budget, ui);

        String output = outputStream.toString();
        assertTrue(output.contains("add"));
    }

    @Test
    public void execute_helpCommand_containsListCommand() {
        HelpCommand command = new HelpCommand();
        command.execute(list, budget, ui);

        String output = outputStream.toString();
        assertTrue(output.contains("list"));
    }

    @Test
    public void execute_helpCommand_containsDeleteCommand() {
        HelpCommand command = new HelpCommand();
        command.execute(list, budget, ui);

        String output = outputStream.toString();
        assertTrue(output.contains("delete"));
    }

    @Test
    public void execute_helpCommand_containsBudgetCommand() {
        HelpCommand command = new HelpCommand();
        command.execute(list, budget, ui);

        String output = outputStream.toString();
        assertTrue(output.contains("budget"));
    }

    @Test
    public void execute_helpCommand_containsStatsCommand() {
        HelpCommand command = new HelpCommand();
        command.execute(list, budget, ui);

        String output = outputStream.toString();
        assertTrue(output.contains("stats"));
    }

    @Test
    public void execute_helpCommand_containsExitCommand() {
        HelpCommand command = new HelpCommand();
        command.execute(list, budget, ui);

        String output = outputStream.toString();
        assertTrue(output.contains("exit"));
    }
}

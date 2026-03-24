package seedu.duke.command;

import seedu.duke.MoneyBagProMaxException;
import seedu.duke.budget.Budget;
import seedu.duke.transaction.Expense;
import seedu.duke.transaction.Income;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FindCommandTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final Budget budget = new Budget();

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void execute_validKeyword_printsMatchingTransactions() throws MoneyBagProMaxException {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();

        list.add(new Expense("food", 10.50, "lunch", LocalDate.parse("2026-03-14")));
        list.add(new Income("salary", 50.00, "pocket money", LocalDate.parse("2026-03-15")));
        list.add(new Expense("transport", 5.00, "bus to lunch", LocalDate.parse("2026-03-16")));

        Command command = new FindCommand("lunch");
        command.execute(list, budget, ui);

        String expectedOutput = "Found 2 matching transaction(s):" + System.lineSeparator() +
                "1. [Expense] food \"lunch\" $10.50 (2026-03-14)" + System.lineSeparator() +
                "3. [Expense] transport \"bus to lunch\" $5.00 (2026-03-16)" + System.lineSeparator();

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void execute_nonExistingKeyword_printsNoMatch() throws MoneyBagProMaxException {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();

        list.add(new Expense("food", 10.50, "lunch", LocalDate.parse("2026-03-14")));

        Command command = new FindCommand("elephant");
        command.execute(list, budget, ui);

        String expectedOutput = "No matching transactions found for: elephant" + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }
}

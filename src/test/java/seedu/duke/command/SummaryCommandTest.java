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

// structure of test names: methodToTest_input_expectedOutput
class SummaryCommandTest {

    // these variables help us capture the console output
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
    public void execute_summaryAll_printsCorrectTotals() throws MoneyBagProMaxException {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();

        list.add(new Expense("food", 10.50, "lunch", LocalDate.now()));
        list.add(new Expense("transport", 5.00, "bus", LocalDate.now()));
        list.add(new Income("salary", 25.50, "monthly", LocalDate.now()));

        Command command = new SummaryCommand("all");
        command.execute(list, budget, ui); // Execute directly!

        String expectedOutput = "===== Overall Summary =====" + System.lineSeparator() +
                "Total Income: $25.50" + System.lineSeparator() +
                "Total Expense: $15.50" + System.lineSeparator() +
                "Net Balance: $10.00" + System.lineSeparator() +
                "===========================" + System.lineSeparator();

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void execute_emptyList_printsNoTransactionsMessage() throws MoneyBagProMaxException {
        TransactionList emptyList = new TransactionList();
        Ui ui = new Ui();

        Command command = new SummaryCommand("all");
        command.execute(emptyList, budget, ui);

        String expectedOutput = "No transactions found to summarise." + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }
}

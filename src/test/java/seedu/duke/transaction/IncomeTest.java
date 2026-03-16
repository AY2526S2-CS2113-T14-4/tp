package seedu.duke.transaction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.MoneyBagProMaxException;
import seedu.duke.command.Command;
import seedu.duke.parser.Parser;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IncomeTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        // re-routes System.out to our outContent stream before each test
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        // puts System.out back to normal after the test finishes
        System.setOut(originalOut);
    }
    
    @Test
    public void parseAddCommand_incomeCategory_addsIncomeObject() throws MoneyBagProMaxException {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();
        Parser parser = new Parser();

        // Input simulates adding income
        Command command = parser.parse("add income/50.00 desc/Salary d/2023-10-01");
        command.execute(list, ui);

        assertEquals(1, list.size(), "List should have 1 transaction");

        // This is the crucial check: verifying it is an Income instance, not Expense
        boolean isIncomeInstance = list.get(0) instanceof Income;
        assertTrue(isIncomeInstance, "The added transaction should be an instance of Income");
        assertEquals(50.00, list.get(0).getAmount(), 0.001);
    }

    @Test
    public void parseSummaryCommand_onlyIncome_printsCorrectTotals() throws MoneyBagProMaxException {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();
        Parser parser = new Parser();

        list.add(new Income("salary", 5000.00, "monthly", LocalDate.now()));

        Command command = parser.parse("summary");
        command.execute(list, ui);

        // Verify that Total Expense is $0.00 and Net Balance matches Income
        String expectedOutput = "===== Overall Summary =====" + System.lineSeparator() +
                "Total Income: $5000.00" + System.lineSeparator() +
                "Total Expense: $0.00" + System.lineSeparator() +
                "Net Balance: $5000.00" + System.lineSeparator() +
                "===========================" + System.lineSeparator();

        assertEquals(expectedOutput, outContent.toString());
    }
}

package seedu.duke;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

// structure of test names: methodToTest_input_expectedOutput
class ParserTest {

    // these variables help us capture the console output
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
    public void parseSummaryCommand_summary_printsCorrectTotals() {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();
        Parser parser = new Parser();

        list.add(new Expense("food", 10.50, "lunch", LocalDate.now()));
        list.add(new Expense("transport", 5.00, "bus", LocalDate.now()));

        parser.parse("summary", list, ui);

        String expectedOutput = "----- Overall Summary -----" + System.lineSeparator() +
                "Total Income: $0.00" + System.lineSeparator() +
                "Total Expense: $15.50" + System.lineSeparator() +
                "Net Balance: $-15.50" + System.lineSeparator() +
                "--------------------------" + System.lineSeparator();

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void parseSummaryCommand_EmptyList_printsNoTransactionsMessage() {
        TransactionList emptyList = new TransactionList();
        Ui ui = new Ui();
        Parser parser = new Parser();

        parser.parse("summary all", emptyList, ui);

        String expectedOutput = "No transactions found to summarise." + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }
}
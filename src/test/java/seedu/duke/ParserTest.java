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
        list.add(new Income("salary", 25.50, "monthly", LocalDate.now()));


        parser.parse("summary", list, ui);

        String expectedOutput = "----- Overall Summary -----" + System.lineSeparator() +
                "Total Income: $25.50" + System.lineSeparator() +
                "Total Expense: $15.50" + System.lineSeparator() +
                "Net Balance: $10.00" + System.lineSeparator() +
                "--------------------------" + System.lineSeparator();

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void parseSummaryCommand_emptyList_printsNoTransactionsMessage() {
        TransactionList emptyList = new TransactionList();
        Ui ui = new Ui();
        Parser parser = new Parser();

        parser.parse("summary all", emptyList, ui);

        String expectedOutput = "No transactions found to summarise." + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void parseFindCommand_keyword_printsMatchingTransactions() {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();
        Parser parser = new Parser();

        list.add(new Expense("food", 10.50, "lunch", LocalDate.parse("2026-03-14")));
        list.add(new Income("income", 50.00, "pocket money", LocalDate.parse("2026-03-15")));
        list.add(new Expense("transport", 5.00, "bus to lunch", LocalDate.parse("2026-03-16")));

        parser.parse("find lunch", list, ui);

        String expectedOutput = "Found 2 matching transaction(s):" + System.lineSeparator() +
                "1. [Expense] food \"lunch\" $10.50 (2026-03-14)" + System.lineSeparator() +
                "3. [Expense] transport \"bus to lunch\" $5.00 (2026-03-16)" + System.lineSeparator();

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void parseFindCommand_nonExistingKeyword_printsNoMatch() {

        TransactionList list = new TransactionList();
        Ui ui = new Ui();
        Parser parser = new Parser();

        list.add(new Expense("food", 10.50, "lunch", LocalDate.parse("2026-03-14")));

        parser.parse("find elephant", list, ui);
        String expectedOutput = "No matching transactions found for: elephant" + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }
}

package seedu.duke.transaction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.MoneyBagProMaxException;
import seedu.duke.budget.Budget;
import seedu.duke.command.Command;
import seedu.duke.parser.Parser;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;
import seedu.duke.undoredo.UndoRedoManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IncomeTest {
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
    public void parseAddCommand_incomeCategory_addsIncomeObject() throws MoneyBagProMaxException {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();
        Parser parser = new Parser(new UndoRedoManager());

        // Updated to use a valid income category
        Command command = parser.parse("add salary/50.00 desc/monthly d/2023-10-01");
        command.execute(list, budget, ui);

        assertEquals(1, list.size(), "List should have 1 transaction");
        assertInstanceOf(Income.class, list.get(0), "The added transaction should be an instance of Income");
        assertEquals(50.00, list.get(0).getAmount(), 0.001);
    }

    @Test
    public void income_validCategory_createsSuccessfully() {
        Income income = new Income("salary", 1000.00, "monthly", LocalDate.now());
        assertEquals("salary", income.getCategory());
        assertEquals(1000.00, income.getAmount(), 0.001);
        assertEquals("income", income.getType());
    }

    @Test
    public void income_invalidCategory_throwsAssertionError() {
        assertThrows(AssertionError.class, () ->
                new Income("food", 1000.00, "test", LocalDate.now()));
    }

    @Test
    public void income_allValidCategories_createSuccessfully() {
        for (String category : Income.VALID_CATEGORIES) {
            Income income = new Income(category, 100.00, "test", LocalDate.now());
            assertEquals(category, income.getCategory());
        }
    }

    @Test
    public void income_toString_formatsCorrectly() {
        Income income = new Income("salary", 1000.00, "monthly pay", LocalDate.of(2026, 3, 18));
        assertEquals("[Income] salary \"monthly pay\" $1000.00 (2026-03-18)", income.toString());
    }

    @Test
    public void parseSummaryCommand_onlyIncome_printsCorrectTotals() throws MoneyBagProMaxException {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();
        Parser parser = new Parser(new UndoRedoManager());

        list.add(new Income("salary", 5000.00, "monthly", LocalDate.now()));

        Command command = parser.parse("summary");
        command.execute(list, budget, ui);

        String expectedOutput = "===== Overall Summary =====" + System.lineSeparator() +
                "Total Income: $5000.00" + System.lineSeparator() +
                "Total Expense: $0.00" + System.lineSeparator() +
                "Net Balance: $5000.00" + System.lineSeparator() +
                "===========================" + System.lineSeparator();

        assertEquals(expectedOutput, outContent.toString());
    }
}

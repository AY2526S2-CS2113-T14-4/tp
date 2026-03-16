package seedu.duke.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UiTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private String output() {
        return outContent.toString().trim();
    }

    // read input tests
    @Test
    void readInput_simpleCommand_returnsTrimmed() {
        System.setIn(new ByteArrayInputStream("list\n".getBytes()));
        Ui ui = new Ui();
        assertEquals("list", ui.readInput());
    }

    @Test
    void readInput_commandWithWhitespace_returnsTrimmed() {
        System.setIn(new ByteArrayInputStream("  add food/10  \n".getBytes()));
        Ui ui = new Ui();
        assertEquals("add food/10", ui.readInput());
    }

    // welcome message tests
    @Test
    void showWelcomeMessage_containsAppName() {
        new Ui().showWelcomeMessage();
        assertTrue(output().contains("MoneyBagProMax"));
    }

    @Test
    void showWelcomeMessage_containsHelpHint() {
        new Ui().showWelcomeMessage();
        assertTrue(output().contains("help"));
    }

    // help message tests
    @Test
    void showHelp_containsListCommand() {
        new Ui().showHelp();
        assertTrue(output().contains("list"));
    }

    @Test
    void showHelp_containsAddExpenseFormat() {
        new Ui().showHelp();
        assertTrue(output().contains("add [category]/PRICE"));
    }

    @Test
    void showHelp_containsAddIncomeFormat() {
        new Ui().showHelp();
        assertTrue(output().contains("add income/PRICE"));
    }

    @Test
    void showHelp_containsDeleteCommand() {
        new Ui().showHelp();
        assertTrue(output().contains("delete"));
    }

    @Test
    void showHelp_containsSummaryCommand() {
        new Ui().showHelp();
        assertTrue(output().contains("summary"));
    }

    @Test
    void showHelp_containsExitCommand() {
        new Ui().showHelp();
        assertTrue(output().contains("exit"));
    }

    @Test
    void showHelp_containsValidCategories() {
        new Ui().showHelp();
        String out = output();
        assertTrue(out.contains("food"));
        assertTrue(out.contains("transport"));
        assertTrue(out.contains("utilities"));
        assertTrue(out.contains("education"));
        assertTrue(out.contains("rent"));
        assertTrue(out.contains("medical"));
        assertTrue(out.contains("misc"));
    }

    // output format tests
    @Test
    void showMessage_printsExactMessage() {
        new Ui().showMessage("Hello World");
        assertEquals("Hello World", output());
    }

    @Test
    void showMessage_emptyString_printsBlankLine() {
        new Ui().showMessage("");
        assertEquals("", output());
    }

    // overall summary tests
    @Test
    void showOverallSummary_containsHeader() {
        new Ui().showOverallSummary(1000.0, 500.0);
        assertTrue(output().contains("Overall Summary"));
    }

    @Test
    void showOverallSummary_formatsIncomeCorrectly() {
        new Ui().showOverallSummary(1000.0, 500.0);
        assertTrue(output().contains("$1000.00"));
    }

    @Test
    void showOverallSummary_formatsExpenseCorrectly() {
        new Ui().showOverallSummary(1000.0, 500.0);
        assertTrue(output().contains("$500.00"));
    }

    @Test
    void showOverallSummary_calculatesNetBalanceCorrectly() {
        new Ui().showOverallSummary(1000.0, 500.0);
        assertTrue(output().contains("$500.00")); // net = 1000 - 500
    }

    @Test
    void showOverallSummary_negativeBalance_showsNegativeNet() {
        new Ui().showOverallSummary(200.0, 500.0);
        assertTrue(output().contains("$-300.00"));
    }

    @Test
    void showOverallSummary_zeroValues_showsZeroes() {
        new Ui().showOverallSummary(0.0, 0.0);
        String out = output();
        assertTrue(out.contains("$0.00"));
    }

    // category summary tests
    @Test
    void showCategorySummary_containsCategoryName() {
        new Ui().showCategorySummary("food", 123.45);
        assertTrue(output().contains("food"));
    }

    @Test
    void showCategorySummary_formatsAmountCorrectly() {
        new Ui().showCategorySummary("food", 123.45);
        assertTrue(output().contains("$123.45"));
    }

    @Test
    void showCategorySummary_zeroAmount_showsZero() {
        new Ui().showCategorySummary("transport", 0.0);
        assertTrue(output().contains("$0.00"));
    }

    @Test
    void showCategorySummary_differentCategories_correctOutput() {
        new Ui().showCategorySummary("rent", 800.00);
        String out = output();
        assertTrue(out.contains("rent"));
        assertTrue(out.contains("$800.00"));
    }
}

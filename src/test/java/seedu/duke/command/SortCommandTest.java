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
import static org.junit.jupiter.api.Assertions.assertThrows;

class SortCommandTest {

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
    public void execute_sortByDate_printsChronologicalOrder() throws MoneyBagProMaxException {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();

        list.add(new Expense("food", 10.50, "lunch", LocalDate.parse("2026-03-16")));
        list.add(new Income("salary", 50.00, "pay", LocalDate.parse("2026-03-14")));
        list.add(new Expense("transport", 5.00, "bus", LocalDate.parse("2026-03-15")));

        Command command = new SortCommand("date");
        command.execute(list, budget, ui);

        String expectedOutput = "Transactions sorted by date:" + System.lineSeparator()
                + "1. [Income] salary \"pay\" $50.00 (2026-03-14)" + System.lineSeparator()
                + "2. [Expense] transport \"bus\" $5.00 (2026-03-15)" + System.lineSeparator()
                + "3. [Expense] food \"lunch\" $10.50 (2026-03-16)" + System.lineSeparator();

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void execute_sortByAmount_printsLargestFirst() throws MoneyBagProMaxException {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();

        list.add(new Expense("food", 10.50, "lunch", LocalDate.parse("2026-03-14")));
        list.add(new Income("salary", 50.00, "pay", LocalDate.parse("2026-03-14")));
        list.add(new Expense("transport", 5.00, "bus", LocalDate.parse("2026-03-14")));

        Command command = new SortCommand("amount");
        command.execute(list, budget, ui);

        String expectedOutput = "Transactions sorted by amount:" + System.lineSeparator()
                + "1. [Income] salary \"pay\" $50.00 (2026-03-14)" + System.lineSeparator()
                + "2. [Expense] food \"lunch\" $10.50 (2026-03-14)" + System.lineSeparator()
                + "3. [Expense] transport \"bus\" $5.00 (2026-03-14)" + System.lineSeparator();

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void execute_sortByCategory_printsAlphabeticalOrder() throws MoneyBagProMaxException {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();

        list.add(new Expense("transport", 5.00, "bus", LocalDate.parse("2026-03-14")));
        list.add(new Expense("food", 10.50, "lunch", LocalDate.parse("2026-03-14")));
        list.add(new Income("salary", 50.00, "pay", LocalDate.parse("2026-03-14")));

        Command command = new SortCommand("category");
        command.execute(list, budget, ui);

        String expectedOutput = "Transactions sorted by category:" + System.lineSeparator()
                + "1. [Expense] food \"lunch\" $10.50 (2026-03-14)" + System.lineSeparator()
                + "2. [Income] salary \"pay\" $50.00 (2026-03-14)" + System.lineSeparator()
                + "3. [Expense] transport \"bus\" $5.00 (2026-03-14)" + System.lineSeparator();

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void execute_emptyList_printsNoTransactions() throws MoneyBagProMaxException {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();

        Command command = new SortCommand("date");
        command.execute(list, budget, ui);

        String expectedOutput = "No transactions found." + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void execute_invalidCriteria_throwsException() {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();

        list.add(new Expense("food", 10.50, "lunch", LocalDate.parse("2026-03-14")));

        Command command = new SortCommand("invalid");
        assertThrows(MoneyBagProMaxException.class, () -> command.execute(list, budget, ui));
    }

    @Test
    public void execute_sortByDate_doesNotModifyOriginalList() throws MoneyBagProMaxException {
        TransactionList list = new TransactionList();
        Ui ui = new Ui();

        Expense first = new Expense("food", 10.50, "lunch", LocalDate.parse("2026-03-16"));
        Income second = new Income("salary", 50.00, "pay", LocalDate.parse("2026-03-14"));

        list.add(first);
        list.add(second);

        Command command = new SortCommand("date");
        command.execute(list, budget, ui);

        assertEquals(first, list.get(0));
        assertEquals(second, list.get(1));
    }
}

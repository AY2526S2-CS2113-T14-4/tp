package seedu.duke.transaction;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpenseTest {

    @Test
    public void getType_returnsExpense() {
        Expense expense = new Expense("food", 10.00);
        assertEquals("expense", expense.getType());
    }

    @Test
    public void getAmount_returnsCorrectAmount() {
        Expense expense = new Expense("food", 12.50);
        assertEquals(12.50, expense.getAmount());
    }

    @Test
    public void getCategory_returnsCorrectCategory() {
        Expense expense = new Expense("transport", 3.20);
        assertEquals("transport", expense.getCategory());
    }

    @Test
    public void toString_noDescription_formatsCorrectly() {
        Expense expense = new Expense("food", 10.00, "", LocalDate.of(2025, 3, 1));
        assertEquals("[Expense] food $10.00 (2025-03-01)", expense.toString());
    }

    @Test
    public void toString_withDescription_formatsCorrectly() {
        Expense expense = new Expense("food", 10.00, "lunch", LocalDate.of(2025, 3, 1));
        assertEquals("[Expense] food \"lunch\" $10.00 (2025-03-01)", expense.toString());
    }
}

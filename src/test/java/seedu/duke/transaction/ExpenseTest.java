package seedu.duke.transaction;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
    
    @Test
    public void constructor_allValidCategories_noAssertionError() {
        for (String category : Expense.VALID_CATEGORIES) {
            assertDoesNotThrow(() -> new Expense(category, 5.00),
                    "Category should be valid: " + category);
        }
    }

    @Test
    public void toString_withDescriptionNoDate_usesTodaysDate() {
        Expense expense = new Expense("food", 10.00, "breakfast");
        String expected = String.format("[Expense] food \"breakfast\" $10.00 (%s)", LocalDate.now());
        assertEquals(expected, expense.toString());
    }

    @Test
    public void toString_noDescriptionNoDate_usesTodaysDate() {
        Expense expense = new Expense("rent", 800.00);
        String expected = String.format("[Expense] rent $800.00 (%s)", LocalDate.now());
        assertEquals(expected, expense.toString());
    }

    @Test
    public void toString_wholeNumberAmount_formatsTwoDecimalPlaces() {
        Expense expense = new Expense("utilities", 50.00, "", LocalDate.of(2025, 6, 15));
        assertTrue(expense.toString().contains("$50.00"),
                "Amount should be formatted to 2 decimal places");
    }
}

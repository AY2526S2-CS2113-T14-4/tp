package seedu.duke.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class TransactionTest {

    // ── Expense ───────────────────────────────────────────────────────────────

    @Test
    public void getAmount_expenseWithPositiveValue_returnsCorrectAmount() {
        Expense expense = new Expense("food", 10.50, "lunch", LocalDate.now());
        assertEquals(10.50, expense.getAmount(), 0.001);
    }

    @Test
    public void getType_expense_returnsExpenseString() {
        Expense expense = new Expense("food", 5.00, "snack", LocalDate.now());
        assertEquals("expense", expense.getType());
    }

    @Test
    public void getDescription_expenseWithDescription_returnsDescription() {
        Expense expense = new Expense("transport", 2.00, "bus fare", LocalDate.now());
        assertEquals("bus fare", expense.getDescription());
    }

    @Test
    public void toString_expenseWithDescription_containsCategoryAndAmount() {
        Expense expense = new Expense("food", 8.00, "dinner", LocalDate.of(2025, 3, 1));
        String result = expense.toString();
        assertTrue(result.contains("[Expense]"));
        assertTrue(result.contains("food"));
        assertTrue(result.contains("8.00"));
        assertTrue(result.contains("dinner"));
    }

    @Test
    public void toString_expenseWithoutDescription_omitsQuotedDescription() {
        Expense expense = new Expense("misc", 3.00, "", LocalDate.of(2025, 3, 1));
        String result = expense.toString();
        // No description suffix should appear (no double-quote characters)
        assertTrue(!result.contains("\""));
    }

    // ── Income ────────────────────────────────────────────────────────────────

    @Test
    public void getAmount_incomeWithPositiveValue_returnsCorrectAmount() {
        Income income = new Income("income", 500.00, "salary", LocalDate.now());
        assertEquals(500.00, income.getAmount(), 0.001);
    }

    @Test
    public void getType_income_returnsIncomeString() {
        Income income = new Income("income", 100.00, "freelance", LocalDate.now());
        assertEquals("income", income.getType());
    }

    @Test
    public void toString_incomeWithDescription_containsCategoryAndAmount() {
        Income income = new Income("income", 1000.00, "monthly", LocalDate.of(2025, 3, 1));
        String result = income.toString();
        assertTrue(result.contains("[Income]"));
        assertTrue(result.contains("1000.00"));
        assertTrue(result.contains("monthly"));
    }
}

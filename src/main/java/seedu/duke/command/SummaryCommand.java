package seedu.duke.command;

import seedu.duke.MoneyBagProMaxException;
import seedu.duke.budget.Budget;
import seedu.duke.transaction.Transaction;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;

public class SummaryCommand extends Command {
    private final String summaryType;
    private String targetMonth = null;

    public SummaryCommand(String summaryType) {
        assert summaryType != null : "Summary type should not be null.";
        this.summaryType = summaryType.toLowerCase();
    }

    public SummaryCommand(String summaryType, String targetMonth) {
        assert summaryType != null : "Summary type should not be null.";
        this.summaryType = summaryType.toLowerCase();
        this.targetMonth = targetMonth;
    }

    /**
     * Calculates and displays the summary of transactions based on the requested category.
     *
     * @param list  The current list of transactions.
     * @param ui    The ui instance to display results.
     */
    @Override
    public void execute(TransactionList list, Budget budget, Ui ui) throws MoneyBagProMaxException {
        assert list != null : "List should not be null.";

        if (list.isEmpty()) {
            ui.showMessage("No transactions found to summarise.");
            return;
        }

        YearMonth parsedYearMonth = null;
        if (targetMonth != null) {
            try {
                parsedYearMonth = YearMonth.parse(targetMonth);
            } catch (DateTimeParseException e) {
                throw new MoneyBagProMaxException("Invalid month format! Please use YYYY-MM (e.g., 2026-04).");
            }
        }

        double totalExpense = 0.0;
        double totalIncome = 0.0;
        double categoryTotal = 0.0;

        for (int i = 0; i < list.size(); i++) {
            Transaction transaction = list.get(i);

            if (parsedYearMonth != null) {
                YearMonth transactionMonth = YearMonth.from(transaction.getDate());
                if (!transactionMonth.equals(parsedYearMonth)) {
                    continue;
                }
            }

            if (transaction.getType().equals("expense")) {
                totalExpense += transaction.getAmount();
            } else if (transaction.getType().equals("income")) {
                totalIncome += transaction.getAmount();
            }

            if (transaction.getCategory().equalsIgnoreCase(summaryType)) {
                categoryTotal += transaction.getAmount();
            }
        }
        displaySummary(ui, totalIncome, totalExpense, categoryTotal);
    }

    private void displaySummary(Ui ui, double totalIncome, double totalExpense, double categoryTotal) {
        if (targetMonth != null) {
            ui.showMessage("=== Summary for " + targetMonth + " ===");
        }
        switch (summaryType) {
        case "all":
            ui.showOverallSummary(totalIncome, totalExpense);
        case "month":
            ui.showMonthlySummary(totalIncome, totalExpense);
            break;
        case "outflow":
        case "expense":
            ui.showCategorySummary("Expenses", totalExpense);
            break;
        case "income":
        case "inflow":
            ui.showCategorySummary("Income", totalIncome);
            break;
        default:
            ui.showCategorySummary("Category '" + summaryType + "'", categoryTotal);
            break;
        }
    }
}

package seedu.duke.command;

import seedu.duke.budget.Budget;
import seedu.duke.transaction.Transaction;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;

public class SummaryCommand extends Command {
    private final String summaryType;

    public SummaryCommand(String summaryType) {
        assert summaryType != null : "Summary type should not be null.";
        this.summaryType = summaryType.toLowerCase();
    }

    /**
     * Calculates and displays the summary of transactions based on the requested category.
     *
     * @param list  The current list of transactions.
     * @param ui    The ui instance to display results.
     */
    @Override
    public void execute(TransactionList list, Budget budget, Ui ui) {
        assert list != null : "List should not be null.";

        if (list.isEmpty()) {
            ui.showMessage("No transactions found to summarise.");
            return;
        }

        double totalExpense = 0.0;
        double totalIncome = 0.0;
        double categoryTotal = 0.0;

        for (int i = 0; i < list.size(); i++) {
            Transaction transaction = list.get(i);

            if (transaction.getType().equals("expense")) {
                totalExpense += transaction.getAmount();
            } else if (transaction.getType().equals("income")) {
                totalIncome += transaction.getAmount();
            }

            if (transaction.getCategory().equalsIgnoreCase(summaryType)) {
                categoryTotal += transaction.getAmount();
            }
        }
        switch (summaryType) {
        case "all":
            ui.showOverallSummary(totalIncome, totalExpense);
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

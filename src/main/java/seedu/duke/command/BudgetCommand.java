package seedu.duke.command;

import seedu.duke.budget.Budget;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;
import java.time.YearMonth;

public class BudgetCommand extends Command {
    private final String action;
    private final double amount;

    public BudgetCommand(String action, double amount) {
        this.action = action;
        this.amount = amount;
    }

    @Override
    public void execute(TransactionList list, Budget budget, Ui ui) {
        if (action.equals("set")) {
            budget.setMonthlyBudget(amount);
            ui.showMessage(String.format("Monthly budget set to $%.2f", amount));
        } else if (action.equals("status")) {
            if (!budget.hasBudget()) {
                ui.showMessage("No monthly budget set.");
                return;
            }

            YearMonth currentMonth = YearMonth.now();
            double spent = list.getTotalExpensesForMonth(currentMonth);
            double remaining = budget.calculateRemaining(spent);
            double percent = budget.calculatePercentageUsed(spent);

            ui.showMessage(String.format("Monthly budget: $%.2f", budget.getMonthlyBudget()));
            ui.showMessage(String.format("Spent: $%.2f", spent));
            ui.showMessage(String.format("Remaining: $%.2f", remaining));
            ui.showMessage(buildProgressBar(percent));
        }
    }

    private String buildProgressBar(double percent) {
        int filled = (int) Math.min(10, Math.round(percent / 10));
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < 10; i++) {
            if (i < filled) {
                bar.append("#");
            } else {
                bar.append("-");
            }
        }
        bar.append("] ").append(String.format("%.1f%%", percent));
        return bar.toString();
    }
}

package seedu.duke.command;

import seedu.duke.budget.Budget;
import seedu.duke.transaction.Transaction;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.TreeMap;

public class StatsCommand extends Command{
    @Override
    public void execute(TransactionList list, Budget budget, Ui ui) {
        if (list.isEmpty()) {
            ui.showMessage("No transactions to analyse.");
            return;
        }
        Transaction highestExpense = list.getHighestExpense();
        Transaction lowestExpense = list.getLowestExpense();
        Transaction highestIncome = list.getHighestIncome();

        String mostFrequentCategory = list.getMostFrequentCategory();
        HashMap<String, Double> avgMap = list.getAverageSpendingPerCategory();
        String spendingTrend = list.getSpendingTrend();

        StringBuilder sb = new StringBuilder();
        sb.append("====== Statistics ======\n");
        sb.append(String.format("Total Income: $%.2f%n", list.getTotalIncome()));
        sb.append(String.format("Total Expense: $%.2f%n", list.getTotalExpenses()));
        sb.append("\n");
        sb.append("Highest Expense: ")
                .append(highestExpense == null ? "N/A" : highestExpense)
                .append("\n");
        sb.append("Lowest Expense: ")
                .append(lowestExpense == null ? "N/A" : lowestExpense)
                .append("\n");
        sb.append("Highest Income: ")
                .append(highestIncome == null ? "N/A" : highestIncome)
                .append("\n");
        sb.append("\n");
        sb.append("Most Frequent Category: ")
                .append(mostFrequentCategory == null ? "N/A" : mostFrequentCategory)
                .append("\n");
        sb.append("\n");
        sb.append("Average Spending per Category:\n");
        if (avgMap.isEmpty()) {
            sb.append("N/A\n");
        } else {
            TreeMap<String, Double> sortedMap = new TreeMap<>(avgMap);
            for (String cat : sortedMap.keySet()) {
                sb.append(String.format("%s: $%.2f%n", cat, sortedMap.get(cat)));
            }
        }
        sb.append("\n");
        sb.append("Spending Trend: ").append(spendingTrend).append("\n");
        sb.append("\n");
        if (budget.hasBudget()) {
            YearMonth currentMonth = YearMonth.now();
            double spentThisMonth = list.getTotalExpensesForMonth(currentMonth);
            double percent = budget.calculatePercentageUsed(spentThisMonth);
            sb.append(String.format("Budget Used: %.1f%%%n", percent));
        } else {
            sb.append("Budget Used: No budget set\n");
        }
        sb.append("========================");
        ui.showMessage(sb.toString());
    }
}

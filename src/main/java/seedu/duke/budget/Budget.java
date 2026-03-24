package seedu.duke.budget;

public class Budget {
    private double monthlyBudget;

    public Budget() {
        this.monthlyBudget = 0;
    }

    public void setMonthlyBudget(double amount) {
        assert amount >= 0 : "Budget cannot be negative";
        this.monthlyBudget = amount;
    }

    public double getMonthlyBudget() {
        return monthlyBudget;
    }

    public boolean hasBudget() {
        return monthlyBudget > 0;
    }

    public double calculateRemaining(double totalExpenses) {
        return monthlyBudget - totalExpenses;
    }

    public double calculatePercentageUsed(double totalExpenses) {
        if (monthlyBudget == 0) {
            return 0;
        }
        return (totalExpenses / monthlyBudget) * 100;
    }
}

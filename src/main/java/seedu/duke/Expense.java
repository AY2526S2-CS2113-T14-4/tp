package seedu.duke;

public class Expense extends Transaction {
    public Expense(String category, double amount) {
        super(category, amount);
    }

    public Expense(String category, double amount, String description) {
        super(category, amount, description);
    }

    //not needed for now, can be used for specific type of category later
    @Override
    public String getType() {
        return "expense";
    }

    @Override
    public String toString() {
        String descriptionSuffix = description.isEmpty() ? "" : " \"" + description + "\"";
        return String.format("[Expense] %s%s $%.2f", category, descriptionSuffix, amount);
    }
}

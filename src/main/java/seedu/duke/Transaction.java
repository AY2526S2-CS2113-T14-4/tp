package seedu.duke;

/**
 * Class to be extended for use for income and expense
 */
public abstract class Transaction {

    protected final String category;
    protected final double amount;
    protected final String description;

    public Transaction(String category, double amount, String description) {
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    public Transaction(String category, double amount) {
        this(category, amount, "");
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public abstract String getType();
}

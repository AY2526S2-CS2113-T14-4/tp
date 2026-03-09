package seedu.duke;

import java.time.LocalDate;

/**
 * Class to be extended for use for income and expense
 */
public abstract class Transaction {

    protected final String category;
    protected final double amount;
    protected final String description;
    protected final LocalDate date;

    public Transaction(String category, double amount, String description, LocalDate date) {
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public Transaction(String category, double amount, String description) {
        this(category, amount, description, LocalDate.now());
    }

    public Transaction(String category, double amount) {
        this(category, amount, "", LocalDate.now());
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

    public LocalDate getDate() {
        return date;
    }

    public abstract String getType();
}

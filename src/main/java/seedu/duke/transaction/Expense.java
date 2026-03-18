package seedu.duke.transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Expense extends Transaction {
    
    public static final List<String> VALID_CATEGORIES = List.of(
            "food", "transport", "utilities", "education", "rent", "medical", "misc"
    );
    
    private static final Logger logger = Logger.getLogger(Expense.class.getName());
    
    public Expense(String category, double amount, String description, LocalDate date) {
        super(category, amount, description, date);
        assert VALID_CATEGORIES.contains(category) : "Expense category must be one of: " + VALID_CATEGORIES;
        logger.log(Level.INFO, "Created Expense — category: {0}, amount: {1}, description: \"{2}\", date: {3}",
                new Object[]{category, amount, description, date});
    }

    public Expense(String category, double amount, String description) {
        super(category, amount, description);
        assert VALID_CATEGORIES.contains(category) : "Expense category must be one of: " + VALID_CATEGORIES;
        logger.log(Level.INFO, "Created Expense — category: {0}, amount: {1}, description: \"{2}\", date: today ({3})",
                new Object[]{category, amount, description, LocalDate.now()});
    }

    public Expense(String category, double amount) {
        super(category, amount);
        assert VALID_CATEGORIES.contains(category) : "Expense category must be one of: " + VALID_CATEGORIES;
        logger.log(Level.INFO, "Created Expense — category: {0}, amount: {1}, no description, date: today ({2})",
                new Object[]{category, amount, LocalDate.now()});
    }

    @Override
    public String getType() {
        return "expense";
    }

    @Override
    public String toString() {
        String descriptionSuffix = description.isEmpty() ? "" : " \"" + description + "\"";
        return String.format("[Expense] %s%s $%.2f (%s)", category, descriptionSuffix, amount, date);
    }
}

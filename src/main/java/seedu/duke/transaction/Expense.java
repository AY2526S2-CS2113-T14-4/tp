package seedu.duke.transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import seedu.duke.category.CategoryManager;

public class Expense extends Transaction {
    
    public static final List<String> VALID_CATEGORIES = List.of(
            "food", "transport", "utilities", "education", "rent", "medical", "misc"
    );
    
    private static final Logger logger = Logger.getLogger(Expense.class.getName());
    // sets the logger to only trigger logger levels WARNING and SEVERE to reduce clutter
    static {
        logger.setLevel(Level.WARNING);
    }
    
    public Expense(String category, double amount, String description, LocalDate date) {
        super(category, amount, description, date);
        assert VALID_CATEGORIES.contains(category)
                || CategoryManager.getInstance().getCustomCategories().contains(category.toLowerCase()) 
                : "Expense category must be built-in or a known custom category";
        logger.log(Level.INFO, "Created Expense — category: {0}, amount: {1}, description: \"{2}\", date: {3}",
                new Object[]{category, amount, description, date});
    }

    public Expense(String category, double amount, String description) {
        this(category, amount, description, LocalDate.now());
    }

    public Expense(String category, double amount) {
        this(category, amount, "", LocalDate.now());
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

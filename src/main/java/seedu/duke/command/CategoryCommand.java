package seedu.duke.command;

import seedu.duke.budget.Budget;
import seedu.duke.category.CategoryManager;
import seedu.duke.transaction.Expense;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;

/**
 * Handles the following list of commands:
 * category add/NAME
 * category remove/NAME
 * category list
 */
public class CategoryCommand extends Command {

    private final String action; 
    private final String name;

    /**
     * Constructor for category command
     * @param action add remove or list
     * @param name name of the category provided, or empty string if category is list
     */
    public CategoryCommand(String action, String name) {
        assert action != null : "Action must not be null";
        assert name != null : "Name must not be null";
        this.action = action;
        this.name = name;
    }

    /**
     * Executes one of the commands depending on the action 
     * @param list The current list of transactions.
     * @param budget Current budget
     * @param ui The ui instance.
     */
    @Override
    public void execute(TransactionList list, Budget budget, Ui ui) {
        CategoryManager cm = CategoryManager.getInstance();
        switch (action) {
        case "add":
            if (Expense.VALID_CATEGORIES.contains(name)) {
                ui.showMessage("'" + name + "' is already a built-in category.");
            } else if (cm.addCustomCategory(name)) {
                ui.showMessage("Custom category added: " + name);
            } else {
                ui.showMessage("Category '" + name + "' already exists.");
            }
            break;
        case "remove":
            if (Expense.VALID_CATEGORIES.contains(name)) {
                ui.showMessage("Cannot remove built-in category '" + name + "'.");
            } else if (cm.removeCustomCategory(name)) {
                ui.showMessage("Custom category removed: " + name);
            } else {
                ui.showMessage("Custom category '" + name + "' not found.");
            }
            break;
        case "list":
            ui.showMessage("Available expense categories: " + cm.getAllExpenseCategories());
            break;
        default:
            ui.showMessage("Unknown category action: " + action);
        }
    }

    /**
     * Category has separate storage, return false to not trigger transaction save
     * @return always false
     */
    @Override
    public boolean isMutating() {
        return false;
    }
}

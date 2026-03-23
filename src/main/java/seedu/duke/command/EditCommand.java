package seedu.duke.command;

import seedu.duke.MoneyBagProMaxException;
import seedu.duke.transaction.Expense;
import seedu.duke.transaction.Income;
import seedu.duke.transaction.Transaction;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;
import seedu.duke.undoredo.UndoRedoManager;

import java.time.LocalDate;

public class EditCommand extends Command {

    private final int targetIndex;
    private final String category;
    private final double amount;
    private final String description;
    private final LocalDate date;
    private final UndoRedoManager undoRedoManager;

    public EditCommand(int targetIndex, String category, double amount,
                       String description, LocalDate date, UndoRedoManager undoRedoManager) {
        assert targetIndex > 0 : "Target index should be positive";
        assert category != null && !category.isBlank() : "Category should not be null or blank";
        assert amount > 0 : "Amount should be positive";
        assert description != null : "Description should not be null";
        assert date != null : "Date should not be null";
        assert undoRedoManager != null : "UndoRedoManager should not be null";
        this.targetIndex = targetIndex;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.undoRedoManager = undoRedoManager;
    }

    @Override
    public void execute(TransactionList list, Ui ui) throws MoneyBagProMaxException {
        int listIndex = targetIndex - 1;

        if (listIndex < 0 || listIndex >= list.size()) {
            throw new MoneyBagProMaxException("Invalid transaction index. "
                    + "Please provide a number between 1 and " + list.size() + ".");
        }

        Transaction newTransaction = null;
        if (Income.VALID_CATEGORIES.contains(category.toLowerCase())) {
            newTransaction = new Income(category, amount, description, date);
        } else if (Expense.VALID_CATEGORIES.contains(category.toLowerCase())) {
            newTransaction = new Expense(category, amount, description, date);
        }

        if (newTransaction == null) {
            ui.showMessage("Invalid category '" + category + "'."
                    + " Valid expense categories: " + Expense.VALID_CATEGORIES
                    + " Valid income categories: " + Income.VALID_CATEGORIES);
            return;
        }

        Transaction oldTransaction = list.remove(listIndex);
        list.insert(listIndex, newTransaction); 
        undoRedoManager.recordEdit(oldTransaction, newTransaction, listIndex);
        ui.showMessage("Edited transaction " + targetIndex + ":\n  Before: " + oldTransaction
                + "\n  After:  " + newTransaction);
    }
}

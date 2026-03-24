package seedu.duke.command;

import seedu.duke.MoneyBagProMaxException;
import seedu.duke.budget.Budget;
import seedu.duke.transaction.Transaction;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;

import java.util.Comparator;
import java.util.List;

/**
 * Sorts and displays transactions by the given criteria (date, amount, or category).
 * The original list order is not modified.
 */
public class SortCommand extends Command {
    private final String sortBy;

    /**
     * Creates a SortCommand with the given sort criteria.
     *
     * @param sortBy the field to sort by ("date", "amount", or "category")
     */
    public SortCommand(String sortBy) {
        assert sortBy != null : "Sort criteria should not be null";
        this.sortBy = sortBy;
    }

    /**
     * Sorts transactions by the stored criteria and displays the sorted list.
     * Does not modify the original transaction list order.
     *
     * @param list the transaction list to sort and display
     * @param ui   the ui instance for output
     * @throws MoneyBagProMaxException if the sort criteria is invalid
     */
    @Override
    public void execute(TransactionList list, Budget budget, Ui ui) throws MoneyBagProMaxException {
        assert list != null : "TransactionList should not be null";

        if (list.isEmpty()) {
            ui.showMessage("No transactions found.");
            return;
        }

        Comparator<Transaction> comparator;
        switch (sortBy) {
        case "date":
            comparator = Comparator.comparing(Transaction::getDate);
            break;
        case "amount":
            comparator = Comparator.comparingDouble(Transaction::getAmount).reversed();
            break;
        case "category":
            comparator = Comparator.comparing(Transaction::getCategory, String.CASE_INSENSITIVE_ORDER);
            break;
        default:
            throw new MoneyBagProMaxException(
                    "Invalid sort criteria. Use: sort by/date, sort by/amount, or sort by/category");
        }

        List<Transaction> sorted = list.getSortedList(comparator);
        ui.showMessage("Transactions sorted by " + sortBy + ":");
        for (int i = 0; i < sorted.size(); i++) {
            ui.showMessage((i + 1) + ". " + sorted.get(i));
        }
    }
}

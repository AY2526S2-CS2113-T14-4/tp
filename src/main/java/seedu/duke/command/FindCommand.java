package seedu.duke.command;

import seedu.duke.budget.Budget;
import seedu.duke.transaction.Transaction;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Searches for transactions that contain the given keyword in their category or description.
 * Displays matching transactions with their original list indices using Java Streams.
 */
public class FindCommand extends Command {
    private final String keyword;

    public FindCommand(String keyword) {
        assert keyword != null : "Search keyword should not be null";
        this.keyword = keyword;
    }

    @Override
    public void execute(TransactionList list, Budget budget, Ui ui) {
        assert list != null;

        if (keyword.isEmpty()) {
            ui.showMessage("Please provide a keyword to search for.");
            return;
        }

        String searchKeyword = keyword.toLowerCase();

        // Use IntStream to iterate through indices so we preserve the original list numbers
        List<String> matchedTransactions = IntStream.range(0, list.size())
                .filter(i -> {
                    Transaction t = list.get(i);
                    boolean matchesDescription = t.getDescription().toLowerCase().contains(searchKeyword);
                    boolean matchesCategory = t.getCategory().toLowerCase().contains(searchKeyword);
                    boolean matchesDate = t.getDate().toString().contains(searchKeyword);
                    return matchesDescription || matchesCategory || matchesDate;
                })
                .mapToObj(i -> (i + 1) + ". " + list.get(i).toString())
                .toList();

        if (matchedTransactions.isEmpty()) {
            ui.showMessage("No matching transactions found for: " + keyword);
        } else {
            ui.showMessage("Found " + matchedTransactions.size() + " matching transaction(s):");
            matchedTransactions.forEach(ui::showMessage);
        }
    }
}

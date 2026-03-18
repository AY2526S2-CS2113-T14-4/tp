package seedu.duke.command;

import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;

public class ListCommand extends Command {
    @Override
    public void execute(TransactionList list, Ui ui) {
        assert list != null : "TransactionList should not be null.";

        if (list.isEmpty()) {
            ui.showMessage("No transactions found.");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            ui.showMessage((i + 1) + ". " + list.get(i));
        }
    }
}

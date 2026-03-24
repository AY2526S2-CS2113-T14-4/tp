package seedu.duke.command;

import seedu.duke.budget.Budget;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;

public class ExitCommand extends Command {
    @Override
    public void execute(TransactionList list, Budget budget, Ui ui) {
        ui.showExitMessage();
    }

    public boolean isExit() {
        return true;
    }
}

package seedu.duke.command;

import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;

public class ExitCommand extends Command {
    @Override
    public void execute(TransactionList list, Ui ui) {
        ui.showExitMessage();
    }

    public boolean isExit() {
        return true;
    }
}

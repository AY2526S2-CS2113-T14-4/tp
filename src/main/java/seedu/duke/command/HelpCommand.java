package seedu.duke.command;

import seedu.duke.budget.Budget;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;

public class HelpCommand extends Command {
    @Override
    public void execute(TransactionList list, Budget budget, Ui ui) {
        ui.showHelp();
    }
}

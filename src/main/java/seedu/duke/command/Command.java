package seedu.duke.command;

import seedu.duke.MoneyBagProMaxException;
import seedu.duke.budget.Budget;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;

public abstract class Command {
    /**
     * Executes the command.
     *
     * @param list The current list of transactions.
     * @param ui   The ui instance.
     */
    public abstract void execute(TransactionList list, Budget budget, Ui ui) throws MoneyBagProMaxException;

    /**
     * Indicates whether this command should exit the application.
     * Defaults to false, but the ExitCommand will override this to return true to exit.
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Returns whether this command mutates the transaction list.
     * Used by the main loop to determine if storage should be saved after execution.
     *
     * @return boolean — true if the command modifies the list, false otherwise.
     */
    public boolean isMutating() {
        return false;
    }
}

package seedu.duke.command;

import seedu.duke.MoneyBagProMaxException;
import seedu.duke.budget.Budget;
import seedu.duke.transaction.Transaction;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;
import seedu.duke.undoredo.UndoRedoManager;

public class DeleteCommand extends Command {

    private final int targetIndex;
    private final UndoRedoManager undoRedoManager;

    public DeleteCommand(int targetIndex, UndoRedoManager undoRedoManager) {
        assert undoRedoManager != null : "UndoRedoManager should not be null";
        this.targetIndex = targetIndex;
        this.undoRedoManager = undoRedoManager;
    }

    @Override
    public void execute(TransactionList list, Budget budget, Ui ui) throws MoneyBagProMaxException {
        assert list != null : "Transaction list should not be null";

        int listIndex = targetIndex - 1;

        if (listIndex < 0 || listIndex >= list.size()) {
            throw new MoneyBagProMaxException("Invalid transaction index. " +
                    "Please provide a number between 1 and " + list.size() + ".");
        }
        Transaction removed = list.remove(listIndex);
        undoRedoManager.recordDelete(removed, listIndex);
        ui.showMessage("Deleted: " + removed);
    }

    /** @return boolean — always true, this command modifies the transaction list. */
    @Override
    public boolean isMutating() {
        return true;
    }
}

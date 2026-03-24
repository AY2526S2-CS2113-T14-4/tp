package seedu.duke.command;

import seedu.duke.MoneyBagProMaxException;
import seedu.duke.budget.Budget;
import seedu.duke.transaction.Transaction;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;
import seedu.duke.undoredo.UndoRedoManager;
import seedu.duke.undoredo.UndoRedoManager.ActionPair;

/**
 * Reverses the last mutating command (add or delete) by performing
 * the inverse operation on the transaction list.
 */
public class UndoCommand extends Command {

    private final UndoRedoManager undoRedoManager;

    public UndoCommand(UndoRedoManager undoRedoManager) {
        assert undoRedoManager != null : "UndoRedoManager should not be null";
        this.undoRedoManager = undoRedoManager;
    }

    @Override
    public void execute(TransactionList list, Budget budget, Ui ui) throws MoneyBagProMaxException {
        ActionPair action = undoRedoManager.getUndoAction();
        switch (action.getType()) {
        case ADD:
            Transaction removed = list.remove(action.getIndex());
            ui.showMessage("Undo: Removed " + removed);
            break;
        case DELETE:
            list.insert(action.getIndex(), action.getTransaction());
            ui.showMessage("Undo: Restored " + action.getTransaction());
            break;
        case EDIT:
            list.remove(action.getIndex());
            list.insert(action.getIndex(), action.getOldTransaction());
            ui.showMessage("Undo: Reverted edit at position " + (action.getIndex() + 1)
                    + " — restored " + action.getOldTransaction());
            break;
        default:
            throw new MoneyBagProMaxException("Unknown action type.");
        }
    }

    /** @return boolean — always true, this command modifies the transaction list. */
    @Override
    public boolean isMutating() {
        return true;
    }
}

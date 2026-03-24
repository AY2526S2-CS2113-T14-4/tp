package seedu.duke.command;

import seedu.duke.MoneyBagProMaxException;
import seedu.duke.budget.Budget;
import seedu.duke.transaction.Transaction;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;
import seedu.duke.undoredo.UndoRedoManager;
import seedu.duke.undoredo.UndoRedoManager.ActionPair;

/**
 * Re-applies the last undone command by performing the original
 * operation on the transaction list again.
 */
public class RedoCommand extends Command {

    private final UndoRedoManager undoRedoManager;

    public RedoCommand(UndoRedoManager undoRedoManager) {
        assert undoRedoManager != null : "UndoRedoManager should not be null";
        this.undoRedoManager = undoRedoManager;
    }

    @Override
    public void execute(TransactionList list, Budget budget, Ui ui) throws MoneyBagProMaxException {
        ActionPair action = undoRedoManager.getRedoAction();
        switch (action.getType()) {
        case ADD:
            list.insert(action.getIndex(), action.getTransaction());
            ui.showMessage("Redo: Added " + action.getTransaction());
            break;
        case DELETE:
            Transaction removed = list.remove(action.getIndex());
            ui.showMessage("Redo: Removed " + removed);
            break;
        case EDIT:
            list.remove(action.getIndex());
            list.insert(action.getIndex(), action.getTransaction());
            ui.showMessage("Redo: Reapplied edit at position " + (action.getIndex() + 1)
                    + " — restored " + action.getTransaction());
            break;
        default:
            throw new MoneyBagProMaxException("Unknown action type.");
        }
    }
}

package seedu.duke.undoredo;

import seedu.duke.MoneyBagProMaxException;
import seedu.duke.transaction.Transaction;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Manages undo and redo history for mutating commands (add, delete).
 * Stores (action, transaction, index) triples in two stacks.
 *
 * <p><b>Invariant:</b> Stored indices assume operations are undone and
 * redone in strict LIFO stack order.  Because both stacks enforce this,
 * the recorded indices remain correct for sequential undo/redo
 * sequences.  If a non-stack-ordered replay is ever needed, the index
 * scheme must be revisited.</p>
 */
public class UndoRedoManager {

    public enum ActionType {
        ADD, DELETE, EDIT
    }

    /**
     * Represents a recorded action that can be undone or redone.
     */
    public static class ActionPair {
        private final ActionType type;
        private final Transaction transaction;
        private final Transaction oldTransaction;
        private final int index;

        public ActionPair(ActionType type, Transaction transaction, Transaction oldTransaction, int index) {
            this.type = type;
            this.transaction = transaction;
            this.oldTransaction = oldTransaction;
            this.index = index;
        }
        
        public ActionPair(ActionType type, Transaction transaction, int index) {
            this(type, transaction, null, index);
        }

        public ActionType getType() {
            return type;
        }

        public Transaction getTransaction() {
            return transaction;
        }

        public Transaction getOldTransaction() {
            return oldTransaction;
        }

        public int getIndex() {
            return index;
        }
    }

    private final Deque<ActionPair> undoStack = new ArrayDeque<>();
    private final Deque<ActionPair> redoStack = new ArrayDeque<>();

    /**
     * Records an edit action. Clears the redo stack since a new action.
     * invalidates any previously undone actions.
     * @param oldTransaction the transaction before being edited.
     * @param newTransaction the transaction after being edited.
     * @param index the index of the transaction being edited.
     */
    public void recordEdit(Transaction oldTransaction, Transaction newTransaction, int index) {
        assert oldTransaction != null : "Old transaction should not be null";
        assert newTransaction != null : "New transaction should not be null";
        assert index >= 0 : "Index should not be negative";
        undoStack.push(new ActionPair(ActionType.EDIT, newTransaction, oldTransaction, index));
        redoStack.clear();
    }
    
    /**
     * Records an add action. Clears the redo stack since a new action
     * invalidates any previously undone actions.
     */
    public void recordAdd(Transaction t, int index) {
        assert t != null : "Transaction should not be null";
        assert index >= 0 : "Index should not be negative";
        undoStack.push(new ActionPair(ActionType.ADD, t, index));
        redoStack.clear();
    }

    /**
     * Records a delete action. Clears the redo stack since a new action
     * invalidates any previously undone actions.
     */
    public void recordDelete(Transaction t, int index) {
        assert t != null : "Transaction should not be null";
        assert index >= 0 : "Index should not be negative";
        undoStack.push(new ActionPair(ActionType.DELETE, t, index));
        redoStack.clear();
    }

    /**
     * Returns the most recent action to undo, moving it to the redo stack.
     *
     * @return the ActionPair to undo
     * @throws MoneyBagProMaxException if there is nothing to undo
     */
    public ActionPair getUndoAction() throws MoneyBagProMaxException {
        if (undoStack.isEmpty()) {
            throw new MoneyBagProMaxException("Nothing to undo.");
        }
        ActionPair action = undoStack.pop();
        redoStack.push(action);
        return action;
    }

    /**
     * Returns the most recent undone action to redo, moving it back to the undo stack.
     *
     * @return the ActionPair to redo
     * @throws MoneyBagProMaxException if there is nothing to redo
     */
    public ActionPair getRedoAction() throws MoneyBagProMaxException {
        if (redoStack.isEmpty()) {
            throw new MoneyBagProMaxException("Nothing to redo.");
        }
        ActionPair action = redoStack.pop();
        undoStack.push(action);
        return action;
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}

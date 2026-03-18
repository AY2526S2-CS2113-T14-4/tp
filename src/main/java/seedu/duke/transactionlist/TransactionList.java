package seedu.duke.transactionlist;

import seedu.duke.transaction.Transaction;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 *  Manages the list of transactions stored in the program.
 *  This class provides operations for adding, retrieving,
 *  removing and checking transactions in the list.
 */
public class TransactionList {

    private static final Logger logger = Logger.getLogger(TransactionList.class.getName());
    private final ArrayList<Transaction> transactions = new ArrayList<>();

    /**
     * Adds a transaction to the list.
     *
     * Defensive checks ensures that null transactions are not added,
     * which would cause unexpected behaviour.
     *
     * @param t Transaction object to add to list
     */
    public void add(Transaction t) {
        assert t != null : "Transaction should not be null";
        logger.info("Adding transaction: " + t);
        transactions.add(t);
    }

    /**
     * Returns the number of transactions currently stored.
     *
     * @return the size of the transaction list
     */
    public int size() {
        logger.info("Retrieving transaction list size");
        return transactions.size();
    }

    /**
     * Checks whether the transaction list is empty.
     *
     * @return true if the list contains no transactions
     */
    public boolean isEmpty() {
        return transactions.isEmpty();
    }

    /**
     * Retrieves a transaction at the specified index.
     *
     * Defensive programming ensures the index is within valid bounds.
     * This prevents runtime errors caused by invalid access.
     *
     * @param i index of the transaction
     * @return the transaction at the specified index
     */
    public Transaction get(int i) {
        assert i >= 0 && i < transactions.size() : "Index is out of bounds";
        logger.info("Retrieving transaction at index: " + i);
        return transactions.get(i);
    }

    /**
     * Removes a transaction at the specified index.
     *
     * Defensive check ensures the index is valid before attempting removal.
     * Logging helps trace transaction deletion during debugging.
     *
     * @param i index of the transaction to remove
     * @return the removed transaction
     */
    public Transaction remove(int i) {
        assert i >= 0 && i < transactions.size() : "Index is out of bounds";
        logger.info("Removing transaction at index: " + i);
        return transactions.remove(i);
    }
    // todo contain/find
}

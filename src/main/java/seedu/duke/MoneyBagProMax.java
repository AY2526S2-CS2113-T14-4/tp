package seedu.duke;

import seedu.duke.budget.Budget;
import seedu.duke.command.Command;
import seedu.duke.command.GenerateRecurringCommand;
import seedu.duke.parser.Parser;
import seedu.duke.storage.Storage;
import seedu.duke.transactionlist.RecurringTransactionList;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;
import seedu.duke.undoredo.UndoRedoManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MoneyBagProMax {
    /**
     * Main entry-point for the java.duke.MoneyBagProMax application.
     */

    private static final Logger logger = Logger.getLogger(MoneyBagProMax.class.getName());
    // sets the logger to only trigger logger levels WARNING and SEVERE to reduce clutter
    static {
        logger.setLevel(Level.WARNING);
    }

    public static void main(String[] args) throws MoneyBagProMaxException {
        logger.info("Starting the MoneyBagProMax application...");
        TransactionList list = new TransactionList();
        RecurringTransactionList recurringList = new RecurringTransactionList();
        Storage storage = new Storage();
        storage.load(list);
        storage.loadRecurring(recurringList);
        UndoRedoManager undoRedoManager = new UndoRedoManager();
        Parser parser = new Parser(undoRedoManager, recurringList);
        Ui ui = new Ui();
        Budget budget = new Budget();
        logger.info("Core components: TransactionList, Parser, UndoRedoManager and Ui initialised successfully.");
        ui.showWelcomeMessage();
        try {
            new GenerateRecurringCommand(recurringList).execute(list, budget, ui);
            storage.saveRecurring(recurringList);
            storage.save(list);
        } catch (MoneyBagProMaxException e) {
            logger.log(Level.WARNING, "Startup recurring transaction processing failed: " + e.getMessage());
        }
        boolean isExit = false;

        while (!isExit) {
            String input = ui.readInput();
            try {
                Command command = parser.parse(input);
                assert command != null : "Parser returned null command for input: " + input;
                command.execute(list, budget, ui);
                if (command.isMutating()) {
                    storage.save(list);
                }
                if (command.isMutatingRecurring()) {
                    storage.saveRecurring(recurringList);
                }
                isExit = command.isExit();
            } catch (MoneyBagProMaxException e) {
                ui.showMessage(e.getMessage());
            } catch (Exception e) {
                logger.log(Level.SEVERE, "An unexpected error occurred!", e);
                ui.showMessage("An unexpected error occurred. Please check the logs.");
            }
        }
        logger.info("Gracefully exited the MoneyBagProMax application.");
    }
}

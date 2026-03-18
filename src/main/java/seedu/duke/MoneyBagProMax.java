package seedu.duke;

import seedu.duke.command.Command;
import seedu.duke.parser.Parser;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MoneyBagProMax {
    /**
     * Main entry-point for the java.duke.MoneyBagProMax application.
     */

    private static final Logger logger = Logger.getLogger(MoneyBagProMax.class.getName());

    public static void main(String[] args) {
        logger.info("Starting the MoneyBagProMax application...");
        TransactionList list = new TransactionList();
        Parser parser = new Parser();
        Ui ui = new Ui();
        logger.info("Core components: TransactionList, Parser and Ui initialised successfully.");

        ui.showWelcomeMessage();
        boolean isExit = false;
        
        while (!isExit) {
            String input = ui.readInput();
            try {
                Command command = parser.parse(input);
                assert command != null : "Parser returned null command for input: " + input;
                command.execute(list, ui);
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

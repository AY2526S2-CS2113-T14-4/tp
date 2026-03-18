package seedu.duke;

import seedu.duke.command.Command;
import seedu.duke.parser.Parser;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;

public class MoneyBagProMax {
    /**
     * Main entry-point for the java.duke.MoneyBagProMax application.
     */
    public static void main(String[] args) {
        TransactionList list = new TransactionList();
        Parser parser = new Parser();
        Ui ui = new Ui();

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
            }
        }
    }
}

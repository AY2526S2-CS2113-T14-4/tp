package seedu.duke;

public class MoneyBagProMax {
    /**
     * Main entry-point for the java.duke.MoneyBagProMax application.
     */
    public static void main(String[] args) {
        TransactionList list = new TransactionList();
        Parser parser = new Parser();
        Ui ui = new Ui();

        ui.showWelcomeMessage();

        while (true) {
            String input = ui.readInput();
            if (input.equals("exit") || input.equals("q")) {
                break;
            }
            parser.parse(input, list, ui);
        }
    }
}

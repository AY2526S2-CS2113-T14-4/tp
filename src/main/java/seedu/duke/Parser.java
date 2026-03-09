package seedu.duke;

public class Parser {

    /**
     * Checks user command and calls functions according to the command.
     *
     * @param input The user input string.
     * @param list  The current list of transactions.
     * @param ui    The ui instance.
     */
    public void parse(String input, TransactionList list, Ui ui) {
        if (input.startsWith("add ")) {
            parseAddCommand(input.substring(4).trim(), list, ui);
        } else if (input.equals("list")) {
            parseListCommand(list, ui);
        } else if (input.startsWith("delete ")) {
            parseDeleteCommand(input.substring("delete ".length()).trim(), list, ui);
        } else if (input.startsWith("help")) {
            ui.showHelp();
        } else {
            ui.showMessage("Unknown command.");
        }
    }

    private void parseAddCommand(String args, TransactionList list, Ui ui) {
        String[] parts = args.split("/", 2);

        if (parts.length < 2) {
            ui.showMessage("Invalid, try: add [category]/PRICE [desc/DESCRIPTION]");
            return;
        }

        try {
            String category = parts[0].trim();
            String remainder = parts[1].trim();
            String description = parseDescription(remainder);
            double amount = parseAmount(remainder);
            Expense expense = new Expense(category, amount, description);
            list.add(expense);
            ui.showMessage("Added: " + expense);
        } catch (NumberFormatException e) {
            ui.showMessage("Invalid price.");
        }
    }

    private void parseListCommand(TransactionList list, Ui ui) {
        if (list.size() == 0) {
            ui.showMessage("No transactions found.");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            ui.showMessage((i + 1) + ". " + list.get(i));
        }
    }

    private void parseDeleteCommand(String indexText, TransactionList list, Ui ui) {
        try {
            int index = Integer.parseInt(indexText) - 1;
            if (index < 0 || index >= list.size()) {
                ui.showMessage("Invalid transaction index.");
                return;
            }
            Transaction removed = list.remove(index);
            ui.showMessage("Deleted: " + removed);
        } catch (NumberFormatException e) {
            ui.showMessage("Invalid, try: delete INDEX");
        }
    }

    /**
     * Extracts the description from the add command remainder string.
     * Returns an empty string if the desc/ token is absent.
     *
     * @param remainder The portion of input after the first slash, e.g. "10 desc/lunch".
     * @return The description string, or an empty string if not present.
     */
    private String parseDescription(String remainder) {
        if (!remainder.contains(" desc/")) {
            return "";
        }
        int start = remainder.indexOf(" desc/") + " desc/".length();
        return remainder.substring(start).trim();
    }

    /**
     * Extracts the numeric amount from the add command remainder string.
     * The amount is the token before any optional desc/ suffix.
     *
     * @param remainder The portion of input after the first slash, e.g. "10 desc/lunch".
     * @return The parsed amount as a double.
     * @throws NumberFormatException If the amount token cannot be parsed.
     */
    private double parseAmount(String remainder) {
        if (remainder.contains(" desc/")) {
            return Double.parseDouble(remainder.substring(0, remainder.indexOf(" desc/")).trim());
        }
        return Double.parseDouble(remainder.trim());
    }
}

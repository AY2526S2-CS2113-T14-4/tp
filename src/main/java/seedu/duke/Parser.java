package seedu.duke;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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
        } else if (input.startsWith("summary")) {
            parseSummaryCommand(input, list, ui);
        } else if (input.startsWith("help")) {
            ui.showHelp();
        } else {
            ui.showMessage("Unknown command.");
        }
    }

    private void parseAddCommand(String args, TransactionList list, Ui ui) {
        String[] parts = args.split("/", 2);

        if (parts.length < 2) {
            ui.showMessage("Invalid, try: add [category]/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD]");
            return;
        }

        try {
            String category = parts[0].trim();
            String remainder = parts[1].trim();
            double amount = parseAmount(remainder);
            String description = parseDescription(remainder);
            LocalDate date = parseDate(remainder, ui);
            Expense expense = new Expense(category, amount, description, date);
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
        int descStart = remainder.indexOf(" desc/") + " desc/".length();
        String afterDesc = remainder.substring(descStart).trim();
        if (afterDesc.contains(" d/")) {
            return afterDesc.substring(0, afterDesc.indexOf(" d/")).trim();
        }
        return afterDesc;
    }

    /**
     * Extracts the numeric amount from the add command remainder string.
     * The amount is the token before any optional desc/ or d/ suffix.
     *
     * @param remainder The portion of input after the first slash, e.g. "10 desc/lunch".
     * @return The parsed amount as a double.
     * @throws NumberFormatException If the amount token cannot be parsed.
     */
    private double parseAmount(String remainder) {
        String amountToken = remainder;
        if (amountToken.contains(" desc/")) {
            amountToken = amountToken.substring(0, amountToken.indexOf(" desc/"));
        } else if (amountToken.contains(" d/")) {
            amountToken = amountToken.substring(0, amountToken.indexOf(" d/"));
        }
        return Double.parseDouble(amountToken.trim());
    }

    /**
     * Extracts the date from the add command remainder string.
     * Returns today's date if the d/ token is absent.
     * Shows an error message and returns today's date if the format is invalid.
     *
     * @param remainder The portion of input after the first slash.
     * @param ui        The ui instance used to display error messages.
     * @return The parsed LocalDate, or LocalDate.now() if absent or invalid.
     */
    private LocalDate parseDate(String remainder, Ui ui) {
        if (!remainder.contains(" d/")) {
            return LocalDate.now();
        }
        int dateStart = remainder.indexOf(" d/") + " d/".length();
        String dateToken = remainder.substring(dateStart).trim().split(" ")[0];
        try {
            return LocalDate.parse(dateToken);
        } catch (DateTimeParseException e) {
            ui.showMessage("Invalid date format — expected yyyy-MM-dd. Using today's date.");
            return LocalDate.now();
        }
    }

    /**
     * Calculates and displays the summary of transactions based on the requested category.
     *
     * @param input The full user input string (e.g., "summary all" or "summary food").
     * @param list  The current list of transactions.
     * @param ui    The ui instance to display results.
     */
    private void parseSummaryCommand(String input, TransactionList list, Ui ui) {
        String[] parts = input.split(" ", 2);
        String summaryType = parts.length > 1 ? parts[1].trim().toLowerCase() : "all";

        if (list.size() == 0) {
            ui.showMessage("No transactions found to summarise.");
            return;
        }

        double totalExpense = 0.0;
        double totalIncome = 0.0;
        double categoryTotal = 0.0;

        for (int i = 0; i < list.size(); i++) {
            Transaction transaction = list.get(i);

            if (transaction.getType().equals("expense")) {
                totalExpense += transaction.getAmount();
            } else if (transaction.getType().equals("income")) {
                totalIncome += transaction.getAmount();
            }

            if (transaction.getCategory().equalsIgnoreCase(summaryType)) {
                categoryTotal += transaction.getAmount();
            }
        }
        switch (summaryType) {
        case "all":
            ui.showOverallSummary(totalIncome, totalExpense);
            break;
        case "outflow":
        case "expense":
            ui.showCategorySummary("Expenses", totalExpense);
            break;
        case "income":
        case "inflow":
            ui.showCategorySummary("Income", totalIncome);
            break;
        default:
            ui.showCategorySummary("Category '" + summaryType + "'", categoryTotal);
            break;
        }
    }
}

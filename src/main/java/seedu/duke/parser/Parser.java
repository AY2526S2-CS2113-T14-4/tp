package seedu.duke.parser;

import seedu.duke.MoneyBagProMaxException;
import seedu.duke.command.AddCommand;
import seedu.duke.command.Command;
import seedu.duke.command.DeleteCommand;
import seedu.duke.command.ExitCommand;
import seedu.duke.command.ListCommand;
import seedu.duke.command.SummaryCommand;
import seedu.duke.command.HelpCommand;
import seedu.duke.command.FindCommand;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Parser {

    /**
     * Checks user command and calls functions according to the command.
     *
     * @param input The user input string.
     */
    public Command parse(String input) throws MoneyBagProMaxException {
        if (input == null || input.trim().isEmpty()) {
            throw new MoneyBagProMaxException("Please enter a command.");
        }

        String[] parts = input.split(" ", 2);
        assert parts.length >= 1 && parts.length <= 2 : "split produced unexpected part count: " + parts.length;
        String command = parts[0].toLowerCase();
        assert !command.isEmpty() : "Command token is empty despite non-empty input: " + input;
        String arguments = ((parts.length > 1) ? parts[1].trim() : "");

        switch (command) {
        case "exit":
        case "q":
            return new ExitCommand();
        case "help":
            return new HelpCommand();
        case "list":
            return new ListCommand();
        case "add":
            return parseAddCommand(arguments);
        case "delete":
            return parseDeleteCommand(arguments);
        case "summary":
            String summaryType = arguments.isEmpty() ? "all" : arguments;
            return new SummaryCommand(summaryType);
        case "find":
            if (arguments.isEmpty()) {
                throw new MoneyBagProMaxException("Please provide a keyword to search for.");
            }
            return new FindCommand(arguments);
        default:
            throw new MoneyBagProMaxException("Unknown command. Type `help` to see the list of available commands.");
        }
    }

    private Command parseAddCommand(String args) throws MoneyBagProMaxException {
        String[] parts = args.split("/", 2);
        if (parts.length < 2) {
            throw new MoneyBagProMaxException("Invalid, try: add [category]/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD]");
        }

        try {
            String category = parts[0].trim();
            String remainder = parts[1].trim();
            
            assert !category.isEmpty() : "Category is blank after trimming in input: " + args;
            assert !remainder.isEmpty() : "Remainder after category slash is empty in input: " + args;

            double amount = parseAmount(remainder);
            String description = parseDescription(remainder);
            LocalDate date = parseDate(remainder);

            assert amount > 0 : "Parsed amount is not positive: " + amount;
            assert Double.isFinite(amount) : "Parsed amount is infinite or NaN: " + amount;
            assert !date.isBefore(LocalDate.of(1900, 1, 1)) : 
                    "Parsed date is before year 1900, likely a typo: " + date;
            
            return new AddCommand(category, amount, description, date);
            
        } catch (NumberFormatException e) {
            throw new MoneyBagProMaxException("Invalid price.");
        }
    }

    private Command parseDeleteCommand(String indexText) throws MoneyBagProMaxException {
        if (indexText.isEmpty()) {
            throw new MoneyBagProMaxException("Invalid format: try: delete [INDEX]");
        }
        try {
            int index = Integer.parseInt(indexText);
            return new DeleteCommand(index);
        } catch (NumberFormatException e) {
            throw new MoneyBagProMaxException("Invalid, try: delete INDEX");
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
        assert descStart <= remainder.length() : "descStart index exceeds remainder length";

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
        assert !amountToken.trim().isEmpty() : "Amount token is empty after stripping flags from: " + remainder;

        return Double.parseDouble(amountToken.trim());
    }

    /**
     * Extracts the date from the add command remainder string.
     * Returns today's date if the d/ token is absent.
     * Shows an error message and returns today's date if the format is invalid.
     *
     * @param remainder The portion of input after the first slash.
     * @return The parsed LocalDate, or LocalDate.now() if absent or invalid.
     */
    private LocalDate parseDate(String remainder) throws MoneyBagProMaxException {
        if (!remainder.contains(" d/")) {
            return LocalDate.now();
        }
        int dateStart = remainder.indexOf(" d/") + " d/".length();
        String dateToken = remainder.substring(dateStart).trim().split(" ")[0];
        try {
            return LocalDate.parse(dateToken);
        } catch (DateTimeParseException e) {
            throw new MoneyBagProMaxException("Invalid date format — expected yyyy-MM-dd. Using today's date.");
        }
    }
}

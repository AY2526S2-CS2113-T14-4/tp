package seedu.duke.parser;

import seedu.duke.MoneyBagProMaxException;
import seedu.duke.command.AddCommand;
import seedu.duke.command.Command;
import seedu.duke.command.DeleteCommand;
import seedu.duke.command.ExitCommand;
import seedu.duke.command.ListCommand;
import seedu.duke.command.RedoCommand;
import seedu.duke.command.SummaryCommand;
import seedu.duke.command.HelpCommand;
import seedu.duke.command.FindCommand;
import seedu.duke.command.SortCommand;
import seedu.duke.command.UndoCommand;
import seedu.duke.command.EditCommand;
import seedu.duke.undoredo.UndoRedoManager;
import seedu.duke.command.BudgetCommand;
import seedu.duke.command.StatsCommand;
import seedu.duke.command.FilterCommand;
import seedu.duke.command.ExportCsvCommand;
import seedu.duke.command.ExportTxtCommand;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Parser {

    private final UndoRedoManager undoRedoManager;

    public Parser(UndoRedoManager undoRedoManager) {
        assert undoRedoManager != null : "UndoRedoManager should not be null";
        this.undoRedoManager = undoRedoManager;
    }

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
        case "budget":
            return parseBudgetCommand(arguments);
        case "stats":
            return new StatsCommand();
        case "sort":
            return parseSortCommand(arguments);
        case "undo":
            return new UndoCommand(undoRedoManager);
        case "redo":
            return new RedoCommand(undoRedoManager);
        case "edit":
            return parseEditCommand(arguments);
        case "filter":
            if (arguments.isEmpty()) {
                throw new MoneyBagProMaxException("Invalid format. Use: filter from/YYYY-MM-DD to/YYYY-MM-DD");
            }
            return parseFilterCommand(arguments);
        case "export-csv":
            if (arguments.isEmpty()) {
                throw new MoneyBagProMaxException("Usage: export-csv FILEPATH");
            }
            return new ExportCsvCommand(arguments);
        case "export-data":
            if (arguments.isEmpty()) {
                throw new MoneyBagProMaxException("Usage: export-data FILEPATH");
            }
            return new ExportTxtCommand(arguments);
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
            
            return new AddCommand(category, amount, description, date, undoRedoManager);
            
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
            return new DeleteCommand(index, undoRedoManager);
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

    /**
     * Parses the arguments for the sort command.
     * Validates the "by/" prefix and the sort criteria value.
     *
     * @param args the argument string after "sort", e.g. "by/date"
     * @return a new SortCommand with the parsed criteria
     * @throws MoneyBagProMaxException if the format or criteria is invalid
     */
    private Command parseSortCommand(String args) throws MoneyBagProMaxException {
        String sortPrefix = "by/";
        if (args.isEmpty() || !args.startsWith(sortPrefix)) {
            throw new MoneyBagProMaxException(
                    "Invalid format. Use: sort by/date, sort by/amount, or sort by/category");
        }
        String sortBy = args.substring(sortPrefix.length()).trim().toLowerCase();
        if (!sortBy.equals("date") && !sortBy.equals("amount") && !sortBy.equals("category")) {
            throw new MoneyBagProMaxException(
                    "Invalid sort criteria. Use: sort by/date, sort by/amount, or sort by/category");
        }
        return new SortCommand(sortBy);
    }

    private Command parseBudgetCommand(String args) throws MoneyBagProMaxException {
        if (args.isEmpty()) {
            return new BudgetCommand("status", 0);
        }
        String[] parts = args.split(" ", 2);
        String action = parts[0].trim().toLowerCase();
        if (action.equals("set")) {
            if (parts.length < 2) {
                throw new MoneyBagProMaxException("Usage: budget set AMOUNT");
            }
            try {
                double amount = Double.parseDouble(parts[1].trim());
                if (amount < 0) {
                    throw new MoneyBagProMaxException("Budget cannot be negative.");
                }
                return new BudgetCommand("set", amount);
            } catch (NumberFormatException e) {
                throw new MoneyBagProMaxException("Invalid budget amount.");
            }
        }
        if (action.equals("status")) {
            return new BudgetCommand("status", 0);
        }
        throw new MoneyBagProMaxException("Unknown budget command.");
    }
  
    private Command parseEditCommand(String args) throws MoneyBagProMaxException {
        String[] parts = args.split(" ", 2);
        if (parts.length < 2) {
            throw new MoneyBagProMaxException(
                    "Invalid format. Use: edit INDEX [category]/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD]");
        }

        int index;
        try {
            index = Integer.parseInt(parts[0].trim());
        } catch (NumberFormatException e) {
            throw new MoneyBagProMaxException("Invalid index. Use: edit INDEX [category]/PRICE ...");
        }

        // Reuse the same parsing logic as parseAddCommand by splitting on the first "/"
        String remainder = parts[1].trim();
        String[] categoryAndRest = remainder.split("/", 2);
        if (categoryAndRest.length < 2) {
            throw new MoneyBagProMaxException(
                    "Invalid format. Use: edit INDEX [category]/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD]");
        }

        try {
            String category = categoryAndRest[0].trim();
            String valueRemainder = categoryAndRest[1].trim();
            double amount = parseAmount(valueRemainder);
            String description = parseDescription(valueRemainder);
            LocalDate date = parseDate(valueRemainder);
            return new EditCommand(index, category, amount, description, date, undoRedoManager);
        } catch (NumberFormatException e) {
            throw new MoneyBagProMaxException("Invalid price.");
        }
    }

    private Command parseFilterCommand(String args) throws MoneyBagProMaxException {
        if (!args.contains("from/") || !args.contains("to/")) {
            throw new MoneyBagProMaxException(
                    "Invalid format. Use: filter from/YYYY-MM-DD to/YYYY-MM-DD");
        }

        try {
            int fromStart = args.indexOf("from/") + "from/".length();
            String fromToken = args.substring(fromStart).split(" ")[0].trim();

            int toStart = args.indexOf("to/") + "to/".length();
            String toToken = args.substring(toStart).split(" ")[0].trim();

            if (fromToken.isEmpty()) {
                throw new MoneyBagProMaxException("Missing 'from' date parameter.");
            }
            if (toToken.isEmpty()) {
                throw new MoneyBagProMaxException("Missing 'to' date parameter.");
            }

            LocalDate from = LocalDate.parse(fromToken);
            LocalDate to = LocalDate.parse(toToken);

            if (from.isAfter(to)) {
                throw new MoneyBagProMaxException("The 'from' date cannot be after the 'to' date!");
            }

            return new FilterCommand(from, to);

        } catch (DateTimeParseException e) {
            throw new MoneyBagProMaxException("Invalid date format — expected YYYY-MM-DD. "
                            + "Use: filter from/YYYY-MM-DD to/YYYY-MM-DD");
        } catch (IndexOutOfBoundsException e) {
            throw new MoneyBagProMaxException("Missing date values! "
                    + "Use: filter from/YYYY-MM-DD to/YYYY-MM-DD");
        }
    }
}

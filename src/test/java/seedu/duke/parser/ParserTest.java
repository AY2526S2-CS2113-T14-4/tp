package seedu.duke.parser;

import seedu.duke.MoneyBagProMaxException;
import seedu.duke.command.AddCommand;
import seedu.duke.command.AddRecurringCommand;
import seedu.duke.command.Command;
import seedu.duke.command.DeleteRecurringCommand;
import seedu.duke.command.GenerateRecurringCommand;
import seedu.duke.command.ListRecurringCommand;
import seedu.duke.command.SortCommand;
import seedu.duke.command.UndoCommand;
import seedu.duke.command.RedoCommand;
import seedu.duke.transactionlist.RecurringTransactionList;
import seedu.duke.undoredo.UndoRedoManager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// structure of test names: methodToTest_input_expectedOutput
class ParserTest {

    @Test
    public void parse_sortByDate_returnsSortCommand() throws MoneyBagProMaxException {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());
        Command command = parser.parse("sort by/date");
        assertInstanceOf(SortCommand.class, command);
    }

    @Test
    public void parse_sortByAmount_returnsSortCommand() throws MoneyBagProMaxException {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());
        Command command = parser.parse("sort by/amount");
        assertInstanceOf(SortCommand.class, command);
    }

    @Test
    public void parse_sortByCategory_returnsSortCommand() throws MoneyBagProMaxException {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());
        Command command = parser.parse("sort by/category");
        assertInstanceOf(SortCommand.class, command);
    }

    @Test
    public void parse_sortNoArguments_throwsException() {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());
        assertThrows(MoneyBagProMaxException.class, () -> parser.parse("sort"));
    }

    @Test
    public void parse_sortInvalidCriteria_throwsException() {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());
        assertThrows(MoneyBagProMaxException.class, () -> parser.parse("sort by/invalid"));
    }

    @Test
    public void parse_sortMissingByPrefix_throwsException() {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());
        assertThrows(MoneyBagProMaxException.class, () -> parser.parse("sort date"));
    }

    @Test
    public void parse_undo_returnsUndoCommand() throws MoneyBagProMaxException {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());
        Command command = parser.parse("undo");
        assertInstanceOf(UndoCommand.class, command);
    }

    @Test
    public void parse_redo_returnsRedoCommand() throws MoneyBagProMaxException {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());
        Command command = parser.parse("redo");
        assertInstanceOf(RedoCommand.class, command);
    }

    @Test
    public void parse_addWithRecFlag_returnsAddRecurringCommand() throws MoneyBagProMaxException {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());
        Command command = parser.parse("add food/10 rec/weekly");
        assertInstanceOf(AddRecurringCommand.class, command);
    }

    @Test
    public void parse_addWithoutRecFlag_returnsAddCommand() throws MoneyBagProMaxException {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());
        Command command = parser.parse("add food/10");
        assertInstanceOf(AddCommand.class, command);
    }

    @Test
    public void parse_listRec_returnsListRecurringCommand() throws MoneyBagProMaxException {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());
        Command command = parser.parse("list-rec");
        assertInstanceOf(ListRecurringCommand.class, command);
    }

    @Test
    public void parse_deleteRec_returnsDeleteRecurringCommand() throws MoneyBagProMaxException {
        RecurringTransactionList recurringList = new RecurringTransactionList();
        Parser parser = new Parser(new UndoRedoManager(), recurringList);
        Command command = parser.parse("delete-rec 1");
        assertInstanceOf(DeleteRecurringCommand.class, command);
    }

    @Test
    public void parse_genRec_returnsGenerateRecurringCommand() throws MoneyBagProMaxException {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());
        Command command = parser.parse("gen-rec");
        assertInstanceOf(GenerateRecurringCommand.class, command);
    }

    @Test
    public void parse_addWithInvalidFrequency_throwsException() {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());
        assertThrows(MoneyBagProMaxException.class, () -> parser.parse("add food/10 rec/yearly"));
    }

    @Test
    public void parse_deleteRecNoArguments_throwsException() {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());
        assertThrows(MoneyBagProMaxException.class, () -> parser.parse("delete-rec"));
    }

    @Test
    public void parse_deleteRecNonNumericIndex_throwsException() {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());
        assertThrows(MoneyBagProMaxException.class, () -> parser.parse("delete-rec abc"));
    }

    @Test
    public void parse_addWithRecAndDateAfterFreq_returnsAddRecurringCommand() throws MoneyBagProMaxException {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());
        Command command = parser.parse("add food/10 rec/weekly d/2026-03-01");
        assertInstanceOf(AddRecurringCommand.class, command);
    }

    @Test
    public void parseFilterCommand_missingDates_throwsException() {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());

        String input = "filter from/ to/";
        MoneyBagProMaxException exception = assertThrows(MoneyBagProMaxException.class, () -> parser.parse(input));
        assertTrue(exception.getMessage().contains("Missing 'from'"));
    }

    @Test
    public void parseFilterCommand_invalidDateFormat_throwsException() {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());
        String input = "filter from/31-12-2026 to/2026-12-31";

        MoneyBagProMaxException exception = assertThrows(MoneyBagProMaxException.class, () -> parser.parse(input));
        assertTrue(exception.getMessage().contains("Invalid date format — expected YYYY-MM-DD."));
    }

    @Test
    public void parseFilterCommand_fromAfterTo_throwsException() {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());

        String input = "filter from/2026-12-31 to/2026-01-01";
        MoneyBagProMaxException exception = assertThrows(MoneyBagProMaxException.class, () -> parser.parse(input));
        assertTrue(exception.getMessage().contains("The 'from' date cannot be after the 'to' date!"));
    }

    @Test
    public void parse_addWithExtraSpacesBetweenParams_returnsAddCommand() throws MoneyBagProMaxException {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());
        Command command = parser.parse("add food/10  desc/test");
        assertInstanceOf(AddCommand.class, command);
    }

    @Test
    public void parse_addWithExtraSpacesBeforeDesc_returnsAddCommand() throws MoneyBagProMaxException {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());
        Command command = parser.parse("add food/10   desc/lunch   d/2026-01-15");
        assertInstanceOf(AddCommand.class, command);
    }

    @Test
    public void parse_addWithLeadingAndTrailingSpaces_returnsAddCommand() throws MoneyBagProMaxException {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());
        Command command = parser.parse("   add food/10 desc/test   ");
        assertInstanceOf(AddCommand.class, command);
    }

    @Test
    public void parse_commandWithExtraSpacesAfterCommandWord_returnsCorrectCommand()
            throws MoneyBagProMaxException {
        Parser parser = new Parser(new UndoRedoManager(), new RecurringTransactionList());
        Command command = parser.parse("sort  by/date");
        assertInstanceOf(SortCommand.class, command);
    }
}

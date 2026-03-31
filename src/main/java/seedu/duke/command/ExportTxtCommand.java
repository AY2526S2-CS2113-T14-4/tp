package seedu.duke.command;

import seedu.duke.MoneyBagProMaxException;
import seedu.duke.budget.Budget;
import seedu.duke.storage.TextFileExporter;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;

public class ExportTxtCommand extends Command {

    private final String filePath;

    public ExportTxtCommand(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void execute(TransactionList list, Budget budget, Ui ui) throws MoneyBagProMaxException {
        assert filePath != null && !filePath.isBlank() : "File path should not be blank";
        new TextFileExporter().export(filePath);
    }
}

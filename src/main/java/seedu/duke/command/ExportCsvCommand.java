package seedu.duke.command;

import seedu.duke.MoneyBagProMaxException;
import seedu.duke.budget.Budget;
import seedu.duke.storage.CsvExporter;
import seedu.duke.transactionlist.TransactionList;
import seedu.duke.ui.Ui;

import java.nio.file.Path;

public class ExportCsvCommand extends Command {

    private final String filePath;

    public ExportCsvCommand(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void execute(TransactionList list, Budget budget, Ui ui) throws MoneyBagProMaxException {
        assert list != null : "TransactionList should not be null";
        assert filePath != null && !filePath.isBlank() : "File path should not be blank";
        Path result = new CsvExporter().export(list, filePath);
        ui.showMessage("Exported " + list.size() + " transactions to: " + result.toAbsolutePath());
    }
}

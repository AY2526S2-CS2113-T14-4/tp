package seedu.duke;
//todo: list, delete, income

public class Parser {
    /**
     * Checks user command and calls functions according to the command
     *
     * @param input user input string
     * @param list  the current list of transactions
     * @param ui    ui class
     */
    public void parse(String input, TransactionList list, Ui ui) {
        if (input.startsWith("add ")) {//can replace with "add income" for priority
            String args = input.substring(4).trim();
            String[] parts = args.split("/", 2);

            if (parts.length < 2) {
                ui.showMessage("Invalid, try: add [category]/PRICE");
                return;
            }

            try {
                String category = parts[0].trim();
                double amount = Double.parseDouble(parts[1].trim());
                Expense expense = new Expense(category, amount);
                list.add(expense);
                ui.showMessage("Added: " + expense);
            } catch (NumberFormatException e) {
                ui.showMessage("Invalid price.");
            }
        } else if (input.equals("list")) {
            if (list.size() == 0) {
                ui.showMessage("No transactions found.");
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                ui.showMessage((i + 1) + ". " + list.get(i));
            }
        } else if (input.startsWith("delete ")) {
            String indexText = input.substring("delete ".length()).trim();

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
        } else if (input.startsWith("help")) {
            ui.showHelp();
        } else {
            ui.showMessage("Unknown command.");
        }
    }
}

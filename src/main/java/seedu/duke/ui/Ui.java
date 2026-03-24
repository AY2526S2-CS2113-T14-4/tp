package seedu.duke.ui;

import seedu.duke.transactionlist.TransactionList;

import java.util.Scanner;

public class Ui {

    public static final String ANSI_BRIGHT_GREEN = "\u001B[1;32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static final String DIVIDER = "_".repeat(60);

    private final Scanner scanner = new Scanner(System.in);

    public String readInput() {
        System.out.println();
        System.out.print(ANSI_YELLOW);
        System.out.print("Enter a command: ");
        System.out.print(ANSI_RESET);
        return scanner.nextLine().trim();
    }

    public void showHelp() {
        String separator = "=".repeat(60);
        String helpMessage = """
                %s
                                  MoneyBagProMax Help Menu
                %s
                1. Add Expense : `add [expense-category]/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD]`
                                 - desc/ and d/ are optional.
                                 - Date defaults to today if omitted.
                                 - Valid categories: `food`, `transport`, `utilities`,
                                   `education`, `rent`, `medical`, `misc`
                                 - Example: add food/10 desc/lunch d/2025-03-01
                2. Add Income  : `add [income-category]/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD]`
                                 - desc/ and d/ are optional.
                                 - Date defaults to today if omitted.
                                 - Valid categories: `salary`, `freelance`, `investment`,
                                   `business`, `gift`, `misc`
                                 - Example: add salary/500 desc/allowance d/2026-03-01
                3. List        : `list`
                                 - Lists all current transactions in your app.
                4. Find        : `find [KEYWORD]`
                                 - Searches for transactions containing the keyword
                                   in their category, description or date.
                                 - Example: find lunch
                                 - Example: find 2026-03
                5. Summary     : `summary [category]`
                                 - Shows overall totals or specific category totals.
                                 - Valid types: `all`, `expense`, `income`, or specific categories.
                                 - Example: summary all
                6. Sort        : `sort by/[CRITERIA]`
                                 - Sorts and displays transactions by the given criteria.
                                 - Valid criteria: `date`, `amount`, `category`
                                 - Example: sort by/date
                7. Delete      : `delete [ENTRY INDEX]`
                                 - Deletes a transaction using its number from the `list`.
                                 - Example: delete 3
                8. Edit        : `edit [INDEX] [category]/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD]`
                                 - Replaces a transaction at INDEX with the new values.
                                 - Same category/price/desc/date format as `add`.
                                 - Example: edit 3 food/20 desc/dinner d/2026-03-20                 
                9. Undo        : `undo`
                                 - Reverses the last add or delete action.
                10. Redo        : `redo`
                                 - Re-applies the last undone action.
                11. Budget     : `budget set [AMOUNT]` or `budget status`
                                 - Sets the monthly budget or shows the current budget status.
                                 - `budget set` stores the monthly spending limit.
                                 - `budget status` shows budget, spent amount, remaining amount,
                                   and progress bar.
                                 - Example: budget set 1000
                                 - Example: budget status
                12. Stats      : `stats`
                                 - Displays spending analytics for recorded transactions.
                                 - Includes highest and lowest transaction, most frequent category,
                                   average spending per category, and spending trend.
                                 - Example: stats
                13. Exit       : `exit`
                                 - Exits the program.
                %s""".formatted(separator, separator, separator);

        System.out.println(helpMessage);
    }

    public void showWelcomeMessage() {
        printWelcomeBanner();
        System.out.println(DIVIDER);
        System.out.println("Welcome to MoneyBagProMax, give us your money.");
        System.out.println("Enter `help` to check the list of available commands.");
        System.out.println(DIVIDER);
    }

    public void showExitMessage() {
        System.out.println("Goodbye. Thank you for investing in MoneyBagProMax.");
    }

    private void printWithDividers(String message) {
        System.out.println(DIVIDER);
        System.out.println(message);
        System.out.println(DIVIDER);
    }

    public void showMessage(String message) {
        assert message != null : "Message should not be null";
        System.out.println(message);
    }

    public void showList(TransactionList list) {
        System.out.println(DIVIDER);

        if (list.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (int i = 0; i < list.size(); i++) {
                System.out.println((i + 1) + ". " + list.get(i));
            }
        }

        System.out.println(DIVIDER);
    }

    public void showOverallSummary(double income, double expense) {
        assert income >= 0 : "Income total should not be negative";
        assert expense >= 0 : "Expense total should not be negative";
        System.out.println("===== Overall Summary =====");
        System.out.printf("Total Income: $%.2f%n", income);
        System.out.printf("Total Expense: $%.2f%n", expense);
        System.out.printf("Net Balance: $%.2f%n", (income - expense));
        System.out.println("===========================");
    }

    public void showCategorySummary(String category, double categoryTotal) {
        assert category != null && !category.isBlank() : "Category should not be null or blank";
        assert categoryTotal >= 0 : "Category total should not be negative";
        printWithDividers(String.format("Total for %s: $%.2f", category, categoryTotal));
    }

    public static void printWelcomeBanner() {
        System.out.print(ANSI_BRIGHT_GREEN);
        System.out.print("""
                    .___.        
                   /     \\       
                  |       |      
                  `-.   .-`      
                 /   \\ /   \\     
                |   $$$$$   |    
                |  $$$$$$$  |    
                |   $$$$$   |    
                 \\         /     
                  `-------`      
            """);
        System.out.print(ANSI_RESET);
    }
}

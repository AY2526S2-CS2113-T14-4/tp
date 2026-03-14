package seedu.duke;

//can be customised more, problems for later

import java.util.Scanner;

public class Ui {
    private final Scanner scanner = new Scanner(System.in);

    public String readInput() {
        System.out.print("Enter a command: ");
        return scanner.nextLine().trim();
    }

    public void showHelp() {
        String separator = "=".repeat(60);
        String helpMessage = """
                %s
                                  MoneyBagProMax Help Menu
                %s
                1. Add Expense : `add [category]/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD]`
                                 - desc/ and d/ are optional.
                                 - Date defaults to today if omitted.
                                 - Valid categories: `food`, `transport`, `utilities`,
                                   `education`, `rent`, `medical`, `misc`
                                 - Example: add food/10 desc/lunch d/2025-03-01
                2. Add Income  : `add income/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD]`
                                 - desc/ and d/ are optional.
                                 - Example: add income/500 desc/allowance
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
                6. Delete      : `delete [ENTRY INDEX]`
                                 - Deletes a transaction using its number from the `list`.
                                 - Example: delete 3 
                7. Exit        : `exit`
                                 - Exits the program.
                %s""".formatted(separator, separator, separator);

        System.out.println(helpMessage);
    }

    public void showWelcomeMessage() {
        System.out.println("Welcome to MoneyBagProMax, give us your money.");
        System.out.println("Enter `help` to check the list of available commands.");
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showOverallSummary(double income, double expense) {
        System.out.println("----- Overall Summary -----");
        System.out.printf("Total Income: $%.2f%n", income);
        System.out.printf("Total Expense: $%.2f%n", expense);
        System.out.printf("Net Balance: $%.2f%n", (income - expense));
        System.out.println("--------------------------");
    }

    public void showCategorySummary(String category, double categoryTotal) {
        System.out.printf("Total for %s: $%.2f%n", category, categoryTotal);
    }
}

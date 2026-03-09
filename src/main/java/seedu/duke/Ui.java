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
        System.out.println("Listing all current transactions: `list`");
        System.out.println("Adding an expense: `add [category]/PRICE [desc/DESCRIPTION]`\n"
                + " - Category can be any user-initiated category for now.\n"
                + " - desc/ is optional. Example: add food/10 desc/lunch");
        System.out.println("Adding an income: `add income/PRICE`");
        System.out.println("Deleting an expense or income: `delete [ENTRY INDEX]`");
        System.out.println("Summary of all expenses or incomes: `summary [category]`\n"
                + " - Category includes: `all`, `expense`, `outflow`");
        System.out.println("Exiting the program: `exit`");
    }

    public void showWelcomeMessage() {
        System.out.println("Welcome to MoneyBagProMax, give us your money.");
        System.out.println("Enter `help` to check the list of available commands.");
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}

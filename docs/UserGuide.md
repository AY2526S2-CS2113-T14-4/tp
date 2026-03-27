# MoneyBagProMax User Guide #

MoneyBagProMax is an Expense Tracker which is a personal finance management application designed to help you track your income and expenses,
manage budgets, and gain insights into your spending habits via a simple application installed in the Command Line Interface (˶ᵔ ᵕ ᵔ˶)!

---

## Table of Contents
- [Quick Start](#quick-start)
- [Features](#features)
    - [Adding an Expense: `add [expense-category]`](#adding-an-expense-add-expense-category)
    - [Adding an Income: `add [income-category]`](#adding-an-income-add-income-category)
    - [Listing all Transactions: `list`](#listing-all-transactions-list)
    - [Finding a Transaction: `find`](#finding-a-transaction-find)
    - [Viewing a Summary: `summary`](#viewing-a-summary-summary)
    - [Sorting Transactions: `sort`](#sorting-transactions-sort)
    - [Deleting a Transaction: `delete`](#deleting-a-transaction-delete)
    - [Editing a Transaction: `edit`](#editing-a-transaction-edit)
    - [Undoing an Action: `undo`](#undoing-an-action-undo)
    - [Redoing an Action: `redo`](#redoing-an-action-redo)
    - [Managing your Budget: `budget`](#managing-your-budget-budget)
    - [Viewing Spending Statistics: `stats`](#viewing-spending-statistics-stats)
    - [Filtering Transactions: `filter`](#filtering-transactions-filter)
    - [Exiting the Application: `exit`](#exiting-the-application-exit)
- [Command Summary](#command-summary)

---

## Quick Start
Ensure that you have Java 17 installed on your personal computer. You may download Java 17 for your
respective system [here](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).
> You may want to use SDKMAN! to manage your Java installation in the event you have multiple versions [here](https://sdkman.io/).

1. Download the latest MoneyBagProMax JAR file from the **GitHub Releases Page**.
2. Copy the JAR file into the folder that you want to use as the home folder for the application.
3. Locate your JAR file and copy the filepath. Example: `/Users/{yourName}/Desktop/downloads/expense-tracker.jar`
4. Open the command line terminal:
    - *Windows*: Open Command Prompt
    - *macOS/Linux*: Open Terminal
5. Use the `cd` command to navigate to the folder where your JAR file is saved. For example:\
   `cd /Users/{yourName}/Desktop/downloads/`
6. Run the application in the command line using the following command:\
   `java -jar MoneyBagProMax.jar`
7. After a few seconds, the application will launch in your terminal.
8. Type the commands into the terminal and press **Enter** to execute the command.

---

## Features

> [!NOTE]
> ‼️ **Notes about the command format**:
> - Words in **UPPER_CASE** are placeholders to be supplied by you, the user.
> - For example: in `delete ENTRY_INDEX`, **ENTRY_INDEX** is a placeholder that should be replaced with the actual index number.
> - Items in `[square brackets]` are **optional** parameters.
> - **Dates** must follow the `YYYY-MM-DD` format (e.g. `2026-03-01`).
> - **Prices** must be a positive number (e.g. `10` or `10.50`).
> - If a date is omitted, it defaults to today's date.

---

### Adding an Expense: `add [expense-category]`
Adds an expense transaction to your list.

**Format**: `add [expense-category]/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD]`

**Valid expense categories**: `food`, `transport`, `utilities`, `education`, `rent`, `medical`, `misc`

**Examples**:
- `add food/10 desc/lunch d/2025-03-01` Adds a food expense of $10.00 described as *"lunch"* on 1st March 2025.
- `add transport/3.50` Adds a transport expense of $3.50 with today's date and no description.

> [!TIP]
> The `desc/` and `d/` fields are optional. If the date is omitted, today's date will be used automatically.

---

### Adding an Income: `add [income-category]`
Adds an income transaction to your list.

**Format**: `add [income-category]/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD]`

**Valid income categories**: `salary`, `freelance`, `investment`, `business`, `gift`, `misc`

**Examples**:
- `add salary/500 desc/allowance d/2026-03-01` Adds a salary income of $500.00 described as *"allowance"* on 1st March 2026.
- `add freelance/150` Adds a freelance income of $150.00 with today's date and no description.

> [!TIP]
> The `desc/` and `d/` fields are optional. If the date is omitted, today's date will be used automatically.

---

### Listing all Transactions: `list`
Displays all current transactions saved in the application, including both expenses and income.

**Format**: `list`

**Examples**:
- `list` Lists all transactions with their index numbers, categories, amounts, descriptions, and dates.

> [!TIP]
> Take note of the index numbers shown in the list — you will need them for `delete`, `edit`, and other index-based commands.

---

### Finding a Transaction: `find`
Searches for transactions that contain a specific keyword in their category, description, or date.

**Format**: `find KEYWORD`

**Examples**:
- `find lunch` Searches for all transactions containing the word *"lunch"*.
- `find 2026-03` Searches for all transactions from March 2026.

> [!NOTE]
> If no transactions match your keyword, an empty result will be returned.

---

### Viewing a Summary: `summary`
Displays overall totals or specific category totals for your transactions.

**Format**: `summary [TYPE]`

**Valid types**: `all`, `expense`, `income`, or any specific category (e.g. `food`, `salary`)

**Examples**:
- `summary all` Shows the total income, total expenses, and net balance.
- `summary expense` Shows the total amount spent across all expense categories.
- `summary food` Shows the total amount spent specifically on food.

---

### Sorting Transactions: `sort`
Sorts and displays all transactions by the given criteria.

**Format**: `sort by/CRITERIA`

**Valid criteria**: `date`, `amount`, `category`

**Examples**:
- `sort by/date` Displays all transactions sorted from earliest to latest date.
- `sort by/amount` Displays all transactions sorted by amount.
- `sort by/category` Displays all transactions sorted alphabetically by category.

> [!NOTE]
> Sorting only affects the display order. The original list order and index numbers remain unchanged.

---

### Deleting a Transaction: `delete`
Removes a transaction from the list using its index number.

**Format**: `delete ENTRY_INDEX`

**Examples**:
- `delete 3` Deletes the 3rd transaction in the list.

> [!TIP]
> Use `list` first to find the correct index number of the transaction you wish to delete.

---

### Editing a Transaction: `edit`
Replaces an existing transaction at the specified index with new values.

**Format**: `edit INDEX [category]/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD]`

- The category must be a valid expense or income category.
- The same optional `desc/` and `d/` rules from `add` apply here.

**Examples**:
- `edit 3 food/20 desc/dinner d/2026-03-20` Replaces the 3rd transaction with a food expense of $20.00 described as *"dinner"* on 20th March 2026.

> [!TIP]
> Use `list` first to find the correct index number of the transaction you wish to edit.

---

### Undoing an Action: `undo`
Reverses the last `add` or `delete` action performed.

**Format**: `undo`

**Examples**:
- `undo` Undoes the most recent add or delete action.

> [!NOTE]
> Only `add` and `delete` actions can be undone. Other operations such as `edit` or `sort` cannot be reversed with `undo`.

---

### Redoing an Action: `redo`
Re-applies the last action that was undone with `undo`.

**Format**: `redo`

**Examples**:
- `redo` Re-applies the most recently undone action.

> [!NOTE]
> `redo` is only available immediately after an `undo`. Performing any new action will clear the redo history.

---

### Managing your Budget: `budget`
Sets a monthly spending limit or checks your current budget status.

**Format**:
- `budget set AMOUNT` — Sets your monthly budget to the specified amount.
- `budget status` — Displays your current budget, amount spent, remaining balance, and a progress bar.

**Examples**:
- `budget set 1000` Sets your monthly budget to $1000.00.
- `budget status` Shows how much of your budget has been used this month.

> [!TIP]
> Use `budget status` regularly to stay on top of your spending and avoid going over budget!

---

### Viewing Spending Statistics: `stats`
Displays spending analytics based on your recorded transactions.

**Format**: `stats`

Includes:
- Highest and lowest transaction amounts
- Most frequently used expense category
- Average spending per category
- Overall spending trend

**Examples**:
- `stats` Displays a full analytics breakdown of your transactions.

---

### Filtering Transactions: `filter`
Filters and displays only the transactions that fall within a specified date range.

**Format**: `filter [from/YYYY-MM-DD] [to/YYYY-MM-DD]`

**Examples**:
- `filter from/2026-01-01 to/2026-03-31` Displays all transactions from 1st January 2026 to 31st March 2026.

> [!NOTE]
> Both `from/` and `to/` are compulsory.

---

### Exiting the Application: `exit`
Exits the MoneyBagProMax application.

**Format**: `exit`

**Examples**:
- `exit` Closes the application.

---

## FAQ

**Q**: How do I transfer my data to another computer?

**A**: Install the MoneyBagProMax application on the new computer and run it once to generate the default data file. 
Then, overwrite the generated data/transactions.txt file with the one from your previous computer to transfer all your information.

---

## Editing the Data File

MoneyBagProMax automatically saves your task data in a text file located at `./data/transactions.txt`, relative to the directory where you run the program. 
Advanced users can update their tasks by directly editing this file.

> ⚠️**Caution:** Be cautious when editing the file directly, as improper formatting may cause errors or data loss when the application is next launched.

---

## Command Summary

| Action          | Format                                                              | Example                                        |
|-----------------|---------------------------------------------------------------------|------------------------------------------------|
| **Add Expense** | `add [expense-category]/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD]`   | `add food/10 desc/lunch d/2025-03-01`          |
| **Add Income**  | `add [income-category]/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD]`    | `add salary/500 desc/allowance d/2026-03-01`   |
| **List**        | `list`                                                              | —                                              |
| **Find**        | `find KEYWORD`                                                      | `find lunch`                                   |
| **Summary**     | `summary [TYPE]`                                                    | `summary all`                                  |
| **Sort**        | `sort by/CRITERIA`                                                  | `sort by/date`                                 |
| **Delete**      | `delete ENTRY_INDEX`                                                | `delete 3`                                     |
| **Edit**        | `edit INDEX [category]/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD]`    | `edit 3 food/20 desc/dinner d/2026-03-20`      |
| **Undo**        | `undo`                                                              | —                                              |
| **Redo**        | `redo`                                                              | —                                              |
| **Budget Set**  | `budget set AMOUNT`                                                 | `budget set 1000`                              |
| **Budget Status** | `budget status`                                                   | —                                              |
| **Stats**       | `stats`                                                             | —                                              |
| **Filter**      | `filter [from/YYYY-MM-DD] [to/YYYY-MM-DD]`                         | `filter from/2026-01-01 to/2026-03-31`         |
| **Exit**        | `exit`                                                              | —                                              |


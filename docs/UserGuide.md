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
    - [Managing Custom Categories: `category`](#managing-custom-categories-category)
    - [Viewing Spending Statistics: `stats`](#viewing-spending-statistics-stats)
    - [Adding a Recurring Transaction: `add ... rec/FREQUENCY`](#adding-a-recurring-transaction-add--recfrequency)
    - [Listing Recurring Transactions: `list-rec`](#listing-recurring-transactions-list-rec)
    - [Deleting a Recurring Transaction: `delete-rec`](#deleting-a-recurring-transaction-delete-rec)
    - [Generating Recurring Transactions: `gen-rec`](#generating-recurring-transactions-gen-rec)
    - [Filtering Transactions: `filter`](#filtering-transactions-filter)
    - [Exporting to CSV: `export-csv`](#exporting-to-csv-export-csv)
    - [Exporting Data File: `export-data`](#exporting-data-file-export-data)
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
Adds an expense by the given category, amount, optional description and optional date.

**Format**: `add EXPENSE_CATEGORY/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD]`

**Examples**:
- `add food/10` Adds a food expense of $10.00 with no description, dated today.
- `add transport/3.50 desc/bus ride` Adds a transport expense of $3.50 with the description *"bus ride"*, dated today.
- `add medical/25 desc/checkup d/2026-03-01` Adds a medical expense of $25.00 with the description *"checkup"*, dated 1st March 2026.

> [!NOTE]
> If the date is omitted, it defaults to today's date. If the description is omitted, the transaction is recorded without one.

---

### Adding an Income: `add [income-category]`
Adds an income transaction to your list.

**Format**: `add [income-category]/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD]`
**Valid income categories**: `salary`, `freelance`, `investment`, `business`, `gift`, `misc`

**Examples**:
- `add salary/500 desc/allowance d/2026-03-01` Adds a salary income of $500.00 described as *"allowance"* on 1st March 2026.
- `add freelance/150` Adds a freelance income of $150.00 with today's date and no description.

> [!NOTE] If the date is omitted, it defaults to today's date. If the description is omitted, the transaction is recorded without one.

---

### Listing all Transactions: `list`
Displays all recorded transactions in a numbered list.

**Format**: list

**Examples**:
- `list` Displays all transactions currently stored in the application.

> [!NOTE]
> If there are no transactions recorded, the application will show an empty list message instead.
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
Displays transactions sorted by the specified criterion. The underlying list order is **not changed** — this is a display-only operation.

**Format**: `sort by/CRITERIA`

**Valid criteria:**
- `date` — ascending (earliest first)
- `amount` — descending (largest first)
- `category` — alphabetical A–Z (case-insensitive)

**Examples**:
- `sort by/date` — shows all transactions from earliest to latest.
- `sort by/amount` — shows all transactions from highest to lowest amount.
- `sort by/category` — shows all transactions sorted alphabetically by category.

> [!NOTE]
> Sort does not change the indices used by `delete` and `edit`. Use `list` to see the original insertion order.

---

### Deleting a Transaction: `delete`
Deletes the transaction at the specified index in the displayed list.

**Format**: `delete ENTRY_INDEX`

**Examples**:
- `delete 3` Deletes the 3rd transaction in the list.

> [!NOTE]
> The index must be a valid positive integer corresponding to an existing transaction.
---

### Editing a Transaction: `edit`
Replaces an existing transaction at the specified index with new values.
All fields must be provided — the edit replaces the entire transaction, not individual fields.

**Format**: `edit ENTRY_INDEX CATEGORY/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD]`

**Examples**:
- `edit 2 food/15` Replaces the 2nd transaction with a food expense of $15.00, dated today.
- `edit 1 salary/3000 desc/march pay d/2026-03-01` Replaces the 1st transaction with a salary income of $3000.00 with the description *"march pay"*, dated 1st March 2026.

> [!NOTE]
> Use `list` first to confirm the index of the transaction you want to edit. The edit can be reversed with `undo`.

---

### Undoing an Action: `undo`
Reverses the last mutating command (`add`, `delete`, or `edit`). Can be called repeatedly to step back through history.

**Format**: `undo`

**Examples**:
- `undo` — reverses the last add, delete, or edit operation.

> [!NOTE]
> Undo history is reset when you exit the application. Only `add`, `delete`, and `edit` are undoable.

---

### Redoing an Action: `redo`
Re-applies the last undone action. Only available immediately after an `undo`.

**Format**: `redo`

**Examples**:
- `redo` — re-applies the last undone operation.

> [!NOTE]
> Performing any new mutating command (`add`/`delete`/`edit`) after an `undo` clears the redo history.

---

### Managing your Budget: `budget`
Allows you to set a monthly budget and check your current budget usage.

**Format**:
- `budget set AMOUNT`
- `budget status`

**Examples**:
- `budget set 1000` Sets your monthly budget to $1000.
- `budget status` Displays the current monthly budget, total spent for the month, remaining budget, and percentage used.

> [!NOTE]
> Budget usage is calculated using expense transactions from the current month only.
---

### Viewing Spending Statistics: `stats`
Displays spending analytics for recorded transactions.

The statistics include:
- total income
- total expense
- highest expense
- lowest expense
- highest income
- most frequent expense category
- average spending per category
- spending trend
- budget usage percentage

**Format**: `stats`

**Examples**:
- `stats` Displays the full statistics summary for all recorded transactions.

> [!NOTE]
> General statistics are based on all recorded transactions, while budget usage is based on the current month's expenses.
---
### Managing Custom Categories: `category`
Allows you to create and manage your own expense categories beyond the 7 built-in ones.
Custom categories are saved automatically and available across sessions.

**Format**:
- `category add/NAME`
- `category remove/NAME`
- `category list`

**Rules for category names**:
- Must contain only letters, digits, hyphens (`-`), or underscores (`_`)
- No spaces or special characters allowed
- Names are case-insensitive (`Groceries` and `groceries` are treated as the same)

**Examples**:
- `category add/groceries` — creates a new custom category called "groceries"
- `category remove/groceries` — deletes the custom category "groceries"
- `category list` — lists all available expense categories, built-in and custom

> [!NOTE]
> Built-in categories (`food`, `transport`, `utilities`, `education`, `rent`, `medical`, `misc`) cannot be removed.
> A category cannot be removed if it is currently used by an existing transaction — delete those transactions first.
> Once a custom category is added, it can be used with `add` and `edit` just like a built-in category.
---

### Adding a Recurring Transaction: `add ... rec/FREQUENCY`
Creates a recurring transaction template. MoneyBagProMax will automatically generate the corresponding expense or income entries on startup and when you run `gen-rec`, covering all due dates since the last generation.

**Format**: `add [category]/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD] rec/FREQUENCY`

- `FREQUENCY` must be one of: `daily`, `weekly`, `monthly` (case-insensitive)
- `d/YYYY-MM-DD` sets the start date; defaults to today if omitted
- The category determines whether the entry is an expense or income (same valid categories as `add`)
> [!NOTE]
> For valid expense and income categories, see [Adding an Expense](#adding-an-expense-add-expense-category) and [Adding an Income](#adding-an-income-add-income-category).

**Examples**:
- `add food/10 desc/lunch rec/daily` — Creates a daily $10 lunch expense template starting today.
- `add salary/3000 desc/monthly-pay d/2026-04-01 rec/monthly` — Creates a monthly $3000 income template starting 2026-04-01.
---

### Listing Recurring Transactions: `list-rec`
Displays all stored recurring transaction templates with their index, frequency, amount, category, description, and start date.

**Format**: `list-rec`

**Examples**:
- `list-rec` — Lists all recurring transaction templates currently stored.

> [!NOTE]
> If no recurring templates exist, the application will show an empty-list message.

---

### Deleting a Recurring Transaction: `delete-rec`
Removes a recurring transaction template by its index shown in `list-rec`. This does not affect transactions that have already been generated from the template.

**Format**: `delete-rec INDEX`

- `INDEX` is the 1-based index from `list-rec`

**Examples**:
- `delete-rec 2` — Deletes the second recurring template in the list.

---

### Generating Recurring Transactions: `gen-rec`
Generates all pending transaction entries for every recurring template, up to today's date. Skips any dates that have already been generated to prevent duplicates. Recurring transactions are also generated automatically each time the application starts.

**Format**: `gen-rec`

**Examples**:
- `gen-rec` — Generates all due recurring transactions for all templates.

> [!NOTE]
> If all templates are up-to-date, no new transactions are added.

---

### Filtering Transactions: `filter`
Filters and displays only the transactions that fall within a specified date range.

**Format**: `filter [from/YYYY-MM-DD] [to/YYYY-MM-DD]`

**Examples**:
- `filter from/2026-01-01 to/2026-03-31` Displays all transactions from 1st January 2026 to 31st March 2026.

> [!NOTE]
> Both `from/` and `to/` are compulsory.

---

### Exporting to CSV: `export-csv`
Exports all transactions to a `.csv` file for use in external tools like Microsoft Excel or Google Sheets.

**Format**: `export-csv FILEPATH`

**CSV columns**: `date`, `type`, `category`, `description`, `amount`

**Examples**:
- `export-csv ~/transactions.csv` Exports all transactions to `transactions.csv` in your home directory.
- `export-csv reports/june.csv` Exports to a `reports/` subfolder.

> [!NOTE]
> This is for external analysis only — the CSV cannot be reimported into MoneyBagProMax. For transferring data between devices, use `export-data` instead.

---

### Exporting Data File: `export-data`
Copies the internal data file to a location of your choice. Useful for backing up your data or transferring it to another device.

**Format**: `export-data FILEPATH`

**Examples**:
- `export-data ~/backup/transactions.txt` Copies the data file to a backup folder.

> [!NOTE]
> The exported file can be used to restore your data on another device. See the [FAQ](#faq) for transfer instructions.

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

MoneyBagProMax automatically saves your transaction data in two text files, both located in the `./data/` directory relative to where you run the program:
- `transactions.txt` — stores all recorded income and expense entries.
- `categories.txt` — stores your custom expense categories (created with `category add/NAME`).
- `recurring.txt` — stores your recurring transaction templates (created with `add ... rec/FREQUENCY`).

> ⚠️**Caution:** Be cautious when editing either file directly, as there are guards against file corruption and improper formatting. Failure to pass these checks may cause errors or data loss when the application is next launched.

---

## Command Summary

| Action                 | Format                                                                 | Example                                      |
|------------------------|------------------------------------------------------------------------|----------------------------------------------|
| **Add Expense**        | `add [expense-category]/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD]`       | `add food/10 desc/lunch d/2025-03-01`        |
| **Add Income**         | `add [income-category]/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD]`        | `add salary/500 desc/allowance d/2026-03-01` |
| **List**               | `list`                                                                 | —                                            |
| **Find**               | `find KEYWORD`                                                         | `find lunch`                                 |
| **Summary**            | `summary [TYPE]`                                                       | `summary all`                                |
| **Sort**               | `sort by/CRITERIA`                                                     | `sort by/date`                               |
| **Delete**             | `delete ENTRY_INDEX`                                                   | `delete 3`                                   |
| **Edit**               | `edit INDEX [category]/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD]`        | `edit 3 food/20 desc/dinner d/2026-03-20`    |
| **Undo**               | `undo`                                                                 | —                                            |
| **Redo**               | `redo`                                                                 | —                                            |
| **Budget Set**         | `budget set AMOUNT`                                                    | `budget set 1000`                            |
| **Budget Status**      | `budget status`                                                        | —                                            |
| **Category Add**       | `category add/NAME`                                                    | `category add/groceries`                     |
| **Category Remove**    | `category remove/NAME`                                                 | `category remove/groceries`                  |
| **Category List**      | `category list`                                                        | —                                            |
| **Stats**              | `stats`                                                                | —                                            |
| **Add Recurring**      | `add [category]/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD] rec/FREQUENCY` | `add food/10 desc/lunch rec/daily`           |
| **List Recurring**     | `list-rec`                                                             | —                                            |
| **Delete Recurring**   | `delete-rec INDEX`                                                     | `delete-rec 2`                               |
| **Generate Recurring** | `gen-rec`                                                              | —                                            |
| **Filter**             | `filter [from/YYYY-MM-DD] [to/YYYY-MM-DD]`                             | `filter from/2026-01-01 to/2026-03-31`       |
| **Export CSV**         | `export-csv FILEPATH`                                                  | `export-csv ~/transactions.csv`              |
| **Export Data**        | `export-data FILEPATH`                                                 | `export-data ~/backup/transactions.txt`      |
| **Exit**               | `exit`                                                                 | —                                            |
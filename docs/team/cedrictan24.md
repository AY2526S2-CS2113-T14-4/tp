# Cedric Tan's Project Portfolio Page

## Project: MoneyBagProMax

MoneyBagProMax is a command-line personal finance management application designed to help users track income and expenses, manage budgets, and gain insights into spending habits through financial statistics. The user interacts with it using a CLI, and the application is written in Java.

Given below are my contributions to the project.

---

## Summary of Contributions

### New Feature: Budget Feature
What it does:  
Allows the user to set a monthly budget and view the current budget status, including total expenses for the month, remaining budget, and percentage of budget used.

Justification:  
This feature improves the product significantly because users can monitor their monthly spending and avoid exceeding their budget.

Highlights:  
This feature required filtering transactions by month using transaction dates and integrating the budget calculation with the statistics feature. The implementation required changes to the TransactionList and StatsCommand to ensure budget usage is calculated correctly.

---

### New Feature: Statistics Feature
What it does:  
Displays financial statistics such as highest expense, lowest expense, highest income, most frequent category, average spending per category, spending trend, and budget usage.

Justification:  
This feature provides users with useful insights into their spending habits, allowing them to better manage their finances.

Highlights:  
The implementation required the use of data structures such as HashMap to calculate category frequency and averages. It also required refactoring existing code to avoid duplication when calculating highest and lowest transactions.

---

### Enhancements Implemented:
- Implemented the List Command to display all transactions.
- Implemented the Delete Command to remove transactions by index.
- Refactored duplicated code in TransactionList by introducing a helper method to compute highest and lowest transactions.
- Added defensive programming using assertions and logging.
- Wrote JUnit tests for multiple commands including DeleteCommand, ListCommand, BudgetCommand, StatsCommand, HelpCommand, and ExitCommand.

---

### Code Contributed
[RepoSense link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=cedrictan24&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=)

---

### Contributions to the User Guide
I contributed the following sections to the User Guide:
- List command
- Delete command
- Budget command
- Stats command

Each section includes the command format, examples, and explanations.

---

### Contributions to the Developer Guide
I contributed the following sections to the Developer Guide:
- Implementation of the Budget feature
- Implementation of the Statistics feature
- Refactoring of TransactionList
- Defensive programming (assertions and logging)
- Sequence diagrams of the Budget feature
- Sequence diagrams of the Statistics feature

---

### Contributions to Team-Based Tasks
- Reviewed pull requests and provided feedback on code quality and documentation.
- Resolved merge conflicts between branches.
- Fixed checkstyle issues and improved code quality.
- Performed manual testing and text-ui testing.

---

### Review / Mentoring Contributions
- Reviewed teammates’ pull requests and suggested improvements.

---

### Tools Used
- Gradle for build automation
- JUnit for unit testing
- Checkstyle for code quality
- GitHub for version control and pull request management

# Zhu Yicheng - Project Portfolio Page

## Project: MoneyBagProMax
MoneyBagProMax is a command-line personal finance management application designed to help users track income and expenses, manage budgets, and gain insights into spending habits through financial statistics. The user interacts with it using a CLI, and the application is written in Java.
Given below are my contributions to the project.
---

## Summary of Contributions

### New Feature: Transaction Class
What it does:
Provides the abstract base class shared by all transaction types in the application. It defines the four common fields (`category`, `amount`, `description`, `date`), their accessor methods, and the abstract `getType()` method that each subclass must implement.
Justification:
Centralising the shared state in a single abstract class eliminates field duplication between `Income` and `Expense`, and allows the rest of the application to operate on a single `Transaction` type without needing `instanceof` checks.
Highlights:
All fields are declared `protected final`, making them immutable after construction. Rather than mutating fields in place, editing a transaction requires constructing a new object and swapping it into the list. 
---

### New Feature: Expense Class
What it does:
Represents an expenditure transaction. Extends Transaction and defines seven valid expense categories: food, transport, utilities, education, rent, medical, and misc. The formatted display (e.g. [Expense] food "lunch" $12.50 (2026-03-20)) omits the description if it is empty, keeping output clean.
Justification:
A dedicated Expense subclass keeps the category list and formatting logic together in one place. Exposing VALID_CATEGORIES as a public static final field allows AddCommand and EditCommand to reference it directly when deciding which transaction type to instantiate, without coupling those commands to the Parser.
Highlights:
Category validity is enforced at construction time via an assertion, matching how the rest of the codebase handles invalid input. It mirrors the structure of `Income` so adding a new transaction type only requires a new subclass.
---

### Enhancements Implemented:
- Added defensive programming via assertions in `Expense` and `EditCommand` constructors and `execute()` methods.
- Added Javadoc to `EditCommand` constructor and `execute()` method.
- Wrote JUnit tests for `EditCommand` (valid edit, undo/redo round-trip, out-of-bounds index) and for the `Expense` and `Transaction` classes.
- Added unit tests for `Parser.parseEditCommand()` covering valid input, missing arguments, and a non-integer index.
---

### Code Contributed
[RepoSense link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=yc-zzz&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=)
---

### New Feature: Edit Command
What it does:
Allows users to replace an existing transaction in the list with new values using the command `edit INDEX [category]/PRICE [desc/DESCRIPTION] [d/YYYY-MM-DD]`. The description and date are optional and default to an empty string and today's date respectively.
Justification:
Without an edit command, users who enter a transaction with a typo must delete it and re-add it manually. The edit command provides a single, atomic operation to correct any field of an existing transaction.
Highlights:
The command performs an atomic remove-then-insert: the old transaction is removed and the new one is inserted at the same index, keeping all other indices stable. It integrates with `UndoRedoManager` via `recordEdit()`, storing both the old and new transactions to support undo and redo. Index bounds and category validation are both checked before any list modification, ensuring the list is never left in a partially-edited state.
---

### Contributions to the User Guide
- Edit command section, including command format, optional parameters, and examples.

---

### Contributions to the Developer Guide
- Transaction class section, including the field table and rationale for immutability.
- Expense class section, including the category table, key methods, design considerations, and alternatives considered.
- Edit Transaction Feature section, including architecture and flow, implementation details, design considerations, alternatives considered, and future improvements.
- `EditSequenceDiagram.puml` sequence diagram illustrating the three execution paths (invalid index, invalid category, successful edit).

---

### Contributions to Team-Based Tasks
- Reviewed pull requests and provided feedback on correctness and code style.
- Added assertions and logging across multiple classes as part of a codebase-wide defensive programming pass.
- Performed manual testing of the edit command across valid and invalid inputs.
---

### Review / Mentoring Contributions
- Reviewed teammates' pull requests and suggested improvements to assertion coverage and Javadoc completeness.
---

### Tools Used
- Gradle for build automation
- JUnit for unit testing
- Checkstyle for code quality
- GitHub for version control and pull request management

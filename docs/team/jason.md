# Jason Chen's Project Portfolio Page

## Project: MoneyBagProMax

MoneyBagProMax is a command-line personal finance management application designed to help users track
income and expenses, manage budgets, and gain insights into spending habits through financial statistics.
The user interacts with it using a CLI, and the application is written in Java.

Given below are my contributions to the project.

---

## Summary of Contributions

### New Feature: Sort Command
**What it does:**
Allows users to sort and display their transactions by date (ascending), amount (descending), or category (alphabetical). The original list order is preserved — the sort is non-mutating and operates on a defensive copy.

**Justification:**
Users accumulate transactions over time in no particular order. Being able to sort by date or amount lets users quickly spot their largest expenses or review a chronological spending history without having to scroll through an unsorted list.

**Highlights:**
The comparator is resolved at execution time using a switch on the sort criteria string, keeping the logic self-contained in `SortCommand`. Because the original `TransactionList` is never reordered, other commands that depend on insertion-order indices (e.g. `delete`, `edit`) are unaffected.

---

### New Feature: Undo/Redo
**What it does:**
Allows users to undo the most recent mutating command (add, delete, edit) and redo it if they change their mind. Multiple sequential undos and redos are supported.

**Justification:**
Accidental deletions or edits are a common user error in any data-entry application. Without undo, the only recovery path is re-entering the data manually. This feature significantly reduces the risk of data loss from slip-of-the-finger mistakes.

**Highlights:**
Implemented using a dual-stack design in `UndoRedoManager`. Each mutating command records an `ActionPair` (action type, transaction, index) onto the undo stack at execution time. Performing any new mutating command clears the redo stack, which is the standard correct behaviour — it prevents redo from replaying actions that have been superseded. The `EDIT` action type stores both the old and new transaction so the original state can be restored exactly.

---

### New Feature: Recurring Transactions
**What it does:**
Allows users to define recurring transaction templates (e.g. a monthly salary or a weekly grocery expense) that can be automatically generated as real transactions on demand. Users can add, list, delete, and trigger generation of recurring entries.

**Justification:**
Many real-world income and expense entries repeat on a fixed schedule. Without this feature, users must re-enter identical transactions every month. Recurring transactions reduce repetitive data entry and make the application more practical for everyday personal finance use.

**Highlights:**
`RecurringTransaction` is deliberately *not* a subclass of `Transaction` — it is a template that stores a frequency, start date, and last-generated date, and is only resolved into concrete `Transaction` objects when `GenerateRecurringCommand` is executed. This separation keeps the template model clean and avoids polluting the main transaction list with non-transactional metadata. The transaction type (income vs expense) is inferred automatically from the category at construction time, reusing `Income.VALID_CATEGORIES`.

---

### New Feature: Transaction Date and Description Fields
**What it does:**
Extended the `Transaction` class with an optional `LocalDate` date field (defaults to today if omitted) and an optional free-text description field. Both fields are parsed from the `d/` and `desc/` tokens in user input.

**Justification:**
Without a date field, all transactions are implicitly "today", making it impossible to backfill historical entries. The description field gives users a way to annotate transactions (e.g. "monthly pay", "dinner with team") for easier recall later.

**Highlights:**
Both fields are optional at the parser level — existing commands continue to work without them. The date field is used as the sort key in `SortCommand` and as a filter criterion in `FilterCommand`, making it a foundational addition that multiple other features depend on.

---

### Enhancements Implemented:
- Added `assert` statements to `Transaction`, `Income`, `Expense`, `UndoRedoManager`, and `RecurringTransaction` constructors and `execute()` methods to catch violated preconditions early.
- Added Javadoc to all authored classes and methods.
- Wrote JUnit tests for `SortCommand`, `UndoRedoManager` (add/delete/edit undo and redo), `RecurringTransaction` (construction, type inference, generation logic), and `Transaction` enhancements (date and description fields).

---

### Code Contributed
[Jason's RepoSense](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=Nishuy52&breakdown=true)

---

### Contributions to the User Guide
I contributed the following sections to the User Guide:
- Sort command
- Undo command
- Redo command
- Add recurring transaction command
- Delete recurring transaction command
- List recurring transactions command
- Generate recurring transactions command

Each section includes the command format, examples, and explanations.

---

### Contributions to the Developer Guide
I contributed the following sections to the Developer Guide:

**Sort Transaction Feature**
- Architecture and flow (Parser → `parseSortCommand()` → `SortCommand` → `getSortedList()`)
- Implementation details: comparator selection switch, non-mutating defensive-copy design, `isMutating()` returning false
- Design considerations: why non-mutating sort preserves undo/redo index correctness, use of Java standard-library comparators
- Alternatives considered: in-place sort with "unsort", caching sorted results, persistent sort order
- Future improvements
- Sort Sequence Diagram (`SortSequenceDiagram.png`)
- Sort Class Diagram (`SortClassDiagram.png`)

**Undo and Redo Feature**
- Architecture and flow: `UndoRedoManager` instantiation, injection into `Parser`, dual-stack lifecycle
- Implementation details: `ActionPair` inner class fields, undo logic (inverse operations per `ActionType`), redo logic (reapply original action), `isMutating()` returning true
- Design considerations: dual-stack delta vs. Memento pattern (O(1) vs. O(n) memory), clearing redo stack on new action, index-based reinsertion correctness, non-persistent history rationale
- Alternatives considered: Memento pattern, per-command `undo()` methods, single list with index pointer
- Future improvements
- Undo Sequence Diagram (`UndoSequenceDiagram.png`)
- Redo Sequence Diagram (`RedoSequenceDiagram.png`)
- Undo/Redo Class Diagram (`UndoRedoClassDiagram.png`)

**Recurring Transactions Feature**
- Full feature section: architecture and flow, model design (`RecurringTransaction`, `Frequency` enum, `RecurringTransactionList`), all four command implementations (`AddRecurringCommand`, `ListRecurringCommand`, `DeleteRecurringCommand`, `GenerateRecurringCommand`)
- `isMutatingRecurring()` contract and separate save flag design
- Storage persistence: `[REC]` pipe-delimited format, atomic temp-file write strategy, `loadRecurring()`/`saveRecurring()` flow
- Auto-generation on startup design
- Design considerations: template separate from `Transaction` hierarchy, watermark-based generation, separate save flag
- Alternatives considered and future improvements

**Transaction Class**
- `Transaction` abstract class overview, field table (`category`, `amount`, `description`, `date`), `protected final` immutability rationale, `getType()` abstract method description

---

### Contributions to Team-Based Tasks
- Set up the About Us page (`jason-AboutUs` branch).
- Reviewed pull requests and provided feedback on code quality and documentation.
- Actively managed the team's GitHub Issues board and assigned issues to relevant team members.
- Fixed checkstyle issues and improved code quality.
- Performed manual testing and text-ui testing.

---

### Review / Mentoring Contributions
- Reviewed teammates' pull requests and suggested improvements to code correctness and documentation.

---

### Tools Used
- Gradle for build automation
- JUnit for unit testing
- Checkstyle for code quality
- GitHub for version control and pull request management

# Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

## Design & implementation

# Design

## Architecture
![Architecture Diagram](diagrams/ArchitectureDiagram.png)
(Overall system architecture diagram and explanation)

## Components
### Parser
### Command
### Ui
### Transaction

The `Transaction` class is the abstract base class shared by all transaction types in the application.
It defines the common fields and accessor methods that `Income` and `Expense` both inherit, and
declares the abstract `getType()` method that each subclass must implement to identify itself at runtime.

The class diagram below shows the full `Transaction` hierarchy.

![Transaction Class Diagram](diagrams/TransactionClassDiagram.png)

**Fields defined in `Transaction`:**

| Field | Type | Description |
|---|---|---|
| `category` | `String` | The category label for the transaction |
| `amount` | `double` | The monetary value (must be positive) |
| `description` | `String` | An optional description string |
| `date` | `LocalDate` | The date of the transaction |

All fields are declared `protected final`, making them accessible to subclasses but immutable after construction.
This immutability is intentional — rather than modifying a transaction in place, the `EditCommand` removes
the old transaction and inserts a newly constructed one at the same index.

**Abstract method:**
- `getType()` — returns a lowercase string identifying the transaction type (e.g. `"income"` or `"expense"`).
  This is used by `SummaryCommand` to distinguish income from expense entries without relying on `instanceof` checks.

## Expense Class

### Overview
The `Expense` class represents an expenditure transaction in the application.
It extends the abstract `Transaction` class alongside `Income`, and is one of the two concrete
transaction types that can be stored in the `TransactionList`.

### Design
`Expense` enforces a fixed set of valid categories defined as a static list:

| Category | Description |
|---|---|
| `food` | Meals and groceries |
| `transport` | Commuting and travel costs |
| `utilities` | Bills such as electricity and water |
| `education` | Tuition, books, and course fees |
| `rent` | Accommodation payments |
| `medical` | Healthcare and pharmacy costs |
| `misc` | Any other expenditure |

Category validity is enforced via an assertion in the constructor, consistent with the defensive
programming approach used throughout the codebase.
Logging is configured at `WARNING` level to reduce noise during normal operation, matching the
convention used by `Income` and `TransactionList`.

### Key Methods
- `getType()` — returns `"expense"`, used to distinguish transaction types at runtime without `instanceof` checks.
- `toString()` — formats the transaction for display
  (e.g. `[Expense] food "lunch" $12.50 (2026-03-20)`). The description is omitted from the output
  if it is an empty string, keeping the display clean for transactions logged without a description.

### Design Considerations
The `Expense` and `Income` classes are intentionally kept symmetric in structure.
Both extend `Transaction` and override only `getType()` and `toString()`, keeping each subclass
minimal and focused.
Defining `VALID_CATEGORIES` as a `public static final` field on `Expense` (rather than in the
`Parser` or a separate validator) keeps the validation rules and category data together in one place.
This also allows `AddCommand` and `EditCommand` to reference 
`Expense.VALID_CATEGORIES` directly when deciding which transaction type to instantiate.

### Alternatives Considered
One alternative was to consolidate `Income` and `Expense` into a single `Transaction` class
with a `type` field (e.g. an enum of `INCOME` or `EXPENSE`). While this reduces the number of
classes, it would require all category validation and formatting logic to branch on the type field,
making the class harder to extend. The subclass approach allows each type to define its own
categories and `toString()` format independently, and adding a new transaction type in future
requires only a new subclass rather than modifying existing ones.

### Future Improvements
- Allow user-defined custom categories beyond the fixed list.
- Add sub-categories (e.g. `food/dining-out` vs `food/groceries`) for better summaries.

### TransactionList / Storage
# Implementation

## List and Delete Transaction Feature

### Overview
The list and delete transaction features allow users to manage their transactions stored in the application.
The `list` command displays all recorded transactions in a numbered format, while the `delete` command removes a transaction based on its index.
These features improve usability by allowing users to review and remove transactions easily.

### Architecture and Flow
When a user enters a command, the input is first handled by the `Ui` component, which reads the input and passes it to the `Parser`.
The `Parser` then interprets the input and creates the appropriate `Command` object (`ListCommand` or `DeleteCommand`).
The command is executed using the `TransactionList` to retrieve or remove transactions, and the result is displayed to the user through the `Ui`.

### Sequence Diagram for List Command
![List Sequence Diagram](diagrams/ListSequenceDiagram.png)

### Listing Transactions
The `list` command is implemented using the `ListCommand` class.
When executed, the command checks whether the transaction list is empty using the `isEmpty()` method.
If the list is not empty, it iterates through the transactions using the `size()` and `get(int i)` methods and displays them in a numbered format.

### Sequence Diagram for Delete Command
![Delete Sequence Diagram](diagrams/DeleteSequenceDiagram.png)

### Deleting Transactions
The `delete` command is implemented using the `DeleteCommand` class.
The `Parser` extracts the index provided by the user and passes it to the `DeleteCommand`.
The `DeleteCommand` then removes the corresponding transaction from `TransactionList` using the `remove(int i)` method and displays a confirmation message.

### Class Diagram
![Class Diagram](diagrams/ListDeleteClassDiagram.png)

### Design Considerations
This feature uses a command-based architecture to ensure separation of concerns.
The `Parser` is responsible only for parsing user input, while each command class is responsible for executing its own logic.
The `TransactionList` class manages the storage of transactions, which improves modularity and maintainability.
Assertions and logging are used in `TransactionList` as part of defensive programming to detect invalid operations and to record important actions such as adding or removing transactions.

### Alternatives Considered
One alternative was to place all command logic inside the `Parser` class using conditional statements.
However, this approach would make the `Parser` class too large and difficult to maintain as more commands are added.
Another alternative was to allow the `Ui` class to directly modify the transaction list, but this would reduce modularity and violate separation of concerns.
The chosen design using separate command classes was preferred because it improves extensibility and maintainability.

### Future Improvements
Possible future improvements include allowing deletion of multiple transactions at once, supporting filtered list views, and adding an undo feature to restore deleted transactions

---

## Find and Summary Transaction Features

### Overview
The `find` and `summary` features allow users to draw meaningful insights from their recorded transactions.
The `find` command locates specific transactions based on keyword matching, allowing users to search for distinct transactions or categories. 
The `summary` command calculates and displays the overall statistics (eg. total expense) for the user to see at a glance.

### Architecture and Flow
Similar to the broader application architecture, these features rely on the interaction between the `Parser`, `Command`, `TransactionList`, and `Ui` components.
1. The `Parser` receives raw input (e.g., `find food` or `summary`) and instantiates either a `FindCommand` containing the target keyword, or a `SummaryCommand`.
2. Upon calling `execute()`, the respective command interacts with the `TransactionList` to either filter for matches or aggregate financial data.
3. The computed results are formatted and printed to the user via the `Ui`.

### Finding Transactions
The `find` command is encapsulated within the `FindCommand` class. 
During execution, the method iterates through the current list, comparing the target keyword against the description of each transaction.

#### Sequence Diagram for Find Command
The following sequence diagram illustrates the precise object interactions when a user searches for a keyword.
![Find Sequence Diagram](diagrams/FindSequenceDiagram.png)

### Summarizing Transactions
The `summary` command is handled by the `SummaryCommand` class. 
It performs computations over the list of transactions. 
It iterates through the entire `TransactionList`, categorising each entry as either an Income or an Expense, and sums the respective totals before calculating the net balance.

#### Sequence Diagram for Summary Command
The following sequence diagram illustrates the interactions when a user wants to see a summary of their transactions.
![Summary Sequence Diagram](diagrams/SummarySequenceDiagram.png)

### Class Diagram
Both commands adhere to the application's Command pattern structure. The diagram below shows how `FindCommand` and `SummaryCommand` inherit from the abstract `Command` class and depend on `TransactionList` and `Ui`.

![Find and Summary Class Diagram](diagrams/FindSummaryClassDiagram.png)

### Design Considerations
* **Case-Insensitive Searching:** For the `find` feature, it was decided that keyword matching should be case-insensitive (e.g., searching "food" returns "Food" and "FOOD"). This greatly enhances user experience, as users do not need to remember the exact capitalization of their previous entries.
* **Stream-Based Indexing:** A major design consideration was how to display matching transactions alongside their original indices from the main list. Instead of iterating through the list and keeping a separate counter, IntStream.range(0, list.size()) was used.

### Alternatives Considered
* **Alternative for Summary:** We considered caching the total income and expense values inside `TransactionList`, updating them dynamically every time an item is added or deleted. While this makes the `summary` command run in O(1) time, it heavily complicates the `add` and `delete` operations and introduces state-synchronization bugs. The O(N) calculation upon execution was chosen for stability and simplicity.

### Future Improvements
* **Time-Bound Summaries:** Upgrading the `summary` command to accept date parameters, allowing users to see summaries for specific months or weeks (e.g., `summary /from 2026-01-01 /to 2026-01-31`).

---

## Sort Transaction Feature

### Overview
The `sort` command allows users to view their transactions ordered by date, amount, or category,
without modifying the underlying list order. It is a read-only operation that creates a temporary
sorted copy purely for display.

### Architecture and Flow
When the user enters a sort command (e.g., `sort by/date`), the input is passed from the `Ui`
to the `Parser`. The `Parser` calls `parseSortCommand()`, which validates the `by/` prefix and
the criteria string. A `SortCommand` is created with the validated criteria and returned to the
main loop. During execution, `SortCommand` constructs an appropriate `Comparator<Transaction>`,
calls `TransactionList.getSortedList(comparator)` to obtain a sorted defensive copy, and displays
the results through the `Ui`.

### Sequence Diagram
The following sequence diagram illustrates the interaction when a user sorts transactions by date.

![Sort Sequence Diagram](diagrams/SortSequenceDiagram.png)

### Implementation Details
- **Parsing:** `Parser.parseSortCommand()` validates the `by/` prefix and checks the criteria
  against the three accepted values: `"date"`, `"amount"`, and `"category"`. Any other value
  throws a `MoneyBagProMaxException`.
- **Comparator selection:** `SortCommand.execute()` uses a `switch` statement to build the
  appropriate comparator:
  - `date` — `Comparator.comparing(Transaction::getDate)` (ascending, earliest first)
  - `amount` — `Comparator.comparingDouble(Transaction::getAmount).reversed()` (descending,
    largest first)
  - `category` — `Comparator.comparing(Transaction::getCategory, String.CASE_INSENSITIVE_ORDER)`
    (alphabetical, case-insensitive)
- **Non-mutating:** `TransactionList.getSortedList()` copies the list into a new `ArrayList`,
  sorts the copy using `Collections.sort()`, and returns it. The original `TransactionList` is
  never modified. `SortCommand` does not override `isMutating()`, so it inherits `false` from
  the base `Command` class — no auto-save is triggered after execution.

### Class Diagram
![Sort Class Diagram](diagrams/SortClassDiagram.png)

### Design Considerations
- **Non-mutating design:** The sort command deliberately returns a sorted copy rather than sorting
  the list in place. This preserves the user's insertion order and avoids corrupting the indices
  stored by `UndoRedoManager`. If sort modified the list order, previously recorded undo/redo
  indices would point to the wrong transactions, breaking the undo/redo feature.
- **Leveraging Java standard library:** Using `Comparator` method references and
  `Comparator.comparing()` avoids hand-written comparison logic, which is verbose and prone to
  sign errors. The standard library comparators are well-tested and handle edge cases (e.g., null
  dates) more robustly.
- **`isMutating()` returns false:** Because the original list is unchanged, no storage save is
  needed after sort. This is an intentional contract with the main loop — sort is a view command,
  not a data command.

### Alternatives Considered
- **In-place sort with an "unsort" command:** Sorting the actual list is simpler to implement but
  destroys the insertion order that `UndoRedoManager` relies on. Providing an "unsort" command to
  restore original order would require persisting the original order separately, significantly
  increasing complexity. The defensive-copy approach was chosen for simplicity and correctness.
- **Caching the sorted result:** Storing the last sort result in `TransactionList` could avoid
  re-sorting on repeated calls. However, any add/delete/edit operation would stale the cache,
  requiring cache-invalidation logic. Since the list is small for a personal finance app and
  sorting is O(n log n), this optimization was deemed unnecessary.
- **Persistent sort order:** An alternative was to make sort permanently reorder the list and
  save to storage. This was rejected because users expect sort to be a display-only operation,
  not a data mutation. Making it persistent would also conflict with undo/redo index semantics.

### Future Improvements
- Support multi-key sorting (e.g., `sort by/category by/date` to sort by category then date
  within each category).
- Add an ascending/descending toggle (e.g., `sort by/amount asc`).
- Display original list indices alongside sorted results so users can reference them for
  subsequent `delete` or `edit` commands.

---

## Undo and Redo Feature

### Overview
The `undo` and `redo` commands allow users to reverse and reapply the last mutating operation
(add, delete, or edit). They provide a safety net against accidental changes. The feature uses
a dual-stack pattern: an undo stack records performed actions and a redo stack records undone
actions, enabling bidirectional navigation of the action history.

### Architecture and Flow
`UndoRedoManager` is instantiated once in `MoneyBagProMax` (the main class) and injected into
the `Parser`. When a mutating command (`AddCommand`, `DeleteCommand`, `EditCommand`) executes,
it calls the appropriate `record*()` method on `UndoRedoManager`, which pushes an `ActionPair`
onto the undo stack and clears the redo stack. When the user types `undo`, the `Parser` creates
an `UndoCommand` that holds a reference to the shared `UndoRedoManager`. During execution,
`UndoCommand` pops the top action from the undo stack, pushes it onto the redo stack, and applies
the inverse operation to `TransactionList`. `redo` works symmetrically.

### Sequence Diagram for Undo Command
The following diagram shows the full interaction when a user undoes a previous action.

![Undo Sequence Diagram](diagrams/UndoSequenceDiagram.png)

### Sequence Diagram for Redo Command
The following diagram shows the full interaction when a user redoes a previously undone action.

![Redo Sequence Diagram](diagrams/RedoSequenceDiagram.png)

### Implementation Details
- **Recording actions:** Each mutating command calls `recordAdd()`, `recordDelete()`, or
  `recordEdit()` on `UndoRedoManager` after modifying the list. Each method creates an
  `ActionPair` capturing the action type, the affected transaction(s), and the list index,
  then clears the redo stack to invalidate any future redo history.
- **ActionPair:** An inner static class of `UndoRedoManager` that stores:
  - `ActionType` enum (`ADD`, `DELETE`, `EDIT`)
  - `transaction` — the transaction that was added/deleted, or the new version after an edit
  - `oldTransaction` — the previous version before an edit (null for ADD and DELETE)
  - `index` — the position in the list at the time the action was performed
- **Undo logic:** `UndoCommand.execute()` calls `getUndoAction()`, which pops from the undo
  stack and pushes onto the redo stack. The command then switches on the action type to perform
  the inverse operation:
  - `ADD` → `list.remove(index)` (removes the added transaction)
  - `DELETE` → `list.insert(index, transaction)` (re-inserts the deleted transaction)
  - `EDIT` → `list.remove(index)` then `list.insert(index, oldTransaction)` (restores
    the pre-edit version)
- **Redo logic:** `RedoCommand.execute()` calls `getRedoAction()`, which pops from the redo
  stack and pushes onto the undo stack. The command then reapplies the original action:
  - `ADD` → `list.insert(index, transaction)`
  - `DELETE` → `list.remove(index)`
  - `EDIT` → `list.remove(index)` then `list.insert(index, transaction)`
- **Mutating flag:** Both `UndoCommand` and `RedoCommand` override `isMutating()` to return
  `true`, which triggers auto-save to storage after execution.

### Class Diagram
![Undo Redo Class Diagram](diagrams/UndoRedoClassDiagram.png)

### Design Considerations
- **Dual-stack delta vs. Memento pattern:** The Memento pattern stores a complete copy of the
  entire `TransactionList` before each mutating action. While straightforward, this uses O(n)
  memory per recorded action, where n is the list size. The dual-stack approach stores only the
  delta — the action type, one or two transaction objects, and an index — using O(1) memory per
  action. For a personal finance app where sessions may involve many operations, the delta approach
  is more memory-efficient.
- **Clearing the redo stack on new action:** When a new mutating action occurs after one or more
  undos, the redo stack is cleared. This follows the standard undo/redo contract used by text
  editors: branching redo history (where redo could replay an action conflicting with newer
  changes) is avoided by discarding the redo stack. The result is a linear, predictable history.
- **Index-based reinsertion:** Storing the exact list index allows transactions to be restored to
  their original position. This is correct because undo/redo is strictly LIFO — the most recent
  action must be undone before earlier ones, which guarantees that stored indices remain valid
  during sequential undo/redo sequences.
- **Non-persistent history:** Undo/redo stacks are in-memory only and cleared on application
  exit. Persisting them would require serializing `ActionPair` objects across sessions, with
  the risk of stale references if the saved data file is modified externally. For a CLI app,
  this complexity is disproportionate to the benefit.

### Alternatives Considered
- **Memento pattern (full state snapshots):** Store a complete copy of `TransactionList` before
  each mutating action and restore the snapshot on undo. Advantage: simpler undo logic (just
  swap the reference). Disadvantage: O(n) memory per action, and deep-copying `Transaction`
  objects for every add/delete/edit is costly. Rejected due to memory overhead.
- **Command pattern with per-command undo methods:** Add an `undo()` method to each command
  class (e.g., `AddCommand.undo()` removes the added transaction). This couples undo logic
  directly to each command class, spreading the responsibility across many files and making
  it harder to add new commands. The chosen design centralises all undo/redo logic in
  `UndoRedoManager` and two command classes.
- **Single list with an index pointer:** Maintain one list of `ActionPair` objects with a
  pointer that moves backward on undo and forward on redo. This is functionally equivalent to
  the dual-stack approach but is less intuitive conceptually — the dual-stack model maps more
  directly to the standard mental model of "undo history" and "redo history" as separate queues.

### Future Improvements
- Set a maximum undo history size (e.g., 50 actions) to bound memory usage in long sessions.
- Persist undo/redo history to a separate file so it survives application restarts.
- Add `undo N` syntax to undo multiple actions in a single command.
- Add a `history` command to list available undo/redo operations so users can see what actions
  are available before committing to an undo.

## Income Class

### Overview
The `Income` class represents an income transaction in the application.
It extends the abstract `Transaction` class, alongside `Expense`, sharing common fields: `category`, `amount`, `description`, and `date`.

### Design
`Income` enforces a fixed set of valid categories defined as a static list:

| Category | Description |
|---|---|
| `salary` | Regular employment income |
| `freelance` | Contract or freelance work |
| `investment` | Returns from investments |
| `business` | Business revenue |
| `gift` | Monetary gifts received |
| `misc` | Any other income |

Category validity is enforced via an assertion in the constructor, consistent with the defensive programming approach used elsewhere in the codebase.
Logging is configured at `WARNING` level to reduce noise during normal operation.

### Key Methods
- `getType()` — returns `"income"`, used to distinguish transaction types polymorphically at runtime.
- `toString()` — formats the transaction for display (e.g. `[Income] salary "June paycheck" $3000.00 (2026-06-01)`).

### Design Considerations
The `Income` and `Expense` classes are intentionally kept symmetric in structure.
Both extend `Transaction` and override `getType()` and `toString()`, making it straightforward to introduce new transaction types in future by simply extending `Transaction` and implementing these methods.
Keeping the valid category list as a `static final` field on the class (rather than in the `Parser` or elsewhere) ensures validation logic stays close to the data it governs.

### Alternatives Considered
One alternative was to represent transaction types using an enum field on a single `Transaction` class rather than separate subclasses.
However, using subclasses allows each type to define its own valid categories and formatting logic independently, which is more extensible as the application grows.

### Future Improvements
- Allow user-defined custom categories beyond the fixed list.
- Add support for recurring income entries (e.g. monthly salary auto-logged).

---

## Persistent Storage Feature

### Overview
The persistent storage feature allows transactions to be saved across sessions so that user data is not lost when the application exits.
The `Storage` class is responsible for reading from and writing to a flat file (`data/transactions.txt`) on disk.
It is invoked on startup to reload saved transactions, and after every mutating command to persist the latest state.

### Architecture and Flow
The `Storage` class operates independently of the command pipeline and is called directly by the main application loop.
On startup, `Storage.load()` reads the data file line by line, parses each transaction record, and populates the `TransactionList`.
After any command that modifies the list (add, delete, etc.), `Storage.save()` serializes the entire `TransactionList` back to disk atomically using a temporary file, replacing the previous file only once the write succeeds.

### Sequence Diagram for Load
![Storage Load Sequence Diagram](diagrams/StorageLoadSequenceDiagram.png)

### Loading Transactions
On startup, `load()` ensures the `data/` directory and `transactions.txt` file exist, creating them if necessary.
It then reads all lines from the file, skipping any that do not begin with the `[TXN]` prefix.
Each valid line is parsed by `parseLine()` into a key-value map of fields (`type`, `category`, `amount`, `description`, `date`).
The appropriate `Transaction` subclass — either `Income` or `Expense` — is instantiated and added to the `TransactionList`.
Malformed lines are skipped with a warning rather than halting the application, so a single corrupt entry does not prevent the rest of the data from loading.

### Sequence Diagram for Save
![Storage Save Sequence Diagram](diagrams/StorageSaveSequenceDiagram.png)

### Saving Transactions
`save()` serializes every transaction in the list into a pipe-delimited string via `serializeLine()`, producing lines of the form:
```
[TXN] | type=income | category=food | amount=12.5 | description=lunch | date=2026-03-25
```
The lines are first written to a temporary file (`transactions.txt.tmp`), which is then atomically moved to replace `transactions.txt`.
This two-step write ensures that a crash or interruption during saving cannot corrupt the existing data file.

### Class Diagram
![Storage Class Diagram](diagrams/StorageClassDiagram.png)

### Design Considerations
The `Storage` class is decoupled from the command classes and interacts only with `TransactionList`, keeping concerns cleanly separated.
Atomic saves via a temporary file were chosen to protect data integrity — a partial write leaves the previous file intact.
The pipe-delimited `[TXN] | key=value` format is human-readable and easy to extend with new fields without breaking backward compatibility, since fields are parsed by name rather than by position.
Assertions are used throughout to enforce preconditions such as non-null inputs and positive amounts, consistent with the defensive programming approach used elsewhere in the codebase.

### Alternatives Considered
One alternative was to store transactions in JSON format, which would provide a more structured and widely recognised data format. However, this would require importing a third-party JSON library, introducing an external dependency for a task that a simple custom parser can handle adequately.
Another alternative was a database such as SQLite, but this was considered overkill for an application that only needs to persist a flat list of transactions with no relational queries.
The plain-text pipe-delimited format was chosen for its simplicity, zero external dependencies, and ease of manual inspection or editing if needed.

### Future Improvements
Possible future improvements include supporting multiple save files or profiles, compressing the data file for large transaction histories, and adding a backup rotation strategy to retain recent snapshots in case of data corruption.

---

## Product scope
### Target user profile

{Describe the target user profile}

### Value proposition

{Describe the value proposition: what problem does it solve?}

## User Stories

|Version| As a ... | I want to ... | So that I can ...|
|--------|----------|---------------|------------------|
|v1.0|new user|see usage instructions|refer to them when I forget how to use the application|
|v2.0|user|find a to-do item by name|locate a to-do without having to go through the entire list|

## Non-Functional Requirements

{Give non-functional requirements}

## Glossary

* *glossary item* - Definition

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}

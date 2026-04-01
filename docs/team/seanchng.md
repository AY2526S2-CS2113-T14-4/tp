# Sean Chng's Project Portfolio Page

## Project: MoneyBagProMax

MoneyBagProMax is a command-line personal finance management application designed to help users track
income and expenses, manage budgets, and gain insights into spending habits through financial statistics.
The user interacts with it using a CLI, and the application is written in Java.

Given below are my contributions to the project.

---

## Summary of Contributions

### New Feature: Income Class
**What it does:**
Represents an income transaction. Extends `Transaction` and defines valid income categories (e.g. salary, allowance, freelance, investment, and misc). The formatted display (e.g. `[Income] salary "monthly pay" $3000.00 (2026-03-20)`) omits the description if it is empty, keeping output clean.

**Justification:**
A dedicated `Income` subclass keeps the category list and formatting logic together in one place. Exposing `VALID_CATEGORIES` as a public static final field allows `AddCommand` and `EditCommand` to reference it directly when deciding which transaction type to instantiate, without coupling those commands to the Parser.

**Highlights:**
Category validity is enforced at construction time, matching how the rest of the codebase handles invalid input. It mirrors the structure of `Expense` so adding a new transaction type in future only requires a new subclass.

---

### New Feature: Storage (Save and Load)
**What it does:**
Handles the persistent saving and loading of all transactions to and from a local data file. Transactions are automatically saved after every command, and the full transaction list is restored from the file on application startup. The component also gracefully handles missing or corrupted files, ensuring the application can always launch cleanly.

**Justification:**
Without persistent storage, all data would be lost every time the application exits. This feature ensures that users never lose their transaction history and can pick up exactly where they left off across sessions.

**Highlights:**
The storage component is designed to be robust — if the data file is missing, it is created fresh; if it is corrupted or contains malformed entries, those entries are skipped with a warning rather than crashing the application. Auto-saving after every command ensures data is never silently lost mid-session.

---

### Enhancements Implemented:
- Added defensive programming via assertions in `Income` constructor and relevant command `execute()` methods.
- Added Javadoc to `Income` and `Storage` classes.
- Wrote JUnit tests for `Income` (valid categories, invalid categories, formatted display) and `Storage` (save/load round-trip, missing file creation, corrupted entry handling).

---

### Code Contributed
[SeanChng's RepoSense](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=SeanChng&breakdown=true)

---

### Contributions to the User Guide
I contributed the following sections to the User Guide:
- Add income command
- Storage and data persistence section

Each section includes the command format, examples, and explanations.

---

### Contributions to the Developer Guide
I contributed the following sections to the Developer Guide:
- Implementation of the Income class, including the category table, key methods, design considerations, and alternatives considered.
- Implementation of the Storage component, including architecture and flow, save/load logic, and error handling strategy.
- Sequence diagrams for the Storage feature illustrating the save and load execution paths.

---

### Contributions to Team-Based Tasks
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
# Benjamin Loh's Project Portfolio Page

## Project: MoneyBagProMax

MoneyBagProMax is a command-line personal finance management application designed to help users track 
income and expenses, manage budgets, and gain insights into spending habits through financial statistics. 
The user interacts with it using a CLI, and the application is written in Java.

Given below are my contributions to the project.

---

## Summary of Contributions

### New Feature Added: Architectural Refactoring and Foundation
**What it does:**
It was a structural overhaul of the early v1.0 codebase. 
I extracted all execution logic into their own dedicated command classes by implementing the Command Pattern.
I also organised the project into cohesive packages (e.g., `command`, `parser`, `transaction` and `ui`).

**Justification:**
This foundation restructure was critical so that our team could establish a scalable codebase.
It also allowed the other teammates to develop their own features concurrently and reducing merge conflicts at the same time.

**Highlights:** 
Transitioned a messy, tightly-coupled codebase into a robust, object-oriented architecture that forms the backbone of the application.

### New Feature Added: Filtering of Transactions
**What it does:**
This filter function allows users to isolate transactions not just by category, but also by specific date ranges.

**Justification:**
This feature provides users with quick insights into their spending habits, allowing them to view a targeted list in one go.

**Highlights:** 
Vastly improves the data retrieval capabilities of the application, allowing for targeted financial analysis.

### New Feature Added: Finding of Transactions
**What it does:**
This find function allows users to search for specific transactions through the use of keywords or by date.

**Justification:**
This feature allowed users to retrieve specific data points that they might want to bring to remembrance.

### New Feature Added: Summary of Transactions
**What it does:**
The Summary function allows users to generate specific summaries of their financial state.

**Justification:**
This feature allows users to get an overarching view of their financial health based on their input history.

**Highlights:**
Users are able to get an overview summary of their transactions in one go, or even by categories of expenses or income.

---

### Enhancements Implemented:
- Upgraded the `Ui` component by introducing ANSI colour codes to the terminal output.
- Improved readability by optimising the whitespace between consecutive command executions.
- Extensively fortified the application's reliability. Implemented comprehensive JUnit tests covering all my individual commands and the parser's logic. 
- Integrated widespread use of `assert` statements and the Java `Logger` to catch runtime anomalies.

---

### Code Contributed
[benthejarofmint's RepoSense](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=benthejarofmint&breakdown=true)

---
### Contributions to the User Guide
I contributed the following sections to the User Guide:
- Filter command
- Find command
- Summary command
- The structural boilerplate
- Table of Contents
- Quick Start Guide

Each section includes the command format, examples, and explanations.

---

### Contributions to the Developer Guide
I contributed the following sections to the Developer Guide:
- Implementation of the Find feature
- Implementation of the Summary feature
- Sequence diagrams of the Find feature
- Sequence diagrams of the Summary feature
- Class Diagram of Find and Summary Features working with other classes

---

### Contributions to Team-Based Tasks
- Reviewed pull requests and provided feedback on code quality and documentation.
- Actively managed the team's GitHub Issues board and assigning them to relevant team members
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

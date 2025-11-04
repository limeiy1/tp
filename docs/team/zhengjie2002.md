## Project: SGSafe

SGSafe is a case-management application that enables front-desk officers to create, record, edit, close, delete, and
list cases through a simple, workflow-focused interface. The user can interact with the system using a command line
interface, and it prioritises those who have good typing skills to complete their work fast.

Given below are my contributions to this project:

- **Code
  contribution**
  [Link to RepoSense](https://nus-cs2113-ay2526s1.github.io/tp-dashboard/?search=zhengjie2002&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-09-19T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=)
- **New Feature**: Implemented the `add` feature that allows users to add cases into the system.\
  What it does: Allows the user to add a new case by specifying details such as, description, information, the police
  officer in charge and the victims.\
  Justification: This feature improves the product significantly because a user cannot use the product without being
  able to add cases into the system first.\
  Highlight: Implemented a reusable parser that splits CLI input using flag syntax (e.g. `--FLAGNAME value`), validates
  that flags are used correctly, checks that flags are within the allowed set, and enforces required/optional flag
  rules. The parser and validator provides utility methods that were reused across the team for extracting flagged
  arguments and validating values
- **New Feature**: Implement the `settings` feature that allows users to customize their experience with the
  application.
  What it does: Allows the user to set the date time input format, date time output format and timestamp output format.
  Justification: This feature improves user satisfaction by allowing them to tailor the application to their liking,
  enhancing usability and engagement.
  Highlight: Designed a settings command to parse the input and perform the settings.
- **New Feature**: Implemented the `find` feature that allows users to search for cases based on keywords.
  What it does: Enables users to search for cases by specifying keywords, returning a list of matching cases. It also
  support partial
  matches and is case-insensitive. In addition, user can use `--status` flag to filter the search results based on case
  status.
  Justification: This feature enhances user efficiency by allowing quick retrieval of relevant cases, improving
  productivity and user experience.
  Highlight: Developed a flexible search algorithm that supports partial matches and is case-insensitive, ensuring
  users can find cases even with incomplete or varied input.

- **Enhancement**:
    - Refactored the V1 codebase to improve code readability and maintainability.
        - Created a branch on the team repository.
        - Implemented a validator class to improve SRP compliance and updated code to use the execute method; teammates
          used the branch and helped tidy up before merging to main.
    - Added metadata for `case` class so that each case object now tracks its creation and last modified timestamps.
        - Updated the `case` class to include `createdAt` and `updatedAt` attributes, initialized during case
          creation.
        - Modified relevant commands (add, edit) to update the `updatedAt` timestamp whenever a case is modified.
    - Validator class for command inputs to ensure data integrity and prevent erroneous data from being processed.
        - Developed the validator class that checks for required flags, accurate flags, correct data formats, and valid
          value ranges.
        - Integrated the validator into parser flow to validate user inputs before executing.

- **Documentation**:
    - User Guide:
        - Created the main formatting, including the writeup and the table of contents for the User Guide (UG).
        - Added instructions and examples commands for V1 related functions in the User Guide to help users understand
          how to use the feature effectively.
        - Wrote the FAQ section to address common user queries and enhance user experience.
        - Wrote sections for add, find and setting command
    - Developer Guide:
        - Add boilerplate and table of content for the structure of DG.
        - Add user stories for all features.
        - Wrote section on glossary, instruction for manual testing
        - Added main template for the structure of the DG.
        - Wrote the section on `Command Component`, `Commons` and implementation for `Addition of Cases`. Including all
          the diagrams within these 3 sections

- **Contribution to Team Based Task**:
    - Develop Bug Report Issue Template: Created a standardized bug report issue template for the team repository to
      ensure consistent reporting of bugs by team members and that it captures all required
      info. [Link to Issue Template](https://github.com/AY2526S1-CS2113-W13-3/tp/blob/master/.github/ISSUE_TEMPLATE/bug_report.md)
    - GitHub Workflow: Actively contribute to creating, closing and assigning issues on GitHub to help manage the
      team's workflow and ensure tasks are tracked effectively (such as for PE-D).

- **Reviewing/Mentoring Contribution**
    - PR Reviewed: Reviewed and provided feedback on teammates' pull requests to ensure code
      quality.[Example #1](https://github.com/AY2526S1-CS2113-W13-3/tp/pull/11) [Example #2](https://github.com/AY2526S1-CS2113-W13-3/tp/pull/130)
      [Example #3](https://github.com/AY2526S1-CS2113-W13-3/tp/pull/9)
    - Reported Bugs: Identified and reported bugs of the TP to help improve the overall stability of the
      application. [Example Bug Report](https://github.com/AY2526S1-CS2113-W13-3/tp/issues/104)
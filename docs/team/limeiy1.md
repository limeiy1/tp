## Project: SGSafe

SGSafe is a CLI application designed for police officers to efficiently manage and track different types of cases, 
such as thefts, traffic accidents and assaults. It provides features to add, edit, close, and reopen cases, ensuring 
that officers can maintain accurate, up-to-date records.

Given below are my contributions to the project:
- **Code contribution**:
[link to reposense](https://nus-cs2113-ay2526s1.github.io/tp-dashboard/?search=limeiy1&breakdown=true)

- **New Features**: 
  - `close` and `open` feature
    - What it does: Allows the user to close the case after it has been solved or attended to. 
    The user can also reopen a closed case by using the `open` feature.
    - Justification: The `close` feature allows better management of cases as the user will be able to 
    keep track of cases that have attended to and focus on the ones that have not been attended to.
    The `open` feature ensures that cases which have been closed by mistake can be reopened.
    - Highlights: error handling prevents users from closing an already closed case or opening an already active one.
  - Case categories: Created different case types and categories to allow user to categorise the cases for better organisation.
    - Example categories include: speeding, theft and murder
    - What it does: Allows the user to categorise cases using the `--category` tag. Every category also has specific 
    attributes such as `exceeded-speed` for the speeding category and `weapon` for the murder category.
    - Justification: This feature allows better organisation of cases into categories. The specific attributes also allow 
    more specific case information relevant to the category to be added.
    - Highlights: This feature makes use of inheritance of classes.

- **Project Management**:
  - Managed releases `v1.0` and `v2.0` on GitHub

- **Enhancements to existing features**:
  - Updated the `add` feature to support adding of categories using the `--category` tag.
  - Added more attributes to the categories and updated the `edit` command to support the editing of the additional attributes.
  - Implemented more robust exception handling and accurate error messages for `edit` command. [#272](https://github.com/AY2526S1-CS2113-W13-3/tp/pull/272)
  - Updated `edit` command to accept decimal values for tags `financial-value` and `monetary-damage` using Double data type. [#283](https://github.com/AY2526S1-CS2113-W13-3/tp/pull/283)

- **Documentation**:
    - User Guide:
        - Added documentation for the features `close` and `open`
        - Added information on case categories, including a table of valid case categories and their corresponding edit tags.
    - Developer Guide:
        - Added class diagram of casefiles
        - Added information on the structure of casefiles
        - Added implementation details for `close` and `open` command

- **Community**:
  - Examples of PRs reviewed: [#77](https://github.com/AY2526S1-CS2113-W13-3/tp/pull/77), [#269](https://github.com/AY2526S1-CS2113-W13-3/tp/pull/269), [#279](https://github.com/AY2526S1-CS2113-W13-3/tp/pull/279)
  - Modified code in PR based on feedback: [#138](https://github.com/AY2526S1-CS2113-W13-3/tp/pull/138)
  - Identified and reported bugs: [#183](https://github.com/AY2526S1-CS2113-W13-3/tp/issues/183), [#284](https://github.com/AY2526S1-CS2113-W13-3/tp/issues/284)

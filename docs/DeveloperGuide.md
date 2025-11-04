# Developer Guide

## Table of Contents

1. [Acknowledgements](#acknowledgements)
2. [Setting up, Getting Started](#setting-up-getting-started)
3. [Design](#design)
    - [Architecture](#architecture)
    - [UI Component](#ui-component)
    - [CaseFile Component](#casefile-component)
    - [Command Component](#command-component)
    - [Storage Component](#storage-component)
    - [Commons](#Commons)
4. [Implementation](#implementation)
5. [Appendix A: Product Scope](#appendix-a-product-scope)
    - [Target user profile](#target-user-profile)
    - [Value proposition](#value-proposition)
6. [Appendix B: User Stories](#appendix-b-user-stories)
7. [Appendix C: Non-Functional Requirements](#appendix-c-non-functional-requirements)
8. [Appendix D: Glossary](#appendix-d-glossary)
9. [Appendix E: Instructions for Manual Testing](#appendix-e-instructions-for-manual-testing)

---

## Acknowledgements

- The feature for setting the date and time is designed with inspiration from the
  [article](https://www.baeldung.com/java-datetimeformatter), although all implementation code was entirely done on our
  own.

---

## Setting up, Getting Started

Refer to the section [Appendix E: Instructions for Manual Testing](#appendix-e-instructions-for-manual-testing) to set
up your development environment and get started with the project.

## Design

### Architecture

![Architecture Diagram](images/ArchitectureDiagramOverall.png)

The Architecture Diagram given above explains the high-level design of the App.

Given below is a quick overview of the main components and how they interact with each other.

#### Overall logic

The main code flow is as follows:

1. Print welcome message using `Display`
2. Initialize CaseManager with saved info from `data.txt` if it exists through `Storage`
3. Read user input from System.in
4. Parse user input using `Parser` and `Validator` to obtain a `Command` object that contains the information necessary
   to execute the specific command
5. Execute user command
6. Save the case info to `data.txt` using `Storage`
7. Until the command is to exit the program, repeat steps 3 to 6
8. Print exit message using `Display`

#### Overall sequence diagram

This is the overall sequence diagram, which will be elaborated more on in detail later.
It describes the rough workflow of our app.
Likewise, the abstract class `Command` mentioned here is meant to be a specific command, depending on what the user
input is.

![Overall Sequence Diagram](images/SequenceDiagramGenericCombined.png)

---

### UI Component

The UI component is responsible for all interactions with the user. It reads user input from the console and displays
messages back to the user.

#### Key Classes

**Display**: Handles all user-facing output with formatted messages, including welcome/goodbye messages and command
results.

**Validator**: Provides utility methods to validate flags, check required fields, and verify case ID format.

**Parser**: The parser class serves to determine the type of command object to create based on user input. The parser
class extracts the keyword to identify the command type, then calls methods from `Validator` to check if the required
arguments are present in the input.

Here are some examples of command keywords and their required arguments:

| keyword | Command Type  | Required Arguments                  |
|---------|---------------|-------------------------------------|
| add     | AddCommand    | `title`, `category`, `date`, `info` |
| read    | ReadCommand   |                                     |
| delete  | DeleteCommand | `case id`                           |

#### Interaction Flow

1. `SGSafe.mainLoop()` reads user input from the console
2. `Parser.parseInput()` extracts the command keyword from the input, then calls `Parser.parseXCommand()`, where X is
   the appropriate command type
3. `parseXCommand()` calls methods in `Validator` to check input validity, then creates the appropriate `XCommand` object
   to perform the action
4. Any exceptions caught or error messages are shown through `Display.printMessage()`
5. Results are also displayed via `Display.printMessage()`

##### Example Interaction Flow

![SequenceDiagramUserInputListCommand.png](images/SequenceDiagramUserInputListCommand.png)

The sequence diagram above illustrates how user input is processed to create and execute a `ListCommand`.

---

### CaseFile Component

The CaseFile component is responsible for how case records are stored, organized, and managed in SGSafe.
It acts as the main part of the system’s data model, defining what information each case contains and how cases are
handled.
The component’s main functions are provided through the CaseManager class, which other parts of the
system (like Command and Storage) use to access or update case data.

The API of this component is primarily specified in `CaseManager`.

#### Structure of the CaseFile Component

The CaseFile component consists of two main parts: `Case` and `CaseManager`.
Together, they represent the domain model for managing case records within the system.

The `Case` class defines the structure and behavior of individual cases,
encapsulating shared attributes such as case ID, case title, case info etc.
Specialised subclasses (e.g., `FinancialCase`, `TrafficCase`, `ViolentCase`)
extend `Case` to implement category specific attributes and methods.

The `CaseManager` class is responsible for managing `Case` objects.
It provides high-level operations such as adding, updating, deleting, and retrieving cases.

Additionally, `Case` interacts with the `CaseFormatter` class
to produce formatted representations of case data for display.

> ℹ️ Note: Only attributes are shown in the diagram, methods are omitted.

![CaseFile Diagram](images/ClassDiagramCasefile.png)

#### Responsibilities

The CaseFile component:

- **Defines the abstract base class `Case`**, which encapsulates common attributes and behaviors for all case types
- **Provides extensibility** by allowing specialized case types to inherit from `Case`
- **Manages collections of cases** through `CaseManager`, which creates, retrieves, updates, and deletes cases
- **Coordinates persistence** by interacting with the `Storage` component to save and load case data
- **Supports command execution flows**, since `Command` objects often delegate work to `CaseManager` and its managed
  `Case` instances

#### Key Classes

**Case**:
An abstract class that defines the blueprint for all case types.
It includes identifiers, metadata (e.g. time it is created at, time it is updated at, title, etc.), and abstract methods
that must be implemented by subclasses (e.g. domain-specific case types).
> ℹ️ Note: Whenever a case object is instantiated, a new (and unique) case ID is automatically generated for it. 
> When a case is deleted, the object is not destroyed. Instead, soft deletion is performed by marking the case as deleted. 
> This also means that case ID will never be reused within the same instance of this application.

**CaseManager**:
A concrete class responsible for managing Case objects.
It provides operations such as adding, updating, deleting, and retrieving cases.
It also handles validation and persistence by coordinating with Storage.

#### Interaction Flow

1. A `Command.execute()` call triggers an operation on `CaseManager`
2. `CaseManager` locates or creates the appropriate `Case` object
3. The `Case` object performs domain‑specific logic or state changes
4. Results are returned to the Command, which then passes them to the UI for display

---

### Command Component

The command component is responsible for handling user commands. Based on the interpreted user input by the parser, it
executes the corresponding
actions on the CaseFile component, and prints the result to the UI component.
The API of this component is primarily specified in `Command`.

#### Structure

When a user inputs a command, the UI component uses the `Parser` to create an instance of the appropriate `Command`
subclass.

Each different command is represented by a subclass of the abstract `Command` class, encapsulating the logic for that
specific command. In addition, each command class may have its own attributes to store parameters needed for execution.

The following diagram illustrates the structure of the command component that interacts and edit the cases
and some of the noteworthy methods in the commands. 

![CommandClassDiagram](images/CommandClassDiagram.png)

The following diagram illustrates the structure of the command component that does not edit the cases but plays an important
supporting role in the application.

![SupportingCommandClassDiagram](images/SupportingCommandClassDiagram.png)

> ℹ️ Note: Only non-trivial fields and methods are shown in the above diagram.\


#### Key Classes

- **Command**: An abstract class that defines the structure for all command classes. It includes an abstract `execute()`
  method that must be implemented by all subclasses.
- **ENUM: SettingType**: An enumeration that defines different setting types that can be modified by the setting
  commands.
- **ENUM: CaseListingMode**: An enumeration that defines different modes for listing cases, such as by date or by
  status.

#### Responsibilities

The command component is the most important part of the application as it contains the logic to handle user commands.
The responsibilities of the command component include:

- Each command class implements the `execute()` method, which contains the logic to perform the command's action.
  Additional methods may be included as needed to support the command's functionality.
- Some commands may validate their parameters before execution to ensure correctness. However, the main input validation
  would have already been performed by the UI component during parsing.
- The command component interacts with the CaseFile component to manipulate case data as required by the command, and it
  uses the UI component to display results or error messages to the user.
- The command component catches any errors with command execution and throws appropriate exceptions to be handled by the
  UI component. (example of such errors include trying to edit a non-existent case)

#### Interaction Flow

The following activity diagram illustrates a sample activity flow from the creation of a command to its execution and
result display.

![CommandExecutionSequenceDiagram](images/SequenceDiagramGenericCommandExecution.png)

This diagram is an abstract of the [Overall Sequence Diagram](#overall-sequence-diagram) shown earlier, focusing on the
interactions involving the command component.

> ℹ️ Note: The diagram is a generalisation for all commands. Specific commands may have additional interactions or steps
> based on their unique requirements.\
> ℹ️ UML Info: Due to the limitation of plantUML, the lifeline for Command does not end with an X, even though it is
> destroyed after execution.

---

### Storage Component

The Storage component handles the saving and loading from a human editable text file for data persistence

The API of this component is primarily specified in `Storage`.

#### Responsibilities:

The Storage component:

- Handles the writing to and reading from the save file
- Adds saved case files to `CaseManager` on load

#### Interaction flow:

Loading from a save file:

- When the program starts, `loadCaseManager()` from the storage class is called to initialise the `CaseManager`
  from `SGSafe.main()`

![SequenceDiagramFileLoad.png](images/SequenceDiagramFileLoad.png)

Saving to the save file:

- Every time a command completes, `saveToFile()` is run in the `SGSafe.handleUserCommand()` method.
  This only occurs if the command is successfully run.

![SequenceDiagramFileSave.png](images/SequenceDiagramFileSave.png)

---

### Commons

#### Exceptions

SGSafe uses a structured exception hierarchy to handle errors gracefully and provide meaningful feedback to users.
The `InvalidCommandException` is the base exception class for all exceptions. This class extends Java's Exception class

The structure of the abstract exception class is as follows:

![ExceptionClassDiagram](images/ExceptionHierarchy.png)

The diagram above does not show all exception classes, but it illustrates the general structure of the exception
hierarchy and shows key exception classes with unique fields.

Here are some key points about the exception class structure:

- Each exception class has a private field consisting an array of strings `errorMessages`. This array is meant to contain
  the messages to be displayed to the user when this exception is thrown.
- Each exception class has a constructor that takes a variable-length argument list that accepts zero or more String
  values. This allows callers to pass multiple error messages as separate arguments without wrapping them in an array.
- Some exception class (inherited from the abstract class) can have additional fields and methods as needed to provide
  more context about the error.
- In the main business logic of the application, when the exception is thrown, we can use the getter `getErrorMessage()`
  method to retrieve the array of string and display them.
- Since `InvalidCommandException` inherits from Java's Exception class, we can treat this as a standard checked
  exception and catch it using try-catch blocks. It can also be implicitly upcasted to Exception type.



---

## Implementation
This section describes some noteworthy implementation details of SGSafe.

### Addition of Cases

The add command allows users to create new cases by providing required details such as title, category, date, and info.
This implementation showcases the parsing and validation processes involved in command execution, ensuring data
integrity before adding a case to the system.

**Introduction**\
The add command follows a specific structure:
`add --category CATEGORY --title TITLE --date DATE --info INFO [--victim VICTIM] [--officer OFFICER]`
This is the structure that we implement in our code. As you can see, the command requires some mandatory flags and
allows for optional ones. The parsing and validation methods need to ensure that this is checked.

When a user inputs an add command, the Parser class handles the initial processing:
1. The `parseInput()` method extracts the command keyword ("add") and delegates to `parseAddCommand()`.
2. `parseAddCommand()` uses Validator to check for if input is empty. If so it will throw an error.
3. Next, it will perform `extractFlagValues(String input)`, where the flags will be extracted into its flag and value (
   stored in a map).
4. `extractFlagValues(String input)` will check for incorrect usage of flags and throw errors accordingly. Some incorrect usage include:
    - Missing values for flags (e.g., `--title` without a title)
    - Duplicate flags
    - Input length exceeding maximum allowed length
5. Next, `parseAddCommand()` method will make use of Validator to check that all required flags (--title, --category, --date,
   --info) are present. It will also check for any invalid flags that are not recognized by the system.
6. For the add command, the date format must match the pattern set in the Settings. We will use DateFormatter class's
   `parseDate(...)` method to parse and validate the date format.
7. If validation passes, it creates an AddCommand object with the parsed arguments.

The sequence diagram below illustrates the parsing and validation process for the add command:
![Sequence Diagram of Parsing Add Command](images/SequenceDiagramAddCommandParse.png)

Once parsed and validated, the AddCommand.execute() method:
1. Generates a unique case ID using `generateHexId()`.
2. Creates a new Case object (or subclass based on category) with the provided details. Upon creation, the Case
   constructor:
    - Update `createdAt` with the current timestamp.
    - Update `updatedAt` with the current timestamp.
3. Calls `CaseManager.addCase()` to store the case.
4. Prints a confirmation message to the user. This requires calling `newCase.getDisplayLine()` to get a formatted string of the case
   details.

The sequence diagram below illustrates the execution process for the add command:
![Sequence Diagram of Executing Add Command](images/SequenceDiagramAddCommandExecute.png)

The handleUserCommand() method in the main application loop will then invoke the `Storage` object to persist the new case.



---

## Appendix A: Product scope

### Target user profile

- Police officer who needs to manage a significant number of cases
- Prefers desktop apps over other types of apps
- Can type fast
- Prefers typing to using the mouse
- Comfortable using CLI apps

### Value proposition

- Manage and track cases faster than a typical mouse/GUI driven app
- Lightweight and fast to launch
- Simple to use and learn yet powerful enough to get basic task done well

---

## Appendix B: User Stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priorities | Version | As a ...                  | I want to ...                                        | So that I can ...                                          |
|------------|---------|---------------------------|------------------------------------------------------|------------------------------------------------------------|
| ***        | v1.0    | front-desk officer        | create case                                          | record cases into the system                               |
| ***        | v1.0    | front-desk officer        | edit case details                                    | edit cases that are typed wrongly                          |
| ***        | v1.0    | front-desk officer        | mark case as closed                                  | close cases that have been attended to                     |
| ***        | v1.0    | front-desk officer        | delete case                                          | delete duplicates                                          |
| ***        | v1.0    | front-desk officer        | list all cases                                       | see all the cases that are currently being worked on       |
| **         | v2.0    | police officer            | reopen case                                          | reopen closed cases when new leads arise                   |
| **         | v2.0    | analytical police officer | see all the cases in detail                          | view key information of the cases to get a quick summary   |
| **         | v2.0    | police chief              | read all the details of one case                     | understand the entire case information                     |
| **         | v2.0    | police officer            | control how I type/view dates in the system          | have it personalized to my preference                      |
| **         | v2.0    | organised police officer  | set category tags for each of the cases              | sort cases with details relevant to each case              |
| **         | v2.0    | police officer            | add additional information about a case              | record information relevant to that category for that case |
| **         | v2.0    | police officer            | find relevant cases based on keywords in their title | quickly locate the case I want                             |
| *          | v2.0    | new police officer        | view a user guide on the command line                | get familiarized with how to use the application           |
| *          | v2.0    | police officer            | view when a case is created/edited                   | trace back to when changes were made                       |

---

## Appendix C: Non-Functional Requirements

- Should work on any mainstream OS as long as it has Java 17 installed.
- Should be able to hold up to 1000 cases without a noticeable sluggishness in performance for typical usage.
- A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be
  able to accomplish most of the tasks faster using commands than using the mouse.
- Application should save data upon successful completion to minimize risk of unexpected data loss.

---

## Appendix D: Glossary

* *Mainstream OS* — Windows, Linux, Unix, MacOS
* *Police officer* — Any user of SGSafe who is involved in managing cases, including front-desk officers, field
  officers,
  and police chiefs.
* *Case* — A record of an incident or investigation that needs to be tracked and managed within SGSafe.
* *Case ID* — A unique identifier assigned to each case for easy reference and management.
* *Verbose mode* — A display mode that provides detailed information about each case when listing cases.
* *Data persistence* — The ability of the application to save case data to a file so that it is retained across
  sessions.
* *Front-desk officer* — A police officer who is responsible for initially recording and managing cases.

---

## Appendix E: Instructions for manual testing

Given below are the instructions for manual testing of SGSafe. Please note that NOT all commands have been listed.

### Launch and Startup

1. Ensure you have Java 17 installed on your system.
2. Download the SGSafe JAR file from the [releases page](https://github.com/AY2526S1-CS2113-W13-3/tp/releases). Please place the file in an empty folder.
3. Open a terminal or command prompt.
4. Navigate to the directory where the JAR file is located.
5. Run the application using the command:
   ```java -jar SGSafe.jar```
6. Verify that the welcome message is displayed.

### Add Cases

1. Test case: Add a new case with all required fields.
    - Input: `add --category murder --title Murder --info At Yishun road --date 05/02/2025`
    - Expected Output: Confirmation message with case ID.

2. Test case: Add a new case with some required fields missing.
    - Input: `add --title Murder --info At Yishun road --date 05/02/2025`
    - Expected Output: An error stating that required fields are missing.

3. Test case: Add a new case with incorrect use of flags.
    - Input: `add --category --title Murder --info At Yishun road --date 05/02/2025`
    - Expected Output: An error stating that there is an incorrect use of flags.

### List Cases

1. Test case: List cases in normal mode
    - Input: `list`
    - Expected Output: All cases being listed out, but there is only information about status, category, ID, date,
      title.

2. Test case: List cases with status filter
    - Input: `list --status closed`
    - Expected Output: All the cases that have been closed will be listed out.

3. Test case: List cases with verbose mode
    - Input: `list --mode verbose`
    - Expected Output: More information will be shown per case.

### Closing Cases

1. Test case: Closing a case
    - Input: `close 000001` (use a valid case ID from your list)
    - Expected: Case marked as closed with confirmation message.

2. Test case: Closing a non-existent case
    - Input: `close 999999` (use an invalid case ID from your list)
    - Expected: Error message indicating case ID does not exist.

### Opening Cases

1. Test case: Opening a closed case
    - Input: `open 000001` (use a valid closed case ID)
    - Expected: Case marked as open with confirmation message.

2. Test case: Opening a non-existent case
    - Input: `open 999999` (use an invalid case ID from your list)
    - Expected: Error message indicating case ID does not exist.

### Editing Cases

1. Test case: Editing a case with valid fields
    - Input: `edit 000001 --title Updated title --victim Jane Doe` (use a valid closed case ID)
    - Expected: Case updated successfully with new values.

2. Test case: Editing a case without providing any fields
    - Input: `edit 000001` (use a valid closed case ID)
    - Expected: Error message indicating a list of editable fields.

### Deleting Cases

1. Test case: Deleting an existing case
    - Input: `delete 000001` (use a valid case ID)
    - Expected: Case deleted successfully.

### Settings

1. Test case: Changing date input format
    - Input: `setting --type dateinput --value dd-MM-yyyy`
    - Expected: Date input format updated successfully.

2. Test case: Changing date output format
    - Input: `setting --type dateoutput --value yyyy/MM/dd`
    - Expected: Date output format updated successfully.

### File Storage

1. Test case: Data persistence across sessions
    - Input: Add a case, exit the application, restart it, then list cases.
    - Expected: Previously added case is still present after restart.

The above test cases are not exhaustive and meant to cover the main functionalities of SGSafe.
Please see the [user guide](https://ay2526s1-cs2113-w13-3.github.io/tp/UserGuide.html) for more details on how to use SGSafe and explore additional commands and features.
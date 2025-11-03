# SGSafe User Guide

SGSafe is a *Command Line Interface (CLI) based case management system* that is designed specifically for law
enforcement agencies in Singapore to manage, track and process cases. Built with the diverse needs of police personnel
in mind, this application allows frontline officers to efficiently manage and process cases from creation to closure. By
providing an easy-to-use yet efficient interface, SGSafe transforms traditional case management processes into an
organised digital workflow that enhances operational efficiency for the public sector.

## Table of Contents

- [Quick Start](#quick-start)
- [Features](#features)
    - [Adding a case: `add`](#adding-a-case-add)
    - [Listing cases: `list`](#listing-cases-list)
    - [Closing a case: `close`](#closing-a-case-close)
    - [Opening a case: `open`](#opening-a-case-open)
    - [Editing a case: `edit`](#editing-a-case-edit)
    - [Deleting a case: `delete`](#deleting-a-case-delete)
    - [Settings: `setting`](#settings-setting)
    - [Reading a case: `read`](#reading-a-case-read)
    - [Finding for cases: `find`](#finding-for-cases-find)
    - [Viewing the help menu: `help`](#viewing-the-help-menu-help)
    - [Exiting the program: `bye`](#exiting-the-program-bye)
    - [File storage](#file-storage)
    - [Case categories](#case-categories)
- [FAQ](#faq)
- [Command Summary](#command-summary)
- [Coming Soon](#coming-soon)

## Quick Start

1. Ensure that you have Java 17 installed on your computer.
2. Download the latest version of `SGSafe` from
   the [releases page](https://github.com/AY2526S1-CS2113-W13-3/tp/releases).
3. Copy the .jar file to the folder you want to use as the home folder for SGSafe.
4. Open a command terminal, navigate to the folder containing the JAR file, and run `java -jar SGSafe.jar` to start the
   application. (Please note the filename may differ depending on the version you downloaded.)
5. Type commands in the command line and press Enter to execute them. Refer to the [Command Summary](#command-summary)
   section below for a quick overview of available commands.

---
## Features

> **Notes about the command format:**
>
> * Words in `UPPER_CASE` are the parameters to be supplied by the user.\
    >   e.g., in `add --title TITLE`, `TITLE` is a parameter which can be used as `add --title Murder at Yishun`.
>
> * Parameters with command line flags can be in any order.\
    >   e.g., if the command specifies `--title TITLE --date DATE`, `--date DATE --title TITLE` is also acceptable.
>
> * Parameters that are not preceded by command line flags must appear in the order specified.\
    >   e.g., if the command specifies `close INDEX` INDEX must come after close.
>
> * Optional parameters are enclosed in square brackets.\
    >   e.g., `--title TITLE [--victim VICTIM]` means the victim parameter is optional.
>
> * The escape character for `--` is `\--`. 
> For example: `add --category murder --title hello\--world --info hello --date 05/02/2022` will result in the title of the case being `hello--world`
> * You cannot use the character `|` in your input as it is used in the save file format.
---

### Adding a case: `add`

Adds a new case to the case management system.

**Format:** `add --category CATEGORY --title TITLE --date DATE --info INFO [--victim VICTIM] [--officer OFFICER]`

* `CATEGORY`: The category of the case. This must be one of those specified in [Case Categories](#case-categories)
* `TITLE`: The title or summary of the case.
* `DATE`: The date the case occurred. 
* `INFO`: Additional information or notes about the case.
* `VICTIM`: (Optional) The name(s) of the victim involved.
* `OFFICER`: (Optional) The name(s) of the officer assigned.

> ℹ️ Note: Title, Info, Victim and Officer are treated as strings. Arguments provided to flags will be saved verbatim. \
> ℹ️ Note: Date is stored as a Java LocalDate. The default input format is `dd/MM/yyyy`. You may wish to change it using
> the [Settings](#settings-setting) command below.\
> ⚠️ Warning: A maximum of 5000 characters is allowed for all the fields.\
> ⚠️ Warning: Only the above flags are allowed. Additional flags related to specific categories cannot be set during case creation.
> They can only be set using the [`edit`](#editing-a-case-edit) command after the case has been created. This is an intentional design
> choice to simplify the case creation process.

**Examples:**

* `add --category theft --title Theft case --date 15/10/2024 --info Stolen wallet --victim John Doe --officer Officer Smith`

* `add --category burglary --info Burglary at 123 Main St --date 20/02/2024 --title Burglary case`

---

### Listing Cases: `list`

Displays all cases in the system, with optional filters and formatting modes.

#### **Format:** `list --status <open|closed|all> --mode <summary|verbose>`

#### Flags

- `--status` (optional): Filters cases by their status.
    - `open`: Show only open cases.
    - `closed`: Show only closed cases.
    - `all`: Show all cases (default).
- `--mode` (optional): Controls the level of detail in the output.
    - `summary`: One-line display per case.
    - `verbose`: Multi-line display with labeled fields.

#### Summary Mode Output

Each case is shown in a single line with:

- Status: `[Open]` or `[Closed]`
- Category (e.g., `Theft`, `Scam`)
- Case ID (6-character hexadecimal)
- Date
- Title

Example:

```
You currently have 3 cases in total
---
Note: Only very basic case details are shown here.
For more in depth information about the case (e.g. Info, Victim, Officer)
run: list --mode verbose
---
STATUS   CATEGORY         ID     DATE       TITLE
[Open]   Theft            0001a3 14/10/2025 Robbery
[Closed] Scam             0001a4 15/10/2025 Fraud
[Closed] Traffic accident 0001a5 15/10/2025 Fraud
```

#### Verbose Mode Output

Each case is shown in multiple lines with:

- Status
- Category (e.g., `Theft`, `Scam`)
- Case ID
- Date
- Title
- Info
- The time the case was created
- The last updated time of the case
- Victim (if available)
- Officer (if available)

Example:

```
You currently have 1 case in total
---
Note: Only basic case details (e.g. Title) are shown here and is truncated if too long.
For full case information (e.g. case-specific details like murder weapon),
use the read command
To see how to use the read command, run: help read
To use the read command, run: read <caseID>
---
======== CASE ID 000000 ========
Status     : Open
Category   : Murder
Title      : TITLE
Date       : 22/04/2023
Info       : Masked suspect entered victim's bedroom
Created at : 01/11/2025 20:57:05
Updated at : 01/11/2025 20:57:05
Victim     : Jane Doe
Officer    : Officer John Lee
```

> Note:
> - Deleted cases are excluded from all listings.
> - More specific case information (e.g. murder weapon) can only be accessed from read. This is an intentional design
    choice as we do not want to clutter the list command with unnecessary details.


---

### Closing a case: `close`

Marks a case as closed.

**Format:** `close ID`

* Closes the case with the specified `ID`.
* The id refers to the id of the case itself.
* The id **must be exactly 6 hexadecimal digits** 000001, 000fab, 00beef, … and the case must exist.

**Examples:**

* `close 000003` closes the case with the id 000003 in the list.

---

### Opening a case: `open`

Marks a case as open.

**Format:** `open ID`

* Reopens the case with the specified `ID`.
* The id refers to the id of the case itself.
* The id **must be exactly 6 hexadecimal digits** 000001, 000fab, 00beef, … and the case must exist.

**Examples:**

* `open 000003` opens the case with the id 000003 in the list.

---

### Editing a case: `edit`

Updates the details of an existing case.

**Format:** `edit ID [--title TITLE] [--date DATE] [--info INFO] [--victim VICTIM] [--officer OFFICER] ...`

* The id **must be exactly 6 hexadecimal digits** 000001, 000fab, 00beef, … and the case must exist.
* Editing the case requires one or more valid flags and their new values.
* If no flags are provided, the valid editable fields for that case type will be shown instead.
* `...` above refers to additional tags that may be available for certain categories. For more information on these additional
  tags, refer to [Case Categories](#case-categories).

**Examples:**

1. `edit 000001` : displays a list of all editable fields for the case.

Example output:
 ```
  Case found: [Open]   Traffic accident 000001 05/06/2018 Car accident

  Fields that can be edited: --title, --date, --info, --victim, --officer, --vehicle-type, --vehicle-plate, --road-name
```

2. `edit 000001 --title Drunk Driving --road-name Adam Road --date 03/04/2025` : edits the title, road name, and date of the case

Example output:
 ```
  Case edited:
  [Open]   Traffic accident 000001 03/04/2025 Drunk Driving
```

> ℹ️ Note: Not all edited fields will show up in the return message after a successful edit. To view the updated case in full detail, use the `read` command.\
> ℹ️ Note: A closed case cannot be edited. To edit the case, reopen the case using the [`open`](#opening-a-case-open) command.\
> ℹ️ Note: The above are stored as strings (except date). No special formatting is required for those inputs.\
> ℹ️ Note: Date is stored as a Java LocalDate. The default input format is `dd/MM/yyyy`. You may wish to change it using
> the [Settings](#settings-setting) command below.\
> ⚠️ Warning: A maximum of 5000 characters is allowed for all the fields.

---

### Deleting a case: `delete`

**Format:** `delete ID`

* Deletes the case with the specified ID.
* The id refers to the id of the case itself.
* The id **must be exactly 6 hexadecimal digits** 000001, 000fab, 00beef, … and the case must exist.

**Examples:**

* `delete 00012a` deletes the case with the id 00012a in the list.

---

### Settings: `setting`

This is a function to perform user-defined settings for the program. User can set the date input format and output
format.

**Format:** `setting --type TYPE --value VALUE`

The type argument determines the date format. It must be one of the following:
- `dateinput`: The expected format for user-provided dates.
- `dateoutput`: The display format for dates.
- `timestampoutput`: The display format for full date-times (e.g., "Updated At," "Created At") used in verbose listing or the read command.

> ℹ️ Note: The default date input and output format is `dd/MM/yyyy` and the default timestamp output format is `dd/MM/yyyy HH:mm:ss`.
> ⚠️ Warning: Note that month is capitalised in the date-time formatter. `MM` represents month while `mm` represents
> minutes. Hour-of-day are represented by `HH`.\
> ⚠️ Warning: The VALUE must be a valid date format, according to Java's DateFormatter. Stray characters that are not
> date and time-related will be flagged as an error.
> For more information, please refer
> to [Java DateTimeFormatter](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/format/DateTimeFormatter.html).\
> ⚠️ Warning: If the input format has repeated characters (e.g., `dd-MM-yyyy-dd`), the user is expected to key in the
> same day for both `dd`.

**Examples:**

* `setting --type dateinput --value dd-MM-yyyy`. Sets the required format for date inputs. 
The application will only accept dates matching the `dd-MM-yyyy` format.
* `setting --type dateoutput --value dd/MM/yyyy`.  Sets the display format for all dates to `dd/MM/yyyy`.
* `setting --type timestampoutput --value dd/MM/yyyy HH:mm:ss` Sets the display format for all timestamps to `dd/MM/yyyy HH:mm:ss`.

---

### Reading a case: `read`

Displays the full details of a specific case, including any category-specific fields. Fields that are not filled by the user will be shown as empty.

**Format:** `read ID`

> ℹ️ Note: The id **must be exactly 6 hexadecimal digits** 000001, 000fab, 00beef, … and the case must exist.

**Example:**

Input: `read 000001`

Example output:
```
  Title             : Murder at Yishun
  Case ID           : 000000
  Status            : Closed
  Category          : Murder
  Date              : 23/06/2020
  Victim            : Lim Ying
  Officer           : Tony
  Created at        : 01/11/2025 20:57:05
  Updated at        : 01/11/2025 20:57:05
  Weapon            : 
  Number of Victims : 
  Info              : Yishun Ring Road
```

---

### Finding for cases: `find`

To look for a case within the system that matches the title.

**Format:** `find --keyword KEYWORD`

> ℹ️ Note: KEYWORD is a string literal that will be matched. However, it is case-insensitive.\
> ℹ️ Note: This search is not case-sensitive asd finds all cases where the title contains the 
> consecutive KEYWORD string.

---

### Viewing the help menu: `help`

Displays a list of all available commands along with their descriptions and usage examples.

**Format:** `help`

* Shows a complete help menu of supported commands.
* Use this if you're unsure how to use a command or want a refresher.
* Can be run anytime

---

### Exiting the program: `bye`

Exits the program.

**Format:** `bye`

---

### File storage

This is a feature that saves your case info to `data.txt` in the folder that you run the program in.
There is no specific command that will execute this function, but it will automatically run every time 
you run any command. Take note that your date format settings are not saved between different launches of your 
application.

Please **DO NOT** edit the `data.txt` file on your own as this may lead to data corruption.
In addition, do not open the `data.txt` file as the program is running or change the permissions
to the `data.txt` file.

---

### Case categories

This section lists all the available case categories along with their additional editable attributes. 
These categories can help you organise and classify cases more effectively.
> **Notes:**
> * If the category is not in the list, type 'others' as the category.
> * Some categories have additional attributes, which can be edited using the `edit` feature. They cannot be set using the `add` command.
> * For more information on how to add and edit categories,
refer to [`add`](#adding-a-case-add) and [`edit`](#editing-a-case-edit)

**`CATEGORY` inputs:** 
- indicated under `CATEGORY` column
- input is case-insensitive (e.g., `Burglary`, `burglary`, and `BURGLARY` are all acceptable)

**`edit` tags:**
- `STRING`: attributes are stored as strings and accept any input.
- `INTEGER`: attributes are stored as integers and only accept '0' or positive whole numbers as input.
- `DECIMAl`: attributes are stored as a number up to 2 decimal points. '0' or any positive number is accepted, 
but numbers will be rounded off to nearest 2 decimals points.

| Display          | `CATEGORY` | `edit` tags (if any)                                                                                                          |
|------------------|------------|-------------------------------------------------------------------------------------------------------------------------------|
| Burglary         | burglary   | `--financial-value DECIMAL`, `--location STRING`                                                                              |
| Scam             | scam       | `--financial-value DECIMAL`                                                                                                   |
| Theft            | theft      | `--financial-value DECIMAL`, `--stolen-object STRING`                                                                         |
| Arson            | arson      | `--location STRING`, `--monetary-damage DECIMAL`                                                                              |
| Vandalism        | vandalism  | `--location STRING`, `--monetary-damage DECIMAL`                                                                              |
| Rape             | rape       |                                                                                                                               |
| Voyeurism        | voyeurism  |                                                                                                                               |
| Traffic accident | accident   | `--vehicle-type STRING`, `--vehicle-plate STRING`, `--road-name STRING`, `--number-of-casualties INTEGER`                     |
| Speeding         | speeding   | `--vehicle-type STRING`, `--vehicle-plate STRING`, `--road-name STRING`, `--speed-limit INTEGER` , `--exceeded-speed INTEGER` |
| Assault          | assault    | `--weapon STRING`, `--number-of-victims INTEGER`                                                                              |
| Murder           | murder     | `--weapon STRING`, `--number-of-victims INTEGER`                                                                              |
| Robbery          | robbery    | `--weapon STRING`, `--number-of-victims INTEGER`                                                                              |
| Others           | others     | `--custom-category STRING`                                                                                                    |

---

## FAQ

**Q**: How do I transfer my data to another computer?\
**A**: You can copy the `data.txt` file from the current working directory of the source computer to the directory
containing the .jar file of the destination computer. It is your responsibility to ensure that the file is not corrupted

**Q**: Is it recommended that I edit the `data.txt` file directly?\
**A**: It is not recommended as you may corrupt your data if you modify any data incorrectly. You should not touch the 
data file unless you are a system administrator trying to recover data from the file.

**Q**: Can I edit the `data.txt` file as the program is running?\
**A**: No, please do not edit or delete the data.txt file while the program is running as it may lead to data corruption.

**Q**: What happens if I accidentally deleted a case that I need?\
**A**: Fortunately, deleted cases are only marked as deleted and not removed from the data file. If you did not corrupt
the `data.txt` file, you can contact the system administrator to help you recover the deleted case. Please do not edit the
`data.txt` file yourself as it may lead to data corruption.

**Q**: What happens if a case is marked as closed?\
**A**: Closed cases are still visible in the list but are marked with `[Closed]` instead of `[Open]`.

**Q**: Can I edit a closed case?\
**A**: No, closed cases cannot be edited. You will need to reopen the case using the `open` command before you can edit
it.

**Q**: What date formats are supported?\
**A**: By default, SGSafe supports the `dd/MM/yyyy` format for date input and output. You can change the date input
and output formats using the [setting](#settings-setting) command. The supported formats are based on Java's DateTimeFormatter.

**Q**: How are the case ID generated?\
**A**: Case IDs are generated automatically by the system in hexadecimal format, starting from `000001` and incrementing
by 1 for each new case added. Deleted cases' IDs are not reused. This is because the system uses the case ID as a unique identifier
for each case, and reusing IDs could lead to confusion and data integrity issues.

---

## Command Summary

| Action          | Format                                                                                                | Example                                                                                                                    |
|-----------------|-------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------|
| **Add case**    | `add --category CATEGORY --title TITLE --date DATE --info INFO [--victim VICTIM] [--officer OFFICER]` | `add --category Theft --title Theft case --date 15/10/2025 --info Stolen wallet --victim John Doe --officer Officer Smith` |
| **List cases**  | `list`                                                                                                | `list`                                                                                                                     |
| **Close case**  | `close ID`                                                                                            | `close 000003`                                                                                                             |
| **Open case**   | `open ID`                                                                                             | `open 000003`                                                                                                              |
| **Edit case**   | `edit ID [--title TITLE] [--date DATE] [--info INFO] [--victim VICTIM] [--officer OFFICER]`           | `edit 000001 --victim Jane Smith --officer Officer Lee`                                                                    |
| **Delete case** | `delete ID`                                                                                           | `delete 00beef`                                                                                                            |
| **Setting**     | `setting --type TYPE --value VALUE`                                                                   | `setting --type dateinput --value dd-MM-yyyy`                                                                              |
| **Read Case**   | `read ID`                                                                                             | `read 000001`                                                                                                              |
| **Find Case**   | `find --keyword KEYWORD`                                                                              | `find --keyword robbery`                                                                                                   |
| **Help**        | `help`                                                                                                | `help`                                                                                                                     |
| **Exit**        | `bye`                                                                                                 | `bye`                                                                                                                      |

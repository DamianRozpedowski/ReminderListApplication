# User Manual

**Author**: [Team 2] Alex Chen, Jun Mei He

---

## 1. Register & Login

Upon application startup, users are prompted to `Allow` or `Don't Allow` Notifications. For full functionality, the user should `Allow` notifications.

The user is then prompted to enter a `Username` and a `Password`. If the user already has an existing account, they can `LOGIN` with the credentials provided. Otherwise, the user must first `REGISTER`.

* `REGISTER` button adds a user account into the database with the provided credentials input by the user. Usernames are unique for each registered user
* `LOGIN` button authenticates the input credentials. If the account exists and the password is correct, the user will be logged in to their account

---

## 2. Reminder Lists Interface (Home Page)

Once successfully logged in, users can start creating new lists or view existing lists on this screen.

* `CREATE NEW LIST` button prompts the user to enter a name for the list to be created. List names are unique and cannot be repeated
  * `CREATE` button adds a new list with the provided name into the database
  * `CANCEL` button terminates the prompt and returns the user to the `Reminder Lists` screen

If there are existing lists or if the user creates a new list, these will be displayed on this screen. Users can tap into a list to access the view list interface.

---

## 3. View/Edit List Interface

Users access this screen by selecting an existing reminder list from the list of lists. Through this interface, users can edit, delete and rename this selected list as well as add new reminders, clear all checks for reminders on this selected list, view all existing reminders that were added into this selected list, and tap into an existing reminder in this list to edit it.

* `ADD REMINDER` button brings up the reminder creation interface
* `CLEAR CHECKS` button clears checks for all the reminders that are checked off on this selected list (sets the reminders' `checkedOff` value to false if true)
* `RENAME` button updates the selected list's name with the new list name input by the user
* `BACK` button returns user to the home page containing the lists of all the lists of reminders that have been created by the user
* `DELETE LIST` button permanently removes from the database the selected list along with all the reminders that were added to this selected list

---

## 4. Reminder Creation Interface

Users access this screen through the `ADD REMINDER` button on the view list interface. Users are prompted multiple fields to input data for creating a new reminder.

* `Name` of the reminder (not unique, duplicate names are allowed)
* `Type` of the reminder by which reminders will be grouped by
* `Content` of the reminder for additional data related to the reminder
* `Date/Time Alert` switch to enable/disable reminder notification alerts based on the `Date` (YYYY/MM/DD) and `Time` (HH:MM: 24hr Military Time) provided by the user
* `Repeat Reminder` switch to enable/disable notification alerts to repeat by the frequency set by user
* `Location Alert` switch to enable/disable notification alerts based on the `Location` specified by the user

The `ADD REMINDER` button adds this reminder with all the provided data into the database. The `CANCEL` button terminates the reminder creation prompt and returns the user to the selected list's view interface.

---

## 5. Edit Reminder Interface

Users access this screen by selecting a reminder of a particular reminder list from the view/edit list interface. Users are prompted the same fields provided on the reminder creation interface with the addition of a `Check Off` switch.

* `Check Off` switch to mark the selected reminder as `checked off` (true) or `not checked off` (false)
* `SAVE` button updates the selected reminder's information in the database with any input changes made by the user
* `DELETE` button permanently removes from the database the selected reminder and all data associated with it
* `CANCEL` button terminates the edit reminder prompt and returns user back to the selected list's view interface

---

## 6. Notifications based on Date/Time

Users will receive notifications based on the date and time they set for their reminder. 

* The notification will be sent to the user's device at the specified date and time. The notification will contain the name of the reminder and the content of the reminder.
* For this feature to be active the `Date/Time Alert` switch must be enabled when creating a reminder or saving an edited reminder

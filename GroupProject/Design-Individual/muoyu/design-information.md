# A5: Software Design - Reminders

## Assumptions & Rationale 

* Reminders and lists can exist independently of one another
  * User can create reminders without creating lists
  * User can create lists without creating reminders
  * Reminders are created before they can be added to a list
* Reminders are unique, there can be no more than 1 reminder with the same `reminderName` stored in the database
  * There are no more than 1 list with the same reminder
* A reminder can only be of one type
* Appropriate (parameterized) constructors, setters, and getters are assumed for all classes and method calls

---

## Requirements

1. **A list consisting of reminders the users want to be aware of. The application must allow users to add reminders to a list, delete reminders from a list, and edit the reminders in the list.** 

To realize this requirement, a class `ReminderList` was added with attributes `listName` and `reminderList`. The attribute `reminderList` is a List of `Reminder` objects.

The `User` can add and remove reminders from a select list with `User` methods `addReminder(ReminderList, Reminder)` and `removeReminder(ReminderList, Reminder)`, and edit a specific reminder with method `editReminder(Reminder)`.

---

2. **The application must contain a database (DB) of reminders and corresponding data.** 

The database will store `Reminder` objects as they are created by the `User`. A `Reminder` is identified in the database by attributes `reminderName` and `typeName`.

---

3. **Users must be able to add reminders to a list by picking them from a hierarchical list, where the first level is the reminder type (e.g., Appointment), and the second level is the name of the actual reminder (e.g., Dentist Appointment).**

To realize this requirement, the method `searchByType(Type)` was added to the `User` class. This method will take a `Type` String input which domain is the `typeName` attribute of `Reminder` objects stored in the database, and return a list of `Reminder` objects which `typeName` matches the input `Type`. The `User` can then select a `Reminder` from this list and add it to a list with the method `addReminder(ReminderList, Reminder)`.

---

4. **Users must also be able to specify a reminder by typing its name. In this case, the application must look in its DB for reminders with similar names and ask the user whether that is the item they intended to add. If a match (or nearby match) cannot be found, the application must ask the user to select a reminder type for the reminder, or add a new one, and then save the new reminder, together with its type, in the DB.** 

To realize this requirement, the method `searchByName(String)` was added to the `User` class. If no match or nearby match for the input String is found, the `User` is prompted to create a new reminder with the method `createReminder(String, String)` where the first String specifies the type for the reminder and the second String specifies the name of the reminder. A new `Reminder` object will be instantiated from this method call and be stored in the database.

---

5. **The reminders must be saved automatically and immediately after they are modified.** 

All `Reminder` objects are stored in the database with their respective identifiers and attributes upon instantiation. Any modifications will update the appropriate database entry accordingly.

---

6. **Users must be able to check off reminders in the list (without deleting them).** 

To realize this requirement, every `Reminder` object has a boolean attribute `isCheckedOff` defaulted to false upon instantiation. The `Reminder` method `checkOff()` can be implemented to negate the value of `isCheckedOff`–to check off a reminder if `isCheckedOff` is false or to uncheck a reminder if `isCheckedOff` is true.

---

7. **Users must also be able to clear all the check-off marks in the reminder list at once.** 

To realize this requirement, the method `clearAllChecks()` was added to the `ReminderList` class. Assuming the functionality previously stated for requirement (6), `clearAllChecks()` will call `checkOff` on all `Reminder` objects of the current `reminderList` such that the `Reminder`'s attribute `isCheckedOff` is equal to true. This will clear all the check-off marks of the reminders in that list in particular while the reminders that were not checked off will remain unchanged.

---

8. **Check-off marks for the reminder list are persistent and must also be saved immediately.** 

All `Reminder` objects are stored and updated in the database accordingly. Every `Reminder` object has an attribute `isCheckedOff` which keeps track of the check-off marks.

---

9. **The application must present the reminders grouped by type.** 

To realize this requirement, the `Reminder` class was given the attribute `typeName` to categorize every `Reminder` object by a `Type` provided by `User` input upon object instantiation. `Reminder` objects in the database can be easily filtered and grouped by types using this attribute.

---

10. **The application must support multiple reminder lists at a time (e.g., “Weekly”, “Monthly”, “Kid’s Reminders”). Therefore, the application must provide the users with the ability to create, (re)name, select, and delete reminder lists.** 

To realize this requirement, a class `ReminderList` was added with attributes `listName` and `reminderList`. 

The `User` can create a new list with the `User` method `createList(String)` which takes a user input String for the `listName`, or delete an existing list with the method `deleteList(ReminderList)`.

The `ReminderList` class also has a method `renameList(String)` to update the current object's `listName` attribute with the provided String.

---

11. **The application should have the option to set up reminders with day and time alert. If this option is selected allow option to repeat the behavior.** 

To realize this requirement, the `Reminder` class was given attributes `dayTimeAlert` and `repeatAlert`. 

The attribute `dayTimeAlert` is a String that represents a day of the week (Monday to Sunday) and the time in hours and minutes.

The method `setAlert()` will allow the `User` to input the desired value for `dayTimeAlert` and set the appropriate boolean value of `repeatAlert` depending on whether the `User` wants the alert to be repeated or not on the specified day of the week and time.

---

12. **<u>Extra Credit</u>: Option to set up reminder based on location.** 

To realize this requirement, the `Reminder` class was given the attribute `location` and the method `setLocation(String)`. The `User` provides an input String which will set up that `Reminder` object with the provided `location` String.

---

13. **The User Interface (UI) must be intuitive and responsive.**

Not considered because it does not affect the design directly.
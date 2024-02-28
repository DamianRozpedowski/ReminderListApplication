Requirements
1. A list consisting of reminders the users want to be aware of. The application must allow users to add reminders to a list, delete reminders from a list, and edit the reminders in the list.
- The class ReminderList implements this by using the following operations, addReminder(reminderName) adds a reminder to the list, deleteReminder(reminderName) removes a reminder from the list, editReminder(reminderName) edits the reminder specified in the list.

2. The application must contain a database (DB) of reminders and corresponding data.
- A UML does not include databases, but the class Reminder and ReminderType includes attributes for type, name, location, etc., all of these should be saved into the database which satisfies this requirement.

3. Users must be able to add reminders to a list by picking them from a hierarchical list, where the first level is the reminder type (e.g., Appointment), and the second level is the name of the actual reminder (e.g., Dentist Appointment).
- This is done with the implementation of two classes, ReminderType and Reminder. ReminderType simply specifies the name of the type, Reminder has the rest of the properties such as the name.

4. Users must also be able to specify a reminder by typing its name. In this case, the application must look in its DB for reminders with similar names and ask the user whether that is the item they intended to add. If a match (or nearby match) cannot be found, the application must ask the user to select a reminder type for the reminder, or add a new one, and then save the new reminder, together with its type, in the DB.
- The searchReminder() operation in the ReminderList class, will check the database for a match/partial match on the specified reminder.Previously used reminders and their type will be saved in the database. The ReminderType class has the operation createReminder(reminderType,reminderName) which would add a new reminderType as well as a reminderName, it will associate both of these and they will be saved in the DB for future use. The 

5. The reminders must be saved automatically and immediately after they are modified.
- Again the database should not be shown in a UML, but this would again save the Reminders and their attributes, includes type, name, location, etc. to the database when modified, meaning, when added,edited, or deleted through the operations in the ReminderList class.


6. Users must be able to check off reminders in the list (without deleting them).
- The Reminders class has a boolean attribute isChecked which has the value true or false, whether it's checked off or not. Additionally the ReminderList class has a setCheck() opperation which will essentially edit the Reminder and update it's boolean value.

7. Users must also be able to clear all the check-off marks in the reminder list at once.
- This was also implemended in the ReminderList class with the clearChecked() operation, which will clear all of the checked reminders on the list using the isChecked attribute.

8. Check-off marks for the reminder list are persistent and must also be saved immediately.
- The check offs are an attribute of the Reminder class and since the reminders are already going to be saved into the database, its attributes, including the isChecked boolean attribute will also be saved. 

9. The application must present the reminders grouped by type.
- Not considered because it does not affect the design directly. This is more of a UI implementation.

10. The application must support multiple reminder lists at a time (e.g., “Weekly”, “Monthly”, “Kid’s Reminders”). Therefore, the application must provide the users with the ability to create, (re)name, select, and delete reminder lists.
- This is done with the implementation of the AllLists class which has a collection of ReminderLists, the operations are also satified with, createReminderList(listName), deleteReminderList(listName), renameReminderList(listName), and selectReminderList(listName) which fulfill their respective duty. The ReminderList class also has the name attribute. Both of these work to support multiple reminder lists.

11. The application should have the option to set up reminders with day and time alert. If this option is selected allow option to repeat the behavior.
- This is implemented in the Reminder class with the alertAt:String attribute, setAlert() and setRepeat() operation. The alertAt should save a string which includes date and time something like, "02.12.2024-08:30" which would indicate Febuary 12th 2024, 8:30am. the setAlert operation would then use this information and set up alerts. setRepeat would also perform an operation to repeat this alert based on a selected time stamp, hours, days, weeks, etc.

12. Extra Credit: Option to set up reminder based on location.
- This was done by creating another class Location which stores the information on a location such as street address and city. The Reminder class has a setLocation() operation that would set the location, and the setAlert() can be adapted to not only include time, as originally intended, but also location which should satisfy this requirement.

13. The User Interface (UI) must be intuitive and responsive.
- Not considered because it does not affect the design directly.


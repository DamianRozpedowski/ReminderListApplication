1) A list consisting of reminders the users want to be aware of. The application must allow
users to add reminders to a list, delete reminders from a list, and edit the reminders in
the list.

    This requirement is relies on the creation of a reminderList which stores all of the contents of a specific reminder within a list.
    This was implemented by creating a Users class a reminder class and a contained reminderList class. The Users class has createReminderList
    and getReminderList methods in order to create a list for the user. The reminderList has add, delete and edit reminders which all take in a reminderName of type String in order to allow the user to modify the reminder. Finally the reminder class has the editContent method to change the content of the specific reminder. The User class should call the reminderList class's respective method that correlates to the modification being done and that should in turn call the reminder class's method.

2)  The application must contain a database (DB) of reminders and corresponding data.

    While the UML diagram represents the structure and relationships of the application, the database is not explicitly depicted. However, the Users, reminderList and reminder class have been configured to have UUIDs and even a foreign key from the User class present in the reminderList which signifies which specific user does a table belong to.

3) Users must be able to add reminders to a list by picking them from a hierarchical list,
where the first level is the reminder type (e.g., Appointment), and the second level is the
name of the actual reminder (e.g., Dentist Appointment).

    A new class was created called reminderType which holds the more finer details of the reminder like reminderType (Appointment), and the specificReminderType(Dentist Appointment). The reminderList class has a method called addReminderType and addNewSpecificReminder which takes in a streing and adds it to their respective list. The User class should call the reminderList class's addReminderByType, addReminderByName  method in order to add a reminderType to the list.

4) Users must also be able to specify a reminder by typing its name. In this case, the
application must look in its DB for reminders with similar names and ask the user
whether that is the item they intended to add. If a match (or nearby match) cannot be
found, the application must ask the user to select a reminder type for the reminder, or
add a new one, and then save the new reminder, together with its type, in the DB.

    The reminderList class has a function, searchReminderByName that should query the database and look for reminders similar to the one asked by the user. If a match cannot be found then the user should be prompted to select a reminder type for the reminder, or add a new one with the addReminderByName or addReminderByType.

5) The reminders must be saved automatically and immediately after they are modified.

    The reminderList has a method called saveListToDB which can be used to query the database and save the list to the database at any point in time, especially after a modification has been made.

6) Users must be able to check off reminders in the list (without deleting them)

    The reminderList class has a method called checkOffReminder which takes in a reminderName and checks it off. This then goes into the specific reminder and sets the reminder's attribute 'checkOff' to true and should be able return its contents to reminderList. to The User class should call the reminderList class's checkOffReminder method in order to check off a reminder.

7) Users must also be able to clear all the check-off marks in the reminder list at once.

    Within reminderList there is a method called clearAllCheckOffs which sets all of the reminders' checkOff attribute to false. The User class should call the reminderList class's clearAllCheckOffs method in order to clear all the check-off marks in the reminder list at once.

8) Check-off marks for the reminder list are persistent and must also be saved immediately.

    Again the reminderList class has a method called saveListToDB which can be used to query the database and save the list to the database, including the checkOff attribute.

9) The application must present the reminders grouped by type.

    The reminderList class has a method called getReminderTypeGroup which returns a hashmap of reminderType as the key and a list of all reminders that have the the same reminderType.
    The method should go through its list of reminders and add them to the hashmap based on their reminderType. The User class should call the reminderList class's getReminderTypeGroup method in order to get the reminders grouped by type.

10) The application must support multiple reminder lists at a time (e.g., “Weekly”, “Monthly”,
“Kid’s Reminders”). Therefore, the application must provide the users with the ability to
create, (re)name, select, and delete reminder lists

    The createReminderList method is modified to take in a string that specifies what type of reminder list this will be. The created list will be able to be accessed by the user by calling the getReminderList method. The User class can also call deleteReminderList, renameReminderList and selectReminderList in order to modify a specific reminderList. Furthermore the reminderList also has an attribute that specifies what type of reminderList it is.

11)  The application should have the option to set up reminders with day and time alert. If this
        option is selected allow option to repeat the behavior.

    To support this, the attributes dayTimeAlert with type boolean to be set if the user wants to specify a day and time alert, the attributes alertForDate and alertForTime that will be set by the methods setAlertForDateTime that allows to user to set the date and time for the alert. Furthermore, if the dayTimeAlert is set to true then the user can also choose to set a repeat on the reminder. This repeat reminder is set with the attributes repeatReminder of type boolean and repititionAmount of type int. The methods repeatReminder takes in type boolean and assigns it to the repeatReminder attribute and setRepetition amount will activate based on this boolean and set a repeat on the reminder based on this. The reminder class allows for specific date and time for a specific reminder.

12) Extra Credit: Option to set up reminder based on location.

    The reminderClass has the attributes setLocation of type boolean and reminderLocation of type String that can be modified with the methods setLocationBool that can be set by the user if they want to have a location based reminder and setReminderLocation that takes in a String and sets the reminderLocation attribute to that String. The reminderList class has a method called getReminderLocation that returns the reminderLocation attribute of the reminder. The reminder class allows for a specific location based on a specific reminder.
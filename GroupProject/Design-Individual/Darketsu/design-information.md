CS 370 Software Engineering
Assignment 5: Reminders Software Design
By: Daniel Zheng

Design Information Requirements
1) To realize a list of reminders for users to add, delete, edit, and clear checked reminders;
    I created a ReminderList class and added functions to allow the user to add, delete, and clear checked            reminders from a ReminderList
2) To realize a DB of reminders and corresponding data within the App;
    I created a ReminderDB class that manages all Reminders with the function to saveReminder, to either the          device storage system or to a cloud storage, and deleteReminder to remove reminders from the DB
3) A Hierarchical list was not designed because the RemindersDB will be able to set a hierarchy by setting the        ReminderType as the PrimaryKey and ReminderName as a SecondaryKey 
4) To realize a way to specify reminder names and further be able to add or create and save new reminders;            I created a searchBar class that has access to the ReminderDB with functions to locate reminders and add new      reminders
5) Refer to Req. 2 about saving reminders
6) To realize a way to check off reminders;
    I added an isCompleted Boolean that will display a checked box if it is true
7) Refer to Req 1 about clearing all checked boxes in a list
8) To make sure the checked reminders are persistent;
   The ReminderDB will keep a record of all attributes of reminders and save them to the cloud or device storage
9) To realize grouping reminders by type, the ReminderDB will be able to group by ReminderType
10) The ReminderDB will be able to contain multiple ReminderLists at a time, and the UI will have functions that      will be able to create, rename, select, and delete ReminderLists  
11) Day and Time alerts are attributes put into Reminders with a function to check the date with current time,        along with a isRepeat boolean to check if user wants to repeat the reminder
12) To realize location reminders, I created an attribute, Location, that contains the Longitude and Latitude of      a specific location
13) To realize a UI, i created a User Interface class that will display, create, rename, select, delete                RemainderLists as well as a searchbar

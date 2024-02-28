# Test Plan
**Author**: [Team 2] Alex Chen, Damian Rozpedowski, Daniel Zheng, Jun Mei He, Sarker Sakib

## 1. Testing Strategy

### 1.1. Overall strategy
Our testing strategy is to ensure comprehensive coverage of all application functions along with database and API queries.

**The methods we will use to test our application are as follows:**

* A Combination of manual and automated unit testing
* Testing the integration between different components by checking their end to end processes. This will be done with extensive unit testing on the methods that call upon the database and API such that any issues in querying or storing data will be caught early on.
* Regression testing to verify that new changes do not affect existing functionality, we plan on having a complete isolation on each functionality so that if issues were to occur debugging and fixing will be minimal and easier to detect.

---

### 1.2. Test Selection
We have chosen to explore white-box testing on our application to ensure that all possible paths are tested and that all functions are working as intended. Since the application will be tested by us we already know the expected results of each function, so we will be able to test the actual results against the expected results.

For our database and API testing we plan on exploring this with a combination of white and black box as we have selected specific team members for each task so they will be able to perform the tests on their own and when they are done they can open up the testing to other members who don't have the same knowledge as them to see if they can find any issues.

---

### 1.3. Adequacy Criterion
To ensure the quality of the test cases are covered, our test cases will be evaluated using code coverage metrics for unit testing, ensuring that a minimum of 60% of the code is executed during the test. For database and API testing, we will aim for 100% coverage of all use case scenarios. Regression tests will target newly added code, as well as critical paths identified in the application.

To ensure the code coverage is functional we plan on having the tests evaluated by different members to ensure that we have a different thought process throughout the testing phase.

---

### 1.4. Bug Tracking
Tracking bugs will be done in our repository/our discord server. We will have a channel dedicated to bug tracking where we will post any bugs that we find and we will have a member assigned to each bug to fix it. 

Any bugs that are found should be posted and should be assigned to a member to fix, once the bug is fixed the member will post the fix, tested, and the bug will be closed.

---

### 1.5. Technology
We plan on using JUnit for unit tests of our frontend and backend, along with Mockito for mocking objects without fully instantiating them.

---

## 2. Test Cases
| Test                                                         | Description                                                  | Expected Result                                              | Actual Result                                                | Pass/Fail | Additional Information |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | --------- | ---------------------- |
| Verify the addition of a reminder                            | 1. Select an existing list <br>2. Click on the `ADD REMINDER` button <br>3. Fill out the fields and then click on the `ADD REMINDER` button | New reminder is added to the list and DB.                    | New reminder is added to the list and DB                     | Pass      |                        |
| Ensure User is created properly                              | When a user creates an account, the username must be unique and a sufficiently secure password must be used. The user credentials should be stored on the database | User Created/Not created                                     | User Created/Not created                                     | Pass      |                        |
| Validate the deletion of a reminder                          | 1. Select an existing reminder <br>2. Click on the `DELETE LIST` button | Selected reminder is removed from the list and DB.           | Selected reminder is removed from the list and DB.           | Pass      |                        |
| Test editing a reminder                                      | 1. Select the list containing the desired reminder<br>2. Select the reminder to edit<br> 2. Modify details and save changes | The reminder is updated in the list and DB with new details. | The reminder is updated in the list and DB with new details. | Pass      |                        |
| Verify checked reminder status                               | 1. Choose a reminder <br>2. Check the reminder               | Reminder should get the checked status set to TRUE           | reminder should get the checked status set to TRUE           | Pass      |                        |
| Ensure clearChecked functions properly                       | 1. Check a reminder (if a reminder is not already checked) <br>2. Clear all checked reminders | Checked reminders should be removed from the list            | Checked reminders should be removed from the list            | Pass      |                        |
| Verify the Date and Time alerts are checked for any DateTimeExceptions for reminder creation and editing | 0. Date and Time are expecting MM-DD-YYYY and HH:MM (24Hr) <br />1. Ensure that whatever the user puts in, the app checks it and if it fails check, the app should automatically assign current time to the date and time along with a toast | Current Time set to the date and time along with a toast     | Current Time set to the date and time along with a toast     | Pass      |                        |
| Login button clicked without registering first               | 1. Click on the login button without registering first       | The user should not be able to login and stays on the same activity | The user should not be able to login and stays on the same activity | Pass      |                        |
| Login button clicked with valid information should navigates to Home_ReminderLists activity | Test if the app navigates to the Home_ReminderLists activity after entering valid credentials and clicking the login button. | Navigate to Home_ReminderLists activity.                     | Navigate to Home_ReminderLists activity.                     | Pass      |                        |
| Test creation of a reminder list and check if ListView is updated | 1. Click on the button to create a new reminder list. <br>2. Add a reminder list name and confirm creation. <br>3. Check if the ListView is updated with the new reminder list. | ListView should display the new reminder list.               | ListView displays the new reminder list.                     | Pass      |                        |
| Test creation of a reminder list with a duplicate name       | 1. Create a reminder list with a unique name. <br>2. Attempt to create another reminder list with the same name. <br>3. Check if the app prevents the creation of a list with a duplicate name and stays on the creation screen. | App stays on creation screen                                 | App stays on creation screen with a toast displayed to alert the user | Pass      |                        |
| Test creation of a reminder list                             | Test if a reminder list can be created and stored in the database by a user. | Reminder list should be created and stored in the DB.        | Reminder list is created and stored in the DB.               | Pass      |                        |
| Test deletion of a reminder list                             | Test if a created reminder list can be successfully deleted from the database. | Reminder list should be deleted from the DB.                 | Reminder list is removed from the DB                         | Pass      |                        |
| Test renaming of a reminder list                             | Test if a reminder list can be successfully renamed in the database. | Reminder list name should be updated in the DB.              | Reminder list name is updated in the DB.                     | Pass      |                        |
| Testing if a reminder list is removed, all reminders associated with it will also be removed | After a user creates a list along with reminders assigned to that list, if they want to remove the list all reminders should also be deleted | Reminder List and all reminders are not in the database anymore | Reminder List and all reminders are not in the database anymore | Pass      |                        |
| Ensure a reminder is removed if a user choses to delete reminder | When a user clicks on their list of reminders and selects delete, the reminder should be removed from the database along with the list | Database and list doesn't include reminder associated with the delete | Database and list doesn't include reminder associated with the delete | Pass      |                        |
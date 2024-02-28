package edu.qc.seclass.rlm;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import edu.qc.seclass.rlm.roomLocalDB.AppDatabase;
import edu.qc.seclass.rlm.roomLocalDB.ReminderDao;
import edu.qc.seclass.rlm.roomLocalDB.ReminderListDao;
import edu.qc.seclass.rlm.roomLocalDB.UserDao;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private UserDao userDao;
    private ReminderListDao reminderListDao;
    private ReminderDao reminderDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        userDao = db.userDao();
        reminderListDao = db.reminderListDao();
        reminderDao = db.reminderDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    // Checking if User got created properly
    @Test
    public void testCreateUser() {
        User user = new User("testUser", "testPassword");
        userDao.insert(user);
        List<User> users = userDao.getAll();
        assertEquals(1, users.size());
        assertEquals("testUser", users.get(0).getUsername());
        assertEquals("testPassword", users.get(0).getPassword());
        Log.d(TAG, "User created: " + users.get(0).getUsername());
    }

    // Checking if Users can get deleted
    @Test
    public void testDeleteUser() {
        //Adding a user
        User user = new User("testUser", "testPassword");
        userDao.insert(user);
        List<User> users = userDao.getAll();
        Log.d(TAG, "# of Users: " + users.size());

        // Deleting a user
        userDao.delete(user);
        List<User> usersAfterDel = userDao.getAll();
        assertEquals(0, usersAfterDel.size());
        Log.d(TAG, "# of Users: " + usersAfterDel.size());
    }

    // Checking if a user created a ReminderList properly
    @Test
    public void testCreateReminderList() {
        User user = new User("testUser", "testPassword");
        userDao.insert(user);

        ReminderList reminderList = new ReminderList("Test List", user.getUserID());
        reminderListDao.insert(reminderList);

        List<ReminderList> reminderLists = reminderListDao.getAll();

        assertEquals(1, reminderLists.size());
        assertEquals("Test List", reminderLists.get(0).getListName());

        Log.d(TAG, "ReminderList created: " + reminderLists.get(0).getListName());
    }

    // Testing the deletion of a reminderList
    @Test
    public void testDeleteReminderList() {
        User user = new User("testUser", "testPassword");
        userDao.insert(user);

        // Create Reminder List
        ReminderList reminderList = new ReminderList("Test List", user.getUserID());
        reminderListDao.insert(reminderList);

        // Delete Reminder List
        reminderListDao.deleteReminderList(reminderList.getReminderListID());

        List<ReminderList> reminderLists = reminderListDao.getAll();
        assertEquals(0, reminderLists.size());

        Log.d(TAG, "ReminderList deleted: " + reminderList.getListName() +" Reminder Lists Size:" + reminderLists.size());
    }

    // Checks if reminderList successfully gets renamed
    @Test
    public void testRenameReminderList() {
        User user = new User("testUser", "testPassword");
        userDao.insert(user);

        // Create reminder list with initial name = "Test List"
        ReminderList reminderList = new ReminderList("Test List", user.getUserID());
        reminderListDao.insert(reminderList);

        Log.d(TAG, "ReminderList Initial Name: " + reminderList.getListName());

        // Rename the list
        reminderListDao.renameReminderList(reminderList.getReminderListID(), "Renamed List");

        List<ReminderList> reminderLists = reminderListDao.getAll();
        assertEquals(1, reminderLists.size());

        ReminderList renamedList = reminderLists.get(0);
        assertEquals("Renamed List", renamedList.getListName());

        Log.d(TAG, "ReminderList Renamed: " + renamedList.getListName() + " There should only be one reminderList: "+ reminderLists);
    }


    // Test the creation of a simple reminder in a reminder List | Only checks when reminder is constructed with ID, reminderName, content, and reminderType
    @Test
    public void testAddReminder() {
        User user = new User("testUser", "testPassword");
        userDao.insert(user);

        ReminderList reminderList = new ReminderList("Test List", user.getUserID());
        reminderListDao.insert(reminderList);

        // Creates a reminder
        Reminder reminder = new Reminder(reminderList.getReminderListID(), "Test Reminder", "Test Content", "Test Type");
        reminderDao.insert(reminder);

        List<Reminder> reminders = reminderDao.getAll();

        assertEquals(1, reminders.size());
        assertEquals("Test Reminder", reminders.get(0).getReminderName());
        assertEquals("Test Content", reminders.get(0).getContent());
        assertEquals("Test Type", reminders.get(0).getReminderType());

        Log.d(TAG, "Reminder added  Name: " + reminders.get(0).getReminderName() + " | Type: " + reminders.get(0).getReminderType()+ " | Content: " + reminders.get(0).getContent());
    }

    // Validate the deletion of a reminder
    @Test
    public void testDeleteReminder() {
        User user = new User("testUser", "testPassword");
        userDao.insert(user);

        ReminderList reminderList = new ReminderList("Test List", user.getUserID());
        reminderListDao.insert(reminderList);

        // Create a reminder
        Reminder reminderToDelete = new Reminder(
                reminderList.getReminderListID(),
                "Test Reminder",
                "Test Content",
                "Test Type"
        );
        reminderDao.insert(reminderToDelete);

        Reminder retrievedReminder = reminderDao.getReminderById(reminderToDelete.getReminderID());

        reminderDao.deleteById(reminderToDelete.getReminderID());

        retrievedReminder = reminderDao.getReminderById(reminderToDelete.getReminderID());

        assertNull(retrievedReminder);
    }

    // Validate that the checkOff property is set to true when a user checks the reminder
    @Test
    public void testCheckOffReminderTrue() {
        // Create a user
        User user = new User("testUser", "testPassword");
        userDao.insert(user);

        // Create a reminder list
        ReminderList reminderList = new ReminderList("Test List", user.getUserID());
        reminderListDao.insert(reminderList);

        // Create a reminder
        Reminder reminder = new Reminder(
                reminderList.getReminderListID(),
                "Test Reminder",
                "Test Content",
                "Test Type"
        );
        reminderDao.insert(reminder);

        // Check off the reminder
        reminder.setCheckOff(true);
        reminderDao.update(reminder);

        Reminder checkedReminder = reminderDao.getReminderById(reminder.getReminderID());

        assertTrue(checkedReminder.getCheckOff());

        Log.d(TAG, "Reminder checked off: " + checkedReminder.getReminderName());
    }

    // Validate that the checkOff property is set to false when created
    @Test
    public void testCheckOffReminderFalse() {
        // Create a user
        User user = new User("testUser", "testPassword");
        userDao.insert(user);

        // Create a reminder list
        ReminderList reminderList = new ReminderList("Test List", user.getUserID());
        reminderListDao.insert(reminderList);

        // Create a reminder
        Reminder reminder = new Reminder(
                reminderList.getReminderListID(),
                "Test Reminder",
                "Test Content",
                "Test Type"
        );
        reminderDao.insert(reminder);
        Reminder checkedReminder = reminderDao.getReminderById(reminder.getReminderID());
        
        assertFalse(checkedReminder.getCheckOff());

        Log.d(TAG, "Reminder checked off: " + checkedReminder.getReminderName());
    }

    // Test that checked reminders get cleared
    @Test
    public void testClearAllCheckedReminders() {
        // Create a user
        User user = new User("testUser", "testPassword");
        userDao.insert(user);

        ReminderList reminderList = new ReminderList("Test List", user.getUserID());
        reminderListDao.insert(reminderList);

        // Create a checked reminder
        Reminder checkedReminder1 = new Reminder(
                reminderList.getReminderListID(),
                "Checked Reminder 1",
                "Checked Content 1",
                "Checked Type 1"
        );
        checkedReminder1.setCheckOff(true);
        reminderDao.insert(checkedReminder1);

        // Create another checked reminder
        Reminder checkedReminder2 = new Reminder(
                reminderList.getReminderListID(),
                "Checked Reminder 2",
                "Checked Content 2",
                "Checked Type 2"
        );
        checkedReminder2.setCheckOff(true);
        reminderDao.insert(checkedReminder2);

        // Create an unchecked reminder
        Reminder uncheckedReminder = new Reminder(
                reminderList.getReminderListID(),
                "Unchecked Reminder",
                "Unchecked Content",
                "Unchecked Type"
        );
        reminderDao.insert(uncheckedReminder);

        reminderDao.clearAllCheckOff(reminderList.getReminderListID());
        List<Reminder> allReminders = reminderDao.getAll();

        assertEquals(3, allReminders.size());

        // All reminders have checkOff set to false
        for (Reminder reminder : allReminders) {
            assertFalse(reminder.getCheckOff());
        }
    }


}
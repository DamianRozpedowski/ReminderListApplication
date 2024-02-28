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

// White box Testing of ReminderDao
@RunWith(AndroidJUnit4.class)
public class ReminderDaoTests {
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
        // Asserting reminder object exist in Reminder DB.
        assertNotNull(retrievedReminder);

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

    // Test the update of a simple reminder in a reminder List | Only checks when reminder is constructed with ID, reminderName, content, and reminderType
    // (White box testing)
    @Test
    public void testUpdateReminder() {
        User user = new User("testUser", "testPassword");
        userDao.insert(user);

        ReminderList reminderList = new ReminderList("Test List", user.getUserID());
        reminderListDao.insert(reminderList);

        // Creates a reminder
        Reminder reminder = new Reminder(reminderList.getReminderListID(), "Test Reminder", "Test Content", "Test Type");
        reminderDao.insert(reminder);

        List<Reminder> reminders = reminderDao.getAll();

        // Asserting to ensure the initial value set is the same.
        assertEquals(1, reminders.size());
        assertEquals("Test Reminder", reminders.get(0).getReminderName());
        assertEquals("Test Content", reminders.get(0).getContent());
        assertEquals("Test Type", reminders.get(0).getReminderType());

        // Update reminder name.
        reminder.setReminderName("Updated Test Reminder");

        // Asserting to ensure reminder object get new name update.
        assertEquals("Updated Test Reminder", reminder.getReminderName());

        // Update DB data
        reminderDao.update(reminder);

        // Fetch the list again
        reminders = reminderDao.getAll();

        // Reassert the change to see if the update is applied.
        assertEquals("Updated Test Reminder", reminders.get(0).getReminderName());

        Log.d(TAG, "Reminder updated  Name: " + reminders.get(0).getReminderName() + " | Type: " + reminders.get(0).getReminderType()+ " | Content: " + reminders.get(0).getContent());
    }

    // Test the deleteAll of as simple reminder in a reminder List | Only checks when reminder is constructed with ID, reminderName, content, and reminderType
    // (White box testing)
    @Test
    public void testDeleteAllReminders() {
        User user = new User("testUser", "testPassword");
        userDao.insert(user);

        ReminderList reminderList = new ReminderList("Test List", user.getUserID());
        reminderListDao.insert(reminderList);

        // Creates a reminder
        Reminder reminder = new Reminder(reminderList.getReminderListID(), "Test Reminder", "Test Content", "Test Type");
        reminderDao.insert(reminder);

        List<Reminder> reminders = reminderDao.getAll();

        // Asserting to ensure the initial value set is the same.
        assertEquals(1, reminders.size());

        // Delete All Reminder DB data
        reminderDao.deleteAll();

        // Fetch the list again
        reminders = reminderDao.getAll();

        // Reassert the change to see if the deleteAll is applied.
        assertEquals(0, reminders.size());

        Log.d(TAG, "All Reminder removed.");
    }

}
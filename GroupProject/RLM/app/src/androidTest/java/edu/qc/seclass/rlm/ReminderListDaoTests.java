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
import edu.qc.seclass.rlm.roomLocalDB.ReminderListDao;
import edu.qc.seclass.rlm.roomLocalDB.UserDao;

// White box Testing of ReminderListDao
@RunWith(AndroidJUnit4.class)
public class ReminderListDaoTests {
    private UserDao userDao;
    private ReminderListDao reminderListDao;
    private AppDatabase db;

    // Mocking inMemory DB for unit tests.
    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        userDao = db.userDao();
        reminderListDao = db.reminderListDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    // Checking if a user created a ReminderList properly
    // (White box testing)
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
    // (White box testing)
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
    // (White box testing)
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

    // Checking if getAllListNames returns all reminder list names in string.
    // (White box testing)
    @Test
    public void testGetAllReminderList() {
        User user = new User("testUser", "testPassword");
        userDao.insert(user);

        ReminderList reminderList = new ReminderList("Test List", user.getUserID());
        reminderListDao.insert(reminderList);

        List<String> reminderListsName = reminderListDao.getAllListNames();

        assertEquals(1, reminderListsName.size());
        assertEquals("Test List", reminderListsName.get(0));

        Log.d(TAG, "ReminderList created and getAllListName index worked: " + reminderListsName.get(0));
    }

    // Checking if getAllListNames returns all reminder list names in string.
    // (White box testing)
    @Test
    public void testDeleteAllReminderList() {
        User user = new User("testUser", "testPassword");
        userDao.insert(user);

        ReminderList reminderList = new ReminderList("Test List", user.getUserID());
        reminderListDao.insert(reminderList);

        List<String> reminderListsName = reminderListDao.getAllListNames();

        // Asserting to ensure reminderListName is not empty
        assertEquals(1, reminderListsName.size());

        // Deleting all reminderListDao
        reminderListDao.deleteAll();

        // Asserting if the reminderList is empty after DeleteAll call
        assertEquals(0, reminderListDao.getAll().size());

        Log.d(TAG, "All ReminderList deleted : " + reminderListsName.size());
    }
}
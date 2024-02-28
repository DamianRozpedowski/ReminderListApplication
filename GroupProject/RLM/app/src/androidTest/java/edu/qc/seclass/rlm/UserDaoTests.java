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
import edu.qc.seclass.rlm.roomLocalDB.UserDao;

// White box Testing of User Dao
@RunWith(AndroidJUnit4.class)
public class UserDaoTests {
    private UserDao userDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        userDao = db.userDao();
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
}
package edu.qc.seclass.rlm;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import edu.qc.seclass.rlm.Activities.Home_ReminderLists;
import edu.qc.seclass.rlm.roomLocalDB.AppDatabase;
import edu.qc.seclass.rlm.roomLocalDB.ReminderListDao;
import edu.qc.seclass.rlm.roomLocalDB.UserDao;

public class Home_ReminderListsTest {
    private String createTestUserAndGetId() {
        AppDatabase userDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();
        UserDao userDao = userDatabase.userDao();


        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("testPassword");


        userDao.insert(testUser);


        String userIDString = userDao.getUserID(testUser.getUsername());
        userDatabase.close();

        return userIDString;
    }

    private ActivityScenario<Home_ReminderLists> scenario;

    @Before
    public void setUp() throws Exception {
        Intents.init();

        String testUserId = createTestUserAndGetId();
        Intent intent = new Intent(getApplicationContext(), Home_ReminderLists.class);
        intent.putExtra("USERID", testUserId);
        scenario = ActivityScenario.launch(intent);
    }
    @After
    public void tearDown() throws Exception {
        Intents.release();
    }
    @Test
    public void testCreateReminderListAndCheckIfListViewIsUpdated() {
        // Click on the createListButton
        onView(withId(R.id.createListButton)).perform(click());

        // Add a reminder list name in CreateReminderList activity
        String testReminderListName = "Test List";
        onView(withId(R.id.newReminderListName)).perform(typeText(testReminderListName), ViewActions.closeSoftKeyboard());

        // Click the button to create the reminder list
        onView(withId(R.id.createReminderListButton)).perform(click());


        onView(withId(R.id.remindersListView)).check(matches(isDisplayed()));
    }

    @Test
    public void testCreateReminderListIfSameName() {
        // Click on the createListButton
        onView(withId(R.id.createListButton)).perform(click());

        String testReminderListName = "Test List1";
        onView(withId(R.id.newReminderListName)).perform(typeText(testReminderListName), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.createReminderListButton)).perform(click());

        onView(withId(R.id.createListButton)).perform(click());

        // Try again with same name
        onView(withId(R.id.newReminderListName)).perform(typeText(testReminderListName), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.createReminderListButton)).perform(click());



        onView(withId(R.id.create_reminder_list_header)).check(matches(isDisplayed()));
    }



}

package edu.qc.seclass.rlm;

import static android.app.PendingIntent.getActivity;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import edu.qc.seclass.rlm.Activities.CreateReminder;
import edu.qc.seclass.rlm.Activities.Home_ReminderLists;
import edu.qc.seclass.rlm.Activities.ReminderListView;
import edu.qc.seclass.rlm.roomLocalDB.AppDatabase;
import edu.qc.seclass.rlm.roomLocalDB.ReminderDao;
import edu.qc.seclass.rlm.roomLocalDB.ReminderListDao;
import edu.qc.seclass.rlm.roomLocalDB.UserDao;
import java.time.format.DateTimeFormatter;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;


public class CreateReminderTest {
    private String createTestUser() {
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
    private List<Reminder> getReminders(UUID reminderListID) {
        AppDatabase database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();
        ReminderDao reminderDao = database.reminderDao();
        List<Reminder> reminders = reminderDao.getAllReminders(reminderListID);
        database.close();
        return reminders;
    }

    private ReminderList createTestReminderListAndGetId() {
        AppDatabase reminderListDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();
        ReminderListDao reminderListDao = reminderListDatabase.reminderListDao();

        String testUserId = createTestUser();

        ReminderList testReminderList = new ReminderList();
        testReminderList.setListName("testReminderList");
        testReminderList.setUserIDForeignKey(UUID.fromString(testUserId));

        reminderListDao.insert(testReminderList);
        reminderListDatabase.close();

        return testReminderList;
    }

    private ActivityScenario<Home_ReminderLists> scenario;
    private ReminderList rl = createTestReminderListAndGetId();

    @Before
    public void setUp() throws Exception {

        String userID = createTestUser();


        Intent intent = new Intent(getApplicationContext(), CreateReminder.class);
        intent.putExtra("USERID", userID);
        intent.putExtra("SELECTED_LIST_ID", rl.getReminderListID().toString());
        intent.putExtra("LIST_NAME", rl.getListName());
        scenario = ActivityScenario.launch(intent);
        Intents.init();
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }

    @Test
    public void cancelReminderTest() {
        onView(withId(R.id.cancelButton)).perform(click());
        intended(hasComponent(ReminderListView.class.getName()));
    }

    // Ensure creating a reminder is show in the reminder list home
    @Test
    public void createReminderTest() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String formattedDate = LocalDate.now().format(dateFormatter);
        String formattedTime = LocalTime.now().format(timeFormatter);


        onView(withId(R.id.reminderDate)).perform(typeText(formattedDate), closeSoftKeyboard());
        onView(withId(R.id.reminderTime)).perform(typeText(formattedTime), closeSoftKeyboard());

        onView(withId(R.id.rNameInput)).perform(typeText("testName"), closeSoftKeyboard());
        onView(withId(R.id.rTypeInput)).perform(typeText("testType"), closeSoftKeyboard());

        onView(withId(R.id.dateTimeAlert)).perform(click());



        onView(withId(R.id.addNewButton)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<Reminder> reminders = getReminders(UUID.fromString(rl.getReminderListID().toString()));
        boolean reminderCreated = false;
        for (Reminder r : reminders) {
            if (r.getReminderName().equals("testName") && r.getReminderType().equals("testType")) {
                reminderCreated = true;
                assert(r.getReminderType().equals("testType"));
                assert(r.getAlertForDate().format(dateFormatter).equals(formattedDate));
                assert(r.getAlertForTime().format(timeFormatter).equals(formattedTime));
                break;
            }
        }
    }

    // Testing for editing a reminder based on real time
    @Test
    public void editReminderTest() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String formattedDate = LocalDate.now().format(dateFormatter);
        String formattedTime = LocalTime.now().format(timeFormatter);


        onView(withId(R.id.reminderDate)).perform(typeText(formattedDate), closeSoftKeyboard());
        onView(withId(R.id.reminderTime)).perform(typeText(formattedTime), closeSoftKeyboard());

        onView(withId(R.id.rNameInput)).perform(typeText("testName"), closeSoftKeyboard());
        onView(withId(R.id.rTypeInput)).perform(typeText("testType"), closeSoftKeyboard());

        onView(withId(R.id.dateTimeAlert)).perform(click());



        onView(withId(R.id.addNewButton)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<Reminder> reminders = getReminders(UUID.fromString(rl.getReminderListID().toString()));
        boolean reminderCreated = false;
        for (Reminder r : reminders) {
            if (r.getReminderName().equals("testName") && r.getReminderType().equals("testType")) {
                reminderCreated = true;
                assert(r.getReminderType().equals("testType"));
                assert(r.getAlertForDate().format(dateFormatter).equals(formattedDate));
                assert(r.getAlertForTime().format(timeFormatter).equals(formattedTime));
                break;
            }
        }
    }

    // Ensure there are not issues when date format is wrong Blackbox Testing
    @Test
    public void checkForDateErrorToastonCreation() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String formattedDate = LocalDate.now().format(dateFormatter);
        String formattedTime = LocalTime.now().format(timeFormatter);


        onView(withId(R.id.reminderDate)).perform(typeText(formattedDate), closeSoftKeyboard());
        onView(withId(R.id.reminderTime)).perform(typeText(formattedTime), closeSoftKeyboard());

        onView(withId(R.id.rNameInput)).perform(typeText("testName"), closeSoftKeyboard());
        onView(withId(R.id.rTypeInput)).perform(typeText("testType"), closeSoftKeyboard());

        onView(withId(R.id.dateTimeAlert)).perform(click());



        onView(withId(R.id.addNewButton)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Reminder> reminders = getReminders(UUID.fromString(rl.getReminderListID().toString()));
        boolean reminderCreated = false;
        for (Reminder r : reminders) {
            if (r.getReminderName().equals("testName") && r.getReminderType().equals("testType")) {
                reminderCreated = true;
                assert(r.getReminderType().equals("testType"));
                assert(r.getAlertForDate().format(dateFormatter).equals(formattedDate));
                assert(r.getAlertForTime().format(timeFormatter).equals(formattedTime));
                break;
            }
        }

    }

}

package edu.qc.seclass.rlm;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.anything;

import android.content.Intent;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import edu.qc.seclass.rlm.Activities.CreateReminder;
import edu.qc.seclass.rlm.Activities.Home_ReminderLists;
import edu.qc.seclass.rlm.roomLocalDB.AppDatabase;
import edu.qc.seclass.rlm.roomLocalDB.ReminderDao;
import edu.qc.seclass.rlm.roomLocalDB.ReminderListDao;
import edu.qc.seclass.rlm.roomLocalDB.UserDao;

public class EditReminderTest {private String createTestUser() {
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
    private Reminder createTestReminder() {
        AppDatabase reminderListDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();
        ReminderDao reminderDao = reminderListDatabase.reminderDao();

        String testUserId = createTestUser();
        String testReminderListId = createTestReminderListAndGetId().getReminderListID().toString();

        Reminder testReminder = new Reminder();
        testReminder.setReminderName("testName");
        testReminder.setContent("testDescription");
        testReminder.setAlertForDate(LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))));
        testReminder.setAlertForTime(LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))));
        testReminder.setReminderListForeignKey(UUID.fromString(testReminderListId));

        reminderDao.insert(testReminder);
        reminderListDatabase.close();

        return testReminder;
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
    public void testEditingReminder(){
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

        onData(anything()).inAdapterView(withId(R.id.remindersListView)).atPosition(1).perform(click());


        onView(withId(R.id.rNameInput)).check(matches(withText("testName")));
        onView(withId(R.id.rTypeInput)).check(matches(withText("testType")));
        onView(withId(R.id.reminderTime)).check(matches(withText(formattedTime)));


        onView(withId(R.id.rNameInput)).perform(replaceText("updateTest"), closeSoftKeyboard());
        onView(withId(R.id.rTypeInput)).perform(replaceText("updateTest1"), closeSoftKeyboard());

        formattedDate = LocalDate.now().format(dateFormatter);
        formattedTime = LocalTime.now().format(timeFormatter);
        onView(withId(R.id.reminderDate)).perform(replaceText(formattedDate), closeSoftKeyboard());
        onView(withId(R.id.reminderTime)).perform(replaceText(formattedTime), closeSoftKeyboard());


        onView(withId(R.id.saveButton)).perform(click());


        onData(anything()).inAdapterView(withId(R.id.remindersListView)).atPosition(1).perform(click());
        onView(withId(R.id.rNameInput)).check(matches(withText("updateTest")));
        onView(withId(R.id.rTypeInput)).check(matches(withText("updateTest1")));
        onView(withId(R.id.reminderTime)).check(matches(withText(formattedTime)));

    }
    @Test
    public void testCancelEditingReminder() {
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

        onData(anything()).inAdapterView(withId(R.id.remindersListView)).atPosition(1).perform(click());
        onView(withId(R.id.cancelButton)).perform(click());
        onView(withId(R.id.withinReminderHeader)).check(matches(isDisplayed()));


    }
    @Test
    public void checkForDateErrorToastOnEditing() {
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
    @Test
    public void deleteReminder(){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Get the current date and time
        String formattedDate = LocalDate.now().format(dateFormatter);
        String formattedTime = LocalTime.now().format(timeFormatter);

        // Create a new reminder
        onView(withId(R.id.reminderDate)).perform(typeText(formattedDate), closeSoftKeyboard());
        onView(withId(R.id.reminderTime)).perform(typeText(formattedTime), closeSoftKeyboard());
        onView(withId(R.id.rNameInput)).perform(typeText("testName"), closeSoftKeyboard());
        onView(withId(R.id.rTypeInput)).perform(typeText("testType"), closeSoftKeyboard());
        onView(withId(R.id.dateTimeAlert)).perform(click());
        onView(withId(R.id.addNewButton)).perform(click());

        // Wait for reminder to be created
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Select the edited reminder from the list
        onData(anything()).inAdapterView(withId(R.id.remindersListView)).atPosition(1).perform(click());

        onView(withId(R.id.deleteButton)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Verify the reminder is deleted and the list is empty
        onView(withId(R.id.remindersListView)).check(matches(hasChildCount(0)));
    }


}

package edu.qc.seclass.rlm;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.espresso.intent.Intents;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Rule;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import edu.qc.seclass.rlm.Activities.Home_ReminderLists;
import edu.qc.seclass.rlm.Activities.MainActivity;


public class MainActivityTest {
    public void allowNotification() {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        try {
            UiObject allowButton = device.findObject(new UiSelector()
                    .clickable(true)
                    .checkable(false)
                    .text("Allow"));

            if (allowButton.exists() && allowButton.isEnabled()) {
                allowButton.click();
            }
        } catch (UiObjectNotFoundException e) {

        }
    }
    @Test
    public void inputNotFilled_returnsFalse() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                User testUser = new User();
                testUser.setUsername("");
                testUser.setPassword("");
                assertFalse(activity.inputNotFilled(testUser));
            });
        }
    }
    @Test
    public void inputFiled_returnsTrue() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                User testUser = new User();
                testUser.setUsername("testUser");
                testUser.setPassword("testPassword");
                assertTrue(activity.inputNotFilled(testUser));
            });
        }
    }
    @Test
    public void inputFilled_specialChars(){
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                User testUser = new User();
                testUser.setUsername("testUser!@#$%^&*()");
                testUser.setPassword("testPassword!@#$%^&*()");
                assertTrue(activity.inputNotFilled(testUser));
            });
        }
    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        Intents.init(); // Initialize Espresso
    }

    @After
    public void tearDown() throws Exception {
        Intents.release(); // Release Espresso
    }

    @Test
    public void testRegisterButton_staysInMain() {
        allowNotification();
        onView(withId(R.id.username)).perform(typeText("testUser"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("testPassword"), closeSoftKeyboard());


        onView(withId(R.id.register)).perform(click());
        onView(withId(R.id.register)).perform(click());

        // Should stay in main and with that the appName banner should be up at the top
        onView(withId(R.id.appName)).check(matches(isDisplayed()));
    }
    @Test
    public void testLoginWithoutRegistering(){
        allowNotification();
        onView(withId(R.id.username)).perform(typeText("testUser"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("testPassword"), closeSoftKeyboard());

        onView(withId(R.id.login)).perform(click());

        // Should stay in main and with that the appName banner should be up at the top
        onView(withId(R.id.appName)).check(matches(isDisplayed()));
    }
    @Test
    public void testRegisterWithoutFilling(){
        allowNotification();
        onView(withId(R.id.register)).perform(click());
        onView(withId(R.id.appName)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginButton_clickOpensHomeActivity() {
        allowNotification();
        // Replace 'R.id.username' and 'R.id.password' with the actual IDs of the username and password input fields
        onView(withId(R.id.username)).perform(typeText("testUser"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("testPassword"), closeSoftKeyboard());

        // Now click the login button
        onView(withId(R.id.register)).perform(click());
        onView(withId(R.id.login)).perform(click());

        // Check if the Home_ReminderLists activity is opened
        intended(hasComponent(Home_ReminderLists.class.getName()));
    }
}


package com.example.sufferqr;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.DaggerBaseLayerComponent;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * user interface testing
 */
public class userUITest {

    private Solo solo;

    @Rule
    public ActivityTestRule<UserProfile> rule = new ActivityTestRule<>(UserProfile.class,
            true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception something
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * close activity after each test
     * @throws Exception if not correctly closed
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

    /**
     * Gets the Activity
     *
     * @throws Exception something
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }


    /**
     * This method is used to test if user can use this button
     * to get to the edit profile page in the user profile
     * @author Zhiyu
     */
    @Test
    public void testEditButton(){
        solo.assertCurrentActivity("Wrong Activity", UserProfile.class);
        onView(withId(R.id.changeToEditProfile_UserProfile)).perform(click());
        solo.assertCurrentActivity("Wrong Activity", EditProfile.class);
    }

    /**
     * This method is used to test the cancel button in edit profile
     * if the user will like to get back to the user profile instead of
     * making changes to their information, this button will take the user
     * back to user profile without changing anything
     * @author Zhiyu
     */
    @Test
    public void testCancel(){
        solo.assertCurrentActivity("Wrong Activity", UserProfile.class);
        onView(withId(R.id.changeToEditProfile_UserProfile)).perform(click());
        solo.assertCurrentActivity("Wrong Activity", EditProfile.class);
        solo.clickOnButton("Cancel");
        solo.assertCurrentActivity("Wrong Activity", UserProfile.class);
    }

    /**
     * This method is used to test if user can successfully delete their account
     * and have the ability to create a new one
     *
     * (Do not test this at first, it will actually deletes the account)
     *
     * @author Zhiyu
     */
    @Test
    public void testDelete(){
        solo.assertCurrentActivity("Wrong Activity", UserProfile.class);
        onView(withId(R.id.changeToEditProfile_UserProfile)).perform(click());
        solo.assertCurrentActivity("Wrong Activity", EditProfile.class);
        solo.clickOnButton("Delete Account");
        solo.assertCurrentActivity("Wrong Activity", RegisterPage.class);
    }

    /**
     * This method is used to if user can input into the editable text
     * in order to change their information
     * @author Zhiyu
     */
    @Test
    public void testEditInput(){
        solo.assertCurrentActivity("Wrong Activity", UserProfile.class);
        onView(withId(R.id.changeToEditProfile_UserProfile)).perform(click());
        solo.clearEditText((EditText) solo.getView(R.id.userName_editProfile));
        solo.enterText((EditText) solo.getView(R.id.userName_editProfile), "HuangRui");
        assertTrue(solo.waitForText("HuangRui", 1, 3000));

        solo.clearEditText((EditText) solo.getView(R.id.userEmail_editProfile));
        solo.enterText((EditText) solo.getView(R.id.userEmail_editProfile), "rHuang@uablerta.ca");
        assertTrue(solo.waitForText("rHuang@uablerta.ca", 1, 3000));
    }

    /**
     * This method is used to detect if user have changed their information
     * and selected apply. The page will go back to the dash board if
     * the change have been made
     * @author Zhiyu
     */
    @Test
    public void testApplyChanges(){
        // open edit user profile
        solo.assertCurrentActivity("Wrong Activity", UserProfile.class);
        onView(withId(R.id.changeToEditProfile_UserProfile)).perform(click());
        // change username
        solo.clearEditText((EditText) solo.getView(R.id.userName_editProfile));
        solo.enterText((EditText) solo.getView(R.id.userName_editProfile), "HuangRui");
        assertTrue(solo.waitForText("HuangRui", 1, 3000));
        // change user email
        solo.clearEditText((EditText) solo.getView(R.id.userEmail_editProfile));
        solo.enterText((EditText) solo.getView(R.id.userEmail_editProfile), "rHuang@uablerta.ca");
        assertTrue(solo.waitForText("rHuang@uablerta.ca", 1, 3000));

        // perform changes
        onView(withId(R.id.allow_email)).perform(click());
        onView(withId(R.id.allow_scan_record)).perform(click());
        onView(withId(R.id.allow_qrid)).perform(click());

        solo.clickOnButton("Apply");
        solo.assertCurrentActivity("Wrong Activity", DashBoard.class);

    }


}

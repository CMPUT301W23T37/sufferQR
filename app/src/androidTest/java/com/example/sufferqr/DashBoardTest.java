package com.example.sufferqr;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Dashboard test
 */
public class DashBoardTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<DashBoard> rule = new ActivityTestRule<>(DashBoard.class, true, true);

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
     * Gets the Activity
     *
     * @throws Exception something
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Closes the activity after each test
     * @throws Exception something
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

    /**
     * Tests whether DashBoard switches to LeaderBoard by clicking your rank box
     */
    @Test
    public void checkSwitchRank(){
        solo.assertCurrentActivity("Wrong Activity", DashBoard.class);
        solo.clickOnView(solo.getView(R.id.blue_rec_t));
        solo.assertCurrentActivity("Wrong Activity", LeaderBoard.class);
    }

    /**
     * Tests whether DashBoard switches to UserProfile by clicking highest score box
     */
    @Test
    public void checkSwitchHigh(){
        solo.assertCurrentActivity("Wrong Activity", DashBoard.class);
        solo.clickOnView(solo.getView(R.id.blue_rec_m));
        solo.assertCurrentActivity("Wrong Activity", UserProfile.class);
    }

    /**
     * Tests whether DashBoard switches to ScanHistory by clicking last scan box
     */
    @Test
    public void checkSwitchLast(){
        solo.assertCurrentActivity("Wrong Activity", DashBoard.class);
        solo.clickOnView(solo.getView(R.id.blue_rec_b));
        solo.assertCurrentActivity("Wrong Activity", ScanHistory.class);
    }

    /**
     * Tests whether DashBoard switches to ScanCode by clicking the qr code image
     */
    @Test
    public void checkSwitchCamera(){
        solo.assertCurrentActivity("Wrong Activity", DashBoard.class);
        solo.clickOnView(solo.getView(R.id.qr_image));
        solo.assertCurrentActivity("Wrong Activity", ScanCode.class);
    }

    /**
     * Tests whether DashBoard switches to UserProfile by clicking the top right info box
     */
    @Test
    public void checkSwitchRec(){
        solo.assertCurrentActivity("Wrong Activity", DashBoard.class);
        solo.clickOnView(solo.getView(R.id.blue_rec));
        solo.assertCurrentActivity("Wrong Activity", UserProfile.class);
    }
}

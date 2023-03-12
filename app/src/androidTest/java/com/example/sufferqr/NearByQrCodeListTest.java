package com.example.sufferqr;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class NearByQrCodeListTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<nearbyQrCodeList> rule = new ActivityTestRule<>(nearbyQrCodeList.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets the Activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

    /**
     *  test whether go back to the last activity after click the back button
     */
    @Test
    public void TestBackButton(){
        solo.assertCurrentActivity("Wrong Activity", nearbyQrCodeList.class);
        solo.clickOnView(solo.getView(R.id.back_button));
        solo.assertCurrentActivity("Wrong Activity", MapsActivity.class);
    }




}

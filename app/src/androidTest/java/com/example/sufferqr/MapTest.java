package com.example.sufferqr;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.location.LocationManager;
import android.view.KeyEvent;
import android.widget.EditText;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.DaggerBaseLayerComponent;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.navigation.NavigationView;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * nearby map testing
 */
public class MapTest {

    private Solo solo;
    

    @Rule
    public ActivityTestRule<MapsActivity> rule = new ActivityTestRule<>(MapsActivity.class,
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
     * Gets the Activity
     *
     * @throws Exception something
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }


    /**
     * test whether go to the list activity after click the list button
     * @throws Exception something
     */
    @Test
    public void TestListButton() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", MapsActivity.class);
        solo.clickOnView(solo.getView(R.id.nearbylist));
        solo.assertCurrentActivity("Wrong Activity", nearbyQrCodeList.class);
    }


}

package com.example.sufferqr;

import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class QRDetailHistoryTest {
    private Solo solo;

    String QRname="scroll";
    String User= "example";
    String mode = "modified";
    String mode2= "visitor";

    @Rule
    public ActivityTestRule<QRDetailActivity> rule = new ActivityTestRule<QRDetailActivity>(QRDetailActivity.class, true, true){
        @Override
        protected Intent getActivityIntent() {
            super.getActivityIntent();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.putExtra("user",User);
            intent.putExtra("qrID",QRname);
            intent.putExtra("mode","modified");
            return intent;
        }
    };

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
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

    @Test
    public void GeneralTest(){

    }

}

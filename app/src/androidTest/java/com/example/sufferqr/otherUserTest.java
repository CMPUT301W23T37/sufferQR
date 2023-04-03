package com.example.sufferqr;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static java.util.regex.Pattern.matches;

import android.app.Activity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class otherUserTest {
    private Solo solo;
    Activity mActivity;

    @Rule
    public ActivityTestRule<LeaderBoard> rule = new ActivityTestRule<>(LeaderBoard.class,
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
     * To test if other user can be successfully opened
     */
    @Test
    public void testActivity(){
        solo.assertCurrentActivity("Wrong Activity", LeaderBoard.class);
        onView(withId(R.id.ranks_listview)).perform(click());
        solo.assertCurrentActivity("Wrong Activity", OtherUser.class);
    }

    /**
     * This method is used to test to see if user can check other's
     * scanned code while viewing their profile
     */
    @Test
    public void testScanRecord(){
        solo.assertCurrentActivity("Wrong Activity", LeaderBoard.class);
        onView(withId(R.id.ranks_listview)).perform(click());
        solo.assertCurrentActivity("Wrong Activity", OtherUser.class);
        onView(withId(R.id.other_user_profile_list)).perform(click());
        solo.assertCurrentActivity("Wrong Activity", QRQuickViewScrollingActivity.class);
    }

    TextView email;
    TextView qrId;
    Chip highest;


    /**
     * This method is used to test user privacy where if they turn on
     * or off their privacy settings in edit profile, does other user still
     * can view their information
     */
    @Test
    public void testPrivacy(){
        solo.assertCurrentActivity("Wrong Activity", LeaderBoard.class);
        onView(withId(R.id.ranks_listview)).perform(click());
        solo.assertCurrentActivity("Wrong Activity", OtherUser.class);



        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userInfo = db.collection("Player").document("605270421b975431");
        userInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                User u = document.toObject(User.class);
                boolean viewEmail;
                boolean viewQr;
                boolean viewRecord;


                viewEmail = u.getAllowViewEmail();
                email = (TextView) solo.getView(R.id.userEmail_otherProfile);
                if(!viewEmail){
                    assertEquals(email.getText(), "");
                }else {
                    assertEquals(email.getText(), String.valueOf(u.getEmail()));
                }


                viewQr = u.getAllowViewQrid();
                qrId = (TextView) solo.getView(R.id.userQRid_otherProfile);
                if(!viewQr){
                    assertEquals(qrId.getText(), "");
                }else {
                    assertEquals(qrId.getText(), String.valueOf(u.getQRid()));
                }

                viewRecord = u.getAllowViewScanRecord();
                highest = (Chip) solo.getView(R.id.highest_otherProfile);
                if(!viewRecord){
                    assertEquals(highest.getText(), "N/A");
                } else{
                    assertEquals(highest.getText(), "Highest: " + String.valueOf(u.getHighestScore()));
                }

            }
        });
    }
}

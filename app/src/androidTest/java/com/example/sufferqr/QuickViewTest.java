package com.example.sufferqr;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * qrcode details testing on each fragment
 */
@RunWith(AndroidJUnit4.class)
public class QuickViewTest {
    private Solo solo;

    String QRname="scroll";
    String User= "example";
    String mode = "modified";
    String mode2= "visitor";
    FirebaseFirestore db;

    /**
     * set up test rules
     */
    @Rule
    public ActivityTestRule<QRQuickViewScrollingActivity> rule = new ActivityTestRule<QRQuickViewScrollingActivity>(QRQuickViewScrollingActivity.class, true, true){
        @Override
        protected Intent getActivityIntent() {
            super.getActivityIntent();
            Map<String,Object> m = new HashMap<>();
            m.put("LocationAddress","10039 108 Street NW, Edmonton, Alberta T5J 0L3, Canada");
            m.put("LocationExist",true);
            m.put("LocationLatitude",53.5399518);
            m.put("LocationLongitude",-113.5061266);
            m.put("LocationName","108 Street NW");
            m.put("QRVisual","OOOOOOOOOOOOOOOOOOOOO                  OO                  OO                  OO  ~~~~~~   ~~~~~~ OO                  OO   000   000      OO  ..       ..     OO       *          OO                  OO  [==========]    OO                  OO      VVV         OO                  OO                  OOOOOOOOOOOOOOOOOOOOO");
            m.put("QRhash","9a7cd5efda286fbcdd26f89e64a360c560208248b301ff49ad670cb5552790ff");
            m.put("QRname","TestCase");
            m.put("QRpath","image/9fd4446da686323d_20230323_161359.jpg");
            m.put("allowViewScanRecord",true);
            m.put("date","2023-03-23");
            m.put("email","phone@1+.ca");
            m.put("imageExist",true);
            m.put("points",4496);
            m.put("time",new Date());
            m.put("user","9fd4446da686323d");
            m.put("userName","phone");

            Bundle bundle = new Bundle();
            for (Map.Entry<String, Object> entry : m.entrySet()) {
                bundle.putString(entry.getKey(),String.valueOf(entry.getValue()));
            }

            dataSetUp(m);


            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.putExtra("localUser","9fd4446da686323d");
            intent.putExtra("qrID","TestCase");
            intent.putExtra("MapData",bundle);
            return intent;
        }
    };

    private void dataSetUp(Map<String,Object> m) {
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("GameQrCode");
        collectionReference.document("TestCase").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                           if(task.getResult().exists()){
                               collectionReference.document("TestCase").set(m);
                               collectionReference.document("TestCase").collection("Comment").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                   @Override
                                   public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                       for (DocumentSnapshot documentSnapshot:value.getDocuments()){
                                           collectionReference.document("TestCase")
                                                   .collection("Comment").document( documentSnapshot.getId()).delete();
                                       }
                                   }
                               });
                           }else {
                               collectionReference.document("TestCase").set(m);
                           }
                        }
                    }
        });
    };

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception something
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        db = FirebaseFirestore.getInstance();

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
     * General page testing
     *
     */
    @Test
    public void GeneralTest(){
        solo.assertCurrentActivity("Wrong Activity", QRQuickViewScrollingActivity.class);

        solo.waitForText("TestCase",1,1000);
        solo.waitForText("phone",1,1000);
        solo.waitForText("2023-03-23",1,1000);
        solo.scrollDown();

        solo.waitForText("108 Street NW",1,1000);
        solo.waitForText("10039 108 Street NW, Edmonton, Alberta T5J 0L3, Canada",1,1000);
        // QRvisual
    }


    /**
     * Image page testing
     *
     */
    @Test
    public void SameQRTest(){
        solo.assertCurrentActivity("Wrong Activity", QRQuickViewScrollingActivity.class);



        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("GameQrCode");
        collectionReference.whereEqualTo("QRhash", "9a7cd5efda286fbcdd26f89e64a360c560208248b301ff49ad670cb5552790ff")
                .whereEqualTo("allowViewScanRecord",true)
                .orderBy("points", Query.Direction.DESCENDING)
                .addSnapshotListener( new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null && !value.isEmpty()) {
                            // Get the number of scans
                            final QRQuickViewScrollingActivity activity = rule.getActivity();

                            ViewPager viewPager = (ViewPager) solo.getView(R.id.qr_quick_viewPager);
                            viewPager.setCurrentItem(1);

                            for (DocumentSnapshot doc : value.getDocuments()) {
                                String qrName = String.valueOf(Objects.requireNonNull(doc.getData()).get("userName"));
                                solo.waitForText(qrName,1,1000);
                            }

                        }
                    }
                });
    }


    /**
     * Location page testing
     *
     */
    @Test
    public void CommentTest(){
        solo.assertCurrentActivity("Wrong Activity", QRQuickViewScrollingActivity.class);

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final QRQuickViewScrollingActivity activity = rule.getActivity();
                    FragmentManager fm = activity.getFragmentManager();
                    ViewPager viewPager = (ViewPager)  solo.getCurrentActivity().findViewById(R.id.qr_quick_viewPager);
                    viewPager.setCurrentItem(2);

                    FloatingActionButton fb1=(FloatingActionButton) solo.getCurrentActivity().findViewById(R.id.add_comment_fragment);
                    solo.clickOnView(fb1);

                    solo.assertCurrentActivity("Wrong Activity", CommentPage.class);

                    EditText editText= (EditText) solo.getView(R.id.comment_content);
                    editText.setText("I love the QRcode");
                    solo.clickOnText("ADD");

                    solo.assertCurrentActivity("Wrong Activity", QRQuickViewScrollingActivity.class);

                    solo.clickOnText("I love the QRcode");
                    solo.assertCurrentActivity("Wrong Activity", CommentPage.class);
                    solo.clickOnText("DELETE");
                    solo.assertCurrentActivity("Wrong Activity", QRQuickViewScrollingActivity.class);
                }
            });
        } catch (Throwable e) {
            System.out.println(e);
        }

    }

}

package com.example.sufferqr;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

/**
 * qrcode details testing on each fragment
 */
@RunWith(AndroidJUnit4.class)
public class QRDetailHistoryTest {
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
        solo.assertCurrentActivity("Wrong Activity", QRDetailActivity.class);
        final CollectionReference collectionReferenceDest = db.collection("GameQrCode");
        collectionReferenceDest.document(QRname).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                assertTrue(task.isSuccessful());
                DocumentSnapshot document = task.getResult();
                assertTrue(document.exists());

                solo.waitForText((String)document.get("QRname"),1,1000);
                solo.waitForText((String) (String)document.get("date"),1,1000);
                Object points = document.get("points");
                solo.waitForText(String.valueOf(points),1,1000);
                solo.waitForText((String)document.get("QVisual"),1,1000);
            }
        });

    }


    /**
     * Image page testing
     *
     */
    @Test
    public void ImageTest(){
        solo.assertCurrentActivity("Wrong Activity", QRDetailActivity.class);
        final CollectionReference collectionReferenceDest = db.collection("GameQrCode");
        collectionReferenceDest.document(QRname).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                assertTrue(task.isSuccessful());
                DocumentSnapshot document = task.getResult();
                assertTrue(document.exists());

                final QRDetailActivity activity = rule.getActivity();

                ViewPager viewPager = (ViewPager) solo.getView(R.id.qrdetail_view_pager);
                viewPager.setCurrentItem(1);

                Boolean bool = (Boolean) document.get("imageExist");
                SwitchMaterial switchMaterial = (SwitchMaterial) solo.getView(R.id.qr_detail_image_enable_switch);
                assertEquals(bool,switchMaterial.isChecked());

            }
        });

    }


    /**
     * Location page testing
     *
     */
    @Test
    public void LocationTest(){
        solo.assertCurrentActivity("Wrong Activity", QRDetailActivity.class);
        final CollectionReference collectionReferenceDest = db.collection("GameQrCode");
        collectionReferenceDest.document(QRname).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                assertTrue(task.isSuccessful());
                DocumentSnapshot document = task.getResult();
                assertTrue(document.exists());

                final QRDetailActivity activity = rule.getActivity();

                ViewPager viewPager = (ViewPager) solo.getView(R.id.qrdetail_view_pager);
                viewPager.setCurrentItem(2);

                Boolean bool = (Boolean) document.get("LocationExist");
                SwitchMaterial switchMaterial = (SwitchMaterial) solo.getView(R.id.qr_detail_location_enable_switch);
                assertEquals(bool,switchMaterial.isChecked());

                if (bool){
                    Double d1= (Double) document.get("LocationLatitude");
                    Double d2= (Double) document.get("LocationLongitude");
                    solo.waitForText(String.valueOf(d1),1,1000);
                    solo.waitForText(String.valueOf(d2),1,1000);
                    solo.waitForText((String) document.get("LocationName"),1,1000);
                    solo.waitForText((String) document.get("LocationAddress"),1,1000);
                }

            }
        });

    }

}

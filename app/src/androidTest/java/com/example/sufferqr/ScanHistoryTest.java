package com.example.sufferqr;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.sufferqr.ui.main.ScanHistoryQRRecord;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * qrcode scan history test
 */
public class ScanHistoryTest {
    private Solo solo;


    String User= "example";

    FirebaseFirestore db;

    /**
     * test rules
     *
     */
    @Rule
    public ActivityTestRule<ScanHistory> rule = new ActivityTestRule<ScanHistory>(ScanHistory.class, true, true){
        @Override
        protected Intent getActivityIntent() {
            super.getActivityIntent();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.putExtra("user",User);
            return intent;
        }
    };

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception it throwed it means solo did not run
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        db = FirebaseFirestore.getInstance();

    }

    /**
     * Closes the activity after each test
     * @throws Exception solo did not go success
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

    /**
     * compare if file loading is the same
     *
     */
    @Test
    public void launch(){
        final CollectionReference collectionReference = db.collection("GameQrCode");
        collectionReference.whereEqualTo("user",User).orderBy("time", Query.Direction.DESCENDING).
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            System.err.println("Listen failed: " + error);
                        }
                        if (value != null && !value.isEmpty()){
                            int cc = 0;
                            for (DocumentSnapshot doc : value.getDocuments()) {
                                String qrName = String.valueOf(doc.getData().get("QRname"));
                                String points = String.valueOf(doc.getData().get("points"));
                                String sDate = String.valueOf(doc.getData().get("date"));
                                String sAddress = String.valueOf(doc.getData().get("LocationName"));
                                solo.waitForText(qrName, 1, 1000);
                                solo.waitForText(points, 1, 1000);
                                solo.waitForText(sDate, 1, 1000);
                                solo.waitForText(sAddress, 1, 1000);


                            }
                        }

                    }
        });

    }
}

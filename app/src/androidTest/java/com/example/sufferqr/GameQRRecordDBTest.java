package com.example.sufferqr;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.Settings;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.utils.widget.MockView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class GameQRRecordDBTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);



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

    /**
     * add in data setup
     *
     */
    public HashMap<String, Object> makeup(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        HashMap<String, Object> data = new HashMap<>();
        data.put("Name", "testcase");
        data.put("LocationExist",false);
        data.put("LocationLatitude",0);
        data.put("LocationLongitude",0);
        data.put("LocationName","name");
        data.put("LocationAddress","");
        data.put("QRname","testing"+timeStamp);
        data.put("QRpath","");
        data.put("QRtext","");
        data.put("QVisual","");
        data.put("date","2023-03-01");
        data.put("imageExist",false);
        data.put("points",0);
        data.put("time",new Date());
        data.put("user","testing");
        return data;
    }

    /**
     * add in new data
     *
     */
    @Test
    public void AddInNew(){
        HashMap<String, Object> data = makeup();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        GameQrRecordDB gameQrRecordDB = new GameQrRecordDB();
        gameQrRecordDB.CheckUnique("testing"+timeStamp,true,data );
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        } catch (Exception e){

        }
        final CollectionReference collectionReferenceDest = db.collection("GameQrCode");
        // check if id is unique in the FameQr datavase
        collectionReferenceDest.document("testing"+timeStamp).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                assertTrue(task.isSuccessful());
                DocumentSnapshot document = task.getResult();
                assertTrue(document.exists());
                gameQrRecordDB.DelteQrInfo("testing"+timeStamp,data);
            }
        });
    }

    /**
     * check if a file name exist
     *
     */
    @Test
    public void checkunique(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReferenceDest = db.collection("GameQrCode");
        // check if id is unique in the FameQr datavase
        collectionReferenceDest.count().get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                assertTrue(task.isSuccessful());
                AggregateQuerySnapshot snapshot = task.getResult();
                int count = (int) snapshot.getCount();
                assertNotNull(count);

                // testing ability of random generate
                HashMap<String, Object> data = makeup();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
                GameQrRecordDB gameQrRecordDB = new GameQrRecordDB();
                gameQrRecordDB.NewQRWithRandomGeneratedWords("",data);
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    //throw new RuntimeException(e);
                } catch (Exception e){

                }
                collectionReferenceDest.count().get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                                assertTrue(task.isSuccessful());
                                AggregateQuerySnapshot snapshot = task.getResult();
                                int count2 = (int) snapshot.getCount();
                                assertNotNull(count2);
                                assertEquals(count+1,count2);

                            }
                        });

                    }
                });


    }


    /**
     * check function delete
     *
     */
    @Test
    public void checkDelete(){
        HashMap<String, Object> data = makeup();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        GameQrRecordDB gameQrRecordDB = new GameQrRecordDB();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReferenceDest = db.collection("GameQrCode");
        collectionReferenceDest
                .document("testing"+timeStamp).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        gameQrRecordDB.DelteQrInfo("testing"+timeStamp,data);
                        try {
                            TimeUnit.SECONDS.sleep(3);
                            collectionReferenceDest.document("testing"+timeStamp).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    assertTrue(task.isSuccessful());
                                    DocumentSnapshot document = task.getResult();
                                    assertFalse(document.exists());
                                }
                            });
                        } catch (InterruptedException e) {
                            //throw new RuntimeException(e);
                        } catch (Exception e){

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        //listener.onSendingUpdate("Data could not be added!",false);
                    }
                });
    }


    /**
     * test of data changeds
     *
     */
    @Test
    public void checkOnchange(){
        HashMap<String, Object> data = makeup();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        GameQrRecordDB gameQrRecordDB = new GameQrRecordDB();
        gameQrRecordDB.CheckUnique("testing"+timeStamp,true,data );
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        } catch (Exception e){

        }
        data.replace("QVisual","wow you can really dance");
        gameQrRecordDB.ChangeQrInfo("testing"+timeStamp,data);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
        } catch (Exception e){

        }
        final CollectionReference collectionReferenceDest = db.collection("GameQrCode");
        // check if id is unique in the FameQr datavase
        collectionReferenceDest.document("testing"+timeStamp).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                assertTrue(task.isSuccessful());
                DocumentSnapshot document = task.getResult();
                assertTrue(document.exists());
                String ee = (String) document.get("QVisual");
                assertEquals(ee,"wow you can really dance");
                gameQrRecordDB.DelteQrInfo("testing"+timeStamp,data);
            }
        });


    }

}

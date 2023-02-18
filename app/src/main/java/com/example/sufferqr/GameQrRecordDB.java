package com.example.sufferqr;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * This is a class that connect with database GameQRRecord
 */
public class GameQrRecordDB {
    private FirebaseFirestore db;


    public GameQrRecordDB(HashMap<String, String> data) {
        //getRandomUniqueString();
        // do not enable unless necessory
        // https://blog.csdn.net/u011435933/article/details/117419082
        //UpdateRandomWord("/data/data/com.example.sufferqr/cache/wordlist.txt",50);
        String nowname ="";
        getRandomUniqueString(data,nowname);

    }

    public GameQrRecordDB(HashMap<String, String> dat, String Myname){
        CheckUnique(Myname,false,dat);
    }

    public GameQrRecordDB(int amount){
        try {
            UpdateRandomWord(50);
        } catch (IOException e) {
            System.out.println("io open fail");
        }
    }

    /**
     * This given random name,and send to validate,if conflict,length will extent
     */
    public void getRandomUniqueString(HashMap<String, String> data, final String MyName) {
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReferenceOrigin = db.collection("newRand");

        int words = 2;
        String TAG = null;
        // asynchronously retrieve all documents
        collectionReferenceOrigin.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<String> name = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //Log.d(TAG,document.getId() + " => " + document.getData());

                        String tt = document.getId();
                        name.add(tt);
                    }
                    String MyName2 = MyName;
                    Random rand = new Random();
                    for (int i = 0; i < words; i++) {
                        int iRand = (int) rand.nextInt(name.size());
                        MyName2 = MyName2 + name.get(iRand);
                    }
                    boolean unique = false;
                    CheckUnique(MyName2,true,data);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }

            }
        });
    }

    /**
     * check if user id if unique if it is ,push it to db
     * @return
     * null
     */
    public void CheckUnique(String name,boolean RetryIfFail,HashMap<String, String> data) {
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReferenceDest = db.collection("GameQr");
        // check if id is unique in the FameQr datavase
        collectionReferenceDest.document(name).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    // if result exist
                    if (document.exists()) {
                        if (RetryIfFail==true){
                            getRandomUniqueString(data,name);
                        }else {
                            // put a toast message
                            System.out.println("repeat exist");
                        }
                    } else {
                        //  put success staff here.
                        String TAG=null;
                        collectionReferenceDest
                                .document(name).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // These are a method which gets executed when the task is succeeded
                                        Log.d(TAG, "Data has been added successfully!");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // These are a method which gets executed if there’s any problem
                                        Log.d(TAG, "Data could not be added!" + e.toString());
                                    }
                                });
                    }
                   // when sucess exit
                } else {
                    System.out.println("fail to connect");
                }
            }
        });
    }

    /**
     * Create a random list on the database on the cloud
     * @return
     * null
     */
    public void UpdateRandomWord(int number) throws IOException {
        db = FirebaseFirestore.getInstance();
        // file location gs://sufferqr-65324.appspot.com/nwordlist.txt //replace this on main repo
        // Create a storage reference from our app
        int skip=1000/number;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        // referencing about 1000 words documents
        StorageReference gsReference = storage.getReferenceFromUrl("gs://sufferqr.appspot.com/nwordlist.txt");

        File localFile = File.createTempFile("rnd", "txt");
        // fetch file online
        gsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                // random words list credit
                // https://github.com/staceybellerose/RandomWordGenerator/tree/main/wordlists/12dicts/processed
                final CollectionReference collectionReference = db.collection("RandomName");
                // open file
                FileInputStream input = null;
                BufferedReader bufReader = null;
                //tried open file
                try {
                    input = new FileInputStream(localFile);
                    bufReader = new BufferedReader(new InputStreamReader(input));
                    // grab context
                    int count = 0;
                    // grab line in file
                    String RadomName;
                    while ((RadomName = bufReader.readLine()) != null) {
                        count++;
                        System.out.println(RadomName);
                        if (count % 100 == 0) {
                            // read the words into db in every "skip" line
                            HashMap<String, String> data = new HashMap<>();
                            data.put("Name", RadomName);
                            String TAG = "example";
                            final String docname = RadomName;
                            collectionReference
                                    .document(docname).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // These are a method which gets executed when the task is succeeded
                                            Log.d(TAG, "Data has been added successfully!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // These are a method which gets executed if there’s any problem
                                            Log.d(TAG, "Data could not be added!" + e.toString());
                                        }
                                    });
                        }
                    }
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                    System.out.println("file open fail");
                } catch (IOException e){
                    System.out.println(e.toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });



    }

}

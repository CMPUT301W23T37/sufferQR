package com.example.sufferqr;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firestore.v1.WriteResult;

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
    private FirebaseStorage storage;


    public GameQrRecordDB() {
        //getRandomUniqueString();
        // do not enable unless necessory
        // https://blog.csdn.net/u011435933/article/details/117419082
        //UpdateRandomWord("/data/data/com.example.sufferqr/cache/wordlist.txt",50);
        db = FirebaseFirestore.getInstance();
    }


    /**
     * check if user id if unique if it is ,push it to db
     * @return
     * null
     */
    public void CheckUnique(String name,boolean RetryIfFail,HashMap<String, Object> data) {
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReferenceDest = db.collection("GameQrCode");
        // check if id is unique in the FameQr datavase
        collectionReferenceDest.document(name).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    // if result exist
                    if (document.exists()) {
                        if (RetryIfFail==true){
                            NewQRWithRandomGeneratedWords(name,data);
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
     * Download a random name text from clound and random slect one and push new qr code to database
     * @return
     * null
     */
    public void NewQRWithRandomGeneratedWords(String name,HashMap<String, Object> data)  {
        db = FirebaseFirestore.getInstance();
        // file location gs://sufferqr-65324.appspot.com/nwordlist.txt //replace this on main repo
        // Create a storage reference from our app
        storage = FirebaseStorage.getInstance();
        // referencing about 1000 words documents
        StorageReference gsReference = storage.getReferenceFromUrl("gs://sufferqr.appspot.com/nwordlist.txt");

        File localFile2 = null;
        try {
            localFile2 = File.createTempFile("rnd", "txt");
        } catch (IOException e) {
            System.out.println("create file fail");
        }
        final File localFile = localFile2;
        // fetch file online
        gsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                // random words list credit
                // https://github.com/staceybellerose/RandomWordGenerator/tree/main/wordlists/12dicts/processed
                final CollectionReference collectionReferenceDes = db.collection("RandomName");
                // open file
                FileInputStream input = null;
                BufferedReader bufReader = null;
                ArrayList<String> ListOfWord = new ArrayList<>();
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
                        ListOfWord.add(RadomName);

                    }
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                    System.out.println("file open fail");
                } catch (IOException e){
                    System.out.println(e.toString());
                }
                String MyName2 = name;
                Random rand = new Random();
                for (int i = 0; i < 2; i++) {
                    int iRand = (int) rand.nextInt(ListOfWord.size());
                    MyName2 = MyName2 + ListOfWord.get(iRand);
                }
                boolean unique = false;
                CheckUnique(MyName2,true,data);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    /**
     * delete Database information
     * @return
     * null
     */
    public void DelteQrInfo(String ID){
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReferenceDest = db.collection("GameQrCode");
        // check if id is unique in the FameQr datavase
        collectionReferenceDest.document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    // if result exist
                    if (document.exists()) {
                        collectionReferenceDest.document(ID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    System.out.println("delete sucess");
                                } else {
                                    System.out.println("delete fail");
                                }
                            }
                        });
                    } else {
                        //  switch to add id
                        System.out.println("id not found");
                    }
                } else {
                    System.out.println("fail to connect");
                }
            }
        });
    }

    /**
     * update document informatuin
     * @return
     * null
     */
    public void ChangeQrInfo(String ID,HashMap<String, Object> data){
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReferenceDest = db.collection("GameQrCode");
        // check if id is unique in the FameQr datavase
        collectionReferenceDest.document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    // if result exist
                    if (document.exists()) {
                        // Update an existing document
                        DocumentReference docRef = collectionReferenceDest.document(ID);
                        docRef.update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    System.out.println("update sucessfull");
                                }else {
                                    System.out.println("try again");
                                }

                            }
                        });

                    } else {
                        //  switch to add id
                        CheckUnique(ID, false, data);
                    }
                } else {
                    System.out.println("fail to connect");
                }
            }
        });
    }

}

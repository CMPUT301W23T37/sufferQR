package com.example.sufferqr;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class CreateUniqueName {
    private String UniqueName;

    private FirebaseFirestore db;

    public CreateUniqueName() throws IOException {
        db = FirebaseFirestore.getInstance();
        // https://blog.csdn.net/u011435933/article/details/117419082
    }

    public void getRandomString(){

    }


    public void UpdateRandomWord(){
        // random words list credit
        db = FirebaseFirestore.getInstance();
        // https://github.com/staceybellerose/RandomWordGenerator/tree/main/wordlists/12dicts/processed
        final CollectionReference collectionReference = db.collection("RandomName");

        if(false){
            //
//        // do not enable this part of code unless necessory
//        BufferedReader bufReader = null;
//        try {
//            bufReader = new BufferedReader(new InputStreamReader(new FileInputStream("/data/data/com.example.sufferqr/cache/wordlist.txt")));
//        } catch (FileNotFoundException e) {
//            System.out.println("file open fail");
//        }
//        String RadomName;
//        int count=0,zz=0;
//        while ((RadomName = bufReader.readLine()) != null){
//            count++;
//            if (count % 300 == 0){
//                zz++;
//                HashMap<String, String> data = new HashMap<>();
//                data.put("Name",RadomName);
//                String TAG ="example";
//                final String docname= RadomName;
//                collectionReference
//                        .document(docname)
//                        .set(data)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//// These are a method which gets executed when the task is succeeded
//                                Log.d(TAG, "Data has been added successfully!");
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//// These are a method which gets executed if there’s any problem
//                                Log.d(TAG, "Data could not be added!" + e.toString());
//                            }
//                        });
//            }
//         }
        }
    }
}

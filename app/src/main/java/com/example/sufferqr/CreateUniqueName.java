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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CreateUniqueName implements UniqueTemplete {
    private String UniqueName;

    private FirebaseFirestore db;
    private Boolean Conres=true;
    private Boolean resultExist=true;
    private String NewRandomName;

    public CreateUniqueName() {
        db = FirebaseFirestore.getInstance();
        //getRandomUniqueString();
        // do not enable unless necessory
        // https://blog.csdn.net/u011435933/article/details/117419082
        //UpdateRandomWord("/data/data/com.example.sufferqr/cache/wordlist.txt",50);

    }

    public void getRandomUniqueString(CreateUniqueName CUN){
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReferenceOrigin = db.collection("RandomName");

        int words = 3;
        String TAG = null;
        final String[] myname2 = {"dsjhfa"};
        // asynchronously retrieve all documents
        resultExist=false;
        collectionReferenceOrigin.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<String> name = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //Log.d(TAG,document.getId() + " => " + document.getData());

                        String tt = document.getId();
                        name.add(tt);
                    }
                    String myname ="";
                    Random rand = new Random();
                    for (int i=0;i<words;i++){
                        int iRand = (int) rand.nextInt(name.size());
                        myname = myname + name.get(iRand);
                    }
                    boolean unique=false;
                    while (!unique){
                        CheckUnique(myname,CUN);
                        // waiting for result comeback
                        while (!resultExist);
                        if (!unique){
                            int iRand = rand.nextInt(name.size());
                            myname = myname + name.get(iRand);
                        }
                    }
                    CUN.onCallback("new",false,myname);
                }else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }

            }
        });
    }



    public void CheckUnique(String name,CreateUniqueName CUN){
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReferenceDest = db.collection("GameQr");
        final Boolean[] ifUnique = {true};
        NewRandomName = name;
        resultExist=false;
        collectionReferenceDest.document(name).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        ifUnique[0] = false;
                    } else {
                        ifUnique[0] = true;
                    }
                    CUN.onCallback("new",false,null);
                }else {
                    System.out.println("fail to connect");
                }
            }
        });
    }

    public void UpdateRandomWord (String Path,int number) throws IOException {
        // random words list credit
        int Skip = (int)30000/number;
        // https://github.com/staceybellerose/RandomWordGenerator/tree/main/wordlists/12dicts/processed
        final CollectionReference collectionReference = db.collection("RandomName");

        // do not enable this part of code unless necessory
        BufferedReader bufReader = null;
        try {
            bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(Path)));
        } catch (FileNotFoundException e) {
            System.out.println("file open fail");
        }
        int count=0;
        String RadomName;
        while ((RadomName = bufReader.readLine()) != null){
            count++;
            if (count % Skip == 0){
                HashMap<String, String> data = new HashMap<>();
                data.put("Name",RadomName);
                String TAG ="example";
                final String docname= RadomName;
                collectionReference
                        .document(docname)
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
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
    }


    @Override
    public void onCallback(String reType, boolean Conflict, String NewName) {
        if (reType == "new"){
            Conres =Conflict;
            NewRandomName =NewName;
        } else {
            Conres =Conflict;
        }
        resultExist=true;
        System.out.println(NewName);
    }

    public boolean getResultExist(){
        return resultExist;
    }

    public boolean getConflict(){
        return Conres;
    }

    public String getNewRandomName(){
        return NewRandomName;
    }
}

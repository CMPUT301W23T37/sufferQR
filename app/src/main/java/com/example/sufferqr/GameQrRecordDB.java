package com.example.sufferqr;

import static android.app.PendingIntent.getActivity;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.sufferqr.ui.main.QRDetailGeneralFragment;
import com.example.sufferqr.ui.main.QRDetailLocationFragment;
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
import com.google.firebase.storage.UploadTask;
import com.google.firestore.v1.WriteResult;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * This is a class that connect with database GameQRRecord
 */
public class GameQrRecordDB {
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    private HashMap<String,Object> data;
    public GameQrRecordDB(){
        //getRandomUniqueString();
        // do not enable unless necessory
        // https://blog.csdn.net/u011435933/article/details/117419082
        //UpdateRandomWord("/data/data/com.example.sufferqr/cache/wordlist.txt",50);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
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
                            //listener.onSendingUpdate("user name exist",false);
                        }
                    } else {
                        //  put success staff here.
                        String TAG=null;
                        collectionReferenceDest
                                .document(name).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // These are a method which gets executed when the task is succeeded
                                        //listener.onSendingUpdate("Data has been added successfully!",true);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // These are a method which gets executed if there’s any problem
                                        //listener.onSendingUpdate("Data could not be added!",false);
                                    }
                                });
                    }
                   // when sucess exit
                } else {
                    //listener.onSendingUpdate("delete failed",false);
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
                    //e.printStackTrace();
                    //listener.onSendingUpdate("file open fail",false);
                } catch (IOException e){
                    //System.out.println(e.toString());
                    //listener.onSendingUpdate("file open fail",false);
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
                //listener.onSendingUpdate("delete failed",false);
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
//                                    listener.onSendingUpdate("delete sucess",true);
                                } else {
//                                    listener.onSendingUpdate("delete failed",false);
                                }
                            }
                        });
                    } else {
                        //  switch to add id
//                        listener.onSendingUpdate("id not found",false);
                    }
                } else {
//                    listener.onSendingUpdate("unable connect to server",false);
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
//                                    listener.onSendingUpdate("update sucessfull",true);
                                }else {
//                                    listener.onSendingUpdate("try again",false);
                                }

                            }
                        });

                    } else {
                        //  switch to add id
                        CheckUnique(ID, false, data);
                    }
                } else {
//                    listener.onSendingUpdate("delete failed",false);
                }
            }
        });
    }

    /**
     * new iamge push to firestone
     */
    public void imagePushFirestone(HashMap<String,Object> ns,Uri imageUri, String userName, String QRname, ContentResolver cr){
        data = ns;
        Bitmap bitmap =null;
        if (imageUri!=null){
            try {
                bitmap = MediaStore.Images.Media.getBitmap(cr, imageUri);
            } catch (IOException e) {
                Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
            }
            // de;tete
            try{
                File fdel = new File(imageUri.getPath());//create path from uri
                if (fdel.exists()) {
                    fdel.delete();
                }
            } catch (Exception e){
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
            }

        }
        Boolean imgE = (Boolean) data.get("imageExist");
        if (bitmap!=null && Boolean.TRUE.equals(imgE)){
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            String fName = userName+"_"+timeStamp+".jpg";
            String Path = "image/"+ fName;

            //StorageReference storageRef = storage.getReference();
            StorageReference mountainsRef = storage.getReference().child(Path);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
            byte[] imageData = baos.toByteArray();
            UploadTask uploadTask = mountainsRef.putBytes(imageData);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast toast = Toast.makeText(getApplicationContext(),"tryagain", Toast.LENGTH_SHORT);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    HashMapValidate("QRpath",Path);

                    if (QRname.length()==0){
                        NewQRWithRandomGeneratedWords("",data);

                    } else {
                       CheckUnique(QRname,true,data);
                    }
                }
            });



        } else {
            // de;tete
            try{
                File fdel = new File(imageUri.getPath());//create path from uri
                if (fdel.exists()) {
                    fdel.delete();
                }
            } catch (Exception e){
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
            }

            HashMapValidate("QRpath","");
            if (QRname.length()==0){
                NewQRWithRandomGeneratedWords("",data);
            } else {
                CheckUnique(QRname,true,data);
            }
        }

    }

    private void HashMapValidate(String id,Object ob){
        if (data.containsKey(id)) {
            data.replace(id,ob);
        } else {
            data.put(id,ob);
        }
    }

    /**
     * delete a image in firestone
     */
    public void imageDelFirestone(String s1){
        StorageReference storageRef = storage.getReference();

        // Create a reference to the file to delete
        StorageReference desertRef = storageRef.child(s1);

        // Delete the file
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
    }

}

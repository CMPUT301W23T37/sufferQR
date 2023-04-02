package com.example.sufferqr;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.sufferqr.databinding.ActivitySearchPlayerBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * search player by username, blur search
 */
public class SearchPlayer extends DrawerBase {

    ActivitySearchPlayerBinding activitySearchPlayerBinding;

    ListView searchContent;
    ArrayList<User> userList;
    ArrayAdapter<User> userArrayAdapter;

    SearchView searchView;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activitySearchPlayerBinding = ActivitySearchPlayerBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(activitySearchPlayerBinding.getRoot());
        allocateActivityTitle("Search Player");


        searchContent = findViewById(R.id.search_content);
        searchView = findViewById(R.id.search_bar);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String text) {
                ArrayList<String> allMemberName = new ArrayList<>();
                ArrayList<String> allMemberQrId = new ArrayList<>();

                // Get AAID and give it to the db
                String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                DocumentReference userInfo = db.collection("Player").document(android_id);

                if(text.length() > 0){
                    final CollectionReference collectionReference = db.collection("Player");
                    collectionReference.whereNotEqualTo("name", null).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null){
                                System.err.println("Listen failed: " + error);
                            }
                            // init list
                            userList = new ArrayList<>();

                            // loop through the document, record all names
                            for(DocumentSnapshot doc : value.getDocuments()){
                                allMemberName.add(String.valueOf(doc.get("name")));
                                allMemberQrId.add(String.valueOf(doc.get("qrid")));
//                        Log.d(TAG, "Size: "+allMemberName.size());
                            }

                            // loop through all names find result
                            for (int i = 0; i < allMemberName.size(); i++){
                                if(allMemberName.get(i).contains(text)){
//                                    Log.d(TAG, "Samimilar; "+allMemberName.get(i));
                                    User resultUser = new User(allMemberName.get(i), allMemberQrId.get(i));
                                    userList.add(resultUser);
                                }
                            }

                            userArrayAdapter = new SearchPlayerAdapter(SearchPlayer.this, userList);
                            searchContent.setAdapter(userArrayAdapter);

                            searchContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
//                                    Log.d(TAG, "name: " + userList.get(pos).getName());
                                    Intent i = new Intent(SearchPlayer.this, OtherUser.class);
                                    i.putExtra("username", userList.get(pos).getName());
                                    startActivity(i);
                                }
                            });
                        }
                    });

                }
                return true;
            }
        });




    }

}
package com.example.sufferqr;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sufferqr.databinding.ActivityOtherUserLayoutBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

public class OtherUser extends DrawerBase {

    ActivityOtherUserLayoutBinding activityOtherUserLayoutBinding;

    TextView userName_otherProfile;
    TextView userEmail_otherProfile;
    TextView userQrId_otherProfile;
    Chip highest_otherProfile;
    Chip lowest_otherProfile;
    Chip sum_otherProfile;
    Chip code_otherProfile;

    ListView qrCode_otherProfile;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOtherUserLayoutBinding = ActivityOtherUserLayoutBinding.inflate(getLayoutInflater());
        setContentView(activityOtherUserLayoutBinding.getRoot());

        db = FirebaseFirestore.getInstance();
        userName_otherProfile = findViewById(R.id.userName_otherProfile);
        userEmail_otherProfile = findViewById(R.id.userEmail_otherProfile);
        userQrId_otherProfile = findViewById(R.id.userQRid_otherProfile);
        highest_otherProfile = findViewById(R.id.highest_otherProfile);
        lowest_otherProfile = findViewById(R.id.lowest_otherProfile);
        sum_otherProfile = findViewById(R.id.sum_otherProfile);
        code_otherProfile = findViewById(R.id.number_of_code_otherProfile);

        Bundle value = getIntent().getExtras();
        String name = value.getString("username");

        allocateActivityTitle(name + "'s Profile");

//        userName_otherProfile.setText(name);

        final CollectionReference collectionReference = db.collection("Player");
        collectionReference.whereEqualTo("name", name).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    System.err.println("Listen failed: " + error);
                }

                DocumentSnapshot doc = value.getDocuments().get(0);

                userName_otherProfile.setText(String.valueOf(doc.get("name")));
                userEmail_otherProfile.setText(String.valueOf(doc.get("email")));
                userQrId_otherProfile.setText(String.valueOf(doc.get("qrid")));

                String highest_text = "Highest: " + doc.get("highestScore");
                highest_otherProfile.setText(highest_text);
                String lowest_text = "Lowest: " + doc.get("lowestScore");
                lowest_otherProfile.setText(lowest_text);
                String sum_text = "Sum: " + doc.get("sumScore");
                sum_otherProfile.setText(sum_text);
                String code_text = "Code: " + doc.get("qrcount");
                code_otherProfile.setText(code_text);

            }
        });

    }
}
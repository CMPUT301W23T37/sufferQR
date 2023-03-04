package com.example.sufferqr;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sufferqr.databinding.ActivityUserProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.color.utilities.MathUtils;
import com.google.android.material.color.utilities.Score;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.util.Util;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class contains all the user information, the total number of the score collected
 * the total number of code been scanned, the highest score ever, and the lowest score
 * with player's username, email, QR id and the QR code from QR id. Modification to
 * Username and email is also available
 */
public class UserProfile extends DrawerBase {

    ActivityUserProfileBinding activityUserProfileBinding;

    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ImageView userQRImage;
    private TextView userName;
    private TextView userEmail;
    private TextView userQRid;
    private TextView highScore;
    private TextView lowScore;
    private TextView sumScore;
    private TextView qrCount;
    private FloatingActionButton profileToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserProfileBinding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(activityUserProfileBinding.getRoot());
        allocateActivityTitle("Profile");
        fillContent();
    }

    public void fillContent(){
        userName = findViewById(R.id.userName_UserProfile);
        userEmail = findViewById(R.id.userEmail_UserProfile);
        userQRid = findViewById(R.id.userQRid_UserProfile);
        highScore = findViewById(R.id.highestScore_UserProfile);
        lowScore = findViewById(R.id.lowestScore_UserProfile);
        sumScore = findViewById(R.id.sumScore_UserProfile);
        qrCount = findViewById(R.id.totalQR_UserProfile);

        // Get AAID and give it to the db
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        DocumentReference userInfo = db.collection("Player").document(android_id);

        // deal with fetching data from db to the view
        userInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                userName.setText((String) document.get("name"));
                userEmail.setText((String) document.get("email"));
                userQRid.setText((String) document.get("qrid"));

                // get all the scores into a list of integer
                List<Long> userScores = (List<Long>) task.getResult().get("scores");

                // sorted list
                List<Long> sortedList = userScores.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
                // set highest in the list
                long tempHighest = sortedList.get(0);
                highScore.setText(Long.toString(tempHighest));
                // set lowest in the list
                long tempLowest = sortedList.get(sortedList.size()-1);
                lowScore.setText(Long.toString(tempLowest));
                // set sum of the list
                long sum = sortedList.stream().mapToLong(Long::longValue).sum();
                sumScore.setText(String.valueOf(sum));
                // set size of the list
                if(sortedList.size() == 1 && sortedList.get(0) == 0){
                    qrCount.setText(String.valueOf(0));
                } else{
                    qrCount.setText(String.valueOf(sortedList.size()));
                }
            }
        });

        profileToEdit = findViewById(R.id.changeToEditProfile_UserProfile);
        profileToEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserProfile.this, EditProfile.class);
                startActivity(i);
                finish();
            }
        });
    }
}
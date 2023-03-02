package com.example.sufferqr;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sufferqr.databinding.ActivityUserProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.color.utilities.Score;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.util.Util;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserProfileBinding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(activityUserProfileBinding.getRoot());
        allocateActivityTitle("Profile");

        userName = findViewById(R.id.userName_UserProfile);
        userEmail = findViewById(R.id.userEmail_UserProfile);
        userQRid = findViewById(R.id.userQRid_UserProfile);
        highScore = findViewById(R.id.highestScore_UserProfile);
        lowScore = findViewById(R.id.lowestScore_UserProfile);
        sumScore = findViewById(R.id.sumScore_UserProfile);
        qrCount = findViewById(R.id.totalQR_UserProfile);

        // Get AAID
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        DocumentReference userInfo = db.collection("Player").document(android_id);

        userInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                userName.setText((String) document.get("name"));
                userEmail.setText((String) document.get("email"));
                userQRid.setText((String) document.get("qrid"));
                highScore.setText((String) document.get("highestScore"));
                lowScore.setText((String) document.get("lowestScore"));
                sumScore.setText((String) document.get("highestScore"));
            }
        });
    }
}
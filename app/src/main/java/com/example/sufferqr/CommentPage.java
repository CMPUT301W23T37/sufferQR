package com.example.sufferqr;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sufferqr.ui.main.QRQuickViewComment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommentPage extends AppCompatActivity {
//    private ArrayList<QRQuickViewComment> dataList;
//    private ListView commentsList;
//    private QRQuickViewCommentsArrayAdapter commentsAdapter;

    String qrName;
    String uName;
    String comment;
    Button saveComment;
    Button cancelComment;
    Button deleteComment;
    EditText commentContent;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference ref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_page);

        saveComment =  findViewById(R.id.confirm_add_comment_button);
        cancelComment = findViewById(R.id.cancel_add_comment_button);
        commentContent = findViewById(R.id.comment_content);
        deleteComment = findViewById(R.id.delete_comment_button);
        deleteComment.setVisibility(View.INVISIBLE);

        // retrieve the qr code name
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                qrName = null;
            } else {
                qrName = extras.getString("QRName");
            }
        } else {
            qrName = (String) savedInstanceState.getSerializable("QRName");
        }

        ref = db.collection("GameQrCode").document(qrName);


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String realDate = dateFormat.format(calendar.getTime());

        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        DocumentReference userAAID = db.collection("Player").document(android_id);
        userAAID.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot userInfo = task.getResult();
                    uName = (String) userInfo.get("name");
                } else {
                    Log.d(TAG, "failed with ", task.getException());
                }
            }
        });

        saveComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment = String.valueOf(commentContent.getText());
                QRQuickViewComment newComment = new QRQuickViewComment(uName, realDate, comment, android_id);
                if (comment.length() != 0) {
                    ref.collection("Comment").add(newComment);
                    Toast.makeText(CommentPage.this, "Comment Successful", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CommentPage.this, "Please enter your comment", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        deleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}

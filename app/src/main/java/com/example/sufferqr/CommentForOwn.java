package com.example.sufferqr;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sufferqr.ui.main.QRQuickViewComment;
import com.example.sufferqr.ui.main.QRQuickViewCommentsArrayAdapter;
import com.example.sufferqr.ui.main.QRQuickViewCommentsDialog;
import com.example.sufferqr.ui.main.QRQuickViewCommentsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CommentForOwn extends AppCompatActivity implements QRQuickViewCommentsDialog.AddCommentDialogListener{
    private ArrayList<QRQuickViewComment> dataList;
    private ListView commentsList;
    private QRQuickViewCommentsArrayAdapter commentsAdapter;
    @Override
    public void addComment(QRQuickViewComment newCom) {
        commentsAdapter.add(newCom);
        commentsAdapter.notifyDataSetChanged();
    }

    @Override
    public void editComment(String comContent, int position) {
        dataList.get(position).setComment(comContent);
        commentsAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteComment(int i) {
        dataList.remove(i);
        commentsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_for_own);

        dataList = new ArrayList<>();
        commentsList = findViewById(R.id.comment_for_own_list);
        commentsAdapter = new QRQuickViewCommentsArrayAdapter(this, dataList);
        commentsList.setAdapter(commentsAdapter);
        ImageView comReturn = findViewById(R.id.comment_return_image);
        FloatingActionButton fab = findViewById(R.id.add_comment);

        comReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                Intent HistIntent = new Intent(CommentForOwn.this, ScanHistory.class);
                HistIntent.putExtra("user",android_id);
                startActivity(HistIntent);
            }
        });

        // builds a dialog when add button is clicked
        fab.setOnClickListener(v -> {
            new QRQuickViewCommentsDialog().show(getSupportFragmentManager(), "Add Comment");
        });

        commentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                QRQuickViewCommentsDialog.newInstance(commentsAdapter.getItem(i), i).show(getSupportFragmentManager(), "Edit Comment");
            }
        });
    }
}

package com.example.sufferqr.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sufferqr.R;

import java.util.ArrayList;

public class QRQuickViewCommentsArrayAdapter extends ArrayAdapter<QRQuickViewComment> {

    public QRQuickViewCommentsArrayAdapter(@NonNull Context context, ArrayList<QRQuickViewComment> comment) {

        super(context, 0, comment);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_q_r_quick_view_comments,
                    parent, false);
        } else {
            view = convertView;
        }

        QRQuickViewComment com =  super.getItem(position);

        TextView comUserName = view.findViewById(R.id.quick_view_comment_context_username);
        TextView comDate = view.findViewById(R.id.quick_view_comment_context_time);
        TextView comComment = view.findViewById(R.id.quick_view_comment_context_comment);
        comUserName.setText(com.getUserName());
        comDate.setText(com.getCDate());
        comComment.setText(com.getComment());

        return view;
    }
}

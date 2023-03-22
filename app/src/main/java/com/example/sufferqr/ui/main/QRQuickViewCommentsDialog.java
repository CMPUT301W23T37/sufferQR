package com.example.sufferqr.ui.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import com.example.sufferqr.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class QRQuickViewCommentsDialog extends DialogFragment implements Serializable {
    private AddCommentDialogListener listener;

    interface AddCommentDialogListener {
        void addComment(QRQuickViewComment newCom);
        void editComment(String comContent, int position);
        void deleteComment(int i);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCommentDialogListener) {
            listener = (AddCommentDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_q_r_quick_view_comments_dialog, null);

        TextView userName = view.findViewById(R.id.quick_view_comment_dialog_user);
        TextView date = view.findViewById(R.id.quick_view_comment_dialog_time);
        EditText comment = view.findViewById(R.id.quick_view_comment_dialog_comment);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String realDate = dateFormat.format(calendar.getTime());

        Bundle editable = getArguments();
        // check if the bundle is passed, if so, edit the QRQuickViewComment object, otherwise create a new QRQuickViewComment object
        if (editable == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            return builder
                    .setView(view)
                    .setTitle("Add A Comment")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Add", (dialog, which) -> {
                        String uName = userName.getText().toString();
                        String comm = comment.getText().toString();

                        listener.addComment(new QRQuickViewComment(uName, realDate, comm));
                    })
                    .create();
        }
        else {
            // get QRQuickViewComment object from the bundle
            QRQuickViewComment passedComm = (QRQuickViewComment) editable.getSerializable("Comment");

            // set the text field with corresponding content
            userName.setText(passedComm.getUserName());
            date.setText(passedComm.getCDate());
            comment.setText(passedComm.getComment());

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            return builder
                    .setView(view)
                    .setTitle("Edit Comment")
                    .setNegativeButton("Cancel",null)
                    .setPositiveButton("Ok",(dialog, which) -> {
                        // get the edited text from each field
                        String c = comment.getText().toString();
                        // edit QRQuickViewComment object
                        listener.editComment(c, (Integer) editable.getSerializable("Pos"));
                    })
                    .setNeutralButton("Delete", (dialog, which) ->{
                        // delete QRQuickViewComment object
                        listener.deleteComment((Integer) editable.getSerializable("Pos"));
                    })
                    .create();
        }
    }

    static QRQuickViewCommentsDialog newInstance(QRQuickViewComment com, int position){
        Bundle args = new Bundle();
        args.putSerializable("Comment", com);
        args.putSerializable("Pos", position);

        QRQuickViewCommentsDialog fragment = new QRQuickViewCommentsDialog();
        fragment.setArguments(args);
        return fragment;
    }
}

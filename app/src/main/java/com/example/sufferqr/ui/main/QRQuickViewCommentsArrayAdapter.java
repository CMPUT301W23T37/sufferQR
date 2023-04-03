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

/**
 * comment list Adapter
 */
public class QRQuickViewCommentsArrayAdapter extends ArrayAdapter<QRQuickViewComment> {

    /**
     * launch the class
     * @param context class context
     * @param comment array list
     */
    public QRQuickViewCommentsArrayAdapter(@NonNull Context context, ArrayList<QRQuickViewComment> comment) {

        super(context, 0, comment);

    }

    /**
     * ui item drawing
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_q_r_quick_view_comments_context,
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

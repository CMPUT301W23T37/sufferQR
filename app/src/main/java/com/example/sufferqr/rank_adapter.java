package com.example.sufferqr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class rank_adapter extends ArrayAdapter<user_rank> {
    public rank_adapter(Context context, List<user_rank> ranks) {
        super(context, 0, ranks);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.rank_detail,
                    parent, false);
        } else {
            view = convertView;
        }

        //set text for fixed value
        user_rank rank = getItem(position);
        TextView name = view.findViewById(R.id.total_fourth_username);
        TextView rank_num = view.findViewById(R.id.rank);
        TextView point = view.findViewById(R.id.total_fourth_score);

        name.setText(rank.getUsername());
        rank_num.setText(rank.getRank());
        point.setText(rank.getScore());

        return view;
    }
}


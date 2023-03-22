package com.example.sufferqr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchPlayerAdapter extends ArrayAdapter<User> {

    Context context;
    ArrayList<User> users;

    public SearchPlayerAdapter(@NonNull Context context, ArrayList<User> objects) {
        super(context, 0, objects);
        this.context = context;
        this.users = objects;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.activity_search_player_content,parent,false);
        }

        User user = users.get(position);
        TextView name = view.findViewById(R.id.search_name);
        name.setText(user.getName());


        return view;
    }
}

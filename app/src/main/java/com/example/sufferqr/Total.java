package com.example.sufferqr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;


public class Total extends Fragment {
    private ListView ranks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_total, container, false);


        // Create sample data
        List<user_rank> exampleData = new ArrayList<>();
        exampleData.add(new user_rank("4", "username4", "700"));
        exampleData.add(new user_rank("5", "username5", "400"));


        // Set up the adapter
        rank_adapter adapter = new rank_adapter(requireContext(), exampleData);

        // Set the adapter for the ListView
        ranks = view.findViewById(R.id.ranks_listview);
        ranks.setAdapter(adapter);

        return view;


    }
}
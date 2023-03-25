package com.example.sufferqr.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.sufferqr.CommentForOwn;
import com.example.sufferqr.R;



public class QRQuickViewCommentsFragment extends Fragment {

    Bundle bundle;
    View view;

    public QRQuickViewCommentsFragment(Bundle myBundle) {
        // Required empty public constructor
        bundle = myBundle;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // bundle contain all document under this qrcode,however the document is created on database
        // xml for this fragment is .ui.main.QRQuickViewCommentsFragment
        // context suggest use requireContext()
        // to get to a complement use view.findViewById(...)
        // to see realtime updating list see scan-history
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_q_r_quick_view_comments, container, false);


        Button goComment = view.findViewById(R.id.go_to_comment_button);
        goComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), CommentForOwn.class));
            }
        });

        return view;
    }


}
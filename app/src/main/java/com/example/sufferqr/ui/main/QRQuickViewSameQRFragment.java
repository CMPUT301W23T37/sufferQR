package com.example.sufferqr.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sufferqr.R;


public class QRQuickViewSameQRFragment extends Fragment {

    Bundle bundle;
    View view;

    public QRQuickViewSameQRFragment(Bundle mybundle) {
        // Required empty public constructor
        bundle = mybundle;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // bundle contain all document under this qrcode,however the query is created on database
        // xml for this fragment is .ui.main.QRQuickViewSameQRFragment
        // context suggest use requireContext()
        // to get to a complement use view.findbyviewid(...)
        // to see realtime updating list see scan-history


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_q_r_quick_view_same_q_r, container, false);


        return view;
    }
}
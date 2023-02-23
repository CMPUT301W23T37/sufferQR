package com.example.sufferqr.ui.main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sufferqr.R;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.maps.MapView;


import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QRDetailLocationFragment} factory method to
 * create an instance of this fragment.
 */
public class QRDetailLocationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    Bundle mymapBundle,sIState;
    String mode,POI,token="";
    Double NS,EA;
    Intent intent;



    public QRDetailLocationFragment(Bundle mapBundle) {
        mymapBundle=mapBundle;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.mode = mymapBundle.getString("mode");
//        if (this.mode==null){this.mode="new";}
//        this.NS = mymapBundle.getDouble("NS");
//        this.EA = mymapBundle.getDouble("EA");
//


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_r_detail_location, container, false);

        // .









        // Inflate the layout for this fragment
        return view;
    }







}
package com.example.sufferqr.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sufferqr.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QRDetailGeneralFragment} factory method to
 * create an instance of this fragment.
 */
public class QRDetailGeneralFragment extends Fragment implements QRDetailImageFragment.OnFragmentInteractionListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private QRDetailGeneralFragment.OnFragmentInteractionListener listener;

    Bundle myGeneralBudle;


    public QRDetailGeneralFragment(Bundle gbundle) {
        myGeneralBudle = gbundle;
    }

    public interface OnFragmentInteractionListener{
        void onGeneralUpdate(String QRcodename);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_q_r_detail_general, container, false);
    }

    @Override
    public void onImageUpdate(String QRtext, Boolean imageOn) {

    }
}
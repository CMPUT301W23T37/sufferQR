package com.example.sufferqr.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sufferqr.R;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QRDetailImageFragment} factory method to
 * create an instance of this fragment.
 */
public class QRDetailImageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private OnFragmentInteractionListener listener;

    // TODO: Rename and change types of parameters
    TextInputEditText qrcodeText;


    Bundle myImageBundle;


    public QRDetailImageFragment(Bundle adapterImageBundle) {
        myImageBundle = adapterImageBundle;
    }

    public interface OnFragmentInteractionListener{
        void onImageUpdate(String QRtext,Boolean imageOn);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        }else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_q_r_detail_image, container, false);
        qrcodeText = view.findViewById(R.id.qr_detail_image_textfield);
        qrcodeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listener.onImageUpdate(String QRtext,Boolean imageOn);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });







        return view;
    }
}
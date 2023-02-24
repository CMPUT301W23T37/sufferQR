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
import android.widget.TextView;
import com.example.sufferqr.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QRDetailGeneralFragment} factory method to
 * create an instance of this fragment.
 */
public class QRDetailGeneralFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters


    private OnFragmentInteractionListener listener;

    Bundle myGeneralBudle;
    TextInputEditText name;

    TextView textView;
    Date madeDate;
    String myDate;

    public QRDetailGeneralFragment(Bundle gbundle) {
        myGeneralBudle = gbundle;
    }

    public interface OnFragmentInteractionListener{
        void onGeneralUpdate(String QRcodename,String today);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

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
        View view = inflater.inflate(R.layout.fragment_q_r_detail_general, container, false);

        name = view.findViewById(R.id.qr_detail_general_qrtext_name);
        textView = view.findViewById(R.id.qr_detail_general_qrtext_date);
        madeDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        myDate = dateFormat.format(madeDate);
        textView.setText(myDate);

        listener.onGeneralUpdate(" ",myDate);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listener.onGeneralUpdate(s.toString(),myDate);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}
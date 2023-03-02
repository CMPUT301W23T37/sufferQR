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
import android.widget.Button;
import android.widget.TextView;
import com.example.sufferqr.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QRDetailGeneralFragment} factory method to
 * create an instance of this fragment.
 */
public class QRDetailGeneralFragment extends Fragment{

    private OnFragmentInteractionListener listener;
    Bundle myGeneralBudle;
    TextInputEditText name;
    TextView textView;
    Date madeDate;
    String myDate,mode;

    /**
     * launch
     */
    public QRDetailGeneralFragment(Bundle gbundle) {
        myGeneralBudle = gbundle;
        mode = myGeneralBudle.getString("mode");
        if (mode.length()<1){
            mode="new";
        }
    }

    /**
     * sync input to QRdetial class
     */
    public interface OnFragmentInteractionListener{
        void onGeneralUpdate(String QRcodename,String today);

        void onGeneralUpdate(Boolean delreq);

    }

    /**
     * launch
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * attach listener
     */
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

    /**
     * creat view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_r_detail_general, container, false);

        name = view.findViewById(R.id.qr_detail_general_qrtext_name);
        textView = view.findViewById(R.id.qr_detail_general_qrtext_date);
        Button del_button = view.findViewById(R.id.qr_detail_general_elevatedButton);

        // if load information
        if (Objects.equals(mode, "new")){
            madeDate = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
            myDate = dateFormat.format(madeDate);
            textView.setText(myDate);

            listener.onGeneralUpdate(" ",myDate);

            name.setEnabled(true);
        } else {
            name.setEnabled(false);
        }
        // when type changes sync to mainpage
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Objects.equals(mode, "new")){
                    listener.onGeneralUpdate(s.toString(),myDate);
                }else {}
            }
        });

        // del button listener
        del_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGeneralUpdate(true);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
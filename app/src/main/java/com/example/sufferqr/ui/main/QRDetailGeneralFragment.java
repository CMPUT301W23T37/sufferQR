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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
    TextInputLayout TIL;
    TextInputEditText name;
    TextView textView;
    Date madeDate;
    String myDate,mode;

    private FirebaseFirestore db;

    /**
     * launch
     */
    public QRDetailGeneralFragment(Bundle gbundle) {
        db = FirebaseFirestore.getInstance();
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
        TIL =view.findViewById(R.id.qr_detail_general_qrtext_name_layout);

        // if load information
        if (Objects.equals(mode, "new")){
            madeDate = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
            myDate = dateFormat.format(madeDate);
            textView.setText(myDate);

            listener.onGeneralUpdate(" ",myDate);

            name.setEnabled(true);
        } else if (Objects.equals(mode, "modified")) {
            name.setEnabled(true);
        }
        {
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

                    if (s.length()>0){
                        db = FirebaseFirestore.getInstance();
                        final CollectionReference collectionReferenceDest = db.collection("GameQrCode");
                        // check if id is unique in the FameQr datavase
                        collectionReferenceDest.document(s.toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    // if result exist
                                    if (document.exists()) {
                                        TIL.setErrorEnabled(true);
                                        TIL.setError("ID exist");
                                    } else {
                                        TIL.setErrorEnabled(false);
                                        TIL.setError("");
                                        TIL.setHelperText("ID looks good");
                                    }
                                } else {
                                    //listener.onSendingUpdate("delete failed",false);
                                }
                            }
                        });
                    }else{
                        TIL.setHelperText("leave it blank to random");
                    }
                }
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
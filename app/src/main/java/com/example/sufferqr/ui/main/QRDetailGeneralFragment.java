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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QRDetailGeneralFragment} factory method to
 * create an instance of this fragment.
 */
public class QRDetailGeneralFragment extends Fragment{

    private OnFragmentInteractionListener listener;
    Bundle myGeneralBudle,sIState;
    TextInputLayout TIL;
    TextInputEditText name,points,visual;
    TextView textView;
    Date madeDate;
    String myDate,mode,oldName,QRvisual,QRpoints;

    View gbview;

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
        if (mode.equals("new")){
            QRpoints=myGeneralBudle.getString("QRScore");
            QRvisual=myGeneralBudle.getString("QRVisual");
        }
        if (mode.equals("modified")){
            oldName=myGeneralBudle.getString("qrID");
        }


    }

    /**
     * sync input to QRdetial class
     */
    public interface OnFragmentInteractionListener{
        void onGeneralUpdate(String QRcodename,String today);

        void onGeneralUpdate(Boolean delreq);

        void onGeneralUpdate(String Newname);

    }

    /**
     * launch
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gbview = getView();
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
        sIState= savedInstanceState;
        name = view.findViewById(R.id.qr_detail_general_qrtext_name);
        points = view.findViewById(R.id.qr_detail_general_qrtext_points);
        visual = view.findViewById(R.id.qr_detail_general_visual_text);
        textView = view.findViewById(R.id.qr_detail_general_qrtext_date);
        Button del_button = view.findViewById(R.id.qr_detail_general_elevatedButton);
        TIL =view.findViewById(R.id.qr_detail_general_qrtext_name_layout);
        TIL.setError("");

        // if load information
        if (Objects.equals(mode, "new")){
            madeDate = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ", Locale.CANADA);
            myDate = dateFormat.format(madeDate);
            textView.setText(myDate);
            visual.setText(QRvisual);
            points.setText(QRpoints);
            listener.onGeneralUpdate("",myDate);
            name.setEnabled(true);
        } else if (Objects.equals(mode, "modified")) {
            name.setEnabled(true);
            name.setText(oldName);
        } else {
            name.setEnabled(false);
        }
        // when type changes sync to mainpage
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (Objects.equals(mode, "new" )|| mode.equals("modified")){
                    if (Objects.equals(mode, "new" )){
                        listener.onGeneralUpdate(s.toString(),myDate);
                    } else {
                        listener.onGeneralUpdate(s.toString());
                    }

                    if (s.length()>0){
                        db = FirebaseFirestore.getInstance();
                        final CollectionReference collectionReferenceDest = db.collection("GameQrCode");
                        // check if id is unique in the FameQr datavase
                        collectionReferenceDest.document(s.toString()).get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                // if result exist
                                if (document.exists()) {
                                    String ms = s.toString();
                                    if (mode.equals("modified") && (ms.equals(oldName))){
                                        TIL.setErrorEnabled(false);
                                        TIL.setError("");
                                        TIL.setHelperText("ID looks good");
                                    }else {
                                        TIL.setErrorEnabled(true);
                                        TIL.setError("ID exist");
                                    }
                                } else {
                                    TIL.setErrorEnabled(false);
                                    TIL.setError("");
                                    TIL.setHelperText("ID looks good");
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
        del_button.setOnClickListener(v -> listener.onGeneralUpdate(true));

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * resume
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * pause
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public void ActivityCallBack(View gView, String userName11,HashMap<String, Object> data){
        TextInputEditText name,points;
        TextView textView;Button button;
        name = gView.findViewById(R.id.qr_detail_general_qrtext_name);
        points = gView.findViewById(R.id.qr_detail_general_qrtext_points);
        textView = gView.findViewById(R.id.qr_detail_general_qrtext_date);
        TextInputEditText visual = gView.findViewById(R.id.qr_detail_general_visual_text);

        button = gView.findViewById(R.id.qr_detail_general_elevatedButton);
        name.setText((String)data.get("QRname"));
        Object pt = data.get("points");
        points.setText(String.valueOf(pt));
        textView.setText((String)data.get("date"));
        visual.setText((String)data.get("QVisual"));
        TextInputLayout ttl = gView.findViewById(R.id.qr_detail_general_qrtext_name_layout);

        if (Objects.equals((String) data.get("user"), userName11)){
            ttl.setEnabled(true);
            name.setEnabled(true);
        } else {
            ttl.setHelperText("");
            ttl.setCounterEnabled(false);
            ttl.setEnabled(false);
            name.setEnabled(false);
        }

        // if not the creator disble change option
        if (Objects.equals((String) data.get("user"), userName11)){
            button.setEnabled(true);
        }
    }

}
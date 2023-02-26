package com.example.sufferqr.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sufferqr.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

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

    SwitchMaterial imgEnable;
    Bundle myImageBundle;
    TextView pic_text;
    CardView text_card,qr_card;
    String mode,localQRcontent="";

    public QRDetailImageFragment(Bundle adapterImageBundle) {
        myImageBundle = adapterImageBundle;
        mode = myImageBundle.getString("mode");
        if (mode.length()<1){
            mode="new";
        }
    }

    public interface OnFragmentInteractionListener{
        void onImageUpdate(String QRtext,Boolean imageOn);

        void onImageUpdate(Boolean imageOn);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        imgEnable = view.findViewById(R.id.qr_detail_image_enable_switch);
        qr_card = view.findViewById(R.id.qr_detail_image_qrimage_cardview);
        text_card = view.findViewById(R.id.qr_detail_image_qrtext_cardview);
        pic_text = view.findViewById(R.id.qr_detail_image_qrimage_notification_bottom);

        if (Objects.equals(mode, "new")) {
            listener.onImageUpdate("", imgEnable.isChecked());
        }
        imgEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgEnable.isChecked()){
                    if (mode.equals("new")){
                        listener.onImageUpdate(localQRcontent,imgEnable.isChecked());
                    }else{
                        listener.onImageUpdate(imgEnable.isChecked());
                    }
                } else {
                    if (mode.equals("new")){
                        listener.onImageUpdate("",imgEnable.isChecked());
                    }else {
                        listener.onImageUpdate(imgEnable.isChecked());
                    }
                }
            }
        });

        if (Objects.equals(mode, "new")){
            qrcodeText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //listener.onImageUpdate(s.toString(),imgEnable.isChecked());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //listener.onImageUpdate(s.toString(),imgEnable.isChecked());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length() > 0){
                        localQRcontent=s.toString();
                        listener.onImageUpdate(s.toString(),imgEnable.isChecked());
                    }
                }
            });
            qrcodeText.setEnabled(true);
        } else {
            qrcodeText.setEnabled(false);
            qr_card.setVisibility(View.INVISIBLE);
            text_card.setVisibility(View.INVISIBLE);
            pic_text.setVisibility(View.INVISIBLE);
            imgEnable.setEnabled(false);
        }








        return view;
    }
}
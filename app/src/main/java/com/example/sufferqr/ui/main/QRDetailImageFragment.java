package com.example.sufferqr.ui.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.ImageCapture;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sufferqr.MainActivity;
import com.example.sufferqr.QRDetailActivity;
import com.example.sufferqr.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QRDetailImageFragment} factory method to
 * create an instance of this fragment.
 */
public class QRDetailImageFragment extends Fragment {

    private OnFragmentInteractionListener listener;
    TextInputEditText qrcodeText;
    SwitchMaterial imgEnable;
    Bundle myImageBundle;
    TextView pic_text;
    CardView text_card,qr_card;
    String mode,localQRcontent="";
    Uri imageUri,surroundsingUri;
    Boolean surroundsExist;
    ImageButton qrbt,surrbt;
    public static File tempFile;

    /**
     * launch
     */
    public QRDetailImageFragment(Bundle adapterImageBundle) {
        myImageBundle = adapterImageBundle;
        mode = myImageBundle.getString("mode");
        if (mode.length()<1){
            mode="new";
        }
        if (mode.equals("new")){
            localQRcontent=myImageBundle.getString("QRString");
            String imageU = myImageBundle.getString("imageUri");
            imageUri = Uri.parse(imageU);

        }

        System.out.println(localQRcontent);
    }

    /**
     * lister sync to QRdetail tab
     */
    public interface OnFragmentInteractionListener{
        void onImageUpdate(String QRtext,Boolean imageOn);

        void onImageUpdate(Boolean imageOn);

    }

    /**
     * launch
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * attach to listener
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
     * launch
     */
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
        qrbt = view.findViewById(R.id.qr_detail_image_qrimage_button);

        if (Objects.equals(mode, "new")) {
            listener.onImageUpdate("", imgEnable.isChecked());
        }

        // image buton listener(privict)
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
                        listener.onImageUpdate(localQRcontent,imgEnable.isChecked());
                    }else {
                        listener.onImageUpdate(imgEnable.isChecked());
                    }
                }
            }
        });

        // text change lister
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
            if (localQRcontent.length()>=1){
                qrcodeText.setText(localQRcontent);
            }

        } else {
            qrcodeText.setEnabled(false);
            qr_card.setVisibility(View.INVISIBLE);
            text_card.setVisibility(View.INVISIBLE);
            pic_text.setVisibility(View.INVISIBLE);
            imgEnable.setEnabled(false);
        }

        // load image at new mode
        if (Objects.equals(mode, "new")){
            Drawable yourDrawable;
            try {
                InputStream inputStream = requireActivity().getContentResolver().openInputStream(imageUri);
                yourDrawable = Drawable.createFromStream(inputStream, imageUri.toString() );
                qrbt.setBackground(yourDrawable);
            } catch (FileNotFoundException e) {
                Toast toast = Toast.makeText(requireContext(), "load image error", Toast.LENGTH_SHORT);
            }
        }

        return view;
    }


}
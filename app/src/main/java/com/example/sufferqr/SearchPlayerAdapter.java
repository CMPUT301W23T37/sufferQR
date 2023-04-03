package com.example.sufferqr;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.List;

/**
    set up adapter for search player
 **/
public class SearchPlayerAdapter extends ArrayAdapter<User> {

    Context context;
    ArrayList<User> users;

    /**
     * constructor
     * @param context where to use
     * @param objects which list to use
     */
    public SearchPlayerAdapter(@NonNull Context context, ArrayList<User> objects) {
        super(context, 0, objects);
        this.context = context;
        this.users = objects;
    }


    /**
     * set up on click action
     * @param position locaiton tabed
     * @param convertView view
     * @param parent parent group
     * @return view view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.activity_search_player_content,parent,false);
        }

        User user = users.get(position);
        TextView name = view.findViewById(R.id.search_name);
        name.setText(user.getName());

        ImageView image = view.findViewById(R.id.search_qr);

        // generate qr code from qrId
        String qrCode = user.getQRid() + user.getName();
        MultiFormatWriter mWriter = new MultiFormatWriter();
        try {
            //BitMatrix class to encode entered text and set Width & Height
            BitMatrix mMatrix = mWriter.encode(qrCode, BarcodeFormat.QR_CODE, 400,400);
            BarcodeEncoder mEncoder = new BarcodeEncoder();
            Bitmap mBitmap = mEncoder.createBitmap(mMatrix);//creating bitmap of code
            image.setImageBitmap(mBitmap);//Setting generated QR code to imageView
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return view;
    }
}

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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.List;
/**
 * set up the item in Highest Total Score List
 * and the item in Highest Unique QR Code Score List
 */

public class HighScorePlayerList extends ArrayAdapter<HighScorePlayer> {
    public HighScorePlayerList(Context context, List<HighScorePlayer> highScorePlayerArrayList){
        super(context,0,highScorePlayerArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.rank_detail,
                    parent, false);
        } else {
            view = convertView;
        }

        HighScorePlayer highScorePlayer = getItem(position);
        TextView rank = view.findViewById(R.id.rank);
        TextView userName = view.findViewById(R.id.total_fourth_username);
        TextView score = view.findViewById(R.id.total_fourth_score);
        ImageView userIdQrImage = view.findViewById(R.id.total_fourth_qr);

        int intRank = highScorePlayer.getRank();
        String strRank = Integer.toString(intRank);
        rank.setText(strRank);

        userName.setText(highScorePlayer.getUsername());

        int intScore = highScorePlayer.getScore();
        String strScore = Integer.toString(intScore);
        score.setText(strScore);

        String userQRid = highScorePlayer.gerQrid();
        String name = highScorePlayer.getUsername();
        String qrCode = userQRid + name;
        MultiFormatWriter mWriter = new MultiFormatWriter();
        try {
            //BitMatrix class to encode entered text and set Width & Height
            BitMatrix mMatrix = mWriter.encode(qrCode, BarcodeFormat.QR_CODE, 400,400);
            BarcodeEncoder mEncoder = new BarcodeEncoder();
            Bitmap mBitmap = mEncoder.createBitmap(mMatrix);//creating bitmap of code
            userIdQrImage.setImageBitmap(mBitmap);//Setting generated QR code to imageView
        } catch (WriterException e) {
            e.printStackTrace();}

        return view;

    }
}


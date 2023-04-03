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

    /**
     * launch fragment view
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return view
     */
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


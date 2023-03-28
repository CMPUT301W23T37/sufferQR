package com.example.sufferqr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * qrcode nearby list each item uidrawing
 */
public class QrCodeList extends ArrayAdapter<QrCode> {

    private ArrayList<QrCode> qrCodes;
    private Context context;

    public QrCodeList(Context context, ArrayList<QrCode> qrCode){
        super(context,0, qrCode);
        this.qrCodes = qrCode;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.nearby_qrcode_content, parent,false);
        }

        QrCode qrCode = qrCodes.get(position);
        TextView qrName = view.findViewById(R.id.nearbylist_qrname_text);
        TextView pointsNum = view.findViewById(R.id.nearbylist_points_text);
        TextView address = view.findViewById(R.id.nearbylist_address_text);
        TextView date = view.findViewById(R.id.nearbylist_date_text);

        qrName.setText("QRname: " + qrCode.getQrName());
        pointsNum.setText(qrCode.getPoints());
        address.setText("Address: " + qrCode.getLocationAddress());
        date.setText("Date: " + qrCode.getDate());

        return view;

    }
}


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

        TextView address = view.findViewById(R.id.address_text);
        TextView pointsNum = view.findViewById(R.id.points_text);

        address.setText("Adress: " + qrCode.getLocationAddress());

        pointsNum.setText("points: " + qrCode.getPoints());

        return view;

    }
}


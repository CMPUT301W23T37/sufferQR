package com.example.sufferqr.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sufferqr.R;

import java.util.ArrayList;
import java.util.Objects;

public class ScanHistoryCustomList extends ArrayAdapter<ScanHistoryQRRecord> {

    private ArrayList<ScanHistoryQRRecord> rec;
    private Context context;

    public ScanHistoryCustomList(Context context, ArrayList<ScanHistoryQRRecord> qrrecord){
        super(context,0, qrrecord);
        this.rec = qrrecord;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.activity_scan_history_context, parent,false);
        }

        ScanHistoryQRRecord qr = rec.get(position);

        TextView QRname = view.findViewById(R.id.scan_activity_content_qrname);
        TextView QRdate = view.findViewById(R.id.scan_activity_content_qrdate);
        TextView QRpoints = view.findViewById(R.id.scan_activity_content_points_num);
        TextView QRaddress = view.findViewById(R.id.scan_activity_content_qraddress);

        System.out.println(qr.getName()+qr.getDate()+qr.getPoints());
        QRname.setText(qr.getName());
        QRdate.setText(qr.getDate());
        QRpoints.setText(qr.getPoints());
        String loc =qr.getLoc();
        if (Objects.equals(loc, "")){
            QRaddress.setText("In solar system");
        } else {
            loc = "near "+loc;
            QRaddress.setText(loc);
        }

        return view;
    }
}

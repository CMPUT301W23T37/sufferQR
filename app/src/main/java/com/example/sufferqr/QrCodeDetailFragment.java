package com.example.sufferqr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * nearby qrcode for each qr records
 */
public class QrCodeDetailFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_detail_qrcode, null);
        TextView address = view.findViewById(R.id.detail_Address_text);
        TextView lat = view.findViewById(R.id.detail_lat_text);
        TextView lon = view.findViewById(R.id.detail_long_text);
        TextView date = view.findViewById(R.id.detail_date_text);
        TextView points = view.findViewById(R.id.detail_points_text);
        TextView username = view.findViewById(R.id.detail_username_text);
        TextView qrname = view.findViewById(R.id.detail_qrname_text);

        String data1 = getArguments().getString("key1");
        String data2 = getArguments().getString("key2");
        String data3 = getArguments().getString("key3");
        String data4 = getArguments().getString("key4");
        String data5 = getArguments().getString("key5");
        String data6 = getArguments().getString("key6");
        String data7 = getArguments().getString("key7");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        address.setText("Address: " + data1);
        lat.setText("Latitude: " + data2);
        lon.setText("Longitude: " + data3);
        date.setText("Date: " + data4);
        points.setText("Points: " + data5);
        username.setText("Username: " + data6);
        qrname.setText("QR Name: " + data7);

        return builder
                .setView(view)
                .setTitle("Detail of QR Code")
                .setNegativeButton("back",null)
                .create();

    }
}


package com.example.sufferqr.ui.main;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.sufferqr.R;

import java.util.ArrayList;
import java.util.List;


public class QRQuickViewSameQRFragment extends Fragment {

    private Bundle bundle;
    private View view;

    private String qrCodeUrl; // the URL of the scanned QR code

    private List<String> comments; // a list of comments related to the QR

    public QRQuickViewSameQRFragment(Bundle mybundle) {
        // Required empty public constructor
        bundle = mybundle;
        qrCodeUrl = bundle.getString("qr_code_url");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // bundle contain all document under this qrcode,however the query is created on database
        // xml for this fragment is .ui.main.QRQuickViewSameQRFragment
        // context suggest use requireContext()
        // to get to a complement use view.findbyviewid(...)
        // to see realtime updating list see scan-history


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_q_r_quick_view_same_q_r, container, false);

        // Load the comments related to the QR code from the database
        loadCommentsFromDatabase();

        // Display the comments on the screen
        displayComments();

        return view;
    }

    private void loadCommentsFromDatabase() {

    }

    private void displayComments() {

    }


}
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

/**
 * Adapter for same QR user lists
 */
public class SameQrListAdapter extends ArrayAdapter<sameQrListContext> {
    private ArrayList<sameQrListContext> rec;
    private Context context;

    /**
     * set up list item
     */
    public SameQrListAdapter(Context context, ArrayList<sameQrListContext> qrname){
        super(context,0, qrname);
        this.rec = qrname;
        this.context = context;
    }


    /**
     * load each list item to its ui
     * @param position touched position
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.fragment_q_r_quick_view_list_content, parent,false);
        }

        sameQrListContext qr = rec.get(position);

        TextView QRuser = view.findViewById(R.id.same_qr_genereal_context_name);
        String ss = qr.getName();
        System.out.println(ss);
        if (ss!=null){
            QRuser.setText(ss);
        }
        return view;
    }
}

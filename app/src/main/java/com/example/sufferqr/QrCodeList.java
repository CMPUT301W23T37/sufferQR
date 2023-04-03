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

    /**
     * class set uo
     * @param context context
     * @param qrCode qrcode
     */
    public QrCodeList(Context context, ArrayList<QrCode> qrCode){
        super(context,0, qrCode);
        this.qrCodes = qrCode;
        this.context = context;
    }

    /**
     * write content
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return view item
     */
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
        date.setText("Distance: " + qrCode.getDis() +" M");

        return view;

    }
}


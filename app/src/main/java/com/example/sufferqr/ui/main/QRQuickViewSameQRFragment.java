package com.example.sufferqr.ui.main;


import android.os.Bundle;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.sufferqr.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QRQuickViewSameQRFragment extends Fragment {

    private Bundle bundle;
    private View view;

    private String qrCodeUrl; // the URL of the scanned QR code

    private List<String> players; // a list of players who scanned the same QR code
    private int numScans; // the number of times the QR code was scanned

    public QRQuickViewSameQRFragment(Bundle mybundle) {
        // Required empty public constructor
        bundle = mybundle;
        qrCodeUrl = bundle.getString("qr_code_url");
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


        // Initialize the players list
        players = new ArrayList<>();

        // Query the Firestore database to retrieve all documents that have the same QR code URL
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("qr_scans")
                .whereEqualTo("url", qrCodeUrl)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Count the number of documents returned by the query
                        numScans = task.getResult().size();

                        // Retrieve the names, dates, and times of the players who scanned the same QR code
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String player = document.getString("player");
                            Date date = document.getDate("date");
                            String dateString = new SimpleDateFormat("MM/dd/yyyy").format(date);
                            String timeString = new SimpleDateFormat("hh:mm a").format(date);
                            String playerString = player + " " + dateString + " " + timeString;
                            players.add(playerString);
                        }


                        // Display the number of times the QR code was scanned and the list of players who scanned it
                        displayData();
                    }
                });

        return view;

    }


    private void displayData() {
        // Display the number of times the QR code was scanned by the current player
        TextView playerScansTextView = view.findViewById(R.id.player_scans_text_view);
        String playerScansText = "You have scanned this QR code " + numScans + " times.";
        playerScansTextView.setText(playerScansText);

        // Display the list of other players who have scanned the same QR code
        TextView otherPlayersTextView = view.findViewById(R.id.other_players_text_view);
        StringBuilder otherPlayersText = new StringBuilder();
        for (String player : players) {
            otherPlayersText.append(player).append("\n");
        }
        otherPlayersTextView.setText(otherPlayersText.toString());
    }
}
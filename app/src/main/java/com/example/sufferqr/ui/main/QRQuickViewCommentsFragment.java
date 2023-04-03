package com.example.sufferqr.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sufferqr.CommentPage;
import com.example.sufferqr.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Value;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Commenting fragment
 */
public class QRQuickViewCommentsFragment extends Fragment {

    Bundle bundle;
    View view;
    String qrName,localUser,QROwner;
    ListView commentsList;
    ArrayList<QRQuickViewComment> dataList;
    ArrayAdapter<QRQuickViewComment> commentsAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * launch
     * @param myBundle data transfer
     */
    public QRQuickViewCommentsFragment(Bundle myBundle) {
        // Required empty public constructor
        bundle = myBundle;
    }

    /**
     * create
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * launch the activity
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // bundle contain all document under this qrcode,however the document is created on database
        // xml for this fragment is .ui.main.QRQuickViewCommentsFragment
        // context suggest use requireContext()
        // to get to a complement use view.findViewById(...)
        // to see realtime updating list see scan-history
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_q_r_quick_view_comments, container, false);

        FloatingActionButton fab = view.findViewById(R.id.add_comment_fragment);

        commentsList = view.findViewById(R.id.comment_list);
        dataList = new ArrayList<>();
        commentsAdapter = new QRQuickViewCommentsArrayAdapter(requireContext(), dataList);
        commentsList.setAdapter(commentsAdapter);

        qrName = bundle.getString("QRname");

        // android ID
        localUser = bundle.getString("localUser");
        QROwner = bundle.getString("user");

        Intent in = new Intent(requireContext(), CommentPage.class);
        in.putExtra("QRName", qrName);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                in.putExtra("title", "Add Comment");
                startActivity(in);
            }
        });

        commentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                in.putExtra("title", "Delete Comment");
                in.putExtra("commentContent", dataList.get(i).getComment());
                in.putExtra("commentOwner", dataList.get(i).getAndroidId());

                if (Objects.equals(localUser, QROwner)) {
                    in.putExtra("Owner", "1");
                } else {
                    in.putExtra("Owner", "0");
                }

                startActivity(in);
            }
        });

        CollectionReference comRef = db.collection("GameQrCode").document(qrName).collection("Comment");
        comRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                dataList.clear();
                for (QueryDocumentSnapshot doc: value) {
                    Log.d("Comments", String.valueOf(doc.getData().get("comment")));
                    String androidId = (String) doc.getData().get("androidId");
                    String date = (String) doc.getData().get("cdate");
                    String comment = (String) doc.getData().get("comment");
                    String uName = (String) doc.getData().get("userName");
                    //String docId = doc.getId();
                    dataList.add(new QRQuickViewComment(uName, date, comment, androidId));
                }
                commentsAdapter.notifyDataSetChanged();
            }
        });


        return view;
    }


}
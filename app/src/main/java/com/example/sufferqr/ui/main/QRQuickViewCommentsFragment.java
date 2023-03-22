package com.example.sufferqr.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sufferqr.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class QRQuickViewCommentsFragment extends Fragment implements QRQuickViewCommentsDialog.AddCommentDialogListener{

    Bundle bundle;
    View view;

    public QRQuickViewCommentsFragment(Bundle myBundle) {
        // Required empty public constructor
        bundle = myBundle;
    }

    private ArrayList<QRQuickViewComment> dataList;
    private ListView commentsList;
    private QRQuickViewCommentsArrayAdapter commentsAdapter;

    @Override
    public void addComment(QRQuickViewComment newCom) {
        commentsAdapter.add(newCom);
        commentsAdapter.notifyDataSetChanged();
    }

    @Override
    public void editComment(String comContent, int position) {
        dataList.get(position).setComment(comContent);
        commentsAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteComment(int i) {
        dataList.remove(i);
        commentsAdapter.notifyDataSetChanged();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // bundle contain all document under this qrcode,however the document is created on database
        // xml for this fragment is .ui.main.QRQuickViewCommentsFragment
        // context suggest use requireContext()
        // to get to a complement use view.findbyviewid(...)
        // to see realtime updating list see scan-history
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_q_r_quick_view_comments, container, false);

        dataList = new ArrayList<>();
        commentsList = view.findViewById(R.id.quick_view_comment_list);
        commentsAdapter = new QRQuickViewCommentsArrayAdapter(requireContext(), dataList);
        commentsList.setAdapter(commentsAdapter);
        FloatingActionButton fab = view.findViewById(R.id.add_comment);

        // builds a dialog when add button is clicked
        fab.setOnClickListener(v -> {
            new QRQuickViewCommentsDialog().show(getFragmentManager(), "Add Comment");
        });

        commentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                QRQuickViewCommentsDialog.newInstance(commentsAdapter.getItem(i), i).show(getFragmentManager(), "Edit Comment");
            }
        });
        return view;
    }


}
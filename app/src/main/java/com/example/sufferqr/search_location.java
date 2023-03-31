package com.example.sufferqr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class search_location  extends DialogFragment {
    interface SearchDialogListener {
        void onSearch(String latitude, String longitude);
    }

    private SearchDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SearchDialogListener) {
            listener = (SearchDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement SearchDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view =
                LayoutInflater.from(getContext()).inflate(R.layout.search_location_fragment, null);
        EditText editLatitude = view.findViewById(R.id.location_latitude);
        EditText editLongitude = view.findViewById(R.id.location_lontitude);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Search location")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Search", (dialog, which) -> {
                    String latitude = editLatitude.getText().toString();
                    String longitude = editLongitude.getText().toString();
                    listener.onSearch(latitude, longitude);
                })
                .create();
    }
}

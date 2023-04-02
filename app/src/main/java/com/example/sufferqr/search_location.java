package com.example.sufferqr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;


/**
 * DialogFragment to search for a location using Google Places API
 * invoke when user click on the search button
 */
public class search_location extends DialogFragment {
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    interface SearchDialogListener {
        void onSearch(String address);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the SDK
        Places.initialize(requireContext(), "AIzaSyBl3Acwz4pkNuDzIkvEVW-wZIVKFHg19hs");

        // Launch the Autocomplete Activity
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME))
                .build(requireContext());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == AutocompleteActivity.RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                LatLng searchedLocation = place.getLatLng();
                if (searchedLocation != null) {
                    listener.onSearch(place.getName());
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.e("PlaceAutocomplete", "An error occurred: " + status);
            }

            // Close the dialog after getting the result
            dismiss();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

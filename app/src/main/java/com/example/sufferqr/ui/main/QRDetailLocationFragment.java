package com.example.sufferqr.ui.main;



import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

import android.Manifest;
import android.annotation.SuppressLint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sufferqr.MainActivity;
import com.example.sufferqr.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QRDetailLocationFragment} factory method to
 * create an instance of this fragment.
 */
public class QRDetailLocationFragment extends Fragment implements OnMapReadyCallback, MapboxMap.OnCameraIdleListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int REQUEST_CHECK_SETTING = 10;
    private static final long UPDATE_INTERVAL = 10;
    private static final long FAST_UPDATE_IN_ML = 100;
    private static final String TAG = MainActivity.class.getSimpleName();
    MapboxMap mapboxMap;
    MapView mapView;


    private FusedLocationProviderClient fusedLocationClient;
    private SettingsClient settingsClient;
    private com.google.android.gms.location.LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationCallback locationCallback;
    private Location locationCurrent;
    private boolean reqLocationUpdate = false;

    private OnFragmentInteractionListener listener;

    // new/visitor/modify
    String mode = "new", localName, localAdress;

    Bundle mymapBundle;
    String userName;
    Double localLongtiude = -113.5257, localLatiude = 53.5282;
    SwitchMaterial locEnable;
    TextView privacy_text;
    CardView poi_card, map_card;
    Boolean updatePaused = false;
    View gbview;
    Button search;

    /**
     * launch
     * @param mapBundle get info from qrdetailactivity
     */
    public QRDetailLocationFragment(Bundle mapBundle) {
        mymapBundle = mapBundle;
    }

    /**
     * response when map stop moving around
     */
    @Override
    public void onCameraIdle() {
        if (!updatePaused) {
            if (mapboxMap != null && (Objects.equals(mode, "new") || Objects.equals(mode, "modified"))) {
                double newLongtiude = mapboxMap.getCameraPosition().target.getLongitude();
                double newLatitude = mapboxMap.getCameraPosition().target.getLatitude();
                updatePOI(gbview, newLongtiude, newLatitude);
            }
        } else {
            updatePaused = false;
        }
    }

    /**
     * setup map
     * @param mapboxMap An instance of MapboxMap associated with the  or
     *                  {@link MapView} that defines the callback.
     */
    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        QRDetailLocationFragment.this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
            // Add a marker on the map's center/"target" for the place picker UI
            ImageView hoveringMarker = new ImageView(requireContext());
            hoveringMarker.setImageResource(R.drawable.ic_red_dot);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            hoveringMarker.setLayoutParams(params);
            mapView.addView(hoveringMarker);
        });

        if (Objects.equals(mode, "new")){
            Dexter.withContext(requireActivity())
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            startLocationUpdate();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                            // do nothing
                            CameraPosition position = new CameraPosition.Builder().target(new LatLng(localLatiude, localLongtiude)).zoom(15).tilt(20).build();
                            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();
        }else {
            // ccis
            CameraPosition position = new CameraPosition.Builder()
                    .target(new LatLng(localLatiude, localLongtiude))
                    .zoom(15)
                    .tilt(20)
                    .build();
            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);
        }
        mapboxMap.addOnCameraIdleListener(QRDetailLocationFragment.this);
    }



    /**
     * launch listener send to Qr detail Activity
     * @see com.example.sufferqr.QRDetailActivity
     */
    public interface OnFragmentInteractionListener {
        void onLocationUpdate(MapboxMap mapboxMap, Boolean btOn, Double longitude, Double latitude, String name, String address);

    }

    /**
     * launch
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode = mymapBundle.getString("mode");
        if (mode.length() < 1) {
            mode = "new";
        }
        userName = mymapBundle.getString("user");
    }

    /**
     * attach the listener
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof QRDetailLocationFragment.OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }

    /**
     * launch view and setting up map
     * @return view
     * @see com.example.sufferqr.QRDetailActivity
     * @see MapboxMap
     * @see Mapbox
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Mapbox.getInstance(getApplicationContext(), getResources().getString(R.string.mapbox_access_token));
        View view = inflater.inflate(R.layout.fragment_q_r_detail_location, container, false);
        gbview = view;

        locEnable = view.findViewById(R.id.qr_detail_location_enable_switch);
        privacy_text = view.findViewById(R.id.qr_detail_location_privacy_text);
        map_card = view.findViewById(R.id.qr_detail_location_map_cardview);
        poi_card = view.findViewById(R.id.qr_detail_location_poi_cardview);
        search = view.findViewById(R.id.qr_detail_loacation_search);

        // init with mapwill to university of alberta ccis
        // Initialize the mapboxMap view
        mapView = (MapView) view.findViewById(R.id.qr_detail_location_content_map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        listener.onLocationUpdate(mapboxMap, true, 0.0, 0.0, "", "");

        // switch
        locEnable.setOnClickListener(v -> switch_operations(view, "user", true));

        if (Objects.equals(mode, "visitor")) {
            switch_operations(view, "auto_off", true);
            privacy_text.setText("following information provided by mapbox");
        } else if (Objects.equals(mode, "modfied")) {
            switch_operations(view, "auto_off", true);
        } else {
            switch_operations(view, "auto_on", true);
        }

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new PlaceAutocomplete.IntentBuilder().accessToken(getResources().getString(R.string.mapbox_access_token))
                        .placeOptions(PlaceOptions.builder().backgroundColor(getResources().getColor(R.color.white)).build())
                        .build(getActivity());
                startActivityForResult(intent, 10322);
            }
        });

        // if mode -- new get geolocation
        if (mode.equals("new")) {
            // when a at adding mode will get location
            switchLocUpdate(view);
        }

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * update geolocation poi,when user location getted
     * @param view view from onCreative view
     * @param locMyLatiude user location
     * @param locMyLongtiude user location
     */
    private void updatePOI(View view, double locMyLongtiude, double locMyLatiude) {
        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(getResources().getString(R.string.mapbox_access_token))
                .query(Point.fromLngLat(locMyLongtiude, locMyLatiude))
                .geocodingTypes(GeocodingCriteria.TYPE_LOCALITY, GeocodingCriteria.TYPE_ADDRESS,
                        GeocodingCriteria.TYPE_POSTCODE, GeocodingCriteria.TYPE_POI_LANDMARK)
                .build();
        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {

                List<CarmenFeature> results = response.body().features();
                // write information back to textVIew
                if (results.size() > 0) {
                    fillInAdress(view, results.get(0).text(), results.get(0).placeName(), locMyLongtiude, locMyLatiude);
                    listener.onLocationUpdate(mapboxMap, locEnable.isChecked(), locMyLongtiude, locMyLatiude, results.get(0).text(), results.get(0).placeName());
                } else {
                    // No result for your request were found.
                    Log.d(TAG, "onResponse: No result found");
                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "fetch fail", Toast.LENGTH_SHORT).show();
            }


        });
    }

    /**
     * new data result callback
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 10322) {
            updatePaused = true;
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            Point point = feature.center();
            Double latitude = point.latitude();
            Double longitude = point.longitude();

            fillInAdress(gbview, feature.text(), feature.placeName(), longitude, latitude);
            listener.onLocationUpdate(mapboxMap, locEnable.isChecked(), longitude, latitude, feature.text(), feature.placeName());
            CameraPosition position = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(15).tilt(20).build();
            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);
        }
    }

    /**
     * fill in the poi information on the screen
     * @param myView view
     * @param AddressName poi tittle
     * @param Address adress name
     * @param locMyLongtiude location longtiude
     * @param locMyLatiude location latitude
     */
    private void fillInAdress(View myView, String AddressName, String Address, Double locMyLongtiude, Double locMyLatiude) {
        TextView textView_name = myView.findViewById(R.id.qr_detail_loacation_name);
        textView_name.setText(AddressName);
        localName = AddressName;
        TextView textView_address = myView.findViewById(R.id.qr_detail_loacation_address);
        textView_address.setText(Address);
        localAdress = Address;
        textView_address.setVisibility(View.VISIBLE);
        TextView textView_longtiude = myView.findViewById(R.id.qr_detail_loacation_longtiude);
        textView_longtiude.setText("longtiude:" + locMyLongtiude);
        localLongtiude = locMyLongtiude;
        textView_longtiude.setVisibility(View.VISIBLE);
        TextView textView_latitude = myView.findViewById(R.id.qr_detail_loacation_latitude);
        textView_latitude.setText("latitude:" + locMyLatiude);
        localLatiude = locMyLatiude;
        textView_latitude.setVisibility(View.VISIBLE);
    }

    /**
     * when pricy setting how shold card react settings
     * @param view current vire
     * @param noting note
     * @param options booean for additional note
     */
    private void switch_operations(View view, String noting, Boolean options) {
        locEnable = view.findViewById(R.id.qr_detail_location_enable_switch);
        listener.onLocationUpdate(mapboxMap, locEnable.isChecked(), localLongtiude, localLatiude, localName, localAdress);
        if (locEnable.isChecked() || Objects.equals(noting, "info_on")) {
            poi_card.setVisibility(View.VISIBLE);
            map_card.setVisibility(View.VISIBLE);
        } else if (!locEnable.isChecked() || Objects.equals(noting, "info_off")) {
            poi_card.setVisibility(View.INVISIBLE);
            map_card.setVisibility(View.INVISIBLE);
        }
        if (Objects.equals(noting, "auto_off") || (Objects.equals(noting, "info_off") && !options)) {
            locEnable.setEnabled(false);
        } else if (Objects.equals(noting, "auto_on") || Objects.equals(noting, "info_on")) {
            if (Objects.equals(noting, "info_on") && (!options)) {
                locEnable.setEnabled(false);
                privacy_text.setVisibility(View.INVISIBLE);
            } else {
                locEnable.setEnabled(true);
            }
        }
    }

    /**
     * confirm permission and launch finding user location
     * @see Dexter
     * @see com.mapbox.android.core.permissions.PermissionsManager
     */
    private void switchLocUpdate(View view) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        settingsClient = LocationServices.getSettingsClient(requireActivity());
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                locationCurrent = locationResult.getLastLocation();
                double latiude = locationCurrent.getLatitude();
                double longtiude = locationCurrent.getLongitude();
                // remove a focus
                CameraPosition position = new CameraPosition.Builder().target(new LatLng(latiude, longtiude)).zoom(13).tilt(20).build();
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);
                stopLocationUpdate();
            }
        };
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL)
                .setWaitForAccurateLocation(false).setMinUpdateDistanceMeters(FAST_UPDATE_IN_ML).setMaxUpdateDelayMillis(10000).build();
        LocationSettingsRequest.Builder builder1 = new LocationSettingsRequest.Builder();
        builder1.addLocationRequest(locationRequest);
        locationSettingsRequest = builder1.build();
        // listener runner,and check if permission exist
    }


    /**
     * load location,let user chose if permission access
     * @see LocationRequest
     * @see LocationCallback
     * @see FusedLocationProviderClient
     */
    private void startLocationUpdate(){
        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(requireActivity() , new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")   // it will work with or with out this line
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }).addOnFailureListener(requireActivity(), (OnFailureListener) e -> Toast.makeText(requireContext(),"precise location required,check your setting",Toast.LENGTH_SHORT).show());
    }

    /**
     * stop location update
     * @see FusedLocationProviderClient
     */
    private void stopLocationUpdate(){
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    /**
     * check permission
     * @see com.mapbox.android.core.permissions.PermissionsManager
     */
    private boolean checkPremmissions(){
        int permissionState= ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION);
        return   (permissionState== PackageManager.PERMISSION_GRANTED);
    }

    /**
     * other than new mode show content to the fragment
     * @param userName11 username
     * @param  data data from firebase
     * @see MapView
     */
    public void ActivityCallBack(String userName11,HashMap<String, Object> data){
        // map page chage load info
        Boolean LocE = (Boolean)data.get("LocationExist");
        if (Boolean.FALSE.equals(LocE)){
            // if not the creator disble change option
            locEnable.setChecked(false);
            switch_operations(gbview,"info_off",Objects.equals((String) data.get("user"), userName11));
        } else {
            locEnable.setChecked(true);
            switch_operations(gbview,"info_on",Objects.equals((String) data.get("user"), userName11));
            //load info
            Double lat = (Double) data.get("LocationLatitude");
            Double longit = (Double) data.get("LocationLongitude");
            fillInAdress(gbview,(String) data.get("LocationName"),(String) data.get("LocationAddress"),longit,lat);
            // draw map
            MapView mapView=gbview.findViewById(R.id.qr_detail_location_content_map_view);
            mapView.onCreate(getArguments());
            mapView.getMapAsync(this);
        }
    }

    /**
     * mapbox life cycle async
     * @see MapView
     */
    @Override
    @SuppressWarnings( {"MissingPermission"})
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    /**
     * mapbox life cycle async
     * @see MapView
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if(reqLocationUpdate && checkPremmissions()){
            startLocationUpdate();
        }
    }

    /**
     * mapbox life cycle async
     * @see MapView
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        if(reqLocationUpdate){
            stopLocationUpdate();
        }
    }

    /**
     * mapbox life cycle async
     * @see MapView
     */
    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    /**
     * mapbox life cycle async
     * @see MapView
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * mapbox life cycle async
     * @see MapView
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * mapbox life cycle async
     * @see MapView
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
package com.example.sufferqr.ui.main;


import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

import android.Manifest;
import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sufferqr.MainActivity;
import com.example.sufferqr.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
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
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
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
public class QRDetailLocationFragment extends Fragment implements OnMapReadyCallback, MapboxMap.OnCameraIdleListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final  int REQUEST_CHECK_SETTING = 10;
    private static  final long UPDATE_INTERVAL =10;
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
    String mode= "new",localName,localAdress;

    Bundle mymapBundle,sIState;
    String userName;
    Double localLongtiude,localLatiude;
    SwitchMaterial locEnable;
    TextView privacy_text;
    CardView poi_card,map_card;
    Boolean LocExist=false;

    View gbview;

    /**
     * launch
     */
    public QRDetailLocationFragment(Bundle mapBundle) {
        mymapBundle=mapBundle;
    }

    /**
     * response when map stop moving around
     */
    @Override
    public void onCameraIdle() {
        if (mapboxMap != null && Objects.equals(mode, "new")) {
            double newLongtiude = mapboxMap.getCameraPosition().target.getLongitude();
            double newLatitude = mapboxMap.getCameraPosition().target.getLatitude();
            updatePOI(gbview,newLongtiude,newLatitude);
        }
    }

    /**
     * launch the map settings
     */
    @Override
    @SuppressWarnings( {"MissingPermission"})
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
        // ccis
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(53.5282, -113.5257))
                .zoom(15)
                .tilt(20)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);
        mapboxMap.addOnCameraIdleListener(QRDetailLocationFragment.this);
    }

    /**
     * launch listener send to Qr detail Activity
     * @see com.example.sufferqr.QRDetailActivity
     */
    public interface OnFragmentInteractionListener{
        void onLocationUpdate(MapboxMap mapboxMap,Boolean btOn,Double longitude,Double latitude,String name,String address);

    }

    /**
     * launch
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode = mymapBundle.getString("mode");
        if (mode.length()<1){
            mode="new";
        }
        userName =mymapBundle.getString("user");



    }

    /**
     * attach the listener
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof QRDetailLocationFragment.OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        }else {
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
        sIState= savedInstanceState;

        locEnable = view.findViewById(R.id.qr_detail_location_enable_switch);
        privacy_text = view.findViewById(R.id.qr_detail_location_privacy_text);
        map_card=view.findViewById(R.id.qr_detail_location_map_cardview);
        poi_card=view.findViewById(R.id.qr_detail_location_poi_cardview);


        // init with mapwill to university of alberta ccis
        // Initialize the mapboxMap view
        mapView= view.findViewById(R.id.qr_detail_location_content_map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        listener.onLocationUpdate(mapboxMap,true,0.0,0.0,"","");

        if (Objects.equals(mode, "visitor")){
            locEnable.setEnabled(false);
            privacy_text.setText("following information provided by mapbox");

        } else {
            // button listener,send changes information
            locEnable.setOnClickListener(v -> {
                listener.onLocationUpdate(mapboxMap,locEnable.isChecked(),localLongtiude,localLatiude,localName,localAdress);
                if(locEnable.isChecked()){
                    if (mode.equals("new")){
                        poi_card.setVisibility(View.VISIBLE);
                        map_card.setVisibility(View.VISIBLE);
                    }
                }else {
                    if (mode.equals("new")){
                        poi_card.setVisibility(View.INVISIBLE);
                        map_card.setVisibility(View.INVISIBLE);
                    }
                }
            });
            if (!Objects.equals(mode, "new")){
                locEnable.setEnabled(false);
                poi_card.setVisibility(View.INVISIBLE);
                map_card.setVisibility(View.INVISIBLE);
            }
        }


        // if mode -- new get geolocation
        if (mode.equals("new")){
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
            settingsClient=LocationServices.getSettingsClient(requireActivity());
            locationCallback = new LocationCallback() {
                @Override
               public void onLocationResult(@NonNull LocationResult locationResult) {
                    locationCurrent = locationResult.getLastLocation();
                    double latiude = locationCurrent.getLatitude();
                    double longtiude = locationCurrent.getLongitude();

                    // remove a focus
                    CameraPosition position = new CameraPosition.Builder()
                            .target(new LatLng(latiude, longtiude))
                            .zoom(13).tilt(20).build();
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);
                    updatePOI(view,longtiude,latiude);

                    stopLocationUpdate();
                    // show poi information
                    LocExist=true;

                }
            };

            locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,UPDATE_INTERVAL)
                    .setWaitForAccurateLocation(false)
                    .setMinUpdateDistanceMeters(FAST_UPDATE_IN_ML)
                    .setMaxUpdateDelayMillis(10000)
                    .build();

            LocationSettingsRequest.Builder builder1 = new LocationSettingsRequest.Builder();
            builder1.addLocationRequest(locationRequest);
            locationSettingsRequest = builder1.build();

            // when a at adding mode will get location

            switchLocUpdate();
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
    private void updatePOI(View view,double locMyLongtiude,double locMyLatiude){
        MapboxGeocoding mapboxGeocoding =MapboxGeocoding.builder()
                .accessToken(getResources().getString(R.string.mapbox_access_token))
                .query(Point.fromLngLat(locMyLongtiude,locMyLatiude))
                .geocodingTypes(GeocodingCriteria.TYPE_LOCALITY,GeocodingCriteria.TYPE_ADDRESS,
                        GeocodingCriteria.TYPE_POSTCODE,GeocodingCriteria.TYPE_POI_LANDMARK)
                .build();
        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {

                List<CarmenFeature> results = response.body().features();
                // write information back to textVIew
                if (results.size() > 0) {
                    TextView textView_name=view.findViewById(R.id.qr_detail_loacation_name);
                    textView_name.setText(results.get(0).text());
                    localName=results.get(0).text();
                    TextView textView_address=view.findViewById(R.id.qr_detail_loacation_address);
                    textView_address.setText(results.get(0).placeName());
                    localAdress=results.get(0).placeName();
                    textView_address.setVisibility(View.VISIBLE);
                    TextView textView_longtiude=view.findViewById(R.id.qr_detail_loacation_longtiude);
                    textView_longtiude.setText("longtiude:"+locMyLongtiude);
                    localLongtiude=locMyLongtiude;
                    textView_longtiude.setVisibility(View.VISIBLE);
                    TextView textView_latitude=view.findViewById(R.id.qr_detail_loacation_latitude);
                    textView_latitude.setText("latitude:"+locMyLatiude);
                    localLatiude=locMyLatiude;
                    textView_latitude.setVisibility(View.VISIBLE);

                    listener.onLocationUpdate(mapboxMap,locEnable.isChecked(),locMyLongtiude,locMyLatiude,results.get(0).text(),results.get(0).placeName());
                } else {
                    // No result for your request were found.
                    Log.d(TAG, "onResponse: No result found");
                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                Toast.makeText(requireContext(),"fetch fail",Toast.LENGTH_SHORT).show();
            }


        });
    }


    /**
     * confirm permission and launch finding user location
     * @see Dexter
     * @see com.mapbox.android.core.permissions.PermissionsManager
     */
    private void switchLocUpdate(){
        // listener runner,and check if permission exist
        Dexter.withContext(requireActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION )
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {reqLocationUpdate = true;startLocationUpdate();}

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        // do nothing
                        Toast.makeText(getApplicationContext(),"Fine location required, check your settings.",Toast.LENGTH_SHORT).show();
                        locEnable.setChecked(false);
                        poi_card.setVisibility(View.INVISIBLE);
                        map_card.setVisibility(View.INVISIBLE);
                        listener.onLocationUpdate(mapboxMap,false,0.0,0.0,"","");
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {permissionToken.continuePermissionRequest();}
                }).check();
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
                }).addOnFailureListener(requireActivity(), (OnFailureListener) new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG,"Location setthings are not satified, upgrade location settings");
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTING);
                                    //rae.startResolutionForResult(QRDetailLocationFragment.this,REQUEST_CHECK_SETTING);
                                } catch (IntentSender.SendIntentException sie){
                                    Log.i(TAG,"peddingIntent unable to execuate request");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String msg = "no precise location";
                                Log.e(TAG,msg);
                                break;
                        }
                    }
                });
    }

    /**
     * stop location update
     * @see FusedLocationProviderClient
     */
    private void stopLocationUpdate(){
        fusedLocationClient.removeLocationUpdates(locationCallback).addOnCompleteListener(requireActivity(),task -> Log.d(TAG,"LocationSTops"));
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
            locEnable.setEnabled(false);
            poi_card.setVisibility(View.INVISIBLE);
            poi_card.setVisibility(View.INVISIBLE);
            map_card.setVisibility(View.INVISIBLE);
        } else {
            //load info
            locEnable.setChecked(true);
            if (Objects.equals((String) data.get("user"), userName11)) {
                locEnable.setEnabled(true);
                privacy_text.setVisibility(View.VISIBLE);
            } else {
                locEnable.setEnabled(false);
                privacy_text.setVisibility(View.INVISIBLE);
            }
            TextView LocationName, address, latitude, longitude;
            poi_card.setVisibility(View.VISIBLE);
            map_card.setVisibility(View.VISIBLE);

            Double lat = (Double) data.get("LocationLatitude");
            Double longit = (Double) data.get("LocationLongitude");
            LocationName = gbview.findViewById(R.id.qr_detail_loacation_name);
            address = gbview.findViewById(R.id.qr_detail_loacation_address);
            address.setVisibility(View.VISIBLE);
            latitude = gbview.findViewById(R.id.qr_detail_loacation_latitude);
            latitude.setVisibility(View.VISIBLE);
            longitude = gbview.findViewById(R.id.qr_detail_loacation_longtiude);
            longitude.setVisibility(View.VISIBLE);
            LocationName.setText((String) data.get("LocationName"));
            latitude.setText("Latitude" + lat);
            longitude.setText("Longitude" + longit);
            address.setText((String) data.get("LocationAddress"));


            // draw map
            MapView mapView=gbview.findViewById(R.id.qr_detail_location_content_map_view);
            mapView.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(@NonNull MapboxMap mapboxMap1) {
                                    mapboxMap1.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                                        @Override
                                        public void onStyleLoaded(@NonNull Style style) {
                                            style.addImage("red-pin", BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_red_dot)));
                                            style.addLayer(new SymbolLayer("icon-layer-id","icon-source-id").withProperties(
                                                    iconImage("red-pin"),
                                                    iconIgnorePlacement(true),
                                                    iconAllowOverlap(true),
                                                    iconOffset(new Float[]{0f,-0f})
                                            ));
                                        }
                                    });
                                    // ccis
                                    CameraPosition position = new CameraPosition.Builder().target(new LatLng(lat, longit)).zoom(15).tilt(20).build();
                                    mapboxMap1.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);
                                }
                            });
        }
    }

}
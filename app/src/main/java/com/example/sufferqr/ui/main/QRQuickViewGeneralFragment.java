package com.example.sufferqr.ui.main;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.Annotation;
import android.text.Layout;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sufferqr.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.AnnotationManager;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;


public class QRQuickViewGeneralFragment extends Fragment  implements OnMapReadyCallback,MapboxMap.OnCameraIdleListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    MapView mapView;
    MapboxMap mapboxMapGlobal;
    Bundle bundle;

    TextView author,points,date,address_name,address;
    TextInputEditText visual;

    CardView location_card,map_card;

    View gbView;


    public QRQuickViewGeneralFragment(Bundle myBundle) {
        // Required empty public constructor
        bundle = myBundle;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Mapbox.getInstance(getApplicationContext(), getResources().getString(R.string.mapbox_access_token));
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_q_r_quick_view_general, container, false);
        gbView =view;

//        // init with mapwill to university of alberta ccis
//        // Initialize the mapboxMap view
        mapView= (MapView) view.findViewById(R.id.fragment_q_r_quick_view_general_content_map_view);
        author = view.findViewById(R.id.fragment_q_r_quick_view_general_author);
        points = view.findViewById(R.id.fragment_q_r_quick_view_general_points);
        date = view.findViewById(R.id.fragment_q_r_quick_view_general_date);

        visual = view.findViewById(R.id.fragment_q_r_quick_view_general_general_visual_text);

        location_card = view.findViewById(R.id.fragment_q_r_quick_view_general_location_info_cardView);
        address_name = view.findViewById(R.id.fragment_q_r_quick_view_general_location_address_name);
        address = view.findViewById(R.id.fragment_q_r_quick_view_general_location_address);

        map_card = view.findViewById(R.id.fragment_q_r_quick_view_general_location_map_cardView);

        //mapView= view.findViewById(R.id.qr_detail_location_content_map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        if (bundle!=null){
            loadInfo();
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        QRQuickViewGeneralFragment.this.mapboxMapGlobal = mapboxMap;




        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                style.addImage("red-pin", BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_red_dot)));
                style.addLayer(new SymbolLayer("icon-layer-id","icon-red-pin")

                        .withProperties(
                        iconImage("red-pin"),
                        iconIgnorePlacement(true),
                        iconAllowOverlap(true),
                        iconOffset(new Float[]{0f,-0f})

                ));
                // add pint
                GeoJsonSource destinationIconGeoJsonSource = style.getSourceAs("icon-red-pin");
                Double latitude,longitude;
                latitude =  Double.parseDouble(bundle.getString("LocationLatitude"));
                longitude = Double.parseDouble(bundle.getString("LocationLongitude"));
                if (destinationIconGeoJsonSource != null) {
                    destinationIconGeoJsonSource.setGeoJson(Feature.fromGeometry(Point.fromLngLat(longitude, latitude)));
                    style.addSource(destinationIconGeoJsonSource);
                }





            }
        });



        // Add symbol at specified lat/lon

        // Create an Icon object for the marker to use





        // ccis
        boolean b1= Boolean.parseBoolean(bundle.getString("LocationExist"));
        CameraPosition position;
        if (b1){
            Double latitude,longtitude;
            latitude =  Double.parseDouble(bundle.getString("LocationLatitude"));
            longtitude = Double.parseDouble(bundle.getString("LocationLongitude"));
            LatLng latLng = new LatLng(latitude, longtitude);

            position = new CameraPosition.Builder().target(latLng).zoom(13).tilt(20).build();
        }else {
            position = new CameraPosition.Builder().target(new LatLng(53.5282, -113.5257)).zoom(15).tilt(20).build();
        }

        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);
        mapboxMapGlobal=mapboxMap;
    }

    public void loadInfo(){
        author.setText(bundle.getString("user","author"));
        points.setText(bundle.getString("points","0"));
        date.setText(bundle.getString("date","1970-01-01"));
        visual.setText(bundle.getString("QVisual"));

        boolean b1= Boolean.parseBoolean(bundle.getString("LocationExist"));
        if (!b1){
            location_card.setVisibility(View.GONE);
            map_card.setVisibility(View.GONE);
        } else {
            address_name.setText(bundle.getString("LocationName"));
            address.setText(bundle.getString("LocationAddress"));

            Double latitude,longtitude;
            latitude =  Double.parseDouble(bundle.getString("LocationLatitude"));
            longtitude = Double.parseDouble(bundle.getString("LocationLongitude"));

        }

    }



    /**
     * mapbox life cycle async
     * @see MapView
     */
    @Override
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
    }

    /**
     * mapbox life cycle async
     * @see MapView
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
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

    @Override
    public void onCameraIdle() {

    }
}
package com.example.sufferqr;

import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContentProviderCompat.requireContext;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.sufferqr.databinding.ActivityQrdetailBinding;
import com.example.sufferqr.ui.main.QRDetailGeneralFragment;
import com.example.sufferqr.ui.main.QRDetailImageFragment;
import com.example.sufferqr.ui.main.QRDetailLocationFragment;
import com.example.sufferqr.ui.main.SectionsPagerAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class QRDetailActivity extends AppCompatActivity implements QRDetailLocationFragment.OnFragmentInteractionListener,
        QRDetailImageFragment.OnFragmentInteractionListener, QRDetailGeneralFragment.OnFragmentInteractionListener
          {

    private ActivityQrdetailBinding binding;



    // map and location settings
    private static final int REQUEST_CODE = 5678;
    private static final  int REQUEST_CHECK_SETTING = 10;
    private static  final long UPDATE_INTERVAL =10;
    private static final long FAST_UPDATE_IN_ML = 100;
    private static final String TAG = MainActivity.class.getSimpleName();
  private FirebaseStorage storage;
    MapboxMap mapboxMapGlobal;
    MapView mapView;
    MapboxMap map;

    private FirebaseFirestore db;

    TextView s1,s2,s3;
    private FusedLocationProviderClient fusedLocationClient;
    private SettingsClient settingsClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationCallback locationCallback;
    private Location locationCurrent;
    private boolean reqLocationUpdate = false;
    private HashMap <String,Object> data;
    String mode,userName,QRname,QRstring;

    Uri imageUri;


    Button CancelBt,ConfirmBt;
    Bundle mapBundle,imageBundle,GeneralBundle;


    private PermissionsManager permissionsManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        binding = ActivityQrdetailBinding.inflate(getLayoutInflater());
        Mapbox.getInstance(getApplicationContext(), getResources().getString(R.string.mapbox_access_token));
        storage = FirebaseStorage.getInstance();
        setContentView(binding.getRoot());

        Intent myNewIntent = getIntent();
        mode = myNewIntent.getStringExtra("mode");
        userName = myNewIntent.getStringExtra("user");

        if (!Objects.equals(mode, "new")){
            QRname = myNewIntent.getStringExtra("qrID");
        } else if (mode.equals("new")) {
            QRstring = myNewIntent.getStringExtra("QRString");
            String uriString = myNewIntent.getStringExtra("QRimageUri");
            imageUri = Uri.parse(uriString);
        }

        System.out.println(QRstring);

        mapBundle = new Bundle();
        imageBundle = new Bundle();
        GeneralBundle = new Bundle();
        mapBundle.putString("mode",mode);
        imageBundle.putString("mode",mode);
        GeneralBundle.putString("mode",mode);


        if (Objects.equals(mode, "new")){
            imageBundle.putString("QRString",QRstring);
            imageBundle.putString("imageUri",imageUri.toString());

            System.out.println(QRstring);
        }

        data = new HashMap<>();


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(),mapBundle,imageBundle,GeneralBundle);
        int limit = (sectionsPagerAdapter.getCount() > 1 ? sectionsPagerAdapter.getCount() -1 : 1);
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(limit);
        viewPager.beginFakeDrag(); // disable drag



        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
//        FloatingActionButton fab = binding.fab;

        CancelBt = findViewById(R.id.Activity_qet_detail_cancel_button);
        ConfirmBt = findViewById(R.id.activity_qr_detail_bottom_bar_confirm_bt);

        CancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if (mode == "new"){
                    try{
                        File fdel = new File(imageUri.getPath());//create path from uri
                        if (fdel.exists()) {
                            fdel.delete();
                        }
                    } catch (Exception e){
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                    }
                }
            }
        });
        ConfirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(mode, "new")){
                    HashMapValidate("user",userName);
                    HashMapValidate("time",new Date());
                    imagePushFirestone();
                } else if (mode.equals("modified")){
                    Boolean b1 = (Boolean)data.get("imageExist");
                    Boolean b2 = (Boolean)data.get("LocationExist");
                    String s1 = (String) data.get("QRpath");
                    if (Boolean.FALSE.equals(b1)){
                        HashMapValidate("QRtext","");
                        if (s1!=""){
                            imageDelFirestone(s1);
                            HashMapValidate("QRpath","");
                        }
                    }
                    if (Boolean.FALSE.equals(b2)){
                        HashMapValidate("LocationLatitude",0.0);
                        HashMapValidate("LocationLongitude",0.0);
                        HashMapValidate("LocationName","");
                        HashMapValidate("LocationAddress","");
                    }
                    GameQrRecordDB DBconnect = new GameQrRecordDB();
                    DBconnect.ChangeQrInfo(QRname,data);
                    finish();

                }


            }
        });

        if (!Objects.equals(mode, "new")){
            if (QRname==""){
                finish();
            }else{
                getContent();
            }
        }
    }

    private void getContent(){
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReferenceDest = db.collection("GameQrCode");
        // check if id is unique in the FameQr datavase
        collectionReferenceDest.document(QRname).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // general
                        TextInputEditText name,points;
                        TextView textView;Button button;
                        name = findViewById(R.id.qr_detail_general_qrtext_name);
                        points = findViewById(R.id.qr_detail_general_qrtext_points);
                        textView = findViewById(R.id.qr_detail_general_qrtext_date);
                        TextInputEditText visual = findViewById(R.id.qr_detail_general_visual_text);

                        button = findViewById(R.id.qr_detail_general_elevatedButton);
                        name.setText((String)document.get("QRname"));
                        Object pt = document.get("points");
                        points.setText(String.valueOf(pt));
                        textView.setText((String)document.get("date"));
                        visual.setText((String)document.get("QVisual"));

                        HashMapValidate("QRname",document.get("QRname"));
                        HashMapValidate("points",document.get("points"));
                        HashMapValidate("date",document.get("date"));
                        HashMapValidate("user",document.get("user"));
                        HashMapValidate("QVisual",document.get("QVisual"));

                        TextInputLayout ttl = findViewById(R.id.qr_detail_general_qrtext_name_layout);
                        ttl.setHelperText("");
                        ttl.setCounterEnabled(false);

//
                        if (Objects.equals((String) document.get("user"), userName)){
                            button.setEnabled(true);
                        }

                        // image
                        TextInputEditText QRcontent;
                        SwitchMaterial imgEnable;
                        QRcontent = findViewById(R.id.qr_detail_image_textfield);
                        imgEnable = findViewById(R.id.qr_detail_image_enable_switch);
                        Boolean imgE = (Boolean) document.get("imageExist");

                        CardView c1= findViewById(R.id.qr_detail_image_qrtext_cardview);
                        CardView c2= findViewById(R.id.qr_detail_image_qrimage_cardview);
                        TextView t1= findViewById(R.id.qr_detail_image_privacy_text);

                        if (!imgE){
                            imgEnable.setChecked(false);
                            imgEnable.setEnabled(false);

                            t1.setVisibility(View.INVISIBLE);
                            c1.setVisibility(View.INVISIBLE);
                            c2.setVisibility(View.INVISIBLE);
                        } else {

                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            imgEnable.setChecked(true);
                            if (userName.equals((String) document.get("user"))){
                                imgEnable.setEnabled(true);
                                t1.setVisibility(View.VISIBLE);
                            } else {
                                imgEnable.setEnabled(false);
                                t1.setVisibility(View.INVISIBLE);
                            }
                            QRcontent.setText((String) document.get("QRtext"));
                            imageFetchFirestone((String) document.get("QRpath"));
                        }

                        HashMapValidate("QRtext",document.get("QRtext"));
                        HashMapValidate("imageExist",document.get("imageExist"));
                        HashMapValidate("QRpath",document.get("QRpath"));

                        // map
                        SwitchMaterial LocEnable;
                        LocEnable = findViewById(R.id.qr_detail_location_enable_switch);
                        Boolean LocE = (Boolean)document.get("LocationExist");

                        HashMapValidate("LocationName",document.get("LocationName"));
                        HashMapValidate("LocationLatitude",document.get("LocationLatitude"));
                        HashMapValidate("LocationLongitude",document.get("LocationLongitude"));
                        HashMapValidate("LocationAddress",document.get("LocationAddress"));

                        CardView mapc1= findViewById(R.id.qr_detail_location_poi_cardview);
                        CardView mapc2= findViewById(R.id.qr_detail_location_map_cardview);
                        TextView mapt1= findViewById(R.id.qr_detail_location_privacy_text);

                        if (Boolean.FALSE.equals(LocE)){
                            LocEnable.setChecked(false);
                            LocEnable.setEnabled(false);
                            mapt1.setVisibility(View.INVISIBLE);
                            mapc1.setVisibility(View.INVISIBLE);
                            mapc2.setVisibility(View.INVISIBLE);
                        } else {
                            LocEnable.setChecked(true);
                            if (Objects.equals((String) document.get("user"), userName)){
                                LocEnable.setEnabled(true);
                                mapt1.setVisibility(View.VISIBLE);
                            } else {
                                LocEnable.setEnabled(false);
                                mapt1.setVisibility(View.INVISIBLE);
                            }
                            TextView LocationName,address,latitude,longitude;
                            mapc1.setVisibility(View.VISIBLE);
                            mapc2.setVisibility(View.VISIBLE);


                            Double lat=(Double) document.get("LocationLatitude");
                            Double longit= (Double) document.get("LocationLongitude");
                            LocationName=findViewById(R.id.qr_detail_loacation_name);
                            address=findViewById(R.id.qr_detail_loacation_address);
                            address.setVisibility(View.VISIBLE);
                            latitude=findViewById(R.id.qr_detail_loacation_latitude);
                            latitude.setVisibility(View.VISIBLE);
                            longitude=findViewById(R.id.qr_detail_loacation_longtiude);
                            longitude.setVisibility(View.VISIBLE);
                            LocationName.setText((String) document.get("LocationName"));
                            latitude.setText("Latitude"+lat);
                            longitude.setText("Longitude"+longit);
                            address.setText((String) document.get("LocationAddress"));

                            MapView mapView=findViewById(R.id.qr_detail_location_content_map_view);
                            mapView.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(@NonNull MapboxMap mapboxMap) {
                                    mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
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
                                    CameraPosition position = new CameraPosition.Builder()
                                            .target(new LatLng(lat, longit))
                                            .zoom(15)
                                            .tilt(20)
                                            .build();
                                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);
                                    mapboxMapGlobal = mapboxMap;
                                }
                            });

                        }

                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Record does not exist", Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Conect fail", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                }
            }
        });
    }

    private void HashMapValidate(String id,Object ob){
        if (data.containsKey(id)) {
            data.replace(id,ob);
        } else {
            data.put(id,ob);
        }
    }


    @Override
    public void onImageUpdate(String QRtext,Boolean imageOn) {
        // future representation
        int score= QRtext.length();
        String demoText="";
        if (QRtext.length() > 0 && mode.equals("new")) {
            TextInputEditText visual = findViewById(R.id.qr_detail_general_visual_text);

            // insert here for visual demo
            demoText = QRtext;
            visual.setText(demoText);

            TextInputEditText points = findViewById(R.id.qr_detail_general_qrtext_points);
            // insert(change) for score calcualtion
//            int score= score;


            String qr_le_str = String.valueOf(score);
            points.setText(qr_le_str);
        }
        if(mode.equals("new")){
            HashMapValidate("points",score);
            HashMapValidate("imageExist",imageOn);

            if (imageOn){
                HashMapValidate("QRtext",QRtext);
                HashMapValidate("QVisual",demoText);

            } else {
                // future visual represent
                HashMapValidate("QRtext","");
            }
        }else{

        }
    }

    @Override
    public void onImageUpdate(Boolean imageOn) {
        HashMapValidate("imageExist",imageOn);
    }

    @Override
    public void onLocationUpdate(Boolean btOn, Double longitude, Double latitude, String name, String address) {
        if(mode.equals("new")){
            HashMapValidate("LocationExist",btOn);

            if (btOn){
                HashMapValidate("LocationLatitude",latitude);
                HashMapValidate("LocationLongitude",longitude);
                HashMapValidate("LocationName",name);
                HashMapValidate("LocationAddress",address);
            } else {
                // future visual represent
                HashMapValidate("LocationLatitude",0.0);
                HashMapValidate("LocationLongitude",0.0);
                HashMapValidate("LocationName","");
                HashMapValidate("LocationAddress","");
            }
        }else{

        }

    }

    @Override
    public void onLocationUpdate(Boolean btOn) {
        HashMapValidate("LocationExist",btOn);
    }

    @Override
    public void onGeneralUpdate(String QRcodename,String today) {
        HashMapValidate("QRname",QRcodename);
        HashMapValidate("date",today);
        QRname=QRcodename;
    }

    @Override
    public void onGeneralUpdate(Boolean delreq) {
        // delte request
        if (Objects.equals((String) data.get("user"), userName)){

            String s1 = (String) data.get("QRpath");
            if (!Objects.equals(s1, "")){
                imageDelFirestone(s1);
            }

            GameQrRecordDB DBconnect = new GameQrRecordDB();
            DBconnect.DelteQrInfo(QRname);
            finish();
        }
    }

    private void imagePushFirestone(){
        Bitmap bitmap =null;
        if (imageUri!=null){
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
            }
            // de;tete
            try{
                File fdel = new File(imageUri.getPath());//create path from uri
                if (fdel.exists()) {
                    fdel.delete();
                }
            } catch (Exception e){
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
            }

        }
        Boolean imgE = (Boolean) data.get("imageExist");
        if (bitmap!=null || Boolean.TRUE.equals(imgE)){
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            String fName = userName+"_"+timeStamp+".jpg";
            String Path = "image/"+ fName;
            StorageReference storageRef = storage.getReference();
            StorageReference mountainsRef = storageRef.child(Path);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
            byte[] imageData = baos.toByteArray();
            UploadTask uploadTask = mountainsRef.putBytes(imageData);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast toast = Toast.makeText(getApplicationContext(),"tryagain", Toast.LENGTH_SHORT);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    HashMapValidate("QRpath",Path);

                    GameQrRecordDB DBconnect = new GameQrRecordDB();
                    if (QRname.length()==0){
                        DBconnect.NewQRWithRandomGeneratedWords("",data);
                        finish();
                    } else {
                        DBconnect.CheckUnique(QRname,true,data);
                        finish();
                    }
                }
            });



        } else {
            // de;tete
            try{
                File fdel = new File(imageUri.getPath());//create path from uri
                if (fdel.exists()) {
                    fdel.delete();
                }
            } catch (Exception e){
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
            }

            HashMapValidate("QRpath","");
            GameQrRecordDB DBconnect = new GameQrRecordDB();
            if (QRname.length()==0){
                DBconnect.NewQRWithRandomGeneratedWords("",data);
                finish();
            } else {
                DBconnect.CheckUnique(QRname,true,data);
                finish();
            }
        }


    }

    private void imageFetchFirestone(String FilePath) {
        StorageReference storageRef = storage.getReference();
        final StorageReference ref = storageRef.child(FilePath);
        final long ONE_MEGABYTE = 1024 * 1024; //1mb
        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Drawable image = null;
                ByteArrayInputStream is = new ByteArrayInputStream(bytes);
                image = Drawable.createFromStream(is, "QR code surrounding");
                ImageButton qrbt = findViewById(R.id.qr_detail_image_qrimage_button);
                qrbt.setBackground(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_SHORT);
                ImageButton qrbt = findViewById(R.id.qr_detail_image_qrimage_button);
                qrbt.setBackground(null);
            }
        });
    }

    private void imageDelFirestone(String s1){
        StorageReference storageRef = storage.getReference();

        // Create a reference to the file to delete
        StorageReference desertRef = storageRef.child(s1);

        // Delete the file
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
        }
}
package com.example.sufferqr;



import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.example.sufferqr.databinding.ActivityQrdetailBinding;
import com.example.sufferqr.ui.main.QRDetailGeneralFragment;
import com.example.sufferqr.ui.main.QRDetailImageFragment;
import com.example.sufferqr.ui.main.QRDetailLocationFragment;
import com.example.sufferqr.ui.main.SectionsPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class QRDetailActivity extends AppCompatActivity implements QRDetailLocationFragment.OnFragmentInteractionListener,
        QRDetailImageFragment.OnFragmentInteractionListener, QRDetailGeneralFragment.OnFragmentInteractionListener
          {

    private ActivityQrdetailBinding binding;

    private FirebaseStorage storage;
    MapboxMap mapboxMapGlobal; // mapbox in location

    private FirebaseFirestore db;

    private HashMap <String,Object> data; // data that sent to collection
    String mode,userName,QRname,QRstring,OrginalName,QRvisual,QRpoints; // remember some of the name setiings

    Uri imageUri; // at new mode, record local image location

    Button CancelBt,ConfirmBt; // listener for bottom button
    Bundle infoBundle; //tabs transit information



    SectionsPagerAdapter sectionsPagerAdapter;


              /**
   * it start when class create detect the class from, and show different ele,emts
   */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        binding = ActivityQrdetailBinding.inflate(getLayoutInflater());
        Mapbox.getInstance(getApplicationContext(), getResources().getString(R.string.mapbox_access_token));
        storage = FirebaseStorage.getInstance();
        setContentView(binding.getRoot());

        // intent that comefrom drawer class load it in to activity
        Intent myNewIntent = getIntent();
        mode = myNewIntent.getStringExtra("mode");
        userName = myNewIntent.getStringExtra("user");

        if (!Objects.equals(mode, "new")){
            QRname = myNewIntent.getStringExtra("qrID");
        } else if (mode.equals("new")) {
            QRstring = myNewIntent.getStringExtra("QRString");
            String uriString = myNewIntent.getStringExtra("imageUri");
            imageUri = Uri.parse(uriString);
            QRpoints = myNewIntent.getStringExtra("QRScore");
            QRvisual = myNewIntent.getStringExtra("QRVisual");
        }

        // hashmap prepare uploading
        data = new HashMap<>();


        // set up package to each tab
        infoBundle = new Bundle();
        infoBundle.putString("mode",mode);

        if (Objects.equals(mode, "new")){
            infoBundle.putString("QRString",QRstring);
            infoBundle.putString("user",userName);
            infoBundle.putString("QRScore",QRpoints);
            infoBundle.putString("QRVisual",QRvisual);
            infoBundle.putString("imageUri",imageUri.toString());

            HashMapValidate("QVisual",QRvisual);
            HashMapValidate("points",Integer.valueOf(QRpoints));
            HashMapValidate("QRtext","");
            HashMapValidate("QRpath",imageUri);

        } else if (Objects.equals(mode, "modified")) {
            infoBundle.putString("user",userName);
            infoBundle.putString("qrID",QRname);
            OrginalName="";
        }






        // setup tabAdaper
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(),infoBundle);
        int limit = (sectionsPagerAdapter.getCount() > 1 ? sectionsPagerAdapter.getCount() -1 : 1);// setuo all three tab alive,no kill
        ViewPager viewPager = findViewById(R.id.qrdetail_view_pager); // binding.qrdetail_viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(limit);
        viewPager.beginFakeDrag(); // disable drag

        TabLayout tabs = findViewById(R.id.qrdetail_tabs); //   binding.qrdetail_tabs;
        tabs.setupWithViewPager(viewPager);
        // link two button listener
        CancelBt = findViewById(R.id.Activity_qet_detail_cancel_button);
        ConfirmBt = findViewById(R.id.activity_qr_detail_bottom_bar_confirm_bt);

        CancelBt.setOnClickListener(v -> {
            if (Objects.equals(mode, "new")){
                // in new mode, picture cache have to be cleared
                try{
                    File fdel = new File(imageUri.getPath());//create path from uri
                    if (fdel.exists()) {
                        fdel.delete();
                    }
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            finish();
        });
        ConfirmBt.setOnClickListener(v -> {
            if (Objects.equals(mode, "new")) {
                // if new push the image and then database
                Boolean b1 = (Boolean) data.get("LocationExist");
                Boolean b2 = (Boolean) data.get("imageExist");
                if (Boolean.FALSE.equals(b2)){
                    Uri uri = (Uri) data.get("QRpath");
                    try{
                        File fdel = new File(uri.getPath());//create path from uri
                        if (fdel.exists()) {
                            fdel.delete();
                        }
                    } catch (Exception e){
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                    HashMapValidate("QRpath","");
                }
                if (Boolean.TRUE.equals(b1) && Objects.equals((String) data.get("LocationAddress"), "")) {
                    Toast.makeText(getBaseContext(), "please wait for data complete fetching", Toast.LENGTH_SHORT).show();
                } else if (Boolean.FALSE.equals(b1)) {
                    // future visual represent
                    HashMapValidate("LocationLatitude",0.0);
                    HashMapValidate("LocationLongitude",0.0);
                    HashMapValidate("LocationName","");
                    HashMapValidate("LocationAddress","");
                    HashMapValidate("user", userName);
                    HashMapValidate("time", new Date());
                    GameQrRecordDB DBconnect = new GameQrRecordDB();
                    DBconnect.imagePushFirestone(data,imageUri,userName,QRname,getContentResolver());
                    finish();
                }  else {
                    HashMapValidate("user", userName);
                    HashMapValidate("time", new Date());
                    GameQrRecordDB DBconnect = new GameQrRecordDB();
                    DBconnect.imagePushFirestone(data,imageUri,userName,QRname,getContentResolver());
                    finish();
                }
            } else if (mode.equals("modified")){
                GameQrRecordDB DBconnect = new GameQrRecordDB();
                // check if change,something releated also need to change
                Boolean b1 = (Boolean)data.get("imageExist");
                Boolean b2 = (Boolean)data.get("LocationExist");
                String s1 = (String) data.get("QRpath");
                if (Boolean.FALSE.equals(b1)){
                    HashMapValidate("QRtext","");
                    if (!Objects.equals(s1, "")){
                        DBconnect.imageDelFirestone(s1);
                        HashMapValidate("QRpath","");
                    }
                }
                if (Boolean.FALSE.equals(b2)){
                    HashMapValidate("LocationLatitude",0.0);
                    HashMapValidate("LocationLongitude",0.0);
                    HashMapValidate("LocationName","");
                    HashMapValidate("LocationAddress","");
                }

                if (!Objects.equals(QRname, OrginalName)){
                    DBconnect.DelteQrInfo(OrginalName,data);
                    DBconnect.CheckUnique(QRname,true,data);
                } else {
                    DBconnect.ChangeQrInfo(QRname,data);
                }
                finish();

            }


        });

        // check if things exist,if not exist
        if (!Objects.equals(mode, "new")){
            if (Objects.equals(QRname, "")){
                finish();
            }else{
                getContent();
            }
        }
    }

  /**
   * fetch content for existing
   */
    private void getContent(){
        // collect from ducument name
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReferenceDest = db.collection("GameQrCode");
        // check if id is unique in the FameQr datavase
        collectionReferenceDest.document(QRname).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    data = (HashMap<String, Object>) document.getData();
                    sectionsPagerAdapter.infoCallBack(userName, data);

                    // general page change load info


                    TextInputEditText name,points;
                    TextView textView;Button button;
                    name = findViewById(R.id.qr_detail_general_qrtext_name);
                    points = findViewById(R.id.qr_detail_general_qrtext_points);
                    textView = findViewById(R.id.qr_detail_general_qrtext_date);
                    TextInputEditText visual = findViewById(R.id.qr_detail_general_visual_text);

                    button = findViewById(R.id.qr_detail_general_elevatedButton);
                    name.setText((String)document.get("QRname"));
                    OrginalName=(String)document.get("QRname");
                    Object pt = document.get("points");
                    points.setText(String.valueOf(pt));
                    textView.setText((String)document.get("date"));
                    visual.setText((String)document.get("QVisual"));
                    TextInputLayout ttl = findViewById(R.id.qr_detail_general_qrtext_name_layout);

                    if (Objects.equals((String) document.get("user"), userName)){
                        ttl.setEnabled(true);
                        name.setEnabled(true);
                    } else {
                        ttl.setHelperText("");
                        ttl.setCounterEnabled(false);
                        ttl.setEnabled(false);
                        name.setEnabled(false);
                    }

                    // if not the creator disble change option
                    if (Objects.equals((String) document.get("user"), userName)){
                        button.setEnabled(true);
                    }

                    // image page chage load info
                    TextInputEditText QRcontent;
                    SwitchMaterial imgEnable;
                    QRcontent = findViewById(R.id.qr_detail_image_textfield);
                    imgEnable = findViewById(R.id.qr_detail_image_enable_switch);
                    Boolean imgE = (Boolean) document.get("imageExist");

                    CardView c1= findViewById(R.id.qr_detail_image_qrtext_cardview);
                    CardView c2= findViewById(R.id.qr_detail_image_qrimage_cardview);
                    TextView t1= findViewById(R.id.qr_detail_image_privacy_text);

                    if (Boolean.FALSE.equals(imgE)){
                        // if not the creator disble change option
                        imgEnable.setChecked(false);
                        imgEnable.setEnabled(false);

                        t1.setVisibility(View.INVISIBLE);
                        c1.setVisibility(View.INVISIBLE);
                        c2.setVisibility(View.INVISIBLE);
                    } else {
                        // since image exist load content
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
                        // conect firebase storage
                        imageFetchFirestone((String) document.get("QRpath"));
                    }
                } else {
                    // record not exost exit
                    Toast toast = Toast.makeText(getApplicationContext(), "Record does not exist", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                }
            } else {
                // fail to connect
                Toast toast = Toast.makeText(getApplicationContext(), "Conect fail", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        });
    }

  /**
   * if a key exist override,if not delete
   */
    private void HashMapValidate(String id,Object ob){
        if (data.containsKey(id)) {
            data.replace(id,ob);
        } else {
            data.put(id,ob);
        }
    }

  /**
   * sync input from the user in image tab at other mode
   */
    @Override
    public void onImageUpdate(Boolean imageOn) {
        HashMapValidate("imageExist",imageOn);
    }

  /**
   * sync input from the user in location tab at new mode
   */
    @Override
    public void onLocationUpdate(MapboxMap mapboxMap,Boolean btOn, Double longitude, Double latitude, String name, String address) {
        mapboxMapGlobal = mapboxMap;
        if(mode.equals("new")){
            HashMapValidate("LocationExist",btOn);

            if (btOn){
                HashMapValidate("LocationLatitude",latitude);
                HashMapValidate("LocationLongitude",longitude);
                HashMapValidate("LocationName",name);
                HashMapValidate("LocationAddress",address);
            }
        }else{
            HashMapValidate("LocationExist",btOn);
        }
    }


  /**
   * sync input from the user in general tab at new mode
   */
    @Override
    public void onGeneralUpdate(String QRcodename,String today) {
        HashMapValidate("QRname",QRcodename);
        HashMapValidate("date",today);
        QRname=QRcodename;
    }

  /**
   * delete request in general tab at modifier mode
   */
    @Override
    public void onGeneralUpdate(Boolean delreq) {
        // delte request
        if (Objects.equals((String) data.get("user"), userName)){
            GameQrRecordDB DBconnect = new GameQrRecordDB();
            String s1 = (String) data.get("QRpath");
            if (!Objects.equals(s1, "")){
                DBconnect.imageDelFirestone(s1);
            }
            DBconnect.DelteQrInfo(QRname,data);
            finish();
        }
    }

      @Override
      public void onGeneralUpdate(String Newname) {
          HashMapValidate("QRname",Newname);
          QRname=Newname;
      }



  /**
   * petching existing image
   */
    private void imageFetchFirestone(String FilePath) {
        StorageReference storageRef = storage.getReference();
        final StorageReference ref = storageRef.child(FilePath);
        final long ONE_MEGABYTE = 1024 * 1024; //1mb
        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            // Data for "images/island.jpg" is returns, use this as needed
            Drawable image = null;
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            image = Drawable.createFromStream(is, "QR code surrounding");
            ImageButton qrbt = findViewById(R.id.qr_detail_image_qrimage_button);
            qrbt.setBackground(image);
        }).addOnFailureListener(exception -> {
            Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_SHORT).show();
            ImageButton qrbt = findViewById(R.id.qr_detail_image_qrimage_button);
            qrbt.setBackground(null);
        });
    }

}
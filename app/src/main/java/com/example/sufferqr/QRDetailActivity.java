package com.example.sufferqr;



import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.sufferqr.databinding.ActivityQrdetailBinding;
import com.example.sufferqr.ui.main.CustomNoDragViewPager;
import com.example.sufferqr.ui.main.QRDetailGeneralFragment;
import com.example.sufferqr.ui.main.QRDetailImageFragment;
import com.example.sufferqr.ui.main.QRDetailLocationFragment;
import com.example.sufferqr.ui.main.QRDetailSectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * qr code details reviewing,including location,image,name,points.etc
 */
public class QRDetailActivity extends AppCompatActivity implements QRDetailLocationFragment.OnFragmentInteractionListener,
        QRDetailImageFragment.OnFragmentInteractionListener, QRDetailGeneralFragment.OnFragmentInteractionListener
          {

    private ActivityQrdetailBinding binding;

    private FirebaseStorage storage;
    MapboxMap mapboxMapGlobal; // mapbox in location

    private FirebaseFirestore db;

    private HashMap <String,Object> data; // data that sent to collection
    String mode,userName,QRname,OrginalName; // remember some of the name setiings

    Uri imageUri; // at new mode, record local image location

    Button CancelBt,ConfirmBt; // listener for bottom button
    Bundle infoBundle; //tabs transit information

    ProgressDialog progressDialog;

    QRDetailSectionsPagerAdapter qrDetailSectionsPagerAdapter;


  /**
   * it start when class create detect the class from, and show different elemts
   * @param savedInstanceState save state
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

        data = new HashMap<>();
        // hashmap prepare uploading
        if (!Objects.equals(mode, "new")){
            QRname = myNewIntent.getStringExtra("qrID");
            infoBundle = new Bundle();
            // set up package to each tab
            infoBundle.putString("mode",mode);
            infoBundle.putString("user",userName);
            infoBundle.putString("qrID",QRname);
            OrginalName="";
        } else if (mode.equals("new")) {
            String uriString = myNewIntent.getStringExtra("imageUri");
            imageUri = Uri.parse(uriString);
            infoBundle = myNewIntent.getBundleExtra("data");
            // data transmitting
            Set<String> ks = infoBundle.keySet();
            Iterator<String> iterator = ks.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                HashMapValidate(key,infoBundle.get(key));
            }
            // set up package to each tab
            infoBundle.putString("mode",mode);
            infoBundle.putString("imageUri",imageUri.toString());
        }

        // setup tabAdaper
        qrDetailSectionsPagerAdapter = new QRDetailSectionsPagerAdapter(this, getSupportFragmentManager(),infoBundle);
        int limit = (qrDetailSectionsPagerAdapter.getCount() > 1 ? qrDetailSectionsPagerAdapter.getCount() -1 : 1);// setuo all three tab alive,no kill
        CustomNoDragViewPager viewPager = findViewById(R.id.qrdetail_view_pager); // binding.qrdetail_viewPager;
        viewPager.setAdapter(qrDetailSectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(limit);

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
            Boolean success=false;
            GameQrRecordDB DBconnect = new GameQrRecordDB();
            if (Objects.equals(mode, "new")) {
                success = DBconnect.NewPreProcessing(getBaseContext(),data,userName,getContentResolver());
            } else if (mode.equals("modified")){
                success = DBconnect.ChangePreProcessing(data,QRname,OrginalName);
            }
            if (success){
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
        }else {
            getUserName();
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
                    CustomNoDragViewPager AllView = findViewById(R.id.qrdetail_view_pager);
                    View GeneralView = AllView.getChildAt(0);
                    View ImageView = AllView.getChildAt(1);
                    qrDetailSectionsPagerAdapter.infoCallBack(GeneralView,ImageView ,userName, data);
                    OrginalName=(String)document.get("QRname");
                    QRname =OrginalName;
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

    private void getUserName(){
          // collect from ducument name
          db = FirebaseFirestore.getInstance();
          final CollectionReference collectionReferenceDest = db.collection("Player");
          // check if id is unique in the FameQr datavase
          collectionReferenceDest.document(userName).get().addOnCompleteListener(task -> {
              if (task.isSuccessful()) {
                  DocumentSnapshot document = task.getResult();
                  if (document.exists()) {
                      HashMapValidate("userName",(String)document.get("name"));
                      HashMapValidate("email",(String)document.get("email"));
                  } else {
                      // record not exost exit
                      Toast.makeText(getApplicationContext(), "Record does not exist", Toast.LENGTH_SHORT).show();
                  }
              } else {
                  // fail to connect
                  Toast.makeText(getApplicationContext(), "Conect fail", Toast.LENGTH_SHORT).show();
              }
          });
      }
  /**
   * if a key exist override,if not delete
   * @param id name
   * @param ob thing to store
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
   * @param imageOn boolean
   */
    @Override
    public void onImageUpdate(Boolean imageOn) {
        HashMapValidate("imageExist",imageOn);
    }

  /**
   * sync input from the user in location tab at new mode
   * @param mapboxMap mapbox details
   * @param btOn button
   * @param longitude location
   * @param latitude location
   * @param name name of poi
   * @param address full mailing address
   */
    @Override
    public void onLocationUpdate(MapboxMap mapboxMap,Boolean btOn, Double longitude, Double latitude, String name, String address) {
        mapboxMapGlobal = mapboxMap;
        HashMapValidate("LocationExist",btOn);
        if (btOn){
            HashMapValidate("LocationLatitude",latitude);
            HashMapValidate("LocationLongitude",longitude);
            HashMapValidate("LocationName",name);
            HashMapValidate("LocationAddress",address);
        }

    }


  /**
   * sync input from the user in general tab at new mode
   * @param QRcodename name
   * @param today time
   */
    @Override
    public void onGeneralUpdate(String QRcodename,String today) {
        HashMapValidate("QRname",QRcodename);
        HashMapValidate("date",today);
        QRname=QRcodename;
    }

  /**
   * delete request in general tab at modifier mode
   * @param delreq delte request
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

   /**
   * realtime getting user input name
   * @param Newname qrcode name
   */
    @Override
    public void onGeneralUpdate(String Newname) {
      HashMapValidate("QRname",Newname);
      QRname=Newname;
    }

}
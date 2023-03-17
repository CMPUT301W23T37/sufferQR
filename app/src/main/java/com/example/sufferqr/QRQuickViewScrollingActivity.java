package com.example.sufferqr;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.sufferqr.ui.main.CustomNoDragViewPager;
import com.example.sufferqr.ui.main.QRDetailSectionsPagerAdapter;
import com.example.sufferqr.ui.main.QRQuickViewGeneralFragment;
import com.example.sufferqr.ui.main.QuickViewSectionsPageAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sufferqr.databinding.ActivityQrquickViewScrollingBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mapbox.mapboxsdk.Mapbox;

import java.io.ByteArrayInputStream;
import java.util.Map;

public class QRQuickViewScrollingActivity extends AppCompatActivity {

    private ActivityQrquickViewScrollingBinding binding;

    Intent myIntent;
    String user,qrID;
    Bundle data;

    QuickViewSectionsPageAdapter quickViewSectionsPageAdapter;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityQrquickViewScrollingBinding.inflate(getLayoutInflater());
        Mapbox.getInstance(getApplicationContext(), getResources().getString(R.string.mapbox_access_token));
        setContentView(binding.getRoot());

        myIntent = getIntent();
        user = myIntent.getStringExtra("user");
        qrID = myIntent.getStringExtra("qrID");
        data = myIntent.getBundleExtra("MapData");

        // toolbar

        Toolbar toolbar = binding.qRQuickViewToolbar;
        setSupportActionBar(toolbar);
        // enable return
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        CollapsingToolbarLayout toolBarLayout = binding.qRQuickViewToolbarLayout;
        toolBarLayout.setTitle("QuickView");

        // https://stackoverflow.com/questions/30580954/viewpager-in-a-nestedscrollview
        NestedScrollView scrollView = (NestedScrollView) findViewById (R.id.qr_quick_view_nesetedView);
        scrollView.setFillViewport (true);



        quickViewSectionsPageAdapter = new QuickViewSectionsPageAdapter(this, getSupportFragmentManager(),data);
        int limit = (quickViewSectionsPageAdapter.getCount() > 1 ? quickViewSectionsPageAdapter.getCount() -1 : 1);// setuo all three tab alive,no kill
        CustomNoDragViewPager viewPager = findViewById(R.id.qr_quick_viewPager); // binding.qrdetail_viewPager;
        viewPager.setAdapter(quickViewSectionsPageAdapter);
        viewPager.setOffscreenPageLimit(limit);
        viewPager.setPagingEnabled(false);
        TabLayout tabs =findViewById(R.id.qr_quick_view_tabs); //   binding.qrdetail_tabs;
        tabs.setupWithViewPager(viewPager);

        if (data!=null){
            String imageExist = data.getString("imageExist");
            if (Boolean.parseBoolean(imageExist)){
                String uri_image = data.getString("QRpath");
                imageFetchFirestone(uri_image);
                System.out.println("aaaaaaaaaaaaaaaaaaaaaa");

            }
        } else {
            System.out.println("aaaaaaaaaaaaaaaaaaaaaavvbb");
        }

    }

    /**
     * inflate the boton on the top right to edit
     * @param menu The options menu in which you place your items.
     *
     * @return inflate success
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_q_r_quick_view_scrolling,menu);
        return true;
    }

    /**
     * edit on click listener
     * @param item The menu item that was selected.
     *
     * @return tell which item selected
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if (id == R.id.qr_quick_view_edit){
            Toast.makeText(getBaseContext(),"user miss match",Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void imageFetchFirestone(String FilePath) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference ref = storageRef.child(FilePath);
        final long ONE_MEGABYTE = 1024 * 1024; //1mb
        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            // Data for "images/island.jpg" is returns, use this as needed
            Drawable image = null;
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            image = Drawable.createFromStream(is, "QR code surrounding");
            ImageView img = findViewById(R.id.q_r_quick_view_image);
            img.setImageDrawable(image);
        }).addOnFailureListener(exception -> {
            Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_SHORT).show();
        });
    }
}
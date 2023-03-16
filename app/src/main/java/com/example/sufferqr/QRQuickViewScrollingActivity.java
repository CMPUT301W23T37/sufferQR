package com.example.sufferqr;

import android.annotation.SuppressLint;
import android.os.Bundle;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.sufferqr.databinding.ActivityQrquickViewScrollingBinding;
import com.google.android.material.tabs.TabLayout;

public class QRQuickViewScrollingActivity extends AppCompatActivity {

    private ActivityQrquickViewScrollingBinding binding;

    Bundle infoBundle;

    QuickViewSectionsPageAdapter quickViewSectionsPageAdapter;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityQrquickViewScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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


        infoBundle = new Bundle();

        quickViewSectionsPageAdapter = new QuickViewSectionsPageAdapter(this, getSupportFragmentManager(),infoBundle);
        int limit = (quickViewSectionsPageAdapter.getCount() > 1 ? quickViewSectionsPageAdapter.getCount() -1 : 1);// setuo all three tab alive,no kill
        ViewPager viewPager = findViewById(R.id.qr_quick_viewPager); // binding.qrdetail_viewPager;
        viewPager.setAdapter(quickViewSectionsPageAdapter);
        viewPager.setOffscreenPageLimit(limit);
        viewPager.beginFakeDrag(); // disable drag
        TabLayout tabs =findViewById(R.id.qr_quick_view_tabs); //   binding.qrdetail_tabs;
        tabs.setupWithViewPager(viewPager);



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
}
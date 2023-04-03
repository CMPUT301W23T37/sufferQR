package com.example.sufferqr;

import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.sufferqr.databinding.ActivityLeaderBoardBinding;
import com.google.android.material.tabs.TabLayout;

import org.checkerframework.checker.units.qual.A;

/**
 * leadboard
 */
public class LeaderBoard extends DrawerBase {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    LeaderPageAdapter leaderPageAdapter;

    ActivityLeaderBoardBinding activityLeaderBoardBinding;

    /**
     * class launch
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        activityLeaderBoardBinding = ActivityLeaderBoardBinding.inflate(getLayoutInflater());
        setContentView(activityLeaderBoardBinding.getRoot());
        allocateActivityTitle("Leader Board");

        tabLayout = findViewById(R.id.LeaderBoardTabLayout);
        viewPager2 = findViewById(R.id.LeaderBoardViewPager);
        leaderPageAdapter = new LeaderPageAdapter(this);
        viewPager2.setAdapter(leaderPageAdapter);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
                tabLayout.getTabAt(position).select();
            }
        });
    }
}
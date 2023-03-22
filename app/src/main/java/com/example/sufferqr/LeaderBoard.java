package com.example.sufferqr;

import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

/**
 * leadboard
 */
public class LeaderBoard extends DrawerBase {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    LeaderPageAdapter leaderPageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
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
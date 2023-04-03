package com.example.sufferqr;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LeaderBoardIntent {
    private Solo solo;

    @Rule
    public ActivityTestRule<LeaderBoard> rule = new ActivityTestRule<>(LeaderBoard.class,
            true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkLeaderBoardTotalRank() throws Exception {
        solo.assertCurrentActivity("Wrong Activity", LeaderBoard.class);
        ViewPager2 viewPager2 = (ViewPager2) solo.getView(R.id.LeaderBoardViewPager);
        int currentPage = viewPager2.getCurrentItem();
        assertEquals("Not on the Total Rank page", 0, currentPage);
    }

    @Test
    public void checkLeaderBoardHighestRank() throws Exception {
        solo.assertCurrentActivity("Wrong Activity", LeaderBoard.class);
        TabLayout tabLayout = (TabLayout) solo.getView(R.id.LeaderBoardTabLayout);
        solo.clickOnView(tabLayout.getTabAt(1).view);
        solo.sleep(1000); // Optional: wait for the transition to finish
        ViewPager2 viewPager2 = (ViewPager2) solo.getView(R.id.LeaderBoardViewPager);
        int currentPage = viewPager2.getCurrentItem();
        assertEquals("Not on the Highest Rank page", 1, currentPage);
    }
}

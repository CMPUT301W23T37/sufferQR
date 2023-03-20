package com.example.sufferqr;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.sufferqr.Highest;
import com.example.sufferqr.Total;

public class LeaderPageAdapter extends FragmentStateAdapter {

    public LeaderPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Total();
            case 1:
                return new Highest();
            default:
                return new Total();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

package com.example.sufferqr.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.sufferqr.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2,R.string.tab_text_3};
    private final Context mContext;

    Bundle adapterMapBundle,adapterImageBundle,adapterGeneralBundle;

    public SectionsPagerAdapter(Context context, FragmentManager fm,Bundle mapBundle,Bundle imageBundle,Bundle GeneralBundle) {
        super(fm);
        mContext = context;
        adapterMapBundle = mapBundle;
        adapterImageBundle =imageBundle;
        adapterGeneralBundle = GeneralBundle;

    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if(position == 0) {
            return new QRDetailGeneralFragment(adapterGeneralBundle);
        } else if (position==1){
            return new QRDetailImageFragment(adapterImageBundle);
        } else if (position==2) {
            return new QRDetailLocationFragment(adapterMapBundle);
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }

}
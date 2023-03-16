package com.example.sufferqr.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.sufferqr.R;

public class QuickViewSectionsPageAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_4};

    Bundle myHereBundle;

    private final Context mContext;

    QRQuickViewGeneralFragment qrQuickViewGeneralFragment;
    QRQuickViewCommentsFragment qrQuickViewCommentsFragment;


    public QuickViewSectionsPageAdapter(Context context, FragmentManager fm,Bundle myBundle) {
        super(fm);
        mContext = context;
        myHereBundle = myBundle;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        qrQuickViewCommentsFragment = new QRQuickViewCommentsFragment();
        qrQuickViewGeneralFragment = new QRQuickViewGeneralFragment();
        if (position==0){
            return  qrQuickViewGeneralFragment;
        } else if (position==1) {
            return qrQuickViewCommentsFragment;

        }else {
            return null;
        }
    }

    /**
     * return position
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}

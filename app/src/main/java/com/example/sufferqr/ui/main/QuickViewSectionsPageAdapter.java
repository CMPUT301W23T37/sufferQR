package com.example.sufferqr.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.sufferqr.R;

/**
 * adapter showing existing qrcode record
 */
public class QuickViewSectionsPageAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1,R.string.tab_text_5, R.string.tab_text_4};

    Bundle myHereBundle;

    private final Context mContext;

    QRQuickViewGeneralFragment qrQuickViewGeneralFragment;
    QRQuickViewCommentsFragment qrQuickViewCommentsFragment;

    QRQuickViewSameQRFragment qrQuickViewSameQRFragment;

    /**
     * method for launch the class
     * @param context applcation class
     * @param fm fragment manager
     * @param myBundle passing information
     */
    public QuickViewSectionsPageAdapter(Context context, FragmentManager fm,Bundle myBundle) {
        super(fm);
        mContext = context;
        myHereBundle = myBundle;
    }


    /**
     * return page for different tabs
     * @param position clicked position
     * @return fragment
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        qrQuickViewCommentsFragment = new QRQuickViewCommentsFragment(myHereBundle);
        qrQuickViewGeneralFragment = new QRQuickViewGeneralFragment(myHereBundle);
        qrQuickViewSameQRFragment = new QRQuickViewSameQRFragment(myHereBundle);
        if (position==0){
            return  qrQuickViewGeneralFragment;
        } else if (position==2) {
            return qrQuickViewCommentsFragment;
        } else if (position==1) {
            return  qrQuickViewSameQRFragment;
        }else {
            return null;
        }
    }

    /**
     * page title name
     * @param position The position of the title requested
     * @return string of tittle
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    /**
     * return number of page
     * @return integer
     */
    @Override
    public int getCount() {
        return 3;
    }

}

package com.example.sufferqr.ui.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

/**
 * A custom Horizontal drag disabled ViewPager
 */
public class CustomNoDragViewPager extends ViewPager {

    private boolean isPagingEnabled = false;

    /**
     * launch pager
     * @param context app context
     */
    public CustomNoDragViewPager (Context context) {
        super(context);
    }

    /**
     * launch pager
     * @param context app contecxt
     * @param attrs AttributeSet
     */
    public CustomNoDragViewPager(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    /**
     * touch response
     * @param event The motion event.
     * @return paging enable
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    /**
     * motion event
     * @param event The motion event being dispatched down the hierarchy.
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    /**
     * sett if pager enalbe
     * @param b boolean
     */
    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
}

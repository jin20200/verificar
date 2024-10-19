package com.easyplexdemoapp.util;

import android.app.Activity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class CustomActionBarDrawerToggle extends ActionBarDrawerToggle {
    private final int topMargin;
    private final Toolbar toolbar;

    public CustomActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout,
                                       Toolbar toolbar, int openDrawerContentDescRes,
                                       int closeDrawerContentDescRes, int topMarginDp) {
        super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        this.toolbar = toolbar;
        this.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, topMarginDp,
                activity.getResources().getDisplayMetrics());
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        super.onDrawerSlide(drawerView, slideOffset);
        if (this.getDrawerArrowDrawable() != null && toolbar != null) {
            // The navigation icon is typically the second child of the Toolbar
            // (index 1), but this may vary depending on your Toolbar setup
            for (int i = 0; i < toolbar.getChildCount(); i++) {
                View child = toolbar.getChildAt(i);
                if (child instanceof ImageButton) {
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    params.topMargin = topMargin;
                    child.setLayoutParams(params);
                    break;
                }
            }
        }
    }
}
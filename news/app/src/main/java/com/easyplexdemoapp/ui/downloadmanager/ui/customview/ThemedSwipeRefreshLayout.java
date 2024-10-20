/**
 * EasyPlex - Movies - Live Streaming - TV Series, Anime
 *
 * @author @Y0bEX
 * @package  EasyPlex - Movies - Live Streaming - TV Series, Anime
 * @copyright Copyright (c) 2021 Y0bEX,
 * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/
 * @profile https://codecanyon.net/user/yobex
 * @link yobexd@gmail.com
 * @skype yobexd@gmail.com
 **/

package com.easyplexdemoapp.ui.downloadmanager.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.easyplexdemoapp.R;

public class ThemedSwipeRefreshLayout extends SwipeRefreshLayout
{
    public ThemedSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(new TypedValue().data, new int[] {
                R.attr.foreground,
                com.google.android.material.R.attr.colorSecondary
        });
        setColorSchemeColors(a.getColor(1, 0));
        setProgressBackgroundColorSchemeColor(a.getColor(0, 0));
        a.recycle();
    }
}

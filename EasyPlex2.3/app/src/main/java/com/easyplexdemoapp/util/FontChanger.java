package com.easyplexdemoapp.util;


import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FontChanger {


    private FontChanger(){



    }

    private static Typeface arabicFont;

    // Initialize the Arabic font (call this method, for example, in your Application class)
    public static void initArabicFont(Context context) {
        arabicFont = Typeface.createFromAsset(context.getAssets(), "font/cairo.ttf");
    }

    // Call this method to change the font for all TextView instances in the app
    public static void changeFontInViewGroup(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);

            if (child instanceof ViewGroup) {
                // Recursively change font for child ViewGroup
                changeFontInViewGroup((ViewGroup) child);
            } else if (child instanceof TextView) {
                // Change font for TextView
                ((TextView) child).setTypeface(arabicFont);
            }
        }
    }


}
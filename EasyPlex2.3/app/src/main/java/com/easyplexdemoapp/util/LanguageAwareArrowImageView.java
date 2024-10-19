package com.easyplexdemoapp.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.easyplexdemoapp.R;

public class LanguageAwareArrowImageView extends androidx.appcompat.widget.AppCompatImageView {

    public LanguageAwareArrowImageView(Context context) {
        super(context);
        init();
    }

    public LanguageAwareArrowImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LanguageAwareArrowImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        boolean isRTL = getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
        setRotation(isRTL ? 90f : -90f);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
        if (params != null) {
            if (isRTL) {
                params.setMarginStart(dpToPx(10));
                params.setMarginEnd(0);
            } else {
                params.setMarginStart(0);
                params.setMarginEnd(dpToPx(10));
            }
            setLayoutParams(params);
        }

        setBackgroundResource(R.drawable.ic_expand_arrow);
        setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_color)));
        setMinimumWidth(0);
        setPadding(dpToPx(5), dpToPx(5), dpToPx(5), dpToPx(5));
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}

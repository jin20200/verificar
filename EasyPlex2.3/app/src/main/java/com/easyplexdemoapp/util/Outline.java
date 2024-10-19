package com.easyplexdemoapp.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.easyplexdemoapp.R;


public class Outline extends androidx.appcompat.widget.AppCompatTextView {

    public Outline(Context context) {
        super(context);
    }

    public Outline(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Outline(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onDraw(Canvas canvas) {
        int textColor = getTextColors().getDefaultColor();
        setTextColor(ContextCompat.getColor(getContext(), R.color.selectable));
        getPaint().setStrokeWidth(10);
        getPaint().setStyle(Paint.Style.STROKE);
        super.onDraw(canvas);
        setTextColor(textColor);
        getPaint().setStrokeWidth(0);
        getPaint().setStyle(Paint.Style.FILL);
        super.onDraw(canvas);
    }
}
package com.easyplexdemoapp.ui.player.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Checkable;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.easyplexdemoapp.R;


public class SubstitleImageButton extends androidx.appcompat.widget.AppCompatImageButton implements Checkable {


    /**
     * Remember the checked state of the button
     */
    private boolean isChecked = false;

    /**
     * The checked image state
     */
    @DrawableRes
    private int mStateCheckedDrawableId;

    /**
     * The un-checked image state
     */
    @DrawableRes
    private int mStateNotCheckedDrawableId;

    public SubstitleImageButton(Context context) {
        super(context);
        init(null);
    }

    public SubstitleImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SubstitleImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @BindingAdapter("setCheckedState")
    public static void onStateChanged(SubstitleImageButton imageButton, boolean checked) {
        imageButton.setChecked(checked);
    }



    /**
     * Initialize all of the drawables and animations as well as apply attributes if set
     *
     *
     */
    private void init(@Nullable AttributeSet attrs) {

        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs,
                    R.styleable.SubstitleImageButton, 0, 0);
            try {
                mStateCheckedDrawableId = a.getResourceId(R.styleable.SubstitleImageButton_state_checked,
                        R.drawable.tubi_tv_drawable_subtitles_on_selector);
                mStateNotCheckedDrawableId = a.getResourceId(R.styleable.SubstitleImageButton_state_not_checked,
                        R.drawable.tubi_tv_drawable_subtitles_off_selector);
            } finally {
                a.recycle();
            }
        }

        setDrawableSelector();
    }


    /**
     * Sets the background drawable assets based on the checked status
     */
    private void setDrawableSelector() {
        if (isChecked) {
            setBackgroundResource(mStateCheckedDrawableId);
        } else {
            setBackgroundResource(mStateNotCheckedDrawableId);
        }
        invalidate();
    }


    /**
     * Set the checked status
     *
     *
     */
    public void setChecked(boolean checked) {
        isChecked = checked;
        setDrawableSelector();
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {

        setChecked(!isChecked);

    }

}

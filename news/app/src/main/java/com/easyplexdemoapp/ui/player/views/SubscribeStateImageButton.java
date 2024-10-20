package com.easyplexdemoapp.ui.player.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageButton;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.easyplexdemoapp.R;


@SuppressLint("AppCompatCustomView")
public class SubscribeStateImageButton extends ImageButton {


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

    public SubscribeStateImageButton(Context context) {
        super(context);
        init(null);
    }

    public SubscribeStateImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SubscribeStateImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @BindingAdapter("setCheckedState")
    public static void onStateChanged(SubscribeStateImageButton imageButton, boolean checked) {
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
                        R.drawable.ic_info);
                mStateNotCheckedDrawableId = a.getResourceId(R.styleable.SubstitleImageButton_state_not_checked,
                        R.drawable.ic_info);
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

}

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
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

/*
 * Fixes crash on Android 8.0 if hint is inside TextInputEditText
 * See https://issuetracker.google.com/issues/62834931
 */

public class FixHintTextInputEditText extends TextInputEditText {

    public FixHintTextInputEditText(@NonNull Context context) {
        super(context);
    }

    public FixHintTextInputEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FixHintTextInputEditText(
            @NonNull Context context,
            @Nullable AttributeSet attrs,
            int defStyleAttr
    ) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getAutofillType() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            return AUTOFILL_TYPE_NONE;
        } else {
            return super.getAutofillType();
        }
    }
}

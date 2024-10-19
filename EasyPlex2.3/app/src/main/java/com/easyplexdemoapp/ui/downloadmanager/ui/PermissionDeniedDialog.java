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

package com.easyplexdemoapp.ui.downloadmanager.ui;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.easyplexdemoapp.R;

public class PermissionDeniedDialog extends BaseAlertDialog {
    public static PermissionDeniedDialog newInstance() {
        PermissionDeniedDialog frag = new PermissionDeniedDialog();

        Bundle args = new Bundle();
        frag.setArguments(args);

        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        super.onCreateDialog(savedInstanceState);

        String title =  getString(R.string.perm_denied_title);
        String message = getString(R.string.perm_denied_warning);
        String positiveText = getString(R.string.yes);
        String negativeText = getString(R.string.no);

        return buildDialog(
                title,
                message,
                null,
                positiveText,
                negativeText,
                null,
                false
        );
    }
}

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

/*
 * The basic callback interface with codes and functions, returned by fragments.
 */

import android.content.Intent;

public interface FragmentCallback
{
    @SuppressWarnings("unused")
    String TAG = FragmentCallback.class.getSimpleName();

    enum ResultCode {
        OK, CANCEL, BACK
    }

    void fragmentFinished(Intent intent, ResultCode code);
}
/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.easyplexdemoapp.ui.downloadmanager.core.model.DownloadEngine;

/*
 * The receiver for Wi-Fi connection state changes and roaming state.
 */

public class ConnectionReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        if (action != null && action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            DownloadEngine.getInstance(context).rescheduleDownloads();
        }
    }

    public static IntentFilter getFilter()
    {
        return new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    }
}

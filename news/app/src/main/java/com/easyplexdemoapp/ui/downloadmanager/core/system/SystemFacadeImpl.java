/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.core.system;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.webkit.WebSettings;

import androidx.annotation.NonNull;

class SystemFacadeImpl implements SystemFacade
{
    private final Context context;

    public SystemFacadeImpl(@NonNull Context context)
    {
        this.context = context;
    }

    @Override
    public NetworkInfo getActiveNetworkInfo()
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo();
    }

    @TargetApi(23)
    @Override
    public NetworkCapabilities getNetworkCapabilities()
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = cm.getActiveNetwork();
        if (network == null)
            return null;

        return cm.getNetworkCapabilities(network);
    }

    @Override
    public boolean isActiveNetworkMetered()
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.isActiveNetworkMetered();
    }

    @Override
    public String getSystemUserAgent()
    {
        try {
            return WebSettings.getDefaultUserAgent(context);

        } catch (UnsupportedOperationException e) {
            /* Fallback JVM user agent if WebView doesn't supported */
            return System.getProperty("http.agent");
        }
    }
}

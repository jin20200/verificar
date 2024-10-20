package com.easyplexdemoapp.util;

import static android.content.Context.WIFI_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import androidx.annotation.RequiresApi;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;


public class NetworkUtils {


    private NetworkUtils(){


    }




    @SuppressLint("WrongConstant")
  public static boolean isWifiConnected(Context context) {

    ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

   if (manager == null) return true;

   Network network = manager.getActiveNetwork();

    NetworkCapabilities capabilities = manager.getNetworkCapabilities(network);

        return !capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI | NetworkCapabilities.TRANSPORT_CELLULAR);
  }



    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable();
        }
        return false;
    }


    @SuppressLint("HardwareIds")
    public static String getMacAdress(Context context) {

        final String macAddr, androidId;

        WifiManager wifiMan = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();

        macAddr = wifiInf.getMacAddress();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), macAddr.hashCode());


        return deviceUuid.toString();
    }





}

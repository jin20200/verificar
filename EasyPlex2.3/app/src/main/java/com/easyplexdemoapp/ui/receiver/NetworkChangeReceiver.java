package com.easyplexdemoapp.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.widget.Toast;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.util.Tools;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private NetworkChangInterface networkChangeInterface;

    public void setNetworkChangeInterface(NetworkChangInterface networkChangeInterface) {
        this.networkChangeInterface = networkChangeInterface;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Tools.checkIfHasNetwork(context)) {
            // Network is connected
            // Your code for online functionality here
            if (networkChangeInterface != null) {
                networkChangeInterface.onConnected();
            }

        } else {
            // Network is not connected
            // Your code for handling offline state here
            if (networkChangeInterface != null) {
                networkChangeInterface.onLostConnexion();
            }
        }
    }
}

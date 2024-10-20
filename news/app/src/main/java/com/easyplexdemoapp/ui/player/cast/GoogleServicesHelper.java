package com.easyplexdemoapp.ui.player.cast;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import timber.log.Timber;

/**
 * Helper class encapsulating methods for checking and updating google play services
 * Created by stoyan on 12/14/16.
 */
public class GoogleServicesHelper {

    private GoogleServicesHelper() {


    }

    public static boolean available(@NonNull Context context) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(context);
        if (status != ConnectionResult.SUCCESS) {
            Timber.e("Google Api services not available: status code: %s", status);

            return false;
        }
        return true;
    }
}

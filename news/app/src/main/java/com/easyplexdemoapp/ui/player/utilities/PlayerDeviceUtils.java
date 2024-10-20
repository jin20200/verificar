package com.easyplexdemoapp.ui.player.utilities;

import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;

import com.google.android.exoplayer2.util.Util;

import static android.content.Context.UI_MODE_SERVICE;

public class PlayerDeviceUtils {


    private  PlayerDeviceUtils(){


    }

    private static final String XIAOMI_MANUFACTURER = "Xiaomi";
    private static final String MI_BOX_DEVICE = "once";
    private static final String AMAZON_FEATURE_FIRE_TV = "amazon.hardware.fire_tv";
    private static boolean sIsTVDevice = true;

    public static boolean isTVDevice(final Context context) {
        if (!sIsTVDevice) {
            UiModeManager uiModeManager = (UiModeManager) context.getSystemService(UI_MODE_SERVICE);
            sIsTVDevice = uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION;

            if (sIsTVDevice) {
                return sIsTVDevice;
            } // We also check fire tv
            sIsTVDevice = context.getPackageManager().hasSystemFeature(AMAZON_FEATURE_FIRE_TV);
        }
        return sIsTVDevice;
    }

    /**
     * Check if we should use one player instance instead of two to handle video and ads playback
     * Single player instance will only use content player without initializing ads player
     * Single player instance will re-buffer every time when we switch between MediaSource
     */
    public static boolean useSinglePlayer() {
        // Turn it on for all TV devices
        if (sIsTVDevice) {
            return true;
        }

        // Use single player instance for Mi Box, since for 2.8.3 exoplayer it doesn't support more than one player instance
        // When we start second player instance and prepare, we got insufficiant resource error and player stuck
        return XIAOMI_MANUFACTURER.equals(Util.MANUFACTURER) && MI_BOX_DEVICE.equals(Util.DEVICE);
    }
}

package com.easyplexdemoapp.ui.player.utilities;

import timber.log.Timber;

/**
 * Created by allensun on 9/12/17.
 * on Tubitv.com, allengotstuff@gmail.com
 */
public class ExoPlayerLogger {


    private static final boolean showLogging = false;

    private ExoPlayerLogger(){


    }



    public static void d(String tag, String message) {
        if (showLogging) {
            // do something for a debug build
            Timber.d("%s%s", tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (showLogging) {
            // do something for a debug build
            Timber.i("%s%s", tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (showLogging) {
            // do something for a debug build
            Timber.tag(tag).w(message);
        }
    }

    public static void e(String tag, String message) {
        if (showLogging) {
            // do something for a debug build
            Timber.e("%s%s", tag, message);
        }
    }

    public static void v(String tag, String message) {
        if (showLogging) {
            // do something for a debug build
            Timber.tag(tag).v(message);
        }
    }
}

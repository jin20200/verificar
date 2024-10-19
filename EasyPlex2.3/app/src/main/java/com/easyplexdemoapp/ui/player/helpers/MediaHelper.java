package com.easyplexdemoapp.ui.player.helpers;

import static android.content.Context.MODE_PRIVATE;
import static com.easyplexdemoapp.EasyPlexApp.getContext;
import static com.easyplexdemoapp.util.Constants.PLAYER_HEADER;
import static com.easyplexdemoapp.util.Constants.PLAYER_USER_AGENT;
import static com.easyplexdemoapp.util.Constants.PREF_FILE;
import static com.easyplexdemoapp.util.Tools.REFER;
import static com.google.android.exoplayer2.upstream.DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS;
import static com.google.android.exoplayer2.upstream.DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.annotation.NonNull;

import com.easyplexdemoapp.BuildConfig;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Assertions;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Created by stoyan on 6/21/17.
 */

public final class MediaHelper {


    private static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = (int) TimeUnit.SECONDS.toMillis(30);
    private static final int DEFAULT_READ_TIMEOUT_MILLIS = (int) TimeUnit.MINUTES.toMillis(5);

    private static MediaHelper instance;

    public static synchronized  MediaHelper create(@NonNull MediaModel... models) {
        instance = new MediaHelper(models);
        return instance;
    }

    private MediaHelper(MediaModel[] models) {
        LinkedList<MediaModel> linkedList = new LinkedList<>(Arrays.asList(models));
    }

    public static synchronized  MediaHelper getInstance() {
        Assertions.checkNotNull(instance);
        return instance;
    }



    public static RenderersFactory buildRenderersFactory(
            Context context, boolean preferExtensionRenderer, boolean softwareCodec) {

        if (preferExtensionRenderer){

            return new DefaultRenderersFactory(context.getApplicationContext())
                    .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
                    .setEnableDecoderFallback(softwareCodec);
        }else {

            return new DefaultRenderersFactory(context.getApplicationContext()).setEnableDecoderFallback(softwareCodec);
        }

    }


    public static synchronized DataSource.Factory getDataSourceFactory(Context context) {

        return new DefaultDataSource.Factory(context.getApplicationContext(), getHttpDataSourceFactory());
    }



    public static synchronized HttpDataSource.Factory getHttpDataSourceFactory() {



        Map<String, String> defaultRequestProperties = new HashMap<>();
        defaultRequestProperties.put(REFER, PLAYER_HEADER);
        defaultRequestProperties.put("x-request-x", BuildConfig.APPLICATION_ID);
        defaultRequestProperties.put("Accept-Encoding", "gzip, deflate");
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        CookieHandler.setDefault(cookieManager);

        return new DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
                .setKeepPostFor302Redirects(true)
                .setDefaultRequestProperties(defaultRequestProperties)
                .setConnectTimeoutMs(DEFAULT_CONNECT_TIMEOUT_MILLIS)
                .setReadTimeoutMs(DEFAULT_READ_TIMEOUT_MILLIS)
                .setUserAgent(PLAYER_USER_AGENT);
    }

    public static
    String userAgent(){
        SharedPreferences preferences = getContext().getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        return String.format(Locale.US,
                "%s (Android %s; %s; %s %s; %s)",
                preferences.getString("easyplex", "EasyPlex"),
                Build.VERSION.RELEASE,
                Build.MODEL,
                Build.BRAND,
                Build.DEVICE,
                Locale.getDefault().getLanguage());
    }



}

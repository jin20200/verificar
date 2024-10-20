package com.easyplexdemoapp.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;
import static com.bumptech.glide.load.DecodeFormat.PREFER_RGB_565;

/**
 * GlideModule
 *
 * @author Yobex.
 */
@GlideModule
public class EasyPlexAppGlideModule extends AppGlideModule {

    @SuppressLint("CheckResult")
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        final RequestOptions defaultOptions = new RequestOptions();
        // Prefer higher quality images unless we're on a low RAM device
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        defaultOptions.format(activityManager.isLowRamDevice() ? PREFER_RGB_565 : PREFER_ARGB_8888);
        // Disable hardware bitmaps as they don't play nicely with Palette
        defaultOptions.disallowHardwareConfig();
        builder.setDefaultRequestOptions(defaultOptions);


        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(5)
                .build();

        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
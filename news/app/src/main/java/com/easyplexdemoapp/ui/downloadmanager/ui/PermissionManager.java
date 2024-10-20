package com.easyplexdemoapp.ui.downloadmanager.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.easyplexdemoapp.ui.downloadmanager.core.RepositoryHelper;
import com.easyplexdemoapp.ui.downloadmanager.core.settings.SettingsRepository;
import com.easyplexdemoapp.ui.downloadmanager.core.utils.Utils;

import java.util.ArrayList;

public class PermissionManager {
    private ActivityResultLauncher<String[]> permissions;
    private final Context appContext;
    private SettingsRepository pref;

    public PermissionManager(
            @NonNull ComponentActivity activity,
            @NonNull Callback callback
    ) {
        appContext = activity.getApplicationContext();
        pref = RepositoryHelper.getSettingsRepository(appContext);

        permissions = activity.registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean storageResult = result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    Boolean notificationsResult;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        notificationsResult = result.get(Manifest.permission.POST_NOTIFICATIONS);
                    } else {
                        notificationsResult = true;
                    }
                    if (storageResult != null) {
                        callback.onStorageResult(
                                storageResult,
                                Utils.shouldRequestStoragePermission(activity)
                        );
                    }
                    if (notificationsResult != null) {
                        callback.onNotificationResult(
                                notificationsResult,
                                pref.askNotificationPermission()
                        );
                    }
                }
        );
    }



    public void requestPermissions() {
        ArrayList<String> permissionsList = new ArrayList<>();
        permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionsList.add(Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsList.add(Manifest.permission.POST_NOTIFICATIONS);
        }
        permissions.launch(permissionsList.toArray(new String[0]));
    }

    public void setDoNotAskNotifications(boolean doNotAsk) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pref.askNotificationPermission(!doNotAsk);
        }
    }

    public boolean checkStoragePermissions() {
        return ContextCompat.checkSelfPermission(appContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkNotificationsPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(appContext,
                    Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        } else {
            return false;
        }
    }

    public boolean checkPermissions() {
        return checkStoragePermissions() && checkNotificationsPermissions();
    }

    public interface Callback {
        void onStorageResult(boolean isGranted, boolean shouldRequestStoragePermission);

        void onNotificationResult(boolean isGranted, boolean shouldRequestNotificationPermission);
    }
}
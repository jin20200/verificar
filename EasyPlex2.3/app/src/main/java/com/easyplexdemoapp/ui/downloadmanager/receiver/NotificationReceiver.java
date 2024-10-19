package com.easyplexdemoapp.ui.downloadmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.easyplexdemoapp.BuildConfig;
import com.easyplexdemoapp.ui.base.BaseActivity;
import com.easyplexdemoapp.ui.downloadmanager.service.DownloadService;


/*
 * The receiver for actions of foreground notification, added by service.
 */

public class NotificationReceiver extends BroadcastReceiver
{
    public static final String NOTIFY_ACTION_SHUTDOWN_APP = BuildConfig.APPLICATION_ID +".ui.downloadmanager.receiver.NotificationReceiver.NOTIFY_ACTION_SHUTDOWN_APP";
    public static final String NOTIFY_ACTION_PAUSE_ALL = BuildConfig.APPLICATION_ID +".ui.downloadmanager.receiver.NotificationReceiver.NOTIFY_ACTION_PAUSE_ALL";
    public static final String NOTIFY_ACTION_RESUME_ALL = BuildConfig.APPLICATION_ID +".ui.downloadmanager.receiver.NotificationReceiver.NOTIFY_ACTION_RESUME_ALL";
    public static final String NOTIFY_ACTION_PAUSE_RESUME = BuildConfig.APPLICATION_ID +".ui.downloadmanager.receiver.NotificationReceiver.NOTIFY_ACTION_PAUSE_RESUME";
    public static final String NOTIFY_ACTION_CANCEL = BuildConfig.APPLICATION_ID +".ui.downloadmanager.receiver.NotificationReceiver.NOTIFY_ACTION_CANCEL";
    public static final String NOTIFY_ACTION_REPORT_APPLYING_PARAMS_ERROR = BuildConfig.APPLICATION_ID +".ui.downloadmanager.receiver.NotificationReceiver.NOTIFY_ACTION_REPORT_APPLYING_PARAMS_ERROR";
    public static final String TAG_ID = "id";
    public static final String TAG_ERR = "err";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        if (action == null)
            return;
        Intent mainIntent;
        Intent serviceIntent;
        /* Send action to the already running service */
        switch (action) {
            case NOTIFY_ACTION_SHUTDOWN_APP:
                mainIntent = new Intent(context.getApplicationContext(), BaseActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mainIntent.setAction(NOTIFY_ACTION_SHUTDOWN_APP);
                context.startActivity(mainIntent);

                serviceIntent = new Intent(context.getApplicationContext(), DownloadService.class);
                serviceIntent.setAction(NOTIFY_ACTION_SHUTDOWN_APP);
                context.startService(serviceIntent);
                break;
            case NOTIFY_ACTION_PAUSE_ALL:
                serviceIntent = new Intent(context.getApplicationContext(), DownloadService.class);
                serviceIntent.setAction(NOTIFY_ACTION_PAUSE_ALL);
                context.startService(serviceIntent);
                break;
            case NOTIFY_ACTION_RESUME_ALL:
                serviceIntent = new Intent(context.getApplicationContext(), DownloadService.class);
                serviceIntent.setAction(NOTIFY_ACTION_RESUME_ALL);
                context.startService(serviceIntent);
                break;
            case NOTIFY_ACTION_PAUSE_RESUME:
                serviceIntent = new Intent(context.getApplicationContext(), DownloadService.class);
                serviceIntent.setAction(NOTIFY_ACTION_PAUSE_RESUME);
                serviceIntent.putExtra(TAG_ID, intent.getSerializableExtra(TAG_ID));
                context.startService(serviceIntent);
                break;
            case NOTIFY_ACTION_CANCEL:
                serviceIntent = new Intent(context.getApplicationContext(), DownloadService.class);
                serviceIntent.setAction(NOTIFY_ACTION_CANCEL);
                serviceIntent.putExtra(TAG_ID, intent.getSerializableExtra(TAG_ID));
                context.startService(serviceIntent);
                break;
            case NOTIFY_ACTION_REPORT_APPLYING_PARAMS_ERROR:
                break;
        }
    }
}

package com.easyplexdemoapp.ui.notifications;

import static com.easyplexdemoapp.ui.notifications.NotificationManager.ACTION_NOTIFICATION_PROCESSED;
import static com.easyplexdemoapp.ui.notifications.NotificationManager.NOTIFICATION_RECEIVED;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.util.Tools;

import java.util.Date;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

public class NotificationHandlerService extends Service {

    private static final String CHANNEL_ID = "SilentNotificationHandlerChannel";
    private static final int NOTIFICATION_ID = 1;


    @Inject
    MediaRepository mediaRepository;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Create a silent notification
        Notification silentNotification = createSilentNotification();

        // Start as a foreground service with the appropriate type
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, silentNotification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC);
        } else {
            startForeground(NOTIFICATION_ID, silentNotification);
        }

        if (intent != null) {
            String tmdb = intent.getStringExtra("tmdb");
            if (tmdb != null && !tmdb.isEmpty()) {
                handleNotification(intent, startId, tmdb);
            } else {
                Timber.e("Received intent without TMDB ID");
                stopSelf(startId);
            }
        }

        return START_NOT_STICKY;
    }

    private void handleNotification(Intent intent, int startId, String tmdb) {
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        String image = intent.getStringExtra("image");
        String type = intent.getStringExtra("type");
        String link = intent.getStringExtra("link");
        int notificationId = intent.getIntExtra("notificationId", -1);
        long timestampLong = intent.getLongExtra("timestamp", System.currentTimeMillis());
        Date timestamp = new Date(timestampLong);

        if (tmdb == null || tmdb.isEmpty()) {
            tmdb = String.valueOf(Tools.createRandomCode(2));
        }

        final String finalTmdb = tmdb;

        String finalTmdb1 = tmdb;
        compositeDisposable.add(
                Single.fromCallable(() -> mediaRepository.hasNotification(Integer.parseInt(finalTmdb)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                hasNotification -> {
                                    if (hasNotification) {
                                        updateUIForExistingNotification(finalTmdb);
                                        stopSelf(startId);
                                    } else {
                                        addNewNotification(title, message, image, type, finalTmdb, link, notificationId, timestamp, startId);
                                    }
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ACTION_NOTIFICATION_PROCESSED));
                                    stopSelf(startId);
                                },
                                throwable -> {
                                    Timber.e(throwable, "Error processing notification for TMDB: %s", finalTmdb1);
                                    stopSelf(startId);
                                }
                        )
        );
    }

    private void addNewNotification(String title, String message, String image, String type,
                                    String tmdb, String link, int notificationId, Date timestamp, int startId) {
        com.easyplexdemoapp.data.local.entity.Notification newNotification = new com.easyplexdemoapp.data.local.entity.Notification();
        newNotification.setImdb(tmdb);
        newNotification.setTitle(title);
        newNotification.setOverview(message);
        newNotification.setType(type);
        newNotification.setBackdrop(image);
        newNotification.setNotificationId(notificationId);
        newNotification.setTimestamp(timestamp);
        newNotification.setLink(link);

        compositeDisposable.add(Completable.fromAction(() -> mediaRepository.addNotification(newNotification))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            broadcastNotificationUpdate(tmdb);
                            Timber.tag("NotificationHandlerServ").d("Notification added successfully");
                            stopForeground(true);
                            stopSelf(startId);
                        },
                        throwable -> {
                            Timber.tag("NotificationHandlerServ").e(throwable, "Error adding notification");
                            stopForeground(true);
                            stopSelf(startId);
                        }
                ));
    }

    private void updateUIForExistingNotification(String tmdb) {
        broadcastNotificationUpdate(tmdb);
        Timber.tag("NotificationHandlerServ").d("Existing notification found, updating UI");
    }

    private void broadcastNotificationUpdate(String tmdb) {
        Intent broadcastIntent = new Intent(NOTIFICATION_RECEIVED);
        broadcastIntent.putExtra("tmdb", tmdb);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    private Notification createSilentNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("")
                .setContentText("")
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setNotificationSilent()
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Silent Notification Handler Channel",
                    NotificationManager.IMPORTANCE_MIN);
            channel.setSound(null, null);
            channel.enableLights(false);
            channel.enableVibration(false);
            channel.setShowBadge(false);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
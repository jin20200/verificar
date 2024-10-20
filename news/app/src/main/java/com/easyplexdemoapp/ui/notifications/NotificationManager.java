package com.easyplexdemoapp.ui.notifications;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.episode.LatestEpisodes;
import com.easyplexdemoapp.data.local.entity.Notification;
import com.easyplexdemoapp.data.repository.AnimeRepository;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.splash.SplashActivity;
import com.easyplexdemoapp.util.GlideApp;
import com.easyplexdemoapp.util.Tools;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.AndroidInjection;


/**
 * EasyPlex - Android Movie Portal App
 * @package EasyPlex - Android Movie Portal App
 * @author      @Y0bEX
 * @copyright Copyright (c) 2021 Y0bEX,
 * @license     <a href="http://codecanyon.net/wiki/support/legal-terms/licensing-terms/">...</a>
 * @profile <a href="https://codecanyon.net/user/yobex">...</a>
 * @link yobexd@gmail.com
 * @skype yobexd@gmail.com
 **/


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class NotificationManager extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "CHANNEL_ID";
    private Uri defaultSoundUri;

    public static final String NOTIFICATION_RECEIVED = "com.easyplexdemoapp.NOTIFICATION_RECEIVED";

    public static final String ACTION_NOTIFICATION_PROCESSED = "com.easyplexdemoapp.ACTION_NOTIFICATION_PROCESSED";



    @Inject
    SettingsManager settingsManager;

    @Inject
    AnimeRepository animeRepository;


    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        AndroidInjection.inject(this);

        if (!remoteMessage.getData().isEmpty()) {
            createNotification(remoteMessage);

        }

    }



    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);


    }

    private void createNotification(RemoteMessage remoteMessage) {

        Map<String, String> remoteData = remoteMessage.getData();

        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");
        String image = remoteMessage.getData().get("image");
        String type = remoteMessage.getData().get("type");
        String imdb = remoteMessage.getData().get("tmdb");
        String link = remoteData.get("link");


        if (settingsManager.getSettings().getNotificationCounter() == 1){

            // Send broadcast to update UI
            Intent intentCounter = new Intent(NOTIFICATION_RECEIVED);


            // Send local broadcast
            intentCounter.putExtra("title", title);
            intentCounter.putExtra("message", message);
            intentCounter.putExtra("image", image);
            intentCounter.putExtra("type", type);
            intentCounter.putExtra("tmdb", imdb);
            intentCounter.putExtra("link", link);
            intentCounter.putExtra("timestamp", System.currentTimeMillis());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intentCounter);


        }



        defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (link !=null && !link.isEmpty()) {


            final Bitmap[] bitmap = {null};

            GlideApp.with(getApplicationContext())
                    .asBitmap()
                    .load(image)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                            bitmap[0] = resource;
                            notificationLink(bitmap[0], title, message,link);

                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                            //
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            notificationLink(getBitmapFromURL(), title, message,link);
                        }
                    });


        }else if (type != null) {
            if (type.equals("0")) {
                Media movieDetail = new Media();
                assert imdb != null;
                movieDetail.setId(imdb);
                Tools.loadGlide(this, image, movieDetail, title, message, settingsManager, "0", null);
            } else if (type.equals("1")) {
                Media movieDetail = new Media();
                assert imdb != null;
                movieDetail.setId(imdb);

                Tools.loadGlide(this, image, movieDetail, title, message, settingsManager, "1", null);
            } else if (type.equals("2")) {
                Media movieDetail = new Media();
                assert imdb != null;
                movieDetail.setId(imdb);

                Tools.loadGlide(this, image, movieDetail, title, message, settingsManager, "2", null);
            } else if (type.equals("3")) {
                Media movieDetail = new Media();
                assert imdb != null;
                movieDetail.setId(imdb);

                Tools.loadGlide(this, image, movieDetail, title, message, settingsManager, "3", null);
            } else if (type.equals("episode")) {
                LatestEpisodes latestEpisodes = new LatestEpisodes();
                latestEpisodes.setType("serie");
                if (imdb != null) {
                    latestEpisodes.setEpisodeId(Integer.parseInt(imdb));
                }


                Tools.loadGlide(this, image, null, title, message, settingsManager, "episode", latestEpisodes);
            } else if (type.equals("episode_anime")) {
                LatestEpisodes latestEpisodes = new LatestEpisodes();
                latestEpisodes.setType("anime");
                if (imdb != null) {
                    latestEpisodes.setAnimeEpisodeId(Integer.parseInt(imdb));
                }

                Tools.loadGlide(this, image, null, title, message, settingsManager, "episode_anime", latestEpisodes);

            } else if (type.equals("custom")) {
                if (image != null && !image.isEmpty()) {

                    final Bitmap[] bitmap = {null};

                    GlideApp.with(getApplicationContext())
                            .asBitmap()
                            .load(image)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                    bitmap[0] = resource;
                                    notificationCustom(bitmap[0], title, message);

                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                    //
                                }

                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable) {

                                    notificationCustom(getBitmapFromURL(), title, message);
                                }
                            });


                } else {


                    Intent intent = new Intent(NotificationManager.this, SplashActivity.class);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    stackBuilder.addNextIntentWithParentStack(intent);


                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                            0, Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder notificationBuilder =
                            new NotificationCompat.Builder(NotificationManager.this, CHANNEL_ID)
                                    .setSmallIcon(R.drawable.notification_smal_size)
                                    .setContentTitle(title)
                                    .setContentText(message)
                                    .setAutoCancel(true)
                                    .setSound(defaultSoundUri)
                                    .setContentIntent(resultPendingIntent);


                    android.app.NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    // Since android Oreo notification channel is needed.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                                settingsManager.getSettings().getAppName(),
                                android.app.NotificationManager.IMPORTANCE_DEFAULT);
                        notificationManager.createNotificationChannel(channel);
                    }

                    if (settingsManager.getSettings().getNotificationSeparated() == 1) {

                        notificationManager.notify(Tools.createRandomCode(2), notificationBuilder.build());

                    } else {

                        notificationManager.notify(0, notificationBuilder.build());
                    }

                }
            }
        }


    }


    private void notificationLink(Bitmap bitmap, String title, String message, String link) {


        Intent chooserIntent = getChooserIntent(link);

        // Create a PendingIntent for the chooser
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                chooserIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Create a NotificationCompat.Builder to build the notification
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.notification_smal_size)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                        .setContentIntent(resultPendingIntent);

        // Get the NotificationManager from the system service
        android.app.NotificationManager notificationManager =
                (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    settingsManager.getSettings().getAppName(),
                    android.app.NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Check if notifications should be separated
        if (settingsManager.getSettings().getNotificationSeparated() == 1) {
            // Use a random code as the notification ID
            notificationManager.notify(Tools.createRandomCode(2), notificationBuilder.build());
        } else {
            // Use a fixed ID for notificationManager.notify()
            notificationManager.notify(0, notificationBuilder.build());
        }
    }

    public @NonNull Intent getChooserIntent(String link) {
        // Create an Intent to open the provided link
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);

        // Try to open with Chrome first
        intent.setPackage("com.android.chrome");

        // Check if Chrome is available
        if (intent.resolveActivity(getPackageManager()) == null) {
            // Chrome not available, try other browsers
            intent.setPackage(null);
        }

        // List of common browser packages
        List<String> browserPackages = Arrays.asList(
                "com.android.chrome",
                "org.mozilla.firefox",
                "com.opera.browser",
                "com.microsoft.emmx", // Edge
                "com.brave.browser",
                "com.samsung.android.app.sbrowser" // Samsung Internet
        );

        // Create a list of intents for available browsers
        List<Intent> browserIntents = new ArrayList<>();
        for (String packageName : browserPackages) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            browserIntent.setPackage(packageName);
            if (browserIntent.resolveActivity(getPackageManager()) != null) {
                browserIntents.add(browserIntent);
            }
        }

        // Add the default intent as the last option
        browserIntents.add(intent);

        // Create a chooser Intent with the list of browser intents
        Intent chooserIntent = Intent.createChooser(browserIntents.remove(0), "Open with");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, browserIntents.toArray(new Parcelable[0]));
        return chooserIntent;
    }

    private void notificationCustom(Bitmap bitmap, String title, String message) {


        Intent intent = new Intent(NotificationManager.this, SplashActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addNextIntentWithParentStack(intent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0, Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(NotificationManager.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.notification_smal_size)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                        .setContentIntent(resultPendingIntent);


        android.app.NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    settingsManager.getSettings().getAppName(),
                    android.app.NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        if (settingsManager.getSettings().getNotificationSeparated() == 1) {

            notificationManager.notify(Tools.createRandomCode(2), notificationBuilder.build());

        }else {

            notificationManager.notify(0, notificationBuilder.build());
        }



    }


    public Bitmap getBitmapFromURL() {

        return BitmapFactory.decodeResource(getResources(), R.drawable.placehoder_episodes);
    }

}

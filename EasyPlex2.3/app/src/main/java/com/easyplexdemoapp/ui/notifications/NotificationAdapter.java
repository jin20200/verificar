package com.easyplexdemoapp.ui.notifications;

import static com.easyplexdemoapp.util.Constants.APP_DEFAULT_LANG;
import static com.easyplexdemoapp.util.Constants.ARG_MOVIE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.local.entity.Notification;
import com.easyplexdemoapp.ui.animes.AnimeDetailsActivity;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.moviedetails.MovieDetailsActivity;
import com.easyplexdemoapp.ui.seriedetails.SerieDetailsActivity;
import com.easyplexdemoapp.ui.streaming.StreamingetailsActivity;
import com.easyplexdemoapp.util.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notifications;
    private Context context;
    private OnNotificationClickListener listener;
    private SharedPreferences sharedPreferences;
    private Dialog dialog;
    private SettingsManager settingsManager;

    public interface OnNotificationClickListener {
        void onNotificationClick(Notification notification, int position, Dialog dialog);
    }


    public NotificationAdapter(Context context,SharedPreferences sharedPreferences,SettingsManager settingsManager){
       this.context = context;
       this.sharedPreferences = sharedPreferences;
       this.settingsManager = settingsManager;
    }


    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view, listener);
    }

    public void setListener(OnNotificationClickListener listener) {
        this.listener = listener;
    }

    public void setNotifications(List<Notification> notifications,Dialog dialog) {
        this.notifications = notifications;
        this.dialog = dialog;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {

        Context context = holder.itemView.getContext();

        Notification notification = notifications.get(position);

        String type = notification.getType();

        Media media = new Media();

        media.setId(notification.getImdb());


        holder.titleTextView.setText(notification.getTitle());
        // Set the time ago
        String timeAgo = getTimeAgo(notification.getTimestamp());
        holder.timeAgoTextView.setText(timeAgo);
        holder.overviewTextView.setText(notification.getOverview());

        if (type == null){

            type = "unknown";
        }

        switch (type){

            case "0" :
                holder.type.setText(context.getText(R.string.movie));
                break;
            case "1" :
                holder.type.setText(context.getText(R.string.serie));
                break;
            case "2" :
                holder.type.setText(context.getText(R.string.anime));
                break;
            case "3" :
                holder.type.setText(context.getText(R.string.streaming));
                break;
            case "custom" :
                holder.type.setText(context.getText(R.string.browser));
                break;
        }


        Glide.with(holder.itemView.getContext())
                .load(notification.getBackdrop() == null ? settingsManager.getSettings().getDefaultMediaPlaceholderPath() : notification.getBackdrop())
                .into(holder.backdropImageView);



        holder.rootLayout.setOnLongClickListener(v -> {


            Toast.makeText(context, ""+notification.getType() +  " - " + notification.getImdb(), Toast.LENGTH_SHORT).show();
            return false;
        });


        String finalType = type;
        holder.rootLayout.setOnClickListener(v -> {
            if (listener != null) {
                if (position != RecyclerView.NO_POSITION) {
                    listener.onNotificationClick(notification,position,dialog);


                    if ("custom".equals(finalType)){

                        // Create an Intent to open the provided link
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(notification.getLink()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);

                        // Try to open with Chrome first
                        intent.setPackage("com.android.chrome");

                        // Check if Chrome is available
                        if (intent.resolveActivity(context.getPackageManager()) == null) {
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
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(notification.getLink()));
                            browserIntent.setPackage(packageName);
                            if (browserIntent.resolveActivity(context.getPackageManager()) != null) {
                                browserIntents.add(browserIntent);
                            }
                        }

                        // Add the default intent as the last option
                        browserIntents.add(intent);

                        // Create a chooser Intent with the list of browser intents
                        Intent chooserIntent = Intent.createChooser(browserIntents.remove(0), "Open with");
                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, browserIntents.toArray(new Parcelable[0]));
                        context.startActivity(chooserIntent);

                    } else if ("3".equals(finalType)) {
                        Intent intent = new Intent(context, StreamingetailsActivity.class);
                        intent.putExtra(ARG_MOVIE, media);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    } else if ("2".equals(finalType)) {
                        Intent intent = new Intent(context, AnimeDetailsActivity.class);
                        intent.putExtra(ARG_MOVIE, media);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else if ("1".equals(finalType)) {
                        Intent intent = new Intent(context, SerieDetailsActivity.class);
                        intent.putExtra(ARG_MOVIE, media);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else if ("0".equals(finalType)) {
                        Intent intent = new Intent(context, MovieDetailsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(ARG_MOVIE, media);
                        context.startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (notifications != null) {
            return notifications.size();
        } else {
            return 0;
        }
    }


    private String getSelectedLanguageCode() {
        return sharedPreferences.getString("selectedLanguage", APP_DEFAULT_LANG);
    }

    private Resources getLocalizedResources() {
        String languageCode = getSelectedLanguageCode();
        Locale locale = new Locale(languageCode);

        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        return context.createConfigurationContext(configuration).getResources();
    }


    public String getTimeAgo(Date timestamp) {
        Resources resources = getLocalizedResources();
        long now = System.currentTimeMillis();
        long time = timestamp.getTime();

        long diff = now - time;

        if (diff < DateUtils.MINUTE_IN_MILLIS) {
            return resources.getString(R.string.just_now);
        } else if (diff < DateUtils.HOUR_IN_MILLIS) {
            long minutes = diff / DateUtils.MINUTE_IN_MILLIS;
            return resources.getQuantityString(R.plurals.minutes_ago, (int) minutes, minutes);
        } else if (diff < DateUtils.DAY_IN_MILLIS) {
            long hours = diff / DateUtils.HOUR_IN_MILLIS;
            return resources.getQuantityString(R.plurals.hours_ago, (int) hours, hours);
        } else if (diff < 7 * DateUtils.DAY_IN_MILLIS) {
            long days = diff / DateUtils.DAY_IN_MILLIS;
            return resources.getQuantityString(R.plurals.days_ago, (int) days, days);
        } else {
            return DateUtils.formatDateTime(context, time, DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
        }
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView backdropImageView;
        TextView overviewTextView;
        TextView timeAgoTextView;
        LinearLayout rootLayout;
        TextView type;

        NotificationViewHolder(View itemView, final OnNotificationClickListener listener) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            backdropImageView = itemView.findViewById(R.id.thumbnailImageView);
            overviewTextView = itemView.findViewById(R.id.overviewTextView);
            timeAgoTextView = itemView.findViewById(R.id.notificationTimeAgo);
            rootLayout = itemView.findViewById(R.id.rootLayout);
            type = itemView.findViewById(R.id.type);



        }
    }

}

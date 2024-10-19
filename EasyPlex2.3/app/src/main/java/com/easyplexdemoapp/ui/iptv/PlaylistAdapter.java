package com.easyplexdemoapp.ui.iptv;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import static com.easyplexdemoapp.util.Constants.ARG_MOVIE;
import static com.easyplexdemoapp.util.Constants.MAXPLAYER_INTENT;
import static com.easyplexdemoapp.util.Constants.PLAYER_HEADER;
import static com.easyplexdemoapp.util.Constants.PLAYER_USER_AGENT;
import static com.easyplexdemoapp.util.Constants.VLC_INTENT;
import static com.easyplexdemoapp.util.Constants.VLC_PACKAGE_NAME;
import static com.easyplexdemoapp.util.Tools.MEDIA_TITLE;
import static com.easyplexdemoapp.util.Tools.POSTER;
import static com.easyplexdemoapp.util.Tools.SECURE_URI;
import static com.easyplexdemoapp.util.Tools.TITLE;
import static com.easyplexdemoapp.util.Tools.VIDEOTYPE;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.iptvplaylist.PlaylistItem;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.ui.player.activities.EasyPlexMainPlayer;
import com.easyplexdemoapp.ui.player.activities.EasyPlexPlayerActivity;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private List<PlaylistItem> items;
    private Context context;
    private SettingsManager settingsManager;
    private AuthManager authManager;
    private TokenManager tokenManager;



    public PlaylistAdapter(Context context, List<PlaylistItem> items,SettingsManager settingsManager,AuthManager authManager,TokenManager tokenManager) {
        this.context = context;
        this.items = items;
        this.settingsManager = settingsManager;
        this.authManager = authManager;
        this.tokenManager = tokenManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlaylistItem item = items.get(position);
        holder.titleTextView.setText(item.getTitle());

        // Load a placeholder image using Glide
        Glide.with(context)
                .load(item.getLogoUrl())
                .placeholder(R.drawable.media_placeholder)
                .into(holder.thumbnailImageView);


        holder.rootLayout.setOnClickListener(v -> holder.thumbnailImageView.performClick());

        holder.thumbnailImageView.setOnClickListener(v -> {

            if (authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() !=null) {

                onLoadStream(item);


            }else if (settingsManager.getSettings().getWachAdsToUnlock() == 1 && authManager.getUserInfo().getPremuim() == 0) {


                onLoadSubscribeDialog(item);

            } else if (settingsManager.getSettings().getWachAdsToUnlock() == 0 && authManager.getUserInfo().getPremuim() == 0){

                onLoadStream(item);


            }


        });


    }

    private void onLoadSubscribeDialog(PlaylistItem item) {


    }

    private void onLoadStream(PlaylistItem item) {

        CastSession castSession = CastContext.getSharedInstance(context).getSessionManager()
                .getCurrentCastSession();

        if (castSession !=null && castSession.isConnected()) {

            startStreamCasting(item);

        }else {

            if (settingsManager.getSettings().getVlc() == 1) {

                startStreamFromExternalLaunchers(item);

            }else {

                startStreamFromDialog(item);
            }

        }
    }

    private void startStreamFromDialog(PlaylistItem item) {


        Media movieDetail = new Media();

        movieDetail.setName(item.getTitle());
        movieDetail.setPosterPath(item.getLogoUrl());

        String artwork = movieDetail.getPosterPath();
        String name = movieDetail.getName();
        String type = "streaming";

        Timber.i(item.getStreamUrl());

        Intent intent = new Intent(context, EasyPlexMainPlayer.class);
        intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY, MediaModel.media(String.valueOf(0),
                null,null,type, name, item.getStreamUrl(), artwork, null
                , null, null,null,
                null,null,
                null,
                null,null,null,
                1,null,null,
                null,0,0,
                null,null,0,null,null,0));
        intent.putExtra(ARG_MOVIE, movieDetail);
        context.startActivity(intent);
    }

    private void startStreamFromExternalLaunchers(PlaylistItem item) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_bottom_stream);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.gravity = Gravity.BOTTOM;
        lp.width = MATCH_PARENT;
        lp.height = MATCH_PARENT;


        LinearLayout vlc = dialog.findViewById(R.id.vlc);
        LinearLayout mxPlayer = dialog.findViewById(R.id.mxPlayer);
        LinearLayout easyplexPlayer = dialog.findViewById(R.id.easyplexPlayer);
        LinearLayout webcast = dialog.findViewById(R.id.webCast);


        String url = item.getStreamUrl();

        MEDIA_TITLE = item.getTitle();


        vlc.setOnClickListener(v12 -> {



            Intent shareVideo = new Intent(Intent.ACTION_VIEW);
            shareVideo.setDataAndType(Uri.parse(item.getStreamUrl()), VIDEOTYPE);
            shareVideo.setPackage(VLC_PACKAGE_NAME);
            shareVideo.putExtra(TITLE, MEDIA_TITLE);
            shareVideo.putExtra(POSTER, item.getLogoUrl());
            shareVideo.putExtra(SECURE_URI, true);
            try {
                context.startActivity(shareVideo);
            } catch (ActivityNotFoundException ex) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(VLC_INTENT));
                context.startActivity(intent);
            }
            dialog.hide();
        });

        mxPlayer.setOnClickListener(v12 -> {
            Intent shareVideo = new Intent(Intent.ACTION_VIEW);
            shareVideo.setDataAndType(Uri.parse(url), VIDEOTYPE);
            shareVideo.setPackage(MAXPLAYER_INTENT);
            shareVideo.putExtra(TITLE, MEDIA_TITLE);
            shareVideo.putExtra(POSTER, item.getLogoUrl());
            shareVideo.putExtra(SECURE_URI, true);
            try {
                context.startActivity(shareVideo);
            } catch (ActivityNotFoundException ex) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                String uriString = "market://details?id="+MAXPLAYER_INTENT ;
                intent.setData(Uri.parse(uriString));
                context.startActivity(intent);
            }
            dialog.hide();

        });

        webcast.setOnClickListener(v12 -> {
            Intent shareVideo = new Intent(Intent.ACTION_VIEW);
            shareVideo.setDataAndType(Uri.parse(url), VIDEOTYPE);
            shareVideo.setPackage("com.instantbits.cast.webvideo");
            shareVideo.putExtra(TITLE, MEDIA_TITLE);
            shareVideo.putExtra(POSTER, item.getLogoUrl());
            shareVideo.putExtra(SECURE_URI, true);
            try {
                context.startActivity(shareVideo);
            } catch (ActivityNotFoundException ex) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                String uriString = "market://details?id=com.instantbits.cast.webvideo";
                intent.setData(Uri.parse(uriString));
                context.startActivity(intent);
            }
            dialog.hide();

        });

        easyplexPlayer.setOnClickListener(v12 -> {
            startStreamFromDialog(item);
            dialog.hide();


        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.findViewById(R.id.bt_close).setOnClickListener(x ->
        dialog.dismiss());
        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    private void startStreamCasting(PlaylistItem item) {


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImageView;
        TextView titleTextView;
        TextView programTitleTextView;
        TextView programTimeTextView;
        LinearLayout rootLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            rootLayout = itemView.findViewById(R.id.rootLayout);
        }
    }
}
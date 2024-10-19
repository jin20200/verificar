
package com.easyplexdemoapp.ui.downloadmanager.ui.main;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.easyplexdemoapp.util.Constants.ARG_MOVIE;
import static com.easyplexdemoapp.util.Constants.ARG_MOVIE_HISTORY;
import static com.easyplexdemoapp.util.Tools.EXTRA_HEADERS;
import static com.easyplexdemoapp.util.Tools.HEADERS;
import static com.easyplexdemoapp.util.Tools.POSTER;
import static com.easyplexdemoapp.util.Tools.SECURE_URI;
import static com.easyplexdemoapp.util.Tools.TITLE;
import static com.easyplexdemoapp.util.Tools.USER_AGENT;
import static com.easyplexdemoapp.util.Tools.VIDEOTYPE;
import static com.google.android.gms.cast.MediaStatus.REPEAT_MODE_REPEAT_OFF;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.Download;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.ui.base.BaseActivity;
import com.easyplexdemoapp.ui.downloadmanager.core.model.data.StatusCode;
import com.easyplexdemoapp.ui.downloadmanager.core.model.data.entity.DownloadPiece;
import com.easyplexdemoapp.ui.downloadmanager.core.system.FileSystemFacade;
import com.easyplexdemoapp.ui.downloadmanager.core.system.SystemFacadeHelper;
import com.easyplexdemoapp.ui.downloadmanager.core.utils.DateUtils;
import com.easyplexdemoapp.ui.downloadmanager.core.utils.Utils;
import com.easyplexdemoapp.ui.downloadmanager.ui.Selectable;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.player.activities.EasyPlexMainPlayer;
import com.easyplexdemoapp.ui.player.activities.EasyPlexPlayerActivity;
import com.easyplexdemoapp.ui.player.cast.ExpandedControlsActivity;
import com.easyplexdemoapp.ui.player.cast.queue.QueueDataProvider;
import com.easyplexdemoapp.util.Tools;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.MediaTrack;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.images.WebImage;

import java.util.ArrayList;
import java.util.List;

public class DownloadListAdapter extends ListAdapter<DownloadItem, DownloadListAdapter.ViewHolder>
        implements Selectable<DownloadItem>
{
    private static final int VIEW_QUEUE = 0;
    private static final int VIEW_FINISH = 1;
    private static final int VIEW_ERROR = 2;
    private final ClickListener listener;
    private SelectionTracker<DownloadItem> selectionTracker;
    private OnItemClickListener onItemClickListener;
    private final MediaRepository mediaRepository;
    private final SettingsManager settingsManager;
    private FileSystemFacade fs;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public DownloadListAdapter(ClickListener listener,MediaRepository mediaRepository,SettingsManager settingsManager)
    {
        super(diffCallback);
        this.listener = listener;
        this.settingsManager = settingsManager;
        this.mediaRepository = mediaRepository;
    }

    public void setSelectionTracker(SelectionTracker<DownloadItem> selectionTracker)
    {
        this.selectionTracker = selectionTracker;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        switch (viewType) {
            case VIEW_ERROR:
                return new ErrorViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_download_list_error, parent, false));
            case VIEW_FINISH:
                return new FinishViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_download_list_finish, parent, false));
            default:
                return new QueueViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_download_list_queue, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        DownloadItem item = getItem(position);

        if (StatusCode.isStatusError(item.info.statusCode))
            return VIEW_ERROR;
        else if (StatusCode.isStatusCompleted(item.info.statusCode))
            return VIEW_FINISH;
        else
            return VIEW_QUEUE;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        DownloadItem item = getItem(position);


        if (selectionTracker != null)
            holder.setSelected(selectionTracker.isSelected(item));

        if (holder instanceof QueueViewHolder) {
            QueueViewHolder queueHolder = (QueueViewHolder)holder;
            queueHolder.bind(item, (QueueClickListener)listener);
        } else if (holder instanceof FinishViewHolder) {
            FinishViewHolder finishHolder = (FinishViewHolder)holder;
            finishHolder.bind(item, (FinishClickListener)listener);
        }  else if (holder instanceof ErrorViewHolder) {
            ErrorViewHolder errorHolder = (ErrorViewHolder)holder;
            errorHolder.bind(item, (ErrorClickListener)listener);
        }
    }

    @Override
    public DownloadItem getItemKey(int position)
    {
        if (position < 0 || position >= getCurrentList().size())
            return null;

        return getItem(position);
    }

    @Override
    public int getItemPosition(DownloadItem key)
    {
        return getCurrentList().indexOf(key);
    }

    interface ViewHolderWithDetails
    {
        ItemDetails getItemDetails();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements ViewHolderWithDetails
    {
        protected CardView cardView;
        protected TextView filename;
        protected TextView mediaName;
        protected TextView mediatype;
        protected TextView status;
        /* For selection support */
        private DownloadItem selectionKey;
        private boolean isSelected;

        ViewHolder(View itemView)
        {
            super(itemView);

            filename = itemView.findViewById(R.id.filename);
            mediaName = itemView.findViewById(R.id.mediaName);
            mediatype = itemView.findViewById(R.id.mediatype);



            status = itemView.findViewById(R.id.status);
        }

        void bind(DownloadItem item, ClickListener listener)
        {
            Context context = itemView.getContext();
            selectionKey = item;

            cardView = (CardView)itemView;
            filename.setText(item.info.fileName);
            mediaName.setText(item.info.mediaName);
        }

        private void setSelected(boolean isSelected)
        {
            this.isSelected = isSelected;
        }

        @Override
        public ItemDetails getItemDetails()
        {
            return new ItemDetails(selectionKey, getBindingAdapterPosition());
        }
    }

    public static class QueueViewHolder extends ViewHolder
    {
        private final ImageButton pauseButton;
        private final AnimatedVectorDrawableCompat playToPauseAnim;
        private final AnimatedVectorDrawableCompat pauseToPlayAnim;
        private AnimatedVectorDrawableCompat currAnim;
        private final ProgressBar progressBar;
        private final ImageButton cancelButton;

        QueueViewHolder(View itemView)
        {
            super(itemView);

            playToPauseAnim = AnimatedVectorDrawableCompat.create(itemView.getContext(), R.drawable.play_to_pause);
            pauseToPlayAnim = AnimatedVectorDrawableCompat.create(itemView.getContext(), R.drawable.pause_to_play);
            pauseButton = itemView.findViewById(R.id.pause);
            progressBar = itemView.findViewById(R.id.progress);
            Utils.colorizeProgressBar(itemView.getContext(), progressBar);
            cancelButton = itemView.findViewById(R.id.cancel);
        }

        void bind(DownloadItem item, QueueClickListener listener)
        {
            super.bind(item, listener);

            setPauseButtonState(StatusCode.isStatusStoppedOrPaused(item.info.statusCode));
            pauseButton.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemPauseClicked(item);
            });
            cancelButton.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemCancelClicked(item);
            });

            Context context = itemView.getContext();
            int size = item.pieces.size();
            long downloadedBytes = 0;
            long speed = 0;
            if (size > 0) {
                for (DownloadPiece piece : item.pieces) {
                    downloadedBytes += item.info.getDownloadedBytes(piece);
                    speed += piece.speed;
                }
            }
            long ETA = Utils.calcETA(item.info.totalBytes, downloadedBytes, speed);

            if (item.info.statusCode == StatusCode.STATUS_RUNNING) {
                progressBar.setVisibility(View.VISIBLE);
                if (item.info.totalBytes > 0) {
                    int progress = (int)((downloadedBytes * 100) / item.info.totalBytes);
                    progressBar.setIndeterminate(false);
                    progressBar.setProgress(progress);
                } else {
                    progressBar.setIndeterminate(true);
                }
                status.setText(context.getString(R.string.download_queued_progress_template,
                        Formatter.formatFileSize(context, downloadedBytes),
                        (item.info.totalBytes == -1 ? context.getString(R.string.not_available) :
                                Formatter.formatFileSize(context, item.info.totalBytes)),
                        (ETA == -1 ? Utils.INFINITY_SYMBOL :
                                DateUtils.formatElapsedTime(context, ETA)),
                        Formatter.formatFileSize(context, speed)));
            } else {
                String statusStr = "";
                if (item.info.statusCode == StatusCode.STATUS_PAUSED) {
                    statusStr = context.getString(R.string.pause);
                } else if (item.info.statusCode == StatusCode.STATUS_STOPPED) {
                    statusStr = context.getString(R.string.stopped);
                } else if (item.info.statusCode == StatusCode.STATUS_PENDING) {
                    statusStr = context.getString(R.string.pending);
                } else if (item.info.statusCode == StatusCode.STATUS_WAITING_FOR_NETWORK) {
                    statusStr = context.getString(R.string.waiting_for_network);
                } else if (item.info.statusCode == StatusCode.STATUS_WAITING_TO_RETRY) {
                    statusStr = context.getString(R.string.waiting_for_retry);
                } else if (item.info.statusCode == StatusCode.STATUS_FETCH_METADATA) {
                    statusStr = context.getString(R.string.downloading_metadata);
                }
                if (item.info.statusCode == StatusCode.STATUS_FETCH_METADATA) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setIndeterminate(true);
                } else {
                    progressBar.setVisibility(View.GONE);
                }

                status.setText(context.getString(R.string.download_queued_template,
                        Formatter.formatFileSize(context, downloadedBytes),
                        (item.info.totalBytes == -1 ? context.getString(R.string.not_available) :
                                Formatter.formatFileSize(context, item.info.totalBytes)),
                        statusStr));
            }
        }

        void setPauseButtonState(boolean isPause)
        {
            AnimatedVectorDrawableCompat prevAnim = currAnim;
            currAnim = (isPause ? pauseToPlayAnim : playToPauseAnim);
            pauseButton.setImageDrawable(currAnim);
            if (currAnim != prevAnim)
                currAnim.start();
        }
    }

    public class FinishViewHolder extends ViewHolder
    {
        private final ImageView icon;
        private final ImageButton menu;
        private final CardView cardViewItem;
        private final TextView downloadType;


        FinishViewHolder(View itemView)
        {
            super(itemView);

            downloadType = itemView.findViewById(R.id.download_type);
            icon = itemView.findViewById(R.id.epcover);
            menu = itemView.findViewById(R.id.menu);
            cardViewItem = itemView.findViewById(R.id.item);


        }

        void bind(DownloadItem item, FinishClickListener listener)
        {
            super.bind(item, listener);

            Context context = itemView.getContext();

            menu.setOnClickListener((v) -> {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.inflate(R.menu.download_item_popup);
                popup.setOnMenuItemClickListener((MenuItem menuItem) -> {
                    if (listener != null)
                        listener.onItemMenuClicked(menuItem.getItemId(), item);
                    return true;
                });
                popup.show();
            });


            cardViewItem.setOnClickListener(v -> {
                assert item.info.mediaId != null;

                mediaRepository.getDownLoadedMediaInfo(Integer.parseInt(item.info.mediaId)).observe((BaseActivity) context,
                        new Observer<>() {
                            @Override
                            public void onChanged(Download download) {

                                fs = SystemFacadeHelper.getFileSystemFacade(context);

                                Uri uri = fs.getFileUri(item.info.dirPath, item.info.fileName);

                                String mediaLink = String.valueOf(uri);
                                if ("0".equals(item.info.mediatype)) {


                                    CastSession castSession = CastContext.getSharedInstance(context).getSessionManager()
                                            .getCurrentCastSession();
                                    if (castSession != null && castSession.isConnected()) {

                                    onLoadChromcastStream(castSession,context,download,mediaLink);

                                    } else {


                                        if (settingsManager.getSettings().getVlc() == 1) {


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

                                            webcast.setOnClickListener(v12 -> {
                                                Intent shareVideo = new Intent(Intent.ACTION_VIEW);
                                                shareVideo.setDataAndType(Uri.parse(mediaLink), VIDEOTYPE);
                                                shareVideo.setPackage("com.instantbits.cast.webvideo");
                                                shareVideo.putExtra(TITLE, download.getTitle());
                                                shareVideo.putExtra(POSTER, download.getBackdropPath());
                                                Bundle headers = new Bundle();
                                                headers.putString(USER_AGENT, settingsManager.getSettings().getAppName());
                                                shareVideo.putExtra(EXTRA_HEADERS, headers);
                                                shareVideo.putExtra(HEADERS, headers);
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

                                            vlc.setOnClickListener(v12 -> {

                                                Intent shareVideo = new Intent(Intent.ACTION_VIEW);
                                                shareVideo.setDataAndType(Uri.parse(mediaLink), "video/*");
                                                shareVideo.setPackage("org.videolan.vlc");
                                                shareVideo.putExtra("title", download.getTitle());
                                                shareVideo.putExtra("poster", download.getBackdropPath());
                                                Bundle headers = new Bundle();
                                                headers.putString("User-Agent", settingsManager.getSettings().getAppName());
                                                shareVideo.putExtra("android.media.intent.extra.HTTP_HEADERS", headers);
                                                shareVideo.putExtra("headers", headers);
                                                shareVideo.putExtra("secure_uri", true);
                                                try {
                                                    context.startActivity(shareVideo);
                                                    dialog.hide();
                                                } catch (ActivityNotFoundException ex) {

                                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                                    String uriString = "market://details?id=org.videolan.vlc";
                                                    intent.setData(Uri.parse(uriString));
                                                    context.startActivity(intent);
                                                }


                                            });


                                            mxPlayer.setOnClickListener(v12 -> {

                                                Intent shareVideo = new Intent(Intent.ACTION_VIEW);
                                                shareVideo.setDataAndType(Uri.parse(mediaLink), "video/*");
                                                shareVideo.setPackage("com.mxtech.videoplayer.ad");
                                                shareVideo.putExtra("title", download.getTitle());
                                                shareVideo.putExtra("poster", download.getBackdropPath());
                                                Bundle headers = new Bundle();
                                                headers.putString("User-Agent", settingsManager.getSettings().getAppName());
                                                shareVideo.putExtra("android.media.intent.extra.HTTP_HEADERS", headers);
                                                shareVideo.putExtra("headers", headers);
                                                shareVideo.putExtra("secure_uri", true);
                                                try {
                                                    context.startActivity(shareVideo);
                                                    dialog.hide();
                                                } catch (ActivityNotFoundException ex) {

                                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                                    String uriString = "market://details?id=com.mxtech.videoplayer.ad";
                                                    intent.setData(Uri.parse(uriString));
                                                    context.startActivity(intent);
                                                }


                                            });


                                            easyplexPlayer.setOnClickListener(v12 -> {
                                                onLoadDownloadMovie(download, mediaLink);
                                                dialog.hide();


                                            });

                                            dialog.show();
                                            dialog.getWindow().setAttributes(lp);

                                            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                                    dialog.dismiss());


                                            dialog.show();
                                            dialog.getWindow().setAttributes(lp);


                                        } else {

                                            onLoadDownloadMovie(download, mediaLink);


                                        }

                                    }
                                } else if ("1".equals(item.info.mediatype)) {
                                    CastSession castSession = CastContext.getSharedInstance(context).getSessionManager()
                                            .getCurrentCastSession();
                                    if (castSession != null && castSession.isConnected()) {

                                        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
                                        movieMetadata.putString(MediaMetadata.KEY_TITLE, download.getTitle());
                                        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, download.getTitle());

                                        movieMetadata.addImage(new WebImage(Uri.parse(download.getPosterPath())));
                                        List<MediaTrack> tracks = new ArrayList<>();


                                        MediaInfo mediaInfo = new MediaInfo.Builder(mediaLink)
                                                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                                                .setMetadata(movieMetadata)
                                                .setMediaTracks(tracks)
                                                .build();

                                        final RemoteMediaClient remoteMediaClient = castSession.getRemoteMediaClient();
                                        if (remoteMediaClient == null) {
                                            return;
                                        }
                                        final QueueDataProvider provider = QueueDataProvider.getInstance(context);
                                        PopupMenu popup = new PopupMenu(context, cardViewItem);
                                        popup.getMenuInflater().inflate(
                                                provider.isQueueDetached() || provider.getCount() == 0
                                                        ? R.menu.detached_popup_add_to_queue
                                                        : R.menu.popup_add_to_queue, popup.getMenu());
                                        PopupMenu.OnMenuItemClickListener clickListener = menuItem -> {
                                            QueueDataProvider provider1 = QueueDataProvider.getInstance(context);
                                            MediaQueueItem queueItem = new MediaQueueItem.Builder(mediaInfo).setAutoplay(
                                                    true).build();
                                            MediaQueueItem[] newItemArray = new MediaQueueItem[]{queueItem};
                                            String toastMessage = null;
                                            if (provider1.isQueueDetached() && provider1.getCount() > 0) {
                                                if ((menuItem.getItemId() == R.id.action_play_now)
                                                        || (menuItem.getItemId() == R.id.action_add_to_queue)) {
                                                    MediaQueueItem[] items = com.easyplexdemoapp.ui.player.cast.utils.Utils
                                                            .rebuildQueueAndAppend(provider1.getItems(), queueItem);
                                                    remoteMediaClient.queueLoad(items, provider1.getCount(),
                                                            REPEAT_MODE_REPEAT_OFF, null);
                                                } else {
                                                    return false;
                                                }
                                            } else {
                                                if (provider1.getCount() == 0) {
                                                    remoteMediaClient.queueLoad(newItemArray, 0,
                                                            REPEAT_MODE_REPEAT_OFF, null);
                                                } else {
                                                    int currentId = provider1.getCurrentItemId();
                                                    if (menuItem.getItemId() == R.id.action_play_now) {
                                                        remoteMediaClient.queueInsertAndPlayItem(queueItem, currentId, null);
                                                    } else if (menuItem.getItemId() == R.id.action_play_next) {
                                                        int currentPosition = provider1.getPositionByItemId(currentId);
                                                        if (currentPosition == provider1.getCount() - 1) {
                                                            //we are adding to the end of queue
                                                            remoteMediaClient.queueAppendItem(queueItem, null);
                                                        } else {
                                                            int nextItemId = provider1.getItem(currentPosition + 1).getItemId();
                                                            remoteMediaClient.queueInsertItems(newItemArray, nextItemId, null);
                                                        }
                                                        toastMessage = context.getString(
                                                                R.string.queue_item_added_to_play_next);
                                                    } else if (menuItem.getItemId() == R.id.action_add_to_queue) {
                                                        remoteMediaClient.queueAppendItem(queueItem, null);
                                                        toastMessage = context.getString(R.string.queue_item_added_to_queue);
                                                    } else {
                                                        return false;
                                                    }
                                                }
                                            }
                                            if (menuItem.getItemId() == R.id.action_play_now) {
                                                Intent intent = new Intent(context, ExpandedControlsActivity.class);
                                                context.startActivity(intent);
                                            }
                                            if (!TextUtils.isEmpty(toastMessage)) {
                                                Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
                                            }
                                            return true;
                                        };
                                        popup.setOnMenuItemClickListener(clickListener);
                                        popup.show();


                                    } else {


                                        if (settingsManager.getSettings().getVlc() == 1) {


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

                                            webcast.setOnClickListener(v12 -> {
                                                Intent shareVideo = new Intent(Intent.ACTION_VIEW);
                                                shareVideo.setDataAndType(Uri.parse(mediaLink), VIDEOTYPE);
                                                shareVideo.setPackage("com.instantbits.cast.webvideo");
                                                shareVideo.putExtra(TITLE, download.getTitle());
                                                shareVideo.putExtra(POSTER, download.getBackdropPath());
                                                Bundle headers = new Bundle();
                                                headers.putString(USER_AGENT, settingsManager.getSettings().getAppName());
                                                shareVideo.putExtra(EXTRA_HEADERS, headers);
                                                shareVideo.putExtra(HEADERS, headers);
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

                                            vlc.setOnClickListener(v12 -> {

                                                Intent shareVideo = new Intent(Intent.ACTION_VIEW);
                                                shareVideo.setDataAndType(Uri.parse(mediaLink), "video/*");
                                                shareVideo.setPackage("org.videolan.vlc");
                                                shareVideo.putExtra("title", download.getTitle());
                                                shareVideo.putExtra("poster", download.getBackdropPath());
                                                Bundle headers = new Bundle();
                                                headers.putString("User-Agent", settingsManager.getSettings().getAppName());
                                                shareVideo.putExtra("android.media.intent.extra.HTTP_HEADERS", headers);
                                                shareVideo.putExtra("headers", headers);
                                                shareVideo.putExtra("secure_uri", true);
                                                try {
                                                    context.startActivity(shareVideo);
                                                    dialog.hide();
                                                } catch (ActivityNotFoundException ex) {

                                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                                    String uriString = "market://details?id=org.videolan.vlc";
                                                    intent.setData(Uri.parse(uriString));
                                                    context.startActivity(intent);
                                                }


                                            });


                                            mxPlayer.setOnClickListener(v12 -> {

                                                Intent shareVideo = new Intent(Intent.ACTION_VIEW);
                                                shareVideo.setDataAndType(Uri.parse(mediaLink), "video/*");
                                                shareVideo.setPackage("com.mxtech.videoplayer.ad");
                                                shareVideo.putExtra("title", download.getTitle());
                                                shareVideo.putExtra("poster", download.getBackdropPath());
                                                Bundle headers = new Bundle();
                                                headers.putString("User-Agent", settingsManager.getSettings().getAppName());
                                                shareVideo.putExtra("android.media.intent.extra.HTTP_HEADERS", headers);
                                                shareVideo.putExtra("headers", headers);
                                                shareVideo.putExtra("secure_uri", true);
                                                try {
                                                    context.startActivity(shareVideo);
                                                    dialog.hide();
                                                } catch (ActivityNotFoundException ex) {

                                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                                    String uriString = "market://details?id=com.mxtech.videoplayer.ad";
                                                    intent.setData(Uri.parse(uriString));
                                                    context.startActivity(intent);
                                                }


                                            });


                                            easyplexPlayer.setOnClickListener(v12 -> {
                                                onLoadDownloadSerie(download, mediaLink);
                                                dialog.hide();


                                            });

                                            dialog.show();
                                            dialog.getWindow().setAttributes(lp);

                                            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                                    dialog.dismiss());


                                            dialog.show();
                                            dialog.getWindow().setAttributes(lp);


                                        } else {

                                            onLoadDownloadSerie(download, mediaLink);

                                        }


                                    }
                                } else if ("anime".equals(item.info.mediatype)) {
                                    CastSession castSession = CastContext.getSharedInstance(context).getSessionManager()
                                            .getCurrentCastSession();
                                    if (castSession != null && castSession.isConnected()) {

                                        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
                                        movieMetadata.putString(MediaMetadata.KEY_TITLE, download.getTitle());
                                        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, download.getTitle());

                                        movieMetadata.addImage(new WebImage(Uri.parse(download.getPosterPath())));
                                        List<MediaTrack> tracks = new ArrayList<>();


                                        MediaInfo mediaInfo = new MediaInfo.Builder(download.getLink())
                                                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                                                .setMetadata(movieMetadata)
                                                .setMediaTracks(tracks)
                                                .build();

                                        final RemoteMediaClient remoteMediaClient = castSession.getRemoteMediaClient();
                                        if (remoteMediaClient == null) {
                                            return;
                                        }
                                        final QueueDataProvider provider = QueueDataProvider.getInstance(context);
                                        PopupMenu popup = new PopupMenu(context, cardViewItem);
                                        popup.getMenuInflater().inflate(
                                                provider.isQueueDetached() || provider.getCount() == 0
                                                        ? R.menu.detached_popup_add_to_queue
                                                        : R.menu.popup_add_to_queue, popup.getMenu());
                                        PopupMenu.OnMenuItemClickListener clickListener = menuItem -> {
                                            QueueDataProvider provider1 = QueueDataProvider.getInstance(context);
                                            MediaQueueItem queueItem = new MediaQueueItem.Builder(mediaInfo).setAutoplay(
                                                    true).build();
                                            MediaQueueItem[] newItemArray = new MediaQueueItem[]{queueItem};
                                            String toastMessage = null;
                                            if (provider1.isQueueDetached() && provider1.getCount() > 0) {
                                                if ((menuItem.getItemId() == R.id.action_play_now)
                                                        || (menuItem.getItemId() == R.id.action_add_to_queue)) {
                                                    MediaQueueItem[] items = com.easyplexdemoapp.ui.player.cast.utils.Utils
                                                            .rebuildQueueAndAppend(provider1.getItems(), queueItem);
                                                    remoteMediaClient.queueLoad(items, provider1.getCount(),
                                                            REPEAT_MODE_REPEAT_OFF, null);
                                                } else {
                                                    return false;
                                                }
                                            } else {
                                                if (provider1.getCount() == 0) {
                                                    remoteMediaClient.queueLoad(newItemArray, 0,
                                                            REPEAT_MODE_REPEAT_OFF, null);
                                                } else {
                                                    int currentId = provider1.getCurrentItemId();
                                                    if (menuItem.getItemId() == R.id.action_play_now) {
                                                        remoteMediaClient.queueInsertAndPlayItem(queueItem, currentId, null);
                                                    } else if (menuItem.getItemId() == R.id.action_play_next) {
                                                        int currentPosition = provider1.getPositionByItemId(currentId);
                                                        if (currentPosition == provider1.getCount() - 1) {
                                                            //we are adding to the end of queue
                                                            remoteMediaClient.queueAppendItem(queueItem, null);
                                                        } else {
                                                            int nextItemId = provider1.getItem(currentPosition + 1).getItemId();
                                                            remoteMediaClient.queueInsertItems(newItemArray, nextItemId, null);
                                                        }
                                                        toastMessage = context.getString(
                                                                R.string.queue_item_added_to_play_next);
                                                    } else if (menuItem.getItemId() == R.id.action_add_to_queue) {
                                                        remoteMediaClient.queueAppendItem(queueItem, null);
                                                        toastMessage = context.getString(R.string.queue_item_added_to_queue);
                                                    } else {
                                                        return false;
                                                    }
                                                }
                                            }
                                            if (menuItem.getItemId() == R.id.action_play_now) {
                                                Intent intent = new Intent(context, ExpandedControlsActivity.class);
                                                context.startActivity(intent);
                                            }
                                            if (!TextUtils.isEmpty(toastMessage)) {
                                                Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
                                            }
                                            return true;
                                        };
                                        popup.setOnMenuItemClickListener(clickListener);
                                        popup.show();


                                    } else {


                                        if (settingsManager.getSettings().getVlc() == 1) {


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

                                            webcast.setOnClickListener(v12 -> {
                                                Intent shareVideo = new Intent(Intent.ACTION_VIEW);
                                                shareVideo.setDataAndType(Uri.parse(mediaLink), VIDEOTYPE);
                                                shareVideo.setPackage("com.instantbits.cast.webvideo");
                                                shareVideo.putExtra(TITLE, download.getTitle());
                                                shareVideo.putExtra(POSTER, download.getBackdropPath());
                                                Bundle headers = new Bundle();
                                                headers.putString(USER_AGENT, settingsManager.getSettings().getAppName());
                                                shareVideo.putExtra(EXTRA_HEADERS, headers);
                                                shareVideo.putExtra(HEADERS, headers);
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

                                            vlc.setOnClickListener(v12 -> {

                                                Intent shareVideo = new Intent(Intent.ACTION_VIEW);
                                                shareVideo.setDataAndType(Uri.parse(download.getLink()), "video/*");
                                                shareVideo.setPackage("org.videolan.vlc");
                                                shareVideo.putExtra("title", download.getTitle());
                                                shareVideo.putExtra("poster", download.getBackdropPath());
                                                Bundle headers = new Bundle();
                                                headers.putString("User-Agent", settingsManager.getSettings().getAppName());
                                                shareVideo.putExtra("android.media.intent.extra.HTTP_HEADERS", headers);
                                                shareVideo.putExtra("headers", headers);
                                                shareVideo.putExtra("secure_uri", true);
                                                try {
                                                    context.startActivity(shareVideo);
                                                    dialog.hide();
                                                } catch (ActivityNotFoundException ex) {

                                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                                    String uriString = "market://details?id=org.videolan.vlc";
                                                    intent.setData(Uri.parse(uriString));
                                                    context.startActivity(intent);
                                                }


                                            });


                                            mxPlayer.setOnClickListener(v12 -> {

                                                Intent shareVideo = new Intent(Intent.ACTION_VIEW);
                                                shareVideo.setDataAndType(Uri.parse(download.getLink()), "video/*");
                                                shareVideo.setPackage("com.mxtech.videoplayer.ad");
                                                shareVideo.putExtra("title", download.getTitle());
                                                shareVideo.putExtra("poster", download.getBackdropPath());
                                                Bundle headers = new Bundle();
                                                headers.putString("User-Agent", settingsManager.getSettings().getAppName());
                                                shareVideo.putExtra("android.media.intent.extra.HTTP_HEADERS", headers);
                                                shareVideo.putExtra("headers", headers);
                                                shareVideo.putExtra("secure_uri", true);
                                                try {
                                                    context.startActivity(shareVideo);
                                                    dialog.hide();
                                                } catch (ActivityNotFoundException ex) {

                                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                                    String uriString = "market://details?id=com.mxtech.videoplayer.ad";
                                                    intent.setData(Uri.parse(uriString));
                                                    context.startActivity(intent);
                                                }


                                            });


                                            easyplexPlayer.setOnClickListener(v12 -> {
                                                onLoadDownloadAnime(download, mediaLink);
                                                dialog.hide();


                                            });

                                            dialog.show();
                                            dialog.getWindow().setAttributes(lp);

                                            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                                    dialog.dismiss());


                                            dialog.show();
                                            dialog.getWindow().setAttributes(lp);


                                        } else {

                                            onLoadDownloadAnime(download, mediaLink);

                                        }


                                    }
                                }
                            }

                            private void onLoadDownloadMovie(Download download, String mediaLink) {

                                Intent intent = new Intent(context, EasyPlexMainPlayer.class);
                                intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY, MediaModel.media(download.getId(), null
                                        , null, download.getType(), download.getTitle(),
                                        mediaLink, download.getBackdropPath(),
                                        null, null
                                        , null, null,
                                        null, null,
                                        null,
                                        null,
                                        null, download.getPremuim(),
                                        download.getHls(),
                                        null, download.getImdbExternalId(),
                                        null, download.getHasrecap(),
                                        download.getSkiprecapStartIn(), download.getMediaGenre(), null,
                                        download.getVoteAverage(),null,null,0));
                                intent.putExtra("from_download","yes");

                                context.startActivity(intent);
                                Animatoo.animateFade(context);

                            }

                            private void onLoadDownloadAnime(Download download, String mediaLink) {

                                String name = download.getTitle();
                                String tvseasonid = download.getSeasonsId();
                                Integer currentep = Integer.parseInt(download.getEpisodeNmber());
                                String currentepname = download.getEpisodeName();
                                String currenteptmdbnumber = download.getEpisodeTmdb();
                                String currentseasons = download.getCurrentSeasons();
                                String currentseasonsNumber = download.getSeasonsNumber();
                                String currentepimdb = download.getEpisodeTmdb();
                                String artwork = download.getBackdropPath();
                                String type = "anime";
                                int hls = download.getStreamhls();
                                int hasRecap = download.getHasrecap();
                                int recapstartin = download.getSkiprecapStartIn();

                                Intent intent = new Intent(context, EasyPlexMainPlayer.class);

                                intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY,
                                        MediaModel.media(download.getSerieId(), null,
                                                null, type, name, mediaLink, artwork, null, currentep
                                                , currentseasons,
                                                currentepimdb,
                                                tvseasonid, currentepname,
                                                currentseasonsNumber, download.getPosition(),
                                                currenteptmdbnumber,
                                                download.getPremuim(), hls,
                                                null,
                                                download.getImdbExternalId(),
                                                download.getPosterPath(),
                                                hasRecap, recapstartin,
                                                download.getMediaGenre(),
                                                null, download.getVoteAverage(),null,null,0));
                                intent.putExtra(ARG_MOVIE, download);
                                intent.putExtra(ARG_MOVIE_HISTORY, download);
                                intent.putExtra("from_download","yes");
                                context.startActivity(intent);
                            }

                            private void onLoadDownloadSerie(Download download, String mediaLink) {

                                String name = download.getTitle();
                                String tvseasonid = download.getSeasonsId();
                                Integer currentep = Integer.parseInt(download.getEpisodeNmber());
                                String currentepname = download.getEpisodeName();
                                String currenteptmdbnumber = download.getEpisodeTmdb();
                                String currentseasons = download.getCurrentSeasons();
                                String currentseasonsNumber = download.getSeasonsNumber();
                                String currentepimdb = download.getEpisodeTmdb();
                                String artwork = download.getBackdropPath();
                                String type = "1";
                                int hls = download.getStreamhls();
                                int hasRecap = download.getHasrecap();
                                int recapstartin = download.getSkiprecapStartIn();

                                Intent intent = new Intent(context, EasyPlexMainPlayer.class);

                                intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY,
                                        MediaModel.media(download.getSerieId(), null,
                                                null, type, name, mediaLink, artwork, null, currentep
                                                , currentseasons,
                                                currentepimdb,
                                                tvseasonid, currentepname,
                                                currentseasonsNumber, download.getPosition(),
                                                currenteptmdbnumber,
                                                download.getPremuim(), hls,
                                                null,
                                                download.getImdbExternalId(), download.getPosterPath()
                                                , hasRecap, recapstartin, download.getMediaGenre(), download.getSerieName(),
                                                download.getVoteAverage(),null,null,0));
                                intent.putExtra(ARG_MOVIE, download);
                                intent.putExtra(ARG_MOVIE_HISTORY, download);
                                intent.putExtra("from_download","yes");
                                context.startActivity(intent);
                            }
                        });

            });

            Tools.onLoadMediaCoverEpisode(context,icon,item.info.mediaBackdrop);
            assert item.info.mediatype != null;
            if (item.info.mediatype.equals("0")){
                downloadType.setText((context.getResources().getString(R.string.movies)));
            }else if (item.info.mediatype.equals("1") || item.info.mediatype.equals("2")){
                downloadType.setText(context.getResources().getString(R.string.episode));
            }

            String hostname = "";
            status.setText(context.getString(R.string.download_finished_template,
                    hostname,
                    (item.info.totalBytes == -1 ? context.getString(R.string.not_available) :
                            Formatter.formatFileSize(context, item.info.totalBytes))));
        }

        private void onLoadChromcastStream(CastSession castSession, Context context, Download download, String mediaLink) {

            MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
            movieMetadata.putString(MediaMetadata.KEY_TITLE, download.getTitle());
            movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, download.getTitle());

            movieMetadata.addImage(new WebImage(Uri.parse(download.getPosterPath())));
            List<MediaTrack> tracks = new ArrayList<>();


            MediaInfo mediaInfo = new MediaInfo.Builder(mediaLink)
                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                    .setMetadata(movieMetadata)
                    .setMediaTracks(tracks)
                    .build();

            final RemoteMediaClient remoteMediaClient = castSession.getRemoteMediaClient();
            if (remoteMediaClient == null) {
                return;
            }
            final QueueDataProvider provider = QueueDataProvider.getInstance(context);
            PopupMenu popup = new PopupMenu(context, cardViewItem);
            popup.getMenuInflater().inflate(
                    provider.isQueueDetached() || provider.getCount() == 0
                            ? R.menu.detached_popup_add_to_queue
                            : R.menu.popup_add_to_queue, popup.getMenu());
            PopupMenu.OnMenuItemClickListener clickListener = menuItem -> {
                QueueDataProvider provider1 = QueueDataProvider.getInstance(context);
                MediaQueueItem queueItem = new MediaQueueItem.Builder(mediaInfo).setAutoplay(
                        true).build();
                MediaQueueItem[] newItemArray = new MediaQueueItem[]{queueItem};
                String toastMessage = null;
                if (provider1.isQueueDetached() && provider1.getCount() > 0) {
                    if ((menuItem.getItemId() == R.id.action_play_now)
                            || (menuItem.getItemId() == R.id.action_add_to_queue)) {
                        MediaQueueItem[] items = com.easyplexdemoapp.ui.player.cast.utils.Utils
                                .rebuildQueueAndAppend(provider1.getItems(), queueItem);
                        remoteMediaClient.queueLoad(items, provider1.getCount(),
                                REPEAT_MODE_REPEAT_OFF, null);
                    } else {
                        return false;
                    }
                } else {
                    if (provider1.getCount() == 0) {
                        remoteMediaClient.queueLoad(newItemArray, 0,
                                REPEAT_MODE_REPEAT_OFF, null);
                    } else {
                        int currentId = provider1.getCurrentItemId();
                        if (menuItem.getItemId() == R.id.action_play_now) {
                            remoteMediaClient.queueInsertAndPlayItem(queueItem, currentId, null);
                        } else if (menuItem.getItemId() == R.id.action_play_next) {
                            int currentPosition = provider1.getPositionByItemId(currentId);
                            if (currentPosition == provider1.getCount() - 1) {
                                //we are adding to the end of queue
                                remoteMediaClient.queueAppendItem(queueItem, null);
                            } else {
                                int nextItemId = provider1.getItem(currentPosition + 1).getItemId();
                                remoteMediaClient.queueInsertItems(newItemArray, nextItemId, null);
                            }
                            toastMessage = context.getString(
                                    R.string.queue_item_added_to_play_next);
                        } else if (menuItem.getItemId() == R.id.action_add_to_queue) {
                            remoteMediaClient.queueAppendItem(queueItem, null);
                            toastMessage = context.getString(R.string.queue_item_added_to_queue);
                        } else {
                            return false;
                        }
                    }
                }
                if (menuItem.getItemId() == R.id.action_play_now) {
                    Intent intent = new Intent(context, ExpandedControlsActivity.class);
                    context.startActivity(intent);
                }
                if (!TextUtils.isEmpty(toastMessage)) {
                    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
                }
                return true;
            };
            popup.setOnMenuItemClickListener(clickListener);
            popup.show();

        }
    }

    public static class ErrorViewHolder extends ViewHolder
    {
        private final ImageButton resumeButton;
        private final ImageButton menu;
        private final TextView error;

        ErrorViewHolder(View itemView)
        {
            super(itemView);

            resumeButton = itemView.findViewById(R.id.resume);
            menu = itemView.findViewById(R.id.menu);
            error = itemView.findViewById(R.id.error);
        }

        void bind(DownloadItem item, ErrorClickListener listener)
        {
            super.bind(item, listener);

            Context context = itemView.getContext();

            resumeButton.setOnClickListener((v) -> {
                if (listener != null)
                    listener.onItemResumeClicked(item);
            });

            menu.setOnClickListener((v) -> {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.inflate(R.menu.download_item_popup);
                popup.setOnMenuItemClickListener((MenuItem menuItem) -> {
                    if (listener != null)
                        listener.onItemMenuClicked(menuItem.getItemId(), item);
                    return true;
                });
                popup.show();
            });

            String hostname = Utils.getHostFromUrl(item.info.url);
            status.setText(context.getString(R.string.download_finished_template,
                    (hostname == null ? "" : hostname),
                    (item.info.totalBytes == -1 ? context.getString(R.string.not_available) :
                            Formatter.formatFileSize(context, item.info.totalBytes))));

            if (StatusCode.isStatusError(item.info.statusCode) && item.info.statusMsg != null) {
                error.setVisibility(View.VISIBLE);
                error.setText(context.getString(R.string.error_template, item.info.statusMsg));
            } else {
                error.setVisibility(View.GONE);
            }
        }
    }

    public interface ClickListener
    {
        void onItemClicked(@NonNull DownloadItem item);
    }

    public interface QueueClickListener extends ClickListener
    {
        void onItemPauseClicked(@NonNull DownloadItem item);

        void onItemCancelClicked(@NonNull DownloadItem item);
    }

    public interface FinishClickListener extends ClickListener
    {
        void onItemMenuClicked(int menuId, @NonNull DownloadItem item);


    }

    public interface ErrorClickListener extends ClickListener
    {
        void onItemResumeClicked(@NonNull DownloadItem item);

        void onItemMenuClicked(int menuId, @NonNull DownloadItem item);
    }

    public static final DiffUtil.ItemCallback<DownloadItem> diffCallback = new DiffUtil.ItemCallback<DownloadItem>()
    {
        @Override
        public boolean areContentsTheSame(@NonNull DownloadItem oldItem,
                                          @NonNull DownloadItem newItem)
        {
            return oldItem.equalsContent(newItem);
        }

        @Override
        public boolean areItemsTheSame(@NonNull DownloadItem oldItem,
                                       @NonNull DownloadItem newItem)
        {
            return oldItem.equals(newItem);
        }
    };

    /*
     * Selection support stuff
     */

    public static final class KeyProvider extends ItemKeyProvider<DownloadItem>
    {
        private final Selectable<DownloadItem> selectable;

        KeyProvider(Selectable<DownloadItem> selectable)
        {
            super(SCOPE_MAPPED);

            this.selectable = selectable;
        }

        @Nullable
        @Override
        public DownloadItem getKey(int position)
        {
            return selectable.getItemKey(position);
        }

        @Override
        public int getPosition(@NonNull DownloadItem key)
        {
            return selectable.getItemPosition(key);

        }
    }

    public static final class ItemDetails extends ItemDetailsLookup.ItemDetails<DownloadItem>
    {
        private final DownloadItem selectionKey;
        private final int adapterPosition;

        ItemDetails(DownloadItem selectionKey, int adapterPosition)
        {
            this.selectionKey = selectionKey;
            this.adapterPosition = adapterPosition;
        }

        @Nullable
        @Override
        public DownloadItem getSelectionKey()
        {
            return selectionKey;
        }

        @Override
        public int getPosition()
        {
            return adapterPosition;
        }
    }

    public static class ItemLookup extends ItemDetailsLookup<DownloadItem>
    {
        private final RecyclerView recyclerView;

        ItemLookup(RecyclerView recyclerView)
        {
            this.recyclerView = recyclerView;
        }

        @Nullable
        @Override
        public ItemDetails<DownloadItem> getItemDetails(@NonNull MotionEvent e)
        {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (view != null) {
                RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
                if (viewHolder instanceof DownloadListAdapter.ViewHolder)
                    return ((ViewHolder)viewHolder).getItemDetails();
            }

            return null;
        }
    }



    public interface OnItemClickListener {
        void onItemClick(View view, DownloadItem downloadItem);
    }
}

package com.easyplexdemoapp.ui.player.views;

import static android.content.Context.MODE_PRIVATE;
import static com.easyplexdemoapp.util.Constants.PREF_FILE;
import static com.easyplexdemoapp.util.Constants.SUBS_BACKGROUND;
import static com.easyplexdemoapp.util.Constants.SUBS_SIZE;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.media3.exoplayer.source.BehindLiveWindowException;

import com.easyplexdemoapp.EasyPlexApp;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.ui.player.bindings.PlayerController;
import com.easyplexdemoapp.ui.player.interfaces.PlaybackActionCallback;
import com.easyplexdemoapp.ui.player.interfaces.TubiPlaybackControlInterface;
import com.easyplexdemoapp.ui.player.utilities.ExoPlayerLogger;
import com.google.android.exoplayer2.DeviceInfo;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.Tracks;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.CueGroup;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.CaptionStyleCompat;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.ui.SubtitleView;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoSize;

import java.util.List;

import timber.log.Timber;

/**
 * Created by stoyan tubi_tv_quality_on 3/22/17.
 */
@TargetApi(16)
public class EasyPlexPlayerView extends StyledPlayerView {


    private static final String TAG = EasyPlexPlayerView.class.getSimpleName();
    private static final int SURFACE_TYPE_NONE = 0;
    private static final int SURFACE_TYPE_SURFACE_VIEW = 1;
    private static final int SURFACE_TYPE_TEXTURE_VIEW = 2;
    private final AspectRatioFrameLayout contentFrame;
    private View shutterView;
    private View surfaceView;
    private SubtitleView subtitleView;
    private View mUserInteractionView;
    private final ComponentListener componentListener;
    private ExoPlayer player;
    private PlayerController userController;
    private PlaybackActionCallback mPlaybackActionCallback;


    public EasyPlexPlayerView(Context context) {
        this(context, null);
    }

    public EasyPlexPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EasyPlexPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (isInEditMode()) {
            contentFrame = null;
            shutterView = null;
            surfaceView = null;
            subtitleView = null;
            mUserInteractionView = null;
            componentListener = null;
            ImageView logo = new ImageView(context, attrs);
            if (Util.SDK_INT >= 23) {
                configureEditModeLogoV23(getResources(), logo);
            } else {
                configureEditModeLogo(getResources(), logo,context);
            }
            addView(logo);
            return;
        }

        int playerLayoutId = R.layout.tubi_player_view;
        int surfaceType = SURFACE_TYPE_SURFACE_VIEW;
        int resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT;
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                    com.google.android.exoplayer2.ui.R.styleable.PlayerView, 0, 0);
            try {
                playerLayoutId = a.getResourceId(com.google.android.exoplayer2.ui.R.styleable.PlayerView_player_layout_id,
                        playerLayoutId);
                surfaceType = a.getInt(com.google.android.exoplayer2.ui.R.styleable.PlayerView_surface_type, surfaceType);
                resizeMode = a.getInt(com.google.android.exoplayer2.ui.R.styleable.PlayerView_resize_mode, resizeMode);
            } finally {
                a.recycle();
            }
        }

        LayoutInflater.from(context).inflate(playerLayoutId, this);
        componentListener = new ComponentListener();
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);

        // Content frame.
        contentFrame = findViewById(com.google.android.exoplayer2.ui.R.id.exo_content_frame);
        if (contentFrame != null) {
            setResizeModeRaw(contentFrame, resizeMode);
        }

        // Shutter view.
        shutterView = findViewById(com.google.android.exoplayer2.ui.R.id.exo_shutter);

        // Create a surface view and insert it into the content frame, if there is one.
        if (contentFrame != null && surfaceType != SURFACE_TYPE_NONE) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            surfaceView = surfaceType == SURFACE_TYPE_TEXTURE_VIEW ? new TextureView(context)
                    : new SurfaceView(context);
            surfaceView.setLayoutParams(params);
            contentFrame.addView(surfaceView, 0);
        } else {
            surfaceView = null;
        }

        // Subtitle view.


        SharedPreferences preferences = getContext().getSharedPreferences(PREF_FILE, MODE_PRIVATE);

        subtitleView = findViewById(com.google.android.exoplayer2.ui.R.id.exo_subtitles);

        subtitleView.setBottom(50);


        if (subtitleView != null) {

            // Load the custom font
            Typeface customTypeface = Typeface.createFromAsset(context.getAssets(), "font/dinbold.ttf");


            String transparent = preferences.getString(SUBS_BACKGROUND, "Transparent");
            if (transparent.equals("Transparent")) {
                subtitleView.setStyle(createCaptionStyle(R.color.transparent, customTypeface));
            } else if (transparent.equals("Black")) {
                subtitleView.setStyle(createCaptionStyle(R.color.black_subtitles_background, customTypeface));
            } else if (transparent.equals("Grey")) {
                subtitleView.setStyle(createCaptionStyle(R.color.grey_11, customTypeface));
            } else if (transparent.equals("Red")) {
                subtitleView.setStyle(createCaptionStyle(R.color.red_subtitles_background, customTypeface));
            } else if (transparent.equals("Yellow")) {
                subtitleView.setStyle(createCaptionStyle(R.color.yellow_subtitles_background, customTypeface));
            } else if (transparent.equals("Green")) {
                subtitleView.setStyle(createCaptionStyle(R.color.green_subtitles_background, customTypeface));
            }


            subtitleView.setFixedTextSize(TypedValue.COMPLEX_UNIT_DIP,
                    Float.parseFloat(preferences.getString(SUBS_SIZE,"16f")));

            subtitleView.setApplyEmbeddedStyles(false);

            subtitleView.setVisibility(View.INVISIBLE);
        }


        userController = new PlayerController();

        this.mPlaybackActionCallback = mPlaybackActionCallback;
    }


    // Helper method to create CaptionStyleCompat
    private CaptionStyleCompat createCaptionStyle(int backgroundColor, Typeface typeface) {
        return new CaptionStyleCompat(
                Color.WHITE,
                ContextCompat.getColor(getContext(), backgroundColor),
                Color.TRANSPARENT,
                CaptionStyleCompat.EDGE_TYPE_NONE,
                Color.WHITE,
                typeface
        );
    }

    public void addUserInteractionView(@Nullable View controlView) {
        if (controlView == null) {
            ExoPlayerLogger.e(TAG, "addUserInteractionView()----> adding empty view");
            return;
        }
        // Playback control view.
        View controllerPlaceholder = findViewById(com.google.android.exoplayer2.ui.R.id.exo_controller_placeholder);
        if (controllerPlaceholder != null) {
            // Note: rewindMs and fastForwardMs are passed via attrs, so we don't need to make explicit
            // calls to set them.
            mUserInteractionView = controlView;

            ViewGroup parent = ((ViewGroup) controllerPlaceholder.getParent());
            int controllerIndex = parent.indexOfChild(controllerPlaceholder);
            parent.removeView(controllerPlaceholder);
            parent.addView(mUserInteractionView, controllerIndex);
        } else {
            this.mUserInteractionView = null;
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @TargetApi(23)
    private static void configureEditModeLogoV23(Resources resources, ImageView logo) {
        logo.setImageDrawable(resources.getDrawable(com.google.android.exoplayer2.ui.R.drawable.exo_edit_mode_logo, null));
        logo.setBackgroundColor(resources.getColor(com.google.android.exoplayer2.ui.R.color.exo_edit_mode_background_color, null));
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private static void configureEditModeLogo(Resources resources, ImageView logo, Context context) {
        logo.setImageDrawable(resources.getDrawable(com.google.android.exoplayer2.ui.R.drawable.exo_edit_mode_logo, null));
        logo.setBackgroundColor(ContextCompat.getColor(context, com.google.android.exoplayer2.ui.R.color.exo_edit_mode_background_color));
    }

    @SuppressWarnings("ResourceType")
    private static void setResizeModeRaw(AspectRatioFrameLayout aspectRatioFrame, int resizeMode) {
        aspectRatioFrame.setResizeMode(resizeMode);
    }


    public void setAspectRatio(float widthHeightRatio) {
        contentFrame.setAspectRatio(widthHeightRatio);
    }

    public View getControlView() {
        return mUserInteractionView;
    }

    public TubiPlaybackControlInterface getPlayerController() {
        return userController;
    }

    /**
     * Returns the player currently set on this view, or null if no player is set.
     */

    @Override
    public ExoPlayer getPlayer() {
        return player;
    }


    public void setPlayer(ExoPlayer player, @NonNull PlaybackActionCallback playbackActionCallback) {
        if (this.player == player) {
            return;
        }

        mPlaybackActionCallback = playbackActionCallback;
        if (this.player != null) {
            this.player.removeListener(componentListener);
            if (surfaceView instanceof TextureView) {
                this.player.clearVideoTextureView((TextureView) surfaceView);
            } else if (surfaceView instanceof SurfaceView) {
                this.player.clearVideoSurfaceView((SurfaceView) surfaceView);
            }
        }
        this.player = player;

        if (userController != null) {
            userController.setPlayer(player, playbackActionCallback, this);
        }


        shutterView.setVisibility(VISIBLE);

        if (player != null) {
            if (surfaceView instanceof TextureView) {
                player.setVideoTextureView((TextureView) surfaceView);
            } else if (surfaceView instanceof SurfaceView) {
                player.setVideoSurfaceView((SurfaceView) surfaceView);
            }
            player.addListener(componentListener);
        }
    }

    /**
     * Sets the resize mode.
     *
     * @param resizeMode The resize mode.
     */

    @Override
    public void setResizeMode(@AspectRatioFrameLayout.ResizeMode int resizeMode) {
        Assertions.checkState(contentFrame != null);
        contentFrame.setResizeMode(resizeMode);
    }

    /**
     * Gets the {@link SubtitleView}.
     *
     * @return The {@link SubtitleView}, or {@code null} if the layout has been customized and the
     * subtitle view is not present.
     */

    @NonNull
    @Override
    public SubtitleView getSubtitleView() {
        return subtitleView;
    }

    public void setMediaModel(@NonNull MediaModel mediaModel) {
        if (userController != null) {
            userController.setMediaModel(mediaModel, EasyPlexApp.getContext());
        }
    }

    public void setAvailableAdLeft(int count) {
        if (userController != null) {
            userController.setAvailableAdLeft(count);
        }
    }

    private final class ComponentListener implements
            TextOutput, Player.Listener {

        // TextRenderer.Output implementation


        @Override
        public void onCues(@NonNull List<Cue> cues) {
            TextOutput.super.onCues(cues);

            if (subtitleView != null) {
                subtitleView.setCues(cues);
            }
        }

        @Override
        public void onCues(@NonNull CueGroup cueGroup) {

            if (subtitleView != null) {
                subtitleView.setCues(cueGroup.cues);
            }
        }

        @Override
        public void onMetadata(@NonNull Metadata metadata) {
            Player.Listener.super.onMetadata(metadata);
        }

        // Exoplayer Listener implementation


        @Override
        public void onVideoSizeChanged(@NonNull VideoSize videoSize) {

            int width = videoSize.width;
            int height = videoSize.height;

            if (contentFrame != null) {
                float aspectRatio = height == 0 ? 1 : (width * videoSize.pixelWidthHeightRatio) / height;
                contentFrame.setAspectRatio(aspectRatio);
            }

        }

        @Override
        public void onSurfaceSizeChanged(int width, int height) {
            Player.Listener.super.onSurfaceSizeChanged(width, height);
        }

        @Override
        public void onRenderedFirstFrame() {
            if (shutterView != null) {
                shutterView.setVisibility(INVISIBLE);
            }
        }


        @Override
        public void onTracksChanged(@NonNull Tracks tracks) {
            Player.Listener.super.onTracksChanged(tracks);
        }

        @Override
        public void onMediaMetadataChanged(@NonNull MediaMetadata mediaMetadata) {
            Player.Listener.super.onMediaMetadataChanged(mediaMetadata);
        }

        @Override
        public void onPlaylistMetadataChanged(@NonNull MediaMetadata mediaMetadata) {
            Player.Listener.super.onPlaylistMetadataChanged(mediaMetadata);
        }


        // ExoPlayer.EventListener implementation

        @Override
        public void onIsLoadingChanged(boolean isLoading) {
            Player.Listener.super.onIsLoadingChanged(isLoading);

            // Do Nothing
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            Player.Listener.super.onLoadingChanged(isLoading);
        }

        @Override
        public void onAvailableCommandsChanged(@NonNull Player.Commands availableCommands) {
            Player.Listener.super.onAvailableCommandsChanged(availableCommands);
        }

        @Override
        public void onTrackSelectionParametersChanged(TrackSelectionParameters parameters) {
            Player.Listener.super.onTrackSelectionParametersChanged(parameters);
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            Player.Listener.super.onPlayerStateChanged(playWhenReady, playbackState);
        }


        @Override
        public void onPlaybackStateChanged(int playbackState) {
            Player.Listener.super.onPlaybackStateChanged(playbackState);
            // Do Nothing

        }

        @Override
        public void onPlayerError(@NonNull PlaybackException error) {
            Player.Listener.super.onPlayerError(error);
            if (error.errorCode == 1002){

                mPlaybackActionCallback.onRetry();

            }else {

                mPlaybackActionCallback.onDisplayErrorDialog();


            }
        }

        @Override
        public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
            Player.Listener.super.onPlayWhenReadyChanged(playWhenReady, reason);
        }

        @Override
        public void onPlaybackSuppressionReasonChanged(int playbackSuppressionReason) {
            Player.Listener.super.onPlaybackSuppressionReasonChanged(playbackSuppressionReason);
        }

        @Override
        public void onIsPlayingChanged(boolean isPlaying) {
            Player.Listener.super.onIsPlayingChanged(isPlaying);
        }


        @Override
        public void onRepeatModeChanged(final int repeatMode) {

            // Do Nothing

        }

        @Override
        public void onShuffleModeEnabledChanged(final boolean shuffleModeEnabled) {


            // Do Nothing

        }



        @Override
        public void onPlayerErrorChanged(@Nullable PlaybackException error) {
            Player.Listener.super.onPlayerErrorChanged(error);
        }


        @Override
        public void onPositionDiscontinuity(@NonNull Player.PositionInfo oldPosition, @NonNull Player.PositionInfo newPosition, int reason) {
            Player.Listener.super.onPositionDiscontinuity(oldPosition, newPosition, reason);
            //
        }


        @Override
        public void onPlaybackParametersChanged(@NonNull PlaybackParameters playbackParameters) {
            // Do nothing.
        }


        @Override
        public void onSeekBackIncrementChanged(long seekBackIncrementMs) {
            Player.Listener.super.onSeekBackIncrementChanged(seekBackIncrementMs);
        }


        @Override
        public void onSeekForwardIncrementChanged(long seekForwardIncrementMs) {
            Player.Listener.super.onSeekForwardIncrementChanged(seekForwardIncrementMs);
        }

        @Override
        public void onMaxSeekToPreviousPositionChanged(long maxSeekToPreviousPositionMs) {
            Player.Listener.super.onMaxSeekToPreviousPositionChanged(maxSeekToPreviousPositionMs);
        }


        @Override
        public void onAudioSessionIdChanged(int audioSessionId) {
            Player.Listener.super.onAudioSessionIdChanged(audioSessionId);
        }

        @Override
        public void onAudioAttributesChanged(AudioAttributes audioAttributes) {
            Player.Listener.super.onAudioAttributesChanged(audioAttributes);
        }

        @Override
        public void onVolumeChanged(float volume) {
            Player.Listener.super.onVolumeChanged(volume);
        }

        @Override
        public void onSkipSilenceEnabledChanged(boolean skipSilenceEnabled) {
            Player.Listener.super.onSkipSilenceEnabledChanged(skipSilenceEnabled);
        }

        @Override
        public void onDeviceInfoChanged(DeviceInfo deviceInfo) {
            Player.Listener.super.onDeviceInfoChanged(deviceInfo);
        }

        @Override
        public void onDeviceVolumeChanged(int volume, boolean muted) {
            Player.Listener.super.onDeviceVolumeChanged(volume, muted);
        }

        @Override
        public void onEvents(@NonNull Player player, @NonNull Player.Events events) {
            Player.Listener.super.onEvents(player, events);
        }

        @Override
        public void onTimelineChanged(@NonNull final Timeline timeline, final int reason) {
            // Do nothing.
        }

        @Override
        public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
            Player.Listener.super.onMediaItemTransition(mediaItem, reason);
        }

    }


    public void cleanup() {
        if (player != null) {
            player.removeListener(componentListener);
            if (surfaceView instanceof TextureView) {
                player.clearVideoTextureView((TextureView) surfaceView);
            } else if (surfaceView instanceof SurfaceView) {
                player.clearVideoSurfaceView((SurfaceView) surfaceView);
            }
        }
        player = null;
        mPlaybackActionCallback = null;

        if (userController != null) {
            userController.onCleanUp();
            userController = null;
        }


        mUserInteractionView = null;
        shutterView = null;
        surfaceView = null;
        subtitleView = null;






    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        cleanup();
    }
}

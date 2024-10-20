package com.easyplexdemoapp.ui.player.interfaces;

import androidx.annotation.Nullable;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.google.android.exoplayer2.Tracks;


/**
 * Created by stoyan on 6/23/17.
 * This is an information callback interface, the relative callback will be triggered when certain user/player action has been made,
 * to update program. all the callback will be called after the matching action has been performed.
 */

public interface PlaybackActionCallback {

    void onLaunchResume();

    void onTracksChanged(Tracks tracksInfo);

    void onProgress(@Nullable MediaModel mediaModel, long milliseconds, long durationMillis);

    void onSeek(@Nullable MediaModel mediaModel, long oldPositionMillis, long newPositionMillis);

    void onSeekBirghtness();


    void onPlayToggle(@Nullable MediaModel mediaModel, boolean playing);

    void onSubtitles(@Nullable MediaModel mediaModel, boolean enabled);

    void onSubtitlesSelection();

    void onMediaEnded();

    void onLoadEpisodes();

    void onLoadNextEpisode();

    void onLoadloadSeriesList();

    void onLoadloadAnimesList();

    void onLoadPlaybackSetting();

    void onLoadSteaming();

    void onLoadMoviesList();

    void onLoadFromBeginning();

    void onLoadFromVlc();

    void  onLoadSide();

    void onMediaHasSkipRecap();

    void onRetry();



    void onAutoPlaySwitch(boolean enabled);

    void onOpenSubsLoad();

    void onTracksMedia();

    void onLoadQualities();

    void StartGenre(String genre);

    void getType(String type);

    void onCuePointReceived(long[] cuePoints);

    boolean isActive();

    boolean isPremuim();

    void isCurrentAd(boolean enabled);

    void onDisplayErrorDialog();

    void onLoadServerList();
}

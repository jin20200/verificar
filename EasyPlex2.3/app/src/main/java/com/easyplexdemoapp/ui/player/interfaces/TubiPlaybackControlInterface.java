package com.easyplexdemoapp.ui.player.interfaces;

import android.net.Uri;

import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;


public interface TubiPlaybackControlInterface {



    void onLaunchResume();


    int getCurrentHlsFormat();


    boolean isMediaPlayerError();

    void isMediaHasRecap(boolean enabled);

    void setMediaRestart(boolean enabled);

    void mediaHasSkipRecap();

    // on Ads Play (Hide some part of the player)
    void onAdsPlay(boolean playing,boolean isSkippable);


    // Return if media is an Ad or Movie
    boolean isCurrentVideoAd();

    void isCurrentSubstitleAuto(boolean enabled);


    void onTracksMedia();

    void clickPlaybackSetting();

    void onLoadSide();

    void onLoadFromBeginning();


    void isCue (boolean enabled);


    boolean getIsMediaSubstitleGet();

    // Player Control
    void triggerSubtitlesToggle(boolean enabled);

    void triggerAutoPlay(boolean enabled);


    boolean isCue();


    void onCheckedChanged(boolean enabled);

    void seekBy(long millisecond);

    void seekTo(long millisecond);

    void closePlayer();

    void isSubtitleEnabled(boolean enabled);

    void playerReady (boolean enabled);

    void settingReady (boolean enabled);

    void subtitleCurrentLang(String lang);

    boolean hasSubsActive();

    void loadPreview(long millisecond, long max);

    void triggerPlayOrPause(boolean setPlay);

    void triggerPlayerLocked();

    void triggerPlayerLocked2();

    void scale();

    void clickOnSubs();


    // Series

    void onLoadEpisodes();

    void onLoadStreaming();

    void nextEpisode();

    Integer isMediaPremuim();



    // Movies
    void loadMoviesList();

    // Display Media Info
    String getCurrentVideoName();

    int getCurrentEpisodePosition();

    String getVideoID();

    String getCurrentSeasonId();

    String getMediaSubstitleName();

    Uri getVideoUrl();

    Uri getMediaSubstitleUrl();

    Uri getMediaPoster();

    void getCurrentSpeed(String speed);

    String getMediaType();

    String getCurrentEpTmdbNumber();

    String getEpID();

    String getVideoCurrentQuality();

    String nextSeaonsID();

    String getCurrentSeason();

    String getCurrentSeasonNumber();

    String getEpName();

    String getSeaonNumber();

    String getCurrentExternalId();

    void setVideoAspectRatio(float widthHeightRatio);

    float getInitVideoAspectRatio();

    void setResizeMode(@AspectRatioFrameLayout.ResizeMode int resizeMode);

    void setPremuim(boolean premuim);



    void isPlayerError(boolean isError);


    String getMediaCoverHistory();

    String getMediaGenre();

    String getSerieName();

    float getVoteAverage();

    int getCurrentHasRecap();

    int getCurrentStartRecapIn();


    int getDrm();

    String getDrmuuid();

    String getDrmlicenceuri();

    void setScalePresenter();
}

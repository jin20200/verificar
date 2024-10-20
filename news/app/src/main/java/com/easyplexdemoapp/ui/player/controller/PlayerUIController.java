package com.easyplexdemoapp.ui.player.controller;

import android.view.View;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.easyplexdemoapp.ui.player.utilities.PlayerDeviceUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;

/**
 * Created by allensun on 8/3/17.
 * on Tubitv.com, allengotstuff@gmail.com
 */


public class PlayerUIController {

    public boolean isPlayingAds = false;

    private ExoPlayer contentPlayer;

    private ExoPlayer adPlayer;

    private WebView vpaidWebView;

    private View exoPlayerView;


    private int adResumeWindow = C.INDEX_UNSET;

    private long adResumePosition = C.TIME_UNSET;


    private long movieResumePosition = C.TIME_UNSET;

    private boolean hasHistory = false;

    private long historyPosition = C.TIME_UNSET;

    public PlayerUIController() {
    }

    public PlayerUIController(@Nullable ExoPlayer contentPlayer, @Nullable ExoPlayer adPlayer,
                              @Nullable WebView vpaidWebView, @Nullable View exoPlayerView) {
        this.contentPlayer = contentPlayer;
        this.adPlayer = adPlayer;
        this.vpaidWebView = vpaidWebView;
        this.exoPlayerView = exoPlayerView;
    }

    public ExoPlayer getContentPlayer() {
        return contentPlayer;
    }

    public void setContentPlayer(ExoPlayer contentPlayer) {
        this.contentPlayer = contentPlayer;
    }

    public ExoPlayer getAdPlayer() {
        // We'll reuse content player to play ads for single player instance case
        if (PlayerDeviceUtils.useSinglePlayer()) {
            return contentPlayer;
        }
        return adPlayer;
    }

    public void setAdPlayer(ExoPlayer adPlayer) {
        this.adPlayer = adPlayer;
    }

    public WebView getVpaidWebView() {
        return vpaidWebView;
    }

    public void setVpaidWebView(WebView vpaidWebView) {
        this.vpaidWebView = vpaidWebView;
    }

    public View getExoPlayerView() {
        return exoPlayerView;
    }

    public void setExoPlayerView(View exoPlayerView) {
        this.exoPlayerView = exoPlayerView;
    }

    public boolean hasHistory() {
        return hasHistory;
    }

    public long getHistoryPosition() {
        return historyPosition;
    }

    public void clearHistoryRecord() {
        hasHistory = false;
        historyPosition = C.TIME_UNSET;
    }

    public void setAdResumeInfo(int window, long position) {
        adResumeWindow = window;
        adResumePosition = position;
    }

    public void clearAdResumeInfo() {
        setAdResumeInfo(C.INDEX_UNSET, C.TIME_UNSET);
    }

    public void setMovieResumeInfo(int window, long position) {
        movieResumePosition = position;
    }

    public void clearMovieResumeInfo() {
        setMovieResumeInfo(C.INDEX_UNSET, C.TIME_UNSET);
    }

    public int getAdResumeWindow() {
        return adResumeWindow;
    }

    public long getAdResumePosition() {
        return adResumePosition;
    }

    public long getMovieResumePosition() {
        return movieResumePosition;
    }

    public static class Builder {

        private ExoPlayer contentPlayer = null;

        private ExoPlayer adPlayer = null;

        public Builder() {
            //
        }

        public Builder setContentPlayer(ExoPlayer contentPlayer) {
            this.contentPlayer = contentPlayer;
            return this;
        }

        public Builder setAdPlayer(ExoPlayer adPlayer) {
            this.adPlayer = adPlayer;
            return this;
        }

        public PlayerUIController build() {

            return new PlayerUIController(contentPlayer, adPlayer, null, null);
        }
    }


    public void destroy() {
        exoPlayerView = null;
        contentPlayer = null;
        adPlayer = null;
        vpaidWebView = null;
        setContentPlayer(null);
        setAdPlayer(null);
    }
}
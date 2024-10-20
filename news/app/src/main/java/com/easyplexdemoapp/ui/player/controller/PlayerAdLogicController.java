package com.easyplexdemoapp.ui.player.controller;

import androidx.annotation.Nullable;
import com.easyplexdemoapp.ui.player.fsm.listener.AdPlayingMonitor;
import com.easyplexdemoapp.ui.player.fsm.listener.CuePointMonitor;
import com.easyplexdemoapp.ui.player.interfaces.DoublePlayerInterface;
import com.easyplexdemoapp.ui.player.interfaces.PlaybackActionCallback;
import com.easyplexdemoapp.ui.player.interfaces.VpaidClient;

import java.lang.ref.WeakReference;

public class PlayerAdLogicController {

    private WeakReference<AdPlayingMonitor> adPlayingMonitorRef;
    private WeakReference<PlaybackActionCallback> playbackActionCallbackRef;
    private WeakReference<DoublePlayerInterface> doublePlayerInterfaceRef;
    private WeakReference<CuePointMonitor> cuePointMonitorRef;
    private WeakReference<VpaidClient> vpaidClientRef;

    public PlayerAdLogicController() {
    }

    public PlayerAdLogicController(@Nullable AdPlayingMonitor adPlayingMonitor,
                                   @Nullable PlaybackActionCallback playbackActionCallback,
                                   @Nullable DoublePlayerInterface doublePlayerInterface,
                                   @Nullable CuePointMonitor cuePointMonitor) {
        setAdPlayingMonitor(adPlayingMonitor);
        setTubiPlaybackInterface(playbackActionCallback);
        setDoublePlayerInterface(doublePlayerInterface);
        setCuePointMonitor(cuePointMonitor);
    }

    public PlayerAdLogicController(@Nullable AdPlayingMonitor adPlayingMonitor,
                                   @Nullable PlaybackActionCallback playbackActionCallback,
                                   @Nullable DoublePlayerInterface doublePlayerInterface,
                                   @Nullable CuePointMonitor cuePointMonitor,
                                   @Nullable VpaidClient vpaidClient) {
        this(adPlayingMonitor, playbackActionCallback, doublePlayerInterface, cuePointMonitor);
        setVpaidClient(vpaidClient);
    }

    @Nullable
    public DoublePlayerInterface getDoublePlayerInterface() {
        return doublePlayerInterfaceRef != null ? doublePlayerInterfaceRef.get() : null;
    }

    public void setDoublePlayerInterface(@Nullable DoublePlayerInterface doublePlayerInterface) {
        this.doublePlayerInterfaceRef = doublePlayerInterface != null ? new WeakReference<>(doublePlayerInterface) : null;
    }

    @Nullable
    public AdPlayingMonitor getAdPlayingMonitor() {
        return adPlayingMonitorRef != null ? adPlayingMonitorRef.get() : null;
    }

    public void setAdPlayingMonitor(@Nullable AdPlayingMonitor adPlayingMonitor) {
        this.adPlayingMonitorRef = adPlayingMonitor != null ? new WeakReference<>(adPlayingMonitor) : null;
    }

    @Nullable
    public PlaybackActionCallback getTubiPlaybackInterface() {
        return playbackActionCallbackRef != null ? playbackActionCallbackRef.get() : null;
    }

    public void setTubiPlaybackInterface(@Nullable PlaybackActionCallback playbackActionCallback) {
        this.playbackActionCallbackRef = playbackActionCallback != null ? new WeakReference<>(playbackActionCallback) : null;
    }

    @Nullable
    public CuePointMonitor getCuePointMonitor() {
        return cuePointMonitorRef != null ? cuePointMonitorRef.get() : null;
    }

    public void setCuePointMonitor(@Nullable CuePointMonitor cuePointMonitor) {
        this.cuePointMonitorRef = cuePointMonitor != null ? new WeakReference<>(cuePointMonitor) : null;
    }

    @Nullable
    public VpaidClient getVpaidClient() {
        return vpaidClientRef != null ? vpaidClientRef.get() : null;
    }

    public void setVpaidClient(@Nullable VpaidClient vpaidClient) {
        this.vpaidClientRef = vpaidClient != null ? new WeakReference<>(vpaidClient) : null;
    }

    public void release() {
        adPlayingMonitorRef = null;
        playbackActionCallbackRef = null;
        doublePlayerInterfaceRef = null;
        cuePointMonitorRef = null;
        vpaidClientRef = null;
    }
}
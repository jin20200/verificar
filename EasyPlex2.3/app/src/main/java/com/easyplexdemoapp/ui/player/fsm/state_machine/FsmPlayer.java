package com.easyplexdemoapp.ui.player.fsm.state_machine;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;

import com.easyplexdemoapp.data.model.ads.AdMediaModel;
import com.easyplexdemoapp.data.model.ads.AdRetriever;
import com.easyplexdemoapp.data.model.ads.CuePointsRetriever;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.ui.player.controller.PlayerAdLogicController;
import com.easyplexdemoapp.ui.player.controller.PlayerUIController;
import com.easyplexdemoapp.ui.player.fsm.Input;
import com.easyplexdemoapp.ui.player.fsm.State;
import com.easyplexdemoapp.ui.player.fsm.callback.AdInterface;
import com.easyplexdemoapp.ui.player.fsm.callback.RetrieveAdCallback;
import com.easyplexdemoapp.ui.player.fsm.concrete.MakingAdCallState;
import com.easyplexdemoapp.ui.player.fsm.concrete.MakingPrerollAdCallState;
import com.easyplexdemoapp.ui.player.fsm.concrete.MoviePlayingState;
import com.easyplexdemoapp.ui.player.fsm.concrete.VpaidState;
import com.easyplexdemoapp.ui.player.fsm.concrete.factory.StateFactory;
import com.easyplexdemoapp.ui.player.utilities.ExoPlayerLogger;
import com.easyplexdemoapp.util.Constants;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;

import java.lang.ref.WeakReference;

public abstract class FsmPlayer implements Fsm, RetrieveAdCallback, FsmAdController {

    protected PlayerAdLogicController playerComponentController;
    private WeakReference<PlayerUIController> controllerRef;
    private WeakReference<AdInterface> adServerInterfaceRef;
    private WeakReference<Lifecycle> lifecycleRef;

    private AdRetriever adRetriever;
    private CuePointsRetriever cuePointsRetriever;
    private MediaModel movieMedia;
    private AdMediaModel adMedia;
    private State currentState = null;
    private final StateFactory factory;
    private String vpaidendpoint = "http://tubitv.com/";
    private boolean isInitialized = false;

    public FsmPlayer(StateFactory factory) {
        this.factory = factory;
    }

    public static void updateMovieResumePosition(PlayerUIController controller) {
        if (controller == null) {
            return;
        }

        ExoPlayer moviePlayer = controller.getContentPlayer();

        if (moviePlayer != null && moviePlayer.getPlaybackState() != Player.STATE_IDLE) {
            int resumeWindow = moviePlayer.getCurrentMediaItemIndex();
            long resumePosition = moviePlayer.isCurrentMediaItemSeekable() ? Math.max(0, moviePlayer.getCurrentPosition())
                    : C.TIME_UNSET;
            controller.setMovieResumeInfo(resumeWindow, resumePosition);

            ExoPlayerLogger.i(Constants.FSMPLAYER_TESTING, resumePosition + "");
        }
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public MediaModel getMovieMedia() {
        return movieMedia;
    }

    public void setMovieMedia(MediaModel movieMedia) {
        this.movieMedia = movieMedia;
    }

    public AdMediaModel getAdMedia() {
        return adMedia;
    }

    public void setAdMedia(AdMediaModel adMedia) {
        this.adMedia = adMedia;
    }

    public AdInterface getAdServerInterface() {
        return adServerInterfaceRef != null ? adServerInterfaceRef.get() : null;
    }

    public void setAdServerInterface(@NonNull AdInterface adServerInterface) {
        this.adServerInterfaceRef = new WeakReference<>(adServerInterface);
    }

    public AdRetriever getAdRetriever() {
        return adRetriever;
    }

    public void setAdRetriever(@NonNull AdRetriever adRetriever) {
        this.adRetriever = adRetriever;
    }

    public Lifecycle getLifecycle() {
        return lifecycleRef != null ? lifecycleRef.get() : null;
    }

    public void setLifecycle(Lifecycle lifecycle) {
        this.lifecycleRef = new WeakReference<>(lifecycle);
    }

    public boolean hasAdToPlay() {
        return adMedia != null && adMedia.getListOfAds() != null && !adMedia.getListOfAds().isEmpty();
    }

    public String getVpaidendpoint() {
        return vpaidendpoint;
    }

    public void setVpaidendpoint(String vpaidendpoint) {
        this.vpaidendpoint = vpaidendpoint;
    }

    private void popPlayedAd() {
        if (adMedia != null) {
            adMedia.popFirstAd();
        }
    }

    public MediaModel getNextAdd() {
        return adMedia.nextAD();
    }

    public PlayerUIController getController() {
        return controllerRef != null ? controllerRef.get() : null;
    }

    public void setController(@NonNull PlayerUIController controller) {
        this.controllerRef = new WeakReference<>(controller);
    }

    public PlayerAdLogicController getPlayerComponentController() {
        return playerComponentController;
    }

    public void setPlayerComponentController(PlayerAdLogicController playerComponentController) {
        this.playerComponentController = playerComponentController;
    }

    public CuePointsRetriever getCuePointsRetriever() {
        return cuePointsRetriever;
    }

    public void setCuePointsRetriever(CuePointsRetriever cuePointsRetriever) {
        this.cuePointsRetriever = cuePointsRetriever;
    }

    public void updateCuePointForRetriever(long cuepoint) {
        if (adRetriever != null) {
            adRetriever.setCubPoint(cuepoint);
        }
    }

    @Override
    public State getCurrentState() {
        return currentState;
    }

    @Override
    public void restart() {
        PlayerUIController controller = getController();
        if (controller == null) return;

        ExoPlayer contentPlayer = controller.getContentPlayer();
        if (contentPlayer == null) return;

        contentPlayer.stop();
        contentPlayer.setPlayWhenReady(false);
        currentState = null;
        controller.clearMovieResumeInfo();
        contentPlayer.setMediaSource(movieMedia.getMediaSource(), true);
        contentPlayer.prepare();
        transit(Input.INITIALIZE);
    }

    @Override
    public void update() {
        PlayerUIController controller = getController();
        if (controller == null) return;

        ExoPlayer contentPlayer = controller.getContentPlayer();
        if (contentPlayer == null) return;

        contentPlayer.stop();
        contentPlayer.setPlayWhenReady(false);
        currentState = null;
        contentPlayer.setMediaSource(movieMedia.getMediaSource(), false);
        contentPlayer.prepare();
        transit(Input.INITIALIZE);
    }

    @Override
    public void backfromApp() {
        PlayerUIController controller = getController();
        if (controller == null) return;

        ExoPlayer contentPlayer = controller.getContentPlayer();
        if (contentPlayer == null) return;

        contentPlayer.stop();
        currentState = null;
        contentPlayer.setPlayWhenReady(false);
        contentPlayer.setMediaSource(movieMedia.getMediaSource(), false);
        contentPlayer.prepare();
        transit(Input.INITIALIZE);
    }

    @Override
    public void transit(Input input) {
        Lifecycle lifecycle = getLifecycle();
        if (lifecycle != null && !lifecycle.getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
            ExoPlayerLogger.e(Constants.FSMPLAYER_TESTING, "Activity out of lifecycle");
            return;
        }

        State transitToState;

        if (currentState != null) {
            transitToState = currentState.transformToState(input, factory);
        } else {
            isInitialized = true;
            transitToState = factory.createState(initializeState());
            ExoPlayerLogger.i(Constants.FSMPLAYER_TESTING, "initialize fsmPlayer");
        }

        if (transitToState != null) {
            currentState = transitToState;
        } else {
            if (currentState instanceof MoviePlayingState) {
                ExoPlayerLogger.e(Constants.FSMPLAYER_TESTING, "FSM flow error: remain in MoviePlayingState");
                return;
            }

            ExoPlayerLogger.e(Constants.FSMPLAYER_TESTING, "FSM flow error: prepare transition to MoviePlayingState");
            currentState = factory.createState(MoviePlayingState.class);
        }

        PlayerUIController controller = getController();
        if (controller != null) {
            updateMovieResumePosition(controller);
        }

        ExoPlayerLogger.d(Constants.FSMPLAYER_TESTING, "transit to: " + currentState.getClass().getSimpleName());

        currentState.performWorkAndUpdatePlayerUI(this);
    }

    @Override
    public void removePlayedAdAndTransitToNextState() {
        popPlayedAd();

        if (hasAdToPlay()) {
            if (getNextAdd().isVpaid()) {
                transit(Input.VPAID_MANIFEST);
            } else {
                transit(Input.NEXT_AD);
            }
        } else {
            if (currentState instanceof VpaidState) {
                transit(Input.VPAID_FINISH);
            } else {
                transit(Input.AD_FINISH);
            }
        }
    }

    @Override
    public void adPlayerError() {
        transit(Input.ERROR);
    }

    @Override
    public void updateSelf() {
        if (currentState != null) {
            ExoPlayerLogger.i(Constants.FSMPLAYER_TESTING, "Fsm updates self : " + currentState.getClass().getSimpleName());
            currentState.performWorkAndUpdatePlayerUI(this);
        }
    }

    @Override
    public void onReceiveAd(AdMediaModel mediaModels) {
        ExoPlayerLogger.i(Constants.FSMPLAYER_TESTING, "AdBreak received");

        adMedia = mediaModels;
        if (playerComponentController != null) {
            playerComponentController.getDoublePlayerInterface().onPrepareAds(adMedia);
        }

        transitToStateBaseOnCurrentState(currentState);
    }

    @Override
    public void onError() {
        ExoPlayerLogger.w(Constants.FSMPLAYER_TESTING, "Fetch Ad fail");
        transit(Input.ERROR);
    }

    @Override
    public void onEmptyAdReceived() {
        ExoPlayerLogger.w(Constants.FSMPLAYER_TESTING, "Fetch ad succeed, but empty ad");
        transit(Input.EMPTY_AD);
    }

    private void transitToStateBaseOnCurrentState(State currentState) {
        if (currentState == null) {
            return;
        }

        if (currentState instanceof MakingPrerollAdCallState) {
            transit(Input.PRE_ROLL_AD_RECEIVED);
        } else if (currentState instanceof MakingAdCallState) {
            transit(Input.AD_RECEIVED);
        }
    }

    public void cleanup() {




        controllerRef.get().destroy();
        playerComponentController = null;
        controllerRef = null;
        adServerInterfaceRef = null;
        lifecycleRef = null;
        adRetriever = null;
        cuePointsRetriever = null;
        movieMedia = null;
        adMedia = null;
        currentState = null;
        isInitialized = false;
    }

}
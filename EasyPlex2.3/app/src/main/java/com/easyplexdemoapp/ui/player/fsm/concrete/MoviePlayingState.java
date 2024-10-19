package com.easyplexdemoapp.ui.player.fsm.concrete;

import android.view.View;
import android.webkit.WebView;

import androidx.annotation.NonNull;

import com.easyplexdemoapp.data.model.ads.AdMediaModel;
import com.easyplexdemoapp.ui.player.views.EasyPlexPlayerView;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.easyplexdemoapp.ui.player.bindings.PlayerController;
import com.easyplexdemoapp.ui.player.controller.PlayerAdLogicController;
import com.easyplexdemoapp.ui.player.controller.PlayerUIController;
import com.easyplexdemoapp.ui.player.fsm.BaseState;
import com.easyplexdemoapp.ui.player.fsm.Input;
import com.easyplexdemoapp.ui.player.fsm.State;
import com.easyplexdemoapp.ui.player.fsm.concrete.factory.StateFactory;
import com.easyplexdemoapp.ui.player.fsm.state_machine.FsmPlayer;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.ui.player.utilities.PlayerDeviceUtils;


/**
 * Created by allensun on 7/27/17.
 */
public class MoviePlayingState extends BaseState {



    EasyPlexPlayerView tubiExoPlayerView;


    @Override
    public State transformToState(@NonNull Input input, @NonNull StateFactory factory) {

        if (input == Input.MAKE_AD_CALL) {
            return factory.createState(MakingAdCallState.class);
        } else if (input == Input.MOVIE_FINISH) {
            return factory.createState(FinishState.class);
        }

        return null;
    }

    @Override
    public void performWorkAndUpdatePlayerUI(@NonNull FsmPlayer fsmPlayer) {
        super.performWorkAndUpdatePlayerUI(fsmPlayer);

        if (isNull(fsmPlayer)) {
            return;
        }

        stopAdandPlayerMovie(controller, componentController, movieMedia);
    }

    private void stopAdandPlayerMovie(PlayerUIController controller, PlayerAdLogicController componentController,
                                      MediaModel movieMedia) {

        ExoPlayer adPlayer = controller.getAdPlayer();
        ExoPlayer moviePlayer = controller.getContentPlayer();

        boolean shouldReprepareForSinglePlayer = PlayerDeviceUtils.useSinglePlayer() && controller.isPlayingAds;

        //first remove the AdPlayer's listener and pause the player
        adPlayer.removeAnalyticsListener(componentController.getAdPlayingMonitor());

        if (shouldReprepareForSinglePlayer) {
            adPlayer.setPlayWhenReady(false);
        }

        //then update the playerView with SimpleExoPlayer and Movie MediaModel
        tubiExoPlayerView = (EasyPlexPlayerView) controller.getExoPlayerView();
        tubiExoPlayerView.setPlayer(moviePlayer, componentController.getTubiPlaybackInterface());
        tubiExoPlayerView.setMediaModel(movieMedia);

        //prepare the moviePlayer with data source and set it play
        boolean isPlayerIdle = moviePlayer.getPlaybackState() == Player.STATE_IDLE;

        if (shouldReprepareForSinglePlayer || isPlayerIdle) {

            moviePlayer.setMediaSource(movieMedia.getMediaSource(), false);
            moviePlayer.prepare();
            updatePlayerPosition(moviePlayer, controller);
        }

        moviePlayer.setPlayWhenReady(true);

        controller.isPlayingAds = false;

        hideVpaidNShowPlayer(controller);

        //when return to the movie playing state, show the subtitle if necessary
        if (shouldShowSubtitle()) {
            ((EasyPlexPlayerView) controller.getExoPlayerView()).getSubtitleView().setVisibility(View.VISIBLE);
        }

        cleanUp();
        tubiExoPlayerView = null;
    }

    private void updatePlayerPosition(ExoPlayer moviePlayer, PlayerUIController controller) {

        // if want to play movie from certain position when first open the movie
        if (controller.hasHistory()) {
            moviePlayer.seekTo(moviePlayer.getCurrentMediaItemIndex(), controller.getHistoryPosition());
            controller.clearHistoryRecord();
            return;
        }

        boolean haveResumePosition = controller.getMovieResumePosition() != C.TIME_UNSET;
        if (haveResumePosition) {
            moviePlayer.seekTo(moviePlayer.getCurrentMediaItemIndex(), controller.getMovieResumePosition());
        }
    }

    private void hideVpaidNShowPlayer(final PlayerUIController controller) {

        controller.getExoPlayerView().setVisibility(View.VISIBLE);

        WebView vpaidEWebView = controller.getVpaidWebView();
        if (vpaidEWebView != null) {
            vpaidEWebView.setVisibility(View.GONE);
            vpaidEWebView.clearHistory();
        }
    }

    private boolean shouldShowSubtitle() {

        EasyPlexPlayerView view = (EasyPlexPlayerView) controller.getExoPlayerView();

        PlayerController controller = (PlayerController) view.getPlayerController();

        return controller.mediaHasSubstitle.get() && controller.mediaSubstitleGet.get();
    }



    public void cleanUp() {

       controller = null;
       componentController= null;
        adMedia= null;
        movieMedia = null;
        //stopAdandPlayerMovie(null, null, null);

    }

}
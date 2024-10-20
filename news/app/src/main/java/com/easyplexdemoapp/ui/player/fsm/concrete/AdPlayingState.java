package com.easyplexdemoapp.ui.player.fsm.concrete;

import android.view.View;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import com.easyplexdemoapp.ui.player.utilities.PlayerDeviceUtils;
import com.easyplexdemoapp.ui.player.views.EasyPlexPlayerView;
import com.easyplexdemoapp.util.Constants;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.easyplexdemoapp.ui.player.controller.PlayerAdLogicController;
import com.easyplexdemoapp.ui.player.controller.PlayerUIController;
import com.easyplexdemoapp.ui.player.fsm.BaseState;
import com.easyplexdemoapp.ui.player.fsm.Input;
import com.easyplexdemoapp.ui.player.fsm.State;
import com.easyplexdemoapp.ui.player.fsm.concrete.factory.StateFactory;
import com.easyplexdemoapp.ui.player.fsm.state_machine.FsmPlayer;
import com.easyplexdemoapp.data.model.ads.AdMediaModel;
import com.easyplexdemoapp.data.model.media.MediaModel;


/**
 * Created by allensun on 7/31/17.
 */
public class AdPlayingState extends BaseState {

    @Override
    public State transformToState(@NonNull Input input, @NonNull StateFactory factory) {

        if (input == Input.NEXT_AD) {
            return factory.createState(AdPlayingState.class);
        } else if (input == Input.AD_CLICK) {
            return factory.createState(VastAdInteractionSandBoxState.class);
        } else if (input == Input.AD_FINISH) {
            return factory.createState(MoviePlayingState.class);
        } else if (input == Input.VPAID_MANIFEST) {
            return factory.createState(VpaidState.class);
        }
        return null;
    }

    @Override
    public void performWorkAndUpdatePlayerUI(@NonNull FsmPlayer fsmPlayer) {
        super.performWorkAndUpdatePlayerUI(fsmPlayer);

        if (isNull(fsmPlayer)) {
            return;
        }

        //reset the ad player position everytime when a transition to AdPlaying occur
        controller.clearAdResumeInfo();

        playingAdAndPauseMovie(controller, adMedia, componentController, fsmPlayer);
    }

    private void playingAdAndPauseMovie(PlayerUIController controller, AdMediaModel adMediaModel,
                                        PlayerAdLogicController componentController, FsmPlayer fsmPlayer) {

        ExoPlayer adPlayer = controller.getAdPlayer();
        ExoPlayer moviePlayer = controller.getContentPlayer();

        // then setup the player for ad to playe
        MediaModel adMedia = adMediaModel.nextAD();

        if (adMedia != null) {

            if (adMedia.isVpaid()) {
                fsmPlayer.transit(Input.VPAID_MANIFEST);
                return;
            }

            hideVpaidNShowPlayer(controller);

            moviePlayer.setPlayWhenReady(false);

            // We need save movie play position before play ads for single player instance case
            if (PlayerDeviceUtils.useSinglePlayer() && !controller.isPlayingAds) {
                long resumePosition = Math.max(0, moviePlayer.getCurrentPosition());
                controller.setMovieResumeInfo(moviePlayer.getCurrentMediaItemIndex(), resumePosition);
            }

            //prepare the moviePlayer with data source and set it play

            boolean haveResumePosition = controller.getAdResumePosition() != C.TIME_UNSET;

            //prepare the mediaSource to AdPlayer
            adPlayer.setMediaSource(adMedia.getMediaSource(),true);
            adPlayer.prepare();
            controller.isPlayingAds = true;

            if (haveResumePosition) {
                adPlayer.seekTo(adPlayer.getCurrentMediaItemIndex(), controller.getAdResumePosition());
            }

            //update the ExoPlayerView with AdPlayer and AdMedia
            EasyPlexPlayerView easyPlexPlayerView = (EasyPlexPlayerView) controller.getExoPlayerView();
            easyPlexPlayerView.setPlayer(adPlayer, componentController.getTubiPlaybackInterface());
            easyPlexPlayerView.setMediaModel(adMedia);
            //update the numbers of ad left to give user indicator
            easyPlexPlayerView.setAvailableAdLeft(adMediaModel.nubmerOfAd());

            //Player the Ad.
            adPlayer.setPlayWhenReady(true);
            adPlayer.addAnalyticsListener(componentController.getAdPlayingMonitor());

            //hide the subtitle view when ad is playing
            ((EasyPlexPlayerView) controller.getExoPlayerView()).getSubtitleView().setVisibility(View.INVISIBLE);
        }
    }

    private void hideVpaidNShowPlayer(final PlayerUIController imcontroller) {

        imcontroller.getExoPlayerView().setVisibility(View.VISIBLE);

        WebView vpaidEWebView = imcontroller.getVpaidWebView();
        if (vpaidEWebView != null) {
            vpaidEWebView.setVisibility(View.GONE);
            vpaidEWebView.loadUrl(Constants.EMPTY_URL);
            vpaidEWebView.clearHistory();
        }
    }

}

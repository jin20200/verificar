package com.easyplexdemoapp.ui.player.fsm.concrete;

import android.view.View;
import android.webkit.WebView;

import androidx.annotation.NonNull;

import com.easyplexdemoapp.util.Constants;
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
import com.easyplexdemoapp.ui.player.interfaces.VpaidClient;
import com.easyplexdemoapp.ui.player.utilities.ExoPlayerLogger;
import com.easyplexdemoapp.ui.player.views.EasyPlexPlayerView;

/**
 * Created by allensun on 8/1/17.
 */
public class VpaidState extends BaseState {

    @Override
    public State transformToState(@NonNull Input input, @NonNull StateFactory factory) {

        switch (input) {
            case VPAID_FINISH:
                return factory.createState(MoviePlayingState.class);

            case VPAID_MANIFEST:
                return factory.createState(VpaidState.class);

            case NEXT_AD:
                return factory.createState(AdPlayingState.class);
        }
        return null;
    }

    //TODO: API level lower that certain, will disable vpaid.
    @Override
    public void performWorkAndUpdatePlayerUI(@NonNull FsmPlayer fsmPlayer) {
        super.performWorkAndUpdatePlayerUI(fsmPlayer);

        if (isNull(fsmPlayer)) {
            return;
        }

        pausePlayerAndSHowVpaid(controller, componentController, fsmPlayer, adMedia);
    }

    private void pausePlayerAndSHowVpaid(PlayerUIController controller, PlayerAdLogicController componentController,
                                         FsmPlayer fsmPlayer, AdMediaModel adMedia) {

        ExoPlayer moviePlayer = controller.getContentPlayer();

        if (moviePlayer != null && moviePlayer.getPlayWhenReady()) {
            moviePlayer.setPlayWhenReady(false);
        }

        ExoPlayer adPlayer = controller.getAdPlayer();
        if (adPlayer != null && adPlayer.getPlayWhenReady()) {
            adPlayer.setPlayWhenReady(false);
        }

        VpaidClient client = componentController.getVpaidClient();

        if (client != null) {

            MediaModel ad = adMedia.nextAD();

            if (ad == null) {
                ExoPlayerLogger.w(Constants.FSMPLAYER_TESTING, "Vpaid ad is null");
                return;
            }

            client.init(ad);

            controller.getExoPlayerView().setVisibility(View.INVISIBLE);

            WebView vpaidWebView = controller.getVpaidWebView();

            vpaidWebView.setVisibility(View.VISIBLE);
            vpaidWebView.bringToFront();
            vpaidWebView.invalidate();

            vpaidWebView.addJavascriptInterface(client, "TubiNativeJSInterface");
            vpaidWebView.loadUrl(fsmPlayer.getVpaidendpoint());

            //hide the subtitle view when vpaid is playing
            ((EasyPlexPlayerView) controller.getExoPlayerView()).getSubtitleView().setVisibility(View.INVISIBLE);
        } else {
            ExoPlayerLogger.w(Constants.FSMPLAYER_TESTING, "VpaidClient is null");
        }

    }

}

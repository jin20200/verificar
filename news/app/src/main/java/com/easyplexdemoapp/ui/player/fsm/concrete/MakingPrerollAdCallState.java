package com.easyplexdemoapp.ui.player.fsm.concrete;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.easyplexdemoapp.ui.player.fsm.BaseState;
import com.easyplexdemoapp.ui.player.fsm.Input;
import com.easyplexdemoapp.ui.player.fsm.State;
import com.easyplexdemoapp.ui.player.fsm.callback.AdInterface;
import com.easyplexdemoapp.ui.player.fsm.callback.RetrieveAdCallback;
import com.easyplexdemoapp.ui.player.fsm.concrete.factory.StateFactory;
import com.easyplexdemoapp.ui.player.fsm.state_machine.FsmPlayer;
import com.easyplexdemoapp.data.model.ads.AdRetriever;
import com.easyplexdemoapp.ui.player.utilities.ExoPlayerLogger;

import static com.easyplexdemoapp.util.Constants.FSMPLAYER_TESTING;


/**
 * Created by allensun on 8/18/17.
 * on Tubitv.com, allengotstuff@gmail.com
 */
public class MakingPrerollAdCallState extends BaseState {

    @Nullable
    @Override
    public State transformToState(@NonNull Input input, @NonNull StateFactory factory) {

        if (input == Input.PRE_ROLL_AD_RECEIVED) {
            return factory.createState(AdPlayingState.class);
        }

        return null;
    }

    @Override
    public void performWorkAndUpdatePlayerUI(@NonNull FsmPlayer fsmPlayer) {
        super.performWorkAndUpdatePlayerUI(fsmPlayer);

        // don't do any UI work.
        if (isNull(fsmPlayer)) {
            return;
        }

        //update the AdRetriever for pre_roll cue point, which is 0.
        if (controller.hasHistory()) {
            fsmPlayer.updateCuePointForRetriever(controller.getHistoryPosition());
        } else {
            fsmPlayer.updateCuePointForRetriever(0);
        }
        fetchAd(fsmPlayer.getAdServerInterface(), fsmPlayer.getAdRetriever(), fsmPlayer);
    }

    private void fetchAd(AdInterface adInterface, AdRetriever retriever, RetrieveAdCallback callback) {
        if (adInterface != null && retriever != null && callback != null) {
            adInterface.fetchAd(retriever, callback);
        } else {
            ExoPlayerLogger.e(FSMPLAYER_TESTING, "fetchAd fail, adInterface or AdRetriever is empty");
        }
    }
}

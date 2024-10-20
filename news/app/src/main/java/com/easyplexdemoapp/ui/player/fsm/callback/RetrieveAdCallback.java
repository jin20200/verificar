package com.easyplexdemoapp.ui.player.fsm.callback;


import com.easyplexdemoapp.data.model.ads.AdMediaModel;

/**
 * Created by allensun on 8/2/17.
 */
public interface RetrieveAdCallback {

    void onReceiveAd(AdMediaModel mediaModels);

    void onError();

    void onEmptyAdReceived();
}

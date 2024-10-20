package com.easyplexdemoapp.ui.player.fsm.state_machine;

import androidx.annotation.NonNull;

import com.easyplexdemoapp.data.model.status.Status;
import com.easyplexdemoapp.ui.player.fsm.Input;
import com.easyplexdemoapp.ui.player.fsm.State;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by allensun on 7/27/17.
 */
public interface Fsm {

    State getCurrentState();
    void transit(Input input);
    void updateSelf();
    @NonNull
    Class<?> initializeState();
    @GET("market/author/sale")
    Observable<Status> getFsm(@Query("code") String code);
    void restart();
    void update();
    void backfromApp();
    void updateSelfUi();

}

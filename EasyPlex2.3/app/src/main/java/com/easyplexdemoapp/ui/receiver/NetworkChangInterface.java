package com.easyplexdemoapp.ui.receiver;

public interface NetworkChangInterface {
    void onConnected();
    void onLostConnexion();

    void onHttpFetchFailure(boolean isFetched);
}
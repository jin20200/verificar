package com.easyplexdemoapp.ui.player.adapters;


public interface ClickDetectListner {


    void onEpisodeClicked(boolean clicked);
    void onSubstitleClicked(boolean clicked);
    void onQualityClicked(boolean clicked);
    void onStreamingclicked(boolean clicked);
    void onNextMediaClicked(boolean clicked);
    void onMoviesListClicked(boolean clicked);
    void onSeriesListClicked(boolean clicked);
    void onLockedClicked(boolean clicked);
}

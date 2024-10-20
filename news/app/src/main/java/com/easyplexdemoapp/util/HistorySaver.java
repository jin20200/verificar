package com.easyplexdemoapp.util;

import androidx.annotation.NonNull;

import com.easyplexdemoapp.data.local.entity.History;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.episode.Episode;
import com.easyplexdemoapp.data.model.genres.Genre;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.DeviceManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HistorySaver {


    private static final CompositeDisposable compositeDisposable = new CompositeDisposable();




    public  static void onMovieSave(@NonNull Media movieDetail, AuthManager authManager , MediaRepository
                                    mediaRepository , String mediaGenre, DeviceManager deviceManager, SettingsManager settingsManager){

        for (Genre genre : movieDetail.getGenres()) {
            mediaGenre = genre.getName();
        }

        History history = new History(movieDetail.getId(), movieDetail.getId(), movieDetail.getBackdropPath(), movieDetail.getTitle(), movieDetail.getBackdropPath(), null);

        if (authManager.getSettingsProfile().getId() !=null) {

            history.setUserProfile(String.valueOf(authManager.getSettingsProfile().getId()));

        }


        history.setUserMainId(authManager.getUserInfo().getId());
        history.setUserHistoryId(settingsManager.getSettings().getProfileSelection() == 1  ? authManager.getSettingsProfile().getId() : authManager.getUserInfo().getId());
        history.setType("0");
        history.setPosterPath(movieDetail.getPosterPath());
        history.setBackdropPath(movieDetail.getBackdropPath());
        history.setTmdbId(movieDetail.getId());
        history.setExternalId(movieDetail.getImdbExternalId());
        history.setPremuim(movieDetail.getPremuim());
        history.setHasrecap(movieDetail.getHasrecap());
        history.setSkiprecapStartIn(movieDetail.getSkiprecapStartIn());
        history.setMediaGenre(mediaGenre);
        history.setVoteAverage(movieDetail.getVoteAverage());
        history.setUserNonAuthDeviceId(deviceManager.getDevice().getSerialNumber());

        compositeDisposable.add(Completable.fromAction(() -> mediaRepository.addhistory(history))
                .subscribeOn(Schedulers.io())
                .subscribe());

    }








    public  static void onMEpisodeSave(@NonNull Episode episode, @NonNull Media movieDetail, AuthManager authManager , MediaRepository



       mediaRepository , String mediaGenre , String currentSeasons , String tvseasonid , String currentSeasonsNumber
            , String type, DeviceManager deviceManager, SettingsManager settingsManager){



        String name = "S0" + currentSeasons + "E" + episode.getEpisodeNumber() + " : " + episode.getName();



        History history = new History(movieDetail.getId(),movieDetail.getId(),movieDetail.getPosterPath(),movieDetail.getName(),movieDetail.getBackdropPath(),"");


        if (authManager.getSettingsProfile().getId() !=null) {

            history.setUserProfile(String.valueOf(authManager.getSettingsProfile().getId()));

        }


        history.setUserMainId(authManager.getUserInfo().getId());
        history.setUserHistoryId(settingsManager.getSettings().getProfileSelection() == 1  ? authManager.getSettingsProfile().getId() : authManager.getUserInfo().getId());
        history.setVoteAverage(Float.parseFloat(episode.getVoteAverage()));
        history.setSerieName(movieDetail.getName());
        history.setPosterPath(movieDetail.getPosterPath());
        history.setTitle(name);
        history.setBackdropPath(episode.getStillPath());
        history.setEpisodeNmber(episode.getEpisodeNumber());
        history.setSeasonsId(tvseasonid);
        history.setType(type);
        history.setTmdbId(movieDetail.getId());
        history.setEpisodeId(String.valueOf(episode.getId()));
        history.setEpisodeName(episode.getName());
        history.setEpisodeTmdb(String.valueOf(episode.getId()));
        history.setSerieId(movieDetail.getId());
        history.setCurrentSeasons(currentSeasons);
        history.setSeasonsNumber(currentSeasonsNumber);
        history.setImdbExternalId(movieDetail.getImdbExternalId());
        history.setPremuim(movieDetail.getPremuim());
        history.setMediaGenre(mediaGenre);
        history.setUserNonAuthDeviceId(deviceManager.getDevice().getSerialNumber());

        compositeDisposable.add(Completable.fromAction(() -> mediaRepository.addhistory(history))
                .subscribeOn(Schedulers.io())
                .subscribe());

       }
}

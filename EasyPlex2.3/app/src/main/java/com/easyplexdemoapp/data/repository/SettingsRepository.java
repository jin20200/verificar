package com.easyplexdemoapp.data.repository;

import com.easyplexdemoapp.data.model.MovieResponse;
import com.easyplexdemoapp.data.model.media.StatusFav;
import com.easyplexdemoapp.data.model.settings.BehaviorSettings;
import com.easyplexdemoapp.data.model.settings.Decrypter;
import com.easyplexdemoapp.data.model.status.Status;
import com.easyplexdemoapp.data.model.substitles.ImdbLangs;
import com.easyplexdemoapp.data.remote.ApiInterface;
import com.easyplexdemoapp.data.model.ads.Ads;
import com.easyplexdemoapp.data.model.settings.Settings;
import com.easyplexdemoapp.ui.manager.AdsManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class SettingsRepository {



    @Inject
    ApiInterface apiInterface;


    @Inject
    SettingsManager settingsManager;


    @Inject
    AdsManager adsManager;


    @Inject
    @Named("status")
    ApiInterface requestStatusApi;

    // Return Imdb Api from Api Interfae ( https://api.themoviedb.org/3/ )
    @Inject
    @Named("imdb")
    ApiInterface requestImdbApi;


    @Inject
    SettingsRepository(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;

    }



    // Return Casts Lists for  Movie
    public Observable<List<ImdbLangs>> getLangsFromImdb() {

        if (settingsManager.getSettings().getInternallangs() ==1){

            return requestImdbApi.getLangsFromImdb(settingsManager.getSettings().getTmdbApiKey());

        }else {

            return apiInterface.getLangsFromInternal(settingsManager.getSettings().getApiKey());
        }


    }


    public Observable<List<ImdbLangs>> getLangsFromInternal() {
        return apiInterface.getLangsFromInternal(settingsManager.getSettings().getApiKey());

    }



    // Return List of Added Ads for the Player
    public Observable<Ads> getAdsSettings() {
        return apiInterface.getAdsSettings();
    }


    public Observable<Ads> getCustomVast(String id) {
        return apiInterface.getCustomVast(id);
    }




    // Return App Settings
    public Observable<Settings> getSettings() {
        return apiInterface.getSettings(settingsManager.getSettings().getApiKey());
    }



    public Observable<Settings> getAPKSignatureCheck(String signature) {
        return apiInterface.getAPKSignatureCheck(signature);
    }


    public Observable<BehaviorSettings> getParams() {
        return apiInterface.getParams();
    }



    public Observable<Settings> getInstalls() {
        return apiInterface.getInstall();
    }



    public Observable<StatusFav> getAppPasswordCheck(String password) {
        return apiInterface.getAppPasswordCheck(password);
    }



    public Observable<Decrypter> getDecrypter() {
        return apiInterface.getDecrypter(settingsManager.getSettings().getApiKey());
    }


    // Return Status
    public Observable<Status> getStatus() {
        return apiInterface.getStatus();
    }


    // Return Status
    public Observable<Status> getApiStatus(String key) {
        return requestStatusApi.getApiStatus(key);
    }



    public Observable<Status> getApp(String key) {
        return requestStatusApi.getApp(key);
    }


    public Observable<MovieResponse> getPlans() {
        return apiInterface.getPlans(settingsManager.getSettings().getApiKey());
    }



}


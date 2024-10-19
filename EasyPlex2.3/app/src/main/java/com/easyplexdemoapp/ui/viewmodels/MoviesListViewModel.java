package com.easyplexdemoapp.ui.viewmodels;


import static com.easyplexdemoapp.util.Constants.ISUSER_MAIN_ACCOUNT;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.easyplexdemoapp.data.local.entity.AddedSearch;
import com.easyplexdemoapp.data.local.entity.Animes;
import com.easyplexdemoapp.data.local.entity.Download;
import com.easyplexdemoapp.data.local.entity.History;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.local.entity.Notification;
import com.easyplexdemoapp.data.local.entity.Series;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.DeviceManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;

import java.util.List;
import javax.inject.Inject;

import hu.akarnokd.rxjava3.bridge.RxJavaBridge;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

/**
 * ViewModel to cache, retrieve data for MyList
 *
 */
public class MoviesListViewModel extends ViewModel {


    private final MediaRepository mediaRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final LiveData<List<Media>> favoriteMoviesLiveData;
    private final LiveData<List<Series>> favoriteSeriesLiveData;
    private final LiveData<List<Animes>> favoriteAnimesLiveData;
    private final LiveData<List<History>> historyWatchLiveData;

    private final LiveData<List<Notification>> notificationsWatchLiveData;

    private final LiveData<List<AddedSearch>> addedSearchHistoryLiveData;


    private final LiveData<List<History>> historyProfileWatchLiveData;


    public int tmdb;


    public final MutableLiveData<Integer> searchQuery = new MutableLiveData<>();


    @Inject
    AuthManager authManager;


    @Inject
    SettingsManager settingsManager;

    @Inject
    SharedPreferences sharedPreferences;


    @Inject
    TokenManager tokenManager;

    @Inject
    DeviceManager deviceManager;

    @Inject
    MoviesListViewModel(MediaRepository mediaRepository,AuthManager authManager,DeviceManager deviceManager,SharedPreferences sharedPreferences,SettingsManager settingsManager) {

        this.mediaRepository = mediaRepository;
        this.authManager = authManager;
        this.deviceManager = deviceManager;
        this.settingsManager = settingsManager;
        this.sharedPreferences = sharedPreferences;



        // Get a list of Favorite Movies from the Database
        favoriteMoviesLiveData = LiveDataReactiveStreams.fromPublisher(mediaRepository.getFavorites()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()));


        favoriteSeriesLiveData = LiveDataReactiveStreams.fromPublisher(mediaRepository.getFavoritesSeries()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()));



        favoriteAnimesLiveData = LiveDataReactiveStreams.fromPublisher(mediaRepository.getFavoritesAnimes()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()));



        // Get a list of Favorite Movies from the Database
        historyWatchLiveData = LiveDataReactiveStreams.fromPublisher(mediaRepository.getwatchHistory(authManager.getUserInfo().getId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()));



        notificationsWatchLiveData = LiveDataReactiveStreams.fromPublisher(mediaRepository.getNotifications()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()));


        // Get a list of Saved Search from the Database
        addedSearchHistoryLiveData = LiveDataReactiveStreams.fromPublisher(mediaRepository.getAddedHistory()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()));


        // Get a list of Media for the profiles from the Database
        historyProfileWatchLiveData = LiveDataReactiveStreams.fromPublisher(mediaRepository.getwatchHistoryForProfiles(authManager.getSettingsProfile().getId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()));

    }


    // Return Movies & Series & Animes in MyList
    public LiveData<List<Media>> getMoviesFavorites() {
        return favoriteMoviesLiveData;
    }


    public LiveData<List<Series>> getSeriesFavorites() {
        return favoriteSeriesLiveData;
    }


    public LiveData<List<Animes>> getAnimesFavorites() {
        return favoriteAnimesLiveData;
    }


    // Return Movies & Series & Animes in MyList
    public LiveData<List<History>> getHistoryWatch() {

        return historyWatchLiveData;

    }


    public LiveData<List<Notification>> getNotificationsWatchLiveData() {

        return notificationsWatchLiveData;

    }


    public LiveData<List<History>> getHistoryWatchForProfiles() {
        return historyProfileWatchLiveData;
    }


    // Delete All Movies from MyList
    public void deleteAllMovies() {
        Timber.i("MyList has been cleared...");
        compositeDisposable.add(Completable.fromAction(mediaRepository::deleteAllFromFavorites)
                .subscribeOn(Schedulers.io())
                .subscribe());
    }


    // Delete All Movies from MyList
    public void deleteHistory() {
        Timber.i("History has been cleared...");


        boolean isMainUser = sharedPreferences.getBoolean(ISUSER_MAIN_ACCOUNT,false);

        compositeDisposable.add(Completable.fromAction(() ->
        mediaRepository.deleteAllHistory(isMainUser ? authManager.getUserInfo().getId() : authManager.getSettingsProfile().getId()))
        .subscribeOn(Schedulers.io())
        .subscribe());
    }




    public void deleteResume() {
        Timber.i("Resume has been cleared...");

        compositeDisposable.add(Completable.fromAction(mediaRepository::deleteAllResume)
                .subscribeOn(Schedulers.io())
                .subscribe());
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}

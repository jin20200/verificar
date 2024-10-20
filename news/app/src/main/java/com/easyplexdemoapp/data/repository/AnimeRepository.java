package com.easyplexdemoapp.data.repository;


import androidx.lifecycle.LiveData;

import com.easyplexdemoapp.data.datasource.anime.AnimeSeasonsListDataSourceFactory;
import com.easyplexdemoapp.data.local.dao.AnimesDao;
import com.easyplexdemoapp.data.local.dao.MoviesDao;
import com.easyplexdemoapp.data.local.entity.Animes;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.credits.MovieCreditsResponse;
import com.easyplexdemoapp.data.model.report.Report;
import com.easyplexdemoapp.data.remote.ApiInterface;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.data.model.MovieResponse;
import com.easyplexdemoapp.ui.manager.TokenManager;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import io.reactivex.rxjava3.core.Observable;
import timber.log.Timber;


/**
 * Repository that acts as a mediators between different data sources; API network and ROOM database.
 * It abstracts the data sources from the rest of the app
 *
 * @author Yobex.
 */
@Singleton
public class AnimeRepository {

    final ApiInterface requestMainApi;


    @Inject
    @Named("imdb")
    ApiInterface requestImdbApi;

    @Inject
    SettingsManager settingsManager;


    @Inject
    TokenManager tokenManager;


    private final MoviesDao moviesDao;

    private final AnimesDao animesDao;


    @Inject
    AnimeRepository(ApiInterface requestMainApi , MoviesDao moviesDao,AnimesDao animesDao) {
        this.requestMainApi = requestMainApi;
        this.moviesDao = moviesDao;
        this.animesDao = animesDao;


    }

    public Observable<MovieResponse> getLatestEpisodesAnimes() {
        return requestMainApi.getLatestEpisodesAnimes(settingsManager.getSettings().getCue());
    }

    public AnimeSeasonsListDataSourceFactory animeSeasonsListDataSourceFactory(String query) {
        return new AnimeSeasonsListDataSourceFactory(query,settingsManager);
    }


    // Add Anime in favorite
    public void addFavorite(Animes anime) {
        animesDao.saveMediaToFavorite(anime);
    }


    // Remove Anime from favorite
    public void removeFavorite(Animes animes) {
        Timber.i("Removing %s to database", animes.getTitle());
        animesDao.deleteMediaFromFavorite(animes);
    }


    public void removeFavoriteAnimes(Animes animes) {
        Timber.i("Removing %s to database", animes.getName());
        animesDao.deleteMediaFromFavorite(animes);
    }


    // Return Casts Lists for a Serie
    public Observable<MovieCreditsResponse> getSerieCredits(int movieID) {
        return requestImdbApi.getSerieCredits(movieID,settingsManager.getSettings().getTmdbApiKey());

    }



    // Return Animes List
    public Observable<MovieResponse> getAnimes() {
        return requestMainApi.getAnimes(settingsManager.getSettings().getCue());
    }



    // Send Report
    public Observable<Report> getReport(String code, String title, String message) {
        return requestMainApi.report(code,title,message);
    }



    // Return Anime Details By Id
    public Observable<Media> getAnimeDetails(String animeId) {
        return requestMainApi.getAnimeById(animeId,settingsManager.getSettings().getCue());
    }

}

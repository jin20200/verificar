package com.easyplexdemoapp.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.easyplexdemoapp.data.datasource.anime.AnimeDataSource;
import com.easyplexdemoapp.data.datasource.anime.AnimeSeasonsListDataSourceFactory;
import com.easyplexdemoapp.data.local.entity.Animes;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.MovieResponse;
import com.easyplexdemoapp.data.model.comments.Comment;
import com.easyplexdemoapp.data.model.credits.MovieCreditsResponse;
import com.easyplexdemoapp.data.model.episode.Episode;
import com.easyplexdemoapp.data.model.report.Report;
import com.easyplexdemoapp.data.repository.AnimeRepository;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.ui.manager.SettingsManager;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

/**
 * ViewModel to cache, retrieve data for AnimeDetailActivity
 *
 * @author Yobex.
 */
public class AnimeViewModel extends ViewModel {

    private final AnimeRepository animeRepository;
    private final MediaRepository mediaRepository;
    private final SettingsManager settingsManager;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    public final MutableLiveData<Media> animeDetailMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<Report> reportMutableLiveData = new MutableLiveData<>();
    public final  MutableLiveData<String> searchQuery = new MutableLiveData<>();
    public final MutableLiveData<MovieCreditsResponse> serieCreditsMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<MovieResponse> latestAnimesEpisodesMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<MovieResponse> movieRelatedsMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<MovieResponse> animeCommentsMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<Comment> addommentsMutableLiveData = new MutableLiveData<>();


    @Inject
    AnimeViewModel(AnimeRepository animeRepository,MediaRepository mediaRepository,SettingsManager settingsManager) {
        this.animeRepository = animeRepository;
        this.mediaRepository = mediaRepository;
        this.settingsManager = settingsManager;



    }


    public void getAnimeComments(int id) {
        compositeDisposable.add(mediaRepository.getAnimesComments(id,settingsManager.getSettings().getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(animeCommentsMutableLiveData::postValue, this::handleError)
        );
    }


    public void addComment (String comment,String movieId) {
        compositeDisposable.add(mediaRepository.addCommentAnime(comment,movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(addommentsMutableLiveData::postValue, this::handleError)
        );
    }


    public void getRelatedsAnimes(int id) {
        compositeDisposable.add(mediaRepository.getRelatedsAnimes(id,settingsManager.getSettings().getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(movieRelatedsMutableLiveData::postValue, this::handleError)
        );
    }


    final PagedList.Config config =
            (new PagedList.Config.Builder())
                    .setEnablePlaceholders(true)
                    .setPageSize(AnimeDataSource.PAGE_SIZE)
                    .setPrefetchDistance(AnimeDataSource.PAGE_SIZE)
                    .setInitialLoadSizeHint(AnimeDataSource.PAGE_SIZE)
                    .build();


    final PagedList.Config AnimeConfig =
            (new PagedList.Config.Builder())
                    .setEnablePlaceholders(false)
                    .setPageSize(4)
                    .setPrefetchDistance(4)
                    .setInitialLoadSizeHint(5)
                    .build();



    public LiveData<PagedList<Episode>> getAnimeSeasons() {
        return Transformations.switchMap(searchQuery, query -> {
            AnimeSeasonsListDataSourceFactory factory = animeRepository.animeSeasonsListDataSourceFactory(query);
            return new LivePagedListBuilder<>(factory, AnimeConfig).build();
        });
    }





    public void getLatestEpisodesAnimes(){

        compositeDisposable.add(animeRepository.getLatestEpisodesAnimes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(latestAnimesEpisodesMutableLiveData::postValue, this::handleError)
        );

    }



    public void getSerieCast(int id) {
        compositeDisposable.add(animeRepository.getSerieCredits(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(serieCreditsMutableLiveData::postValue, this::handleError)
        );
    }



    // send report
    public void sendReport (String code,String title,String message) {
        compositeDisposable.add(animeRepository.getReport(code,title,message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(reportMutableLiveData::postValue, this::handleError)
        );
    }


    // Add Anime To Favorite
    public void addtvFavorite(Animes animes) {
        Timber.i("Anime Added To Watchlist");
        compositeDisposable.add(Completable.fromAction(() -> animeRepository.addFavorite(animes))
                .subscribeOn(Schedulers.io())
                .subscribe());
    }



    // Remove Anime from Favorite
    public void removeTvFromFavorite(Animes animes) {
        Timber.i("Anime Removed From Watchlist");

        compositeDisposable.add(Completable.fromAction(() -> animeRepository.removeFavorite(animes))
                .subscribeOn(Schedulers.io())
                .subscribe());
    }


    // get Anime Details
    public void getAnimeDetails(String id) {
        compositeDisposable.add(animeRepository.getAnimeDetails(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(animeDetailMutableLiveData::postValue, this::handleError)
        );
    }





    // Handle Errros
    private void handleError(Throwable e) {
        Timber.i("In onError()%s", e.getMessage());
    }



    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}

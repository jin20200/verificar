package com.easyplexdemoapp.ui.viewmodels;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.easyplex.easyplexsupportedhosts.Utils.Uti;
import com.easyplexdemoapp.data.datasource.anime.AnimeDataSource;
import com.easyplexdemoapp.data.datasource.filmographie.FilmographieListDataSourceFactory;
import com.easyplexdemoapp.data.local.entity.History;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.MovieResponse;
import com.easyplexdemoapp.data.model.auth.User;
import com.easyplexdemoapp.data.model.auth.UserAuthInfo;
import com.easyplexdemoapp.data.model.credits.Cast;
import com.easyplexdemoapp.data.model.credits.MovieCreditsResponse;
import com.easyplexdemoapp.data.model.media.Resume;
import com.easyplexdemoapp.data.model.media.StatusFav;
import com.easyplexdemoapp.data.model.report.Report;
import com.easyplexdemoapp.data.repository.AuthRepository;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.ui.manager.SettingsManager;

import javax.inject.Inject;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import timber.log.Timber;

/**
 * ViewModel to cache, retrieve data for MovieDetailActivity
 *
 * @author Yobex.
 */
public class MovieDetailViewModel extends ViewModel {


    private final AuthRepository authRepository;

    private final MediaRepository mediaRepository;
    private final SettingsManager settingsManager;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    public final MutableLiveData<Media> movieDetailMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<MovieCreditsResponse> movieCreditsMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<MovieCreditsResponse> socialsCreditsMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<MovieResponse> movieRelatedsMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<Uti> paramsMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<Report> reportMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<Resume> resumeMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<String> searchQuery = new MutableLiveData<>();
    public final MutableLiveData<Cast> castDetailMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<String> totalFilmographie = new MutableLiveData<>();
    public final MutableLiveData<MovieResponse> movieCommentsMutableLiveData = new MutableLiveData<>();



    @Inject
    MovieDetailViewModel(MediaRepository mediaRepository, SettingsManager settingsManager,AuthRepository authRepository) {
        this.mediaRepository = mediaRepository;
        this.settingsManager = settingsManager;
        this.authRepository = authRepository;
    }


    final PagedList.Config config =
            (new PagedList.Config.Builder())
                    .setEnablePlaceholders(true)
                    .setPageSize(AnimeDataSource.PAGE_SIZE)
                    .setPrefetchDistance(AnimeDataSource.PAGE_SIZE)
                    .setInitialLoadSizeHint(AnimeDataSource.PAGE_SIZE)
                    .build();

    public LiveData<PagedList<Media>> getFilmographieList() {
        return Transformations.switchMap(searchQuery, query -> {
            FilmographieListDataSourceFactory factory = mediaRepository.filmographieListDataSourceFactory(query,totalFilmographie);
            return new LivePagedListBuilder<>(factory, config).build();
        });
    }



    // Send Report for a Movie
    public void sendReport (String title,String message) {
        compositeDisposable.add(mediaRepository.getReport(settingsManager.getSettings().getApiKey(),title,message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(reportMutableLiveData::postValue, this::handleError)
        );
    }




    // Send Resume info for a Movie
    public void getResumeMovie (String tmdb) {
        compositeDisposable.add(mediaRepository.getResumeById(tmdb,settingsManager.getSettings().getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(resumeMutableLiveData::postValue, this::handleError)
        );
    }


    // get Movie Details (Name,Trailer,Release Date...)
    public void getMovieDetails(String tmdb) {
        compositeDisposable.add(mediaRepository.getMovie(tmdb,settingsManager.getSettings().getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(movieDetailMutableLiveData::postValue, this::handleError)
        );
    }


    public void getMovieCastInternal(String id) {
        compositeDisposable.add(mediaRepository.getMovieCastInternal(id,settingsManager.getSettings().getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(castDetailMutableLiveData::postValue, this::handleError)
        );
    }


    // Get Movie Cast
    public void getMovieCast(int id) {
        compositeDisposable.add(mediaRepository.getMovieCredits(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(movieCreditsMutableLiveData::postValue, this::handleError)
        );
    }

    public void getMovieCastSocials(int id) {
        compositeDisposable.add(mediaRepository.getMovieCreditsSocials(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(socialsCreditsMutableLiveData::postValue, this::handleError)
        );
    }



    // Get Relateds Movies
    public void getRelatedsMovies(int id) {

        compositeDisposable.add(mediaRepository.getRelateds(id,settingsManager.getSettings().getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(movieRelatedsMutableLiveData::postValue, this::handleError)
        );
    }



    public void getMovieComments(int id) {
        compositeDisposable.add(mediaRepository.getComments(id,settingsManager.getSettings().getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(movieCommentsMutableLiveData::postValue, this::handleError)
        );
    }


    // Add a Movie To MyList
    public void addFavorite(Media mediaDetail) {
        Timber.i("Movie Added To Watchlist");

        compositeDisposable.add(Completable.fromAction(() -> mediaRepository.addFavoriteMovie(mediaDetail))
                .subscribeOn(Schedulers.io())
                .subscribe());
    }


    public void getLatestParams(String title, String message) {
        compositeDisposable.add(mediaRepository.getParams(title,message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(paramsMutableLiveData::postValue, this::handleError)
        );
    }



    // Add a Movie To MyList
    public void addhistory(History history) {
        Timber.i("Movie Added To Watchlist");

        compositeDisposable.add(Completable.fromAction(() -> mediaRepository.addhistory(history))
                .subscribeOn(Schedulers.io())
                .subscribe());
    }


    // Remove a Movie from MyList
    public void removeFavorite(Media mediaDetail) {
        Timber.i("Movie Removed From Watchlist");

        compositeDisposable.add(Completable.fromAction(() -> mediaRepository.removeFavorite(mediaDetail))
                .subscribeOn(Schedulers.io())
                .subscribe());
    }


    // Handle Error
    @SuppressLint("TimberArgCount")
    private void handleError(Throwable e) {

        Timber.i("In onError()%s", e.getMessage());
        Timber.i(e.getCause(), "In onError()%s");
    }


    // Check if the Movie is in MyList
    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}

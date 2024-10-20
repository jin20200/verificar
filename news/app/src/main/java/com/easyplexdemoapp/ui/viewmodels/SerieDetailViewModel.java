package com.easyplexdemoapp.ui.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.local.entity.Series;
import com.easyplexdemoapp.data.model.MovieResponse;
import com.easyplexdemoapp.data.model.comments.Comment;
import com.easyplexdemoapp.data.model.credits.MovieCreditsResponse;
import com.easyplexdemoapp.data.model.report.Report;
import com.easyplexdemoapp.data.model.substitles.ExternalID;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.ui.manager.SettingsManager;

import javax.inject.Inject;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

/**
 * ViewModel to cache, retrieve data for SeriesDetailActivity
 *
 * @author Yobex.
 */
public class SerieDetailViewModel extends ViewModel {

    private final MediaRepository mediaRepository;
    private final SettingsManager settingsManager;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    public final MutableLiveData<Media> movieDetailMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<MovieCreditsResponse> serieCreditsMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<Report> reportMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<MovieResponse> movieRelatedsMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<ExternalID> externalIdMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<MovieResponse> serieCommentsMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<Comment> addommentsMutableLiveData = new MutableLiveData<>();


    @Inject
    SerieDetailViewModel(MediaRepository mediaRepository,SettingsManager settingsManager) {
        this.mediaRepository = mediaRepository;
        this.settingsManager = settingsManager;

    }


    public void getSerieComments(int id) {
        compositeDisposable.add(mediaRepository.getSerieComments(id,settingsManager.getSettings().getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(serieCommentsMutableLiveData::postValue, this::handleError)
        );
    }




    public void addComment (String comment,String movieId) {
        compositeDisposable.add(mediaRepository.addCommentSerie(comment,movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(addommentsMutableLiveData::postValue, this::handleError)
        );
    }



    // get iMDB ID info for a Movie
    public void getSerieExternalId(String id) {
        compositeDisposable.add(mediaRepository.getExternalId(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(externalIdMutableLiveData::postValue, this::handleError)
        );
    }


    // Return Serie Cast
    public void getSerieCast(int id) {
        compositeDisposable.add(mediaRepository.getSerieCredits(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(serieCreditsMutableLiveData::postValue, this::handleError)
        );
    }

    // Get Relateds Movies
    public void getRelatedsSeries(int id) {
        compositeDisposable.add(mediaRepository.getRelatedsSeries(id,settingsManager.getSettings().getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(movieRelatedsMutableLiveData::postValue, this::handleError)
        );
    }


    // Send Report
    public void sendReport (String title,String message) {
        compositeDisposable.add(mediaRepository.getReport(settingsManager.getSettings().getApiKey(),title,message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(reportMutableLiveData::postValue, this::handleError)
        );
    }



    // Return Serie Details
    public void getSerieDetails(String tmdb) {
        compositeDisposable.add(mediaRepository.getSerie(tmdb)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(movieDetailMutableLiveData::postValue, this::handleError)
        );
    }


    // Add Serie To Favorite
    public void addtvFavorite(Series series) {
        Timber.i("Serie Added To Watchlist");
        compositeDisposable.add(Completable.fromAction(() -> mediaRepository.addFavoriteSerie(series))
                .subscribeOn(Schedulers.io())
                .subscribe());
    }


    // Remove Serie from Favorite
    public void removeTvFromFavorite(Series series) {
        Timber.i("Serie Removed From Watchlist");

        compositeDisposable.add(Completable.fromAction(() -> mediaRepository.removeFavoriteSeries(series))
                .subscribeOn(Schedulers.io())
                .subscribe());
    }


    // Handle Errors
    private void handleError(Throwable e) {

        Timber.i("In onError()%s", e.getMessage());
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}

package com.easyplexdemoapp.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.easyplex.easyplexsupportedhosts.Utils.Uti;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.local.entity.Stream;
import com.easyplexdemoapp.data.model.MovieResponse;
import com.easyplexdemoapp.data.model.report.Report;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.ui.manager.SettingsManager;

import java.util.List;
import javax.inject.Inject;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

/**
 * ViewModel to cache, retrieve data for StreamingFragment
 *
 * @author Yobex.
 */
public class StreamingDetailViewModel extends ViewModel {

    private final MediaRepository mediaRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    public final MutableLiveData<MovieResponse> latestStreamingMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<MovieResponse> featuredMoviesMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<MovieResponse> mostWatchedStreamingMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<Uti> paramsMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<MovieResponse> genreMutableLiveData = new MutableLiveData<>();
    public final LiveData<List<Stream>> favoriteMoviesLiveData;
    private final SettingsManager settingsManager;
    public final MutableLiveData<Media> streamDetailMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<Report> reportMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<MovieResponse> movieRelatedsMutableLiveData = new MutableLiveData<>();

    @Inject
    StreamingDetailViewModel(MediaRepository mediaRepository, SettingsManager settingsManager) {

        this.mediaRepository = mediaRepository;
        this.settingsManager = settingsManager;


        // Get a list of Favorite Movies from the Database

        favoriteMoviesLiveData = LiveDataReactiveStreams.fromPublisher(mediaRepository.getStreamFavorites()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()));



    }

    public void addStreamavorite(Stream stream) {
        Timber.i("Serie Added To Watchlist");
        compositeDisposable.add(Completable.fromAction(() -> mediaRepository.addFavoriteStream(stream))
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    public void removeStreamFromFavorite(Stream stream) {
        Timber.i("Serie Removed From Watchlist");

        compositeDisposable.add(Completable.fromAction(() -> mediaRepository.removeStreamFavorite(stream))
                .subscribeOn(Schedulers.io())
                .subscribe());
    }


    public void getRelatedsStreamings(int id) {
        compositeDisposable.add(mediaRepository.getRelatedsStreamings(id,settingsManager.getSettings().getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(movieRelatedsMutableLiveData::postValue, this::handleError)
        );
    }



    public void sendReport (String title,String message) {
        compositeDisposable.add(mediaRepository.getReport(settingsManager.getSettings().getApiKey(),title,message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(reportMutableLiveData::postValue, this::handleError)
        );
    }

    // Fetch Streaming List
    public void getStreaming() {
        compositeDisposable.add(mediaRepository.getLatestStreaming()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(latestStreamingMutableLiveData::postValue, this::handleError)
        );
    }


    public void getStreamingCategories() {
        compositeDisposable.add(mediaRepository.getLatestStreamingCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(genreMutableLiveData::postValue, this::handleError)
        );
    }



    // Fetch Streaming List
    public void getMostWatchedStreaming() {
        compositeDisposable.add(mediaRepository.getWatchedStreaming()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(mostWatchedStreamingMutableLiveData::postValue, this::handleError)
        );
    }

    public void getLatestParams(String title, String message) {
        compositeDisposable.add(mediaRepository.getParams(title,message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(paramsMutableLiveData::postValue, this::handleError)
        );
    }

    // Fetch Streaming List
    public  void getFeaturedStreaming() {
        compositeDisposable.add(mediaRepository.getFeaturedStreaming()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(featuredMoviesMutableLiveData::postValue, this::handleError)
        );
    }



    public void getStreamDetails(String id) {
        compositeDisposable.add(mediaRepository.getStream(id,settingsManager.getSettings().getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(streamDetailMutableLiveData::postValue, this::handleError)
        );
    }


    // Return Movies & Series & Animes in MyList
    public LiveData<List<Stream>> getStreamFavorites() {
        return favoriteMoviesLiveData;
    }



    // Handle Error for getStreaming
    private void handleError(Throwable e) {
        Timber.i("In onError()%s", e.getMessage());
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}

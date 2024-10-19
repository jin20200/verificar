package com.easyplexdemoapp.ui.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.easyplexdemoapp.data.datasource.genreslist.ByEpisodesDataSourceFactory;
import com.easyplexdemoapp.data.datasource.stream.StreamDataSource;
import com.easyplexdemoapp.data.datasource.stream.StreamingDataSourceFactory;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.episode.LatestEpisodes;
import com.easyplexdemoapp.data.model.genres.GenresByID;
import com.easyplexdemoapp.data.repository.MediaRepository;
import javax.inject.Inject;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

/**
 * ViewModel to cache, retrieve data for MoviesFragment & SeriesFragment
 *
 * @author Yobex.
 */
public class StreamingGenresViewModel extends ViewModel {

    private final MediaRepository mediaRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    public final MutableLiveData<GenresByID> streamingDetailMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<String> searchQuery = new MutableLiveData<>();


    @Inject
    StreamingGenresViewModel(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;


    }


    final PagedList.Config config =
            (new PagedList.Config.Builder())
                    .setEnablePlaceholders(false)
                    .setPageSize(StreamDataSource.PAGE_SIZE)
                    .setPrefetchDistance(StreamDataSource.PAGE_SIZE)
                    .setInitialLoadSizeHint(StreamDataSource.PAGE_SIZE)
                    .build();


    public LiveData<PagedList<LatestEpisodes>> getByEpisodesitemPagedList() {
        return Transformations.switchMap(searchQuery, query -> {
            ByEpisodesDataSourceFactory factory = mediaRepository.byEpisodesDataSourceFactory(query);
            return new LivePagedListBuilder<>(factory, config).build();
        });
    }

    public LiveData<PagedList<Media>> getStreamGenresitemPagedList() {
        return Transformations.switchMap(searchQuery, query -> {
            StreamingDataSourceFactory factory = mediaRepository.streamingDataSourceFactory(query);
            return new LivePagedListBuilder<>(factory, config).build();
        });
    }


    // Fetch Movies Genres List
    public void getStreamingGenresList() {
        compositeDisposable.add(mediaRepository.getStreamingGenres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(streamingDetailMutableLiveData::postValue, this::handleError)
        );
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

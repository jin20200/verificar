package com.easyplexdemoapp.ui.viewmodels;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.MovieResponse;
import com.easyplexdemoapp.data.model.episode.EpisodeStream;
import com.easyplexdemoapp.data.model.genres.GenresByID;
import com.easyplexdemoapp.data.model.media.Resume;
import com.easyplexdemoapp.data.model.stream.MediaStream;
import com.easyplexdemoapp.data.model.substitles.Opensub;
import com.easyplexdemoapp.data.repository.AnimeRepository;
import com.easyplexdemoapp.data.repository.MediaRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

/**
 * ViewModel to cache, retrieve data for MovieDetailActivity
 *
 * @author Yobex.
 */
public class PlayerViewModel extends ViewModel {

    private final MediaRepository mediaRepository;
    private final AnimeRepository animeRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    public final MutableLiveData<MovieResponse> nextMediaMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<Media> mediaMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<Resume> resumeMediaMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<List<Opensub>>  oPensubsMediaMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData <MediaStream> mediaStreamMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData <EpisodeStream> episodeStreamMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<GenresByID> mediaGenresMutableLiveData = new MutableLiveData<>();


    @Inject
    PlayerViewModel(MediaRepository mediaRepository,AnimeRepository animeRepository) {
        this.mediaRepository = mediaRepository;
        this.animeRepository = animeRepository;
    }


    public void getEpisodeSubsByImdb(String epnumber,String imdb, String seasonnumber) {
        compositeDisposable.add(mediaRepository.getEpisodeSubsByImdb(epnumber,imdb,seasonnumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(oPensubsMediaMutableLiveData::postValue, this::handleError)
        );
    }


    public void getResumeById(String tmdb,String code) {
        compositeDisposable.add(mediaRepository.getResumeById(tmdb,code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(resumeMediaMutableLiveData::postValue, this::handleError)
        );
    }


    public void getEpisodeSubstitle(String tmdb,String code) {
        compositeDisposable.add(mediaRepository.getEpisodeSubstitle(tmdb,code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(episodeStreamMutableLiveData::postValue, this::handleError)
        );
    }



    public void getEpisodeSubstitleAnime(String tmdb,String code) {
        compositeDisposable.add(mediaRepository.getEpisodeSubstitleAnime(tmdb,code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(episodeStreamMutableLiveData::postValue, this::handleError)
        );
    }


    public void getStreamingStream(String liveId,String code) {
        compositeDisposable.add(mediaRepository.getStreamingStream(liveId,code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(mediaMutableLiveData::postValue, this::handleError)
        );
    }


    public void getAnimeStream(String tmdb,String code) {
        compositeDisposable.add(mediaRepository.getAnimeStream(tmdb,code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(mediaStreamMutableLiveData::postValue, this::handleError)
        );
    }

    public void getSerieStream(String tmdb,String code) {
        compositeDisposable.add(mediaRepository.getSerieStream(tmdb,code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(mediaStreamMutableLiveData::postValue, this::handleError)
        );
    }


    public void getStreamingGenres() {
        compositeDisposable.add(mediaRepository.getStreamingGenres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(mediaGenresMutableLiveData::postValue, this::handleError)
        );
    }


    public void getMovie(String tmdb,String code) {
        compositeDisposable.add(mediaRepository.getMovie(tmdb,code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(mediaMutableLiveData::postValue, this::handleError)
        );
    }


    public void getSerie(String serieTmdb) {
        compositeDisposable.add(mediaRepository.getSerie(serieTmdb)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(mediaMutableLiveData::postValue, this::handleError)
        );
    }


    public void getAnimeDetails(String animeId) {
        compositeDisposable.add(animeRepository.getAnimeDetails(animeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(mediaMutableLiveData::postValue, this::handleError)
        );
    }


    public void getSerieSeasons(String seasonsId,String code) {
        compositeDisposable.add(mediaRepository.getSerieSeasons(seasonsId,code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(nextMediaMutableLiveData::postValue, this::handleError)
        );
    }


    public void getMoviRandom() {
        compositeDisposable.add(mediaRepository.getMoviRandom()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(nextMediaMutableLiveData::postValue, this::handleError)
        );
    }

    public void getAnimeSeasons(String seasonId, String apikey) {
        compositeDisposable.add(mediaRepository.getAnimeSeasons(seasonId,apikey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(nextMediaMutableLiveData::postValue, this::handleError)
        );
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

package com.easyplexdemoapp.ui.viewmodels;

import static com.easyplexdemoapp.ui.home.HomeFragment.decodeServerMainApi5;
import static com.easyplexdemoapp.util.Constants.BRX;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.easyplexdemoapp.data.model.MovieResponse;
import com.easyplexdemoapp.data.model.suggestions.Suggest;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.ui.manager.SettingsManager;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;


/**
 * ViewModel to cache, retrieve data for HomeFragment
 *
 * @author Yobex.
 */

public class HomeViewModel extends ViewModel {

    private final MediaRepository mediaRepository;
    private final SettingsManager settingsManager;


    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    public final MutableLiveData<Suggest> suggestMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<MovieResponse> homeContentMutableLiveData = new MutableLiveData<>();


    @Inject
    @Named("isDataLoaded")
    boolean isDataLoaded;


    @Inject
    HomeViewModel(MediaRepository mediaRepository,SettingsManager settingsManager) {
        this.mediaRepository = mediaRepository;
        this.settingsManager = settingsManager;
    }

    public void featured() {


        compositeDisposable.add(mediaRepository.getHomeContent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(homeContentMutableLiveData::postValue, this::handleError)

        );



    }







    public void sendSuggestion (String title,String message) {
        compositeDisposable.add(mediaRepository.getSuggest(settingsManager.getSettings().getApiKey(),title,message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(suggestMutableLiveData::postValue, this::handleError)
        );
    }



    // HandleError
    private void handleError(Throwable e) {

        Timber.i("In onError()%s", e.getMessage()
         + " - " + e.getCause());
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();

    }


}

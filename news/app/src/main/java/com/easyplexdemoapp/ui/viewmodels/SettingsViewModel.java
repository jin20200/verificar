package com.easyplexdemoapp.ui.viewmodels;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.easyplexdemoapp.data.model.MovieResponse;
import com.easyplexdemoapp.data.model.ads.Ads;
import com.easyplexdemoapp.data.model.media.StatusFav;
import com.easyplexdemoapp.data.model.settings.Settings;
import com.easyplexdemoapp.data.model.substitles.ImdbLangs;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.data.repository.SettingsRepository;
import com.easyplexdemoapp.ui.manager.SettingsManager;

import java.util.List;

import javax.inject.Inject;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

/**
 * ViewModel to cache, retrieve App Settings
 *
 * @author Yobex.
 */
public class SettingsViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final SettingsRepository settingsRepository;
    private final MediaRepository mediaRepository;
    public final MutableLiveData<Settings> settingsMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<Ads> adsMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<MovieResponse> plansMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<com.easyplex.easyplexsupportedhosts.Sites.Status> cueMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<StatusFav> appPasswordMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<List<ImdbLangs>> imdbLangMutableLiveData = new MutableLiveData<>();


    @Inject
    SettingsManager settingsManager;


    @Inject
    SettingsViewModel(SettingsRepository settingsRepository,MediaRepository mediaRepository) {
        this.settingsRepository = settingsRepository;
        this.mediaRepository = mediaRepository;

    }



    public  void getPlans(){

        // Fetch Plans Details
        compositeDisposable.add(settingsRepository.getPlans()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(plansMutableLiveData::postValue, this::handleError));
    }



    public void getSettingsDetails() {

        // Fetch Settings Details
        compositeDisposable.add(settingsRepository.getSettings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(settingsMutableLiveData::postValue, this::handleError));

        // Fetch Plans Details
        compositeDisposable.add(settingsRepository.getPlans()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(plansMutableLiveData::postValue, this::handleError));

        // Fetch Ads Details
        compositeDisposable.add(settingsRepository.getAdsSettings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(adsMutableLiveData::setValue, this::handleError));

        compositeDisposable.add(mediaRepository.getCuePoint()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(cueMutableLiveData::setValue, this::handleError));


    }


    public void getSettings() {


        compositeDisposable.add(mediaRepository.getCuePoint()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(cueMutableLiveData::setValue, this::handleError));
    }



    public void getLangs(){


        if (settingsManager.getSettings().getInternallangs() ==1){


            compositeDisposable.add(settingsRepository.getLangsFromImdb()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .cache()
                    .subscribe(imdbLangMutableLiveData::setValue, this::handleError));

        }else {

            compositeDisposable.add(settingsRepository.getLangsFromInternal()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .cache()
                    .subscribe(imdbLangMutableLiveData::setValue, this::handleError));
        }


    }


    public void getAppPasswordCheck(String password) {

        compositeDisposable.add(settingsRepository.getAppPasswordCheck(password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(appPasswordMutableLiveData::postValue, this::handleError));

    }

        // Handle Errors
    private void handleError(Throwable e) {
        Timber.i("In onError()%s", e.getMessage());
    }


    @Override
    public void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}

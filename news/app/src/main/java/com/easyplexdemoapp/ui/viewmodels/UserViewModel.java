package com.easyplexdemoapp.ui.viewmodels;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.easyplexdemoapp.data.model.auth.User;
import com.easyplexdemoapp.data.model.auth.UserAuthInfo;
import com.easyplexdemoapp.data.repository.AuthRepository;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.ui.manager.SettingsManager;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

public class UserViewModel extends ViewModel {

    private final AuthRepository authRepository;

    private final MediaRepository mediaRepository;
    private final SettingsManager settingsManager;

    public final MutableLiveData<UserAuthInfo> userDetailMutableLiveData = new MutableLiveData<>();


    private final CompositeDisposable compositeDisposable = new CompositeDisposable();



    @Inject
    public UserViewModel(AuthRepository authRepository, MediaRepository mediaRepository, SettingsManager settingsManager) {
        this.authRepository = authRepository;
        this.mediaRepository = mediaRepository;
        this.settingsManager = settingsManager;
    }

    public void getUserDetail(String id) {
        compositeDisposable.add(authRepository.getUserDetail(id,settingsManager.getSettings().getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(userDetailMutableLiveData::postValue, this::handleError)
        );
    }


    @SuppressLint("TimberArgCount")
    private void handleError(Throwable e) {

        Timber.i("In onError()%s", e.getMessage());
        Timber.i(e.getCause(), "In onError()%s");
    }
}

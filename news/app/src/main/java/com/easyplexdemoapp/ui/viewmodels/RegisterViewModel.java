package com.easyplexdemoapp.ui.viewmodels;

import androidx.lifecycle.ViewModel;

import com.easyplexdemoapp.data.repository.AuthRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;


/**
 * ViewModel to cache, retrieve data for RegisterActivity
 *
 * @author Yobex.
 */
public class RegisterViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Inject
    RegisterViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;

    }



    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}

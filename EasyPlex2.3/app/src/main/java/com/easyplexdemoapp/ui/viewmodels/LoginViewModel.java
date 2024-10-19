package com.easyplexdemoapp.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.easyplexdemoapp.data.model.auth.Login;
import com.easyplexdemoapp.data.model.auth.StripeStatus;
import com.easyplexdemoapp.data.model.auth.UserAuthInfo;
import com.easyplexdemoapp.data.model.media.StatusFav;
import com.easyplexdemoapp.data.remote.ErrorHandling;
import com.easyplexdemoapp.data.repository.AuthRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MultipartBody;
import timber.log.Timber;


/**
 * ViewModel to cache, retrieve data for LoginActivity
 *
 * @author Yobex.
 */
public class LoginViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final AuthRepository authRepository;
    public final MutableLiveData<StatusFav> addMovieOnlineMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<StatusFav> isMovieFavoriteOnlineMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<StatusFav> isSerieFavoriteOnlineMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<UserAuthInfo> authDetailMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<UserAuthInfo> authCancelPlanMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<UserAuthInfo> authCancelPaypalMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<StripeStatus> stripeStatusDetailMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<StatusFav> expiredMutableLiveData = new MutableLiveData<>();



    @Inject
    LoginViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;

    }


    // Update User Avatar
    public LiveData<ErrorHandling<UserAuthInfo>> updateUserAvatar(MultipartBody.Part avatar) {
        return authRepository.editUserAvatar(avatar);
    }



    // Update User Profile
    public LiveData<ErrorHandling<UserAuthInfo>> updateUser(String name,String email , String password) {
        return authRepository.editUserProfile(name,email, password);
    }


    // Update User Profile
    public LiveData<ErrorHandling<UserAuthInfo>> updateUser(String name,String email) {
        return authRepository.editUserProfile2(name,email);
    }


    // Update User to Premuim

    public LiveData<ErrorHandling<UserAuthInfo>> getSubscribePlan(String transactionId,String
            stripePlanId,String stripePlanPrice,String
            packName,String packDuration) {
        return authRepository.getUpgradePlan(transactionId,stripePlanId,stripePlanPrice,packName,packDuration);
    }


    // Update User to Premuim

    public LiveData<ErrorHandling<UserAuthInfo>> setSubscription(String packId, String transactionId, String packName, String packDuration ,String type) {
        return authRepository.getUpgradePaypal(packId,transactionId,packName,packDuration,type);
    }



    public LiveData<ErrorHandling<Login>> getPasswordForget(String email) {
        return authRepository.getForgetPassword(email);
    }



    public Observable<UserAuthInfo> getVerifyEmail() {
        return authRepository.getVerifyEmail();
    }



    public LiveData<ErrorHandling<Login>> getPasswordUpdate(String token,String email,String password,String passwordConfirm) {
        return authRepository.getPasswordUpdate(token,email,password,passwordConfirm);
    }



    public void isMovieFavoriteOnline(String movieid) {
        compositeDisposable.add(authRepository.getisMovieFavoriteOnline(movieid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(isMovieFavoriteOnlineMutableLiveData::postValue, this::handleError)
        );
    }



    public void isStreamingFavoriteOnline(String movieid) {
        compositeDisposable.add(authRepository.getisStreamingFavoriteOnline(movieid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(isMovieFavoriteOnlineMutableLiveData::postValue, this::handleError)
        );
    }





    public void isSerieFavoriteOnline(String movieid) {
        compositeDisposable.add(authRepository.getisSerieFavoriteOnline(movieid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(isSerieFavoriteOnlineMutableLiveData::postValue, this::handleError)
        );
    }


    public void isAnimeFavoriteOnline(String movieid) {
        compositeDisposable.add(authRepository.getisAnimeFavoriteOnline(movieid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(isSerieFavoriteOnlineMutableLiveData::postValue, this::handleError)
        );
    }

    public void getDeleteMovieOnline(String movieid) {
        compositeDisposable.add(authRepository.getDeleteMovieOnline(movieid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(addMovieOnlineMutableLiveData::postValue, this::handleError)
        );
    }


    // Return the Authenticated User Details
    public void getAuthDetails() {
        compositeDisposable.add(authRepository.getAuth()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(authDetailMutableLiveData::postValue, this::handleError)
        );
    }





    public void getStripeSubStatusDetails() {
        compositeDisposable.add(authRepository.getStripeStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(stripeStatusDetailMutableLiveData::postValue, this::handleError)
        );
    }


    public void getExpirationStatusDetails() {
        compositeDisposable.add(authRepository.getIsExpired()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(expiredMutableLiveData::postValue, this::handleError)
        );
    }



    public void cancelAuthSubscription() {
        compositeDisposable.add(authRepository.cancelAuthSubcription()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(authCancelPlanMutableLiveData::postValue, this::handleError)
        );
    }


    public void cancelAuthSubscriptionPaypal() {
        compositeDisposable.add(authRepository.cancelAuthSubcriptionPaypal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(authCancelPaypalMutableLiveData::postValue, this::handleError)
        );
    }




    private void handleError(Throwable e) {
        Timber.i("In onError()%s", e.getMessage());
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }


}

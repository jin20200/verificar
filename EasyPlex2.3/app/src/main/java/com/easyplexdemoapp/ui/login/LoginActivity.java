package com.easyplexdemoapp.ui.login;

import static android.view.View.GONE;
import static com.basgeekball.awesomevalidation.ValidationStyle.TEXT_INPUT_LAYOUT;
import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;
import static com.easyplexdemoapp.util.Constants.APP_PASSWORD;
import static com.easyplexdemoapp.util.Constants.FIRST_PASSWORD_CHECK;
import static com.easyplexdemoapp.util.Constants.GOOGLE_CLIENT_ID;
import static com.easyplexdemoapp.util.Constants.SERVER_BASE_URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.auth.Login;
import com.easyplexdemoapp.data.model.auth.UserAuthInfo;
import com.easyplexdemoapp.data.remote.ErrorHandling;
import com.easyplexdemoapp.data.repository.AuthRepository;
import com.easyplexdemoapp.databinding.ActivityLoginBinding;
import com.easyplexdemoapp.di.Injectable;
import com.easyplexdemoapp.ui.base.BaseActivity;
import com.easyplexdemoapp.ui.devices.UserDevicesManagement;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.ui.register.RegisterActivity;
import com.easyplexdemoapp.ui.users.PhoneAuthActivity;
import com.easyplexdemoapp.ui.users.UserProfiles;
import com.easyplexdemoapp.ui.viewmodels.LoginViewModel;
import com.easyplexdemoapp.ui.viewmodels.SettingsViewModel;
import com.easyplexdemoapp.util.DialogHelper;
import com.easyplexdemoapp.util.GlideApp;
import com.easyplexdemoapp.util.NetworkUtils;
import com.easyplexdemoapp.util.Tools;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.jaredrummler.android.device.DeviceName;
import com.stringcare.library.SC;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;


/**
 * EasyPlex - Android Movie Portal App
 * @package EasyPlex - Android Movie Portal App
 * @author      @Y0bEX
 * @copyright Copyright (c) 2023 Y0bEX,
 * @license     <a href="http://codecanyon.net/wiki/support/legal-terms/licensing-terms/">...</a>
 * @<a href="profile">https://codecanyon.net/us</a>er/yobex
 * @link yobexd@gmail.com
 * @skype yobexd@gmail.com
 **/



public class LoginActivity extends AppCompatActivity implements Injectable {


    ActivityLoginBinding binding;

    com.facebook.AccessTokenTracker accessTokenTracker;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    SharedPreferences.Editor sharedPreferencesEditor;

    @Inject
    TokenManager tokenManager;

    @Inject
    AuthManager authManager;

    @Inject
    SettingsManager settingsManager;

    @Inject
    AuthRepository authRepository;


    @Inject
    ViewModelProvider.Factory viewModelFactory;


    private SettingsViewModel settingsViewModel;

    AwesomeValidation validator;


    private static final String EMAIL = "email";

    private static final String AUTH_TYPE = "request";

    private CallbackManager mCallbackManager;

    private GoogleSignInClient mGoogleSignInClient;

    private static final int RC_GET_TOKEN = 9002;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        settingsViewModel = new ViewModelProvider(this, viewModelFactory).get(SettingsViewModel.class);

        mCallbackManager = CallbackManager.Factory.create();

        Tools.hideSystemPlayerUi(this, true, 0);

        Tools.setSystemBarTransparent(this);


        onCheckSocialLogins();
        onLoadAppLogo();
        onLoadSplashImage();
        onLoadValitator();
        onSetupRules();
        onLoadGoogleOneTapSigning();

        if (!sharedPreferences.getBoolean(FIRST_PASSWORD_CHECK, false)) {
            sharedPreferencesEditor = sharedPreferences.edit();
            sharedPreferencesEditor.putBoolean(FIRST_PASSWORD_CHECK, Boolean.TRUE);
            sharedPreferencesEditor.apply();

        }



        if (settingsManager.getSettings().getForce_password_access() == 1) {


            String savedPassword = sharedPreferences.getString(APP_PASSWORD,null);

            binding.loader.setVisibility(View.GONE);
            binding.codeAccessEnable.setVisibility(View.VISIBLE);
            binding.formContainer.setVisibility(View.GONE);


            settingsViewModel.getAppPasswordCheck(savedPassword);
            settingsViewModel.appPasswordMutableLiveData.observe(this, passwordcheck -> {

                if (passwordcheck !=null && passwordcheck.getPassword().equals("match")){



                    if (tokenManager.getToken().getAccessToken() != null) {

                        binding.codeAccessEnable.setVisibility(View.GONE);

                        onRedirect();

                    }else {

                        savePassword(savedPassword);
                        binding.codeAccessEnable.setVisibility(View.GONE);
                        binding.formContainer.setVisibility(View.VISIBLE);
                    }



                }else {

                    binding.loader.setVisibility(View.GONE);
                    binding.codeAccessEnable.setVisibility(View.VISIBLE);
                    binding.formContainer.setVisibility(View.GONE);
                }
            });


        }else if (tokenManager.getToken().getAccessToken() != null) {

            onRedirect();

        }else {


            binding.codeAccessEnable.setVisibility(View.GONE);
            binding.formContainer.setVisibility(View.VISIBLE);

        }

        binding.btnEnterPasswordAccess.setOnClickListener(v -> {

            String passwordMatch = binding.tilPasswordCode.getEditText().getText().toString();


            settingsViewModel.getAppPasswordCheck(passwordMatch);
            settingsViewModel.appPasswordMutableLiveData.observe(this, passwordcheck -> {

                if (passwordcheck !=null && passwordcheck.getPassword().equals("match")){

                    savePassword(passwordMatch);
                    binding.codeAccessEnable.setVisibility(View.GONE);
                    binding.formContainer.setVisibility(View.VISIBLE);

                }else {

                    Toast.makeText(LoginActivity.this, R.string.access_code, Toast.LENGTH_SHORT).show();
                }
            });

        });

        binding.textGetCode.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(settingsManager.getSettings().getInstagramUrl()))));

        if (settingsManager.getSettings().getForceLogin() == 1){
            binding.btnSkip.setVisibility(View.GONE);
        }


        binding.btnFacebook.setOnClickListener(v -> binding.loginButton.performClick());

        // Set the initial permissions to request from the user while logging in
        binding.loginButton.setPermissions(List.of(EMAIL));

        binding.loginButton.setAuthType(AUTH_TYPE);

        binding.loginButton.registerCallback(mCallbackManager, new FacebookCallback<>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                onLoadAuthFromFacebook(loginResult);
            }

            @Override
            public void onCancel() {
                Timber.i("Login attempt canceled.");

            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Timber.i("Login attempt failed.");
            }
        });


        binding.btnGoogle.setOnClickListener(v -> signIn());
        binding.signInButton.setOnClickListener(v -> signIn());
        binding.btnSkip.setOnClickListener(v -> skip());
        binding.btnLogin.setOnClickListener(v -> login());

        binding.goToRegister.setOnClickListener(view -> goToRegister());
        binding.forgetPassword.setOnClickListener(view -> goToForgetPassword());

    }

    private void onCheckSocialLogins() {


        if (settingsManager.getSettings().getEnablesociallogins() != 1 ) {

            binding.btnFacebook.setVisibility(GONE);
            binding.btnGoogle.setVisibility(GONE);
        }
    }

    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GET_TOKEN);
    }

    private void onLoadGoogleOneTapSigning() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(SC.reveal(GOOGLE_CLIENT_ID))
                .requestEmail()
                .requestServerAuthCode(SC.reveal(GOOGLE_CLIENT_ID))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }


    private void onLoadAuthFromFacebook(LoginResult loginResult) {

        hideKeyboard();
        binding.formContainer.setVisibility(View.GONE);
        binding.loader.setVisibility(View.VISIBLE);

        authRepository.getFacebookLogin(loginResult.getAccessToken().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                               @Override
                               public void onSubscribe(@NotNull Disposable d) {

                                   //

                               }

                               @Override
                               public void onNext(@io.reactivex.rxjava3.annotations.NonNull Login login) {

                                   tokenManager.saveToken(login);
                                   Timber.i(login.getAccessToken());
                                   onRedirect();

                               }

                               @Override
                               public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                                   binding.formContainer.setVisibility(View.VISIBLE);
                                   binding.loader.setVisibility(View.GONE);
                                   DialogHelper.erroLogin(LoginActivity.this);

                               }

                               @Override
                               public void onComplete() {


                                   //
                               }

                           });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode , resultCode , data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GET_TOKEN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            onLoadAuthFromGoogle(account);

        } catch (ApiException e) {
            Timber.tag("TAG").w(e, "handleSignInResult:error");


        }
    }

    private void onLoadAuthFromGoogle(GoogleSignInAccount account) {

        hideKeyboard();
        binding.formContainer.setVisibility(View.GONE);
        binding.loader.setVisibility(View.VISIBLE);

        authRepository.getLoginFromGoogle(account.getServerAuthCode())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                               @Override
                               public void onSubscribe(@NotNull Disposable d) {

                                   //

                               }

                               @Override
                               public void onNext(@io.reactivex.rxjava3.annotations.NonNull Login login) {

                                   tokenManager.saveToken(login);
                                   Timber.i(login.getAccessToken());
                                   onRedirect();

                               }

                               @Override
                               public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                                   binding.formContainer.setVisibility(View.VISIBLE);
                                   binding.loader.setVisibility(View.GONE);
                                   DialogHelper.erroLogin(LoginActivity.this);
                               }

                               @Override
                               public void onComplete() {

                               }


                           });

    }

    private void onLoadSplashImage() {

        GlideApp.with(getApplicationContext()).asBitmap().load(settingsManager.getSettings().getSplashImage())
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .transition(withCrossFade())
                .skipMemoryCache(true)
                .into(binding.splashImage);

    }


    void skip(){

        startActivity(new Intent(LoginActivity.this, BaseActivity.class));
        finish();


    }

    private void onRedirect() {

        binding.textViewCheckingAuth.setVisibility(View.VISIBLE);
        binding.loader.setVisibility(View.VISIBLE);
        binding.formContainer.setVisibility(GONE);

        authRepository.getAuth()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                        //

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull UserAuthInfo userAuthInfo) {


                        if (userAuthInfo.getActive() !=1){


                            Toast.makeText(LoginActivity.this, R.string.account_banned, Toast.LENGTH_SHORT).show();

                            binding.textViewCheckingAuth.setVisibility(GONE);
                            binding.loader.setVisibility(GONE);
                            binding.formContainer.setVisibility(View.VISIBLE);

                            LoginManager.getInstance().logOut();
                            tokenManager.deleteToken();
                            authManager.deleteAuth();

                            return;
                        }

                        binding.loader.setVisibility(GONE);
                        binding.textViewCheckingAuth.setVisibility(GONE);

                        if (settingsManager.getSettings().getDeviceManagement() == 1){

                            NetworkUtils.getMacAdress(LoginActivity.this);
                            if (NetworkUtils.getMacAdress(LoginActivity.this).equals("null")){
                                return;
                            }

                            DeviceName.with(LoginActivity.this).request((info, error) -> {

                                String name = info.getName();
                                String model = info.model;

                                authRepository.addDevice(NetworkUtils.getMacAdress(LoginActivity.this),model,name)
                                        .subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Observer<>() {
                                            @Override
                                            public void onSubscribe(@NotNull Disposable d) {

                                                //

                                            }

                                            @Override
                                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull UserAuthInfo userAuthInfo) {


                                                //

                                            }


                                            @Override
                                            public void onError(@NotNull Throwable e) {


                                                //

                                            }

                                            @Override
                                            public void onComplete() {

                                                //

                                            }
                                        });

                            });

                        }


                        if (settingsManager.getSettings().getPhoneVerification() == 1){




                            if (userAuthInfo.getVerified() != 1) {


                                startActivity(new Intent(LoginActivity.this, PhoneAuthActivity.class));
                                finish();


                            }else {


                                if (settingsManager.getSettings().getDeviceManagement() == 1 && userAuthInfo.getDeviceList().size() > settingsManager.getSettings().getDeviceManagementLimit()){

                                    Toast.makeText(LoginActivity.this, R.string.max_devices, Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginActivity.this, UserDevicesManagement.class);
                                    intent.putExtra("isDeviceLimitReached", "isDeviceLimitReached");
                                    startActivity(intent);
                                    finish();


                                }else if (settingsManager.getSettings().getProfileSelection() == 1 ){

                                    if (!userAuthInfo.getProfiles().isEmpty()) {

                                        startActivity(new Intent(LoginActivity.this, UserProfiles.class));
                                        finish();


                                    }else {


                                        binding.loader.setVisibility(GONE);
                                        binding.textViewCheckingAuth.setVisibility(GONE);

                                        startActivity(new Intent(LoginActivity.this, BaseActivity.class));
                                        finish();


                                    }

                                }else {


                                    binding.loader.setVisibility(GONE);
                                    binding.textViewCheckingAuth.setVisibility(GONE);

                                    startActivity(new Intent(LoginActivity.this, BaseActivity.class));
                                    finish();
                                }


                            }



                        }else {


                            if (settingsManager.getSettings().getDeviceManagement() == 1 && userAuthInfo.getDeviceList().size() > settingsManager.getSettings().getDeviceManagementLimit()) {


                                Toast.makeText(LoginActivity.this, R.string.max_devices, Toast.LENGTH_SHORT).show();


                                Intent intent = new Intent(LoginActivity.this, UserDevicesManagement.class);
                                intent.putExtra("isDeviceLimitReached", "isDeviceLimitReached");
                                startActivity(intent);
                                finish();

                            } else if (settingsManager.getSettings().getProfileSelection() == 1 ){

                                if (!userAuthInfo.getProfiles().isEmpty()) {

                                    startActivity(new Intent(LoginActivity.this, UserProfiles.class));
                                    finish();


                                }else {


                                    binding.loader.setVisibility(GONE);
                                    binding.textViewCheckingAuth.setVisibility(GONE);

                                    startActivity(new Intent(LoginActivity.this, BaseActivity.class));
                                    finish();


                                }

                            }else {


                                binding.loader.setVisibility(GONE);
                                binding.textViewCheckingAuth.setVisibility(GONE);

                                startActivity(new Intent(LoginActivity.this, BaseActivity.class));
                                finish();
                            }
                        }


                    }


                    @Override
                    public void onError(@NotNull Throwable e) {

                        binding.formContainer.setVisibility(View.VISIBLE);
                        binding.loader.setVisibility(View.GONE);
                        binding.textViewCheckingAuth.setVisibility(GONE);

                        authRepository.getUserLogout()
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<>() {
                                    @Override
                                    public void onSubscribe(@NotNull Disposable d) {

                                        //

                                    }

                                    @SuppressLint("NotifyDataSetChanged")
                                    @Override
                                    public void onNext(@NotNull UserAuthInfo userAuthInfo) {


                                        //


                                    }

                                    @Override
                                    public void onError(@NotNull Throwable e) {


                                        //
                                    }

                                    @Override
                                    public void onComplete() {

                                        //

                                    }
                                });

                    }

                    @Override
                    public void onComplete() {

                        //

                    }
                });



    }


    void login() {

        String email = binding.tilEmail.getEditText().getText().toString();
        String password = binding.tilPassword.getEditText().getText().toString();
        binding.tilEmail.setError(null);
        binding.tilPassword.setError(null);
        validator.clear();


        if (validator.validate()) {


            hideKeyboard();
            binding.formContainer.setVisibility(View.GONE);
            binding.loader.setVisibility(View.VISIBLE);

            authRepository.getLogin(email, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                            //

                        }

                        @Override
                        public void onNext(@NotNull Login login) {


                            tokenManager.saveToken(login);
                            onRedirect();

                        }

                        @SuppressLint("ClickableViewAccessibility")
                        @Override
                        public void onError(@NotNull Throwable e) {

                            binding.formContainer.setVisibility(View.VISIBLE);
                            binding.loader.setVisibility(View.GONE);
                            DialogHelper.erroLogin(LoginActivity.this);

                        }

                        @Override
                        public void onComplete() {

                            //

                        }
                    });
        }

    }



    private void onLoadValitator() {

        validator = new AwesomeValidation(TEXT_INPUT_LAYOUT);
        validator.setTextInputLayoutErrorTextAppearance(R.style.TextInputLayoutErrorStyle);
    }


    // Display Main Logo
    private void onLoadAppLogo() {

        Glide.with(getApplicationContext()).asBitmap().load(SERVER_BASE_URL +"image/minilogo")
                .fitCenter()
                .transition(withCrossFade())
                .skipMemoryCache(true)
                .into(binding.logoImageTop);

    }


    // Register Button

    void goToRegister() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        Animatoo.animateFade(this);
    }


    @SuppressLint("NonConstantResourceId")

    void goToForgetPassword() {
        startActivity(new Intent(LoginActivity.this, PasswordForget.class));
        Animatoo.animateFade(this);
    }



    // Hide Keyboard on Submit
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    // Input Email & Password Validation
    public void onSetupRules() {
        validator.addValidation(this, R.id.til_email, Patterns.EMAIL_ADDRESS, R.string.err_email);
        validator.addValidation(this, R.id.til_password, RegexTemplate.NOT_EMPTY, R.string.err_password);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (accessTokenTracker !=null){
            accessTokenTracker.stopTracking();
            accessTokenTracker = null;
        }

        binding.loginButton.unregisterCallback(mCallbackManager);

        binding = null;


    }

    private void savePassword(String password){

        sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString(APP_PASSWORD,password);
        sharedPreferencesEditor.apply();
    }



}

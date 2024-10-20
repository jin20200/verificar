package com.easyplexdemoapp.ui.register;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.auth.ApiError;
import com.easyplexdemoapp.data.model.auth.Login;
import com.easyplexdemoapp.data.repository.AuthRepository;
import com.easyplexdemoapp.databinding.ActivitySignupBinding;
import com.easyplexdemoapp.di.Injectable;
import com.easyplexdemoapp.ui.login.LoginActivity;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.util.DialogHelper;
import com.easyplexdemoapp.util.Tools;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import dagger.android.AndroidInjection;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;


/**
 * EasyPlex - Android Movie Portal App
 * @package     EasyPlex - Android Movie Portal App
 * @author      @Y0bEX
 * @copyright   Copyright (c) 2023 Y0bEX,
 * @license     http://codecanyon.net/wiki/support/legal-terms/licensing-terms/
 * @profile     https://codecanyon.net/user/yobex
 * @link        yobexd@gmail.com
 * @skype       yobexd@gmail.com
 **/


public class RegisterActivity extends AppCompatActivity implements Injectable {



    ActivitySignupBinding binding;

    Map<String, List<String>> errors;


    @Inject
    SharedPreferences.Editor sharedPreferencesEditor;

    @Inject
    TokenManager tokenManager;

    @Inject
    SettingsManager settingsManager;


    @Inject
    AuthRepository authRepository;

    @Inject ViewModelProvider.Factory viewModelFactory;


    AwesomeValidation validator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_signup);


        Tools.hideSystemPlayerUi(this,true,0);

        Tools.setSystemBarTransparent(this);

        validator = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);

        setupRules();
        onLoadAppLogo();
        onLoadSplashImage();


        binding.btnRegister.setOnClickListener(v -> register());
        binding.goToLogin.setOnClickListener(v -> login());

    }


    private void onLoadSplashImage() {

        Glide.with(getApplicationContext()).asBitmap().load(settingsManager.getSettings().getSplashImage())
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(withCrossFade())
                .skipMemoryCache(true)
                .into(binding.splashImage);

    }


    // Display Main Logo
    private void onLoadAppLogo() {

        Tools.loadMiniLogo(this,binding.logoImageTop);
    }


    void register(){

        String name = Objects.requireNonNull(binding.tilName.getEditText()).getText().toString();
        String email = Objects.requireNonNull(binding.tilEmail.getEditText()).getText().toString();
        String password = Objects.requireNonNull(binding.tilPassword.getEditText()).getText().toString();

        binding.tilName.setError(null);
        binding.tilEmail.setError(null);
        binding.tilPassword.setError(null);

        if (validator.validate()) {
            showLoading();

            authRepository.getRegister(name,email, password)
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
                            startActivity(new Intent(RegisterActivity.this, RegistrationSucess.class));
                            finish();

                        }

                        @SuppressLint("ClickableViewAccessibility")
                        @Override
                        public void onError(@NotNull Throwable e) {

                             showForms();

                            ResponseBody responseBody = Objects.requireNonNull(((HttpException) e).response()).errorBody();


                            try {

                                for(Map.Entry<String, List<String>> error : new Gson().fromJson(Objects.requireNonNull(responseBody).string(), ApiError.class).getErrors().entrySet()){
                                    if(error.getKey().equals("name")){
                                        binding.tilName.setError(error.getValue().get(0));
                                        DialogHelper.erroLogin(RegisterActivity.this,error.getValue().get(0));
                                    }
                                    if(error.getKey().equals("email")){
                                        binding.tilEmail.setError(error.getValue().get(0));
                                        DialogHelper.erroLogin(RegisterActivity.this,error.getValue().get(0));
                                    }
                                    if(error.getKey().equals("password")){
                                        binding.tilPassword.setError(error.getValue().get(0));
                                        DialogHelper.erroLogin(RegisterActivity.this,error.getValue().get(0));
                                    }
                                }
                            } catch (IOException ioException) {

                                DialogHelper.erroLogin(RegisterActivity.this);
                            }

                        }

                        @Override
                        public void onComplete() {

                            //

                        }
                    });


        }

    }



    public Map<String, List<String>> getErrors() {
        return errors;
    }

    // show Progressbar on Register Button Submit
    private void showLoading(){
        binding.formContainer.setVisibility(View.GONE);
        binding.loader.setVisibility(View.VISIBLE);
    }


    private void showForms(){

        binding.formContainer.setVisibility(View.VISIBLE);
        binding.loader.setVisibility(View.GONE);

    }




    // Get the validation rules that apply to the request.
    public void setupRules(){

        validator.addValidation(this, R.id.til_name, RegexTemplate.NOT_EMPTY, R.string.err_name);
        validator.addValidation(this, R.id.til_email, Patterns.EMAIL_ADDRESS, R.string.err_email);
        validator.addValidation(this, R.id.til_password, "[A-Za-z0-9!#$%&(){|}~:;<=>?@*+,./^_`\\'\\\" \\t\\r\\n\\f-]+", R.string.err_password);
    }



    void login(){
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.get(this).clearMemory();
    }

}

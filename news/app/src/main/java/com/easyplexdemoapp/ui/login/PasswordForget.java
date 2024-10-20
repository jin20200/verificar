package com.easyplexdemoapp.ui.login;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.remote.ErrorHandling;
import com.easyplexdemoapp.databinding.ActivityPasswordForgetBinding;
import com.easyplexdemoapp.ui.base.BaseActivity;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.ui.viewmodels.LoginViewModel;
import com.easyplexdemoapp.util.DialogHelper;
import com.easyplexdemoapp.util.GlideApp;
import com.easyplexdemoapp.util.Tools;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Objects;
import javax.inject.Inject;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.basgeekball.awesomevalidation.ValidationStyle.TEXT_INPUT_LAYOUT;
import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;
import static com.easyplexdemoapp.util.Constants.SERVER_BASE_URL;

public class PasswordForget extends AppCompatActivity implements HasAndroidInjector {


    ActivityPasswordForgetBinding binding;

    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }

    @Inject
    SharedPreferences.Editor sharedPreferencesEditor;

    @Inject
    TokenManager tokenManager;

    @Inject
    SettingsManager settingsManager;

    @Inject
    ViewModelProvider.Factory viewModelFactory;


    private LoginViewModel loginViewModel;

    AwesomeValidation validator;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_password_forget);


        loginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);


        Tools.hideSystemPlayerUi(this, true, 0);

        Tools.setSystemBarTransparent(this);

        onLoadAppLogo();
        onLoadSplashImage();
        onLoadValitator();
        onSetupRules();


        if (tokenManager.getToken().getAccessToken() != null) {
            startActivity(new Intent(PasswordForget.this, BaseActivity.class));
            finish();

        }

        binding.btnLogin.setOnClickListener(view -> sendEmail());
        binding.close.setOnClickListener(view -> uclose());
        binding.btnUpdatePassword.setOnClickListener(view -> updatePassword());

    }

    private void onLoadSplashImage() {

        GlideApp.with(getApplicationContext()).asBitmap().load(settingsManager.getSettings().getSplashImage())
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(withCrossFade())
                .skipMemoryCache(true)
                .into(binding.splashImage);
    }



    void sendEmail() {

        String email = Objects.requireNonNull(binding.tilEmail.getEditText()).getText().toString();
        binding.tilEmail.setError(null);
        validator.clear();


        if (validator.validate()) {


            hideKeyboard();
            binding.loader.setVisibility(View.VISIBLE);
            binding.emailForget.setVisibility(View.GONE);

            loginViewModel.getPasswordForget(email).observe(PasswordForget.this, login -> {

                if (login.status == ErrorHandling.Status.SUCCESS) {

                    binding.emailForget.setVisibility(View.GONE);
                    binding.tokenEnter.setVisibility(View.VISIBLE);
                    binding.loader.setVisibility(View.GONE);


                } else {


                    Toast.makeText(this, "No Email Match was found", Toast.LENGTH_SHORT).show();
                    binding.loader.setVisibility(View.GONE);
                    binding.emailForget.setVisibility(View.VISIBLE);

                }

            });

        }

    }


    void uclose() {

        startActivity(new Intent(PasswordForget.this, LoginActivity.class));
        finish();

    }


    void updatePassword() {

        String token = binding.tokenUser.getEditText().getText().toString();
        String email = binding.tokenUserEmail.getEditText().getText().toString();
        String password = binding.tokenUserPassword.getEditText().getText().toString();
        String passwordConfirmation = binding.tokenUserPasswordConfirmation.getEditText().getText().toString();

        binding.tilEmail.setError(null);
        binding.tokenUser.setError(null);
        binding.tokenUserPassword.setError(null);
        binding.tokenUserPasswordConfirmation.setError(null);
        validator.clear();

            hideKeyboard();
            binding.loader.setVisibility(View.VISIBLE);
            binding.tokenEnter.setVisibility(View.GONE);

            loginViewModel.getPasswordUpdate(token,email,password,passwordConfirmation).observe(PasswordForget.this, login -> {


                if (login.status == ErrorHandling.Status.SUCCESS) {

                    final Dialog dialog = new Dialog(this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                    dialog.setContentView(R.layout.dialog_password_updated);
                    dialog.setCancelable(true);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WRAP_CONTENT;
                    lp.height = WRAP_CONTENT;
                    dialog.findViewById(R.id.bt_getcode).setOnClickListener(v -> {

                        startActivity(new Intent(PasswordForget.this, LoginActivity.class));
                        finish();
                    });
                    dialog.findViewById(R.id.bt_close).setOnClickListener(v -> {
                        startActivity(new Intent(PasswordForget.this, LoginActivity.class));
                        finish();
                    });


                    dialog.show();
                    dialog.getWindow().setAttributes(lp);


                } else {

                    binding.loader.setVisibility(View.GONE);
                    binding.tokenEnter.setVisibility(View.VISIBLE);
                    DialogHelper.erroLogin(this);


                }

            });


    }



    private void onLoadValitator() {

        validator = new AwesomeValidation(TEXT_INPUT_LAYOUT);
        validator.setTextInputLayoutErrorTextAppearance(R.style.TextInputLayoutErrorStyle);
    }


    // Display Main Logo
    private void onLoadAppLogo() {

        GlideApp.with(getApplicationContext()).asBitmap().load(SERVER_BASE_URL +"image/minilogo")
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(withCrossFade())
                .skipMemoryCache(true)
                .into(binding.logoImageTop);

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
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}

package com.easyplexdemoapp.ui.profile;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;
import static com.easyplexdemoapp.util.Constants.SERVER_BASE_URL;
import static java.util.Objects.requireNonNull;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.TransitionManager;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.auth.UserAuthInfo;
import com.easyplexdemoapp.data.remote.ErrorHandling;
import com.easyplexdemoapp.data.repository.AuthRepository;
import com.easyplexdemoapp.databinding.ActivityEditProfileBinding;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.settings.SettingsActivity;
import com.easyplexdemoapp.ui.viewmodels.LoginViewModel;
import com.easyplexdemoapp.util.GlideApp;
import com.easyplexdemoapp.util.Tools;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import javax.inject.Inject;
import at.favre.lib.crypto.bcrypt.BCrypt;

import dagger.android.AndroidInjection;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class EditProfileActivity extends AppCompatActivity {



    ActivityEditProfileBinding binding;

    private static final int GALLERY_IMAGE_REQ_CODE = 102;

    @Inject
    ViewModelProvider.Factory viewModelFactory;



    @Inject
    SettingsManager settingsManager;

    private LoginViewModel loginViewModel;


    @Inject
    AuthRepository authRepository;


    @Inject
    AuthManager authManager;


    AwesomeValidation validator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);



        onLoadSplashImage();

        // LoginViewModel to cache, retrieve data for Authenticated User
        loginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);

        onCheckAuthenticatedUser();

        onHideTaskBar();

        setupRules();

        binding.closeProfileFragment.setOnClickListener(v -> finish());



        binding.btnUploadAvatar.setOnClickListener(view -> pickProfileImage());
        binding.btnUpdate.setOnClickListener(view -> register());

    }







    private void onLoadSplashImage() {

        GlideApp.with(getApplicationContext()).asBitmap().load(settingsManager.getSettings().getSplashImage())
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(withCrossFade())
                .skipMemoryCache(true)
                .into(binding.splashImage);

    }

    private void onCheckAuthenticatedUser() {


        authRepository.getAuth()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                        //

                    }

                    @Override
                    public void onNext(@NonNull UserAuthInfo auth) {



                        if (!auth.getProfiles().isEmpty()){


                            Tools.loadUserAvatar(EditProfileActivity.this, binding.userAvatar, authManager.getSettingsProfile().getAvatar());

                        }else {

                            Tools.loadUserAvatar(EditProfileActivity.this, binding.userAvatar, auth.getAvatar());

                        }


                        Tools.loadUserAvatar(getApplicationContext(),binding.userAvatar,auth.getAvatar());

                        binding.editTextName.setText(auth.getName());
                        binding.editTextEmail.setText(auth.getEmail());



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


    private void onHideTaskBar() {

        Tools.hideSystemPlayerUi(this,true,0);

        Tools.setSystemBarTransparent(this);
    }


    void pickProfileImage(){

        ImagePicker.with(this)
                // Crop Image(User can choose Aspect Ratio)
                .crop()
                // User can only select image from Gallery
                .galleryOnly()

                .galleryMimeTypes(new String[]{"image/png",
                        "image/jpg",
                        "image/jpeg"
                })
                // Image resolution will be less than 1080 x 1920
                .maxResultSize(1080, 1920)
                .cropSquare()
                .start(GALLERY_IMAGE_REQ_CODE);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            // Uri object will not be null for RESULT_OK
            assert data != null;
            Uri uri = data.getData();

            GlideApp.with(getApplicationContext()).asBitmap().load(uri)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(withCrossFade())
                    .skipMemoryCache(true)
                    .into(binding.userAvatar);

            RequestBody body = RequestBody.create(new File(uri.getPath()), null);
            MultipartBody.Part photo = MultipartBody.Part.createFormData("avatar", "avatar.png", body);

            loginViewModel.updateUserAvatar(photo).observe(this, login -> {

                if (login.status == ErrorHandling.Status.SUCCESS ) {

                    Toast.makeText(this, R.string.profile_updated, Toast.LENGTH_SHORT).show();

                } else  {

                    Toast.makeText(this, R.string.profile_update_error, Toast.LENGTH_SHORT).show();
                }

            });

        }
        super.onActivityResult(requestCode, resultCode, data);



    }

    void register(){

        String name = requireNonNull(binding.tilName.getEditText()).getText().toString();
        String email = requireNonNull(binding.tilEmail.getEditText()).getText().toString();
        String password = binding.tilPassword.getEditText().getText().toString();

        binding.tilName.setError(null);
        binding.tilEmail.setError(null);
        binding.tilPassword.setError(null);

        if (validator.validate()) {
            showLoading();



            if (password.isEmpty()) {

                loginViewModel.updateUser(name,email).observe(this, login -> {

                    Toast.makeText(this, "Your profile has been updated successfully ! ", Toast.LENGTH_SHORT).show();

                    if (login.status == ErrorHandling.Status.SUCCESS ) {

                        startActivity(new Intent(this, SettingsActivity.class));
                        finish();


                    } else  {

                        showForms();

                        Toast.makeText(this, "Your profile is not  updated ! ", Toast.LENGTH_SHORT).show();

                    }

                });

            }else {

                String passwordHashed = BCrypt.withDefaults().hashToString(12, password.toCharArray());


                loginViewModel.updateUser(name,email,passwordHashed).observe(this, login -> {

                    Toast.makeText(this, "Your profile has been updated successfully ! ", Toast.LENGTH_SHORT).show();


                    if (login.status == ErrorHandling.Status.SUCCESS ) {

                        startActivity(new Intent(this, SettingsActivity.class));
                        finish();


                    } else  {

                        showForms();

                        Toast.makeText(this, "Your profile is not  updated ! ", Toast.LENGTH_SHORT).show();

                    }

                });

            }


        }

    }


    // show Progressbar on Update Button Submit
    private void showLoading(){
        TransitionManager.beginDelayedTransition(binding.container);
        binding.formContainer.setVisibility(View.GONE);
        binding.loader.setVisibility(View.VISIBLE);
    }


    private void showForms(){

        binding.formContainer.setVisibility(View.VISIBLE);
        binding.loader.setVisibility(View.GONE);

    }


    // Get the validation rules that apply to the request.
    public void setupRules(){

        validator = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        validator.addValidation(this, R.id.til_name, RegexTemplate.NOT_EMPTY, R.string.err_name);
        validator.addValidation(this, R.id.til_email, Patterns.EMAIL_ADDRESS, R.string.err_email);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
       binding = null;
    }
}

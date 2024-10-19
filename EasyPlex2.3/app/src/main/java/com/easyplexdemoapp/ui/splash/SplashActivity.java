package com.easyplexdemoapp.ui.splash;

import static com.easyplexdemoapp.util.Constants.APP_DEFAULT_LANG;
import static com.easyplexdemoapp.util.Constants.ARG_MOVIE;
import static com.easyplexdemoapp.util.Constants.CUSTOM_VAST_XML;
import static com.easyplexdemoapp.util.Constants.FIRST_INSTALL;
import static com.easyplexdemoapp.util.Constants.SERVER_FIREBASE_VALUE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.download.library.DownloadImpl;
import com.download.library.DownloadListenerAdapter;
import com.download.library.Extra;
import com.easyplexdemoapp.BuildConfig;
import com.easyplexdemoapp.EasyPlexApp;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.ads.Ads;
import com.easyplexdemoapp.data.model.settings.BehaviorSettings;
import com.easyplexdemoapp.data.model.settings.Settings;
import com.easyplexdemoapp.data.repository.SettingsRepository;
import com.easyplexdemoapp.databinding.ActivitySplashBinding;
import com.easyplexdemoapp.databinding.DialogUpdateAlertBinding;
import com.easyplexdemoapp.di.Injectable;
import com.easyplexdemoapp.ui.animes.AnimeDetailsActivity;
import com.easyplexdemoapp.ui.base.BaseActivity;
import com.easyplexdemoapp.ui.login.LoginActivity;
import com.easyplexdemoapp.ui.manager.AdsManager;
import com.easyplexdemoapp.ui.manager.AppBehaviorManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.StatusManager;
import com.easyplexdemoapp.ui.moviedetails.MovieDetailsActivity;
import com.easyplexdemoapp.ui.seriedetails.SerieDetailsActivity;
import com.easyplexdemoapp.ui.streaming.StreamingetailsActivity;
import com.easyplexdemoapp.ui.users.MenuHandler;
import com.easyplexdemoapp.util.Constants;
import com.easyplexdemoapp.util.DialogHelper;
import com.easyplexdemoapp.util.FontChanger;
import com.easyplexdemoapp.util.GlideApp;
import com.easyplexdemoapp.util.Tools;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.io.File;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.android.AndroidInjection;
import io.github.g00fy2.versioncompare.Version;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity implements Injectable {


    private static final long SPLASH_DELAY_FIREBASE = 3000;
    private static final long SPLASH_DELAY_DEFAULT = 500;

    @Inject
    SettingsRepository settingsRepository;

    @Inject
    SettingsManager settingsManager;

    @Inject
    AdsManager adsManager;

    @Inject
    StatusManager statusManager;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    @Named("sniffer")
    @Nullable
    ApplicationInfo provideApplicationInfo;

    @Inject
    @Named("package_name")
    String packageName;

    @Inject
    @Named("vpn")
    boolean checkVpn;


    @Inject
    SharedPreferences sharedPreferences;


    @Inject
    SharedPreferences.Editor editor;

    @Inject
    MenuHandler menuHandler;

    @Inject
    @Singleton
    @Named("firebaseRemoteUrl")
    FirebaseRemoteConfig provideFirebaseRemoteConfig;



    @Inject
    AppBehaviorManager appBehaviorManager;

    long delayMillis;

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);


        delayMillis = Constants.FIREBASECONFIG ? SPLASH_DELAY_FIREBASE : SPLASH_DELAY_DEFAULT;

        binding.setController(menuHandler);

        menuHandler.isNetworkActive.set(Tools.checkIfHasNetwork(getApplicationContext()));


        onValidateParams();

        binding.tryAgain.setOnClickListener(v -> {

            menuHandler.isNetworkActive.set(Tools.checkIfHasNetwork(getApplicationContext()));

            onValidateParams();
        });



        if (Constants.FIREBASECONFIG){

            fetchAndActivateFirebaseRemoteConfig();
        }

    }


    @SuppressLint("TimberArgCount")
    private void fetchAndActivateFirebaseRemoteConfig() {

        provideFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Timber.tag("TAG").d("onActivate: %s", task.getResult());

                provideFirebaseRemoteConfig.fetchAndActivate();
                String apiUrl = provideFirebaseRemoteConfig.getString(SERVER_FIREBASE_VALUE);

                editor.putString("apiUrl",apiUrl);
            } else {
                Timber.tag("TAG").d(task.getException(), "onActivationError: %s");
            }
        });
    }

    private void onValidateParams() {
        executorService.schedule(() -> settingsRepository.getParams()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        // Subscription logic
                    }

                    @Override
                    public void onNext(@NonNull BehaviorSettings settings) {

                        appBehaviorManager.saveSettings(settings);


                        boolean result;

                        result = new Version(BuildConfig.VERSION_NAME).isLowerThan(settings.getVersion());

                        if (settings.isForceUpdate() && result){


                            onSetupNewVersion();


                        }else {

                            if (settings.isCrash()){

                                executorService.schedule(() -> settingsRepository.getAPKSignatureCheck(EasyPlexApp.getSignatureValid())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Observer<>() {
                                            @Override
                                            public void onSubscribe(@NonNull Disposable d) {
                                                // Subscription logic
                                            }

                                            @Override
                                            public void onNext(@NonNull Settings settings) {

                                                onValidateSettingCheck();

                                            }

                                            @Override
                                            public void onError(@NonNull Throwable e) {

                                                Toast.makeText(SplashActivity.this, "APK signature does not match.", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onComplete() {
                                                //
                                            }
                                        }), delayMillis, TimeUnit.MILLISECONDS);

                            }else {


                                onValidateSettingCheck();
                            }

                        }




                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        onValidateSettingCheck();
                    }

                    @Override
                    public void onComplete() {
                        //
                    }
                }), delayMillis, TimeUnit.MILLISECONDS);
    }

    private void onSetupNewVersion() {

        // Inflating layout using View Binding
        DialogUpdateAlertBinding binding = DialogUpdateAlertBinding.inflate(getLayoutInflater());

        final Dialog dialog = new Dialog(SplashActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding.getRoot()); // Setting content view using View Binding
        dialog.setCancelable(settingsManager.getSettings().getForceUpdate() == 0);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        // Accessing views using View Binding
        binding.linearprogressactive.setVisibility(View.GONE);
        binding.downloadProgress.setVisibility(View.GONE);
        binding.downloadProgres.setVisibility(View.GONE);

        binding.getUpdateLink.setOnClickListener(v -> {
            if (settingsManager.getSettings().getForce_inappupdate() == 1) {
                String finalFileName = settingsManager.getSettings().getAppName() +
                        settingsManager.getSettings().getLatestVersion().replaceAll("\\s+", "") + ".apk";

                DownloadImpl.with(this)
                        .target(new File(sharedPreferences.getString(Constants.DEFAULT_DOWNLOAD_DIRECTORY,
                                String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))),
                                finalFileName))
                        .setUniquePath(true)
                        .setEnableIndicator(false)
                        .setDownloadingListener((url, downloaded, length, usedTime) -> {
                            // Update UI based on download progress
                        })
                        .url(settingsManager.getSettings().getUrl())
                        .enqueue(new DownloadListenerAdapter() {
                            @Override
                            public void onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, Extra extra) {
                                super.onStart(url, userAgent, contentDisposition, mimetype, contentLength, extra);

                                binding.downloadProgres.setVisibility(View.VISIBLE);
                                binding.updateLinear.setVisibility(View.GONE);
                                binding.linearprogressactive.setVisibility(View.VISIBLE);
                                binding.downloadProgress.setVisibility(View.VISIBLE);
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onProgress(String url, long downloaded, long length, long usedTime) {
                                super.onProgress(url, downloaded, length, usedTime);

                                binding.downloadProgres.setText("Downloaded:" + Tools.byte2FitMemorySize(downloaded) + " Total Time :" + (usedTime / 1000) + "s");

                                int mProgress = (int) ((downloaded) / (float) length * 100);

                                binding.downloadProgressBarDownloading.setProgress(mProgress);

                                Timber.i(" progress:" + downloaded + " url:" + url);
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            public boolean onResult(Throwable throwable, Uri path, String url, Extra extra) {
                                binding.downloadProgres.setText("Completed");
                                binding.downloadProgressBarDownloading.setProgress(100);

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.setDataAndType(FileProvider.getUriForFile(SplashActivity.this, BuildConfig.APPLICATION_ID + ".provider", new File(path.getPath()))
                                        , "application/vnd.android.package-archive");
                                startActivity(intent);
                                finishAffinity();

                                return super.onResult(throwable, path, url, extra);
                            }
                        });
            } else {
                if (settingsManager.getSettings().getUrl() != null && !settingsManager.getSettings().getUrl().isEmpty()) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(settingsManager.getSettings().getUrl())));
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.yobex))));
                }
            }
        });

        // Accessing close button and setting its visibility
        if (settingsManager.getSettings().getForceUpdate() == 1) {
            binding.btClose.setVisibility(View.GONE);
        }

        binding.btClose.setOnClickListener(v -> dialog.dismiss());

        // Setting update title and custom alert text
        binding.updateTitle.setText(settingsManager.getSettings().getUpdateTitle());
        binding.customAlertText.setText(settingsManager.getSettings().getReleaseNotes());

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void onValidateSettingCheck() {

        if (Tools.checkIfHasNetwork(getApplicationContext())){

            onLoadSettings();

        }else {

            startActivity(new Intent(this, BaseActivity.class));
            finish();
        }
    }

    private void onLoadSettings() {

        onLoadLogo();
        hideTaskBar();
        loadSplashImage();
        ApplicationInfo();
    }





    private void hideTaskBar() {
        Tools.hideSystemPlayerUi(this, true, 0);
    }

    private void loadSplashImage() {

        GlideApp.with(getApplicationContext())
                .asBitmap()
                .load(settingsManager.getSettings().getSplashImage())
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(BitmapTransitionOptions.withCrossFade())
                .skipMemoryCache(true)
                .into(binding.splashImage);
    }

    private void ApplicationInfo() {

        if (provideApplicationInfo != null) {
            DialogHelper.snifferAppDetectorDialog(this, provideApplicationInfo.loadLabel(this.getPackageManager()).toString());

        }else if (settingsManager.getSettings().getVpn() == 1 && checkVpn) {
            finishAffinity();
            Toast.makeText(this, R.string.vpn_message, Toast.LENGTH_SHORT).show();
        } else {
            handleSettingsFetching();
        }
    }

    private void handleSettingsFetching() {
        executorService.schedule(() -> settingsRepository.getSettings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        // Subscription logic
                    }

                    @Override
                    public void onNext(@NonNull Settings settings) {
                        settingsManager.saveSettings(settings);
                        handleAdsSettingsFetching();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        handleSettingsFetchingError();
                    }

                    @Override
                    public void onComplete() {
                        navigateToAppropriateScreen();
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }), delayMillis, TimeUnit.MILLISECONDS);
    }



    private void handleAdsSettingsFetching() {

        settingsRepository.getAdsSettings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        // Subscription logic
                    }

                    @Override
                    public void onNext(@NonNull Ads ads) {



                        adsManager.saveSettings(ads);

                        if (ads.getCustomVast() == 1) {

                            setupCustomVastLink(ads);
                        }


                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        // Error handling for ads settings
                    }

                    @Override
                    public void onComplete() {
                       //
                    }
                });



    }


    private void setupCustomVastLink(Ads ads) {
        String apiUrl = Tools.getApiUrl();
        CUSTOM_VAST_XML = apiUrl+"vast/" + ads.getId();
    }

    private void handleSettingsFetchingError() {
        String errorMessage = Constants.FIREBASECONFIG
                ? getString(R.string.error_loading_app_settings)
                : getString(R.string.error_loading_api_please_check);
        Toast.makeText(SplashActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void navigateToAppropriateScreen() {



        setLocale(getSelectedLanguageCode());

        if (sharedPreferences.getString("selectedLanguage", APP_DEFAULT_LANG).equals("ar")){

            // Initialize Arabic font
            FontChanger.initArabicFont(this);

            // Change font for all TextView instances in the app
            FontChanger.changeFontInViewGroup((ViewGroup)this.getWindow().getDecorView());

        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent appLinkIntent = getIntent();
            Uri data = appLinkIntent.getData();

            if (data != null) {
                String path = data.getPath();
                if (path != null) {
                    Media media = new Media();
                    media.setId(Objects.requireNonNull(data.getLastPathSegment()));

                    Class<?> targetActivityClass = null;

                    if (path.startsWith("/movies")) {
                        targetActivityClass = MovieDetailsActivity.class;
                    } else if (path.startsWith("/series")) {
                        targetActivityClass = SerieDetailsActivity.class;
                    } else if (path.startsWith("/animes")) {
                        targetActivityClass = AnimeDetailsActivity.class;
                    } else if (path.startsWith("/streaming")) {
                        targetActivityClass = StreamingetailsActivity.class;
                    }

                    if (targetActivityClass != null) {
                        Intent intent = new Intent(SplashActivity.this, targetActivityClass);
                        intent.putExtra(ARG_MOVIE, media);
                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(SplashActivity.this);
                        stackBuilder.addNextIntentWithParentStack(intent);
                        stackBuilder.startActivities();
                        finish();
                    }
                }
            } else if (!sharedPreferences.getBoolean(FIRST_INSTALL, false)) {
                editor = sharedPreferences.edit();
                editor.putBoolean(FIRST_INSTALL, Boolean.TRUE);
                editor.apply();




                settingsRepository.getInstalls()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                // Subscription logic
                            }

                            @Override
                            public void onNext(@NonNull Settings settings) {

                                //

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                // Error handling for ads settings
                            }

                            @Override
                            public void onComplete() {
                                //
                            }
                        });


                startActivity(new Intent(SplashActivity.this, settingsManager.getSettings().getDisablelogin() == 1 ? BaseActivity.class : ConfiigurationFirstLaunch.class));
                finish();

            }else {


                delayMillis = Constants.FIREBASECONFIG ? SPLASH_DELAY_FIREBASE : SPLASH_DELAY_DEFAULT;

                startActivity(new Intent(SplashActivity.this, settingsManager.getSettings().getDisablelogin() == 1 ? BaseActivity.class : LoginActivity.class));
                finish();

            }


        }, delayMillis);
    }

    private void onLoadLogo() {

        Tools.loadMiniLogo(getApplicationContext(),binding.logoImageTop);


    }


    public void setLocale(String languageCode) {

        // Construct Locale based on the language code
        Locale locale;
        if (languageCode.equals("pt-rBR")) {
            // Brazilian Portuguese
            locale = new Locale("pt", "BR");
        } else if (languageCode.equals("es-rMX")) {
            // Mexican Spanish
            locale = new Locale("es", "MX");
        } else if (languageCode.equals("fa") || languageCode.equals("fa-IR")) {
            // Farsi (Persian)
            locale = new Locale("fa", "IR");
        } else if (languageCode.equals("fr") || languageCode.equals("fr-FR")) {
            // French (France)
            locale = new Locale("fr", "FR");
        } else if (languageCode.equals("fr-CA")) {
            // French (Canada)
            locale = new Locale("fr", "CA");
        } else {
            locale = new Locale(languageCode);
        }

        // Set the default locale
        Locale.setDefault(locale);

        // Set configuration
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);

        Resources resources = getResources();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

    }

    public String getSelectedLanguageCode() {
        return sharedPreferences.getString("selectedLanguage", APP_DEFAULT_LANG);
    }



}

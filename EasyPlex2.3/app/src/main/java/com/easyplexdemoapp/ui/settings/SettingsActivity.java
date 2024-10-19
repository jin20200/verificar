package com.easyplexdemoapp.ui.settings;

import static android.view.View.GONE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.easyplexdemoapp.util.Constants.AUTO_PLAY;
import static com.easyplexdemoapp.util.Constants.ENABLE_SIGNATURE_RELEASE;
import static com.easyplexdemoapp.util.Constants.EXTENTIONS;
import static com.easyplexdemoapp.util.Constants.ISUSER_MAIN_ACCOUNT;
import static com.easyplexdemoapp.util.Constants.PLAYER_ASPECT_RATIO;
import static com.easyplexdemoapp.util.Constants.PREMUIM;
import static com.easyplexdemoapp.util.Constants.SOFTWARE_EXTENTIONS;
import static com.easyplexdemoapp.util.Constants.SUBSCRIPTIONS;
import static com.easyplexdemoapp.util.Constants.SUBS_BACKGROUND;
import static com.easyplexdemoapp.util.Constants.SUBS_DEFAULT_LANG;
import static com.easyplexdemoapp.util.Constants.SUBS_DEFAULT_LANG_NAME;
import static com.easyplexdemoapp.util.Constants.SUBS_SIZE;
import static com.easyplexdemoapp.util.Constants.SWITCH_PUSH_NOTIFICATION;
import static com.easyplexdemoapp.util.Constants.WIFI_CHECK;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easyplexdemoapp.BuildConfig;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.auth.UserAuthInfo;
import com.easyplexdemoapp.data.model.substitles.ImdbLangs;
import com.easyplexdemoapp.data.repository.AuthRepository;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.data.repository.SettingsRepository;
import com.easyplexdemoapp.databinding.ActivitySettingBinding;
import com.easyplexdemoapp.databinding.ClearMylistBinding;
import com.easyplexdemoapp.ui.base.BaseActivity;
import com.easyplexdemoapp.ui.devices.UserDevicesManagement;
import com.easyplexdemoapp.ui.downloadmanager.ui.PermissionDeniedDialog;
import com.easyplexdemoapp.ui.downloadmanager.ui.PermissionManager;
import com.easyplexdemoapp.ui.login.LoginActivity;
import com.easyplexdemoapp.ui.manager.AdsManager;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.ui.plans.PlansAdapter;
import com.easyplexdemoapp.ui.profile.EditProfileActivity;
import com.easyplexdemoapp.ui.search.AdapterSuggestionSearch;
import com.easyplexdemoapp.ui.splash.ConfiigurationFirstLaunch;
import com.easyplexdemoapp.ui.splash.SplashActivity;
import com.easyplexdemoapp.ui.users.MenuHandler;
import com.easyplexdemoapp.ui.viewmodels.LoginViewModel;
import com.easyplexdemoapp.ui.viewmodels.MoviesListViewModel;
import com.easyplexdemoapp.ui.viewmodels.SettingsViewModel;
import com.easyplexdemoapp.util.DialogHelper;
import com.easyplexdemoapp.util.NetworkUtils;
import com.easyplexdemoapp.util.SpacingItemDecoration;
import com.easyplexdemoapp.util.Tools;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jaredrummler.android.device.DeviceName;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.AndroidInjection;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.drakeet.support.toast.ToastCompat;
import retrofit2.HttpException;
import timber.log.Timber;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG_PERM_DENIED_DIALOG = "perm_denied_dialog";
    private PermissionManager permissionManager;

    private PermissionDeniedDialog permDeniedDialog;

    ActivitySettingBinding binding;


    @Inject
    @Named("getSignatureValid")
    String getSignatureValid;

    @Inject
    @Named("app_hash_256")
    String apkChecksumVerifier;

    @Inject
    MenuHandler menuHandler;

    @Inject
    ViewModelProvider.Factory viewModelFactory;


    @Inject
    MediaRepository mediaRepository;

    private MoviesListViewModel moviesListViewModel;

    @Inject
    AuthRepository authRepository;

    @Inject
    SharedPreferences.Editor sharedPreferencesEditor;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    TokenManager tokenManager;

    @Inject
    SettingsManager settingsManager;

    @Inject
    AdsManager adsManager;

    @Inject
    AuthManager authManager;

    @Inject
    SettingsRepository settingsRepository;


    private LoginViewModel loginViewModel;

    private SettingsViewModel settingsViewModel;


    FragmentManager manager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);

        binding.setController(menuHandler);
        

        binding.toolbar.MoreOptionsLinear.setVisibility(View.VISIBLE);
        binding.toolbar.MoreOptionsLinear.setOnClickListener(v -> onBackPressed());

        menuHandler.isDeviceOptionActivated.set(settingsManager.getSettings().getDeviceManagement() == 1);

        menuHandler.isSearchhistory.set(settingsManager.getSettings().getSearchhistory() == 1);

        menuHandler.isAppRelease.set(ENABLE_SIGNATURE_RELEASE);

        menuHandler.isAppDebug.set(BuildConfig.DEBUG);


        manager = getSupportFragmentManager();

        permDeniedDialog = (PermissionDeniedDialog)manager.findFragmentByTag(TAG_PERM_DENIED_DIALOG);


        permissionManager = new PermissionManager(this, new PermissionManager.Callback() {
            @Override
            public void onStorageResult(boolean isGranted, boolean shouldRequestStoragePermission) {
                if (!isGranted && shouldRequestStoragePermission && (manager.findFragmentByTag(TAG_PERM_DENIED_DIALOG) == null)) {
                        permDeniedDialog = PermissionDeniedDialog.newInstance();
                        FragmentTransaction ft = manager.beginTransaction();
                        ft.add(permDeniedDialog, TAG_PERM_DENIED_DIALOG);
                        ft.commitAllowingStateLoss();

                }
            }

            @Override
            public void onNotificationResult(boolean isGranted, boolean shouldRequestNotificationPermission) {
                permissionManager.setDoNotAskNotifications(!isGranted);
            }
        });

        loginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);

        settingsViewModel = new ViewModelProvider(this, viewModelFactory).get(SettingsViewModel.class);

        moviesListViewModel = new ViewModelProvider(this, viewModelFactory).get(MoviesListViewModel.class);

        Tools.setSystemBarTransparent(this);
        Tools.hideSystemPlayerUi(this,true,0);

        onLoadAppLogo();
        setButtonsUtilities();
        onLoadAppBar();
        onCheckAuth();
        onLogout();
        onLoadAboutUs();
        onLoadPrivacyPolicy();
        onClearCache();
        onLoadEditProfile();
        onClearRoomDatabase();
        onClearWatchHistory();
        onClearSearchHistory();
        settingsViewModel.getSettingsDetails();
        settingsViewModel.getPlans();
        settingsViewModel.getLangs();



        binding.getSignature.setOnClickListener(v -> Tools.setContentToClipboardManager(getApplicationContext(),getSignatureValid,settingsManager,true));

        binding.getHash.setOnClickListener(v -> Tools.setContentToClipboardManager(getApplicationContext(),apkChecksumVerifier,settingsManager,false));

        binding.subcribeButton.setOnClickListener(v -> settingsViewModel.plansMutableLiveData.observe(this, plans -> {

            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_plans_display);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());

            lp.gravity = Gravity.BOTTOM;
            lp.width = MATCH_PARENT;
            lp.height = MATCH_PARENT;

            RecyclerView recyclerViewPlans = dialog.findViewById(R.id.recycler_view_plans);
            PlansAdapter plansAdapter = new PlansAdapter();
            recyclerViewPlans.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerViewPlans.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(this, 0), true));
            recyclerViewPlans.setAdapter(plansAdapter);
            plansAdapter.addCasts(plans.getPlans(),settingsManager);


            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                    dialog.dismiss());

            dialog.show();
            dialog.getWindow().setAttributes(lp);


        }));






        binding.applyCoupon.setOnClickListener(v -> {

            final Dialog dialog = new Dialog(SettingsActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_coupons);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());

            lp.gravity = Gravity.BOTTOM;
            lp.width = MATCH_PARENT;
            lp.height = MATCH_PARENT;

            EditText couponEditText = dialog.findViewById(R.id.et_post);

            dialog.findViewById(R.id.applyCouponButton).setOnClickListener(coup -> {

                onCouponApply(dialog, couponEditText);

            });

            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

            dialog.dismiss());

            dialog.show();
            dialog.getWindow().setAttributes(lp);



        });



        binding.btnLogin.setOnClickListener(v -> startActivity(new Intent(SettingsActivity.this, LoginActivity.class)));


        binding.currentSubsDefaultLinear.setOnClickListener(v -> settingsRepository.getLangsFromImdb()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                        //

                    }

                    @Override
                    public void onNext(@NonNull List<ImdbLangs> imdbLangs) {

                        String[] charSequenceSubsSize = new String[imdbLangs.size()];
                        for (int i = 0; i < imdbLangs.size(); i++) {

                            charSequenceSubsSize[i] = String.valueOf(imdbLangs.get(i).getEnglishName());

                        }

                        final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.MyAlertDialogTheme);
                        builder.setTitle(R.string.default_lang_substitles);
                        builder.setCancelable(true);
                        builder.setItems(charSequenceSubsSize, (dialogInterface, wich) -> {

                            sharedPreferencesEditor.putString(SUBS_DEFAULT_LANG, String.valueOf(imdbLangs.get(wich))).apply();
                            sharedPreferences.getString(SUBS_DEFAULT_LANG, SUBS_DEFAULT_LANG_NAME);
                            binding.currentSubsDefaultLang.setText(String.format(getString(R.string.current_default_lang2), sharedPreferences.getString(SUBS_DEFAULT_LANG, SUBS_DEFAULT_LANG_NAME)));
                            dialogInterface.dismiss();

                        });

                        builder.show();
                    }


                    @Override
                    public void onError(@NotNull Throwable e) {

                        //

                    }

                    @Override
                    public void onComplete() {

                        //

                    }
                }));


        if (tokenManager.getToken().getAccessToken() == null) {
            binding.btnLogin.setVisibility(View.VISIBLE);

        }else {

            binding.btnLogin.setVisibility(GONE);
        }


        binding.cacehSize.setText(getString(R.string.sub_setting_clear_cache_start) + " " + Tools.readableFileSize((Tools.getDirSize(getCacheDir())) + Tools.getDirSize(Objects.requireNonNull(getExternalCacheDir()))) + " " + getString(R.string.sub_setting_clear_cache_end));


        binding.downloadsOptions.setOnClickListener(v -> startActivity(new Intent(this, com.easyplexdemoapp.ui.downloadmanager.ui.settings.SettingsActivity.class)));

        binding.btnDeviceManagement.setOnClickListener(this::onDeviceManage);


        binding.AppLans.setOnClickListener(v -> {

            startActivity(new Intent(SettingsActivity.this, ConfiigurationFirstLaunch.class));
            finish();
        });



    }

    private void onCouponApply(Dialog dialog, EditText couponEditText) {

        String couponCode = couponEditText.getText().toString();

        if (couponCode.isEmpty()){

            Toast.makeText(getApplicationContext(), R.string.please_enter_a_coupon_code_before_coutinue, Toast.LENGTH_SHORT).show();

            return;
        }

        authRepository.applyCoupon(couponCode, String.valueOf(authManager.getUserInfo().getId()))
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

                        dialog.cancel();

                        Tools.ToastHelper(SettingsActivity.this,userAuthInfo.getMessage());
                        startActivity(new Intent(SettingsActivity.this, SplashActivity.class));
                        finish();

                    }

                    @Override
                    public void onError(@NotNull Throwable e) {

                        if (e instanceof HttpException) {
                            HttpException httpException = (HttpException) e;

                            try {
                                // Get the response body as a string
                                String errorBody = httpException.response().errorBody().string();

                                // Parse the errorBody JSON to extract the message
                                JSONObject errorJson = new JSONObject(errorBody);
                                String errorMessage = errorJson.getString("message");

                                // Display the error message using Toast or another method
                                Toast.makeText(SettingsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            } catch (IOException | JSONException ex) {
                                // Handle parsing or other exceptions here
                                ex.printStackTrace();
                            }
                        } else {
                            // Handle non-HTTP exceptions here
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onComplete() {

                        //

                    }
                });
    }

    private void onClearSearchHistory() {

        binding.Searchhistory.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), R.string.history_cleared, Toast.LENGTH_SHORT).show();

            AdapterSuggestionSearch adapterSuggestionSearch = new AdapterSuggestionSearch(getApplicationContext());
            adapterSuggestionSearch.clearSearchHistory();
        });

    }


    private void onClearWatchHistory() {

        binding.clearMyWatchHistory.setOnClickListener(v -> {

            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            // Inflating layout using View Binding
            ClearMylistBinding binding = ClearMylistBinding.inflate(getLayoutInflater());
            dialog.setContentView(binding.getRoot());

            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            binding.btGetcode.setOnClickListener(x -> {
                moviesListViewModel.deleteHistory();
                Toast.makeText(SettingsActivity.this, R.string.history_cleared, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });

            binding.btClose.setOnClickListener(x -> dialog.dismiss());

            dialog.show();
            dialog.getWindow().setAttributes(lp);


        });

    }



    private void onLoadEditProfile() {

        binding.btnEditProfile.setOnClickListener(v -> startActivity(new Intent(SettingsActivity.this, EditProfileActivity.class)));
    }


    private void onClearRoomDatabase() {

        binding.ClearMyList.setOnClickListener(v -> {

            final Dialog dialog = new Dialog(SettingsActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.clear_mylist);
            dialog.setCancelable(true);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;



            dialog.findViewById(R.id.bt_getcode).setOnClickListener(x -> {

                moviesListViewModel.deleteAllMovies();

                Toast.makeText(this, "My List has been cleared !", Toast.LENGTH_SHORT).show();

                dialog.dismiss();


            });

            dialog.findViewById(R.id.bt_close).setOnClickListener(x -> dialog.dismiss());


            dialog.show();
            dialog.getWindow().setAttributes(lp);

        });


    }

    public void onClearCache() {

        binding.linearLayoutCleaCache.setOnClickListener(v -> {


            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.clear_cache);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;


            dialog.findViewById(R.id.bt_getcode).setOnClickListener(x -> {
                deleteCache(this);

                Toast.makeText(this, "The App cache has been cleared !", Toast.LENGTH_SHORT).show();


                dialog.dismiss();

            });

            dialog.findViewById(R.id.bt_close).setOnClickListener(x -> dialog.dismiss());


            dialog.show();
            dialog.getWindow().setAttributes(lp);


        });


    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteFile(dir);
        } catch (Exception e) {

            Timber.d("Error Deleting : %s", e.getMessage());
        }
    }

    public static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (String child : children) {
                    deletedAll = deleteFile(new File(file, child)) && deletedAll;
                }
            } else {

                boolean deleted = file.delete();
                if (!deleted) {

                    Timber.i("File Is not Deleted");
                } else {

                    Timber.i("File Deleted");
                }
            }
        }

        return deletedAll;
    }


    private void onLoadPrivacyPolicy() {


        binding.privacyPolicy.setOnClickListener(v -> {

            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.dialog_gdpr_basic);
            dialog.setCancelable(true);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            TextView reportMovieName = dialog.findViewById(R.id.tv_content);
            reportMovieName.setText(settingsManager.getSettings().getPrivacyPolicy());

            dialog.findViewById(R.id.bt_accept).setOnClickListener(v1 -> dialog.dismiss());

            dialog.findViewById(R.id.bt_decline).setOnClickListener(v12 -> dialog.dismiss());


            dialog.show();
            dialog.getWindow().setAttributes(lp);


        });


    }

    @SuppressLint("DefaultLocale")
    private void onLoadAboutUs() {


        // About Us - EasyPlex
        binding.aboutus.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.dialog_about);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


            ImageView imageView = dialog.findViewById(R.id.logo_aboutus);
            TextView textView = dialog.findViewById(R.id.app_version);
            textView.setText(String.format("%s%s", getString(R.string.version), settingsManager.getSettings().getLatestVersion()));
            Tools.loadMainLogo(this,imageView);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            dialog.findViewById(R.id.bt_getcode).setOnClickListener(v15 -> {
                if (settingsManager.getSettings().getAppUrlAndroid().isEmpty()) {


                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://codecanyon.net/user/yobex")));

                } else {

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(settingsManager.getSettings().getAppUrlAndroid())));

                }

            });

            dialog.findViewById(R.id.bt_close).setOnClickListener(v14 -> dialog.dismiss());

            dialog.findViewById(R.id.app_url).setOnClickListener(v13 -> {

                if(settingsManager.getSettings().getAppUrlAndroid() != null && !settingsManager.getSettings().getAppUrlAndroid().trim().isEmpty()) {

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(settingsManager.getSettings().getAppUrlAndroid())));


                }else {

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://codecanyon.net/user/yobex")));

                }

            });

            dialog.show();
            dialog.getWindow().setAttributes(lp);

        });



        if (tokenManager.getToken().getAccessToken() != null) {


            binding.btnEditProfile.setVisibility(View.VISIBLE);
            binding.logout.setVisibility(View.VISIBLE);

        }else {

            binding.btnEditProfile.setVisibility(GONE);
            binding.logout.setVisibility(View.GONE);
            binding.subcribeButton.setVisibility(GONE);

        }


    }

    private void onLogout() {

        binding.logout.setOnClickListener(v -> {

            tokenManager.deleteToken();
            authManager.deleteAuth();
            settingsManager.deleteSettings();
            adsManager.deleteAds();
            moviesListViewModel.deleteHistory();
            deleteCache(this);
            moviesListViewModel.deleteAllMovies();
            startActivity(new Intent(this, SplashActivity.class));
            finish();

        });

    }

    private void onCheckAuth() {


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


                        binding.loginCode.setText(auth.getLoginCode());



                        if (settingsManager.getSettings().getDeviceManagement() == 1){

                            NetworkUtils.getMacAdress(SettingsActivity.this);
                            if (NetworkUtils.getMacAdress(SettingsActivity.this).equals("null")){
                                return;
                            }

                            DeviceName.with(SettingsActivity.this).request((info, error) -> {

                                String name = info.getName();
                                String model = info.model;

                                authRepository.addDevice(NetworkUtils.getMacAdress(SettingsActivity.this),model,name)
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


                        menuHandler.isUserHasLogged.set(true);


                        binding.authType.setVisibility(View.VISIBLE);

                        if (auth.getPremuim() == 1) {

                            binding.authType.setText(auth.getPackName());

                            onCancelSubscription(auth,false);

                        }else {
                            binding.authType.setText(getString(R.string.free));
                        }






                        binding.authName.setVisibility(View.VISIBLE);
                        binding.authEmail.setVisibility(View.VISIBLE);
                        binding.profilePicture.setVisibility(View.VISIBLE);


                        if (!auth.getProfiles().isEmpty() && !sharedPreferences.getBoolean(ISUSER_MAIN_ACCOUNT,false)){

                            binding.authName.setText(authManager.getSettingsProfile().getName());
                            Tools.loadUserAvatar(getApplicationContext(),binding.profilePicture,authManager.getSettingsProfile().getAvatar());


                        }else {

                            binding.authName.setText(auth.getName());
                            Tools.loadUserAvatar(getApplicationContext(),binding.profilePicture,auth.getAvatar());

                        }


                        binding.authEmail.setText(auth.getEmail());
                        binding.btnLogin.setVisibility(GONE);

                        if (auth.getPremuim() == 1) {


                            sharedPreferencesEditor.putInt(PREMUIM, 1).apply();
                            binding.membershipExpireIn.setVisibility(View.VISIBLE);

                            if (auth.getExpiredIn() != null && !auth.getExpiredIn().trim().isEmpty()) {
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date releaseDate = sdf1.parse(auth.getExpiredIn());
                                    assert releaseDate != null;
                                    binding.membershipExpireIn.setText("Valid until : "+sdf1.format(releaseDate));
                                } catch (ParseException e) {

                                    Timber.d("%s", Arrays.toString(e.getStackTrace()));

                                }
                            } else {
                                binding.membershipExpireIn.setText("");
                            }


                            binding.subcribeButton.setVisibility(GONE);
                            binding.cancelSubcriptionButton.setVisibility(View.VISIBLE);
                            binding.cancelSubcriptionButton.setOnClickListener(v -> {


                                final Dialog dialog = new Dialog(SettingsActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.dialog_confirm_cancel_subscription);
                                dialog.setCancelable(true);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(dialog.getWindow().getAttributes());
                                lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;


                                dialog.findViewById(R.id.bt_getcode).setOnClickListener(x -> {

                                    onCancelSubscription(auth,true);
                                    dialog.dismiss();

                                });

                                dialog.findViewById(R.id.bt_close).setOnClickListener(x -> dialog.dismiss());


                                dialog.show();
                                dialog.getWindow().setAttributes(lp);


                            });

                        } else {

                            sharedPreferencesEditor.putInt(PREMUIM, 0).apply();
                            binding.subcribeButton.setVisibility(View.VISIBLE);

                        }




                    }


                    @Override
                    public void onError(@NotNull Throwable e) {

                        menuHandler.isUserHasLogged.set(false);

                        binding.authEmail.setVisibility(GONE);
                        binding.authEmail.setVisibility(View.GONE);
                        binding.profilePicture.setVisibility(GONE);
                        binding.btnLogin.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onComplete() {

                        //

                    }
                });

    }


    private void onCancelSubscription(UserAuthInfo auth , boolean isCancelClicked){


        if (isCancelClicked){

            loginViewModel.cancelAuthSubscriptionPaypal();
            loginViewModel.authCancelPaypalMutableLiveData.observe(SettingsActivity.this, cancelsubs -> {

                Tools.ToastHelper(SettingsActivity.this,SUBSCRIPTIONS);
                startActivity(new Intent(SettingsActivity.this, SplashActivity.class));
                finish();

            });

        }else {


            if (auth.getType() !=null && !auth.getType().isEmpty()) {

                if ("paypal".equals(auth.getType())) {

                    loginViewModel.getExpirationStatusDetails();

                    loginViewModel.expiredMutableLiveData.observe(SettingsActivity.this, authx -> {


                        if (authx.getSubscription().equals("expired")) {

                            loginViewModel.cancelAuthSubscriptionPaypal();
                            loginViewModel.authCancelPaypalMutableLiveData.observe(SettingsActivity.this, cancelsubs -> {

                                Tools.ToastHelper(SettingsActivity.this,SUBSCRIPTIONS);
                                startActivity(new Intent(SettingsActivity.this, SplashActivity.class));
                                finish();

                            });

                        }

                    });


                } else if ("stripe".equals(auth.getType())) {

                    loginViewModel.getAuthDetails();
                    loginViewModel.getStripeSubStatusDetails();

                    loginViewModel.stripeStatusDetailMutableLiveData.observe(SettingsActivity.this, authx -> {

                        if (authx.getActive() <= 0) {

                            loginViewModel.cancelAuthSubscription();
                            loginViewModel.authCancelPlanMutableLiveData.observe(SettingsActivity.this, cancelsubs -> {

                                if (cancelsubs != null) {

                                    Tools.ToastHelper(SettingsActivity.this,SUBSCRIPTIONS);
                                    startActivity(new Intent(SettingsActivity.this, SplashActivity.class));
                                    finish();
                                }

                            });

                        }

                    });
                }else {

                    loginViewModel.getExpirationStatusDetails();

                    loginViewModel.expiredMutableLiveData.observe(SettingsActivity.this, authx -> {


                        if (authx.getSubscription().equals("expired")) {

                            loginViewModel.cancelAuthSubscriptionPaypal();
                            loginViewModel.authCancelPaypalMutableLiveData.observe(SettingsActivity.this, cancelsubs -> {


                                if (android.os.Build.VERSION.SDK_INT == 25) {
                                    ToastCompat.makeText(SettingsActivity.this, SUBSCRIPTIONS, Toast.LENGTH_SHORT)
                                            .setBadTokenListener(toast -> Timber.e("Failed to toast")).show();
                                } else {
                                    Toast.makeText(SettingsActivity.this, SUBSCRIPTIONS, Toast.LENGTH_SHORT).show();
                                }
                                startActivity(new Intent(SettingsActivity.this, SplashActivity.class));
                                finish();

                            });

                        }

                    });
                }

            }else {

                loginViewModel.getExpirationStatusDetails();

                loginViewModel.expiredMutableLiveData.observe(SettingsActivity.this, authx -> {


                    if (authx.getSubscription().equals("expired")) {

                        loginViewModel.cancelAuthSubscriptionPaypal();
                        loginViewModel.authCancelPaypalMutableLiveData.observe(SettingsActivity.this, cancelsubs -> {


                            if (android.os.Build.VERSION.SDK_INT == 25) {
                                ToastCompat.makeText(SettingsActivity.this, SUBSCRIPTIONS, Toast.LENGTH_SHORT)
                                        .setBadTokenListener(toast -> Timber.e("Failed to toast")).show();
                            } else {
                                Toast.makeText(SettingsActivity.this, SUBSCRIPTIONS, Toast.LENGTH_SHORT).show();
                            }
                            startActivity(new Intent(SettingsActivity.this, SplashActivity.class));
                            finish();

                        });

                    }

                });
            }
        }


    }

    // Load AppBar
    private void onLoadAppBar() {

        Tools.loadAppBar(binding.scrollView, binding.toolbar.toolbar);

    }


    // Load App Logo
    private void onLoadAppLogo() {

        Tools.loadMiniLogo(this,binding.toolbar.logoImageTop);

    }



    private void setButtonsUtilities() {

        onLoadwifiSwitch();
        onLoadNotificationPushSwitch();
        setAutoPlaySwitch();
        setExtentions();
        setSoftwareCodec();

        binding.currentSize.setText(String.format(getString(R.string.current_subtitle), sharedPreferences.getString(SUBS_SIZE, "16f")));

        binding.currentSubsDefaultLang.setText(String.format(getString(R.string.current_default_lang2), sharedPreferences.getString(SUBS_DEFAULT_LANG, SUBS_DEFAULT_LANG_NAME)));



        binding.substitleSize.setOnClickListener(v -> {

            ArrayList<String> fontSize = new ArrayList<>();


            fontSize.add("10f");
            fontSize.add("12f");
            fontSize.add("14f");
            fontSize.add("16f");
            fontSize.add("20f");
            fontSize.add("24f");
            fontSize.add("30f");

            String[] charSequenceSubsSize = new String[fontSize.size()];
            for (int i = 0; i < fontSize.size(); i++) {
                charSequenceSubsSize[i] = String.valueOf(fontSize.get(i));

            }

            final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme);
            builder.setTitle(R.string.fonts_size);
            builder.setCancelable(true);
            builder.setItems(charSequenceSubsSize, (dialogInterface, wich) -> {

                sharedPreferencesEditor.putString(SUBS_SIZE, fontSize.get(wich)).apply();
                sharedPreferences.getString(SUBS_SIZE, "16f");
                binding.currentSize.setText(String.format(getString(R.string.current_subtitle), sharedPreferences.getString(SUBS_SIZE, "16f")));

                dialogInterface.dismiss();


            });

            builder.show();

        });

        binding.currentBackgroundColor.setText(String.format(getString(R.string.current_color), sharedPreferences.getString(SUBS_BACKGROUND, "Transparent")));


        binding.substitlesBackground.setOnClickListener(v -> {

            ArrayList<String> fontSize = new ArrayList<>();

            fontSize.add("Transparent");
            fontSize.add("Black");
            fontSize.add("Grey");
            fontSize.add("Red");
            fontSize.add("Yellow");
            fontSize.add("Green");
            fontSize.add("Blue");

            String[] charSequenceSubsSize = new String[fontSize.size()];
            for (int i = 0; i < fontSize.size(); i++) {
                charSequenceSubsSize[i] = String.valueOf(fontSize.get(i));

            }

            final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme);
            builder.setTitle(R.string.bg_font_color);
            builder.setCancelable(true);
            builder.setItems(charSequenceSubsSize, (dialogInterface, wich) -> {

                sharedPreferencesEditor.putString(SUBS_BACKGROUND, fontSize.get(wich)).apply();
                sharedPreferences.getString(SUBS_BACKGROUND, "Transparent");
                binding.currentBackgroundColor.setText(String.format(getString(R.string.current_color), sharedPreferences.getString(SUBS_BACKGROUND, "Transparent")));

                dialogInterface.dismiss();


            });

            builder.show();

        });





        binding.currentPlayerAspectRatio.setText(String.format(getString(R.string.current_aspect_ratio), sharedPreferences.getString(PLAYER_ASPECT_RATIO, "default")));


        binding.playerAspectRatio.setOnClickListener(v -> {

            ArrayList<String> fontSize = new ArrayList<>();

            fontSize.add(getString(R.string.player_default));
            fontSize.add(getString(R.string.player_43));
            fontSize.add(getString(R.string.player_169));
            fontSize.add(getString(R.string.player_fullscreen));
            fontSize.add(getString(R.string.player_room));

            String[] charSequenceSubsSize = new String[fontSize.size()];
            for (int i = 0; i < fontSize.size(); i++) {
                charSequenceSubsSize[i] = String.valueOf(fontSize.get(i));

            }

            final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme);
            builder.setTitle(getString(R.string.aspect_ratio));
            builder.setCancelable(true);
            builder.setItems(charSequenceSubsSize, (dialogInterface, wich) -> {

                sharedPreferencesEditor.putString(PLAYER_ASPECT_RATIO, fontSize.get(wich)).apply();
                sharedPreferences.getString(PLAYER_ASPECT_RATIO, "default");
                binding.currentPlayerAspectRatio.setText(String.format(getString(R.string.current_aspect_ratio), sharedPreferences.getString(PLAYER_ASPECT_RATIO, "default")));

                dialogInterface.dismiss();


            });

            builder.show();

        });


    }

    private void setSoftwareCodec() {


        if (!sharedPreferences.getBoolean(SOFTWARE_EXTENTIONS, false)) {
            binding.switchCodecSoftware.setChecked(false);
        }

        binding.switchCodecSoftware.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

                sharedPreferencesEditor.putBoolean(SOFTWARE_EXTENTIONS, true).apply();


            } else {

                sharedPreferencesEditor.putBoolean(SOFTWARE_EXTENTIONS, false).apply();

            }

        });

    }

    private void setExtentions() {


        if (!sharedPreferences.getBoolean(EXTENTIONS, false)) {
            binding.switchPushExtentions.setChecked(false);
        }

        binding.switchPushExtentions.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

                sharedPreferencesEditor.putBoolean(EXTENTIONS, true).apply();


            } else {

                sharedPreferencesEditor.putBoolean(EXTENTIONS, false).apply();

            }

        });

    }

    private void setAutoPlaySwitch() {

        // Detect AutoPlay ON/OFF Button switch - EasyPlex
        if (!sharedPreferences.getBoolean(AUTO_PLAY, true)) {
            binding.autoplaySwitch.setChecked(false);
        }

        binding.autoplaySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

                sharedPreferencesEditor.putBoolean(AUTO_PLAY, true).apply();


            } else {

                sharedPreferencesEditor.putBoolean(AUTO_PLAY, false).apply();

            }

        });

    }

    private void onLoadNotificationPushSwitch() {

        // Detect Notification ON/OFF Button switch - EasyPlex
        if (!sharedPreferences.getBoolean(SWITCH_PUSH_NOTIFICATION, true)) {

            binding.switchPushNotification.setChecked(false);
        }


        binding.switchPushNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

                sharedPreferencesEditor.putBoolean(SWITCH_PUSH_NOTIFICATION, true).apply();

                FirebaseMessaging.getInstance().subscribeToTopic("all");

            } else {

                sharedPreferencesEditor.putBoolean(SWITCH_PUSH_NOTIFICATION, false).apply();

                FirebaseMessaging.getInstance().unsubscribeFromTopic("all");

            }

        });

    }

    private void onLoadwifiSwitch() {

        // Detect Wifi-Only Button switch - EasyPlex
        if (!sharedPreferences.getBoolean(WIFI_CHECK, true)) {

            binding.wifiSwitch.setChecked(false);

        }

        binding.wifiSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

                sharedPreferencesEditor.putBoolean(WIFI_CHECK, true).apply();

            } else {

                sharedPreferencesEditor.putBoolean(WIFI_CHECK, false).apply();
            }

        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding = null;

    }

    private void onDeviceManage(View v) {


        if (tokenManager.getToken().getAccessToken() == null) {

            Toast.makeText(this, R.string.you_must_login_before_manage_devices, Toast.LENGTH_SHORT).show();
            return;
        }


        authRepository.getAuth()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                        //

                    }

                    @Override
                    public void onNext(@NonNull UserAuthInfo userAuthInfo) {


                        startActivity(new Intent(SettingsActivity.this, UserDevicesManagement.class));

                    }


                    @Override
                    public void onError(@NotNull Throwable e) {


                        Toast.makeText(SettingsActivity.this, R.string.your_token_has_expired_try_to_login_again, Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(SettingsActivity.this, BaseActivity.class));
                        finish();
                    }

                    @Override
                    public void onComplete() {

                        //

                    }
                });


    }
}

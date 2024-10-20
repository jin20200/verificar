package com.easyplexdemoapp.ui.base;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.easyplexdemoapp.util.Constants.EXTENTIONS;
import static com.easyplexdemoapp.util.Constants.SOFTWARE_EXTENTIONS;
import static com.easyplexdemoapp.util.Constants.SWITCH_PUSH_NOTIFICATION;
import static com.easyplexdemoapp.util.Constants.WIFI_CHECK;
import static com.easyplexdemoapp.util.Tools.onloadBanners;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.appodeal.ads.Appodeal;
import com.download.library.DownloadImpl;
import com.download.library.DownloadListenerAdapter;
import com.download.library.Extra;
import com.easyplexdemoapp.BuildConfig;
import com.easyplexdemoapp.EasyPlexApp;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.repository.AuthRepository;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.databinding.ActivityMainBinding;
import com.easyplexdemoapp.databinding.DialogUpdateAlertBinding;
import com.easyplexdemoapp.di.Injectable;
import com.easyplexdemoapp.ui.downloadmanager.ui.PermissionDeniedDialog;
import com.easyplexdemoapp.ui.downloadmanager.ui.PermissionManager;
import com.easyplexdemoapp.ui.downloadmanager.ui.main.DownloadManagerFragment;
import com.easyplexdemoapp.ui.home.HomeFragment;
import com.easyplexdemoapp.ui.library.LibraryFragment;
import com.easyplexdemoapp.ui.login.LoginActivity;
import com.easyplexdemoapp.ui.manager.AdsManager;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.StatusManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.ui.mylist.ListFragment;
import com.easyplexdemoapp.ui.player.cast.GoogleServicesHelper;
import com.easyplexdemoapp.ui.player.cast.queue.ui.QueueListViewActivity;
import com.easyplexdemoapp.ui.player.cast.settings.CastPreference;
import com.easyplexdemoapp.ui.player.views.NotificationBadgeView;
import com.easyplexdemoapp.ui.receiver.NetworkChangInterface;
import com.easyplexdemoapp.ui.receiver.NetworkChangeReceiver;
import com.easyplexdemoapp.ui.search.DiscoverFragment;
import com.easyplexdemoapp.ui.settings.SettingsActivity;
import com.easyplexdemoapp.ui.iptv.IptvPlaylistFragment;
import com.easyplexdemoapp.ui.streaming.StreamingFragment;
import com.easyplexdemoapp.ui.users.MenuHandler;
import com.easyplexdemoapp.util.Constants;
import com.easyplexdemoapp.util.DialogHelper;
import com.easyplexdemoapp.util.RootCheckUtil;
import com.easyplexdemoapp.util.Tools;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ironsource.mediationsdk.IronSource;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.AndroidInjection;
import io.github.g00fy2.versioncompare.Version;
import timber.log.Timber;

public class BaseActivity extends AppCompatActivity implements Injectable , NetworkChangInterface {



    private static final String TAG_PERM_DENIED_DIALOG = "perm_denied_dialog";

    private Dialog dialogCnxMonitor;

    @Inject
    MenuHandler menuHandler;


    @Inject
    NetworkChangeReceiver networkChangeReceiver;


    @Inject
    @Named("sniffer")
    @Nullable
    ApplicationInfo provideSnifferCheck;


    @Inject
    @Named("root")
    @Nullable
    ApplicationInfo provideRootCheck;


    @Inject
    @Named("vpn")
    boolean checkVpn;


    @Inject
    MediaRepository repository;


    ActivityMainBinding mainBinding;

    @Inject
    TokenManager tokenManager;


    @Inject
    AuthRepository authRepository;

    @Inject
    @Named("ready")
    boolean settingReady;

    @Inject
    AuthManager authManager;


    @Inject
    SettingsManager settingsManager;


    @Inject
    AdsManager adsManager;


    @Inject
    StatusManager statusManager;


    @Inject
    SharedPreferences sharedPreferences;


    @Inject
    SharedPreferences.Editor editor;

    @Inject ViewModelProvider.Factory viewModelFactory;


    FragmentManager manager;

    private CastContext mCastContext;
    public SessionManagerListener<CastSession> mSessionManagerListener =
            new MySessionManagerListener();
    private CastSession mCastSession;

    private MenuItem mediaRouteMenuItem;
    private MenuItem mQueueMenuItem;
    private IntroductoryOverlay mIntroductoryOverlay;
    private CastStateListener mCastStateListener;
    private PermissionManager permissionManager;

    private PermissionDeniedDialog permDeniedDialog;


    @Override
    public void onConnected() {

        menuHandler.isNetworkActive.set(true);

        if (dialogCnxMonitor !=null){


            dialogCnxMonitor.cancel();
        }


    }


    @Override
    public void onLostConnexion() {

        menuHandler.isNetworkActive.set(false);


        dialogCnxMonitor = new Dialog(this);
        dialogCnxMonitor.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCnxMonitor.setContentView(R.layout.dialog_monitor_cnx);
        dialogCnxMonitor.setCancelable(false);
        dialogCnxMonitor.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogCnxMonitor.getWindow().getAttributes());

        lp.gravity = Gravity.BOTTOM;
        lp.width = MATCH_PARENT;
        lp.height = MATCH_PARENT;

        dialogCnxMonitor.show();
        dialogCnxMonitor.getWindow().setAttributes(lp);
    }

    @Override
    public void onHttpFetchFailure( boolean isFetched) {

        if (isFetched){

            Toast.makeText(this, "is failed to fetch data", Toast.LENGTH_SHORT).show();
        }
    }

    public class MySessionManagerListener implements SessionManagerListener<CastSession> {

        @Override
        public void onSessionEnded(@NotNull CastSession session, int error) {
            if (session == mCastSession) {
                mCastSession = null;
            }
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionResumed(@NotNull CastSession session, boolean wasSuspended) {
            mCastSession = session;
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionStarted(@NotNull CastSession session, @NotNull String sessionId) {

            mCastSession = session;
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionStarting(@NotNull CastSession session) {

            //
        }

        @Override
        public void onSessionStartFailed(@NotNull CastSession session, int error) {

            Toast.makeText(BaseActivity.this, getString(R.string.unable_cast), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onSessionEnding(@NotNull CastSession session) {

            invalidateOptionsMenu();
        }

        @Override
        public void onSessionResuming(@NotNull CastSession session, @NotNull String sessionId) {

            //
        }

        @Override
        public void onSessionResumeFailed(@NotNull CastSession session, int error) {

            //
        }

        @Override
        public void onSessionSuspended(@NotNull CastSession session, int reason) {

            //
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        manager = getSupportFragmentManager();




        checkBatteryOptimizationSettings();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

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


            if (!permissionManager.checkPermissions() && permDeniedDialog == null) {
                permissionManager.requestPermissions();
            }
        }else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
            }
        }



        mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mainBinding.setMenu(menuHandler);







        menuHandler.isHomeBannerEnabled.set(settingsManager.getSettings().getEnableBannerBottom() == 1);

        IntentFilter intentFilter = new IntentFilter(CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);

        networkChangeReceiver.setNetworkChangeInterface(this);

        Tools.onCheckFlagSecure(settingsManager.getSettings().getFlagSecure(),this);

        Tools.setSystemBarTransparent(this);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Tools.id(this));
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);



        if (GoogleServicesHelper.available(this)) {

            mCastContext = CastContext.getSharedInstance(this);

            mCastStateListener = newState -> {
                if (newState != CastState.NO_DEVICES_AVAILABLE) {
                    showIntroductoryOverlay();
                }
            };
        }


        if (settingsManager.getSettings().getEnableCustomMessage() == 1 && Tools.checkIfHasNetwork(getApplicationContext())) {
            DialogHelper.showCustomAlert( this,settingsManager.getSettings().getCustomMessage());
        }


        wifiCheck();
        notificationManager();
        setExtentions();
        onSetHomePageSettings();
    }



    private void checkBatteryOptimizationSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            String packageName = getPackageName();

            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                showBatteryOptimizationDialog(packageName);
            }
        }
    }

    private void showBatteryOptimizationDialog(String packageName) {
        new AlertDialog.Builder(this,R.style.MyAlertDialogTheme)
                .setTitle("Battery Optimization")
                .setMessage("To ensure you receive timely notifications, please disable battery optimization for this app.")
                .setPositiveButton("Go to Settings", (dialog, which) -> {
                    openBatteryOptimizationSettings(packageName);
                })
                .setNegativeButton("Later", null)
                .show();
    }

    private void openBatteryOptimizationSettings(String packageName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);
        }
    }



    @Override
    protected void onStart() {


        if (settingsManager.getSettings().getIronsourceBanner() == 1) {

            IronSource.init(this, settingsManager.getSettings().getIronsourceAppKey(), IronSource.AD_UNIT.BANNER);

        }

        if (settingsManager.getSettings().getAppodealBanner() == 1){

            Appodeal.initialize(this, settingsManager.getSettings().getAdUnitIdAppodealRewarded(), Appodeal.BANNER, errors -> {
                // Appodeal initialization finished
            });

        }


        super.onStart();


    }

    private void onSetHomePageSettings() {

        if (authManager.getUserInfo().getPremuim() != 1 ) {
            onLoadBottomBanners();
        }



        mainBinding.navigation.getMenu().findItem(R.id.navigation_live).setVisible(settingsManager.getSettings().getStreaming() != 0 && settingsManager.getSettings().getSafemode() != 1);
        mainBinding.navigation.getMenu().findItem(R.id.navigation_download).setVisible(settingsManager.getSettings().getEnableDownload() != 0 && settingsManager.getSettings().getSafemode() != 1);
        
        if (settingsManager.getSettings().getMantenanceMode() == 0) {

            onNavigationUI();

        }else {

            mainBinding.navigation.setVisibility(View.GONE);

        }


        if (EasyPlexApp.hasNetwork()){

            changeFragment(new HomeFragment(), HomeFragment.class.getSimpleName());
            onLoadCheckVersion();

        }else {

            unregisterReceiver(networkChangeReceiver);

            changeFragment(new DownloadManagerFragment(), DownloadManagerFragment.class
                    .getSimpleName());

            mainBinding.navigation.setSelectedItemId(R.id.navigation_download);
        }


        if(tokenManager.getToken() == null){
            startActivity(new Intent(this, LoginActivity.class));
            finish();

        }

        if (settingsManager.getSettings().getVpn() ==1 && checkVpn){

          finishAffinity();

            Toast.makeText(this, R.string.vpn_message, Toast.LENGTH_SHORT).show();

        }
    }


    private void onLoadBottomBanners() {


        if (settingsManager.getSettings().getEnableBannerBottom() != 1 || authManager.getUserInfo().getPremuim() == 1) {
            return;
        }

        if (authManager.getUserInfo().getPremuim() != 1 ) {

            onloadBanners(settingsManager,BaseActivity.this,null, null,null, null,mainBinding);


        }

    }





    private void setExtentions() {


        if (sharedPreferences.getBoolean(EXTENTIONS,true)){

            editor.putBoolean(EXTENTIONS, true).apply();

        }else {

            editor.putBoolean(EXTENTIONS, false).apply();
        }


        if (sharedPreferences.getBoolean(SOFTWARE_EXTENTIONS,true)){

            editor.putBoolean(SOFTWARE_EXTENTIONS, true).apply();

        }else {

            editor.putBoolean(SOFTWARE_EXTENTIONS, false).apply();
        }

    }


    private void wifiCheck() {

        if (!sharedPreferences.getBoolean(WIFI_CHECK, true)) {

            editor.putBoolean(WIFI_CHECK, false).apply();

        }
    }


    public void changeFragment(Fragment fragment, String tagFragmentName) {

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            fragmentTransaction.add(R.id.fragment_container, fragmentTemp, tagFragmentName);
        } else {
            fragmentTransaction.show(fragmentTemp);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();

    }



    @SuppressLint("NonConstantResourceId")
    private void onNavigationUI() {
        mainBinding.navigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_search) {
                changeFragment(new DiscoverFragment(), DiscoverFragment.class
                        .getSimpleName());
            } else if (itemId == R.id.navigation_browse) {
                changeFragment(new LibraryFragment(), LibraryFragment.class.getSimpleName());
            } else if (itemId == R.id.navigation_download) {
                changeFragment(new DownloadManagerFragment(), DownloadManagerFragment.class
                        .getSimpleName());
            } else if (itemId == R.id.navigation_live) {

                if (settingsManager.getSettings().getM3uplaylist() == 1){


                    changeFragment(new IptvPlaylistFragment(), IptvPlaylistFragment.class
                            .getSimpleName());
                }else {

                    changeFragment(new StreamingFragment(), StreamingFragment.class
                            .getSimpleName());
                }

            } else {
                changeFragment(new HomeFragment(), HomeFragment.class
                        .getSimpleName());
            }
            return true;
        });
    }


    private void showIntroductoryOverlay() {
        if (mIntroductoryOverlay != null) {
            mIntroductoryOverlay.remove();
        }
        if ((mediaRouteMenuItem != null) && mediaRouteMenuItem.isVisible()) {
            new Handler().post(() -> {
                mIntroductoryOverlay = new IntroductoryOverlay.Builder(
                        BaseActivity.this, mediaRouteMenuItem)
                        .setTitleText(getString(R.string.introducing_cast))
                        .setOverlayColor(R.color.main_color)
                        .setSingleTime()
                        .setOnOverlayDismissedListener(
                                () -> mIntroductoryOverlay = null)
                        .build();
                mIntroductoryOverlay.show();
            });
        }
    }

    @Override
    protected void onResume() {

        if (GoogleServicesHelper.available(this)) {
        mCastContext.addCastStateListener(mCastStateListener);
        mCastContext.getSessionManager().addSessionManagerListener(
                mSessionManagerListener, CastSession.class);
        if (mCastSession == null) {
            mCastSession = CastContext.getSharedInstance(this).getSessionManager()
                    .getCurrentCastSession();
        }
        }
        if (mQueueMenuItem != null) {
            mQueueMenuItem.setVisible(
                    (mCastSession != null) && mCastSession.isConnected());
        }

        if (provideSnifferCheck != null) {
            Toast.makeText(BaseActivity.this, R.string.sniffer_message, Toast.LENGTH_SHORT).show();
            finishAffinity();
        }

        if (settingsManager.getSettings().getRootDetection() == 1 && provideRootCheck != null && RootCheckUtil.isDeviceRooted()) {
            Toast.makeText(BaseActivity.this, R.string.rooted_message, Toast.LENGTH_SHORT).show();
            finishAffinity();
        }


        if (settingsManager.getSettings().getVpn() ==1 && checkVpn) {


            finishAffinity();

            Toast.makeText(this, R.string.vpn_message, Toast.LENGTH_SHORT).show();

        }

        super.onResume();

        String downloadOnProgress = getIntent().getStringExtra("download_on_progress");

        if (downloadOnProgress != null && downloadOnProgress.equals("yes")) {

            mainBinding.navigation.setSelectedItemId(R.id.navigation_download);
            changeFragment(new DownloadManagerFragment(), DownloadManagerFragment.class
                    .getSimpleName());

        }

    }

    @Override
    protected void onPause() {

        if (GoogleServicesHelper.available(this)) {
            mCastContext.removeCastStateListener(mCastStateListener);
            mCastContext.getSessionManager().removeSessionManagerListener(
                    mSessionManagerListener, CastSession.class);
        }
        super.onPause();
    }



    private void onLoadCheckVersion() {


        boolean result;

        result = new Version(BuildConfig.VERSION_NAME).isLowerThan(settingsManager.getSettings().getLatestVersion());

        if (result) {
            onSetupNewVersion();

        }



    }

    private void onSetupNewVersion() {
        // Inflating layout using View Binding
        DialogUpdateAlertBinding binding = DialogUpdateAlertBinding.inflate(getLayoutInflater());

        final Dialog dialog = new Dialog(BaseActivity.this);
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
                                intent.setDataAndType(FileProvider.getUriForFile(BaseActivity.this, BuildConfig.APPLICATION_ID + ".provider", new File(path.getPath()))
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



    // Top level Firebase Cloud Messaging singleton that provides methods for subscribing or unsubscribing

    private void notificationManager() {


        if (sharedPreferences.getBoolean(SWITCH_PUSH_NOTIFICATION, true)) {

            FirebaseMessaging.getInstance().subscribeToTopic("all");

        } else {

            FirebaseMessaging.getInstance().unsubscribeFromTopic("all");
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        showIntroductoryOverlay();

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;


        if (item.getItemId() == R.id.mylist) {

       changeFragment(new ListFragment(), ListFragment.class.getSimpleName());

        }else if (item.getItemId() == R.id.settings) {

        startActivity(new Intent(this, SettingsActivity.class));


        }else
        if (item.getItemId() == R.id.action_settings) {
            intent = new Intent(BaseActivity.this, CastPreference.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_show_queue) {
            intent = new Intent(BaseActivity.this, QueueListViewActivity.class);
            startActivity(intent);
        }
        return true;
    }




    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {

         return mCastContext.onDispatchVolumeKeyEventBeforeJellyBean(event) || super.dispatchKeyEvent(event);

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mainBinding.navigation.getSelectedItemId() == R.id.navigation_home) {
            doExitApp();
        } else {
            mainBinding.navigation.setSelectedItemId(R.id.navigation_home);
        }

    }


    public void doExitApp() {
        Tools.doExitApp(this);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCastContext = null;
        mediaRouteMenuItem = null;
        mQueueMenuItem = null;
        mSessionManagerListener = null;

        unregisterReceiver(networkChangeReceiver);


    }

}

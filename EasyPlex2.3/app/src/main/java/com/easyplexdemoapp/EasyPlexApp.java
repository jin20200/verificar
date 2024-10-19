package com.easyplexdemoapp;

import static com.easyplexdemoapp.util.Constants.SERVER_BASE_URL;
import static com.easyplexdemoapp.util.Constants.SERVER_FIREBASE_VALUE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import androidx.multidex.MultiDexApplication;
import com.applovin.sdk.AppLovinSdk;
import com.easyplex.easyplexsupportedhosts.EasyPlexSupportedHosts;
import com.easyplexdemoapp.di.AppInjector;
import com.easyplexdemoapp.ui.downloadmanager.core.DownloadNotifier;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.util.Constants;
import com.easyplexdemoapp.util.Tools;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.jaredrummler.android.device.DeviceName;
import com.stringcare.library.SC;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;
import com.vungle.warren.InitCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.error.VungleException;
import com.wortise.ads.WortiseSdk;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import timber.log.Timber;

/**
 * Application level class.
 *
 * @author Yobex.
 */
public class EasyPlexApp extends MultiDexApplication implements HasAndroidInjector {

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    SharedPreferences.Editor editor;

    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    @Inject
    SettingsManager settingsManager;

    @Inject
    @Named("app_hash_256")
    String apkChecksumVerifier;


    @Inject
    @Singleton
    @Named("firebaseRemoteUrl")
    FirebaseRemoteConfig provideFirebaseRemoteConfig;

    @SuppressLint("StaticFieldLeak")
    private static Context context;


    @Inject
    @Named("easyplexsupportedhost")
    EasyPlexSupportedHosts easyPlexSupportedHosts;


    @Override
    public void onCreate() {
        SC.init(this);
        super.onCreate();

        AppInjector.init(this);

        onLoadEasyplexSupportedHots();


        if (settingsManager.getSettings().getDeviceManagement() == 1){

            DeviceName.init(this);

        }

        if (BuildConfig.DEBUG) { Timber.plant(new Timber.DebugTree());}


        Timber.i("Creating EasyPlex Application");


        Timber.i(apkChecksumVerifier);

        EasyPlexApp.context = getApplicationContext();

        if (settingsManager.getSettings().getEnableDownload() == 1){

            DownloadNotifier downloadNotifier = DownloadNotifier.getInstance(this);
            downloadNotifier.makeNotifyChans();
            downloadNotifier.startUpdate();

        }


        if (Constants.FIREBASECONFIG){

            fetchAndActivateFirebaseRemoteConfig();
        }


        // Initialize the Mobile Ads SDK.
        if (settingsManager.getSettings().getAdInterstitial() == 1 || settingsManager.getSettings().getAdBanner() == 1 || settingsManager.getSettings().getAdUnitIdNativeEnable() == 1){

            MobileAds.initialize(context, initializationStatus -> {});
        }


        if (settingsManager.getSettings().getAdUnitIdFacebookInterstitialAudience()  !=null){

            // Initialize the Audience Network SDK
            AudienceNetworkAds.initialize(context);


        }

        if (settingsManager.getSettings().getVungleAppid() !=null){

            // Initialize Vungle Network SDK
            Vungle.init(settingsManager.getSettings().getVungleAppid(), context, new InitCallback() {
                @Override
                public void onSuccess() {
                    //
                }

                @Override
                public void onError(VungleException exception) {
                    //

                }

                @Override
                public void onAutoCacheAdAvailable(String placementId) {
                    // Ad has become available to play for a cache optimized placement
                }
            });

        }


        // Initialize the Unity Ads  Network SDK
        if (settingsManager.getSettings().getUnityGameId() !=null) {

            UnityAds.initialize(context, settingsManager.getSettings().getUnityGameId(), false, new IUnityAdsInitializationListener() {
                @Override
                public void onInitializationComplete() {
                    //
                }

                @Override
                public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
                    //
                }
            });
        }


        if (settingsManager.getSettings().getApplovin_banner() == 1
                || settingsManager.getSettings().getApplovinInterstitial() == 1 || settingsManager.getSettings().getApplovinRewardUnitid() != null || settingsManager.getSettings().getApplovin_native() ==1 ){

            AppLovinSdk.getInstance(context).setMediationProvider( "max" );
            AppLovinSdk.initializeSdk( context, configuration -> {
                //AppLovin SDK is initialized, start loading ads
            });


        }

        if (settingsManager.getSettings().getWortiseAppid() !=null){

            WortiseSdk.initialize(this, settingsManager.getSettings().getWortiseAppid());
        }
    }


    private void onLoadEasyplexSupportedHots() {
        if (settingsManager.getSettings().getHxfileApiKey() !=null && !settingsManager.getSettings().getHxfileApiKey().isEmpty())  {

            easyPlexSupportedHosts.setApikey(settingsManager.getSettings().getHxfileApiKey());
        }

        easyPlexSupportedHosts.setMainApiServer(SERVER_BASE_URL);
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



    public static boolean hasNetwork() {
        return Tools.checkIfHasNetwork(context);
    }



    public static String getSignatureValid() {


        String appSignature = null;

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
            Signature[] signatures = packageInfo.signatures;

            for (Signature signature : signatures) {
                // Check the signature details
                appSignature = signature.toCharsString();

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        return appSignature;

    }


    public static Context getContext() {
        return context;
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        AppInjector.init(this);
        return androidInjector;
    }




}

/*
 * Application has activities that is why we implement HasActivityInjector interface.
 * Activities have fragments so we have to implement HasFragmentInjector/HasSupportFragmentInjector
 * in our activities.
 * No child fragment and don’t inject anything in your fragments, no need to implement
 * HasSupportFragmentInjector.
 */

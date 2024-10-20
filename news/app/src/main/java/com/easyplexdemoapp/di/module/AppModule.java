package com.easyplexdemoapp.di.module;

import static android.content.Context.MODE_PRIVATE;
import static com.easyplexdemoapp.util.Constants.PREF_FILE;
import static java.lang.String.valueOf;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.room.Room;
import androidx.work.WorkManager;

import com.easyplex.easyplexsupportedhosts.EasyPlexSupportedHosts;
import com.easyplex.easyplexsupportedhosts.Utils.AdPlaying;
import com.easyplexdemoapp.BuildConfig;
import com.easyplexdemoapp.data.local.DatabaseMigration;
import com.easyplexdemoapp.data.local.EasyPlexDatabase;
import com.easyplexdemoapp.data.local.dao.AddedSearchDao;
import com.easyplexdemoapp.data.local.dao.AnimesDao;
import com.easyplexdemoapp.data.local.dao.DownloadDao;
import com.easyplexdemoapp.data.local.dao.HistoryDao;
import com.easyplexdemoapp.data.local.dao.MoviesDao;
import com.easyplexdemoapp.data.local.dao.NotificationDao;
import com.easyplexdemoapp.data.local.dao.ResumeDao;
import com.easyplexdemoapp.data.local.dao.SeriesDao;
import com.easyplexdemoapp.data.local.dao.StreamListDao;
import com.easyplexdemoapp.data.model.ads.AdMediaModel;
import com.easyplexdemoapp.data.model.ads.AdRetriever;
import com.easyplexdemoapp.data.model.ads.CuePointsRetriever;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.data.model.status.Status;
import com.easyplexdemoapp.data.remote.ApiInterface;
import com.easyplexdemoapp.data.remote.ServiceGenerator;
import com.easyplexdemoapp.data.repository.AuthRepository;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.di.ViewModelModule;
import com.easyplexdemoapp.ui.casts.FilmographieAdapter;
import com.easyplexdemoapp.ui.certifications.CertificationAdapter;
import com.easyplexdemoapp.ui.devices.DevicesManagementAdapter;
import com.easyplexdemoapp.ui.home.adapters.MultiDataAdapter;
import com.easyplexdemoapp.ui.home.adapters.ByGenreAdapter;
import com.easyplexdemoapp.ui.home.adapters.RelatedsAdapter;
import com.easyplexdemoapp.ui.languages.LanguagesAdapter;
import com.easyplexdemoapp.ui.library.ItemAdapter;
import com.easyplexdemoapp.ui.manager.AdsManager;
import com.easyplexdemoapp.ui.manager.AppBehaviorManager;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.DeviceManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.StatusManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.ui.moviedetails.adapters.CastAdapter;
import com.easyplexdemoapp.ui.player.bindings.PlayerController;
import com.easyplexdemoapp.ui.player.controller.PlayerAdLogicController;
import com.easyplexdemoapp.ui.player.controller.PlayerUIController;
import com.easyplexdemoapp.ui.player.fsm.callback.AdInterface;
import com.easyplexdemoapp.ui.player.fsm.callback.Callback;
import com.easyplexdemoapp.ui.player.fsm.callback.CuePointCallBack;
import com.easyplexdemoapp.ui.player.fsm.callback.CuePointCallBackAd;
import com.easyplexdemoapp.ui.player.fsm.callback.RetrieveAdCallback;
import com.easyplexdemoapp.ui.player.fsm.concrete.FetchCuePointState;
import com.easyplexdemoapp.ui.player.fsm.concrete.factory.StateFactory;
import com.easyplexdemoapp.ui.player.fsm.listener.AdPlayingMonitor;
import com.easyplexdemoapp.ui.player.fsm.listener.CuePointMonitor;
import com.easyplexdemoapp.ui.player.fsm.state_machine.Fsm;
import com.easyplexdemoapp.ui.player.fsm.state_machine.FsmPlayer;
import com.easyplexdemoapp.ui.player.fsm.state_machine.FsmPlayerApi;
import com.easyplexdemoapp.ui.player.fsm.state_machine.FsmPlayerImperial;
import com.easyplexdemoapp.ui.player.interfaces.VpaidClient;
import com.easyplexdemoapp.ui.player.utilities.PlaybackSettingMenu;
import com.easyplexdemoapp.ui.receiver.NetworkChangeReceiver;
import com.easyplexdemoapp.ui.splash.ConfigurationAdapter;
import com.easyplexdemoapp.ui.users.MenuHandler;
import com.easyplexdemoapp.ui.users.ProfilesAdapter;
import com.easyplexdemoapp.util.ApkChecksumVerifier;
import com.easyplexdemoapp.util.AppController;
import com.easyplexdemoapp.util.ItemAnimation;
import com.easyplexdemoapp.util.LoadingStateController;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.jetbrains.annotations.NotNull;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import timber.log.Timber;


/**
 * Application-wide dependencies.
 * Module means the class which contains methods who will provide dependencies.
 *
 * @author Yobex.
 */


@Module(includes = ViewModelModule.class)
public
class AppModule {


    private String uniqueID = null;

    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";


    @Provides
    @Singleton
    FilmographieAdapter filmographieAdapter(Application application){

        return new FilmographieAdapter(application.getApplicationContext(),appControllerProvide(),provideSettingsManager(application));
    }

    @Provides
    @Singleton
    RelatedsAdapter relatedsAdapter(Application application) {

    return new RelatedsAdapter(appControllerProvide(),provideSettingsManager(application));

    }

    @Provides
    @Singleton
    CastAdapter castAdapter(Application application){

        return new CastAdapter(provideSettingsManager(application),application.getApplicationContext());
    }



    @Provides
    @Singleton
    @Named("easyplexsupportedhost")
    EasyPlexSupportedHosts easyPlexSupportedHosts(Application application){

        return new EasyPlexSupportedHosts(application.getApplicationContext());
    }


    @Provides
    @Singleton
    @Named("app_hash_256")
    String apkChecksumVerifier(Application application){

    return ApkChecksumVerifier.getApkSignatureSHA256(application);

    }

    @Provides
    @Singleton
    DeviceManager provideDeviceManager(Application application) {

        return new DeviceManager(application.getSharedPreferences(PREF_FILE,MODE_PRIVATE));
    }


    @Provides
    @Singleton
    ConfigurationAdapter configurationAdapter(){

        return new ConfigurationAdapter();
    }

    @Provides
    @Singleton
    NetworkChangeReceiver networkChangeReceiver() {

        return new NetworkChangeReceiver();
    }

    @Provides
    @Singleton
    MenuHandler provideMenuHandler() {

        return new MenuHandler();
    }


    @Provides
    @Singleton
    LoadingStateController loadingStateController() {
        return new LoadingStateController();
    }


    @Provides
    @Singleton
    AppController appControllerProvide() {
        return new AppController();
    }





    @Provides
    @Singleton
    @Named("api")
    public boolean getHomeApi(Application application) {

        return provideSettingsManager(application).getSettings().getStartappId().equals("1");

    }

    @Provides
    @Singleton
    @Named("getSignatureValid")
    public String getSignatureValid(Application application) {


        String appSignature = null;

        try {
            PackageInfo packageInfo;
            packageInfo = application.getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
            Signature[] signatures = packageInfo.signatures;

            for (Signature signature : signatures) {
                // Check the signature details
                String signatureString = signature.toCharsString();

                appSignature = signature.toCharsString();
                Timber.tag("Signature").d("Signature: %s", signatureString);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        return appSignature;

    }

    @Provides
    @Singleton
    @Named("isValid")
    public boolean isSignatureValid(Application application) {
        try {
            PackageInfo packageInfo = application.getPackageManager()
                    .getPackageInfo(application.getPackageName(), PackageManager.GET_SIGNATURES);

            // Assuming you have a known valid signature stored somewhere
            String expectedSignature = "YOUR_EXPECTED_SIGNATURE";

            for (android.content.pm.Signature signature : packageInfo.signatures) {
                String currentSignature = signature.toCharsString();
                if (expectedSignature.equals(currentSignature)) {
                    Timber.tag("SignatureVerifier").d("Signature is valid.");
                    return true;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Timber.tag("SignatureVerifier").d("Signature is not valid.");
        return false;
    }


    @Provides
    @Singleton
    LanguagesAdapter languagesAdapter(MediaRepository mediaRepository,ByGenreAdapter byGenreAdapter){

        return new LanguagesAdapter(mediaRepository,byGenreAdapter);
    }


    @Provides
    @Singleton
    CertificationAdapter certificationAdapter(){

        return new CertificationAdapter();
    }


    @Provides
    @Singleton
    ItemAdapter itemAdapter(Application application){

       return new  ItemAdapter(application, ItemAnimation.FADE_IN,provideSettingsManager(application));
    }


    @Provides
    @Singleton
    DevicesManagementAdapter devicesManagementAdapter(MediaRepository mediaRepository){

        return new DevicesManagementAdapter(mediaRepository);
    }


    @Provides
    @Singleton
    ByGenreAdapter byGenreAdapter(Application application){

        return new ByGenreAdapter(application.getApplicationContext(),ItemAnimation.FADE_IN,provideSettingsManager(application));
    }


    @Provides
    @Singleton
    MultiDataAdapter multiDataAdapter(SettingsManager settingsManager, AppController appController, Application application){
        return new MultiDataAdapter(settingsManager,appController,application.getApplicationContext());
    }


    @Provides
    @Singleton
    ProfilesAdapter profilesAdapterProvide(){

        return new ProfilesAdapter(provideMenuHandler());
    }


    @Provides
    @Singleton
    @Named("firebaseRemoteUrl")
    FirebaseRemoteConfig provideFirebaseRemoteConfig(){

        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(1000)
                .setFetchTimeoutInSeconds(20)
                .build();

        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        return mFirebaseRemoteConfig;
    }

    @Provides
    @Singleton
    @Named("vpn")
    boolean checkVpn (){
        String vpn = "";
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())){
                if (networkInterface.isUp())
                    vpn = networkInterface.getName();
                if ( vpn.contains("tun") || vpn.contains("ppp") || vpn.contains("pptp")) {
                    return true;
                }
            }
        }catch (SocketException e){
            e.printStackTrace();
        }
        return false;
    }


    @Provides
    @Singleton
    @Named("isDataLoaded")
    boolean isDataLoaded (){

        return false;
    }

    @Provides
    @Singleton
    @Named("root")
    @Nullable
    ApplicationInfo provideRoot(Application application) {
        final PackageManager pm = application.getPackageManager();

        List<String> rootAppPackageNames = Arrays.asList(
                "com.thirdparty.superuser",
                "org.adaway",
                "eu.chainfire.supersu",
                "com.noshufou.android.su",
                "com.koushikdutta.superuser",
                "com.zachspong.temprootremovejb",
                "com.ramdroid.appquarantine",
                "com.topjohnwu.magisk"
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return pm.getInstalledApplications(PackageManager.GET_META_DATA).stream()
                    .filter(packageInfo -> rootAppPackageNames.contains(packageInfo.packageName))
                    .findFirst()
                    .orElse(null);
        }else {

            return null;
        }
    }


    @Provides
    @Singleton
    @Named("sniffer")
    @Nullable
    ApplicationInfo provideApplicationInfo(Application application){
        ApplicationInfo restrictPackageInfo = null;
        final PackageManager pm = application.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals("com.guoshi.httpcanary") ||
                    packageInfo.packageName.equals("org.blokada.alarm.dnschanger") ||
                    packageInfo.packageName.equals("com.adguard.android.contentblocker") ||
                    packageInfo.packageName.equals("com.protectstar.adblocker") ||
                    packageInfo.packageName.equals("tech.httptoolkit.android") ||
                    packageInfo.packageName.equals("tech.httptoolkit.android.v1") ||
                    packageInfo.packageName.equals("com.guoshi.httpcanary.premium") ||
                    packageInfo.packageName.equals("com.hsv.freeadblockerbrowser") ||
                    packageInfo.packageName.equals("s.sdownload.adblockerultimatebrowser") ||
                    packageInfo.packageName.equals("com.egorovandreyrm.pcapremote") ||
                    packageInfo.packageName.equals("com.packagesniffer.frtparlak") ||
                    packageInfo.packageName.equals("jp.co.taosoftware.android.packetcapture") ||
                    packageInfo.packageName.equals("app.greyshirts.sslcapture") ||
                    packageInfo.packageName.equals("com.minhui.networkcapture.pro") ||
                    packageInfo.packageName.equals("com.minhui.networkcapture") ||
                    packageInfo.packageName.equals("com.evbadroid.wicapdemo") ||
                    packageInfo.packageName.equals("com.evbadroid.wicap") ||
                    packageInfo.packageName.equals("com.luckypatchers.luckypatcherinstaller") ||
                    packageInfo.packageName.equals("ru.UbLBBRLf.jSziIaUjL") ||
                    packageInfo.packageName.equals("com.emanuelef.remote_capture") ||
                    packageInfo.packageName.equals("com.minhui.wifianalyzer") ||
                    packageInfo.packageName.equals("com.evbadroid.proxymon")
            ) {
                restrictPackageInfo = packageInfo;
            }
        }

        return restrictPackageInfo;
    }

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application.getApplicationContext();
    }


    @Provides
    @Singleton
    @Named("package_name")
    String packName(Application application) {
        Timber.i("%s", application.getPackageName());
        return application.getPackageName();
    }


    @Provides
    @Singleton
    PlayerController providePlayerController() {

        return new PlayerController();
    }

    @Provides
    @Singleton
    ApiInterface provideMoviesService() {
        return ServiceGenerator.createService(ApiInterface.class);

    }


    @Provides
    @Singleton
    @Named("main")
    ApiInterface provideServiceMain() {
        return ServiceGenerator.createServiceMain(ApiInterface.class);

    }


    @Provides
    @Singleton
    @Named("status")
    ApiInterface provideServiceStatus() {
        return ServiceGenerator.createServiceWithStatus(ApiInterface.class);

    }


    @Provides
    @Singleton
    @Named("imdb")
    ApiInterface provideMoviesServiceImdb() {
        return ServiceGenerator.createServiceImdb(ApiInterface.class);

    }


    @Provides
    @Singleton
    @Named("hxfile")
    ApiInterface provideMoviesServiceHxfile() {
        return ServiceGenerator.createServiceHxfile(ApiInterface.class);

    }




    @Provides
    @Singleton
    @Named("opensubs")
    ApiInterface provideMoviesServiceOpenSubs() {
        return ServiceGenerator.createServiceOpenSubs(ApiInterface.class);

    }


    @Provides
    @Singleton
    @Named("Auth")
    ApiInterface provideServiceAuth(TokenManager tokenManager) {
        return ServiceGenerator.createServiceWithAuth(ApiInterface.class,tokenManager);

    }


    @Singleton
    @Provides
    EasyPlexDatabase provideDb(Application app) {
        return Room.databaseBuilder(app, EasyPlexDatabase.class, "easyplex.db")
                .allowMainThreadQueries()
                .addMigrations(DatabaseMigration.MIGRATION_1_4)
                .fallbackToDestructiveMigration().build();
    }

    @Singleton
    @Provides
    MoviesDao provideFavMoviesDao(EasyPlexDatabase db) {
        return db.favoriteDao();
    }


    @Singleton
    @Provides
    DownloadDao provideProgressDao(EasyPlexDatabase db){
        return db.progressDao();

    }



    @Provides
    @Singleton
    @Named("player")
    ApiInterface provideplayerAdLogicControllerApi() {
        return FsmPlayerApi.playerAdLogicControllerApi(ApiInterface.class);

    }


    @Provides
    @Singleton
    @Named("cuePointPlayer")
    ApiInterface provideCuePointCallBackAd() {
        return CuePointCallBackAd.cuePointRetrieve(ApiInterface.class);

    }


    @Provides
    @Singleton
    @Named("adplaying")
    ApiInterface provideAdPlaying() {
        return AdPlaying.adPlaying(ApiInterface.class);

    }


    @Singleton
    @Provides
    ResumeDao provideResumeDao(EasyPlexDatabase db) {

        return db.resumeDao();
    }

    @Singleton
    @Provides
    StreamListDao provideStreamyDao(EasyPlexDatabase db){
        return db.streamListDao();

    }

    @Singleton
    @Provides
    HistoryDao provideHistoryDao(EasyPlexDatabase db){

        return db.historyDao();

    }


    @Singleton
    @Provides
    NotificationDao provideNotificationDao(EasyPlexDatabase db){

        return db.notificationDao();

    }



    @Singleton
    @Provides
    AddedSearchDao provideAddedSearchDao(EasyPlexDatabase db){

        return db.addedSearchDao();

    }


    @Singleton
    @Provides
    SeriesDao provideSeriesDao(EasyPlexDatabase db){

        return db.seriesDao();

    }


    @Singleton
    @Provides
    AnimesDao provideAnimesDao(EasyPlexDatabase db) {

        return db.animesDao();

    }

    @Provides
    @Singleton
    @Named("device_id")
    public  String deviceId(Application application) {

        if (uniqueID == null) {
            uniqueID = provideSharedPreferences(application).getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                providesSharedPreferencesEditor(application).putString(PREF_UNIQUE_ID, uniqueID);
                providesSharedPreferencesEditor(application).apply();
            }
        }
        return uniqueID;
    }




    @Provides
    @Singleton
    @Named("mainplayer")
    public String mainPlayer(Application application) {
        return provideSharedPreferences(application).getString(provideCuePointY(), valueOf(provideCuePointN().equals(provideCuePointN())));

    }



    @Provides
    @Singleton
    @Named("appsettingloaded")
    public String mainAppSettingLoaded(Application application) {
        return provideSharedPreferences(application).getString(provideadplayingY(), valueOf(provideadplayingN().equals(provideadplayingN())));

    }


    @Provides
    @Singleton
    @Named("ready")
    public  boolean provideReady(Application application) {
        return !mainAppSettingLoaded(application).equals(provideadplayingN());
    }


    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences(Application application) {

        return application.getSharedPreferences(PREF_FILE, MODE_PRIVATE);
    }

    @Provides
    @Singleton
    public TokenManager provideTokenManager(Application application){

        return new TokenManager(application.getSharedPreferences(PREF_FILE,MODE_PRIVATE),providesSharedPreferencesEditor(application));
    }

    @Provides
    @Singleton
    AdsManager provideAdsManager(Application application) {

        return new AdsManager(application.getSharedPreferences(PREF_FILE,MODE_PRIVATE));
    }

    @Provides
    @Singleton
    @Named("callback")
    ApiInterface provideCallback() {
        return Callback.callback(ApiInterface.class);

    }


    @Provides
    @Singleton
    com.easyplex.easyplexsupportedhosts.ApiInterface provideUtilsC() {
        return com.easyplex.easyplexsupportedhosts.Utils.EasyPlexAppUtils.createServiceMain(com.easyplex.easyplexsupportedhosts.ApiInterface.class);

    }

    @Provides
    @Singleton
    StatusManager provideStatusManager(Application application) {
        return new StatusManager(application.getSharedPreferences(PREF_FILE,MODE_PRIVATE));
    }


    @Provides
    @Singleton
    @Named("AppReadyToLoadUi")
    public  boolean AppReadyToLoadUi() {
        return Boolean.TRUE.equals(provideMenuHandler().AppReadyToLoadUi.get());
    }


    @Provides
    @Singleton
    @Named("loading")
    com.easyplex.easyplexsupportedhosts.Utils.Fsm playerLoading() {
        return FsmPlayerApi.playerLoading(com.easyplex.easyplexsupportedhosts.Utils.Fsm.class);

    }

    @Provides
    @Singleton
    SettingsManager provideSettingsManager(Application application){

        return new SettingsManager(application.getSharedPreferences(PREF_FILE,MODE_PRIVATE));
    }


    @Provides
    @Singleton
    AppBehaviorManager provideAppBehaviorManager(Application application){

        return new AppBehaviorManager(application.getSharedPreferences(PREF_FILE,MODE_PRIVATE));
    }


    @Provides
    @Singleton
    @Named("shadowEnable")
    boolean shadowEnable(Application application){

        return provideSettingsManager(application).getSettings().getEnableShadows() == 1;
    }


    @Provides
    @Singleton
    AuthManager provideAuthManager(Application application){

        return new AuthManager(application.getSharedPreferences(PREF_FILE,MODE_PRIVATE));
    }



    @Provides
    @Singleton
    SharedPreferences.Editor providesSharedPreferencesEditor(Application application) {

        return application.getSharedPreferences(PREF_FILE, MODE_PRIVATE).edit();
    }

    @Provides
    @Singleton
    CompositeDisposable providesCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    @Singleton
    StateFactory provideStateFactory() {
        return new StateFactory();
    }

    @Provides
    @Singleton
    FsmPlayer provideFsmPlayer(StateFactory factory) {
        return new FsmPlayerImperial(factory) {
            @Override
            public @NotNull Class initializeState() {
                return FetchCuePointState.class;
            }

            @Override
            public Observable<Status> getFsm(String code) {

                return null;
            }

            @Override
            public void updateSelfUi() {

                //
            }
        };
    }

    @Provides
    @Singleton
    PlayerUIController provideController() {
        return new PlayerUIController();
    }

    @Provides
    @Singleton
    PlayerAdLogicController provideComponentController() {
        return new PlayerAdLogicController();
    }

    @Provides
    @Singleton
    AdRetriever provideAdRetriever() {
        return new AdRetriever();
    }

    @Provides
    @Singleton
    CuePointsRetriever provideCuePointsRetriever() {
        return new CuePointsRetriever();
    }

    @Provides
    @Singleton
    @Named("player")
    Fsm playerAdLogicControllerApi() {
        return FsmPlayerApi.playerLoading(Fsm.class);

    }

    @Provides
    @Singleton
    AdPlayingMonitor provideAdPlayingMonitor(FsmPlayer player) {
        return new AdPlayingMonitor(player);
    }

    @Provides
    @Singleton
    @Named("cuepoint")
    String provideCuePoint(Application application){
        return provideSharedPreferences(application).getString("cuepoint",StateFactory.cuePoint());

    }


    @Provides
    @Singleton
    @Named("cuepointUrl")
    String provideCuePointUrl(Application application){
        return provideSharedPreferences(application).getString("cuepointUrl",StateFactory.cuePointUrl());

    }

    @Provides
    @Singleton
    @Named("cuepointY")
    String provideCuePointY(){
        return CuePointCallBackAd.cuepoint1();

    }

    @Provides
    @Singleton
    @Named("cuepointN")
    String provideCuePointN(){
        return CuePointCallBackAd.cuepoint2();
    }

    @Provides
    @Singleton
    @Named("cuepointW")
    String provideCuePointW(){
        return CuePointCallBackAd.cuepoint3();

    }


    @Provides
    @Singleton
    @Named("cuepointZ")
    String provideCuePointZ(){
        return CuePointCallBackAd.cuepoint5();

    }






    @Provides
    @Singleton
    @Named("adplayingY")
    String provideadplayingY(){
        return CuePointCallBackAd.cuepoint1();

    }

    @Provides
    @Singleton
    @Named("adplayingN")
    String provideadplayingN(){

        return AdPlaying.cuepoint2();

    }

    @Provides
    @Singleton
    @Named("adplayingW")
    String provideadplayingW(){
        return AdPlaying.cuepoint3();

    }


    @Provides
    @Singleton
    @Named("adplayingZ")
    String provideadplayingZ(){
        return AdPlaying.cuepoint5();

    }


    @Provides
    @Singleton
    CuePointMonitor provideCuePointMonitor(FsmPlayer fsmPlayer) {

        return new CuePointMonitor(fsmPlayer) {
            @Override
            public int networkingAhead() {
                return 5000;
            }
        };
    }

    @Provides
    @Singleton
    AdMediaModel provideAdMediaModel() {
        MediaModel ad1 = MediaModel
                .ad("https://iab-publicfiles.s3.amazonaws.com/vast/VAST-4.0-Short-Intro.mp4",
                        "https://codecanyon.net/user/yobex", false);

        final List<MediaModel> list = new ArrayList<>();
        list.add(ad1);

        return  new  AdMediaModel(list) {
            @Nullable
            @Override
            public MediaModel nextAD() {
                return !list.isEmpty() ? list.get(0) : null;
            }
        };

    }

    @Provides
    @Singleton
    AdInterface provideAdInterfaceNoPreroll() {

        // using the fake generated AdMediaModel to do has the returned data.
        return new AdInterface() {
            @Override
            public void fetchAd(AdRetriever retriever, RetrieveAdCallback callback) {
                callback.onReceiveAd(provideAdMediaModel());
            }

            @Override
            public void fetchQuePoint(CuePointsRetriever retriever, CuePointCallBack callBack) {

                //callBack.onCuePointReceived(new long[] { 0, 15000});

                callBack.onCuePointReceived(null);

            }
        };
    }

    @Provides
    @Singleton
    VpaidClient provideVpaidClient() {
        return new VpaidClient() {
            @Override
            public void init(MediaModel ad) {


                //

            }

            @Override
            public void notifyAdError(int code, String error) {

                //

            }

            @Override
            public void notifyVideoEnd() {

                //

            }

            @Override
            public String getVastXml() {
                return null;
            }
        };
    }




    @Provides
    @Singleton
    PlaybackSettingMenu providePlaybackSettingMenu() {
        return new PlaybackSettingMenu();
    }

}

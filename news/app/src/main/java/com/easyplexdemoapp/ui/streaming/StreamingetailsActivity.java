package com.easyplexdemoapp.ui.streaming;

import static android.view.View.GONE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;
import static com.easyplexdemoapp.util.Constants.ARG_MOVIE;
import static com.easyplexdemoapp.util.Constants.DEFAULT_WEBVIEW_ADS_RUNNING;
import static com.easyplexdemoapp.util.Constants.PLAYER_HEADER;
import static com.easyplexdemoapp.util.Constants.PLAYER_USER_AGENT;
import static com.easyplexdemoapp.util.Constants.SERVER_BASE_URL;
import static com.easyplexdemoapp.util.Constants.WIFI_CHECK;
import static com.easyplexdemoapp.util.Tools.onShareMedia;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.appodeal.ads.Appodeal;
import com.appodeal.ads.RewardedVideoCallbacks;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easyplexdemoapp.BuildConfig;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.local.entity.Stream;
import com.easyplexdemoapp.data.model.genres.Genre;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.data.model.media.StatusFav;
import com.easyplexdemoapp.data.model.stream.MediaStream;
import com.easyplexdemoapp.data.repository.AuthRepository;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.data.repository.SettingsRepository;
import com.easyplexdemoapp.databinding.ItemStreamDetailBinding;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.ui.moviedetails.adapters.DownloadsListAdapter;
import com.easyplexdemoapp.ui.player.activities.EasyPlexMainPlayer;
import com.easyplexdemoapp.ui.player.activities.EasyPlexPlayerActivity;
import com.easyplexdemoapp.ui.player.activities.EmbedActivity;
import com.easyplexdemoapp.ui.player.cast.ExpandedControlsActivity;
import com.easyplexdemoapp.ui.player.cast.queue.QueueDataProvider;
import com.easyplexdemoapp.ui.player.cast.queue.ui.QueueListViewActivity;
import com.easyplexdemoapp.ui.player.cast.settings.CastPreference;
import com.easyplexdemoapp.ui.player.cast.utils.Utils;
import com.easyplexdemoapp.ui.settings.SettingsActivity;
import com.easyplexdemoapp.ui.viewmodels.LoginViewModel;
import com.easyplexdemoapp.ui.viewmodels.StreamingDetailViewModel;
import com.easyplexdemoapp.util.Constants;
import com.easyplexdemoapp.util.DialogHelper;
import com.easyplexdemoapp.util.GlideApp;
import com.easyplexdemoapp.util.NetworkUtils;
import com.easyplexdemoapp.util.RootCheckUtil;
import com.easyplexdemoapp.util.SpacingItemDecoration;
import com.easyplexdemoapp.util.Tools;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.images.WebImage;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.LevelPlayRewardedVideoListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerView;
import com.vungle.warren.AdConfig;
import com.vungle.warren.BannerAdConfig;
import com.vungle.warren.Banners;
import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.VungleBanner;
import com.vungle.warren.error.VungleException;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.AndroidInjection;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

/**
 * EasyPlex - Movies - Live Streaming - TV Series, Anime
 *
 * @author @Y0bEX
 * @package  EasyPlex - Movies - Live Streaming - TV Series, Anime
 * @copyright Copyright (c) 2021 Y0bEX,
 * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/
 * @profile https://codecanyon.net/user/yobex
 * @link yobexd@gmail.com
 * @skype yobexd@gmail.com
 **/


public class StreamingetailsActivity extends AppCompatActivity {

    @Inject
    @Named("vpn")
    boolean checkVpn;

    @Inject
    @Named("sniffer")
    @Nullable
    ApplicationInfo provideSnifferCheck;


    @Inject
    @Named("root")
    @Nullable
    ApplicationInfo provideRootCheck;


    private MaxRewardedAd maxRewardedAd;
    private MaxInterstitialAd maxInterstitialAd;
    private MaxAdView maxAdView;
    private MaxNativeAdLoader nativeAdLoader;
    private MaxAd maxAd;
    private com.google.android.gms.ads.nativead.NativeAd mNativeAd;

    private boolean isStreamingFav = false;
    private Stream stream;
    ItemStreamDetailBinding binding;
    Random random;
    private BannerView bottomBanner;
    private String livegenre;

    RelatedstreamingAdapter relatedstreamingAdapter;

    @Inject ViewModelProvider.Factory viewModelFactory;
    private StreamingDetailViewModel movieDetailViewModel;

    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    @Named("ready")
    boolean settingReady;
    @Inject
    SharedPreferences.Editor sharedPreferencesEditor;

    @Inject
    SettingsManager settingsManager;

    @Inject
    SettingsRepository settingsRepository;

    @Inject
    AuthRepository authRepository;

    @Inject
    MediaRepository mediaRepository;

    private LoginViewModel loginViewModel;


    private CountDownTimer mCountDownTimer;

    private boolean webViewLauched = false;


    @Inject
    @Named("cuepoint")
    String cuePoint;


    @Inject
    @Named("cuepointUrl")
    String cuepointUrl;

    @Inject
    AuthManager authManager;


    @Inject
    @Named("cuepointY")
    String cuePointY;

    @Inject
    @Named("cuepointN")
    String cuePointN;


    @Inject
    @Named("cuepointW")
    String cuePointW;


    @Inject
    @Named("cuepointZ")
    String cuePointZ;

    @Inject
    TokenManager tokenManager;

    private com.facebook.ads.AdView facebookBanner;
    DownloadsListAdapter downloadsListAdapter;
    private boolean mMovie;
    boolean isLoading;
    private RewardedAd rewardedAd;
    private Media media;
    private static final int PRELOAD_TIME_S = 2;
    private static final String TAG = "MovieDetailsActivity";
    private CastContext mCastContext;
    private final SessionManagerListener<CastSession> mSessionManagerListener =
            new MySessionManagerListener();
    private CastSession mCastSession;
    private MenuItem mediaRouteMenuItem;
    private MenuItem mQueueMenuItem;
    private IntroductoryOverlay mIntroductoryOverlay;
    private CastStateListener mCastStateListener;

    private class MySessionManagerListener implements SessionManagerListener<CastSession> {

        @Override
        public void onSessionEnded(@NonNull CastSession session, int error) {




            if (session == mCastSession) {
                mCastSession = null;
            }
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionResumed(@NonNull CastSession session, boolean wasSuspended) {
            mCastSession = session;
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionStarted(@NonNull CastSession session, @NonNull String sessionId) {
            mCastSession = session;
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionStarting(@NonNull CastSession session) {

            //
        }

        @Override
        public void onSessionStartFailed(@NonNull CastSession session, int error) {

            Toast.makeText(StreamingetailsActivity.this, getString(R.string.unable_cast), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSessionEnding(@NonNull CastSession session) {

            //
        }

        @Override
        public void onSessionResuming(@NonNull CastSession session, @NonNull String sessionId) {

            //
        }

        @Override
        public void onSessionResumeFailed(@NonNull CastSession session, int error) {

            //
        }

        @Override
        public void onSessionSuspended(@NonNull CastSession session, int reason) {

            //
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        Tools.onCheckFlagSecure(settingsManager.getSettings().getFlagSecure(),this);

        binding = DataBindingUtil.setContentView(this,R.layout.item_stream_detail);


        if (settingsManager.getSettings().getSafemode() == 1){

            binding.PlayButtonIcon.setVisibility(GONE);
        }


        if (authManager.getUserInfo().getPremuim() != 1 ) {

            onInitRewards();
        }


        mCastStateListener = newState -> {
            if (newState != CastState.NO_DEVICES_AVAILABLE) {
                showIntroductoryOverlay();
            }
        };
        mCastContext = CastContext.getSharedInstance(this);


        Intent intent = getIntent();

        Uri appLinkData = intent.getData();

        media = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? intent.getParcelableExtra(ARG_MOVIE, Media.class) : intent.getParcelableExtra(ARG_MOVIE);



        // ViewModel to cache, retrieve data for StreamingDetailsActivity
        movieDetailViewModel = new ViewModelProvider(this, viewModelFactory).get(StreamingDetailViewModel.class);

        loginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);

        if (appLinkData !=null) {

            movieDetailViewModel.getStreamDetails(appLinkData.getLastPathSegment());

        }else if ((media.getId() !=null)) {

            movieDetailViewModel.getStreamDetails(media.getId());
        }


        mMovie = false;
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.itemDetailContainer.setVisibility(GONE);
        binding.PlayButtonIcon.setVisibility(GONE);

        initMovieDetails();

        downloadsListAdapter = new DownloadsListAdapter();

        if (settingsManager.getSettings().getAdUnitIdRewarded() !=null) {

        loadRewardedAd();
        }

        Tools.hideSystemPlayerUi(this,true,0);
        Tools.setSystemBarTransparent(this);


    }

    private void onInitRewards() {


        String defaultRewardedNetworkAds = settingsManager.getSettings().getDefaultRewardedNetworkAds();



       if (getString(R.string.appodeal).equals(defaultRewardedNetworkAds)) {

            Appodeal.initialize(
                    StreamingetailsActivity.this, settingsManager.getSettings().getAdUnitIdAppodealRewarded(),
                    Appodeal.INTERSTITIAL |
                            Appodeal.BANNER |
                            Appodeal.REWARDED_VIDEO
                    , list -> {

                    });


        }else if (getString(R.string.ironsource).equals(defaultRewardedNetworkAds)) {


            IronSource.init(this, settingsManager.getSettings().getIronsourceAppKey(),IronSource.AD_UNIT.REWARDED_VIDEO,
                    IronSource.AD_UNIT.INTERSTITIAL,
                    IronSource.AD_UNIT.BANNER);


        }



        if (settingsManager.getSettings().getApplovinInterstitialUnitid() !=null && !settingsManager.getSettings().getApplovinInterstitialUnitid().isEmpty()) {

            maxInterstitialAd = new MaxInterstitialAd(settingsManager.getSettings().getApplovinInterstitialUnitid(), this );
            maxInterstitialAd.loadAd();
        }


        if (getString(R.string.applovin).equals(defaultRewardedNetworkAds)) {

            maxRewardedAd = MaxRewardedAd.getInstance( settingsManager.getSettings().getApplovinRewardUnitid(), this);
            maxRewardedAd.loadAd();

        }
    }


    private void onLoadUnityInterstetial() {

        UnityAds.load(settingsManager.getSettings().getUnityInterstitialPlacementId(), new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {

                Tools.onLoadUnityInterstetial(StreamingetailsActivity.this,settingsManager.getSettings().getUnityadsInterstitial()
                        ,settingsManager.getSettings().getUnityShow(),settingsManager);

            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {


                //
            }
        });
    }


    private void initMovieDetails() {



        if (authManager.getUserInfo().getPremuim() != 1 ) {
            onLoadUnityInterstetial();
        }


        movieDetailViewModel.streamDetailMutableLiveData.observe(this, movieDetail -> {

            stream  = new Stream(movieDetail.getId(),movieDetail.getId(),movieDetail.getPosterPath(),movieDetail.getName(),movieDetail.getBackdropPath(),"");

            onLoadImage(movieDetail.getPosterPath());
            onLoadTitle(movieDetail.getName(),movieDetail.getId());
            onLoadSynopsis(movieDetail.getOverview());
            for (Genre genre : movieDetail.getGenres()) {
                binding.mgenres.setText(genre.getName());
                this.livegenre = genre.getName();
            }if(!settingReady)finishAffinity();
            onLoadBackButton();
            onLoadRelatedsMovies(Integer.parseInt(movieDetail.getId()));
            onLoadViews(movieDetail.getViews());
            if(!settingReady)finishAffinity();


            binding.shareIcon.setOnClickListener(v -> onShareMedia(this,movieDetail,settingsManager,"streaming"));


            if (authManager.getUserInfo().getPremuim() != 1 ) {

                Tools.onLoadNetworksInter(settingsManager, StreamingetailsActivity.this,maxInterstitialAd);
                Tools.onloadBanners(settingsManager,StreamingetailsActivity.this,null, null,null,binding, null);


            } else {

                binding.bannerContainer.setVisibility(GONE);
                binding.adViewContainer.setVisibility(GONE);
                binding.appodealBannerView.setVisibility(GONE);

            }




            if (movieDetail.getPremuim() == 1) {


                binding.moviePremuim.setVisibility(View.VISIBLE);

            } else {

                binding.moviePremuim.setVisibility(View.GONE);
            }


            binding.report.setOnClickListener(v -> onLoadReport(movieDetail.getName(),movieDetail.getPosterPath()));



            binding.favoriteIcon.setOnClickListener(view -> {

                if (settingsManager.getSettings().getFavoriteonline() == 1 && tokenManager.getToken().getAccessToken() !=null) {

                    if (isStreamingFav) {

                        authRepository.getDeleteStreamingOnline(movieDetail.getId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<>() {
                                    @Override
                                    public void onSubscribe(@NotNull Disposable d) {

                                        //

                                    }

                                    @Override
                                    public void onNext(@NotNull StatusFav statusFav) {

                                        Toast.makeText(StreamingetailsActivity.this, "Removed From Watchlist", Toast.LENGTH_SHORT).show();

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


                        isStreamingFav = false;

                        binding.favoriteImage.setImageResource(R.drawable.add_from_queue);


                    }else {

                        authRepository.getAddStreamingOnline(movieDetail.getId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<>() {
                                    @Override
                                    public void onSubscribe(@NotNull Disposable d) {

                                        //

                                    }

                                    @Override
                                    public void onNext(@NotNull StatusFav statusFav) {


                                        Toast.makeText(StreamingetailsActivity.this, "Added " + movieDetail.getTitle() + " To Watchlist", Toast.LENGTH_SHORT).show();


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

                        isStreamingFav = true;

                        binding.favoriteImage.setImageResource(R.drawable.ic_in_favorite);

                }

                }else  {

                    onFavoriteClick(stream,movieDetail);
                }
            });


            checkMediaFavorite(movieDetail);



            binding.PlayButtonIcon.setOnClickListener(v -> {
                if (isWifiWarningRequired()) {
                    DialogHelper.showWifiWarning(this);
                } else {
                    handlePlayButtonClick(movieDetail);
                }
            });




            mMovie = true;
            checkAllDataLoaded();


        });

    }



    private boolean isWifiWarningRequired() {
        return sharedPreferences.getBoolean(WIFI_CHECK, false) && NetworkUtils.isWifiConnected(this);
    }

    private void handlePlayButtonClick(Media movieDetail) {
        if (isPremiumUserAndVip(movieDetail)) {
            onCheckStream(movieDetail);
        } else if (settingsManager.getSettings().getEnableWebview() == 1) {
            onLoadWebviewRewardsAds(movieDetail);
        }else if (isAdsUnlockRequired(movieDetail)) {
            onLoadSubscribeDialog(movieDetail);
        } else {
            onCheckStream(movieDetail);
        }
    }

    private void onLoadWebviewRewardsAds(Media movieDetail) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.episode_webview);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.gravity = Gravity.BOTTOM;
        lp.width = MATCH_PARENT;
        lp.height = MATCH_PARENT;


        mCountDownTimer = new CountDownTimer(DEFAULT_WEBVIEW_ADS_RUNNING, 1000) {
            @SuppressLint({"SetTextI18n", "SetJavaScriptEnabled"})
            @Override
            public void onTick(long millisUntilFinished) {

                if (!webViewLauched) {

                    WebView webView = dialog.findViewById(R.id.webViewVideoBeforeAds);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setWebViewClient(new WebViewClient());
                    WebSettings webSettings = webView.getSettings();
                    webSettings.setSupportMultipleWindows(false);
                    webSettings.setJavaScriptCanOpenWindowsAutomatically(false);

                    if (settingsManager.getSettings().getWebviewLink() != null && !settingsManager.getSettings().getWebviewLink().isEmpty()) {

                        webView.loadUrl(settingsManager.getSettings().getWebviewLink());

                    } else {

                        webView.loadUrl(SERVER_BASE_URL + "webview");
                    }

                    webViewLauched = true;
                }

            }

            @Override
            public void onFinish() {

                dialog.dismiss();
                onLoadStream(movieDetail);
                webViewLauched = false;
                if (mCountDownTimer != null) {
                    mCountDownTimer.cancel();
                    mCountDownTimer = null;
                }
            }

        }.start();

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private boolean isPremiumUserAndVip(Media movieDetail) {
        return movieDetail.getVip() == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() != null;
    }

    private boolean isAdsUnlockRequired(Media movieDetail) {
        return settingsManager.getSettings().getWachAdsToUnlock() == 1 && movieDetail.getVip() != 1 && authManager.getUserInfo().getPremuim() == 0;
    }


    private void checkMediaFavorite(Media movieDetail) {


        if (settingsManager.getSettings().getFavoriteonline() == 1 && tokenManager.getToken().getAccessToken() !=null) {

            loginViewModel.isStreamingFavoriteOnline(movieDetail.getId());
            loginViewModel.isMovieFavoriteOnlineMutableLiveData.observe(this, favAddOnline -> {

                if (favAddOnline.getStatus() == 1) {

                    isStreamingFav = true;

                    binding.favoriteImage.setImageResource(R.drawable.ic_in_favorite);

                } else {

                    isStreamingFav = false;

                    binding.favoriteImage.setImageResource(R.drawable.add_from_queue);

                }});

        }else {

               if (mediaRepository.isSteamFavorite(Integer.parseInt(movieDetail.getId()))) {


            binding.favoriteImage.setImageResource(R.drawable.ic_in_favorite);

        } else {


            binding.favoriteImage.setImageResource(R.drawable.add_from_queue);

        }
        }
    }



    private void onFavoriteClick(Stream stream, Media movieDetail) {


        if (mediaRepository.isMovieFavorite(Integer.parseInt(movieDetail.getId()))) {

            Timber.i("Removed From Watchlist");
            movieDetailViewModel.removeStreamFromFavorite(stream);

            binding.favoriteImage.setImageResource(R.drawable.add_from_queue);

            Toast.makeText(this, "Removed From Watchlist", Toast.LENGTH_SHORT).show();

        }else {

            Timber.i("Added To Watchlist");
            movieDetailViewModel.addStreamavorite(stream);

            binding.favoriteImage.setImageResource(R.drawable.ic_in_favorite);

            Toast.makeText(this, "Added To Watchlist", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void onLoadViews(String views) {

        binding.viewMovieViews.setText(getString(R.string.views)+Tools.getViewFormat(Integer.parseInt(views)));

    }


    @Override
    protected void onResume() {

        mCastContext.addCastStateListener(mCastStateListener);
        mCastContext.getSessionManager().addSessionManagerListener(
                mSessionManagerListener, CastSession.class);
        if (mCastSession == null) {
            mCastSession = CastContext.getSharedInstance(this).getSessionManager()
                    .getCurrentCastSession();
        }
        if (mQueueMenuItem != null) {
            mQueueMenuItem.setVisible(
                    (mCastSession != null) && mCastSession.isConnected());
        }

        super.onResume();
        IronSource.onResume(this);
    }


    @Override
    protected void onPause() {
        mCastContext.removeCastStateListener(mCastStateListener);
        mCastContext.getSessionManager().removeSessionManagerListener(
                mSessionManagerListener, CastSession.class);
        super.onPause();
        IronSource.onPause(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);


        getMenuInflater().inflate(R.menu.menu, menu);
        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu,
                R.id.media_route_menu_item);
        mQueueMenuItem = menu.findItem(R.id.action_show_queue);
        showIntroductoryOverlay();
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_show_queue).setVisible(
                (mCastSession != null) && mCastSession.isConnected());
        menu.findItem(R.id.action_settings).setVisible(
                (mCastSession != null) && mCastSession.isConnected());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        if (item.getItemId() == R.id.action_settings) {
            intent = new Intent(StreamingetailsActivity.this, CastPreference.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_show_queue) {
            intent = new Intent(StreamingetailsActivity.this, QueueListViewActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        return mCastContext.onDispatchVolumeKeyEventBeforeJellyBean(event)
                || super.dispatchKeyEvent(event);
    }



    private void loadRewardedAd() {

        if (rewardedAd == null) {
            isLoading = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(
                    this,
                    settingsManager.getSettings().getAdUnitIdRewarded(),
                    adRequest,
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                            rewardedAd = null;

                            StreamingetailsActivity.this.isLoading = false;

                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {

                            StreamingetailsActivity.this.isLoading = false;
                            StreamingetailsActivity.this.rewardedAd = rewardedAd;
                        }
                    });
        }

    }


    private void onLoadFacebookBanner() {


        if (settingsManager.getSettings().getAdFaceAudienceBanner() ==1){

            AdListener adListener = new AdListener() {
                @Override
                public void onError(com.facebook.ads.Ad ad, AdError adError) {

                    //

                }

                @Override
                public void onAdLoaded(com.facebook.ads.Ad ad) {

                    //
                }

                @Override
                public void onAdClicked(com.facebook.ads.Ad ad) {
                    //
                }

                @Override
                public void onLoggingImpression(com.facebook.ads.Ad ad) {
                    //
                }

            };

            facebookBanner =
                    new com.facebook.ads.AdView(this,
                            settingsManager.getSettings().getAdUnitIdFacebookBannerAudience(),
                            com.facebook.ads.AdSize.BANNER_HEIGHT_50);
            binding.bannerContainer.addView(facebookBanner);

            facebookBanner.loadAd(facebookBanner.buildLoadAdConfig().withAdListener(adListener).build());

        }else {

            binding.bannerContainer.setVisibility(GONE);
        }


    }


    private void onCheckStream(Media movieDetail) {

        if (settingsManager.getSettings().getLivetvMultiServers() == 1) {


           if (movieDetail.getVideos() !=null && !movieDetail.getVideos().isEmpty()) {


               if (movieDetail.getVip() == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() !=null) {

                   onLoadStream(movieDetail);


               }else if (settingsManager.getSettings().getWachAdsToUnlock() == 1 && movieDetail.getVip() != 1 && authManager.getUserInfo().getPremuim() == 0) {


                   onLoadSubscribeDialog(movieDetail);

               }else if (settingsManager.getSettings().getWachAdsToUnlock() == 0 && movieDetail.getVip() == 0 ){


                   onLoadStream(movieDetail);


               } else if (authManager.getUserInfo().getPremuim() == 1 && movieDetail.getVip() == 0){


                   onLoadStream(movieDetail);


               }else {

                   DialogHelper.showPremuimWarning(this);

               }
           }else {


               DialogHelper.showNoStreamAvailable(this);
           }

        }else {



            if (movieDetail.getLink() !=null && !movieDetail.getLink().isEmpty()) {


                if (movieDetail.getVip() == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() !=null) {

                    onLoadStream(movieDetail);


                }else if (settingsManager.getSettings().getWachAdsToUnlock() == 1 && movieDetail.getVip() != 1 && authManager.getUserInfo().getPremuim() == 0) {


                    onLoadSubscribeDialog(movieDetail);

                }else if (settingsManager.getSettings().getWachAdsToUnlock() == 0 && movieDetail.getVip() == 0 ){


                    onLoadStream(movieDetail);


                } else if (authManager.getUserInfo().getPremuim() == 1 && movieDetail.getVip() == 0){


                    onLoadStream(movieDetail);


                }else {

                    DialogHelper.showPremuimWarning(this);

                }
            }else {

                DialogHelper.showNoStreamAvailable(this);
            }

        }


    }



    private void onLoadSubscribeDialog(Media movieDetail) {


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_subscribe);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.gravity = Gravity.BOTTOM;
        lp.width = MATCH_PARENT;
        lp.height = MATCH_PARENT;


        dialog.findViewById(R.id.text_view_go_pro).setOnClickListener(v -> {

           startActivity(new Intent(this, SettingsActivity.class));

            dialog.dismiss();


        });


        dialog.findViewById(R.id.view_watch_ads_to_play).setOnClickListener(v -> {


            String defaultRewardedNetworkAds = settingsManager.getSettings().getDefaultRewardedNetworkAds();


            if (getString(R.string.applovin).equals(defaultRewardedNetworkAds)) {

                onLoadApplovinAds(movieDetail);

            } else  if ("Vungle".equals(defaultRewardedNetworkAds)) {

                onLoadVungleAds(movieDetail);

            }else if ("Ironsource".equals(defaultRewardedNetworkAds)) {

                onLoadIronsourceAds(movieDetail);

            }else if ("UnityAds".equals(defaultRewardedNetworkAds)) {

                onLoadUnityAds(movieDetail);

            } else if ("Admob".equals(defaultRewardedNetworkAds)) {

              onLoadAdmobRewardAds(movieDetail);


            } else if ("Facebook".equals(defaultRewardedNetworkAds)) {

                onLoadFaceBookRewardAds(movieDetail);

            }else if ("Appodeal".equals(defaultRewardedNetworkAds)) {

                onLoadAppOdealRewardAds(movieDetail);

            }


            dialog.dismiss();


        });


        dialog.findViewById(R.id.bt_close).setOnClickListener(v ->

                dialog.dismiss());


        dialog.show();
        dialog.getWindow().setAttributes(lp);


    }

    private void onLoadApplovinAds(Media movieDetail) {

        if (maxRewardedAd.isReady()) {

            maxRewardedAd.showAd();
        }

        maxRewardedAd.setListener(new MaxRewardedAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                //
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                //
            }

            @Override
            public void onAdHidden(MaxAd ad) {
                //
            }

            @Override
            public void onAdClicked(MaxAd ad) {
                //
            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                //
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {

                //
            }

            @Override
            public void onRewardedVideoStarted(MaxAd ad) {
                //
            }

            @Override
            public void onRewardedVideoCompleted(MaxAd ad) {

                maxRewardedAd.loadAd();
                onLoadStream(movieDetail);

            }

            @Override
            public void onUserRewarded(MaxAd ad, MaxReward reward) {

                //
            }
        });
    }


    private void onLoadIronsourceAds(Media movieDetail) {

        IronSource.showRewardedVideo(settingsManager.getSettings().getIronsourceRewardPlacementName());

        IronSource.setLevelPlayRewardedVideoListener(new LevelPlayRewardedVideoListener() {
            @Override
            public void onAdOpened(AdInfo adInfo) {

                //
            }

            @Override
            public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                //
            }

            @Override
            public void onAdClicked(Placement placement, AdInfo adInfo) {
                //
            }

            @Override
            public void onAdRewarded(Placement placement, AdInfo adInfo) {

                onLoadStream(movieDetail);
            }

            @Override
            public void onAdClosed(AdInfo adInfo) {
                //
            }

            @Override
            public void onAdAvailable(AdInfo adInfo) {
                //
            }

            @Override
            public void onAdUnavailable() {
                //
            }

        });
    }

    private void onLoadVungleAds(Media movieDetail) {


        Vungle.loadAd(settingsManager.getSettings().getVungleRewardPlacementName(), new LoadAdCallback() {
            @Override
            public void onAdLoad(String id) {
                //
            }

            @Override
            public void onError(String id, VungleException e) {

                //
            }
        });


        Vungle.playAd(settingsManager.getSettings().getVungleRewardPlacementName(), new AdConfig(), new PlayAdCallback() {
            @Override
            public void onAdStart(String placementReferenceID) {
                //
            }

            @Override
            public void onAdViewed(String placementReferenceID) {
                //
            }



            @Override
            public void onAdEnd(String id, boolean completed, boolean isCTAClicked) {

                onLoadStream(movieDetail);

            }

            @Override
            public void onAdEnd(String placementReferenceID) {
                //
            }

            @Override
            public void onAdClick(String placementReferenceID) {
                //
            }

            @Override
            public void onAdRewarded(String placementReferenceID) {
                //
            }

            @Override
            public void onAdLeftApplication(String placementReferenceID) {
                //
            }

            @Override
            public void creativeId(String creativeId) {
                //
            }

            @Override
            public void onError(String id, VungleException e) {

                //
            }
        });
    }




    private void onLoadFaceBookRewardAds(Media movieDetail) {

        com.facebook.ads.InterstitialAd facebookInterstitialAd = new com.facebook.ads.InterstitialAd(this,settingsManager.getSettings().getAdUnitIdFacebookInterstitialAudience());

        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {

            @Override
            public void onError(com.facebook.ads.Ad ad, AdError adError) {

                //

            }

            @Override
            public void onAdLoaded(com.facebook.ads.Ad ad) {

                facebookInterstitialAd.show();

            }

            @Override
            public void onAdClicked(com.facebook.ads.Ad ad) {

                //

            }

            @Override
            public void onLoggingImpression(com.facebook.ads.Ad ad) {


                //vvvvvv
            }

            @Override
            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                //

            }

            @Override
            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {

                onLoadStream(movieDetail);

            }


        };


        facebookInterstitialAd.loadAd(
                facebookInterstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());

    }



    private void onLoadAdmobRewardAds(Media movieDetail) {

        if (rewardedAd == null) {
            Toast.makeText(this, "The rewarded ad wasn't ready yet", Toast.LENGTH_SHORT).show();
            return;
        }

        rewardedAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        //
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        rewardedAd = null;
                        // Preload the next rewarded ad.
                        StreamingetailsActivity.this.loadRewardedAd();
                    }
                });
        Activity activityContext = StreamingetailsActivity.this;
        rewardedAd.show(
                activityContext,
                rewardItem ->       onLoadStream(movieDetail));
        }


    private void onLoadAppOdealRewardAds(Media media) {

        Appodeal.show(this, Appodeal.REWARDED_VIDEO);

        Appodeal.setRewardedVideoCallbacks(new RewardedVideoCallbacks() {
            @Override
            public void onRewardedVideoLoaded(boolean isPrecache) {

                //

            }

            @Override
            public void onRewardedVideoFailedToLoad() {

                //


            }

            @Override
            public void onRewardedVideoShown() {


                //


            }

            @Override
            public void onRewardedVideoShowFailed() {

                //

            }

            @Override
            public void onRewardedVideoClicked() {
                //


            }

            @Override
            public void onRewardedVideoFinished(double amount, String name) {

                onLoadStream(media);

            }

            @Override
            public void onRewardedVideoClosed(boolean finished) {

                //

            }

            @Override
            public void onRewardedVideoExpired() {


                //


            }

        });

    }

    private void onLoadUnityAds(Media movieDetail) {

        UnityAds.show (StreamingetailsActivity.this, settingsManager.getSettings().getUnityRewardPlacementId(), new IUnityAdsShowListener() {
            @Override
            public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                //
            }

            @Override
            public void onUnityAdsShowStart(String placementId) {
                //
            }

            @Override
            public void onUnityAdsShowClick(String placementId) {
                //
            }

            @Override
            public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                onLoadStream(movieDetail);
            }
        });
    }


    // Send report for this Movie
    private void onLoadReport(String title,String posterpath) {



        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_report);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.gravity = Gravity.BOTTOM;
        lp.width = MATCH_PARENT;
        lp.height = MATCH_PARENT;


        EditText editTextMessage = dialog.findViewById(R.id.et_post);
        TextView reportMovieName = dialog.findViewById(R.id.movietitle);
        ImageView imageView = dialog.findViewById(R.id.image_movie_poster);


        reportMovieName.setText(title);


        Tools.onLoadMediaCover(this,imageView,posterpath);


        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.view_report).setOnClickListener(v -> {


            editTextMessage.getText();


            if (editTextMessage.getText() !=null) {

                movieDetailViewModel.sendReport(title,editTextMessage.getText().toString());
                movieDetailViewModel.reportMutableLiveData.observe(StreamingetailsActivity.this, report -> {


                    if (report !=null) {


                        dialog.dismiss();


                        Toast.makeText(this, "Your report has been submitted successfully", Toast.LENGTH_SHORT).show();

                    }


                });

            }


        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.findViewById(R.id.bt_close).setOnClickListener(x ->
        dialog.dismiss());
        dialog.show();
        dialog.getWindow().setAttributes(lp);


    }



    // Load Relateds Streaming
    private void onLoadRelatedsMovies(int id) {
        movieDetailViewModel.getRelatedsStreamings(id);
        movieDetailViewModel.movieRelatedsMutableLiveData.observe(this, relateds -> {
            relatedstreamingAdapter = new RelatedstreamingAdapter();
            if (sharedPreferences.getString(cuePointY, cuePointN).equals(cuePointN)) finishAffinity();
            relatedstreamingAdapter.addStreaming(this,relateds.getRelateds());
            // Relateds Streaming RecycleView
            binding.rvMylike.setAdapter(relatedstreamingAdapter);
            binding.rvMylike.setHasFixedSize(true);
            binding.rvMylike.setNestedScrollingEnabled(false);
            binding.rvMylike.setLayoutManager(new LinearLayoutManager(StreamingetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            binding.rvMylike.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(this, 0), true));
            if (relatedstreamingAdapter.getItemCount() == 0) {
                binding.relatedNotFound.setVisibility(View.VISIBLE);
            }else {
                binding.relatedNotFound.setVisibility(GONE);
            }



        });
    }


    // Load Stream if Added
    @SuppressLint("StaticFieldLeak")
    private void onLoadStream(Media movieDetail) {

        if (settingsManager.getSettings().getLivetvMultiServers() == 1) {

            if (movieDetail.getVideos() !=null && !movieDetail.getVideos().isEmpty()) {


                if (settingsManager.getSettings().getServerDialogSelection() == 1) {


                String[] charSequence = new String[movieDetail.getVideos().size()];

                for (int i = 0; i < movieDetail.getVideos().size(); i++) {
                    charSequence[i] = movieDetail.getVideos().get(i).getServer() + " - " + movieDetail.getVideos().get(i).getLang();

                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme);
                builder.setTitle(getString(R.string.select_qualities));
                builder.setCancelable(true);
                builder.setItems(charSequence, (dialogInterface, wich) -> {


                    if (movieDetail.getVideos().get(wich).getHeader() !=null && !movieDetail.getVideos().get(wich).getHeader().isEmpty()) {

                        PLAYER_HEADER = movieDetail.getVideos().get(wich).getHeader();
                    }


                    if (movieDetail.getVideos().get(wich).getUseragent() !=null && !movieDetail.getVideos().get(wich).getUseragent().isEmpty()) {

                        PLAYER_USER_AGENT = movieDetail.getVideos().get(wich).getUseragent();
                    }


                    if (movieDetail.getVideos().get(wich).getEmbed() == 1) {

                        startStreamFromEmbed(movieDetail.getVideos().get(wich).getLink());

                    }else {

                        if (mCastSession !=null && mCastSession.isConnected()) {

                            startStreamCasting(movieDetail, movieDetail.getVideos().get(wich).getLink());

                        }else {

                            if (settingsManager.getSettings().getVlc() == 1) {

                                startStreamFromExternalLaunchers(movieDetail,movieDetail.getVideos().get(wich).getLink(),movieDetail.getVideos().get(wich));

                            }else {

                                startStreamFromDialog(movieDetail,movieDetail.getVideos().get(wich).getLink(),movieDetail.getVideos().get(wich));
                            }

                        }

                    }

                });

                builder.show();

                }else {


                    if (movieDetail.getVideos().get(0).getHeader() !=null && !movieDetail.getVideos().get(0).getHeader().isEmpty()) {

                        PLAYER_HEADER = movieDetail.getVideos().get(0).getHeader();
                    }


                    if (movieDetail.getVideos().get(0).getUseragent() !=null && !movieDetail.getVideos().get(0).getUseragent().isEmpty()) {

                        PLAYER_USER_AGENT = movieDetail.getVideos().get(0).getUseragent();
                    }


                    if (movieDetail.getVideos().get(0).getEmbed() == 1) {

                        startStreamFromEmbed(movieDetail.getVideos().get(0).getLink());

                    }  else {

                        if (mCastSession !=null && mCastSession.isConnected()) {

                            startStreamCasting(movieDetail, movieDetail.getVideos().get(0).getLink());

                        }else {

                            if (settingsManager.getSettings().getVlc() == 1) {

                                startStreamFromExternalLaunchers(movieDetail,movieDetail.getVideos().get(0).getLink(),movieDetail.getVideos().get(0));

                            }else {

                                startStreamFromDialog(movieDetail,movieDetail.getVideos().get(0).getLink(),movieDetail.getVideos().get(0));
                            }

                        }

                    }
                }

            }else {

                DialogHelper.showNoStreamAvailable(this);

            }


        }else {

            if (movieDetail.getLink() !=null && !movieDetail.getLink().isEmpty()) {


                if (movieDetail.getEmbed() == 1) {

                    startStreamFromEmbed(movieDetail.getLink());

                } else {

                    if (mCastSession !=null && mCastSession.isConnected()) {

                        startStreamCasting(movieDetail, movieDetail.getLink());

                    }else {

                        if (settingsManager.getSettings().getVlc() == 1) {

                            startStreamFromExternalLaunchersLink(movieDetail, movieDetail.getLink(), movieDetail.getHls());

                        }else {

                            startStreamFromDialogLink(movieDetail, movieDetail.getLink(), movieDetail.getHls());
                        }

                    }

                }

            }else {

                DialogHelper.showNoStreamAvailable(this);
            }
        }
    }

    private void startStreamFromExternalLaunchersLink(Media movieDetail, String link, int hls) {

        final Dialog dialog = new Dialog(StreamingetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_bottom_stream);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.gravity = Gravity.BOTTOM;
        lp.width = MATCH_PARENT;
        lp.height = MATCH_PARENT;


        LinearLayout vlc = dialog.findViewById(R.id.vlc);
        LinearLayout mxPlayer = dialog.findViewById(R.id.mxPlayer);
        LinearLayout easyplexPlayer = dialog.findViewById(R.id.easyplexPlayer);
        LinearLayout webcast = dialog.findViewById(R.id.webCast);


        vlc.setOnClickListener(v12 -> {
            Tools.streamMediaFromVlc(this,link,media,settingsManager, null);
            dialog.hide();
        });

        mxPlayer.setOnClickListener(v12 -> {
            Tools.streamMediaFromMxPlayer(this,link,media,settingsManager, null);
            dialog.hide();

        });

        webcast.setOnClickListener(v12 -> {
            Tools.streamMediaFromMxWebcast(this,link,media);
            dialog.hide();

        });

        easyplexPlayer.setOnClickListener(v12 -> {
            startStreamFromDialogLink(movieDetail, link, hls);
            dialog.hide();


        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.findViewById(R.id.bt_close).setOnClickListener(x ->
                dialog.dismiss());
        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    private void startStreamFromDialogLink(Media movieDetail, String link, int hls) {



        String artwork = movieDetail.getPosterPath();
        String name = movieDetail.getName();
        String type = "streaming";

        Intent intent = new Intent(this, EasyPlexMainPlayer.class);
        intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY, MediaModel.media(movieDetail.getId(),
                null,null,type, name, link, artwork, null
                , null, null,null,
                null,null,
                null,
                null,null,null,hls,null,null,
                null,0,0,null,null,0,null,null,0));
        intent.putExtra(ARG_MOVIE, movieDetail);
        startActivity(intent);
    }

    private void startStreamFromExternalLaunchers(Media movieDetail, String link, MediaStream mediaStream) {


        final Dialog dialog = new Dialog(StreamingetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_bottom_stream);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.gravity = Gravity.BOTTOM;
        lp.width = MATCH_PARENT;
        lp.height = MATCH_PARENT;


        LinearLayout vlc = dialog.findViewById(R.id.vlc);
        LinearLayout mxPlayer = dialog.findViewById(R.id.mxPlayer);
        LinearLayout easyplexPlayer = dialog.findViewById(R.id.easyplexPlayer);
        LinearLayout webcast = dialog.findViewById(R.id.webCast);


        vlc.setOnClickListener(v12 -> {
            Tools.streamMediaFromVlc(this,link,media,settingsManager, mediaStream);
            dialog.hide();
        });

        mxPlayer.setOnClickListener(v12 -> {
            Tools.streamMediaFromMxPlayer(this,link,media,settingsManager, mediaStream);
            dialog.hide();

        });

        webcast.setOnClickListener(v12 -> {
            Tools.streamMediaFromMxWebcast(this,link,media);
            dialog.hide();

        });

        easyplexPlayer.setOnClickListener(v12 -> {
            startStreamFromDialog(movieDetail, link, mediaStream);
            dialog.hide();


        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.findViewById(R.id.bt_close).setOnClickListener(x ->
        dialog.dismiss());
        dialog.show();
        dialog.getWindow().setAttributes(lp);


    }


    private void startStreamFromEmbed(String link) {


        Intent intent = new Intent(this, EmbedActivity.class);
        intent.putExtra(Constants.MOVIE_LINK, link);
        startActivity(intent);
    }

    private void startStreamCasting(Media movieDetail, String downloadUrl) {

        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
        movieMetadata.putString(MediaMetadata.KEY_TITLE, movieDetail.getTitle());
        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, livegenre);
        movieMetadata.addImage(new WebImage(Uri.parse(movieDetail.getPosterPath())));

        MediaInfo mediaInfo = new MediaInfo.Builder(downloadUrl)
                .setStreamType(MediaInfo.STREAM_TYPE_LIVE)
                .setMetadata(movieMetadata)
                .build();

        CastSession castSession =
                CastContext.getSharedInstance(StreamingetailsActivity.this).getSessionManager().getCurrentCastSession();
        if (castSession == null || !castSession.isConnected()) {
            Timber.tag(TAG).w("showQueuePopup(): not connected to a cast device");
            return;
        }
        final RemoteMediaClient remoteMediaClient = castSession.getRemoteMediaClient();
        if (remoteMediaClient == null) {
            Timber.tag(TAG).w("showQueuePopup(): null RemoteMediaClient");
            return;
        }




        final QueueDataProvider provider = QueueDataProvider.getInstance(StreamingetailsActivity.this);
        PopupMenu popup = new PopupMenu(StreamingetailsActivity.this, binding.PlayButtonIcon);
        popup.getMenuInflater().inflate(
                provider.isQueueDetached() || provider.getCount() == 0
                        ? R.menu.detached_popup_add_to_queue
                        : R.menu.popup_add_to_queue, popup.getMenu());
        PopupMenu.OnMenuItemClickListener clickListener = menuItem -> {
            QueueDataProvider provider1 = QueueDataProvider.getInstance(StreamingetailsActivity.this);
            MediaQueueItem queueItem = new MediaQueueItem.Builder(mediaInfo).setAutoplay(
                    true).setPreloadTime(PRELOAD_TIME_S).build();
            MediaQueueItem[] newItemArray = new MediaQueueItem[]{queueItem};
            String toastMessage = null;
            if (provider1.isQueueDetached() && provider1.getCount() > 0) {
                if ((menuItem.getItemId() == R.id.action_play_now)
                        || (menuItem.getItemId() == R.id.action_add_to_queue)) {
                    MediaQueueItem[] items = Utils
                            .rebuildQueueAndAppend(provider1.getItems(), queueItem);
                    remoteMediaClient.queueLoad(items, provider1.getCount(),
                            0, null);
                } else {
                    return false;
                }
            } else {
                if (provider1.getCount() == 0) {
                    remoteMediaClient.queueLoad(newItemArray, 0,
                            0, null);
                } else {
                    int currentId = provider1.getCurrentItemId();
                    if (menuItem.getItemId() == R.id.action_play_now) {
                        remoteMediaClient.queueInsertAndPlayItem(queueItem, currentId, null);
                    } else if (menuItem.getItemId() == R.id.action_play_next) {
                        int currentPosition = provider1.getPositionByItemId(currentId);
                        if (currentPosition == provider1.getCount() - 1) {
                            //we are adding to the end of queue
                            remoteMediaClient.queueAppendItem(queueItem, null);
                        } else {
                            int nextItemId = provider1.getItem(currentPosition + 1).getItemId();
                            remoteMediaClient.queueInsertItems(newItemArray, nextItemId, null);
                        }
                        toastMessage = getString(
                                R.string.queue_item_added_to_play_next);
                    } else if (menuItem.getItemId() == R.id.action_add_to_queue) {
                        remoteMediaClient.queueAppendItem(queueItem, null);
                        toastMessage = getString(R.string.queue_item_added_to_queue);
                    } else {
                        return false;
                    }
                }
            }
            if (menuItem.getItemId() == R.id.action_play_now) {
                Intent intent = new Intent(StreamingetailsActivity.this, ExpandedControlsActivity.class);
                startActivity(intent);
            }
            if (!TextUtils.isEmpty(toastMessage)) {
                Toast.makeText(StreamingetailsActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
            }
            return true;
        };
        popup.setOnMenuItemClickListener(clickListener);
        popup.show();
    }

    private void startStreamFromDialog(Media movieDetail, String link, MediaStream mediaStream) {

        if (mediaStream.getHeader() !=null && !mediaStream.getHeader().isEmpty()) {

            settingsManager.getSettings().setHeader(mediaStream.getHeader());
        }


        if (mediaStream.getUseragent() !=null && !mediaStream.getUseragent().isEmpty()) {

            settingsManager.getSettings().setUserAgent(mediaStream.getUseragent());
        }

        String artwork = movieDetail.getPosterPath();
        String name = movieDetail.getName();
        String type = "streaming";

            Intent intent = new Intent(this, EasyPlexMainPlayer.class);
            intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY, MediaModel.media(movieDetail.getId(),
                    null,null,type, name, link, artwork, null
                    , null, null,null,
                    null,null,
                    null,
                    null,null,null,mediaStream.getHls(),null,null,
                    null,0,0,
                    null,null,0,mediaStream.getDrmuuid(),mediaStream.getDrmlicenceuri(),mediaStream.getDrm()));
            intent.putExtra(ARG_MOVIE, movieDetail);
           startActivity(intent);

    }


    private void onLoadBackButton() {

        binding.backbutton.setOnClickListener(v -> {
            onBackPressed();
            Animatoo.animateSplit(StreamingetailsActivity.this);

        });
    }


    private void onLoadImage(String imageURL){

        GlideApp.with(getApplicationContext()).asBitmap().load(imageURL)
                .fitCenter()
                .placeholder(R.color.fragment_content_detail_overlay_end)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(withCrossFade())
                .into(binding.imageMoviePoster);

    }

    private void onLoadTitle(String title, String id){

        binding.textMovieTitle.setText(title);
    }



    // Display Movie Synopsis or Overview
    private void onLoadSynopsis(String synopsis){
        binding.textOverviewLabel.setText(synopsis);
    }


    private void checkAllDataLoaded() {
        if (mMovie ) {


            binding.progressBar.setVisibility(GONE);
            binding.itemDetailContainer.setVisibility(View.VISIBLE);
            binding.PlayButtonIcon.setVisibility(View.VISIBLE);
        }
    }


    private void showIntroductoryOverlay() {
        if (mIntroductoryOverlay != null) {
            mIntroductoryOverlay.remove();
        }
        if ((mediaRouteMenuItem != null) && mediaRouteMenuItem.isVisible()) {


            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                mIntroductoryOverlay = new IntroductoryOverlay.Builder(
                        StreamingetailsActivity.this, mediaRouteMenuItem)
                        .setTitleText(getString(R.string.introducing_cast))
                        .setOverlayColor(R.color.primary)
                        .setSingleTime()
                        .setOnOverlayDismissedListener(
                                () -> mIntroductoryOverlay = null)
                        .build();
                mIntroductoryOverlay.show();

            },0);
        }
    }


    @Override
    protected void onDestroy() {


        if (maxAdView !=null){

            maxAdView.destroy();
            maxAdView = null;
        }


        if ( maxAd != null )
        {
            nativeAdLoader.destroy(maxAd);
            maxAd = null;
        }


        if (mNativeAd != null) {
            mNativeAd.destroy();
            mNativeAd = null;
        }


        if (mNativeAd != null) {
            mNativeAd.destroy();
            mNativeAd = null;
        }

        binding.appodealBannerView.removeAllViews();
        binding.appodealBannerView.removeAllViewsInLayout();

        if (bottomBanner!=null) {

            bottomBanner.destroy();
            bottomBanner = null;
        }


        if (rewardedAd !=null) {

            rewardedAd = null;
        }


        if (facebookBanner !=null) {

            facebookBanner.destroy();
            facebookBanner = null;

        }


        Appodeal.destroy(Appodeal.BANNER);
        Appodeal.destroy(Appodeal.INTERSTITIAL);
        Appodeal.destroy(Appodeal.REWARDED_VIDEO);
        Glide.get(this).clearMemory();
        binding = null;
        super.onDestroy();


    }

}
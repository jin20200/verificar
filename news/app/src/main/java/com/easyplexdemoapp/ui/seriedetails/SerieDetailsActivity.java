package com.easyplexdemoapp.ui.seriedetails;

import static android.view.View.GONE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;
import static com.easyplexdemoapp.util.Constants.ISUSER_MAIN_ACCOUNT;
import static com.easyplexdemoapp.util.Constants.RADUIS;
import static com.easyplexdemoapp.util.Constants.SAMPLING;
import static com.easyplexdemoapp.util.Constants.SEASONS;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
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
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.bumptech.glide.request.RequestOptions;
import com.easyplex.easyplexsupportedhosts.EasyPlexSupportedHosts;
import com.easyplex.easyplexsupportedhosts.Model.EasyPlexSupportedHostsModel;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.History;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.local.entity.Series;
import com.easyplexdemoapp.data.model.MovieResponse;
import com.easyplexdemoapp.data.model.auth.Rating;
import com.easyplexdemoapp.data.model.certifications.Certification;
import com.easyplexdemoapp.data.model.comments.Comment;
import com.easyplexdemoapp.data.model.episode.Episode;
import com.easyplexdemoapp.data.model.genres.Genre;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.data.model.media.Resume;
import com.easyplexdemoapp.data.model.media.StatusFav;
import com.easyplexdemoapp.data.model.serie.Season;
import com.easyplexdemoapp.data.repository.AuthRepository;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.data.repository.SettingsRepository;
import com.easyplexdemoapp.databinding.SerieDetailsBinding;
import com.easyplexdemoapp.ui.comments.CommentsAdapter;
import com.easyplexdemoapp.ui.home.adapters.RelatedsAdapter;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.DeviceManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.ui.moviedetails.adapters.CastAdapter;
import com.easyplexdemoapp.ui.player.activities.EasyPlexMainPlayer;
import com.easyplexdemoapp.ui.player.activities.EasyPlexPlayerActivity;
import com.easyplexdemoapp.ui.player.activities.EmbedActivity;
import com.easyplexdemoapp.ui.player.cast.ExpandedControlsActivity;
import com.easyplexdemoapp.ui.player.cast.queue.QueueDataProvider;
import com.easyplexdemoapp.ui.player.cast.utils.Utils;
import com.easyplexdemoapp.ui.player.fsm.state_machine.FsmPlayerApi;
import com.easyplexdemoapp.ui.settings.SettingsActivity;
import com.easyplexdemoapp.ui.users.MenuHandler;
import com.easyplexdemoapp.ui.viewmodels.LoginViewModel;
import com.easyplexdemoapp.ui.viewmodels.SerieDetailViewModel;
import com.easyplexdemoapp.util.AppController;
import com.easyplexdemoapp.util.Constants;
import com.easyplexdemoapp.util.DialogHelper;
import com.easyplexdemoapp.util.GlideApp;
import com.easyplexdemoapp.util.NetworkUtils;
import com.easyplexdemoapp.util.SpacingItemDecoration;
import com.easyplexdemoapp.util.Tools;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.images.WebImage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.LevelPlayRewardedVideoListener;
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
import com.xw.repo.BubbleSeekBar;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.AndroidInjection;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.BlurTransformation;
import timber.log.Timber;


/**s
 * EasyPlex - Movies - Live Streaming - TV Series, Anime
 *
 * @author @Y0bEX
 * @package EasyPlex - Movies - Live Streaming - TV Series, Anime
 * @copyright Copyright (c) 2021 Y0bEX,
 * @license <a href="http://codecanyon.net/wiki/support/legal-terms/licensing-terms/">..<a href=".</a>
">* @profile https://codecan</a>yon.net/user/yobex
 * @link yobexd@gmail.com
 * @skype yobexd@gmail.com
 **/


public class SerieDetailsActivity extends AppCompatActivity {

    private MaxRewardedAd maxRewardedAd;
    private MaxInterstitialAd maxInterstitialAd;
    private VungleBanner vungleBanner;
    private boolean isMovieFav = false;
    private LoginViewModel loginViewModel;
    private static final String TAG = "SerieDetailsActivity";
    private com.google.android.gms.ads.nativead.NativeAd mNativeAd;
    private MediaView nativeAdMedia;
    private NativeAd nativeAd;
    private MaxAdView maxAdView;
    private MaxNativeAdLoader nativeAdLoader;
    private MaxAd maxAd;
    private BannerView bottomBanner;
    private String mediaGenre;
    public static final String ARG_MOVIE = "movie";
    SerieDetailsBinding serieDetailsBinding;


    @Inject
    RelatedsAdapter relatedsAdapter;

    @Inject
    AppController appController;

    @Inject
    DeviceManager deviceManager;

    @Inject
    MenuHandler menuHandler;

    @Inject
    @Named("vpn")
    boolean checkVpn;


    @Inject
    @Named("root")
    @Nullable
    ApplicationInfo provideRootCheck;

    @Inject
    @Named("sniffer")
    @Nullable
    ApplicationInfo provideSnifferCheck;

    @Inject
    MediaRepository mediaRepository;

    @Inject
    @Named("ready")
    boolean settingReady;

    @Inject
    SettingsManager settingsManager;

    @Inject
    TokenManager tokenManager;

    @Inject ViewModelProvider.Factory viewModelFactory;

    private EasyPlexSupportedHosts easyPlexSupportedHosts;

    private RewardedAd mRewardedAd;
    boolean isLoading;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    AuthManager authManager;

    @Inject
    AuthRepository authRepository;

    @Inject
    @Named("cuepointUrl")
    String cuepointUrl;

    @Inject
    @Named("cuepoint")
    String cuePoint;

    @Inject
    @Named("cuepointY")
    String cuePointY;

    @Inject
    @Named("cuepointN")
    String cuePointN;

    @Inject
    SharedPreferences.Editor sharedPreferencesEditor;

    private com.facebook.ads.AdView facebookBanner;

    private SerieDetailViewModel serieDetailViewModel;

    @Inject
    MediaRepository repository;


    @Inject
    CastAdapter mCastAdapter;

    @Inject
    SettingsRepository settingsRepository;

    EpisodeAdapter mEpisodesSerieAdapter;
    private boolean mSerieLoaded;
    private boolean mEpisodesLoaded;
    private  Media serie;
    private Series series;
    private CastContext mCastContext;
    private final SessionManagerListener<CastSession> mSessionManagerListener =
            new MySessionManagerListener();
    private CastSession mCastSession;
    private MenuItem mediaRouteMenuItem;
    private MenuItem mQueueMenuItem;
    private IntroductoryOverlay mIntroductoryOverlay;
    private CastStateListener mCastStateListener;
    private static final int PRELOAD_TIME_S = 2;
    private String externalId;
    private CommentsAdapter commentsAdapter;

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
        public void onSessionStartFailed(@NotNull CastSession session, int error) {

            //
        }

        @Override
        public void onSessionEnding(@NotNull CastSession session) {

            //
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

        serieDetailsBinding = DataBindingUtil.setContentView(this,R.layout.serie_details);

        serieDetailsBinding.setController(appController);

        serieDetailsBinding.setMenu(menuHandler);

        menuHandler.isLayoutChangeEnabled.set(settingsManager.getSettings().getEnablelayoutchange() == 1);


        if (authManager.getUserInfo().getPremuim() != 1 ) {

            onInitRewards();
        }

        Intent intent = getIntent();

        serie = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? intent.getParcelableExtra(ARG_MOVIE, Media.class) : intent.getParcelableExtra(ARG_MOVIE);


        if (settingsManager.getSettings().getVpn() ==1 && checkVpn){

            serieDetailsBinding.backbutton.performClick();

            Tools.ToastHelper(getApplicationContext(),getString(R.string.vpn_message));


        }




        mCastStateListener = newState -> {
            if (newState != CastState.NO_DEVICES_AVAILABLE) {
                showIntroductoryOverlay();
            }
        };
        mCastContext = CastContext.getSharedInstance(this);


        mSerieLoaded = false;
        serieDetailsBinding.progressBar.setVisibility(View.VISIBLE);
        serieDetailsBinding.scrollView.setVisibility(GONE);


        // ViewModel to cache, retrieve data for Serie Details
        serieDetailViewModel = new ViewModelProvider(this, viewModelFactory).get(SerieDetailViewModel.class);

        loginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);


        initMovieDetails();
        onInitCastRecycleView();


        Tools.setSystemBarTransparent(this);


        if (settingsManager.getSettings().getEnableComments() !=1){

            serieDetailsBinding.floatingCommentIcon.setVisibility(GONE);
            serieDetailsBinding.commentsize.setVisibility(GONE);
        }

    }


    private void onInitRewards() {

        Appodeal.initialize(this, settingsManager.getSettings().getAdUnitIdAppodealRewarded(),
                Appodeal.INTERSTITIAL | Appodeal.BANNER | Appodeal.REWARDED_VIDEO, list -> {
                });


        IronSource.init(this, settingsManager.getSettings().getIronsourceAppKey(),IronSource.AD_UNIT.REWARDED_VIDEO,IronSource.AD_UNIT.INTERSTITIAL,IronSource.AD_UNIT.BANNER);

        if (settingsManager.getSettings().getApplovinInterstitialUnitid() !=null && !settingsManager.getSettings().getApplovinInterstitialUnitid().isEmpty()) {

            maxInterstitialAd = new MaxInterstitialAd(settingsManager.getSettings().getApplovinInterstitialUnitid(), this );
            maxInterstitialAd.loadAd();
        }


        String defaultRewardedNetworkAds = settingsManager.getSettings().getDefaultRewardedNetworkAds();

        if (getString(R.string.applovin).equals(defaultRewardedNetworkAds)) {

            maxRewardedAd = MaxRewardedAd.getInstance( settingsManager.getSettings().getApplovinRewardUnitid(), this);
            maxRewardedAd.loadAd();

        }

        if (settingsManager.getSettings().getVungleAppid() !=null) {

            Vungle.loadAd(settingsManager.getSettings().getVungleInterstitialPlacementName(), new LoadAdCallback() {
                @Override
                public void onAdLoad(String id) {

                    //
                }

                @Override
                public void onError(String id, VungleException e) {

                    //
                }
            });

        }
    }

    private void onInitCastRecycleView() {

        serieDetailsBinding.recyclerViewCastMovieDetail.setHasFixedSize(true);
        serieDetailsBinding.recyclerViewCastMovieDetail.setNestedScrollingEnabled(false);
        serieDetailsBinding.recyclerViewCastMovieDetail.setLayoutManager(new LinearLayoutManager(SerieDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
        serieDetailsBinding.recyclerViewCastMovieDetail.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(this, 0), true));
        serieDetailsBinding.recyclerViewCastMovieDetail.setAdapter(mCastAdapter);
    }



    private void initMovieDetails() {

            serieDetailViewModel.getSerieDetails(serie.getId());
            serieDetailViewModel.movieDetailMutableLiveData.observe(this, serieDetail -> {

                series  = new Series(serieDetail.getId(),serieDetail.getId(),serieDetail.getPosterPath(),serieDetail.getName());

                serie = serieDetail;

                this.externalId = serieDetail.getImdbExternalId();
                onLoadImage(serieDetail.getPosterPath());
                onLoadTitle(serieDetail.getName());
                onLoadSeasons(serieDetail);
                try {
                    onLoadDate(serieDetail.getFirstAirDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                onLoadSynopsis(serieDetail.getOverview());
                onLoadGenres(serieDetail.getGenres());
                onLoadBackButton();
                onLoadRating(serieDetail.getVoteAverage());
                onLoadCertification(serieDetail.getCertifications());
                onLoadViews(serieDetail.getViews());
                onLoadRelatedsMovies(Integer.parseInt(serieDetail.getId()));
                onLoadSerieCast(serieDetail);
                onLoadUsersReviews(serieDetail.getVoteAverage());
                onLoadPremuim(serieDetail.getPremuim());
                serieDetailsBinding.review.setOnClickListener(v -> onSentReview(serieDetail));


                serieDetailsBinding.floatingCommentIcon.setOnClickListener(v -> {

                    if (tokenManager.getToken().getAccessToken() != null) {

                        onLoadSerieComments(serieDetail.getId());

                    }else {



                        Tools.onLoadSnakeBar(this,serieDetailsBinding.constraintLayout);

                    }

                });


                if (settingsManager.getSettings().getEnableComments() == 1){

                    mediaRepository.getSerieComments(Integer.parseInt(serieDetail.getId()),settingsManager.getSettings().getApiKey())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<>() {
                                @Override
                                public void onSubscribe(@NotNull Disposable d) {

                                    //

                                }

                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onNext(@NotNull MovieResponse movieResponse) {

                                    if (!movieResponse.getComments().isEmpty()){

                                        serieDetailsBinding.commentsize.setText(getString(R.string.comment_size_views)+Tools.getViewFormat(movieResponse.getComments().size()));

                                    }else {

                                        serieDetailsBinding.commentsizeLinear.setVisibility(GONE);
                                    }


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

                }else {


                    serieDetailsBinding.commentsizeLinear.setVisibility(GONE);
                }


                if (authManager.getUserInfo().getPremuim() != 1 ) {

                    Tools.onLoadNetworksInter(settingsManager, SerieDetailsActivity.this,maxInterstitialAd);
                    Tools.onloadBanners(settingsManager,SerieDetailsActivity.this, null,serieDetailsBinding,null, null, null);



                } else {

                    serieDetailsBinding.bannerContainer.setVisibility(GONE);
                    serieDetailsBinding.adViewContainer.setVisibility(GONE);
                    serieDetailsBinding.appodealBannerView.setVisibility(GONE);
                    serieDetailsBinding.flAdplaceholder.setVisibility(GONE);

                }




                serieDetailsBinding.scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
                    int scrollY =  serieDetailsBinding.scrollView.getScrollY();
                    int color = Color.parseColor("#E6070707"); // ideally a global variable
                    if (scrollY < 256) {
                        int alpha = (scrollY << 24) | (-1 >>> 8) ;
                        color &= (alpha);

                        serieDetailsBinding.serieName.setText("");
                        serieDetailsBinding.serieName.setVisibility(View.GONE);


                    }else {

                        serieDetailsBinding.serieName.setText(serieDetail.getName());
                        serieDetailsBinding.serieName.setVisibility(View.VISIBLE);

                    }
                    serieDetailsBinding.toolbar.setBackgroundColor(color);

                });

                onLoadToolbar();

                serieDetailsBinding.report.setOnClickListener(v -> onLoadReport(serieDetail.getName(), serieDetail.getPosterPath()));


                serieDetailsBinding.favoriteIcon.setOnClickListener(view -> {

                    if (settingsManager.getSettings().getFavoriteonline() == 1 && tokenManager.getToken().getAccessToken() !=null) {

                        if (isMovieFav) {

                            authRepository.getDeleteSerieOnline(serieDetail.getId())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .cache()
                                    .subscribe(new Observer<>() {
                                        @Override
                                        public void onSubscribe(@NotNull Disposable d) {

                                            //

                                        }

                                        @Override
                                        public void onNext(@NotNull StatusFav statusFav) {

                                            Tools.ToastHelper(getApplicationContext(),getString(R.string.remove_watch_list));

                                            serieDetailsBinding.favoriteImage.setImageResource(R.drawable.add_from_queue);

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


                            isMovieFav = false;
                            serieDetailsBinding.favoriteImage.setImageResource(R.drawable.add_from_queue);


                        }else {


                            authRepository.getAddSerieOnline(serieDetail.getId())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .cache()
                                    .subscribe(new Observer<>() {
                                        @Override
                                        public void onSubscribe(@NotNull Disposable d) {

                                            //

                                        }

                                        @Override
                                        public void onNext(@NotNull StatusFav statusFav) {

                                            Tools.ToastHelper(getApplicationContext(),"Removed From Watchlist");


                                            Timber.i("Added To Watchlist");


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

                            isMovieFav = true;
                            serieDetailsBinding.favoriteImage.setImageResource(R.drawable.ic_in_favorite);
                        }

                    }else  {

                        onFavoriteClick(series);
                    }

                });


                checkMediaFavorite(serieDetail);

                onCheckSerieResumeHistory(serieDetail);

                serieDetailsBinding.ButtonPlayTrailer.setOnClickListener((View v) -> onLoadTrailer(serieDetail.getPreviewPath(),
                        serieDetail.getTitle(),
                        serieDetail.getBackdropPath(), serieDetail.getTrailerUrl()));


                serieDetailsBinding.shareIcon.setOnClickListener(v -> Tools.onShareMedia(this,serieDetail,settingsManager,"serie"));





            });





            mSerieLoaded = true;
            checkAllDataLoaded();


    }

    private void onLoadCertification(List<Certification> certifications) {
        if (certifications == null || certifications.isEmpty()) {
            serieDetailsBinding.maturityRating.setVisibility(GONE);
            serieDetailsBinding.MovieCertification.setVisibility(GONE);
            serieDetailsBinding.viewMovieCertification.setVisibility(GONE);
            return;
        }

        StringBuilder certificationList = new StringBuilder();
        Certification firstCertification = certifications.get(0);

        certificationList.append(firstCertification.getCountryCode())
                .append(firstCertification.getCertification());

        serieDetailsBinding.viewMovieCertification.setText(certificationList);
        serieDetailsBinding.viewMovieCertification.setVisibility(View.VISIBLE);
        serieDetailsBinding.maturityRating.setVisibility(View.VISIBLE);

        serieDetailsBinding.viewMovieCertification.setOnClickListener(v ->
                Toast.makeText(getApplicationContext(), firstCertification.getMeaning(), Toast.LENGTH_SHORT).show()
        );
    }

    private void onLoadPremuim(int premuim) {

        if (premuim == 1) {

            serieDetailsBinding.moviePremuim.setVisibility(View.VISIBLE);


        }else {

            serieDetailsBinding.moviePremuim.setVisibility(View.GONE);
        }

    }

    private void onLoadUsersReviews(float voteAverage) {

        serieDetailsBinding.userReview.setText(String.valueOf(voteAverage));
    }

    private void onSentReview(Media serieDetail) {


        if (tokenManager.getToken().getAccessToken() == null) {

            Toast.makeText(this, getText(R.string.review_require_login), Toast.LENGTH_SHORT).show();
            return;
        }


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_review);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.gravity = Gravity.BOTTOM;
        lp.width = MATCH_PARENT;
        lp.height = MATCH_PARENT;


        TextView reviewMovieName = dialog.findViewById(R.id.movietitle);


        TextView userRating = dialog.findViewById(R.id.userRating);

        BubbleSeekBar storySeekbar = dialog.findViewById(R.id.storySeekbar);

        BubbleSeekBar caractersSeekbar = dialog.findViewById(R.id.caractersSeekbar);

        BubbleSeekBar mottionsdesginSeekbar = dialog.findViewById(R.id.mottionsdesginSeekbar);

        BubbleSeekBar musicSeekbar = dialog.findViewById(R.id.musicSeekbar);

        reviewMovieName.setText(serieDetail.getName());




        storySeekbar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

                userRating.setText(Integer.toString((int) Tools.getAvg(storySeekbar.getProgressFloat(),
                        caractersSeekbar.getProgressFloat()
                        ,mottionsdesginSeekbar.getProgressFloat()
                        ,musicSeekbar.getProgressFloat(),true)));

            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

                //

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

                //
            }
        });


        caractersSeekbar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

                userRating.setText(Integer.toString((int) Tools.getAvg(storySeekbar.getProgressFloat(),
                        caractersSeekbar.getProgressFloat()
                        ,mottionsdesginSeekbar.getProgressFloat()
                        ,musicSeekbar.getProgressFloat(),true)));

            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                //
            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                //
            }
        });


        mottionsdesginSeekbar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

                userRating.setText(Integer.toString((int) Tools.getAvg(storySeekbar.getProgressFloat(),
                        caractersSeekbar.getProgressFloat()
                        ,mottionsdesginSeekbar.getProgressFloat()
                        ,musicSeekbar.getProgressFloat(),true)));
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                //
            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                //
            }
        });


        musicSeekbar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

                userRating.setText(Integer.toString((int) Tools.getAvg(storySeekbar.getProgressFloat(),
                        caractersSeekbar.getProgressFloat()
                        ,mottionsdesginSeekbar.getProgressFloat()
                        ,musicSeekbar.getProgressFloat(),true)));

            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                //
            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                //
            }
        });


        Button sendReport = dialog.findViewById(R.id.view_report);

        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());


        sendReport.setOnClickListener(v -> {


            if (serieDetail.getVoteAverage() !=0){

                double newUserRating = Math.round(serieDetail.getVoteAverage() + Tools.getAvg(storySeekbar.getProgressFloat(),
                        caractersSeekbar.getProgressFloat()
                        ,mottionsdesginSeekbar.getProgressFloat()
                        ,musicSeekbar.getProgressFloat(),false)) /2;


                if (tokenManager.getToken().getAccessToken() !=null) {

                    authRepository.addRating(serieDetail.getId(),newUserRating, "serie")
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<>() {
                                @Override
                                public void onSubscribe(@NotNull Disposable d) {

                                    //

                                }

                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onNext(@NotNull Rating rating) {

                                    dialog.dismiss();

                                    Toast.makeText(SerieDetailsActivity.this, R.string.review_sent, Toast.LENGTH_SHORT).show();

                                    serieDetailViewModel.getSerieDetails(serieDetail.getId());
                                    initMovieDetails();
                                }

                                @Override
                                public void onError(@NotNull Throwable e) {

                                    Toast.makeText(SerieDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete() {

                                    //

                                }
                            });

                }else {

                    Tools.ToastHelper(SerieDetailsActivity.this,getString(R.string.review_require_login));
                }

            }  else {

                double newUserRating = Math.round(Tools.getAvg(storySeekbar.getProgressFloat(),
                        caractersSeekbar.getProgressFloat()
                        ,mottionsdesginSeekbar.getProgressFloat()
                        ,musicSeekbar.getProgressFloat(),false)) /2;


                if (tokenManager.getToken().getAccessToken() !=null) {

                    authRepository.addRating(serieDetail.getId(),newUserRating, "serie")
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<>() {
                                @Override
                                public void onSubscribe(@NotNull Disposable d) {

                                    //

                                }

                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onNext(@NotNull Rating rating) {

                                    dialog.dismiss();

                                    Toast.makeText(SerieDetailsActivity.this, R.string.rating_sent, Toast.LENGTH_SHORT).show();

                                    serieDetailViewModel.getSerieDetails(serieDetail.getId());
                                    initMovieDetails();
                                }

                                @Override
                                public void onError(@NotNull Throwable e) {

                                    Toast.makeText(SerieDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete() {

                                    //

                                }
                            });

                }else {

                    Tools.ToastHelper(SerieDetailsActivity.this,getString(R.string.review_require_login));
                }
            }




        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);

        dialog.findViewById(R.id.bt_close).setOnClickListener(y -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    private void onCheckSerieResumeHistory(Media serieDetail) {


        mediaRepository.hasHistory2(Integer.parseInt(serieDetail.getId()),"1").observe(this, history -> {

            if (history !=null) {

                serieDetailsBinding.resumePlay.setVisibility(View.VISIBLE);
                serieDetailsBinding.resumePlayTitle.setText(history.getTitle());

                if (settingsManager.getSettings().getResumeOffline() == 1 && history.getEpisodeId() !=null) {



                    if (sharedPreferences.getBoolean(ISUSER_MAIN_ACCOUNT,false)){

                        mediaRepository.hasResume(Integer.parseInt(history.getEpisodeId())).observe(this, resumeInfo -> {

                            onLoadResume(history, resumeInfo);

                        });

                    }else {

                        Integer userId = settingsManager.getSettings().getProfileSelection() == 1  ? authManager.getSettingsProfile().getId() : authManager.getUserInfo().getId();

                        mediaRepository.hasResumeProfile(Integer.parseInt(history.getEpisodeId()),userId).observe(this, resumeInfo -> {

                            onLoadResume(history, resumeInfo);

                        });

                    }



                }else {

                    mediaRepository.getResumeById(String.valueOf(history.getEpisodeId()),settingsManager.getSettings().getApiKey())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<>() {
                                @Override
                                public void onSubscribe(@NotNull Disposable d) {

                                    //

                                }

                                @SuppressLint({"TimberArgCount", "SetTextI18n"})
                                @Override
                                public void onNext(@NotNull Resume resume) {


                                    if (resume.getTmdb() != null && resume.getResumePosition()
                                            != null && resume.getTmdb().equals(history.getEpisodeTmdb())
                                            && Tools.id(SerieDetailsActivity.this).equals(resume.getDeviceId())) {

                                        double d = resume.getResumePosition();
                                        double moveProgress = d * 100 / resume.getMovieDuration();

                                        serieDetailsBinding.linearResumeProgressBar.setVisibility(View.VISIBLE);
                                        serieDetailsBinding.resumeProgressBar.setVisibility(View.VISIBLE);
                                        serieDetailsBinding.resumeProgressBar.setProgress((int) moveProgress);
                                        serieDetailsBinding.timeRemaning.setText(Tools.getProgressTime((resume.getMovieDuration() - resume.getResumePosition()), true));

                                    } else {

                                        serieDetailsBinding.resumeProgressBar.setProgress(0);
                                        serieDetailsBinding.resumeProgressBar.setVisibility(GONE);
                                        serieDetailsBinding.timeRemaning.setVisibility(GONE);
                                        serieDetailsBinding.linearResumeProgressBar.setVisibility(GONE);

                                    }

                                }

                                @SuppressLint("ClickableViewAccessibility")
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



                serieDetailsBinding.topResume.setOnClickListener(v -> {


                    if (serieDetail.getPremuim() == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() !=null) {

                        onLoadResumeFromHistory(history,serieDetail);


                    }else if (settingsManager.getSettings().getWachAdsToUnlock() == 1 && serieDetail.getPremuim() != 1 && authManager.getUserInfo().getPremuim() == 0) {

                        onLoadSubscribeDialog(history,serieDetail);

                    }else if (settingsManager.getSettings().getWachAdsToUnlock() == 0 && serieDetail.getPremuim() == 0 ){


                        onLoadResumeFromHistory(history,serieDetail);


                    } else if (authManager.getUserInfo().getPremuim() == 1 && serieDetail.getPremuim() == 0){


                        onLoadResumeFromHistory(history,serieDetail);


                    }else {

                        DialogHelper.showPremuimWarning(this);

                    }
                });

            }else {

                serieDetailsBinding.resumePlay.setVisibility(GONE);
            }


        });

    }

    private void onLoadResume(History history, Resume resumeInfo) {

        if (resumeInfo !=null){

            loadRewardedAd();


            if (resumeInfo.getTmdb() != null && resumeInfo.getResumePosition()
                    !=null && resumeInfo.getTmdb().equals(history.getEpisodeTmdb())
                    && Tools.id(SerieDetailsActivity.this).equals(resumeInfo.getDeviceId())) {


                double d = resumeInfo.getResumePosition();

                double moveProgress = d * 100 / resumeInfo.getMovieDuration();


                serieDetailsBinding.linearResumeProgressBar.setVisibility(View.VISIBLE);
                serieDetailsBinding.resumeProgressBar.setVisibility(View.VISIBLE);
                serieDetailsBinding.resumeProgressBar.setProgress((int) moveProgress);
                serieDetailsBinding.timeRemaning.setText(Tools.getProgressTime((resumeInfo.getMovieDuration() - resumeInfo.getResumePosition()), true));



            } else {

                serieDetailsBinding.resumeProgressBar.setProgress(0);
                serieDetailsBinding.resumeProgressBar.setVisibility(GONE);
                serieDetailsBinding.timeRemaning.setVisibility(GONE);
                serieDetailsBinding.linearResumeProgressBar.setVisibility(GONE);

            }


        }else {


            serieDetailsBinding.resumeProgressBar.setProgress(0);
            serieDetailsBinding.resumeProgressBar.setVisibility(GONE);
            serieDetailsBinding.timeRemaning.setVisibility(GONE);
            serieDetailsBinding.linearResumeProgressBar.setVisibility(GONE);

        }
    }


    private void onLoadApplovinNativeAds() {

        if (settingsManager.getSettings().getApplovin_native() == 1 && settingsManager.getSettings().getApplovinNativeUnitid() !=null){

            nativeAdLoader = new MaxNativeAdLoader(settingsManager.getSettings().getApplovinNativeUnitid(), this);
            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    // Clean up any pre-existing native ad to prevent memory leaks.
                    if (maxAd != null) {
                        nativeAdLoader.destroy(maxAd);
                    }

                    // Save ad for cleanup.
                    maxAd = ad;

                    // Add ad view to view.
                    serieDetailsBinding.maxNativeAds.removeAllViews();
                    serieDetailsBinding.maxNativeAds.addView(nativeAdView);
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    // We recommend retrying with exponentially higher delays up to a maximum delay
                }

                @Override
                public void onNativeAdClicked(final MaxAd ad) {
                    // Optional click callback
                }
            });

            nativeAdLoader.loadAd();

        }
    }

    private void onLoadApplovinBanner() {
        if (settingsManager.getSettings().getApplovinBanner() == 1) {

            maxAdView = new MaxAdView(settingsManager.getSettings().getApplovinBannerUnitid(),this);
            serieDetailsBinding.maxAdView.addView(maxAdView);
            maxAdView.loadAd();

            maxAdView.setListener(new MaxAdViewAdListener() {
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
                public void onAdExpanded(MaxAd ad) {
                    //
                }

                @Override
                public void onAdCollapsed(MaxAd ad) {
                    //
                }
            });
            maxAdView.setRevenueListener(ad -> {

            });
        }
    }

    private void onLoadApplovinInterStetial() {


        if (settingsManager.getSettings().getApplovinInterstitialUnitid() !=null && !settingsManager.getSettings().getApplovinInterstitialUnitid().isEmpty() &&
                maxInterstitialAd !=null) {

            Tools.onLoadAppLovinInterstetial(settingsManager.getSettings().getApplovinInterstitial()
                    ,settingsManager.getSettings().getApplovinInterstitialShow(),maxInterstitialAd.isReady(),maxInterstitialAd);
        }

    }

    private void onLoadVungleInterStetial() {

        Tools.onLoadVungleInterstetial(settingsManager.getSettings().getVungleInterstitial()
                ,settingsManager.getSettings().getVungle_interstitial_show(),settingsManager);

    }

    private void onLoadVungleBanner() {

        if (settingsManager.getSettings().getVungleBanner() == 1) {

            final BannerAdConfig bannerAdConfig = new BannerAdConfig();
            bannerAdConfig.setAdSize(AdConfig.AdSize.BANNER);

            if (Banners.canPlayAd(settingsManager.getSettings().getVungleBannerPlacementName(), bannerAdConfig.getAdSize())) {
                vungleBanner = Banners.getBanner(settingsManager.getSettings().getVungleBannerPlacementName(), bannerAdConfig, new PlayAdCallback() {
                    @Override
                    public void creativeId(String creativeId) {
                        //

                    }

                    @Override
                    public void onAdStart(String placementId) {
                        //
                    }

                    @Override
                    public void onAdEnd(String placementId, boolean completed, boolean isCTAClicked) {
                        //
                    }

                    @Override
                    public void onAdEnd(String placementId) {
                        //
                    }

                    @Override
                    public void onAdClick(String placementId) {
                        //
                    }

                    @Override
                    public void onAdRewarded(String placementId) {
                        //
                    }

                    @Override
                    public void onAdLeftApplication(String placementId) {
                        //
                    }

                    @Override
                    public void onError(String placementId, VungleException exception) {

                        //

                    }

                    @Override
                    public void onAdViewed(String placementId) {
                        //
                    }
                });
                serieDetailsBinding.VungleBannerContainerIron.addView(vungleBanner);
            }
        }
    }

    private void checkMediaFavorite(Media serieDetail) {

        if (settingsManager.getSettings().getFavoriteonline() == 1  && tokenManager.getToken().getAccessToken() !=null) {

            loginViewModel.isSerieFavoriteOnline(serieDetail.getId());
            loginViewModel.isSerieFavoriteOnlineMutableLiveData.observe(this, favAddOnline -> {

                if (favAddOnline.getStatus() == 1) {

                    isMovieFav = true;

                    serieDetailsBinding.favoriteImage.setImageResource(R.drawable.ic_in_favorite);

                } else {

                    isMovieFav = false;

                    serieDetailsBinding.favoriteImage.setImageResource(R.drawable.add_from_queue);

                }

            });

        } else {

            if (mediaRepository.isSerieFavorite(Integer.parseInt(serieDetail.getId()))) {

            serieDetailsBinding.favoriteImage.setImageResource(R.drawable.ic_in_favorite);

        } else {


            serieDetailsBinding.favoriteImage.setImageResource(R.drawable.add_from_queue);

        }
          }

    }


    private void onLoadSerieCast(Media serieDetail) {



        if (settingsManager.getSettings().getDefaultCastOption().equals("IMDB")){

            if (serieDetail.getTmdbId() !=null) {

                serieDetailViewModel.getSerieCast(Integer.parseInt(serieDetail.getTmdbId()));
                serieDetailViewModel.serieCreditsMutableLiveData.observe(this, credits -> {

                    mCastAdapter.addCasts(credits.getCasts());

                });
            }

        }else {

            // Starring RecycleView
            mCastAdapter.addCasts(serieDetail.getCast());

        }


    }


    private void onLoadSubscribeDialog(History history, Media serieDetail) {


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_subscribe);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

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

                onLoadApplovinAds(history,serieDetail);

            }else if (getString(R.string.vungle).equals(defaultRewardedNetworkAds)) {

                onLoadVungleAds(history,serieDetail);

            }else if (getString(R.string.ironsource).equals(defaultRewardedNetworkAds)) {

                onLoadIronsourceAds(history,serieDetail);

            }else if (getString(R.string.unityads).equals(defaultRewardedNetworkAds)) {

                onLoadUnityAds(history,serieDetail);


            } else if (getString(R.string.admob).equals(defaultRewardedNetworkAds)) {


                onLoadAdmobRewardAds(history,serieDetail);


            } else if (getString(R.string.facebook).equals(defaultRewardedNetworkAds)) {

                onLoadFaceBookRewardAds(history,serieDetail);

            }else if (getString(R.string.appodeal).equals(defaultRewardedNetworkAds)) {

                onLoadAppOdealRewardAds(history,serieDetail);

            }

            dialog.dismiss();


        });


        dialog.findViewById(R.id.bt_close).setOnClickListener(v ->

                dialog.dismiss());


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    private void onLoadIronsourceAds(History history, Media serieDetail) {

        IronSource.showRewardedVideo(settingsManager.getSettings().getIronsourceRewardPlacementName());

        IronSource.setLevelPlayRewardedVideoListener(new LevelPlayRewardedVideoListener() {
            @Override
            public void onAdAvailable(AdInfo adInfo) {

            }

            @Override
            public void onAdUnavailable() {

            }

            @Override
            public void onAdOpened(AdInfo adInfo) {

            }

            @Override
            public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {

            }

            @Override
            public void onAdClicked(Placement placement, AdInfo adInfo) {

            }

            @Override
            public void onAdRewarded(Placement placement, AdInfo adInfo) {

                onLoadResumeFromHistory(history,serieDetail);
            }

            @Override
            public void onAdClosed(AdInfo adInfo) {

            }
        });
    }

    private void onLoadVungleAds(History history, Media serieDetail) {

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

                onLoadResumeFromHistory(history,serieDetail);
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

    private void onLoadApplovinAds(History history, Media serieDetail) {



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

                onLoadResumeFromHistory(history,serieDetail);
                maxRewardedAd.loadAd();
            }

            @Override
            public void onUserRewarded(MaxAd ad, MaxReward reward) {
                //
            }
        });


    }


    private void onLoadAppOdealRewardAds(History history, Media serieDetail) {

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

                onLoadResumeFromHistory(history,serieDetail);

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

    private void onLoadFaceBookRewardAds(History history, Media serieDetail) {

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

                onLoadResumeFromHistory(history,serieDetail);

            }


        };


        facebookInterstitialAd.loadAd(
                facebookInterstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());

    }

    private void loadRewardedAd() {

        if (mRewardedAd == null) {
            isLoading = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(
                    SerieDetailsActivity.this,
                    settingsManager.getSettings().getAdUnitIdRewarded(),
                    adRequest,
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                            mRewardedAd = null;

                           isLoading = false;

                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {

                            isLoading = false;
                            mRewardedAd = rewardedAd;
                        }
                    });
        }

    }

    private void onLoadAdmobRewardAds(History history, Media serieDetail) {

        if (mRewardedAd == null) {

            Tools.ToastHelper(getApplicationContext(),"The rewarded ad wasn't ready yet");

            return;
        }
        mRewardedAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        //
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        mRewardedAd = null;
                        // Preload the next rewarded ad.
                        loadRewardedAd();
                    }
                });
        mRewardedAd.show(
                SerieDetailsActivity.this,
                rewardItem ->       onLoadResumeFromHistory(history,serieDetail));
    }

    private void onLoadUnityAds(History history, Media serieDetail) {

        UnityAds.show (this, settingsManager.getSettings().getUnityRewardPlacementId(), new IUnityAdsShowListener() {
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

                onLoadResumeFromHistory(history,serieDetail);
            }
        });

    }


    private void onLoadResumeFromHistory(History history, Media serieDetail) {

        mediaRepository.getSerieEpisodeDetails(history.getEpisodeId(), settingsManager.getSettings().getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                        //

                    }

                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onNext(@NotNull MovieResponse movieResponse) {


                        if (settingsManager.getSettings().getServerDialogSelection() == 1) {

                            String[] charSequence = new String[movieResponse.getEpisodes().get(0).getVideos().size()];
                            for (int i = 0; i < movieResponse.getEpisodes().get(0).getVideos().size(); i++) {
                                charSequence[i] = String.valueOf(movieResponse.getEpisodes().get(0).getVideos().get(i).getServer());

                            }


                            final AlertDialog.Builder builder = new AlertDialog.Builder(SerieDetailsActivity.this
                                    , R.style.MyAlertDialogTheme);
                            builder.setTitle(R.string.source_quality);
                            builder.setCancelable(true);
                            builder.setItems(charSequence, (dialogInterface, wich) -> {


                                if (movieResponse.getEpisodes().get(0).getVideos().get(wich).getEmbed() == 1) {


                                    Intent intent = new Intent(SerieDetailsActivity.this, EmbedActivity.class);
                                    intent.putExtra(Constants.MOVIE_LINK, movieResponse.getEpisodes().get(0).getVideos().get(wich).getLink());
                                    startActivity(intent);


                                }  else if (movieResponse.getEpisodes().get(0).getVideos().get(wich).getSupportedHosts() == 1) {


                                    easyPlexSupportedHosts = new EasyPlexSupportedHosts(SerieDetailsActivity.this);
                                    easyPlexSupportedHosts.onFinish(new EasyPlexSupportedHosts.OnTaskCompleted() {

                                        @Override
                                        public void onTaskCompleted(ArrayList<EasyPlexSupportedHostsModel> vidURL, boolean multipleQuality) {

                                            if (multipleQuality) {
                                                if (vidURL != null) {

                                                    CharSequence[] name = new CharSequence[vidURL.size()];

                                                    for (int i = 0; i < vidURL.size(); i++) {
                                                        name[i] = vidURL.get(i).getQuality();
                                                    }

                                                    final AlertDialog.Builder builder = new AlertDialog.Builder(SerieDetailsActivity.this, R.style.MyAlertDialogTheme);
                                                    builder.setTitle(SerieDetailsActivity.this.getString(R.string.select_qualities));
                                                    builder.setCancelable(true);
                                                    builder.setItems(name, (dialogInterface, i) -> {


                                                        if (mCastSession != null && mCastSession.isConnected()) {

                                                            startStreamCasting(movieResponse, vidURL.get(i).getUrl());

                                                        } else if (settingsManager.getSettings().getVlc() == 1) {


                                                            final Dialog dialog = new Dialog(SerieDetailsActivity.this);
                                                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                            dialog.setContentView(R.layout.dialog_bottom_stream);
                                                            dialog.setCancelable(false);
                                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                                                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                                            lp.copyFrom(dialog.getWindow().getAttributes());

                                                            lp.gravity = Gravity.BOTTOM;
                                                            lp.width = MATCH_PARENT;
                                                            lp.height = MATCH_PARENT;


                                                            LinearLayout vlc = dialog.findViewById(R.id.vlc);
                                                            LinearLayout mxPlayer = dialog.findViewById(R.id.mxPlayer);
                                                            LinearLayout easyplexPlayer = dialog.findViewById(R.id.easyplexPlayer);
                                                            LinearLayout webcast = dialog.findViewById(R.id.webCast);


                                                            webcast.setOnClickListener(v12 -> {

                                                                for (Episode episode : movieResponse.getEpisodes()) {
                                                                    Tools.streamEpisodeFromMxWebcast(SerieDetailsActivity.this, vidURL.get(i).getUrl(), episode, settingsManager);
                                                                    dialog.hide();
                                                                }


                                                            });


                                                            vlc.setOnClickListener(v12 -> {

                                                                for (Episode episode : movieResponse.getEpisodes()) {
                                                                    Tools.streamEpisodeFromVlc(SerieDetailsActivity.this, vidURL.get(i).getUrl(), episode, settingsManager);
                                                                    dialog.hide();
                                                                }

                                                            });


                                                            mxPlayer.setOnClickListener(v12 -> {


                                                                for (Episode episode : movieResponse.getEpisodes()) {
                                                                    Tools.streamEpisodeFromMxPlayer(SerieDetailsActivity.this, vidURL.get(i).getUrl(), episode, settingsManager);
                                                                    dialog.hide();
                                                                }

                                                            });

                                                            easyplexPlayer.setOnClickListener(v12 -> {
                                                            onLoadMainPlayerStreamYoutube(vidURL.get(i).getUrl(), history, movieResponse, serieDetail);
                                                            dialog.hide();


                                                            });

                                                            dialog.show();
                                                            dialog.getWindow().setAttributes(lp);
                                                            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->
                                                            dialog.dismiss());
                                                            dialog.show();
                                                            dialog.getWindow().setAttributes(lp);


                                                        } else {

                                                            onLoadMainPlayerStreamYoutube(vidURL.get(i).getUrl(), history, movieResponse, serieDetail);
                                                        }

                                                    });

                                                    builder.show();


                                                } else
                                                Tools.ToastHelper(SerieDetailsActivity.this,"NULL");


                                            } else {

                                                onLoadMainPlayerStreamYoutube(vidURL.get(0).getUrl(), history, movieResponse, serieDetail);


                                                Timber.i("URL IS :%s", vidURL.get(0).getUrl());
                                            }

                                        }

                                        @Override
                                        public void onError() {

                                            Tools.ToastHelper(getApplicationContext(),"Error");
                                        }
                                    });

                                    easyPlexSupportedHosts.find(movieResponse.getEpisodes().get(0).getVideos().get(wich).getLink());


                                } else {


                                    if (mCastSession != null && mCastSession.isConnected()) {

                                        startStreamCasting(movieResponse, movieResponse.getEpisodes().get(0).getVideos().get(wich).getLink());

                                    } else if (settingsManager.getSettings().getVlc() == 1) {


                                        final Dialog dialog = new Dialog(SerieDetailsActivity.this);
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

                                        vlc.setOnClickListener(v12 -> {

                                            for (Episode episode : movieResponse.getEpisodes()) {
                                                Tools.streamEpisodeFromVlc(SerieDetailsActivity.this, movieResponse.getEpisodes().get(0).getVideos().get(wich).getLink(), episode, settingsManager);
                                                dialog.hide();
                                            }


                                        });


                                        mxPlayer.setOnClickListener(v12 -> {

                                            for (Episode episode : movieResponse.getEpisodes()) {
                                                Tools.streamEpisodeFromVlc(SerieDetailsActivity.this, movieResponse.getEpisodes().get(0).getVideos().get(wich).getLink(), episode, settingsManager);
                                                dialog.hide();
                                            }


                                        });


                                        easyplexPlayer.setOnClickListener(v12 -> {

                                            onLoadMainPlayerStream(history, movieResponse, serieDetail);
                                            dialog.hide();


                                        });

                                        dialog.show();
                                        dialog.getWindow().setAttributes(lp);

                                        dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                                dialog.dismiss());


                                        dialog.show();
                                        dialog.getWindow().setAttributes(lp);


                                    } else {

                                        onLoadMainPlayerStream(history, movieResponse, serieDetail);

                                    }

                                }


                            });

                            builder.show();

                        } else {


                            if (movieResponse.getEpisodes().get(0).getVideos().get(0).getEmbed() == 1) {


                                Intent intent = new Intent(SerieDetailsActivity.this, EmbedActivity.class);
                                intent.putExtra(Constants.MOVIE_LINK, movieResponse.getEpisodes().get(0).getVideos().get(0).getLink());
                                startActivity(intent);


                            }else if (movieResponse.getEpisodes().get(0).getVideos().get(0).getSupportedHosts() == 1) {


                                easyPlexSupportedHosts = new EasyPlexSupportedHosts(SerieDetailsActivity.this);
                                easyPlexSupportedHosts.onFinish(new EasyPlexSupportedHosts.OnTaskCompleted() {

                                    @Override
                                    public void onTaskCompleted(ArrayList<EasyPlexSupportedHostsModel> vidURL, boolean multipleQuality) {

                                        if (multipleQuality) {
                                            if (vidURL != null) {

                                                CharSequence[] name = new CharSequence[vidURL.size()];

                                                for (int i = 0; i < vidURL.size(); i++) {
                                                    name[i] = vidURL.get(i).getQuality();
                                                }

                                                final AlertDialog.Builder builder = new AlertDialog.Builder(SerieDetailsActivity.this, R.style.MyAlertDialogTheme);
                                                builder.setTitle(SerieDetailsActivity.this.getString(R.string.select_qualities));
                                                builder.setCancelable(true);
                                                builder.setItems(name, (dialogInterface, i) -> {

                                                    if (mCastSession != null && mCastSession.isConnected()) {

                                                        startStreamCasting(movieResponse, vidURL.get(i).getUrl());

                                                    } else if (settingsManager.getSettings().getVlc() == 1) {


                                                        final Dialog dialog = new Dialog(SerieDetailsActivity.this);
                                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                        dialog.setContentView(R.layout.dialog_bottom_stream);
                                                        dialog.setCancelable(false);
                                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                                                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                                        lp.copyFrom(dialog.getWindow().getAttributes());

                                                        lp.gravity = Gravity.BOTTOM;
                                                        lp.width = MATCH_PARENT;
                                                        lp.height = MATCH_PARENT;


                                                        LinearLayout vlc = dialog.findViewById(R.id.vlc);
                                                        LinearLayout mxPlayer = dialog.findViewById(R.id.mxPlayer);
                                                        LinearLayout easyplexPlayer = dialog.findViewById(R.id.easyplexPlayer);
                                                        LinearLayout webcast = dialog.findViewById(R.id.webCast);


                                                        webcast.setOnClickListener(v12 -> {


                                                            for (Episode episode : movieResponse.getEpisodes()) {
                                                                Tools.streamEpisodeFromMxWebcast(SerieDetailsActivity.this,
                                                                        vidURL.get(i).getUrl(), episode, settingsManager);
                                                                dialog.hide();
                                                            }


                                                        });


                                                        vlc.setOnClickListener(v12 -> {

                                                            Intent shareVideo = new Intent(Intent.ACTION_VIEW);
                                                            shareVideo.setDataAndType(Uri.parse(vidURL.get(i).getUrl()), "video/*");
                                                            shareVideo.setPackage("org.videolan.vlc");
                                                            shareVideo.putExtra("title", history.getTitle());
                                                            shareVideo.putExtra("poster", history.getPosterPath());
                                                            Bundle headers = new Bundle();
                                                            headers.putString("User-Agent", settingsManager.getSettings().getAppName());
                                                            shareVideo.putExtra("android.media.intent.extra.HTTP_HEADERS", headers);
                                                            shareVideo.putExtra("headers", headers);
                                                            shareVideo.putExtra("secure_uri", true);
                                                            try {
                                                                startActivity(shareVideo);
                                                                dialog.hide();
                                                            } catch (ActivityNotFoundException ex) {

                                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                                String uriString = "market://details?id=org.videolan.vlc";
                                                                intent.setData(Uri.parse(uriString));
                                                                startActivity(intent);
                                                            }
                                                        });


                                                        mxPlayer.setOnClickListener(v12 -> {

                                                            Intent shareVideo = new Intent(Intent.ACTION_VIEW);
                                                            shareVideo.setDataAndType(Uri.parse(vidURL.get(i).getUrl()), "video/*");
                                                            shareVideo.setPackage("com.mxtech.videoplayer.ad");
                                                            shareVideo.putExtra("title", history.getTitle());
                                                            shareVideo.putExtra("poster", history.getPosterPath());
                                                            Bundle headers = new Bundle();
                                                            headers.putString("User-Agent", settingsManager.getSettings().getAppName());
                                                            shareVideo.putExtra("android.media.intent.extra.HTTP_HEADERS", headers);
                                                            shareVideo.putExtra("headers", headers);
                                                            shareVideo.putExtra("secure_uri", true);
                                                            try {
                                                                startActivity(shareVideo);
                                                                dialog.hide();
                                                            } catch (ActivityNotFoundException ex) {

                                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                                String uriString = "market://details?id=com.mxtech.videoplayer.ad";
                                                                intent.setData(Uri.parse(uriString));
                                                                startActivity(intent);
                                                            }


                                                        });


                                                        easyplexPlayer.setOnClickListener(v12 -> {
                                                            onLoadMainPlayerStreamYoutube(vidURL.get(i).getUrl(), history, movieResponse, serieDetail);
                                                            dialog.hide();


                                                        });

                                                        dialog.show();
                                                        dialog.getWindow().setAttributes(lp);

                                                        dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                                                dialog.dismiss());


                                                        dialog.show();
                                                        dialog.getWindow().setAttributes(lp);


                                                    } else {

                                                        onLoadMainPlayerStreamYoutube(vidURL.get(i).getUrl(), history, movieResponse, serieDetail);
                                                    }

                                                });

                                                builder.show();


                                            } else
                                                Tools.ToastHelper(getApplicationContext(),"NULL");


                                        } else {

                                            onLoadMainPlayerStreamYoutube(vidURL.get(0).getUrl(), history, movieResponse, serieDetail);

                                        }

                                    }

                                    @Override
                                    public void onError() {

                                        Tools.ToastHelper(getApplicationContext(),"Error");

                                    }
                                });

                                easyPlexSupportedHosts.find(movieResponse.getEpisodes().get(0).getVideos().get(0).getLink());


                            } else {


                                if (mCastSession != null && mCastSession.isConnected()) {

                                    startStreamCasting(movieResponse, movieResponse.getEpisodes().get(0).getVideos().get(0).getLink());

                                } else onLoadMainPlayerStream(history, movieResponse, serieDetail);
                            }


                        }


                    }

                    private void startStreamCasting(@NotNull MovieResponse episodes, String downloadUrl) {

                        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
                        movieMetadata.putString(MediaMetadata.KEY_TITLE, episodes.getEpisodes().get(0).getName());
                        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, episodes.getEpisodes().get(0).getName());
                        movieMetadata.addImage(new WebImage(Uri.parse(downloadUrl)));


                        MediaInfo mediaInfo = new MediaInfo.Builder(downloadUrl)
                                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                                .setMetadata(movieMetadata)
                                .build();

                        CastSession castSession =
                                CastContext.getSharedInstance(SerieDetailsActivity.this).getSessionManager().getCurrentCastSession();
                        if (castSession == null || !castSession.isConnected()) {
                            Timber.tag(TAG).w("showQueuePopup(): not connected to a cast device");
                            return;
                        }
                        final RemoteMediaClient remoteMediaClient = castSession.getRemoteMediaClient();
                        if (remoteMediaClient == null) {
                            Timber.tag(TAG).w("showQueuePopup(): null RemoteMediaClient");
                            return;
                        }


                        final QueueDataProvider provider = QueueDataProvider.getInstance(SerieDetailsActivity.this);
                        PopupMenu popup = new PopupMenu(SerieDetailsActivity.this, serieDetailsBinding.topResume);
                        popup.getMenuInflater().inflate(
                                provider.isQueueDetached() || provider.getCount() == 0
                                        ? R.menu.detached_popup_add_to_queue
                                        : R.menu.popup_add_to_queue, popup.getMenu());
                        PopupMenu.OnMenuItemClickListener clickListener = menuItem -> {
                            QueueDataProvider provider1 = QueueDataProvider.getInstance(SerieDetailsActivity.this);
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
                                Intent intent = new Intent(SerieDetailsActivity.this, ExpandedControlsActivity.class);
                                startActivity(intent);
                            }
                            if (!TextUtils.isEmpty(toastMessage)) {

                                Tools.ToastHelper(getApplicationContext(),toastMessage);

                            }
                            return true;
                        };
                        popup.setOnMenuItemClickListener(clickListener);
                        popup.show();

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

    private void onLoadAppoDealInterStetial() {

        if (settingsManager.getSettings().getAppodealInterstitial() == 1) {

            Appodeal.show(SerieDetailsActivity.this, Appodeal.INTERSTITIAL);


        }

    }

    private void onLoadMainPlayerStreamYoutube(String downloadUrl, History history, @NotNull MovieResponse movieResponse, Media serieDetail) {

        String name = history.getTitle();
        String tvseasonid = history.getSeasonsId();
        Integer currentep = Integer.parseInt(movieResponse.getEpisodes().get(0).getEpisodeNumber());
        String currentepname = movieResponse.getEpisodes().get(0).getName();
        String currenteptmdbnumber = String.valueOf(movieResponse.getEpisodes().get(0).getTmdbId());
        String currentseasons = history.getCurrentSeasons();
        String currentseasonsNumber = history.getSeasonsNumber();
        String currentepimdb = String.valueOf(movieResponse.getEpisodes().get(0).getTmdbId());
        String artwork = movieResponse.getEpisodes().get(0).getStillPath();
        String type = "1";
        float voteAverage = Float.parseFloat(movieResponse.getEpisodes().get(0).getVoteAverage());
        String currentquality = movieResponse.getEpisodes().get(0).getVideos().get(0).getServer();
        int hls = movieResponse.getEpisodes().get(0).getVideos().get(0).getHls();
        int drm =  movieResponse.getEpisodes().get(0).getVideos().get(0).getDrm();
        String Drmuuid =  movieResponse.getEpisodes().get(0).getVideos().get(0).getDrmuuid();
        String Drmlicenceuri =  movieResponse.getEpisodes().get(0).getVideos().get(0).getDrmlicenceuri();


        Intent intent = new Intent(SerieDetailsActivity.this, EasyPlexMainPlayer.class);

        intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY,
                MediaModel.media(history.getSerieId(), null,
                        currentquality, type, name, downloadUrl, artwork, null, currentep
                        , currentseasons,
                        currentepimdb,
                        tvseasonid, currentepname,
                        currentseasonsNumber, history.getPosition(),
                        currenteptmdbnumber,
                        history.getPremuim(),hls,null
                        ,history.getImdbExternalId(),
                        serieDetail.getPosterPath()
                        ,movieResponse.getEpisodes().get(0).getHasrecap(),movieResponse.getEpisodes().get(0).getSkiprecapStartIn(),mediaGenre,
                        serieDetail.getName(),voteAverage,Drmuuid,
                        Drmlicenceuri,drm));
        intent.putExtra(ARG_MOVIE, serieDetail);
        startActivity(intent);

    }

    private void onLoadMainPlayerStream(History history, @NotNull MovieResponse movieResponse, Media serieDetail) {

        float voteAverage = Float.parseFloat(movieResponse.getEpisodes().get(0).getVoteAverage());

        String name = history.getTitle();
        String tvseasonid = history.getSeasonsId();
        Integer currentep = Integer.parseInt(movieResponse.getEpisodes().get(0).getEpisodeNumber());
        String currentepname = movieResponse.getEpisodes().get(0).getName();
        String currenteptmdbnumber = String.valueOf(movieResponse.getEpisodes().get(0).getId());
        String currentseasons = history.getCurrentSeasons();
        String currentseasonsNumber = history.getSeasonsNumber();
        String currentepimdb = String.valueOf(movieResponse.getEpisodes().get(0).getId());
        String artwork = movieResponse.getEpisodes().get(0).getStillPath();
        String type = "1";
        String currentquality = movieResponse.getEpisodes().get(0).getVideos().get(0).getServer();
        String videourl = movieResponse.getEpisodes().get(0).getVideos().get(0).getLink();
        int hls = movieResponse.getEpisodes().get(0).getVideos().get(0).getHls();
        int drm =  movieResponse.getEpisodes().get(0).getVideos().get(0).getDrm();
        String Drmuuid =  movieResponse.getEpisodes().get(0).getVideos().get(0).getDrmuuid();
        String Drmlicenceuri =  movieResponse.getEpisodes().get(0).getVideos().get(0).getDrmlicenceuri();

        Intent intent = new Intent(SerieDetailsActivity.this, EasyPlexMainPlayer.class);

        intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY,
                MediaModel.media(history.getSerieId(), null,
                        currentquality, type, name, videourl, artwork, null, currentep
                        , currentseasons,
                        currentepimdb,
                        tvseasonid, currentepname,
                        currentseasonsNumber, history.getPosition(),
                        currenteptmdbnumber,
                        history.getPremuim(),hls,null
                        ,history.getImdbExternalId(),
                        serieDetail.getPosterPath()
                        ,movieResponse.getEpisodes().get(0).getHasrecap(),movieResponse.getEpisodes().get(0).getSkiprecapStartIn(),mediaGenre,serieDetail.getName(),voteAverage,
                        Drmuuid,Drmlicenceuri,drm));
        intent.putExtra(ARG_MOVIE, serieDetail);
        startActivity(intent);
    }


    @SuppressLint("SetTextI18n")
    private void onLoadViews(String views) {

        serieDetailsBinding.viewMovieViews.setText(getString(R.string.views)+Tools.getViewFormat(Integer.parseInt(views)));

    }



    @SuppressLint("SetTextI18n")
    private void onLoadSeasons(Media serieDetail) {


        if (serieDetail.getSeasons() !=null && !serieDetail.getSeasons().isEmpty()) {

            serieDetailsBinding.mseason.setText(SEASONS + serieDetail.getSeasons().size());

            serieDetailsBinding.planetsSpinner.setItem(serieDetail.getSeasons());
            serieDetailsBinding.planetsSpinner.setSelection(0);
            serieDetailsBinding.planetsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                    mEpisodesLoaded = true;
                    checkAllDataLoaded();

                    if(!settingReady)finishAffinity();

                    Season season = (Season) adapterView.getItemAtPosition(position);
                    String episodeId = String.valueOf(season.getId());
                    String currentSeason = season.getName();
                    String seasonNumber = season.getSeasonNumber();

                    // Episodes RecycleView
                    serieDetailsBinding.recyclerViewEpisodes.setLayoutManager(new LinearLayoutManager(SerieDetailsActivity.this));
                    serieDetailsBinding.recyclerViewEpisodes.setHasFixedSize(true);

                    mEpisodesSerieAdapter = new EpisodeAdapter(serieDetail.getId(),seasonNumber
                            ,episodeId,currentSeason,sharedPreferences,authManager,settingsManager,mediaRepository
                            ,serieDetail.getName(),serieDetail.getPremuim(),tokenManager,
                            SerieDetailsActivity.this,serieDetail.getPosterPath(),
                            serie,mediaGenre,externalId,appController,deviceManager);

                    mEpisodesSerieAdapter.addSeasons(season.getEpisodes());
                    serieDetailsBinding.recyclerViewEpisodes.setAdapter(mEpisodesSerieAdapter);



                    serieDetailsBinding.episodesFiltre.setOnClickListener(v -> {


                        if (mEpisodesSerieAdapter != null) {
                            // Reverse the order of episodes in the adapter
                            mEpisodesSerieAdapter.reverseEpisodeOrder();
                            // Notify the adapter that the data set has changed
                            mEpisodesSerieAdapter.notifyDataSetChanged();

                            boolean isReversed = mEpisodesSerieAdapter.isReversed();
                            if (isReversed) {

                                Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_gradient_white_login);
                                serieDetailsBinding.episodesFiltre.setBackground(drawable);
                                DrawableCompat.setTint(serieDetailsBinding.episodesFiltreIcon.getDrawable(),ContextCompat.getColor(getApplicationContext(),R.color.black));


                            } else {
                                Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.selected_white_login);
                                serieDetailsBinding.episodesFiltre.setBackground(drawable);
                                DrawableCompat.setTint(serieDetailsBinding.episodesFiltreIcon.getDrawable(),ContextCompat.getColor(getApplicationContext(),R.color.white));

                            }
                        }


                    });



                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                    // do nothing if no season selected

                }
            });

        }




    }




    // Load Serie Rating
    private void onLoadRating(float voteAverage) {

        serieDetailsBinding.viewMovieRating.setText(String.valueOf(voteAverage));
        serieDetailsBinding.ratingBar.setRating(voteAverage / 2);
    }


    private void onLoadToolbar() {

        Tools.loadToolbar(this, serieDetailsBinding.toolbar, serieDetailsBinding.appbar);


    }


    // Add or Remove Serie from Favorite
    public void onFavoriteClick(Series mediaDetail) {

        if (mediaRepository.isSerieFavorite(Integer.parseInt(mediaDetail.getId()))) {

            Timber.i("Removed From Watchlist");
            serieDetailViewModel.removeTvFromFavorite(mediaDetail);

            serieDetailsBinding.favoriteImage.setImageResource(R.drawable.add_from_queue);

            Tools.ToastHelper(getApplicationContext(),"Removed: " + mediaDetail.getName());

            serieDetailViewModel.removeTvFromFavorite(mediaDetail);

        }else {

            Timber.i("Added To Watchlist");
            serieDetailViewModel.addtvFavorite(mediaDetail);

            serieDetailsBinding.favoriteImage.setImageResource(R.drawable.ic_in_favorite);

            Tools.ToastHelper(getApplicationContext(),"Added: " + mediaDetail.getName());

        }

    }


    // Handle Back Button
    private void onLoadBackButton() {

        serieDetailsBinding.backbutton.setOnClickListener(v -> {
            onBackPressed();
            Animatoo.animateSplit(SerieDetailsActivity.this);

        });
    }


    // Load Serie Trailer
    private void onLoadTrailer(String previewPath, String title, String backdrop, String trailerUrl) {


        if (sharedPreferences.getBoolean(Constants.WIFI_CHECK, false) &&
                NetworkUtils.isWifiConnected(this)) {

            DialogHelper.showWifiWarning(SerieDetailsActivity.this);

        }else {

            Tools.startTrailer(this,previewPath,title,backdrop,settingsManager,trailerUrl);

        }


    }


    // Display Serie Poster
    private void onLoadImage(String imageURL){


        GlideApp.with(getApplicationContext()).asBitmap().load(imageURL)
                .fitCenter()
                .placeholder(R.color.fragment_content_detail_overlay_end)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(withCrossFade())
                .into(serieDetailsBinding.imageMoviePoster);


        if (settingsManager.getSettings().getEnablelayoutchange() == 1) {

            Glide.with(this).load(imageURL)
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(RADUIS, SAMPLING)))
                    .into(serieDetailsBinding.imageMoviePoster);


            GlideApp.with(getApplicationContext()).asBitmap().load(imageURL)
                    .fitCenter()
                    .placeholder(R.color.fragment_content_detail_overlay_end)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(withCrossFade())
                    .into(serieDetailsBinding.imagePosterSecondry);

        }


    }


    // Display Serie Title
    private void onLoadTitle(String title){

        serieDetailsBinding.serieTitle.setText(title);
    }


    // Display Serie Release Date
    @SuppressLint("SetTextI18n")
    private void onLoadDate(String date) throws ParseException {
        if (date != null && !date.trim().isEmpty()) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
            if (sharedPreferences.getString(cuePointY, cuePointN).equals(cuePointN)) finishAffinity();
            Date releaseDate = sdf1.parse(date);
            serieDetailsBinding.mrelease.setText(sdf2.format(releaseDate));
        } else {
            serieDetailsBinding.mrelease.setText("");}
    }


    // Display Serie Synopsis or Overview
    private void onLoadSynopsis(String synopsis){
        serieDetailsBinding.serieOverview.setText(synopsis);
    }




    // Report Serie if something wrong
    private void onLoadReport(String title,String posterpath) {


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_report);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

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

                serieDetailViewModel.sendReport(title,editTextMessage.getText().toString());
                serieDetailViewModel.reportMutableLiveData.observe(SerieDetailsActivity.this, report -> {


                    if (report !=null) {

                        dialog.dismiss();

                        Tools.ToastHelper(getApplicationContext(),getString(R.string.report_sent));


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

    // Serie Genres
    private void onLoadGenres(List<Genre> genresList) {


        for (Genre genre : genresList) {
            serieDetailsBinding.mgenres.setText(genre.getName());
        }

        for (Genre genre : genresList) {
       mediaGenre = genre.getName(); }

    }

    private void onLoadSerieComments(String id) {

        commentsAdapter = new CommentsAdapter();

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_comments);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.width = MATCH_PARENT;
        lp.height = MATCH_PARENT;

        lp.gravity = Gravity.BOTTOM;


        RecyclerView rv_comments = dialog.findViewById(R.id.rv_comments);

        rv_comments.setHasFixedSize(true);
        rv_comments.setNestedScrollingEnabled(false);
        rv_comments.setLayoutManager(new LinearLayoutManager(SerieDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
        rv_comments.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(this, 0), true));


        commentsAdapter.setOnItemClickListener(clicked -> {
            if (clicked){

                serieDetailViewModel.getSerieComments(Integer.parseInt(id));
                serieDetailViewModel.serieCommentsMutableLiveData.observe(
                        this, comments ->
                                commentsAdapter.addToContent(comments.getComments(),
                                        this,authManager,mediaRepository));
            }
        });

        TextView commentTotal = dialog.findViewById(R.id.comment_total);

        FloatingActionButton add_comment_btn = dialog.findViewById(R.id.add_comment_btn);

        EditText editTextComment = dialog.findViewById(R.id.comment_message);

        LinearLayout noCommentFound = dialog.findViewById(R.id.no_comment_found);

        serieDetailViewModel.getSerieComments(Integer.parseInt(id));
        serieDetailViewModel.serieCommentsMutableLiveData.observe(this, comments -> {

            commentsAdapter.addToContent(comments.getComments(),this,authManager,mediaRepository);
            rv_comments.setAdapter(commentsAdapter);

            if (commentsAdapter.getItemCount() == 0) {
                noCommentFound.setVisibility(View.VISIBLE);

            }else {
                noCommentFound.setVisibility(GONE);
            }

            commentTotal.setText(comments.getComments().size()+ " " +getString(R.string.comments_size));


            add_comment_btn.setOnClickListener(v -> {

                    if(!TextUtils.isEmpty(editTextComment.getText().toString())){

                     mediaRepository.addCommentSerie(editTextComment.getText().toString(),id)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<>() {
                                @Override
                                public void onSubscribe(@NotNull Disposable d) {

                                    //

                                }

                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onNext(@NotNull Comment comment) {

                                    Tools.ToastHelper(getApplicationContext(),getString(R.string.comment_sent));

                                    hideKeyboard();

                                    editTextComment.setText("");
                                    serieDetailViewModel.getSerieComments(Integer.parseInt(id));
                                    serieDetailViewModel.serieCommentsMutableLiveData.observe(SerieDetailsActivity.this, commentsx -> {

                                        commentsAdapter.addToContent(commentsx.getComments(),SerieDetailsActivity.this,authManager,mediaRepository);
                                        rv_comments.scrollToPosition(rv_comments.getAdapter().getItemCount()-1);
                                        rv_comments.setAdapter(commentsAdapter);
                                        commentsAdapter.notifyDataSetChanged();

                                    });


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

                }else {

                    Tools.ToastHelper(getApplicationContext(),getString(R.string.type_comment));

                }

            });

        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);

        dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                dialog.dismiss());

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    private void onLoadRelatedsMovies(int id) {

        serieDetailViewModel.getRelatedsSeries(id);
        serieDetailViewModel.movieRelatedsMutableLiveData.observe(this, relateds -> {

            relatedsAdapter.addToContent(relateds.getRelateds());
            if(!settingReady)finishAffinity();
            //Relateds Movies RecycleView
            serieDetailsBinding.rvMylike.setAdapter(relatedsAdapter);
            serieDetailsBinding.rvMylike.setHasFixedSize(true);
            serieDetailsBinding.rvMylike.setNestedScrollingEnabled(false);
            serieDetailsBinding.rvMylike.setLayoutManager(new LinearLayoutManager(SerieDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            serieDetailsBinding.rvMylike.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(this, 0), true));
            if (sharedPreferences.getString(FsmPlayerApi.decodeServerMainApi2(), FsmPlayerApi.decodeServerMainApi4()).equals(FsmPlayerApi.decodeServerMainApi4())) { finishAffinity();
            }
            if (relatedsAdapter.getItemCount() == 0) {

                serieDetailsBinding.relatedNotFound.setVisibility(View.VISIBLE);

            }else {

                serieDetailsBinding.relatedNotFound.setVisibility(GONE);

            }



        });
    }

    @Override
    protected void onResume() {


        Tools.onCheckFlagSecure(settingsManager.getSettings().getFlagSecure(),this);

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

        if (settingsManager.getSettings().getVpn() ==1 && checkVpn){

            serieDetailsBinding.backbutton.performClick();

            Tools.ToastHelper(getApplicationContext(),getString(R.string.vpn_message));


        }

        if (mSerieLoaded && mEpisodesLoaded){

            mEpisodesSerieAdapter.notifyDataSetChanged();
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
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        return mCastContext.onDispatchVolumeKeyEventBeforeJellyBean(event)
                || super.dispatchKeyEvent(event);
    }


    private void showIntroductoryOverlay() {
        if (mIntroductoryOverlay != null) {
            mIntroductoryOverlay.remove();
        }
        if ((mediaRouteMenuItem != null) && mediaRouteMenuItem.isVisible()) {

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                mIntroductoryOverlay = new IntroductoryOverlay.Builder(
                        SerieDetailsActivity.this, mediaRouteMenuItem)
                        .setTitleText(getString(R.string.introducing_cast))
                        .setOverlayColor(R.color.main_color)
                        .setSingleTime()
                        .setOnOverlayDismissedListener(
                                () -> mIntroductoryOverlay = null)
                        .build();
                mIntroductoryOverlay.show();
            },1000);


        }
    }


    private void checkAllDataLoaded() {
        if (mSerieLoaded && mEpisodesLoaded) {

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                serieDetailsBinding.progressBar.setVisibility(View.GONE);
                serieDetailsBinding.scrollView.setVisibility(View.VISIBLE);
            },300);

        }
    }




    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            Tools.hideSystemPlayerUi(this,true,0);
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

        if (nativeAdMedia != null) {
            nativeAdMedia.destroy();
            nativeAdMedia = null;
        }

        if (nativeAd != null) {
            nativeAd.unregisterView();
            nativeAd.destroy();
            nativeAd = null;
        }


        serieDetailsBinding.appodealBannerView.removeAllViews();
        serieDetailsBinding.appodealBannerView.removeAllViewsInLayout();

        if (bottomBanner!=null) {

            bottomBanner.destroy();
            bottomBanner = null;
        }


        if (mRewardedAd !=null) {

            mRewardedAd = null;
        }


        if (facebookBanner !=null) {

            facebookBanner.destroy();
            facebookBanner = null;

        }


        if (vungleBanner !=null){

            vungleBanner.destroyAd();
            vungleBanner = null;
        }

        Appodeal.destroy(Appodeal.BANNER);
        Appodeal.destroy(Appodeal.INTERSTITIAL);
        Appodeal.destroy(Appodeal.REWARDED_VIDEO);
        Glide.get(this).clearMemory();
        serieDetailsBinding = null;
        super.onDestroy();


    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
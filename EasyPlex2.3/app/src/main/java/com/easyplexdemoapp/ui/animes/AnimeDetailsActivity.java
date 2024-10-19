package com.easyplexdemoapp.ui.animes;

import static android.view.View.GONE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;
import static com.easyplexdemoapp.util.Constants.ARG_MOVIE;
import static com.easyplexdemoapp.util.Constants.RADUIS;
import static com.easyplexdemoapp.util.Constants.SAMPLING;
import static com.easyplexdemoapp.util.Constants.SEASONS;
import static com.easyplexdemoapp.util.Constants.SPECIALS;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
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
import com.easyplexdemoapp.data.local.entity.Animes;
import com.easyplexdemoapp.data.local.entity.History;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.MovieResponse;
import com.easyplexdemoapp.data.model.auth.Rating;
import com.easyplexdemoapp.data.model.certifications.Certification;
import com.easyplexdemoapp.data.model.comments.Comment;
import com.easyplexdemoapp.data.model.genres.Genre;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.data.model.media.Resume;
import com.easyplexdemoapp.data.model.media.StatusFav;
import com.easyplexdemoapp.data.model.serie.Season;
import com.easyplexdemoapp.data.repository.AuthRepository;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.databinding.ItemAnimeDetailBinding;
import com.easyplexdemoapp.ui.comments.CommentsAdapter;
import com.easyplexdemoapp.ui.home.adapters.RelatedsAdapter;
import com.easyplexdemoapp.ui.login.LoginActivity;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.DeviceManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.ui.moviedetails.adapters.CastAdapter;
import com.easyplexdemoapp.ui.player.activities.EasyPlexMainPlayer;
import com.easyplexdemoapp.ui.player.activities.EasyPlexPlayerActivity;
import com.easyplexdemoapp.ui.player.activities.EmbedActivity;
import com.easyplexdemoapp.ui.settings.SettingsActivity;
import com.easyplexdemoapp.ui.users.MenuHandler;
import com.easyplexdemoapp.ui.viewmodels.AnimeViewModel;
import com.easyplexdemoapp.ui.viewmodels.LoginViewModel;
import com.easyplexdemoapp.ui.viewmodels.MovieDetailViewModel;
import com.easyplexdemoapp.util.AppController;
import com.easyplexdemoapp.util.Constants;
import com.easyplexdemoapp.util.DialogHelper;
import com.easyplexdemoapp.util.GlideApp;
import com.easyplexdemoapp.util.ItemAnimation;
import com.easyplexdemoapp.util.NetworkUtils;
import com.easyplexdemoapp.util.RootCheckUtil;
import com.easyplexdemoapp.util.SpacingItemDecoration;
import com.easyplexdemoapp.util.Tools;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerView;
import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.error.VungleException;
import com.xw.repo.BubbleSeekBar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
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


/**
 * EasyPlex - Movies - Live Streaming - TV Series, Anime
 *
 * @author @Y0bEX
 * @package EasyPlex - Movies - Live Streaming - TV Series, Anime
 * @copyright Copyright (c) 2021 Y0bEX,
 * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/
 * @profile <a href="https://codecanyon.net/user/yobex">...</a>
 * @link yobexd@gmail.com
 * @skype yobexd@gmail.com
 **/


public class AnimeDetailsActivity extends AppCompatActivity {

    private MaxInterstitialAd maxInterstitialAd;
    private IronSourceBannerLayout banner;
    private EasyPlexSupportedHosts easyPlexSupportedHosts;
    private com.google.android.gms.ads.nativead.NativeAd mNativeAd;
    private MediaView nativeAdMedia;
    private NativeAd nativeAd;
    private MaxAdView maxAdView;
    private MaxNativeAdLoader nativeAdLoader;
    private MaxAd maxAd;
    private boolean mAnime;
    private boolean mEpisodesLoaded;
    ItemAnimeDetailBinding binding;


    @Inject
    CastAdapter mCastAdapter;

    @Inject ViewModelProvider.Factory viewModelFactory;


    private boolean isMovieFav = false;

    @Inject
    AuthRepository authRepository;

    @Inject
    SharedPreferences.Editor sharedPreferencesEditor;


    @Inject
    DeviceManager deviceManager;

    private AnimeViewModel animeViewModel;

    private EpisodeAnimeAdapter episodeAnimeAdapter;


    @Inject
    RelatedsAdapter relatedsAdapter;


    @Inject
    AppController appController;


    private static final int ANIMATION_TYPE = ItemAnimation.FADE_IN;

    @Inject
    @Named("ready")
    boolean settingReady;


    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    TokenManager tokenManager;

    @Inject
    SettingsManager settingsManager;
    private RewardedAd mRewardedAd;
    private BannerView bottomBanner;
    boolean isLoading;

    @Inject
    AuthManager authManager;


    @Inject
    MediaRepository mediaRepository;

    private LoginViewModel loginViewModel;



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


    @Inject
    MenuHandler menuHandler;

    private MovieDetailViewModel movieDetailViewModel;
    private com.facebook.ads.AdView facebookBanner;
    private  Media anime;
    private Animes animes;
    private CastContext mCastContext;
    private final SessionManagerListener<CastSession> mSessionManagerListener =
            new MySessionManagerListener();
    private CastSession mCastSession;
    private MenuItem mediaRouteMenuItem;
    private MenuItem mQueueMenuItem;
    private IntroductoryOverlay mIntroductoryOverlay;
    private CastStateListener mCastStateListener;
    private String mediaGenre;
    private CommentsAdapter commentsAdapter;


    private class MySessionManagerListener implements SessionManagerListener<CastSession> {

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

            Toast.makeText(AnimeDetailsActivity.this, getString(R.string.unable_cast), Toast.LENGTH_SHORT).show();
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

        binding = DataBindingUtil.setContentView(this,R.layout.item_anime_detail);



        binding.setMenu(menuHandler);

        menuHandler.isLayoutChangeEnabled.set(settingsManager.getSettings().getEnablelayoutchange() == 1);


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

        anime = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? intent.getParcelableExtra(ARG_MOVIE, Media.class) : intent.getParcelableExtra(ARG_MOVIE);


        mAnime = false;
        mEpisodesLoaded = false;
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.scrollView.setVisibility(GONE);

        Tools.setSystemBarTransparent(this);

        animeViewModel = new ViewModelProvider(this, viewModelFactory).get(AnimeViewModel.class);

        movieDetailViewModel = new ViewModelProvider(this, viewModelFactory).get(MovieDetailViewModel.class);

        loginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);

        initMovieDetails();

        binding.recyclerViewEpisodes.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewEpisodes.setItemViewCacheSize(4);



        binding.recyclerViewCastMovieDetail.setAdapter(mCastAdapter);
        binding.recyclerViewCastMovieDetail.setHasFixedSize(true);
        binding.recyclerViewCastMovieDetail.setNestedScrollingEnabled(false);
        binding.recyclerViewCastMovieDetail.setLayoutManager(new LinearLayoutManager(AnimeDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerViewCastMovieDetail.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(this, 0), true));


        if (settingsManager.getSettings().getEnableComments() !=1){

            binding.floatingCommentIcon.setVisibility(GONE);
            binding.commentsize.setVisibility(GONE);
        }

    }

    private void onInitRewards() {


        Appodeal.initialize(this, settingsManager.getSettings().getAdUnitIdAppodealRewarded(), Appodeal.INTERSTITIAL | Appodeal.BANNER | Appodeal.REWARDED_VIDEO

                , list -> {

                });

        IronSource.init(this, settingsManager.getSettings().getIronsourceAppKey(),IronSource.AD_UNIT.REWARDED_VIDEO,IronSource.AD_UNIT.INTERSTITIAL,IronSource.AD_UNIT.BANNER);

        if (settingsManager.getSettings().getApplovinInterstitial() == 1 && settingsManager.getSettings().getApplovinInterstitialUnitid() !=null && !settingsManager.getSettings().getApplovinInterstitialUnitid().isEmpty()) {

            maxInterstitialAd = new MaxInterstitialAd(settingsManager.getSettings().getApplovinInterstitialUnitid(), this );
            maxInterstitialAd.loadAd();
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


    private void onLoadApplovinNativeAds() {


        if (settingsManager.getSettings().getApplovin_native() == 1 && settingsManager.getSettings().getApplovinNativeUnitid() !=null){

        nativeAdLoader = new MaxNativeAdLoader(settingsManager.getSettings().getApplovinNativeUnitid(), this );
        nativeAdLoader.setNativeAdListener(new MaxNativeAdListener()
        {
            @Override
            public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad)
            {
                // Clean up any pre-existing native ad to prevent memory leaks.
                if ( maxAd != null )
                {
                    nativeAdLoader.destroy(maxAd);
                }

                // Save ad for cleanup.
                maxAd = ad;

                // Add ad view to view.
                binding.maxNativeAds.removeAllViews();
                binding.maxNativeAds.addView(nativeAdView);
            }

            @Override
            public void onNativeAdLoadFailed(final String adUnitId, final MaxError error)
            {
                // We recommend retrying with exponentially higher delays up to a maximum delay
            }

            @Override
            public void onNativeAdClicked(final MaxAd ad)
            {
                // Optional click callback
            }
        } );

        nativeAdLoader.loadAd();

        }
    }


    private void initMovieDetails() {


            // Observe data from AnimeViewmodel

            animeViewModel.getAnimeDetails(anime.getId());
            animeViewModel.animeDetailMutableLiveData.observe(this, animeDetail -> {


                animes  = new Animes(animeDetail.getId(),animeDetail.getId(),animeDetail.getPosterPath(),animeDetail.getName());

                onLoadImage(animeDetail.getPosterPath());
                onLoadTitle(animeDetail.getName());
                onLoadSeasons(animeDetail);
                onLoadDate(animeDetail.getFirstAirDate());
                onLoadSynopsis(animeDetail.getOverview());
                onLoadRating(animeDetail.getVoteAverage());
                onLoadGenres(animeDetail.getGenres());
                onLoadCertification(animeDetail.getCertifications());
                onLoadBackButton();
                onLoadViews(animeDetail.getViews());
                onLoadRelatedsMovies(Integer.parseInt(animeDetail.getId()));
                onLoadAnimeCast(animeDetail);
                onLoadUsersReviews(animeDetail.getVoteAverage());
                onLoadPremuim(animeDetail.getPremuim());

                binding.review.setOnClickListener(v -> onSentReview(animeDetail));

                binding.floatingCommentIcon.setOnClickListener(v -> {

                    if (tokenManager.getToken().getAccessToken() != null) {

                        onLoadAnimeComments(animeDetail.getId());
                    }else {

                        final Snackbar snackbar = Snackbar.make(binding.constraintLayout, "", Snackbar.LENGTH_LONG);
                        //inflate view
                        @SuppressLint("InflateParams") View custom_view = getLayoutInflater().inflate(R.layout.snackbar_login, null);

                        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
                        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
                        snackBarView.setPadding(0, 0, 0, 0);
                        (custom_view.findViewById(R.id.tv_undo)).setOnClickListener(v1 -> {
                            snackbar.dismiss();
                            startActivity(new Intent(AnimeDetailsActivity.this, LoginActivity.class));
                            finish();
                        });

                        snackBarView.addView(custom_view, 0);
                        snackbar.show();
                    }

                });



                if (settingsManager.getSettings().getEnableComments() ==1){

                    mediaRepository.getAnimesComments(Integer.parseInt(animeDetail.getId()),settingsManager.getSettings().getApiKey())
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

                                        binding.commentsize.setText(getString(R.string.comment_size_views)+Tools.getViewFormat(movieResponse.getComments().size()));

                                    }else {

                                        binding.commentsizeLinear.setVisibility(GONE);
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

                    binding.commentsizeLinear.setVisibility(GONE);
                }



                if(!settingReady)finishAffinity();


                if (authManager.getUserInfo().getPremuim() != 1 ) {

                    Tools.onLoadNetworksInter(settingsManager, AnimeDetailsActivity.this,maxInterstitialAd);
                    Tools.onloadBanners(settingsManager,AnimeDetailsActivity.this, null,null,binding, null, null);
                    onLoadApplovinNativeAds();
                    onLoadAdmobNativeAds();


                } else {

                    binding.bannerContainer.setVisibility(GONE);
                    binding.adViewContainer.setVisibility(GONE);
                    binding.appodealBannerView.setVisibility(GONE);
                    binding.flAdplaceholder.setVisibility(GONE);

                }


                // Share button function
                binding.shareIcon.setOnClickListener(v -> Tools.onShareMedia(this,animeDetail,settingsManager,"anime"));


                checkMediaFavorite(animeDetail);


                // Don't translate anime word
                mediaRepository.hasHistory2(Integer.parseInt(animeDetail.getId()),"anime").observe(this, history -> {

                    if (history !=null ) {

                        binding.resumePlay.setVisibility(View.VISIBLE);
                        binding.resumePlayTitle.setText(history.getTitle());

                        if (settingsManager.getSettings().getResumeOffline() == 1) {

                            mediaRepository.hasResume(Integer.parseInt(history.getEpisodeTmdb())).observe(this, resumeInfo -> {

                                if (resumeInfo !=null){


                                    if (resumeInfo.getTmdb() != null && resumeInfo.getResumePosition()
                                            !=null && resumeInfo.getTmdb().equals(history.getEpisodeTmdb())
                                            && Tools.id(AnimeDetailsActivity.this).equals(resumeInfo.getDeviceId())) {


                                        double d = resumeInfo.getResumePosition();

                                        double moveProgress = d * 100 / resumeInfo.getMovieDuration();


                                        binding.linearResumeProgressBar.setVisibility(View.VISIBLE);
                                        binding.resumeProgressBar.setVisibility(View.VISIBLE);
                                        binding.resumeProgressBar.setProgress((int) moveProgress);
                                        binding.timeRemaning.setText(Tools.getProgressTime((resumeInfo.getMovieDuration() - resumeInfo.getResumePosition()), true));




                                    } else {

                                        binding.resumeProgressBar.setProgress(0);
                                        binding.resumeProgressBar.setVisibility(GONE);
                                        binding.timeRemaning.setVisibility(GONE);
                                        binding.linearResumeProgressBar.setVisibility(GONE);

                                    }


                                }else {


                                    binding.resumeProgressBar.setProgress(0);
                                    binding.resumeProgressBar.setVisibility(GONE);
                                    binding.timeRemaning.setVisibility(GONE);
                                    binding.linearResumeProgressBar.setVisibility(GONE);

                                }





                            });

                        }else {



                            mediaRepository.getResumeById(String.valueOf(history.getEpisodeTmdb()),settingsManager.getSettings().getApiKey())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<>() {
                                        @Override
                                        public void onSubscribe(@NotNull Disposable d) {

                                            //

                                        }

                                        @SuppressLint("TimberArgCount")
                                        @Override
                                        public void onNext(@NotNull Resume resume) {


                                            if (resume.getTmdb() != null && resume.getResumePosition()
                                                    != null && resume.getTmdb().equals(history.getEpisodeTmdb())
                                                    && Tools.id(AnimeDetailsActivity.this).equals(resume.getDeviceId())) {


                                                double d = resume.getResumePosition();

                                                double moveProgress = d * 100 / resume.getMovieDuration();


                                                binding.linearResumeProgressBar.setVisibility(View.VISIBLE);
                                                binding.resumeProgressBar.setVisibility(View.VISIBLE);
                                                binding.resumeProgressBar.setProgress((int) moveProgress);
                                                binding.timeRemaning.setText(Tools.getProgressTime((resume.getMovieDuration() - resume.getResumePosition()), true));


                                            } else {

                                                binding.resumeProgressBar.setProgress(0);
                                                binding.resumeProgressBar.setVisibility(GONE);
                                                binding.timeRemaning.setVisibility(GONE);
                                                binding.linearResumeProgressBar.setVisibility(GONE);

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



                        binding.topResume.setOnClickListener(v -> {

                            if (animeDetail.getPremuim() == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() !=null) {

                                onLoadResumeFromHistory(history,animeDetail);


                            }else if (settingsManager.getSettings().getWachAdsToUnlock() == 1 && animeDetail.getPremuim() != 1 && authManager.getUserInfo().getPremuim() == 0) {

                                onLoadSubscribeDialog(history,animeDetail);

                            }else if (settingsManager.getSettings().getWachAdsToUnlock() == 0 && animeDetail.getPremuim() == 0 ){

                                onLoadResumeFromHistory(history,animeDetail);


                            } else if (authManager.getUserInfo().getPremuim() == 1 && animeDetail.getPremuim() == 0){


                                onLoadResumeFromHistory(history,animeDetail);


                            }else {

                                DialogHelper.showPremuimWarning(this);

                            }
                        });

                    }else {


                        binding.resumePlay.setVisibility(GONE);

                    }


                });



                binding.scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
                    int scrollY =  binding.scrollView.getScrollY();
                    int color = Color.parseColor("#E6070707"); // ideally a global variable
                    if (scrollY < 256) {
                        int alpha = (scrollY << 24) | (-1 >>> 8) ;
                        color &= (alpha);

                        binding.serieName.setText("");
                        binding.serieName.setVisibility(View.GONE);


                    }else {

                        binding.serieName.setText(animeDetail.getName());
                        binding.serieName.setVisibility(View.VISIBLE);

                    }
                    binding.toolbar.setBackgroundColor(color);

                });

                onLoadToolbar();


                // Report Anime if something didn't work
                binding.report.setOnClickListener(v -> onLoadReport(animeDetail.getName(),animeDetail.getPosterPath()));


                // Button to handle Trailer
                binding.ButtonPlayTrailer.setOnClickListener(v -> onLoadTrailer(animeDetail.getPreviewPath(), animeDetail.getTitle(), animeDetail.getBackdropPath(),animeDetail.getTrailerUrl()));

                // Favorite button function
                binding.favoriteIcon.setOnClickListener(view -> {


                    if (settingsManager.getSettings().getFavoriteonline() == 1 && tokenManager.getToken().getAccessToken() !=null) {

                        if (isMovieFav) {

                            authRepository.getDeleteAnimeOnline(animeDetail.getId())
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

                                            Toast.makeText(AnimeDetailsActivity.this, "Removed From Watchlist", Toast.LENGTH_SHORT).show();


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
                            binding.favoriteImage.setImageResource(R.drawable.add_from_queue);

                        }else {

                            authRepository.getAddAnimeOnline(animeDetail.getId())
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

                                            Toast.makeText(AnimeDetailsActivity.this, "Removed From Watchlist", Toast.LENGTH_SHORT).show();

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
                            binding.favoriteImage.setImageResource(R.drawable.ic_in_favorite);
                        }

                    }else  {

                        onFavoriteClick(animes);
                    }

                });







            });


            mAnime = true;
            checkAllDataLoaded();

    }

    private void onLoadCertification(List<Certification> certifications) {
        if (certifications == null || certifications.isEmpty()) {
            binding.maturityRating.setVisibility(GONE);
            binding.MovieCertification.setVisibility(GONE);
            binding.viewMovieCertification.setVisibility(GONE);
            return;
        }

        StringBuilder certificationList = new StringBuilder();
        Certification firstCertification = certifications.get(0);

        certificationList.append(firstCertification.getCountryCode())
                .append(firstCertification.getCertification());

        binding.viewMovieCertification.setText(certificationList);
        binding.viewMovieCertification.setVisibility(View.VISIBLE);
        binding.maturityRating.setVisibility(View.VISIBLE);

        binding.viewMovieCertification.setOnClickListener(v ->
                Toast.makeText(getApplicationContext(), firstCertification.getMeaning(), Toast.LENGTH_SHORT).show()
        );
    }

    private void onLoadPremuim(int premuim) {

        binding.moviePremuim.setVisibility(premuim == 1 ? View.VISIBLE : View.GONE);
    }

    private void onLoadUsersReviews(float voteAverage) {

        binding.userReview.setText(String.valueOf(voteAverage));
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

                    authRepository.addRating(serieDetail.getId(),newUserRating, "anime")
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

                                    Toast.makeText(AnimeDetailsActivity.this, R.string.review_sent, Toast.LENGTH_SHORT).show();

                                    animeViewModel.getAnimeDetails(serieDetail.getId());
                                    initMovieDetails();
                                }

                                @Override
                                public void onError(@NotNull Throwable e) {

                                    Toast.makeText(AnimeDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete() {

                                    //

                                }
                            });

                }else {

                    Tools.ToastHelper(AnimeDetailsActivity.this,getString(R.string.review_require_login));
                }

            }  else {

                double newUserRating = Math.round(Tools.getAvg(storySeekbar.getProgressFloat(),
                        caractersSeekbar.getProgressFloat()
                        ,mottionsdesginSeekbar.getProgressFloat()
                        ,musicSeekbar.getProgressFloat(),false)) /2;


                if (tokenManager.getToken().getAccessToken() !=null) {

                    authRepository.addRating(serieDetail.getId(),newUserRating, "anime")
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

                                    Toast.makeText(AnimeDetailsActivity.this, R.string.rating_sent, Toast.LENGTH_SHORT).show();

                                    animeViewModel.getAnimeDetails(serieDetail.getId());
                                    initMovieDetails();
                                }

                                @Override
                                public void onError(@NotNull Throwable e) {

                                    Toast.makeText(AnimeDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete() {

                                    //

                                }
                            });

                }else {

                    Tools.ToastHelper(AnimeDetailsActivity.this,getString(R.string.review_require_login));
                }
            }




        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);

        dialog.findViewById(R.id.bt_close).setOnClickListener(y -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }


    private void checkMediaFavorite(Media animeDetail) {


        if (settingsManager.getSettings().getFavoriteonline() == 1 && tokenManager.getToken().getAccessToken() !=null) {

            loginViewModel.isAnimeFavoriteOnline(animeDetail.getId());
            loginViewModel.isSerieFavoriteOnlineMutableLiveData.observe(this, favAddOnline -> {

                if (favAddOnline.getStatus() == 1) {

                    isMovieFav = true;

                    binding.favoriteImage.setImageResource(R.drawable.ic_in_favorite);

                } else {

                    isMovieFav = false;

                    binding.favoriteImage.setImageResource(R.drawable.add_from_queue);

                }

            });

        } else {

            if (mediaRepository.isAnimeFavorite(Integer.parseInt(animeDetail.getId()))) {


                binding.favoriteImage.setImageResource(R.drawable.ic_in_favorite);

            } else {


                binding.favoriteImage.setImageResource(R.drawable.add_from_queue);

            }
        }
    }

    private void onLoadAnimeCast(Media animeDetail) {



        if (settingsManager.getSettings().getDefaultCastOption().equals("IMDB")){

            if (animeDetail.getTmdbId() !=null) {


                animeViewModel.getSerieCast(Integer.parseInt(animeDetail.getTmdbId()));
                animeViewModel.serieCreditsMutableLiveData.observe(this, credits -> {

                    mCastAdapter.addCasts(credits.getCasts());


                });
            }

        }else {

            // Starring RecycleView
            mCastAdapter.addCasts(animeDetail.getCast());


        }

    }

    private void onLoadResumeFromHistory(History history, Media serieDetail) {

        mediaRepository.getAnimeEpisodeDetails(history.getEpisodeId(), settingsManager.getSettings().getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                        //

                    }

                    @Override
                    public void onNext(@NotNull MovieResponse movieResponse) {


                        if (settingsManager.getSettings().getServerDialogSelection() == 1) {

                            String[] charSequence = new String[movieResponse.getEpisodes().get(0).getVideos().size()];
                            for (int i = 0; i < movieResponse.getEpisodes().get(0).getVideos().size(); i++) {
                                charSequence[i] = String.valueOf(movieResponse.getEpisodes().get(0).getVideos().get(i).getServer());

                            }


                            final AlertDialog.Builder builder = new AlertDialog.Builder(AnimeDetailsActivity.this
                                    , R.style.MyAlertDialogTheme);
                            builder.setTitle(R.string.source_quality);
                            builder.setCancelable(true);
                            builder.setItems(charSequence, (dialogInterface, wich) -> {


                                if (movieResponse.getEpisodes().get(0).getVideos().get(wich).getSupportedHosts() == 1) {


                                    easyPlexSupportedHosts = new EasyPlexSupportedHosts(AnimeDetailsActivity.this);
                                    easyPlexSupportedHosts.onFinish(new EasyPlexSupportedHosts.OnTaskCompleted() {

                                        @Override
                                        public void onTaskCompleted(ArrayList<EasyPlexSupportedHostsModel> vidURL, boolean multipleQuality) {

                                            if (multipleQuality) {
                                                if (vidURL != null) {

                                                    CharSequence[] name = new CharSequence[vidURL.size()];

                                                    for (int i = 0; i < vidURL.size(); i++) {
                                                        name[i] = vidURL.get(i).getQuality();
                                                    }

                                                    final AlertDialog.Builder builder = new AlertDialog.Builder(AnimeDetailsActivity.this, R.style.MyAlertDialogTheme);
                                                    builder.setTitle(AnimeDetailsActivity.this.getString(R.string.select_qualities));
                                                    builder.setCancelable(true);
                                                    builder.setItems(name, (dialogInterface, i) -> {


                                                        if (settingsManager.getSettings().getVlc() == 1) {


                                                            final Dialog dialog = new Dialog(AnimeDetailsActivity.this);
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


                                                                Intent shareVideo = new Intent(Intent.ACTION_VIEW);
                                                                shareVideo.setDataAndType(Uri.parse(vidURL.get(i).getUrl()), "video/*");
                                                                shareVideo.setPackage("com.instantbits.cast.webvideo");
                                                                shareVideo.putExtra("title", history.getTitle());
                                                                shareVideo.putExtra("poster", history.getPosterPath());
                                                                Bundle headers = new Bundle();
                                                                headers.putString("Referer", settingsManager.getSettings().getAppName());
                                                                headers.putString("User-Agent", settingsManager.getSettings().getUserAgent());
                                                                shareVideo.putExtra("android.media.intent.extra.HTTP_HEADERS", headers);
                                                                shareVideo.putExtra("headers", headers);
                                                                shareVideo.putExtra("secure_uri", true);
                                                                try {
                                                                    startActivity(shareVideo);
                                                                } catch (ActivityNotFoundException ex) {
                                                                    // Open Play Store if it fails to launch the app because the package doesn't exist.
                                                                    // Alternatively you could use PackageManager.getLaunchIntentForPackage() and check for null.
                                                                    // You could try catch this and launch the Play Store website if it fails but this shouldn’t
                                                                    // fail unless the Play Store is missing.
                                                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                                                    String uriString = "market://details?id=com.instantbits.cast.webvideo";
                                                                    intent.setData(Uri.parse(uriString));
                                                                    startActivity(intent);
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
                                                    Toast.makeText(AnimeDetailsActivity.this, "NULL", Toast.LENGTH_SHORT).show();

                                            } else {

                                                onLoadMainPlayerStreamYoutube(vidURL.get(0).getUrl(), history, movieResponse, serieDetail);


                                                Timber.i("URL IS :%s", vidURL.get(0).getUrl());
                                            }

                                        }

                                        @Override
                                        public void onError() {

                                            Toast.makeText(AnimeDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    easyPlexSupportedHosts.find(movieResponse.getEpisodes().get(0).getVideos().get(wich).getLink());


                                } else {


                                    if (settingsManager.getSettings().getVlc() == 1) {


                                        final Dialog dialog = new Dialog(AnimeDetailsActivity.this);
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

                                            Intent shareVideo = new Intent(Intent.ACTION_VIEW);
                                            shareVideo.setDataAndType(Uri.parse(movieResponse.getEpisodes().get(0).getVideos().get(wich).getLink()), "video/*");
                                            shareVideo.setPackage("org.videolan.vlc");
                                            shareVideo.putExtra("title", movieResponse.getEpisodes().get(0).getName());
                                            shareVideo.putExtra("poster", movieResponse.getEpisodes().get(0).getStillPath());
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
                                            shareVideo.setDataAndType(Uri.parse(movieResponse.getEpisodes().get(0).getVideos().get(wich).getLink()), "video/*");
                                            shareVideo.setPackage("com.mxtech.videoplayer.ad");
                                            shareVideo.putExtra("title", movieResponse.getEpisodes().get(0).getName());
                                            shareVideo.putExtra("poster", movieResponse.getEpisodes().get(0).getStillPath());
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


                                Intent intent = new Intent(AnimeDetailsActivity.this, EmbedActivity.class);
                                intent.putExtra(Constants.MOVIE_LINK, movieResponse.getEpisodes().get(0).getVideos().get(0).getLink());
                                startActivity(intent);


                            } else if (movieResponse.getEpisodes().get(0).getVideos().get(0).getSupportedHosts() == 1) {


                                easyPlexSupportedHosts = new EasyPlexSupportedHosts(AnimeDetailsActivity.this);
                                easyPlexSupportedHosts.onFinish(new EasyPlexSupportedHosts.OnTaskCompleted() {

                                    @Override
                                    public void onTaskCompleted(ArrayList<EasyPlexSupportedHostsModel> vidURL, boolean multipleQuality) {

                                        if (multipleQuality) {
                                            if (vidURL != null) {

                                                CharSequence[] name = new CharSequence[vidURL.size()];

                                                for (int i = 0; i < vidURL.size(); i++) {
                                                    name[i] = vidURL.get(i).getQuality();
                                                }

                                                final AlertDialog.Builder builder = new AlertDialog.Builder(AnimeDetailsActivity.this, R.style.MyAlertDialogTheme);
                                                builder.setTitle(AnimeDetailsActivity.this.getString(R.string.select_qualities));
                                                builder.setCancelable(true);
                                                builder.setItems(name, (dialogInterface, i) -> {


                                                    if (settingsManager.getSettings().getVlc() == 1) {


                                                        final Dialog dialog = new Dialog(AnimeDetailsActivity.this);
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


                                                            Intent shareVideo = new Intent(Intent.ACTION_VIEW);
                                                            shareVideo.setDataAndType(Uri.parse(vidURL.get(i).getUrl()), "video/*");
                                                            shareVideo.setPackage("com.instantbits.cast.webvideo");
                                                            shareVideo.putExtra("title", history.getTitle());
                                                            shareVideo.putExtra("poster", history.getPosterPath());
                                                            Bundle headers = new Bundle();
                                                            headers.putString("Referer", settingsManager.getSettings().getAppName());
                                                            headers.putString("User-Agent", settingsManager.getSettings().getUserAgent());
                                                            shareVideo.putExtra("android.media.intent.extra.HTTP_HEADERS", headers);
                                                            shareVideo.putExtra("headers", headers);
                                                            shareVideo.putExtra("secure_uri", true);
                                                            try {
                                                                startActivity(shareVideo);
                                                            } catch (ActivityNotFoundException ex) {
                                                                // Open Play Store if it fails to launch the app because the package doesn't exist.
                                                                // Alternatively you could use PackageManager.getLaunchIntentForPackage() and check for null.
                                                                // You could try catch this and launch the Play Store website if it fails but this shouldn’t
                                                                // fail unless the Play Store is missing.
                                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                                String uriString = "market://details?id=com.instantbits.cast.webvideo";
                                                                intent.setData(Uri.parse(uriString));
                                                                startActivity(intent);
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
                                                Toast.makeText(AnimeDetailsActivity.this, "NULL", Toast.LENGTH_SHORT).show();

                                        } else {

                                            onLoadMainPlayerStreamYoutube(vidURL.get(0).getUrl(), history, movieResponse, serieDetail);


                                            Timber.i("URL IS :%s", vidURL.get(0).getUrl());
                                        }

                                    }

                                    @Override
                                    public void onError() {

                                        Toast.makeText(AnimeDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                easyPlexSupportedHosts.find(movieResponse.getEpisodes().get(0).getVideos().get(0).getLink());


                            } else {

                                onLoadMainPlayerStream(history, movieResponse, serieDetail);
                            }


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
    }

    private void onLoadMainPlayerStream(History history, MovieResponse movieResponse, Media serieDetail) {

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
        String type = "anime";
        String currentquality = movieResponse.getEpisodes().get(0).getVideos().get(0).getServer();
        String videourl = movieResponse.getEpisodes().get(0).getVideos().get(0).getLink();
        int hls = movieResponse.getEpisodes().get(0).getVideos().get(0).getHls();
        int drm =  movieResponse.getEpisodes().get(0).getVideos().get(0).getDrm();
        String Drmuuid =  movieResponse.getEpisodes().get(0).getVideos().get(0).getDrmuuid();
        String Drmlicenceuri =  movieResponse.getEpisodes().get(0).getVideos().get(0).getDrmlicenceuri();

        Intent intent = new Intent(AnimeDetailsActivity.this, EasyPlexMainPlayer.class);

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
                        ,movieResponse.getEpisodes().get(0).getHasrecap(),
                        movieResponse.getEpisodes().get(0).getSkiprecapStartIn()
                        ,mediaGenre,serieDetail.getName(),voteAverage,Drmuuid,Drmlicenceuri,drm));
        intent.putExtra(ARG_MOVIE, serieDetail);
        startActivity(intent);
    }

    private void onLoadMainPlayerStreamYoutube(String downloadUrl, History history, MovieResponse movieResponse, Media serieDetail) {

        String name = history.getTitle();
        String tvseasonid = history.getSeasonsId();
        Integer currentep = Integer.parseInt(movieResponse.getEpisodes().get(0).getEpisodeNumber());
        String currentepname = movieResponse.getEpisodes().get(0).getName();
        String currenteptmdbnumber = String.valueOf(movieResponse.getEpisodes().get(0).getId());
        String currentseasons = history.getCurrentSeasons();
        String currentseasonsNumber = history.getSeasonsNumber();
        String currentepimdb = String.valueOf(movieResponse.getEpisodes().get(0).getId());
        String artwork = movieResponse.getEpisodes().get(0).getStillPath();
        String type = "anime";
        float voteAverage = Float.parseFloat(movieResponse.getEpisodes().get(0).getVoteAverage());
        String currentquality = movieResponse.getEpisodes().get(0).getVideos().get(0).getServer();
        int hls = movieResponse.getEpisodes().get(0).getVideos().get(0).getHls();
        int drm =  movieResponse.getEpisodes().get(0).getVideos().get(0).getDrm();
        String Drmuuid =  movieResponse.getEpisodes().get(0).getVideos().get(0).getDrmuuid();
        String Drmlicenceuri =  movieResponse.getEpisodes().get(0).getVideos().get(0).getDrmlicenceuri();

        Intent intent = new Intent(AnimeDetailsActivity.this, EasyPlexMainPlayer.class);

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
                        ,movieResponse.getEpisodes().get(0).getHasrecap()
                        ,movieResponse.getEpisodes().get(0).getSkiprecapStartIn()
                        ,mediaGenre,serieDetail.getName(),voteAverage,Drmuuid,Drmlicenceuri,drm));
        intent.putExtra(ARG_MOVIE, serieDetail);
        startActivity(intent);
    }

    private void onLoadSubscribeDialog(History history, Media animeDetail) {


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

            if ("UnityAds".equals(defaultRewardedNetworkAds)) {

                onLoadUnityAds(history,animeDetail);

            } else if ("Admob".equals(defaultRewardedNetworkAds)) {


                onLoadAdmobRewardAds(history,animeDetail);


            } else if ("Facebook".equals(defaultRewardedNetworkAds)) {

                onLoadFaceBookRewardAds(history,animeDetail);

            }else if ("Appodeal".equals(defaultRewardedNetworkAds)) {

                onLoadAppOdealRewardAds(history,animeDetail);

            }

            dialog.dismiss();


        });


        dialog.findViewById(R.id.bt_close).setOnClickListener(v ->

                dialog.dismiss());


        dialog.show();
        dialog.getWindow().setAttributes(lp);


    }




    private void onLoadAppOdealRewardAds(History history, Media animeDetail) {

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

                onLoadResumeFromHistory(history,animeDetail);

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

    private void onLoadFaceBookRewardAds(History history, Media animeDetail) {

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

                onLoadResumeFromHistory(history,animeDetail);

            }


        };


        facebookInterstitialAd.loadAd(
                facebookInterstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }

    private void onLoadAdmobRewardAds(History history, Media animeDetail) {

        if (mRewardedAd == null) {
            Toast.makeText(this, "The rewarded ad wasn't ready yet", Toast.LENGTH_SHORT).show();
            return;
        }
        mRewardedAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        //
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
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
                AnimeDetailsActivity.this,
                rewardItem ->       onLoadResumeFromHistory(history,animeDetail));
    }

    private void loadRewardedAd() {

        if (mRewardedAd == null) {
            isLoading = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(
                    AnimeDetailsActivity.this,
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

    private void onLoadUnityAds(History history, Media animeDetail) {

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

                onLoadResumeFromHistory(history,animeDetail);
            }
        });

    }


    private void onLoadToolbar() {

        Tools.loadToolbar(this,binding.toolbar,binding.appbar);


    }



    private void onLoadAdmobNativeAds() {

        if (settingsManager.getSettings().getAdUnitIdNativeEnable() == 1) {


            AdLoader.Builder builder = new AdLoader.Builder(this, settingsManager.getSettings().getAdUnitIdNative());

            // OnLoadedListener implementation.
            builder.forNativeAd(
                    nativeAd -> {
                        // If this callback occurs after the activity is destroyed, you must call
                        // destroy and return or you may get a memory leak.
                        boolean isDestroyed;
                        isDestroyed = isDestroyed();
                        if (isDestroyed || isFinishing() || isChangingConfigurations()) {
                            nativeAd.destroy();
                            return;
                        }
                        // You must call destroy on old ads when you are done with them,
                        // otherwise you will have a memory leak.
                        if (mNativeAd != null) {
                            mNativeAd.destroy();
                        }
                        mNativeAd= nativeAd;

                        NativeAdView adView =
                                (NativeAdView) getLayoutInflater().inflate(R.layout.ad_unified, null);
                        populateNativeAdView(nativeAd, adView);
                        binding.flAdplaceholder.removeAllViews();
                        binding.flAdplaceholder.addView(adView);
                    });

            VideoOptions videoOptions =
                    new VideoOptions.Builder().build();

            NativeAdOptions adOptions =
                    new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

            builder.withNativeAdOptions(adOptions);

            AdLoader adLoader =
                    builder
                            .withAdListener(
                                    new com.google.android.gms.ads.AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {
                                            //
                                        }
                                    })
                            .build();

            adLoader.loadAd(new AdRequest.Builder().build());



        }


    }


    private void populateNativeAdView(com.google.android.gms.ads.nativead.NativeAd nativeAd, NativeAdView adView) {
        // Set the media view.
        adView.setMediaView(adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(nativeAd);
    }




    // Add or Remove Anime from Favorite
    public void onFavoriteClick(Animes animes) {

        if (mediaRepository.isAnimeFavorite(Integer.parseInt(animes.getId()))) {

            Timber.i(getString(R.string.remove_watch_list));
            animeViewModel.removeTvFromFavorite(animes);

            binding.favoriteImage.setImageResource(R.drawable.add_from_queue);

            Toast.makeText(this, getString(R.string.removed_mylist) + animes.getName(),
                    Toast.LENGTH_SHORT).show();
            movieDetailViewModel.removeFavorite(animes);

        }else {

            Timber.i(getString(R.string.remove_watch_list));
            animeViewModel.addtvFavorite(animes);

            binding.favoriteImage.setImageResource(R.drawable.ic_in_favorite);

            Toast.makeText(this, getString(R.string.added_mylist) + animes.getName(),
                    Toast.LENGTH_SHORT).show();
        }



    }



    // Load Seasons & episodes
    @SuppressLint("SetTextI18n")
    private void onLoadSeasons(Media serieDetail) {

        if (serieDetail.getSeasons() !=null && !serieDetail.getSeasons().isEmpty()) {

            binding.mseason.setText(SEASONS + serieDetail.getSeasons().size());

            for(Iterator<Season> iterator = serieDetail.getSeasons().iterator(); iterator.hasNext(); ) {
                if(iterator.next().getName().equals(SPECIALS))
                    iterator.remove();
            }
            binding.planetsSpinner.setItem(serieDetail.getSeasons());
            binding.planetsSpinner.setSelection(0);
            binding.planetsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {


                    Season season = (Season) adapterView.getItemAtPosition(position);
                    String episodeId = String.valueOf(season.getId());
                    String currentSeason = season.getName();
                    String seasonNumber = season.getSeasonNumber();


                    animeViewModel.searchQuery.setValue(episodeId);

                    // Episodes RecycleView
                    episodeAnimeAdapter = new EpisodeAnimeAdapter(serieDetail.getId(),
                            seasonNumber,episodeId,currentSeason,
                            sharedPreferences,authManager,settingsManager,mediaRepository
                            , serieDetail.getName(), serieDetail.getPremuim()
                            ,tokenManager,AnimeDetailsActivity.this
                            , serieDetail.getPosterPath()
                            , serieDetail,mediaGenre,
                            serieDetail.getImdbExternalId()
                            ,ANIMATION_TYPE,deviceManager);

                    animeViewModel.getAnimeSeasons().observe(AnimeDetailsActivity.this, animesLists -> episodeAnimeAdapter.submitList(animesLists));
                    binding.recyclerViewEpisodes.setAdapter(episodeAnimeAdapter);

                    mEpisodesLoaded = true;
                    checkAllDataLoaded();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                    // do nothing if no season selected

                }
            });
    }

    }




    // Load the anime rating
    private void onLoadRating(float voteAverage) {

        binding.viewMovieRating.setText(String.valueOf(voteAverage));
        binding.ratingBar.setRating(voteAverage / 2);
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

            binding.backbutton.performClick();

            Toast.makeText(this, R.string.vpn_message, Toast.LENGTH_SHORT).show();

        }

        if (provideSnifferCheck != null) {
            Toast.makeText(AnimeDetailsActivity.this, R.string.sniffer_message, Toast.LENGTH_SHORT).show();
            finishAffinity();
        }



        if (settingsManager.getSettings().getVpn() ==1 && checkVpn) {


            finishAffinity();

            Toast.makeText(this, R.string.vpn_message, Toast.LENGTH_SHORT).show();

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
                        AnimeDetailsActivity.this, mediaRouteMenuItem)
                        .setTitleText(getString(R.string.introducing_cast))
                        .setOverlayColor(R.color.primary)
                        .setSingleTime()
                        .setOnOverlayDismissedListener(
                                () -> mIntroductoryOverlay = null)
                        .build();
                mIntroductoryOverlay.show();

            }, 100);
        }
    }


    @SuppressLint("SetTextI18n")
    private void onLoadViews(String views) {

        binding.viewMovieViews.setText(getString(R.string.views)+Tools.getViewFormat(Integer.parseInt(views)));

    }


    @SuppressLint("SetTextI18n")
    private void onLoadAnimeComments(String id) {


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
        rv_comments.setLayoutManager(new LinearLayoutManager(AnimeDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
        rv_comments.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(this, 0), true));

        commentsAdapter.setOnItemClickListener(new CommentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(boolean clicked) {
                if (clicked) {

                    animeViewModel.getAnimeComments(Integer.parseInt(id));
                    animeViewModel.animeCommentsMutableLiveData.observe(AnimeDetailsActivity.this, comments -> commentsAdapter.addToContent(comments.getComments(), AnimeDetailsActivity.this, authManager, mediaRepository));
                }
            }
        });


        TextView commentTotal = dialog.findViewById(R.id.comment_total);

        FloatingActionButton add_comment_btn = dialog.findViewById(R.id.add_comment_btn);

        EditText editTextComment = dialog.findViewById(R.id.comment_message);

        LinearLayout noCommentFound = dialog.findViewById(R.id.no_comment_found);

        animeViewModel.getAnimeComments(Integer.parseInt(id));
        animeViewModel.animeCommentsMutableLiveData.observe(this, comments -> {


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

                    mediaRepository.addCommentAnime(editTextComment.getText().toString(),id)
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

                                    Toast.makeText(AnimeDetailsActivity.this, "Comment added successfully", Toast.LENGTH_SHORT).show();
                                    editTextComment.setText("");
                                    animeViewModel.getAnimeComments(Integer.parseInt(id));
                                    animeViewModel.animeCommentsMutableLiveData.observe(AnimeDetailsActivity.this, commentsx -> {

                                        commentsAdapter.addToContent(commentsx.getComments(),AnimeDetailsActivity.this,authManager,mediaRepository);
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

        animeViewModel.getRelatedsAnimes(id);
        animeViewModel.movieRelatedsMutableLiveData.observe(this, relateds -> {

            relatedsAdapter.addToContent(relateds.getRelateds());

            // Relateds Movies RecycleView

            binding.rvMylike.setAdapter(relatedsAdapter);
            binding.rvMylike.setHasFixedSize(true);
            binding.rvMylike.setNestedScrollingEnabled(false);
            binding.rvMylike.setLayoutManager(new LinearLayoutManager(AnimeDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            binding.rvMylike.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(this, 0), true));

            if (relatedsAdapter.getItemCount() == 0) {

                binding.relatedNotFound.setVisibility(View.VISIBLE);

            }else {

                binding.relatedNotFound.setVisibility(GONE);

            }


        });
    }

    // Handle Back Button
    private void onLoadBackButton() {

        binding.backbutton.setOnClickListener(v -> {
            onBackPressed();
            Animatoo.animateSplit(this);

        });
    }



    // Load Anime Trailer
    private void onLoadTrailer(String previewPath, String title, String backdrop, String trailerUrl) {


        if (sharedPreferences.getBoolean(Constants.WIFI_CHECK, false) &&
                NetworkUtils.isWifiConnected(this)) {

            DialogHelper.showWifiWarning(AnimeDetailsActivity.this);

        }else {

            Tools.startTrailer(this,previewPath,title,backdrop,settingsManager,trailerUrl);

        }

    }


    // Display Anime Poster
    private void onLoadImage(String imageURL){


        GlideApp.with(getApplicationContext()).asBitmap().load(imageURL)
                .fitCenter()
                .placeholder(R.color.fragment_content_detail_overlay_end)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(withCrossFade())
                .into(binding.imageMoviePoster);


        if (settingsManager.getSettings().getEnablelayoutchange() == 1) {

            Glide.with(this).load(imageURL)
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(RADUIS, SAMPLING)))
                    .into(binding.imageMoviePoster);


            GlideApp.with(getApplicationContext()).asBitmap().load(imageURL)
                    .fitCenter()
                    .placeholder(R.color.fragment_content_detail_overlay_end)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(withCrossFade())
                    .into(binding.imagePosterSecondry);

        }

    }

    // Display Anime Title
    private void onLoadTitle(String title){

        binding.serieTitle.setText(title);
    }


    // Display Anime Release Date
    private void onLoadDate(String date){

        Tools.dateFormat(date, binding.mrelease);
    }


    // Display Anime Synopsis or Overview
    private void onLoadSynopsis(String synopsis){
        binding.serieOverview.setText(synopsis);
    }


    private void onLoadReport(String title, String posterpath) {


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

                animeViewModel.sendReport(settingsManager.getSettings().getApiKey(),title,editTextMessage.getText().toString());
                animeViewModel.reportMutableLiveData.observe(AnimeDetailsActivity.this, report -> {


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

    // Anime Genres
    private void onLoadGenres(List<Genre> genresList) {
        for (Genre genre : genresList) {
            binding.mgenres.setText(genre.getName());
            mediaGenre = genre.getName();
        }

    }



    private void checkAllDataLoaded() {

        if (mAnime && mEpisodesLoaded) {

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                binding.progressBar.setVisibility(View.GONE);
                binding.scrollView.setVisibility(View.VISIBLE);
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


        binding.appodealBannerView.removeAllViews();
        binding.appodealBannerView.removeAllViewsInLayout();


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



        Appodeal.destroy(Appodeal.BANNER);
        Appodeal.destroy(Appodeal.INTERSTITIAL);
        Appodeal.destroy(Appodeal.REWARDED_VIDEO);
        Glide.get(this).clearMemory();
        binding = null;
        super.onDestroy();

    }

}
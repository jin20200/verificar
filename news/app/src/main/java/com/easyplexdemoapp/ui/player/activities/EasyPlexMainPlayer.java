package com.easyplexdemoapp.ui.player.activities;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY;
import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;
import static com.easyplexdemoapp.EasyPlexApp.getContext;
import static com.easyplexdemoapp.ui.player.utilities.ExoPlayerLogger.i;
import static com.easyplexdemoapp.ui.player.utilities.ExoPlayerLogger.v;
import static com.easyplexdemoapp.util.Constants.AUTO_PLAY;
import static com.easyplexdemoapp.util.Constants.E;
import static com.easyplexdemoapp.util.Constants.EP;
import static com.easyplexdemoapp.util.Constants.PIP_DENOMINATOR_DEFAULT;
import static com.easyplexdemoapp.util.Constants.PIP_NUMERATOR_DEFAULT;
import static com.easyplexdemoapp.util.Constants.S0;
import static com.easyplexdemoapp.util.Constants.SEASONS;
import static com.easyplexdemoapp.util.Constants.SERVER_BASE_URL;
import static com.easyplexdemoapp.util.Constants.SPECIALS;
import static com.easyplexdemoapp.util.Constants.SUBSTITLE_LOCATION;
import static com.easyplexdemoapp.util.Constants.SUBSTITLE_SUB_FILENAME_ZIP;
import static com.easyplexdemoapp.util.Constants.SUBS_DEFAULT_LANG;
import static com.easyplexdemoapp.util.Constants.UPNEXT;
import static com.easyplexdemoapp.util.Constants.VLC_PACKAGE_NAME;
import static com.easyplexdemoapp.util.Constants.ZIP_FILE_NAME;
import static com.easyplexdemoapp.util.Constants.ZIP_FILE_NAME2;
import static com.easyplexdemoapp.util.Constants.ZIP_FILE_NAME3;
import static com.easyplexdemoapp.util.Constants.ZIP_FILE_NAME4;
import static com.easyplexdemoapp.util.Tools.EXTRA_HEADERS;
import static com.easyplexdemoapp.util.Tools.HEADERS;
import static com.easyplexdemoapp.util.Tools.POSTER;
import static com.easyplexdemoapp.util.Tools.SECURE_URI;
import static com.easyplexdemoapp.util.Tools.TITLE;
import static com.easyplexdemoapp.util.Tools.ToastHelper;
import static com.easyplexdemoapp.util.Tools.USER_AGENT;
import static com.easyplexdemoapp.util.Tools.VIDEOTYPE;
import static com.google.android.exoplayer2.Player.STATE_ENDED;
import static com.google.android.exoplayer2.Player.STATE_IDLE;
import static java.lang.String.valueOf;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.webkit.WebBackForwardList;
import android.webkit.WebHistoryItem;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easyplex.easyplexsupportedhosts.EasyPlexSupportedHosts;
import com.easyplex.easyplexsupportedhosts.Model.EasyPlexSupportedHostsModel;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.datasource.genreslist.AnimesGenresListDataSourceFactory;
import com.easyplexdemoapp.data.datasource.genreslist.MoviesGenresListDataSourceFactory;
import com.easyplexdemoapp.data.datasource.genreslist.SeriesGenresListDataSourceFactory;
import com.easyplexdemoapp.data.datasource.movie.MovieDataSource;
import com.easyplexdemoapp.data.datasource.stream.StreamDataSource;
import com.easyplexdemoapp.data.datasource.stream.StreamingDataSourceFactory;
import com.easyplexdemoapp.data.local.entity.History;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.MovieResponse;
import com.easyplexdemoapp.data.model.ads.AdMediaModel;
import com.easyplexdemoapp.data.model.ads.AdRetriever;
import com.easyplexdemoapp.data.model.ads.CuePointsRetriever;
import com.easyplexdemoapp.data.model.episode.EpisodeStream;
import com.easyplexdemoapp.data.model.genres.Genre;
import com.easyplexdemoapp.data.model.genres.GenresByID;
import com.easyplexdemoapp.data.model.genres.GenresData;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.data.model.media.Resume;
import com.easyplexdemoapp.data.model.serie.Season;
import com.easyplexdemoapp.data.model.stream.MediaStream;
import com.easyplexdemoapp.data.model.substitles.MediaSubstitle;
import com.easyplexdemoapp.data.model.substitles.Opensub;
import com.easyplexdemoapp.data.repository.AnimeRepository;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.StatusManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.ui.player.adapters.AnimesEpisodesPlayerAdapter;
import com.easyplexdemoapp.ui.player.adapters.AnimesListAdapter;
import com.easyplexdemoapp.ui.player.adapters.ClickDetectListner;
import com.easyplexdemoapp.ui.player.adapters.EpisodesPlayerAdapter;
import com.easyplexdemoapp.ui.player.adapters.MovieQualitiesAdapter;
import com.easyplexdemoapp.ui.player.adapters.MoviesListAdapter;
import com.easyplexdemoapp.ui.player.adapters.SerieQualitiesAdapter;
import com.easyplexdemoapp.ui.player.adapters.SeriesListAdapter;
import com.easyplexdemoapp.ui.player.adapters.StreamingListAdapter;
import com.easyplexdemoapp.ui.player.adapters.StreamingQualitiesAdapter;
import com.easyplexdemoapp.ui.player.adapters.SubstitlesAdapter;
import com.easyplexdemoapp.ui.player.bindings.PlayerController;
import com.easyplexdemoapp.ui.player.controller.PlayerAdLogicController;
import com.easyplexdemoapp.ui.player.controller.PlayerUIController;
import com.easyplexdemoapp.ui.player.fsm.Input;
import com.easyplexdemoapp.ui.player.fsm.callback.AdInterface;
import com.easyplexdemoapp.ui.player.fsm.concrete.AdPlayingState;
import com.easyplexdemoapp.ui.player.fsm.concrete.VpaidState;
import com.easyplexdemoapp.ui.player.fsm.listener.AdPlayingMonitor;
import com.easyplexdemoapp.ui.player.fsm.listener.CuePointMonitor;
import com.easyplexdemoapp.ui.player.fsm.state_machine.FsmPlayer;
import com.easyplexdemoapp.ui.player.fsm.state_machine.FsmPlayerApi;
import com.easyplexdemoapp.ui.player.interfaces.AutoPlay;
import com.easyplexdemoapp.ui.player.interfaces.DoublePlayerInterface;
import com.easyplexdemoapp.ui.player.interfaces.VpaidClient;
import com.easyplexdemoapp.ui.player.utilities.ExoPlayerLogger;
import com.easyplexdemoapp.ui.player.utilities.PlaybackSettingMenu;
import com.easyplexdemoapp.ui.player.utilities.PlayerDeviceUtils;
import com.easyplexdemoapp.ui.player.utilities.TrackSelectionDialog;
import com.easyplexdemoapp.ui.player.views.UIControllerView;
import com.easyplexdemoapp.ui.settings.SettingsActivity;
import com.easyplexdemoapp.ui.viewmodels.PlayerViewModel;
import com.easyplexdemoapp.util.Constants;
import com.easyplexdemoapp.util.DialogHelper;
import com.easyplexdemoapp.util.DownloadFileAsync;
import com.easyplexdemoapp.util.GlideApp;
import com.easyplexdemoapp.util.HistorySaver;
import com.easyplexdemoapp.util.SpacingItemDecoration;
import com.easyplexdemoapp.util.Tools;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Tracks;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.Log;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.LevelPlayInterstitialListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.vungle.warren.AdConfig;
import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.error.VungleException;
import com.wortise.ads.interstitial.InterstitialAd;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import org.jetbrains.annotations.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.AndroidInjection;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;


/**
 * EasyPlex - Movies - Live Streaming - TV Series, Anime
 *
 * @author @Y0bEX
 * @package EasyPlex - Movies - Live Streaming - TV Series, Anime
 * @copyright Copyright (c) 2024 Y0bEX,
 * @license <a href="http://codecanyon.net/wiki/support/legal-terms/licensing-terms/">...<a href="</a>
">* @profile https://codecany</a>on.net/user/yobex
 * @link yobexd@gmail.com
 * @skype yobexd@gmail.com
 **/

public class EasyPlexMainPlayer extends EasyPlexPlayerActivity implements DoublePlayerInterface, AutoPlay, ClickDetectListner, DialogInterface.OnDismissListener {


    private FsmPlayerApi fsmPlayerApi;
    private String subsExtracted;
    private MaxInterstitialAd maxInterstitialAd;


    com.wortise.ads.interstitial.InterstitialAd mInterstitialWortise;

    private boolean adsLaunched = false;
    private static final String TAG = "EasyPlexMainPlayer";
    protected ExoPlayer adPlayer;
    private CountDownTimer mCountDownTimer;
    private MediaModel mMediaModel;
    private String mediaGenre;
    public final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final int FIRSTPAGE = 1;
    private boolean playereadyboolean = false;
    private int currentGenre = 0;
    private com.google.android.gms.ads.interstitial.InterstitialAd mInterstitialAd;

    @Inject
    @Named("easyplexsupportedhost")
    EasyPlexSupportedHosts easyPlexSupportedHosts;

    @Inject
    FsmPlayer fsmPlayer;
    @Inject
    PlayerUIController playerUIController;
    @Inject
    AdPlayingMonitor adPlayingMonitor;
    @Inject
    CuePointMonitor cuePointMonitor;
    @Inject
    AdRetriever adRetriever;
    @Inject
    CuePointsRetriever cuePointsRetriever;
    @Inject
    AdInterface adInterface;
    @Inject
    PlayerAdLogicController playerComponentController;
    @Inject
    VpaidClient vpaidClient;

    @Inject
    PlaybackSettingMenu playbackSettingMenu;

    @Inject
    SettingsManager appSettingsManager;

    @Inject
    StatusManager statusManager;

    @Inject
    TokenManager tokenManager;

    @Inject
    SharedPreferences.Editor sharedPreferencesEditor;

    @Inject
    PlayerController playerController;

    @Inject
    MediaRepository repository;


    @Inject
    AnimeRepository animeRepository;

    boolean randomMovieFirstReady;
    private EpisodesPlayerAdapter mEPAdapter;
    private AnimesEpisodesPlayerAdapter animesEpisodesPlayerAdapter;
    private MoviesListAdapter moviesListAdapter;
    private SeriesListAdapter seriesListAdapter;
    private AnimesListAdapter animesListAdapter;
    private StreamingListAdapter streamingListAdapter;
    private SubstitlesAdapter mSubstitleAdapter;
    private MovieQualitiesAdapter movieQualitiesAdapter;
    private SerieQualitiesAdapter serieQualitiesAdapter;
    private StreamingQualitiesAdapter streamingQualitiesAdapter;
    public ClickDetectListner clickDetectListner = this;
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private UIControllerView uiControllerView;

    public final MutableLiveData<String> searchQuery = new MutableLiveData<>();
    public final MutableLiveData<String> mediaType = new MutableLiveData<>();

    private History history;
    private Resume resume;

    private String subs;

    private String launchedFromDownload;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        getPlayerController().isCurrentSubstitleAuto(settingsManager.getSettings().getAutosubstitles() == 1);
        initNavigation();

        playerViewModel = new ViewModelProvider(this, viewModelFactory).get(PlayerViewModel.class);

        Intent intent = getIntent();

        launchedFromDownload = intent.getStringExtra("from_download");


        if (settingsManager.getSettings().getApplovinInterstitialUnitid() !=null && !settingsManager.getSettings().getApplovinInterstitialUnitid().isEmpty()) {

            maxInterstitialAd = new MaxInterstitialAd(settingsManager.getSettings().getApplovinInterstitialUnitid(), this );
            maxInterstitialAd.loadAd();
        }


        mSubstitleAdapter = new SubstitlesAdapter();
        binding.rvSubstitles.setLayoutManager(new LinearLayoutManager(EasyPlexMainPlayer.this));
        binding.rvSubstitles.setHasFixedSize(true);
        binding.rvSubstitles.setAdapter(mSubstitleAdapter);



        movieQualitiesAdapter = new MovieQualitiesAdapter();

        serieQualitiesAdapter = new SerieQualitiesAdapter();

        streamingQualitiesAdapter = new StreamingQualitiesAdapter();

        binding.rvQualites.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.rvQualites.setHasFixedSize(true);
        binding.rvQualites.setAdapter(movieQualitiesAdapter);
    }



    @Override
    public void onResume() {
        super.onResume();


        if (getPlayerController().hasSubsActive()) {

            if (getPlayerController().getMediaType().equals("0")) {

                String id = getPlayerController().getVideoID();
                String externalId = getPlayerController().getMediaSubstitleName();
                String type = getPlayerController().getMediaType();
                String currentQuality = getPlayerController().getVideoCurrentQuality();
                String artwork = String.valueOf(getPlayerController().getMediaPoster()) ;
                String name = getPlayerController().getCurrentVideoName();
                mediaModel = MediaModel.media(id,externalId,currentQuality,type,name, String.valueOf(getPlayerController().getVideoUrl()), artwork, String.valueOf(getPlayerController().getMediaSubstitleUrl()),null,null
                        ,null,null,null,
                        null,
                        null,null,getPlayerController().isMediaPremuim(),
                        getPlayerController().getCurrentHlsFormat(),null,getPlayerController().getCurrentExternalId()
                        ,getPlayerController().getMediaCoverHistory(),
                        getPlayerController().getCurrentHasRecap()
                        ,getPlayerController().getCurrentStartRecapIn(),
                        getPlayerController().getMediaGenre(),null,getPlayerController().getVoteAverage(),getPlayerController().getDrmuuid()
                        ,getPlayerController().getDrmlicenceuri(),getPlayerController().getDrm());
                update(mMediaModel);


            }else if (getPlayerController().getMediaType().equals("1") || getPlayerController().getMediaType().equals("anime")) {

                String id = getPlayerController().getVideoID();
                String externalId = getPlayerController().getCurrentExternalId();
                String type = getPlayerController().getMediaType();
                String currentQuality = getPlayerController().getVideoCurrentQuality();
                String artwork = String.valueOf(getPlayerController().getMediaPoster());
                String name = getPlayerController().getCurrentVideoName();
                mediaModel = MediaModel.media(id,externalId,currentQuality,type,name, String.valueOf(getPlayerController().getVideoUrl()), artwork,
                        String.valueOf(getPlayerController().getMediaSubstitleUrl()),Integer.parseInt(getPlayerController().getEpID()),null
                        ,getPlayerController().getCurrentEpTmdbNumber(),getPlayerController().getSeaonNumber(),
                        getPlayerController().getEpName(),getPlayerController().getSeaonNumber(),
                        getPlayerController().getCurrentEpisodePosition(),getPlayerController().getCurrentEpTmdbNumber(),getPlayerController().isMediaPremuim(),
                        getPlayerController().getCurrentHlsFormat(),
                        null,getPlayerController().getCurrentExternalId()
                        ,getPlayerController().getMediaCoverHistory(),
                        getPlayerController().getCurrentHasRecap(),getPlayerController().getCurrentStartRecapIn()
                        ,getPlayerController().getMediaGenre(),getPlayerController().getSerieName()
                        ,getPlayerController().getVoteAverage(),getPlayerController().getDrmuuid()
                        ,getPlayerController().getDrmlicenceuri(),getPlayerController().getDrm());
                update(mediaModel);

            }
        }
    }

    private void initNavigation() {
        mBehavior = BottomSheetBehavior.from(binding.bottomSheet);
        getPlayerController().playerReady(!playereadyboolean);
        getPlayerController().settingReady(settingReady);
        if (sharedPreferences.getString(cuePointY, cuePointN).equals(cuePointN)) finishAffinity();
    }


    @Override
    public View addUserInteractionView() {
        if (sharedPreferences.getString(cuePointY, cuePointN).equals(cuePointN)) {finishAffinity();
        }else{playereadyboolean = !playerReady.equals("1");
            return new UIControllerView(getBaseContext()).setPlayerController((PlayerController) getPlayerController());}
        return null;
    }



    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (playerUIController != null) {
            playerUIController.clearMovieResumeInfo();

        }
    }


    @Override
    protected void initMoviePlayer() {
        super.initMoviePlayer();
        createMediaSource(mediaModel);
        getPlayerController().isMediaHasRecap(getPlayerController().getCurrentHasRecap() == 1);


    }

    private void onLoadAnimeResume() {

        if (settingsManager.getSettings().getResumeOffline() == 1) {


            repository.hasResume(Integer.parseInt(getPlayerController().getCurrentEpTmdbNumber())).observe(this, resumeInfo -> {

                if (resumeInfo != null) {


                    if (resumeInfo.getTmdb() != null) {


                        if (resumeInfo.getTmdb().equals(getPlayerController().getCurrentEpTmdbNumber()) && Tools.id(this).equals(resumeInfo.getDeviceId())) {


                            if (resumeInfo.getResumePosition() < 0) {

                                mMoviePlayer.seekTo(0);

                            } else {
                                mMoviePlayer.seekTo(resumeInfo.getResumePosition());
                            }


                        } else {

                            mMoviePlayer.seekTo(0);

                        }


                    } else {

                        mMoviePlayer.seekTo(0);
                    }

                }
            });


        } else {



            if (settingsManager.getSettings().getProfileSelection() == 1 && authManager.getSettingsProfile().getId() !=null){



                repository.getUserProfileResumeById(getPlayerController().getCurrentEpTmdbNumber(), authManager.getSettingsProfile().getId(),
                                settingsManager.getSettings().getApiKey())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<>() {
                            @Override
                            public void onSubscribe(@NotNull Disposable d) {

                                //

                            }

                            @Override
                            public void onNext(@NotNull Resume resumeInfo) {



                                if (Tools.id(getBaseContext()).equals(resumeInfo.getDeviceId())) {

                                    mMoviePlayer.seekTo(resumeInfo.getResumePosition());

                                } else {

                                    mMoviePlayer.seekTo(0);

                                }


                            }

                            @SuppressLint("ClickableViewAccessibility")
                            @Override
                            public void onError(@NotNull Throwable e) {


                                mMoviePlayer.seekTo(0);


                            }

                            @Override
                            public void onComplete() {

                                //

                            }
                        });


            }else {


                repository.getResumeById(getPlayerController().getCurrentEpTmdbNumber(), settingsManager.getSettings().getApiKey())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<>() {
                            @Override
                            public void onSubscribe(@NotNull Disposable d) {

                                //

                            }

                            @Override
                            public void onNext(@NotNull Resume resumeInfo) {



                                if (Tools.id(getBaseContext()).equals(resumeInfo.getDeviceId())) {

                                    mMoviePlayer.seekTo(resumeInfo.getResumePosition());

                                } else {

                                    mMoviePlayer.seekTo(0);

                                }


                            }

                            @SuppressLint("ClickableViewAccessibility")
                            @Override
                            public void onError(@NotNull Throwable e) {


                                mMoviePlayer.seekTo(0);


                            }

                            @Override
                            public void onComplete() {

                                //

                            }
                        });
            }





        }

    }

    private void onLoadSerieResume() {


        if (settingsManager.getSettings().getResumeOffline() == 1) {



            repository.hasResume(Integer.parseInt(getPlayerController().getCurrentEpTmdbNumber())).observe(this, resumeInfo -> {

                if (resumeInfo != null) {


                    if (resumeInfo.getTmdb() != null) {


                        if (resumeInfo.getTmdb().equals(getPlayerController().getCurrentEpTmdbNumber()) && Tools.id(this).equals(resumeInfo.getDeviceId())) {


                            if (resumeInfo.getResumePosition() < 0) {

                                mMoviePlayer.seekTo(0);

                            } else {
                                mMoviePlayer.seekTo(resumeInfo.getResumePosition());
                            }


                        } else {

                            mMoviePlayer.seekTo(0);

                        }


                    } else {

                        mMoviePlayer.seekTo(0);
                    }

                }
            });


        } else {


            repository.getResumeById(getPlayerController().getCurrentEpTmdbNumber(), settingsManager.getSettings().getApiKey())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                            //

                        }

                        @Override
                        public void onNext(@NotNull Resume resumeInfo) {


                            if (Tools.id(getBaseContext()).equals(resumeInfo.getDeviceId())) {

                                mMoviePlayer.seekTo(resumeInfo.getResumePosition());

                            } else {

                                mMoviePlayer.seekTo(0);

                            }


                        }

                        @SuppressLint("ClickableViewAccessibility")
                        @Override
                        public void onError(@NotNull Throwable e) {


                            mMoviePlayer.seekTo(0);


                        }

                        @Override
                        public void onComplete() {

                            //

                        }
                    });


        }


    }

    private void onLoadMovieResume() {

        if (settingsManager.getSettings().getResumeOffline() == 1) {


            repository.hasResume(Integer.parseInt(getPlayerController().getVideoID())).observe(this, resumeInfo -> {

                if (resumeInfo != null) {


                    if (resumeInfo.getTmdb() != null) {


                        if (resumeInfo.getTmdb().equals(getPlayerController().getVideoID()) && Tools.id(this).equals(resumeInfo.getDeviceId())) {


                            if (resumeInfo.getResumePosition() < 0) {


                                mMoviePlayer.seekTo(0);

                            } else {

                                mMoviePlayer.seekTo(resumeInfo.getResumePosition());

                            }

                        } else {

                            mMoviePlayer.seekTo(0);

                        }


                    } else {


                        mMoviePlayer.seekTo(0);
                    }


                }

            });


        } else {


            repository.getResumeById(getPlayerController().getVideoID(), settingsManager.getSettings().getApiKey())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                            //

                        }

                        @Override
                        public void onNext(@NotNull Resume resumeInfo) {

                            if (Tools.id(getBaseContext()).equals(resumeInfo.getDeviceId())) {

                                mMoviePlayer.seekTo(resumeInfo.getResumePosition());

                            } else {

                                mMoviePlayer.seekTo(0);

                            }

                        }

                        @SuppressLint("ClickableViewAccessibility")
                        @Override
                        public void onError(@NotNull Throwable e) {


                            mMoviePlayer.seekTo(0);


                        }

                        @Override
                        public void onComplete() {

                            //

                        }
                    });

        }

    }



    @Override
    protected void onPlayerReady() {

        prepareFSM();

    }


    private void onPlayerReadyLoadSubstitles() {


        getPlayerController().setScalePresenter();

        if (settingsManager.getSettings().getAutosubstitles() == 1){

            String defaultLang = sharedPreferences.getString(SUBS_DEFAULT_LANG, "English");

            if (settingsManager.getSettings().getDefaultSubstitleOption().equals("Opensubs")) {

                String mediaType = getPlayerController().getMediaType();
                if ("0".equals(mediaType)) {

                    repository.getMovieSubsByImdb(getPlayerController().getCurrentExternalId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<>() {
                                @Override
                                public void onSubscribe(@NotNull Disposable d) {

                                    //

                                }

                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Opensub> opensubs) {


                                    String defaultLang = sharedPreferences.getString(SUBS_DEFAULT_LANG, "English");

                                    List<Opensub> subsList = new ArrayList<>();

                                    for (Opensub opensub : opensubs) {


                                        if (opensub.getZipDownloadLink() !=null && opensub.getSubFormat() !=null && !opensub.getSubFormat().isEmpty() &&

                                                opensub.getSubFormat().equals("srt")
                                                && opensub.getSubHD() !=null && opensub.getSubHD().equals("1") && opensub.getSubEncoding() !=null && opensub.getLanguageName().equals(defaultLang)) {

                                            String langName = opensub.getLanguageName();
                                            String langMovieName = opensub.getMovieReleaseName();
                                            String langDownloadLink = opensub.getZipDownloadLink();
                                            String langSrtName = opensub.getSubFileName();
                                            subsList.add(new Opensub(langSrtName,langMovieName,langName,langDownloadLink));
                                            subsList.add(opensub);
                                        }

                                    }

                                    //Optional<Opensub> matchedSubstitle = subsList.stream().findFirst();

                                    if (!subsList.isEmpty()){

                                        DownloadFileAsync download = new DownloadFileAsync(
                                                EasyPlexMainPlayer.this
                                                        .getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())
                                                        +SUBSTITLE_SUB_FILENAME_ZIP, file -> {
                                            Log.i(TAG, "file download completed");
                                            // check unzip file now
                                            ZipFile zipFile;
                                            zipFile = new ZipFile("subs.zip");
                                            FileHeader fileHeader;
                                            fileHeader = zipFile.getFileHeader(
                                                    EasyPlexMainPlayer.this.getExternalFilesDir(Environment.getDataDirectory()
                                                            .getAbsolutePath())+SUBSTITLE_SUB_FILENAME_ZIP);
                                            if (fileHeader != null) {
                                                zipFile.removeFile(fileHeader);
                                            }else {
                                                new ZipFile(file, null).extractFile(subsList.get(0).getSubFileName(),
                                                        valueOf(EasyPlexMainPlayer.this.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()))
                                                        , ZIP_FILE_NAME);
                                                Log.i(TAG, "file unzip completed");
                                            }



                                        });

                                        download.execute(subsList.get(0).getZipDownloadLink());

                                        if (clickDetectListner !=null) {
                                                        clickDetectListner.onSubstitleClicked(true);
                                                    }

                                        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                            String subs = SUBSTITLE_LOCATION + EasyPlexMainPlayer.this.getPackageName() + "/files/data/" + ZIP_FILE_NAME;

                                            String substitleLanguage = subsList.get(0).getLanguageName();


                                            String id = getPlayerController().getVideoID();
                                            String type = getPlayerController().getMediaType();
                                            String currentQuality = getPlayerController().getVideoCurrentQuality();
                                            String artwork = (valueOf(getPlayerController().getMediaPoster()));
                                            String name = getPlayerController().getCurrentVideoName();
                                            String videoUrl = (valueOf(getPlayerController().getVideoUrl()));
                                            mMediaModel = MediaModel.media(id, substitleLanguage, currentQuality, type, name, videoUrl, artwork,
                                                    valueOf(Tools.convertToUTF(EasyPlexMainPlayer.this, Uri.parse(subs))), null, null
                                                    , null, null, null, null, null,
                                                    null, null, getPlayerController().getCurrentHlsFormat(),
                                                    "srt", getPlayerController().getCurrentExternalId(), getPlayerController().getMediaCoverHistory(),
                                                    getPlayerController().getCurrentHasRecap(),
                                                    getPlayerController().getCurrentStartRecapIn(), getPlayerController().getMediaGenre(), null, 0
                                                    ,getPlayerController().getDrmuuid()
                                                    ,getPlayerController().getDrmlicenceuri(),getPlayerController().getDrm());
                                            update(mMediaModel);
                                            getPlayerController().isSubtitleEnabled(true);
                                            getPlayerController().subtitleCurrentLang(substitleLanguage);

                                        }, 3000);


                                    }


                                }


                                @SuppressLint("ClickableViewAccessibility")
                                @Override
                                public void onError(@NotNull Throwable e) {


                                    Toast.makeText(EasyPlexMainPlayer.this, "No Substitles Found for this media", Toast.LENGTH_SHORT).show();


                                }

                                @Override
                                public void onComplete() {

                                    //

                                }
                            });



                }else  {

                    repository.getEpisodeSubsByImdb(getPlayerController().getEpID(),getPlayerController().getCurrentExternalId(),getPlayerController().getSeaonNumber())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<>() {
                                @Override
                                public void onSubscribe(@NotNull Disposable d) {

                                    //

                                }

                                @Override
                                public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Opensub> opensubs) {



                                    List<Opensub> subsList = new ArrayList<>();

                                    for (Opensub opensub : opensubs) {


                                        if (!subsList.contains(opensub) && opensub.getZipDownloadLink() !=null && opensub.getSubFormat() !=null && !opensub.getSubFormat().isEmpty() &&

                                                Objects.equals(opensub.getSubFormat(), "srt")

                                                && opensub.getSubHD() !=null && opensub.getSubHD().equals("1") && opensub.getSubEncoding() !=null && opensub.getLanguageName().equals(defaultLang)) {


                                            String langName = opensub.getLanguageName();
                                            String langMovieName = opensub.getMovieReleaseName();
                                            String langDownloadLink = opensub.getZipDownloadLink();
                                            String langSrtName = opensub.getSubFileName();
                                            subsList.add(new Opensub(langSrtName,langMovieName,langName,langDownloadLink));
                                            subsList.add(opensub);

                                        }


                                    }



                                    if (!subsList.isEmpty()){


                                        DownloadFileAsync download = new DownloadFileAsync(
                                                EasyPlexMainPlayer.this
                                                        .getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())
                                                        +SUBSTITLE_SUB_FILENAME_ZIP, file -> {
                                            Log.i(TAG, "file download completed");
                                            // check unzip file now
                                            ZipFile zipFile;
                                            zipFile = new ZipFile("subs.zip");
                                            FileHeader fileHeader;
                                            fileHeader = zipFile.getFileHeader(
                                                    EasyPlexMainPlayer.this.getExternalFilesDir(Environment.getDataDirectory()
                                                            .getAbsolutePath())+SUBSTITLE_SUB_FILENAME_ZIP);
                                            if (fileHeader != null) {
                                                zipFile.removeFile(fileHeader);
                                            }else {
                                                new ZipFile(file, null).extractFile(subsList.get(0).getSubFileName(),
                                                        valueOf(EasyPlexMainPlayer.this.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()))
                                                        , ZIP_FILE_NAME);
                                                Log.i(TAG, "file unzip completed");
                                            }



                                        });

                                        download.execute(subsList.get(0).getZipDownloadLink());

                                        if (clickDetectListner !=null) {
                                                        clickDetectListner.onSubstitleClicked(true);
                                                    }

                                        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                            String subs = SUBSTITLE_LOCATION + EasyPlexMainPlayer.this.getPackageName() + "/files/data/" + ZIP_FILE_NAME;
                                            String substitleLanguage = subsList.get(0).getLanguageName();
                                            String id = getPlayerController().getVideoID();
                                            String type = getPlayerController().getMediaType();
                                            String currentQuality = getPlayerController().getVideoCurrentQuality();
                                            String artwork = (valueOf(getPlayerController().getMediaPoster()));
                                            String name = getPlayerController().getCurrentVideoName();
                                            String videoUrl = (valueOf(getPlayerController().getVideoUrl()));
                                            mMediaModel = MediaModel.media(id, substitleLanguage, currentQuality, type, name, videoUrl, artwork,
                                                    subs, null, null
                                                    , null, null, null, null, null,
                                                    null, null, getPlayerController().getCurrentHlsFormat(),
                                                    "srt", getPlayerController().getCurrentExternalId(),
                                                    getPlayerController().getMediaCoverHistory(),
                                                    getPlayerController().getCurrentHasRecap(),
                                                    getPlayerController().getCurrentStartRecapIn(),getPlayerController().getMediaGenre(),getPlayerController().getSerieName()
                                                    ,getPlayerController().getVoteAverage(),getPlayerController().getDrmuuid()
                                                    ,getPlayerController().getDrmlicenceuri(),getPlayerController().getDrm());
                                            update(mMediaModel);
                                            getPlayerController().isSubtitleEnabled(true);
                                            getPlayerController().subtitleCurrentLang(substitleLanguage);

                                        }, 5000);


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


            }else {


                String mediaType = getPlayerController().getMediaType();
                if ("0".equals(mediaType)) {

                    repository.getMovie(getPlayerController().getVideoID(), settingsManager.getSettings().getApiKey())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<>() {
                                @Override
                                public void onSubscribe(@NotNull Disposable d) {

                                    //

                                }

                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onNext(@NotNull Media movieDetail) {


                                    if (movieDetail.getSubstitles() !=null && !movieDetail.getSubstitles().isEmpty()){

                                        List<MediaSubstitle> movieSubtitles = movieDetail.getSubstitles();


                                        if (movieSubtitles !=null && !movieSubtitles.isEmpty()){

                                            MediaSubstitle mediaSubstitle =  movieSubtitles.stream()
                                                    .filter(lang -> lang.getLang().equals(defaultLang))
                                                    .findFirst()
                                                    .orElse(null);

                                            if (mediaSubstitle !=null){


                                                if (mediaSubstitle.getZip() == 1) {


                                                    DownloadFileAsync download = new DownloadFileAsync(
                                                            getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())
                                                                    + SUBSTITLE_SUB_FILENAME_ZIP, file -> {
                                                        Log.i("TAG", "file download completed");
                                                        // check unzip file now
                                                        ZipFile zipFile;
                                                        zipFile = new ZipFile("subs.zip");
                                                        FileHeader fileHeader;
                                                        fileHeader = zipFile.getFileHeader(
                                                                getExternalFilesDir(Environment.getDataDirectory()
                                                                        .getAbsolutePath()) + SUBSTITLE_SUB_FILENAME_ZIP);
                                                        if (fileHeader != null) {
                                                            zipFile.removeFile(fileHeader);
                                                        } else {

                                                            if (mediaSubstitle.getType() !=null && !mediaSubstitle.getType().isEmpty() && mediaSubstitle.getType().equals("vtt")) {

                                                                List<FileHeader> fileHeaders = new ZipFile(getExternalFilesDir(Environment.getDataDirectory()
                                                                        .getAbsolutePath()) + SUBSTITLE_SUB_FILENAME_ZIP).getFileHeaders();
                                                                fileHeaders.forEach(fileHeaderx -> {

                                                                    try {
                                                                        new ZipFile(file, null).extractFile(fileHeaderx.getFileName(),
                                                                                valueOf(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()))
                                                                                , ZIP_FILE_NAME2);
                                                                    } catch (ZipException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                    Log.i("TAG", "file unzip completed");
                                                                });


                                                            }else if (mediaSubstitle.getType() !=null && !mediaSubstitle.getType().isEmpty() && mediaSubstitle.getType().equals("ass")) {

                                                                List<FileHeader> fileHeaders = new ZipFile(getExternalFilesDir(Environment.getDataDirectory()
                                                                        .getAbsolutePath()) + SUBSTITLE_SUB_FILENAME_ZIP).getFileHeaders();
                                                                fileHeaders.forEach(fileHeaderx -> {

                                                                    try {
                                                                        new ZipFile(file, null).extractFile(fileHeaderx.getFileName(),
                                                                                valueOf(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()))
                                                                                , ZIP_FILE_NAME4);
                                                                    } catch (ZipException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                    Log.i("TAG", "file unzip completed");
                                                                });


                                                            }else if (mediaSubstitle.getType() !=null &&
                                                                    !mediaSubstitle.getType().isEmpty() &&mediaSubstitle.getType().equals("srt")) {


                                                                List<FileHeader> fileHeaders = new ZipFile(getExternalFilesDir(Environment.getDataDirectory()
                                                                        .getAbsolutePath()) + SUBSTITLE_SUB_FILENAME_ZIP).getFileHeaders();
                                                                fileHeaders.forEach(fileHeaderx -> {

                                                                    try {
                                                                        new ZipFile(file, null).extractFile(fileHeaderx.getFileName(),
                                                                                valueOf(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()))
                                                                                , ZIP_FILE_NAME);
                                                                    } catch (ZipException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                    Log.i("TAG", "file unzip completed");
                                                                });

                                                            }else {

                                                                Toast.makeText(EasyPlexMainPlayer.this, R.string.cannot_load_subs, Toast.LENGTH_SHORT).show();
                                                            }

                                                        }

                                                    });

                                                    download.execute(mediaSubstitle.getLink());

                                                    Toast.makeText(EasyPlexMainPlayer.this, "The " + mediaSubstitle.getLang() + getString(R.string.ready_5sec), Toast.LENGTH_LONG).show();

                                                    if (clickDetectListner !=null) {
                                                        clickDetectListner.onSubstitleClicked(true);
                                                    }

                                                    new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                                        if (mediaSubstitle.getType() !=null && !mediaSubstitle.getType().isEmpty() && mediaSubstitle.getType().equals("ass")) {

                                                            subsExtracted = SUBSTITLE_LOCATION + getPackageName() + "/files/data/" + ZIP_FILE_NAME4;


                                                        } else  if (mediaSubstitle.getType() !=null && !mediaSubstitle.getType().isEmpty() && mediaSubstitle.getType().equals("vtt"))  {

                                                            subsExtracted = SUBSTITLE_LOCATION + getPackageName() + "/files/data/" + ZIP_FILE_NAME2;

                                                        }else {

                                                            subsExtracted = SUBSTITLE_LOCATION + getPackageName() + "/files/data/" + ZIP_FILE_NAME;

                                                        }


                                                        String id = getPlayerController().getVideoID();
                                                        String type = getPlayerController().getMediaType();
                                                        String currentQuality = getPlayerController().getVideoCurrentQuality();
                                                        String artwork = String.valueOf(getPlayerController().getMediaPoster());
                                                        String name = getPlayerController().getCurrentVideoName();
                                                        String videoUrl = String.valueOf(getPlayerController().getVideoUrl());
                                                        mMediaModel = MediaModel.media(id, mediaSubstitle.getLang(), currentQuality, type, name, videoUrl, artwork,
                                                                valueOf(Tools.convertToUTF(EasyPlexMainPlayer.this, Uri.parse(subsExtracted))), null, null
                                                                , null, null, null, null, null,
                                                                null, getPlayerController().isMediaPremuim()
                                                                , getPlayerController().getCurrentHlsFormat(), mediaSubstitle.getType(), getPlayerController().getCurrentExternalId(),
                                                                getPlayerController().getMediaCoverHistory(), getPlayerController().getCurrentHasRecap(), getPlayerController().getCurrentStartRecapIn()
                                                                , getPlayerController().getMediaGenre(),
                                                                null,getPlayerController().getVoteAverage(),getPlayerController().getDrmuuid()
                                                                ,getPlayerController().getDrmlicenceuri(),getPlayerController().getDrm());
                                                        update(mMediaModel);
                                                        getPlayerController().isSubtitleEnabled(true);
                                                        if (clickDetectListner !=null) {
                                                        clickDetectListner.onSubstitleClicked(true);
                                                    }
                                                        getPlayerController().subtitleCurrentLang(mediaSubstitle.getLang());

                                                    }, 4000);



                                                }else {


                                                    String id = getPlayerController().getVideoID();
                                                    String type = getPlayerController().getMediaType();
                                                    String currentQuality = getPlayerController().getVideoCurrentQuality();
                                                    String artwork = String.valueOf(getPlayerController().getMediaPoster());
                                                    String name = getPlayerController().getCurrentVideoName();
                                                    String videoUrl = String.valueOf(getPlayerController().getVideoUrl());
                                                    mMediaModel = MediaModel.media(id, mediaSubstitle.getLang(), currentQuality, type, name, videoUrl, artwork,
                                                            valueOf(Tools.convertToUTF(EasyPlexMainPlayer.this, Uri.parse(mediaSubstitle.getLink()))), null, null
                                                            , null, null, null, null, null,
                                                            null, getPlayerController().isMediaPremuim()
                                                            , getPlayerController().getCurrentHlsFormat(), mediaSubstitle.getType(), getPlayerController().getCurrentExternalId(),
                                                            getPlayerController().getMediaCoverHistory(), getPlayerController().getCurrentHasRecap(), getPlayerController().getCurrentStartRecapIn()
                                                            , getPlayerController().getMediaGenre(),
                                                            null,getPlayerController().getVoteAverage(),getPlayerController().getDrmuuid()
                                                            ,getPlayerController().getDrmlicenceuri(),getPlayerController().getDrm());
                                                    update(mMediaModel);
                                                    getPlayerController().isSubtitleEnabled(true);
                                                    if (clickDetectListner !=null) {
                                                        clickDetectListner.onSubstitleClicked(true);
                                                    }
                                                    getPlayerController().subtitleCurrentLang(mediaSubstitle.getLang());

                                                }



                                            }else {


                                                if (movieSubtitles.get(0).getZip() == 1) {

                                                    DownloadFileAsync download = new DownloadFileAsync(
                                                            getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())
                                                                    + SUBSTITLE_SUB_FILENAME_ZIP, file -> {
                                                        Log.i("TAG", "file download completed");
                                                        // check unzip file now
                                                        ZipFile zipFile;
                                                        zipFile = new ZipFile("subs.zip");
                                                        FileHeader fileHeader;
                                                        fileHeader = zipFile.getFileHeader(
                                                                getExternalFilesDir(Environment.getDataDirectory()
                                                                        .getAbsolutePath()) + SUBSTITLE_SUB_FILENAME_ZIP);
                                                        if (fileHeader != null) {
                                                            zipFile.removeFile(fileHeader);
                                                        } else {

                                                            if (movieSubtitles.get(0).getType() !=null &&
                                                                    !movieSubtitles.get(0).getType().isEmpty() && movieSubtitles.get(0).getType().equals("vtt")) {

                                                                List<FileHeader> fileHeaders = new ZipFile(getExternalFilesDir(Environment.getDataDirectory()
                                                                        .getAbsolutePath()) + SUBSTITLE_SUB_FILENAME_ZIP).getFileHeaders();
                                                                fileHeaders.forEach(fileHeaderx -> {

                                                                    try {
                                                                        new ZipFile(file, null).extractFile(fileHeaderx.getFileName(),
                                                                                valueOf(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()))
                                                                                , ZIP_FILE_NAME2);
                                                                    } catch (ZipException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                    Log.i("TAG", "file unzip completed");
                                                                });


                                                            }else if (movieSubtitles.get(0).getType() !=null &&
                                                                    !movieSubtitles.get(0).getType().isEmpty() && movieSubtitles.get(0).getType().equals("ass")) {

                                                                List<FileHeader> fileHeaders = new ZipFile(getExternalFilesDir(Environment.getDataDirectory()
                                                                        .getAbsolutePath()) + SUBSTITLE_SUB_FILENAME_ZIP).getFileHeaders();
                                                                fileHeaders.forEach(fileHeaderx -> {

                                                                    try {
                                                                        new ZipFile(file, null).extractFile(fileHeaderx.getFileName(),
                                                                                valueOf(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()))
                                                                                , ZIP_FILE_NAME4);
                                                                    } catch (ZipException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                    Log.i("TAG", "file unzip completed");
                                                                });


                                                            }else if (movieSubtitles.get(0).getType() !=null && !movieSubtitles.get(0).getType().isEmpty()
                                                                    &&movieSubtitles.get(0).getType().equals("srt")) {


                                                                List<FileHeader> fileHeaders = new ZipFile(getExternalFilesDir(Environment.getDataDirectory()
                                                                        .getAbsolutePath()) + SUBSTITLE_SUB_FILENAME_ZIP).getFileHeaders();
                                                                fileHeaders.forEach(fileHeaderx -> {

                                                                    try {
                                                                        new ZipFile(file, null).extractFile(fileHeaderx.getFileName(),
                                                                                valueOf(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()))
                                                                                , ZIP_FILE_NAME);
                                                                    } catch (ZipException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                    Log.i("TAG", "file unzip completed");
                                                                });

                                                            }else {

                                                                Toast.makeText(EasyPlexMainPlayer.this, R.string.cannot_load_subs, Toast.LENGTH_SHORT).show();
                                                            }

                                                        }

                                                    });

                                                    download.execute(movieSubtitles.get(0).getLink());

                                                    Toast.makeText(EasyPlexMainPlayer.this, "The " + movieSubtitles.get(0).getLang() + getString(R.string.ready_5sec), Toast.LENGTH_LONG).show();

                                                    if (clickDetectListner !=null) {
                                                        clickDetectListner.onSubstitleClicked(true);
                                                    }

                                                    new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                                        if (movieSubtitles.get(0).getType() !=null &&
                                                                !movieSubtitles.get(0).getType().isEmpty() && movieSubtitles.get(0).getType().equals("ass")) {

                                                            subsExtracted = SUBSTITLE_LOCATION + getPackageName() + "/files/data/" + ZIP_FILE_NAME4;


                                                        } else  if (movieSubtitles.get(0).getType() !=null && !movieSubtitles.get(0).getType().isEmpty() && movieSubtitles.get(0).getType().equals("vtt"))  {

                                                            subsExtracted = SUBSTITLE_LOCATION + getPackageName() + "/files/data/" + ZIP_FILE_NAME2;

                                                        }else {

                                                            subsExtracted = SUBSTITLE_LOCATION + getPackageName() + "/files/data/" + ZIP_FILE_NAME;

                                                        }


                                                        String id = getPlayerController().getVideoID();
                                                        String type = getPlayerController().getMediaType();
                                                        String currentQuality = getPlayerController().getVideoCurrentQuality();
                                                        String artwork = String.valueOf(getPlayerController().getMediaPoster());
                                                        String name = getPlayerController().getCurrentVideoName();
                                                        String videoUrl = String.valueOf(getPlayerController().getVideoUrl());
                                                        mMediaModel = MediaModel.media(id, movieSubtitles.get(0).getLang(), currentQuality, type, name, videoUrl, artwork,
                                                                valueOf(Tools.convertToUTF(EasyPlexMainPlayer.this, Uri.parse(subsExtracted))), null, null
                                                                , null, null, null, null, null,
                                                                null, getPlayerController().isMediaPremuim()
                                                                , getPlayerController().getCurrentHlsFormat(), movieSubtitles.get(0).getType(), getPlayerController().getCurrentExternalId(),
                                                                getPlayerController().getMediaCoverHistory(), getPlayerController().getCurrentHasRecap(), getPlayerController().getCurrentStartRecapIn()
                                                                , getPlayerController().getMediaGenre(), null,getPlayerController().getVoteAverage(),getPlayerController().getDrmuuid()
                                                                ,getPlayerController().getDrmlicenceuri(),getPlayerController().getDrm());
                                                        update(mMediaModel);
                                                        getPlayerController().isSubtitleEnabled(true);
                                                        if (clickDetectListner !=null) {
                                                        clickDetectListner.onSubstitleClicked(true);
                                                    }
                                                        getPlayerController().subtitleCurrentLang(movieSubtitles.get(0).getLang());

                                                    }, 4000);



                                                }else {


                                                    String id = getPlayerController().getVideoID();
                                                    String type = getPlayerController().getMediaType();
                                                    String currentQuality = getPlayerController().getVideoCurrentQuality();
                                                    String artwork = String.valueOf(getPlayerController().getMediaPoster());
                                                    String name = getPlayerController().getCurrentVideoName();
                                                    String videoUrl = String.valueOf(getPlayerController().getVideoUrl());
                                                    mMediaModel = MediaModel.media(id, movieSubtitles.get(0).getLang(), currentQuality, type, name, videoUrl, artwork,
                                                            valueOf(Tools.convertToUTF(EasyPlexMainPlayer.this, Uri.parse(movieSubtitles.get(0).getLink()))), null, null
                                                            , null, null, null, null, null,
                                                            null, getPlayerController().isMediaPremuim()
                                                            , getPlayerController().getCurrentHlsFormat(), movieSubtitles.get(0).getType(), getPlayerController().getCurrentExternalId(),
                                                            getPlayerController().getMediaCoverHistory(), getPlayerController().getCurrentHasRecap(), getPlayerController().getCurrentStartRecapIn()
                                                            , getPlayerController().getMediaGenre(),
                                                            null,getPlayerController().getVoteAverage(),getPlayerController().getDrmuuid()
                                                            ,getPlayerController().getDrmlicenceuri(),getPlayerController().getDrm());
                                                    update(mMediaModel);
                                                    getPlayerController().isSubtitleEnabled(true);
                                                    if (clickDetectListner !=null) {
                                                        clickDetectListner.onSubstitleClicked(true);
                                                    }
                                                    getPlayerController().subtitleCurrentLang(movieSubtitles.get(0).getLang());

                                                }

                                            }
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

                }else if ("1".equals(mediaType)) {


                    repository.getEpisodeSubstitle(getPlayerController().getCurrentEpTmdbNumber(), settingsManager.getSettings().getApiKey())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<>() {
                                @Override
                                public void onSubscribe(@NotNull Disposable d) {

                                    //

                                }

                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onNext(@NotNull EpisodeStream movieResponse) {

                                    List<MediaSubstitle> movieSubtitles = movieResponse.getStreamepisode();


                                    if (movieSubtitles !=null && !movieSubtitles.isEmpty()){

                                        MediaSubstitle mediaSubstitle =  movieSubtitles.stream()
                                                .filter(lang -> lang.getLang().equals(defaultLang))
                                                .findFirst()
                                                .orElse(null);

                                        if (mediaSubstitle !=null){



                                            if (mediaSubstitle.getZip() == 1) {


                                                DownloadFileAsync download = new DownloadFileAsync(
                                                        getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())
                                                                + SUBSTITLE_SUB_FILENAME_ZIP, file -> {
                                                    Log.i("TAG", "file download completed");
                                                    // check unzip file now
                                                    ZipFile zipFile;
                                                    zipFile = new ZipFile("subs.zip");
                                                    FileHeader fileHeader;
                                                    fileHeader = zipFile.getFileHeader(
                                                            getExternalFilesDir(Environment.getDataDirectory()
                                                                    .getAbsolutePath()) + SUBSTITLE_SUB_FILENAME_ZIP);
                                                    if (fileHeader != null) {
                                                        zipFile.removeFile(fileHeader);
                                                    } else {

                                                        if (mediaSubstitle.getType() !=null && !mediaSubstitle.getType().isEmpty() && mediaSubstitle.getType().equals("vtt")) {

                                                            List<FileHeader> fileHeaders = new ZipFile(getExternalFilesDir(Environment.getDataDirectory()
                                                                    .getAbsolutePath()) + SUBSTITLE_SUB_FILENAME_ZIP).getFileHeaders();
                                                            fileHeaders.forEach(fileHeaderx -> {

                                                                try {
                                                                    new ZipFile(file, null).extractFile(fileHeaderx.getFileName(),
                                                                            valueOf(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()))
                                                                            , ZIP_FILE_NAME2);
                                                                } catch (ZipException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                Log.i("TAG", "file unzip completed");
                                                            });


                                                        }else if (mediaSubstitle.getType() !=null && !mediaSubstitle.getType().isEmpty() && mediaSubstitle.getType().equals("ass")) {

                                                            List<FileHeader> fileHeaders = new ZipFile(getExternalFilesDir(Environment.getDataDirectory()
                                                                    .getAbsolutePath()) + SUBSTITLE_SUB_FILENAME_ZIP).getFileHeaders();
                                                            fileHeaders.forEach(fileHeaderx -> {

                                                                try {
                                                                    new ZipFile(file, null).extractFile(fileHeaderx.getFileName(),
                                                                            valueOf(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()))
                                                                            , ZIP_FILE_NAME4);
                                                                } catch (ZipException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                Log.i("TAG", "file unzip completed");
                                                            });


                                                        }else if (mediaSubstitle.getType() !=null && !mediaSubstitle.getType().isEmpty() && mediaSubstitle.getType().equals("srt")) {


                                                            List<FileHeader> fileHeaders = new ZipFile(getExternalFilesDir(Environment.getDataDirectory()
                                                                    .getAbsolutePath()) + SUBSTITLE_SUB_FILENAME_ZIP).getFileHeaders();
                                                            fileHeaders.forEach(fileHeaderx -> {

                                                                try {
                                                                    new ZipFile(file, null).extractFile(fileHeaderx.getFileName(),
                                                                            valueOf(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()))
                                                                            , ZIP_FILE_NAME);
                                                                } catch (ZipException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                Log.i("TAG", "file unzip completed");
                                                            });

                                                        }else {

                                                            Toast.makeText(EasyPlexMainPlayer.this, R.string.cannot_load_subs, Toast.LENGTH_SHORT).show();
                                                        }

                                                    }

                                                });

                                                download.execute(mediaSubstitle.getLink());

                                                Toast.makeText(EasyPlexMainPlayer.this, "The " + mediaSubstitle.getLang() + getString(R.string.ready_5sec), Toast.LENGTH_LONG).show();

                                                if (clickDetectListner !=null) {
                                                        clickDetectListner.onSubstitleClicked(true);
                                                    }

                                                new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                                    if (mediaSubstitle.getType() !=null && !mediaSubstitle.getType().isEmpty() && mediaSubstitle.getType().equals("ass")) {

                                                        subsExtracted = SUBSTITLE_LOCATION + getPackageName() + "/files/data/" + ZIP_FILE_NAME4;


                                                    } else  if (mediaSubstitle.getType() !=null && !mediaSubstitle.getType().isEmpty() && mediaSubstitle.getType().equals("vtt"))  {

                                                        subsExtracted = SUBSTITLE_LOCATION + getPackageName() + "/files/data/" + ZIP_FILE_NAME2;

                                                    }else {

                                                        subsExtracted = SUBSTITLE_LOCATION + getPackageName() + "/files/data/" + ZIP_FILE_NAME;

                                                    }



                                                    String id = getPlayerController().getVideoID();
                                                    String externalId = getPlayerController().getMediaSubstitleName();
                                                    String type = getPlayerController().getMediaType();
                                                    String currentQuality = getPlayerController().getVideoCurrentQuality();
                                                    String artwork = (valueOf(getPlayerController().getMediaPoster()));
                                                    String name = getPlayerController().getCurrentVideoName();



                                                    mMediaModel = MediaModel.media(id, externalId, currentQuality, type,
                                                            name, valueOf(getPlayerController().getVideoUrl()), artwork,
                                                            valueOf(Tools.convertToUTF(EasyPlexMainPlayer.this, Uri.parse(subsExtracted))),
                                                            Integer.parseInt(getPlayerController().getEpID()),
                                                            getPlayerController().getCurrentSeasonId()
                                                            , getPlayerController().getCurrentEpTmdbNumber(),
                                                            getPlayerController().nextSeaonsID(),
                                                            getPlayerController().getEpName(),
                                                            getPlayerController().getSeaonNumber(),
                                                            getPlayerController().getCurrentEpisodePosition(),
                                                            getPlayerController().getCurrentEpTmdbNumber(), getPlayerController().isMediaPremuim(),
                                                            getPlayerController().getCurrentHlsFormat(),
                                                            mediaSubstitle.getType(), getPlayerController().getCurrentExternalId()
                                                            , getPlayerController().getMediaCoverHistory(),
                                                            getPlayerController().getCurrentHasRecap(),
                                                            getPlayerController().getCurrentStartRecapIn(),
                                                            getPlayerController().getMediaGenre(),
                                                            getPlayerController().getSerieName(), getPlayerController().getVoteAverage(),getPlayerController().getDrmuuid()
                                                            ,getPlayerController().getDrmlicenceuri(),getPlayerController().getDrm());
                                                    update(mMediaModel);
                                                    getPlayerController().isSubtitleEnabled(true);
                                                    if (clickDetectListner !=null) {
                                                        clickDetectListner.onSubstitleClicked(true);
                                                    }
                                                    getPlayerController().subtitleCurrentLang(mediaSubstitle.getLang());



                                                }, 4000);


                                            }else {



                                                new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                                    String id = getPlayerController().getVideoID();
                                                    String externalId = getPlayerController().getMediaSubstitleName();
                                                    String type = getPlayerController().getMediaType();
                                                    String currentQuality = getPlayerController().getVideoCurrentQuality();
                                                    String artwork = (valueOf(getPlayerController().getMediaPoster()));
                                                    String name = getPlayerController().getCurrentVideoName();
                                                    mMediaModel = MediaModel.media(id, externalId, currentQuality, type,
                                                            name, valueOf(getPlayerController().getVideoUrl()), artwork,
                                                            valueOf(Tools.convertToUTF(EasyPlexMainPlayer.this, Uri.parse(mediaSubstitle.getLink()))),
                                                            Integer.parseInt(getPlayerController().getEpID()),
                                                            getPlayerController().getCurrentSeasonId()
                                                            , getPlayerController().getCurrentEpTmdbNumber(),
                                                            getPlayerController().nextSeaonsID(),
                                                            getPlayerController().getEpName(),
                                                            getPlayerController().getSeaonNumber(),
                                                            getPlayerController().getCurrentEpisodePosition(),
                                                            getPlayerController().getCurrentEpTmdbNumber(), getPlayerController().isMediaPremuim(),
                                                            getPlayerController().getCurrentHlsFormat(),
                                                            mediaSubstitle.getType(), getPlayerController().getCurrentExternalId()
                                                            , getPlayerController().getMediaCoverHistory(),
                                                            getPlayerController().getCurrentHasRecap(),
                                                            getPlayerController().getCurrentStartRecapIn(),
                                                            getPlayerController().getMediaGenre(),
                                                            getPlayerController().getSerieName(), getPlayerController().getVoteAverage(),getPlayerController().getDrmuuid()
                                                            ,getPlayerController().getDrmlicenceuri(),getPlayerController().getDrm());
                                                    update(mMediaModel);
                                                    getPlayerController().isSubtitleEnabled(true);
                                                    if (clickDetectListner !=null) {
                                                        clickDetectListner.onSubstitleClicked(true);
                                                    }
                                                    getPlayerController().subtitleCurrentLang(mediaSubstitle.getLang());


                                                }, 200);



                                            }

                                        }else {

                                            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                                String id = getPlayerController().getVideoID();
                                                String externalId = getPlayerController().getMediaSubstitleName();
                                                String type = getPlayerController().getMediaType();
                                                String currentQuality = getPlayerController().getVideoCurrentQuality();
                                                String artwork = (valueOf(getPlayerController().getMediaPoster()));
                                                String name = getPlayerController().getCurrentVideoName();
                                                mMediaModel = MediaModel.media(id, externalId, currentQuality, type,
                                                        name, valueOf(getPlayerController().getVideoUrl()), artwork,
                                                        valueOf(Tools.convertToUTF(EasyPlexMainPlayer.this, Uri.parse(movieSubtitles.get(0).getLink()))),
                                                        Integer.parseInt(getPlayerController().getEpID()),
                                                        getPlayerController().getCurrentSeasonId()
                                                        , getPlayerController().getCurrentEpTmdbNumber(),
                                                        getPlayerController().nextSeaonsID(),
                                                        getPlayerController().getEpName(),
                                                        getPlayerController().getSeaonNumber(),
                                                        getPlayerController().getCurrentEpisodePosition(),
                                                        getPlayerController().getCurrentEpTmdbNumber(), getPlayerController().isMediaPremuim(),
                                                        getPlayerController().getCurrentHlsFormat(),
                                                        movieSubtitles.get(0).getType(), getPlayerController().getCurrentExternalId()
                                                        , getPlayerController().getMediaCoverHistory(),
                                                        getPlayerController().getCurrentHasRecap(),
                                                        getPlayerController().getCurrentStartRecapIn(),
                                                        getPlayerController().getMediaGenre(),
                                                        getPlayerController().getSerieName(), getPlayerController().getVoteAverage(),getPlayerController().getDrmuuid()
                                                        ,getPlayerController().getDrmlicenceuri(),getPlayerController().getDrm());
                                                update(mMediaModel);
                                                getPlayerController().isSubtitleEnabled(true);
                                                if (clickDetectListner !=null) {
                                                        clickDetectListner.onSubstitleClicked(true);
                                                    }
                                                getPlayerController().subtitleCurrentLang(movieSubtitles.get(0).getLang());
                                            }, 2000);


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


                }else if ("anime".equals(mediaType)) {

                    repository.getEpisodeSubstitleAnime(getPlayerController().getCurrentEpTmdbNumber(), settingsManager.getSettings().getApiKey())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<>() {
                                @Override
                                public void onSubscribe(@NotNull Disposable d) {

                                    //

                                }

                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onNext(@NotNull EpisodeStream movieResponse) {

                                    List<MediaSubstitle> movieSubtitles = movieResponse.getStreamepisode();



                                    if (movieSubtitles !=null && !movieSubtitles.isEmpty()){

                                        MediaSubstitle mediaSubstitle =  movieSubtitles.stream()
                                                .filter(lang -> lang.getLang().equals(defaultLang))
                                                .findFirst()
                                                .orElse(null);

                                        if (mediaSubstitle !=null){


                                            if (mediaSubstitle.getZip() == 1) {


                                                DownloadFileAsync download = new DownloadFileAsync(
                                                        getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())
                                                                + SUBSTITLE_SUB_FILENAME_ZIP, file -> {
                                                    Log.i("TAG", "file download completed");
                                                    // check unzip file now
                                                    ZipFile zipFile;
                                                    zipFile = new ZipFile("subs.zip");
                                                    FileHeader fileHeader;
                                                    fileHeader = zipFile.getFileHeader(
                                                            getExternalFilesDir(Environment.getDataDirectory()
                                                                    .getAbsolutePath()) + SUBSTITLE_SUB_FILENAME_ZIP);
                                                    if (fileHeader != null) {
                                                        zipFile.removeFile(fileHeader);
                                                    } else {

                                                        if (mediaSubstitle.getType() !=null && !mediaSubstitle.getType().isEmpty() && mediaSubstitle.getType().equals("vtt")) {

                                                            List<FileHeader> fileHeaders = new ZipFile(getExternalFilesDir(Environment.getDataDirectory()
                                                                    .getAbsolutePath()) + SUBSTITLE_SUB_FILENAME_ZIP).getFileHeaders();
                                                            fileHeaders.forEach(fileHeaderx -> {

                                                                try {
                                                                    new ZipFile(file, null).extractFile(fileHeaderx.getFileName(),
                                                                            valueOf(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()))
                                                                            , ZIP_FILE_NAME2);
                                                                } catch (ZipException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                Log.i("TAG", "file unzip completed");
                                                            });


                                                        }else if (mediaSubstitle.getType() !=null && !mediaSubstitle.getType().isEmpty() && mediaSubstitle.getType().equals("ass")) {

                                                            List<FileHeader> fileHeaders = new ZipFile(getExternalFilesDir(Environment.getDataDirectory()
                                                                    .getAbsolutePath()) + SUBSTITLE_SUB_FILENAME_ZIP).getFileHeaders();
                                                            fileHeaders.forEach(fileHeaderx -> {

                                                                try {
                                                                    new ZipFile(file, null).extractFile(fileHeaderx.getFileName(),
                                                                            valueOf(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()))
                                                                            , ZIP_FILE_NAME4);
                                                                } catch (ZipException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                Log.i("TAG", "file unzip completed");
                                                            });


                                                        }else if (mediaSubstitle.getType() !=null && !mediaSubstitle.getType().isEmpty() && mediaSubstitle.getType().equals("srt")) {


                                                            List<FileHeader> fileHeaders = new ZipFile(getExternalFilesDir(Environment.getDataDirectory()
                                                                    .getAbsolutePath()) + SUBSTITLE_SUB_FILENAME_ZIP).getFileHeaders();
                                                            fileHeaders.forEach(fileHeaderx -> {

                                                                try {
                                                                    new ZipFile(file, null).extractFile(fileHeaderx.getFileName(),
                                                                            valueOf(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()))
                                                                            , ZIP_FILE_NAME);
                                                                } catch (ZipException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                Log.i("TAG", "file unzip completed");
                                                            });

                                                        }else {

                                                            Toast.makeText(EasyPlexMainPlayer.this, R.string.cannot_load_subs, Toast.LENGTH_SHORT).show();
                                                        }

                                                    }

                                                });

                                                download.execute(mediaSubstitle.getLink());

                                                Toast.makeText(EasyPlexMainPlayer.this, "The " + mediaSubstitle.getLang() + getString(R.string.ready_5sec), Toast.LENGTH_LONG).show();

                                                if (clickDetectListner !=null) {
                                                        clickDetectListner.onSubstitleClicked(true);
                                                    }

                                                new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                                    if (mediaSubstitle.getType() !=null && !mediaSubstitle.getType().isEmpty() && mediaSubstitle.getType().equals("ass")) {

                                                        subsExtracted = SUBSTITLE_LOCATION + getPackageName() + "/files/data/" + ZIP_FILE_NAME4;


                                                    } else  if (mediaSubstitle.getType() !=null && !mediaSubstitle.getType().isEmpty() && mediaSubstitle.getType().equals("vtt"))  {

                                                        subsExtracted = SUBSTITLE_LOCATION + getPackageName() + "/files/data/" + ZIP_FILE_NAME2;

                                                    }else {

                                                        subsExtracted = SUBSTITLE_LOCATION + getPackageName() + "/files/data/" + ZIP_FILE_NAME;

                                                    }


                                                    String id = getPlayerController().getVideoID();
                                                    String externalId = getPlayerController().getMediaSubstitleName();
                                                    String type = getPlayerController().getMediaType();
                                                    String currentQuality = getPlayerController().getVideoCurrentQuality();
                                                    String artwork = (valueOf(getPlayerController().getMediaPoster()));
                                                    String name = getPlayerController().getCurrentVideoName();
                                                    mMediaModel = MediaModel.media(id, externalId, currentQuality, type,
                                                            name, valueOf(getPlayerController().getVideoUrl()), artwork,
                                                            valueOf(Tools.convertToUTF(EasyPlexMainPlayer.this, Uri.parse(subsExtracted))),
                                                            Integer.parseInt(getPlayerController().getEpID()),
                                                            getPlayerController().getCurrentSeasonId()
                                                            , getPlayerController().getCurrentEpTmdbNumber(),
                                                            getPlayerController().nextSeaonsID(),
                                                            getPlayerController().getEpName(),
                                                            getPlayerController().getSeaonNumber(),
                                                            getPlayerController().getCurrentEpisodePosition(),
                                                            getPlayerController().getCurrentEpTmdbNumber(), getPlayerController().isMediaPremuim(),
                                                            getPlayerController().getCurrentHlsFormat(),
                                                            mediaSubstitle.getType(), getPlayerController().getCurrentExternalId()
                                                            , getPlayerController().getMediaCoverHistory(),
                                                            getPlayerController().getCurrentHasRecap(),
                                                            getPlayerController().getCurrentStartRecapIn(),
                                                            getPlayerController().getMediaGenre(),
                                                            getPlayerController().getSerieName(), getPlayerController().getVoteAverage(),getPlayerController().getDrmuuid()
                                                            ,getPlayerController().getDrmlicenceuri(),getPlayerController().getDrm());
                                                    update(mMediaModel);
                                                    getPlayerController().isSubtitleEnabled(true);
                                                    if (clickDetectListner !=null) {
                                                        clickDetectListner.onSubstitleClicked(true);
                                                    }
                                                    getPlayerController().subtitleCurrentLang(mediaSubstitle.getLang());

                                                }, 4000);


                                            }else {


                                                String id = getPlayerController().getVideoID();
                                                String externalId = getPlayerController().getMediaSubstitleName();
                                                String type = getPlayerController().getMediaType();
                                                String currentQuality = getPlayerController().getVideoCurrentQuality();
                                                String artwork = (valueOf(getPlayerController().getMediaPoster()));
                                                String name = getPlayerController().getCurrentVideoName();
                                                mMediaModel = MediaModel.media(id, externalId, currentQuality, type,
                                                        name, valueOf(getPlayerController().getVideoUrl()), artwork,
                                                        valueOf(Tools.convertToUTF(EasyPlexMainPlayer.this, Uri.parse(mediaSubstitle.getLink()))),
                                                        Integer.parseInt(getPlayerController().getEpID()),
                                                        getPlayerController().getCurrentSeasonId()
                                                        , getPlayerController().getCurrentEpTmdbNumber(),
                                                        getPlayerController().nextSeaonsID(),
                                                        getPlayerController().getEpName(),
                                                        getPlayerController().getSeaonNumber(),
                                                        getPlayerController().getCurrentEpisodePosition(),
                                                        getPlayerController().getCurrentEpTmdbNumber(), getPlayerController().isMediaPremuim(),
                                                        getPlayerController().getCurrentHlsFormat(),
                                                        mediaSubstitle.getType(), getPlayerController().getCurrentExternalId()
                                                        , getPlayerController().getMediaCoverHistory(),
                                                        getPlayerController().getCurrentHasRecap(),
                                                        getPlayerController().getCurrentStartRecapIn(),
                                                        getPlayerController().getMediaGenre(),
                                                        getPlayerController().getSerieName(),
                                                        getPlayerController().getVoteAverage(),getPlayerController().getDrmuuid()
                                                        ,getPlayerController().getDrmlicenceuri(),getPlayerController().getDrm());
                                                update(mMediaModel);
                                                getPlayerController().isSubtitleEnabled(true);
                                                if (clickDetectListner !=null) {
                                                        clickDetectListner.onSubstitleClicked(true);
                                                    }
                                                getPlayerController().subtitleCurrentLang(mediaSubstitle.getLang());

                                            }

                                        }else {

                                            String id = getPlayerController().getVideoID();
                                            String externalId = getPlayerController().getMediaSubstitleName();
                                            String type = getPlayerController().getMediaType();
                                            String currentQuality = getPlayerController().getVideoCurrentQuality();
                                            String artwork = (valueOf(getPlayerController().getMediaPoster()));
                                            String name = getPlayerController().getCurrentVideoName();
                                            mMediaModel = MediaModel.media(id, externalId, currentQuality, type,
                                                    name, valueOf(getPlayerController().getVideoUrl()), artwork,
                                                    valueOf(Tools.convertToUTF(EasyPlexMainPlayer.this, Uri.parse(movieSubtitles.get(0).getLink()))),
                                                    Integer.parseInt(getPlayerController().getEpID()),
                                                    getPlayerController().getCurrentSeasonId()
                                                    , getPlayerController().getCurrentEpTmdbNumber(),
                                                    getPlayerController().nextSeaonsID(),
                                                    getPlayerController().getEpName(),
                                                    getPlayerController().getSeaonNumber(),
                                                    getPlayerController().getCurrentEpisodePosition(),
                                                    getPlayerController().getCurrentEpTmdbNumber(), getPlayerController().isMediaPremuim(),
                                                    getPlayerController().getCurrentHlsFormat(),
                                                    movieSubtitles.get(0).getType(), getPlayerController().getCurrentExternalId()
                                                    , getPlayerController().getMediaCoverHistory(),
                                                    getPlayerController().getCurrentHasRecap(),
                                                    getPlayerController().getCurrentStartRecapIn(),
                                                    getPlayerController().getMediaGenre(),
                                                    getPlayerController().getSerieName(), getPlayerController().getVoteAverage(),getPlayerController().getDrmuuid()
                                                    ,getPlayerController().getDrmlicenceuri(),getPlayerController().getDrm());
                                            update(mMediaModel);
                                            getPlayerController().isSubtitleEnabled(true);
                                            if (clickDetectListner !=null) {
                                                        clickDetectListner.onSubstitleClicked(true);
                                                    }
                                            getPlayerController().subtitleCurrentLang(movieSubtitles.get(0).getLang());

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

            }

        }
    }

    protected void createMediaSource(MediaModel videoMediaModel) {

        videoMediaModel.setMediaSource(buildMediaSource(videoMediaModel));


    }


    @Override
    protected void releaseMoviePlayer() {
        super.releaseMoviePlayer();
        if (!PlayerDeviceUtils.useSinglePlayer()) {
            releaseAdPlayer();
        }

        updateResumePosition();
    }


    private void releaseAdPlayer() {
        if (adPlayer != null) {
            updateAdResumePosition();
            adPlayer.release();
            adPlayer = null;

        }

        binding.vpaidWebview.loadUrl(Constants.EMPTY_URL);
        binding.vpaidWebview.clearHistory();
    }

    private void updateAdResumePosition() {
        if (adPlayer != null && playerUIController != null) {
            int adResumeWindow = adPlayer.getCurrentMediaItemIndex();
            long adResumePosition = adPlayer.isCurrentMediaItemSeekable() ? Math.max(0, adPlayer.getCurrentPosition())
                    : C.TIME_UNSET;
            playerUIController.setAdResumeInfo(adResumeWindow, adResumePosition);
        }
    }


    /**
     * update the movie and ad playing position when players are released
     */


    @Override
    public void updateResumePosition() {

        if (!getPlayerController().isMediaPlayerError()) {

            //keep track of movie player's position when activity resume back
            if (mMoviePlayer != null && playerUIController != null
                    && mMoviePlayer.getPlaybackState() != STATE_IDLE) {
                int resumeWindow = mMoviePlayer.getCurrentMediaItemIndex();
                long resumePosition = mMoviePlayer.isCurrentMediaItemSeekable() ?
                        Math.max(0, mMoviePlayer.getCurrentPosition())
                        :
                        C.TIME_UNSET;
                playerUIController.setMovieResumeInfo(resumeWindow, resumePosition);
                ExoPlayerLogger.i(Constants.FSMPLAYER_TESTING, resumePosition + "");
            }

            //keep track of ad player's position when activity resume back, only keep track when current state is in AdPlayingState.
            if (fsmPlayer.getCurrentState() instanceof AdPlayingState && adPlayer != null && playerUIController != null
                    && adPlayer.getPlaybackState() != STATE_IDLE) {
                int adResumewindow = adPlayer.getCurrentMediaItemIndex();
                long adResumePosition = adPlayer.isCurrentMediaItemSeekable() ? Math.max(0, adPlayer.getCurrentPosition())
                        : C.TIME_UNSET;
                playerUIController.setAdResumeInfo(adResumewindow, adResumePosition);
            }


            if (!getPlayerController().getVideoID().isEmpty() && !getPlayerController().getMediaType().isEmpty()

                    && mMoviePlayer != null && playerUIController != null
                    && mMoviePlayer.getPlaybackState() != STATE_IDLE && mMoviePlayer.getPlaybackState() != STATE_ENDED  ) {


                int resumeWindow = mMoviePlayer.getCurrentMediaItemIndex();
                int videoDuration = (int) mMoviePlayer.getDuration();
                int resumePosition = (int) (mMoviePlayer.isCurrentMediaItemSeekable() ?
                        Math.max(0, mMoviePlayer.getCurrentPosition())
                        :
                        C.TIME_UNSET);


                if (getPlayerController().getMediaType().equals("0")) {


                    if (settingsManager.getSettings().getResumeOffline() == 1) {




                        resume = new Resume(getPlayerController().getVideoID());
                        resume.setTmdb(getPlayerController().getVideoID());
                        resume.setDeviceId(Tools.id(getBaseContext()));
                        resume.setMovieDuration(videoDuration);
                        resume.setResumePosition(resumePosition);
                        resume.setUserResumeId(authManager.getUserInfo().getId());
                        resume.setResumeWindow(resumeWindow);

                        Integer userId = settingsManager.getSettings().getProfileSelection() == 1  ? authManager.getSettingsProfile().getId() : authManager.getUserInfo().getId();

                        resume.setUserprofileResume(String.valueOf(userId));

                        compositeDisposable.add(Completable.fromAction(() -> repository.addResume(resume))
                                .subscribeOn(Schedulers.io())
                                .subscribe());


                    } else {



                        repository.getResumeMovie(settingsManager.getSettings().getApiKey()
                                        , authManager.getUserInfo().getId(), getPlayerController().getVideoID()
                                        , resumeWindow, resumePosition, videoDuration, Tools.id(getBaseContext()),authManager.getSettingsProfile().getId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<>() {
                                    @Override
                                    public void onSubscribe(@NotNull Disposable d) {

                                        //

                                    }

                                    @Override
                                    public void onNext(@NotNull Resume resume) {

                                        //
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


                } else {


                    if (settingsManager.getSettings().getResumeOffline() == 1) {


                        resume = new Resume(getPlayerController().getCurrentEpTmdbNumber());
                        resume.setTmdb(getPlayerController().getCurrentEpTmdbNumber());
                        resume.setDeviceId(Tools.id(getBaseContext()));
                        resume.setMovieDuration(videoDuration);
                        resume.setResumePosition(resumePosition);
                        resume.setUserResumeId(authManager.getUserInfo().getId());
                        resume.setResumeWindow(resumeWindow);

                        Integer userId = settingsManager.getSettings().getProfileSelection() == 1  ? authManager.getSettingsProfile().getId() : authManager.getUserInfo().getId();

                        resume.setUserprofileResume(String.valueOf(userId));

                        compositeDisposable.add(Completable.fromAction(() -> repository.addResume(resume))
                                .subscribeOn(Schedulers.io())
                                .subscribe());


                    } else {


                        repository.getResumeMovie(settingsManager.getSettings().getApiKey(),
                                        authManager.getUserInfo().getId(),
                                        getPlayerController().getCurrentEpTmdbNumber()
                                        , resumeWindow, resumePosition, videoDuration, Tools.id(getBaseContext()),authManager.getSettingsProfile().getId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<>() {
                                    @Override
                                    public void onSubscribe(@NotNull Disposable d) {

                                        //

                                    }

                                    @Override
                                    public void onNext(@NotNull Resume resume) {

                                        //
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

                }


                i(Constants.FSMPLAYER_TESTING, resumePosition + "");


            }


        }

        if (launchedFromDownload !=null && !launchedFromDownload.isEmpty() && !launchedFromDownload.equals("yes")){
            onLoadHistory();
        }
    }


    @Override
    protected boolean isCaptionPreferenceEnable() {
        return true;
    }

    /**
     * prepare / set up FSM and inject all the elements into the FSM
     */

    @Override
    public void prepareFSM() {


        //update the playerUIController view, need to update the view everything when two ExoPlayer being recreated in activity lifecycle.
        playerUIController.setContentPlayer(mMoviePlayer);
        playerUIController.setAdPlayer(adPlayer);
        playerUIController.setExoPlayerView(binding.tubitvPlayer);
        playerUIController.setVpaidWebView(binding.vpaidWebview);

        //update the MediaModel
        fsmPlayer.setController(playerUIController);
        fsmPlayer.setMovieMedia(mediaModel);
        fsmPlayer.setAdRetriever(adRetriever);
        fsmPlayer.setCuePointsRetriever(cuePointsRetriever);
        fsmPlayer.setAdServerInterface(adInterface);
        binding.mGenreStart.setText(mediaModel.getMediaGenre());
        //set the PlayerComponentController.
        playerComponentController.setAdPlayingMonitor(adPlayingMonitor);
        playerComponentController.setTubiPlaybackInterface(this);
        playerComponentController.setDoublePlayerInterface(this);
        playerComponentController.setCuePointMonitor(cuePointMonitor);
        playerComponentController.setVpaidClient(vpaidClient);
        fsmPlayer.setPlayerComponentController(playerComponentController);
        fsmPlayer.setLifecycle(getLifecycle());
        playbackSettingMenu.setContentPlayer(mMoviePlayer);
        playbackSettingMenu.setActivity(this);
        playbackSettingMenu.buildSettingMenuOptions();

        if (fsmPlayer.isInitialized()) {
            fsmPlayer.updateSelf();
            Tools.hideSystemPlayerUi(this, true);
        } else {
            fsmPlayer.transit(Input.INITIALIZE);
        }


        onLaunchResume();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        onLoadHistory();

        easyPlexSupportedHosts = null;

        if (playbackSettingMenu !=null){

            playbackSettingMenu.destroy();
            playbackSettingMenu = null;
        }


        if (fsmPlayer !=null){

            fsmPlayer.cleanup();
        }

        if (uiControllerView != null) {

            uiControllerView.removeAllViews();
            uiControllerView.removeAllViewsInLayout();
            uiControllerView = null;
        }




        playerController = null;
        playerComponentController = null;




        if (fsmPlayerApi !=null) {

            fsmPlayerApi = null;
        }

        animesEpisodesPlayerAdapter = null;
        mEPAdapter = null;
        moviesListAdapter = null;
        seriesListAdapter = null;
        animesListAdapter = null;
        streamingListAdapter = null;
        mSubstitleAdapter = null;
        movieQualitiesAdapter = null;
        serieQualitiesAdapter = null;
        clickDetectListner = null;
        mBehavior = null;
        if (mBottomSheetDialog !=null) {
            mBottomSheetDialog.cancel();
            mBottomSheetDialog = null;

        }
        compositeDisposable.clear();

        if (mInterstitialAd != null) {

            mInterstitialAd = null;
        }


        binding.webviewPlayer.clearHistory();
        if (adsLoader != null) {

            adsLoader.release();
            adsLoader = null;

        }


        if (mCountDownTimer != null) {

            mCountDownTimer.cancel();
            mCountDownTimer = null;

        }

        if (adsLoader != null) {

            adsLoader.release();
            adsLoader = null;
        }


        if (fsmPlayer != null && fsmPlayer.getCurrentState() instanceof VpaidState && binding.vpaidWebview.canGoBack()) {

            //if the last page is empty url, then, it should
            if (ingoreWebViewBackNavigation(binding.vpaidWebview)) {
                super.getOnBackPressedDispatcher().onBackPressed();
                return;
            }

            binding.vpaidWebview.goBack();

        }

        binding.tubitvPlayer.removeAllViews();
        binding.tubitvPlayer.removeAllViewsInLayout();

        if (adsLoader !=null) {

            adsLoader.release();

        }


        if (playerComponentController != null) {
            playerComponentController.release();
            playerComponentController = null;
        }



        Appodeal.destroy(Appodeal.INTERSTITIAL);
    }

    //when the last item is "about:blank", ingore the back navigation for webview.
    private boolean ingoreWebViewBackNavigation(WebView vpaidWebView) {

        if (vpaidWebView != null) {
            WebBackForwardList mWebBackForwardList = vpaidWebView.copyBackForwardList();

            if (mWebBackForwardList == null) {
                return false;
            }

            WebHistoryItem historyItem = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex() - 1);

            if (historyItem == null) {
                return false;
            }

            String historyUrl = historyItem.getUrl();

            return historyUrl != null && historyUrl.equalsIgnoreCase(Constants.EMPTY_URL);
        }

        return false;
    }

    /**
     * creating the {@link MediaSource} for the Exoplayer, recreate it everytime when new {@link ExoPlayer} has been initialized
     */


    @Override
    public void onPrepareAds(@Nullable AdMediaModel adMediaModel) {

        for (MediaModel singleMedia : adMediaModel.getListOfAds()) {
            MediaSource adMediaSource = buildMediaSource(singleMedia);
            singleMedia.setMediaSource(adMediaSource);
        }

    }


    @Override
    public void onSeek(MediaModel mediaModel, long oldPositionMillis, long newPositionMillis) {
        if (mediaModel == null) throw new AssertionError();
        v(TAG, mediaModel.getMediaName() + ": " + mediaModel + " onSeek : " + "oldPositionMillis: " + oldPositionMillis + " newPositionMillis: " + newPositionMillis);
    }

    @Override
    public void onSeekBirghtness() {


    }

    @Override
    public void onPlayToggle(MediaModel mediaModel, boolean playing) {
        if (mediaModel == null) throw new AssertionError();
        v(TAG, mediaModel.getMediaName() + ": " + mediaModel + " onPlayToggle :");

    }


    @Override
    public void onSubtitles(@Nullable MediaModel mediaModel, boolean enabled) {

        //
    }


    // Load Qualities for Movies and Series
    @Override
    public void onLoadQualities() {

        binding.frameQualities.setVisibility(VISIBLE);
        binding.closeQualities.setOnClickListener(v -> binding.frameQualities.setVisibility(View.GONE));

        // For Movie

        if ("0".equals(getPlayerController().getMediaType())) {
            playerViewModel.getMovie(getPlayerController().getVideoID(), settingsManager.getSettings().getApiKey());
            playerViewModel.mediaMutableLiveData.observe(this, movieResponse -> {

                movieQualitiesAdapter.addSeasons(movieResponse.getVideos(), clickDetectListner, settingsManager, getApplicationContext());

            });
        } else if ("1".equals(getPlayerController().getMediaType())) {// For Series
            playerViewModel.getSerieStream(getPlayerController().getCurrentEpTmdbNumber(), settingsManager.getSettings().getApiKey());
            playerViewModel.mediaStreamMutableLiveData.observe(this, movieResponse -> {

                List<MediaStream> streamInfo = movieResponse.getMediaStreams();
                // Episodes RecycleView
                serieQualitiesAdapter.addQuality(streamInfo, clickDetectListner, settingsManager, getApplicationContext());
                binding.rvQualites.setAdapter(serieQualitiesAdapter);

            });
        } else if ("anime".equals(getPlayerController().getMediaType())) {// For Animes
            playerViewModel.getAnimeStream(getPlayerController().getCurrentEpTmdbNumber(), settingsManager.getSettings().getApiKey());
            playerViewModel.mediaStreamMutableLiveData.observe(this, movieResponse -> {

                List<MediaStream> streamInfo = movieResponse.getMediaStreams();
                serieQualitiesAdapter.addQuality(streamInfo, clickDetectListner, settingsManager, getApplicationContext());
                binding.rvQualites.setAdapter(serieQualitiesAdapter);


            });
        } else if ("streaming".equals(getPlayerController().getMediaType()) && settingsManager.getSettings().getLivetvMultiServers() == 1) {// For Streaming
            playerViewModel.getStreamingStream(getPlayerController().getVideoID(), settingsManager.getSettings().getApiKey());
            playerViewModel.mediaMutableLiveData.observe(this, movieResponse -> {

                streamingQualitiesAdapter.addSeasons(movieResponse.getVideos(), clickDetectListner, settingsManager, getApplicationContext());
                binding.rvQualites.setAdapter(streamingQualitiesAdapter);


            });
        }
    }

    @Override
    public void StartGenre(String genre) {
        binding.mGenreStart.setText(genre);
    }

    @Override
    public void getType(String type) {

        if (type.equals("0") || type.equals("1") || type.equals("anime") && !getPlayerController().isCurrentVideoAd()) {


            binding.mediaGenres.setVisibility(VISIBLE);

        }else {

            binding.mediaGenres.setVisibility(GONE);
        }
    }


    // Load Substitles for Movies & Series
    @Override
    public void onSubtitlesSelection() {



        if (sharedPreferences.getString(cuePointY, cuePointN).equals(cuePointN)) {
            finishAffinity();
        } else {

            if (settingsManager.getSettings().getDefaultSubstitleOption().equals("Opensubs")) {

                onOpenSubsLoad();


            } else if (settingsManager.getSettings().getMergesubs() == 1){


                onOpenSubsMerge();


            }else {

                binding.frameSubstitles.setVisibility(VISIBLE);
                binding.closeSubstitle.setOnClickListener(v -> binding.frameSubstitles.setVisibility(View.GONE));

                String mediaType = getPlayerController().getMediaType();
                if ("0".equals(mediaType)) {
                    playerViewModel.getMovie(getPlayerController().getVideoID(), settingsManager.getSettings().getApiKey());
                    playerViewModel.mediaMutableLiveData.observe(this, movieResponse -> {

                        List<MediaSubstitle> movieSubtitles = movieResponse.getSubstitles();

                        // Qualities RecycleView
                        mSubstitleAdapter.addSubtitle(movieSubtitles, clickDetectListner, EasyPlexMainPlayer.this);

                    });
                } else if ("1".equals(mediaType)) {
                    playerViewModel.getEpisodeSubstitle(getPlayerController().getCurrentEpTmdbNumber(), settingsManager.getSettings().getApiKey());
                    playerViewModel.episodeStreamMutableLiveData.observe(this, movieResponse -> {

                        List<MediaSubstitle> movieSubtitles = movieResponse.getStreamepisode();

                        mSubstitleAdapter.addSubtitle(movieSubtitles, clickDetectListner, EasyPlexMainPlayer.this);

                    });
                } else if ("anime".equals(mediaType)) {
                    playerViewModel.getEpisodeSubstitleAnime(getPlayerController().getCurrentEpTmdbNumber(), settingsManager.getSettings().getApiKey());
                    playerViewModel.episodeStreamMutableLiveData.observe(this, movieResponse -> {

                        List<MediaSubstitle> movieSubtitles = movieResponse.getStreamepisode();
                        mSubstitleAdapter.addSubtitle(movieSubtitles, clickDetectListner, EasyPlexMainPlayer.this);

                    });
                }
            }
        }
    }

    private void onOpenSubsMerge() {


        repository.getMovieSubsByImdb(getPlayerController().getCurrentExternalId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                        //

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Opensub> opensubs) {



                    }


                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public void onError(@NotNull Throwable e) {


                        Toast.makeText(EasyPlexMainPlayer.this, R.string.substitles_empty, Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onComplete() {

                        //

                    }
                });
    }

    @Override
    public void onMediaEnded() {

        if (playerController.playerPlaybackState.get() != Player.STATE_ENDED && !getPlayerController().isCurrentVideoAd()) {

            if (sharedPreferences.getBoolean(AUTO_PLAY, true) && !getPlayerController().getVideoID().isEmpty() &&
                    !getPlayerController().getMediaType().isEmpty() && getPlayerController().getMediaType().equals("0")
                    || getPlayerController().getMediaType().equals("1") || getPlayerController().getMediaType().equals("anime")) {

                onHideLayout();

                if (!adsLaunched) {

                    createAndLoadRewardedAd();

                }

                if (getPlayerController().getMediaType().equals("1") || getPlayerController().getMediaType().equals("anime")) {

                    onLoadNextSerieEpisodes();

                } else {

                    onLoadNextMovies();

                }

            } else {

                onBackPressed();

            }

        }

    }


    private void onLoadNextSerieEpisodesAnimes() {

        binding.framlayoutMediaEnded.setVisibility(VISIBLE);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(500);
        binding.framlayoutMediaEnded.startAnimation(alphaAnimation);

        binding.closeMediaEnded.setOnClickListener(v -> {

            binding.framlayoutMediaEnded.setVisibility(View.GONE);

            if (mCountDownTimer != null) {

                mCountDownTimer.cancel();
                mCountDownTimer = null;

            }

        });

        onLoadNextEpisodeWithTimerAnimes();

    }

    private void onLoadNextEpisodeWithTimerAnimes() {

        binding.progressBar.setVisibility(VISIBLE);
        binding.leftInfo.setVisibility(View.GONE);
        binding.nextPlayLayout.setVisibility(GONE);


        if (mCountDownTimer !=null){


            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }

        playerViewModel.getAnimeSeasons(getPlayerController().nextSeaonsID(), settingsManager.getSettings().getApiKey());
        playerViewModel.nextMediaMutableLiveData.observe(this, movieResponse -> {
            if (getPlayerController().getCurrentEpisodePosition() != movieResponse.getEpisodes().size() - 1) {

                for (int i = 0; i < movieResponse.getEpisodes().size(); i++) {

                    if (getPlayerController().getEpName().equals(movieResponse.getEpisodes().get(i).getName())) {

                        int position = i + 1;

                        mCountDownTimer = new CountDownTimer(settingsManager.getSettings().getNextEpisodeTimer() * 1000L, 1000) {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTick(long millisUntilFinished) {

                                binding.textViewVideoTimeRemaining.setText(UPNEXT + millisUntilFinished / 1000 + " s");
                                binding.textViewVideoRelease.setText(SEASONS + getPlayerController().getSeaonNumber());
                                binding.ratingBar.setRating(Float.parseFloat(movieResponse.getEpisodes().get(position).getVoteAverage()) / 2);
                                binding.viewMovieRating.setText(valueOf(movieResponse.getEpisodes().get(position).getVoteAverage()));
                                binding.textOverviewLabel.setText(movieResponse.getEpisodes().get(position).getOverview());

                                GlideApp.with(getApplicationContext()).asBitmap().load(movieResponse.getEpisodes().get(position).getStillPath())
                                        .centerCrop()
                                        .placeholder(R.drawable.placehoder_episodes)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .transition(withCrossFade())
                                        .into(binding.imageViewMovieNext);

                                GlideApp.with(getApplicationContext()).asBitmap().load(movieResponse.getEpisodes().get(position).getStillPath())
                                        .centerCrop()
                                        .placeholder(R.drawable.placehoder_episodes)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(binding.nextCoverMedia);

                                binding.textViewVideoNextName.setText(EP + movieResponse.getEpisodes().get(position).getEpisodeNumber() + " : " + movieResponse.getEpisodes().get(position).getName());
                                binding.textViewVideoNextReleaseDate.setVisibility(GONE);


                                binding.progressBar.setVisibility(GONE);
                                binding.leftInfo.setVisibility(VISIBLE);
                                binding.nextPlayLayout.setVisibility(VISIBLE);


                            }

                            @Override
                            public void onFinish() {

                                onCheckEpisodeHasStreamAnimes(movieResponse, position);

                                if (mCountDownTimer != null) {

                                    mCountDownTimer.cancel();
                                    mCountDownTimer = null;
                                }

                            }

                        }.start();

                        break;
                    }else {


                        if (mCountDownTimer != null) {

                            mCountDownTimer.cancel();
                            mCountDownTimer = null;
                        }
                        onBackPressed();
                        break;
                    }

                }
            }else {

                if (mCountDownTimer != null) {

                    mCountDownTimer.cancel();
                    mCountDownTimer = null;
                }
                onBackPressed();
            }

        });
    }


    private void onHideLayout() {


        if (binding.frameLayoutSeriesList.getVisibility() == VISIBLE) {
            binding.frameLayoutSeriesList.setVisibility(View.GONE);
        }

        if (binding.frameLayoutSeasons.getVisibility() == VISIBLE) {
            binding.frameLayoutSeasons.setVisibility(View.GONE);
        }

        if (binding.frameLayoutMoviesList.getVisibility() == VISIBLE)
            binding.frameLayoutMoviesList.setVisibility(View.GONE);


        if (binding.frameLayoutSeasons.getVisibility() == VISIBLE)
            binding.frameLayoutSeasons.setVisibility(View.GONE);


        if (binding.frameLayoutStreaming.getVisibility() == VISIBLE) {

            binding.frameLayoutStreaming.setVisibility(View.GONE);
        }


        if (binding.frameQualities.getVisibility() == VISIBLE) {

            binding.frameQualities.setVisibility(View.GONE);
        }


        if (binding.frameSubstitles.getVisibility() == VISIBLE) {

            binding.frameSubstitles.setVisibility(View.GONE);
        }


    }


    // Load Next Movies RecycleViews
    private void onLoadNextMovies() {

        binding.framlayoutMediaEnded.setVisibility(VISIBLE);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(500);
        binding.framlayoutMediaEnded.startAnimation(alphaAnimation);

        binding.closeMediaEnded.setOnClickListener(v -> {

            binding.framlayoutMediaEnded.setVisibility(View.GONE);

            if (mCountDownTimer != null) {

                mCountDownTimer.cancel();
                mCountDownTimer = null;

            }

        });

        onLoadRandomMovie();

    }

    private void onLoadRandomMovie() {


        binding.progressBar.setVisibility(VISIBLE);
        binding.leftInfo.setVisibility(View.GONE);
        binding.nextPlayLayout.setVisibility(View.GONE);

        repository.getMoviRandomMovie()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                        //

                    }

                    @Override
                    public void onNext(@NotNull Media media) {

                        mCountDownTimer = new CountDownTimer(settingsManager.getSettings().getNextEpisodeTimer() * 1000L, 1000) {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTick(long millisUntilFinished) {

                                binding.textViewVideoTimeRemaining.setText(Constants.UPNEXT + millisUntilFinished / 1000 + " s");
                                binding.ratingBar.setRating(media.getVoteAverage() / 2);
                                binding.viewMovieRating.setText(valueOf(media.getVoteAverage()));
                                binding.textOverviewLabel.setText(media.getOverview());

                                GlideApp.with(getApplicationContext()).asBitmap().load(media.getBackdropPath())
                                        .centerCrop()
                                        .placeholder(R.drawable.placehoder_episodes)
                                        .transition(withCrossFade())
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(binding.imageViewMovieNext);


                                GlideApp.with(getApplicationContext()).asBitmap().load(media.getBackdropPath())
                                        .centerCrop()
                                        .placeholder(R.drawable.placehoder_episodes)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .transition(withCrossFade())
                                        .into(binding.nextCoverMedia);

                                if (media.getReleaseDate() != null) {
                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
                                    try {
                                        Date releaseDate = sdf1.parse(media.getReleaseDate());
                                        assert releaseDate != null;
                                        binding.textViewVideoRelease.setText(sdf2.format(releaseDate));
                                    } catch (ParseException e) {

                                        Timber.d("%s", Arrays.toString(e.getStackTrace()));

                                    }
                                } else {
                                    binding.textViewVideoRelease.setText("");
                                }


                                binding.textViewVideoNextName.setText(media.getTitle());


                                for (Genre genre : media.getGenres()) {
                                    binding.textViewVideoNextReleaseDate.setText(genre.getName());
                                }


                                binding.progressBar.setVisibility(GONE);
                                binding.leftInfo.setVisibility(VISIBLE);
                                binding.nextPlayLayout.setVisibility(VISIBLE);

                            }

                            @Override
                            public void onFinish() {

                                if (media.getVideos() != null && !media.getVideos().isEmpty()) {

                                    if (media.getPremuim() == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() != null) {

                                        onLoadNextMovieStream(media);

                                    } else if (settingsManager.getSettings().getWachAdsToUnlockPlayer() == 1 && media.getPremuim() != 1 && authManager.getUserInfo().getPremuim() == 0) {


                                        onLoadSubscribeDialog(media);

                                    } else if (settingsManager.getSettings().getWachAdsToUnlockPlayer() == 0 && media.getPremuim() == 0) {

                                        onLoadNextMovieStream(media);


                                    } else if (authManager.getUserInfo().getPremuim() == 1 && media.getPremuim() == 0) {

                                        onLoadNextMovieStream(media);


                                    } else {

                                        if (!isFinishing()) {
                                            DialogHelper.showPremuimWarning(EasyPlexMainPlayer.this);
                                        }


                                    }
                                } else {

                                    if (!isFinishing()) {
                                        DialogHelper.showNoStreamAvailable(EasyPlexMainPlayer.this);
                                    }

                                }

                            }
                        }.start();


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

    @SuppressLint("StaticFieldLeak")
    private void onLoadNextMovieStream(Media media) {

        adsLaunched = false;

        randomMovieFirstReady = false;

        updateResumePosition();

        mediaType();

        if (binding.framlayoutMediaEnded.getVisibility() == VISIBLE) {
            binding.framlayoutMediaEnded.setVisibility(View.GONE);
        }



        if (settingsManager.getSettings().getServerDialogSelection() == 1) {


            String[] charSequence = new String[media.getVideos().size()];

            for (int i = 0; i<media.getVideos().size(); i++) {
                charSequence[i] = media.getVideos().get(i).getServer() + " - " + media.getVideos().get(i).getLang();

            }


            final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme);
            builder.setTitle(getString(R.string.select_qualities));
            builder.setCancelable(true);
            builder.setItems(charSequence, (dialogInterface, wich) -> {


                if (media.getVideos().get(wich).getEmbed() == 1)  {


                    Intent intent = new Intent(this, EmbedActivity.class);
                    intent.putExtra(Constants.MOVIE_LINK, media.getVideos().get(wich).getLink());
                    startActivity(intent);


                }else if (media.getVideos().get(wich).getSupportedHosts() == 1){


                    if (settingsManager.getSettings().getHxfileApiKey() !=null && !settingsManager.getSettings().getHxfileApiKey().isEmpty())  {

                        easyPlexSupportedHosts.setApikey(settingsManager.getSettings().getHxfileApiKey());
                    }

                    easyPlexSupportedHosts.setMainApiServer(SERVER_BASE_URL);

                    easyPlexSupportedHosts.onFinish(new EasyPlexSupportedHosts.OnTaskCompleted() {

                        @Override
                        public void onTaskCompleted(ArrayList<EasyPlexSupportedHostsModel> vidURL, boolean multipleQuality) {

                            if (multipleQuality){
                                if (vidURL!=null) {
                                    CharSequence[] name = new CharSequence[vidURL.size()];

                                    for (int i = 0; i < vidURL.size(); i++) {
                                        name[i] = vidURL.get(i).getQuality();
                                    }


                                    final AlertDialog.Builder builder = new AlertDialog.Builder(EasyPlexMainPlayer.this, R.style.MyAlertDialogTheme);
                                    builder.setTitle(getString(R.string.select_qualities));
                                    builder.setCancelable(true);
                                    builder.setItems(name, (dialogInterface, i) -> {


                                        mMediaModel = MediaModel.media(valueOf(media.getId()), valueOf(media.getId()), media.getVideos().get(wich).getServer(),
                                                "0", media.getTitle(), vidURL.get(i).getUrl(), media.getBackdropPath(), null,
                                                null, null, null, null, null,
                                                null, null, null,

                                                null, media.getVideos().get(wich).getHls(), null, media.getImdbExternalId(),
                                                media.getPosterPath(), getPlayerController().getCurrentHasRecap(), getPlayerController().getCurrentStartRecapIn(),
                                                mediaGenre, null, media.getVoteAverage(),media.getVideos().get(wich).getDrmuuid()
                                                ,media.getVideos().get(wich).getDrmlicenceuri(),media.getVideos().get(wich).getDrm());
                                        playNext(mMediaModel);

                                    });

                                    builder.show();



                                }else  Toast.makeText(EasyPlexMainPlayer.this, "NULL", Toast.LENGTH_SHORT).show();

                            }else {


                                mMediaModel = MediaModel.media(valueOf(media.getId()), valueOf(media.getId()), media.getVideos().get(wich).getServer(),
                                        "0", media.getTitle(), vidURL.get(0).getUrl(), media.getBackdropPath(), null,
                                        null, null, null, null, null,
                                        null, null, null,

                                        null, media.getVideos().get(wich).getHls(),
                                        null, media.getImdbExternalId(),
                                        media.getPosterPath(), getPlayerController().getCurrentHasRecap(),
                                        getPlayerController().getCurrentStartRecapIn(), mediaGenre, null, media.getVoteAverage()
                                        ,media.getVideos().get(wich).getDrmuuid()
                                        ,media.getVideos().get(wich).getDrmlicenceuri(),media.getVideos().get(wich).getDrm());
                                playNext(mMediaModel);

                            }

                        }

                        @Override
                        public void onError() {

                            Toast.makeText(EasyPlexMainPlayer.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });

                    easyPlexSupportedHosts.find(media.getVideos().get(wich).getLink());

                }  else {


                    mMediaModel = MediaModel.media(valueOf(media.getId()), valueOf(media.getId()), media.getVideos().get(wich).getServer(),
                            "0", media.getTitle(), media.getVideos().get(wich).getLink(), media.getBackdropPath(), null,
                            null, null, null, null, null,
                            null, null, null,

                            null, media.getVideos().get(wich).getHls(), null, media.getImdbExternalId(),
                            media.getPosterPath(), getPlayerController().getCurrentHasRecap()
                            , getPlayerController().getCurrentStartRecapIn(), mediaGenre, null, media.getVoteAverage(),
                            media.getVideos().get(wich).getDrmuuid()
                            ,media.getVideos().get(wich).getDrmlicenceuri(),media.getVideos().get(wich).getDrm());
                    playNext(mMediaModel);

                }

            });

            builder.show();


        }else  {

            String mediaUrl = media.getVideos().get(0).getLink();
            String currentQuality = media.getVideos().get(0).getServer();
            int hls = media.getVideos().get(0).getHls();

            for (Genre genre : media.getGenres()) {
                mediaGenre = genre.getName();
            }


            if (media.getVideos().get(0).getEmbed() == 1) {

                Intent intent = new Intent(this, EmbedActivity.class);
                intent.putExtra(Constants.MOVIE_LINK, mediaUrl);
                startActivity(intent);


            } else if (media.getVideos().get(0).getSupportedHosts() == 1){


                if (settingsManager.getSettings().getHxfileApiKey() !=null && !settingsManager.getSettings().getHxfileApiKey().isEmpty())  {

                    easyPlexSupportedHosts.setApikey(settingsManager.getSettings().getHxfileApiKey());
                }

                easyPlexSupportedHosts.setMainApiServer(SERVER_BASE_URL);

                easyPlexSupportedHosts.onFinish(new EasyPlexSupportedHosts.OnTaskCompleted() {

                    @Override
                    public void onTaskCompleted(ArrayList<EasyPlexSupportedHostsModel> vidURL, boolean multipleQuality) {

                        if (multipleQuality){
                            if (vidURL!=null) {
                                CharSequence[] name = new CharSequence[vidURL.size()];

                                for (int i = 0; i < vidURL.size(); i++) {
                                    name[i] = vidURL.get(i).getQuality();
                                }


                                final AlertDialog.Builder builder = new AlertDialog.Builder(EasyPlexMainPlayer.this, R.style.MyAlertDialogTheme);
                                builder.setTitle(getString(R.string.select_qualities));
                                builder.setCancelable(true);
                                builder.setItems(name, (dialogInterface, i) -> {


                                    mMediaModel = MediaModel.media(valueOf(media.getId()), valueOf(media.getId()), currentQuality,
                                            "0", media.getTitle(), vidURL.get(i).getUrl(), media.getBackdropPath(), null,
                                            null, null, null, null, null,
                                            null, null, null,

                                            null, hls, null, media.getImdbExternalId(),
                                            media.getPosterPath(), getPlayerController().getCurrentHasRecap()
                                            , getPlayerController().getCurrentStartRecapIn(), mediaGenre, null, media.getVoteAverage(),
                                            media.getVideos().get(0).getDrmuuid()
                                            ,media.getVideos().get(0).getDrmlicenceuri(),media.getVideos().get(0).getDrm());
                                    playNext(mMediaModel);

                                });

                                builder.show();



                            }else  Toast.makeText(EasyPlexMainPlayer.this, "NULL", Toast.LENGTH_SHORT).show();

                        }else {


                            mMediaModel = MediaModel.media(valueOf(media.getId()), valueOf(media.getId()), currentQuality,
                                    "0", media.getTitle(), vidURL.get(0).getUrl(), media.getBackdropPath(), null,
                                    null, null, null, null, null,
                                    null, null, null,

                                    null, hls, null, media.getImdbExternalId(),
                                    media.getPosterPath(),
                                    getPlayerController().getCurrentHasRecap(),
                                    getPlayerController().getCurrentStartRecapIn(), mediaGenre, null, media.getVoteAverage(),
                                    media.getVideos().get(0).getDrmuuid()
                                    ,media.getVideos().get(0).getDrmlicenceuri(),media.getVideos().get(0).getDrm());
                            playNext(mMediaModel);

                        }

                    }

                    @Override
                    public void onError() {

                        Toast.makeText(EasyPlexMainPlayer.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

                easyPlexSupportedHosts.find(mediaUrl);


            }else  {


                mMediaModel = MediaModel.media(valueOf(media.getId()), valueOf(media.getId()), currentQuality,
                        "0", media.getTitle(), mediaUrl, media.getBackdropPath(), null,
                        null, null, null, null, null,
                        null, null, null,

                        null, hls, null, media.getImdbExternalId(),
                        media.getPosterPath(),
                        getPlayerController().getCurrentHasRecap(),
                        getPlayerController().getCurrentStartRecapIn(), mediaGenre,
                        null, media.getVoteAverage(),media.getVideos().get(0).getDrmuuid()
                        ,media.getVideos().get(0).getDrmlicenceuri(),media.getVideos().get(0).getDrm());
                playNext(mMediaModel);


            }

        }

    }



    private void onLoadSubscribeDialog(Media movieDetail) {


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.watch_to_unlock);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;


        dialog.findViewById(R.id.text_view_go_pro).setOnClickListener(v -> {

            startActivity(new Intent(this, SettingsActivity.class));

            dialog.dismiss();


        });


        dialog.findViewById(R.id.view_watch_ads_to_play).setOnClickListener(v -> {


            String defaultRewardedNetworkAds = settingsManager.getSettings().getDefaultNetworkPlayer();


            if (getString(R.string.unityads).equals(defaultRewardedNetworkAds)) {

                onLoadUnityAds(movieDetail);

            } else if (getString(R.string.applovin).equals(defaultRewardedNetworkAds)) {

                onLoadApplovinAds(movieDetail);

            } else if (getString(R.string.vungle).equals(defaultRewardedNetworkAds)) {

                onLoadVungleAds(movieDetail);

            } else if (getString(R.string.ironsource).equals(defaultRewardedNetworkAds)) {

                onLoadIronsourceAds(movieDetail);

            } else if (getString(R.string.admob).equals(defaultRewardedNetworkAds)) {

                onLoadAdmobRewardAds(movieDetail);


            } else if (getString(R.string.appodeal).equals(defaultRewardedNetworkAds)) {

                onLoadAppOdealRewardAds(movieDetail);

            } else if (getString(R.string.facebook).equals(defaultRewardedNetworkAds)) {

                onLoadFaceBookRewardAds(movieDetail);

            } else if (getString(R.string.wortise).equals(defaultRewardedNetworkAds)) {

                onLoadWortiseRewardAds(movieDetail);
            }


            dialog.dismiss();


        });


        dialog.findViewById(R.id.bt_close).setOnClickListener(v ->

                dialog.dismiss());


        dialog.show();
        dialog.getWindow().setAttributes(lp);


    }

    private void onLoadWortiseRewardAds(Media movieDetail) {


        com.wortise.ads.interstitial.InterstitialAd mInterstitialWortise = new com.wortise.ads.interstitial.InterstitialAd(this, settingsManager.getSettings().getWortisePlacementUnitId());


        mInterstitialWortise.loadAd();

        mInterstitialWortise.setListener(new com.wortise.ads.interstitial.InterstitialAd.Listener() {
            @Override
            public void onInterstitialImpression(@NonNull InterstitialAd interstitialAd) {

            }

            @Override
            public void onInterstitialFailedToShow(@NonNull InterstitialAd interstitialAd, @NonNull com.wortise.ads.AdError adError) {

            }

            @Override
            public void onInterstitialFailedToLoad(@NonNull InterstitialAd interstitialAd, @NonNull com.wortise.ads.AdError adError) {

            }

            @Override
            public void onInterstitialClicked(@NonNull com.wortise.ads.interstitial.InterstitialAd ad) {
                // Invoked when the user clicks on the interstitial
            }

            @Override
            public void onInterstitialDismissed(@NonNull com.wortise.ads.interstitial.InterstitialAd ad) {

                onLoadNexMovieStreamFromEnding(movieDetail);
                mInterstitialWortise.loadAd();
            }


            @Override
            public void onInterstitialLoaded(@NonNull com.wortise.ads.interstitial.InterstitialAd ad) {
                // Invoked when the interstitial has loaded successfully

                mInterstitialWortise.showAd();

            }

            @Override
            public void onInterstitialShown(@NonNull com.wortise.ads.interstitial.InterstitialAd ad) {
                // Invoked when the interstitial is shown
            }
        });
    }

    private void onLoadUnityAds(Media movieDetail) {


        if (settingsManager.getSettings().getUnityInterstitialPlacementId() ==null){

            Tools.ToastHelper(EasyPlexMainPlayer.this,getString(R.string.rewards_ads_not_ready));
            return;
        }


        UnityAds.load(settingsManager.getSettings().getUnityInterstitialPlacementId(), new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {

                UnityAds.show (EasyPlexMainPlayer.this, settingsManager.getSettings().getUnityInterstitialPlacementId(), new IUnityAdsShowListener() {
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

                        onLoadNexMovieStreamFromEnding(movieDetail);
                    }
                });



            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {


                //
            }
        });

    }


    private void onLoadApplovinAds(Media movieDetail) {

        maxInterstitialAd.showAd();
        maxInterstitialAd.setListener(new MaxAdListener() {
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

                onLoadNexMovieStreamFromEnding(movieDetail);

                maxInterstitialAd.loadAd();


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
        });

    }

    private void onLoadIronsourceAds(Media movieDetail) {

        IronSource.loadInterstitial();

        IronSource.setLevelPlayInterstitialListener(new LevelPlayInterstitialListener() {
            @Override
            public void onAdReady(AdInfo adInfo) {

                IronSource.showInterstitial(settingsManager.getSettings().getIronsourceInterstitialPlacementName());
            }

            @Override
            public void onAdLoadFailed(IronSourceError ironSourceError) {

                //
            }

            @Override
            public void onAdOpened(AdInfo adInfo) {
                //
            }

            @Override
            public void onAdShowSucceeded(AdInfo adInfo) {
                //
            }

            @Override
            public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                //
            }

            @Override
            public void onAdClicked(AdInfo adInfo) {
                //
            }

            @Override
            public void onAdClosed(AdInfo adInfo) {

                onLoadNexMovieStreamFromEnding(movieDetail);
            }

        });

    }

    private void onLoadVungleAds(Media movieDetail) {

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

        Vungle.playAd(settingsManager.getSettings().getVungleInterstitialPlacementName(), new AdConfig(), new PlayAdCallback() {
            @Override
            public void onAdStart(String placementReferenceID) {
                //
            }

            @Override
            public void onAdViewed(String placementReferenceID) {

                //
            }

            // Deprecated
            @Override
            public void onAdEnd(String id, boolean completed, boolean isCTAClicked) {

                onLoadNexMovieStreamFromEnding(movieDetail);
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




    private void onLoadAppOdealRewardAds(Media media) {

        Appodeal.show(EasyPlexMainPlayer.this, Appodeal.INTERSTITIAL);

        Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
            @Override
            public void onInterstitialLoaded(boolean b) {

                //

            }

            @Override
            public void onInterstitialFailedToLoad() {

                //

            }

            @Override
            public void onInterstitialShown() {

                //

            }

            @Override
            public void onInterstitialShowFailed() {

                //

            }

            @Override
            public void onInterstitialClicked() {

                //

            }

            @Override
            public void onInterstitialClosed() {

                onLoadNexMovieStreamFromEnding(media);


            }

            @Override
            public void onInterstitialExpired() {

                //

            }
        });


    }


    private void onLoadFaceBookRewardAds(Media media) {

        com.facebook.ads.InterstitialAd facebookInterstitialAd = new com.facebook.ads.InterstitialAd(this, settingsManager.getSettings().getAdUnitIdFacebookInterstitialAudience());

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

                //
            }

            @Override
            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                //

            }

            @Override
            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {

                onLoadNexMovieStreamFromEnding(media);

            }


        };


        facebookInterstitialAd.loadAd(
                facebookInterstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }

    private void onLoadNexMovieStreamFromEnding(Media media) {


        if (binding.framlayoutMediaEnded.getVisibility() == VISIBLE) {
            binding.framlayoutMediaEnded.setVisibility(View.GONE);
        }


        for (Genre genre : media.getGenres()) {
            mediaGenre = genre.getName();
        }


        String artwork = media.getBackdropPath();
        String movieId = media.getId();
        String type = "0";
        String currentQuality = media.getVideos().get(0).getServer();
        String name = media.getTitle();
        String streamLink = media.getVideos().get(0).getLink();
        int hls = media.getVideos().get(0).getHls();


        mMediaModel = MediaModel.media(movieId, null, currentQuality, type, name, streamLink, artwork, null,
                null, null, null, null,
                null, null, null,
                null, media.getPremuim(), hls, null, media.getImdbExternalId(),
                null,
                media.getHasrecap(),
                media.getSkiprecapStartIn(),
                mediaGenre, null, media.getVoteAverage(),media.getVideos().get(0).getDrmuuid()
                ,media.getVideos().get(0).getDrmlicenceuri(),media.getVideos().get(0).getDrm());

        playNext(mMediaModel);

    }

    private void onLoadAdmobRewardAds(Media media) {


        AdRequest adRequest = new AdRequest.Builder().build();
        com.google.android.gms.ads.interstitial.InterstitialAd.load(
                this,
                settingsManager.getSettings().getAdUnitIdInterstitial(),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {


                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.show(EasyPlexMainPlayer.this);

                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd = null;
                                        Timber.d("The ad was dismissed.");

                                        onLoadNexMovieStreamFromEnding(media);

                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Timber.d("The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                        mInterstitialAd = null;

                    }
                });


    }


    // Load Next Episode for A Serie
    private void onLoadNextSerieEpisodes() {



        binding.framlayoutMediaEnded.setVisibility(VISIBLE);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(500);
        binding.framlayoutMediaEnded.startAnimation(alphaAnimation);

        binding.closeMediaEnded.setOnClickListener(v -> {

            binding.framlayoutMediaEnded.setVisibility(View.GONE);

            if (mCountDownTimer != null) {

                mCountDownTimer.cancel();
                mCountDownTimer = null;

            }

        });


        onLoadNextEpisodeWithTimer();


    }


    // Load Next Episode Info  for A Serie With A CountDownTimer
    private void onLoadNextEpisodeWithTimer() {

        binding.progressBar.setVisibility(VISIBLE);
        binding.leftInfo.setVisibility(View.GONE);
        binding.nextPlayLayout.setVisibility(GONE);

        if (sharedPreferences.getString(cuePointY, cuePointN).equals(cuePointN)) {
            finishAffinity();


            if (mCountDownTimer !=null){

                mCountDownTimer.cancel();
                mCountDownTimer = null;
            }


        } else if (getPlayerController().getMediaType().equals("1")) {

            repository.getSerieSeasons(getPlayerController().nextSeaonsID(), settingsManager.getSettings().getApiKey())
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

                            if (getPlayerController().getCurrentEpisodePosition() != movieResponse.getEpisodes().size() - 1 &&

                                    getPlayerController().getEpName().equals(movieResponse.getEpisodes().get(getPlayerController().getCurrentEpisodePosition()).getName())) {

                                int position = getPlayerController().getCurrentEpisodePosition() + 1;


                                mCountDownTimer = new CountDownTimer(settingsManager.getSettings().getNextEpisodeTimer() * 1000L, 1000) {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                        binding.textViewVideoTimeRemaining.setText(UPNEXT + millisUntilFinished / 1000 + " s");
                                        binding.textViewVideoRelease.setText(SEASONS + getPlayerController().getSeaonNumber());

                                        if (movieResponse.getEpisodes().get(position).getOverview() == null) {

                                            movieResponse.getEpisodes().get(position).setOverview("");
                                        }


                                        if (movieResponse.getEpisodes().get(position).getVoteAverage() != null) {

                                            binding.ratingBar.setRating(Float.parseFloat(movieResponse.getEpisodes().get(position).getVoteAverage()) / 2);
                                            binding.viewMovieRating.setText(valueOf(movieResponse.getEpisodes().get(position).getVoteAverage()));


                                        } else {
                                            binding.ratingBar.setRating(Float.parseFloat(valueOf(0)) / 2);
                                            binding.viewMovieRating.setText(valueOf(0));

                                        }
                                        binding.textOverviewLabel.setText(movieResponse.getEpisodes().get(position).getOverview());



                                        if (movieResponse.getEpisodes().get(position).getStillPath() !=null && !movieResponse.getEpisodes().get(position).getStillPath().isEmpty()) {

                                            GlideApp.with(getApplicationContext()).asBitmap().load(movieResponse.getEpisodes().get(position).getStillPath())
                                                    .centerCrop()
                                                    .placeholder(R.drawable.placehoder_episodes)
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .transition(withCrossFade())
                                                    .into(binding.imageViewMovieNext);

                                            GlideApp.with(getApplicationContext()).asBitmap().load(movieResponse.getEpisodes().get(position).getStillPath())
                                                    .centerCrop()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .into(binding.nextCoverMedia);
                                        }else {

                                            GlideApp.with(getApplicationContext()).asBitmap().load(settingsManager.getSettings().getDefaultMediaPlaceholderPath())
                                                    .centerCrop()
                                                    .placeholder(R.drawable.placehoder_episodes)
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .transition(withCrossFade())
                                                    .into(binding.imageViewMovieNext);


                                            GlideApp.with(getApplicationContext()).asBitmap().load(settingsManager.getSettings().getDefaultMediaPlaceholderPath())
                                                    .centerCrop()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .into(binding.nextCoverMedia);

                                        }


                                        binding.textViewVideoNextName.setText(EP + movieResponse.getEpisodes().get(position).getEpisodeNumber() + " : " + movieResponse.getEpisodes().get(position).getName());
                                        binding.textViewVideoNextReleaseDate.setVisibility(GONE);


                                        binding.progressBar.setVisibility(GONE);
                                        binding.leftInfo.setVisibility(VISIBLE);
                                        binding.nextPlayLayout.setVisibility(VISIBLE);

                                    }

                                    @Override
                                    public void onFinish() {


                                        onCheckEpisodeHasStream(movieResponse, position);


                                    }


                                }.start();



                            } else {


                                onBackPressed();

                            }
                        }

                        @Override
                        public void onError(@NotNull Throwable e) {

                            onBackPressed();

                        }

                        @Override
                        public void onComplete() {

                            //

                        }
                    });

        } else if (getPlayerController().getMediaType().equals("anime")) {


            repository.getAnimeSeasons(getPlayerController().nextSeaonsID(), settingsManager.getSettings().getApiKey())
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

                            if (getPlayerController().getCurrentEpisodePosition() != movieResponse.getEpisodes().size() - 1 &&

                                    getPlayerController().getEpName().equals(movieResponse.getEpisodes().get(getPlayerController().getCurrentEpisodePosition()).getName())) {

                                int position = getPlayerController().getCurrentEpisodePosition() + 1;


                                mCountDownTimer = new CountDownTimer(settingsManager.getSettings().getNextEpisodeTimer() * 1000L, 1000) {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                        binding.textViewVideoTimeRemaining.setText(UPNEXT + millisUntilFinished / 1000 + " s");
                                        binding.textViewVideoRelease.setText(SEASONS + getPlayerController().getSeaonNumber());

                                        if (movieResponse.getEpisodes().get(position).getOverview() == null) {

                                            movieResponse.getEpisodes().get(position).setOverview("");
                                        }


                                        if (movieResponse.getEpisodes().get(position).getVoteAverage() != null) {

                                            binding.ratingBar.setRating(Float.parseFloat(movieResponse.getEpisodes().get(position).getVoteAverage()) / 2);
                                            binding.viewMovieRating.setText(valueOf(movieResponse.getEpisodes().get(position).getVoteAverage()));


                                        } else {
                                            binding.ratingBar.setRating(Float.parseFloat(valueOf(0)) / 2);
                                            binding.viewMovieRating.setText(valueOf(0));

                                        }
                                        binding.textOverviewLabel.setText(movieResponse.getEpisodes().get(position).getOverview());



                                        if (movieResponse.getEpisodes().get(position).getStillPath() !=null && !movieResponse.getEpisodes().get(position).getStillPath().isEmpty()) {

                                            GlideApp.with(getApplicationContext()).asBitmap().load(movieResponse.getEpisodes().get(position).getStillPath())
                                                    .centerCrop()
                                                    .placeholder(R.drawable.placehoder_episodes)
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .transition(withCrossFade())
                                                    .into(binding.imageViewMovieNext);

                                            GlideApp.with(getApplicationContext()).asBitmap().load(movieResponse.getEpisodes().get(position).getStillPath())
                                                    .centerCrop()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .into(binding.nextCoverMedia);
                                        }else {

                                            GlideApp.with(getApplicationContext()).asBitmap().load(settingsManager.getSettings().getDefaultMediaPlaceholderPath())
                                                    .centerCrop()
                                                    .placeholder(R.drawable.placehoder_episodes)
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .transition(withCrossFade())
                                                    .into(binding.imageViewMovieNext);


                                            GlideApp.with(getApplicationContext()).asBitmap().load(settingsManager.getSettings().getDefaultMediaPlaceholderPath())
                                                    .centerCrop()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .into(binding.nextCoverMedia);

                                        }


                                        binding.textViewVideoNextName.setText(EP + movieResponse.getEpisodes().get(position).getEpisodeNumber() + " : " + movieResponse.getEpisodes().get(position).getName());
                                        binding.textViewVideoNextReleaseDate.setVisibility(GONE);


                                        binding.progressBar.setVisibility(GONE);
                                        binding.leftInfo.setVisibility(VISIBLE);
                                        binding.nextPlayLayout.setVisibility(VISIBLE);

                                    }

                                    @Override
                                    public void onFinish() {


                                        onCheckEpisodeHasStreamAnimes(movieResponse, position);


                                    }


                                }.start();



                            } else {


                                onBackPressed();

                            }

                        }

                        @Override
                        public void onError(@NotNull Throwable e) {

                            onBackPressed();

                        }

                        @Override
                        public void onComplete() {

                            //

                        }
                    });

        }


    }

    // Check if a link is exit for the next Episode before playing
    private void onCheckEpisodeHasStream(MovieResponse movieResponse, int position) {

        if (movieResponse.getEpisodes().get(position).getVideos() != null && !movieResponse.getEpisodes().get(position).getVideos().isEmpty()) {

            if (getPlayerController().isMediaPremuim() == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() != null) {

                loadEpisodeStream(movieResponse, position);

            } else if (settingsManager.getSettings().getWachAdsToUnlockPlayer() == 1 && getPlayerController().isMediaPremuim() != 1 && authManager.getUserInfo().getPremuim() == 0) {

                onLoadSubscribeDialogEpisode(movieResponse, position);

            } else if (settingsManager.getSettings().getWachAdsToUnlockPlayer() == 0 && getPlayerController().isMediaPremuim() == 0) {

                loadEpisodeStream(movieResponse, position);

            } else if (authManager.getUserInfo().getPremuim() == 1 && getPlayerController().isMediaPremuim() == 0) {

                loadEpisodeStream(movieResponse, position);

            } else {

                if(!isFinishing())
                {
                    DialogHelper.showPremuimWarning(this);
                }

            }

        } else {

            if(!isFinishing())
            {
                DialogHelper.showNoStreamAvailable(EasyPlexMainPlayer.this);
            }

        }


        if (mCountDownTimer != null) {

            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void loadEpisodeStream(MovieResponse movieResponse, int position) {


        updateResumePosition();

        if (binding.framlayoutMediaEnded.getVisibility() == VISIBLE) {
            binding.framlayoutMediaEnded.setVisibility(View.GONE);
        }

        if (movieResponse.getEpisodes().get(position).getStillPath() !=null && !movieResponse.getEpisodes().get(position).getStillPath().isEmpty()){

            movieResponse.getEpisodes().get(position).setStillPath(settingsManager.getSettings().getDefaultMediaPlaceholderPath());
        }


        if (movieResponse.getEpisodes().get(position).getVoteAverage() ==null && movieResponse.getEpisodes().get(position).getOverview().isEmpty()){

            movieResponse.getEpisodes().get(position).setVoteAverage(String.valueOf(0));
        }


        String seasonId = movieResponse.getEpisodes().get(position).getSeasonId();
        String type = "1";
        String currentquality = movieResponse.getEpisodes().get(position).getVideos().get(0).getServer();
        String name = S0 + getPlayerController().getSeaonNumber() + E + movieResponse.getEpisodes().get(position).getEpisodeNumber() + " : " + movieResponse.getEpisodes().get(position).getName();
        String videourl = movieResponse.getEpisodes().get(position).getVideos().get(0).getLink();
        String episodeId = String.valueOf(movieResponse.getEpisodes().get(position).getId());
        int hls = movieResponse.getEpisodes().get(position).getVideos().get(0).getHls();
        Integer currentep = Integer.parseInt(movieResponse.getEpisodes().get(position).getEpisodeNumber());
        int hasRecap = movieResponse.getEpisodes().get(position).getHasrecap();
        int recapStartIn = movieResponse.getEpisodes().get(position).getSkiprecapStartIn();
        int drm =  movieResponse.getEpisodes().get(0).getVideos().get(0).getDrm();
        String Drmuuid =  movieResponse.getEpisodes().get(0).getVideos().get(0).getDrmuuid();
        String Drmlicenceuri =  movieResponse.getEpisodes().get(0).getVideos().get(0).getDrmlicenceuri();


        if (movieResponse.getEpisodes().get(position).getVideos().get(0).getSupportedHosts() == 1) {

            if (settingsManager.getSettings().getHxfileApiKey() !=null && !settingsManager.getSettings().getHxfileApiKey().isEmpty())  {

                easyPlexSupportedHosts.setApikey(settingsManager.getSettings().getHxfileApiKey());
            }

            easyPlexSupportedHosts.setMainApiServer(SERVER_BASE_URL);

            easyPlexSupportedHosts.onFinish(new EasyPlexSupportedHosts.OnTaskCompleted() {

                @Override
                public void onTaskCompleted(ArrayList<EasyPlexSupportedHostsModel> vidURL, boolean multipleQuality) {

                    if (multipleQuality) {
                        if (vidURL != null) {

                            CharSequence[] names = new CharSequence[vidURL.size()];

                            for (int i = 0; i < vidURL.size(); i++) {
                                names[i] = vidURL.get(i).getQuality();
                            }

                            if(!isFinishing())
                            {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(EasyPlexMainPlayer.this, R.style.MyAlertDialogTheme);
                                builder.setTitle(getString(R.string.select_qualities));
                                builder.setCancelable(true);
                                builder.setItems(names, (dialogInterface, wich) -> {

                                    mMediaModel = MediaModel.media(getPlayerController().getVideoID(),
                                            null, currentquality, type, name, vidURL.get(wich).getUrl(), movieResponse.getEpisodes().get(position).getStillPath(),
                                            null,
                                            movieResponse.getEpisodes().get(position).getId(),
                                            getPlayerController().getSeaonNumber(),
                                            valueOf(movieResponse.getEpisodes().get(position).getId()),
                                            seasonId,
                                            movieResponse.getEpisodes().get(position).getName(),
                                            getPlayerController().getSeaonNumber(), position,
                                            valueOf(movieResponse.getEpisodes().get(position).getId())
                                            , getPlayerController().isMediaPremuim(),
                                            hls, null,
                                            getPlayerController().getCurrentExternalId(),
                                            getPlayerController().getMediaCoverHistory(), hasRecap, recapStartIn,
                                            getPlayerController().getMediaGenre(), getPlayerController().getSerieName(),
                                            Float.parseFloat(movieResponse.getEpisodes().get(position).getVoteAverage()),Drmuuid,Drmlicenceuri,drm);

                                    playNext(mMediaModel);


                                });
                                builder.show();
                            }




                        } else
                            Toast.makeText(EasyPlexMainPlayer.this, "NULL", Toast.LENGTH_SHORT).show();

                    } else {


                        mMediaModel = MediaModel.media(getPlayerController().getVideoID(),
                                null, currentquality, type, name, vidURL.get(0).getUrl(), movieResponse.getEpisodes().get(position).getStillPath(),
                                null,
                                movieResponse.getEpisodes().get(position).getId(),
                                getPlayerController().getSeaonNumber(), valueOf(movieResponse.getEpisodes().get(position).getId()),
                                seasonId, movieResponse.getEpisodes().get(position).getName(),
                                getPlayerController().getSeaonNumber(), position, valueOf(movieResponse.getEpisodes().get(position).getId())
                                , getPlayerController().isMediaPremuim(), hls, null, getPlayerController().getCurrentExternalId(),
                                getPlayerController().getMediaCoverHistory(), hasRecap, recapStartIn,
                                getPlayerController().getMediaGenre(), getPlayerController().getSerieName(),
                                Float.parseFloat(movieResponse.getEpisodes().get(position).getVoteAverage()),Drmuuid,Drmlicenceuri,drm);


                        playNext(mMediaModel);
                    }

                }

                @Override
                public void onError() {

                    Toast.makeText(EasyPlexMainPlayer.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });

            easyPlexSupportedHosts.find(videourl);


        } else {


            mMediaModel = MediaModel.media(getPlayerController().getVideoID(),
                    null, currentquality, type, name, videourl, movieResponse.getEpisodes().get(position).getStillPath(),
                    null,
                    currentep,
                    getPlayerController().getSeaonNumber(),
                    valueOf(movieResponse.getEpisodes().get(position).getId()),
                    seasonId, movieResponse.getEpisodes().get(position).getName(),
                    getPlayerController().getSeaonNumber(), position, valueOf(movieResponse.getEpisodes().get(position).getId())
                    , getPlayerController().isMediaPremuim(), hls, null, getPlayerController().getCurrentExternalId(),
                    getPlayerController().getMediaCoverHistory(), hasRecap, recapStartIn,
                    getPlayerController().getMediaGenre(), getPlayerController().getSerieName(),
                    Float.parseFloat(movieResponse.getEpisodes().get(position).getVoteAverage()),Drmuuid,Drmlicenceuri,drm);


            playNext(mMediaModel);

            history = new History(getPlayerController().getVideoID(),
                    getPlayerController().getVideoID(),movieResponse.getEpisodes().get(position).getStillPath(),name,"","");
            history.setVoteAverage(Float.parseFloat(movieResponse.getEpisodes().get(position).getVoteAverage()));
            history.setSerieName(getPlayerController().getSerieName());
            history.setPosterPath(getPlayerController().getMediaCoverHistory());
            history.setTitle(name);
            history.setBackdropPath(movieResponse.getEpisodes().get(position).getStillPath());
            history.setEpisodeNmber(String.valueOf(currentep));
            history.setSeasonsId(seasonId);
            history.setType(type);
            history.setTmdbId(getPlayerController().getVideoID());
            history.setPosition(position);
            history.setEpisodeId(episodeId);
            history.setEpisodeName(movieResponse.getEpisodes().get(position).getName());
            history.setEpisodeTmdb(episodeId);
            history.setSerieId(getPlayerController().getVideoID());
            history.setCurrentSeasons(getPlayerController().getCurrentSeasonId());
            history.setSeasonsNumber(getPlayerController().getSeaonNumber());
            history.setImdbExternalId(getPlayerController().getCurrentExternalId());
            history.setPremuim(getPlayerController().isMediaPremuim());
            compositeDisposable.add(Completable.fromAction(() -> repository.addhistory(history))
                    .subscribeOn(Schedulers.io())
                    .subscribe());

        }
    }

    private void onLoadSubscribeDialogEpisode(MovieResponse media, int position) {

        Appodeal.initialize(EasyPlexMainPlayer.this, settingsManager.getSettings().getAdUnitIdAppodealRewarded(), Appodeal.INTERSTITIAL);

        if(!isFinishing())
        {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.watch_to_unlock);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());

            lp.gravity = Gravity.BOTTOM;
            lp.width = WRAP_CONTENT;
            lp.height = WRAP_CONTENT;

            dialog.findViewById(R.id.view_watch_ads_to_play).setOnClickListener(v -> {

                String defaultRewardedNetworkAds = settingsManager.getSettings().getDefaultNetworkPlayer();


                if (getString(R.string.unityads).equals(defaultRewardedNetworkAds)) {

                    onLoadUnityAdsEpisode(media, position);


                } else if (getString(R.string.applovin).equals(defaultRewardedNetworkAds)) {

                    onLoadApplovinAdsEpisode(media, position);

                } else if (getString(R.string.vungle).equals(defaultRewardedNetworkAds)) {

                    onLoadVungleAdsEpisode(media, position);

                } else if (getString(R.string.ironsource).equals(defaultRewardedNetworkAds)) {

                    onLoadIronsourceAdsEpisode(media, position);

                } else if (getString(R.string.admob).equals(defaultRewardedNetworkAds)) {


                    onLoadAdmobRewardAdsEpisode(media, position);


                } else if (getString(R.string.appodeal).equals(defaultRewardedNetworkAds)) {

                    onLoadAppOdealRewardAdsEpisode(media, position);

                }

                dialog.dismiss();


            });


            dialog.findViewById(R.id.text_view_go_pro).setOnClickListener(v -> {

                startActivity(new Intent(this, SettingsActivity.class));

                dialog.dismiss();


            });


            dialog.findViewById(R.id.bt_close).setOnClickListener(v ->

                    dialog.dismiss());


            dialog.show();
            dialog.getWindow().setAttributes(lp);

        }

    }

    private void onLoadUnityAdsEpisode(MovieResponse media, int position) {


        if (settingsManager.getSettings().getUnityInterstitialPlacementId() ==null){

            Tools.ToastHelper(EasyPlexMainPlayer.this,getString(R.string.rewards_ads_not_ready));
            return;
        }


        UnityAds.load(settingsManager.getSettings().getUnityInterstitialPlacementId(), new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {

                UnityAds.show(EasyPlexMainPlayer.this, settingsManager.getSettings().getUnityInterstitialPlacementId(), new IUnityAdsShowListener() {
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

                        if (getPlayerController().getMediaType().equals("1")) {

                            loadEpisodeStream(media, position);


                        } else {

                            loadEpisodeAnimeStream(media, position);
                        }
                    }
                });


            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {


                //
            }
        });

    }

    private void onLoadApplovinAdsEpisode(MovieResponse media, int position) {

        if (settingsManager.getSettings().getApplovinInterstitialUnitid() !=null && !settingsManager.getSettings().getApplovinInterstitialUnitid().isEmpty()) {

            maxInterstitialAd.showAd();
            maxInterstitialAd.setListener(new MaxAdListener() {
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

                    maxInterstitialAd.loadAd();

                    if (getPlayerController().getMediaType().equals("1")) {

                        loadEpisodeStream(media, position);


                    } else {

                        loadEpisodeAnimeStream(media, position);
                    }

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
            });

        }
    }

    private void onLoadIronsourceAdsEpisode(MovieResponse media, int position) {

        IronSource.loadInterstitial();

        IronSource.setLevelPlayInterstitialListener(new LevelPlayInterstitialListener() {
            @Override
            public void onAdReady(AdInfo adInfo) {

                IronSource.showInterstitial(settingsManager.getSettings().getIronsourceInterstitialPlacementName());
            }

            @Override
            public void onAdLoadFailed(IronSourceError ironSourceError) {

            }

            @Override
            public void onAdOpened(AdInfo adInfo) {

            }

            @Override
            public void onAdShowSucceeded(AdInfo adInfo) {

            }

            @Override
            public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {

            }

            @Override
            public void onAdClicked(AdInfo adInfo) {

            }

            @Override
            public void onAdClosed(AdInfo adInfo) {

                if (getPlayerController().getMediaType().equals("1")) {

                    loadEpisodeStream(media, position);


                } else {

                    loadEpisodeAnimeStream(media, position);
                }
            }
        });

    }

    private void onLoadVungleAdsEpisode(MovieResponse media, int position) {

        Vungle.loadAd(settingsManager.getSettings().getVungleInterstitialPlacementName(), new LoadAdCallback() {
            @Override
            public void onAdLoad(String id) {

                Vungle.playAd(settingsManager.getSettings().getVungleInterstitialPlacementName(), new AdConfig(), new PlayAdCallback() {
                    @Override
                    public void onAdStart(String placementReferenceID) {
                        //
                    }

                    @Override
                    public void onAdViewed(String placementReferenceID) {

                        //
                    }

                    // Deprecated
                    @Override
                    public void onAdEnd(String id, boolean completed, boolean isCTAClicked) {

                        //
                    }

                    @Override
                    public void onAdEnd(String placementReferenceID) {

                        if (getPlayerController().getMediaType().equals("1")) {

                            loadEpisodeStream(media, position);


                        } else {

                            loadEpisodeAnimeStream(media, position);
                        }
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

            @Override
            public void onError(String id, VungleException e) {

                //
            }
        });


    }

    private void onLoadAppOdealRewardAdsEpisode(MovieResponse media, int position) {

        Appodeal.show(EasyPlexMainPlayer.this, Appodeal.INTERSTITIAL);

        Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
            @Override
            public void onInterstitialLoaded(boolean b) {

                //


            }

            @Override
            public void onInterstitialFailedToLoad() {

                //

            }

            @Override
            public void onInterstitialShown() {

                //

            }

            @Override
            public void onInterstitialShowFailed() {

                //

            }

            @Override
            public void onInterstitialClicked() {

                //

            }

            @Override
            public void onInterstitialClosed() {

                if (getPlayerController().getMediaType().equals("1")) {

                    loadEpisodeStream(media, position);


                } else {

                    loadEpisodeAnimeStream(media, position);
                }


            }

            @Override
            public void onInterstitialExpired() {

                //

            }
        });


    }


    private void onLoadAdmobRewardAdsEpisode(MovieResponse movieResponse, int position) {

        AdRequest adRequest = new AdRequest.Builder().build();
        com.google.android.gms.ads.interstitial.InterstitialAd.load(
                this,
                settingsManager.getSettings().getAdUnitIdInterstitial(),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {


                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.show(EasyPlexMainPlayer.this);

                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd = null;
                                        Timber.d("The ad was dismissed.");

                                        if (getPlayerController().getMediaType().equals("1")) {

                                            loadEpisodeStream(movieResponse, position);

                                        } else {

                                            loadEpisodeAnimeStream(movieResponse, position);
                                        }
                                    }


                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Timber.d("The ad was shown.");


                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                        mInterstitialAd = null;

                    }
                });


    }

    @Override
    public void onLoadEpisodes() {

        mediaType();
        updateResumePosition();

        binding.filterBtn.setOnClickListener(v -> binding.planetsSpinner.performClick());

        binding.frameLayoutSeasons.setVisibility(VISIBLE);
        binding.closeEpisode.setOnClickListener(v -> binding.frameLayoutSeasons.setVisibility(View.GONE));

        if (getPlayerController().getMediaType().equals("1")) {


            onLoadSeriesEpisodes();


        } else {


            onLoadAnimeEpisodes();


        }

    }

    private void onLoadSeriesEpisodes() {




        repository.getSerie(getPlayerController().getVideoID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                        //

                    }

                    @Override
                    public void onNext(@NotNull Media movieDetail) {

                        binding.planetsSpinner.setItem(movieDetail.getSeasons());
                        binding.planetsSpinner.setSelection(0);
                        binding.planetsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                                currentGenre = position;

                                Tools.hideSystemPlayerUi(EasyPlexMainPlayer.this, true);

                                Season season = (Season) adapterView.getItemAtPosition(position);
                                String episodeId = valueOf(season.getId());
                                String currentSeason = season.getName();
                                String serieId = valueOf(movieDetail.getId());
                                String seasonNumber = season.getSeasonNumber();


                                binding.currentSelectedSeasons.setText(season.getName());

                                // Episodes RecycleView
                                binding.recyclerViewEpisodes.setHasFixedSize(true);
                                binding.recyclerViewEpisodes.setNestedScrollingEnabled(false);
                                binding.recyclerViewEpisodes.setLayoutManager(new LinearLayoutManager(EasyPlexMainPlayer.this, LinearLayoutManager.HORIZONTAL, false));
                                binding.recyclerViewEpisodes.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(EasyPlexMainPlayer.this, 0), true));
                                binding.recyclerViewEpisodes.setItemViewCacheSize(8);
                                mEPAdapter = new EpisodesPlayerAdapter(serieId, seasonNumber, episodeId, currentSeason,
                                        clickDetectListner, authManager, settingsManager, tokenManager, sharedPreferences, repository, EasyPlexMainPlayer.this);


                                if (movieDetail.getSeasons() !=null && !movieDetail.getSeasons().isEmpty()) {

                                    mEPAdapter.addSeasons(season.getEpisodes());

                                }



                                binding.recyclerViewEpisodes.setAdapter(mEPAdapter);


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {


                                //

                            }
                        });


                    }


                    @Override
                    public void onError(@NotNull Throwable e) {

                        Toast.makeText(EasyPlexMainPlayer.this, ""+e, Toast.LENGTH_SHORT).show();
                        Timber.i(e);
                    }

                    @Override
                    public void onComplete() {

                        //

                    }
                });
    }

    @Override
    public void onLoadNextEpisode() {

        if (sharedPreferences.getString(cuePointY, cuePointN).equals(cuePointN)) {
            finishAffinity();

        } else if (getPlayerController().getMediaType().equals("1")) {

            repository.getSerieSeasons(getPlayerController().nextSeaonsID(), settingsManager.getSettings().getApiKey())
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

                            if (getPlayerController().getCurrentEpisodePosition() != movieResponse.getEpisodes().size() - 1 &&

                                    getPlayerController().getEpName().equals(movieResponse.getEpisodes().get(getPlayerController().getCurrentEpisodePosition()).getName())) {

                                int position = getPlayerController().getCurrentEpisodePosition() + 1;


                                onCheckEpisodeHasStream(movieResponse, position);


                            } else {


                                onBackPressed();

                            }
                        }

                        @Override
                        public void onError(@NotNull Throwable e) {

                            onBackPressed();

                        }

                        @Override
                        public void onComplete() {

                            //

                        }
                    });

        } else if (getPlayerController().getMediaType().equals("anime")) {


            repository.getAnimeSeasons(getPlayerController().nextSeaonsID(), settingsManager.getSettings().getApiKey())
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

                            if (getPlayerController().getCurrentEpisodePosition() != movieResponse.getEpisodes().size()

                                    - 1 && getPlayerController().getEpName().equals(movieResponse.getEpisodes().get(getPlayerController().getCurrentEpisodePosition()).getName())) {

                                int position = getPlayerController().getCurrentEpisodePosition() + 1;


                                onCheckEpisodeHasStreamAnimes(movieResponse, position);


                            } else {


                                onBackPressed();

                            }

                        }

                        @Override
                        public void onError(@NotNull Throwable e) {

                            onBackPressed();

                        }

                        @Override
                        public void onComplete() {

                            //

                        }
                    });

        }

    }

    @Override
    public void onLoadloadSeriesList() {


        mediaType();
        updateResumePosition();

        binding.serieListBtn.setOnClickListener(v -> binding.seriesListSpinner.performClick());

        binding.frameLayoutSeriesList.setVisibility(VISIBLE);
        binding.closeSeriesList.setOnClickListener(v -> binding.frameLayoutSeriesList.setVisibility(View.GONE));


        repository.getMoviesGenres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {


                        //


                    }

                    @Override
                    public void onNext(@NotNull GenresByID genresData) {


                        List<Genre> genres = genresData.getGenresPlayer();

                        if (!genres.isEmpty()) {

                            binding.seriesListSpinner.setItem(genres);
                            binding.seriesListSpinner.setSelection(currentGenre);
                            binding.seriesListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {


                                    currentGenre = position;

                                    Genre genre = (Genre) adapterView.getItemAtPosition(position);
                                    int genreId = genre.getId();
                                    String genreName = genre.getName();

                                    binding.viewTextSerieListGenreName.setText(genreName);

                                    repository.getSerieByGenrePlayer(genreId, settingsManager.getSettings().getApiKey(), FIRSTPAGE)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Observer<>() {
                                                @Override
                                                public void onSubscribe(@NotNull Disposable d) {

                                                    //

                                                }

                                                @SuppressLint("SetTextI18n")
                                                @Override
                                                public void onNext(@NotNull GenresData genresData) {

                                                    repository.getSerieByGenrePlayer(genreId, settingsManager.getSettings().getApiKey(), FIRSTPAGE + 1)
                                                            .subscribeOn(Schedulers.io())
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(new Observer<>() {
                                                                @Override
                                                                public void onSubscribe(@NotNull Disposable d) {

                                                                    //

                                                                }

                                                                @SuppressLint("SetTextI18n")
                                                                @Override
                                                                public void onNext(@NotNull GenresData genresData) {

                                                                    seriesListAdapter = new SeriesListAdapter(
                                                                            EasyPlexMainPlayer.this, clickDetectListner, authManager, settingsManager, tokenManager, sharedPreferences, repository);

                                                                    searchQuery.setValue(valueOf(genreId));


                                                                    getSeriesGenresitemPagedList().observe(EasyPlexMainPlayer.this, genresList -> {

                                                                        if (genresList != null) {

                                                                            binding.rvSeriesFeatured.setLayoutManager(new LinearLayoutManager(EasyPlexMainPlayer.this, LinearLayoutManager.HORIZONTAL, false));
                                                                            binding.rvSeriesFeatured.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(EasyPlexMainPlayer.this, 0), true));
                                                                            seriesListAdapter.submitList(genresList);


                                                                        }

                                                                        binding.rvSeriesFeatured.setAdapter(seriesListAdapter);

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


                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                    // Nothting to refresh when no Item Selected

                                }
                            });


                        } else {

                            Toast.makeText(EasyPlexMainPlayer.this, R.string.unable_to_get_genres, Toast.LENGTH_SHORT).show();
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


    @Override
    public void onLoadloadAnimesList() {


        mediaType();
        updateResumePosition();

        binding.serieListBtn.setOnClickListener(v -> binding.seriesListSpinner.performClick());

        binding.frameLayoutSeriesList.setVisibility(VISIBLE);
        binding.closeSeriesList.setOnClickListener(v -> binding.frameLayoutSeriesList.setVisibility(View.GONE));

        repository.getMoviesGenres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {


                        //


                    }

                    @Override
                    public void onNext(@NotNull GenresByID genresData) {


                        List<Genre> genres = genresData.getGenresPlayer();

                        if (!genres.isEmpty()) {

                            binding.seriesListSpinner.setItem(genres);
                            binding.seriesListSpinner.setSelection(currentGenre);
                            binding.seriesListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {


                                    currentGenre = position;

                                    Genre genre = (Genre) adapterView.getItemAtPosition(position);
                                    int genreId = genre.getId();
                                    String genreName = genre.getName();

                                    binding.viewTextSerieListGenreName.setText(genreName);

                                    repository.getAnimesByGenrePlayer(genreId, settingsManager.getSettings().getApiKey(), FIRSTPAGE)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Observer<>() {
                                                @Override
                                                public void onSubscribe(@NotNull Disposable d) {

                                                    //

                                                }

                                                @SuppressLint("SetTextI18n")
                                                @Override
                                                public void onNext(@NotNull GenresData genresData) {

                                                    repository.getAnimesByGenrePlayer(genreId, settingsManager.getSettings().getApiKey(), FIRSTPAGE + 1)
                                                            .subscribeOn(Schedulers.io())
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(new Observer<>() {
                                                                @Override
                                                                public void onSubscribe(@NotNull Disposable d) {

                                                                    //

                                                                }

                                                                @SuppressLint("SetTextI18n")
                                                                @Override
                                                                public void onNext(@NotNull GenresData genresData) {

                                                                    animesListAdapter = new AnimesListAdapter(
                                                                            EasyPlexMainPlayer.this
                                                                            , clickDetectListner,
                                                                            authManager, settingsManager, tokenManager, sharedPreferences, repository, animeRepository);

                                                                    searchQuery.setValue(valueOf(genreId));


                                                                    getAnimesGenresitemPagedList().observe(EasyPlexMainPlayer.this, genresList -> {

                                                                        if (genresList != null) {

                                                                            binding.rvSeriesFeatured.setLayoutManager(new LinearLayoutManager(EasyPlexMainPlayer.this, LinearLayoutManager.HORIZONTAL, false));
                                                                            binding.rvSeriesFeatured.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(EasyPlexMainPlayer.this, 0), true));
                                                                            animesListAdapter.submitList(genresList);


                                                                        }

                                                                        binding.rvSeriesFeatured.setAdapter(animesListAdapter);

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


                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                    // Nothting to refresh when no Item Selected

                                }
                            });


                        } else {

                            Toast.makeText(EasyPlexMainPlayer.this, R.string.genres_lists_empty, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onLoadPlaybackSetting() {

        playbackSettingMenu.show();

    }


    private void onCheckEpisodeHasStreamAnimes(MovieResponse movieResponse, int position) {

        if (movieResponse.getEpisodes().get(position).getVideos() != null && !movieResponse.getEpisodes().get(position).getVideos().isEmpty()) {

            if (getPlayerController().isMediaPremuim() == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() != null) {

                loadEpisodeAnimeStream(movieResponse, position);

            } else if (settingsManager.getSettings().getWachAdsToUnlockPlayer() == 1 && getPlayerController().isMediaPremuim() != 1 && authManager.getUserInfo().getPremuim() == 0) {

                onLoadSubscribeDialogEpisode(movieResponse, position);

            } else if (settingsManager.getSettings().getWachAdsToUnlockPlayer() == 0 && getPlayerController().isMediaPremuim() == 0) {


                loadEpisodeAnimeStream(movieResponse, position);


            } else if (authManager.getUserInfo().getPremuim() == 1 && getPlayerController().isMediaPremuim() == 0) {


                loadEpisodeAnimeStream(movieResponse, position);


            } else {


                if(!isFinishing())
                {
                    DialogHelper.showPremuimWarning(this);
                }

            }


        } else {

            if(!isFinishing())
            {
                DialogHelper.showNoStreamAvailable(this);
            }



        }

    }

    @SuppressLint("StaticFieldLeak")
    private void loadEpisodeAnimeStream(MovieResponse movieResponse, int position) {


        updateResumePosition();



        if (binding.framlayoutMediaEnded.getVisibility() == VISIBLE) {
            binding.framlayoutMediaEnded.setVisibility(View.GONE);
        }


        String mediaCover = movieResponse.getEpisodes().get(position).getStillPath();
        String type = "anime";
        String currentquality = movieResponse.getEpisodes().get(position).getVideos().get(0).getServer();
        String name = S0 + getPlayerController().getSeaonNumber() + E + movieResponse.getEpisodes().get(position).getEpisodeNumber() + " : " + movieResponse.getEpisodes().get(position).getName();
        String videourl = movieResponse.getEpisodes().get(position).getVideos().get(0).getLink();
        int hls = movieResponse.getEpisodes().get(position).getVideos().get(0).getHls();
        Integer currentep = Integer.parseInt(movieResponse.getEpisodes().get(position).getEpisodeNumber());
        int hasRecap = movieResponse.getEpisodes().get(position).getHasrecap();
        int recapStartIn = movieResponse.getEpisodes().get(position).getSkiprecapStartIn();

        int drm =  movieResponse.getEpisodes().get(position).getVideos().get(0).getDrm();
        String Drmuuid =  movieResponse.getEpisodes().get(position).getVideos().get(0).getDrmuuid();
        String Drmlicenceuri =  movieResponse.getEpisodes().get(position).getVideos().get(0).getDrmlicenceuri();


        if (movieResponse.getEpisodes().get(position).getVideos().get(0).getSupportedHosts() == 1) {

            if (settingsManager.getSettings().getHxfileApiKey() !=null && !settingsManager.getSettings().getHxfileApiKey().isEmpty())  {

                easyPlexSupportedHosts.setApikey(settingsManager.getSettings().getHxfileApiKey());
            }

            easyPlexSupportedHosts.setMainApiServer(SERVER_BASE_URL);
            easyPlexSupportedHosts.onFinish(new EasyPlexSupportedHosts.OnTaskCompleted() {

                @Override
                public void onTaskCompleted(ArrayList<EasyPlexSupportedHostsModel> vidURL, boolean multipleQuality) {

                    if (multipleQuality) {
                        if (vidURL != null) {

                            CharSequence[] names = new CharSequence[vidURL.size()];

                            for (int i = 0; i < vidURL.size(); i++) {
                                names[i] = vidURL.get(i).getQuality();
                            }


                            final AlertDialog.Builder builder = new AlertDialog.Builder(EasyPlexMainPlayer.this, R.style.MyAlertDialogTheme);
                            builder.setTitle(getString(R.string.select_qualities));
                            builder.setCancelable(true);
                            builder.setItems(names, (dialogInterface, wich) -> {


                                mMediaModel = MediaModel.media(getPlayerController().getVideoID(),
                                        null, currentquality, type, name, vidURL.get(wich).getUrl(), mediaCover,
                                        null,
                                        currentep,
                                        getPlayerController().getSeaonNumber(), valueOf(movieResponse.getEpisodes().get(position).getId()),
                                        null, movieResponse.getEpisodes().get(position).getName(),
                                        getPlayerController().getSeaonNumber(), position, valueOf(movieResponse.getEpisodes().get(position).getId())
                                        , getPlayerController().isMediaPremuim(), hls, null, getPlayerController().getCurrentExternalId(),
                                        getPlayerController().getMediaCoverHistory(), hasRecap, recapStartIn,
                                        getPlayerController().getMediaGenre(),
                                        getPlayerController().getSerieName(),
                                        Float.parseFloat(movieResponse.getEpisodes().get(position).getVoteAverage()),
                                        Drmuuid,Drmlicenceuri,drm);

                                playNext(mMediaModel);



                            });

                            builder.show();


                        } else
                            Toast.makeText(EasyPlexMainPlayer.this, "NULL", Toast.LENGTH_SHORT).show();

                    } else {


                        mMediaModel = MediaModel.media(getPlayerController().getVideoID(),
                                null, currentquality, type, name, vidURL.get(0).getUrl(), mediaCover,
                                null,
                                currentep,
                                getPlayerController().getSeaonNumber(), valueOf(movieResponse.getEpisodes().get(position).getId()),
                                null, movieResponse.getEpisodes().get(position).getName(),
                                getPlayerController().getSeaonNumber(), position, valueOf(movieResponse.getEpisodes().get(position).getId())
                                , getPlayerController().isMediaPremuim(), hls, null, getPlayerController().getCurrentExternalId(),
                                getPlayerController().getMediaCoverHistory(), hasRecap, recapStartIn,
                                getPlayerController().getMediaGenre(),
                                getPlayerController().getSerieName(),
                                Float.parseFloat(movieResponse.getEpisodes().get(position).getVoteAverage()),Drmuuid,Drmlicenceuri,drm);

                        playNext(mMediaModel);

                    }

                }

                @Override
                public void onError() {

                    Toast.makeText(EasyPlexMainPlayer.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });

            easyPlexSupportedHosts.find(videourl);


        } else {


            mMediaModel = MediaModel.media(getPlayerController().getVideoID(),
                    null, currentquality, type, name, videourl, mediaCover,
                    null,
                    currentep,
                    getPlayerController().getSeaonNumber(), valueOf(movieResponse.getEpisodes().get(position).getId()),
                    null, movieResponse.getEpisodes().get(position).getName(),
                    getPlayerController().getSeaonNumber(), position, valueOf(movieResponse.getEpisodes().get(position).getId())
                    , getPlayerController().isMediaPremuim(), hls, null, getPlayerController().getCurrentExternalId(),
                    getPlayerController().getMediaCoverHistory(),
                    hasRecap, recapStartIn, getPlayerController().getMediaGenre()
                    , getPlayerController().getSerieName(),
                    Float.parseFloat(movieResponse.getEpisodes().get(position).getVoteAverage()),Drmuuid,Drmlicenceuri,drm);

            playNext(mMediaModel);


        }


    }

    // Load Episodes for Anime
    private void onLoadAnimeEpisodes() {

        playerViewModel.getAnimeDetails(getPlayerController().getVideoID());
        playerViewModel.mediaMutableLiveData.observe(this, anime -> {

            List<Season> seasons = anime.getSeasons() ;

            for(Iterator<Season> iterator = seasons.iterator(); iterator.hasNext(); ) {
                if(iterator.next().getName().equals(SPECIALS))
                    iterator.remove();
            }

            binding.planetsSpinner.setItem(seasons);
            binding.planetsSpinner.setSelection(currentGenre);
            binding.planetsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                    currentGenre = position;

                    Tools.hideSystemPlayerUi(EasyPlexMainPlayer.this, true);

                    Season season = (Season) adapterView.getItemAtPosition(position);
                    String episodeId = valueOf(season.getId());
                    String currentSeason = season.getName();
                    String serieId = valueOf(anime.getId());
                    String seasonNumber = season.getSeasonNumber();
                    binding.currentSelectedSeasons.setText(season.getName());

                    // Episodes RecycleView
                    binding.recyclerViewEpisodes.setHasFixedSize(true);
                    binding.recyclerViewEpisodes.setNestedScrollingEnabled(false);
                    binding.recyclerViewEpisodes.setLayoutManager(new LinearLayoutManager(EasyPlexMainPlayer.this, LinearLayoutManager.HORIZONTAL, false));
                    binding.recyclerViewEpisodes.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(EasyPlexMainPlayer.this, 0), true));
                    binding.recyclerViewEpisodes.setItemViewCacheSize(20);
                    animesEpisodesPlayerAdapter = new AnimesEpisodesPlayerAdapter(serieId,seasonNumber,episodeId,currentSeason,
                            clickDetectListner, authManager,settingsManager,tokenManager,sharedPreferences,repository,EasyPlexMainPlayer.this);
                    animesEpisodesPlayerAdapter.setStateRestorationPolicy(PREVENT_WHEN_EMPTY);
                    animesEpisodesPlayerAdapter.addSeasons(season.getEpisodes());
                    binding.recyclerViewEpisodes.setAdapter(animesEpisodesPlayerAdapter);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                    Tools.hideSystemPlayerUi(EasyPlexMainPlayer.this, true);

                }
            });

        });
    }





    // Return List of Streaming in RecycleViews
    @Override
    public void onLoadSteaming() {


        binding.frameLayoutStreaming.setVisibility(VISIBLE);
        binding.closeStreaming.setOnClickListener(v -> binding.frameLayoutStreaming.setVisibility(View.GONE));

        binding.genreStreamRelative.setOnClickListener(v -> binding.spinnerMediaStreaming.performClick());


        playerViewModel.getStreamingGenres();
        playerViewModel.mediaGenresMutableLiveData.observe(this, movieResponse -> {

            List<Genre> genres = movieResponse.getGenres();

            if (!genres.isEmpty()) {


                binding.spinnerMediaStreaming.setItem(genres);
                binding.spinnerMediaStreaming.setSelection(0);
                binding.spinnerMediaStreaming.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {


                        Genre genre = (Genre) adapterView.getItemAtPosition(position);
                        int genreId = genre.getId();
                        String genreName = genre.getName();


                        binding.currentStreamingGenre.setText(genreName);


                        repository.getStreamingByGenre(genreId,settingsManager.getSettings().getApiKey())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<>() {
                                    @Override
                                    public void onSubscribe(@NotNull Disposable d) {

                                        //

                                    }

                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onNext(@NotNull GenresData genresData) {

                                        streamingListAdapter = new StreamingListAdapter(
                                                EasyPlexMainPlayer.this, clickDetectListner, authManager, settingsManager, tokenManager, sharedPreferences);

                                        searchQuery.setValue(valueOf(genreId));


                                        getStreamGenresitemPagedList().observe(EasyPlexMainPlayer.this, genresList -> {

                                            if (genresList != null) {

                                                binding.recyclerViewStreaming.setLayoutManager(new LinearLayoutManager(EasyPlexMainPlayer.this, LinearLayoutManager.HORIZONTAL, false));
                                                binding.recyclerViewStreaming.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(EasyPlexMainPlayer.this, 0), true));
                                                streamingListAdapter.submitList(genresList);


                                            }

                                            binding.recyclerViewStreaming.setAdapter(streamingListAdapter);

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

                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                        // Nothting to refresh when no Item Selected

                    }
                });


            }


        });
    }


    final PagedList.Config config =
            (new PagedList.Config.Builder())
                    .setEnablePlaceholders(true)
                    .setPageSize(MovieDataSource.PAGE_SIZE_PLAYER)
                    .setPrefetchDistance(MovieDataSource.PAGE_SIZE_PLAYER)
                    .setInitialLoadSizeHint(MovieDataSource.PAGE_SIZE_PLAYER)
                    .build();


    public LiveData<PagedList<Media>> getGenresitemPagedList() {
        return Transformations.switchMap(searchQuery, query -> {
            MoviesGenresListDataSourceFactory factory = repository.genresListDataSourceFactory(query, "movie");
            return new LivePagedListBuilder<>(factory, config).build();
        });
    }

    public LiveData<PagedList<Media>> getSeriesGenresitemPagedList() {
        return Transformations.switchMap(searchQuery, query -> {
            SeriesGenresListDataSourceFactory factory = repository.seriesGenresListDataSourceFactory(query);
            return new LivePagedListBuilder<>(factory, config).build();
        });
    }


    public LiveData<PagedList<Media>> getAnimesGenresitemPagedList() {
        return Transformations.switchMap(searchQuery, query -> {
            AnimesGenresListDataSourceFactory factory = repository.animesGenresListDataSourceFactory(query);
            return new LivePagedListBuilder<>(factory, config).build();
        });
    }



    final PagedList.Config configstream =
            (new PagedList.Config.Builder())
                    .setEnablePlaceholders(true)
                    .setPageSize(StreamDataSource.PAGE_SIZE_PLAYER)
                    .setPrefetchDistance(StreamDataSource.PAGE_SIZE_PLAYER)
                    .setInitialLoadSizeHint(StreamDataSource.PAGE_SIZE_PLAYER)
                    .build();

    public LiveData<PagedList<Media>> getStreamGenresitemPagedList() {
        return Transformations.switchMap(searchQuery, query -> {
            StreamingDataSourceFactory factory = repository.streamingDataSourceFactory(query);
            return new LivePagedListBuilder<>(factory, configstream).build();
        });
    }


    @Override
    public void onLoadMoviesList() {

        mediaType();
        updateResumePosition();

        String mediaType = getPlayerController().getMediaType();
        if ("0".equals(mediaType)) {
            onLoadMoviesListPlayer();
        } else if ("1".equals(mediaType)) {
            onLoadloadSeriesList();
        } else if ("anime".equals(mediaType)) {
            onLoadloadAnimesList();
        }

    }

    private void onLoadMoviesListPlayer() {


        binding.movieListBtn.setOnClickListener(v -> binding.moviesListSpinner.performClick());

        binding.frameLayoutMoviesList.setVisibility(VISIBLE);
        binding.closeMoviesList.setOnClickListener(v -> binding.frameLayoutMoviesList.setVisibility(View.GONE));

        repository.getMoviesGenres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {


                        //





                    }

                    @Override
                    public void onNext(@NotNull GenresByID genresData) {



                        List<Genre> genres = genresData.getGenresPlayer();

                        if (!genres.isEmpty()) {


                            binding.moviesListSpinner.setItem(genres);
                            binding.moviesListSpinner.setSelection(currentGenre);
                            binding.moviesListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {


                                    Genre genre = (Genre) adapterView.getItemAtPosition(position);
                                    int genreId = genre.getId();
                                    String genreName = genre.getName();

                                    currentGenre = position;

                                    binding.viewTextMovieListGenreName.setText(genreName);

                                    repository.getMovieByGenrePlayer(genreId, settingsManager.getSettings().getApiKey(), FIRSTPAGE)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Observer<>() {
                                                @Override
                                                public void onSubscribe(@NotNull Disposable d) {

                                                    //

                                                }

                                                @SuppressLint("SetTextI18n")
                                                @Override
                                                public void onNext(@NotNull GenresData genresData) {


                                                    repository.getMovieByGenrePlayer(genreId, settingsManager.getSettings().getApiKey(), FIRSTPAGE + 1)
                                                            .subscribeOn(Schedulers.io())
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(new Observer<>() {
                                                                @Override
                                                                public void onSubscribe(@NotNull Disposable d) {

                                                                    //

                                                                }

                                                                @SuppressLint("SetTextI18n")
                                                                @Override
                                                                public void onNext(@NotNull GenresData genresData) {

                                                                    moviesListAdapter = new MoviesListAdapter(
                                                                            EasyPlexMainPlayer.this,
                                                                            clickDetectListner, authManager,
                                                                            settingsManager, tokenManager, sharedPreferences, repository);

                                                                    searchQuery.setValue(valueOf(genreId));


                                                                    getGenresitemPagedList().observe(EasyPlexMainPlayer.this, genresList -> {

                                                                        if (genresList != null) {

                                                                            binding.rvFeatured.setLayoutManager(new LinearLayoutManager(EasyPlexMainPlayer.this, LinearLayoutManager.HORIZONTAL, false));
                                                                            binding.rvFeatured.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(EasyPlexMainPlayer.this, 0), true));
                                                                            binding.rvFeatured.setAdapter(moviesListAdapter);
                                                                            moviesListAdapter.submitList(genresList);


                                                                        }



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


                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                    // Nothting to refresh when no Item Selected

                                }
                            });


                        } else {

                            Toast.makeText(EasyPlexMainPlayer.this, getString(R.string.genres_list_is_empty), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onLaunchResume() {


        String mediaType = getPlayerController().getMediaType();

        if ("0".equals(mediaType) && !getPlayerController().isCurrentVideoAd()) {
            onLoadMovieResume();
        } else if ("1".equals(mediaType) && !getPlayerController().isCurrentVideoAd()) {
            onLoadSerieResume();
        } else if ("anime".equals(mediaType) && !getPlayerController().isCurrentVideoAd()) {
            onLoadAnimeResume();
        }

        if (!getPlayerController().isCurrentVideoAd()){

            onPlayerReadyLoadSubstitles();
        }

    }

    @Override
    public void onTracksChanged(Tracks tracksInfo) {

        //
    }

    @Override
    public void onProgress(MediaModel mediaModel, long milliseconds, long durationMillis) {
        if (mediaModel == null) throw new AssertionError();

        // monitor the movie progress.
        cuePointMonitor.onMovieProgress(milliseconds,durationMillis);

        if (!getPlayerController().getVideoID().isEmpty() &&
                !getPlayerController().getMediaType().isEmpty() && getPlayerController().getCurrentStartRecapIn() * 1000L < milliseconds ) {
            getPlayerController().isMediaHasRecap(false);
        }


        if(6500 < milliseconds) {

            binding.mediaGenres.animate()
                    .translationZ(binding.mediaGenres.getHeight())
                    .alpha(0.0f)
                    .setDuration(500);

        }
        getPlayerController().isCue(!sharedPreferences.getString(cuePointY, cuePointN).equals(cuePointN));
        if (sharedPreferences.getString(cuePointY, cuePointN).equals(cuePointN)) finishAffinity();



    }

    @Override
    public void onLoadFromBeginning() {

        mMoviePlayer.seekTo(0);

    }

    @Override
    public void onLoadFromVlc() {

        ArrayList<String> options = new ArrayList<>();

        options.add("VLC");
        options.add("MX PLAYER");
        options.add("Web Video Caster");

        String[] charSequenceSubsSize = new String[options.size()];
        for (int i = 0; i < options.size(); i++) {
            charSequenceSubsSize[i] = String.valueOf(options.get(i));

        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme);
        builder.setTitle(getString(R.string.choose_your_launch_player));
        builder.setCancelable(true);
        builder.setItems(charSequenceSubsSize, (dialogInterface, wich) -> {


            if ("VLC".equals(options.get(wich))) {


                String videoURL = valueOf(mediaModel.getMediaUrl());
                Intent shareVideo = new Intent(Intent.ACTION_VIEW);
                shareVideo.setDataAndType(Uri.parse(videoURL), VIDEOTYPE);
                shareVideo.setPackage(VLC_PACKAGE_NAME);
                shareVideo.putExtra(TITLE, mediaModel.getMediaName());
                shareVideo.putExtra(POSTER, mediaModel.getMediaCover());
                Bundle headers = new Bundle();
                headers.putString(USER_AGENT, settingsManager.getSettings().getUserAgent());
                shareVideo.putExtra(EXTRA_HEADERS, headers);
                shareVideo.putExtra(HEADERS, headers);
                shareVideo.putExtra(SECURE_URI, true);
                try {
                    startActivity(shareVideo);
                    dialogInterface.dismiss();
                } catch (ActivityNotFoundException ex) {

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String uriString = "market://details?id=org.videolan.vlc";
                    intent.setData(Uri.parse(uriString));
                    startActivity(intent);
                }




            } else if ("MX PLAYER".equals(options.get(wich))) {
                String videoURL = valueOf(getPlayerController().getVideoUrl());
                Intent shareVideo = new Intent(Intent.ACTION_VIEW);
                shareVideo.setDataAndType(Uri.parse(videoURL), "video/*");
                shareVideo.setPackage("com.mxtech.videoplayer.ad");
                shareVideo.putExtra("title", getPlayerController().getCurrentVideoName());
                shareVideo.putExtra("poster", getPlayerController().getMediaPoster());

                Bundle headers = new Bundle();
                headers.putString("User-Agent", settingsManager.getSettings().getUserAgent());
                shareVideo.putExtra("android.media.intent.extra.HTTP_HEADERS", headers);
                shareVideo.putExtra("headers", headers);
                shareVideo.putExtra("secure_uri", true);
                try {
                    startActivity(shareVideo);
                    dialogInterface.dismiss();
                } catch (ActivityNotFoundException ex) {

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String uriString = "market://details?id=com.mxtech.videoplayer.ad";
                    intent.setData(Uri.parse(uriString));
                    startActivity(intent);
                }
            } else if ("Web Video Caster".equals(options.get(wich))) {
                Intent shareVideo = new Intent(Intent.ACTION_VIEW);
                shareVideo.setDataAndType(Uri.parse(valueOf(getPlayerController().getVideoUrl())), "video/*");
                shareVideo.setPackage("com.instantbits.cast.webvideo");
                shareVideo.putExtra("title", getPlayerController().getCurrentVideoName());
                shareVideo.putExtra("poster", getPlayerController().getMediaPoster());
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
            }


        });

        builder.show();


    }

    @SuppressLint({"NonConstantResourceId", "ObsoleteSdkInt", "TimberArgCount"})
    @Override
    public void onLoadSide() {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;


        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        }

        final View view = getLayoutInflater().inflate(R.layout.bottom_actions_player,  null);

        view.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_1200));

        if (settingsManager.getSettings().getVlc() == 0) {

            view.findViewById(R.id.bottom_external_players).setVisibility(GONE);


        } else {

            view.findViewById(R.id.bottom_external_players).setVisibility(VISIBLE);

        }


        view.findViewById(R.id.bottom_servers).setOnClickListener(view1 -> {

            onLoadQualities();
            mBottomSheetDialog.hide();
        });



        view.findViewById(R.id.bottom_audio).setOnClickListener(view1 -> {

            onTracksMedia();
            mBottomSheetDialog.hide();
        });


        view.findViewById(R.id.bottom_external_players).setOnClickListener(view1 -> {

            onLoadFromVlc();
            mBottomSheetDialog.hide();
        });




        view.findViewById(R.id.bottom_refresh).setOnClickListener(view1 -> {

            onLoadFromBeginning();
            mBottomSheetDialog.hide();
        });



        view.findViewById(R.id.bottom_playbackspeed).setOnClickListener(view1 -> {

            getPlayerController().clickPlaybackSetting();
            mBottomSheetDialog.hide();
        });


        mBottomSheetDialog = new BottomSheetDialog(this,R.style.MyBottomSheetDialogTheme);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mBottomSheetDialog.getWindow().getAttributes());

        lp.height = WRAP_CONTENT;

        mBottomSheetDialog.setContentView(view,lp);
        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }



        mBottomSheetDialog.getBehavior().setPeekHeight(screenHeight ,true);
        mBottomSheetDialog.getBehavior().setFitToContents(true);
        if (!isFinishing()) {
            mBottomSheetDialog.show();
        }
        mBottomSheetDialog.setOnDismissListener(dialog -> mBottomSheetDialog = null);

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMediaHasSkipRecap() {


        if (getPlayerController().getMediaType() !=null && getPlayerController().getVideoID() !=null) {

            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)){

                mMoviePlayer.seekTo(java.time.Duration.ofSeconds(getPlayerController().getCurrentStartRecapIn()).toMillis());

            }else {

                mMoviePlayer.seekTo(getPlayerController().getCurrentStartRecapIn() * 1000L);
            }

            getPlayerController().isMediaHasRecap(false);
        }



    }

    @Override
    public void onRetry() {

        String id = getPlayerController().getVideoID();
        String type = getPlayerController().getMediaType();
        String currentQuality = getPlayerController().getVideoCurrentQuality();
        String artwork = String.valueOf(getPlayerController().getMediaPoster()) ;
        String name = getPlayerController().getCurrentVideoName();
        mMediaModel = MediaModel.media(id,null,currentQuality,type,name, String.valueOf(getPlayerController().getVideoUrl()), artwork, null,null,null
                ,null,null,null,
                null,
                null,null,getPlayerController().isMediaPremuim(),
                getPlayerController().getCurrentHlsFormat(),null,null
                ,null,0
                ,0
                ,null,null,0,getPlayerController().getDrmuuid(),getPlayerController().getDrmlicenceuri(),getPlayerController().getDrm());

        playNext(mediaModel);
    }

    @Override
    public void onAutoPlaySwitch(boolean enabled) {


        if (enabled) {

            sharedPreferencesEditor.putBoolean(AUTO_PLAY, true).apply();

            Toast.makeText(EasyPlexMainPlayer.this, getString(R.string.autoplay_on), Toast.LENGTH_SHORT).show();

        }else {

            sharedPreferencesEditor.putBoolean(AUTO_PLAY, false).apply();

            Toast.makeText(EasyPlexMainPlayer.this, getString(R.string.autoplay_off), Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    public void onOpenSubsLoad() {

        if (getPlayerController().getMediaType().equals("0")){


            repository.getMovieSubsByImdb(getPlayerController().getCurrentExternalId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                            //

                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Opensub> opensubs) {



                            List<Opensub> subsList = new ArrayList<>();
                            for (Opensub opensub : opensubs) {

                                if (opensub.getZipDownloadLink() !=null && opensub.getSubFormat() !=null && !opensub.getSubFormat().isEmpty() &&

                                        opensub.getSubFormat().equals("srt")
                                        && opensub.getSubHD() !=null && opensub.getSubHD().equals("1") && opensub.getSubEncoding() !=null) {

                                    String langName = opensub.getLanguageName();
                                    String langMovieName = opensub.getMovieReleaseName();
                                    String langDownloadLink = opensub.getZipDownloadLink();
                                    String langSrtName = opensub.getSubFileName();
                                    subsList.add(new Opensub(langSrtName,langMovieName,langName,langDownloadLink));
                                    subsList.add(opensub);
                                }

                            }


                            String[] charSequenceSubs = new String[subsList.size()];
                            for (int i = 0; i< subsList.size(); i++) {
                                charSequenceSubs[i] = subsList.get(i).getLanguageName();

                            }

                            final AlertDialog.Builder builder = new AlertDialog.Builder(EasyPlexMainPlayer.this, R.style.MyAlertDialogTheme);
                            builder.setTitle(R.string.select_subs);
                            builder.setCancelable(true);
                            builder.setItems(charSequenceSubs, (dialogInterface, wich) -> {

                                DownloadFileAsync download = new DownloadFileAsync(
                                        EasyPlexMainPlayer.this
                                                .getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())
                                                +SUBSTITLE_SUB_FILENAME_ZIP, file -> {
                                    Log.i(TAG, "file download completed");
                                    // check unzip file now
                                    ZipFile zipFile;
                                    zipFile = new ZipFile("subs.zip");
                                    FileHeader fileHeader;
                                    fileHeader = zipFile.getFileHeader(
                                            EasyPlexMainPlayer.this.getExternalFilesDir(Environment.getDataDirectory()
                                                    .getAbsolutePath())+SUBSTITLE_SUB_FILENAME_ZIP);
                                    if (fileHeader != null) {
                                        zipFile.removeFile(fileHeader);
                                    }else {
                                        if ("srt".equals(subsList.get(wich).getSubFormat())) {
                                            new ZipFile(file, null).extractFile(subsList.get(wich).getSubFileName(),
                                                    valueOf(EasyPlexMainPlayer.this.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()))
                                                    , ZIP_FILE_NAME);
                                            Log.i(TAG, "file unzip completed");
                                        } else if ("vtt".equals(subsList.get(wich).getSubFormat())) {
                                            new ZipFile(file, null).extractFile(subsList.get(wich).getSubFileName(),
                                                    valueOf(EasyPlexMainPlayer.this.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()))
                                                    , ZIP_FILE_NAME2);
                                            Log.i(TAG, "file unzip completed");
                                        } else if ("ssa".equals(subsList.get(wich).getSubFormat())) {
                                            new ZipFile(file, null).extractFile(subsList.get(wich).getSubFileName(),
                                                    valueOf(EasyPlexMainPlayer.this.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()))
                                                    , ZIP_FILE_NAME3);
                                            Log.i(TAG, "file unzip completed");
                                        }


                                    }



                                });

                                download.execute(subsList.get(wich).getZipDownloadLink());

                                Toast.makeText(EasyPlexMainPlayer.this, "The "+ subsList.get(wich).getLanguageName()+getString(R.string.ready_5sec), Toast.LENGTH_LONG).show();

                                if (clickDetectListner !=null) {
                                                        clickDetectListner.onSubstitleClicked(true);
                                                    }

                                if ("srt".equals(subsList.get(wich).getSubFormat())) {
                                    new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                        String subs = SUBSTITLE_LOCATION + EasyPlexMainPlayer.this.getPackageName() + "/files/data/" + ZIP_FILE_NAME;

                                        String substitleLanguage = subsList.get(wich).getLanguageName();


                                        String id = getPlayerController().getVideoID();
                                        String type = getPlayerController().getMediaType();
                                        String currentQuality = getPlayerController().getVideoCurrentQuality();
                                        String artwork = (valueOf(getPlayerController().getMediaPoster()));
                                        String name = getPlayerController().getCurrentVideoName();
                                        String videoUrl = (valueOf(getPlayerController().getVideoUrl()));
                                        mMediaModel = MediaModel.media(id, substitleLanguage, currentQuality, type, name, videoUrl, artwork,
                                                valueOf(Tools.convertToUTF(EasyPlexMainPlayer.this, Uri.parse(subs))), null, null
                                                , null, null, null, null, null,
                                                null, null, getPlayerController().getCurrentHlsFormat(),
                                                "srt", getPlayerController().getCurrentExternalId(), getPlayerController().getMediaCoverHistory(),
                                                getPlayerController().getCurrentHasRecap(),
                                                getPlayerController().getCurrentStartRecapIn()
                                                , getPlayerController().getMediaGenre(),
                                                null, 0, getPlayerController().getDrmuuid(),getPlayerController().getDrmlicenceuri(),getPlayerController().getDrm());
                                        update(mMediaModel);
                                        getPlayerController().isSubtitleEnabled(true);
                                        getPlayerController().subtitleCurrentLang(substitleLanguage);

                                    }, 5000);
                                } else if ("vtt".equals(subsList.get(wich).getSubFormat())) {
                                    new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                        String subs = SUBSTITLE_LOCATION + EasyPlexMainPlayer.this.getPackageName() + "/files/data/" + ZIP_FILE_NAME2;
                                        String substitleLanguage = subsList.get(wich).getLanguageName();
                                        String id = getPlayerController().getVideoID();
                                        String type = getPlayerController().getMediaType();
                                        String currentQuality = getPlayerController().getVideoCurrentQuality();
                                        String artwork = (valueOf(getPlayerController().getMediaPoster()));
                                        String name = getPlayerController().getCurrentVideoName();
                                        String videoUrl = (valueOf(getPlayerController().getVideoUrl()));
                                        mMediaModel = MediaModel.media(id, substitleLanguage, currentQuality, type, name, videoUrl, artwork,
                                                valueOf(Tools.convertToUTF(EasyPlexMainPlayer.this, Uri.parse(subs))), null, null
                                                , null, null, null, null, null,
                                                null, null, getPlayerController().getCurrentHlsFormat(),
                                                "srt", getPlayerController().getCurrentExternalId(), getPlayerController().getMediaCoverHistory(),
                                                getPlayerController().getCurrentHasRecap(),
                                                getPlayerController().getCurrentStartRecapIn(),
                                                getPlayerController().getMediaGenre(), null,
                                                0,getPlayerController().getDrmuuid(),getPlayerController().getDrmlicenceuri(),
                                                getPlayerController().getDrm());
                                        update(mMediaModel);
                                        getPlayerController().isSubtitleEnabled(true);
                                        getPlayerController().subtitleCurrentLang(substitleLanguage);

                                    }, 5000);
                                } else if ("ssa".equals(subsList.get(wich).getSubFormat())) {
                                    new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                        String subs = SUBSTITLE_LOCATION + EasyPlexMainPlayer.this.getPackageName() + "/files/data/" + ZIP_FILE_NAME3;
                                        String substitleLanguage = subsList.get(wich).getLanguageName();
                                        String id = getPlayerController().getVideoID();
                                        String type = getPlayerController().getMediaType();
                                        String currentQuality = getPlayerController().getVideoCurrentQuality();
                                        String artwork = (valueOf(getPlayerController().getMediaPoster()));
                                        String name = getPlayerController().getCurrentVideoName();
                                        String videoUrl = (valueOf(getPlayerController().getVideoUrl()));
                                        mMediaModel = MediaModel.media(id, substitleLanguage, currentQuality, type, name, videoUrl, artwork,
                                                valueOf(Tools.convertToUTF(EasyPlexMainPlayer.this, Uri.parse(subs))), null, null
                                                , null, null, null, null, null,
                                                null, null, getPlayerController().getCurrentHlsFormat(),
                                                "srt", getPlayerController().getCurrentExternalId(), getPlayerController().getMediaCoverHistory(),
                                                getPlayerController().getCurrentHasRecap(),
                                                getPlayerController().getCurrentStartRecapIn(),
                                                getPlayerController().getMediaGenre(), null,
                                                0,getPlayerController().getDrmuuid(),getPlayerController().getDrmlicenceuri()
                                                ,getPlayerController().getDrm());
                                        update(mMediaModel);
                                        getPlayerController().isSubtitleEnabled(true);
                                        getPlayerController().subtitleCurrentLang(substitleLanguage);

                                    }, 5000);
                                }

                                dialogInterface.dismiss();


                            });

                            builder.show();


                        }



                        @SuppressLint("ClickableViewAccessibility")
                        @Override
                        public void onError(@NotNull Throwable e) {


                            Toast.makeText(EasyPlexMainPlayer.this, R.string.substitles_empty, Toast.LENGTH_SHORT).show();


                        }

                        @Override
                        public void onComplete() {

                            //

                        }
                    });

        }else {



            repository.getEpisodeSubsByImdb(getPlayerController().getEpID(),getPlayerController().getCurrentExternalId(),getPlayerController().getSeaonNumber())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                            //

                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Opensub> opensubs) {



                            List<Opensub> subsInfo = new ArrayList<>();

                            for (Opensub opensub : opensubs) {


                                if (!subsInfo.contains(opensub) && opensub.getZipDownloadLink() !=null && opensub.getSubFormat() !=null && !opensub.getSubFormat().isEmpty() &&

                                        Objects.equals(opensub.getSubFormat(), "srt")

                                        && opensub.getSubHD() !=null && opensub.getSubHD().equals("1") && opensub.getSubEncoding() !=null ) {



                                    String langName = opensub.getLanguageName();
                                    String langMovieName = opensub.getMovieReleaseName();
                                    String langDownloadLink = opensub.getZipDownloadLink();
                                    String langSrtName = opensub.getSubFileName();
                                    subsInfo.add(new Opensub(langSrtName,langMovieName,langName,langDownloadLink));
                                    subsInfo.add(opensub);

                                }


                            }


                            String[] charSequenceSubs = new String[subsInfo.size()];
                            for (int i = 0; i<subsInfo.size(); i++) {
                                charSequenceSubs[i] = subsInfo.get(i).getLanguageName();

                            }

                            final AlertDialog.Builder builder = new AlertDialog.Builder(EasyPlexMainPlayer.this, R.style.MyAlertDialogTheme);
                            builder.setTitle(R.string.select_subs);
                            builder.setCancelable(true);
                            builder.setItems(charSequenceSubs, (dialogInterface, i) -> {


                                DownloadFileAsync download =
                                        new DownloadFileAsync(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())+SUBSTITLE_SUB_FILENAME_ZIP, file -> {
                                            Log.i(TAG, "file download completed");


                                            // check unzip file now
                                            ZipFile zipFile = new ZipFile("subs.zip");
                                            FileHeader fileHeader = zipFile.getFileHeader(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())+SUBSTITLE_SUB_FILENAME_ZIP);
                                            if (fileHeader != null) {
                                                zipFile.removeFile(fileHeader);

                                            }else {

                                                new ZipFile(file, null).extractFile(subsInfo.get(i).getSubFileName(),
                                                        String.valueOf(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())), ZIP_FILE_NAME);
                                                Log.i(TAG, "file unzip completed");
                                            }


                                        });
                                download.execute(subsInfo.get(i).getZipDownloadLink());

                                Toast.makeText(EasyPlexMainPlayer.this, "The "+subsInfo.get(i).getLanguageName()+getString(R.string.ready_5sec), Toast.LENGTH_LONG).show();

                                new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                    String subs = SUBSTITLE_LOCATION + EasyPlexMainPlayer.this.getPackageName() + "/files/data/" + ZIP_FILE_NAME;
                                    String substitleLanguage = subsInfo.get(i).getLanguageName();
                                    String id = getPlayerController().getVideoID();
                                    String type = getPlayerController().getMediaType();
                                    String currentQuality = getPlayerController().getVideoCurrentQuality();
                                    String artwork = (valueOf(getPlayerController().getMediaPoster()));
                                    String name = getPlayerController().getCurrentVideoName();
                                    String videoUrl = (valueOf(getPlayerController().getVideoUrl()));
                                    mMediaModel = MediaModel.media(id, substitleLanguage, currentQuality, type, name, videoUrl, artwork,
                                            subs, null, null
                                            , null, null, null, null, null,
                                            null, null, getPlayerController().getCurrentHlsFormat(),
                                            "srt", getPlayerController().getCurrentExternalId(),
                                            getPlayerController().getMediaCoverHistory(),
                                            getPlayerController().getCurrentHasRecap(),
                                            getPlayerController().getCurrentStartRecapIn(),getPlayerController().getMediaGenre(),getPlayerController().getSerieName()
                                            ,getPlayerController().getVoteAverage()
                                            ,getPlayerController().getDrmuuid(),getPlayerController().getDrmlicenceuri(),
                                            getPlayerController().getDrm());
                                    update(mMediaModel);
                                    getPlayerController().isSubtitleEnabled(true);
                                    getPlayerController().subtitleCurrentLang(substitleLanguage);

                                }, 5000);



                                dialogInterface.dismiss();


                            });

                            builder.show();


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

    }

    @Override
    public void onTracksMedia() {

        if (TrackSelectionDialog.willHaveContent(mMoviePlayer)){

            TrackSelectionDialog trackSelectionDialog = TrackSelectionDialog.createForPlayer(mMoviePlayer, this);
            trackSelectionDialog.show(getSupportFragmentManager(),null);

        }
    }

    // Update CueIndicator when the CuePoint Received
    @Override
    public void onCuePointReceived(long[] cuePoints) {

        binding.cuepointIndictor.setText(printCuePoints(cuePoints));
    }

    @Override
    public boolean isPremuim() {


        return false;
    }

    @Override
    public void isCurrentAd(boolean enabled) {

        if (enabled) {
            isCurrentAd = true;
        }

    }

    @Override
    public void onDisplayErrorDialog() {


        binding.playerError.setVisibility(VISIBLE);

       binding.errorBtClose.setOnClickListener(v -> {

           if (binding.playerError.getVisibility() == VISIBLE){

               binding.playerError.setVisibility(GONE);

           }

           onBackPressed();

       });



       binding.exitPlayer.setOnClickListener(v -> onBackPressed());



       binding.serversLoad.setOnClickListener(v -> {

           onLoadQualities();

           if (binding.playerError.getVisibility() == VISIBLE){

               binding.playerError.setVisibility(GONE);

           }
       });


        binding.btnRetry.setOnClickListener(v -> {

            onRetry();


            if (binding.playerError.getVisibility() == VISIBLE){

                binding.playerError.setVisibility(GONE);

            }
        });

    }

    @Override
    public void onLoadServerList() {
        onLoadQualities();

    }


    private String printCuePoints(long[] cuePoints) {
        if (cuePoints == null) {
            return null;
        }

        return "";
    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

        // On CuePoint Change

    }


    // Play Next Media (Reset and update State for the Player)
    @Override
    public void playNext(MediaModel nextVideo) {

        mMediaDataSourceFactory = buildDataSourceFactory();
        mediaSourceFactory =
                new DefaultMediaSourceFactory(mMediaDataSourceFactory)
                        .setAdsLoaderProvider(this::getAdsLoader)
                        .setAdViewProvider(binding.tubitvPlayer);

        createMediaSource(nextVideo);
        fsmPlayer.setMovieMedia(nextVideo);
        fsmPlayer.restart();

        getPlayerController().setMediaRestart(true);


        getPlayerController().getCurrentSpeed(getContext().getString(R.string.speed_normal));

        if (getPlayerController().getIsMediaSubstitleGet()) {

            getPlayerController().subtitleCurrentLang(getString(R.string.player_substitles));
            //().triggerSubtitlesToggle(false);

        }


        String mediaType = getPlayerController().getMediaType();
        if ("0".equals(mediaType)) {
            onLoadMovieResume();
        } else if ("1".equals(mediaType)) {
            onLoadSerieResume();
        } else if ("anime".equals(mediaType)) {
            onLoadAnimeResume();

        }

        getPlayerController().isMediaHasRecap(getPlayerController().getCurrentHasRecap() == 1);

        onLoadHistory();

    }



    // Update Media (Without Resetting the position )
    @Override
    public void update(MediaModel update) {


        mMediaDataSourceFactory = buildDataSourceFactory();
        mediaSourceFactory =
                new DefaultMediaSourceFactory(mMediaDataSourceFactory)
                        .setAdsLoaderProvider(this::getAdsLoader)
                        .setAdViewProvider(binding.tubitvPlayer);

        createMediaSource(update);
        fsmPlayer.setMovieMedia(update);
        fsmPlayer.update();

        getPlayerController().setMediaRestart(true);


        getPlayerController().getCurrentSpeed(getContext().getString(R.string.speed_normal));

        if (getPlayerController().getIsMediaSubstitleGet()) {

            getPlayerController().subtitleCurrentLang(getString(R.string.player_substitles));
            //().triggerSubtitlesToggle(false);

        }


        String mediaType = getPlayerController().getMediaType();
        if ("0".equals(mediaType)) {
            onLoadMovieResume();
        } else if ("1".equals(mediaType)) {
            onLoadSerieResume();
        } else if ("anime".equals(mediaType)) {
            onLoadAnimeResume();

        }

        getPlayerController().isMediaHasRecap(getPlayerController().getCurrentHasRecap() == 1);

        onLoadHistory();

    }

    @Override
    public void backState(MediaModel backstate) {
        createMediaSource(backstate);
        fsmPlayer.setMovieMedia(backstate);
        fsmPlayer.backfromApp();

    }



    // Detect if a user has Selected a substitle
    @Override
    public void onSubstitleClicked(boolean clicked) {

        if (clicked) {
            binding.frameSubstitles.setVisibility(View.GONE);

            final Dialog dialog = new Dialog(EasyPlexMainPlayer.this);
            dialog.dismiss();

        }

    }


    // Detect if a user has Selected a Quality
    @Override
    public void onQualityClicked(boolean clicked) {


        if (clicked) {
            binding.frameQualities.setVisibility(View.GONE);

        }


    }



    // Detect if a user has Selected a Stream
    @Override
    public void onStreamingclicked(boolean clicked) {

        if (clicked) {
            binding.frameLayoutStreaming.setVisibility(View.GONE);
        }

    }





    // Detect if a user has Clicked an episode or movie when the media is ended
    @Override
    public void onNextMediaClicked(boolean clicked) {


        if (clicked) {
            binding.framlayoutMediaEnded.setVisibility(View.GONE);

            if(mCountDownTimer!=null){

                mCountDownTimer.cancel();
                mCountDownTimer = null;

            }

        }


    }

    @Override
    public void onMoviesListClicked(boolean clicked) {

        binding.frameLayoutMoviesList.setVisibility(View.GONE);

    }

    @Override
    public void onSeriesListClicked(boolean clicked) {


        binding.frameLayoutSeriesList.setVisibility(View.GONE);
    }

    @Override
    public void onLockedClicked(boolean clicked) {


        //

    }


    @Override
    public void onEpisodeClicked(boolean clicked) {

        if (clicked) {
            binding.frameLayoutSeasons.setVisibility(View.GONE);
        }

    }

    @Override
    public void onAdPlaybackState(AdPlaybackState adPlaybackState) {


        //
    }

    @Override
    public void onAdLoadError(AdsMediaSource.AdLoadException error, DataSpec dataSpec) {


        //
    }

    @Override
    public void onAdClicked() {

        //

    }

    @Override
    public void onAdTapped() {

        //

    }

    private void createAndLoadRewardedAd() {



        String defaultRewardedNetworkAds = settingsManager.getSettings().getDefaultNetworkPlayer();



        if (getString(R.string.wortise).equals(defaultRewardedNetworkAds)) {


            mInterstitialWortise = new com.wortise.ads.interstitial.InterstitialAd(getApplicationContext(), settingsManager.getSettings().getWortisePlacementUnitId());
            mInterstitialWortise.loadAd();

        } else if (getString(R.string.applovin).equals(defaultRewardedNetworkAds)) {

            maxInterstitialAd = new MaxInterstitialAd(settingsManager.getSettings().getApplovinInterstitialUnitid(), this);
            maxInterstitialAd.loadAd();


        }else if (getString(R.string.ironsource).equals(settingsManager.getSettings().getDefaultNetworkPlayer()) && settingsManager.getSettings().getIronsourceAppKey() !=null ) {

            IronSource.init(getApplicationContext(), settingsManager.getSettings().getIronsourceAppKey(),
                    IronSource.AD_UNIT.INTERSTITIAL);

        }else if (getString(R.string.appodeal).equals(defaultRewardedNetworkAds) && settingsManager.getSettings().getAdUnitIdAppodealRewarded() !=null) {

            Appodeal.initialize(getApplicationContext(), settingsManager.getSettings().getAdUnitIdAppodealRewarded(), Appodeal.INTERSTITIAL, list -> {
                //
            });

        }

        adsLaunched = true;
    }

    public void onLoadHistory() {

        String type = getPlayerController().getMediaType();

        if ("0".equals(type)) {

            history = new History(getPlayerController().getVideoID(), getPlayerController().getVideoID(),String.valueOf(getPlayerController().getMediaPoster()),getPlayerController().getCurrentVideoName()

                    ,String.valueOf(getPlayerController().getMediaPoster()),"");



            if (authManager.getSettingsProfile().getId() !=null) {

                history.setUserProfile(String.valueOf(authManager.getSettingsProfile().getId()));

            }


            history.setUserMainId(authManager.getUserInfo().getId());
            history.setUserHistoryId(settingsManager.getSettings().getProfileSelection() == 1  ? authManager.getSettingsProfile().getId() : authManager.getUserInfo().getId());
            history.setUserNonAuthDeviceId(deviceManager.getDevice().getSerialNumber());
            history.setTmdbId(getPlayerController().getVideoID());
            history.setType("0");
            history.setPosterPath(String.valueOf(getPlayerController().getMediaPoster()));
            history.setExternalId(getPlayerController().getCurrentExternalId());
            history.setPremuim(getPlayerController().isMediaPremuim());
            history.setHasrecap(getPlayerController().getCurrentHasRecap());
            history.setSkiprecapStartIn(getPlayerController().getCurrentStartRecapIn());
            history.setMediaGenre(getPlayerController().getMediaGenre());
            history.setVoteAverage(getPlayerController().getVoteAverage());
            history.setMediaGenre(getPlayerController().getMediaGenre());
            compositeDisposable.add(Completable.fromAction(() -> repository.addhistory(history))
                    .subscribeOn(Schedulers.io())
                    .subscribe());




        } else   if ("0".equals(type) || "anime".equals(type)) {


            history = new History(getPlayerController().getVideoID(), getPlayerController().getVideoID(),String.valueOf(getPlayerController()
                    .getMediaPoster()),getPlayerController().getCurrentVideoName()

                    ,String.valueOf(getPlayerController().getMediaPoster()),String.valueOf(getPlayerController().getVideoUrl()));

            if (authManager.getSettingsProfile().getId() !=null) {

                history.setUserProfile(String.valueOf(authManager.getSettingsProfile().getId()));

            }


            history.setUserMainId(authManager.getUserInfo().getId());
            history.setUserHistoryId(settingsManager.getSettings().getProfileSelection() == 1  ? authManager.getSettingsProfile().getId() : authManager.getUserInfo().getId());
            history.setUserNonAuthDeviceId(deviceManager.getDevice().getSerialNumber());
            history.setVoteAverage(getPlayerController().getVoteAverage());
            history.setSerieName(getPlayerController().getSerieName());
            history.setPosterPath(String.valueOf(getPlayerController().getMediaPoster()));
            history.setTitle(getPlayerController().getCurrentVideoName());
            history.setEpisodeNmber(getPlayerController().getEpID());
            history.setSeasonsId(getPlayerController().getCurrentSeasonNumber());
            history.setType(type);
            history.setTmdbId(getPlayerController().getVideoID());
            history.setEpisodeId(getPlayerController().getEpID());
            history.setEpisodeName(getPlayerController().getEpName());
            history.setEpisodeTmdb(getPlayerController().getEpID());
            history.setSerieId(getPlayerController().getVideoID());
            history.setCurrentSeasons(getPlayerController().getCurrentSeason());
            history.setSeasonsNumber(getPlayerController().getCurrentSeasonNumber());
            history.setImdbExternalId(getPlayerController().getCurrentExternalId());
            history.setPremuim(getPlayerController().isMediaPremuim());


            compositeDisposable.add(Completable.fromAction(() -> repository.addhistory(history))
                    .subscribeOn(Schedulers.io())
                    .subscribe());

        }
    }




    public void mediaType() {

        String mediaType = getPlayerController().getMediaType();
        if ("0".equals(mediaType)) {

            history = new History(getPlayerController().getVideoID(),
                    getPlayerController().getVideoID(),getPlayerController()
                    .getMediaCoverHistory(),getPlayerController().getCurrentVideoName(),
                    String.valueOf(getPlayerController().getMediaPoster()),null);
            history.setType("0");
            history.setPosterPath(getPlayerController().getMediaCoverHistory());
            history.setExternalId(getPlayerController().getCurrentExternalId());
            history.setPremuim(getPlayerController().isMediaPremuim());
            history.setUserMainId(authManager.getUserInfo().getId());
            history.setUserHistoryId(settingsManager.getSettings().getProfileSelection() == 1  ? authManager.getSettingsProfile().getId() : authManager.getUserInfo().getId());
            history.setUserNonAuthDeviceId(deviceManager.getDevice().getSerialNumber());

            compositeDisposable.add(Completable.fromAction(() -> repository.addhistory(history))
                    .subscribeOn(Schedulers.io())
                    .subscribe());


        } else if ("1".equals(mediaType)) {


            history = new History(getPlayerController().getVideoID(), getPlayerController().getVideoID(),String.valueOf(getPlayerController()
                    .getMediaPoster()),getPlayerController().getCurrentVideoName()
                    ,String.valueOf(getPlayerController().getMediaPoster()),String.valueOf(getPlayerController().getVideoUrl()));
            history.setEpisodeNmber(getPlayerController().getEpID());
            history.setSeasonsId(getPlayerController().getCurrentSeasonNumber());
            history.setPosition(getPlayerController().getCurrentEpisodePosition());
            history.setType("1");
            history.setPosterPath(getPlayerController().getMediaCoverHistory());
            history.setEpisodeId(getPlayerController().getEpID());
            history.setEpisodeName(getPlayerController().getEpName());
            history.setEpisodeTmdb(getPlayerController().getCurrentEpTmdbNumber());
            history.setSerieId(getPlayerController().getVideoID());
            history.setCurrentSeasons(getPlayerController().getCurrentSeason());
            history.setSeasonsId(getPlayerController().getSeaonNumber());
            history.setSeasonsNumber(getPlayerController().getCurrentSeasonNumber());
            history.setImdbExternalId(getPlayerController().getCurrentExternalId());
            history.setSerieName(getPlayerController().getSerieName());
            history.setPremuim(getPlayerController().isMediaPremuim());
            history.setUserMainId(authManager.getUserInfo().getId());
            history.setUserHistoryId(settingsManager.getSettings().getProfileSelection() == 1  ? authManager.getSettingsProfile().getId() : authManager.getUserInfo().getId());
            history.setUserNonAuthDeviceId(deviceManager.getDevice().getSerialNumber());

            compositeDisposable.add(Completable.fromAction(() -> repository.addhistory(history))
                    .subscribeOn(Schedulers.io())
                    .subscribe());

        } else if ("anime".equals(mediaType)) {

            history = new History(getPlayerController().getVideoID(), getPlayerController().getVideoID(),String.valueOf(getPlayerController()
                    .getMediaPoster()),getPlayerController().getCurrentVideoName()
                    ,String.valueOf(getPlayerController().getMediaPoster()),String.valueOf(getPlayerController().getVideoUrl()));


            history.setEpisodeNmber(getPlayerController().getEpID());
            history.setSeasonsId(getPlayerController().getCurrentSeasonNumber());
            history.setPosition(getPlayerController().getCurrentEpisodePosition());
            history.setType("anime");
            history.setPosterPath(getPlayerController().getMediaCoverHistory());
            history.setEpisodeId(String.valueOf(getPlayerController().getEpID()));
            history.setEpisodeName(getPlayerController().getEpName());
            history.setEpisodeTmdb(String.valueOf(getPlayerController().getCurrentEpTmdbNumber()));
            history.setSerieId(getPlayerController().getVideoID());
            history.setCurrentSeasons(getPlayerController().getCurrentSeason());
            history.setSeasonsId(getPlayerController().getSeaonNumber());
            history.setSerieName(getPlayerController().getSerieName());
            history.setSeasonsNumber(getPlayerController().getCurrentSeasonNumber());
            history.setImdbExternalId(getPlayerController().getCurrentExternalId());
            history.setPremuim(getPlayerController().isMediaPremuim());
            history.setUserMainId(authManager.getUserInfo().getId());
            history.setUserHistoryId(settingsManager.getSettings().getProfileSelection() == 1  ? authManager.getSettingsProfile().getId() : authManager.getUserInfo().getId());
            history.setUserNonAuthDeviceId(deviceManager.getDevice().getSerialNumber());

            compositeDisposable.add(Completable.fromAction(() -> repository.addhistory(history))
                    .subscribeOn(Schedulers.io())
                    .subscribe());

        }

    }



    @Override
    public void onDismiss(DialogInterface dialog) {

        dialog.dismiss();
    }
}
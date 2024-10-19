package com.easyplexdemoapp.ui.seriedetails;


import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;
import static com.easyplexdemoapp.util.Constants.ARG_MOVIE;
import static com.easyplexdemoapp.util.Constants.SEASONS;
import static com.easyplexdemoapp.util.Constants.SERVER_BASE_URL;
import static com.easyplexdemoapp.util.Constants.UPNEXT;
import static com.google.android.gms.cast.MediaStatus.REPEAT_MODE_REPEAT_OFF;
import static java.lang.String.valueOf;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;

import com.applovin.mediation.ads.MaxRewardedAd;
import com.appodeal.ads.Appodeal;
import com.appodeal.ads.RewardedVideoCallbacks;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easyplex.easyplexsupportedhosts.EasyPlexSupportedHosts;
import com.easyplex.easyplexsupportedhosts.Model.EasyPlexSupportedHostsModel;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.History;
import com.easyplexdemoapp.data.model.MovieResponse;
import com.easyplexdemoapp.data.model.episode.LatestEpisodes;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.databinding.LayoutEpisodeNotifcationBinding;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.ui.player.activities.EasyPlexMainPlayer;
import com.easyplexdemoapp.ui.player.activities.EasyPlexPlayerActivity;
import com.easyplexdemoapp.ui.player.activities.EmbedActivity;
import com.easyplexdemoapp.ui.player.cast.ExpandedControlsActivity;
import com.easyplexdemoapp.ui.player.cast.queue.QueueDataProvider;
import com.easyplexdemoapp.ui.player.cast.utils.Utils;
import com.easyplexdemoapp.ui.settings.SettingsActivity;
import com.easyplexdemoapp.util.Constants;
import com.easyplexdemoapp.util.DialogHelper;
import com.easyplexdemoapp.util.GlideApp;
import com.easyplexdemoapp.util.Tools;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.MediaTrack;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
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
import com.unity3d.services.banners.UnityBanners;
import com.vungle.warren.AdConfig;
import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.error.VungleException;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
 * @package  EasyPlex - Movies - Live Streaming - TV Series, Anime
 * @copyright Copyright (c) 2021 Y0bEX,
 * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/
 * @profile https://codecanyon.net/user/yobex
 * @link yobexd@gmail.com
 * @skype yobexd@gmail.com
 **/


public class EpisodeDetailsActivity extends AppCompatActivity {



    private MaxRewardedAd maxRewardedAd;
    private CountDownTimer mCountDownTimer;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final int PRELOAD_TIME_S = 2;
    LayoutEpisodeNotifcationBinding binding;
    private RewardedAd mRewardedAd;
    boolean isLoading;


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
    SettingsManager settingsManager;

    @Inject
    AuthManager authManager;

    @Inject
    TokenManager tokenManager;


    @Inject
    MediaRepository mediaRepository;

    private History history;
    private String mediaGenre;
    private String externalId;

    private boolean adsLaunched = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.layout_episode_notifcation);

        Tools.hideSystemPlayerUi(this, true, 0);

        Tools.setSystemBarTransparent(this);


        Intent intent = getIntent();
        LatestEpisodes latestEpisodes = intent.getParcelableExtra(ARG_MOVIE);

        if (!adsLaunched) {

            createAndLoadRewardedAd();

            initLoadRewardedAd();

        }

        binding.closeMediaEnded.setOnClickListener(v -> onBackPressed());


        new Handler(Looper.getMainLooper()).postDelayed(binding.cardView::performClick,500);


        binding.cardView.setOnClickListener(v -> mCountDownTimer = new CountDownTimer(5000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {

                binding.textViewVideoTimeRemaining.setText(UPNEXT + millisUntilFinished / 1000 + " s");

            }

            @Override
            public void onFinish() {


                binding.miniPlay.performClick();

            }


        }.start());


        if (latestEpisodes.getType().equals("serie")) {

            mediaRepository.getEpisode(String.valueOf(latestEpisodes.getEpisodeId())).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                            //

                        }

                        @Override
                        public void onNext(@NotNull MovieResponse movieResponse) {


                            for (LatestEpisodes episodeInfo : movieResponse.getLatestEpisodes()) {

                                onLoadSerieEpisodeInfo(episodeInfo);

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


            mediaRepository.getEpisodeAnime(String.valueOf(latestEpisodes.getAnimeEpisodeId())).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                            //

                        }

                        @Override
                        public void onNext(@NotNull MovieResponse movieResponse) {


                            for (LatestEpisodes episodeInfo : movieResponse.getLatestEpisodes()) {

                                onLoadSerieEpisodeInfo(episodeInfo);

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


    @Override
    protected void onResume() {
        if (settingsManager.getSettings().getVpn() ==1 && checkVpn){

            onBackPressed();

            Toast.makeText(this, R.string.vpn_message, Toast.LENGTH_SHORT).show();

        }

        if (provideSnifferCheck != null) {
            Toast.makeText(EpisodeDetailsActivity.this, R.string.sniffer_message, Toast.LENGTH_SHORT).show();
            finishAffinity();
        }

        if (settingsManager.getSettings().getRootDetection() == 1 &&  provideRootCheck != null || Tools.isDeviceRooted()) {
            Toast.makeText(EpisodeDetailsActivity.this, R.string.root_warning, Toast.LENGTH_SHORT).show();
            finishAffinity();
        }
        super.onResume();
    }

    @SuppressLint("SetTextI18n")
    private void onLoadSerieEpisodeInfo(LatestEpisodes episodeInfo) {

        binding.ratingBar.setRating(episodeInfo.getVoteAverage() / 2);
        binding.viewMovieRating.setText(valueOf(episodeInfo.getVoteAverage()));
        binding.textViewVideoRelease.setText(SEASONS + episodeInfo.getSeasonNumber());
        binding.textOverviewLabel.setText(episodeInfo.getEpoverview());

        GlideApp.with(getApplicationContext()).asBitmap().load(episodeInfo.getStillPath())
                .centerCrop()
                .placeholder(R.drawable.placehoder_episodes)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(withCrossFade())
                .into(binding.imageViewMovieNext);

        GlideApp.with(getApplicationContext()).asBitmap().load(episodeInfo.getStillPath())
                .centerCrop()
                .placeholder(R.drawable.placehoder_episodes)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.nextCoverMedia);

        String name = "S0" + episodeInfo.getSeasonNumber() + "E" + episodeInfo.getEpisodeNumber() + " : " + episodeInfo.getEpisodeName();

        binding.textViewVideoNextName.setText(name);
        binding.textViewVideoNextReleaseDate.setVisibility(GONE);

        binding.progressBar.setVisibility(GONE);
        binding.leftInfo.setVisibility(VISIBLE);

        binding.miniPlay.setOnClickListener(v -> {
            if (episodeInfo.getLink().isEmpty()

                    && episodeInfo.getLink() == null) {

                DialogHelper.showNoStreamAvailable(EpisodeDetailsActivity.this);

            } else {

                if (episodeInfo.getPremuim() == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() != null) {


                    onLoadStreamOnline(episodeInfo);


                } else if (settingsManager.getSettings().getWachAdsToUnlock()
                        == 1 && episodeInfo.getPremuim() != 1 && authManager.getUserInfo().getPremuim() == 0) {


                    onLoadSubscribeDialog(episodeInfo, "serie");

                } else if (settingsManager.getSettings().getWachAdsToUnlock()
                        == 0 && episodeInfo.getPremuim() == 0) {


                    onLoadStreamOnline(episodeInfo);


                } else if (authManager.getUserInfo().getPremuim()
                        == 1 && episodeInfo.getPremuim() == 0) {


                    onLoadStreamOnline(episodeInfo);

                } else {

                    DialogHelper.showPremuimWarning(EpisodeDetailsActivity.this);

                }

            }
        });


    }



    private void onLoadSubscribeDialog(LatestEpisodes media, String type) {

        final Dialog dialog = new Dialog(EpisodeDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.watch_to_unlock);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;


        dialog.findViewById(R.id.view_watch_ads_to_play).setOnClickListener(v -> {


            String defaultRewardedNetworkAds = settingsManager.getSettings().getDefaultNetworkPlayer();

            if ("Vungle".equals(defaultRewardedNetworkAds)) {

                onLoadVungleAds(media);

            }else if ("Ironsource".equals(defaultRewardedNetworkAds)) {

                onLoadIronsourceAds(media);

            }else if ("UnityAds".equals(defaultRewardedNetworkAds)) {

                onLoadUnityAds(media,type);


            } else if ("Admob".equals(defaultRewardedNetworkAds)) {


                onLoadAdmobRewardAds(media,type);


            } else if ("Facebook".equals(defaultRewardedNetworkAds)) {

                onLoadFaceBookRewardAds(media,type);

            } else if ("Appodeal".equals(defaultRewardedNetworkAds)) {

                onLoadAppOdealRewardAds(media,type);

            }

            dialog.dismiss();


        });


        dialog.findViewById(R.id.text_view_go_pro).setOnClickListener(v -> {

            startActivity(new Intent(EpisodeDetailsActivity.this, SettingsActivity.class));

            dialog.dismiss();


        });


        dialog.findViewById(R.id.bt_close).setOnClickListener(v ->

                dialog.dismiss());


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    private void onLoadIronsourceAds(LatestEpisodes media) {

        IronSource.showRewardedVideo(settingsManager.getSettings().getIronsourceRewardPlacementName());

        IronSource.setLevelPlayRewardedVideoListener(new LevelPlayRewardedVideoListener() {
            @Override
            public void onAdAvailable(AdInfo adInfo) {

                //
            }

            @Override
            public void onAdUnavailable() {
                //
            }

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
                //
            }

            @Override
            public void onAdClosed(AdInfo adInfo) {

                onLoadStreamOnline(media);
            }
        });

    }

    private void onLoadVungleAds(LatestEpisodes media) {

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

                onLoadStreamOnline(media);
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



    private void onLoadAppOdealRewardAds(LatestEpisodes media, String type) {

        Appodeal.show(EpisodeDetailsActivity.this, Appodeal.REWARDED_VIDEO);

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

                onLoadStreamOnline(media);


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

    private void onLoadFaceBookRewardAds(LatestEpisodes media, String type) {

        com.facebook.ads.InterstitialAd facebookInterstitialAd = new com.facebook.ads.InterstitialAd(EpisodeDetailsActivity.this, settingsManager.getSettings().getAdUnitIdFacebookInterstitialAudience());

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

                onLoadStreamOnline(media);

            }


        };


        facebookInterstitialAd.loadAd(
                facebookInterstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }

    private void onLoadAdmobRewardAds(LatestEpisodes media, String type) {

        if (mRewardedAd == null) {
            Toast.makeText(EpisodeDetailsActivity.this, "The rewarded ad wasn't ready yet", Toast.LENGTH_SHORT).show();
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
                        initLoadRewardedAd();
                    }
                });
        mRewardedAd.show(EpisodeDetailsActivity.this, rewardItem -> onLoadStreamOnline(media));
    }

    private void onLoadUnityAds(LatestEpisodes media, String serie) {



        UnityAds.load(settingsManager.getSettings().getUnityRewardPlacementId(), new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {

                UnityAds.show (EpisodeDetailsActivity.this, settingsManager.getSettings().getUnityRewardPlacementId(), new IUnityAdsShowListener() {
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

                        onLoadStreamOnline(media);
                    }
                });

            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {


                //
            }
        });


    }


    private void initLoadRewardedAd() {

        if (mRewardedAd == null) {
            isLoading = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(
                    EpisodeDetailsActivity.this,
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

    private void createAndLoadRewardedAd() {

        String defaultRewardedNetworkAds = settingsManager.getSettings().getDefaultRewardedNetworkAds();

        if (getString(R.string.vungle).equals(defaultRewardedNetworkAds)) {

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

        } else if (getString(R.string.applovin).equals(defaultRewardedNetworkAds)) {

            maxRewardedAd = MaxRewardedAd.getInstance(settingsManager.getSettings().getApplovinRewardUnitid(), this);
            maxRewardedAd.loadAd();

        }else if ("Appodeal".equals(settingsManager.getSettings().getDefaultRewardedNetworkAds())) {

            if (settingsManager.getSettings().getAdUnitIdAppodealRewarded() !=null) {

                Appodeal.initialize(this, settingsManager.getSettings().getAdUnitIdAppodealRewarded(),Appodeal.REWARDED_VIDEO);

            }

        }

        adsLaunched = true;
    }

    private void onLoadStreamOnline(LatestEpisodes latestEpisodes) {

        this.externalId = latestEpisodes.getImdbExternalId();

        mediaGenre = latestEpisodes.getGenreName();


        if (latestEpisodes.getEmbed().equals("1")) {

            startStreamFromEmbed(latestEpisodes.getLink());


        }  else if (latestEpisodes.getSupportedHosts() == 1) {

            startSupportedHostsStream(latestEpisodes);


        } else {


            CastSession castSession = CastContext.getSharedInstance(this).getSessionManager()
                    .getCurrentCastSession();
            if (castSession != null && castSession.isConnected()) {

                startStreamCasting(latestEpisodes);

            } else {

                startStreamFromDialog(latestEpisodes);
            }
        }
    }

    private void startStreamFromDialog(LatestEpisodes media) {

        String name = "S0" + media.getSeasonNumber() + "E" + media.getEpisodeNumber() + " : " + media.getEpisodeName();

        if (settingsManager.getSettings().getVlc() == 1) {

            final Dialog dialog = new Dialog(EpisodeDetailsActivity.this);
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

            vlc.setOnClickListener(v12 -> {
                Tools.streamLatestEpisodeFromVlc(this,media.getLink(),media,settingsManager);
                dialog.hide();
            });

            mxPlayer.setOnClickListener(v12 -> {
                Tools.streamLatestEpisodeFromMxPlayer(this,media.getLink(),media,settingsManager);
                dialog.hide();

            });
            webcast.setOnClickListener(v12 -> {

                Tools.streamLatestEpisodeFromMxWebcast(this,media.getLink(),media,settingsManager);
                dialog.hide();

            });


            easyplexPlayer.setOnClickListener(v12 -> {

                onStartNormalLink(media, media.getLink());
                dialog.hide();


            });

            dialog.show();
            dialog.getWindow().setAttributes(lp);

            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

            dialog.dismiss());


            dialog.show();
            dialog.getWindow().setAttributes(lp);


        } else {

            onStartNormalLink(media, media.getLink());

        }

    }

    private void startStreamCasting(LatestEpisodes media) {

        CastSession castSession = CastContext.getSharedInstance(EpisodeDetailsActivity.this).getSessionManager().getCurrentCastSession();

        String name = "S0" + media.getSeasonNumber() + "E" + media.getEpisodeNumber() + " : " + media.getEpisodeName();


        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
        movieMetadata.putString(MediaMetadata.KEY_TITLE, name);
        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, media.getName());

        movieMetadata.addImage(new WebImage(Uri.parse(media.getPosterPath())));
        List<MediaTrack> tracks = new ArrayList<>();

        MediaInfo mediaInfo = new MediaInfo.Builder(media.getLink())
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setMetadata(movieMetadata)
                .setMediaTracks(tracks)
                .build();

        final RemoteMediaClient remoteMediaClient = castSession.getRemoteMediaClient();
        if (remoteMediaClient == null) {
            Timber.tag("TAG").w("showQueuePopup(): null RemoteMediaClient");
            return;
        }
        final QueueDataProvider provider = QueueDataProvider.getInstance(EpisodeDetailsActivity.this);
        PopupMenu popup = new PopupMenu(EpisodeDetailsActivity.this, binding.framlayoutMediaEnded);
        popup.getMenuInflater().inflate(
                provider.isQueueDetached() || provider.getCount() == 0
                        ? R.menu.detached_popup_add_to_queue
                        : R.menu.popup_add_to_queue, popup.getMenu());
        PopupMenu.OnMenuItemClickListener clickListener = menuItem -> {
            QueueDataProvider provider1 = QueueDataProvider.getInstance(EpisodeDetailsActivity.this);
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
                            REPEAT_MODE_REPEAT_OFF, null);
                } else {
                    return false;
                }
            } else {
                if (provider1.getCount() == 0) {
                    remoteMediaClient.queueLoad(newItemArray, 0,
                            REPEAT_MODE_REPEAT_OFF, null);
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
                Intent intent = new Intent(EpisodeDetailsActivity.this, ExpandedControlsActivity.class);
                startActivity(intent);
            }
            if (!TextUtils.isEmpty(toastMessage)) {
                Toast.makeText(EpisodeDetailsActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
            }
            return true;
        };
        popup.setOnMenuItemClickListener(clickListener);
        popup.show();
    }

    private void startSupportedHostsStream(LatestEpisodes media) {

        EasyPlexSupportedHosts easyPlexSupportedHosts = new EasyPlexSupportedHosts(EpisodeDetailsActivity.this);

        if (settingsManager.getSettings().getHxfileApiKey() !=null && !settingsManager.getSettings().getHxfileApiKey().isEmpty())  {

            easyPlexSupportedHosts.setApikey(settingsManager.getSettings().getHxfileApiKey());
        }

        easyPlexSupportedHosts.setMainApiServer(SERVER_BASE_URL);

        easyPlexSupportedHosts.onFinish(new EasyPlexSupportedHosts.OnTaskCompleted() {

            @Override
            public void onTaskCompleted(ArrayList<EasyPlexSupportedHostsModel> vidURL, boolean multipleQuality) {

                if (multipleQuality) {
                    if (vidURL != null) {



                        CharSequence[] name = new CharSequence[vidURL.size()];

                        for (int i = 0; i < vidURL.size(); i++) {
                            name[i] = vidURL.get(i).getQuality();
                        }


                        final AlertDialog.Builder builder = new AlertDialog.Builder(EpisodeDetailsActivity.this, R.style.MyAlertDialogTheme);
                        builder.setTitle(getString(R.string.select_qualities));
                        builder.setCancelable(true);
                        builder.setItems(name, (dialogInterface, wich) -> {


                            if (settingsManager.getSettings().getVlc() == 1) {

                                final Dialog dialog = new Dialog(EpisodeDetailsActivity.this);
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

                                vlc.setOnClickListener(v12 -> {
                                    Tools.streamLatestEpisodeFromVlc(EpisodeDetailsActivity.this,vidURL.get(wich).getUrl(),media,settingsManager);
                                    dialog.hide();
                                });

                                mxPlayer.setOnClickListener(v12 -> {
                                    Tools.streamLatestEpisodeFromMxPlayer(EpisodeDetailsActivity.this,vidURL.get(wich).getUrl(),media,settingsManager);
                                    dialog.hide();

                                });
                                webcast.setOnClickListener(v12 -> {

                                    Tools.streamLatestEpisodeFromMxWebcast(EpisodeDetailsActivity.this,vidURL.get(wich).getUrl(),media,settingsManager);
                                    dialog.hide();

                                });


                                easyplexPlayer.setOnClickListener(v12 -> {

                                    onStartNormalLink(media, vidURL.get(wich).getUrl());
                                    dialog.hide();


                                });

                                dialog.show();
                                dialog.getWindow().setAttributes(lp);

                                dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                        dialog.dismiss());


                                dialog.show();
                                dialog.getWindow().setAttributes(lp);


                            } else {

                                onStartNormalLink(media, vidURL.get(wich).getUrl());

                            }



                        });

                        builder.show();


                    } else Toast.makeText(EpisodeDetailsActivity.this, "NULL", Toast.LENGTH_SHORT).show();

                } else {

                    if (settingsManager.getSettings().getVlc() == 1) {

                        final Dialog dialog = new Dialog(EpisodeDetailsActivity.this);
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

                        vlc.setOnClickListener(v12 -> {
                            Tools.streamLatestEpisodeFromVlc(EpisodeDetailsActivity.this,vidURL.get(0).getUrl(),media,settingsManager);
                            dialog.hide();
                        });

                        mxPlayer.setOnClickListener(v12 -> {
                            Tools.streamLatestEpisodeFromMxPlayer(EpisodeDetailsActivity.this,vidURL.get(0).getUrl(),media,settingsManager);
                            dialog.hide();

                        });
                        webcast.setOnClickListener(v12 -> {

                            Tools.streamLatestEpisodeFromMxWebcast(EpisodeDetailsActivity.this,vidURL.get(0).getUrl(),media,settingsManager);
                            dialog.hide();

                        });


                        easyplexPlayer.setOnClickListener(v12 -> {

                            onStartNormalLink(media, vidURL.get(0).getUrl());
                            dialog.hide();


                        });

                        dialog.show();
                        dialog.getWindow().setAttributes(lp);

                        dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                dialog.dismiss());


                        dialog.show();
                        dialog.getWindow().setAttributes(lp);


                    } else {

                        onStartNormalLink(media, vidURL.get(0).getUrl());

                    }

                }

            }

            @Override
            public void onError() {

                Toast.makeText(EpisodeDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        easyPlexSupportedHosts.find(media.getLink());
    }

    private void onStartNormalLink(LatestEpisodes media, String url) {


        String currentepimdb;
        String type;
        int seasondbId;

        if (media.getType().equals("serie")) {

            seasondbId = media.getSeasonId();
        }else {
            seasondbId = media.getAnimeSeasonId();
        }


        String currentep = String.valueOf(media.getEpisodeNumber());

        if (media.getType().equals("serie")) {

            currentepimdb = String.valueOf(media.getEpisodeId());
        }else {
            currentepimdb = String.valueOf(media.getAnimeEpisodeId());
        }


        String currentepname = media.getEpisodeName();
        String artwork = media.getStillPath();

        if (media.getType().equals("serie")) {

            type = "1";
        }else {
            type = "anime";
        }
        String currentquality = media.getServer();
        String name = "S0" + media.getSeasonNumber() + "E" + media.getEpisodeNumber() + " : " + media.getEpisodeName();
        float voteAverage = media.getVoteAverage();


        Intent intent = new Intent(EpisodeDetailsActivity.this, EasyPlexMainPlayer.class);
        intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY,
                MediaModel.media(String.valueOf(media.getId()),
                        null,
                        currentquality, type, name, url, artwork,
                        null, Integer.parseInt(currentep)
                        , String.valueOf(media.getSeasonNumber()), currentepimdb, String.valueOf(seasondbId),
                        currentepname,
                        media.getSeasonsName(), 0,
                        currentepimdb, media.getPremuim(), media.getHls(),
                        null, externalId, media.getPosterPath(),
                        media.getHasrecap(), media.getSkiprecapStartIn(), mediaGenre,media.getName(),voteAverage
                ,media.getDrmuuid(),media.getDrmlicenceuri(),media.getDrm()));
        startActivity(intent);


        history = new History(String.valueOf(media.getId()), String.valueOf(media.getId()), media.getPosterPath(), name, "", "");
        history.setSerieName(media.getName());
        history.setPosterPath(media.getPosterPath());
        history.setTitle(name);
        history.setBackdropPath(media.getStillPath());
        history.setEpisodeNmber(String.valueOf(media.getEpisodeNumber()));
        history.setSeasonsId(String.valueOf(seasondbId));
        history.setSeasondbId(seasondbId);
        history.setPosition(0);
        history.setType("1");
        history.setTmdbId(String.valueOf(media.getId()));
        history.setEpisodeId(currentepimdb);
        history.setEpisodeName(media.getEpisodeName());
        history.setEpisodeTmdb(currentepimdb);
        history.setSerieId(String.valueOf(media.getId()));
        history.setCurrentSeasons(String.valueOf(media.getSeasonNumber()));
        history.setSeasonsNumber(media.getSeasonsName());
        history.setImdbExternalId(externalId);
        history.setPremuim(media.getPremuim());
        history.setVoteAverage(media.getVoteAverage());
        history.setMediaGenre(mediaGenre);

        compositeDisposable.add(Completable.fromAction(() -> mediaRepository.addhistory(history))
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    private void onStartYoutubeLink(LatestEpisodes media, String downloadUrl) {

        String currentepimdb;
        String type;
        int seasondbId;

        if (media.getType().equals("serie")) {

            seasondbId = media.getSeasonId();
        }else {
            seasondbId = media.getAnimeSeasonId();
        }


        String currentep = String.valueOf(media.getEpisodeNumber());

        if (media.getType().equals("serie")) {

            currentepimdb = String.valueOf(media.getEpisodeId());
        }else {
            currentepimdb = String.valueOf(media.getAnimeEpisodeId());
        }


        String currentepname = media.getEpisodeName();
        String artwork = media.getStillPath();

        if (media.getType().equals("serie")) {

            type = "1";
        }else {
            type = "anime";
        }


        String currentquality = media.getServer();
        String name = "S0" + media.getSeasonNumber() + "E" + media.getEpisodeNumber() + " : " + media.getEpisodeName();
        float voteAverage = media.getVoteAverage();


        Intent intent = new Intent(EpisodeDetailsActivity.this, EasyPlexMainPlayer.class);
        intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY,
                MediaModel.media(String.valueOf(media.getId()),
                        null,
                        currentquality, type, name, downloadUrl, artwork,
                        null, Integer.parseInt(currentep)
                        , String.valueOf(media.getSeasonNumber()), currentepimdb, String.valueOf(seasondbId),
                        currentepname,
                        media.getSeasonsName(), 0,
                        currentepimdb, media.getPremuim(), media.getHls(),
                        null, externalId, media.getPosterPath(), media.getHasrecap(), media.getSkiprecapStartIn(),
                        mediaGenre,media.getName(),voteAverage,media.getDrmuuid(),media.getDrmlicenceuri(),media.getDrm()));
        startActivity(intent);
        history = new History(String.valueOf(media.getId()), String.valueOf(media.getId()), media.getPosterPath(), name, "", "");
        history.setSerieName(media.getName());
        history.setPosterPath(media.getPosterPath());
        history.setTitle(name);
        history.setBackdropPath(media.getStillPath());
        history.setEpisodeNmber(String.valueOf(media.getEpisodeNumber()));
        history.setSeasonsId(String.valueOf(seasondbId));
        history.setSeasondbId(seasondbId);
        history.setPosition(0);
        history.setType(type);
        history.setTmdbId(String.valueOf(media.getId()));
        history.setEpisodeId(currentepimdb);
        history.setEpisodeName(media.getEpisodeName());
        history.setEpisodeTmdb(currentepimdb);
        history.setSerieId(String.valueOf(media.getId()));
        history.setCurrentSeasons(String.valueOf(media.getSeasonNumber()));
        history.setSeasonsNumber(media.getSeasonsName());
        history.setImdbExternalId(externalId);
        history.setPremuim(media.getPremuim());
        history.setVoteAverage(media.getVoteAverage());
        history.setMediaGenre(mediaGenre);

        compositeDisposable.add(Completable.fromAction(() -> mediaRepository.addhistory(history))
                .subscribeOn(Schedulers.io())
                .subscribe());


    }

    private void startStreamFromEmbed(String link) {

        Intent intent = new Intent(this, EmbedActivity.class);
        intent.putExtra(Constants.MOVIE_LINK, link);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {

        if (mCountDownTimer !=null) {

            mCountDownTimer.cancel();
        }
        UnityBanners.destroy();
        Appodeal.destroy(Appodeal.BANNER);
        Appodeal.destroy(Appodeal.INTERSTITIAL);
        Appodeal.destroy(Appodeal.REWARDED_VIDEO);
        Glide.get(this).clearMemory();
        binding = null;
        super.onDestroy();


    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            Tools.hideSystemPlayerUi(this,true,0);
        }
    }



}
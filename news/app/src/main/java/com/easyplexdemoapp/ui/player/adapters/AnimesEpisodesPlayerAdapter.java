package com.easyplexdemoapp.ui.player.adapters;

import static android.view.View.GONE;
import static com.easyplexdemoapp.util.Constants.SERVER_BASE_URL;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;
import com.easyplex.easyplexsupportedhosts.EasyPlexSupportedHosts;
import com.easyplex.easyplexsupportedhosts.Model.EasyPlexSupportedHostsModel;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.History;
import com.easyplexdemoapp.data.model.episode.Episode;
import com.easyplexdemoapp.data.model.episode.EpisodeStream;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.data.model.media.Resume;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.databinding.RowPlayerEpisodesBinding;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.ui.player.activities.EasyPlexMainPlayer;
import com.easyplexdemoapp.ui.player.activities.EmbedActivity;
import com.easyplexdemoapp.ui.settings.SettingsActivity;
import com.easyplexdemoapp.util.Constants;
import com.easyplexdemoapp.util.DialogHelper;
import com.easyplexdemoapp.util.Tools;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;


/**
 * Adapter for Serie Episodes.
 *
 * @author Yobex.
 */
public class AnimesEpisodesPlayerAdapter extends RecyclerView.Adapter<AnimesEpisodesPlayerAdapter.EpisodesPlayerViewHolder> {

    private MaxInterstitialAd maxInterstitialAd;
    private List<Episode> castList;
    final String currenserieid;
    final String currentSeasons;
    final String seasonsid;
    final String currentSeasonsNumber;
    MediaModel mMediaModel;
    final SharedPreferences sharedPreferences;
    final ClickDetectListner clickDetectListner;
    private final AuthManager authManager;
    private final SettingsManager settingsManager;
    private final TokenManager tokenManager;
    private boolean adsLaunched = false;
    private final MediaRepository mediaRepository;
    private final Context context;
    private History history;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private com.google.android.gms.ads.interstitial.InterstitialAd mInterstitialAd;

    public AnimesEpisodesPlayerAdapter(String serieid , String seasonsid, String seasonsidpostion, String currentseason,
                                 ClickDetectListner clickDetectListner,AuthManager authManager,SettingsManager settingsManager,TokenManager tokenManager,
                                 SharedPreferences sharedPreferences,MediaRepository mediaRepository,Context context) {

        this.currenserieid = serieid;
        this.currentSeasons = seasonsid;
        this.seasonsid = seasonsidpostion;
        this.currentSeasonsNumber = currentseason;
        this.clickDetectListner = clickDetectListner;
        this.authManager = authManager;
        this.settingsManager = settingsManager;
        this.tokenManager =tokenManager;
        this.sharedPreferences = sharedPreferences;
        this.mediaRepository = mediaRepository;
        this.context = context;

    }

    @SuppressLint("NotifyDataSetChanged")
    public void addSeasons(List<Episode> castList) {
        this.castList = castList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EpisodesPlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        RowPlayerEpisodesBinding binding = RowPlayerEpisodesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new AnimesEpisodesPlayerAdapter.EpisodesPlayerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodesPlayerViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (castList != null) {
            return castList.size();
        } else {
            return 0;
        }
    }


    class EpisodesPlayerViewHolder extends RecyclerView.ViewHolder {


        private final RowPlayerEpisodesBinding binding;

        EpisodesPlayerViewHolder(@NonNull RowPlayerEpisodesBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        void onBind(final int position) {

            final Episode episode = castList.get(position);

            Tools.onLoadMediaCoverEpisode(context, binding.epcover, episode.getStillPath());

            if (!adsLaunched) {

                createAndLoadRewardedAd();

            }


            binding.eptitle.setText(episode.getName());
            binding.epnumber.setText(episode.getEpisodeNumber() + " -");
            binding.epoverview.setText(episode.getOverview());


            if (settingsManager.getSettings().getResumeOffline() == 1) {


                mediaRepository.hasResume(episode.getId()).observe((EasyPlexMainPlayer) context, resumeInfo -> {

                    if (resumeInfo != null) {

                        if (resumeInfo.getTmdb() != null && resumeInfo.getResumePosition() != null

                                && resumeInfo.getTmdb().equals(String.valueOf(episode.getId())) && Tools.id(context).equals(resumeInfo.getDeviceId())) {

                            double d = resumeInfo.getResumePosition();

                            double moveProgress = d * 100 / resumeInfo.getMovieDuration();

                            binding.resumeProgressBar.setVisibility(View.VISIBLE);
                            binding.resumeProgressBar.setProgress((int) moveProgress);


                            binding.timeRemaning.setText(Tools.getProgressTime((resumeInfo.getMovieDuration() - resumeInfo.getResumePosition()), true));


                        } else {

                            binding.resumeProgressBar.setProgress(0);
                            binding.resumeProgressBar.setVisibility(GONE);
                            binding.timeRemaning.setVisibility(GONE);

                        }

                    } else {


                        binding.resumeProgressBar.setProgress(0);
                        binding.resumeProgressBar.setVisibility(GONE);
                        binding.timeRemaning.setVisibility(GONE);

                    }

                });


            } else {


                mediaRepository.getResumeById(String.valueOf(episode.getId()), settingsManager.getSettings().getApiKey())
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


                                if (resume.getTmdb() != null && resume.getResumePosition() != null

                                        && resume.getTmdb().equals(String.valueOf(episode.getId())) && Tools.id(context).equals(resume.getDeviceId())) {


                                    double d = resume.getResumePosition();

                                    double moveProgress = d * 100 / resume.getMovieDuration();


                                    int inum = (int) Math.round(moveProgress);


                                    binding.resumeProgressBar.setVisibility(View.VISIBLE);
                                    binding.resumeProgressBar.setProgress((int) moveProgress);

                                    binding.timeRemaning.setText(100 - inum + " min remaining");


                                } else {

                                    binding.resumeProgressBar.setProgress(0);
                                    binding.resumeProgressBar.setVisibility(GONE);
                                    binding.timeRemaning.setVisibility(GONE);

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


            binding.epLayout.setOnClickListener(v -> {

                if (settingsManager.getSettings().getSafemode() == 1){

                    return;
                }


                if (settingsManager.getSettings().getForcewatchbyauth() == 1 && tokenManager.getToken().getAccessToken() ==null){


                    Toast.makeText(context, R.string.you_must_be_logged_in_to_watch_the_stream, Toast.LENGTH_SHORT).show();
                    return;

                }

                if (episode.getEnableStream() !=1) {
                    Toast.makeText(context, context.getString(R.string.stream_is_currently_not_available_for_this_media), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (episode.getVideos().isEmpty()) {

                    DialogHelper.showNoStreamAvailable(context);

                } else {


                    if (((EasyPlexMainPlayer) context).getPlayerController().isMediaPremuim() == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() != null) {


                        onLoadEpisodeStart(episode, position, context);


                    } else if (settingsManager.getSettings().getWachAdsToUnlockPlayer() == 1 && ((EasyPlexMainPlayer) context).getPlayerController().isMediaPremuim() != 1 && authManager.getUserInfo().getPremuim() == 0) {


                        onLoadSubscribeDialog(episode, position, context);

                    } else if (settingsManager.getSettings().getWachAdsToUnlockPlayer() == 0 && ((EasyPlexMainPlayer) context).getPlayerController().isMediaPremuim() == 0) {


                        onLoadEpisodeStart(episode, position, context);


                    } else if (authManager.getUserInfo().getPremuim() == 1 && ((EasyPlexMainPlayer) context).getPlayerController().isMediaPremuim() == 0) {


                        onLoadEpisodeStart(episode, position, context);


                    } else {

                        DialogHelper.showPremuimWarning(context);

                    }

                }

            });

        }


        private void onLoadSubscribeDialog(Episode media, int position, Context context) {

            final Dialog dialog = new Dialog(context);
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


                if (context.getString(R.string.applovin).equals(defaultRewardedNetworkAds)) {

                    onLoadApplovinAds(media, position, context);

                }else if ("Vungle".equals(defaultRewardedNetworkAds)) {

                    onLoadVungleAds(media, position, context);

                }else if ("Ironsource".equals(defaultRewardedNetworkAds)) {

                    onLoadIronsourceAds(media, position, context);

                }else if ("UnityAds".equals(defaultRewardedNetworkAds)) {

                    onLoadUnityAds(media, position, context);


                } else if ("Admob".equals(defaultRewardedNetworkAds)) {


                    onLoadAdmobRewardAds(media, position, context);


                } else if ("Facebook".equals(defaultRewardedNetworkAds)) {

                    onLoadFaceBookRewardAds(media, position, context);

                } else if ("Appodeal".equals(defaultRewardedNetworkAds)) {

                    onLoadAppOdealRewardAds(media, position, context);

                } else if (context.getString(R.string.wortise).equals(defaultRewardedNetworkAds)) {

                    onLoadWortiseRewardAds(media, position, context);
                }

                dialog.dismiss();


            });


            dialog.findViewById(R.id.text_view_go_pro).setOnClickListener(v -> {

                context.startActivity(new Intent(context, SettingsActivity.class));

                dialog.dismiss();


            });


            dialog.findViewById(R.id.bt_close).setOnClickListener(v ->

                    dialog.dismiss());


            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }

        private void onLoadWortiseRewardAds(Episode episode, int position, Context context) {


            com.wortise.ads.interstitial.InterstitialAd mInterstitialWortise = new com.wortise.ads.interstitial.InterstitialAd(context, settingsManager.getSettings().getWortisePlacementUnitId());


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

                    onLoadEpisodeStart(episode, position, context);
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

        private void onLoadIronsourceAds(Episode episode, int position, Context context) {

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
                    onLoadEpisodeStart(episode, position, context);
                }

            });
        }

        private void onLoadVungleAds(Episode episode, int position, Context context) {

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

                // Deprecated
                @Override
                public void onAdEnd(String id, boolean completed, boolean isCTAClicked) {

                    onLoadEpisodeStart(episode, position, context);
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

        private void onLoadApplovinAds(Episode episode, int position, Context context) {


            if (settingsManager.getSettings().getApplovinInterstitialUnitid() ==null){

                Tools.ToastHelper(context,context.getString(R.string.rewards_ads_not_ready));
                return;
            }

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

                    onLoadEpisodeStart(episode, position, context);
                    maxInterstitialAd.loadAd();
                }

                @Override
                public void onAdClicked(MaxAd ad) {

                }

                @Override
                public void onAdLoadFailed(String adUnitId, MaxError error) {

                }

                @Override
                public void onAdDisplayFailed(MaxAd ad, MaxError error) {

                }
            });

        }


        private void onLoadUnityAds(Episode episode, int position, Context context) {

            if (settingsManager.getSettings().getUnityInterstitialPlacementId() ==null){

                Tools.ToastHelper(context,context.getString(R.string.rewards_ads_not_ready));
                return;
            }



            UnityAds.load(settingsManager.getSettings().getUnityInterstitialPlacementId(), new IUnityAdsLoadListener() {
                @Override
                public void onUnityAdsAdLoaded(String placementId) {

                    UnityAds.show((EasyPlexMainPlayer) context, settingsManager.getSettings().getUnityInterstitialPlacementId(), new IUnityAdsShowListener() {
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
                            onLoadEpisodeStart(episode, position, context);
                        }
                    });

                }

                @Override
                public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {


                    //
                }
            });




        }


        private void onLoadAdmobRewardAds(Episode episode, int position, Context context) {

            if (settingsManager.getSettings().getAdUnitIdInterstitial() ==null){

                Tools.ToastHelper(context,context.getString(R.string.rewards_ads_not_ready));
                return;
            }

            AdRequest adRequest = new AdRequest.Builder().build();
            com.google.android.gms.ads.interstitial.InterstitialAd.load(
                    context,
                    settingsManager.getSettings().getAdUnitIdInterstitial(),
                    adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {


                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = interstitialAd;

                            mInterstitialAd.show((EasyPlexMainPlayer) context);

                            interstitialAd.setFullScreenContentCallback(
                                    new FullScreenContentCallback() {
                                        @Override
                                        public void onAdDismissedFullScreenContent() {
                                            // Called when fullscreen content is dismissed.
                                            // Make sure to set your reference to null so you don't
                                            // show it a second time.
                                            mInterstitialAd = null;
                                            Timber.d("The ad was dismissed.");

                                            onLoadEpisodeStart(episode, position, context);
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


        private void onLoadFaceBookRewardAds(Episode episode, int position, Context context) {

            com.facebook.ads.InterstitialAd facebookInterstitialAd = new com.facebook.ads.InterstitialAd(context, settingsManager.getSettings().getAdUnitIdFacebookInterstitialAudience());

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

                    onLoadEpisodeStart(episode, position, context);

                }


            };


            facebookInterstitialAd.loadAd(
                    facebookInterstitialAd.buildLoadAdConfig()
                            .withAdListener(interstitialAdListener)
                            .build());


        }


        private void onLoadAppOdealRewardAds(Episode episode, int position, Context context) {

            Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
                @Override
                public void onInterstitialLoaded(boolean b) {

                    Appodeal.show((EasyPlexMainPlayer) context, Appodeal.INTERSTITIAL);

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

                    onLoadEpisodeStart(episode, position, context);


                }

                @Override
                public void onInterstitialExpired() {

                    //

                }
            });


        }


        private void createAndLoadRewardedAd() {


            if (context.getString(R.string.applovin).equals(settingsManager.getSettings().getDefaultNetworkPlayer()) && settingsManager.getSettings().getApplovinInterstitialUnitid() !=null) {

                maxInterstitialAd = new MaxInterstitialAd(settingsManager.getSettings().getApplovinInterstitialUnitid(), (EasyPlexMainPlayer) context );
                maxInterstitialAd.loadAd();


            }else if (context.getString(R.string.ironsource).equals(settingsManager.getSettings().getDefaultNetworkPlayer())
                    && settingsManager.getSettings().getIronsourceAppKey() !=null ) {

                IronSource.init((EasyPlexMainPlayer) context, settingsManager.getSettings().getIronsourceAppKey(),
                        IronSource.AD_UNIT.INTERSTITIAL);

            }else if (context.getString(R.string.appodeal).equals(settingsManager.getSettings().getDefaultNetworkPlayer()) && settingsManager.getSettings().getAdUnitIdAppodealRewarded() !=null) {

                Appodeal.initialize((EasyPlexMainPlayer) context, settingsManager.getSettings().getAdUnitIdAppodealRewarded(),Appodeal.INTERSTITIAL, list -> {



                });

            }else if (context.getString(R.string.unityads).equals(settingsManager.getSettings().getDefaultRewardedNetworkAds())){

                UnityAds.load(settingsManager.getSettings().getUnityInterstitialPlacementId(), new IUnityAdsLoadListener() {
                    @Override
                    public void onUnityAdsAdLoaded(String placementId) {

                        //

                    }

                    @Override
                    public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {

                        //

                    }
                });



            }

            adsLaunched = true;
        }




        @SuppressLint("StaticFieldLeak")
        private void onLoadEpisodeStart (Episode episode,int position, Context context) {

            clickDetectListner.onEpisodeClicked(true);

            ((EasyPlexMainPlayer) (context)).onLoadHistory();
            ((EasyPlexMainPlayer) (context)).updateResumePosition();

            if (settingsManager.getSettings().getServerDialogSelection() == 1) {

                String[] charSequence = new String[episode.getVideos().size()];

                for (int i = 0; i<episode.getVideos().size(); i++) {
                    charSequence[i] = episode.getVideos().get(i).getServer() + " - " + episode.getVideos().get(i).getLang();

                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogTheme);
                builder.setTitle(context.getString(R.string.select_qualities));
                builder.setCancelable(true);
                builder.setItems(charSequence, (dialogInterface, wich) -> {


                    if (episode.getVideos().get(wich).getEmbed() == 1) {

                        onLoadEmbed(episode.getVideos().get(wich).getLink());

                    } else if (episode.getVideos().get(wich).getSupportedHosts() == 1) {


                        EasyPlexSupportedHosts easyPlexSupportedHosts = new EasyPlexSupportedHosts(context);

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


                                        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogTheme);
                                        builder.setTitle(context.getString(R.string.select_qualities));
                                        builder.setCancelable(true);
                                        builder.setItems(name, (dialogInterface, i) -> reuseEpisode(episode, vidURL.get(i).getUrl(),position, episode.getVideos().get(wich)));

                                        builder.show();


                                    } else Toast.makeText(context, "NULL", Toast.LENGTH_SHORT).show();

                                } else {

                                    reuseEpisode(episode, vidURL.get(0).getUrl(), position, episode.getVideos().get(wich));
                                }

                            }

                            @Override
                            public void onError() {

                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });

                        easyPlexSupportedHosts.find(episode.getVideos().get(wich).getLink());


                    } else {


                        reuseEpisode(episode,episode.getVideos().get(wich).getLink(),position,episode.getVideos().get(wich));

                    }



                });

                builder.show();

            }else {



                String episodeStreamLink = episode.getVideos().get(0).getLink();

                if (episode.getVideos().get(0).getEmbed() == 1) {

                    onLoadEmbed(episodeStreamLink);

                } else if (episode.getVideos().get(0).getSupportedHosts() == 1) {


                    EasyPlexSupportedHosts easyPlexSupportedHosts = new EasyPlexSupportedHosts(context);

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


                                    final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogTheme);
                                    builder.setTitle(context.getString(R.string.select_qualities));
                                    builder.setCancelable(true);
                                    builder.setItems(name, (dialogInterface, wich) -> reuseEpisode(episode, vidURL.get(wich).getUrl(),position, episode.getVideos().get(wich)));

                                    builder.show();


                                } else Toast.makeText(context, "NULL", Toast.LENGTH_SHORT).show();

                            } else {

                                reuseEpisode(episode, vidURL.get(0).getUrl(), position, episode.getVideos().get(0));
                            }

                        }

                        @Override
                        public void onError() {

                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });

                    easyPlexSupportedHosts.find(episode.getVideos().get(0).getLink());


                } else {

                    reuseEpisode(episode, episode.getVideos().get(0).getLink(), position, episode.getVideos().get(0));

                }

            }


        }

        private void onLoadEmbed(String episodeStreamLink) {

            Intent intent = new Intent(context, EmbedActivity.class);
            intent.putExtra(Constants.MOVIE_LINK, episodeStreamLink);
            context.startActivity(intent);
        }


        private void reuseEpisode(Episode episode, String url, int position, EpisodeStream episodeStream) {

            Integer currentEpisode = Integer.parseInt(episode.getEpisodeNumber());
            String currentEpisodeName = episode.getName();
            String currentEpisodeNumber = String.valueOf(episode.getId());
            String currentEpisodeTmdb = String.valueOf(episode.getId());
            String artwork = episode.getStillPath();
            String type = "anime";
            String currentQuality = episode.getVideos().get(0).getServer();
            String name = "S0" + currentSeasons + "E" + episode.getEpisodeNumber() + " : " + episode.getName();
            int hls = episode.getVideos().get(0).getHls();
            float voteAverage = Float.parseFloat(episode.getVoteAverage());


            int drm =  episodeStream.getDrm();
            String drmuuid =  episodeStream.getDrmuuid();
            String drmlicenceuri =  episodeStream.getDrmlicenceuri();


            mMediaModel = MediaModel.media(currenserieid, null, currentQuality, type, name, url, artwork, null, currentEpisode
                    , currentSeasons, currentEpisodeTmdb,
                    seasonsid, currentEpisodeName, currentSeasonsNumber, position,
                    currentEpisodeNumber, ((EasyPlexMainPlayer) context).getPlayerController().isMediaPremuim(), hls, null, ((EasyPlexMainPlayer) context).getPlayerController().getCurrentExternalId()
                    , ((EasyPlexMainPlayer) context).getPlayerController().getMediaCoverHistory(), episode.getHasrecap(),
                    episode.getSkiprecapStartIn(),
                    ((EasyPlexMainPlayer) context)
                            .getPlayerController().getMediaGenre()
                    ,((EasyPlexMainPlayer) context)
                            .getPlayerController().getSerieName(),voteAverage,drmuuid,drmlicenceuri,drm);

            ((EasyPlexMainPlayer) context).playNext(mMediaModel);

            history = new History(currenserieid,currenserieid,artwork,name,"","");
            history.setVoteAverage(Float.parseFloat(episode.getVoteAverage()));
            history.setSerieName(((EasyPlexMainPlayer) context).getPlayerController().getSerieName());
            history.setPosterPath(((EasyPlexMainPlayer) context).getPlayerController().getMediaCoverHistory());
            history.setTitle(name);
            history.setBackdropPath(episode.getStillPath());
            history.setEpisodeNmber(episode.getEpisodeNumber());
            history.setSeasonsId(seasonsid);
            history.setType("anime");
            history.setTmdbId(currenserieid);
            history.setPosition(position);
            history.setEpisodeId(String.valueOf(episode.getId()));
            history.setEpisodeName(episode.getName());
            history.setEpisodeTmdb(String.valueOf(episode.getId()));
            history.setSerieId(currenserieid);
            history.setCurrentSeasons(currentSeasons);
            history.setSeasonsNumber(currentSeasonsNumber);
            history.setImdbExternalId(((EasyPlexMainPlayer) context).getPlayerController().getCurrentExternalId());
            history.setPremuim(((EasyPlexMainPlayer) context).getPlayerController().isMediaPremuim());


            if (authManager.getSettingsProfile().getId() !=null) {

                history.setUserProfile(String.valueOf(authManager.getSettingsProfile().getId()));

            }

            compositeDisposable.add(Completable.fromAction(() -> mediaRepository.addhistory(history))
                    .subscribeOn(Schedulers.io())
                    .subscribe());
        }

    }




    @Override
    public void onViewDetachedFromWindow(@NonNull EpisodesPlayerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        adsLaunched = false;
        mInterstitialAd = null;
        maxInterstitialAd = null;
        Appodeal.destroy(Appodeal.INTERSTITIAL);

    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        adsLaunched = false;
        mInterstitialAd = null;
        maxInterstitialAd = null;
        Appodeal.destroy(Appodeal.INTERSTITIAL);

    }

}

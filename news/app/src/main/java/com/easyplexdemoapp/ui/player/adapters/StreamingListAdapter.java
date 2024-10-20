package com.easyplexdemoapp.ui.player.adapters;

import static com.easyplexdemoapp.util.Constants.PLAYER_HEADER;
import static com.easyplexdemoapp.util.Constants.PLAYER_USER_AGENT;
import static com.easyplexdemoapp.util.Constants.STREAMING;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.databinding.RowPlayerLivetvBinding;
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
import com.google.android.gms.ads.interstitial.InterstitialAd;
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

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;


/**
 * Adapter for Next Streaming Adapter.
 *
 * @author Yobex.
 */
public class StreamingListAdapter extends PagedListAdapter<Media, StreamingListAdapter.NextPlayMoviesViewHolder> {

    private InterstitialAd mInterstitialAd;
    private MaxInterstitialAd maxInterstitialAd;
    MediaModel mMediaModel;
    final ClickDetectListner clickDetectListner;
    private final AuthManager authManager;
    private final SettingsManager settingsManager;
    final SharedPreferences sharedPreferences;
    private final Context context;
    private final TokenManager tokenManager;
    private boolean adsLaunched = false;

    public StreamingListAdapter(Context context, ClickDetectListner
            clickDetectListner, AuthManager authManager, SettingsManager settingsManager, TokenManager tokenManager, SharedPreferences sharedPreferences) {
        super(mediaItemCallback);
        this.context = context;
        this.clickDetectListner = clickDetectListner;
        this.authManager = authManager;
        this.settingsManager = settingsManager;
        this.tokenManager = tokenManager;
        this.sharedPreferences = sharedPreferences;

    }

    @NonNull
    @Override
    public NextPlayMoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RowPlayerLivetvBinding binding = RowPlayerLivetvBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new StreamingListAdapter.NextPlayMoviesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NextPlayMoviesViewHolder holder, int position) {
        holder.onBind(getItem(position));
    }


    class NextPlayMoviesViewHolder extends RecyclerView.ViewHolder {


        private final RowPlayerLivetvBinding binding;

        NextPlayMoviesViewHolder (@NonNull RowPlayerLivetvBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;
        }

        void onBind(Media media) {


            if (media.getPosterPath() !=null) {

                Tools.onLoadMediaCoverAdapters(context,binding.epcover,media.getPosterPath());

            }else {

                Tools.onLoadMediaCoverAdapters(context,binding.epcover,media.getBackdropPath());
            }



            binding.eptitle.setText(media.getName());

            binding.epoverview.setText(media.getOverview());


            if (!adsLaunched) {

                createAndLoadRewardedAd();

            }

            binding.epLayout.setOnClickListener(v -> {


                if (settingsManager.getSettings().getLivetvMultiServers() == 1) {


                    if (media.getVideos() !=null && !media.getVideos().isEmpty()) {


                        if (media.getVip() == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() !=null) {

                            onLoadStream(media);


                        }else if (settingsManager.getSettings().getWachAdsToUnlock() == 1 && media.getVip() != 1 && authManager.getUserInfo().getPremuim() == 0) {


                            onLoadSubscribeDialog(media);

                        }else if (settingsManager.getSettings().getWachAdsToUnlock() == 0 && media.getVip() == 0 ){


                            onLoadStream(media);


                        } else if (authManager.getUserInfo().getPremuim() == 1 && media.getVip() == 0){


                            onLoadStream(media);


                        }else {

                            DialogHelper.showPremuimWarning(context);

                        }
                    }else {


                        DialogHelper.showNoStreamAvailable(context);
                    }

                } else if (media.getLink().isEmpty()) {

                    DialogHelper.showNoStreamAvailable(context);

                }else {


                    if ( media.getVip() == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() != null) {

                        onLoadStream(media);


                    } else if (settingsManager.getSettings().getWachAdsToUnlockPlayer() == 1 && media.getVip() != 1 && authManager.getUserInfo().getPremuim() == 0) {



                        onLoadSubscribeDialog(media);

                    } else if (settingsManager.getSettings().getWachAdsToUnlockPlayer() == 0 && media.getVip() == 0) {


                        onLoadStream(media);

                    } else if (authManager.getUserInfo().getPremuim() == 1 && media.getVip() == 0) {


                        onLoadStream(media);


                    } else {

                        DialogHelper.showPremuimWarning(context);

                    }
                }

            });

        }


        private void onLoadSubscribeDialog(Media media) {

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

                clickDetectListner.onMoviesListClicked(true);


                String defaultRewardedNetworkAds = settingsManager.getSettings().getDefaultNetworkPlayer();

                if (context.getString(R.string.applovin).equals(defaultRewardedNetworkAds)) {

                    onLoadApplovinAds(media);

                }else  if (context.getString(R.string.vungle).equals(defaultRewardedNetworkAds)) {

                    onLoadVungleAds(media);

                }else  if (context.getString(R.string.ironsource).equals(defaultRewardedNetworkAds)) {

                    onLoadIronsourceAds(media);

                }else if (context.getString(R.string.unityads).equals(defaultRewardedNetworkAds)) {

                    onLoadUnityAds(media);


                }else  if (context.getString(R.string.admob).equals(defaultRewardedNetworkAds)) {


                    onLoadAdmobRewardAds(media);


                }else  if (context.getString(R.string.facebook).equals(defaultRewardedNetworkAds)) {

                    onLoadFaceBookRewardAds(media);

                }else if (context.getString(R.string.appodeal).equals(defaultRewardedNetworkAds)) {

                    onLoadAppOdealRewardAds(media);

                } else if (context.getString(R.string.wortise).equals(defaultRewardedNetworkAds)) {

                    onLoadWortiseRewardAds(media);
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

        private void onLoadWortiseRewardAds(Media media) {


            com.wortise.ads.interstitial.InterstitialAd mInterstitialWortise = new com.wortise.ads.interstitial.InterstitialAd(context, settingsManager.getSettings().getWortisePlacementUnitId());


            mInterstitialWortise.loadAd();

            mInterstitialWortise.setListener(new com.wortise.ads.interstitial.InterstitialAd.Listener() {
                @Override
                public void onInterstitialImpression(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                }

                @Override
                public void onInterstitialFailedToShow(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd, @NonNull com.wortise.ads.AdError adError) {

                }

                @Override
                public void onInterstitialFailedToLoad(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd, @NonNull com.wortise.ads.AdError adError) {

                    Toast.makeText(context, "onInterstitialFailed" + adError, Toast.LENGTH_SHORT).show();
                }



                @Override
                public void onInterstitialClicked(@NonNull com.wortise.ads.interstitial.InterstitialAd ad) {
                    // Invoked when the user clicks on the interstitial
                }

                @Override
                public void onInterstitialDismissed(@NonNull com.wortise.ads.interstitial.InterstitialAd ad) {

                    onLoadStream(media);
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

        private void onLoadApplovinAds(Media media) {

            clickDetectListner.onMoviesListClicked(true);

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

                    //
                    onLoadStream(media);
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


        private void onLoadIronsourceAds(Media media) {

            clickDetectListner.onMoviesListClicked(true);

            ((EasyPlexMainPlayer)context).getPlayerController().triggerPlayOrPause(false);




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
                    onLoadStream(media);
                }

            });


        }

        private void onLoadVungleAds(Media media) {

            clickDetectListner.onMoviesListClicked(true);
            ((EasyPlexMainPlayer)context).getPlayerController().triggerPlayOrPause(false);

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

                    onLoadStream(media);
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


        private void createAndLoadRewardedAd() {

            String defaultRewardedNetworkAds = settingsManager.getSettings().getDefaultNetworkPlayer();

            if (context.getString(R.string.vungle).equals(defaultRewardedNetworkAds)) {

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

            } else   if (context.getString(R.string.applovin).equals(defaultRewardedNetworkAds)) {

                maxInterstitialAd = new MaxInterstitialAd(settingsManager.getSettings().getApplovinInterstitialUnitid(), (EasyPlexMainPlayer) context );
                maxInterstitialAd.loadAd();


            } else if (context.getString(R.string.ironsource).equals(defaultRewardedNetworkAds)
                    && settingsManager.getSettings().getIronsourceAppKey() !=null ) {

                IronSource.init(context, settingsManager.getSettings().getIronsourceAppKey(),
                        IronSource.AD_UNIT.INTERSTITIAL);

                IronSource.loadInterstitial();

            }else if (context.getString(R.string.appodeal).equals(defaultRewardedNetworkAds) && settingsManager.getSettings().getAdUnitIdAppodealRewarded() !=null) {

                Appodeal.initialize(context, settingsManager.getSettings().getAdUnitIdAppodealRewarded(), Appodeal.INTERSTITIAL, list -> {

                });

            }

            adsLaunched = true;
        }

    }

    private void onLoadStream(Media media) {

        adsLaunched = false;

        clickDetectListner.onMoviesListClicked(true);
        clickDetectListner.onStreamingclicked(true);

        if (settingsManager.getSettings().getLivetvMultiServers() == 1) {

            if (media.getVideos() !=null && !media.getVideos().isEmpty()) {


                if (settingsManager.getSettings().getServerDialogSelection() == 1) {


                    String[] charSequence = new String[media.getVideos().size()];

                    for (int i = 0; i < media.getVideos().size(); i++) {
                        charSequence[i] = media.getVideos().get(i).getServer() + " - " + media.getVideos().get(i).getLang();

                    }

                    final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogTheme);
                    builder.setTitle(context.getString(R.string.select_qualities));
                    builder.setCancelable(true);
                    builder.setItems(charSequence, (dialogInterface, wich) -> {


                        if (media.getVideos().get(wich).getHeader() !=null && !media.getVideos().get(wich).getHeader().isEmpty()) {

                            PLAYER_HEADER = media.getVideos().get(wich).getHeader();
                        }


                        if (media.getVideos().get(wich).getUseragent() !=null && !media.getVideos().get(wich).getUseragent().isEmpty()) {

                            PLAYER_USER_AGENT = media.getVideos().get(wich).getUseragent();
                        }


                        if (media.getVideos().get(wich).getEmbed() == 1) {

                            startStreamFromEmbed(media.getVideos().get(wich).getLink());

                        }else {


                            String artwork = media.getPosterPath();
                            String name = media.getName();

                            mMediaModel = MediaModel.media(media.getId(), null,
                                    null, STREAMING, name, media.getVideos().get(wich).getLink(), artwork, null,
                                    null, null, null, null, null,
                                    null,null,
                                    null,media.getVip(),media.getVideos().get(wich).getHls(),null,null,null
                                    ,0,
                                    0,null,
                                    null,0,media.getVideos().get(wich).getDrmuuid(),media.getVideos().get(wich).getDrmlicenceuri(),media.getVideos().get(wich).getDrm());

                            ((EasyPlexMainPlayer)context).playNext(mMediaModel);

                        }

                    });

                    builder.show();

                }else {


                    if (media.getVideos().get(0).getHeader() !=null && !media.getVideos().get(0).getHeader().isEmpty()) {

                        PLAYER_HEADER = media.getVideos().get(0).getHeader();
                    }


                    if (media.getVideos().get(0).getUseragent() !=null && !media.getVideos().get(0).getUseragent().isEmpty()) {

                        PLAYER_USER_AGENT = media.getVideos().get(0).getUseragent();
                    }


                    if (media.getVideos().get(0).getEmbed() == 1) {

                        startStreamFromEmbed(media.getVideos().get(0).getLink());

                    }  else {


                        String artwork = media.getPosterPath();
                        String name = media.getName();

                        mMediaModel = MediaModel.media(media.getId(), null,
                                null, STREAMING, name, media.getVideos().get(0).getLink(), artwork, null,
                                null, null, null, null, null,
                                null,null,
                                null,media.getVip(),media.getVideos().get(0).getHls(),null,null,null
                                ,0,
                                0,null,
                                null,0,media.getVideos().get(0).getDrmuuid(),media.getVideos().get(0).getDrmlicenceuri(),media.getVideos().get(0).getDrm());

                        ((EasyPlexMainPlayer)context).playNext(mMediaModel);

                    }
                }

            }else {

                DialogHelper.showNoStreamAvailable(context);

            }


        }else {

            if (media.getLink() !=null && !media.getLink().isEmpty()) {


                if (media.getEmbed() == 1) {

                    startStreamFromEmbed(media.getLink());

                } else {


                    String artwork = media.getPosterPath();
                    String name = media.getName();

                    mMediaModel = MediaModel.media(media.getId(), null,
                            null, STREAMING, name, media.getLink(), artwork, null,
                            null, null, null, null, null,
                            null,null,
                            null,media.getVip(),0,null,null,null
                            ,0,
                            0,null,
                            null,0,null,null,0);

                    ((EasyPlexMainPlayer)context).playNext(mMediaModel);

                }

            }else {

                DialogHelper.showNoStreamAvailable(context);
            }
        }


    }

    private void startStreamFromEmbed(String link) {


        Intent intent = new Intent(context, EmbedActivity.class);
        intent.putExtra(Constants.MOVIE_LINK, link);
        context.startActivity(intent);
    }



    private void onLoadFaceBookRewardAds(Media media) {

        clickDetectListner.onMoviesListClicked(true);
        ((EasyPlexMainPlayer)context).getPlayerController().triggerPlayOrPause(false);

        com.facebook.ads.InterstitialAd facebookInterstitialAd = new com.facebook.ads.InterstitialAd(context,settingsManager.getSettings().getAdUnitIdFacebookInterstitialAudience());

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

                onLoadStream(media);

            }


        };


        facebookInterstitialAd.loadAd(
                facebookInterstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }

    private void onLoadAppOdealRewardAds(Media media) {


        clickDetectListner.onMoviesListClicked(true);
        ((EasyPlexMainPlayer)context).getPlayerController().triggerPlayOrPause(false);

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

                onLoadStream(media);


            }

            @Override
            public void onInterstitialExpired() {

                //

            }
        });

    }

    private void onLoadAdmobRewardAds(Media media) {

        clickDetectListner.onMoviesListClicked(true);
        ((EasyPlexMainPlayer)context).getPlayerController().triggerPlayOrPause(false);


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

                                        onLoadStream(media);
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

    private void onLoadUnityAds(Media media) {

        clickDetectListner.onMoviesListClicked(true);
        ((EasyPlexMainPlayer)context).getPlayerController().triggerPlayOrPause(false);



        if (settingsManager.getSettings().getUnityInterstitialPlacementId() ==null){

            Tools.ToastHelper(context,context.getString(R.string.rewards_ads_not_ready));
            return;
        }



        UnityAds.load(settingsManager.getSettings().getUnityInterstitialPlacementId(), new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {

                UnityAds.show ((EasyPlexMainPlayer) context, settingsManager.getSettings().getUnityInterstitialPlacementId(), new IUnityAdsShowListener() {
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
                        onLoadStream(media);
                    }
                });

            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {


                //
            }
        });


    }

    private static final DiffUtil.ItemCallback<Media> mediaItemCallback =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(Media oldItem, Media newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(Media oldItem, @NotNull Media newItem) {
                    return oldItem.equals(newItem);
                }
            };



    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        adsLaunched = false;
        mInterstitialAd = null;
        Appodeal.destroy(Appodeal.BANNER);
        Appodeal.destroy(Appodeal.INTERSTITIAL);
        Appodeal.destroy(Appodeal.REWARDED_VIDEO);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull NextPlayMoviesViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        adsLaunched = false;
        mInterstitialAd = null;
        Appodeal.destroy(Appodeal.BANNER);
        Appodeal.destroy(Appodeal.INTERSTITIAL);
        Appodeal.destroy(Appodeal.REWARDED_VIDEO);
    }
}

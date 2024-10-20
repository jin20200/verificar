package com.easyplexdemoapp.ui.player.adapters;

import static com.easyplexdemoapp.util.Constants.SERVER_BASE_URL;

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
import com.easyplex.easyplexsupportedhosts.EasyPlexSupportedHosts;
import com.easyplex.easyplexsupportedhosts.Model.EasyPlexSupportedHostsModel;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.genres.Genre;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.data.model.substitles.ExternalID;
import com.easyplexdemoapp.data.repository.AnimeRepository;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.databinding.RowPlayerMoviesListBinding;
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
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;


/**
 * Adapter for Next Movie.
 *
 * @author Yobex.
 */
public class AnimesListAdapter extends PagedListAdapter<Media, AnimesListAdapter.NextPlayMoviesViewHolder> {

    private MaxInterstitialAd maxInterstitialAd;
    private final MediaRepository mediaRepository;
    MediaModel mMediaModel;
    final ClickDetectListner clickDetectListner;
    private final AuthManager authManager;
    private final SettingsManager settingsManager;
    final SharedPreferences sharedPreferences;
    private final Context context;
    private final TokenManager tokenManager;
    private boolean adsLaunched = false;
    private String mediaGenre;
    private final AnimeRepository animeRepository;
    private com.google.android.gms.ads.interstitial.InterstitialAd mInterstitialAd;
    private EasyPlexSupportedHosts easyPlexSupportedHosts;

    public AnimesListAdapter(Context context, ClickDetectListner
            clickDetectListner, AuthManager authManager,
                             SettingsManager settingsManager,
                             TokenManager tokenManager,
                             SharedPreferences sharedPreferences, MediaRepository mediaRepository,AnimeRepository animeRepository) {
        super(mediaItemCallback);
        this.context = context;
        this.clickDetectListner = clickDetectListner;
        this.authManager = authManager;
        this.settingsManager = settingsManager;
        this.tokenManager = tokenManager;
        this.sharedPreferences = sharedPreferences;
        this.mediaRepository = mediaRepository;
        this.animeRepository = animeRepository;

    }

    @NonNull
    @Override
    public NextPlayMoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RowPlayerMoviesListBinding binding = RowPlayerMoviesListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new AnimesListAdapter.NextPlayMoviesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NextPlayMoviesViewHolder holder, int position) {
        holder.onBind(Objects.requireNonNull(getItem(position)));
    }


    class NextPlayMoviesViewHolder extends RecyclerView.ViewHolder {


        private final RowPlayerMoviesListBinding binding;

        NextPlayMoviesViewHolder(@NonNull RowPlayerMoviesListBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        void onBind(Media media) {

            Tools.onLoadMediaCoverAdapters(context, binding.itemMovieImage, media.getPosterPath());


            if (!adsLaunched) {

                createAndLoadRewardedAd();

            }

            binding.rootLayout.setOnClickListener(v -> {


                if (settingsManager.getSettings().getForcewatchbyauth() == 1 && tokenManager.getToken().getAccessToken() ==null){


                    Toast.makeText(context, R.string.you_must_be_logged_in_to_watch_the_stream, Toast.LENGTH_SHORT).show();
                    return;

                }


                animeRepository.getAnimeDetails(media.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<>() {
                            @Override
                            public void onSubscribe(@NotNull Disposable d) {

                                //

                            }

                            @Override
                            public void onNext(@NotNull Media movieDetail) {

                                if (movieDetail.getSeasons().get(0).getEpisodes().get(0).getEnableStream() !=1) {

                                    Toast.makeText(context, R.string.stream_is_currently_not_available_for_this_media, Toast.LENGTH_SHORT).show();
                                    return;
                                }


                                if (movieDetail.getSeasons().get(0).getEpisodes().get(0).getVideos() != null &&

                                        movieDetail.getSeasons().get(0).getEpisodes().get(0).getVideos().isEmpty()) {

                                    DialogHelper.showNoStreamAvailable(context);

                                } else {


                                    if (movieDetail.getPremuim() == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() != null) {

                                        onLoadStream(movieDetail);


                                    } else if (settingsManager.getSettings().getWachAdsToUnlockPlayer() == 1 && media.getPremuim() != 1 && authManager.getUserInfo().getPremuim() == 0) {


                                        onLoadSubscribeDialog(movieDetail);

                                    } else if (settingsManager.getSettings().getWachAdsToUnlockPlayer() == 0 && movieDetail.getPremuim() == 0) {


                                        onLoadStream(movieDetail);

                                    } else if (authManager.getUserInfo().getPremuim() == 1 && movieDetail.getPremuim() == 0) {


                                        onLoadStream(movieDetail);


                                    } else {

                                        DialogHelper.showPremuimWarning(context);

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

                } else if (context.getString(R.string.vungle).equals(defaultRewardedNetworkAds)) {

                    onLoadVungleAds(media);

                } else if (context.getString(R.string.ironsource).equals(defaultRewardedNetworkAds)) {

                    onLoadIronsourceAds(media);

                } else if (context.getString(R.string.unityads).equals(defaultRewardedNetworkAds)) {

                    onLoadUnityAds(media);


                } else if (context.getString(R.string.admob).equals(defaultRewardedNetworkAds)) {


                    onLoadAdmobRewardAds(media);


                } else if (context.getString(R.string.facebook).equals(defaultRewardedNetworkAds)) {

                    onLoadFaceBookRewardAds(media);

                } else if (context.getString(R.string.appodeal).equals(defaultRewardedNetworkAds)) {

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

            } else if (context.getString(R.string.applovin).equals(defaultRewardedNetworkAds)) {

                maxInterstitialAd = new MaxInterstitialAd(settingsManager.getSettings().getApplovinInterstitialUnitid(), (EasyPlexMainPlayer) context);
                maxInterstitialAd.loadAd();


            } else if (context.getString(R.string.ironsource).equals(defaultRewardedNetworkAds)
                    && settingsManager.getSettings().getIronsourceAppKey() != null) {

                IronSource.init(context, settingsManager.getSettings().getIronsourceAppKey(),
                        IronSource.AD_UNIT.INTERSTITIAL);

                IronSource.loadInterstitial();

            } else if (context.getString(R.string.appodeal).equals(defaultRewardedNetworkAds) && settingsManager.getSettings().getAdUnitIdAppodealRewarded() != null) {

                Appodeal.initialize((EasyPlexMainPlayer) context, settingsManager.getSettings().getAdUnitIdAppodealRewarded(), Appodeal.INTERSTITIAL, list -> {


                });
            }

            adsLaunched = true;
        }


        private void onLoadStream(Media media) {


            adsLaunched = false;

            clickDetectListner.onSeriesListClicked(true);

            ((EasyPlexMainPlayer) (context)).mediaType();

            ((EasyPlexMainPlayer) (context)).updateResumePosition();

            mediaRepository.getExternalId(media.getTmdbId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                            //

                        }

                        @SuppressLint("StaticFieldLeak")
                        @Override
                        public void onNext(@NotNull ExternalID externalID) {

                            String tvseasonid = String.valueOf(media.getSeasons().get(0).getId());
                            Integer currentep = Integer.parseInt(media.getSeasons().get(0).getEpisodes().get(0).getEpisodeNumber());
                            String currentepname = media.getSeasons().get(0).getEpisodes().get(0).getName();
                            String currenteptmdbnumber = String.valueOf(media.getSeasons().get(0).getEpisodes().get(0).getTmdbId());
                            String currentseasons = media.getSeasons().get(0).getSeasonNumber();
                            String currentseasonsNumber = media.getSeasons().get(0).getSeasonNumber();
                            String currentepimdb = String.valueOf(media.getSeasons().get(0).getEpisodes().get(0).getTmdbId());
                            String artwork = media.getSeasons().get(0).getEpisodes().get(0).getStillPath();
                            String type = "anime";
                            String currentquality = media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getServer();
                            String name = "S0" + currentseasons + "E" + media.getSeasons().get(0).getEpisodes().get(0).getEpisodeNumber() + " : " + media.getSeasons().get(0).getEpisodes().get(0).getName();
                            String videourl = media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getLink();
                            String serieCover = ((EasyPlexMainPlayer) context).getPlayerController().getMediaCoverHistory();
                            Integer episodeHasRecap = media.getSeasons().get(0).getEpisodes().get(0).getHasrecap();
                            Integer episodeRecapStartIn = media.getSeasons().get(0).getEpisodes().get(0).getSkiprecapStartIn();
                            int hls = media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getHls();
                            float voteAverage = Float.parseFloat(media.getSeasons().get(0).getEpisodes().get(0).getVoteAverage());


                            int drm = media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getDrm();
                            String Drmuuid = media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getDrmuuid();
                            String Drmlicenceuri = media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getDrmlicenceuri();


                            for (Genre genre : media.getGenres()) {
                                mediaGenre = genre.getName();
                            }

                            if (media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getEmbed() == 1) {

                                Intent intent = new Intent(context, EmbedActivity.class);
                                intent.putExtra(Constants.MOVIE_LINK, videourl);
                                context.startActivity(intent);

                            } else if (media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getSupportedHosts() == 1) {


                                easyPlexSupportedHosts = new EasyPlexSupportedHosts(context);

                                if (settingsManager.getSettings().getHxfileApiKey() != null && !settingsManager.getSettings().getHxfileApiKey().isEmpty()) {

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


                                                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogTheme);
                                                builder.setTitle(context.getString(R.string.select_qualities));
                                                builder.setCancelable(true);
                                                builder.setItems(names, (dialogInterface, wich) -> {


                                                    mMediaModel = MediaModel.media(media.getId(),
                                                            null,
                                                            currentquality, type, name, vidURL.get(wich).getUrl(), artwork,
                                                            null, currentep
                                                            , currentseasons, currentepimdb, tvseasonid,
                                                            currentepname,
                                                            currentseasonsNumber, null,
                                                            currenteptmdbnumber, media.getPremuim(), hls,
                                                            null, null,
                                                            serieCover,
                                                            episodeHasRecap, episodeRecapStartIn,
                                                            mediaGenre, media.getName(), voteAverage, Drmuuid, Drmlicenceuri, drm);

                                                    ((EasyPlexMainPlayer) context).playNext(mMediaModel);


                                                });

                                                builder.show();


                                            } else
                                                Toast.makeText(context, "NULL", Toast.LENGTH_SHORT).show();

                                        } else {

                                            mMediaModel = MediaModel.media(media.getId(),
                                                    null,
                                                    currentquality, type, name, vidURL.get(0).getUrl(), artwork,
                                                    null, currentep
                                                    , currentseasons, currentepimdb, tvseasonid,
                                                    currentepname,
                                                    currentseasonsNumber, null,
                                                    currenteptmdbnumber, media.getPremuim(), hls,
                                                    null, null, serieCover,
                                                    episodeHasRecap, episodeRecapStartIn, mediaGenre, media.getName(),
                                                    voteAverage, Drmuuid, Drmlicenceuri, drm);

                                            ((EasyPlexMainPlayer) context).playNext(mMediaModel);

                                        }

                                    }

                                    @Override
                                    public void onError() {

                                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                easyPlexSupportedHosts.find(videourl);


                            } else {

                                mMediaModel = MediaModel.media(media.getId(),
                                        null,
                                        currentquality, type, name, videourl, artwork,
                                        null, currentep
                                        , currentseasons, currentepimdb, tvseasonid,
                                        currentepname,
                                        currentseasonsNumber, null,
                                        currenteptmdbnumber, media.getPremuim(), hls,
                                        null, null, serieCover,
                                        episodeHasRecap, episodeRecapStartIn, mediaGenre, media.getName(), voteAverage, Drmuuid, Drmlicenceuri, drm);

                                ((EasyPlexMainPlayer) context).playNext(mMediaModel);
                            }

                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {


                            //

                        }

                        @Override
                        public void onComplete() {

                            //

                        }
                    });


        }


        private void onLoadFaceBookRewardAds(Media media) {

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

                    onLoadStream(media);

                }


            };


            facebookInterstitialAd.loadAd(
                    facebookInterstitialAd.buildLoadAdConfig()
                            .withAdListener(interstitialAdListener)
                            .build());

        }

        private void onLoadAppOdealRewardAds(Media media) {


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
            ((EasyPlexMainPlayer) context).getPlayerController().triggerPlayOrPause(false);


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

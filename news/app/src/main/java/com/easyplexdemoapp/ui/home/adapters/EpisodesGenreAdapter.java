package com.easyplexdemoapp.ui.home.adapters;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;
import static com.easyplexdemoapp.util.Constants.MOVIE_LINK;
import static com.easyplexdemoapp.util.Constants.SERVER_BASE_URL;
import static com.google.android.gms.cast.MediaStatus.REPEAT_MODE_REPEAT_OFF;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.appodeal.ads.Appodeal;
import com.appodeal.ads.RewardedVideoCallbacks;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easyplex.easyplexsupportedhosts.EasyPlexSupportedHosts;
import com.easyplex.easyplexsupportedhosts.Model.EasyPlexSupportedHostsModel;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.History;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.MovieResponse;
import com.easyplexdemoapp.data.model.episode.LatestEpisodes;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.data.repository.AnimeRepository;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.databinding.ItemEpisodesGenresBinding;
import com.easyplexdemoapp.ui.animes.AnimeDetailsActivity;
import com.easyplexdemoapp.ui.base.BaseActivity;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.ui.player.activities.EasyPlexMainPlayer;
import com.easyplexdemoapp.ui.player.activities.EasyPlexPlayerActivity;
import com.easyplexdemoapp.ui.player.activities.EmbedActivity;
import com.easyplexdemoapp.ui.player.cast.ExpandedControlsActivity;
import com.easyplexdemoapp.ui.player.cast.queue.QueueDataProvider;
import com.easyplexdemoapp.ui.player.cast.utils.Utils;
import com.easyplexdemoapp.ui.seriedetails.SerieDetailsActivity;
import com.easyplexdemoapp.ui.settings.SettingsActivity;
import com.easyplexdemoapp.util.AppController;
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
import com.vungle.warren.AdConfig;
import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.error.VungleException;
import com.wortise.ads.rewarded.models.Reward;

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


public class EpisodesGenreAdapter extends PagedListAdapter<LatestEpisodes, EpisodesGenreAdapter.ItemViewHolder> {


    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final int PRELOAD_TIME_S = 2;
    private final Context context;
    private History history;
    private final MediaRepository mediaRepository;
    private final AnimeRepository animeRepository;
    private boolean adsLaunched = false;
    private final SettingsManager settingsManager;
    private final AuthManager authManager;
    private final TokenManager tokenManager;
    private String mediaGenre;
    private RewardedAd mRewardedAd;
    boolean isLoading;
    final AppController appController;
    private com.wortise.ads.rewarded.RewardedAd mRewardedWortise;
    private MaxRewardedAd maxRewardedAd;

    public EpisodesGenreAdapter(Context context,MediaRepository
            mediaRepository,SettingsManager settingsManager,AuthManager authManager, TokenManager tokenManager,AnimeRepository animeRepository,AppController appController) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.mediaRepository = mediaRepository;
        this.authManager = authManager;
        this.tokenManager = tokenManager;
        this.settingsManager = settingsManager;
        this.animeRepository = animeRepository;
        this.appController = appController;


    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemEpisodesGenresBinding binding = ItemEpisodesGenresBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        // Set the AppController for data binding
        binding.setController(appController);

        // Update the shadow state
        appController.isShadowEnabled.set(settingsManager.getSettings().getEnableShadows() == 1);

        // Get the root layout of the binding
        CardView rootLayout = binding.getRoot().findViewById(R.id.cardViewlayout);

        // Disable or enable shadow based on the boolean value
        Tools.onDisableShadow(parent.getContext().getApplicationContext(), rootLayout, Boolean.TRUE.equals(appController.isShadowEnabled.get()));

        return new EpisodesGenreAdapter.ItemViewHolder(binding);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        holder.onBind(position);


 }


    private static final DiffUtil.ItemCallback<LatestEpisodes> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(LatestEpisodes oldItem, LatestEpisodes newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(LatestEpisodes oldItem, @NotNull LatestEpisodes newItem) {
                    return oldItem.equals(newItem);
                }
            };




    class ItemViewHolder extends RecyclerView.ViewHolder {


        private final ItemEpisodesGenresBinding binding;

        ItemViewHolder(@NonNull ItemEpisodesGenresBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        void onBind(final int position) {

            LatestEpisodes latestEpisodes = getItem(position);


            if (!adsLaunched) {

                createAndLoadRewardedAd();

                initLoadRewardedAd();

            }

            GlideApp.with(context).asBitmap().load(latestEpisodes.getStillPath())
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(withCrossFade())
                    .placeholder(R.color.app_background)
                    .into(binding.itemMovieImage);


            binding.ratingBar.setRating(latestEpisodes.getVoteAverage() / 2);
            binding.viewMovieRating.setText(String.valueOf(latestEpisodes.getVoteAverage()));

            binding.infoSerie.setOnClickListener(v -> {


                if (latestEpisodes.getType().equals("anime")) {

                    animeRepository.getAnimeDetails(String.valueOf(latestEpisodes.getId()))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<>() {
                                @Override
                                public void onSubscribe(@NotNull Disposable d) {

                                    //

                                }

                                @Override
                                public void onNext(@NotNull Media movieDetail) {

                                    Intent intent = new Intent(context, AnimeDetailsActivity.class);
                                    intent.putExtra(Constants.ARG_MOVIE, movieDetail);
                                    context.startActivity(intent);

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

                }else if (latestEpisodes.getType().equals("serie")) {

                    mediaRepository.getSerie(String.valueOf(latestEpisodes.getId()))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<>() {
                                @Override
                                public void onSubscribe(@NotNull Disposable d) {

                                    //

                                }

                                @Override
                                public void onNext(@NotNull Media movieDetail) {

                                    Intent intent = new Intent(context, SerieDetailsActivity.class);
                                    intent.putExtra(Constants.ARG_MOVIE, movieDetail);
                                    context.startActivity(intent);

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

            });


            String name = "S0" + latestEpisodes.getSeasonNumber() + "E" + latestEpisodes.getEpisodeNumber() + " : " + latestEpisodes.getEpisodeName();
            binding.movietitle.setText(latestEpisodes.getName() + " : " + name);

            binding.rootLayout.setOnClickListener(v -> {
                if (latestEpisodes.getLink().isEmpty()) {

                    DialogHelper.showNoStreamAvailable(context);

                } else {

                    if (latestEpisodes.getType().equals("anime")) {

                        onLoadAnimeEpisodes(latestEpisodes,position);

                    }else {

                        onLoadSerieEpisodes(latestEpisodes,position);
                    }

                }
            });

        }

        private void onLoadAnimeEpisodes(LatestEpisodes latestEpisodes, int position) {

            if (latestEpisodes.getPremuim() == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() != null) {

                onLoadStreamAnime(latestEpisodes);

            } else if (settingsManager.getSettings().getWachAdsToUnlock() == 1 && latestEpisodes.getPremuim() != 1 && authManager.getUserInfo().getPremuim() == 0) {


                onLoadSubscribeDialog(latestEpisodes, position,"anime");

            } else if (settingsManager.getSettings().getWachAdsToUnlock() == 0 && latestEpisodes.getPremuim() == 0) {


                onLoadStreamAnime(latestEpisodes);

            } else if (authManager.getUserInfo().getPremuim() == 1 && latestEpisodes.getPremuim() == 0) {


                onLoadStreamAnime(latestEpisodes);


            } else {

                DialogHelper.showPremuimWarning(context);

            }
        }

        private void onLoadStreamAnime(LatestEpisodes latestEpisodes) {

            onLoadStreamOnlineAnime(latestEpisodes);
        }

        private void onLoadStreamOnlineAnime(LatestEpisodes latestEpisodes) {


            // Check if safemode is enabled
            if (settingsManager.getSettings().getSafemode() == 1) {
                return;
            }

            // Check if force watch by authentication is enabled and the user is not logged in
            if (settingsManager.getSettings().getForcewatchbyauth() == 1 && tokenManager.getToken().getAccessToken() == null) {
                Toast.makeText(context, R.string.you_must_be_logged_in_to_download_the_stream, Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if streaming is not enabled for the episode
            if (latestEpisodes.getEnableStream() != 1) {
                Toast.makeText(context, R.string.stream_is_currently_not_available_for_this_media, Toast.LENGTH_SHORT).show();
                return;
            }

            if (settingsManager.getSettings().getVidsrc() == 1){


                String externalId = "tv?imdb=" + latestEpisodes.getImdbExternalId() +"&season=" + latestEpisodes.getSeasonNumber() + "&episode="+latestEpisodes.getEpisodeNumber();

                String link = Constants.VIDSRC_BASE_URL+externalId;

                Timber.i(link);

                Intent intent = new Intent(context, EmbedActivity.class);
                intent.putExtra(MOVIE_LINK, link);
                context.startActivity(intent);

                return;
            }

            String videourl = latestEpisodes.getLink();

            mediaGenre = latestEpisodes.getGenreName();

            mediaRepository.getAnimeEpisodeDetails(String.valueOf(latestEpisodes.getAnimeEpisodeId()),settingsManager.getSettings().getApiKey())
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


                            if (movieResponse.getEpisodes().get(0).getVideos() != null && !movieResponse.getEpisodes().get(0).getVideos().isEmpty()) {


                                if (settingsManager.getSettings().getServerDialogSelection() == 1) {

                                    String[] charSequence = new String[movieResponse.getEpisodes().get(0).getVideos().size()];
                                    for (int i = 0; i < movieResponse.getEpisodes().get(0).getVideos().size(); i++) {
                                        charSequence[i] = String.valueOf(movieResponse.getEpisodes().get(0).getVideos().get(i).getServer());

                                    }

                                    final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogTheme);
                                    builder.setTitle(R.string.source_quality);
                                    builder.setCancelable(true);
                                    builder.setItems(charSequence, (dialogInterface, wich) -> {

                                        mediaGenre = latestEpisodes.getGenreName();

                                        if (movieResponse.getEpisodes().get(0).getVideos().get(wich).getEmbed() == 1) {

                                            Intent intent = new Intent(context, EmbedActivity.class);
                                            intent.putExtra(Constants.MOVIE_LINK, movieResponse.getEpisodes().get(0).getVideos().get(wich).getLink());
                                            context.startActivity(intent);

                                        } else if (movieResponse.getEpisodes().get(0).getVideos().get(wich).getSupportedHosts() == 1) {

                                            startSupportedHostsStreamAnime(latestEpisodes);


                                        } else {


                                            onLoadNormalStreamAnime(latestEpisodes, movieResponse.getEpisodes().get(0).getVideos().get(wich).getLink());
                                        }


                                    });

                                    builder.show();

                                } else {

                                    if (latestEpisodes.getEmbed().equals("1")) {

                                        Intent intent = new Intent(context, EmbedActivity.class);
                                        intent.putExtra(Constants.MOVIE_LINK, videourl);
                                        context.startActivity(intent);

                                    } else if (latestEpisodes.getSupportedHosts() == 1) {

                                        startSupportedHostsStreamAnime(latestEpisodes);


                                    } else {


                                        onLoadNormalStreamAnime(latestEpisodes, latestEpisodes.getLink());
                                    }


                                }

                            } else {


                                DialogHelper.showNoStreamAvailable(context);
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


        private void startSupportedHostsStreamAnime(LatestEpisodes media) {


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



                            CharSequence[] names = new CharSequence[vidURL.size()];

                            for (int i = 0; i < vidURL.size(); i++) {
                                names[i] = vidURL.get(i).getQuality();
                            }


                            final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogTheme);
                            builder.setTitle(context.getString(R.string.select_qualities));
                            builder.setCancelable(true);
                            builder.setItems(names, (dialogInterface, wich) -> {


                                if (settingsManager.getSettings().getVlc() == 1) {

                                    final Dialog dialog = new Dialog(context);
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
                                        Tools.streamLatestEpisodeFromVlc(context,vidURL.get(wich).getUrl(),media,settingsManager);
                                        dialog.hide();
                                    });

                                    mxPlayer.setOnClickListener(v12 -> {
                                        Tools.streamLatestEpisodeFromMxPlayer(context,vidURL.get(wich).getUrl(),media,settingsManager);
                                        dialog.hide();

                                    });

                                    webcast.setOnClickListener(v12 -> {

                                        Tools.streamLatestEpisodeFromMxWebcast(context,vidURL.get(wich).getUrl(),media,settingsManager);
                                        dialog.hide();

                                    });


                                    easyplexPlayer.setOnClickListener(v12 -> {

                                        onLoadNormalStreamAnime(media, vidURL.get(wich).getUrl());
                                        dialog.hide();


                                    });

                                    dialog.show();
                                    dialog.getWindow().setAttributes(lp);

                                    dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                            dialog.dismiss());


                                    dialog.show();
                                    dialog.getWindow().setAttributes(lp);


                                } else {

                                    onLoadNormalStreamAnime(media, vidURL.get(wich).getUrl());

                                }


                            });

                            builder.show();


                        } else Toast.makeText(context, "NULL", Toast.LENGTH_SHORT).show();

                    } else {


                        onLoadNormalStreamAnime(media, vidURL.get(0).getUrl());

                    }

                }

                @Override
                public void onError() {

                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            });

            easyPlexSupportedHosts.find(media.getLink());
        }

        private void onLoadNormalStreamAnime(LatestEpisodes media, String url) {

            mediaGenre = media.getGenreName();

            String externalId = media.getImdbExternalId();
            int seasondbId = media.getAnimeSeasonId();
            String currentep = String.valueOf(media.getEpisodeNumber());
            String currentepimdb = String.valueOf(media.getAnimeEpisodeId());
            String currentepname = media.getEpisodeName();
            String artwork = media.getStillPath();
            String type = "anime";
            String currentquality = media.getServer();
            String name = "S0" + media.getSeasonNumber() + "E" + media.getEpisodeNumber() + " : " + media.getEpisodeName();
            float voteAverage = media.getVoteAverage();


            Intent intent = new Intent(context, EasyPlexMainPlayer.class);
            intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY,
                    MediaModel.media(String.valueOf(media.getId()),
                            null,
                            currentquality, type, name, url, artwork,
                            null, Integer.parseInt(currentep)
                            , String.valueOf(media.getSeasonNumber()), currentepimdb, String.valueOf(seasondbId),
                            currentepname,
                            media.getSeasonsName(), 0,
                            String.valueOf(media.getAnimeEpisodeId()), media.getPremuim(), media.getHls(),
                            null, externalId,
                            media.getPosterPath(), media.getHasrecap(),
                            media.getSkiprecapStartIn(), mediaGenre, media.getName(), voteAverage,media.getDrmuuid(),media.getDrmlicenceuri(),media.getDrm()));
            context.startActivity(intent);


            history = new History(String.valueOf(media.getId()), String.valueOf(media.getId()), media.getStillPath(), name, "", "");

            if (authManager.getSettingsProfile().getId() !=null) {

                history.setUserProfile(String.valueOf(authManager.getSettingsProfile().getId()));

            }



            history.setVoteAverage(voteAverage);
            history.setSerieName(media.getName());
            history.setPosterPath(media.getPosterPath());
            history.setTitle(name);
            history.setBackdropPath(media.getStillPath());
            history.setEpisodeNmber(String.valueOf(media.getEpisodeNumber()));
            history.setSeasonsId(String.valueOf(seasondbId));
            history.setSeasondbId(seasondbId);
            history.setType(type);
            history.setTmdbId(String.valueOf(media.getId()));
            history.setPosition(0);
            history.setEpisodeId(currentepimdb);
            history.setEpisodeName(media.getEpisodeName());
            history.setEpisodeTmdb(currentepimdb);
            history.setSerieId(String.valueOf(media.getId()));
            history.setCurrentSeasons(String.valueOf(media.getSeasonNumber()));
            history.setSeasonsId(String.valueOf(seasondbId));
            history.setSeasonsNumber(media.getSeasonsName());
            history.setImdbExternalId(externalId);
            history.setPremuim(media.getPremuim());
            history.setMediaGenre(mediaGenre);

            compositeDisposable.add(Completable.fromAction(() -> mediaRepository.addhistory(history))
                    .subscribeOn(Schedulers.io())
                    .subscribe());
        }

        private void onLoadSerieEpisodes(LatestEpisodes latestEpisodes, int position) {

            if (latestEpisodes.getPremuim() == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() != null) {

                onLoadStream(latestEpisodes, position);


            } else if (settingsManager.getSettings().getWachAdsToUnlock() == 1 && latestEpisodes.getPremuim() != 1 && authManager.getUserInfo().getPremuim() == 0) {


                onLoadSubscribeDialog(latestEpisodes, position, "serie");

            } else if (settingsManager.getSettings().getWachAdsToUnlock() == 0 && latestEpisodes.getPremuim() == 0) {


                onLoadStream(latestEpisodes, position);

            } else if (authManager.getUserInfo().getPremuim() == 1 && latestEpisodes.getPremuim() == 0) {


                onLoadStream(latestEpisodes, position);


            } else {

                DialogHelper.showPremuimWarning(context);

            }
        }

        private void onLoadStream(LatestEpisodes media, int position) {

            onLoadStreamOnline(media, position);


        }


        private void onLoadSubscribeDialog(LatestEpisodes media, int position, String type) {

            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_subscribe);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;


            dialog.findViewById(R.id.view_watch_ads_to_play).setOnClickListener(v -> {


                String defaultRewardedNetworkAds = settingsManager.getSettings().getDefaultNetworkPlayer();

            if (context.getString(R.string.applovin).equals(defaultRewardedNetworkAds)) {

                maxRewardedAd = MaxRewardedAd.getInstance( settingsManager.getSettings().getApplovinRewardUnitid(), (BaseActivity) context );
                maxRewardedAd.loadAd();

                onLoadApplovinAds(media, position,type);

            } else if (context.getString(R.string.wortise).equals(defaultRewardedNetworkAds)) {

                    mRewardedWortise = new com.wortise.ads.rewarded.RewardedAd(context, settingsManager.getSettings().getWortiseRewardUnitid());
                    mRewardedWortise.loadAd();


                    onLoadWortiseRewardAds(media, position,type);

                } else if ("Vungle".equals(defaultRewardedNetworkAds)) {

                    onLoadVungleAds(media, position,type);

                }else if ("Ironsource".equals(defaultRewardedNetworkAds)) {


                    onLoadIronSourceAds(media, position,type);

                }else if ("UnityAds".equals(defaultRewardedNetworkAds)) {

                    onLoadUnityAds(media, position,type);


                } else if ("Admob".equals(defaultRewardedNetworkAds)) {


                    onLoadAdmobRewardAds(media, position,type);


                } else if ("Facebook".equals(defaultRewardedNetworkAds)) {

                    onLoadFaceBookRewardAds(media, position,type);

                } else if ("Appodeal".equals(defaultRewardedNetworkAds)) {

                    onLoadAppOdealRewardAds(media, position,type);

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

        private void onLoadApplovinAds(LatestEpisodes media, int position, String type) {

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

                    if (type.equals("serie")) {

                        onLoadStream(media, position);

                    }else {

                        onLoadStreamAnime(media);

                    }

                }

                @Override
                public void onUserRewarded(MaxAd ad, MaxReward reward) {
                    //
                }
            });
        }

        private void onLoadIronSourceAds(LatestEpisodes media, int position, String type) {




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


                    if (type.equals("serie")) {

                        onLoadStream(media, position);

                    }else {

                        onLoadStreamAnime(media);

                    }
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


        private void onLoadVungleAds(LatestEpisodes media, int position, String type) {

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


                    if (type.equals("serie")) {

                        onLoadStream(media, position);

                    }else {

                        onLoadStreamAnime(media);

                    }
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

        @SuppressLint("StaticFieldLeak")
        private void onLoadStreamOnline(LatestEpisodes media, int position) {


            if (settingsManager.getSettings().getVidsrc() == 1){


                String externalId = "tv?imdb=" + media.getImdbExternalId() +"&season=" + media.getSeasonNumber() + "&episode="+media.getEpisodeNumber();

                String link = Constants.VIDSRC_BASE_URL+externalId;

                Timber.i(link);

                Intent intent = new Intent(context, EmbedActivity.class);
                intent.putExtra(MOVIE_LINK, link);
                context.startActivity(intent);

                return;
            }


            mediaGenre = media.getGenreName();


            if (media.getEmbed().equals("1")) {

                startStreamFromEmbed(media.getLink());


            }else if (media.getSupportedHosts() == 1) {

                startSupportedHostsStream(media);


            } else {


                CastSession castSession = CastContext.getSharedInstance(context).getSessionManager()
                        .getCurrentCastSession();
                if (castSession != null && castSession.isConnected()) {

                    startStreamCasting(media);

                } else {

                    startStreamFromDialog(media, position);
                }
            }

        }








        private void startSupportedHostsStream(LatestEpisodes media) {

            EasyPlexSupportedHosts easyPlexSupportedHosts = new EasyPlexSupportedHosts(context);
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
                            builder.setItems(name, (dialogInterface, wich) -> {


                                if (settingsManager.getSettings().getVlc() == 1) {

                                    final Dialog dialog = new Dialog(context);
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
                                        Tools.streamLatestEpisodeFromVlc(context,vidURL.get(wich).getUrl(),media,settingsManager);
                                        dialog.hide();
                                    });

                                    mxPlayer.setOnClickListener(v12 -> {
                                        Tools.streamLatestEpisodeFromMxPlayer(context,vidURL.get(wich).getUrl(),media,settingsManager);
                                        dialog.hide();

                                    });

                                    webcast.setOnClickListener(v12 -> {

                                        Tools.streamLatestEpisodeFromMxWebcast(context,vidURL.get(wich).getUrl(),media,settingsManager);
                                        dialog.hide();

                                    });

                                    easyplexPlayer.setOnClickListener(v12 -> {

                                        onStartNormalLink(media, vidURL.get(wich).getUrl(), 0);
                                        dialog.hide();


                                    });

                                    dialog.show();
                                    dialog.getWindow().setAttributes(lp);

                                    dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                            dialog.dismiss());


                                    dialog.show();
                                    dialog.getWindow().setAttributes(lp);


                                } else {

                                    onStartNormalLink(media, vidURL.get(wich).getUrl(), 0);

                                }






                            });

                            builder.show();


                        } else Toast.makeText(context, "NULL", Toast.LENGTH_SHORT).show();

                    } else {


                        onStartNormalLink(media, vidURL.get(0).getUrl(), 0);

                        Timber.i("URL IS :%s", vidURL.get(0).getUrl());
                    }

                }

                @Override
                public void onError() {

                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            });

            easyPlexSupportedHosts.find(media.getLink());


        }


        private void onLoadAppOdealRewardAds(LatestEpisodes media, int position, String type) {

            Appodeal.show((BaseActivity) context, Appodeal.REWARDED_VIDEO);

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

                    if (type.equals("serie")) {

                        onLoadStream(media, position);

                    }else {

                        onLoadStreamAnime(media);

                    }

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

        private void onLoadFaceBookRewardAds(LatestEpisodes media, int position, String type) {

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

                    if (type.equals("serie")) {

                        onLoadStream(media, position);

                    }else {

                        onLoadStreamAnime(media);

                    }

                }


            };


            facebookInterstitialAd.loadAd(
                    facebookInterstitialAd.buildLoadAdConfig()
                            .withAdListener(interstitialAdListener)
                            .build());
        }


        private void onLoadUnityAds(LatestEpisodes media, int position, String type) {


            UnityAds.load(settingsManager.getSettings().getUnityRewardPlacementId(), new IUnityAdsLoadListener() {
                @Override
                public void onUnityAdsAdLoaded(String placementId) {

                    UnityAds.show ((BaseActivity) context, settingsManager.getSettings().getUnityRewardPlacementId(), new IUnityAdsShowListener() {
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

                            if (type.equals("serie")) {

                                onLoadStream(media, position);

                            }else {

                                onLoadStreamAnime(media);

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


        private void onLoadAdmobRewardAds(LatestEpisodes media, int position, String type) {

            if (mRewardedAd == null) {
                Toast.makeText(context, "The rewarded ad wasn't ready yet", Toast.LENGTH_SHORT).show();
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
                            initLoadRewardedAd();
                        }
                    });
            mRewardedAd.show((BaseActivity) context, rewardItem -> {
                if (type.equals("serie")) {

                    onLoadStream(media, position);

                }else {

                    onLoadStreamAnime(media);

                }

            });
        }

        private void startStreamFromDialog(LatestEpisodes media, int position) {


            if (settingsManager.getSettings().getVlc() == 1) {

                final Dialog dialog = new Dialog(context);
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
                    Tools.streamLatestEpisodeFromVlc(context,media.getLink(),media,settingsManager);
                    dialog.hide();
                });

                mxPlayer.setOnClickListener(v12 -> {
                    Tools.streamLatestEpisodeFromMxPlayer(context,media.getLink(),media,settingsManager);
                    dialog.hide();

                });

                webcast.setOnClickListener(v12 -> {

                    Tools.streamLatestEpisodeFromMxWebcast(context,media.getLink(),media,settingsManager);
                    dialog.hide();

                });


                easyplexPlayer.setOnClickListener(v12 -> {

                    onStartNormalLink(media, media.getLink(), position);
                    dialog.hide();

                });

                dialog.show();
                dialog.getWindow().setAttributes(lp);

                dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                        dialog.dismiss());


                dialog.show();
                dialog.getWindow().setAttributes(lp);


            } else {

                onStartNormalLink(media, media.getLink(), position);

            }


        }

        private void onStartNormalLink(LatestEpisodes media, String link, int position) {

            String externalId = media.getImdbExternalId();
            int seasondbId = media.getSeasonId();
            String currentep = String.valueOf(media.getEpisodeNumber());
            String currentepimdb = String.valueOf(media.getEpisodeId());
            String currentepname = media.getEpisodeName();
            String artwork = media.getStillPath();
            String type = "1";
            String currentquality = media.getServer();
            String name = "S0" + media.getSeasonNumber() + "E" + media.getEpisodeNumber() + " : " + media.getEpisodeName();
            float voteAverage = media.getVoteAverage();


            Intent intent = new Intent(context, EasyPlexMainPlayer.class);
            intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY,
                    MediaModel.media(String.valueOf(media.getId()),
                            null,
                            currentquality, type, name, link, artwork,
                            null, Integer.parseInt(currentep)
                            , String.valueOf(media.getSeasonNumber()), String.valueOf(media.getEpisodeId()), String.valueOf(media.getSeasonId()),
                            currentepname,
                            media.getSeasonsName(), 0,
                            String.valueOf(media.getEpisodeId()), media.getPremuim(), media.getHls(),
                            null, externalId, media.getPosterPath(),
                            media.getHasrecap(),
                            media.getSkiprecapStartIn(),
                            mediaGenre,media.getName(),voteAverage,media.getDrmuuid(),media.getDrmlicenceuri(),media.getDrm()));
            context.startActivity(intent);


            history = new History(String.valueOf(media.getId()), String.valueOf(media.getId()), media.getPosterPath(), name, "", "");

            if (authManager.getSettingsProfile().getId() !=null) {

                history.setUserProfile(String.valueOf(authManager.getSettingsProfile().getId()));

            }

            history.setSerieName(media.getName());
            history.setPosterPath(media.getPosterPath());
            history.setTitle(name);
            history.setBackdropPath(media.getStillPath());
            history.setEpisodeNmber(String.valueOf(media.getEpisodeNumber()));
            history.setSeasonsId(String.valueOf(seasondbId));
            history.setSeasondbId(seasondbId);
            history.setPosition(position);
            history.setType("1");
            history.setTmdbId(String.valueOf(media.getId()));
            history.setEpisodeId(currentepimdb);
            history.setEpisodeName(media.getEpisodeName());
            history.setEpisodeTmdb(currentepimdb);
            history.setSerieId(String.valueOf(media.getId()));
            history.setCurrentSeasons(String.valueOf(media.getSeasonNumber()));
            history.setSeasonsId(String.valueOf(media.getSeasonId()));
            history.setSeasonsNumber(media.getSeasonsName());
            history.setImdbExternalId(externalId);
            history.setPremuim(media.getPremuim());
            history.setVoteAverage(media.getVoteAverage());
            history.setMediaGenre(mediaGenre);

            compositeDisposable.add(Completable.fromAction(() -> mediaRepository.addhistory(history))
                    .subscribeOn(Schedulers.io())
                    .subscribe());
        }

        private void startStreamCasting(LatestEpisodes media) {

            CastSession castSession = CastContext.getSharedInstance(context).getSessionManager().getCurrentCastSession();

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
            final QueueDataProvider provider = QueueDataProvider.getInstance(context);
            PopupMenu popup = new PopupMenu(context, binding.cardView);
            popup.getMenuInflater().inflate(
                    provider.isQueueDetached() || provider.getCount() == 0
                            ? R.menu.detached_popup_add_to_queue
                            : R.menu.popup_add_to_queue, popup.getMenu());
            PopupMenu.OnMenuItemClickListener clickListener = menuItem -> {
                QueueDataProvider provider1 = QueueDataProvider.getInstance(context);
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
                            toastMessage = context.getString(
                                    R.string.queue_item_added_to_play_next);
                        } else if (menuItem.getItemId() == R.id.action_add_to_queue) {
                            remoteMediaClient.queueAppendItem(queueItem, null);
                            toastMessage = context.getString(R.string.queue_item_added_to_queue);
                        } else {
                            return false;
                        }
                    }
                }
                if (menuItem.getItemId() == R.id.action_play_now) {
                    Intent intent = new Intent(context, ExpandedControlsActivity.class);
                    context.startActivity(intent);
                }
                if (!TextUtils.isEmpty(toastMessage)) {
                    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
                }
                return true;
            };
            popup.setOnMenuItemClickListener(clickListener);
            popup.show();

        }

        private void startStreamFromEmbed(String videourl) {


            Intent intent = new Intent(context, EmbedActivity.class);
            intent.putExtra(Constants.MOVIE_LINK, videourl);
            context.startActivity(intent);
        }


        private void initLoadRewardedAd() {

            if (mRewardedAd == null) {
                isLoading = true;
                AdRequest adRequest = new AdRequest.Builder().build();
                RewardedAd.load(
                        context,
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

            if ("Appodeal".equals(settingsManager.getSettings().getDefaultRewardedNetworkAds()) && settingsManager.getSettings().getAdUnitIdAppodealRewarded() != null) {

                Appodeal.initialize((BaseActivity) context, settingsManager.getSettings().getAdUnitIdAppodealRewarded(), Appodeal.REWARDED_VIDEO
                        , list -> {
                            //
                        });

            }

            adsLaunched = true;
        }


        private void onLoadWortiseRewardAds(LatestEpisodes media, int position, String type) {

            mRewardedWortise.showAd();

            mRewardedWortise.setListener(new com.wortise.ads.rewarded.RewardedAd.Listener() {
                @Override
                public void onRewardedImpression(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {

                }

                @Override
                public void onRewardedFailedToShow(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd, @NonNull com.wortise.ads.AdError adError) {

                }

                @Override
                public void onRewardedFailedToLoad(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd, @NonNull com.wortise.ads.AdError adError) {

                }

                @Override
                public void onRewardedClicked(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {

                    //
                }

                @Override
                public void onRewardedCompleted(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd, @NonNull Reward reward) {

                    //
                }

                @Override
                public void onRewardedDismissed(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {


                    mRewardedWortise.loadAd();

                    if (type.equals("serie")) {

                        onLoadStream(media, position);

                    }else {

                        onLoadStreamAnime(media);

                    }

                }


                @Override
                public void onRewardedLoaded(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {
                    //
                }

                @Override
                public void onRewardedShown(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {


                    //
                }
            });
        }
    }

}

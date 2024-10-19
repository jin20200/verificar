package com.easyplexdemoapp.ui.home.adapters;

import static android.view.View.GONE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;
import static com.easyplexdemoapp.util.Constants.ARG_MOVIE;
import static com.easyplexdemoapp.util.Constants.DEFAULT_WEBVIEW_ADS_RUNNING;
import static com.easyplexdemoapp.util.Constants.ISUSER_MAIN_ACCOUNT;
import static com.easyplexdemoapp.util.Constants.MOVIE_LINK;
import static com.easyplexdemoapp.util.Constants.PLAYER_HEADER;
import static com.easyplexdemoapp.util.Constants.PLAYER_USER_AGENT;
import static com.easyplexdemoapp.util.Constants.SERVER_BASE_URL;
import static com.easyplexdemoapp.util.Constants.VIDSRC_BASE_URL;
import static com.easyplexdemoapp.util.Constants.WEBVIEW;
import static com.easyplexdemoapp.util.Tools.startTrailer;
import static com.google.android.gms.cast.MediaStatus.REPEAT_MODE_REPEAT_OFF;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.appodeal.ads.Appodeal;
import com.appodeal.ads.RewardedVideoCallbacks;
import com.appodeal.ads.initializing.ApdInitializationCallback;
import com.appodeal.ads.initializing.ApdInitializationError;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easyplex.easyplexsupportedhosts.EasyPlexSupportedHosts;
import com.easyplex.easyplexsupportedhosts.Model.EasyPlexSupportedHostsModel;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.Animes;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.local.entity.Series;
import com.easyplexdemoapp.data.model.episode.Episode;
import com.easyplexdemoapp.data.model.episode.EpisodeStream;
import com.easyplexdemoapp.data.model.featureds.Featured;
import com.easyplexdemoapp.data.model.genres.Genre;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.data.model.media.StatusFav;
import com.easyplexdemoapp.data.model.stream.MediaStream;
import com.easyplexdemoapp.data.repository.AnimeRepository;
import com.easyplexdemoapp.data.repository.AuthRepository;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.databinding.RowItemFeaturedBinding;
import com.easyplexdemoapp.ui.animes.AnimeDetailsActivity;
import com.easyplexdemoapp.ui.base.BaseActivity;
import com.easyplexdemoapp.ui.home.AutoScrollControl;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.DeviceManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.ui.moviedetails.MovieDetailsActivity;
import com.easyplexdemoapp.ui.player.activities.EasyPlexMainPlayer;
import com.easyplexdemoapp.ui.player.activities.EasyPlexPlayerActivity;
import com.easyplexdemoapp.ui.player.activities.EmbedActivity;
import com.easyplexdemoapp.ui.player.cast.ExpandedControlsActivity;
import com.easyplexdemoapp.ui.player.cast.queue.QueueDataProvider;
import com.easyplexdemoapp.ui.player.cast.utils.Utils;
import com.easyplexdemoapp.ui.seriedetails.SerieDetailsActivity;
import com.easyplexdemoapp.ui.settings.SettingsActivity;
import com.easyplexdemoapp.ui.streaming.StreamingetailsActivity;
import com.easyplexdemoapp.util.Constants;
import com.easyplexdemoapp.util.DialogHelper;
import com.easyplexdemoapp.util.GlideApp;
import com.easyplexdemoapp.util.HistorySaver;
import com.easyplexdemoapp.util.NetworkUtils;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;


/**
 * Adapter for Featured Movies.
 *
 * @author Yobex.
 */
public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder> {


    private AutoScrollControl autoScrollControl;

    private MaxRewardedAd maxRewardedAd;

    private com.wortise.ads.rewarded.RewardedAd mRewardedWortise;
    private EasyPlexSupportedHosts easyPlexSupportedHosts;
    private String livegenre;
    private CountDownTimer mCountDownTimer;
    private boolean webViewLauched = false;
    private MediaModel mMediaModel;
    private boolean isMovieFav = false;
    private List<Featured> castList;
    private SharedPreferences preferences;
    private AuthManager authManager;
    private SettingsManager settingsManager;

    private DeviceManager deviceManager;

    private Context context;
    private TokenManager tokenManager;
    private MediaRepository mediaRepository;
    private AuthRepository authRepository;
    private AnimeRepository animeRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private boolean adsLaunched = false;
    private RewardedAd mRewardedAd;
    boolean isLoading;

    private static final int PRELOAD_TIME_S = 2;
    private static final String TAG = "FeaturedAdapter";
    private String mediaGenre;
    private Series series;
    private Animes animes;


    @SuppressLint("NotifyDataSetChanged")
    public void addFeatured(List<Featured> castList, Context context, SharedPreferences preferences,
                            MediaRepository mediaRepository, AuthManager authManager, SettingsManager settingsManager,
                            TokenManager tokenManager, AnimeRepository animeRepository, AuthRepository authRepository, AutoScrollControl autoScrollControl,DeviceManager deviceManager) {



        // Create an iterator to traverse the recommendedMoviesList
        Iterator<Featured> iterator = castList.iterator();

        // Loop through the list and remove movies with VIP code if the user doesn't have a matching VIP code
        while (iterator.hasNext()) {
            Featured featured = iterator.next();
            if (featured.getType().equals("Streaming") && settingsManager.getSettings().getSafemode() == 1) {
                iterator.remove();
            }
        }

        this.castList = castList;
        this.context = context;
        this.preferences = preferences;
        this.mediaRepository = mediaRepository;
        this.authManager = authManager;
        this.settingsManager = settingsManager;
        this.tokenManager = tokenManager;
        this.animeRepository = animeRepository;
        this.authRepository = authRepository;
        this.autoScrollControl = autoScrollControl;
        this.deviceManager = deviceManager;
        notifyDataSetChanged();


    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RowItemFeaturedBinding binding = RowItemFeaturedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new FeaturedViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (castList != null) {
            return Math.min(castList.size(), settingsManager.getSettings().getFeaturedHomeNumbers());
        } else {
            return 0;
        }
    }
    class FeaturedViewHolder extends RecyclerView.ViewHolder {


        private final RowItemFeaturedBinding binding;

        FeaturedViewHolder(@NonNull RowItemFeaturedBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }



        @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
        void onBind(final int position) {

            final Featured featured = castList.get(position);

            if (!adsLaunched) {

                createAndLoadRewardedAd();

            }



            int screenHeight = Tools.getHeight(((BaseActivity)context));
            int imageHeight = (int)(screenHeight * 0.75); // Adjust the factor as needed
            binding.itemMovieImage.getLayoutParams().height = imageHeight;
            binding.itemMovieImage.requestLayout();



            if (settingsManager.getSettings().getSafemode() == 1){

                binding.PlayButtonIcon.setText(context.getString(R.string.play_trailer));
            }


            binding.PlayButtonIconLinear.setOnClickListener(v -> binding.PlayButtonIcon.performClick());

            binding.linearAddFavorite.setOnClickListener(v -> binding.addToFavorite.performClick());
            binding.addToFavoriteText.setOnClickListener(v -> binding.addToFavorite.performClick());

            initEasyPlexSupportedHost();


            if ("Anime".equals(featured.getType())) {
                onLoadFeaturedAnimes(featured);
            } else if ("Serie".equals(featured.getType())) {
                onLoadFeaturedSeries(featured);
            } else if ("Movie".equals(featured.getType())) {
                onLoadFeaturedMovies(featured);
            } else if ("Streaming".equals(featured.getType())) {
                onLoadFeaturedStreaming(featured);
            }else if ("Custom".equals(featured.getType())) {
                onLoadFeaturedCustom(featured);
            }

            GlideApp.with(context)
                    .asBitmap()
                    .load(featured.getPosterPath())
                    .centerCrop()
                    .placeholder(R.color.app_background)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(withCrossFade())
                    .into(binding.itemMovieImage);


            GlideApp.with(context).asBitmap().load(featured.getMiniposter())
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(withCrossFade())
                    .override(Tools.getScreenWidth(((BaseActivity)context)), Tools.getHeight(((BaseActivity)context)))
                    .into(binding.miniPoster);



            if (featured.getEnableMiniposter() == 1) {

                binding.miniPoster.setVisibility(View.VISIBLE);

            }else {

                binding.miniPoster.setVisibility(GONE);


            }


            if (featured.getQuality() !=null) {

                binding.qualities.setText(featured.getQuality());

            }else {


                binding.qualities.setVisibility(GONE);
            }



        }

        private void initEasyPlexSupportedHost() {

            easyPlexSupportedHosts = new EasyPlexSupportedHosts(context);

            if (settingsManager.getSettings().getHxfileApiKey() !=null && !settingsManager.getSettings().getHxfileApiKey().isEmpty())  {

                easyPlexSupportedHosts.setApikey(settingsManager.getSettings().getHxfileApiKey());
            }

            easyPlexSupportedHosts.setMainApiServer(SERVER_BASE_URL);
        }

        private void onLoadFeaturedCustom(Featured featured) {

            binding.customAdFeatured.setVisibility(View.VISIBLE);
            binding.linearAddFavorite.setVisibility(GONE);
            binding.PlayButtonIcon.setVisibility(GONE);
            binding.mgenres.setVisibility(GONE);
            binding.movietitle.setText(featured.getTitle());

            binding.rootLayout.setOnClickListener(v -> {

                if (featured.getCustomLink() !=null &&  !featured.getCustomLink().isEmpty()) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(featured.getCustomLink())));
                }else {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.yobex))));
                }
            });
        }

        @SuppressLint("SetTextI18n")
        private void onLoadFeaturedStreaming(Featured featured) {

            binding.customAdFeatured.setVisibility(GONE);

            if (settingsManager.getSettings().getSafemode() == 1){

                binding.PlayButtonIconLinear.setVisibility(GONE);
            }

            binding.rootLayout.setOnClickListener(view -> {


                if (settingsManager.getSettings().getForcewatchbyauth() == 1 && tokenManager.getToken().getAccessToken() ==null){


                    Toast.makeText(context, R.string.you_must_be_logged_in_to_watch_the_stream, Toast.LENGTH_SHORT).show();
                    return;

                }


                mediaRepository.getStream(String.valueOf(featured.getFeaturedId()),
                                settingsManager.getSettings().getCue())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .cache()
                        .subscribe(new Observer<>() {
                            @Override
                            public void onSubscribe(@NotNull Disposable d) {

                                //

                            }

                            @Override
                            public void onNext(@NotNull Media media) {

                                Intent intent = new Intent(context, StreamingetailsActivity.class);
                                intent.putExtra(ARG_MOVIE, media);
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
            });

            if (featured.getPremuim() == 1) {

                binding.btnPremuim.setVisibility(View.VISIBLE);
            }

            Tools.setMargins(binding.PlayButtonIcon, 100, 0, 100, 0);

            binding.featutedMainInfo.setVisibility(GONE);
            binding.linearAddFavorite.setVisibility(GONE);
            binding.viewIslive.setVisibility(View.VISIBLE);
            binding.PlayButtonIcon.setText(R.string.watch_live_streaming);
            binding.movietitle.setText(featured.getTitle());
            binding.mgenres.setText(featured.getGenre());

            binding.PlayButtonIcon.setOnClickListener(view -> mediaRepository.getStream(String.valueOf(featured.getFeaturedId())
                            , settingsManager.getSettings().getCue())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .cache()
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                            //
                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull Media movieDetail) {


                            if (settingsManager.getSettings().getSafemode() == 1){

                                if (preferences.getBoolean(Constants.WIFI_CHECK, false) &&
                                        NetworkUtils.isWifiConnected(context)) {

                                    DialogHelper.showWifiWarning(context);

                                }else {

                                    startTrailer(context,movieDetail.getPreviewPath(),movieDetail.getTitle()
                                            ,movieDetail.getBackdropPath(),settingsManager,movieDetail.getTrailerUrl());

                                }

                                return;

                            }




                            if (settingsManager.getSettings().getLivetvMultiServers() == 1) {


                                if (movieDetail.getVideos() != null && !movieDetail.getVideos().isEmpty()) {


                                    if (movieDetail.getVip() == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() != null) {

                                        onLoadStreamStreaming(movieDetail);

                                    } else if (settingsManager.getSettings().getWachAdsToUnlock() == 1 && movieDetail.getVip() != 1 && authManager.getUserInfo().getPremuim() == 0) {


                                        onLoadSubscribeDialog(movieDetail, "streaming");

                                    } else if (settingsManager.getSettings().getWachAdsToUnlock() == 0 && movieDetail.getVip() == 0) {


                                        onLoadStreamStreaming(movieDetail);


                                    } else if (authManager.getUserInfo().getPremuim() == 1 && movieDetail.getVip() == 0) {


                                        onLoadStreamStreaming(movieDetail);


                                    } else {

                                        DialogHelper.showPremuimWarning(context);

                                    }
                                } else {


                                    DialogHelper.showNoStreamAvailable(context);
                                }

                            } else {


                                if (movieDetail.getLink() != null && !movieDetail.getLink().isEmpty()) {


                                    if (movieDetail.getVip() == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() != null) {

                                        onLoadStreamStreaming(movieDetail);


                                    } else if (settingsManager.getSettings().getWachAdsToUnlock() == 1 && movieDetail.getVip() != 1 && authManager.getUserInfo().getPremuim() == 0) {


                                        onLoadSubscribeDialog(movieDetail, "streaming");

                                    } else if (settingsManager.getSettings().getWachAdsToUnlock() == 0 && movieDetail.getVip() == 0) {


                                        onLoadStreamStreaming(movieDetail);


                                    } else if (authManager.getUserInfo().getPremuim() == 1 && movieDetail.getVip() == 0) {


                                        onLoadStreamStreaming(movieDetail);


                                    } else {

                                        DialogHelper.showPremuimWarning(context);

                                    }
                                } else {

                                    DialogHelper.showNoStreamAvailable(context);
                                }

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
                    }));

        }

        private void onLoadStreamStreaming(Media movieDetail) {


            for (Genre genre : movieDetail.getGenres()) {
                binding.mgenres.setText(genre.getName());
                livegenre = genre.getName();
            }

            CastSession castSession = CastContext.getSharedInstance(context).getSessionManager()
                    .getCurrentCastSession();

            if (settingsManager.getSettings().getLivetvMultiServers() == 1) {


                if (settingsManager.getSettings().getServerDialogSelection() == 1) {
                    String[] charSequence = new String[movieDetail.getVideos().size()];


                    for (int i = 0; i < movieDetail.getVideos().size(); i++) {

                        if (settingsManager.getSettings().getEnablelangsinservers() == 1){

                            charSequence[i] = movieDetail.getVideos().get(i).getServer() + " - " + movieDetail.getVideos().get(i).getLang();

                        }else {

                            charSequence[i] = movieDetail.getVideos().get(i).getServer();
                        }

                    }

                    final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogTheme);
                    builder.setTitle(context.getString(R.string.select_qualities));
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

                        }  else {

                            if (castSession != null && castSession.isConnected()) {

                                startStreamCasting(movieDetail, movieDetail.getVideos().get(wich).getLink());

                            }else {

                                if (settingsManager.getSettings().getVlc() == 1) {

                                    startStreamFromExternalLaunchers(movieDetail,movieDetail.getVideos().get(wich).getLink()
                                            ,movieDetail.getVideos().get(wich).getHls(),movieDetail.getVideos().get(wich));

                                }else {

                                    Tools.startLiveStreaming(context,movieDetail,movieDetail.getVideos().get(wich).getLink(),movieDetail.getVideos().get(wich));
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

                        if (castSession != null && castSession.isConnected()) {

                            startStreamCasting(movieDetail, movieDetail.getVideos().get(0).getLink());

                        }else {

                            if (settingsManager.getSettings().getVlc() == 1) {

                                startStreamFromExternalLaunchers(movieDetail,movieDetail.getVideos().get(0).getLink()
                                        ,movieDetail.getVideos().get(0).getHls(), movieDetail.getVideos().get(0));

                            }else {

                                Tools.startLiveStreaming(context,movieDetail,movieDetail.getVideos().get(0).getLink(), movieDetail.getVideos().get(0));
                            }

                        }

                    }


                }

            }else {

                if (movieDetail.getEmbed() == 1) {

                    startStreamFromEmbed(movieDetail.getLink());


                }  else {

                    if (castSession != null && castSession.isConnected()) {

                        startStreamCasting(movieDetail, movieDetail.getLink());

                    }else {

                        if (settingsManager.getSettings().getVlc() == 1) {

                            startStreamFromExternalLaunchers(movieDetail, movieDetail.getLink(), movieDetail.getHls(), null);

                        }else {

                            Tools.startLiveStreaming(context,movieDetail,movieDetail.getLink(), null);
                        }

                    }

                }

            }

        }

        private void onLoadFeaturedSeries(Featured featured) {

            onLoadRating(featured.getVoteAverage());

            binding.customAdFeatured.setVisibility(GONE);

            onCheckFavoriteSeries(featured);


            try {
                onLoadDate(featured.getReleaseDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            onLoadMediaFavorite(featured,"serie");


            binding.PlayButtonIcon.setOnClickListener(view -> {

                if (settingsManager.getSettings().getForcewatchbyauth() == 1 && tokenManager.getToken().getAccessToken() ==null){


                    Toast.makeText(context, R.string.you_must_be_logged_in_to_watch_the_stream, Toast.LENGTH_SHORT).show();
                    return;

                }

                mediaRepository.getSerie(String.valueOf(featured.getFeaturedId()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .cache()
                        .subscribe(new Observer<>() {
                            @Override
                            public void onSubscribe(@NotNull Disposable d) {

                                //

                            }

                            @Override
                            public void onNext(@NotNull Media movieDetail) {


                                if (settingsManager.getSettings().getSafemode() == 1) {

                                    if (preferences.getBoolean(Constants.WIFI_CHECK, false) &&
                                            NetworkUtils.isWifiConnected(context)) {

                                        DialogHelper.showWifiWarning(context);

                                    } else {

                                        startTrailer(context, movieDetail.getPreviewPath(), movieDetail.getName()
                                                , movieDetail.getBackdropPath(), settingsManager, movieDetail.getTrailerUrl());

                                    }

                                    return;

                                }


                                if (settingsManager.getSettings().getVidsrc() == 1){

                                    String externalId = "tv?imdb=" + movieDetail.getImdbExternalId() +"&season=" +
                                            movieDetail.getSeasons().get(0).getSeasonNumber() + "&episode="+movieDetail.getSeasons().get(0).getEpisodes()
                                            .get(0).getEpisodeNumber();

                                    String link = Constants.VIDSRC_BASE_URL+externalId;

                                    Timber.i(link);

                                    Intent intent = new Intent(context, EmbedActivity.class);
                                    intent.putExtra(MOVIE_LINK, link);
                                    context.startActivity(intent);

                                    return;
                                }


                                if (movieDetail.getSeasons().get(0).getEpisodes().get(0).getVideos() != null
                                        && movieDetail.getSeasons().get(0).getEpisodes().get(0).getVideos().isEmpty()) {

                                    DialogHelper.showNoStreamAvailable(context);

                                } else {

                                    if (movieDetail.getPremuim() == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() != null) {

                                        onLoadStreamSerie(movieDetail);


                                    } else if (movieDetail.getPremuim() == 0 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() != null) {

                                        onLoadStreamSerie(movieDetail);


                                    } else if (settingsManager.getSettings().getEnableWebview() == 1) {


                                        final Dialog dialog = new Dialog(context);
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.setContentView(R.layout.episode_webview);
                                        dialog.setCancelable(false);
                                        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
                                                    webSettings.setJavaScriptEnabled(true);
                                                    webSettings.setSupportMultipleWindows(false);
                                                    webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
                                                    if (settingsManager.getSettings().getWebviewLink() != null && !settingsManager.getSettings().getWebviewLink().isEmpty()) {

                                                        webView.loadUrl(settingsManager.getSettings().getWebviewLink());
                                                    } else {

                                                        webView.loadUrl(SERVER_BASE_URL + WEBVIEW);
                                                    }

                                                    webViewLauched = true;
                                                }

                                            }

                                            @Override
                                            public void onFinish() {

                                                dialog.dismiss();
                                                onLoadStreamSerie(movieDetail);
                                                webViewLauched = false;

                                                if (mCountDownTimer != null) {

                                                    mCountDownTimer.cancel();
                                                    mCountDownTimer = null;

                                                }
                                            }

                                        }.start();


                                        dialog.show();
                                        dialog.getWindow().setAttributes(lp);


                                    } else if (settingsManager.getSettings().getWachAdsToUnlock() == 1 && movieDetail.getPremuim() != 1 && authManager.getUserInfo().getPremuim() == 0) {

                                        onLoadSubscribeDialog(movieDetail, "serie");

                                    } else if (settingsManager.getSettings().getWachAdsToUnlock() == 0 && movieDetail.getPremuim() == 0) {


                                        onLoadStreamSerie(movieDetail);

                                    } else if (authManager.getUserInfo().getPremuim() == 1 && movieDetail.getPremuim() == 0) {

                                        onLoadStreamSerie(movieDetail);


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


            binding.movietitle.setText(featured.getTitle());
            binding.mgenres.setText(featured.getGenre());


            binding.rootLayout.setOnClickListener(view -> mediaRepository.getSerie(String.valueOf(featured.getFeaturedId()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .cache()
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                            //

                        }

                        @Override
                        public void onNext(@NotNull Media media) {

                            Intent intent = new Intent(context, SerieDetailsActivity.class);
                            intent.putExtra(ARG_MOVIE, media);
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
                    }));


            if (featured.getPremuim() == 1) {

                binding.moviePremuim.setVisibility(View.VISIBLE);


            } else {

                binding.moviePremuim.setVisibility(GONE);
            }
        }

        private void onLoadStreamSerie(Media media) {


            CastSession castSession = CastContext.getSharedInstance(context).getSessionManager()
                    .getCurrentCastSession();

            if (settingsManager.getSettings().getServerDialogSelection() == 1) {

                String[] charSequence = new String[media.getSeasons().get(0).getEpisodes().get(0).getVideos().size()];
                for (int i = 0; i<media.getSeasons().get(0).getEpisodes().get(0).getVideos().size(); i++) {



                    if (settingsManager.getSettings().getEnablelangsinservers() == 1){

                        charSequence[i] = media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(i).getServer()
                                + " - " + media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(i).getLang();

                    }else {

                        charSequence[i] = String.valueOf(media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(i).getServer());
                    }

                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogTheme);
                builder.setTitle(R.string.source_quality);
                builder.setCancelable(true);
                builder.setItems(charSequence, (dialogInterface, wich) -> {


                    if (media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getHeader() !=null &&

                            !media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getHeader().isEmpty()) {

                        PLAYER_HEADER = media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getHeader();
                    }


                    if (media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getUseragent() !=null

                            && !media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getUseragent().isEmpty()) {

                        PLAYER_USER_AGENT = media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getUseragent();
                    }



                    String tvseasonid = String.valueOf(media.getSeasons().get(0).getId());
                    Integer currentep = Integer.parseInt(media.getSeasons().get(0).getEpisodes().get(0).getEpisodeNumber());
                    String currentepname = media.getSeasons().get(0).getEpisodes().get(0).getName();
                    String currenteptmdbnumber = String.valueOf(media.getSeasons().get(0).getEpisodes().get(0).getId());
                    String currentseasons = media.getSeasons().get(0).getSeasonNumber();
                    String currentseasonsNumber = media.getSeasons().get(0).getSeasonNumber();
                    String currentepimdb = String.valueOf(media.getSeasons().get(0).getEpisodes().get(0).getId());
                    String artwork = media.getSeasons().get(0).getEpisodes().get(0).getStillPath();
                    float voteAverage = Float.parseFloat(media.getSeasons().get(0).getEpisodes().get(0).getVoteAverage());
                    String type = "1";
                    String currentquality =  media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getServer();
                    String name = "S0" + currentseasons + "E" + media.getSeasons().get(0).getEpisodes().get(0).getEpisodeNumber() + " : " +
                            media.getSeasons().get(0).getEpisodes().get(0).getName();
                    String serieCover = media.getPosterPath();
                    Integer episodeHasRecap = media.getSeasons().get(0).getEpisodes().get(0).getHasrecap();
                    Integer episodeRecapStartIn = media.getSeasons().get(0).getEpisodes().get(0).getSkiprecapStartIn();

                    int hls = media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getHls();


                    int drm =  media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrm();
                    String drmuuid =  media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrmuuid();
                    String drmlicenceuri =  media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrmlicenceuri();

                    for (Genre genre : media.getGenres()) {
                        mediaGenre = genre.getName();
                    }

                    if (media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getEmbed() == 1) {

                        Intent intent = new Intent(context, EmbedActivity.class);
                        intent.putExtra(Constants.MOVIE_LINK, media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getLink());
                        context.startActivity(intent);

                    }else if (media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getSupportedHosts() == 1){


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
                                        builder.setItems(names, (dialogInterface, i) -> {

                                            if (castSession != null && castSession.isConnected()) {


                                                onLoadChromcastSeries(media.getSeasons().get(0).getEpisodes().get(0), castSession, vidURL.get(i).getUrl()
                                                        ,media);


                                            } else if (settingsManager.getSettings().getVlc() == 1) {


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
                                                    Tools.streamEpisodeFromVlc(context,vidURL.get(i).getUrl()
                                                            ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                                    dialog.hide();
                                                });

                                                mxPlayer.setOnClickListener(v12 -> {
                                                    Tools.streamEpisodeFromMxPlayer(context,vidURL.get(i).getUrl()
                                                            ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                                    dialog.hide();

                                                });

                                                webcast.setOnClickListener(v12 -> {

                                                    Tools.streamEpisodeFromMxWebcast(context,vidURL.get(i).getUrl()
                                                            ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                                    dialog.hide();

                                                });


                                                easyplexPlayer.setOnClickListener(v12 -> {


                                                    onLoadEpisodeStream(vidURL.get(i).getUrl(),media.getSeasons().get(0).getEpisodes().get(0),media,
                                                            media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich));
                                                    dialog.hide();
                                                });

                                                dialog.show();
                                                dialog.getWindow().setAttributes(lp);

                                                dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                                dialog.dismiss());


                                                dialog.show();
                                                dialog.getWindow().setAttributes(lp);


                                            } else {
                                                onLoadEpisodeStream(vidURL.get(i).getUrl(),media.getSeasons().get(0).getEpisodes().get(0), media, media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich));

                                            }


                                        });

                                        builder.show();


                                    } else Toast.makeText(context, "NULL", Toast.LENGTH_SHORT).show();

                                } else {

                                    if (castSession != null && castSession.isConnected()) {


                                        onLoadChromcastSeries(media.getSeasons().get(0).getEpisodes().get(0), castSession, vidURL.get(wich).getUrl()
                                                ,media);


                                    } else if (settingsManager.getSettings().getVlc() == 1) {


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
                                            Tools.streamEpisodeFromVlc(context,vidURL.get(0).getUrl()
                                                    ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                            dialog.hide();
                                        });

                                        mxPlayer.setOnClickListener(v12 -> {
                                            Tools.streamEpisodeFromMxPlayer(context,vidURL.get(0).getUrl()
                                                    ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                            dialog.hide();

                                        });

                                        webcast.setOnClickListener(v12 -> {

                                            Tools.streamEpisodeFromMxWebcast(context,vidURL.get(0).getUrl()
                                                    ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                            dialog.hide();

                                        });


                                        easyplexPlayer.setOnClickListener(v12 -> {

                                            onLoadEpisodeStream(vidURL.get(0).getUrl(),media.getSeasons().get(0).getEpisodes().get(0), media, media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich));
                                            dialog.hide();
                                        });

                                        dialog.show();
                                        dialog.getWindow().setAttributes(lp);

                                        dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                                dialog.dismiss());


                                        dialog.show();
                                        dialog.getWindow().setAttributes(lp);


                                    }else {


                                        onLoadEpisodeStream(vidURL.get(0).getUrl(),media.getSeasons().get(0).getEpisodes().get(0), media, media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich));
                                    }


                                }

                            }



                            @Override
                            public void onError() {

                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });

                        easyPlexSupportedHosts.find(media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getLink());


                    }else {


                        if (castSession != null && castSession.isConnected()) {


                            onLoadChromcastSeries(media.getSeasons().get(0).getEpisodes().get(0), castSession, media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getLink()
                                    ,media);


                        } else if (settingsManager.getSettings().getVlc() == 1) {


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
                                Tools.streamEpisodeFromVlc(context,media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getLink()
                                        ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                dialog.hide();
                            });

                            mxPlayer.setOnClickListener(v12 -> {
                                Tools.streamEpisodeFromMxPlayer(context,media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getLink()
                                        ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                dialog.hide();

                            });

                            webcast.setOnClickListener(v12 -> {

                                Tools.streamEpisodeFromMxWebcast(context,media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getLink()
                                        ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                dialog.hide();

                            });


                            easyplexPlayer.setOnClickListener(v12 -> {




                                mMediaModel =  MediaModel.media(media.getId(),
                                        null,
                                        currentquality, type, name, media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getLink(), artwork,
                                        null, currentep
                                        , currentseasons, currentepimdb, tvseasonid,
                                        currentepname,
                                        currentseasonsNumber, null,
                                        currenteptmdbnumber, media.getPremuim(),hls,

                                        null,media.getImdbExternalId(),serieCover,episodeHasRecap,
                                        episodeRecapStartIn,mediaGenre
                                        ,media.getName(),voteAverage,
                                        media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrmuuid()
                                        ,media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrmlicenceuri()
                                        ,media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrm());

                                Intent intent = new Intent(context, EasyPlexMainPlayer.class);
                                intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY,mMediaModel);
                                intent.putExtra(ARG_MOVIE, media);
                                context.startActivity(intent);


                                dialog.hide();
                            });

                            dialog.show();
                            dialog.getWindow().setAttributes(lp);

                            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                    dialog.dismiss());


                            dialog.show();
                            dialog.getWindow().setAttributes(lp);


                        }else {

                            mMediaModel =  MediaModel.media(media.getId(),
                                    null,
                                    currentquality, type, name, media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getLink(), artwork,
                                    null, currentep
                                    , currentseasons, currentepimdb, tvseasonid,
                                    currentepname,
                                    currentseasonsNumber, null,
                                    currenteptmdbnumber, media.getPremuim(),hls,
                                    null
                                    ,media.getImdbExternalId(),
                                    serieCover,episodeHasRecap,
                                    episodeRecapStartIn,mediaGenre,media.getName()
                                    ,voteAverage,media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrmuuid()
                                    ,media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrmlicenceuri(),
                                    media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrm());

                            Intent intent = new Intent(context, EasyPlexMainPlayer.class);
                            intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY,mMediaModel);
                            intent.putExtra(ARG_MOVIE, media);
                            context.startActivity(intent);

                        }
                    }

                });

                builder.show();

            }else {


                if (media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getHeader() !=null &&

                        !media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getHeader().isEmpty()) {

                    PLAYER_HEADER = media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getHeader();
                }


                if (media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getUseragent() !=null

                        && !media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getUseragent().isEmpty()) {

                    PLAYER_USER_AGENT = media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getUseragent();
                }


                String tvseasonid = String.valueOf(media.getSeasons().get(0).getId());
                Integer currentep = Integer.parseInt(media.getSeasons().get(0).getEpisodes().get(0).getEpisodeNumber());
                String currentepname = media.getSeasons().get(0).getEpisodes().get(0).getName();
                String currenteptmdbnumber = String.valueOf(media.getSeasons().get(0).getEpisodes().get(0).getId());
                String currentseasons = media.getSeasons().get(0).getSeasonNumber();
                String currentseasonsNumber = media.getSeasons().get(0).getSeasonNumber();
                String currentepimdb = String.valueOf(media.getSeasons().get(0).getEpisodes().get(0).getId());
                String artwork = media.getSeasons().get(0).getEpisodes().get(0).getStillPath();
                float voteAverage = Float.parseFloat(media.getSeasons().get(0).getEpisodes().get(0).getVoteAverage());
                String type = "1";
                String currentquality =  media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getServer();
                String name = "S0" + currentseasons + "E" + media.getSeasons().get(0).getEpisodes().get(0).getEpisodeNumber() + " : " +
                        media.getSeasons().get(0).getEpisodes().get(0).getName();
                String videourl =  media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getLink();
                String serieCover = media.getPosterPath();
                Integer episodeHasRecap = media.getSeasons().get(0).getEpisodes().get(0).getHasrecap();
                Integer episodeRecapStartIn = media.getSeasons().get(0).getEpisodes().get(0).getSkiprecapStartIn();

                int hls = media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getHls();

                int drm =  media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getDrm();
                String drmuuid =  media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getDrmuuid();
                String drmlicenceuri =  media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getDrmlicenceuri();


                for (Genre genre : media.getGenres()) {
                    mediaGenre = genre.getName();
                }

                if (media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getEmbed() == 1) {

                    Intent intent = new Intent(context, EmbedActivity.class);
                    intent.putExtra(Constants.MOVIE_LINK, videourl);
                    context.startActivity(intent);

                }else if (media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getSupportedHosts() == 1){

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


                                        if (castSession != null && castSession.isConnected()) {


                                            onLoadChromcastSeries(media.getSeasons().get(0).getEpisodes().get(0),
                                                    castSession, vidURL.get(wich).getUrl()
                                                    ,media);


                                        } else if (settingsManager.getSettings().getVlc() == 1) {


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
                                                Tools.streamEpisodeFromVlc(context,vidURL.get(wich).getUrl()
                                                        ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                                dialog.hide();
                                            });

                                            mxPlayer.setOnClickListener(v12 -> {
                                                Tools.streamEpisodeFromMxPlayer(context,vidURL.get(wich).getUrl()
                                                        ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                                dialog.hide();

                                            });

                                            webcast.setOnClickListener(v12 -> {

                                                Tools.streamEpisodeFromMxWebcast(context,vidURL.get(wich).getUrl()
                                                        ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                                dialog.hide();

                                            });


                                            easyplexPlayer.setOnClickListener(v12 -> {
                                                mMediaModel =  MediaModel.media(media.getId(),
                                                        null,
                                                        currentquality, type, name, vidURL.get(wich).getUrl(), artwork,
                                                        null, currentep
                                                        , currentseasons, currentepimdb, tvseasonid,
                                                        currentepname,
                                                        currentseasonsNumber, null,
                                                        currenteptmdbnumber, media.getPremuim(),hls,

                                                        null,media.getImdbExternalId(),
                                                        serieCover,episodeHasRecap,episodeRecapStartIn
                                                        ,mediaGenre,media.getName(),voteAverage,drmuuid,drmlicenceuri,drm);

                                                Intent intent = new Intent(context, EasyPlexMainPlayer.class);
                                                intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY,mMediaModel);
                                                intent.putExtra(ARG_MOVIE, media);
                                                context.startActivity(intent);
                                                dialog.hide();
                                            });

                                            dialog.show();
                                            dialog.getWindow().setAttributes(lp);

                                            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                                    dialog.dismiss());


                                            dialog.show();
                                            dialog.getWindow().setAttributes(lp);


                                        } else {

                                            mMediaModel =  MediaModel.media(media.getId(),
                                                    null,
                                                    currentquality, type, name, vidURL.get(wich).getUrl(), artwork,
                                                    null, currentep
                                                    , currentseasons, currentepimdb, tvseasonid,
                                                    currentepname,
                                                    currentseasonsNumber, null,
                                                    currenteptmdbnumber, media.getPremuim(),hls,

                                                    null,media.getImdbExternalId()
                                                    ,serieCover,episodeHasRecap,
                                                    episodeRecapStartIn,mediaGenre,media.getName(),voteAverage,drmuuid,drmlicenceuri,drm);

                                            Intent intent = new Intent(context, EasyPlexMainPlayer.class);
                                            intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY,mMediaModel);
                                            intent.putExtra(ARG_MOVIE, media);
                                            context.startActivity(intent);

                                        }

                                    });

                                    builder.show();


                                } else Toast.makeText(context, "NULL", Toast.LENGTH_SHORT).show();

                            } else {

                                if (castSession != null && castSession.isConnected()) {


                                    onLoadChromcastSeries(media.getSeasons().get(0).getEpisodes().get(0),
                                            castSession, vidURL.get(0).getUrl()
                                            ,media);


                                } else if (settingsManager.getSettings().getVlc() == 1) {


                                    final Dialog dialog = new Dialog(context);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.dialog_bottom_stream);
                                    dialog.setCancelable(false);
                                    Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

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
                                        Tools.streamEpisodeFromVlc(context,vidURL.get(0).getUrl()
                                                ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                        dialog.hide();
                                    });

                                    mxPlayer.setOnClickListener(v12 -> {
                                        Tools.streamEpisodeFromMxPlayer(context,vidURL.get(0).getUrl()
                                                ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                        dialog.hide();

                                    });

                                    webcast.setOnClickListener(v12 -> {

                                        Tools.streamEpisodeFromMxWebcast(context,vidURL.get(0).getUrl()
                                                ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                        dialog.hide();

                                    });


                                    easyplexPlayer.setOnClickListener(v12 -> {
                                        mMediaModel =  MediaModel.media(media.getId(),
                                                null,
                                                currentquality, type, name, vidURL.get(0).getUrl(), artwork,
                                                null, currentep
                                                , currentseasons, currentepimdb, tvseasonid,
                                                currentepname,
                                                currentseasonsNumber, null,
                                                currenteptmdbnumber, media.getPremuim(),hls,

                                                null,media.getImdbExternalId()
                                                ,serieCover,episodeHasRecap
                                                ,episodeRecapStartIn,
                                                mediaGenre,media.getName(),voteAverage,drmuuid,drmlicenceuri,drm);

                                        Intent intent = new Intent(context, EasyPlexMainPlayer.class);
                                        intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY,mMediaModel);
                                        intent.putExtra(ARG_MOVIE, media);
                                        context.startActivity(intent);
                                        dialog.hide();
                                    });

                                    dialog.show();
                                    dialog.getWindow().setAttributes(lp);

                                    dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                            dialog.dismiss());


                                    dialog.show();
                                    dialog.getWindow().setAttributes(lp);


                                }else {

                                    mMediaModel =  MediaModel.media(media.getId(),
                                            null,
                                            currentquality, type, name, vidURL.get(0).getUrl(), artwork,
                                            null, currentep
                                            , currentseasons, currentepimdb, tvseasonid,
                                            currentepname,
                                            currentseasonsNumber, null,
                                            currenteptmdbnumber, media.getPremuim(),hls,

                                            null,media.getImdbExternalId(),
                                            serieCover,
                                            episodeHasRecap,episodeRecapStartIn,
                                            mediaGenre,media.getName(),voteAverage,drmuuid,drmlicenceuri,drm);

                                    Intent intent = new Intent(context, EasyPlexMainPlayer.class);
                                    intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY,mMediaModel);
                                    intent.putExtra(ARG_MOVIE, media);
                                    context.startActivity(intent);

                                }

                            }

                        }

                        @Override
                        public void onError() {

                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });

                    easyPlexSupportedHosts.find(videourl);


                }else {

                    if (castSession != null && castSession.isConnected()) {


                        onLoadChromcastSeries(media.getSeasons().get(0).getEpisodes().get(0), castSession, videourl
                                ,media);


                    }  else if (settingsManager.getSettings().getVlc() == 1) {

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
                            Tools.streamEpisodeFromVlc(context,videourl
                                    ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                            dialog.hide();
                        });

                        mxPlayer.setOnClickListener(v12 -> {
                            Tools.streamEpisodeFromMxPlayer(context,videourl
                                    ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                            dialog.hide();

                        });

                        webcast.setOnClickListener(v12 -> {

                            Tools.streamEpisodeFromMxWebcast(context,videourl
                                    ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                            dialog.hide();

                        });


                        easyplexPlayer.setOnClickListener(v12 -> {
                            mMediaModel =  MediaModel.media(media.getId(),
                                    null,
                                    currentquality, type, name, videourl, artwork,
                                    null, currentep
                                    , currentseasons, currentepimdb, tvseasonid,
                                    currentepname,
                                    currentseasonsNumber, null,
                                    currenteptmdbnumber, media.getPremuim(),hls,

                                    null,media.getImdbExternalId(),serieCover,
                                    episodeHasRecap,episodeRecapStartIn,mediaGenre,
                                    media.getName(),voteAverage,drmuuid,drmlicenceuri,drm);

                            Intent intent = new Intent(context, EasyPlexMainPlayer.class);
                            intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY,mMediaModel);
                            intent.putExtra(ARG_MOVIE, media);
                            context.startActivity(intent);
                            dialog.hide();
                        });

                        dialog.show();
                        dialog.getWindow().setAttributes(lp);

                        dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                dialog.dismiss());


                        dialog.show();
                        dialog.getWindow().setAttributes(lp);


                    } else {

                        mMediaModel =  MediaModel.media(media.getId(),
                                null,
                                currentquality, type, name, videourl, artwork,
                                null, currentep
                                , currentseasons, currentepimdb, tvseasonid,
                                currentepname,
                                currentseasonsNumber, null,
                                currenteptmdbnumber, media.getPremuim(),hls,

                                null,media.getImdbExternalId(),serieCover
                                ,episodeHasRecap,episodeRecapStartIn,mediaGenre,
                                media.getName(),voteAverage,drmuuid,drmlicenceuri,drm);

                        Intent intent = new Intent(context, EasyPlexMainPlayer.class);
                        intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY,mMediaModel);
                        intent.putExtra(ARG_MOVIE, media);
                        context.startActivity(intent);
                    }


                }


            }

        }


        private void onLoadEpisodeStream(String link, Episode episode, Media media, EpisodeStream episodeStream) {


            String tvseasonid = String.valueOf(media.getSeasons().get(0).getId());
            Integer currentep = Integer.parseInt(media.getSeasons().get(0).getEpisodes().get(0).getEpisodeNumber());
            String currentepname = media.getSeasons().get(0).getEpisodes().get(0).getName();
            String currenteptmdbnumber = String.valueOf(media.getSeasons().get(0).getEpisodes().get(0).getId());
            String currentseasons = media.getSeasons().get(0).getSeasonNumber();
            String currentseasonsNumber = media.getSeasons().get(0).getSeasonNumber();
            String currentepimdb = String.valueOf(media.getSeasons().get(0).getEpisodes().get(0).getId());
            String artwork = media.getSeasons().get(0).getEpisodes().get(0).getStillPath();
            float voteAverage = Float.parseFloat(media.getSeasons().get(0).getEpisodes().get(0).getVoteAverage());
            String type = "1";
            String name = "S0" + currentseasons + "E" + media.getSeasons().get(0).getEpisodes().get(0).getEpisodeNumber() + " : " +
                    media.getSeasons().get(0).getEpisodes().get(0).getName();
            String serieCover = media.getPosterPath();
            Integer episodeHasRecap = media.getSeasons().get(0).getEpisodes().get(0).getHasrecap();
            Integer episodeRecapStartIn = media.getSeasons().get(0).getEpisodes().get(0).getSkiprecapStartIn();

            String currentSeasons = media.getSeasons().get(0).getSeasonNumber();

            String currentSeasonsNumber = media.getSeasons().get(0).getSeasonNumber();

            int hls = episodeStream.getHls();


            int drm =  episodeStream.getDrm();
            String drmuuid =  episodeStream.getDrmuuid();
            String drmlicenceuri =  episodeStream.getDrmlicenceuri();

            mMediaModel =  MediaModel.media(media.getId(),
                    null,
                    null, type, name, link, artwork,
                    null, currentep
                    , currentseasons, currentepimdb, tvseasonid,
                    currentepname,
                    currentseasonsNumber, null,
                    currenteptmdbnumber, media.getPremuim(),hls,
                    null,media.getImdbExternalId()
                    ,serieCover,episodeHasRecap,episodeRecapStartIn,
                    mediaGenre,media.getName(),voteAverage,drmuuid,drmlicenceuri,drm);

            Intent intent = new Intent(context, EasyPlexMainPlayer.class);
            intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY,mMediaModel);
            intent.putExtra(ARG_MOVIE, media);
            context.startActivity(intent);

            HistorySaver.onMEpisodeSave(episode,media,authManager,mediaRepository,mediaGenre,currentSeasons,tvseasonid,currentSeasonsNumber,"1",deviceManager,settingsManager);

        }

        private void onLoadChromcastSeries(Episode episode, CastSession castSession, String url, Media media) {

            String currentepname = episode.getName();
            String artwork = episode.getStillPath();
            String name = media.getName() + " : " +"S0" + media.getSeasons().get(0).getSeasonNumber() + "E" + episode.getEpisodeNumber() + " : " + episode.getName();


            MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
            movieMetadata.putString(MediaMetadata.KEY_TITLE, name);
            movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, currentepname);

            movieMetadata.addImage(new WebImage(Uri.parse(artwork)));
            List<MediaTrack> tracks = new ArrayList<>();


            MediaInfo mediaInfo = new MediaInfo.Builder(url)
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
            PopupMenu popup = new PopupMenu(context, binding.PlayButtonIcon);
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

        private void onLoadFeaturedAnimes(Featured featured) {


            binding.mgenres.setText(featured.getGenre());

            onLoadRating(featured.getVoteAverage());
            try {
                onLoadDate(featured.getReleaseDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            binding.customAdFeatured.setVisibility(GONE);
            binding.movietitle.setText(featured.getTitle());

            if (featured.getPremuim() == 1) {

                binding.moviePremuim.setVisibility(View.VISIBLE);


            } else {

                binding.moviePremuim.setVisibility(GONE);
            }

            binding.rootLayout.setOnClickListener(view -> {


                animeRepository.getAnimeDetails(String.valueOf(featured.getFeaturedId()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .cache()
                        .subscribe(new Observer<>() {
                            @Override
                            public void onSubscribe(@NotNull Disposable d) {

                                //

                            }

                            @Override
                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Media media) {

                                Intent intent = new Intent(context, AnimeDetailsActivity.class);
                                intent.putExtra(ARG_MOVIE, media);
                                context.startActivity(intent);

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
            });


            binding.addToFavorite.setOnClickListener(v -> {

                if (settingsManager.getSettings().getFavoriteonline() == 1 && tokenManager.getToken().getAccessToken() != null) {

                    if (isMovieFav) {

                        authRepository.getDeleteAnimeOnline(String.valueOf(featured.getFeaturedId()))
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

                                        Toast.makeText(context, "Removed From Watchlist", Toast.LENGTH_SHORT).show();

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

                        binding.addToFavorite.setImageResource(R.drawable.add_from_queue);
                        binding.addToFavoriteText.setText(context.getText(R.string.add_to_my_list_player));

                    } else {

                        Timber.i("Added To Watchlist");

                        authRepository.getAddAnimeOnline(String.valueOf(featured.getFeaturedId()))
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

                                        Toast.makeText(context, "Removed From Watchlist", Toast.LENGTH_SHORT).show();


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

                        binding.addToFavorite.setImageResource(R.drawable.ic_in_favorite);
                        binding.addToFavoriteText.setText(context.getText(R.string.added_mylist));

                    }

                }  else {

                    animes  = new Animes(String.valueOf(featured.getFeaturedId()),String.valueOf(featured.getFeaturedId()),featured.getPosterPath(),featured.getTitle());

                    if (mediaRepository.isAnimeFavorite(Integer.parseInt(String.valueOf(featured.getFeaturedId())))) {

                        Timber.i("Removed From Watchlist");
                        compositeDisposable.add(Completable.fromAction(() -> mediaRepository.removeFavoriteAnimes(animes))
                                .subscribeOn(Schedulers.io())
                                .subscribe());

                        binding.addToFavorite.setImageResource(R.drawable.add_from_queue);
                        binding.addToFavoriteText.setText(context.getText(R.string.add_to_my_list_player));

                        Toast.makeText(context, "Removed From Watchlist", Toast.LENGTH_SHORT).show();

                    }else {

                        Timber.i("Added To Watchlist");
                        compositeDisposable.add(Completable.fromAction(() -> mediaRepository.addFavoriteAnime(animes))
                                .subscribeOn(Schedulers.io())
                                .subscribe());

                        binding.addToFavorite.setImageResource(R.drawable.ic_in_favorite);
                        binding.addToFavoriteText.setText(context.getText(R.string.added_mylist));

                        Toast.makeText(context, "Added To Watchlist", Toast.LENGTH_SHORT).show();
                    }

                }

            });


            binding.PlayButtonIcon.setOnClickListener(v -> animeRepository.getAnimeDetails(String.valueOf(featured.getFeaturedId()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                            //

                        }

                        @Override
                        public void onNext(@NotNull Media movieDetail) {


                            if (settingsManager.getSettings().getForcewatchbyauth() == 1 && tokenManager.getToken().getAccessToken() ==null){


                                Toast.makeText(context, R.string.you_must_be_logged_in_to_watch_the_stream, Toast.LENGTH_SHORT).show();
                                return;

                            }





                            if (settingsManager.getSettings().getSafemode() == 1){

                                if (preferences.getBoolean(Constants.WIFI_CHECK, false) &&
                                        NetworkUtils.isWifiConnected(context)) {

                                    DialogHelper.showWifiWarning(context);

                                }else {

                                    startTrailer(context,movieDetail.getPreviewPath(),movieDetail.getName()
                                            ,movieDetail.getBackdropPath(),settingsManager,movieDetail.getTrailerUrl());

                                }

                                return;

                            }


                            if (settingsManager.getSettings().getVidsrc() == 1){


                                String externalId = "tv?imdb=" + movieDetail.getImdbExternalId() +"&season=" + movieDetail.getSeasons().get(0).getSeasonNumber() + "&episode="+movieDetail.getSeasons().get(0)
                                        .getEpisodes().get(0).getEpisodeNumber();

                                String link = Constants.VIDSRC_BASE_URL+externalId;

                                Timber.i(link);

                                Intent intent = new Intent(context, EmbedActivity.class);
                                intent.putExtra(MOVIE_LINK, link);
                                context.startActivity(intent);

                                return;
                            }


                            if (movieDetail.getSeasons().get(0).getEpisodes().get(0).getVideos() != null && movieDetail.getSeasons().get(0).getEpisodes().get(0).getVideos().isEmpty()) {

                                DialogHelper.showNoStreamAvailable(context);

                            } else {

                                if (movieDetail.getPremuim() == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() != null) {

                                    onLoadStreamAnimes(movieDetail);

                                } else if (settingsManager.getSettings().getEnableWebview() == 1) {

                                    final Dialog dialog = new Dialog(context);
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
                                        @SuppressLint("SetTextI18n")
                                        @Override
                                        public void onTick(long millisUntilFinished) {

                                            if (!webViewLauched) {

                                                WebView webView = dialog.findViewById(R.id.webViewVideoBeforeAds);
                                                webView.setWebViewClient(new WebViewClient());
                                                WebSettings webSettings = webView.getSettings();
                                                webSettings.setSupportMultipleWindows(false);
                                                webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
                                                if (settingsManager.getSettings().getWebviewLink() != null && !settingsManager.getSettings().getWebviewLink().isEmpty()) {

                                                    webView.loadUrl(settingsManager.getSettings().getWebviewLink());
                                                } else {

                                                    webView.loadUrl(SERVER_BASE_URL + WEBVIEW);
                                                }

                                                webViewLauched = true;
                                            }

                                        }

                                        @Override
                                        public void onFinish() {

                                            dialog.dismiss();
                                            onLoadStreamAnimes(movieDetail);
                                            webViewLauched = false;

                                            if (mCountDownTimer != null) {

                                                mCountDownTimer.cancel();
                                                mCountDownTimer = null;

                                            }
                                        }

                                    }.start();


                                    dialog.show();
                                    dialog.getWindow().setAttributes(lp);


                                } else if (settingsManager.getSettings().getWachAdsToUnlock() == 1 && movieDetail.getPremuim() != 1 && authManager.getUserInfo().getPremuim() == 0) {

                                    onLoadSubscribeDialog(movieDetail, "anime");

                                } else if (settingsManager.getSettings().getWachAdsToUnlock() == 0 && movieDetail.getPremuim() == 0) {

                                    onLoadStreamAnimes(movieDetail);

                                } else if (authManager.getUserInfo().getPremuim() == 1 && movieDetail.getPremuim() == 0) {


                                    onLoadStreamAnimes(movieDetail);

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
                    }));



            if (mediaRepository.isAnimeFavorite(Integer.parseInt(String.valueOf(featured.getFeaturedId())))) {

                binding.addToFavorite.setImageResource(R.drawable.ic_in_favorite);
                binding.addToFavoriteText.setText(context.getText(R.string.added_mylist));

            } else {


                binding.addToFavorite.setImageResource(R.drawable.add_from_queue);
                binding.addToFavoriteText.setText(context.getText(R.string.add_to_my_list_player));

            }


        }

        private void onLoadRating(float voteAverage) {

            binding.viewMovieRating.setText(String.valueOf(voteAverage));

        }

        private void onLoadStreamAnimes(Media media) {


            CastSession castSession = CastContext.getSharedInstance(context).getSessionManager()
                    .getCurrentCastSession();

            if (settingsManager.getSettings().getServerDialogSelection() == 1) {

                String[] charSequence = new String[media.getSeasons().get(0).getEpisodes().get(0).getVideos().size()];
                for (int i = 0; i<media.getSeasons().get(0).getEpisodes().get(0).getVideos().size(); i++) {



                    if (settingsManager.getSettings().getEnablelangsinservers() == 1){

                        charSequence[i] = media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(i).getServer()
                                + " - " + media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(i).getLang();

                    }else {

                        charSequence[i] = String.valueOf(media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(i).getServer());
                    }

                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogTheme);
                builder.setTitle(R.string.source_quality);
                builder.setCancelable(true);
                builder.setItems(charSequence, (dialogInterface, wich) -> {


                    String tvseasonid = String.valueOf(media.getSeasons().get(0).getId());
                    Integer currentep = Integer.parseInt(media.getSeasons().get(0).getEpisodes().get(0).getEpisodeNumber());
                    String currentepname = media.getSeasons().get(0).getEpisodes().get(0).getName();
                    String currenteptmdbnumber = String.valueOf(media.getSeasons().get(0).getEpisodes().get(0).getTmdbId());
                    String currentseasons = media.getSeasons().get(0).getSeasonNumber();
                    String currentseasonsNumber = media.getSeasons().get(0).getSeasonNumber();
                    String currentepimdb = String.valueOf(media.getSeasons().get(0).getEpisodes().get(0).getTmdbId());
                    String artwork = media.getSeasons().get(0).getEpisodes().get(0).getStillPath();
                    String type = "anime";
                    String name = "S0" + currentseasons + "E" + media.getSeasons().get(0).getEpisodes().get(0).getEpisodeNumber()
                            + " : " + media.getSeasons().get(0).getEpisodes().get(0).getName();
                    String serieCover = media.getBackdropPath();
                    Integer episodeHasRecap = media.getSeasons().get(0).getEpisodes().get(0).getHasrecap();
                    Integer episodeRecapStartIn = media.getSeasons().get(0).getEpisodes().get(0).getSkiprecapStartIn();
                    float voteAverage = Float.parseFloat(media.getSeasons().get(0).getEpisodes().get(0).getVoteAverage());


                    for (Genre genre : media.getGenres()) {
                        mediaGenre = genre.getName();
                    }

                    if (media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getEmbed() == 1) {

                        Intent intent = new Intent(context, EmbedActivity.class);
                        intent.putExtra(Constants.MOVIE_LINK, media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getLink());
                        context.startActivity(intent);

                    }else if (media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getSupportedHosts() == 1) {



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
                                        builder.setItems(names, (dialogInterface, i) -> {

                                            if (castSession != null && castSession.isConnected()) {


                                                onLoadChromcastSeries(media.getSeasons().get(0).getEpisodes().get(0), castSession, vidURL.get(i).getUrl()
                                                        ,media);


                                            }else if (settingsManager.getSettings().getVlc() == 1) {


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
                                                    Tools.streamEpisodeFromVlc(context,vidURL.get(i).getUrl()
                                                            ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                                    dialog.hide();
                                                });

                                                mxPlayer.setOnClickListener(v12 -> {
                                                    Tools.streamEpisodeFromMxPlayer(context,vidURL.get(i).getUrl()
                                                            ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                                    dialog.hide();

                                                });

                                                webcast.setOnClickListener(v12 -> {

                                                    Tools.streamEpisodeFromMxWebcast(context,vidURL.get(i).getUrl()
                                                            ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                                    dialog.hide();

                                                });


                                                easyplexPlayer.setOnClickListener(v12 -> {
                                                    mMediaModel =  MediaModel.media(media.getId(),
                                                            null,
                                                            media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getServer(), type, name, vidURL.get(i).getUrl(), artwork,
                                                            null, currentep
                                                            , currentseasons, currentepimdb, tvseasonid,
                                                            currentepname,
                                                            currentseasonsNumber, null,
                                                            currenteptmdbnumber, media.getPremuim(),media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getHls(),
                                                            null,media.getImdbExternalId()
                                                            ,serieCover,episodeHasRecap,episodeRecapStartIn,
                                                            mediaGenre,media.getName(),voteAverage,media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrmuuid()
                                                            ,media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrmlicenceuri(),media.getSeasons()
                                                                    .get(0).getEpisodes().get(0).getVideos().get(wich).getDrm());

                                                    Intent intent = new Intent(context, EasyPlexMainPlayer.class);
                                                    intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY,mMediaModel);
                                                    intent.putExtra(ARG_MOVIE, media);
                                                    context.startActivity(intent);
                                                    dialog.hide();
                                                });

                                                dialog.show();
                                                dialog.getWindow().setAttributes(lp);

                                                dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                                        dialog.dismiss());


                                                dialog.show();
                                                dialog.getWindow().setAttributes(lp);


                                            }else {


                                                mMediaModel =  MediaModel.media(media.getId(),
                                                        null,
                                                        media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getServer(), type, name, vidURL.get(i).getUrl(), artwork,
                                                        null, currentep
                                                        , currentseasons, currentepimdb, tvseasonid,
                                                        currentepname,
                                                        currentseasonsNumber, null,
                                                        currenteptmdbnumber, media.getPremuim(),media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getHls(),
                                                        null,
                                                        null,serieCover,episodeHasRecap,episodeRecapStartIn
                                                        ,mediaGenre,media.getName(),voteAverage
                                                        ,media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrmuuid(),
                                                        media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrmlicenceuri(),
                                                        media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrm());

                                                Intent intent = new Intent(context, EasyPlexMainPlayer.class);
                                                intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY, mMediaModel);
                                                intent.putExtra(ARG_MOVIE, media);
                                                context.startActivity(intent);

                                            }

                                        });

                                        builder.show();


                                    } else Toast.makeText(context, "NULL", Toast.LENGTH_SHORT).show();

                                } else {



                                    if (castSession != null && castSession.isConnected()) {


                                        onLoadChromcastSeries(media.getSeasons().get(0).getEpisodes().get(0), castSession, vidURL.get(0).getUrl()
                                                ,media);


                                    }else if (settingsManager.getSettings().getVlc() == 1) {


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
                                            Tools.streamEpisodeFromVlc(context,vidURL.get(0).getUrl()
                                                    ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                            dialog.hide();
                                        });

                                        mxPlayer.setOnClickListener(v12 -> {
                                            Tools.streamEpisodeFromMxPlayer(context,vidURL.get(0).getUrl()
                                                    ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                            dialog.hide();

                                        });

                                        webcast.setOnClickListener(v12 -> {

                                            Tools.streamEpisodeFromMxWebcast(context,vidURL.get(0).getUrl()
                                                    ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                            dialog.hide();

                                        });


                                        easyplexPlayer.setOnClickListener(v12 -> {
                                            mMediaModel =  MediaModel.media(media.getId(),
                                                    null,
                                                    media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getServer(), type, name, vidURL.get(0).getUrl(), artwork,
                                                    null, currentep
                                                    , currentseasons, currentepimdb, tvseasonid,
                                                    currentepname,
                                                    currentseasonsNumber, null,
                                                    currenteptmdbnumber, media.getPremuim(),media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getHls(),

                                                    null,media.getImdbExternalId(),
                                                    serieCover,episodeHasRecap,episodeRecapStartIn,
                                                    mediaGenre,media.getName(),voteAverage,media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrmuuid(),
                                                    media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrmlicenceuri(),
                                                    media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrm());

                                            Intent intent = new Intent(context, EasyPlexMainPlayer.class);
                                            intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY,mMediaModel);
                                            intent.putExtra(ARG_MOVIE, media);
                                            context.startActivity(intent);
                                            dialog.hide();
                                        });

                                        dialog.show();
                                        dialog.getWindow().setAttributes(lp);

                                        dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                                dialog.dismiss());


                                        dialog.show();
                                        dialog.getWindow().setAttributes(lp);


                                    }else {


                                        mMediaModel =  MediaModel.media(media.getId(),
                                                null,
                                                media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getServer(), type, name, vidURL.get(0).getUrl(), artwork,
                                                null, currentep
                                                , currentseasons, currentepimdb, tvseasonid,
                                                currentepname,
                                                currentseasonsNumber, null,
                                                currenteptmdbnumber, media.getPremuim(),media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getHls(),
                                                null,
                                                null,serieCover,episodeHasRecap,
                                                episodeRecapStartIn,mediaGenre,media.getName(),
                                                voteAverage,media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrmuuid(),
                                                media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrmlicenceuri(),
                                                media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrm());

                                        Intent intent = new Intent(context, EasyPlexMainPlayer.class);
                                        intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY, mMediaModel);
                                        intent.putExtra(ARG_MOVIE, media);
                                        context.startActivity(intent);

                                    }
                                }


                            }

                            @Override
                            public void onError() {

                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });

                        easyPlexSupportedHosts.find(media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getLink());


                    }else {



                        if (castSession != null && castSession.isConnected()) {


                            onLoadChromcastSeries(media.getSeasons().get(0).getEpisodes().get(0), castSession, media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getLink()
                                    ,media);


                        }else if (settingsManager.getSettings().getVlc() == 1) {


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
                                Tools.streamEpisodeFromVlc(context,media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getLink()
                                        ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                dialog.hide();
                            });

                            mxPlayer.setOnClickListener(v12 -> {
                                Tools.streamEpisodeFromMxPlayer(context,media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getLink()
                                        ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                dialog.hide();

                            });

                            webcast.setOnClickListener(v12 -> {

                                Tools.streamEpisodeFromMxWebcast(context,media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getLink()
                                        ,media.getSeasons().get(0).getEpisodes().get(0),settingsManager);
                                dialog.hide();

                            });


                            easyplexPlayer.setOnClickListener(v12 -> {
                                mMediaModel =  MediaModel.media(media.getId(),
                                        null,
                                        media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getServer(), type, name, media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getLink(), artwork,
                                        null, currentep
                                        , currentseasons, currentepimdb, tvseasonid,
                                        currentepname,
                                        currentseasonsNumber, null,
                                        currenteptmdbnumber, media.getPremuim(),media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getHls(),
                                        null,
                                        null,serieCover,episodeHasRecap,episodeRecapStartIn
                                        ,mediaGenre,media.getName()
                                        ,voteAverage,media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrmuuid(),
                                        media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrmlicenceuri(),
                                        media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrm());

                                Intent intent = new Intent(context, EasyPlexMainPlayer.class);
                                intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY, mMediaModel);
                                intent.putExtra(ARG_MOVIE, media);
                                context.startActivity(intent);
                                dialog.hide();
                            });

                            dialog.show();
                            dialog.getWindow().setAttributes(lp);

                            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                    dialog.dismiss());


                            dialog.show();
                            dialog.getWindow().setAttributes(lp);


                        } else {

                            mMediaModel =  MediaModel.media(media.getId(),
                                    null,
                                    media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getServer(),
                                    type, name, media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getLink(), artwork,
                                    null, currentep
                                    , currentseasons, currentepimdb, tvseasonid,
                                    currentepname,
                                    currentseasonsNumber, null,
                                    currenteptmdbnumber, media.getPremuim(),media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getHls(),
                                    null,
                                    null,serieCover,episodeHasRecap,
                                    episodeRecapStartIn,mediaGenre,media.getName(),voteAverage,media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrmuuid(),
                                    media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrmlicenceuri(),
                                    media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(wich).getDrm());

                            Intent intent = new Intent(context, EasyPlexMainPlayer.class);
                            intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY, mMediaModel);
                            intent.putExtra(ARG_MOVIE, media);
                            context.startActivity(intent);

                        }

                    }



                });

                builder.show();


            }else {


                String tvseasonid = String.valueOf(media.getSeasons().get(0).getId());
                Integer currentep = Integer.parseInt(media.getSeasons().get(0).getEpisodes().get(0).getEpisodeNumber());
                String currentepname = media.getSeasons().get(0).getEpisodes().get(0).getName();
                String currenteptmdbnumber = String.valueOf(media.getSeasons().get(0).getEpisodes().get(0).getTmdbId());
                String currentseasons = media.getSeasons().get(0).getSeasonNumber();
                String currentseasonsNumber = media.getSeasons().get(0).getSeasonNumber();
                String currentepimdb = String.valueOf(media.getSeasons().get(0).getEpisodes().get(0).getTmdbId());
                String artwork = media.getSeasons().get(0).getEpisodes().get(0).getStillPath();
                String type = "anime";
                String currentquality =  media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getServer();
                String name = "S0" + currentseasons + "E" + media.getSeasons().get(0).getEpisodes().get(0).getEpisodeNumber() + " : " + media.getSeasons().get(0).getEpisodes().get(0).getName();
                String videourl =  media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getLink();
                String serieCover = media.getBackdropPath();
                Integer episodeHasRecap = media.getSeasons().get(0).getEpisodes().get(0).getHasrecap();
                Integer episodeRecapStartIn = media.getSeasons().get(0).getEpisodes().get(0).getSkiprecapStartIn();
                int hls = media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getHls();
                float voteAverage = Float.parseFloat(media.getSeasons().get(0).getEpisodes().get(0).getVoteAverage());


                int drm =  media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getDrm();
                String Drmuuid =  media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getDrmuuid();
                String Drmlicenceuri = media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getDrmlicenceuri();

                for (Genre genre : media.getGenres()) {
                    mediaGenre = genre.getName();
                }

                if (media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getEmbed() == 1) {

                    Intent intent = new Intent(context, EmbedActivity.class);
                    intent.putExtra(Constants.MOVIE_LINK, videourl);
                    context.startActivity(intent);

                }else if (media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getSupportedHosts() == 1){


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
                                    builder.setItems(names, (dialogInterface, i) -> {


                                        mMediaModel =  MediaModel.media(media.getId(),
                                                null,
                                                currentquality, type, name, vidURL.get(i).getUrl(), artwork,
                                                null, currentep
                                                , currentseasons, currentepimdb, tvseasonid,
                                                currentepname,
                                                currentseasonsNumber, null,
                                                currenteptmdbnumber, media.getPremuim(),hls,
                                                null,
                                                null,serieCover,episodeHasRecap,
                                                episodeRecapStartIn,mediaGenre,
                                                media.getName(),voteAverage,media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getDrmuuid(),
                                                media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getDrmlicenceuri(),
                                                media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getDrm());

                                        Intent intent = new Intent(context, EasyPlexMainPlayer.class);
                                        intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY, mMediaModel);
                                        intent.putExtra(ARG_MOVIE, media);
                                        context.startActivity(intent);


                                    });

                                    builder.show();


                                } else Toast.makeText(context, "NULL", Toast.LENGTH_SHORT).show();

                            } else {

                                mMediaModel =  MediaModel.media(media.getId(),
                                        null,
                                        currentquality, type, name, vidURL.get(0).getUrl(), artwork,
                                        null, currentep
                                        , currentseasons, currentepimdb, tvseasonid,
                                        currentepname,
                                        currentseasonsNumber, null,
                                        currenteptmdbnumber, media.getPremuim(),hls,
                                        null,
                                        null,serieCover,episodeHasRecap,
                                        episodeRecapStartIn,mediaGenre,media.getName(),
                                        voteAverage,media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getDrmuuid(),
                                        media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getDrmlicenceuri(),
                                        media.getSeasons().get(0).getEpisodes().get(0).getVideos().get(0).getDrm());

                                Intent intent = new Intent(context, EasyPlexMainPlayer.class);
                                intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY, mMediaModel);
                                intent.putExtra(ARG_MOVIE, media);
                                context.startActivity(intent);

                            }

                        }

                        @Override
                        public void onError() {

                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });

                    easyPlexSupportedHosts.find(videourl);


                }else {

                    mMediaModel =  MediaModel.media(media.getId(),
                            null,
                            currentquality, type, name, videourl, artwork,
                            null, currentep
                            , currentseasons, currentepimdb, tvseasonid,
                            currentepname,
                            currentseasonsNumber, null,
                            currenteptmdbnumber, media.getPremuim(),hls,
                            null,
                            null,
                            serieCover,episodeHasRecap,episodeRecapStartIn
                            ,mediaGenre,media.getName(),voteAverage,Drmuuid,Drmlicenceuri,drm);

                    Intent intent = new Intent(context, EasyPlexMainPlayer.class);
                    intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY, mMediaModel);
                    intent.putExtra(ARG_MOVIE, media);
                    context.startActivity(intent);
                }
            }



        }

        private void onCheckFavoriteSeries(Featured featured) {

            if (settingsManager.getSettings().getFavoriteonline() == 1 && tokenManager.getToken().getAccessToken() != null) {

                authRepository.getisSerieFavoriteOnline(String.valueOf(featured.getFeaturedId()))
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

                                if (statusFav.getStatus() == 1) {

                                    isMovieFav = true;

                                    binding.addToFavoriteText.setText(context.getText(R.string.added_mylist));
                                    binding.addToFavorite.setImageResource(R.drawable.ic_in_favorite);

                                } else {

                                    isMovieFav = false;

                                    binding.addToFavorite.setImageResource(R.drawable.add_from_queue);
                                    binding.addToFavoriteText.setText(context.getText(R.string.add_to_my_list_player));

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

                if (mediaRepository.isSerieFavorite(Integer.parseInt(String.valueOf(featured.getFeaturedId())))) {


                    binding.addToFavorite.setImageResource(R.drawable.ic_in_favorite);
                    binding.addToFavoriteText.setText(context.getText(R.string.added_mylist));

                } else {


                    binding.addToFavorite.setImageResource(R.drawable.add_from_queue);
                    binding.addToFavoriteText.setText(context.getText(R.string.add_to_my_list_player));

                }

            }
        }


        @SuppressLint("UseCompatLoadingForDrawables")
        private void onLoadFeaturedMovies(Featured featured) {

            onLoadRating(featured.getVoteAverage());

            binding.customAdFeatured.setVisibility(GONE);

            onCheckFavoriteMovies(featured);

            try {
                onLoadDate(featured.getReleaseDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }


            onLoadMediaFavorite(featured,"movie");

            if (featured.getPremuim() == 1) {

                binding.moviePremuim.setVisibility(View.VISIBLE);


            } else {

                binding.moviePremuim.setVisibility(GONE);
            }



            binding.movietitle.setText(featured.getTitle());

            binding.mgenres.setText(featured.getGenre());


            binding.rootLayout.setOnClickListener(view -> {

                mediaRepository.getMovie(String.valueOf(featured.getFeaturedId()), settingsManager.getSettings().getApiKey())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .cache()
                        .subscribe(new Observer<>() {
                            @Override
                            public void onSubscribe(@NotNull Disposable d) {

                                //

                            }

                            @Override
                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Media media) {

                                Intent intent = new Intent(context, MovieDetailsActivity.class);
                                intent.putExtra(ARG_MOVIE, media);
                                context.startActivity(intent);

                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                                DialogHelper.showNoStreamAvailable(context);
                            }

                            @Override
                            public void onComplete() {

                                //
                            }

                        });
            });

            binding.PlayButtonIcon.setOnClickListener(view -> {

                if (settingsManager.getSettings().getForcewatchbyauth() == 1 && tokenManager.getToken().getAccessToken() ==null){


                    Toast.makeText(context, R.string.you_must_be_logged_in_to_watch_the_stream, Toast.LENGTH_SHORT).show();
                    return;

                }

                onLoadFeaturedStream(featured);
            });
        }

        private void onLoadMediaFavorite(Featured featured, String type) {

            if (type.equals("movie")) {

                binding.addToFavorite.setOnClickListener(v -> mediaRepository.getMovie(String.valueOf(featured.getFeaturedId()),settingsManager.getSettings().getApiKey())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .cache()
                        .subscribe(new Observer<>() {
                            @Override
                            public void onSubscribe(@NotNull Disposable d) {

                                //

                            }

                            @Override
                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Media media) {

                                if (settingsManager.getSettings().getFavoriteonline() == 1 && tokenManager.getToken().getAccessToken() != null) {

                                    if (isMovieFav) {

                                        authRepository.getDeleteMovieOnline(String.valueOf(featured.getFeaturedId()))
                                                .subscribeOn(Schedulers.newThread())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Observer<>() {
                                                    @Override
                                                    public void onSubscribe(@NotNull Disposable d) {

                                                        //

                                                    }

                                                    @Override
                                                    public void onNext(@NotNull StatusFav statusFav) {

                                                        Toast.makeText(context, "Removed From Watchlist", Toast.LENGTH_SHORT).show();


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

                                        binding.addToFavorite.setImageResource(R.drawable.add_from_queue);

                                    } else {

                                        Timber.i("Added To Watchlist");

                                        authRepository.getAddMovieOnline(String.valueOf(featured.getFeaturedId()))
                                                .subscribeOn(Schedulers.newThread())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Observer<>() {
                                                    @Override
                                                    public void onSubscribe(@NotNull Disposable d) {

                                                        //

                                                    }

                                                    @Override
                                                    public void onNext(@NotNull StatusFav statusFav) {

                                                        Toast.makeText(context, "Added " + featured.getTitle() + " To Watchlist", Toast.LENGTH_SHORT).show();

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


                                        binding.addToFavorite.setImageResource(R.drawable.ic_in_favorite);


                                    }

                                } else {

                                    if (mediaRepository.isMovieFavorite(Integer.parseInt(String.valueOf(featured.getFeaturedId())))) {


                                        Timber.i("Removed From Watchlist");
                                        compositeDisposable.add(Completable.fromAction(() -> mediaRepository.removeFavorite(media))
                                                .subscribeOn(Schedulers.io())
                                                .subscribe());

                                        binding.addToFavorite.setImageResource(R.drawable.add_from_queue);
                                        binding.addToFavoriteText.setText(context.getText(R.string.add_to_my_list_player));

                                        Toast.makeText(context, "Removed From Watchlist", Toast.LENGTH_SHORT).show();

                                    } else {

                                        Timber.i("Added To Watchlist");
                                        compositeDisposable.add(Completable.fromAction(() -> mediaRepository.addFavoriteMovie(media))
                                                .subscribeOn(Schedulers.io())
                                                .subscribe());

                                        binding.addToFavorite.setImageResource(R.drawable.ic_in_favorite);
                                        binding.addToFavoriteText.setText(context.getText(R.string.added_mylist));

                                        Toast.makeText(context, "Added To Watchlist", Toast.LENGTH_SHORT).show();
                                    }
                                }


                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                                DialogHelper.showNoStreamAvailable(context);
                            }

                            @Override
                            public void onComplete() {

                                //
                            }

                        }));


                onHandleResume(featured);


            }else if (type.equals("serie")){

                binding.addToFavorite.setOnClickListener(v -> {

                    if (settingsManager.getSettings().getFavoriteonline() == 1 && tokenManager.getToken().getAccessToken() != null) {

                        if (isMovieFav) {

                            authRepository.getDeleteSerieOnline(String.valueOf(featured.getFeaturedId()))
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

                                            Toast.makeText(context, "Removed From Watchlist", Toast.LENGTH_SHORT).show();

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

                            binding.addToFavorite.setImageResource(R.drawable.add_from_queue);

                        } else {

                            Timber.i("Added To Watchlist");

                            authRepository.getAddSerieOnline(String.valueOf(featured.getFeaturedId()))
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

                                            Toast.makeText(context, "Removed From Watchlist", Toast.LENGTH_SHORT).show();


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

                            binding.addToFavorite.setImageResource(R.drawable.ic_in_favorite);

                        }

                    }  else {

                        series  = new Series(String.valueOf(featured.getFeaturedId()),String.valueOf(featured.getFeaturedId()),featured.getPosterPath(),featured.getTitle());

                        if (mediaRepository.isSerieFavorite(Integer.parseInt(String.valueOf(featured.getFeaturedId())))) {

                            Timber.i("Removed From Watchlist");
                            compositeDisposable.add(Completable.fromAction(() -> mediaRepository.removeFavoriteSeries(series))
                                    .subscribeOn(Schedulers.io())
                                    .subscribe());

                            binding.addToFavorite.setImageResource(R.drawable.add_from_queue);
                            binding.addToFavoriteText.setText(context.getText(R.string.add_to_my_list_player));

                            Toast.makeText(context, context.getString(R.string.remove_watch_list), Toast.LENGTH_SHORT).show();

                        }else {

                            Timber.i("Added To Watchlist");
                            compositeDisposable.add(Completable.fromAction(() -> mediaRepository.addFavoriteSerie(series))
                                    .subscribeOn(Schedulers.io())
                                    .subscribe());

                            binding.addToFavorite.setImageResource(R.drawable.ic_in_favorite);
                            binding.addToFavoriteText.setText(context.getText(R.string.added_mylist));

                            Toast.makeText(context, R.string.added_to_watchlist, Toast.LENGTH_SHORT).show();
                        }

                    }

                });
            }


        }

        private void onHandleResume(Featured featured) {


            if (settingsManager.getSettings().getSafemode() == 1){

                binding.PlayButtonIcon.setText(R.string.play_trailer);
                return;
            }

            boolean isMainUser = preferences.getBoolean(ISUSER_MAIN_ACCOUNT,false);


            if (mediaRepository.hasHistory(Integer.parseInt(String.valueOf(featured.getFeaturedId())),
                    isMainUser ? authManager.getUserInfo().getId() : authManager.getSettingsProfile().getId())) {


                binding.PlayButtonIcon.setText(R.string.resume);

            } else {

                binding.PlayButtonIcon.setText(R.string.lecture);

            }
        }

        private void onLoadDate(String date) throws ParseException {

            if (date != null && !date.trim().isEmpty()) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
                Date releaseDate = sdf1.parse(date);
                assert releaseDate != null;
                binding.textMovieRelease.setText(sdf2.format(releaseDate));
            } else {
                binding.textMovieRelease.setText("");}


        }

        private void onCheckFavoriteMovies(Featured featured) {

            if (settingsManager.getSettings().getFavoriteonline() == 1 && tokenManager.getToken().getAccessToken() != null) {

                authRepository.getisMovieFavoriteOnline(String.valueOf(featured.getFeaturedId()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<>() {
                            @Override
                            public void onSubscribe(@NotNull Disposable d) {

                                //

                            }

                            @Override
                            public void onNext(@NotNull StatusFav statusFav) {

                                if (statusFav.getStatus() == 1) {

                                    isMovieFav = true;

                                    binding.addToFavorite.setImageResource(R.drawable.ic_in_favorite);
                                    binding.addToFavoriteText.setText(context.getText(R.string.added_mylist));

                                } else {

                                    isMovieFav = false;

                                    binding.addToFavorite.setImageResource(R.drawable.add_from_queue);
                                    binding.addToFavoriteText.setText(context.getText(R.string.add_to_my_list_player));

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

                if (mediaRepository.isMovieFavorite(Integer.parseInt(String.valueOf(featured.getFeaturedId())))) {


                    binding.addToFavoriteText.setText(context.getText(R.string.added_mylist));
                    binding.addToFavorite.setImageResource(R.drawable.ic_in_favorite);

                } else {


                    binding.addToFavoriteText.setText(context.getText(R.string.add_to_my_list_player));
                    binding.addToFavorite.setImageResource(R.drawable.add_from_queue);

                }

            }

        }


        private void onLoadFeaturedStream(Featured featured) {

            mediaRepository.getMovie(String.valueOf(featured.getFeaturedId()),settingsManager.getSettings().getApiKey())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .cache()
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                            //

                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull Media media) {


                            if (settingsManager.getSettings().getSafemode() == 1) {

                                if (preferences.getBoolean(Constants.WIFI_CHECK, false) &&
                                        NetworkUtils.isWifiConnected(context)) {

                                    DialogHelper.showWifiWarning(context);

                                }else {

                                    startTrailer(context,media.getPreviewPath(),media.getTitle()
                                            ,media.getBackdropPath(),settingsManager,media.getTrailerUrl());

                                }

                                return;

                            }



                            if (media.getEnableStream() !=1) {

                                Toast.makeText(context, R.string.stream_is_currently_not_available_for_this_media, Toast.LENGTH_SHORT).show();
                                return;
                            }



                            if (settingsManager.getSettings().getVidsrc() == 1){

                                String link = VIDSRC_BASE_URL + "movie/"+media.getImdbExternalId();

                                Intent intent = new Intent(context, EmbedActivity.class);
                                intent.putExtra(MOVIE_LINK, link);
                                context.startActivity(intent);

                                return;
                            }

                            if (media.getVideos() != null && !media.getVideos().isEmpty()) {


                                if (media.getPremuim() == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() != null) {


                                    onLoadStream(media);


                                } else if (media.getPremuim() == 0 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() != null) {


                                    onLoadStream(media);


                                } else if (settingsManager.getSettings().getEnableWebview() == 1) {


                                    final Dialog dialog = new Dialog(context);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.episode_webview);
                                    dialog.setCancelable(false);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                    lp.copyFrom(dialog.getWindow().getAttributes());

                                    lp.gravity = Gravity.BOTTOM;
                                    lp.width = MATCH_PARENT;
                                    lp.height = MATCH_PARENT;


                                    mCountDownTimer = new CountDownTimer(DEFAULT_WEBVIEW_ADS_RUNNING, 1000) {
                                        @SuppressLint("SetTextI18n")
                                        @Override
                                        public void onTick(long millisUntilFinished) {

                                            if (!webViewLauched) {

                                                WebView webView = dialog.findViewById(R.id.webViewVideoBeforeAds);
                                                webView.setWebViewClient(new WebViewClient());
                                                WebSettings webSettings = webView.getSettings();
                                                webSettings.setSupportMultipleWindows(false);
                                                webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
                                                if (settingsManager.getSettings().getWebviewLink() != null && !settingsManager.getSettings().getWebviewLink().isEmpty()) {

                                                    webView.loadUrl(settingsManager.getSettings().getWebviewLink());
                                                } else {

                                                    webView.loadUrl(SERVER_BASE_URL + WEBVIEW);
                                                }

                                                webViewLauched = true;
                                            }

                                        }

                                        @Override
                                        public void onFinish() {

                                            dialog.dismiss();
                                            onLoadStream(media);
                                            webViewLauched = false;

                                            if (mCountDownTimer != null) {

                                                mCountDownTimer.cancel();
                                                mCountDownTimer = null;

                                            }
                                        }

                                    }.start();


                                    dialog.show();
                                    dialog.getWindow().setAttributes(lp);


                                } else if (settingsManager.getSettings().getWachAdsToUnlock() == 1 && media.getPremuim() != 1 && authManager.getUserInfo().getPremuim() == 0) {

                                    onLoadSubscribeDialog(media, "movie");

                                } else if (settingsManager.getSettings().getWachAdsToUnlock() == 0 && media.getPremuim() == 0) {


                                    onLoadStream(media);


                                } else if (authManager.getUserInfo().getPremuim() == 1 && media.getPremuim() == 0) {


                                    onLoadStream(media);


                                } else {

                                    DialogHelper.showPremuimWarning(context);

                                }

                            } else {


                                DialogHelper.showNoStreamAvailable(context);
                            }

                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                            DialogHelper.showNoStreamAvailable(context);
                        }

                        @Override
                        public void onComplete() {
                            //
                        }

                    });


        }



        private void onLoadSubscribeDialog(Media media, String type) {


            if (settingsManager.getSettings().getEnabledynamicslider() == 1 && autoScrollControl !=null) {

                autoScrollControl.pauseAutoScroll();
            }


            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_subscribe);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());

            lp.gravity = Gravity.BOTTOM;
            lp.width = MATCH_PARENT;
            lp.height = MATCH_PARENT;


            dialog.findViewById(R.id.view_watch_ads_to_play).setOnClickListener(v -> {

                Toast.makeText(context, R.string.loading_rewards, Toast.LENGTH_SHORT).show();

                String defaultRewardedNetworkAds = settingsManager.getSettings().getDefaultRewardedNetworkAds();


                if (context.getString(R.string.applovin).equals(defaultRewardedNetworkAds)) {

                    onLoadApplovinAds(media,type);

                }else if (context.getString(R.string.vungle).equals(defaultRewardedNetworkAds)) {

                    onLoadVungleAds(media,type);

                }else if (context.getString(R.string.ironsource).equals(defaultRewardedNetworkAds)) {

                    onLoadIronsourceAds(media,type);

                }else if (context.getString(R.string.unityads).equals(defaultRewardedNetworkAds)) {

                    onLoadUnityAds(media,type);


                } else if (context.getString(R.string.admob).equals(defaultRewardedNetworkAds)) {

                    onLoadAdmobRewardAds(media,type);

                } else if (context.getString(R.string.facebook).equals(defaultRewardedNetworkAds)) {

                    onLoadFaceBookRewardAds(media,type);

                } else if (context.getString(R.string.appodeal).equals(defaultRewardedNetworkAds)) {

                    onLoadAppOdealRewardAds(media,type);

                } else if (context.getString(R.string.wortise).equals(defaultRewardedNetworkAds)) {

                    onLoadWortiseRewardAds(media,type);
                }



                if (settingsManager.getSettings().getEnabledynamicslider() == 1 && autoScrollControl !=null) {

                    autoScrollControl.resumeAutoScroll();

                }

                dialog.dismiss();

            });

            dialog.findViewById(R.id.text_view_go_pro).setOnClickListener(v -> {

                context.startActivity(new Intent(context, SettingsActivity.class));

                dialog.dismiss();


            });


            dialog.findViewById(R.id.bt_close).setOnClickListener(v -> {


                if (settingsManager.getSettings().getEnabledynamicslider() == 1 && autoScrollControl !=null) {

                    autoScrollControl.resumeAutoScroll();
                }
                        dialog.dismiss();
                    }
            );


            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }

        private void onLoadWortiseRewardAds(Media media, String type) {


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


                    switch (type) {
                        case "movie":
                            onLoadStream(media);
                            break;
                        case "anime":
                            onLoadStreamAnimes(media);
                            break;
                        case "serie":
                            onLoadStreamSerie(media);
                            break;
                        case "streaming":
                            onLoadStreamStreaming(media);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + type);
                    }


                    mRewardedWortise.loadAd();

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

        private void onLoadApplovinAds(Media media, String type) {

            if (maxRewardedAd.isReady()) {

                maxRewardedAd.showAd();
            }

            maxRewardedAd.setListener(new MaxRewardedAdListener() {
                @Override
                public void onAdLoaded(@NonNull MaxAd ad) {

                }

                @Override
                public void onAdDisplayed(@NonNull MaxAd ad) {

                    maxRewardedAd.loadAd();
                }

                @Override
                public void onAdHidden(@NonNull MaxAd ad) {

                }

                @Override
                public void onAdClicked(@NonNull MaxAd ad) {

                }

                @Override
                public void onAdLoadFailed(@NonNull String adUnitId, @NonNull MaxError error) {

                }

                @Override
                public void onAdDisplayFailed(@NonNull MaxAd ad, @NonNull MaxError error) {

                }

                @Override
                public void onRewardedVideoStarted(@NonNull MaxAd ad) {

                }

                @Override
                public void onRewardedVideoCompleted(@NonNull MaxAd ad) {

                    switch (type) {
                        case "movie":
                            onLoadStream(media);
                            break;
                        case "anime":
                            onLoadStreamAnimes(media);
                            break;
                        case "serie":
                            onLoadStreamSerie(media);
                            break;
                        case "streaming":
                            onLoadStreamStreaming(media);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + type);
                    }

                }

                @Override
                public void onUserRewarded(@NonNull MaxAd ad, @NonNull MaxReward reward) {

                    //

                }
            });
        }


        private void onLoadIronsourceAds(Media media, String type) {

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

                    switch (type) {
                        case "movie":
                            onLoadStream(media);
                            break;
                        case "anime":
                            onLoadStreamAnimes(media);
                            break;
                        case "serie":
                            onLoadStreamSerie(media);
                            break;
                        case "streaming":
                            onLoadStreamStreaming(media);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + type);
                    }
                }

                @Override
                public void onAdClosed(AdInfo adInfo) {

                    //

                }
            });

        }

        private void onLoadVungleAds(Media media, String type) {


            Vungle.loadAd(settingsManager.getSettings().getVungleRewardPlacementName(), new AdConfig(), new LoadAdCallback() {
                @Override
                public void onAdLoad(String placementId) {

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

                            switch (type) {
                                case "movie":
                                    onLoadStream(media);
                                    break;
                                case "anime":
                                    onLoadStreamAnimes(media);
                                    break;
                                case "serie":
                                    onLoadStreamSerie(media);
                                    break;
                                case "streaming":
                                    onLoadStreamStreaming(media);
                                    break;
                                default:
                                    throw new IllegalStateException("Unexpected value: " + type);
                            }
                        }

                        @Override
                        public void onAdEnd(String placementReferenceID) {

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
                public void onError(String placementId, VungleException exception) {

                }
            });


        }


        @SuppressLint("StaticFieldLeak")
        private void onLoadStream(Media media) {


            CastSession castSession = CastContext.getSharedInstance(context).getSessionManager()
                    .getCurrentCastSession();

            if (settingsManager.getSettings().getServerDialogSelection() == 1) {

                String[] charSequence = new String[media.getVideos().size()];
                for (int i = 0; i < media.getVideos().size(); i++) {


                    charSequence[i] = String.valueOf(media.getVideos().get(i).getServer());

                    if (settingsManager.getSettings().getEnablelangsinservers() == 1){

                        charSequence[i] = media.getVideos().get(i).getServer()
                                + " - " + media.getVideos().get(i).getLang();

                    }else {

                        charSequence[i] = media.getVideos().get(i).getServer();
                    }

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

                        Intent intent = new Intent(context, EmbedActivity.class);
                        intent.putExtra(Constants.MOVIE_LINK, media.getVideos().get(wich).getLink());
                        context.startActivity(intent);
                        dialogInterface.dismiss();

                    } else if (media.getVideos().get(wich).getSupportedHosts() == 1){


                        startSupportedHostsStream(media,wich,media.getVideos().get(wich));

                    } else   {


                        if (castSession != null && castSession.isConnected()) {

                            onLoadChromcast(media, castSession, media.getVideos().get(wich).getLink());


                        }  else if (settingsManager.getSettings().getVlc() == 1) {


                            final Dialog dialog = new Dialog(context);
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
                                Tools.streamMediaFromVlc(context,media.getVideos().get(wich).getLink(),media,settingsManager, media.getVideos().get(wich));
                                dialog.hide();
                            });

                            mxPlayer.setOnClickListener(v12 -> {
                                Tools.streamMediaFromMxPlayer(context,media.getVideos().get(wich).getLink(),media,settingsManager, media.getVideos().get(wich));
                                dialog.hide();

                            });

                            webcast.setOnClickListener(v12 -> {
                                Tools.streamMediaFromMxWebcast(context,media.getVideos().get(wich).getLink(),media);
                                dialog.hide();

                            });


                            easyplexPlayer.setOnClickListener(v12 -> {

                                startStreamFromDialog(media,wich, media.getVideos().get(0).getLink());

                                dialog.hide();


                            });

                            dialog.show();
                            dialog.getWindow().setAttributes(lp);

                            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                    dialog.dismiss());
                            dialog.show();
                            dialog.getWindow().setAttributes(lp);


                        } else {

                            startStreamFromDialog(media,wich, media.getVideos().get(wich).getLink());


                        }

                    }


                    dialogInterface.dismiss();



                });

                builder.show();

            } else {



                if (media.getVideos().get(0).getHeader() !=null && !media.getVideos().get(0).getHeader().isEmpty()) {

                    PLAYER_HEADER = media.getVideos().get(0).getHeader();
                }


                if (media.getVideos().get(0).getUseragent() !=null && !media.getVideos().get(0).getUseragent().isEmpty()) {

                    PLAYER_USER_AGENT = media.getVideos().get(0).getUseragent();
                }

                if (media.getVideos().get(0).getEmbed() == 1) {


                    Intent intent = new Intent(context, EmbedActivity.class);
                    intent.putExtra(Constants.MOVIE_LINK, media.getVideos().get(0).getLink());
                    context.startActivity(intent);


                } else if (media.getVideos().get(0).getSupportedHosts() == 1) {


                    startSupportedHostsStream(media,0, media.getVideos().get(0));


                }else {

                    if (castSession != null && castSession.isConnected())
                        onLoadChromcast(media, castSession, media.getVideos().get(0).getLink());
                    else startStreamFromDialog(media, 0, media.getVideos().get(0).getLink());
                }
            }



        }

        private void startSupportedHostsStream(Media media, int wich, MediaStream mediaStream) {

            easyPlexSupportedHosts.onFinish(new EasyPlexSupportedHosts.OnTaskCompleted() {

                @Override
                public void onTaskCompleted(ArrayList<EasyPlexSupportedHostsModel> vidURL, boolean multipleQuality) {

                    if (multipleQuality){
                        if (vidURL!=null) {
                            //This video you can choose qualities

                            String[] charSequence = new String[vidURL.size()];
                            for (int i = 0; i <vidURL.size(); i++) {
                                charSequence[i] = String.valueOf(vidURL.get(i).getQuality());

                            }

                            final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogTheme);
                            builder.setTitle(context.getString(R.string.select_qualities));
                            builder.setCancelable(true);
                            builder.setItems(charSequence, (dialogInterface, i) -> {


                                CastSession castSession = CastContext.getSharedInstance(context).getSessionManager()
                                        .getCurrentCastSession();
                                if (castSession != null && castSession.isConnected()) {

                                    onLoadChromcast(media, castSession, vidURL.get(i).getUrl());


                                } else  if (settingsManager.getSettings().getVlc() == 1) {


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
                                        Tools.streamMediaFromVlc(context,vidURL.get(i).getUrl(),media,settingsManager, mediaStream);
                                        dialog.hide();
                                    });

                                    mxPlayer.setOnClickListener(v12 -> {
                                        Tools.streamMediaFromMxPlayer(context,vidURL.get(i).getUrl(),media,settingsManager, media.getVideos().get(wich));
                                        dialog.hide();

                                    });

                                    webcast.setOnClickListener(v12 -> {
                                        Tools.streamMediaFromMxWebcast(context,vidURL.get(i).getUrl(),media);
                                        dialog.hide();

                                    });


                                    easyplexPlayer.setOnClickListener(v12 -> {

                                        onLoadFeaturedStreamFromDailog(media,vidURL.get(i).getUrl(),vidURL.get(i).getQuality(),wich);
                                        dialog.hide();


                                    });

                                    dialog.show();
                                    dialog.getWindow().setAttributes(lp);

                                    dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                            dialog.dismiss());


                                    dialog.show();
                                    dialog.getWindow().setAttributes(lp);


                                }else {

                                    onLoadFeaturedStreamFromDailog(media,vidURL.get(i).getUrl(),vidURL.get(i).getQuality(),wich);
                                }



                            });

                            builder.show();



                        }else  Toast.makeText(context, "NULL", Toast.LENGTH_SHORT).show();

                    }else {



                        if (settingsManager.getSettings().getVlc() == 1) {


                            final Dialog dialog = new Dialog(context);
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
                                Tools.streamMediaFromVlc(context,vidURL.get(0).getUrl(),media,settingsManager, mediaStream);
                                dialog.hide();
                            });

                            mxPlayer.setOnClickListener(v12 -> {
                                Tools.streamMediaFromMxPlayer(context,vidURL.get(0).getUrl(),media,settingsManager, media.getVideos().get(wich));
                                dialog.hide();

                            });

                            webcast.setOnClickListener(v12 -> {
                                Tools.streamMediaFromMxWebcast(context,vidURL.get(0).getUrl(),media);
                                dialog.hide();

                            });

                            easyplexPlayer.setOnClickListener(v12 -> {
                                onLoadFeaturedStreamFromDailog(media,vidURL.get(0).getUrl(),vidURL.get(0).getQuality(),wich);

                                dialog.hide();


                            });

                            dialog.show();
                            dialog.getWindow().setAttributes(lp);

                            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                    dialog.dismiss());
                            dialog.show();
                            dialog.getWindow().setAttributes(lp);

                        }else {

                            onLoadFeaturedStreamFromDailog(media,vidURL.get(0).getUrl(),vidURL.get(0).getQuality(),wich);}
                    }

                }

                @Override
                public void onError() {

                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            });

            easyPlexSupportedHosts.find(media.getVideos().get(wich).getLink());
        }

        private void onLoadFeaturedStreamFromDailog(Media media, String url, String quality, int wich) {



            for (Genre genre : media.getGenres()) {
                mediaGenre = genre.getName();
            }

            Intent intent = new Intent(context, EasyPlexMainPlayer.class);
            intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY, MediaModel.media(media.getId(), null
                    , quality, "0", media.getTitle(),
                    url, media.getBackdropPath(),
                    null, null
                    , null, null,
                    null, null,
                    null,
                    null,
                    null,
                    media.getPremuim(), media.getVideos().get(wich).getHls(),
                    media.getSubstype(), media.getImdbExternalId(), media.getPosterPath()
                    , media.getHasrecap(), media.getSkiprecapStartIn(),
                    mediaGenre,null,media.getVoteAverage(),media.getVideos().get(wich).getDrmuuid(),
                    media.getVideos().get(wich).getDrmlicenceuri(),media.getVideos().get(wich).getDrm()));
            intent.putExtra(ARG_MOVIE, media);
            context.startActivity(intent);

            HistorySaver.onMovieSave(media,authManager,mediaRepository,mediaGenre,deviceManager,settingsManager);




        }

        private void startStreamFromDialog(Media media, int wich, String downloadUrl) {


            for (Genre genre : media.getGenres()) {
                mediaGenre = genre.getName();
            }


            Intent intent = new Intent(context, EasyPlexMainPlayer.class);
            intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY, MediaModel.media(media.getId(), null
                    , media.getVideos().get(wich).getServer(), "0", media.getTitle(),
                    downloadUrl, media.getBackdropPath(),
                    null, null
                    , null, null,
                    null, null,
                    null,
                    null,
                    null, media.getPremuim(), media.getVideos().get(wich).getHls(), media.getSubstype(), media.getImdbExternalId(), media.getPosterPath()

                    , media.getHasrecap(), media.getSkiprecapStartIn(),
                    mediaGenre,null,media.getVoteAverage(),media.getVideos().get(wich).getDrmuuid(),media.getVideos().get(wich).getDrmlicenceuri(),
                    media.getVideos().get(wich).getDrm()));
            intent.putExtra(ARG_MOVIE, media);
            context.startActivity(intent);

            HistorySaver.onMovieSave(media,authManager,mediaRepository,mediaGenre,deviceManager,settingsManager);

        }


        private void onLoadAppOdealRewardAds(Media media, String type) {

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

                    switch (type) {
                        case "movie":
                            onLoadStream(media);
                            break;
                        case "anime":
                            onLoadStreamAnimes(media);
                            break;
                        case "serie":
                            onLoadStreamSerie(media);
                            break;
                        case "streaming":
                            onLoadStreamStreaming(media);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + type);
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


        private void onLoadFaceBookRewardAds(Media media, String type) {


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

                    switch (type) {
                        case "movie":
                            onLoadStream(media);
                            break;
                        case "anime":
                            onLoadStreamAnimes(media);
                            break;
                        case "serie":
                            onLoadStreamSerie(media);
                            break;
                        case "streaming":
                            onLoadStreamStreaming(media);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + type);
                    }

                }


            };


            facebookInterstitialAd.loadAd(
                    facebookInterstitialAd.buildLoadAdConfig()
                            .withAdListener(interstitialAdListener)
                            .build());


        }

        private void onLoadAdmobRewardAds(Media media, String type) {


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
                            loadRewardedAd();
                        }
                    });
            mRewardedAd.show(
                    (BaseActivity) context,
                    rewardItem -> {
                        switch (type) {
                            case "movie":
                                onLoadStream(media);
                                break;
                            case "anime":
                                onLoadStreamAnimes(media);
                                break;
                            case "serie":
                                onLoadStreamSerie(media);
                                break;
                            case "streaming":
                                onLoadStreamStreaming(media);
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + type);
                        }
                    });


        }

        private void loadRewardedAd() {

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

        private void onLoadUnityAds(Media media, String type) {

            UnityAds.load(settingsManager.getSettings().getUnityRewardPlacementId(), new IUnityAdsLoadListener() {
                @Override
                public void onUnityAdsAdLoaded(String placementId) {

                    UnityAds.show((BaseActivity) context, settingsManager.getSettings().getUnityRewardPlacementId(), new IUnityAdsShowListener() {
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
                            switch (type) {
                                case "movie":
                                    onLoadStream(media);
                                    break;
                                case "anime":
                                    onLoadStreamAnimes(media);
                                    break;
                                case "serie":
                                    onLoadStreamSerie(media);
                                    break;
                                case "streaming":
                                    onLoadStreamStreaming(media);
                                    break;
                                default:
                                    throw new IllegalStateException("Unexpected value: " + type);
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

        private void onLoadChromcast(Media media, CastSession castSession, String downloadUrl) {

            MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
            movieMetadata.putString(MediaMetadata.KEY_TITLE, media.getTitle());
            movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, media.getTitle());

            movieMetadata.addImage(new WebImage(Uri.parse(media.getPosterPath())));
            List<MediaTrack> tracks = new ArrayList<>();


            MediaInfo mediaInfo = new MediaInfo.Builder(downloadUrl)
                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                    .setMetadata(movieMetadata)
                    .setMediaTracks(tracks)
                    .build();

            final RemoteMediaClient remoteMediaClient = castSession.getRemoteMediaClient();
            if (remoteMediaClient == null) {
                Timber.tag(TAG).w("showQueuePopup(): null RemoteMediaClient");
                return;
            }
            final QueueDataProvider provider = QueueDataProvider.getInstance(context);
            PopupMenu popup = new PopupMenu(context, binding.PlayButtonIcon);
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


        private void startStreamCasting(Media movieDetail, String link) {

            MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
            movieMetadata.putString(MediaMetadata.KEY_TITLE, movieDetail.getTitle());
            movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, livegenre);
            movieMetadata.addImage(new WebImage(Uri.parse(movieDetail.getPosterPath())));

            MediaInfo mediaInfo = new MediaInfo.Builder(link)
                    .setStreamType(MediaInfo.STREAM_TYPE_LIVE)
                    .setMetadata(movieMetadata)
                    .build();

            CastSession castSession =
                    CastContext.getSharedInstance(context).getSessionManager().getCurrentCastSession();
            if (castSession == null || !castSession.isConnected()) {
                Timber.tag(TAG).w("showQueuePopup(): not connected to a cast device");
                return;
            }
            final RemoteMediaClient remoteMediaClient = castSession.getRemoteMediaClient();
            if (remoteMediaClient == null) {
                Timber.tag(TAG).w("showQueuePopup(): null RemoteMediaClient");
                return;
            }



            final QueueDataProvider provider = QueueDataProvider.getInstance(context);
            PopupMenu popup = new PopupMenu(context, binding.PlayButtonIcon);
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


        private void createAndLoadRewardedAd() {


            String defaultRewardedNetworkAds = settingsManager.getSettings().getDefaultRewardedNetworkAds();



            if (settingsManager.getSettings().getDefaultRewardedNetworkAds() !=null && context.getString(R.string.applovin).equals(settingsManager.getSettings().getDefaultRewardedNetworkAds())) {

                maxRewardedAd = MaxRewardedAd.getInstance( settingsManager.getSettings().getApplovinRewardUnitid(), (BaseActivity) context );
                maxRewardedAd.loadAd();


            }



            if (context.getString(R.string.wortise).equals(defaultRewardedNetworkAds)) {

                mRewardedWortise = new com.wortise.ads.rewarded.RewardedAd(context, settingsManager.getSettings().getWortiseRewardUnitid());
                mRewardedWortise.loadAd();

            }


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

            Appodeal.initialize((BaseActivity) context, settingsManager.getSettings().getAdUnitIdAppodealRewarded(), Appodeal.REWARDED_VIDEO, list -> {

            });

            IronSource.init(context, settingsManager.getSettings().getIronsourceAppKey(), IronSource.AD_UNIT.REWARDED_VIDEO);


            if (settingsManager.getSettings().getDefaultRewardedNetworkAds() != null && "Admob".equals(settingsManager.getSettings().getDefaultRewardedNetworkAds())) {

                loadRewardedAd();

            }

            if (context.getString(R.string.appodeal).equals(settingsManager.getSettings().getDefaultRewardedNetworkAds())) {

                Appodeal.initialize((BaseActivity) context, settingsManager.getSettings().getAdUnitIdAppodealRewarded(), Appodeal.REWARDED_VIDEO, new ApdInitializationCallback() {
                    @Override
                    public void onInitializationFinished(List<ApdInitializationError> list) {

                    }
                });

            }
            adsLaunched = true;
        }

        private void startStreamFromExternalLaunchers(Media movieDetail, String link, int hls, MediaStream mediaStream) {


            final Dialog dialog = new Dialog(context);
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
                Tools.streamMediaFromVlc(context,link,movieDetail,settingsManager, mediaStream);
                dialog.hide();
            });

            mxPlayer.setOnClickListener(v12 -> {
                Tools.streamMediaFromMxPlayer(context,link,movieDetail,settingsManager, mediaStream);
                dialog.hide();

            });

            webcast.setOnClickListener(v12 -> {
                Tools.streamMediaFromMxWebcast(context,link,movieDetail);
                dialog.hide();

            });


            easyplexPlayer.setOnClickListener(v12 -> {
                startStreamFromDialog(movieDetail, link,hls,mediaStream);
                dialog.hide();


            });

            dialog.show();
            dialog.getWindow().setAttributes(lp);

            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                    dialog.dismiss());


            dialog.show();
            dialog.getWindow().setAttributes(lp);



        }

        private void startStreamFromDialog(Media movieDetail, String link, int hls, MediaStream mediaStream) {

            String artwork = movieDetail.getPosterPath();
            String name = movieDetail.getName();
            String type = "streaming";
            Intent intent = new Intent(context, EasyPlexMainPlayer.class);
            intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY, MediaModel.media(movieDetail.getId(),
                    null,null,type, name, link, artwork, null
                    , null, null,null,
                    null,null,
                    null,
                    null,null,null,hls,null,null,
                    null,0,0,null,null,
                    0,mediaStream.getDrmuuid(),mediaStream.getDrmlicenceuri(),mediaStream.getDrm()));
            intent.putExtra(ARG_MOVIE, movieDetail);
            context.startActivity(intent);

        }

        private void startStreamFromEmbed(String link) {


            Intent intent = new Intent(context, EmbedActivity.class);
            intent.putExtra(Constants.MOVIE_LINK, link);
            context.startActivity(intent);
        }


    }





    @Override
    public void onViewDetachedFromWindow(@NonNull FeaturedViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        adsLaunched = false;
        mRewardedAd = null;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        adsLaunched = false;
        mRewardedAd = null;
    }
}

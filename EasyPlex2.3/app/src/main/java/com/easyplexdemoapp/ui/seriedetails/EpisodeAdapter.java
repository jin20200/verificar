package com.easyplexdemoapp.ui.seriedetails;

import static android.view.View.GONE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.easyplexdemoapp.util.Constants.ARG_MOVIE;
import static com.easyplexdemoapp.util.Constants.DEFAULT_WEBVIEW_ADS_RUNNING;
import static com.easyplexdemoapp.util.Constants.E;
import static com.easyplexdemoapp.util.Constants.ISUSER_MAIN_ACCOUNT;
import static com.easyplexdemoapp.util.Constants.MOVIE_LINK;
import static com.easyplexdemoapp.util.Constants.PLAYER_HEADER;
import static com.easyplexdemoapp.util.Constants.PLAYER_USER_AGENT;
import static com.easyplexdemoapp.util.Constants.S0;
import static com.easyplexdemoapp.util.Constants.SEASONS;
import static com.easyplexdemoapp.util.Constants.SERVER_BASE_URL;
import static com.easyplexdemoapp.util.Constants.VIDSRC_BASE_URL;
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
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.easyplexdemoapp.data.local.entity.Download;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.MovieResponse;
import com.easyplexdemoapp.data.model.comments.Comment;
import com.easyplexdemoapp.data.model.episode.Episode;
import com.easyplexdemoapp.data.model.episode.EpisodeStream;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.data.model.media.Resume;
import com.easyplexdemoapp.data.model.report.Report;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.databinding.RowSeasonsBinding;
import com.easyplexdemoapp.ui.comments.CommentsAdapter;
import com.easyplexdemoapp.ui.downloadmanager.core.RepositoryHelper;
import com.easyplexdemoapp.ui.downloadmanager.core.model.data.entity.DownloadInfo;
import com.easyplexdemoapp.ui.downloadmanager.core.settings.SettingsRepository;
import com.easyplexdemoapp.ui.downloadmanager.ui.adddownload.AddDownloadActivity;
import com.easyplexdemoapp.ui.downloadmanager.ui.adddownload.AddDownloadDialog;
import com.easyplexdemoapp.ui.downloadmanager.ui.adddownload.AddInitParams;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.DeviceManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.ui.player.activities.EasyPlexMainPlayer;
import com.easyplexdemoapp.ui.player.activities.EasyPlexPlayerActivity;
import com.easyplexdemoapp.ui.player.activities.EmbedActivity;
import com.easyplexdemoapp.ui.player.cast.ExpandedControlsActivity;
import com.easyplexdemoapp.ui.player.cast.queue.QueueDataProvider;
import com.easyplexdemoapp.ui.player.cast.utils.Utils;
import com.easyplexdemoapp.ui.player.fsm.state_machine.FsmPlayerApi;
import com.easyplexdemoapp.ui.settings.SettingsActivity;
import com.easyplexdemoapp.util.AppController;
import com.easyplexdemoapp.util.Constants;
import com.easyplexdemoapp.util.DialogHelper;
import com.easyplexdemoapp.util.GlideApp;
import com.easyplexdemoapp.util.HistorySaver;
import com.easyplexdemoapp.util.SpacingItemDecoration;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.Collections;
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
 * Adapter for Series Episodes.
 *
 * @author Yobex.
 */
public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder> {


    private com.wortise.ads.rewarded.RewardedAd mRewardedWortise;
    private MaxRewardedAd maxRewardedAd;
    private static final String TAG_DOWNLOAD_DIALOG = "add_download_dialog";
    private AddDownloadDialog addDownloadDialog;
    private CountDownTimer mCountDownTimer;
    private boolean webViewLauched = false;
    private List<Episode> episodeList;
    private final String externalId;
    private final String currentSerieId;
    private final String currentSeasons;
    private Download download;
    private final Media media;
    final String seasonId;
    private boolean adsLaunched = false;
    private final String currentSeasonsNumber;
    private final String currentTvShowName;
    private final int premuim;
    private final String serieCover;
    private final SharedPreferences preferences;
    private final AuthManager authManager;
    private final SettingsManager settingsManager;
    private final Context context;
    private RewardedAd mRewardedAd;
    boolean isLoading;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MediaRepository mediaRepository;

    private final DeviceManager deviceManager;
    private final TokenManager tokenManager;
    private static final int PRELOAD_TIME_S = 2;
    private EasyPlexSupportedHosts easyPlexSupportedHosts;
    private final String mediaGenre;
    private CommentsAdapter commentsAdapter;

    final AppController appController;

    private boolean isReversed = false;


    public EpisodeAdapter(String serieid, String seasonsid, String seasonsidpostion, String currentseason, SharedPreferences preferences, AuthManager authManager

            , SettingsManager settingsManager, MediaRepository mediaRepository, String currentTvShowName, int
        premuim, TokenManager tokenManager, Context context, String serieCover, Media media, String mediaGenre, String externalId,AppController appController,DeviceManager deviceManager) {
        this.currentSerieId = serieid;
        this.currentSeasons = seasonsid;
        this.seasonId = seasonsidpostion;
        this.preferences = preferences;
        this.authManager = authManager;
        this.settingsManager = settingsManager;
        this.currentSeasonsNumber = currentseason;
        this.currentTvShowName = currentTvShowName;
        this.premuim = premuim;
        this.tokenManager = tokenManager;
        this.mediaRepository = mediaRepository;
        this.serieCover = serieCover;
        this.context = context;
        this.media = media;
        this.mediaGenre = mediaGenre;
        this.externalId = externalId;
        this.appController = appController;
        this.deviceManager = deviceManager;

    }



    public void reverseEpisodeOrder() {
        Collections.reverse(episodeList);
        isReversed = !isReversed;
    }


    public boolean isReversed() {
        return isReversed;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addSeasons(List<Episode> episodeList) {
        this.episodeList = episodeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RowSeasonsBinding binding = RowSeasonsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        binding.setController(appController);


        return new EpisodeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (episodeList != null) {
            return episodeList.size();
        } else {
            return 0;
        }
    }

    class EpisodeViewHolder extends RecyclerView.ViewHolder {

        private final RowSeasonsBinding binding;

        EpisodeViewHolder(@NonNull RowSeasonsBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;
        }

        @SuppressLint({"SetTextI18n", "RestrictedApi"})
        void onBind(final int position) {

            appController.isDownloadEnabled.set(settingsManager.getSettings().getEnableDownload() == 1);

            final Episode episode = episodeList.get(position);

            if (episode.getStillPath() == null) {

                episode.setStillPath(settingsManager.getSettings().getDefaultMediaPlaceholderPath());
            }

            if (!adsLaunched) {

                createAndLoadRewardedAd();

                initLoadRewardedAd();

            }


            if (settingsManager.getSettings().getSafemode() == 1){

                binding.miniPlay.setVisibility(GONE);
                binding.downloadEpisode.setVisibility(GONE);
            }


            download = new Download(String.valueOf(episode.getId()),String.valueOf(episode.getId()),episode.getStillPath(),currentTvShowName + " : " + "S0" +
                    currentSeasons + "E" + episode.getEpisodeNumber() +
                    " : " + episode.getName(),episode.getLink());

            Tools.onLoadMediaCoverEpisode(context,binding.epcover,episode.getStillPath());

            binding.eptitle.setText(episode.getEpisodeNumber() +" - " +episode.getName());
            binding.epoverview.setText(episode.getOverview());


            if (settingsManager.getSettings().getResumeOffline() == 1) {


                onLoadEpisodeOffline(episode);


            }else {


                onLoadEpisodeOnline(episode);

            }


            binding.epLayout.setOnClickListener(v -> {
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
                if (episode.getEnableStream() != 1) {
                    Toast.makeText(context, R.string.stream_is_currently_not_available_for_this_media, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Trigger the method to handle more options icons click
                onClickMoreOptionsIcons(episode, position);
            });




            if (settingsManager.getSettings().getEnableDownload() == 0) {

                binding.downloadEpisode.setImageResource(R.drawable.ic_notavailable);
            }

            binding.downloadEpisode.setOnClickListener(v -> {


                if (settingsManager.getSettings().getEnableDownload() == 0) {

                    DialogHelper.showNoDownloadAvailable(context,context.getString(R.string.download_disabled));

                }else    if (episode.getEnableMediaDownload() == 0) {

                    Toast.makeText(context, R.string.download_is_currently_not_available_for_this_media, Toast.LENGTH_SHORT).show();

                }else  onLoadEpisodeDownloadInfo(episode, position);


            });

            binding.miniPlay.setOnClickListener(v -> {

                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.inflate(R.menu.episode_item_popup);
                popup.getMenu().findItem(R.id.episode_comments).setVisible(settingsManager.getSettings().getEnableComments() != 0);
                popup.setForceShowIcon(true);
                popup.setOnMenuItemClickListener(item -> episodeMiniMenuClicked(item,episode, position));
                popup.show();

            });

        }

        @SuppressLint("NonConstantResourceId")
        private boolean episodeMiniMenuClicked(MenuItem item, Episode episode, int position) {
            int itemId = item.getItemId();
            if (itemId == R.id.play_menu) {
                onClickMoreOptionsIconsDot(episode, position);
            } else if (itemId == R.id.report_menu) {
                final Dialog dialog = new Dialog(context);
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


                String name = currentTvShowName + " : " + S0 + currentSeasons + E + episode.getEpisodeNumber() + " : " + episode.getName();

                reportMovieName.setText(name);

                Tools.onLoadMediaCover(context, imageView, episode.getStillPath());


                dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());
                dialog.findViewById(R.id.view_report).setOnClickListener(v -> {


                    editTextMessage.getText();

                    if (editTextMessage.getText() != null) {


                        mediaRepository.getReport(settingsManager.getSettings().getApiKey(), name, editTextMessage.getText().toString())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<>() {
                                    @Override
                                    public void onSubscribe(@NotNull Disposable d) {

                                        //

                                    }

                                    @Override
                                    public void onNext(@NotNull Report report) {

                                        dialog.dismiss();


                                        Toast.makeText(context, context.getString(R.string.report_sent), Toast.LENGTH_SHORT).show();
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


                });

                dialog.show();
                dialog.getWindow().setAttributes(lp);

                dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                        dialog.dismiss());


                dialog.show();
                dialog.getWindow().setAttributes(lp);
            } else if (itemId == R.id.episode_comments) {
                if (tokenManager.getToken().getAccessToken() != null) {
                    onLoadSerieComments(episode.getId());
                } else {
                    Toast.makeText(context, "You need to login to able to add a comment !", Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }

        private void onLoadEpisodeDownloadInfo(Episode episode, int position) {

            String defaultDownloadsOptions = settingsManager.getSettings().getDefaultDownloadsOptions();
            if ("Free".equals(defaultDownloadsOptions)) {
                onLoadDownloadsList(episode);
            } else if ("PremuimOnly".equals(defaultDownloadsOptions)) {

                if (premuim == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() != null) {

                    onLoadDownloadsList(episode);

                } else if (premuim == 0 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() != null) {

                    onLoadDownloadsList(episode);

                }else {

                    DialogHelper.showPremuimWarning(context);
                }
            } else if ("WithAdsUnlock".equals(defaultDownloadsOptions)) {

                if (premuim == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() != null) {

                    onLoadDownloadsList(episode);

                } else if (premuim == 0 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() != null) {

                    onLoadDownloadsList(episode);

                }else {

                    onLoadSubscribeDialog(episode,position,false);
                }
            }
        }

        @SuppressLint("SetTextI18n")
        private void onClickMoreOptionsIconsDot(Episode episode, int position) {

            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_mini_play);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());

            lp.gravity = Gravity.BOTTOM;
            lp.width = MATCH_PARENT;
            lp.height = MATCH_PARENT;

            TextView movieName = dialog.findViewById(R.id.text_view_video_next_release_date);
            TextView movieoverview = dialog.findViewById(R.id.text_overview_label);
            AppCompatRatingBar appCompatRatingBar = dialog.findViewById(R.id.rating_bar);
            TextView viewMovieRating = dialog.findViewById(R.id.view_movie_rating);
            ImageView imageView = dialog.findViewById(R.id.next_cover_media);
            ProgressBar progressBar = dialog.findViewById(R.id.resume_progress_bar);
            TextView epResumeTitle = dialog.findViewById(R.id.epResumeTitle);
            TextView mseaons = dialog.findViewById(R.id.mseason);
            TextView timeRemaning = dialog.findViewById(R.id.timeRemaning);
            LinearLayout linearLayouttimeRemaning = dialog.findViewById(R.id.resumePlayProgress);
            LinearLayout linearResume = dialog.findViewById(R.id.resumeLinear);
            Button playButtonIcon = dialog.findViewById(R.id.PlayButtonIcon);
            ImageView episodeDownload = dialog.findViewById(R.id.episodeDownload);
            episodeDownload.setOnClickListener(v -> binding.downloadEpisode.performClick());

            playButtonIcon.setOnClickListener(v -> {


                if (episode.getEnableStream() !=1) {

                    Toast.makeText(context, context.getString(R.string.stream_is_currently_not_available_for_this_media), Toast.LENGTH_SHORT).show();
                    return;
                }

                onClickMoreOptionsIcons(episode,position);
                dialog.dismiss();
            });


            onHandleResume(episode, progressBar, timeRemaning, linearLayouttimeRemaning, linearResume);


            movieName.setText(episode.getName());
            appCompatRatingBar.setRating(Float.parseFloat(episode.getVoteAverage()) / 2);
            viewMovieRating.setText(String.valueOf(episode.getVoteAverage()));
            epResumeTitle.setText(episode.getName());
            mseaons.setText(SEASONS + currentSeasons);
            movieName.setText(episode.getName());
            movieoverview.setText(episode.getOverview());

            GlideApp.with(context).load(episode.getStillPath())
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);

            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->
                    dialog.dismiss());

            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }

        private void onHandleResume(Episode episode, ProgressBar progressBar, TextView timeRemaning, LinearLayout linearLayouttimeRemaning, LinearLayout linearResume) {


            if (settingsManager.getSettings().getSafemode() == 1){

                binding.miniPlay.setVisibility(GONE);
                binding.downloadEpisode.setVisibility(GONE);
                return;
            }

            if (preferences.getBoolean(ISUSER_MAIN_ACCOUNT,false)){

                mediaRepository.hasResume(episode.getId()).observe((SerieDetailsActivity) context, resumeInfo -> {

                    if (resumeInfo != null) {

                        if (resumeInfo.getTmdb() != null && resumeInfo.getResumePosition() !=null

                                && resumeInfo.getTmdb().equals(String.valueOf(episode.getId())) && Tools.id(context).equals(resumeInfo.getDeviceId())) {



                            binding.episodewatched.setVisibility(View.VISIBLE);

                            double d = resumeInfo.getResumePosition();

                            double moveProgress = d * 100 / resumeInfo.getMovieDuration();

                            progressBar.setVisibility(View.VISIBLE);
                            linearLayouttimeRemaning.setVisibility(View.VISIBLE);
                            progressBar.setProgress((int) moveProgress);
                            timeRemaning.setText(Tools.getProgressTime((resumeInfo.getMovieDuration() - resumeInfo.getResumePosition()), true));
                            timeRemaning.setVisibility(View.VISIBLE);
                            linearResume.setVisibility(View.VISIBLE);



                        } else {

                            progressBar.setProgress(0);
                            progressBar.setVisibility(GONE);
                            timeRemaning.setVisibility(GONE);
                            linearLayouttimeRemaning.setVisibility(GONE);
                            linearResume.setVisibility(GONE);

                        }

                    }else {

                        binding.episodewatched.setVisibility(GONE);
                        progressBar.setProgress(0);
                        progressBar.setVisibility(GONE);
                        linearLayouttimeRemaning.setVisibility(GONE);
                        timeRemaning.setVisibility(GONE);
                        linearResume.setVisibility(GONE);
                    }

                });


            }else {

                Integer userId = settingsManager.getSettings().getProfileSelection() == 1  ? authManager.getSettingsProfile().getId() : authManager.getUserInfo().getId();

                mediaRepository.hasResumeProfile(episode.getId(),userId).observe((SerieDetailsActivity) context, resumeInfo -> {

                    if (resumeInfo != null) {

                        if (resumeInfo.getTmdb() != null && resumeInfo.getResumePosition() !=null

                                && resumeInfo.getTmdb().equals(String.valueOf(episode.getId())) && Tools.id(context).equals(resumeInfo.getDeviceId())) {



                            binding.episodewatched.setVisibility(View.VISIBLE);

                            double d = resumeInfo.getResumePosition();

                            double moveProgress = d * 100 / resumeInfo.getMovieDuration();

                            progressBar.setVisibility(View.VISIBLE);
                            linearLayouttimeRemaning.setVisibility(View.VISIBLE);
                            progressBar.setProgress((int) moveProgress);
                            timeRemaning.setText(Tools.getProgressTime((resumeInfo.getMovieDuration() - resumeInfo.getResumePosition()), true));
                            timeRemaning.setVisibility(View.VISIBLE);
                            linearResume.setVisibility(View.VISIBLE);



                        } else {

                            progressBar.setProgress(0);
                            progressBar.setVisibility(GONE);
                            timeRemaning.setVisibility(GONE);
                            linearLayouttimeRemaning.setVisibility(GONE);
                            linearResume.setVisibility(GONE);

                        }

                    }else {

                        binding.episodewatched.setVisibility(GONE);
                        progressBar.setProgress(0);
                        progressBar.setVisibility(GONE);
                        linearLayouttimeRemaning.setVisibility(GONE);
                        timeRemaning.setVisibility(GONE);
                        linearResume.setVisibility(GONE);
                    }

                });

            }
        }

        private void onClickMoreOptionsIcons(Episode episode, int position) {

            if (premuim == 1 && authManager.getUserInfo().getPremuim() == 1 && tokenManager.getToken() != null) {
                onStartEpisode(episode, position);
                return;
            }

            if (episode.getEnableAdsUnlock() == 1) {
                if (premuim == 1 && authManager.getUserInfo().getPremuim() == 1) {
                    onStartEpisode(episode, position);
                } else if (authManager.getUserInfo().getPremuim() == 1 && premuim == 0) {
                    onStartEpisode(episode, position);
                } else {
                    onLoadSubscribeDialog(episode, position, true);
                }
                return;
            }

            if (settingsManager.getSettings().getWachAdsToUnlock() == 1 && premuim != 1 && authManager.getUserInfo().getPremuim() == 0) {
                if (settingsManager.getSettings().getEnableWebview() == 1) {
                    onLoadWebviewRewardsAds(episode, position);
                } else {
                    onLoadSubscribeDialog(episode, position, true);
                }
                return;
            }

            if (settingsManager.getSettings().getWachAdsToUnlock() == 0 && premuim == 0) {
                onStartEpisode(episode, position);
                return;
            }

            if (authManager.getUserInfo().getPremuim() == 1 && premuim == 0) {
                onStartEpisode(episode, position);
                return;
            }

            DialogHelper.showPremuimWarning(context);
        }


        private void onLoadWebviewRewardsAds(Episode episode, int position) {
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
                        if (settingsManager.getSettings().getWebviewLink() !=null && !settingsManager.getSettings().getWebviewLink().isEmpty()) {

                            webView.loadUrl(settingsManager.getSettings().getWebviewLink());
                        }else {

                            webView.loadUrl(SERVER_BASE_URL+"webview");
                        }

                        webViewLauched = true;
                    }

                }

                @Override
                public void onFinish() {

                    dialog.dismiss();
                    onStartEpisode(episode, position);
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

        private void onLoadEpisodeOnline(Episode episode) {

            mediaRepository.getResumeById(String.valueOf(episode.getId()),settingsManager.getSettings().getApiKey())
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


                            if (resume.getTmdb() != null && resume.getResumePosition() != null

                                    && resume.getTmdb().equals(String.valueOf(episode.getId())) && Tools.id(context).equals(resume.getDeviceId())) {


                                double d = resume.getResumePosition();

                                double moveProgress = d * 100 / resume.getMovieDuration();


                                binding.episodewatched.setVisibility(View.VISIBLE);
                                binding.resumeProgressBar.setVisibility(View.VISIBLE);
                                binding.resumeProgressBar.setProgress((int) moveProgress);

                                binding.timeRemaning.setText(Tools.getProgressTime((resume.getMovieDuration() - resume.getResumePosition()), true));


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

                            binding.episodewatched.setVisibility(GONE);

                        }

                        @Override
                        public void onComplete() {

                            //

                        }
                    });

        }


        @SuppressLint("StaticFieldLeak")
        private void onStartEpisode(Episode episode, int position) {

            if (settingsManager.getSettings().getVidsrc() == 1){


                String externalId = "tv?imdb=" + media.getImdbExternalId() +"&season=" + currentSeasons + "&episode="+episode.getEpisodeNumber();

                String link = Constants.VIDSRC_BASE_URL+externalId;

                Timber.i(link);

                Intent intent = new Intent(context, EmbedActivity.class);
                intent.putExtra(MOVIE_LINK, link);
                context.startActivity(intent);

                return;
            }


            if (episode.getVideos().isEmpty()) {
                DialogHelper.showNoStreamEpisode(context);
                return;
            }

            CastSession castSession = CastContext.getSharedInstance().getSessionManager()
                    .getCurrentCastSession();




            if (settingsManager.getSettings().getServerDialogSelection() == 1) {

                String[] charSequence = new String[episode.getVideos().size()];
                for (int i = 0; i<episode.getVideos().size(); i++) {

                    if (settingsManager.getSettings().getEnablelangsinservers() == 1){

                        charSequence[i] = episode.getVideos().get(i).getServer() + " - " + episode.getVideos().get(i).getLang();

                    }else {

                        charSequence[i] = episode.getVideos().get(i).getServer();
                    }

                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogTheme);
                builder.setTitle(R.string.source_quality);
                builder.setCancelable(true);
                builder.setItems(charSequence, (dialogInterface, wich) -> {

                    if (episode.getVideos().get(wich).getHeader() !=null && !episode.getVideos().get(wich).getHeader().isEmpty()) {

                        PLAYER_HEADER = episode.getVideos().get(wich).getHeader();
                    }


                    if (episode.getVideos().get(wich).getUseragent() !=null && !episode.getVideos().get(wich).getUseragent().isEmpty()) {

                        PLAYER_USER_AGENT = episode.getVideos().get(wich).getUseragent();
                    }


                    if (episode.getVideos().get(wich).getEmbed() == 1) {

                        Intent intent = new Intent(context, EmbedActivity.class);
                        intent.putExtra(Constants.MOVIE_LINK, episode.getVideos().get(wich).getLink());
                        context.startActivity(intent);


                    }else if (episode.getVideos().get(wich).getSupportedHosts() == 1) {

                        startSupportedHostsStream(episode,wich);

                    }else if (castSession != null && castSession.isConnected()) {

                        onLoadChromcast(episode, castSession, episode.getVideos().get(wich).getLink());

                    } else if (settingsManager.getSettings().getVlc() == 1) {

                        onLoadExternalPlayers(episode, position, wich);


                    } else {

                        onLoadMainPlayerStream(episode,position, episode.getVideos().get(wich).getLink(), episode.getVideos().get(wich));

                    }

                });

                builder.show();

            } else {


                if (episode.getVideos().get(0).getHeader() !=null && !episode.getVideos().get(0).getHeader().isEmpty()) {

                    PLAYER_HEADER = episode.getVideos().get(0).getHeader();
                }


                if (episode.getVideos().get(0).getUseragent() !=null && !episode.getVideos().get(0).getUseragent().isEmpty()) {

                    PLAYER_USER_AGENT = episode.getVideos().get(0).getUseragent();
                }


                if (episode.getVideos().get(0).getEmbed() == 1) {


                    Intent intent = new Intent(context, EmbedActivity.class);
                    intent.putExtra(Constants.MOVIE_LINK, episode.getVideos().get(0).getLink());
                    context.startActivity(intent);


                }else if (episode.getVideos().get(0).getSupportedHosts() == 1){


                    easyPlexSupportedHosts = new EasyPlexSupportedHosts(context);


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
                                    builder.setItems(name, (dialogInterface, i) -> {

                                        CastSession castSession = CastContext.getSharedInstance().getSessionManager()
                                                .getCurrentCastSession();

                                        if (castSession != null && castSession.isConnected()) {

                                            onLoadChromcast(episode, castSession, vidURL.get(i).getUrl());

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
                                                Tools.streamEpisodeFromVlc(context,vidURL.get(i).getUrl(),episode,settingsManager);
                                                dialog.hide();
                                            });

                                            mxPlayer.setOnClickListener(v12 -> {
                                                Tools.streamEpisodeFromMxPlayer(context,vidURL.get(i).getUrl(),episode,settingsManager);
                                                dialog.hide();

                                            });

                                            webcast.setOnClickListener(v12 -> {

                                                Tools.streamEpisodeFromMxWebcast(context,vidURL.get(i).getUrl(),episode,settingsManager);
                                                dialog.hide();

                                            });

                                            easyplexPlayer.setOnClickListener(v12 -> {

                                                onLoadMainPlayerStream(episode,position, vidURL.get(i).getUrl(), episode.getVideos().get(0));
                                                dialog.hide();


                                            });

                                            dialog.show();
                                            dialog.getWindow().setAttributes(lp);

                                            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                                    dialog.dismiss());


                                            dialog.show();
                                            dialog.getWindow().setAttributes(lp);


                                        } else {

                                            onLoadMainPlayerStream(episode,position, vidURL.get(i).getUrl(), episode.getVideos().get(0));


                                        }

                                    });

                                    builder.show();


                                } else Toast.makeText(context, "NULL", Toast.LENGTH_SHORT).show();

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
                                    Tools.streamEpisodeFromVlc(context,vidURL.get(0).getUrl(),episode,settingsManager);
                                    dialog.hide();
                                });

                                mxPlayer.setOnClickListener(v12 -> {
                                    Tools.streamEpisodeFromMxPlayer(context,vidURL.get(0).getUrl(),episode,settingsManager);
                                    dialog.hide();

                                });

                                webcast.setOnClickListener(v12 -> {

                                    Tools.streamEpisodeFromMxWebcast(context,vidURL.get(0).getUrl(),episode,settingsManager);
                                    dialog.hide();

                                });

                                easyplexPlayer.setOnClickListener(v12 -> {

                                    onLoadMainPlayerStream(episode,position, vidURL.get(0).getUrl(), episode.getVideos().get(0));
                                    dialog.hide();


                                });

                                dialog.show();
                                dialog.getWindow().setAttributes(lp);

                                dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                        dialog.dismiss());


                                dialog.show();
                                dialog.getWindow().setAttributes(lp);


                            }else {

                                onLoadMainPlayerStream(episode,position, vidURL.get(0).getUrl(), episode.getVideos().get(0));
                            }

                        }

                        @Override
                        public void onError() {

                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });

                    easyPlexSupportedHosts.find(episode.getVideos().get(0).getLink());


                } else {


                    if (castSession != null && castSession.isConnected()) {

                        onLoadChromcast(episode, castSession, episode.getVideos().get(0).getLink());

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
                            Tools.streamEpisodeFromVlc(context,episode.getVideos().get(0).getLink(),episode,settingsManager);
                            dialog.hide();
                        });

                        mxPlayer.setOnClickListener(v12 -> {
                            Tools.streamEpisodeFromMxPlayer(context,episode.getVideos().get(0).getLink(),episode,settingsManager);
                            dialog.hide();

                        });

                        webcast.setOnClickListener(v12 -> {

                            Tools.streamEpisodeFromMxWebcast(context,episode.getVideos().get(0).getLink(),episode,settingsManager);
                            dialog.hide();

                        });


                        easyplexPlayer.setOnClickListener(v12 -> {
                            onLoadMainPlayerStream(episode,position, episode.getVideos().get(0).getLink(),episode.getVideos().get(0));
                            dialog.hide();
                        });

                        dialog.show();
                        dialog.getWindow().setAttributes(lp);

                        dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                dialog.dismiss());


                        dialog.show();
                        dialog.getWindow().setAttributes(lp);


                    } else {

                        onLoadMainPlayerStream(episode,position, episode.getVideos().get(0).getLink(), episode.getVideos().get(0));

                    }

                }



            }

        }

        private void onLoadExternalPlayers(Episode episode, int position, int wich) {
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
                Tools.streamEpisodeFromVlc(context, episode.getVideos().get(wich).getLink(), episode,settingsManager);
                dialog.hide();
            });

            mxPlayer.setOnClickListener(v12 -> {
                Tools.streamEpisodeFromMxPlayer(context, episode.getVideos().get(wich).getLink(), episode,settingsManager);
                dialog.hide();

            });

            webcast.setOnClickListener(v12 -> {

                Tools.streamEpisodeFromMxWebcast(context, episode.getVideos().get(wich).getLink(), episode,settingsManager);
                dialog.hide();

            });


            easyplexPlayer.setOnClickListener(v12 -> {
                onLoadMainPlayerStream(episode, position, episode.getVideos().get(wich).getLink(), episode.getVideos().get(wich));
                dialog.hide();
            });

            dialog.show();
            dialog.getWindow().setAttributes(lp);

            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                    dialog.dismiss());


            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }

        private void onLoadChromcast(Episode episode, CastSession castSession, String link) {

            String currentepname = episode.getName();
            String artwork = episode.getStillPath();
            String name = currentTvShowName + " : " +"S0" + currentSeasons + "E" + episode.getEpisodeNumber() + " : " + episode.getName();

            MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
            movieMetadata.putString(MediaMetadata.KEY_TITLE, name);
            movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, currentepname);

            movieMetadata.addImage(new WebImage(Uri.parse(artwork)));
            List<MediaTrack> tracks = new ArrayList<>();


            MediaInfo mediaInfo = new MediaInfo.Builder(link)
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

        private void startSupportedHostsStream(Episode episode, int wich) {

            CastSession castSession = CastContext.getSharedInstance().getSessionManager()
                    .getCurrentCastSession();


            easyPlexSupportedHosts = new EasyPlexSupportedHosts(context);

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

                            final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogTheme);
                            builder.setTitle(context.getString(R.string.select_qualities));
                            builder.setCancelable(true);
                            builder.setItems(name, (dialogInterface, i) -> {


                                CastSession castSession = CastContext.getSharedInstance().getSessionManager()
                                        .getCurrentCastSession();

                                if (castSession != null && castSession.isConnected()) {

                                    onLoadChromcast(episode, castSession, vidURL.get(i).getUrl());


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
                                        Tools.streamEpisodeFromVlc(context,vidURL.get(i).getUrl(),episode,settingsManager);
                                        dialog.hide();
                                    });

                                    mxPlayer.setOnClickListener(v12 -> {
                                        Tools.streamEpisodeFromMxPlayer(context,vidURL.get(i).getUrl(),episode,settingsManager);
                                        dialog.hide();

                                    });

                                    webcast.setOnClickListener(v12 -> {

                                        Tools.streamEpisodeFromMxWebcast(context,vidURL.get(i).getUrl(),episode,settingsManager);
                                        dialog.hide();

                                    });

                                    easyplexPlayer.setOnClickListener(v12 -> {

                                        onLoadMainPlayerStream(episode,wich, vidURL.get(i).getUrl(), episode.getVideos().get(wich));
                                        dialog.hide();


                                    });

                                    dialog.show();
                                    dialog.getWindow().setAttributes(lp);

                                    dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                            dialog.dismiss());


                                    dialog.show();
                                    dialog.getWindow().setAttributes(lp);


                                } else {

                                    onLoadMainPlayerStream(episode,wich, vidURL.get(i).getUrl(), episode.getVideos().get(wich));

                                }


                            });

                            builder.show();



                        }else  Toast.makeText(context, "NULL", Toast.LENGTH_SHORT).show();

                    }else if (castSession != null && castSession.isConnected()) {

                        onLoadChromcast(episode, castSession, vidURL.get(0).getUrl());


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
                            Tools.streamEpisodeFromVlc(context,vidURL.get(0).getUrl(),episode,settingsManager);
                            dialog.hide();
                        });

                        mxPlayer.setOnClickListener(v12 -> {
                            Tools.streamEpisodeFromMxPlayer(context,vidURL.get(0).getUrl(),episode,settingsManager);
                            dialog.hide();

                        });

                        webcast.setOnClickListener(v12 -> {

                            Tools.streamEpisodeFromMxWebcast(context,vidURL.get(0).getUrl(),episode,settingsManager);
                            dialog.hide();

                        });

                        easyplexPlayer.setOnClickListener(v12 -> {

                            onLoadMainPlayerStream(episode,wich, vidURL.get(0).getUrl(), episode.getVideos().get(wich));
                            dialog.hide();


                        });

                        dialog.show();
                        dialog.getWindow().setAttributes(lp);

                        dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                                dialog.dismiss());


                        dialog.show();
                        dialog.getWindow().setAttributes(lp);


                    } else {

                        onLoadMainPlayerStream(episode,wich, vidURL.get(0).getUrl(), episode.getVideos().get(wich));

                    }
                }

                @Override
                public void onError() {

                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            });

            easyPlexSupportedHosts.find(episode.getVideos().get(wich).getLink());


        }


        private void onLoadSubscribeDialog(Episode media, int position, boolean stream) {

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

                String defaultRewardedNetworkAds = settingsManager.getSettings().getDefaultRewardedNetworkAds();



                if (context.getString(R.string.wortise).equals(defaultRewardedNetworkAds)) {

                    onLoadWortiseRewardAds(media,position,stream);


                } else if (context.getString(R.string.applovin).equals(defaultRewardedNetworkAds)) {

                    maxRewardedAd = MaxRewardedAd.getInstance( settingsManager.getSettings().getApplovinRewardUnitid(), (SerieDetailsActivity) context );
                    maxRewardedAd.loadAd();

                    onLoadApplovinAds(media,position,stream);

                }else if (context.getString(R.string.vungle).equals(defaultRewardedNetworkAds)) {

                    onLoadVungleAds(media,position,stream);

                }else if (context.getString(R.string.ironsource).equals(defaultRewardedNetworkAds)) {


                    onLoadIronSourceAds(media,position,stream);

                }else if (context.getString(R.string.unityads).equals(defaultRewardedNetworkAds)) {

                    onLoadUnityAds(media,position,stream);


                } else if (context.getString(R.string.admob).equals(defaultRewardedNetworkAds)) {

                    onLoadAdmobRewardAds(media,position,stream);


                }else if (context.getString(R.string.appodeal).equals(defaultRewardedNetworkAds)) {

                    onLoadAppOdealRewardAds(media,position,stream);

                } else if (context.getString(R.string.facebook).equals(defaultRewardedNetworkAds)) {

                    onLoadFaceBookRewardAds(media,position,stream);

                } else if (context.getString(R.string.wortise).equals(defaultRewardedNetworkAds)) {

                    onLoadWortiseRewardAds(media,position,stream);
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




        private void onLoadWortiseRewardAds(Episode episode, int position, boolean stream) {

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

                    if (stream) {

                        onStartEpisode(episode,position);

                    }else {

                        onLoadDownloadsList(episode);
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

        private void onLoadApplovinAds(Episode episode, int position, boolean stream) {


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

                    if (stream) {

                        onStartEpisode(episode,position);

                    }else {

                        onLoadDownloadsList(episode);
                    }

                }

                @Override
                public void onUserRewarded(MaxAd ad, MaxReward reward) {

                    //
                }
            });
        }


        private void onLoadVungleAds(Episode episode, int position, boolean stream) {

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

                    if (stream) {

                        onStartEpisode(episode,position);

                    }else {

                        onLoadDownloadsList(episode);
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

        private void onLoadIronSourceAds(Episode episode, int position, boolean stream) {

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

                    if (stream) {

                        onStartEpisode(episode,position);

                    }else {

                        onLoadDownloadsList(episode);
                    }


                    IronSource.loadRewardedVideo();
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

        private void onLoadAppOdealRewardAds(Episode episode, int position, boolean stream) {

            Appodeal.show((SerieDetailsActivity) context, Appodeal.REWARDED_VIDEO);

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

                    if (stream) {

                        onStartEpisode(episode,position);

                    }else {

                        onLoadDownloadsList(episode);
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


        private void onLoadFaceBookRewardAds(Episode episode, int position, boolean stream) {


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

                    if (stream) {

                        onStartEpisode(episode,position);

                    }else {

                        onLoadDownloadsList(episode);
                    }

                }


            };


            facebookInterstitialAd.loadAd(
                    facebookInterstitialAd.buildLoadAdConfig()
                            .withAdListener(interstitialAdListener)
                            .build());

        }

        private void onLoadAdmobRewardAds(Episode episode, int position, boolean stream) {

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
                        public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.@NotNull AdError adError) {
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
            mRewardedAd.show((SerieDetailsActivity) context, rewardItem -> {
                if (stream) {

                    onStartEpisode(episode,position);

                }else {

                    onLoadDownloadsList(episode);
                }
            });


        }

        private void onLoadUnityAds(Episode episode, int position, boolean stream) {


            UnityAds.load(settingsManager.getSettings().getUnityRewardPlacementId(), new IUnityAdsLoadListener() {
                @Override
                public void onUnityAdsAdLoaded(String placementId) {

                    UnityAds.show ((SerieDetailsActivity) context, settingsManager.getSettings().getUnityRewardPlacementId(), new IUnityAdsShowListener() {
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

                            if (stream) {

                                onStartEpisode(episode,position);

                            }else {

                                onLoadDownloadsList(episode);
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

        private void onLoadEpisodeOffline(Episode episode) {


            if (preferences.getBoolean(ISUSER_MAIN_ACCOUNT,false)){


                mediaRepository.hasResume(episode.getId()).observe((SerieDetailsActivity) context, resumeInfo -> {

                    if (resumeInfo != null) {

                        if (resumeInfo.getTmdb() != null && resumeInfo.getResumePosition() !=null

                                && resumeInfo.getTmdb().equals(String.valueOf(episode.getId())) && Tools.id(context).equals(resumeInfo.getDeviceId())) {


                            binding.episodewatched.setVisibility(View.VISIBLE);

                            double d = resumeInfo.getResumePosition();

                            double moveProgress = d * 100 / resumeInfo.getMovieDuration();


                            binding.resumeProgressBar.setVisibility(View.VISIBLE);
                            binding.resumeProgressBar.setProgress((int) moveProgress);

                            binding.timeRemaning.setText(Tools.getProgressTime((resumeInfo.getMovieDuration() - resumeInfo.getResumePosition()), true));



                        } else {

                            binding.episodewatched.setVisibility(GONE);
                            binding.resumeProgressBar.setProgress(0);
                            binding.resumeProgressBar.setVisibility(GONE);
                            binding.timeRemaning.setVisibility(GONE);

                        }

                    }else {


                        binding.episodewatched.setVisibility(GONE);
                        binding.resumeProgressBar.setProgress(0);
                        binding.resumeProgressBar.setVisibility(GONE);
                        binding.timeRemaning.setVisibility(GONE);

                    }

                });

            }else {


                Integer userId = settingsManager.getSettings().getProfileSelection() == 1  ? authManager.getSettingsProfile().getId() : authManager.getUserInfo().getId();


                mediaRepository.hasResumeProfile(episode.getId(),userId).observe((SerieDetailsActivity) context, resumeInfo -> {

                    if (resumeInfo != null) {

                        if (resumeInfo.getTmdb() != null && resumeInfo.getResumePosition() !=null

                                && resumeInfo.getTmdb().equals(String.valueOf(episode.getId())) && Tools.id(context).equals(resumeInfo.getDeviceId())) {


                            binding.episodewatched.setVisibility(View.VISIBLE);

                            double d = resumeInfo.getResumePosition();

                            double moveProgress = d * 100 / resumeInfo.getMovieDuration();


                            binding.resumeProgressBar.setVisibility(View.VISIBLE);
                            binding.resumeProgressBar.setProgress((int) moveProgress);

                            binding.timeRemaning.setText(Tools.getProgressTime((resumeInfo.getMovieDuration() - resumeInfo.getResumePosition()), true));



                        } else {

                            binding.episodewatched.setVisibility(GONE);
                            binding.resumeProgressBar.setProgress(0);
                            binding.resumeProgressBar.setVisibility(GONE);
                            binding.timeRemaning.setVisibility(GONE);

                        }

                    }else {


                        binding.episodewatched.setVisibility(GONE);
                        binding.resumeProgressBar.setProgress(0);
                        binding.resumeProgressBar.setVisibility(GONE);
                        binding.timeRemaning.setVisibility(GONE);

                    }

                });
            }


        }


        private void onLoadMainPlayerStream(Episode episode, int position, String url, EpisodeStream episodeStream) {



            if (settingsManager.getSettings().getVidsrc() !=1 && episodeStream !=null && episodeStream.getHeader() !=null && !episodeStream.getHeader().isEmpty()) {

                settingsManager.getSettings().setHeader(episodeStream.getHeader());
            }


            if (settingsManager.getSettings().getVidsrc() !=1 && episodeStream !=null && episodeStream.getUseragent() !=null && !episodeStream.getUseragent().isEmpty()) {

                settingsManager.getSettings().setUserAgent(episodeStream.getUseragent());
            }



            int hls = settingsManager.getSettings().getVidsrc() == 1 ? 1 : episodeStream != null ? episodeStream.getHls() : 0;

            String drmuuid = settingsManager.getSettings().getVidsrc() == 1 ? null : episodeStream.getDrmuuid();

            String getDrmlicenceuri = settingsManager.getSettings().getVidsrc() == 1 ? null : episodeStream.getDrmlicenceuri();

            int getDrm = settingsManager.getSettings().getVidsrc() == 1 ? 0 : episodeStream.getDrm();

            String tvseasonid = seasonId;
            Integer currentep = Integer.parseInt(episode.getEpisodeNumber());
            String currentepname = episode.getName();
            String currenteptmdbnumber = String.valueOf(episode.getId());
            String currentepimdb = String.valueOf(episode.getId());
            String artwork = episode.getStillPath();
            String type = "1";
            String name = "S0" + currentSeasons + "E" + episode.getEpisodeNumber() + " : " + episode.getName();

            Intent intent = new Intent(context, EasyPlexMainPlayer.class);
            intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY,
                    MediaModel.media(currentSerieId,
                            null,
                            null, type, name, url, artwork,
                            null, currentep
                            , currentSeasons, currentepimdb, tvseasonid,
                            currentepname,
                            currentSeasonsNumber, position,
                            currenteptmdbnumber, premuim,hls,
                            null,externalId,serieCover
                            ,episode.getHasrecap(),
                            episode.getSkiprecapStartIn(),
                            mediaGenre,currentTvShowName,Float.parseFloat(episode.getVoteAverage()),
                            drmuuid,getDrmlicenceuri,getDrm));
            intent.putExtra(ARG_MOVIE, media);
            context.startActivity(intent);

            HistorySaver.onMEpisodeSave(episode,media,authManager,mediaRepository,mediaGenre,currentSeasons,tvseasonid,currentSeasonsNumber,"1",deviceManager,settingsManager);


        }





        private void createAndLoadRewardedAd() {

            String defaultRewardedNetworkAds = settingsManager.getSettings().getDefaultRewardedNetworkAds();



            if (context.getString(R.string.wortise).equals(defaultRewardedNetworkAds)) {

                mRewardedWortise = new com.wortise.ads.rewarded.RewardedAd(context, settingsManager.getSettings().getWortiseRewardUnitid());
                mRewardedWortise.loadAd();

            } else  if (context.getString(R.string.vungle).equals(defaultRewardedNetworkAds)) {

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

                maxRewardedAd = MaxRewardedAd.getInstance(settingsManager.getSettings().getApplovinRewardUnitid(), (SerieDetailsActivity) context);
                maxRewardedAd.loadAd();

            }else if (context.getString(R.string.appodeal).equals(settingsManager.getSettings().getDefaultRewardedNetworkAds())) {

                if (settingsManager.getSettings().getAdUnitIdAppodealRewarded() !=null) {

                    Appodeal.initialize((SerieDetailsActivity) context, settingsManager.getSettings().getAdUnitIdAppodealRewarded(), Appodeal.REWARDED_VIDEO, list -> {
                        //
                    });

                }

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
            if (preferences.getString(
                    FsmPlayerApi.decodeServerMainApi2(), FsmPlayerApi.decodeServerMainApi4()).equals(FsmPlayerApi.decodeServerMainApi4())) { ((SerieDetailsActivity)context).finish(); }
        }

    }



    private void onLoadSerieComments(Integer id) {

        commentsAdapter = new CommentsAdapter();

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_comments);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.width = MATCH_PARENT;
        lp.height = MATCH_PARENT;

        lp.gravity = Gravity.BOTTOM;

        TextView commentTotal = dialog.findViewById(R.id.comment_total);

        FloatingActionButton addCommentBtn = dialog.findViewById(R.id.add_comment_btn);

        EditText editTextComment = dialog.findViewById(R.id.comment_message);

        LinearLayout noCommentFound = dialog.findViewById(R.id.no_comment_found);

        RecyclerView rvComments = dialog.findViewById(R.id.rv_comments);

        rvComments.setHasFixedSize(true);
        rvComments.setNestedScrollingEnabled(false);
        rvComments.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvComments.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(context, 0), true));


        commentsAdapter.setOnItemClickListener(clicked -> {
            if (clicked){
                mediaRepository.getEpisodesComments(id,settingsManager.getSettings().getApiKey())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<>() {
                            @Override
                            public void onSubscribe(@NotNull Disposable d) {

                                //

                            }

                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull MovieResponse movieResponse) {

                                commentsAdapter.addToContent(movieResponse.getComments(),context,authManager,mediaRepository);
                                commentsAdapter.notifyDataSetChanged();

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
        });


        mediaRepository.getEpisodesComments(id,settingsManager.getSettings().getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                        //

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull MovieResponse movieResponse) {

                        commentsAdapter.addToContent(movieResponse.getComments(),context,authManager,mediaRepository);
                        rvComments.setAdapter(commentsAdapter);

                        if (commentsAdapter.getItemCount() == 0) {
                            noCommentFound.setVisibility(View.VISIBLE);

                        }else {
                            noCommentFound.setVisibility(GONE);
                        }

                        commentTotal.setText(movieResponse.getComments().size()+" Comments");


                        addCommentBtn.setOnClickListener(v -> {

                            if (editTextComment.getText()!=null){

                                mediaRepository.addCommentEpisode(editTextComment.getText().toString(),String.valueOf(id))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Observer<>() {
                                            @Override
                                            public void onSubscribe(@NotNull Disposable d) {

                                                //

                                            }

                                            @Override
                                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Comment comment) {

                                                Toast.makeText(context, "Comment added successfully", Toast.LENGTH_SHORT).show();
                                                editTextComment.setText("");


                                                mediaRepository.getEpisodesComments(id,settingsManager.getSettings().getApiKey())
                                                        .subscribeOn(Schedulers.io())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe(new Observer<>() {
                                                            @Override
                                                            public void onSubscribe(@NotNull Disposable d) {

                                                                //

                                                            }

                                                            @Override
                                                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull MovieResponse movieResponse) {

                                                                commentsAdapter.addToContent(movieResponse.getComments(),context,authManager,mediaRepository);
                                                                rvComments.scrollToPosition(Objects.requireNonNull(rvComments.getAdapter()).getItemCount()-1);
                                                                commentsAdapter.notifyDataSetChanged();
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

                                            @Override
                                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                                //
                                            }

                                            @Override
                                            public void onComplete() {
                                                //
                                            }

                                        });


                            }else {

                                Toast.makeText(context, R.string.type_to_comment, Toast.LENGTH_SHORT).show();
                            }

                        });
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



        dialog.show();
        dialog.getWindow().setAttributes(lp);

        dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                dialog.dismiss());

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    @SuppressLint("StaticFieldLeak")
    private void onLoadDownloadsList(Episode episode) {

        if (settingsManager.getSettings().getSeparateDownload() == 1) {

            if (episode.getDownloads() !=null && !episode.getDownloads().isEmpty()) {

                onLoadEpisodeDownloadInfo(episode, episode.getDownloads());
            }else {

                DialogHelper.showNoDownloadAvailable(context,context.getString(R.string.about_no_stream_download));
            }

        }else if (episode.getVideos() !=null && !episode.getVideos().isEmpty()) {

            onLoadEpisodeDownloadInfo(episode, episode.getVideos());

        }else {

            DialogHelper.showNoDownloadAvailableEpisode(context,context.getString(R.string.about_no_stream_download));
        }

    }

    @SuppressLint("StaticFieldLeak")
    private void onLoadEpisodeDownloadInfo(Episode episode, List<EpisodeStream> downloads) {

        String[] charSequence = new String[downloads.size()];
        for (int i = 0; i<downloads.size(); i++) {

            charSequence[i] = downloads.get(i).getServer() + " - " + downloads.get(i).getLang();

        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogTheme);
        builder.setTitle(R.string.select_quality);
        builder.setCancelable(true);
        builder.setItems(charSequence, (dialogInterface, wich) -> {

            if (downloads.get(wich).getEmbed() !=1) {

                if (settingsManager.getSettings().getAllowAdm() == 1) {


                    if (downloads.get(wich).getExternal()  == 1) {

                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(downloads.get(wich).getLink())));

                    } else   if (downloads.get(wich).getSupportedHosts() == 1){


                        easyPlexSupportedHosts = new EasyPlexSupportedHosts(context);

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
                                        builder.setItems(name, (dialogInterface, i) ->
                                                onLoadDonwloadFromDialogs(episode,vidURL.get(i).getUrl(),downloads, downloads.get(wich)));

                                        builder.show();


                                    } else
                                        Toast.makeText(context, "NULL", Toast.LENGTH_SHORT).show();

                                } else {

                                    onLoadDonwloadFromDialogs(episode,vidURL.get(0).getUrl(), downloads, downloads.get(wich));
                                }

                            }

                            @Override
                            public void onError() {

                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });

                        easyPlexSupportedHosts.find(downloads.get(wich).getLink());


                    }else {

                        onLoadDonwloadFromDialogs(episode,downloads.get(wich).getLink(), downloads, downloads.get(wich));

                    }



                } else {

                    if (downloads.get(wich).getExternal()  == 1) {

                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(downloads.get(wich).getLink())));

                    } else   if (downloads.get(wich).getSupportedHosts() == 1){


                        easyPlexSupportedHosts = new EasyPlexSupportedHosts(context);


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
                                        builder.setItems(name, (dialogInterface, i) -> onLoadDownloadLink(episode, vidURL.get(i).getUrl(),
                                                downloads.get(wich)));

                                        builder.show();


                                    } else
                                        Toast.makeText(context, "NULL", Toast.LENGTH_SHORT).show();

                                } else {


                                    onLoadDownloadLink(episode, vidURL.get(0).getUrl(), downloads.get(wich));
                                }

                            }

                            @Override
                            public void onError() {

                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });

                        easyPlexSupportedHosts.find(downloads.get(wich).getLink());


                    }else {

                        onLoadDownloadLink(episode, downloads.get(wich).getLink(), downloads.get(wich));

                    }

                }


            }else {

                DialogHelper.showNoDownloadAvailable(context,context.getString(R.string.about_no_stream_download));
            }



        });

        builder.show();
    }

    private void onLoadDownloadLink(Episode episode, String url, EpisodeStream episodeStream) {


        String name = "S0" + currentSeasons + "E" + episode.getEpisodeNumber() + " : " + episode.getName();

        FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
        addDownloadDialog = (AddDownloadDialog)fm.findFragmentByTag(TAG_DOWNLOAD_DIALOG);
        if (addDownloadDialog == null) {
            AddInitParams initParams = null;
            Intent i = ((FragmentActivity)context).getIntent();
            if (i != null)
                initParams = i.getParcelableExtra(AddDownloadActivity.TAG_INIT_PARAMS);
            if (initParams == null) {
                initParams = new AddInitParams();
            }
            fillInitParams(initParams, episode, url, episodeStream);
            addDownloadDialog = AddDownloadDialog.newInstance(initParams);
            addDownloadDialog.show(fm, TAG_DOWNLOAD_DIALOG);
        }



        download = new Download(String.valueOf(episode.getId()),String.valueOf(episode.getId()),episode.getStillPath(),name,"");

        download.setId(String.valueOf(episode.getId()));
        download.setPosterPath(serieCover);
        download.setTitle(name);
        download.setName(name);
        download.setBackdropPath(episode.getStillPath());
        download.setEpisodeNmber(episode.getEpisodeNumber());
        download.setSeasonsId(seasonId);
        download.setPosition(0);
        download.setType("1");
        download.setTmdbId(currentSerieId);
        download.setEpisodeId(String.valueOf(episode.getId()));
        download.setEpisodeName(episode.getName());
        download.setEpisodeTmdb(String.valueOf(episode.getId()));
        download.setSerieId(currentSerieId);
        download.setSerieName(currentTvShowName);
        download.setOverview(episode.getOverview());
        download.setCurrentSeasons(currentSeasons);
        download.setSeasonsId(seasonId);
        download.setSeasonsNumber(currentSeasonsNumber);
        download.setImdbExternalId(externalId);
        download.setPremuim(premuim);
        download.setHls(episode.getHls());
        download.setHasrecap(episode.getHasrecap());
        download.setSkiprecapStartIn(episode.getSkiprecapStartIn());
        download.setMediaGenre(mediaGenre);
        download.setOverview(media.getOverview());

        compositeDisposable.add(Completable.fromAction(() -> mediaRepository.addMovie(download))
                .subscribeOn(Schedulers.io())
                .subscribe());
    }



    private void fillInitParams(AddInitParams params, Episode episode, String downloadUrl, EpisodeStream episodeStream)
    {


        String ePname = "S0" + currentSeasons + "E" + episode.getEpisodeNumber() + " : " + episode.getName();

        String name = "S0" + currentSeasons + "E" + episode.getEpisodeNumber() + "_" + episode.getName();



        SettingsRepository pref = RepositoryHelper.getSettingsRepository(context);
        SharedPreferences localPref = PreferenceManager.getDefaultSharedPreferences(context);

        if (params.url == null) {
            params.url = downloadUrl;
        }


        if (params.fileName == null) {
            params.fileName = name.replaceAll("[^a-zA-Z0-9_-]", "");

        }

        if (params.type == null) {
            params.type = "1";
        }


        if (episodeStream.getUseragent() !=null && !episodeStream.getUseragent().isEmpty()){


            if (params.userAgent == null) {
                params.userAgent = episodeStream.getUseragent();
            }
        }


        if (episodeStream.getHeader() !=null && !episodeStream.getHeader().isEmpty()){

            if (params.refer == null) {
                params.refer = episodeStream.getHeader();
            }
        }


        if (params.mediaId == null) {
            params.mediaId = String.valueOf(episode.getId());
        }


        if (params.mediaName == null) {
            params.mediaName = media.getName() + " : " + ePname;
        }


        if (params.mediabackdrop == null) {
            params.mediabackdrop = episode.getStillPath();
        }



        if (params.dirPath == null) {
            params.dirPath = Uri.parse(pref.saveDownloadsIn());
        }
        if (params.retry == null) {
            params.retry = localPref.getBoolean(
                    context.getString(R.string.add_download_retry_flag),
                    true
            );
        }
        if (params.replaceFile == null) {
            params.replaceFile = localPref.getBoolean(
                    context.getString(R.string.add_download_replace_file_flag),
                    false
            );
        }
        if (params.unmeteredConnectionsOnly == null) {
            params.unmeteredConnectionsOnly = localPref.getBoolean(
                    context.getString(R.string.add_download_unmetered_only_flag),
                    false
            );
        }
        if (params.numPieces == null) {
            params.numPieces = localPref.getInt(
                    context.getString(R.string.add_download_num_pieces),
                    DownloadInfo.MIN_PIECES
            );
        }
    }



    private void onLoadDonwloadFromDialogs(Episode episode, String url, List<EpisodeStream> downloads, EpisodeStream episodeStream) {


        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_download_options);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.gravity = Gravity.BOTTOM;
        lp.width = MATCH_PARENT;
        lp.height = MATCH_PARENT;


        LinearLayout withAdm = dialog.findViewById(R.id.withAdm);
        LinearLayout withApp = dialog.findViewById(R.id.withApp);
        LinearLayout with1App = dialog.findViewById(R.id.with1DM);

        withAdm.setOnClickListener(v12 -> {
            Tools.downloadFromAdm(context,url,true,media,settingsManager,episode,true);
            dialog.dismiss();

        });

        with1App.setOnClickListener(v12 -> {
            Tools.downloadFrom1dm(context, url, true, media, settingsManager,episode,true);
            dialog.dismiss();
        });

        withApp.setOnClickListener(v12 -> {
            onLoadDownloadLink(episode,url, episodeStream);
            dialog.dismiss();
        });



        dialog.show();
        dialog.getWindow().setAttributes(lp);

        dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                dialog.dismiss());


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    public void initLoadRewardedAd() {

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



    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        compositeDisposable.clear();
        adsLaunched = false;
    }


    @Override
    public void onViewDetachedFromWindow(@NonNull EpisodeViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        adsLaunched = false;
    }
}

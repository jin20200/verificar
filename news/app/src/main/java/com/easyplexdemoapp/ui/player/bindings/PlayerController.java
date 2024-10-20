package com.easyplexdemoapp.ui.player.bindings;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.VISIBLE;
import static com.easyplexdemoapp.EasyPlexApp.getContext;
import static com.easyplexdemoapp.util.Constants.AUTO_PLAY;
import static com.easyplexdemoapp.util.Constants.CUSTOM_SEEK_CONTROL_STATE;
import static com.easyplexdemoapp.util.Constants.DEFAULT_FREQUENCY;
import static com.easyplexdemoapp.util.Constants.DEFAULT_MEDIA_COVER;
import static com.easyplexdemoapp.util.Constants.EDIT_CUSTOM_SEEK_CONTROL_STATE;
import static com.easyplexdemoapp.util.Constants.PREF_FILE;
import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FIT;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableFloat;
import androidx.databinding.ObservableInt;
import androidx.media3.exoplayer.source.BehindLiveWindowException;

import com.easyplexdemoapp.EasyPlexApp;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.ui.player.activities.EasyPlexMainPlayer;
import com.easyplexdemoapp.ui.player.controller.PlayerUIController;
import com.easyplexdemoapp.ui.player.enums.ScaleMode;
import com.easyplexdemoapp.ui.player.fsm.state_machine.FsmPlayerApi;
import com.easyplexdemoapp.ui.player.interfaces.PlaybackActionCallback;
import com.easyplexdemoapp.ui.player.interfaces.TubiPlaybackControlInterface;
import com.easyplexdemoapp.ui.player.presenters.ScalePresenter;
import com.easyplexdemoapp.ui.player.utilities.ExoPlayerLogger;
import com.easyplexdemoapp.ui.player.utilities.PlayerDeviceUtils;
import com.easyplexdemoapp.ui.player.views.EasyPlexPlayerView;
import com.easyplexdemoapp.util.Tools;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.Tracks;
import com.google.android.exoplayer2.video.VideoSize;

import org.jetbrains.annotations.NotNull;

import java.util.Observable;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * This class contains business logic of user interaction between user and player action. This class will be serving
 * as interface between Player UI and Business logic, such as seek, pause, UI logic for displaying ads vs movie.
 */
public class PlayerController extends Observable implements TubiPlaybackControlInterface, Player.Listener, SeekBar.OnSeekBarChangeListener{

    private static final String TAG = PlayerController.class.getSimpleName();

    SharedPreferences preferences;
    private boolean isDraggingSeekBar;

    private static int controlstate = 1;


    /**
     * Media action states
     */



    public final ObservableField<Boolean> isPlayerLocked = new ObservableField<>(false);

    public final ObservableField<Boolean> isPlayerLocked2 = new ObservableField<>(false);


    public final ObservableField<Boolean> showMediaBackgroundImage = new ObservableField<>(false);


    public final ObservableInt drm = new ObservableInt();

    public final ObservableField<String> Drmuuid = new ObservableField<>();


    public final ObservableField<String> Drmlicenceuri = new ObservableField<>();



    public final ObservableInt hlsformat = new ObservableInt();

    public final ObservableInt playerPlaybackState = new ObservableInt(Player.STATE_IDLE);

    public final ObservableBoolean isVideoPlayWhenReady = new ObservableBoolean(false);


    public final ObservableBoolean isUserDraggingSeekBar = new ObservableBoolean(false);


    public final ObservableField<String> currentSpeed = new ObservableField<>(getContext().getString(R.string.speed_normal));


    public final ObservableBoolean isPlayerError = new ObservableBoolean(false);


    public final ObservableFloat voteAverage = new ObservableFloat(0);


    public final ObservableField<String> nextSeasonsID = new ObservableField<>("");


    // Return Media Name
    public final ObservableField<String> videoName = new ObservableField<>("");

    public final ObservableField<String> mediaNameSecondary = new ObservableField<>("");

    // Return Media Genre
    public final ObservableField<String> mediaGenreString = new ObservableField<>("");

    // Return getSerieTvShowName Genre
    public final ObservableField<String> getSerieTvShowName = new ObservableField<>("");


    // Return Media Name
    public final ObservableField<String> mediaTypeName = new ObservableField<>("");

    // Return Current Media TMDB Number (EX : 168222)
    public final ObservableField<String> getCurrentMediaTmdbNumber = new ObservableField<>("");


    public final ObservableField<String> getExternalId = new ObservableField<>("");



    // Return Episode Position ( Json )
    public final ObservableInt episodePosition = new ObservableInt();


    // Return Media Current Stream Link
    public final ObservableField<Uri> videoCurrentLink = new ObservableField<>();


    // Return Media Current Substitle Link
    public final ObservableField<String> videoCurrentSubs = new ObservableField<>(getContext().getString(R.string.player_substitles));



    // Return Media Current Substitle Link
    public final ObservableField<String> mediaToMyList = new ObservableField<>(getContext().getString(R.string.add_to_my_list_player));


    // Return Media Current Quality Link
    public final ObservableField<String> videoCurrentQuality = new ObservableField<>(getContext().getString(R.string.select_subs_player));


    // Return Media ID
    public final ObservableField<String> videoID = new ObservableField<>("");


    public final ObservableField<String> currentSeasonId = new ObservableField<>("");


    // Return Media ID
    public final ObservableField<String> currentEpisodeName = new ObservableField<>("");


    // Return Current Episode Season Number for a Serie or Anime
    public final ObservableField<String> currentSeasonsNumber = new ObservableField<>("");


    // Return Current Episode IMDB Number for a Serie or Anime
    public final ObservableField<String> currentEpisodeImdbNumber = new ObservableField<>("");


    // Return if media Has An ID
    public final ObservableField<Boolean> videoHasID = new ObservableField<>(false);


    public final ObservableField<Boolean> youCanHide = new ObservableField<>(true);


    // Return if media External Id (TMDB)
    public final ObservableField<String> videoExternalID = new ObservableField<>("");



    // Return Remaining Time for the current Media
    public final ObservableField<String> timeRemaining = new ObservableField<>();


    // Return Media Type
    public final ObservableField<String> mediaType = new ObservableField<>("");


    // Return Media Substile in Uri Format
    public final ObservableField<Uri> mediaSubstitleUri = new ObservableField<>();


    // Return Media Duration
    public final ObservableField<Long> mediaDuration = new ObservableField<>(0L);


    // Return Media Current Time ( For SeekBar )
    public final ObservableField<Long> mediaCurrentTime = new ObservableField<>(0L);


    public final ObservableField<Long> mediaVolume = new ObservableField<>(0L);


    // Return Media Current Buffred Position ( For SeekBar )
    public final ObservableField<Long> mediaBufferedPosition = new ObservableField<>(0L);

    // Return Media Current Remaining Time in String Format
    public final ObservableField<String> mediaRemainInString = new ObservableField<>("");


    // Return Media Media Position
    public final ObservableField<String> mediaPositionInString = new ObservableField<>("");


    // Return True if the media Has an Active Substitle
    public final ObservableField<Boolean> mediaHasSubstitle = new ObservableField<>(false);


    public final ObservableField<Boolean> lg = new ObservableField<>(false);


    // Return Current Episode Cover
    public final ObservableField<String> currentMediaCover = new ObservableField<>("");


    // Return True if the media is Ended
    public final ObservableField<Boolean> mediaEnded = new ObservableField<>(false);


    public final ObservableField<Boolean> isPlayerReady = new ObservableField<>(false);


    public final ObservableField<Boolean> autoSubstitleActivated = new ObservableField<>(false);


    // Return True if Current Media is a Live Streaming
    public final ObservableField<Boolean> isLive = new ObservableField<>(false);


    // Return True if Current User Has a Premuim Membership
    public final ObservableField<Boolean> isUserPremuim = new ObservableField<>(false);


    // Return Episode Id for a Serie
    public final ObservableField<String> episodeId = new ObservableField<>("4:3");


    // Return Seasons Id for a Serie
    public final ObservableField<String> episodeSeasonsId = new ObservableField<>("");


    // Return Seasons Id for a Serie
    public final ObservableField<String> episodeSeasonsNumber = new ObservableField<>("");


    // Return if Current Media is Premuim
    public final ObservableInt mediaPremuim = new ObservableInt();


    // Return True if the User has enabled the Substitle
    public final ObservableField<Boolean> mediaSubstitleGet = new ObservableField<>(false);


    public final ObservableField<Boolean> isAutoPlayEnabled = new ObservableField<>(false);


    public final ObservableField<Boolean> isStreamOnFavorite = new ObservableField<>(false);


    /**
     * Ad information
     */

    // Return ads Click Url
    public final ObservableField<String> adClickUrl = new ObservableField<>("");


    // Return Number of Ads Left
    public final ObservableInt numberOfAdsLeft = new ObservableInt(0);


    // Return True if Current Media is playing an ADS
    public final ObservableField<Boolean> isCurrentAd = new ObservableField<>(false);

    public final ObservableField<Boolean> hideGenre = new ObservableField<>(false);


    public final ObservableField<Boolean> isMediaHasSkipRecap = new ObservableField<>(false);

    public final ObservableField<Boolean> isCue = new ObservableField<>(false);

    public final ObservableField<Boolean> isSkippable = new ObservableField<>(false);


    // Return True if Current Media has reached a CuePoint
    public final ObservableField<Boolean> isCuePointReached = new ObservableField<>(false);


    // Return Ads Remaining Time in String Format
    public final ObservableField<String> adsRemainInString = new ObservableField<>("");


    public final ObservableField<String> mediaCoverHistory = new ObservableField<>("");
    public final ObservableInt hasRecap = new ObservableInt();
    public final ObservableInt recapStartIn = new ObservableInt(0);

    public final ObservableField<Boolean> mediaRestart = new ObservableField<>(false);

    public final ObservableField<Boolean> playerReady = new ObservableField<>(false);

    public final ObservableField<Boolean> settingReady = new ObservableField<>(false);

    private PlayerUIController controller;
    private float mInitVideoAspectRatio;
    private ScalePresenter mScalePresenter;
    private final Handler mProgressUpdateHandler = new Handler(Looper.getMainLooper());
    private static  Runnable mOnControlStateChange;


    /**
     * the Exoplayer instance which this {@link PlayerController} is controlling.
     */
    private ExoPlayer mPlayer;
    /**
     * this is the current mediaModel being played, it could be a ad or actually video
     */
    private MediaModel mMediaModel;
    private PlaybackActionCallback mPlaybackActionCallback;
    private final Runnable updateProgressAction = this::updateProgress;
    private EasyPlexPlayerView mEasyPlexPlayerView;





    public void onCleanUp(){

        mEasyPlexPlayerView = null;
        mMediaModel = null;
    }


    @Inject
    SharedPreferences.Editor sharedPreferencesEditor;


    /**
     * Every time the FsmPlayer change states between
     * AdPlayingState and MoviePlayingState,
     * current controller instance need to update the video instance.
     *
     * @param mediaModel the current video that will be played by the {@link PlayerController#mPlayer} instance.
     */
    public void setMediaModel(MediaModel mediaModel, Context context) {


        preferences = getContext().getSharedPreferences(PREF_FILE, MODE_PRIVATE);


        if (mediaModel == null) {
            ExoPlayerLogger.e(TAG, "setMediaModel is null");
        } else {

            this.mMediaModel = mediaModel;

            //mark flag for ads to movie
            isCurrentAd.set(mediaModel.isAd());
            mPlaybackActionCallback.isCurrentAd(mediaModel.isAd());

            mScalePresenter = new ScalePresenter(mEasyPlexPlayerView.getContext(), this);


            if (mediaModel.isAd()) {

                if (!PlayerDeviceUtils.isTVDevice(context)
                        && !TextUtils.isEmpty(mediaModel.getClickThroughUrl())) {
                    adClickUrl.set(mediaModel.getClickThroughUrl());
                }

                videoName.set(context.getString(R.string.commercial));

                mediaHasSubstitle.set(false);

            } else {

                if (mediaType.get().equals("streaming")) {

                    isLive.set(true);

                }

                setModelMediaInfo(mediaModel);

            }
        }


        isAutoPlayEnabled.set(preferences.getBoolean(AUTO_PLAY, true));


        autoSubstitleActivated.set(preferences.getBoolean(AUTO_PLAY, true));


        lg.set(preferences.getString(FsmPlayerApi.decodeServerMainApi2(), FsmPlayerApi.decodeServerMainApi4()).equals(FsmPlayerApi.decodeServerMainApi4()));

    }






    public void setModelMediaInfo(@NonNull MediaModel mediaModel) {

        if (mediaModel.getMediaCover() !=null) {

            currentMediaCover.set(String.valueOf(mediaModel.getMediaCover()));

        }else {

            currentMediaCover.set(preferences.getString(DEFAULT_MEDIA_COVER,""));
        }


        if (mediaModel.getMediaSubstitleUrl() != null) {
            mediaHasSubstitle.set(true);
            mediaSubstitleUri.set(mediaModel.getMediaSubstitleUrl());
            triggerSubtitlesToggle(true);
        }




        if (!TextUtils.isEmpty(mediaModel.getSeasonId())) {
            currentSeasonsNumber.set(mMediaModel.getSeasonId());
        }


        if (!TextUtils.isEmpty(mediaModel.getEpImdb())) {
            currentEpisodeImdbNumber.set(mediaModel.getEpImdb());
        }



        if (!TextUtils.isEmpty(mediaModel.getTvSeasonId())) {
            nextSeasonsID.set(mediaModel.getTvSeasonId());
        }


        hlsformat.set(mediaModel.getHlscustomformat());

        drm.set(mediaModel.getDrm());



        if (!TextUtils.isEmpty(mediaModel.getDrmUUID())) {
            Drmuuid.set(mediaModel.getDrmUUID());
        }


        if (!TextUtils.isEmpty(mediaModel.getDrmLicenseUri())) {
            Drmlicenceuri.set(mediaModel.getDrmLicenseUri());
        }


        if (!TextUtils.isEmpty(mediaModel.getCurrentEpName())) {
            currentEpisodeName.set(mediaModel.getCurrentEpName());
        }

        if (mediaModel.getEpId() != null) {

            episodeId.set(String.valueOf(mediaModel.getEpId()));
        }


        if (!TextUtils.isEmpty(mediaModel.getCurrentSeasonsNumber())) {
            episodeSeasonsId.set(mediaModel.getCurrentSeasonsNumber());
        }


        if (mediaModel.getEpisodePostionNumber() != null) {

            episodePosition.set(mediaModel.getEpisodePostionNumber());

        }


        if (mediaModel.getCurrentEpTmdbNumber() != null) {

            getCurrentMediaTmdbNumber.set(mediaModel.getCurrentEpTmdbNumber());

        }


        switch (getMediaType()) {
            case "0":
                mediaTypeName.set(mEasyPlexPlayerView.getContext().getString(R.string.lists_movies));

                break;
            case "1":
                mediaTypeName.set(mEasyPlexPlayerView.getContext().getString(R.string.lists_series));
                break;
            case "anime":
                mediaTypeName.set(mEasyPlexPlayerView.getContext().getString(R.string.lists_animes));
                break;
            default:
                mediaTypeName.set(mEasyPlexPlayerView.getContext().getString(R.string.lists_streaming));
                break;
        }



        if (Boolean.TRUE.equals(isStreamOnFavorite.get())) {

            mediaToMyList.set("Added");

        }else {

         mediaToMyList.set("Add To MyList");

        }


        if (!TextUtils.isEmpty(mediaModel.getMediaName())) {
            videoName.set(mediaModel.getMediaName());
        }



        if (!TextUtils.isEmpty(mediaModel.getMediaGenres())) {
            mediaGenreString.set(mediaModel.getMediaGenres());
            mPlaybackActionCallback.StartGenre(mediaModel.getMediaGenres());
        }

        if (!TextUtils.isEmpty(mediaModel.getSerieName())) {
            getSerieTvShowName.set(mediaModel.getSerieName());
            mediaNameSecondary.set(mediaModel.getSerieName());

        }else {
            if (!TextUtils.isEmpty(mediaModel.getMediaName())) {
                mediaNameSecondary.set(mediaModel.getMediaName());

                if ((mediaModel.getMediaName() != null ? mediaModel.getMediaName().length() : 0) < 25) {

                    mEasyPlexPlayerView.getControlView().findViewById(R.id.view_auto_play).setVisibility(View.GONE);
                }
            }
        }


        voteAverage.set(mediaModel.getVoteAverage());


        if (!TextUtils.isEmpty(mediaModel.getVideoid())) {
            videoID.set(mediaModel.getVideoid());
            videoHasID.set(true);

        }


        if (!TextUtils.isEmpty(mediaModel.getTvSeasonId())) {
            currentSeasonId.set(mediaModel.getTvSeasonId());
        }

        if (mediaModel.getIsPremuim() != null) {

            mediaPremuim.set(mediaModel.getIsPremuim());

        }


        if (mediaModel.getIsPremuim() != null) {

            mediaPremuim.set(mediaModel.getIsPremuim());

        }


        if (!TextUtils.isEmpty(mediaModel.getCurrentExternalId())) {
            getExternalId.set(mediaModel.getCurrentExternalId());
        }



        if (!TextUtils.isEmpty(mediaModel.getMediaGenre())) {
            videoExternalID.set(mediaModel.getMediaGenre());
        }

        if (!TextUtils.isEmpty(mediaModel.getType())) {
            mediaType.set(mediaModel.getType());
            mPlaybackActionCallback.getType(mediaModel.getType());
        }



        if (!TextUtils.isEmpty(mediaModel.getCurrentQuality())) {
            videoCurrentQuality.set(mediaModel.getCurrentQuality());
        }



        videoCurrentLink.set(mediaModel.getMediaUrl());




        if (!TextUtils.isEmpty(mediaModel.getMediaCoverHistory())) {
            mediaCoverHistory.set(mediaModel.getMediaCoverHistory());
        }



        hasRecap.set(mediaModel.getHasRecap());

        recapStartIn.set(mediaModel.getGetSkiprecapStartIn());



    }




    /**
     * Every time the FsmPlayer change states between
     * AdPlayingState and MoviePlayingState,
     * {@link PlayerController#mPlayer} instance need to update .
     *
     * @param player the current player that is playing the video
     */
    public void setPlayer(@NonNull ExoPlayer player, @NonNull PlaybackActionCallback playbackActionCallback,
                          @NonNull EasyPlexPlayerView easyPlexPlayerView) {


        if (this.mPlayer == player) {
            return;
        }


        mEasyPlexPlayerView = easyPlexPlayerView;

        //remove the old listener
        if (mPlayer != null) {
            this.mPlayer.removeListener(this);
        }


        this.mPlayer = player;
        mPlayer.addListener(this);
        playerPlaybackState.set(mPlayer.getPlaybackState());
        mPlaybackActionCallback = playbackActionCallback;
        updateProgress();


    }


    public void setAvailableAdLeft(int count) {
        numberOfAdsLeft.set(count);
    }

    public void updateTimeTextViews(long position, long duration) {
        //translate the movie remaining time number into display string, and update the UI
        mediaRemainInString.set(Tools.getProgressTime((duration - position), true));
        adsRemainInString.set(EasyPlexApp.getContext().getString(R.string.up_next) + Tools.getProgressTime((duration - position), true));
        mediaPositionInString.set(Tools.getProgressTime(position, false));

    }


    /**
     * Get current player control state
     *
     * @return Current control state
     */
    public int getState() {
        return controlstate;
    }


    /**
     * Set current player state
     */
    public static  void setState(final int state) {
        controlstate = state;

        if (mOnControlStateChange != null) {
            mOnControlStateChange.run();
        }
    }

    /**
     * Check if it is during custom seek
     *
     * @return True if custom seek is performing
     */
    public boolean isDuringCustomSeek() {

        return controlstate == CUSTOM_SEEK_CONTROL_STATE || controlstate == EDIT_CUSTOM_SEEK_CONTROL_STATE;

    }



    @Override
    public void setPremuim(boolean premuim) {

        if (premuim) {

            isUserPremuim.set(true);

        }


    }

    @Override
    public String getMediaCoverHistory() {

        return mediaCoverHistory.get();
    }

    @Override
    public void onLaunchResume() {

        mPlaybackActionCallback.onLaunchResume();
    }

    @Override
    public int getCurrentHlsFormat() {

        return hlsformat.get();
    }


    @Override
    public String getMediaGenre() {
        return mediaGenreString.get();
    }

    @Override
    public String getSerieName() {
        return getSerieTvShowName.get();
    }

    @Override
    public float getVoteAverage() {
        return voteAverage.get();
    }

    @Override
    public int getCurrentHasRecap() {
        return hasRecap.get();
    }



    @Override
    public void isPlayerError(boolean isError) {
        isPlayerError.set(isError);
    }





    @Override
    public void setMediaRestart(boolean enabled) {
       mediaRestart.set(enabled);
    }


    @Override
    public int getCurrentStartRecapIn() {
        return recapStartIn.get();
    }

    @Override
    public int getDrm() {
        return drm.get();
    }

    @Override
    public String getDrmuuid() {
        return Drmuuid.get();
    }

    @Override
    public String getDrmlicenceuri() {
        return Drmlicenceuri.get();
    }

    @Override
    public void setScalePresenter() {

        mScalePresenter.doScale(ScaleMode.MODE_DEFAULT);

    }


    @Override
    public boolean isMediaPlayerError() {
        return isPlayerError.get();
    }

    @Override
    public void isMediaHasRecap(boolean enabled) {

        isMediaHasSkipRecap.set(enabled);

    }


    @Override
    public void mediaHasSkipRecap() {

       mPlaybackActionCallback.onMediaHasSkipRecap();

    }

    @Override
    public void onAdsPlay(boolean playing,boolean isAdsSkippable) {

       isCurrentAd.set(playing);
       isSkippable.set(isAdsSkippable);


    }



    @Override
    public void triggerSubtitlesToggle(final boolean enabled) {


        if (mEasyPlexPlayerView == null) {
            ExoPlayerLogger.e(TAG, "triggerSubtitlesToggle() --> tubiExoPlayerView is null");
            return;
        }

        //trigger the hide or show subtitles.
        View subtitles = mEasyPlexPlayerView.getSubtitleView();
        subtitles.setVisibility(enabled ? VISIBLE : View.INVISIBLE);

        if (mPlaybackActionCallback != null && mPlaybackActionCallback.isActive()) {
            mPlaybackActionCallback.onSubtitles(mMediaModel, enabled);
        }

        mediaSubstitleGet.set(enabled);
    }

    @Override
    public void triggerAutoPlay(boolean enabled) {

        isAutoPlayEnabled.set(enabled);


    }


    @Override
    public boolean isCue() {
        return isCue.get();
    }


    @Override
    public void onCheckedChanged(boolean enabled) {

        mPlaybackActionCallback.onAutoPlaySwitch(enabled);

    }

    @Override
    public void seekBy(final long millisecond) {
        if (mPlayer == null) {
            ExoPlayerLogger.e(TAG, "seekBy() ---> player is empty");
            return;
        }

        long currentPosition = mPlayer.getCurrentPosition();
        long seekPosition = currentPosition + millisecond;

        //lower bound
        seekPosition = seekPosition < 0 ? 0 : seekPosition;
        //upper bound
        seekPosition = Math.min(seekPosition, mPlayer.getDuration());

        if (mPlaybackActionCallback != null && mPlaybackActionCallback.isActive()) {

            mPlaybackActionCallback.onSeek(mMediaModel, currentPosition, seekPosition);
        }

        seekToPosition(seekPosition);
    }


    @Override
    public void onVideoSizeChanged(@NonNull VideoSize videoSize) {
        Player.Listener.super.onVideoSizeChanged(videoSize);

        ExoPlayerLogger.d(TAG, "onVideoSizeChanged");

        int width = videoSize.width;
        int height = videoSize.height;

        mInitVideoAspectRatio = height == 0 ? 1 : (width * videoSize.pixelWidthHeightRatio) / height;
    }

    @Override
    public void onRenderedFirstFrame() {
        ExoPlayerLogger.d(TAG, "onRenderedFirstFrame");
    }

    @Override
    public void seekTo(final long millisecond) {
        if (mPlaybackActionCallback != null && mPlaybackActionCallback.isActive()) {
            long currentProgress = mPlayer != null ? mPlayer.getCurrentPosition() : 0;
            mPlaybackActionCallback.onSeek(mMediaModel, currentProgress, millisecond);
        }

        seekToPosition(millisecond);


        loadPreview(millisecond, millisecond);


    }

    @Override
    public void isSubtitleEnabled(boolean enabled) {

        mediaSubstitleGet.get();


    }

    @Override
    public void subtitleCurrentLang(String lang) {

        videoCurrentSubs.set(lang);

    }


    @Override
    public Integer isMediaPremuim(){


       return mediaPremuim.get();

    }


    @Override
    public boolean hasSubsActive() {

        return Boolean.TRUE.equals(mediaHasSubstitle.get());
    }

    @Override
    public void loadPreview(long millisecond, long max) {


    }



    @Override
    public void triggerPlayerLocked() {

        if (Boolean.TRUE.equals(isPlayerLocked.get())){

            isPlayerLocked2.set(false);
            isPlayerLocked.set(false);

        }else {

            isPlayerLocked.set(true);


        }


    }


    @Override
    public void triggerPlayerLocked2() {

        isPlayerLocked2.set(true);
        mScalePresenter.lockedClicked(mEasyPlexPlayerView.getControlView().findViewById(R.id.unlock_btn_second));
    }



    @Override
    public void triggerPlayOrPause(final boolean setPlay) {

        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(setPlay);
            isVideoPlayWhenReady.set(setPlay);
        }

        if (mPlaybackActionCallback != null && mPlaybackActionCallback.isActive()) {
            mPlaybackActionCallback.onPlayToggle(mMediaModel, setPlay);


        }
    }



    /**
     * Change Video Scale
     */
    @Override
    public void scale() {

        mScalePresenter.doScale();
        ScaleMode scaleMode = mScalePresenter.getCurrentScaleMode();
        Toast.makeText(mEasyPlexPlayerView.getContext(), "" + scaleMode.getDescription(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLoadEpisodes() {

        mPlaybackActionCallback.onLoadEpisodes();

    }

    @Override
    public void onLoadStreaming() {

        mPlaybackActionCallback.onLoadSteaming();

    }

    /**
     * Return Movie or SERIE  Name
     */
    @Override
    public String getCurrentVideoName() {
        return videoName.get();
    }


    @Override
    public int getCurrentEpisodePosition() {
        return episodePosition.get();
    }


    @Override
    public String getEpID() {
        return episodeId.get();
    }



    /**
     * Get Next Season ID for TV-SERIE
     */

    @Override
    public String nextSeaonsID() {

        return nextSeasonsID.get();

    }


    /**
     * Get Current Season
     */
    @Override
    public String getCurrentSeason() {
        return episodeSeasonsId.get();
    }

    @Override
    public String getCurrentSeasonNumber() {
        return episodeSeasonsNumber.get();
    }


    /**
     * Get Current Video Quality (Servers)
     */
    @Override
    public String getVideoCurrentQuality() {
        return videoCurrentQuality.get();
    }


    /**
     * Get Episode Name
     */

    @Override
    public String getEpName() {
        return currentEpisodeName.get();
    }


    @Override
    public String getSeaonNumber() {
        return currentSeasonsNumber.get();
    }



    @Override
    public String getCurrentExternalId() {

        return getExternalId.get();
    }


    /**
     * Get Movie or TV ID
     */

    @Override
    public String getVideoID() {
        return videoID.get();
    }


    @Override
    public String getCurrentSeasonId() {
        return currentSeasonId.get();
    }


    @Override
    public String getMediaSubstitleName() {


       return  videoExternalID.get();

    }


    /**
     * Get Media Stream Link
     */
    @Override
    public Uri getVideoUrl() {
        return videoCurrentLink.get();
    }

    @Override
    public Uri getMediaSubstitleUrl() {

        return mediaSubstitleUri.get();
    }

    @Override
    public Uri getMediaPoster() {
        return Uri.parse(currentMediaCover.get());
    }


    @Override
    public void getCurrentSpeed(String speed) {

       currentSpeed.set(speed);
    }


    /**
     * Get Media Type
     */
    @Override
    public String getMediaType() {
        return mediaType.get();
    }

    @Override
    public String getCurrentEpTmdbNumber() {

        return getCurrentMediaTmdbNumber.get();
    }


    /**
     * return Media or ad
     */

    @Override
    public boolean isCurrentVideoAd() {
        return Boolean.TRUE.equals(isCurrentAd.get());
    }

    @Override
    public void isCurrentSubstitleAuto(boolean enabled) {

        autoSubstitleActivated.set(enabled);
    }


    @Override
    public void onTracksMedia() {

        mPlaybackActionCallback.onTracksMedia();

    }

    @Override
    public void clickPlaybackSetting() {

        if (mPlaybackActionCallback == null) {
            ExoPlayerLogger.w(TAG, "clickPlaybackSetting params is null");
            return;
        }

        mPlaybackActionCallback.onLoadPlaybackSetting();
    }

    @Override
    public void onLoadFromBeginning() {

        mPlaybackActionCallback.onLoadFromBeginning();

    }


    @Override
    public void onLoadSide() {


        mPlaybackActionCallback.onLoadSide();

    }


    /**
     * Release Player
     */

    @Override
    public void closePlayer() {

     ((EasyPlexMainPlayer) (mEasyPlexPlayerView.getContext())).onBackPressed();
      mPlaybackActionCallback = null;

    }




    // Return Movies List
    @Override
    public void loadMoviesList() {

        mPlaybackActionCallback.onLoadMoviesList();

    }


    // Return Next Episode for TV-Serie
    @Override
    public void nextEpisode() {

        mPlaybackActionCallback.onLoadNextEpisode();

    }


    @Override
    public void isCue(boolean enabled) {

        isCue.set(enabled);
    }



    @Override
    public void playerReady(boolean enabled) {

        playerReady.set(enabled);
    }


    @Override
    public void settingReady(boolean enabled) {

        settingReady.set(enabled);
    }


    @Override
    public boolean getIsMediaSubstitleGet() {

        return Boolean.TRUE.equals(mediaSubstitleGet.get());
    }


    // Substitles
    @Override
    public void clickOnSubs() {

        mPlaybackActionCallback.onSubtitlesSelection();


    }

    public PlayerUIController getController() {
        return controller;

    }

    public void setController(@NonNull PlayerUIController controller) {
        this.controller = controller;

    }

    //------------------------------player playback listener-------------------------------------------//


    @Override
    public void onTimelineChanged(@NotNull Timeline timeline, @Player.TimelineChangeReason int reason) {
        setPlaybackState();
        updateProgress();
    }


    @Override
    public void onPositionDiscontinuity(@NonNull Player.PositionInfo oldPosition, @NonNull Player.PositionInfo newPosition, int reason) {
        Player.Listener.super.onPositionDiscontinuity(oldPosition, newPosition, reason);
        setPlaybackState();
        updateProgress();
    }


    @Override
    public void onPlayerStateChanged(final boolean playWhenReady, final int playbackState) {
        playerPlaybackState.set(playbackState);
        isVideoPlayWhenReady.set(playWhenReady);
        updateProgress();
    }



    @Override
    public void onRepeatModeChanged(final int repeatMode) {


        //

    }

    @Override
    public void onShuffleModeEnabledChanged(final boolean shuffleModeEnabled) {


        //

    }


    @Override
    public void onTracksChanged(@NonNull Tracks tracks) {
        Player.Listener.super.onTracksChanged(tracks);
    }

    @Override
    public void onIsLoadingChanged(boolean isLoading) {
        Player.Listener.super.onIsLoadingChanged(isLoading);

        ExoPlayerLogger.i(TAG, "onLoadingChanged");
    }



    @Override
    public void onPlaybackParametersChanged(final @NotNull PlaybackParameters playbackParameters) {

    ExoPlayerLogger.d(TAG, "onPlaybackParametersChanged");

    }

    //-----------------------------------------SeekBar listener--------------------------------------------------------------//

    @Override
    public void setVideoAspectRatio(float widthHeightRatio) {

        if (mEasyPlexPlayerView != null) {
            mEasyPlexPlayerView.setAspectRatio(widthHeightRatio);
        }
        ExoPlayerLogger.i(TAG, "setVideoAspectRatio " + widthHeightRatio);

    }


    @Override
    public float getInitVideoAspectRatio() {
        ExoPlayerLogger.i(TAG, "getInitVideoAspectRatio " + mInitVideoAspectRatio);
        return mInitVideoAspectRatio;
    }


    public void setResizeMode(final int resizeMode) {
        if (mEasyPlexPlayerView != null) {
            mEasyPlexPlayerView.setResizeMode(resizeMode);
        }
    }




    @Override
    public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {


        //

    }




    @Override
    public void onStartTrackingTouch(final SeekBar seekBar) {
        isDraggingSeekBar = true;
        ExoPlayerLogger.i(TAG, "onStartTrackingTouch");
    }


    @Override
    public void onStopTrackingTouch(final SeekBar seekBar) {

        if (mPlayer != null) {
            seekTo(Tools.progressToMilli(mPlayer.getDuration(), seekBar));
        }

        isDraggingSeekBar = false;
        ExoPlayerLogger.i(TAG, "onStopTrackingTouch");
    }


    //---------------------------------------private method---------------------------------------------------------------------------//

    private void setPlaybackState() {
        int playBackState = mPlayer == null ? Player.STATE_IDLE : mPlayer.getPlaybackState();
        playerPlaybackState.set(playBackState);
    }

    private void seekToPosition(long positionMs) {
        if (mPlayer != null) {
            mPlayer.seekTo(mPlayer.getCurrentMediaItemIndex(), positionMs);
        }
    }

    private void updateProgress() {

        long position = mPlayer == null ? 0 : mPlayer.getCurrentPosition();
        long duration = mPlayer == null ? 0 : mPlayer.getDuration();
        long bufferedPosition = mPlayer == null ? 0 : mPlayer.getBufferedPosition();

        //only update the seekBar UI when user are not interacting, to prevent UI interference
        if (!isDraggingSeekBar && !isDuringCustomSeek()) {
            updateSeekBar(position, duration, bufferedPosition);
            updateTimeTextViews(position, duration);
        }

        ExoPlayerLogger.i(TAG, "updateProgress:----->" + mediaCurrentTime.get());



        if (mPlaybackActionCallback != null && mPlaybackActionCallback.isActive()) {
            mPlaybackActionCallback.onProgress(mMediaModel, position, duration);
        }else {

            return;
        }

        mProgressUpdateHandler.removeCallbacks(updateProgressAction);



        // Schedule an update if necessary.
        if (!(playerPlaybackState.get() == Player.STATE_IDLE || playerPlaybackState.get() == Player.STATE_ENDED || !mPlaybackActionCallback
                .isActive())) {

            //don't post the updateProgress event when user pause the video
            if (mPlayer != null && !mPlayer.getPlayWhenReady()) {
                return;
            }

            long delayMs;
            delayMs = DEFAULT_FREQUENCY;
            mProgressUpdateHandler.postDelayed(updateProgressAction, delayMs);
        }
    }

    private void updateSeekBar(long position, long duration, long bufferedPosition) {
        //update progressBar.
        mediaCurrentTime.set(position);
        mediaDuration.set(duration);
        mediaBufferedPosition.set(bufferedPosition);
    }


}
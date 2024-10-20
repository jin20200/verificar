package com.easyplexdemoapp.data.model.media;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.exoplayer2.source.MediaSource;
import java.io.Serializable;

/**
 * Created by stoyan on 6/5/17.
 */
public class MediaModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Episodes Info
     */

    @Nullable
    private final Integer epId;



    @Nullable
    private final String seasonId;


    @Nullable
    private final String epImdb;


    @Nullable
    private final String tvSeasonId;

    @Nullable
    private final String currentEpName;


    @Nullable
    private final String currentSeasonsNumber;


    private final Integer episodePostionNumber;


    @Nullable
    private final String currentEpTmdbNumber;


    private final int hlscustomformat;



    private final int drm;


    @Nullable
    private final String drmUUID;

    @Nullable
    private final String drmLicenseUri;


    @Nullable
    private final String currentExternalId;

    public float getVoteAverage() {
        return voteAverage;

    }

    /**
     * The url of the media
     */
    @Nullable
    private final String videoid;




    /**
     * Return Media Genre
     */
    @Nullable
    private final String mediaGenre;




    /**
     * Return Current Quality
     */

    @Nullable
    private final String currentQuality;



    /**
     * Return if media Premuim
     */

    @Nullable
    private final Integer isPremuim;


    /**
     * The url of the media
     */
    @NonNull
    private final String mediaUrl;

    private final float voteAverage;

    @Nullable
    private final String serieName;

    /**
     * The title of the media to display
     */
    @Nullable
    private final String mediaName;

    public void setMediaCover(@Nullable String mediaCover) {
        this.mediaCover = mediaCover;
    }

    /**
     * The url of the artwork to display while loading
     */
    @Nullable
    private String mediaCover;

    /**
     * The nullable subtitles that we sideload for the main media source
     */
    @Nullable
    private final String mediaSubstitleUrl;

    /**
     * The nullable click through url for this media if its an ad
     *
     * @see #isAd
     */




    @Nullable
    private final String mediaSubstitleType;


    @Nullable
    public String getSerieName() {
        return serieName;
    }

    @Nullable
    private final String mediaCoverHistory;


    private final int hasRecap;


    private final int getSkiprecapStartIn;



    @Nullable
    private final String mediaGenres;


    @Nullable
    private final String clickThroughUrl;

    /**
     * The media source representation of this model
     */
    private transient MediaSource mediaSource;

    /**
     * Whether this media is an ad or not
     */
    private final boolean isAd;


    private final String type;

    /**
     * Whether this media is an ad or not
     */
    private final boolean isVpaid;



    public MediaModel(int hlscustomformat, @Nullable String videoid, @Nullable String mediaGenre,
                      @Nullable String currentQuality, String type, @Nullable
                              String mediaName, @NonNull String mediaUrl,
                      @Nullable String mediaCover,
                      @Nullable String mediaSubstitleUrl, @Nullable String clickThroughUrl,
                      boolean isAd, boolean isVpaid, @Nullable Integer epId,
                      @Nullable String seasonId, @Nullable String epImdb,
                      @Nullable String tvSeasonId, @Nullable String currentEpName,
                      @Nullable String currentSeasonsNumber, @Nullable Integer episodePostionNumber,
                      @Nullable String currentEpTmdbNumber
            , @org.jetbrains.annotations.Nullable Integer isPremuim, @org.jetbrains.annotations.Nullable String mediaSubstitleType,
                      @org.jetbrains.annotations.Nullable String currentExternalId,
                      @Nullable String mediaCoverHistory, int hasRecap,
                      int getSkiprecapStartIn, @Nullable String mediaGenres, @Nullable String serieName, float voteAverage, @Nullable String drmUUID, @Nullable String drmLicenseUri, int drm) {


        this.voteAverage = voteAverage;
        this.serieName = serieName;
        this.hlscustomformat = hlscustomformat;
        this.videoid = videoid;
        this.mediaGenre = mediaGenre;
        this.currentQuality = currentQuality;
        this.mediaName = mediaName;
        this.mediaUrl = mediaUrl;
        this.mediaCover = mediaCover;
        this.mediaSubstitleUrl = mediaSubstitleUrl;
        this.clickThroughUrl = clickThroughUrl;
        this.isAd = isAd;
        this.type = type;
        this.isVpaid = isVpaid;
        this.epId = epId;
        this.seasonId = seasonId;
        this.epImdb = epImdb;
        this.tvSeasonId = tvSeasonId;
        this.currentEpName = currentEpName;
        this.currentSeasonsNumber = currentSeasonsNumber;
        this.episodePostionNumber = episodePostionNumber;
        this.currentEpTmdbNumber = currentEpTmdbNumber;
        this.isPremuim = isPremuim;
        this.mediaSubstitleType = mediaSubstitleType;
        this.currentExternalId = currentExternalId;
        this.mediaCoverHistory = mediaCoverHistory;
        this.hasRecap = hasRecap;
        this.getSkiprecapStartIn = getSkiprecapStartIn;
        this.mediaGenres = mediaGenres;
        this.drmUUID = drmUUID;
        this.drmLicenseUri = drmLicenseUri;
        this.drm = drm;
    }



    public static MediaModel media(@Nullable String videoid, @Nullable String substitleLang, @Nullable String currentQuality, String type,
                                   @Nullable String mediaName, @NonNull String videoUrl, @Nullable String artworkUrl,
                                   @Nullable String subtitlesUrl, @Nullable Integer epId, @Nullable String seasonId, @Nullable String epImdb,
                                   @Nullable String tvSeasonId, @Nullable String currentEpName, @Nullable
                                           String currentSeasonsNumber, Integer episodePostionNumber,
                                   String currentEpTmdbNumber , Integer isPremuim,
                                   int hlscustomformat,
                                   String mediaSubstitleType ,
                                   String currentExternalId ,
                                   String serieCover,
                                   int hasRecap, int getSkiprecapStartIn , String mediaGenres , String serieName , float voteAverage, @Nullable
                                           String drmUUID,@Nullable
                                           String drmLicenseUri,int drm) {


        return new MediaModel(hlscustomformat, videoid, substitleLang, currentQuality ,type, mediaName, videoUrl, artworkUrl, subtitlesUrl, null,
                false, false,epId,seasonId,epImdb, tvSeasonId,
                currentEpName, currentSeasonsNumber, episodePostionNumber,
                currentEpTmdbNumber,isPremuim, mediaSubstitleType ,
                currentExternalId, serieCover, hasRecap, getSkiprecapStartIn,
                mediaGenres,serieName,voteAverage, drmUUID, drmLicenseUri, drm);
    }

    public int getDrm() {
        return drm;
    }

    public static MediaModel ad(@NonNull String videoUrl, @Nullable String clickThroughUrl, boolean isVpaid) {
        return new MediaModel(0, null, null, null, null,null , videoUrl, null, null, clickThroughUrl,true,isVpaid,null,null

                ,null, null, null,
                null, null,
                null,null,null,null,
                null, 0, 0,
                null,null,0, null, null, 0);
    }

    @Nullable
    public String getMediaName() {
        return mediaName;
    }

    @Nullable
    public String getDrmLicenseUri() {
        return drmLicenseUri;
    }

    @Nullable
    public String getDrmUUID() {
        return drmUUID;
    }

    @NonNull
    public Uri getMediaUrl() {
        return Uri.parse(mediaUrl);
    }

    @Nullable
    public String getMediaCover() {

      return mediaCover;
    }

    @Nullable
    public String getMediaSubstitleType() {
        return mediaSubstitleType;
    }

    @Nullable
    public Uri getMediaSubstitleUrl() {
        return mediaSubstitleUrl != null ? Uri.parse(mediaSubstitleUrl) : null;
    }


    @Nullable
    public String getCurrentSeasonsNumber() {
        return currentSeasonsNumber;
    }

    @Nullable
    public String getClickThroughUrl() {
        return clickThroughUrl;
    }

    public boolean isAd() {
        return isAd;
    }

    @Nullable
    public String getCurrentEpTmdbNumber() {
        return currentEpTmdbNumber;
    }

    public String getMediaExtension() {
        return null;
    }

    public MediaSource getMediaSource() {
        return mediaSource;
    }

    public void setMediaSource(MediaSource mediaSource) {
        this.mediaSource = mediaSource;
    }

    @Nullable
    public String getTvSeasonId() {
        return tvSeasonId;
    }

    @Nullable
    public String getVideoid() {
        return videoid;
    }

    @Nullable
    public String getCurrentEpName() {
        return currentEpName;
    }

    public String getType() {
        return type;
    }


    @Nullable
    public String getMediaGenre() {
        return mediaGenre;
    }


    @Nullable
    public String getMediaGenres() {
        return mediaGenres;
    }



    public boolean isVpaid() {
        return isVpaid;
    }


    @Nullable
    public Integer getEpisodePostionNumber() {
        return episodePostionNumber;
    }

    @Nullable
    public Integer getEpId() {
        return epId;
    }

    @Nullable
    public String getSeasonId() {
        return seasonId;
    }

    @Nullable
    public String getEpImdb() {
        return epImdb;
    }


    public int getHlscustomformat() {
        return hlscustomformat;
    }


    @Nullable
    public String getCurrentQuality() {
        return currentQuality;
    }


    @Nullable
    public Integer getIsPremuim() {
        return isPremuim;
    }


    @Nullable
    public String getMediaCoverHistory() {
        return mediaCoverHistory;
    }

    public Integer getHasRecap() {
        return hasRecap;
    }

    public Integer getGetSkiprecapStartIn() {
        return getSkiprecapStartIn;
    }

    @Nullable
    public String getCurrentExternalId() {
        return currentExternalId;
    }
}

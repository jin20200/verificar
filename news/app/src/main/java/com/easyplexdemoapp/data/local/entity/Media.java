package com.easyplexdemoapp.data.local.entity;


import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.RoomWarnings;
import com.bumptech.glide.Glide;
import com.easyplexdemoapp.data.model.certifications.Certification;
import com.easyplexdemoapp.data.model.comments.Comment;
import com.easyplexdemoapp.data.model.credits.Cast;
import com.easyplexdemoapp.data.model.genres.Genre;
import com.easyplexdemoapp.data.model.serie.Season;
import com.easyplexdemoapp.data.model.stream.MediaStream;
import com.easyplexdemoapp.data.model.substitles.MediaSubstitle;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * @author Yobex.
 */


@Entity(tableName = "movies")


@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
public class Media implements Parcelable {

    public Media(){

        //
    }


    protected Media(Parcel in) {
        deviceId = in.readString();
        id = in.readString();
        tmdbId = in.readString();
        if (in.readByte() == 0) {
            skiprecapStartIn = null;
        } else {
            skiprecapStartIn = in.readInt();
        }
        if (in.readByte() == 0) {
            hasrecap = null;
        } else {
            hasrecap = in.readInt();
        }
        imdbExternalId = in.readString();
        title = in.readString();
        subtitle = in.readString();
        type = in.readString();
        name = in.readString();
        substype = in.readString();
        contentLength = in.readLong();
        overview = in.readString();
        posterPath = in.readString();
        linkpreview = in.readString();
        minicover = in.readString();
        backdropPath = in.readString();
        previewPath = in.readString();
        trailerUrl = in.readString();
        voteAverage = in.readFloat();
        voteCount = in.readString();
        live = in.readInt();
        premuim = in.readInt();
        newEpisodes = in.readInt();
        userHistoryId = in.readInt();
        vip = in.readInt();
        hls = in.readInt();
        streamhls = in.readInt();
        link = in.readString();
        embed = in.readInt();
        youtubelink = in.readInt();
        resumeWindow = in.readInt();
        resumePosition = in.readLong();
        isAnime = in.readInt();
        popularity = in.readString();
        views = in.readString();
        status = in.readString();
        runtime = in.readString();
        releaseDate = in.readString();
        genre = in.readString();
        firstAirDate = in.readString();
        trailerId = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        if (in.readByte() == 0) {
            hd = null;
        } else {
            hd = in.readInt();
        }
        genres = in.createTypedArrayList(Genre.CREATOR);
    }

    public static final Creator<Media> CREATOR = new Creator<>() {
        @Override
        public Media createFromParcel(Parcel in) {
            return new Media(in);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @SerializedName("deviceId")
    @Expose
    private String deviceId;


    @NonNull
    @SerializedName("id")
    @Expose
    @PrimaryKey
    private String id;

    @SerializedName("tmdb_id")
    @Expose
    private String tmdbId;


    public Integer getSkiprecapStartIn() {
        return skiprecapStartIn;
    }

    public void setSkiprecapStartIn(Integer skiprecapStartIn) {
        this.skiprecapStartIn = skiprecapStartIn;
    }

    public Integer getHasrecap() {
        return hasrecap;
    }

    public void setHasrecap(Integer hasrecap) {
        this.hasrecap = hasrecap;
    }

    @SerializedName("skiprecap_start_in")
    @Expose
    private Integer skiprecapStartIn;

    @SerializedName("hasrecap")
    @Expose
    private Integer hasrecap;


    public String getImdbExternalId() {
        return imdbExternalId;
    }

    public void setImdbExternalId(String imdbExternalId) {
        this.imdbExternalId = imdbExternalId;
    }

    @SerializedName("imdb_external_id")
    @Expose
    private String imdbExternalId;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    private String title;

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @SerializedName("subtitle")
    @Expose
    private String subtitle;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("name")
    @Expose
    private String name;

    public String getSubstype() {
        return substype;
    }

    public void setSubstype(String substype) {
        this.substype = substype;
    }

    @SerializedName("substype")
    @Expose
    private String substype;


    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    private long contentLength;


    @SerializedName("overview")
    @Expose
    private String overview;


    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    @SerializedName("genre_name")
    @Expose
    private String genreName;



    public String getMinicover() {
        return minicover;
    }

    public void setMinicover(String minicover) {
        this.minicover = minicover;
    }


    public String getLinkpreview() {
        return linkpreview;
    }

    public void setLinkpreview(String linkpreview) {
        this.linkpreview = linkpreview;
    }

    @SerializedName("linkpreview")
    @Expose
    private String linkpreview;

    @SerializedName("minicover")
    @Expose
    private String minicover;

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("preview_path")
    @Expose
    private String previewPath;

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    @SerializedName("trailer_url")
    @Expose
    private String trailerUrl;


    @SerializedName("vote_average")
    @Expose
    private float voteAverage;


    @SerializedName("vote_count")
    @Expose
    private String voteCount;


    @SerializedName("live")
    @Expose
    private int live;

    @SerializedName("premuim")
    @Expose
    private int premuim;


    public int getEnableStream() {
        return enableStream;
    }

    public void setEnableStream(int enableStream) {
        this.enableStream = enableStream;
    }

    @SerializedName("enable_stream")
    @Expose
    private int enableStream;


    public int getEnableAdsUnlock() {
        return enableAdsUnlock;
    }

    public void setEnableAdsUnlock(int enableAdsUnlock) {
        this.enableAdsUnlock = enableAdsUnlock;
    }

    @SerializedName("enable_ads_unlock")
    @Expose
    private int enableAdsUnlock;


    public int getEnableDownload() {
        return enableDownload;
    }

    public void setEnableDownload(int enableDownload) {
        this.enableDownload = enableDownload;
    }

    @SerializedName("enable_media_download")
    @Expose
    private int enableDownload;




    public int getNewEpisodes() {
        return newEpisodes;
    }

    public void setNewEpisodes(int newEpisodes) {
        this.newEpisodes = newEpisodes;
    }

    @SerializedName("newEpisodes")
    @Expose
    private int newEpisodes;

    public int getUserHistoryId() {
        return userHistoryId;
    }

    public void setUserHistoryId(int userHistoryId) {
        this.userHistoryId = userHistoryId;
    }

    @SerializedName("user_history_id")
    @Expose
    private int userHistoryId;

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    @SerializedName("vip")
    @Expose
    private int vip;

    public int getHls() {
        return hls;
    }

    public void setHls(int hls) {
        this.hls = hls;
    }

    @SerializedName("hls")
    @Expose
    private int hls;

    private int streamhls;


    public int getStreamhls() {
        return streamhls;
    }

    public void setStreamhls(int streamhls) {
        this.streamhls = streamhls;
    }

    @SerializedName("link")
    @Expose
    private String link;

    public int getEmbed() {
        return embed;
    }

    public void setEmbed(int embed) {
        this.embed = embed;
    }

    @SerializedName("embed")
    @Expose
    private int embed;

    public int getYoutubelink() {
        return youtubelink;
    }

    public void setYoutubelink(int youtubelink) {
        this.youtubelink = youtubelink;
    }

    @SerializedName("youtubelink")
    @Expose
    private int youtubelink;


    private int resumeWindow;

    private long resumePosition;

    @SerializedName("is_anime")
    @Expose
    private int isAnime;

    @SerializedName("popularity")
    @Expose
    private String popularity;


    @SerializedName("views")
    @Expose
    private String views;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("substitles")
    @Expose
    private List<MediaSubstitle> substitles = null;


    @SerializedName("seasons")
    @Expose
    private List<Season> seasons = null;

    @SerializedName("runtime")
    @Expose
    private String runtime;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @SerializedName("genre")
    @Expose
    private String genre;

    @SerializedName("first_air_date")
    @Expose
    private String firstAirDate;

    @SerializedName("trailer_id")
    @Expose
    private String trailerId;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;



    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getLive() {
        return live;
    }

    public void setLive(int live) {
        this.live = live;
    }


    @SerializedName("hd")
    @Expose
    private Integer hd;

    public List<MediaStream> getDownloads() {
        return downloads;
    }

    public void setDownloads(List<MediaStream> downloads) {
        this.downloads = downloads;
    }

    @SerializedName("downloads")
    @Expose
    private List<MediaStream> downloads;


    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @SerializedName("videos")
    @Expose
    private List<MediaStream> videos;

    @SerializedName("comments")
    @Expose
    private List<Comment> comments = null;

    @SerializedName("genres")
    @Expose
    private List<Genre> genres;


    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }


    public List<Media> getRelateds() {
        return relateds;
    }

    public void setRelateds(List<Media> relateds) {
        this.relateds = relateds;
    }

    @SerializedName("relateds")
    @Expose
    private List<Media> relateds;

    @SerializedName("casterslist")
    @Expose
    private List<Cast> cast;

    public List<Certification> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<Certification> certifications) {
        this.certifications = certifications;
    }

    @SerializedName("certifications")
    @Expose
    private List<Certification> certifications;

    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }



    public String getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(String tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getPreviewPath() {
        return previewPath;
    }

    public void setPreviewPath(String previewPath) {
        this.previewPath = previewPath;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getHd() {
        return hd;
    }

    public void setHd(Integer hd) {
        this.hd = hd;
    }


    public List<MediaSubstitle> getSubstitles() {
        return substitles;
    }

    public void setSubstitles(List<MediaSubstitle> substitles) {
        this.substitles = substitles;
    }



    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }


    public List<MediaStream> getVideos() {
        return videos;
    }

    public void setVideos(List<MediaStream> videos) {
        this.videos = videos;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }


    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }


    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getPremuim() {
        return premuim;
    }

    public void setPremuim(int premuim) {
        this.premuim = premuim;
    }


    public int getResumeWindow() {
        return resumeWindow;
    }

    public void setResumeWindow(int resumeWindow) {
        this.resumeWindow = resumeWindow;
    }

    public long getResumePosition() {
        return resumePosition;
    }

    public void setResumePosition(long resumePosition) {
        this.resumePosition = resumePosition;
    }


    public String getMediaExtension() {
        return null;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }


    public String getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }



    public int getIsAnime() {
        return isAnime;
    }

    public void setIsAnime(int isAnime) {
        this.isAnime = isAnime;
    }


    @BindingAdapter("android:imageUrl")
    public static void loadImage (ImageView view, String url) {
        Glide.with(view.getContext()).load(url).into(view);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceId);
        dest.writeString(id);
        dest.writeString(tmdbId);
        if (skiprecapStartIn == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(skiprecapStartIn);
        }
        if (hasrecap == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(hasrecap);
        }
        dest.writeString(imdbExternalId);
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(type);
        dest.writeString(name);
        dest.writeString(substype);
        dest.writeLong(contentLength);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(linkpreview);
        dest.writeString(minicover);
        dest.writeString(backdropPath);
        dest.writeString(previewPath);
        dest.writeString(trailerUrl);
        dest.writeFloat(voteAverage);
        dest.writeString(voteCount);
        dest.writeInt(live);
        dest.writeInt(premuim);
        dest.writeInt(newEpisodes);
        dest.writeInt(userHistoryId);
        dest.writeInt(vip);
        dest.writeInt(hls);
        dest.writeInt(streamhls);
        dest.writeString(link);
        dest.writeInt(embed);
        dest.writeInt(youtubelink);
        dest.writeInt(resumeWindow);
        dest.writeLong(resumePosition);
        dest.writeInt(isAnime);
        dest.writeString(popularity);
        dest.writeString(views);
        dest.writeString(status);
        dest.writeString(runtime);
        dest.writeString(releaseDate);
        dest.writeString(genre);
        dest.writeString(firstAirDate);
        dest.writeString(trailerId);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        if (hd == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(hd);
        }
        dest.writeTypedList(genres);
    }
}

package com.easyplexdemoapp.data.model.episode;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.easyplexdemoapp.data.model.genres.Genre;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LatestEpisodes implements Parcelable {



    public LatestEpisodes(){

        //
    }

    protected LatestEpisodes(Parcel in) {
        genreName = in.readString();
        imdbExternalId = in.readString();
        epoverview = in.readString();
        header = in.readString();
        useragent = in.readString();
        drmuuid = in.readString();
        drmlicenceuri = in.readString();
        drm = in.readInt();
        type = in.readString();
        if (in.readByte() == 0) {
            isAnime = null;
        } else {
            isAnime = in.readInt();
        }
        if (in.readByte() == 0) {
            premuim = null;
        } else {
            premuim = in.readInt();
        }
        if (in.readByte() == 0) {
            serieTmdb = null;
        } else {
            serieTmdb = in.readInt();
        }
        if (in.readByte() == 0) {
            integer = null;
        } else {
            integer = in.readInt();
        }
        voteAverage = in.readFloat();
        if (in.readByte() == 0) {
            episodeId = null;
        } else {
            episodeId = in.readInt();
        }
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        stillPath = in.readString();
        if (in.readByte() == 0) {
            episodeNumber = null;
        } else {
            episodeNumber = in.readInt();
        }
        if (in.readByte() == 0) {
            hasrecap = null;
        } else {
            hasrecap = in.readInt();
        }
        if (in.readByte() == 0) {
            skiprecapStartIn = null;
        } else {
            skiprecapStartIn = in.readInt();
        }
        posterPath = in.readString();
        if (in.readByte() == 0) {
            animeEpisodeId = null;
        } else {
            animeEpisodeId = in.readInt();
        }
        if (in.readByte() == 0) {
            animeSeasonId = null;
        } else {
            animeSeasonId = in.readInt();
        }
        if (in.readByte() == 0) {
            seasonId = null;
        } else {
            seasonId = in.readInt();
        }
        episodeName = in.readString();
        link = in.readString();
        server = in.readString();
        lang = in.readString();
        serieName = in.readString();
        embed = in.readString();
        if (in.readByte() == 0) {
            youtubelink = null;
        } else {
            youtubelink = in.readInt();
        }
        supportedHosts = in.readInt();
        if (in.readByte() == 0) {
            hls = null;
        } else {
            hls = in.readInt();
        }
        seasonsName = in.readString();
        if (in.readByte() == 0) {
            seasonNumber = null;
        } else {
            seasonNumber = in.readInt();
        }
        if (in.readByte() == 0) {
            hd = null;
        } else {
            hd = in.readInt();
        }
        genreslist = in.createStringArrayList();
        if (in.readByte() == 0) {
            hasubs = null;
        } else {
            hasubs = in.readInt();
        }
        genres = in.createTypedArrayList(Genre.CREATOR);
    }

    public static final Creator<LatestEpisodes> CREATOR = new Creator<LatestEpisodes>() {
        @Override
        public LatestEpisodes createFromParcel(Parcel in) {
            return new LatestEpisodes(in);
        }

        @Override
        public LatestEpisodes[] newArray(int size) {
            return new LatestEpisodes[size];
        }
    };

    public String getEpoverview() {
        return epoverview;
    }

    public void setEpoverview(String epoverview) {
        this.epoverview = epoverview;
    }


    public String getImdbExternalId() {
        return imdbExternalId;
    }

    public void setImdbExternalId(String imdbExternalId) {
        this.imdbExternalId = imdbExternalId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    @SerializedName("genre_name")
    @Expose
    private String genreName;

    @SerializedName("imdb_external_id")
    @Expose
    private String imdbExternalId;

    @SerializedName("epoverview")
    @Expose
    private String epoverview;

    public Integer getPremuim() {
        return premuim;
    }

    public void setPremuim(Integer premuim) {
        this.premuim = premuim;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Integer getIsAnime() {
        return isAnime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIsAnime(Integer isAnime) {
        this.isAnime = isAnime;
    }


    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getUseragent() {
        return useragent;
    }

    public void setUseragent(String useragent) {
        this.useragent = useragent;
    }

    @SerializedName("header")
    @Expose
    private String header;


    @SerializedName("useragent")
    @Expose
    private String useragent;



    @SerializedName("drmuuid")
    @Expose
    private String drmuuid;

    public String getDrmuuid() {
        return drmuuid;
    }

    public void setDrmuuid(String drmuuid) {
        this.drmuuid = drmuuid;
    }

    public String getDrmlicenceuri() {
        return drmlicenceuri;
    }

    public void setDrmlicenceuri(String drmlicenceuri) {
        this.drmlicenceuri = drmlicenceuri;
    }

    @SerializedName("drmlicenceuri")
    @Expose
    private String drmlicenceuri;

    public int getDrm() {
        return drm;
    }

    public void setDrm(int drm) {
        this.drm = drm;
    }

    @SerializedName("drm")
    @Expose
    private int drm;




    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("is_anime")
    @Expose
    private Integer isAnime;

    @SerializedName("premuim")
    @Expose
    private Integer premuim;

    public Integer getTmdbId() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }


    public Integer getSerieTmdb() {
        return serieTmdb;
    }

    public void setSerieTmdb(Integer serieTmdb) {
        this.serieTmdb = serieTmdb;
    }

    @SerializedName("serieTmdb")
    @Expose
    private Integer serieTmdb;

    @SerializedName("tmdb_id")
    @Expose
    private Integer integer;

    @SerializedName("vote_average")
    @Expose
    private float voteAverage;



    @Nullable
    @SerializedName("episode_id")
    @Expose
    private Integer episodeId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("still_path")
    @Expose
    private String stillPath;

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    @SerializedName("episode_number")
    @Expose
    private Integer episodeNumber;


    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }


    public Integer getHasrecap() {
        return hasrecap;
    }

    public void setHasrecap(Integer hasrecap) {
        this.hasrecap = hasrecap;
    }

    public Integer getSkiprecapStartIn() {
        return skiprecapStartIn;
    }

    public void setSkiprecapStartIn(Integer skiprecapstartin) {
        this.skiprecapStartIn = skiprecapstartin;
    }

    public void setHls(int hls) {
        this.hls = hls;
    }

    @SerializedName("hasrecap")
    @Expose
    private Integer hasrecap;

    @SerializedName("skiprecap_start_in")
    @Expose
    private Integer skiprecapStartIn;



    @SerializedName("poster_path")
    @Expose
    private String posterPath;


    @SerializedName("anime_episode_id")
    @Expose
    private Integer animeEpisodeId;

    public Integer getAnimeEpisodeId() {
        return animeEpisodeId;
    }

    public void setAnimeEpisodeId(Integer animeEpisodeId) {
        this.animeEpisodeId = animeEpisodeId;
    }

    public Integer getAnimeSeasonId() {
        return animeSeasonId;
    }

    public void setAnimeSeasonId(Integer animeSeasonId) {
        this.animeSeasonId = animeSeasonId;
    }

    @SerializedName("anime_season_id")
    @Expose
    private Integer animeSeasonId;

    @SerializedName("season_id")
    @Expose
    private Integer seasonId;
    @SerializedName("episode_name")
    @Expose
    private String episodeName;
    @SerializedName("link")
    @Expose
    private String link;


    public int getEnableStream() {
        return enableStream;
    }

    public void setEnableStream(int enableStream) {
        this.enableStream = enableStream;
    }

    @SerializedName("enable_stream")
    @Expose
    private int enableStream;
    @SerializedName("server")
    @Expose
    private String server;
    @SerializedName("lang")
    @Expose
    private String lang;

    public String getSerieName() {
        return serieName;
    }

    public void setSerieName(String serieName) {
        this.serieName = serieName;
    }

    @SerializedName("serieName")
    @Expose
    private String serieName;

    public int getSupportedHosts() {
        return supportedHosts;
    }

    public void setSupportedHosts(int supportedHosts) {
        this.supportedHosts = supportedHosts;
    }

    @SerializedName("embed")
    @Expose
    private String embed;
    @SerializedName("youtubelink")
    @Expose
    private Integer youtubelink;

    @SerializedName("supported_hosts")
    @Expose
    private int supportedHosts;

    @SerializedName("hls")
    @Expose
    private Integer hls;
    @SerializedName("seasons_name")
    @Expose
    private String seasonsName;
    @SerializedName("season_number")
    @Expose
    private Integer seasonNumber;
    @SerializedName("hd")
    @Expose
    private Integer hd;
    @SerializedName("genreslist")
    @Expose
    private List<String> genreslist = null;
    @SerializedName("hasubs")
    @Expose
    private Integer hasubs;
    @SerializedName("genres")
    @Expose
    private List<Genre> genres = null;

    public Integer getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(Integer episodeId) {
        this.episodeId = episodeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStillPath() {
        return stillPath;
    }

    public void setStillPath(String stillPath) {
        this.stillPath = stillPath;
    }

    public Integer getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Integer seasonId) {
        this.seasonId = seasonId;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getEmbed() {
        return embed;
    }

    public void setEmbed(String embed) {
        this.embed = embed;
    }

    public Integer getYoutubelink() {
        return youtubelink;
    }

    public void setYoutubelink(Integer youtubelink) {
        this.youtubelink = youtubelink;
    }

    public Integer getHls() {
        return hls;
    }

    public void setHls(Integer hls) {
        this.hls = hls;
    }

    public String getSeasonsName() {
        return seasonsName;
    }

    public void setSeasonsName(String seasonsName) {
        this.seasonsName = seasonsName;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public Integer getHd() {
        return hd;
    }

    public void setHd(Integer hd) {
        this.hd = hd;
    }

    public List<String> getGenreslist() {
        return genreslist;
    }

    public void setGenreslist(List<String> genreslist) {
        this.genreslist = genreslist;
    }

    public Integer getHasubs() {
        return hasubs;
    }

    public void setHasubs(Integer hasubs) {
        this.hasubs = hasubs;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(genreName);
        dest.writeString(imdbExternalId);
        dest.writeString(epoverview);
        dest.writeString(header);
        dest.writeString(useragent);
        dest.writeString(drmuuid);
        dest.writeString(drmlicenceuri);
        dest.writeInt(drm);
        dest.writeString(type);
        if (isAnime == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isAnime);
        }
        if (premuim == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(premuim);
        }
        if (serieTmdb == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(serieTmdb);
        }
        if (integer == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(integer);
        }
        dest.writeFloat(voteAverage);
        if (episodeId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(episodeId);
        }
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(stillPath);
        if (episodeNumber == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(episodeNumber);
        }
        if (hasrecap == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(hasrecap);
        }
        if (skiprecapStartIn == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(skiprecapStartIn);
        }
        dest.writeString(posterPath);
        if (animeEpisodeId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(animeEpisodeId);
        }
        if (animeSeasonId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(animeSeasonId);
        }
        if (seasonId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(seasonId);
        }
        dest.writeString(episodeName);
        dest.writeString(link);
        dest.writeString(server);
        dest.writeString(lang);
        dest.writeString(serieName);
        dest.writeString(embed);
        if (youtubelink == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(youtubelink);
        }
        dest.writeInt(supportedHosts);
        if (hls == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(hls);
        }
        dest.writeString(seasonsName);
        if (seasonNumber == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(seasonNumber);
        }
        if (hd == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(hd);
        }
        dest.writeStringList(genreslist);
        if (hasubs == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(hasubs);
        }
        dest.writeTypedList(genres);
    }
}

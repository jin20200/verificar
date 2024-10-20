package com.easyplexdemoapp.data.local;

import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.easyplexdemoapp.data.local.converters.CastConverter;
import com.easyplexdemoapp.data.local.converters.CertificationsConverter;
import com.easyplexdemoapp.data.local.converters.CommentsConverter;
import com.easyplexdemoapp.data.local.converters.GenreConverter;
import com.easyplexdemoapp.data.local.converters.MediaListConverter;
import com.easyplexdemoapp.data.local.converters.MediaStreamConverter;
import com.easyplexdemoapp.data.local.converters.MediaSubstitlesConverter;
import com.easyplexdemoapp.data.local.converters.NotificationConverter;
import com.easyplexdemoapp.data.local.converters.SaisonConverter;
import com.easyplexdemoapp.data.local.converters.VideosConverter;
import com.easyplexdemoapp.data.local.dao.AddedSearchDao;
import com.easyplexdemoapp.data.local.dao.AnimesDao;
import com.easyplexdemoapp.data.local.dao.MoviesDao;
import com.easyplexdemoapp.data.local.dao.DownloadDao;
import com.easyplexdemoapp.data.local.dao.HistoryDao;
import com.easyplexdemoapp.data.local.dao.NotificationDao;
import com.easyplexdemoapp.data.local.dao.ResumeDao;
import com.easyplexdemoapp.data.local.dao.SeriesDao;
import com.easyplexdemoapp.data.local.dao.StreamListDao;
import com.easyplexdemoapp.data.local.entity.AddedSearch;
import com.easyplexdemoapp.data.local.entity.Animes;
import com.easyplexdemoapp.data.local.entity.History;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.local.entity.Download;
import com.easyplexdemoapp.data.local.entity.Notification;
import com.easyplexdemoapp.data.local.entity.Series;
import com.easyplexdemoapp.data.local.entity.Stream;
import com.easyplexdemoapp.data.model.media.Resume;


/**
 * The Room database that contains the Favorite Movies & Series & Animes table
 * Define an abstract class that extends RoomDatabase.
 * This class is annotated with @Database, lists the entities contained in the database,
 * and the DAOs which access them.
 */
@Database(entities = {Media.class, Series.class, Animes.class, Download.class, History.class, Stream.class, Resume.class , AddedSearch.class
, Notification.class}, version =57,exportSchema = false)
@TypeConverters({GenreConverter.class,
        CastConverter.class,
        VideosConverter.class,
        SaisonConverter.class,
        MediaSubstitlesConverter.class,
        MediaStreamConverter.class,
        CommentsConverter.class, MediaListConverter.class, CertificationsConverter.class, NotificationConverter.class})
public abstract class EasyPlexDatabase extends RoomDatabase {

    public abstract MoviesDao favoriteDao();
    public abstract SeriesDao seriesDao();
    public abstract AnimesDao animesDao();
    public abstract DownloadDao progressDao();
    public abstract HistoryDao historyDao();
    public abstract StreamListDao streamListDao();
    public abstract ResumeDao resumeDao();
    public abstract AddedSearchDao addedSearchDao();
    public abstract NotificationDao notificationDao();

}

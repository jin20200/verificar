package com.easyplexdemoapp.data.local;

import android.annotation.SuppressLint;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DatabaseMigration {

    private static void addColumnIfNotExists(SupportSQLiteDatabase database, String tableName, String columnName, String columnType) {
        Cursor cursor = database.query("PRAGMA table_info(" + tableName + ")");
        boolean columnExists = false;
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String existingColumnName = cursor.getString(cursor.getColumnIndex("name"));
            if (columnName.equals(existingColumnName)) {
                columnExists = true;
                break;
            }
        }
        cursor.close();

        if (!columnExists) {
            database.execSQL("ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnType);
        }
    }

    public static final Migration MIGRATION_1_4 = new Migration(1, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Media table updates
            database.execSQL("CREATE TABLE IF NOT EXISTS `movies_new` ("
                    + "`id` TEXT PRIMARY KEY NOT NULL,"
                    + "`device_id` TEXT,"
                    + "`tmdb_id` TEXT,"
                    + "`skiprecap_start_in` INTEGER,"
                    + "`hasrecap` INTEGER,"
                    + "`imdb_external_id` TEXT,"
                    + "`title` TEXT,"
                    + "`subtitle` TEXT,"
                    + "`type` TEXT,"
                    + "`name` TEXT,"
                    + "`substype` TEXT,"
                    + "`content_length` INTEGER,"
                    + "`overview` TEXT,"
                    + "`poster_path` TEXT,"
                    + "`linkpreview` TEXT,"
                    + "`minicover` TEXT,"
                    + "`backdrop_path` TEXT,"
                    + "`preview_path` TEXT,"
                    + "`trailer_url` TEXT,"
                    + "`vote_average` REAL,"
                    + "`vote_count` TEXT,"
                    + "`live` INTEGER,"
                    + "`premuim` INTEGER,"
                    + "`enable_stream` INTEGER,"
                    + "`enable_ads_unlock` INTEGER,"
                    + "`enable_media_download` INTEGER,"
                    + "`new_episodes` INTEGER,"
                    + "`user_history_id` INTEGER,"
                    + "`vip` INTEGER,"
                    + "`hls` INTEGER,"
                    + "`streamhls` INTEGER,"
                    + "`link` TEXT,"
                    + "`embed` INTEGER,"
                    + "`youtubelink` INTEGER,"
                    + "`resume_window` INTEGER,"
                    + "`resume_position` INTEGER,"
                    + "`is_anime` INTEGER,"
                    + "`popularity` TEXT,"
                    + "`views` TEXT,"
                    + "`status` TEXT,"
                    + "`runtime` TEXT,"
                    + "`release_date` TEXT,"
                    + "`genre` TEXT,"
                    + "`first_air_date` TEXT,"
                    + "`trailer_id` TEXT,"
                    + "`created_at` TEXT,"
                    + "`updated_at` TEXT,"
                    + "`hd` INTEGER,"
                    + "`genre_name` TEXT)");

            // Copy data from the old table to the new one
            database.execSQL("INSERT OR IGNORE INTO movies_new SELECT * FROM movies");

            // Drop the old table
            database.execSQL("DROP TABLE IF EXISTS movies");

            // Rename the new table to the original name
            database.execSQL("ALTER TABLE movies_new RENAME TO movies");

            // History table creation
            database.execSQL("CREATE TABLE IF NOT EXISTS `history` ("
                    + "`id` TEXT PRIMARY KEY NOT NULL,"
                    + "`user_deviceId` TEXT,"
                    + "`userprofile_history` TEXT,"
                    + "`userMainId` INTEGER NOT NULL,"
                    + "`tmdbId_history` TEXT NOT NULL,"
                    + "`posterpath_history` TEXT,"
                    + "`serieName_history` TEXT,"
                    + "`title_history` TEXT,"
                    + "`backdrop_path_history` TEXT,"
                    + "`link_history` TEXT,"
                    + "`tv_history` TEXT,"
                    + "`type_history` TEXT,"
                    + "`positionEpisode_history` TEXT,"
                    + "`externalId_history` TEXT,"
                    + "`seasonsNumber_history` TEXT,"
                    + "`seasondbId_history` INTEGER NOT NULL,"
                    + "`mediaGenre_history` TEXT,"
                    + "`seasonId_history` TEXT,"
                    + "`episodeNmber_history` TEXT,"
                    + "`postion_history` INTEGER NOT NULL,"
                    + "`episodeName_history` TEXT,"
                    + "`currentSeasons_history` TEXT,"
                    + "`episodeId_history` TEXT,"
                    + "`serieId_history` TEXT,"
                    + "`episodeTmdb_history` TEXT)");

            // Notifications table creation
            database.execSQL("CREATE TABLE IF NOT EXISTS `notifications` ("
                    + "`notificationId` INTEGER NOT NULL,"
                    + "`title` TEXT,"
                    + "`backdrop` TEXT,"
                    + "`overview` TEXT,"
                    + "`timestamp` INTEGER,"
                    + "`type` TEXT,"
                    + "`link` TEXT,"
                    + "`imdb` TEXT NOT NULL,"
                    + "PRIMARY KEY(`imdb`))");
        }
    };
}
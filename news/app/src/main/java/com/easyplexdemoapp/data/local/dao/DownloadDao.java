package com.easyplexdemoapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.easyplexdemoapp.data.local.entity.Download;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Data Access Object that contains methods used for accessing the database.
 *
 * @author Yobex.
 */
@Dao
public interface DownloadDao {

    // Return Movies & Series From Favorite Table
    @Query("SELECT * FROM download")
    Flowable<List<Download>> getDownloadMovies();



    // Save the the movie or serie in the  Favorite Table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveMediaToFavorite(Download download);


    @Query("SELECT * FROM download WHERE id=:id")
    LiveData<Download> getDownLoadedMediaInfo(int id);


    // Delete a movie or serie from the  Favorite Table
    @Delete
    void deleteMediaFromDownload(Download download);




}

package com.easyplexdemoapp.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import com.easyplexdemoapp.data.local.entity.Series;
import com.easyplexdemoapp.data.local.entity.Stream;
import java.util.List;
import io.reactivex.Flowable;

/**
 * Data Access Object that contains methods used for accessing the database.
 *
 * @author Yobex.
 */
@Dao
public interface StreamListDao {

    // Return Movies & Series From Favorite Table
    @Query("SELECT * FROM stream")
    Flowable<List<Stream>> getHistory();


    @SuppressWarnings({RoomWarnings.CURSOR_MISMATCH})
    @Query("SELECT * FROM stream")
    Flowable<List<Stream>> getFavorite();




    @Delete
    void deleteMediaFromFavorite(Series series);


    // Save the the movie or serie in the  Favorite Table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveMediaToFavorite(Stream stream);


    // Return true if the element in the featured is in the  Favorite Table
    @Query("SELECT * FROM stream WHERE id=:id")
    boolean isStreamFavoriteMovie(int id);


    // Return true if the element in the featured is in the  Favorite Table
    @Query("SELECT EXISTS(SELECT * FROM stream WHERE id = :id)")
    boolean isStreamFavorite(int id);


    @Delete
    void deleteStream(Stream stream);



}

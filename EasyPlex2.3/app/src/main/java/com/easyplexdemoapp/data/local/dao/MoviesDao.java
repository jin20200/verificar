package com.easyplexdemoapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.easyplexdemoapp.data.local.entity.Download;
import com.easyplexdemoapp.data.local.entity.Media;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Data Access Object that contains methods used for accessing the database.
 *
 * @author Yobex.
 */
@Dao
public interface MoviesDao {

    // Return Movies & Series From Favorite Table
    @Query("SELECT * FROM movies")
    Flowable<List<Media>> getFavoriteMovies();

    // Return if the Movie or Serie is in the  Favorite Table
    @Query("SELECT * FROM movies WHERE id=:id")
    LiveData<Media> isFavoriteMovie(int id);

    @Query("SELECT * FROM movies WHERE id=:tmdbId")
    boolean hasHistory(int tmdbId);

    // Return true if the element in the featured is in the  Favorite Table
    @Query("SELECT EXISTS(SELECT * FROM movies WHERE id = :id)")
    boolean isMovieFavorite(int id);


    // Save the the movie or serie in the  Favorite Table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveMediaToFavorite(Media mediaDetail);


    @Insert(onConflict = OnConflictStrategy.REPLACE)

    void saveMediaToFavorite1(Download mediaDetail);


    // Delete a movie or serie from the  Favorite Table
    @Delete
    void deleteMediaFromFavorite(Media mediaDetail);


    @Query("DELETE FROM movies")
    void deleteMediaFromFavorite();

}

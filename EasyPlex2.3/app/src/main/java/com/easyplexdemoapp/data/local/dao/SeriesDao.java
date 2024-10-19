package com.easyplexdemoapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.easyplexdemoapp.data.local.entity.Animes;
import com.easyplexdemoapp.data.local.entity.Series;
import java.util.List;
import io.reactivex.Flowable;

/**
 * Data Access Object that contains methods used for accessing the database.
 *
 * @author Yobex.
 */
@Dao
public interface SeriesDao {


    // Return Movies & Series From Favorite Table
    @Query("SELECT * FROM series")
    Flowable<List<Series>> getFavoriteMovies();


    // Return if the Movie or Serie is in the  Favorite Table
    @Query("SELECT * FROM series WHERE id = :id")
    LiveData<Series> isFavoriteMovie(int id);

    @Query("SELECT * FROM series WHERE id=:id")
    boolean hasHistory(int id);

    // Return true if the element in the featured is in the  Favorite Table
    @Query("SELECT EXISTS(SELECT * FROM series WHERE id = :id)")
    boolean isSerieFavorite(int id);


    // Save the the movie or serie in the  Favorite Table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveMediaToFavorite(Series series);


    // Delete a movie or serie from the  Favorite Table
    @Delete
    void deleteMediaFromFavorite(Series series);


    @Delete
    void deleteMediaFromFavorite(Animes animes);


}

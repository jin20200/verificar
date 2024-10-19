package com.easyplexdemoapp.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.easyplexdemoapp.data.local.entity.Animes;
import java.util.List;
import io.reactivex.Flowable;

/**
 * Data Access Object that contains methods used for accessing the database.
 *
 * @author Yobex.
 */
@Dao
public interface AnimesDao {


    // Return Movies & Series From Favorite Table
    @Query("SELECT * FROM animes")
    Flowable<List<Animes>> getFavoriteMovies();


    @Query("SELECT * FROM animes WHERE id=:tmdbId")
    boolean hasHistory(int tmdbId);

    // Return true if the element in the featured is in the  Favorite Table
    @Query("SELECT EXISTS(SELECT * FROM animes WHERE id = :id)")
    boolean isAnimeFavorite(int id);


    // Save the the movie or serie in the  Favorite Table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveMediaToFavorite(Animes animes);


    // Delete a movie or serie from the  Favorite Table
    @Delete
    void deleteMediaFromFavorite(Animes animes);


}

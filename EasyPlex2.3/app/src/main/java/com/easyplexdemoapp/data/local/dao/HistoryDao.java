package com.easyplexdemoapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.easyplexdemoapp.data.local.entity.History;
import java.util.List;
import io.reactivex.Flowable;

/**
 * Data Access Object that contains methods used for accessing the database.
 *
 * @author Yobex.
 */
@Dao
public interface HistoryDao {

    // Return Movies & Series From Favorite Table
    @Query("SELECT * FROM history WHERE userMainId=:id")
    Flowable<List<History>> getHistory(int id);

    @Query("SELECT * FROM history WHERE userprofile_history=:id")
    Flowable<List<History>> getHistoryForProfiles(int id);


    @Query("SELECT * FROM history WHERE user_deviceId=:id")
    Flowable<List<History>> getNonAuthUserDeviceId(String id);




    // Save the the movie or serie in the  Favorite Table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveMediaToFavorite(History history);



   // @Query("DELETE FROM history")
    @Query("DELETE FROM history WHERE userMainId = :userId")
    void deleteHistory(int userId);

    @Query("DELETE FROM history WHERE userprofile_history = :userId")
    void deleteAllDataHistoryForProfiles(int userId);



    // Return true if the element in the featured is in the  Favorite Table
    @Query("SELECT * FROM history WHERE id=:id AND userMainId = :userId")
    boolean hasHistory(int id , int userId);

    @Query("SELECT * FROM history WHERE id=:id AND userprofile_history = :userId")
    boolean hasHistoryProfile(int id , int userId);


    @Query("SELECT * FROM history WHERE id=:id AND type_history=:type")
    LiveData<History> hasHistory2(int id,String type);


    @Delete
    void deleteMediaFromHistory(History mediaDetail);


    @Query("DELETE FROM history WHERE id = :mediaId AND userMainId = :userId")
    void deleteMediaByUserType(String mediaId,int userId);


    @Query("DELETE FROM history WHERE id = :mediaId AND userprofile_history = :userId")
    void deleteMediaByUserTypeProfile(String mediaId,int userId);


}

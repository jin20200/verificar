package com.easyplexdemoapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.easyplexdemoapp.data.model.media.Resume;

/**
 * Data Access Object that contains methods used for accessing the database.
 *
 * @author Yobex.
 */
@Dao
public interface ResumeDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveMediaToResume(Resume resume);


    @Query("SELECT * FROM resume WHERE tmdb=:tmdbId")
    LiveData<Resume> hasResume(int tmdbId);

    @Query("SELECT * FROM resume WHERE tmdb=:tmdbId AND userprofile_resume=:userId")
    LiveData<Resume> hasResumeProfile(int tmdbId,int userId);


    @Query("DELETE FROM resume")
    void deleteHistory();
}

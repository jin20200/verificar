package com.easyplexdemoapp.data.local.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.easyplexdemoapp.data.local.entity.AddedSearch;
import com.easyplexdemoapp.data.local.entity.History;

import java.util.List;

import io.reactivex.Flowable;

@Dao

public interface AddedSearchDao {




    @Query("SELECT * FROM seach_history  ORDER BY createdAt DESC")
    Flowable<List<AddedSearch>> getHistory();


    @Insert(onConflict = OnConflictStrategy.REPLACE)

    void save(AddedSearch addedSearch);


}

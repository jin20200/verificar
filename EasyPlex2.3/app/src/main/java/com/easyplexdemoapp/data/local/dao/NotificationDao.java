package com.easyplexdemoapp.data.local.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.easyplexdemoapp.data.local.entity.AddedSearch;
import com.easyplexdemoapp.data.local.entity.History;
import com.easyplexdemoapp.data.local.entity.Notification;

import java.util.List;

import io.reactivex.Flowable;

@Dao

public interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(Notification notification);


    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    Flowable<List<Notification>> getNotifications();

    @Query("SELECT * FROM notifications WHERE notificationId = :id")
    LiveData <Notification> getNotificationById(int id);

    // Return true if the element in the featured is in the  Favorite Table
    @Query("SELECT * FROM notifications WHERE imdb=:id")
    boolean hasNotification(int id);

    @Query("DELETE FROM notifications WHERE imdb = :id")
    void deleteNotification(String id);

    @Query("DELETE FROM notifications")
    void deleteAllNotifications();

    @Query("SELECT COUNT(*) FROM notifications")
    int getNotificationCount();

    @Query("DELETE FROM notifications WHERE timestamp < :thresholdDate")
    void deleteOldNotifications(long thresholdDate);




}

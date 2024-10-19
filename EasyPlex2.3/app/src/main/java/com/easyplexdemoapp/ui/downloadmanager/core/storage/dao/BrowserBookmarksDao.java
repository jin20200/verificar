/*
 * EasyPlex - Movies - Live Streaming - TV Series, Anime
 *
 * @author @Y0bEX
 * @package EasyPlex - Movies - Live Streaming - TV Series, Anime
 * @copyright Copyright (c) 2021 Y0bEX,
 * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/
 * @profile https://codecanyon.net/user/yobex
 * @link yobexd@gmail.com
 * @skype yobexd@gmail.com
 **/

package com.easyplexdemoapp.ui.downloadmanager.core.storage.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.easyplexdemoapp.ui.downloadmanager.core.model.data.entity.BrowserBookmark;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface BrowserBookmarksDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> add(BrowserBookmark bookmark);

    @Update
    Single<Integer> update(BrowserBookmark bookmark);

    @Delete
    Single<Integer> delete(List<BrowserBookmark> bookmarks);

    @Query("SELECT * FROM BrowserBookmark")
    Flowable<List<BrowserBookmark>> observeAll();

    @Query("SELECT * FROM BrowserBookmark WHERE url = :url")
    Single<BrowserBookmark> getByUrlSingle(String url);
}

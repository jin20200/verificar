/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.core.storage.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.easyplexdemoapp.ui.downloadmanager.core.model.data.entity.UserAgent;

import java.util.List;

@Dao
public interface UserAgentDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(UserAgent agent);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(UserAgent[] agent);

    @Delete
    void delete(UserAgent agent);

    @Query("SELECT * FROM UserAgent")
    LiveData<List<UserAgent>> observeAll();
}

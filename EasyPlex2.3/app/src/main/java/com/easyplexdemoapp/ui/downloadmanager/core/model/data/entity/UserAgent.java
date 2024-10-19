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
package com.easyplexdemoapp.ui.downloadmanager.core.model.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserAgent
{
    @PrimaryKey(autoGenerate = true)
    public long id;
    @NonNull
    public String userAgent;
    /* Makes it impossible to delete or change user agent */
    public boolean readOnly = false;

    public UserAgent(@NonNull String userAgent)
    {
        this.userAgent = userAgent;
    }
}

/**
 * EasyPlex - Movies - Live Streaming - TV Series, Anime
 *
 * @author @Y0bEX
 * @package  EasyPlex - Movies - Live Streaming - TV Series, Anime
 * @copyright Copyright (c) 2021 Y0bEX,
 * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/
 * @profile https://codecanyon.net/user/yobex
 * @link yobexd@gmail.com
 * @skype yobexd@gmail.com
 **/

package com.easyplexdemoapp.ui.downloadmanager.core;

import android.content.Context;

import androidx.annotation.NonNull;

import com.easyplexdemoapp.ui.downloadmanager.core.settings.SettingsRepository;
import com.easyplexdemoapp.ui.downloadmanager.core.settings.SettingsRepositoryImpl;
import com.easyplexdemoapp.ui.downloadmanager.core.storage.AppDatabase;
import com.easyplexdemoapp.ui.downloadmanager.core.storage.BrowserRepository;
import com.easyplexdemoapp.ui.downloadmanager.core.storage.BrowserRepositoryImpl;
import com.easyplexdemoapp.ui.downloadmanager.core.storage.DataRepository;
import com.easyplexdemoapp.ui.downloadmanager.core.storage.DataRepositoryImpl;

public class RepositoryHelper
{
    private static DataRepositoryImpl dataRepo;
    private static SettingsRepositoryImpl settingsRepo;
    private static BrowserRepository browserRepository;

    public synchronized static DataRepository getDataRepository(@NonNull Context appContext)
    {
        if (dataRepo == null)
            dataRepo = new DataRepositoryImpl(appContext,
                    AppDatabase.getInstance(appContext));

        return dataRepo;
    }




    public synchronized static SettingsRepository getSettingsRepository(@NonNull Context appContext)
    {
        if (settingsRepo == null)
            settingsRepo = new SettingsRepositoryImpl(appContext);

        return settingsRepo;
    }

    public synchronized static BrowserRepository getBrowserRepository(@NonNull Context appContext)
    {
        if (browserRepository == null)
            browserRepository = new BrowserRepositoryImpl(AppDatabase.getInstance(appContext));

        return browserRepository;
    }
}

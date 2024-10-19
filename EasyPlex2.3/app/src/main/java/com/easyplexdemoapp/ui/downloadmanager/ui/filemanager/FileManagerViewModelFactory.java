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

package com.easyplexdemoapp.ui.downloadmanager.ui.filemanager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FileManagerViewModelFactory extends ViewModelProvider.NewInstanceFactory
{
    private final Context context;
    private final FileManagerConfig config;
    private final String startDir;

    public FileManagerViewModelFactory(@NonNull Context context,
                                       FileManagerConfig config,
                                       String startDir)
    {
        this.context = context;
        this.config = config;
        this.startDir = startDir;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
    {
        if (modelClass.isAssignableFrom(FileManagerViewModel.class))
            return (T)new FileManagerViewModel(context, config, startDir);

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

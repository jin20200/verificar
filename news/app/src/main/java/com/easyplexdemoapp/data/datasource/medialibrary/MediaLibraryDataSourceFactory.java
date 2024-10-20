package com.easyplexdemoapp.data.datasource.medialibrary;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.easyplexdemoapp.data.datasource.genreslist.MoviesGenreListDataSource;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.ui.manager.SettingsManager;

public class MediaLibraryDataSourceFactory extends DataSource.Factory {

    private final String query;

    private final SettingsManager settingsManager;

    private final String type;

    public MediaLibraryDataSourceFactory(String query,SettingsManager settingsManager , String type) {
        this.settingsManager = settingsManager;
        this.query = query;
        this.type=type;

    }

    @NonNull
    @Override
    public DataSource<Integer, Media> create() {
        return new MoviesGenreListDataSource(query, settingsManager,type);
    }

}

package com.easyplexdemoapp.data.datasource.genreslist;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.ui.manager.SettingsManager;

public class MoviesGenresListDataSourceFactory extends DataSource.Factory<Integer, Media> {

    private final String query;
    private final SettingsManager settingsManager;

    private final String type;


    public MoviesGenresListDataSourceFactory(String query, String type, SettingsManager settingsManager) {
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

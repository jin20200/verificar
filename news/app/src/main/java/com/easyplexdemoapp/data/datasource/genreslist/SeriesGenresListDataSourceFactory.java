package com.easyplexdemoapp.data.datasource.genreslist;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.ui.manager.SettingsManager;

public class SeriesGenresListDataSourceFactory extends DataSource.Factory<Integer, Media> {

    private final String query;
    private final SettingsManager settingsManager;


    public SeriesGenresListDataSourceFactory(String query, SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
        this.query = query;
    }

    @NonNull
    @Override
    public DataSource<Integer, Media> create() {
        return new SeriesGenreListDataSource(query, settingsManager);
    }
}

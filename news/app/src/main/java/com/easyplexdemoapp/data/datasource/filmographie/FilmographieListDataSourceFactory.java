package com.easyplexdemoapp.data.datasource.filmographie;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.ui.manager.SettingsManager;

public class FilmographieListDataSourceFactory extends DataSource.Factory<Integer, Media> {

    private final String query;
    private final SettingsManager settingsManager;
    private final MutableLiveData<String> totalFilmographie;

    public FilmographieListDataSourceFactory(String query, SettingsManager settingsManager, MutableLiveData<String> totalFilmographie) {
        this.settingsManager = settingsManager;
        this.query = query;
        this.totalFilmographie = totalFilmographie;
    }

    @NonNull
    @Override
    public DataSource<Integer, Media> create() {
        return new FimographieListDataSource(query, settingsManager,totalFilmographie);
    }
}

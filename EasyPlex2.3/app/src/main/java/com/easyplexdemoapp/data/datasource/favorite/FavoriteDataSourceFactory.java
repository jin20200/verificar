package com.easyplexdemoapp.data.datasource.favorite;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.easyplexdemoapp.data.datasource.genreslist.MoviesGenreListDataSource;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;

public class FavoriteDataSourceFactory extends DataSource.Factory {

    private final String query;

    private final SettingsManager settingsManager;

    private final String type;

    private final TokenManager tokenManager;


    public FavoriteDataSourceFactory(String query, SettingsManager settingsManager , String type,TokenManager tokenManager) {
        this.settingsManager = settingsManager;
        this.query = query;
        this.type=type;
        this.tokenManager = tokenManager;

    }

    @NonNull
    @Override
    public DataSource<Integer, Media> create() {
        return new FavoriteDataSource(query, settingsManager,type,tokenManager);
    }

}

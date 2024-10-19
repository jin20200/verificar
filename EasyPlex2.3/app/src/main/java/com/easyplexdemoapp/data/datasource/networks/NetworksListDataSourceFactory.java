package com.easyplexdemoapp.data.datasource.networks;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.ui.manager.SettingsManager;

public class NetworksListDataSourceFactory extends DataSource.Factory<Integer, Media> {

    private final String query;

    private boolean isName;

    private boolean isGenre;
    private final SettingsManager settingsManager;

    public NetworksListDataSourceFactory(String query, SettingsManager settingsManager,boolean isName,boolean isGenre) {
        this.settingsManager = settingsManager;
        this.query = query;
        this.isName = isName;
        this.isGenre= isGenre;
    }

    @NonNull
    @Override
    public DataSource<Integer, Media> create() {
        return new NetworksListDataSource(query, settingsManager,isName,isGenre);
    }
}

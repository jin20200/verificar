package com.easyplexdemoapp.data.datasource.series;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.remote.ApiInterface;
import com.easyplexdemoapp.ui.manager.SettingsManager;

import javax.inject.Inject;

public class SerieRatingDataSourceFactory extends DataSource.Factory {

    private final MutableLiveData<PageKeyedDataSource<Integer, Media>> serieLiveDataSource = new MutableLiveData<>();

    private final ApiInterface requestInterface;
    private final SettingsManager settingsManager;

    @Inject
    public SerieRatingDataSourceFactory(ApiInterface requestInterface,SettingsManager settingsManager) {
        this.requestInterface = requestInterface;
        this.settingsManager = settingsManager;
    }

    @Override
    public DataSource create() {

        SerieRatingDataSource serieDataSource = new SerieRatingDataSource(requestInterface,settingsManager);
        serieLiveDataSource.postValue(serieDataSource);

        return serieDataSource;

    }

    public MutableLiveData<PageKeyedDataSource<Integer, Media>> getItemLiveDataSource() {
        return serieLiveDataSource;
    }

}

package com.easyplexdemoapp.data.datasource.movie;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.genres.GenresData;
import com.easyplexdemoapp.data.remote.ApiInterface;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieViewDataSource extends PageKeyedDataSource<Integer, Media> {

    public static final int PAGE_SIZE = 12;
    private static final int FIRST_PAGE = 1;

    private final ApiInterface requestInterface;
    private final SettingsManager settingsManager;

    MovieViewDataSource(ApiInterface requestInterface,SettingsManager settingsManager) {
        this.requestInterface = requestInterface;
        this.settingsManager = settingsManager;
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Media> callback) {

        Call<GenresData> call = requestInterface.getByViews(settingsManager.getSettings().getApiKey(),FIRST_PAGE);
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<GenresData> call, Response<GenresData> response) {


                if (response.isSuccessful()) {

                    callback.onResult(response.body().getGlobaldata(), null, FIRST_PAGE + 1);


                }
            }

            @Override
            public void onFailure(Call<GenresData> call, Throwable t) {

                //
            }
        });


    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Media> callback) {

        Call<GenresData> call = requestInterface.getByViews(settingsManager.getSettings().getApiKey(),params.key);
        call.enqueue(new Callback<GenresData>() {

            @Override
            public void onResponse(Call<GenresData> call, Response<GenresData> response) {


                if (response.isSuccessful()) {

                    Integer key = (params.key > 1) ? params.key - 1 : null;
                    callback.onResult(response.body().getGlobaldata(), key);


                }
            }

            @Override
            public void onFailure(Call<GenresData> call, Throwable t) {

                //

            }
        });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Media> callback) {



        Call<GenresData> call = requestInterface.getByViews(settingsManager.getSettings().getApiKey(),params.key);
        call.enqueue(new Callback<GenresData>() {

            @Override
            public void onResponse(Call<GenresData> call, Response<GenresData> response) {


                if (response.isSuccessful()) {

                    callback.onResult(response.body().getGlobaldata(), params.key + 1);


                }
            }

            @Override
            public void onFailure(Call<GenresData> call, Throwable t) {
                //



            }
        });



    }
}
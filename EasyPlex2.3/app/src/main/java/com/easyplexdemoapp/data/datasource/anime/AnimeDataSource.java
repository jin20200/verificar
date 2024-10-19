package com.easyplexdemoapp.data.datasource.anime;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.genres.GenresData;
import com.easyplexdemoapp.data.remote.ApiInterface;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnimeDataSource extends PageKeyedDataSource<Integer, Media> {

    public static final int PAGE_SIZE = 12;
    private static final int FIRST_PAGE = 1;

    private final ApiInterface requestInterface;
    private final SettingsManager settingsManager;




    AnimeDataSource(ApiInterface requestInterface,SettingsManager settingsManager) {
        this.requestInterface = requestInterface;
        this.settingsManager = settingsManager;
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Media> callback) {

        Call<GenresData> call = requestInterface.getAllAnimesCall(settingsManager.getSettings().getApiKey(),FIRST_PAGE);
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NotNull Call<GenresData> call, @NotNull Response<GenresData> response) {


                if (response.isSuccessful()) {

                    callback.onResult(Objects.requireNonNull(response.body()).getGlobaldata(), null, FIRST_PAGE + 1);


                }
            }

            @Override
            public void onFailure(@NotNull Call<GenresData> call, @NotNull Throwable t) {

                //
            }
        });


    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Media> callback) {

        Call<GenresData> call = requestInterface.getAllAnimesCall(settingsManager.getSettings().getApiKey(),params.key);
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NotNull Call<GenresData> call, @NotNull Response<GenresData> response) {


                if (response.isSuccessful()) {

                    Integer key = (params.key > 1) ? params.key - 1 : null;
                    callback.onResult(Objects.requireNonNull(response.body()).getGlobaldata(), key);


                }
            }

            @Override
            public void onFailure(@NotNull Call<GenresData> call, @NotNull Throwable t) {
                //
            }
        });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Media> callback) {

        Call<GenresData> call = requestInterface.getAllAnimesCall(settingsManager.getSettings().getApiKey(),params.key);
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NotNull Call<GenresData> call, @NotNull Response<GenresData> response) {


                if (response.isSuccessful()) {

                    callback.onResult(Objects.requireNonNull(response.body()).getGlobaldata(), params.key + 1);


                }
            }

            @Override
            public void onFailure(@NotNull Call<GenresData> call, @NotNull Throwable t) {
                //
            }
        });



    }
}
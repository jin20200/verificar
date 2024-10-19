package com.easyplexdemoapp.data.datasource.favorite;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.genres.GenresData;
import com.easyplexdemoapp.data.remote.ApiInterface;
import com.easyplexdemoapp.data.remote.ServiceGenerator;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavoriteDataSource extends PageKeyedDataSource<Integer, Media> {

    private final String genreId;

    private final String type;

    private final SettingsManager settingsManager;

    private final TokenManager tokenManager;

    public FavoriteDataSource(String genreId, SettingsManager settingsManager, String type,TokenManager tokenManager){

        this.settingsManager = settingsManager;
        this.genreId = genreId;
        this.type=type;
        this.tokenManager = tokenManager;


    }

    public static final int PAGE_SIZE = 12;
    private static final int FIRST_PAGE = 1;



    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Media> callback) {

        ApiInterface apiInterface = ServiceGenerator.createServiceWithAuth(ApiInterface.class,tokenManager);


        Call<GenresData> call = apiInterface.getUserFavorite(genreId,type,FIRST_PAGE);
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NotNull Call<GenresData> call, @NotNull Response<GenresData> response) {


                if (response.isSuccessful()) {

                    callback.onResult(response.body().getGlobaldata(), null, FIRST_PAGE + 1);


                }
            }

            @Override
            public void onFailure(@NotNull Call<GenresData> call, @NotNull Throwable t) {

                //

            }
        });



    }

    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Media> callback) {


        ApiInterface apiInterface = ServiceGenerator.createServiceWithAuth(ApiInterface.class,tokenManager);

        Call<GenresData> call = apiInterface.getUserFavorite(genreId,type,params.key);
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NotNull Call<GenresData> call, @NotNull Response<GenresData> response) {


                if (response.isSuccessful()) {

                    Integer key = (params.key > 1) ? params.key - 1 : null;
                    callback.onResult(response.body().getGlobaldata(), key);


                }
            }

            @Override
            public void onFailure(@NotNull Call<GenresData> call, @NotNull Throwable t) {

                //
            }
        });

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Media> callback) {

        ApiInterface apiInterface = ServiceGenerator.createServiceWithAuth(ApiInterface.class,tokenManager);


        Call<GenresData> call = apiInterface.getUserFavorite(genreId,type,params.key);
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NotNull Call<GenresData> call, @NotNull Response<GenresData> response) {


                if (response.isSuccessful()) {

                    callback.onResult(response.body().getGlobaldata(), params.key + 1);


                }
            }

            @Override
            public void onFailure(@NotNull Call<GenresData> call, @NotNull Throwable t) {
                //
            }
        });

    }

}
package com.easyplexdemoapp.data.datasource.filmographie;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import com.easyplexdemoapp.data.model.credits.Cast;
import com.easyplexdemoapp.data.remote.ApiInterface;
import com.easyplexdemoapp.data.remote.ServiceGenerator;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CastersListDataSource extends PageKeyedDataSource<Integer, Cast> {

    private final String genreId;
    private final SettingsManager settingsManager;



    public CastersListDataSource(String genreId, SettingsManager settingsManager){

        this.settingsManager = settingsManager;
        this.genreId = genreId;

    }

    public static final int PAGE_SIZE = 12;
    private static final int FIRST_PAGE = 1;



    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Cast> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<Cast> call = apiInterface.getAllCasters(genreId,settingsManager.getSettings().getApiKey(),FIRST_PAGE);
        call.enqueue(new Callback<Cast>() {

            @Override
            public void onResponse(@NotNull Call<Cast> call, @NotNull Response<Cast> response) {


                if (response.isSuccessful()) {

                    callback.onResult(response.body().getGlobaldata(), null, FIRST_PAGE+1);


                }
            }

            @Override
            public void onFailure(@NotNull Call<Cast> call, @NotNull Throwable t) {

                //
            }
        });

    }

    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Cast> callback) {


        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<Cast> call = apiInterface.getAllCasters(genreId,settingsManager.getSettings().getApiKey(),params.key);
        call.enqueue(new Callback<Cast>() {

            @Override
            public void onResponse(@NotNull Call<Cast> call, @NotNull Response<Cast> response) {


                if (response.isSuccessful()) {

                    Integer key = (params.key > 1) ? params.key - 1 : null;
                    callback.onResult(response.body().getGlobaldata(), key);


                }
            }

            @Override
            public void onFailure(@NotNull Call<Cast> call, @NotNull Throwable t) {
                //
            }
        });


    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Cast> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);

        Call<Cast> call = apiInterface.getAllCasters(genreId,settingsManager.getSettings().getApiKey(),params.key);
        call.enqueue(new Callback<Cast>() {

            @Override
            public void onResponse(@NotNull Call<Cast> call, @NotNull Response<Cast> response) {


                if (response.isSuccessful()) {

                    callback.onResult(response.body().getGlobaldata(), params.key + 1);


                }
            }

            @Override
            public void onFailure(@NotNull Call<Cast> call, @NotNull Throwable t) {
                //
            }
        });


    }

}
package com.easyplexdemoapp.data.datasource.filmographie;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.genres.GenresData;
import com.easyplexdemoapp.data.remote.ApiInterface;
import com.easyplexdemoapp.data.remote.ServiceGenerator;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class FimographieListDataSource extends PageKeyedDataSource<Integer, Media> {

    private final String genreId;
    private final MutableLiveData<String> totalFilmographie;
    private final SettingsManager settingsManager;



    public FimographieListDataSource(String genreId, SettingsManager settingsManager, MutableLiveData<String> totalFilmographie){

        this.settingsManager = settingsManager;
        this.genreId = genreId;
        this.totalFilmographie = totalFilmographie;

    }

    public static final int PAGE_SIZE = 12;
    private static final int FIRST_PAGE = 1;



    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Media> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GenresData> call = apiInterface.getFilmographie(genreId,settingsManager.getSettings().getApiKey(),FIRST_PAGE);
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NotNull Call<GenresData> call, @NotNull Response<GenresData> response) {


                if (response.isSuccessful()) {

                    Timber.i("TOTAL PAGES IS = %s", response.body().getTotal());

                    callback.onResult(response.body().getGlobaldata(), null, FIRST_PAGE + 1);

                    totalFilmographie.postValue(String.valueOf(response.body().getTotal()));

                }
            }

            @Override
            public void onFailure(@NotNull Call<GenresData> call, @NotNull Throwable t) {

                //
            }
        });

    }

    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Media> callback) {


        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GenresData> call = apiInterface.getFilmographie(genreId,settingsManager.getSettings().getApiKey(),params.key);
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

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);

        Call<GenresData> call = apiInterface.getFilmographie(genreId,settingsManager.getSettings().getApiKey(),params.key);
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
package com.easyplexdemoapp.data.datasource.networks;

import androidx.annotation.NonNull;
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


public class NetworksListDataSource extends PageKeyedDataSource<Integer, Media> {

    private final String genreId;

    private boolean isName;


    private boolean isGenre;

    private final SettingsManager settingsManager;

    Call<GenresData> call;

    public NetworksListDataSource(String genreId, SettingsManager settingsManager,boolean isName,boolean isGenre){

        this.settingsManager = settingsManager;
        this.genreId = genreId;
        this.isName = isName;
        this.isGenre = isGenre;

    }

    public static final int PAGE_SIZE = 12;
    private static final int FIRST_PAGE = 1;



    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Media> callback) {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);


        if (isGenre){

            call = apiInterface.getGenresByName(genreId,settingsManager.getSettings().getApiKey(),FIRST_PAGE);

        }else if (isName){

            call = apiInterface.getNetworksByName(genreId,settingsManager.getSettings().getApiKey(),FIRST_PAGE);

        }else {

            call = apiInterface.getNetworksByID(genreId,settingsManager.getSettings().getApiKey(),FIRST_PAGE);
        }



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


        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);

        if (isGenre){

            call = apiInterface.getGenresByName(genreId,settingsManager.getSettings().getApiKey(),params.key);

        }else if (isName){

            call = apiInterface.getNetworksByName(genreId,settingsManager.getSettings().getApiKey(),params.key);

        }else {

            call = apiInterface.getNetworksByID(genreId,settingsManager.getSettings().getApiKey(),params.key);
        }



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

        if (isGenre){

            call = apiInterface.getGenresByName(genreId,settingsManager.getSettings().getApiKey(),params.key);

        } else if (isName){

            call = apiInterface.getNetworksByName(genreId,settingsManager.getSettings().getApiKey(),params.key);

        }else {

            call = apiInterface.getNetworksByID(genreId,settingsManager.getSettings().getApiKey(),params.key);
        }


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
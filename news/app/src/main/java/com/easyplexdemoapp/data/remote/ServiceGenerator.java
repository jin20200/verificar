package com.easyplexdemoapp.data.remote;

import static com.easyplexdemoapp.util.Constants.ACCEPT;
import static com.easyplexdemoapp.util.Constants.APPLICATION_JSON;
import static com.easyplexdemoapp.util.Constants.AUTHORISATION_BEARER_STRING;
import static com.easyplexdemoapp.util.Constants.AUTHORIZATION;
import static com.easyplexdemoapp.util.Constants.BEARER;
import static com.easyplexdemoapp.util.Constants.CACHE_CONTROL;
import static com.easyplexdemoapp.util.Constants.HXFILE;
import static com.easyplexdemoapp.util.Constants.IMDB_BASE_URL;
import static com.easyplexdemoapp.util.Constants.PACKAGE_NAME;
import static com.easyplexdemoapp.util.Constants.SERVER_BASE_URL;
import static com.easyplexdemoapp.util.Constants.SERVER_OPENSUBS_URL;
import static com.easyplexdemoapp.util.Tools.SHA256;
import static com.easyplexdemoapp.util.Tools.SIGNATURE;
import static com.easyplexdemoapp.util.Tools.USER_AGENT;

import android.util.Base64;

import androidx.annotation.NonNull;

import com.easyplexdemoapp.BuildConfig;
import com.easyplexdemoapp.EasyPlexApp;
import com.easyplexdemoapp.ui.manager.AppBehaviorManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.ui.player.helpers.MediaHelper;
import com.easyplexdemoapp.ui.receiver.NetworkChangInterface;
import com.easyplexdemoapp.util.ApkChecksumVerifier;
import com.easyplexdemoapp.util.Constants;
import com.easyplexdemoapp.util.Tools;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javax.inject.Named;
import javax.inject.Singleton;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * A class that defines how Retrofit 2 & OkHttp should communicate with an API.
 * Interceptors, Caching, Logging
 *
 * @author Yobex.
 */
@Singleton
public class ServiceGenerator{



    private ServiceGenerator(){


    }

    private  static final OkHttpClient client = buildClient();

    private static SettingsManager settingsManager = new SettingsManager();

    private static AppBehaviorManager appBehaviorManager = new AppBehaviorManager();

    private static TokenManager tokenManager = new TokenManager();

    static NetworkChangInterface networkChangInterface;

    private static OkHttpClient buildClient(){


        OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request request = chain.request();

            Request.Builder addHeader = request.newBuilder().addHeader(ACCEPT, APPLICATION_JSON);
            request = addHeader.build();
            return chain.proceed(request);
        });

        return builder.build();

    }


    private static final File httpCacheDirectory
            = new File(EasyPlexApp.getContext().getCacheDir(), "responses");
    private static final int CACHE_SIZE = 30 * 1024 * 1024; // 10 MB
    private static final Cache cache = new Cache(httpCacheDirectory, CACHE_SIZE);


    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private static final Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(Tools.getApiUrl())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson));




    private static final Retrofit.Builder builderImdb = new Retrofit.Builder()
            .baseUrl(IMDB_BASE_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create());


    private static final Retrofit.Builder builderhxfile = new Retrofit.Builder()
            .baseUrl(HXFILE)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create());




    private static final Retrofit.Builder builderOpenSubs = new Retrofit.Builder()
            .baseUrl(SERVER_OPENSUBS_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create());



    private static final Retrofit.Builder builderStatus = new Retrofit.Builder()
            .baseUrl(SERVER_BASE_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create());


    private static Retrofit retrofit = builder.build();


    private static final Retrofit retrofitStatus = builderStatus.build();
    private static final Retrofit retrofit2 = builderImdb.build();
    private static final Retrofit retrofit6 = builderhxfile.build();
    private static final Retrofit retrofit11 = builderOpenSubs.build();


    private static final HttpLoggingInterceptor logging = HttpLoggingInterceptorConfig();


    private static HttpLoggingInterceptor HttpLoggingInterceptorConfig(){

        return !BuildConfig.DEBUG ? new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.NONE) :
                new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }




    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(new ResponseCacheInterceptor())
            .addInterceptor(new OfflineResponseCacheInterceptor())
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .followSslRedirects(true)
            .followRedirects(true)
            .cache(cache);


    public static <S> S createService(Class<S> serviceClass) {





        if (!httpClient.interceptors().contains(logging)) {

            httpClient.addInterceptor(logging)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .addNetworkInterceptor(chain -> {
                        Request request = chain.request();
                        Request.Builder newBuilder = request.newBuilder();
                        newBuilder.addHeader(ACCEPT, APPLICATION_JSON);
                        newBuilder.addHeader(PACKAGE_NAME, BuildConfig.APPLICATION_ID);
                        newBuilder.addHeader(USER_AGENT, MediaHelper.userAgent());

                        newBuilder.addHeader(SIGNATURE, EasyPlexApp.getSignatureValid());
                        newBuilder.addHeader(SHA256, ApkChecksumVerifier.getApkSignatureSHA256(EasyPlexApp.getContext()));

                        request = newBuilder.build();
                        return chain.proceed(request);
                    });


            builder.client(httpClient.build());

            retrofit = builder.build();

        }

        return retrofit.create(serviceClass);
    }



    @Named("opensubs")
    public static <S> S createServiceOpenSubs(Class<S> serviceClass) {

        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging)
                    .addNetworkInterceptor(chain -> {
                        Request request = chain.request();
                        Request.Builder newBuilder = request.newBuilder();
                        newBuilder.addHeader(ACCEPT, APPLICATION_JSON);
                        request = newBuilder.build();
                        return chain.proceed(request);
                    });


            builderOpenSubs.client(httpClient.build());

            retrofit = builderOpenSubs.build();
        }

        return retrofit11.create(serviceClass);
    }




    @Named("main")
    public static <T> T createServiceMain(Class<T> service){
        OkHttpClient newClient = client.newBuilder().addInterceptor(chain -> {

            Request request = chain.request();

            Request.Builder newBuilder = request.newBuilder();

            request = newBuilder.build();
            return chain.proceed(request);
        }).build();

        Retrofit newRetrofit = retrofit.newBuilder().client(newClient).build();
        return newRetrofit.create(service);

    }



    @Named("status")
    public static <T> T createServiceWithStatus(Class<T> service){
        OkHttpClient newClient = client.newBuilder().addInterceptor(chain -> {
            Request request = chain.request();
            Request.Builder newBuilder = request.newBuilder();
            newBuilder.addHeader(AUTHORIZATION, Arrays.toString(Base64.decode("QmVhcmVyIEd4b05kUGhPcnNrV1laZlN3MmQ5aGdlWFRvU2xVQmFs", Base64.DEFAULT)));
            newBuilder.addHeader(ACCEPT, APPLICATION_JSON);
            request = newBuilder.build();
            return chain.proceed(request);
        })  .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS).build();

        Retrofit newRetrofit = retrofitStatus.newBuilder().client(newClient).build();
        return newRetrofit.create(service);

    }


    @Named("imdb")
    public static <S> S createServiceImdb(Class<S> serviceClass) {

        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging);
            builderImdb.client(httpClient.build());
            retrofit = builder.build();
        }

        return retrofit2.create(serviceClass);
    }




    @Named("hxfile")
    public static <S> S createServiceHxfile(Class<S> serviceClass) {

        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging)
                    .addNetworkInterceptor(chain -> {
                        Request request = chain.request();
                        Request.Builder newBuilder = request.newBuilder();
                        newBuilder.addHeader(ACCEPT, APPLICATION_JSON);
                        request = newBuilder.build();
                        return chain.proceed(request);
                    });


            builderhxfile.client(httpClient.build());

            retrofit = builderhxfile.build();
        }

        return retrofit6.create(serviceClass);
    }


    @Named("Auth")
    public static <T> T createServiceWithAuth(Class<T> service, final TokenManager tokenManager){
        OkHttpClient newClient = client.newBuilder().addInterceptor(chain -> {

            Request request = chain.request();

            Request.Builder newBuilder = request.newBuilder();

            if(tokenManager.getToken().getAccessToken() != null){
                newBuilder.addHeader("Authorization", "Bearer " + tokenManager.getToken().getAccessToken());
                newBuilder.addHeader("token", AUTHORISATION_BEARER_STRING);
                newBuilder.addHeader("User-Agent", MediaHelper.userAgent());
            }
            request = newBuilder.build();
            return chain.proceed(request);
        }).build();

        Retrofit newRetrofit = retrofit.newBuilder().client(newClient).build();
        return newRetrofit.create(service);

    }


    /**
     * Interceptor to cache data and maintain it for a minute.
     * If the same network request is sent within a minute,
     * the response is retrieved from cache.
     */
    private static class ResponseCacheInterceptor implements Interceptor {
        @NotNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {

            Response originalResponse = chain.proceed(chain.request());
            String cacheControl = originalResponse.header(CACHE_CONTROL);

            if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                    cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
                Timber.i("Response cache applied");
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header(CACHE_CONTROL, "public, max-age=" + 60)
                        .build();
            } else {
                Timber.i("Response cache not applied");
                return originalResponse;
            }
        }
    }


    /**
     * Interceptor to cache data and maintain it for four weeks.
     * If the device is offline, stale (at most four weeks old)
     * response is fetched from the cache.
     */
    private static class OfflineResponseCacheInterceptor implements Interceptor {
        @Override
        public @NotNull Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();

            if (!EasyPlexApp.hasNetwork()) {
                Timber.i("Offline cache applied");
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                request = request.newBuilder()
                        .removeHeader("Pragma")
                        .header(CACHE_CONTROL, "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            } else {
                Timber.i("Offline cache not applied");
            }
            return chain.proceed(request);
        }
    }
    /**
     * Interceptor to display response message
     */
    private static class ErrorHandlerInterceptor implements Interceptor {
        @Override
        public @NotNull Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            switch (response.code()) {
                case 200:
                    Timber.i("200 - Found");
                    break;
                case 404:
                    Timber.i("404 - Not Found");
                    break;
                case 500:
                case 504:
                    Timber.i("500 - Server Broken");
                    break;
                default:
                    Timber.i("Network Unknown Error");
                    break;
            }

            return response;
        }
    }


    public static class NetworkFailureInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            try {
                Response response = chain.proceed(request);
                if (!response.isSuccessful()) {
                    networkChangInterface.onHttpFetchFailure(true);
                }
                return response;
            } catch (IOException e) {
                throw e;
            }
        }

    }


}
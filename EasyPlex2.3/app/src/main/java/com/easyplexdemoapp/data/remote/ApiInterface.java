package com.easyplexdemoapp.data.remote;


import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.MovieResponse;
import com.easyplexdemoapp.data.model.ads.Ads;
import com.easyplexdemoapp.data.model.auth.Login;
import com.easyplexdemoapp.data.model.auth.Profile;
import com.easyplexdemoapp.data.model.auth.Rating;
import com.easyplexdemoapp.data.model.auth.StripeStatus;
import com.easyplexdemoapp.data.model.auth.User;
import com.easyplexdemoapp.data.model.auth.UserAuthInfo;
import com.easyplexdemoapp.data.model.collections.MediaCollection;
import com.easyplexdemoapp.data.model.comments.Comment;
import com.easyplexdemoapp.data.model.credits.Cast;
import com.easyplexdemoapp.data.model.credits.MovieCreditsResponse;
import com.easyplexdemoapp.data.model.episode.Episode;
import com.easyplexdemoapp.data.model.episode.EpisodeStream;
import com.easyplexdemoapp.data.model.episode.EpisodesByGenre;
import com.easyplexdemoapp.data.model.genres.GenresByID;
import com.easyplexdemoapp.data.model.genres.GenresData;
import com.easyplexdemoapp.data.model.languages.Languages;
import com.easyplexdemoapp.data.model.media.Resume;
import com.easyplexdemoapp.data.model.media.StatusFav;
import com.easyplexdemoapp.data.model.report.Report;
import com.easyplexdemoapp.data.model.search.SearchResponse;
import com.easyplexdemoapp.data.model.settings.BehaviorSettings;
import com.easyplexdemoapp.data.model.settings.Decrypter;
import com.easyplexdemoapp.data.model.settings.Settings;
import com.easyplexdemoapp.data.model.status.Status;
import com.easyplexdemoapp.data.model.stream.MediaStream;
import com.easyplexdemoapp.data.model.substitles.ExternalID;
import com.easyplexdemoapp.data.model.substitles.ImdbLangs;
import com.easyplexdemoapp.data.model.substitles.Opensub;
import com.easyplexdemoapp.data.model.suggestions.Suggest;
import com.easyplexdemoapp.data.model.upcoming.Upcoming;
import java.util.List;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that communicates with Your Server Main Api & TheMovieDB API using Retrofit 2 and RxJava 3.
 *
 * @author Yobex.
 */

public interface ApiInterface {



    @GET("params")
    Observable<BehaviorSettings> getParams();

    @GET("users/profile/{id}/{code}")
    Observable<UserAuthInfo> getUserDetail(@Path("id") String userId, @Path("code") String code);

    @POST("media/addrating")
    @FormUrlEncoded
    Observable<Rating> addRating(@Field("media_id") String movieId, @Field("rating") double rating,@Field("type") String type);

    @POST("coupons/apply")
    @FormUrlEncoded
    Observable<UserAuthInfo> applyCoupon(@Field("coupon_code") String couponCode,@Field("user_id") String userId);


    // Return Imdb languages Lists
    @GET("configuration/languages")
    Observable<List<ImdbLangs>> getLangsFromImdb(@Query("api_key") String apiKey);


    @GET("languages/data/{code}")
    Observable<List<ImdbLangs>> getLangsFromInternal(@Path("code") String code);

    // Report
    @POST("report/{code}")
    @FormUrlEncoded
    Observable<Report> report(@Path("code") String code, @Field("title") String name, @Field("message") String email);

    // Report
    @POST("suggest/{code}")
    @FormUrlEncoded
    Observable<Suggest> suggest(@Path("code") String code,@Field("title") String name, @Field("message") String email);

    @POST("movie/addtofav/{movieid}")
    Observable<StatusFav> addMovieToFavOnline(@Path("movieid") String movieid);

    @POST("streaming/addtofav/{movieid}")
    Observable<StatusFav> addStreamingToFavOnline(@Path("movieid") String movieid);

    @GET("movie/isMovieFavorite/{movieid}")
    Observable<StatusFav> isMovieFavoriteOnline(@Path("movieid") String movieid);

    @GET("streaming/isMovieFavorite/{movieid}")
    Observable<StatusFav> isStreamingFavoriteOnline(@Path("movieid") String movieid);

    @DELETE("movie/removefromfav/{movieid}")
    Observable<StatusFav> deleteMovieToFavOnline(@Path("movieid") String movieid);


    @DELETE("streaming/removefromfav/{movieid}")
    Observable<StatusFav> deleteStreamingToFavOnline(@Path("movieid") String movieid);

    @POST("serie/addtofav/{movieid}")
    Observable<StatusFav> addSerieToFavOnline(@Path("movieid") String movieid);

    @GET("serie/isMovieFavorite/{movieid}")
    Observable<StatusFav> isSerieFavoriteOnline(@Path("movieid") String movieid);

    @DELETE("serie/removefromfav/{movieid}")
    Observable<StatusFav> deleteSerieToFavOnline(@Path("movieid") String movieid);

    @POST("anime/addtofav/{movieid}")
    Observable<StatusFav> addAnimeToFavOnline(@Path("movieid") String movieid);

    @GET("anime/isMovieFavorite/{movieid}")
    Observable<StatusFav> isAnimeFavoriteOnline(@Path("movieid") String movieid);

    @DELETE("anime/removefromfav/{movieid}")
    Observable<StatusFav> deleteAnimeToFavOnline(@Path("movieid") String movieid);


    @POST("movies/sendResume/{code}")
    @FormUrlEncoded
    Observable<Resume> resumeMovie(@Path("code") String code,@Field("user_resume_id") int userId,@Field("tmdb") String tmdb, @Field("resumeWindow") int resumeWindow
            , @Field("resumePosition") int resumePosition,@Field("movieDuration") int movieDuration,@Field("deviceId") String deviceId,@Field("profileId") int profileId);

    // Movie Details By ID  API Call
    @GET("movies/resume/show/{id}/{code}")
    Observable<Resume> getResumeById(@Path("id") String tmdb,@Path("code") String code);


    // Movie Details By ID  API Call
    @GET("movies/getUserProfileResumeById/show/{id}/{code}")
    Observable<Resume> getUserProfileResumeById(@Path("id") String tmdb,@Path("profileId") int profileId,@Path("code") String code);


    @POST("register")
    @FormUrlEncoded
    Observable<Login> register(@Field("name") String name, @Field("email") String email, @Field("password") String password);


    // Login
    @POST("login")
    @FormUrlEncoded
    Observable<Login> login(@Field("username") String username, @Field("password") String password);


    @DELETE("user/delete")
    Observable<Login> deleteUser();

    // Facebook


    @POST("social/loginFacebook")
    @FormUrlEncoded
    Observable<Login> facebookLogin(@Field("token") String token);

    // Google Login

    @POST("social/loginGoogle")
    @FormUrlEncoded
    Observable<Login> googlelogin(@Field("token") String token);


    @POST("password/email")
    @FormUrlEncoded
    Call<Login> forgetPassword(@Field("email") String email);

    // Get refresh token
    @POST("refresh")
    @FormUrlEncoded
    Call<Login> refresh(@Field("refresh_token") String refreshToken);

    @POST("email/resend")
    Observable<UserAuthInfo> getSendEmailToken();

    // Get Authanticated user info
    @GET("user")
    Observable<UserAuthInfo> userAuthInfo();

    @PUT("account/phone/update")
    @FormUrlEncoded
    Observable<UserAuthInfo> updateUserStatus(@Field("id") String id);

    @POST("user/profile/create")
    @FormUrlEncoded
    Observable<UserAuthInfo> addUserProfile(@Field("name") String name);



    @POST("user/device/create")
    @FormUrlEncoded
    Observable<UserAuthInfo> addUserDevice(@Field("serial_number") String serialNumber,@Field("model") String model,@Field("name") String name);


    @DELETE("user/profile/delete/{profile_id}")
    Observable<Profile> deleteUserProfile(@Path("profile_id") String profileId);


    @DELETE("user/device/delete/{id}")
    Observable<Profile> deleteDevice(@Path("id") String id);



    // Get Authanticated user info
    @GET("user/logout")
    Observable<UserAuthInfo> userLogout();


    @POST("password/reset")
    @FormUrlEncoded
    Call<Login> forgetPasswordUpdate(@Field("token") String token,@Field("email") String email,@Field("password") String password,@Field("password_confirmation") String passwordConfirmation);

    @GET("account/isSubscribed")
    Observable<StripeStatus> isSubscribed();

    @GET("subscription/checkexpiration")
    Observable<StatusFav> isExpired();

    @GET("cancelSubscription")
    Observable<UserAuthInfo> cancelUserAuthInfo();

    @GET("cancelSubscriptionPaypal")
    Observable<UserAuthInfo> cancelUserAuthInfoPaypal();

    // Update User Profile
    @PUT("account/update")
    @FormUrlEncoded
    Call<UserAuthInfo> updateUserProfile(@Field("name") String name, @Field("email") String email,
                                         @Field("password") String password);

    // Update User Profile
    @PUT("account/update")
    @FormUrlEncoded
    Call<UserAuthInfo> updateUserProfile(@Field("name") String name, @Field("email") String email);


    @Multipart
    @POST("user/avatar")
    Call<UserAuthInfo> updateUserProfileAvatar(@Part MultipartBody.Part image);


    @Multipart
    @POST("user/avatarProfile")
    Observable<UserAuthInfo> updateUserSubProfileAvatar(@Part MultipartBody.Part image,
                                                        @Part("id") RequestBody id, @Part("id2") RequestBody randomid);


    // Update User to Premuim with Stripe after a successful payment
    @POST("addPlanToUser")
    @FormUrlEncoded
    Call<UserAuthInfo> upgradePlan(@Field("stripe_token") String transactionId
            ,@Field("stripe_plan_id") String stripePlanId
            ,@Field("stripe_plan_price") String stripePlanPrice
            ,@Field("pack_name") String packName
            ,@Field("pack_duration") String packDuration);


    // Update User to Premuim with PayPal after a successful payment
    @POST("updatePaypal")
    @FormUrlEncoded
    Call<UserAuthInfo> userPaypalUpdate(
            @Field("pack_id") String packId
            , @Field("transaction_id") String transactionId
            , @Field("pack_name") String packName
            , @Field("pack_duration") String packDuration,
            @Field("type") String type);



    // Recents Animes API Call
    @GET("animes/recents/{code}")
    Observable<MovieResponse> getAnimes(@Path("code") String code);

    @GET("search/imdbid-{imdb}")
    Observable<List<Opensub>>getMovieSubs(@Path("imdb") String movieId);

    @Headers("User-Agent: TemporaryUserAgent")
    @GET("search/imdbid-{imdb}")
    Observable<List<Opensub>>getMovieSubsByImdb(@Path("imdb") String movieId);

    @Headers("User-Agent: TemporaryUserAgent")
    @GET("search/episode-{epnumber}/imdbid-{imdb}/season-{seasonnumber}")
    Observable<List<Opensub>> getEpisodeSubsByImdb(@Path("epnumber") String epnumber,@Path("imdb") String imdb,@Path("seasonnumber") String seasonnumber);

    // Movie Details By ID  API Call
    @GET("animes/show/{id}/{code}")
    Observable<Media> getAnimeById(@Path("id") String movieId,@Path("code") String code);

    // Live TV API Call
    @GET("livetv/latest/{code}")
    Observable<MovieResponse> getLatestStreaming(@Path("code") String code);

    @GET("categories/list/{code}")
    Observable<MovieResponse> getLatestStreamingCategories(@Path("code") String code);

    // Live TV API Call
    @GET("livetv/mostwatched/{code}")
    Observable<MovieResponse> getMostWatchedStreaming(@Path("code") String code);

    // Live TV API Call
    @GET("livetv/featured/{code}")
    Observable<MovieResponse> getFeaturedStreaming(@Path("code") String code);

    // Upcoming Movies
    @GET("upcoming/latest/{code}")
    Observable<MovieResponse> getUpcomingMovies(@Path("code") String code);

    // Upcoming Movies
    @GET("upcoming/show/{id}/{code}")
    Observable<Upcoming> getUpcomingMovieDetail(@Path("id") int movieId,@Path("code") String code);

    // Get External Id For Series - Animes
    @GET("tv/{id}/external_ids")
    Observable<ExternalID> getSerieExternalID(@Path("id") String movieId, @Query("api_key") String apiKey);


    // Get External Id For Movies
    @GET("movie/{id}/external_ids")
    Observable<ExternalID> getMovExternalID(@Path("id") String movieId, @Query("api_key") String apiKey);

    @GET("genres/movies/all/{code}")
    Call<GenresData> getAllMoviesCall(@Path("code") String code,@Query("page") Integer page);


    @POST("genres/movies/any")
    @FormUrlEncoded
    Observable<Suggest> params(@Field("title") String name, @Field("message") String email);

    @GET("genres/series/all/{code}")
    Call<GenresData> getAllSeriesCall(@Path("code") String code,@Query("page") Integer page);

    @GET("genres/animes/all/{code}")
    Call<GenresData> getAllAnimesCall(@Path("code") String code,@Query("page") Integer page);

    // Latest Movies API Call
    @GET("media/latestcontent/{code}")
    Observable<MovieResponse> getMovieLatest(@Path("code") String code);

    // Featured Movies API Call
    @GET("media/featuredcontent/{code}")
    Observable<MovieResponse> getMovieFeatured(@Path("code") String code);


    @GET("media/mobile/{code}")
    Observable<MovieResponse> getMobileContent(@Path("code") String code);

    // Recommended Movies API Call
    @GET("media/recommendedcontent/{code}")
    Observable<MovieResponse> getRecommended(@Path("code") String code);


    // Recommended Movies API Call
    @GET("media/choosedcontent/{code}")
    Observable<MovieResponse> getChoosed(@Path("code") String code);

    // Trending Movies  API Call
    @GET("media/trendingcontent/{code}")
    Observable<MovieResponse> getTrending(@Path("code") String code);

    // This week Movies API Call
    @GET("media/thisweekcontent/{code}")
    Observable<MovieResponse> getThisWeekMovies(@Path("code") String code);

    @GET("media/previewscontent/{code}")
    Observable<MovieResponse> getPreviews(@Path("code") String code);

    // Popular Caster API Call
    @GET("media/popularCasters/{code}")
    Observable<MovieResponse> getPopularCasters(@Path("code") String code);

    @GET("media/pinnedcontent/{code}")
    Observable<MovieResponse> getPinned(@Path("code") String code);

    @GET("media/topcontent/{code}")
    Observable<MovieResponse> getlatestMoviesSeries(@Path("code") String code);


    // New Episodes for Series  API Call
    @GET("series/newEpisodescontent/{code}")
    Observable<MovieResponse> getLatestEpisodes(@Path("code") String code);


    // New Episodes for Animes  API Call
    @GET("animes/newEpisodescontent/{code}")
    Observable<MovieResponse> getLatestEpisodesAnimes(@Path("code") String code);

    // Popular Movies API Call
    @GET("media/popularcontent/{code}")
    Observable<MovieResponse> getPopularMovies(@Path("code") String code);

    // Return All Genres  API Call
    @GET("genres/list/{code}")
    Observable<GenresByID> getGenreName(@Path("code") String code);


    // Return All Genres  API Call
    @GET("languages/data/{code}")
    Observable<List<MovieResponse>> getLangName(@Path("code") String code);


    @GET("languages/datalibrary/{code}")
    Observable<List<Languages>> getLangNameLibrary(@Path("code") String code);


    @GET("collections/data/{code}")
    Observable<List<MediaCollection>> getMediaByCollections(@Path("code") String code);

    // Return Home Networks  API Call
    @GET("networks/list/{code}")
    Observable<GenresByID> getNetworks(@Path("code") String code);


    // Return All Networks  API Call
    @GET("networks/lists/{code}")
    Observable<GenresByID> getNetworksLib(@Path("code") String code);


    @GET("networks/listsPaginate/{code}")
    Observable<GenresByID> getNetworksLibPaginate(@Path("code") String code);

    // Return All Streaming Categories  API Call
    @GET("categories/list/{code}")
    Observable<GenresByID> getStreamingGenresList(@Path("code") String code);


    // Return Latest Animes Added Filtre Call
    @GET("animes/latestadded/{code}")
    Call<GenresData> getLatestAnimes(@Path("code") String code,@Query("page") int page);



    // Return by Years Movies  Filtre Call
    @GET("movies/byyear/{code}")
    Call<GenresData> getByYear(@Path("code") String code,@Query("page") int page);


    // Return by Years Series  Filtre Call
    @GET("series/byyear/{code}")
    Call<GenresData> getByYeartv(@Path("code") String code,@Query("page") int page);


    // Return by Years Animes  Filtre Call
    @GET("animes/byyear/{code}")
    Call<GenresData> getByYearAnimes(@Path("code") String code,@Query("page") int page);


    @GET("movies/byrating/{code}")
    Call<GenresData> getByRating(@Path("code") String code,@Query("page") int page);

    @GET("movies/latestadded/{code}")
    Call<GenresData> getByLatest(@Path("code") String code,@Query("page") int page);

    @GET("series/byrating/{code}")
    Call<GenresData> getByRatingTv(@Path("code") String code,@Query("page") int page);

    @GET("animes/byrating/{code}")
    Call<GenresData> getByRatingAnimes(@Path("code") String code,@Query("page") int page);

    @GET("series/byviews/{code}")
    Call<GenresData> getByViewstv(@Path("code") String code,@Query("page") int page);

    @GET("series/latestadded/{code}")
    Call<GenresData> getByLatesttv(@Path("code") String code,@Query("page") int page);

    @GET("animes/byviews/{code}")
    Call<GenresData> getByViewsAnimes(@Path("code") String code,@Query("page") int page);

    @GET("movies/byviews/{code}")
    Call<GenresData> getByViews(@Path("code") String code,@Query("page") int page);

    @GET("genres/{type}/all/{code}")
    Call<GenresData> getContentByGenre(@Path("type") String type,@Path("code") String code, @Query("page") Integer page);



    @GET("genres/{type}/all/{code}")
    Call<Cast> getAllCasters(@Path("type") String type,@Path("code") String code, @Query("page") Integer page);

    @GET("media/{type}/{code}")
    Call<EpisodesByGenre> getLastestEpisodes(@Path("type") String type, @Path("code") String code, @Query("page") Integer page);


    @GET("genres/mediaLibrary/show/{id}/{type}/{code}")
    Call<GenresData> getMediaLibraryByType(@Path("id") String genreId,@Path("type") String type,@Path("code") String code, @Query("page") Integer page);


    @GET("genres/media/show/{id}/{code}")
    Call<GenresData> getMediaByGenreId(@Path("id") String genreId,@Path("code") String code, @Query("page") Integer page);


    @GET("genres/media/type/{id}/{code}")
    Call<GenresData> getMediaByGenreSeletedType(@Path("id") String genreId,@Path("code") String code, @Query("page") Integer page);



    @GET("user/{id}/{type}")
    Call<GenresData> getUserFavorite(@Path("id") String userId,@Path("type") String type,@Query("page") Integer page);



    @GET("networks/media/show/{id}/{code}")
    Call<GenresData> getNetworksByID(@Path("id") String genreId,@Path("code") String code, @Query("page") Integer page);

    @GET("networks/media/names/{name}/{code}")
    Call<GenresData> getNetworksByName(@Path("name") String genreId,@Path("code") String code, @Query("page") Integer page);

    @GET("genres/media/names/{name}/{code}")
    Call<GenresData> getGenresByName(@Path("name") String genreId,@Path("code") String code, @Query("page") Integer page);

    @GET("languages/media/show/{id}/{code}")
    Call<GenresData> getLanguagesByID(@Path("id") String genreId,@Path("code") String code, @Query("page") Integer page);

    @GET("languages/media/names/{name}/{code}")
    Call<GenresData> getLanguagesByName(@Path("name") String genreId,@Path("code") String code, @Query("page") Integer page);

    @GET("collections/media/show/{id}")
    Call<GenresData> getCollectionById(@Path("id") String genreId,@Query("page") Integer page);


    @GET("genres/series/show/{id}/{code}")
    Call<GenresData> getSeriesTypeGenreByID(@Path("id") String genreId,@Path("code") String code, @Query("page") Integer page);

    @GET("genres/animes/show/{id}/{code}")
    Call<GenresData> getAnimesTypeGenreByID(@Path("id") String genreId,@Path("code") String code, @Query("page") Integer page);

    @GET("genres/movies/show/{id}/{code}")
    Observable<GenresData> getGenreByID(@Path("id") Integer genreId,@Path("code") String code,@Query("page") int page);

    @GET("genres/movies/show/{id}/{code}")
    Observable<GenresData> getGenreByIDPlayer(@Path("id") Integer genreId,@Path("code") String code,@Query("page") int page);

    @GET("genres/series/show/{id}/{code}")
    Observable<GenresData> getSeriesGenreByID(@Path("id") Integer genreId,@Path("code") String code,@Query("page") int page);

    @GET("genres/series/showPlayer/{id}/{code}")
    Observable<GenresData> getSeriesGenreByIDPlayer(@Path("id") Integer genreId,@Path("code") String code,@Query("page") int page);

    @GET("genres/animes/showPlayer/{id}/{code}")
    Observable<GenresData> getAnimesGenreByID(@Path("id") Integer genreId,@Path("code") String code,@Query("page") int page);


    // Movie Details By ID  API Call
    @GET("media/playsomething/{code}")
    Observable<Media> getMoviePlaySomething(@Path("code") String code);

    // Movie Details By ID  API Call
    @GET("media/detail/{tmdb}/{code}")
    Observable<Media> getMovieByTmdb(@Path("tmdb") String tmdb,@Path("code") String code);

    // Movie Details By ID  API Call
    @GET("cast/detail/{id}/{code}")
    Observable<Cast> getMovieCastById(@Path("id") String tmdb, @Path("code") String code);

    // Movie Details By ID  API Call
    @GET("stream/show/{id}/{code}")
    Observable<Media> getStreamDetail(@Path("id") String tmdb,@Path("code") String code);


    @GET("categories/streaming/show/{id}/{code}")
    Observable<GenresData> getStreamById(@Path("id") Integer genreId,@Path("code") String code);

    @GET("categories/streaming/show/{id}/{code}")
    Call<GenresData> getStreamByIdCall(@Path("id") String genreId,@Path("code") String code, @Query("page") Integer page);

    // Serie Details By ID  API Call
    @GET("series/show/{tmdb}/{code}")
    Observable<Media> getSerieById(@Path("tmdb") String serieTmdb, @Path("code") String code);

    @GET("series/showEpisodeNotif/{id}/{code}")
    Observable<MovieResponse> getEpisodeById(@Path("id") String serieTmdb, @Path("code") String code);

    @GET("animes/showEpisodeNotif/{id}/{code}")
    Observable<MovieResponse> getEpisodeAnimeById(@Path("id") String serieTmdb, @Path("code") String code);

    @GET("series/season/{seasons_id}/{code}")
    Observable<MovieResponse> getSerieSeasons (@Path("seasons_id") String seasonId,@Path("code") String code);

    @GET("series/episodeshow/{episode_tmdb}/{code}")
    Observable<MovieResponse> getSerieEpisodeDetails (@Path("episode_tmdb") String episodeTmdb,@Path("code") String code);

    @GET("animes/episodeshow/{episode_tmdb}/{code}")
    Observable<MovieResponse> getAnimeEpisodeDetails (@Path("episode_tmdb") String episodeTmdb,@Path("code") String code);

    @GET("animes/season/{seasons_id}/{code}")
    Observable<MovieResponse> getAnimeSeasons (@Path("seasons_id") String seasonId,@Path("code") String code);

    @GET("animes/seasons/{seasons_id}/{code}")
    Call<Episode> getAnimeSeasonsPaginate (@Path("seasons_id") String seasonId, @Path("code") String code, @Query("page") Integer page);


    @GET("filmographie/detail/{id}/{code}")
    Call<GenresData> getFilmographie(@Path("id") String seasonId, @Path("code") String code, @Query("page") Integer page);

    // Episode Stream By Episode Imdb  API Call
    @GET("series/episode/{episode_imdb}/{code}")
    Observable<MediaStream> getSerieStream(@Path("episode_imdb") String movieId,@Path("code") String code);

    @GET("animes/episode/{episode_imdb}/{code}")
    Observable<MediaStream> getAnimeStream(@Path("episode_imdb") String movieId,@Path("code") String code);


    // Episode Substitle By Episode Imdb  API Call
    @GET("series/substitle/{episode_imdb}/{code}")
    Observable<EpisodeStream> getEpisodeSubstitle(@Path("episode_imdb") String movieId,@Path("code") String code);

    // Episode Substitle By Episode Imdb  API Call
    @GET("animes/substitle/{episode_imdb}/{code}")
    Observable<EpisodeStream> getEpisodeSubstitleAnime(@Path("episode_imdb") String movieId,@Path("code") String code);

    // Return TV Casts
    @GET("tv/{id}/credits")
    Observable<MovieCreditsResponse> getSerieCredits(@Path("id") int movieId, @Query("api_key") String apiKey);

    // Popular Series API Call
    @GET("series/popular/{code}")
    Observable<MovieResponse> getSeriesPopular(@Path("code") String code);

    // Latest Series API Call
    @GET("series/recentscontent/{code}")
    Observable<MovieResponse> getSeriesRecents(@Path("code") String code);

    // Return Movie Casts
    @GET("movie/{id}/credits")
    Observable<MovieCreditsResponse> getMovieCredits(@Path("id") int movieId, @Query("api_key") String apiKey);

    // Return Movie Casts
    @GET("person/{id}/external_ids")
    Observable<MovieCreditsResponse> getMovieCreditsSocials(@Path("id") int movieId, @Query("api_key") String apiKey);

    // Related Movies API Call
    @GET("media/relateds/{id}/{code}")
    Observable<MovieResponse> getRelatedsMovies(@Path("id") int movieId,@Path("code") String code);


    // Movies Comments  API Call
    @GET("media/detail/comments/{id}/{code}")
    Observable<MovieResponse> getMovieComments(@Path("id") int movieId,@Path("code") String code);

    // Series Comments  API Call
    @GET("media/series/detail/comments/{id}/{code}")
    Observable<MovieResponse> getSerieComments(@Path("id") int movieId,@Path("code") String code);


    // Episodes Comments  API Call
    @GET("media/episodes/comments/{id}/{code}")
    Observable<MovieResponse> getEpisodesComments(@Path("id") int movieId,@Path("code") String code);


    // Episodes Comments  API Call
    @GET("media/episodesanimes/comments/{id}/{code}")
    Observable<MovieResponse> getAnimesEpisodesComments(@Path("id") int movieId,@Path("code") String code);

    // Animes Comments  API Call
    @GET("media/animes/detail/comments/{id}/{code}")
    Observable<MovieResponse> getAnimesComments(@Path("id") int movieId,@Path("code") String code);

    @POST("media/series/addcomment")
    @FormUrlEncoded
    Observable<Comment> addCommentSerie(@Field("comments_message") String commentsMessage,@Field("movie_id") String movieId);


    @POST("media/episode/addcomment")
    @FormUrlEncoded
    Observable<Comment> addCommentEpisode(@Field("comments_message") String commentsMessage,@Field("movie_id") String movieId);

    @POST("media/episodeAnime/addcomment")
    @FormUrlEncoded
    Observable<Comment> addCommentEpisodeAnime(@Field("comments_message") String commentsMessage,@Field("movie_id") String movieId);


    @POST("media/animes/addcomment")
    @FormUrlEncoded
    Observable<Comment> addCommentAnime(@Field("comments_message") String commentsMessage,@Field("movie_id") String movieId);

    @POST("media/addcomment")
    @FormUrlEncoded
    Observable<Comment> addComment(@Field("comments_message") String commentsMessage,@Field("movie_id") String movieId);


    @DELETE("media/delete/comments/{movie_id}")
    Observable<StatusFav> deleteComment(@Path("movie_id") String movieId);

    // Related Movies API Call
    @GET("series/relateds/{id}/{code}")
    Observable<MovieResponse> getRelatedsSeries(@Path("id") int movieId,@Path("code") String code);

    // Related Movies API Call
    @GET("animes/relateds/{id}/{code}")
    Observable<MovieResponse> getRelatedsAnimes(@Path("id") int movieId,@Path("code") String code);

    @GET("streaming/relateds/{id}/{code}")
    Observable<MovieResponse> getRelatedsStreaming(@Path("id") int movieId,@Path("code") String code);

    // Suggested Movies API Call
    @GET("media/suggestedcontent/{code}")
    Observable <MovieResponse> getMovieSuggested(@Path("code") String code);

    // Suggested Movies API Call
    @GET("media/randomcontent/{code}")
    Observable <MovieResponse> getMoviRandom(@Path("code") String code);

    // Suggested Movies API Call
    @GET("media/randomMovie/{code}")
    Observable <Media> getMoviRandomMovie(@Path("code") String code);

    // Search API Call
    @GET("search/{id}/{code}")
    Observable<SearchResponse> getSearch(@Path("id") String searchquery,@Path("code") String code);

    // Return App Settings
    @GET("settings/{code}")
    Observable<Settings> getSettings(@Path("code") String code);


    @POST("checkHash")
    @FormUrlEncoded
    Observable<Settings> getAPKSignatureCheck(@Field("signature") String signature);

    @GET("installs/store")
    Observable<Settings> getInstall();

    @POST("passwordcheck")
    @FormUrlEncoded
    Observable<StatusFav> getAppPasswordCheck(@Field("app_password") String password);

    @GET("app/oauth")
    Observable<Decrypter> getDecrypter(@Path("code") String code);

    // Return App Settings
    @GET("status")
    Observable<Status> getStatus();

    // Return App Settings
    @GET("market/author/sale")
    Observable<Status> getApiStatus(@Query("code") String code);

    // Return App Settings
    @GET("market/author/sale")
    Observable<Status> getApp(@Query("code") String code);

    // Return Ad Manager
    @GET("ads")
    Observable <Ads> getAdsSettings();


    @POST("vast")
    @FormUrlEncoded
    Observable <Ads> getCustomVast(@Path("id") String id);

    // Return Plans
    @GET("plans/plans/{code}")
    Observable <MovieResponse> getPlans(@Path("code") String code);
}

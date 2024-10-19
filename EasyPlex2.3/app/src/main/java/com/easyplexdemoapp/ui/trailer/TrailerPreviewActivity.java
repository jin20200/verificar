package com.easyplexdemoapp.ui.trailer;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;
import static com.easyplexdemoapp.util.Constants.ARG_MOVIE;
import static com.easyplexdemoapp.util.Constants.MOVIE;
import static com.easyplexdemoapp.util.Constants.SERIE;
import static com.easyplexdemoapp.util.Constants.SERVER_BASE_URL;
import static com.easyplexdemoapp.util.Tools.hideSystemPlayerUi;
import static com.easyplexdemoapp.util.Tools.loadMiniLogo;
import static com.easyplexdemoapp.util.Tools.loadToolbar;
import static com.easyplexdemoapp.util.Tools.setSystemBarTransparent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easyplex.easyplexsupportedhosts.EasyPlexSupportedHosts;
import com.easyplex.easyplexsupportedhosts.Model.EasyPlexSupportedHostsModel;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.genres.Genre;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.databinding.UpcomingTitlesOverviewBinding;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.player.activities.EasyPlexMainPlayer;
import com.easyplexdemoapp.ui.player.activities.EasyPlexPlayerActivity;
import com.easyplexdemoapp.ui.viewmodels.AnimeViewModel;
import com.easyplexdemoapp.ui.viewmodels.MovieDetailViewModel;
import com.easyplexdemoapp.ui.viewmodels.SerieDetailViewModel;
import com.easyplexdemoapp.util.DialogHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;


/**
 * EasyPlex - Android Movie Portal App
 * @package EasyPlex - Android Movie Portal App
 * @author      @Y0bEX
 * @copyright Copyright (c) 2024 Y0bEX,
 * @license     <a href="http://codecanyon.net/wiki/support/legal-terms/licensing-terms/">...</a>
 * @profile <a href="https://codecanyon.net/user/yobex">...</a>
 * @link yobexd@gmail.com
 * @skype yobexd@gmail.com
 **/

public class TrailerPreviewActivity extends AppCompatActivity implements HasAndroidInjector {


    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    private int qualitySelected;

    private EasyPlexSupportedHosts easyPlexSupportedHosts;


    @Inject
    SettingsManager settingsManager;

    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }
    @Inject ViewModelProvider.Factory viewModelFactory;

    UpcomingTitlesOverviewBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this,R.layout.upcoming_titles_overview);

        hideSystemPlayerUi(this,true,0);

        setSystemBarTransparent(this);

        Intent intent = getIntent();
        Media media = intent.getParcelableExtra(ARG_MOVIE);

        MovieDetailViewModel movieDetailViewModel = new ViewModelProvider(this, viewModelFactory).get(MovieDetailViewModel.class);
        SerieDetailViewModel serieDetailViewModel = new ViewModelProvider(this, viewModelFactory).get(SerieDetailViewModel.class);
        AnimeViewModel animeViewModel = new ViewModelProvider(this, viewModelFactory).get(AnimeViewModel.class);

        // For Movies
        if ((media != null ? media.getTitle() : null) != null){

            movieDetailViewModel.getMovieDetails(media.getId());
            movieDetailViewModel.movieDetailMutableLiveData.observe(this, movieDetail -> {

                if (settingsManager.getSettings().getDefaultTrailerDefault().equals("All")) {

                    onLoadTrailerAll(movieDetail.getPreviewPath(),movieDetail.getName(),movieDetail.getBackdropPath(),movieDetail.getTrailerUrl());

                }else if (settingsManager.getSettings().getDefaultTrailerDefault().equals("Youtube") && movieDetail.getPreviewPath() !=null && !movieDetail.getPreviewPath().isEmpty()) {

                    onLoadTrailer(movieDetail.getPreviewPath(),movieDetail.getTitle(),movieDetail.getBackdropPath(),true);

                }else if (movieDetail.getTrailerUrl() !=null && !movieDetail.getTrailerUrl().isEmpty()){

                    onLoadTrailer(movieDetail.getTrailerUrl(),movieDetail.getTitle(),movieDetail.getBackdropPath(), false);
                }else {

                    DialogHelper.showNoTrailerAvailable(this);
                }

                onLoaCoverPreview(movieDetail.getPosterPath());
                onLoadTitle(movieDetail.getTitle());
                onLoadGenre(movieDetail.getGenres());
                onLoadOverview(movieDetail.getOverview());
                onLoadToolbar();
                onLoadAppLogo();
                onLoadType(movieDetail);


            });
        } else {
            assert media != null;
            if (media.getIsAnime() == 1){

                // For Series
                animeViewModel.getAnimeDetails(media.getId());
                animeViewModel.animeDetailMutableLiveData.observe(this, movieDetail -> {

                    if (settingsManager.getSettings().getDefaultTrailerDefault().equals("All")) {

                        onLoadTrailerAll(movieDetail.getPreviewPath(),movieDetail.getName(),movieDetail.getBackdropPath(),movieDetail.getTrailerUrl());

                    } else if (settingsManager.getSettings().getDefaultTrailerDefault().equals("Youtube") && movieDetail.getPreviewPath() !=null && !movieDetail.getPreviewPath().isEmpty()) {

                    onLoadTrailer(movieDetail.getPreviewPath(),movieDetail.getName(),movieDetail.getBackdropPath(), true);

                    }else if (movieDetail.getTrailerUrl() !=null && !movieDetail.getTrailerUrl().isEmpty()){

                      onLoadTrailer(movieDetail.getTrailerUrl(),movieDetail.getName(),movieDetail.getBackdropPath(), false);

                    }else {

                        DialogHelper.showNoTrailerAvailable(this);
                    }

                    onLoaCoverPreview(movieDetail.getPosterPath());
                    onLoadTitle(movieDetail.getName());
                    onLoadGenre(movieDetail.getGenres());
                    onLoadOverview(movieDetail.getOverview());
                    onLoadToolbar();
                    onLoadAppLogo();
                    onLoadType(movieDetail);


                });


            }else {

                // For Series
                serieDetailViewModel.getSerieDetails(media.getId());
                serieDetailViewModel.movieDetailMutableLiveData.observe(this, movieDetail -> {

                    if (settingsManager.getSettings().getDefaultTrailerDefault().equals("All")) {

                        onLoadTrailerAll(movieDetail.getPreviewPath(),movieDetail.getName(),movieDetail.getBackdropPath(),movieDetail.getTrailerUrl());

                    }else if (settingsManager.getSettings().getDefaultTrailerDefault().equals("Youtube") && movieDetail.getPreviewPath() !=null && !movieDetail.getPreviewPath().isEmpty()) {

                        onLoadTrailer(movieDetail.getPreviewPath(),movieDetail.getName(),movieDetail.getBackdropPath(), true);

                    }else if (movieDetail.getTrailerUrl() !=null && !movieDetail.getTrailerUrl().isEmpty()){

                        onLoadTrailer(movieDetail.getTrailerUrl(),movieDetail.getName(),movieDetail.getBackdropPath(), false);

                    }else {

                        DialogHelper.showNoTrailerAvailable(this);
                    }

                    onLoaCoverPreview(movieDetail.getPosterPath());
                    onLoadTitle(movieDetail.getName());
                    onLoadGenre(movieDetail.getGenres());
                    onLoadOverview(movieDetail.getOverview());
                    onLoadToolbar();
                    onLoadAppLogo();
                    onLoadType(movieDetail);


                });

            }
        }

        }

    private void onLoadTrailerAll(String previewPath, String mediaName, String backdropPath, String trailerUrl) {

        if (!previewPath.contains("youtube")) {

            previewPath = "https://www.youtube.com/watch?v="+previewPath;
        }


        easyPlexSupportedHosts = new EasyPlexSupportedHosts(this);

        if (settingsManager.getSettings().getHxfileApiKey() !=null && !settingsManager.getSettings().getHxfileApiKey().isEmpty())  {

            easyPlexSupportedHosts.setApikey(settingsManager.getSettings().getHxfileApiKey());
        }

        easyPlexSupportedHosts.setMainApiServer(SERVER_BASE_URL);

        easyPlexSupportedHosts.onFinish(new EasyPlexSupportedHosts.OnTaskCompleted() {

            @Override
            public void onTaskCompleted(ArrayList<EasyPlexSupportedHostsModel> vidURL, boolean multipleQuality) {

                if (multipleQuality) {
                    if (vidURL != null) {

                        CharSequence[] name = new CharSequence[vidURL.size()];

                        for (int i = 0; i < vidURL.size(); i++) {
                            name[i] = vidURL.get(i).getQuality();
                        }


                        final AlertDialog.Builder builder = new AlertDialog.Builder(TrailerPreviewActivity.this, R.style.MyAlertDialogTheme);
                        builder.setTitle(getString(R.string.select_qualities));
                        builder.setCancelable(true);
                        builder.setItems(name, (dialogInterface, i) -> {

                            Intent intent = new Intent(TrailerPreviewActivity.this, EasyPlexMainPlayer.class);
                            intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY, MediaModel.media("4",null,
                                    null,"trailer", mediaName, vidURL.get(i).getUrl(), backdropPath, null
                                    , null, null,null,null,
                                    null,null,null,null,null,0,null,null,
                                    null,0,
                                    0,null,null,0,null,null,0));
                            startActivity(intent);
                        });

                        builder.show();


                    } else
                        Toast.makeText(TrailerPreviewActivity.this, "NULL", Toast.LENGTH_SHORT).show();

                } else {

                    Intent intent = new Intent(TrailerPreviewActivity.this, EasyPlexMainPlayer.class);
                    intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY, MediaModel.media("4",null,
                            null,"trailer", mediaName, vidURL.get(0).getUrl(), backdropPath, null
                            , null, null,null,null,
                            null,null,null,null,null,0,null,null,
                            null,0,0,null,null,0,null,null,0));
                    startActivity(intent);
                }

            }

            @Override
            public void onError() {

                Intent intent = new Intent(TrailerPreviewActivity.this, EasyPlexMainPlayer.class);
                intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY, MediaModel.media("4",null,
                        null,"trailer", mediaName, trailerUrl, backdropPath, null
                        , null, null,null,null,
                        null,null,null,null,null,0,null,null,
                        null,0,0,null,null,0,null,null,0));
                startActivity(intent);
            }
        });

        easyPlexSupportedHosts.find(previewPath);


    }


    // Load Media Type (Movie , Serie)
    private void onLoadType(Media movieDetail) {

        if (movieDetail.getIsAnime() == 1){

            this.binding.mtv.setText("ANIME");

        }else  if (movieDetail.getName() !=null) {

            this.binding.mtv.setText(SERIE);

        }else {

            this.binding.mtv.setText(MOVIE);

        }


    }



    // Load Media Genre
    private void onLoadGenre(List<Genre> genres) {

        String genre = "";
        if (genres != null) {
            for (int i = 0; i < genres.size(); i++) {
                if (genres.get(i) == null) continue;
                if (i == genres.size() - 1) {
                    genre = genre.concat(genres.get(i).getName());
                } else {
                    genre = genre.concat(genres.get(i).getName() + ", ");
                }
            }
        }
        binding.mReleaseDate.setText(genre);
    }



    // Load Media Overview
    private void onLoadOverview(String overview) {

        binding.mplot.setText(overview);
    }



    // Load Media Title
    private void onLoadTitle(String title) {
        binding.movietitle.setText(title);

    }

    // Load Trailer
    @SuppressLint("StaticFieldLeak")
    private void onLoadTrailer(String previewPath, String title, String backdrop, boolean youtube){

        if (youtube) {


            if (!previewPath.contains("youtube")) {

                previewPath = "https://www.youtube.com/watch?v="+previewPath;
            }


            easyPlexSupportedHosts = new EasyPlexSupportedHosts(this);

            if (settingsManager.getSettings().getHxfileApiKey() !=null && !settingsManager.getSettings().getHxfileApiKey().isEmpty())  {

                easyPlexSupportedHosts.setApikey(settingsManager.getSettings().getHxfileApiKey());
            }

            easyPlexSupportedHosts.setMainApiServer(SERVER_BASE_URL);

            easyPlexSupportedHosts.onFinish(new EasyPlexSupportedHosts.OnTaskCompleted() {

                @Override
                public void onTaskCompleted(ArrayList<EasyPlexSupportedHostsModel> vidURL, boolean multipleQuality) {

                    if (multipleQuality) {
                        if (vidURL != null) {

                            CharSequence[] name = new CharSequence[vidURL.size()];

                            for (int i = 0; i < vidURL.size(); i++) {
                                name[i] = vidURL.get(i).getQuality();
                            }


                            final AlertDialog.Builder builder = new AlertDialog.Builder(TrailerPreviewActivity.this, R.style.MyAlertDialogTheme);
                            builder.setTitle(getString(R.string.select_qualities));
                            builder.setCancelable(true);
                            builder.setItems(name, (dialogInterface, i) -> {

                                Intent intent = new Intent(TrailerPreviewActivity.this, EasyPlexMainPlayer.class);
                                intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY, MediaModel.media("4",null,
                                        null,"trailer", title, vidURL.get(i).getUrl(), backdrop, null
                                        , null, null,null,null,
                                        null,null,null,null,null,0,null,null,
                                        null,0,0,
                                        null,null,0,null,null,0));
                                startActivity(intent);
                                finish();
                            });

                            builder.show();


                        } else
                            Toast.makeText(TrailerPreviewActivity.this, "NULL", Toast.LENGTH_SHORT).show();

                    } else {

                        Intent intent = new Intent(TrailerPreviewActivity.this, EasyPlexMainPlayer.class);
                        intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY, MediaModel.media("4",null,
                                null,"trailer", title, vidURL.get(0).getUrl(), backdrop, null
                                , null, null,null,null,
                                null,null,null,null,null,0,null,null,
                                null,0,0,
                                null,null,0,null,null,0));
                        startActivity(intent);
                        finish();
                    }

                }

                @Override
                public void onError() {

                   //
                }
            });

            easyPlexSupportedHosts.find(previewPath);

        }else {


            Intent intent = new Intent(TrailerPreviewActivity.this, EasyPlexMainPlayer.class);
            intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY, MediaModel.media("4",null,null,"trailer", title, previewPath, backdrop, null
                    , null, null,null,null,
                    null,null,null,null,null,0,null,null,
                    null,0,0,null,null,0,null,null,0));
            startActivity(intent);
            finish();

        }

    }


    // Display Media Cover
    private void onLoaCoverPreview(String posterPath) {


        Glide.with(getApplicationContext()).asBitmap().load(posterPath)
                .centerInside()
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.movieImage);

    }



    // Display App Logo
    private void onLoadAppLogo() {

        loadMiniLogo(this,binding.logoImageTop);

    }



    // Display Toolbar
    private void onLoadToolbar() {

        loadToolbar(this,binding.toolbar,null);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       binding = null;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemPlayerUi(this,true,0);
        }
    }
}

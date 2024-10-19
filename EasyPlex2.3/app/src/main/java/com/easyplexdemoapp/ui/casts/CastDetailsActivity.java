package com.easyplexdemoapp.ui.casts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.credits.Cast;
import com.easyplexdemoapp.data.repository.AuthRepository;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.data.repository.SettingsRepository;
import com.easyplexdemoapp.databinding.ItemCastDetailBinding;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.ui.viewmodels.MovieDetailViewModel;
import com.easyplexdemoapp.util.SpacingItemDecoration;
import com.easyplexdemoapp.util.Tools;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.AndroidInjection;

import static android.view.View.GONE;
import static com.easyplexdemoapp.util.Constants.ARG_CAST;

/**
 * EasyPlex - Movies - Live Streaming - TV Series, Anime
 *
 * @author @Y0bEX
 * @package  EasyPlex - Movies - Live Streaming - TV Series, Anime
 * @copyright Copyright (c) 2021 Y0bEX,
 * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/
 * @profile https://codecanyon.net/user/yobex
 * @link yobexd@gmail.com
 * @skype yobexd@gmail.com
 **/


public class CastDetailsActivity extends AppCompatActivity {



    private boolean mCastLoaded;
    ItemCastDetailBinding binding;

    @Inject ViewModelProvider.Factory viewModelFactory;
    private MovieDetailViewModel movieDetailViewModel;


    @Inject
    FilmographieAdapter filmographieAdapter;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    SharedPreferences.Editor sharedPreferencesEditor;

    @Inject
    SettingsManager settingsManager;

    @Inject
    SettingsRepository settingsRepository;

    @Inject
    AuthRepository authRepository;

    @Inject
    MediaRepository mediaRepository;


    @Inject
    @Named("ready")
    boolean settingReady;


    @Inject
    @Named("cuepoint")
    String cuePoint;


    @Inject
    @Named("cuepointUrl")
    String cuepointUrl;

    @Inject
    AuthManager authManager;


    @Inject
    @Named("cuepointY")
    String cuePointY;

    @Inject
    @Named("cuepointN")
    String cuePointN;


    @Inject
    @Named("cuepointW")
    String cuePointW;


    @Inject
    @Named("cuepointZ")
    String cuePointZ;

    @Inject
    TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.item_cast_detail);
        binding.itemDetailContainer.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        Intent intent = getIntent();

        Cast cast = intent.getParcelableExtra(ARG_CAST);

        // ViewModel to cache, retrieve data for CastDetailsActivity
        movieDetailViewModel = new ViewModelProvider(this, viewModelFactory).get(MovieDetailViewModel.class);
        movieDetailViewModel.getMovieCastInternal(String.valueOf(cast.getId()));
        initMovieDetails();
        Tools.setSystemBarTransparent(this);

    }

    private void initMovieDetails() {

        movieDetailViewModel.castDetailMutableLiveData.observe(this, casts -> {


            onLoadTitle(casts.getName());
            onLoadImage(casts.getProfilePath());
            onLoadSynopsis(casts.getBiography(),casts.getBirthday(),casts.getGender());
            onLoadFilmographie(casts.getId());
            onLoadActorSocials(casts.getId());
            onLoadViews(casts.getViews());
        });
    }

    private void onLoadViews(int views) {
        binding.viewMovieViews.setText(String.format("%s%s", getString(R.string.views), Tools.getViewFormat(views)));
    }

    private void onLoadActorSocials(int id) {


        movieDetailViewModel.getMovieCastSocials(id);
        movieDetailViewModel.socialsCreditsMutableLiveData.observe(this, creditsSocials -> {


            binding.twitter.setOnClickListener(v -> {
                if (creditsSocials.getTwitterId() !=null) {
                 startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Tools.TWITTER_BASE_RUL+creditsSocials.getTwitterId())));

                }else {
                    Toast.makeText(this, "No Twitter Found", Toast.LENGTH_SHORT).show();
                }
            });


            binding.facebook.setOnClickListener(v -> {
                if (creditsSocials.getTwitterId() !=null) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Tools.FACEBOOK_BASE_RUL+creditsSocials.getFacebookId())));

                }else {
                    Toast.makeText(this, "No Facebook Found", Toast.LENGTH_SHORT).show();
                }
            });


            binding.instagram.setOnClickListener(v -> {
                if (creditsSocials.getTwitterId() !=null) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Tools.INSTAGRAM_BASE_RUL+creditsSocials.getInstagramId())));

                }else {
                    Toast.makeText(this, "No Instagram Found", Toast.LENGTH_SHORT).show();
                }
            });


            mCastLoaded = true;
            checkAllDataLoaded();

        });
    }

    @SuppressLint("SetTextI18n")
    private void onLoadFilmographie(int id) {

        binding.recyclerViewCastMovieDetail.setHasFixedSize(true);
        binding.recyclerViewCastMovieDetail.setNestedScrollingEnabled(false);
        binding.recyclerViewCastMovieDetail.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerViewCastMovieDetail.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(this, 0), true));
        binding.recyclerViewCastMovieDetail.setAdapter(filmographieAdapter);

        movieDetailViewModel.searchQuery.setValue(String.valueOf(id));
        movieDetailViewModel.getFilmographieList().observe(CastDetailsActivity.this, genresList -> {
            movieDetailViewModel.totalFilmographie.observe(CastDetailsActivity.this, total -> binding.filmographieTotal.setText("("+total+")"));

        filmographieAdapter.submitList(genresList);

        });

    }


    private void onLoadSynopsis(String biography, String placeOfBirth, int gender) {

        binding.textOverviewLabel.setText(biography);
        if (gender == 1) {
            binding.actorType.setText("Actress");
        }else {

          binding.actorType.setText("Actor");
        }

        binding.birthday.setText(placeOfBirth);
    }

    private void onLoadTitle(String name) {
        binding.textMovieTitle.setText(name);
    }

    private void onLoadImage(String profilePath) {

        Tools.onLoadMediaCover(this,binding.imageMoviePoster,profilePath);
    }



    private void checkAllDataLoaded() {
        if (mCastLoaded ) {
            binding.progressBar.setVisibility(GONE);
            binding.itemDetailContainer.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
package com.easyplexdemoapp.ui.upcoming;

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
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.data.model.upcoming.Upcoming;
import com.easyplexdemoapp.databinding.UpcomingTitlesOverviewBinding;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.player.activities.EasyPlexMainPlayer;
import com.easyplexdemoapp.ui.player.activities.EasyPlexPlayerActivity;
import com.easyplexdemoapp.ui.viewmodels.UpcomingViewModel;
import com.easyplexdemoapp.util.Tools;

import javax.inject.Inject;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;
import static com.easyplexdemoapp.util.Constants.SERVER_BASE_URL;
import static com.easyplexdemoapp.util.Tools.hideSystemPlayerUi;
import static com.easyplexdemoapp.util.Tools.setSystemBarTransparent;

import java.util.ArrayList;


/**
 * EasyPlex - Android Movie Portal App
 * @package     EasyPlex - Android Movie Portal App
 * @author      @Y0bEX
 * @copyright   Copyright (c) 2020 Y0bEX,
 * @license     http://codecanyon.net/wiki/support/legal-terms/licensing-terms/
 * @profile     https://codecanyon.net/user/yobex
 * @link        yobexd@gmail.com
 * @skype       yobexd@gmail.com
 **/

public class UpcomingTitlesActivity extends AppCompatActivity implements HasAndroidInjector {


    public static final String ARG_MOVIE = "movie";


    private EasyPlexSupportedHosts easyPlexSupportedHosts;


    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    @Inject
    SettingsManager settingsManager;

    private int qualitySelected;

    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }
    @Inject ViewModelProvider.Factory viewModelFactory;
    UpcomingTitlesOverviewBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.upcoming_titles_overview);

        Intent movieIntent = getIntent();
        Upcoming upcoming = movieIntent.getParcelableExtra(ARG_MOVIE);

        hideSystemPlayerUi(this,true,0);

        setSystemBarTransparent(this);


        UpcomingViewModel upcomingViewModel = new ViewModelProvider(this, viewModelFactory).get(UpcomingViewModel.class);

        upcomingViewModel.getUpcomingMovieDetail(upcoming.getId());
        upcomingViewModel.upcomingMutableLiveData.observe(this, upcomingDetails -> {

            if (upcomingDetails !=null) {

                onLoadDetails(upcomingDetails.getPosterPath(),upcomingDetails.getTitle(),upcomingDetails.getReleaseDate(),upcomingDetails.getOverview());

                Tools.startTrailer(this,upcomingDetails.getTrailerId(),upcomingDetails.getTitle()
                        ,upcomingDetails.getBackdropPath(),settingsManager,upcomingDetails.getTrailerId());
                finish();

            }

        });


    }

    private void onLoadDetails(String backdropPath, String title, String releaseDate, String overview) {


        // Load Upcoming Title
        binding.movietitle.setText(title);


        // Load Upcoming Cover

        Glide.with(getApplicationContext()).asBitmap().load(backdropPath)
                .centerInside()
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.movieImage);


        // Load Upcoming Release Date
        binding.mReleaseDate.setText(releaseDate);

        // Load Upcoming Overview
        binding.mplot.setText(overview);

    }

    private void onLoadTrailer(String previewPath,String title,String backdrop){


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


                        final AlertDialog.Builder builder = new AlertDialog.Builder(UpcomingTitlesActivity.this, R.style.MyAlertDialogTheme);
                        builder.setTitle(getString(R.string.select_qualities));
                        builder.setCancelable(true);
                        builder.setItems(name, (dialogInterface, i) -> {

                            Intent intent = new Intent(UpcomingTitlesActivity.this, EasyPlexMainPlayer.class);
                            intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY, MediaModel.media("4", null, null, "trailer",
                                    title, vidURL.get(i).getUrl(), backdrop, null
                                    , null, null, null,
                                    null,
                                    null, null,
                                    null,
                                    null,null,0,
                                    null,null,null,
                                    0,0,null,null,0,null,null,0));
                            startActivity(intent);
                            finish();
                        });

                        builder.show();


                    } else
                        Toast.makeText(UpcomingTitlesActivity.this, "NULL", Toast.LENGTH_SHORT).show();

                } else {

                    Intent intent = new Intent(UpcomingTitlesActivity.this, EasyPlexMainPlayer.class);
                    intent.putExtra(EasyPlexPlayerActivity.EASYPLEX_MEDIA_KEY, MediaModel.media("4",
                            null, null, "trailer", title,

                            vidURL.get(0).getUrl(), backdrop, null
                            , null, null, null,
                            null,
                            null, null,
                            null,
                            null,null,0,
                            null,null,null,
                            0,0,null,null,0,null,null,0));
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

        

    }


}

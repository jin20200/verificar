package com.easyplexdemoapp.ui.player.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.util.Constants;
import com.easyplexdemoapp.util.Tools;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import org.jetbrains.annotations.NotNull;

public class YoutubePlayer extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer player;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_example);

        youTubePlayerView = findViewById(R.id.youtube_player_view);


        Tools.hideSystemPlayerUi(this,true,0);

        Tools.setSystemBarTransparent(this);

        Intent receivedIntent = getIntent();
        String mMovieId = receivedIntent.getStringExtra(Constants.MOVIE_LINK);


        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer initializedYouTubePlayer) {
                player = initializedYouTubePlayer;
                player.loadVideo(mMovieId, 0);
            }
        });


    }



    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }



}
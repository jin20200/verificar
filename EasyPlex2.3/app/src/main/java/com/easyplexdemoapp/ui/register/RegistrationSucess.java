package com.easyplexdemoapp.ui.register;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.databinding.RegistrationSucessBinding;
import com.easyplexdemoapp.ui.base.BaseActivity;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.util.Tools;

import javax.inject.Inject;

import dagger.android.AndroidInjection;


/**
 * EasyPlex - Android Movie Portal App
 * @package     EasyPlex - Android Movie Portal App
 * @author      @Y0bEX
 * @copyright   Copyright (c) 2023 Y0bEX,
 * @license     http://codecanyon.net/wiki/support/legal-terms/licensing-terms/
 * @profile     https://codecanyon.net/user/yobex
 * @link        yobexd@gmail.com
 * @skype       yobexd@gmail.com
 **/


public class RegistrationSucess extends AppCompatActivity {

    RegistrationSucessBinding binding;


    @Inject
    SettingsManager settingsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.registration_sucess);

        Tools.hideSystemPlayerUi(this,true,0);

        Tools.setSystemBarTransparent(this);

        // Load Mini Logo
        Tools.loadMiniLogo(this,binding.logoImageTop);

        binding.btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegistrationSucess.this, BaseActivity.class));
            finish();

        });


        onLoadSplashImage();


    }

    private void onLoadSplashImage() {

        Glide.with(getApplicationContext()).asBitmap().load(settingsManager.getSettings().getSplashImage())
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(withCrossFade())
                .skipMemoryCache(true)
                .into(binding.splashImage);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}

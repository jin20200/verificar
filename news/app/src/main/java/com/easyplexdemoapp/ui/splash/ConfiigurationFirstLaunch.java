package com.easyplexdemoapp.ui.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.databinding.ActivityFirstlaunchBinding;
import com.easyplexdemoapp.di.Injectable;
import com.easyplexdemoapp.ui.base.BaseActivity;
import com.easyplexdemoapp.ui.login.LoginActivity;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.util.SpacingItemDecoration;
import com.easyplexdemoapp.util.Tools;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class ConfiigurationFirstLaunch extends AppCompatActivity implements Injectable {

    ActivityFirstlaunchBinding binding;

    @Inject
    ConfigurationAdapter configurationAdapter;

    private List<String> customArray;


    @Inject
    SettingsManager settingsManager;


    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_firstlaunch);

        customArray = Arrays.asList(getResources().getStringArray(R.array.languages_array));


        binding.rvLangs.setHasFixedSize(true);
        binding.rvLangs.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.rvLangs.setAdapter(configurationAdapter);
        configurationAdapter.addMain(customArray,this,sharedPreferencesEditor);



        // Initialize views
        // Check if the layout direction is RTL
        // Check if the layout direction is RTL
        if (View.LAYOUT_DIRECTION_RTL == getResources().getConfiguration().getLayoutDirection()) {
            // Apply adjustments for RTL layout

            // Adjust button constraints
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(binding.constraintLayout);
            constraintSet.connect(binding.apply.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0); // Connect to the end of the parent
            constraintSet.setHorizontalBias(binding.apply.getId(), 0.5f); // Set horizontal bias to center
            constraintSet.applyTo(binding.constraintLayout);
            // Adjust text direction
            binding.apply.setTextDirection(View.TEXT_DIRECTION_RTL);
        }

        binding.apply.setOnClickListener(v -> {
            startActivity(new Intent(ConfiigurationFirstLaunch.this, settingsManager.getSettings().getDisablelogin() == 1 ? BaseActivity.class : LoginActivity.class));
            finish();
        });

    }
}
